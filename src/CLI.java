import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.apache.tika.Tika;
import java.io.File;
import java.io.IOException;

/**
 * Classe qui permet d'utiliser le programme en mode lignes de commandes (CLI)
 * @author Ghalia NE
 */
public class CLI {
	/**
	 * Fonction main
	 * @param args Differents arguments
	 * @throws IOException Fichier non trouve
	 * @throws InvalidDataException Donnee non trouve
	 * @throws UnsupportedTagException Metadonnees non supportes
	 */
	public static void main(String[] args) throws UnsupportedTagException, IOException, InvalidDataException {
		if (args.length == 0) { //si aucun argument
			System.out.println("\nIl manque des arguments. Veuillez entrer [java -jar CLI.jar -h]\n");
		}
		else {
			switch (args[0]) { //argument >= 1
			
			case "": //argument vide
				System.out.println("\nIl manque des arguments. Veuillez entrer [java -jar CLI.jar -h]\n");
				break;
				
			case "-h": //affiche l'aide
				printHelpGuide();
				break;
	
			case "-d": //explore le dossier
				if(args.length > 2) { //si 3 arguments ou plus
					if (args[2].equals("-o")) { //si on veut generer une playlist XSPF
						if(args.length > 3) { //si le nombre d'arguments requis est respecte
						    new XSPF(args[3], new Explorer(args[1]).getMp3()).write(); //creation d'un fichier XSPF
							System.out.println("\nLe fichier " + args[3] + " à été crée avec succès.\n");
						}
						else { //si pas assez d'arguments
							System.out.println("\nIl manque des arguments. Veuillez entrer [java -jar CLI.jar -h]\n");
						}
					}
					else { //si argument non reconnu
						System.out.println("Essayez  -o au lieu de " + args[2]);
					}
				}
				else if(args.length == 2){ //explore le dossier
					System.out.print("\n");
					dirInformation(args[1], 1);
					System.out.print("\n");
				}
				else { //s'il manque des arguments
					System.out.println("\nIl manque des arguments. Veuillez entrer [java -jar CLI.jar -h]\n");
				}
				break;

			case "-f": //affiche les metadonnees d'un fichier
				if (args.length > 1) { //si 2 arguments ou plus
					String file = args[1]; //chemin du fichier mp3
					if (file.toLowerCase().endsWith(".mp3") && new Tika().detect(file).contains("audio/mpeg")) { //si mp3 valide
						System.out.print("\n");
						new Metadata(args[1]).print(); //affiche
						System.out.print("\n");
					}
					else {
						System.out.println("\nMauvais fichier\n");
					}
				}
				else { //si pas assez d'arguments
					System.out.println("\nIl manque des arguments. Veuillez entrer [java -jar CLI.jar -h]\n");
				}
				break;			
	
			default: //argument non reconnu
				System.out.println("\nIl manque des arguments. Veuillez entrer [java -jar CLI.jar -h]\n");
				break;
			}
		}
	}

	/**
	 * Fonction qui affiche l'aide
	 */
	public static void printHelpGuide() {
		System.out.println("\njava -jar CLI.jar [...]");
		System.out.println("-h : afficher de l'aide");
		System.out.println("-d <dossier> : lister et scanner tous les fichiers du dossier");
		System.out.println("-f <fichier mp3> : afficher les métadonnées du fichier");
		System.out.println("-d <dossier> -o <fichier xspf> : sauvergarde une playlist de tous les fichiers mp3 du dossier et des sous-dossiers dans un fichier XSPF spécifié\n");
	}

	/**
	 * Fonction qui explore et scanne un dossier
	 * @param path Chemin du dossier
	 * @param nb Profondeur de dossier
	 */
	public static void dirInformation(String path, int nb) {
		File folder = new File(path); //ouvrir le dossier
		File[] listOfFiles = folder.listFiles(); //liste du contenu
		//scan du dossier et des sous-dossiers
		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			for(int j = 0 ; j<nb ; j++) {
				System.out.print("\t");
			}
				System.out.print("L ");
			System.out.println(file.getName());
			if (file.isDirectory()) { //si dossier
				dirInformation(file.getPath(), nb+1);
			}
		}
	}
}
