package fr.ecn.medev.ui;

/**
 * Affiche le dessin ASCII du pendu en fonction du nombre d'erreurs
 * @author Imane Laasri
 * @version 1.0
 */
public class AfficheurPendu {

    // Constantes pour les éléments du dessin
    private static final String POTENCE = "  +---+\n  |   |\n";
    private static final String LIGNE_VIDE = "      |\n";
    private static final String BASE = "      |\n=========";

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

    /**
     * Construit le dessin pour une configuration à 6 erreurs
     * @param erreurs nombre d'erreurs actuelles
     * @return le dessin ASCII
     */
    private String obtenirDessin6Erreurs(int erreurs) {
        StringBuilder dessin = new StringBuilder();
        dessin.append(POTENCE);

        // Tête (erreur 1)
        dessin.append(obtenirTete(erreurs, 1));

        // Corps + bras (erreurs 2-4)
        dessin.append(obtenirCorps(erreurs, 2, 3, 4));

        // Jambes (erreurs 5-6)
        dessin.append(obtenirJambes(erreurs, 5, 6));

        dessin.append(BASE);
        return dessin.toString();
    }

    /**
     * Construit le dessin pour une configuration à 7 erreurs
     * @param erreurs nombre d'erreurs actuelles
     * @return le dessin ASCII
     */
    private String obtenirDessin7Erreurs(int erreurs) {
        StringBuilder dessin = new StringBuilder();
        dessin.append(POTENCE);

        // Tête (erreur 1)
        dessin.append(obtenirTete(erreurs, 1));

        // Corps + bras (erreurs 2-5)
        dessin.append(obtenirCorps(erreurs, 2, 4, 5));

        // Jambes (erreurs 6-7) + ligne supplémentaire
        if (erreurs >= 7) {
            dessin.append(" / \\  |\n");
        } else if (erreurs >= 6) {
            dessin.append(" /    |\n");
        } else if (erreurs >= 3) {
            dessin.append("  |   |\n");
        } else {
            dessin.append(LIGNE_VIDE);
        }

        dessin.append(BASE);
        return dessin.toString();
    }

    /**
     * Retourne la ligne de la tête selon le nombre d'erreurs
     * @param erreurs nombre d'erreurs actuelles
     * @param seuilTete seuil à partir duquel la tête apparaît
     * @return la ligne ASCII de la tête
     */
    private String obtenirTete(int erreurs, int seuilTete) {
        if (erreurs >= seuilTete) {
            return "  O   |\n";
        }
        return LIGNE_VIDE;
    }

    /**
     * Retourne la ligne du corps et des bras selon le nombre d'erreurs
     * @param erreurs nombre d'erreurs actuelles
     * @param seuilCorps seuil pour le corps seul
     * @param seuilBrasGauche seuil pour le bras gauche
     * @param seuilBrasDroit seuil pour le bras droit
     * @return la ligne ASCII du corps
     */
    private String obtenirCorps(int erreurs, int seuilCorps, int seuilBrasGauche, int seuilBrasDroit) {
        if (erreurs >= seuilBrasDroit) {
            return " /|\\  |\n";
        } else if (erreurs >= seuilBrasGauche) {
            return " /|   |\n";
        } else if (erreurs >= seuilCorps) {
            return "  |   |\n";
        }
        return LIGNE_VIDE;
    }

    /**
     * Retourne la ligne des jambes selon le nombre d'erreurs
     * @param erreurs nombre d'erreurs actuelles
     * @param seuilJambeGauche seuil pour la jambe gauche
     * @param seuilJambeDroite seuil pour la jambe droite
     * @return la ligne ASCII des jambes
     */
    private String obtenirJambes(int erreurs, int seuilJambeGauche, int seuilJambeDroite) {
        if (erreurs >= seuilJambeDroite) {
            return " / \\  |\n";
        } else if (erreurs >= seuilJambeGauche) {
            return " /    |\n";
        }
        return LIGNE_VIDE;
    }

    /**
     * Génère un dessin générique basé sur la progression des erreurs
     * @param erreurs nombre d'erreurs actuelles
     * @return le dessin ASCII
     */
    private String obtenirDessinGenerique(int erreurs) {
        double progression = (double) erreurs / erreursMaximales;

        if (progression < 0.2) {
            return POTENCE + LIGNE_VIDE + LIGNE_VIDE + LIGNE_VIDE + BASE;
        } else if (progression < 0.4) {
            return POTENCE + "  O   |\n" + LIGNE_VIDE + LIGNE_VIDE + BASE;
        } else if (progression < 0.6) {
            return POTENCE + "  O   |\n  |   |\n" + LIGNE_VIDE + BASE;
        } else if (progression < 0.8) {
            return POTENCE + "  O   |\n /|\\  |\n" + LIGNE_VIDE + BASE;
        } else {
            return POTENCE + "  O   |\n /|\\  |\n / \\  |\n" + BASE;
        }
    }
}