package fr.ecn.medev.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente l'état complet d'une partie de pendu
 * @author Imane Laasri
 * @version 1.0
 */
public class PartieJeu {
    
    private final MotSecret motSecret;
    private final Set<Character> lettresProposees;
    private final int erreursMaximales;
    private int nombreErreurs;
    private EtatPartie etat;
    
    /**
     * Constructeur d'une partie de pendu
     * @param mot le mot à deviner
     * @param erreursMax le nombre maximum d'erreurs autorisées
     * @throws IllegalArgumentException si erreursMax est inférieur à 1
     */
    public PartieJeu(String mot, int erreursMax) {
        if (erreursMax < 1) {
            throw new IllegalArgumentException("Le nombre d'erreurs doit être au moins 1");
        }
        
        this.motSecret = new MotSecret(mot);
        this.lettresProposees = new HashSet<>();
        this.erreursMaximales = erreursMax;
        this.nombreErreurs = 0;
        this.etat = EtatPartie.EN_COURS;
    }
    
    /**
     * Propose une lettre pour deviner le mot
     * @param lettre la lettre proposée
     * @return true si la lettre est dans le mot, false sinon
     * @throws IllegalArgumentException si la lettre n'est pas alphabétique
     * @throws IllegalStateException si la partie est terminée
     */
    public boolean proposerLettre(char lettre) {
        if (!Character.isLetter(lettre)) {
            throw new IllegalArgumentException("Seules les lettres sont autorisées");
        }
        
        if (etat != EtatPartie.EN_COURS) {
            throw new IllegalStateException("La partie est terminée");
        }
        
        
        char lettreMaj = Character.toUpperCase(lettre);
        
        // Lettre déjà proposée - on ne compte pas comme erreur
        if (lettresProposees.contains(lettreMaj)) {
            return false;
        }
        
        lettresProposees.add(lettreMaj);
        
        boolean presente = motSecret.revelerLettre(lettreMaj);
        
        if (!presente) {
            nombreErreurs++;
        }
        
        mettreAJourEtat();
        
        return presente;
    }
    
    /**
     * Met à jour l'état de la partie (victoire ou défaite)
     */
    private void mettreAJourEtat() {
        if (motSecret.estComplet()) {
            etat = EtatPartie.GAGNEE;
        } else if (nombreErreurs >= erreursMaximales) {
            etat = EtatPartie.PERDUE;
        }
    }
    
    /**
     * Vérifie si une lettre a déjà été proposée
     * @param lettre la lettre à vérifier
     * @return true si la lettre a été proposée, false sinon
     */
    public boolean estLettreDejaProposee(char lettre) {
        return lettresProposees.contains(Character.toUpperCase(lettre));
    }
    
    /**
     * Retourne l'affichage actuel du mot
     * @return le mot avec lettres révélées et underscores
     */
    public String getMotAffiche() {
        return motSecret.getMotAffiche();
    }
    
    /**
     * Retourne le nombre d'erreurs commises
     * @return nombre d'erreurs
     */
    public int getNombreErreurs() {
        return nombreErreurs;
    }
    
    /**
     * Retourne le nombre d'erreurs restantes autorisées
     * @return erreurs restantes
     */
    public int getErreursRestantes() {
        return erreursMaximales - nombreErreurs;
    }
    
    /**
     * Retourne l'état actuel de la partie
     * @return l'état (EN_COURS, GAGNEE, PERDUE)
     */
    public EtatPartie getEtat() {
        return etat;
    }
    
    /**
     * Retourne l'ensemble des lettres proposées
     * @return copie de l'ensemble des lettres
     */
    public Set<Character> getLettresProposees() {
        return new HashSet<>(lettresProposees);
    }
    
    /**
     * Retourne le mot secret (à utiliser en fin de partie)
     * @return le mot complet
     */
    public String getMotSecret() {
        return motSecret.getMotComplet();
    }
    
    /**
     * Retourne le nombre maximal d'erreurs autorisées
     * @return erreurs maximales
     */
    public int getErreursMaximales() {
        return erreursMaximales;
    }
}