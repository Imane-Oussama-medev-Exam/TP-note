package fr.ecn.medev;

import fr.ecn.medev.service.DictionnaireService;
import fr.ecn.medev.service.GestionnairePartie;
import fr.ecn.medev.ui.InterfaceTexte;

import java.io.IOException;

/**
 * Point d'entree principal du jeu du pendu
 * @author Oussama Kazoubi
 * @author Imane Laasri
 * @version 1.0
 */
public class Main {

    private static final String FICHIER_DICTIONNAIRE = "dictionnaire.txt";
    private static final int ERREURS_PAR_DEFAUT = 6;

    /**
     * Methode principale
     * @param args arguments de ligne de commande (non utilises)
     */
    public static void main(String[] args) {
        afficherBanniere();

        DictionnaireService dictionnaire = new DictionnaireService();

        try {
            dictionnaire.chargerDepuisFichier(FICHIER_DICTIONNAIRE);
            System.out.println("Dictionnaire charge : " + dictionnaire.getNombreMots() + " mots disponibles\n");
        } catch (IOException e) {
            System.out.println("ERREUR : Impossible de charger le dictionnaire.");
            System.out.println("Fichier attendu : " + FICHIER_DICTIONNAIRE);
            System.out.println("Message : " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("ERREUR : " + e.getMessage());
            return;
        }

        GestionnairePartie gestionnaire = new GestionnairePartie(dictionnaire, ERREURS_PAR_DEFAUT);
        InterfaceTexte interfaceJeu = new InterfaceTexte(gestionnaire);

        interfaceJeu.afficherMenuPrincipal();
        interfaceJeu.fermer();
    }

    /**
     * Affiche la banniere de bienvenue
     */
    private static void afficherBanniere() {
        System.out.println("================================================");
        System.out.println("                                                ");
        System.out.println("           JEU DU PENDU                         ");
        System.out.println("           Version 1.0                          ");
        System.out.println("                                                ");
        System.out.println("   Developpe par : Oussama K. & Imane L.       ");
        System.out.println("   TP Note MEDEV - Janvier 2026                ");
        System.out.println("                                                ");
        System.out.println("================================================");
        System.out.println();
    }
}