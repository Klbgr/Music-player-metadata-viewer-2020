import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe qui genere une playlist XSPF a partir d'une liste de mp3
 * @author Ghalia NE
 */
public class XSPF {
    /**
     * Liste des mp3
     */
    private HashMap<String, String> mp3;

    /**
     * Nom du fichier XSPF
     */
    private String name;

    /**
     * Constructeur, initialise les variables
     * @param name Nom du fichier XSPF
     * @param mp3 Liste des fichiers mp3 (HashMap)
     * @throws IOException Fichier non trouve
     */
    public XSPF(String name, HashMap<String, String> mp3) throws IOException {
        this.name = name;
        this.mp3 = mp3;
    }

    /**
     * Fonction qui permet d'ecrire le fichier XSPF
     * @throws IOException Fichier non trouve
     */
    public void write() throws IOException {
        Iterator it = mp3.keySet().iterator();
        //contenu du fichier
        StringBuffer out = new StringBuffer();
        out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.append("<playlist version=\"1\" xmlns=\"http://xspf.org/ns/0/\">\n");
        out.append("\t<trackList>\n");
        while(it.hasNext()){
            out.append("\t\t<track>\n");
            out.append("\t\t\t<location>file:///" + mp3.get(it.next()) + "</location>\n");
            out.append("\t\t</track>\n");
        }
        out.append("\t</trackList>\n");
        out.append("</playlist>");
        //creation du fichier
        BufferedWriter bw = new BufferedWriter(new FileWriter(".\\" + name));
        bw.write(out.toString()); //ecriture du fichier
        bw.close(); //fermer le fichier
    }
}
