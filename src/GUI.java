import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Classe qui permet d'utiliser le programme avec une interface graphique (GUI)
 * @author Antoine QIU
 */
public class GUI extends JFrame {
    /**
     * Nom de la fenetre
     */
    private final static String title = "Metadonnées";

    /**
     * Chemin du dossier initial choisi
     */
    private String path;

    //elements graphiques
    /**
     * Titre du mp3
     */
    private JLabel songTitle;

    /**
     * Groupe d'elements
     */
    private JPanel songPanel;

    /**
     * Liste deroulante des fichiers mp3
     */
    private JComboBox songList;

    /**
     * Bouton pour choisir le dossier initial
     */
    private JButton browseButton;

    /**
     * Fenetre de choix du dossier
     */
    private JFileChooser browse;

    /**
     * Groupe d'elements
     */
    private JPanel metadataPanel;

    /**
     * Liste dynamique des metadonnees du fichier mp3
     */
    private DefaultListModel metadataContent;

    /**
     * Liste des noms de chaque metadonnees
     */
    private JList metadataTitleList;

    /**
     * Liste des metadonnees
     */
    private JList metadataContentList;

    /**
     * Panneau contenant l'image du fichier mp3
     */
    private JPanel imagePanel;

    /**
     * Groupe d'elements
     */
    private JPanel playerPanel;

    /**
     * Bouton play/pause pour le lecteur de musique
     */
    private JButton playPauseButton;

    /**
     * Avancement du lecteur
     */
    private JLabel currentTime;

    /**
     * Barre de progression du lecteur
     */
    private JProgressBar playerProgress;

    /**
     * Duree du mp3
     */
    private JLabel totalTime;

    /**
     * Groupe d'elements
     */
    private JPanel buttonPanel;

    /**
     * Bouton pour generer une playlist XSPF
     */
    private JButton playlistButton;

    /**
     * Bouton pour basculer entre le mode clair et sombre
     */
    private JButton themeButton;

    //variables lecteur mp3
    /**
     * Etat du lecteur, -1 pas demarre, 0 pause, 1 play
     */
    private int state = -1;

    /**
     * Ouvrir fichier mp3 pour extraire les metadonnees
     */
    private Metadata mp3file;

    /**
     * Ouvrir fichier mp3 pour le lire
     */
    private MP3Player player;

    /**
     * Liste des fichiers dans le dossier initial et ses sous-dossiers
     */
    private HashMap<String, String> mp3;

    //variables GUI
    /**
     * Grande police
     */
    private final float bigFont = 40f;

    /**
     * Petite police
     */
    private final float smallFont = 20f;

    /**
     * Etat du mode d'affichage
     */
    private boolean darkMode = false;

    /**
     * Couleur noir
     */
    private final Color noir = Color.black;

    /**
     * Couleur blanc
     */
    private final Color blanc = Color.white;

    /**
     * Couleur gris clair
     */
    private final Color grisClair = new Color(195, 195, 195);

    /**
     * Couleur gris fonce
     */
    private final Color grisFonce = new Color(50, 50, 50);

    /**
     * Couleur bleu
     */
    private final Color bleu = new Color(26, 115, 232);

    /**
     * Couleur bleu clair
     */
    private final Color bleuClair = new Color(138, 180, 248);

    //images
    /**
     * Image du bouton play
     */
    private ImageIcon play = new ImageIcon(GUI.class.getResource("playBlack.png"));

    /**
     * Image du bouton pause
     */
    private ImageIcon pause = new ImageIcon(GUI.class.getResource("pauseBlack.png"));

    /**
     * Image de soleil pour le mode clair
     */
    private final ImageIcon sun = new ImageIcon(GUI.class.getResource("sun.png"));

    /**
     * Image de la lune pour le mode sombre
     */
    private final ImageIcon moon = new ImageIcon(GUI.class.getResource("moon.png"));

