import com.mpatric.mp3agic.*;
import javax.swing.*;
import java.io.IOException;

/**
 * Classe qui permet de recuperer les metadonnees d'un fichier mp3 (ID3)
 * @author Ghalia NE
 */
public class Metadata {
    /**
     * Chemin vers le dossier initial
     */
    private String path;

    /**
     * Permet d'ouvrir les mp3
     */
    private Mp3File mp3file;

    /**
     * Constructeur, initialise les variables
     * @param path Chemin du dossier choisi
     * @throws InvalidDataException Donnee non trouve
     * @throws IOException Fichier non trouve
     * @throws UnsupportedTagException Metadonnees non supportes
     */
    public Metadata(String path) throws IOException, UnsupportedTagException, InvalidDataException {
        this.path = path;
        mp3file = new Mp3File(path);
    }

    /**
     * Fonction qui retourne la duree d'un fichier mp3 dans une String
     * @return Duree du mp3
     */
    public String getLength() {
        String out = ""; //initialise
        long sec = mp3file.getLengthInSeconds(); //recupere la duree en seconde
        //conversion en minutes et secondes
        long m = sec / 60;
        long s = sec - m * 60;
        //adapte l'affichage
        if (s < 10)
            out = out + m + ":0" + s;
        else
            out = out + m + ":" + s;
        return out;
    }

    /**
     * Fonction qui retourne la duree d'un fichier mp3 dans une long
     * @return Duree du mp3 en secondes
     */
    public long getLengthInSec() {
        return mp3file.getLengthInSeconds();
    }

    /**
     * Fonction qui retourne le numero de morceau d'un fichier mp3
     * @return Numero de morceau du mp3
     */
    public String getTrack() {
        if (mp3file.hasId3v2Tag()) {
            return mp3file.getId3v2Tag().getTrack();
        } else if (mp3file.hasId3v1Tag()) {
            return mp3file.getId3v1Tag().getTrack();
        } else
            return "";
    }

    /**
     * Fonction qui retourne l'artiste d'un fichier mp3
     * @return Artiste du mp3
     */
    public String getArtist() {
        if (mp3file.hasId3v2Tag()) {
            return mp3file.getId3v2Tag().getArtist();
        } else if (mp3file.hasId3v1Tag()) {
            return mp3file.getId3v1Tag().getArtist();
        } else
            return "";
    }

    /**
     * Fonction qui retourne le titre d'un fichier mp3
     * @return Titre du mp3
     */
    public String getTitle() {
        if (mp3file.hasId3v2Tag()) {
            return mp3file.getId3v2Tag().getTitle();
        } else if (mp3file.hasId3v1Tag()) {
            return mp3file.getId3v1Tag().getTitle();
        } else
            return "";
    }

    /**
     * Fonction qui retourne l'album d'un fichier mp3
     * @return Album du mp3
     */
    public String getAlbum() {
        if (mp3file.hasId3v2Tag()) {
            return mp3file.getId3v2Tag().getAlbum();
        } else if (mp3file.hasId3v1Tag()) {
            return mp3file.getId3v1Tag().getAlbum();
        } else
            return "";
    }

    /**
     * Fonction qui retourne l'annee d'un fichier mp3
     * @return Annee du mp3
     */
    public String getYear() {
        if (mp3file.hasId3v2Tag()) {
            return mp3file.getId3v2Tag().getYear();
        } else if (mp3file.hasId3v1Tag()) {
            return mp3file.getId3v1Tag().getYear();
        } else
            return "";
    }

    /**
     * Fonction qui retourne le genre d'un fichier mp3
     * @return Genre du mp3
     */
    public String getGenre() {
        if (mp3file.hasId3v2Tag()) {
            return mp3file.getId3v2Tag().getGenreDescription();
        } else if (mp3file.hasId3v1Tag()) {
            return mp3file.getId3v1Tag().getGenreDescription();
        } else
            return "";
    }

    /**
     * Fonction qui retourne l'image d'un fichier mp3 dans une ImageIcon
     * @return Image du mp3
     */
    public ImageIcon getImage() {
        if (mp3file.hasId3v2Tag()) { //seul le ID3v2 peut contenir une image
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            byte[] imageData = id3v2Tag.getAlbumImage();
            return new ImageIcon(imageData);
        } else
            return null;
    }

    /**
     * Fonction qui affiche les metadonnees d'un fichier mp3
     * @throws InvalidDataException Donnee non trouve
     * @throws IOException Fichier non trouve
     * @throws UnsupportedTagException Metadonnees non supportes
     */
    public void print() throws InvalidDataException, IOException, UnsupportedTagException {
        Mp3File mp3file = new Mp3File(path);
        if (mp3file.hasId3v2Tag()) { //si le mp3 contient des tags ID3v2
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            System.out.println("Morceau: " + id3v2Tag.getTrack());
            System.out.println("Artiste: " + id3v2Tag.getArtist());
            System.out.println("Titre: " + id3v2Tag.getTitle());
            System.out.println("Album: " + id3v2Tag.getAlbum());
            System.out.println("Année: " + id3v2Tag.getYear());
            System.out.println("Genre: " + id3v2Tag.getGenreDescription());
            System.out.println("Commentaire: " + id3v2Tag.getComment());
            System.out.println("Compositeur: " + id3v2Tag.getComposer());
            System.out.println("Editeur: " + id3v2Tag.getPublisher());
            System.out.println("Artiste original: " + id3v2Tag.getOriginalArtist());
            System.out.println("Artiste de l'album: " + id3v2Tag.getAlbumArtist());
            System.out.println("Copyright: " + id3v2Tag.getCopyright());
            System.out.println("URL: " + id3v2Tag.getUrl());
            System.out.println("Encodeur: " + id3v2Tag.getEncoder());
        }
        else if (mp3file.hasId3v1Tag()) { //sinon si le mp3 contient des tags ID3v1
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            System.out.println("Morceau: " + id3v1Tag.getTrack());
            System.out.println("Artiste: " + id3v1Tag.getArtist());
            System.out.println("Titre: " + id3v1Tag.getTitle());
            System.out.println("Album: " + id3v1Tag.getAlbum());
            System.out.println("Année: " + id3v1Tag.getYear());
            System.out.println("Genre: " + id3v1Tag.getGenreDescription());
            System.out.println("Commentaire: " + id3v1Tag.getComment());
        }
        else{ //sinon si aucun tags ID3
            System.out.println("Aucune donnee trouvee");
        }

    }


}
