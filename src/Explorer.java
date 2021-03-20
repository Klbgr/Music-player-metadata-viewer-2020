import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.tika.Tika;

/**
 * Classe qui liste tous les fichiers mp3 d'un dossier et de ses sous-dossiers
 * @author Antoine QIU
 */
public class Explorer  {
    /**
     * Chemin vers le dossier initial
     */
    private String path;

    /**
     * Permet d'ouvrir les dossiers/fichiers
     */
    private File folder;

    /**
     * Stocker le nom et chemin des fichiers mp3 trouves
     */
    private final HashMap<String, String> mp3;

    /**
     * Constructeur, initialise les variables
     * @param path Chemin du dossier choisi
     */
    public Explorer(String path) {
        this.path = path;
        folder = new File(path);
        mp3 = new HashMap<>();
        traiter();
    }

    /**
     * Fonction qui parcours le dossier et les sous-dossiers a la recherce de fichiers mp3
     */
    public void traiter() {
        //initialisation
        String[] list = folder.list(); //liste tous les elements du dossier
        File f;
        ArrayList<String> sub = new ArrayList<>(); //contiendra la liste des sous-dossiers a explorer
        for(int i=0 ; i<list.length ; i++) {
            f = new File(path + "\\" + list[i]); //ouverture du i-eme fichier/dossier
            boolean cond1 = f.getPath().toLowerCase().endsWith(".mp3") && new Tika().detect(f.getPath()).contains("audio/mpeg"); //verifie l'extension et le type MIME;
            boolean cond2 = !f.isDirectory(); //verifier que c'est bien un fichier et non un dossier
            if(cond1 && cond2) { //si c'est bien un mp3
                mp3.put(f.getName(), f.getPath()); //on l'ajoute a la liste
            }
            if(f.isDirectory()) //si le i-eme est un dossier
                sub.add(f.toString()); //on l'ajoute a la liste des sous-dossier a explorer
        }
        //s'il y a des sous-dossiers a explorer, on utilise la recursivite pour les explorer
        if(!sub.isEmpty()) {
            Iterator<String> it = sub.iterator(); //on utilise Iterator pour parcourir la liste
            while(it.hasNext()) { //tant qu'il y a des elements dans la liste
                folder = new File(it.next()); //on associe l'attribue folder a un sous-dossier
                path = folder.getPath();  //on associe l'attribue path au chemin de folder
                traiter(); //recursivite
            }
        }
    }

    /**
     * Fonction qui retourne la liste des noms et des chemins des fichiers mp3 trouves sous la forme d'une HashMap
     * @return Liste des noms et des chemins de tous les fichiers mp3 trouves
     */
    public HashMap<String, String> getMp3(){
        return mp3;
    }
}
