package fr.ecn.medev.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;


/**
 * Service de gestion du dictionnaire de mots pour le jeu du pendu
 * @author Imane Laasri
 * @version 1.0
 */
public class DictionnaireService {

    private final List<String> mots;
    private final SecureRandom random;

    /**
     * Constructeur du service de dictionnaire
     */
    public DictionnaireService() {
        this.mots = new ArrayList<>();
        this.random = new SecureRandom();
    }
    
    /**
     * Charge les mots depuis un fichier texte
     * @param cheminFichier le chemin vers le fichier dictionnaire
     * @throws IOException si le fichier n'existe pas ou est illisible
     * @throws IllegalArgumentException si le fichier est vide
     */
    public void chargerDepuisFichier(String cheminFichier) throws IOException {
        mots.clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                ligne = ligne.trim().toUpperCase();
                if (!ligne.isEmpty() && estMotValide(ligne)) {
                    mots.add(ligne);
                }
            }
        }
        
        if (mots.isEmpty()) {
            throw new IllegalArgumentException("Le fichier ne contient aucun mot valide");
        }
    }
    
    /**
     * Ajoute un mot au dictionnaire
     * @param mot le mot à ajouter
     * @throws IllegalArgumentException si le mot est invalide
     */
    public void ajouterMot(String mot) {
        if (mot == null || mot.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot ne peut pas être vide");
        }
        
        String motFormate = mot.trim().toUpperCase();
        
        if (!estMotValide(motFormate)) {
            throw new IllegalArgumentException("Le mot contient des caractères invalides");
        }
        
        if (!mots.contains(motFormate)) {
            mots.add(motFormate);
        }
    }
    
    /**
     * Vérifie si un mot est valide (uniquement des lettres)
     * @param mot le mot à vérifier
     * @return true si le mot est valide
     */
    private boolean estMotValide(String mot) {
        if (mot == null || mot.isEmpty()) {
            return false;
        }
        
        for (char c : mot.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Sélectionne un mot aléatoire du dictionnaire
     * @return un mot aléatoire
     * @throws IllegalStateException si le dictionnaire est vide
     */
    public String obtenirMotAleatoire() {
        if (mots.isEmpty()) {
            throw new IllegalStateException("Le dictionnaire est vide");
        }
        
        int index = random.nextInt(mots.size());
        return mots.get(index);
    }
    
    /**
     * Retourne le nombre de mots dans le dictionnaire
     * @return nombre de mots
     */
    public int getNombreMots() {
        return mots.size();
    }
    
    /**
     * Vérifie si le dictionnaire est vide
     * @return true si vide
     */
    public boolean estVide() {
        return mots.isEmpty();
    }
    
    /**
     * Retourne la liste des mots du dictionnaire
     * @return copie de la liste des mots
     */
    public List<String> getMots() {
        return new ArrayList<>(mots);
    }
    
    // Manque Javadoc (pour SonarCloud)
    public void vider() {
        mots.clear();
    }
}