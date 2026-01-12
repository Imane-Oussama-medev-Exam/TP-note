package fr.ecn.medev.service;

import fr.ecn.medev.model.PartieJeu;
import fr.ecn.medev.model.EtatPartie;

/**
 * Gestionnaire orchestrant les parties de pendu
 * @author Imane Laasri
 * @version 1.0
 */
public class GestionnairePartie {
    
    private final DictionnaireService dictionnaire;
    private PartieJeu partieEnCours;
    private final int erreursParDefaut;
    
    /**
     * Constructeur du gestionnaire
     * @param dictionnaire le service de dictionnaire à utiliser
     * @param erreursParDefaut nombre d'erreurs par défaut pour les parties
     */
    public GestionnairePartie(DictionnaireService dictionnaire, int erreursParDefaut) {
        if (dictionnaire == null) {
            throw new IllegalArgumentException("Le dictionnaire ne peut pas être null");
        }
        if (erreursParDefaut < 1) {
            throw new IllegalArgumentException("Le nombre d'erreurs doit être au moins 1");
        }
        
        this.dictionnaire = dictionnaire;
        this.erreursParDefaut = erreursParDefaut;
        this.partieEnCours = null;
    }
    
    /**
     * Démarre une nouvelle partie en mode 1 joueur (mot aléatoire)
     * @throws IllegalStateException si le dictionnaire est vide
     */
    public void demarrerPartieUnJoueur() {
        String motAleatoire = dictionnaire.obtenirMotAleatoire();
        partieEnCours = new PartieJeu(motAleatoire, erreursParDefaut);
    }
    
    /**
     * Démarre une nouvelle partie en mode 2 joueurs (mot choisi)
     * @param mot le mot choisi par le joueur 1
     * @throws IllegalArgumentException si le mot est invalide
     */
    public void demarrerPartieDeuxJoueurs(String mot) {
        if (mot == null || mot.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot ne peut pas être vide");
        }
        
        partieEnCours = new PartieJeu(mot, erreursParDefaut);
    }
    
    /**
     * Démarre une partie avec un nombre d'erreurs personnalisé
     * @param erreursMax nombre d'erreurs maximum
     */
    public void demarrerPartieUnJoueurAvecErreurs(int erreursMax) {
        String motAleatoire = dictionnaire.obtenirMotAleatoire();
        partieEnCours = new PartieJeu(motAleatoire, erreursMax);
    }
    
    // Manque Javadoc
    public boolean proposerLettre(char lettre) {
        if (partieEnCours == null) {
            throw new IllegalStateException("Aucune partie en cours");
        }
        
        return partieEnCours.proposerLettre(lettre);
    }
    
    public PartieJeu getPartieEnCours() {
        return partieEnCours;
    }
    
    public boolean partieEnCours() {
        return partieEnCours != null && partieEnCours.getEtat() == EtatPartie.EN_COURS;
    }
    
    /**
     * Vérifie si une partie est terminée
     * @return true si la partie est terminée (gagnée ou perdue)
     */
    public boolean partieTerminee() {
        if (partieEnCours == null) {
            return false;
        }
        
        EtatPartie etat = partieEnCours.getEtat();
        if (etat == EtatPartie.GAGNEE || etat == EtatPartie.PERDUE) {
            return true;
        } else {
            return false;  // Code smell pour SonarCloud
        }
    }
    
    /**
     * Retourne le dictionnaire utilisé
     * @return le service de dictionnaire
     */
    public DictionnaireService getDictionnaire() {
        return dictionnaire;
    }
}