/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travail_pratique_3;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Jeu de boules. Le but est de faire des lignes de boule d'une meme couleur.
 * 07-05-20
 * @version 1.0.1
 * @author de la version 1.0.0 carm
 * @author de la version 1.0.1 Alexandre Lambert
 */
public class Travail_Pratique_3 extends Application {
    
    private static final int W = 6;         // Largeur « Width » du tableau de
                                            //boules.
    
    private static final int H = 6;         // Hauteur « Heigh » du tableau de
                                            //boules.
    
    private static final int SIZE = 100;    //Diametre des cercles « boules ».

    
    // Tableau « Array » contenant les couleurs ci-dessous :
    // [0]: Noir, [1]: Rouge, [2]: Bleu, [3]: Vert, [4]: Jaune
    private Color[] colors = new Color[] {
            Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW
    };

    // Cercle « Boule » qui sera selectionne lors des interactions utilisateurs.
    // Variable est de type classe boule. Null par defaut.
    private Boule selected = null;
    
    //Tableau de toutes les boules dans le jeu. 6 boules par 6 pour total de 36.
    private List<Boule> Tab_Boules;

    //Score du joueur. Augmente a chaque fois qu'une ligne de cercle d'une meme
    //couleur est creee par l'utilisateur.
    private IntegerProperty score = new SimpleIntegerProperty();

    /**
     * Fonction de creation de la scene et disposition de la fenetre.
     * @return La scene
     */
    private Parent createContent() {
        
        //Declaration de la scene
        Pane root = new Pane();
        
        //Lien web qui sera mis dans la fenetre "?"
        Hyperlink link = new Hyperlink("https://github.com/moino157/Travail_Pratique_3");
            link.setOnAction(new EventHandler<ActionEvent>() {
                
                //Voici la source qui m'a montre cette methode : https://www.youtube.com/watch?v=bLek4Ycb7p8
                
                @Override
                public void handle(ActionEvent event) {
                    
                    //URL du site recherche
                    String URL = "https://github.com/moino157/Travail_Pratique_3";
                    
                    //Tentative d'ouverture de l'URL dans un web browser
                    try{
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(URL));
                    }
                    catch(Exception exception)
                    {
                        System.out.println("Une erreur s'est produite.");
                    }
                }
            }); 
        
        root.setPrefSize((W+2) * SIZE, (H+2) * SIZE); //Defini la taille de la
                                                    //fenetre.

        //Initialisation du tableau des cercles « boules »
        Tab_Boules = IntStream.range(0, W * H)
                .mapToObj(i -> new Point2D((i % W) + 1, (i / W) + 1))
                .map(Boule::new)
                .collect(Collectors.toList());

        //Ajout du tableau de boules a la scene
        root.getChildren().addAll(Tab_Boules);
        
                
        //Menu Bar
        Menu menu = new Menu("Menu");
        
        //Option Affichant les informations du programme
        MenuItem aide = new MenuItem("?");
        aide.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                //Creer une alerte. C'est une fenetre qui s'ouvre pour afficher
                //de l'information
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("À propos");
                alert.setHeaderText("Information du Programme");
                alert.setContentText("Nom de la Version : Travail_Pratique_3 avec Menu et Commentaires\nVersion : 1.0.1\n"
                        + "Auteur de la Version: Alexandre Lambert\nAuteur de la version Original: carm\nDate : 07/05/2020\n");

