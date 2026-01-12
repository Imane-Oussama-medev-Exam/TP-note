package fr.ecn.medev.ui;

/**
 * Affiche le dessin ASCII du pendu en fonction du nombre d'erreurs
 * @author Imane Laasri
 * @version 1.0
 */
public class AfficheurPendu {
    
    private final int erreursMaximales;
    
    /**
     * Constructeur de l'afficheur
     * @param erreursMax nombre maximal d'erreurs (détermine le dessin)
     */
    public AfficheurPendu(int erreursMax) {
        this.erreursMaximales = erreursMax;
    }
    
    /**
     * Affiche le pendu selon le nombre d'erreurs actuelles
     * @param nombreErreurs nombre d'erreurs commises
     */
    public void afficher(int nombreErreurs) {
        System.out.println(obtenirDessin(nombreErreurs));
    }
    
    /**
     * Retourne le dessin ASCII correspondant au nombre d'erreurs
     * @param nombreErreurs nombre d'erreurs
     * @return le dessin ASCII
     */
    public String obtenirDessin(int nombreErreurs) {
        if (erreursMaximales == 6) {
            return obtenirDessin6Erreurs(nombreErreurs);
        } else if (erreursMaximales == 7) {
            return obtenirDessin7Erreurs(nombreErreurs);
        } else {
            return obtenirDessinGenerique(nombreErreurs);
        }
    }
    
    private String obtenirDessin6Erreurs(int erreurs) {
        StringBuilder dessin = new StringBuilder();
        dessin.append("  +---+\n");
        dessin.append("  |   |\n");
        
        // Tête (erreur 1)
        if (erreurs >= 1) {
            dessin.append("  O   |\n");
        } else {
            dessin.append("      |\n");
        }
        
        // Corps + bras (erreurs 2-4)
        if (erreurs >= 4) {
            dessin.append(" /|\\  |\n");
        } else if (erreurs >= 3) {
            dessin.append(" /|   |\n");
        } else if (erreurs >= 2) {
            dessin.append("  |   |\n");
        } else {
            dessin.append("      |\n");
        }
        
        // Jambes (erreurs 5-6)
        if (erreurs >= 6) {
            dessin.append(" / \\  |\n");
        } else if (erreurs >= 5) {
            dessin.append(" /    |\n");
        } else {
            dessin.append("      |\n");
        }
        
        dessin.append("      |\n");
        dessin.append("=========");
        
        return dessin.toString();
    }
    
    private String obtenirDessin7Erreurs(int erreurs) {
        StringBuilder dessin = new StringBuilder();
        dessin.append("  +---+\n");
        dessin.append("  |   |\n");
        
        if (erreurs >= 1) {
            dessin.append("  O   |\n");
        } else {
            dessin.append("      |\n");
        }
        
        if (erreurs >= 5) {
            dessin.append(" /|\\  |\n");
        } else if (erreurs >= 4) {
            dessin.append(" /|   |\n");
        } else if (erreurs >= 2) {
            dessin.append("  |   |\n");
        } else {
            dessin.append("      |\n");
        }
        
        if (erreurs >= 7) {
            dessin.append(" / \\  |\n");
        } else if (erreurs >= 6) {
            dessin.append(" /    |\n");
        } else if (erreurs >= 3) {
            dessin.append("  |   |\n");
        } else {
            dessin.append("      |\n");
        }
        
        dessin.append("      |\n");
        dessin.append("=========");
        
        return dessin.toString();
    }
    
    // Méthode sans Javadoc pour SonarCloud
    private String obtenirDessinGenerique(int erreurs) {
        double progression = (double) erreurs / erreursMaximales;
        
        if (progression < 0.2) {
            return "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========";
        } else if (progression < 0.4) {
            return "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========";
        } else if (progression < 0.6) {
            return "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========";
        } else if (progression < 0.8) {
            return "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========";
        } else {
            return "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n=========";
        }
    }
}