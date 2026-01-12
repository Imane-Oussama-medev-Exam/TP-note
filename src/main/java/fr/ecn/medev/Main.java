package fr.ecn.medev;

import fr.ecn.medev.service.DictionnaireService;
import fr.ecn.medev.service.GestionnairePartie;
import fr.ecn.medev.ui.InterfaceTexte;

import java.io.IOException;

/**
 * Point d'entrée principal du jeu du pendu
 * @author Oussama Kazoubi
 * @author Imane Laasri
 * @version 1.0
 */
public class Main {

    private static final String FICHIER_DICTIONNAIRE = "dictionnaire.txt";
    private static final int ERREURS_PAR_DEFAUT = 6;

    /**
     * Méthode principale
     * @param args arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        afficherBanniere();

        DictionnaireService dictionnaire = new DictionnaireService();

        try {
            dictionnaire.chargerDepuisFichier(FICHIER_DICTIONNAIRE);
            System.out.println("✓ Dictionnaire chargé : " + dictionnaire.getNombreMots() + " mots disponibles\n");
        } catch (IOException e) {
            System.out.println("✗ Erreur : Impossible de charger le dictionnaire.");
            System.out.println("  Fichier attendu : " + FICHIER_DICTIONNAIRE);
            System.out.println("  Message : " + e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Erreur : " + e.getMessage());
            return;
        }

        GestionnairePartie gestionnaire = new GestionnairePartie(dictionnaire, ERREURS_PAR_DEFAUT);
        InterfaceTexte interface_jeu = new InterfaceTexte(gestionnaire);

        interface_jeu.afficherMenuPrincipal();
        interface_jeu.fermer();
    }

    /**
     * Affiche la bannière de bienvenue
     */
    private static void afficherBanniere() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║                                                ║");
        System.out.println("║           JEU DU PENDU                         ║");
        System.out.println("║           Version 1.0                          ║");
        System.out.println("║                                                ║");
        System.out.println("║   Développé par : Oussama K. & Imane L.        ║");
        System.out.println("║   TP Noté MEDEV - Janvier 2026                 ║");
        System.out.println("║                                                ║");
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println();
    }
}