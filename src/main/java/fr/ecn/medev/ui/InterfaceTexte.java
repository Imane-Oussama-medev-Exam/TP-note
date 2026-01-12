package fr.ecn.medev.ui;

import fr.ecn.medev.model.EtatPartie;
import fr.ecn.medev.model.PartieJeu;
import fr.ecn.medev.service.GestionnairePartie;

import java.util.Scanner;
import java.util.Set;

/**
 * Interface texte pour interagir avec le jeu du pendu
 * @author Oussama Kazoubi
 * @version 1.0
 */
public class InterfaceTexte {

    private static final String SEPARATEUR = "========================================";
    private static final int LIGNES_EFFACEMENT = 50;

    private final Scanner scanner;
    private final GestionnairePartie gestionnaire;
    private AfficheurPendu afficheur;

    /**
     * Constructeur de l'interface texte
     * @param gestionnaire le gestionnaire de partie
     */
    public InterfaceTexte(GestionnairePartie gestionnaire) {
        this.scanner = new Scanner(System.in);
        this.gestionnaire = gestionnaire;
    }

    /**
     * Affiche le menu principal et gère la sélection
     */
    public void afficherMenuPrincipal() {
        boolean continuer = true;

        while (continuer) {
            afficherMenu();
            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    jouerModeUnJoueur();
                    break;
                case "2":
                    jouerModeDeuxJoueurs();
                    break;
                case "3":
                    continuer = false;
                    System.out.println("\nMerci d'avoir joué ! À bientôt.");
                    break;
                default:
                    System.out.println("\nChoix invalide. Veuillez entrer 1, 2 ou 3.");
            }
        }
    }

    /**
     * Affiche le menu des options
     */
    private void afficherMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║       JEU DU PENDU - MENU         ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("\n1. Mode 1 joueur (mot aléatoire)");
        System.out.println("2. Mode 2 joueurs (choisir le mot)");
        System.out.println("3. Quitter");
        System.out.print("\nVotre choix : ");
    }

    /**
     * Lance une partie en mode 1 joueur
     */
    private void jouerModeUnJoueur() {
        try {
            gestionnaire.demarrerPartieUnJoueur();
            initialiserAfficheur();
            jouerPartie();
        } catch (IllegalStateException e) {
            System.out.println("\nErreur : " + e.getMessage());
        }
    }

    /**
     * Lance une partie en mode 2 joueurs
     */
    private void jouerModeDeuxJoueurs() {
        System.out.print("\nJoueur 1, entrez le mot secret : ");
        String mot = scanner.nextLine().trim().toUpperCase();

        if (mot.isEmpty()) {
            System.out.println("\nLe mot ne peut pas être vide.");
            return;
        }

        effacerEcran();

        try {
            gestionnaire.demarrerPartieDeuxJoueurs(mot);
            initialiserAfficheur();
            System.out.println("\n✓ Mot enregistré ! Joueur 2, à vous de jouer !\n");
            jouerPartie();
        } catch (IllegalArgumentException e) {
            System.out.println("\nErreur : " + e.getMessage());
        }
    }

    /**
     * Initialise l'afficheur de pendu
     */
    private void initialiserAfficheur() {
        afficheur = new AfficheurPendu(gestionnaire.getPartieEnCours().getErreursMaximales());
    }

    /**
     * Efface l'écran (approximatif)
     */
    private void effacerEcran() {
        for (int i = 0; i < LIGNES_EFFACEMENT; i++) {
            System.out.println();
        }
    }

    /**
     * Boucle principale du jeu
     */
    private void jouerPartie() {
        PartieJeu partie = gestionnaire.getPartieEnCours();

        while (partie.getEtat() == EtatPartie.EN_COURS) {
            afficherEtatPartie();
            traiterPropositionLettre(partie);
        }

        afficherResultatFinal();
    }

    /**
     * Traite la proposition d'une lettre par le joueur
     * @param partie la partie en cours
     */
    private void traiterPropositionLettre(PartieJeu partie) {
        System.out.print("\nProposez une lettre : ");
        String input = scanner.nextLine().trim();

        if (input.length() != 1) {
            System.out.println("⚠ Veuillez entrer une seule lettre.");
            return;
        }

        char lettre = input.charAt(0);

        try {
            if (partie.estLettreDejaProposee(lettre)) {
                System.out.println("⚠ Vous avez déjà proposé cette lettre !");
                return;
            }

            boolean presente = gestionnaire.proposerLettre(lettre);
            afficherResultatProposition(presente);

        } catch (IllegalArgumentException e) {
            System.out.println("⚠ " + e.getMessage());
        }
    }

    /**
     * Affiche le résultat d'une proposition de lettre
     * @param presente true si la lettre est dans le mot
     */
    private void afficherResultatProposition(boolean presente) {
        if (presente) {
            System.out.println("✓ Bien joué ! La lettre est présente.");
        } else {
            System.out.println("✗ Dommage ! La lettre n'est pas dans le mot.");
        }
    }

    /**
     * Affiche l'état actuel de la partie
     */
    private void afficherEtatPartie() {
        PartieJeu partie = gestionnaire.getPartieEnCours();

        System.out.println("\n" + SEPARATEUR);
        afficheur.afficher(partie.getNombreErreurs());
        System.out.println("\n" + SEPARATEUR);

        System.out.println("\nMot : " + partie.getMotAffiche());
        System.out.println("Erreurs : " + partie.getNombreErreurs() + "/" + partie.getErreursMaximales());

        afficherLettresProposees(partie.getLettresProposees());
    }

    /**
     * Affiche les lettres déjà proposées
     * @param lettresProposees ensemble des lettres proposées
     */
    private void afficherLettresProposees(Set<Character> lettresProposees) {
        if (!lettresProposees.isEmpty()) {
            System.out.println("Lettres proposées : " + lettresProposees);
        }
    }

    /**
     * Affiche le résultat final de la partie
     */
    private void afficherResultatFinal() {
        PartieJeu partie = gestionnaire.getPartieEnCours();

        System.out.println("\n" + SEPARATEUR);
        afficheur.afficher(partie.getNombreErreurs());
        System.out.println("\n" + SEPARATEUR);

        if (partie.getEtat() == EtatPartie.GAGNEE) {
            afficherVictoire(partie);
        } else {
            afficherDefaite(partie);
        }
    }

    /**
     * Affiche le message de victoire
     * @param partie la partie terminée
     */
    private void afficherVictoire(PartieJeu partie) {
        System.out.println("\n🎉 FÉLICITATIONS ! Vous avez gagné !");
        System.out.println("Le mot était : " + partie.getMotSecret());
        System.out.println("Erreurs : " + partie.getNombreErreurs() + "/" + partie.getErreursMaximales());
    }

    /**
     * Affiche le message de défaite
     * @param partie la partie terminée
     */
    private void afficherDefaite(PartieJeu partie) {
        System.out.println("\n💀 PERDU ! Le pendu est complet...");
        System.out.println("Le mot était : " + partie.getMotSecret());
    }

    /**
     * Ferme le scanner
     */
    public void fermer() {
        scanner.close();
    }
}