    /**
     * Contstructeur du GUI
     */
    public GUI(){
        super(title); //titre
        //initialisation des composants
        songTitle = new JLabel("Bienvenue");
        songTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        songList = new JComboBox();
        songList.setFocusable(false);
        browseButton = new JButton("Dossier...");
        browseButton.setFocusPainted(false);
        songPanel = new JPanel(new FlowLayout());
        songPanel.add(songList);
        songPanel.add(browseButton);
        String[] metadataTitle = {"Morceau :", "Artiste :", "Titre :", "Album :", "Année :", "Genre :"};
        metadataTitleList = new JList(metadataTitle);
        metadataTitleList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        metadataTitleList.setLayoutOrientation(JList.VERTICAL);
        metadataTitleList.setVisibleRowCount(5);
        metadataContent = new DefaultListModel();
        metadataContentList = new JList(metadataContent);
        metadataContentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        metadataContentList.setLayoutOrientation(JList.VERTICAL);
        metadataContentList.setVisibleRowCount(5);
        imagePanel = new JPanel(new BorderLayout());
        metadataPanel = new JPanel(new FlowLayout());
        metadataPanel.add(metadataTitleList);
        metadataPanel.add(metadataContentList);
        metadataPanel.add(imagePanel);
        playPauseButton = new JButton();
        playPauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playPauseButton.setContentAreaFilled(false);
        playPauseButton.setFocusPainted(false);
        playPauseButton.setBorderPainted(false);
        setIcon(playPauseButton, play, 60);
        currentTime = new JLabel("0:00");
        playerProgress = new JProgressBar();
        totalTime = new JLabel("0:00");
        playerPanel = new JPanel(new FlowLayout());
        playerPanel.add(currentTime);
        playerPanel.add(playerProgress);
        playerPanel.add(totalTime);
        themeButton = new JButton();
        themeButton.setContentAreaFilled(false);
        themeButton.setFocusPainted(false);
        themeButton.setBorderPainted(false);
        setIcon(themeButton, sun, 30);
        playlistButton = new JButton("Générer une Playlist");
        playlistButton.setFocusable(false);
        buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(themeButton, BorderLayout.LINE_START);
        buttonPanel.add(playlistButton, BorderLayout.LINE_END);

        //definit les actions de chaques boutons
        songList.addActionListener(new actionSongList());
        browseButton.addActionListener(new actionBrowse());
        playPauseButton.addActionListener(new actionPlayPause());
        themeButton.addActionListener(new actionTheme());
        playlistButton.addActionListener(new actionPlaylist());

        //definit la taille de la police de chaques textes
        songTitle.setFont(songTitle.getFont().deriveFont (bigFont));
        songList.setFont(songList.getFont().deriveFont(smallFont));
        browseButton.setFont(browseButton.getFont().deriveFont(smallFont));
        metadataTitleList.setFont(metadataTitleList.getFont().deriveFont(smallFont));
        metadataContentList.setFont(metadataContentList.getFont().deriveFont(smallFont));
        currentTime.setFont(currentTime.getFont().deriveFont(smallFont));
        totalTime.setFont(totalTime.getFont().deriveFont(smallFont));
        playlistButton.setFont(playPauseButton.getFont().deriveFont(smallFont));

        //definit la taille de chaques composants
        songTitle.setPreferredSize(new Dimension(1000, 50));
        songList.setPreferredSize(new Dimension(800 ,30));
        browseButton.setPreferredSize(new Dimension(150, 30));
        songPanel.setPreferredSize(new Dimension(1000, 50));
        imagePanel.setPreferredSize(new Dimension( 250, 250));
        metadataTitleList.setPreferredSize(new Dimension(100, 275));
        metadataTitleList.setFixedCellHeight(46);
        metadataContentList.setPreferredSize(new Dimension(200, 275));
        metadataContentList.setFixedCellHeight(46);
        metadataPanel.setPreferredSize(new Dimension(1000, 300));
        playPauseButton.setPreferredSize(new Dimension(50, 50));
        playerProgress.setPreferredSize(new Dimension(550, 30));
        playerPanel.setPreferredSize(new Dimension(1000, 50));
        themeButton.setPreferredSize(new Dimension(30, 30));
        playlistButton.setPreferredSize(new Dimension(250, 30));

        //ajout de tous les composants a la fenetre principale
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.getContentPane().add(songTitle);
        this.getContentPane().add(songPanel);
        this.getContentPane().add(metadataPanel);
        this.getContentPane().add(playPauseButton);
        this.getContentPane().add(playerPanel);
        this.getContentPane().add(buttonPanel);
        this.setResizable(false); //desactiver redimensionner pour eviter des deformations du GUI
        this.pack(); //taille de la fenetre en fonction de la taille des composants
        //confirmation avant de quitter
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(GUI.this, "Voulez vous vraiment quitter ?", "Quitter ?", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                    GUI.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                else if (result == JOptionPane.NO_OPTION)
                    GUI.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);            }
        });
        this.setVisible(true);
        this.setIconImage(play.getImage()); //lecteur non demarre

        //theme clair par defaut
        this.getContentPane().setBackground(blanc);
        songTitle.setForeground(noir);
        songList.setForeground(noir);
        songList.setBackground(blanc);
        songList.setBorder(new LineBorder(noir));
        browseButton.setForeground(noir);
        browseButton.setBackground(blanc);
        browseButton.setBorder(new LineBorder(noir));
        songPanel.setBackground(blanc);
        metadataTitleList.setForeground(noir);
        metadataTitleList.setBackground(blanc);
        metadataTitleList.setBorder(new LineBorder(noir));
        metadataContentList.setForeground(noir);
        metadataContentList.setBackground(blanc);
        metadataContentList.setBorder(new LineBorder(noir));
        metadataPanel.setBackground(blanc);
        imagePanel.setBackground(blanc);
        imagePanel.setBorder(new LineBorder(noir));
        currentTime.setForeground(noir);
        playerProgress.setForeground(bleu);
        playerProgress.setBackground(blanc);
        playerProgress.setBorder(new LineBorder(bleu));
        totalTime.setForeground(noir);
        playerPanel.setBackground(blanc);
        playlistButton.setForeground(noir);
        playlistButton.setBackground(blanc);
        playlistButton.setBorder(new LineBorder(noir));
        buttonPanel.setBackground(blanc);
    }

    /**
     * Fonction a l'appui du bouton browse
     */
    class actionBrowse implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            browse = new JFileChooser(); //fenetre de choix du dossier
            browse.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //dossier et fichier
            browse.setFileFilter(new FileNameExtensionFilter("Dossier ou fichier XSPF", "xspf")); //uniquement fichiers xspf
            if(browse.showOpenDialog(GUI.this) == JFileChooser.APPROVE_OPTION) { //si un dossier est choisi
                songList.removeAllItems(); //vide la liste des de fichiers mp3
                metadataContent.clear(); //vide la liste des metadonnees
                imagePanel.removeAll(); //vide l'image
                mp3 = null; //vide la liste
                if(browse.getSelectedFile().isDirectory()) { //si dossier
                    Explorer explorer = new Explorer(browse.getSelectedFile().getPath()); //liste des fichiers mp3 dans ce dossier et ses sous-dossiers
                    mp3 = explorer.getMp3(); //recupere la liste
                }
                else if(browse.getSelectedFile().isFile() && browse.getSelectedFile().getName().toLowerCase().endsWith(".xspf")){ //si fichier xspf
                    try {
                        Scanner scanner = new Scanner(new File(browse.getSelectedFile().getPath())); //scanneur
                        mp3 = new HashMap<>();
                        while (scanner.hasNextLine()) { //parcours ligne par ligne
                            String line = scanner.nextLine();
                            if(line.contains("location")) { //si contient chemin du mp3
                                line = line.substring(line.indexOf("C"), line.indexOf("</location>")); //garder que le chemin
                                mp3.put(line.substring(line.lastIndexOf("\\")+1), line); //ajout a la liste
                            }
                        }
                    } catch(FileNotFoundException xspfEx) {
                        JOptionPane.showMessageDialog(GUI.this, "Fichier invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
                Iterator<String> it = mp3.keySet().iterator(); //on utilise Iterator pour parcourir la liste
                while (it.hasNext()) //tant qu'il y a des elements dans la liste
                    songList.addItem(mp3.get(it.next())); //on ajoute a la liste deroulante
            }
        }
    }

    /**
     * Fonction a l'appui du bouton playPause
     */
    class actionPlayPause implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(state == -1){ //si pas demarre
                state = 1; //change l'etat en demarre
                setIcon(playPauseButton, pause, 60); //affiche l'image en pause
                player = new MP3Player(); //creation du lecteur
                player.start(); //demarrage du lecteur
            }
            else if(state == 0){ //sinon si pause
                state = 1; //change l'etat en demarre
                setIcon(playPauseButton, pause, 60); //affiche l'image en pause
            }
            else if(state == 1){ //sinon si play
                state = 0; //change l'etat en pause
                setIcon(playPauseButton, play, 60); //affiche l'image en play
            }
        }
    }

    /**
     * Fonction a la selection d'un element dans la liste deroulante
     */
    class actionSongList implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                path = songList.getSelectedItem().toString(); //recupere le chemin
                songTitle.setText(path.substring(path.lastIndexOf("\\") + 1)); //affiche le titre
                mp3file = new Metadata(path); //extrait les metadonnees
                totalTime.setText(mp3file.getLength()); //affiche la duree du mp3
            }
            catch(NullPointerException | UnsupportedTagException | IOException | InvalidDataException exceptions) { //si erreur
                path = ""; //remise a zero du chemin
                songTitle.setText("Bienvenue"); //remise a zero du titre
                totalTime.setText("0:00"); //remise a zero de la duree
            }

            metadataContent.clear(); //vide la liste des metadonnees
            //rempli la liste avec les nouvelles donnees
            metadataContent.addElement(mp3file.getTrack());
            metadataContent.addElement(mp3file.getArtist());
            metadataContent.addElement(mp3file.getTitle());
            metadataContent.addElement(mp3file.getAlbum());
            metadataContent.addElement(mp3file.getYear());
            metadataContent.addElement(mp3file.getGenre());
            imagePanel.removeAll(); //vide l'image
            try {
                imagePanel.add(new JLabel(new ImageIcon(mp3file.getImage().getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT))), BorderLayout.CENTER); //affiche l'image du mp3
            } catch (NullPointerException imageEx) { //pas d'image
                imageEx.printStackTrace();
            }
            setIcon(playPauseButton, play, 60); //remise a zero du bouton playPause
            state = -1; //remise a zero du lecteur
        }
    }

    /**
     * Fonction a l'appui du bouton theme
     */
    class actionTheme implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(darkMode){ //si en mode sombre
                //passage au mode clair
                GUI.this.getContentPane().setBackground(blanc);
                songTitle.setForeground(noir);
                songList.setForeground(noir);
                songList.setBackground(blanc);
                songList.setBorder(new LineBorder(noir));
                browseButton.setForeground(noir);
                browseButton.setBackground(blanc);
                browseButton.setBorder(new LineBorder(noir));
                songPanel.setBackground(blanc);
                metadataTitleList.setForeground(noir);
                metadataTitleList.setBackground(blanc);
                metadataTitleList.setBorder(new LineBorder(noir));
                metadataContentList.setForeground(noir);
                metadataContentList.setBackground(blanc);
                metadataContentList.setBorder(new LineBorder(noir));
                metadataPanel.setBackground(blanc);
                imagePanel.setBackground(blanc);
                imagePanel.setBorder(new LineBorder(noir));
                currentTime.setForeground(noir);
                playerProgress.setForeground(bleu);
                playerProgress.setBackground(blanc);
                playerProgress.setBorder(new LineBorder(bleu));
                totalTime.setForeground(noir);
                playerPanel.setBackground(blanc);
                playlistButton.setForeground(noir);
                playlistButton.setBackground(blanc);
                playlistButton.setBorder(new LineBorder(noir));
                buttonPanel.setBackground(blanc);
                play = new ImageIcon(GUI.class.getResource("playBlack.png"));
                pause = new ImageIcon(GUI.class.getResource("pauseBlack.png"));
                if(state == 1)
                    setIcon(playPauseButton, pause, 60);
                else
                    setIcon(playPauseButton, play, 60);
                setIcon(themeButton, sun, 30);
                darkMode = false;
            }
            else{ //sinon en mode clair
                //passage au mode sombre
                GUI.this.getContentPane().setBackground(noir);
                songTitle.setForeground(blanc);
                songList.setForeground(grisClair);
                songList.setBackground(grisFonce);
                songList.setBorder(new LineBorder(grisClair));
                browseButton.setForeground(grisClair);
                browseButton.setBackground(grisFonce);
                browseButton.setBorder(new LineBorder(grisClair));
                songPanel.setBackground(noir);
                metadataTitleList.setForeground(blanc);
                metadataTitleList.setBackground(grisFonce);
                metadataTitleList.setBorder(new LineBorder(grisClair));
                metadataContentList.setForeground(blanc);
                metadataContentList.setBackground(grisFonce);
                metadataContentList.setBorder(new LineBorder(grisClair));
                metadataPanel.setBackground(noir);
                imagePanel.setBackground(grisFonce);
                imagePanel.setBorder(new LineBorder(grisClair));
                currentTime.setForeground(blanc);
                playerProgress.setForeground(bleuClair);
                playerProgress.setBackground(grisFonce);
                playerProgress.setBorder(new LineBorder(bleuClair));
                totalTime.setForeground(blanc);
                playerPanel.setBackground(noir);
                playlistButton.setForeground(grisClair);
                playlistButton.setBackground(grisFonce);
                playlistButton.setBorder(new LineBorder(grisClair));
                buttonPanel.setBackground(noir);
                play = new ImageIcon(GUI.class.getResource("playWhite.png"));
                pause = new ImageIcon(GUI.class.getResource("pauseWhite.png"));
                if(state == 1)
                    setIcon(playPauseButton, pause, 60);
                else
                    setIcon(playPauseButton, play, 60);
                setIcon(themeButton, moon, 30);
                darkMode = true;
            }
        }
    }

    /**
     * Fonction permettant dans changer l'image d'un bouton
     * @param b Bouton cible
     * @param Icon Image a applique au bouton
     * @param size Taille de l'image
     */
    public void setIcon(JButton b, ImageIcon Icon, int size){
        b.setIcon(new ImageIcon(Icon.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT)));
    }

    /**
     * Fonction a l'appui du bouton playlist
     */
    class actionPlaylist implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] choice = {"Par défaut", "Personnalisée"};
            String choosed = (String) JOptionPane.showInputDialog(GUI.this,"Choix :", "Choisissez le type de playlist à générer", JOptionPane.QUESTION_MESSAGE, null, choice, choice[0]);
            try {
                if (choosed.equals(choice[0])) { //par defaut
                    new XSPF("playslist.xspf", mp3).write(); //creation d'un fichier XSPF
                    JOptionPane.showMessageDialog(GUI.this, "Le fichier playlist.xspf a été créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else if (choosed.equals(choice[1])) { //perso
                    JList list = new JList(mp3.keySet().toArray(new String[0])); //recup liste des mp3
                    JOptionPane.showMessageDialog(GUI.this, list, "Sélectionnez les musiques à inclure", JOptionPane.PLAIN_MESSAGE);
                    HashMap<String, String> custom = new HashMap(); //nouvelle liste perso
                    for(int i = 0 ; i<list.getSelectedValuesList().size() ; i++) {
                        String name = list.getSelectedValuesList().toArray()[i].toString(); //recupere selection
                        custom.put(name , mp3.get(name)); //ajouter a la liste
                    }
                    new XSPF("playslist.xspf", custom).write(); //creation d'un fichier XSPF
                    JOptionPane.showMessageDialog(GUI.this, "Le fichier playlist.xspf a été créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException | NullPointerException xspfEx) { //aucune entree
                JOptionPane.showMessageDialog(GUI.this, "La création du fichier playlist.xspf à échoué.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Fonction main
     * @param args Void
     */
    public static void main(String[] args) {
        new GUI(); //creation du GUI
    }


    /**
     * Classe permet de lire les mp3, utilisant Thread pour ne pas figer tout le programme pendant la lecture
     */
    public class MP3Player extends Thread{
        private Player player; //player mp3agic
        public void run() { //demarrage du lecteur
            int totalSeconds = 0; //initialisation
            try {
                player = new Player(new FileInputStream(path)); //ouverture du fichier mp3
                totalSeconds = (int) mp3file.getLengthInSec(); //recupere la duree
            } catch (JavaLayerException | NullPointerException | IOException playerEx) { //si erreur, remise a zero
                playerEx.printStackTrace();
                state = -1; //change l'etat en non demarre
                setIcon(playPauseButton, play, 60); //affiche l'image en play
                JOptionPane.showMessageDialog(GUI.this, "Aucun fichier mp3 n'est selectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            while(true) { //boucle lecture
                try {
                    player.play(1); //joue la musique
                } catch (JavaLayerException playerEx) {
                    playerEx.printStackTrace();
                }
                int sec = player.getPosition()/1000; //avancement du lecteur en secondes
                int m = sec/60; //minutes
                int s = sec - m*60; //secondes
                //adapte l'affichage
                if(s<10)
                    currentTime.setText(m+":0"+s);
                else
                    currentTime.setText(m+":"+s);
                playerProgress.setValue(sec*100/totalSeconds); //mis a jour de la barre de progression

                while(state == 0) { //si en pause
                    try {
                        sleep(1); //attendre
                    } catch (InterruptedException sleepEx) {
                        sleepEx.printStackTrace();
                    }
                }

                if(player.isComplete()) //si musique finie
                    state = -1; //change l'etat en non demarre

                if(state == -1) { //si non demarre
                    player.close(); //ferme le lecteur
                    currentTime.setText("0:00"); //remise a zero de l'avancement
                    playerProgress.setValue(0); //remise a zero de la barre de progression
                    setIcon(playPauseButton, play, 60); //remise a zero en play
                    break; //sort de la boucle while
                }
            }
        }
    }
}
