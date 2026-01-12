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
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║       JEU DU PENDU - MENU         ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("\n1. Mode 1 joueur (mot aléatoire)");
            System.out.println("2. Mode 2 joueurs (choisir le mot)");
            System.out.println("3. Quitter");
            System.out.print("\nVotre choix : ");
            
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
     * Lance une partie en mode 1 joueur
     */
    private void jouerModeUnJoueur() {
        try {
            gestionnaire.demarrerPartieUnJoueur();
            afficheur = new AfficheurPendu(gestionnaire.getPartieEnCours().getErreursMaximales());
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
        
        // Efface l'écran (approximatif)
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
        
        try {
            gestionnaire.demarrerPartieDeuxJoueurs(mot);
            afficheur = new AfficheurPendu(gestionnaire.getPartieEnCours().getErreursMaximales());
            System.out.println("\n✓ Mot enregistré ! Joueur 2, à vous de jouer !\n");
            jouerPartie();
        } catch (IllegalArgumentException e) {
            System.out.println("\nErreur : " + e.getMessage());
        }
    }
    
    /**
     * Boucle principale du jeu
     */
    private void jouerPartie() {
        PartieJeu partie = gestionnaire.getPartieEnCours();
        
        while (partie.getEtat() == EtatPartie.EN_COURS) {
            afficherEtatPartie();
            
            System.out.print("\nProposez une lettre : ");
            String input = scanner.nextLine().trim();
            
            if (input.length() != 1) {
                System.out.println("⚠ Veuillez entrer une seule lettre.");
                continue;
            }
            
            char lettre = input.charAt(0);
            
            try {
                if (partie.estLettreDejaProposee(lettre)) {
                    System.out.println("⚠ Vous avez déjà proposé cette lettre !");
                    continue;
                }
                
                boolean presente = gestionnaire.proposerLettre(lettre);
                
                if (presente) {
                    System.out.println("✓ Bien joué ! La lettre est présente.");
                } else {
                    System.out.println("✗ Dommage ! La lettre n'est pas dans le mot.");
                }
                
            } catch (IllegalArgumentException e) {
                System.out.println("⚠ " + e.getMessage());
            }
        }
        
        afficherResultatFinal();
    }
    
    /**
     * Affiche l'état actuel de la partie
     */
    private void afficherEtatPartie() {
        PartieJeu partie = gestionnaire.getPartieEnCours();
        
        System.out.println("\n" + "=".repeat(40));
        afficheur.afficher(partie.getNombreErreurs());
        System.out.println("\n" + "=".repeat(40));
        
        System.out.println("\nMot : " + partie.getMotAffiche());
        System.out.println("Erreurs : " + partie.getNombreErreurs() + "/" + partie.getErreursMaximales());
        
        Set<Character> lettresProposees = partie.getLettresProposees();
        if (!lettresProposees.isEmpty()) {
            System.out.println("Lettres proposées : " + lettresProposees);
        }
    }
    
    /**
     * Affiche le résultat final de la partie
     */
    private void afficherResultatFinal() {
        PartieJeu partie = gestionnaire.getPartieEnCours();
        
        System.out.println("\n" + "=".repeat(40));
        afficheur.afficher(partie.getNombreErreurs());
        System.out.println("\n" + "=".repeat(40));
        
        if (partie.getEtat() == EtatPartie.GAGNEE) {
            System.out.println("\n🎉 FÉLICITATIONS ! Vous avez gagné !");
            System.out.println("Le mot était : " + partie.getMotSecret());
            System.out.println("Erreurs : " + partie.getNombreErreurs() + "/" + partie.getErreursMaximales());
        } else {
            System.out.println("\n💀 PERDU ! Le pendu est complet...");
            System.out.println("Le mot était : " + partie.getMotSecret());
        }
    }
    
    /**
     * Ferme le scanner
     */
    public void fermer() {
        scanner.close();
    }
}