                alert.setGraphic(link);
                alert.showAndWait();    //Fait afficher la fenetre et arrete le programme principal
            }
        });
        
        //Declaration et Definition de la barre de menu
        MenuBar menuBar = new MenuBar(menu);
        menuBar.prefWidthProperty().bind(root.widthProperty());
        VBox vBox = new VBox(menuBar);

        //SubMenu
        //Boutton pour quitter
        MenuItem quit = new MenuItem("Quitter");
        quit.setOnAction(new EventHandler<ActionEvent>() {  //Definition de ce que boutton fait

            @Override
            public void handle(ActionEvent event) {

                //Quitte et ferme le programme
                System.exit(1);

            }
        });
        
        //Boutton de redemarrage
        MenuItem reset = new MenuItem("Redémarrer");
        reset.setOnAction(new EventHandler<ActionEvent>() {  //Definition de ce que boutton fait

            @Override
            public void handle(ActionEvent event) {

                //Remet la selection a null
                selected = null;
                
                //Remet le score a zero
                score.set(0);
                
                //On enleve la liste afin de travailler dessus
                root.getChildren().removeAll(Tab_Boules);
                
                //Initialisation du tableau des cercles « boules » de 0 a la hauteur fois la largeur
                Tab_Boules = IntStream.range(0, W * H)
                    .mapToObj(i -> new Point2D((i % W) + 1, (i / W) + 1))
                    .map(Boule::new)
                    .collect(Collectors.toList());
                
                //Rajout du tableau de boules a la scene
                root.getChildren().addAll(Tab_Boules);

            }
        });
        
        
        menu.getItems().addAll(reset, quit, aide);
        

        //Declaration et initialisation du texte affichant le score du joueur.
        Text textScore = new Text();
        //textScore.setTranslateX(W * SIZE);  //Definition de sa position en X
        textScore.setTranslateY(80);   //Definition de sa position en Y
        textScore.setFont(Font.font(68));   //Definition de sa police
        textScore.textProperty().bind(score.asString("Score: [%d]"));   //Definition de son contenu (ce qu'il ecrit)

        //Ajout du texte de score a la scene.
        root.getChildren().addAll(textScore, vBox);
        return root;
    }

    /**
     * Methode prenant les lignes et les colonnes du tableau de « boules » pour
     * ensuite appeler une methode de verification de ces lignes et colonnes
     */
    private void checkState() {
        Map<Integer, List<Boule>> rows = Tab_Boules.stream().collect(Collectors.groupingBy(Boule::getRow));
        Map<Integer, List<Boule>> columns = Tab_Boules.stream().collect(Collectors.groupingBy(Boule::getColumn));

        rows.values().forEach(this::checkCombo);    //Pour chaque ligne, on regarde si tous les cercles sont d'une meme couleur
        columns.values().forEach(this::checkCombo); //Pour chaque colonne, on regarde si tous les cercles sont d'une meme couleur
    }

    /**
     * Methode verifiant s'il y a une ligne/colonne de boule de la meme couleur.
     * S'il en a, on hausse le score du joueur et on change les boules de cette
     * meme ligne/colonne avec des boules de couleur aleatoires.
     * @param Tab_BoulesLine 
     */
    private void checkCombo(List<Boule> Tab_BoulesLine) {
        
        Boule jewel = Tab_BoulesLine.get(0);  //Prend la premiere boule de la ligne
        
        //Compte le nombre de boule de couleur differente a la premiere boule.
        long count = Tab_BoulesLine.stream().filter(j -> j.getColor() != jewel.getColor()).count();
        
        //Si toutes les boules sont identiques
        if (count == 0) {
            score.set(score.get() + 1000);  //On augmente le score
            Tab_BoulesLine.forEach(Boule::randomize);   //On change les couleurs
                                                        //des boules de la ligne de facon aleatoire.
                                                        
            //Delcaration du son
            javafx.scene.media.AudioClip son = new
            javafx.scene.media.AudioClip(new File("bruh.mp3").toURI().toString());
            
            //Joue le son (volume reduit de 50%)
            son.play(0.50);
        }
    }

    /**
     * Methode servant a interchanger les couleurs des boules 'a' et 'b'
     * @param a Premiere boule selectionnee
     * @param b Deuxieme boule selectionnee
     */
    private void swap(Boule a, Boule b) {
        Paint color = a.getColor(); //Variable de couleur temporaire servant a permuter
        a.setColor(b.getColor());   //Change la couleur de la boule 'a' avec la couleur de la boule 'b'
        b.setColor(color);          //Change la couleur de la boule 'b' avec la couleur stockee dans la variable temporaire
    }

    @Override
    /**
     * methode d'affichage de la scene « root »
     */
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();    //Affichage de la scene avec le « root » cree par la fonction « createContent() »
    }

    /**
     * Classe des cercles « boules » qui seront la composante principale du gameplay.
     * En d'autres mots, le gameplay est centre sur cette classe.
     */
    private class Boule extends Parent {
        
        //Variable de type circle provenant de javafx.scene.shape.Circle;
        //Le parametre donne est le diametre divise par deux (donc le rayon)
        private Circle circle = new Circle(SIZE / 2);

        /**
         * Constructeur de la classe.
         * @param point Un point géométrique 2D qui représente les coordonnées x, y.
         */
        public Boule(Point2D point) {
            
            //Defini le centre du cercle a l'aide de son rayon
            circle.setCenterX(SIZE / 2);
            circle.setCenterY(SIZE / 2);
            
            //Definition de la couleur du cercle de facon aleatoire
            circle.setFill(colors[new Random().nextInt(colors.length)]);

            //Definition de la position du cercle dans la scene
            setTranslateX(point.getX() * SIZE);
            setTranslateY(point.getY() * SIZE);
            getChildren().add(circle);  //Ajoute du cercle dans la scene

            /**
             * Definition de ce qui se passe lorsqu'on clique sur un cercle.
             */
            setOnMouseClicked(event -> {
                
                //S'il n'y a pas de selection, le cercle que l'utilisateur a clique devient la selection
                if (selected == null) {
                    selected = this;
                }
                else {  //Sinon: 
                    swap(selected, this);   //Interchange le cercle « selected » et le cercle que l'utilisateur a clique
                    checkState();       //On verifie si ca cree une ligne/colonne de cercles « boules » d'un meme couleur.
                    selected = null;        //On remet la selection a null
                }
            });
        }

        /**
         * Methode definissant la couleur du cercle de facon aleatoire
         */
        public void randomize() {
            circle.setFill(colors[new Random().nextInt(colors.length)]);
        }

        /**
         * Fonction retournant la colonne de la boule « this »
         * @return integer representant la position en X de la colonne
         */
        public int getColumn() {
            return (int)getTranslateX() / SIZE;
        }

        /**
         * Fonction retournant la ligne de la boule « this »
         * @return integer representant la position en Y de la ligne
         */
        public int getRow() {
            return (int)getTranslateY() / SIZE;
        }

        /**
         * Method changeant la couleur du cercle « boule »
         * @param color la couleur que l'on veut que le cercle soit
         */
        public void setColor(Paint color) {
            circle.setFill(color);
        }

        /**
         * Fonction retournant la couleur du cercle « boule »
         * @return la couleur du cercle
         */
        public Paint getColor() {
            return circle.getFill();
        }
    }

    //Le main
    public static void main(String[] args) {
        launch(args);
    }
}
