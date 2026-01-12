package fr.ecn.medev.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Gère le mot secret et les lettres révélées dans une partie de pendu
 * @author Oussama Kazoubi
 * @version 1.0
 */
public class MotSecret {

    private final String motComplet;
    private final Set<Character> lettresRevelees;

    /**
     * Constructeur du mot secret
     * @param mot le mot à deviner (sera converti en majuscules)
     * @throws IllegalArgumentException si le mot est null ou vide
     */
    public MotSecret(String mot) {
        if (mot == null || mot.isEmpty()) {
            throw new IllegalArgumentException("Le mot ne peut pas être null ou vide");
        }
        this.motComplet = mot.toUpperCase();
        this.lettresRevelees = new HashSet<>();
    }

    /**
     * Révèle une lettre dans le mot
     * @param lettre la lettre à révéler
     * @return true si la lettre est présente dans le mot, false sinon
     */
    public boolean revelerLettre(char lettre) {
        char lettreMaj = Character.toUpperCase(lettre);

        if (motComplet.indexOf(lettreMaj) >= 0) {
            lettresRevelees.add(lettreMaj);
            return true;
        }

        return false;
    }

    /**
     * Vérifie si une lettre a déjà été révélée
     * @param lettre la lettre à vérifier
     * @return true si la lettre a été révélée, false sinon
     */
    public boolean estLettreRevelee(char lettre) {
        return lettresRevelees.contains(Character.toUpperCase(lettre));
    }

    /**
     * Retourne le mot avec les lettres révélées et underscores pour les lettres cachées
     * @return l'état actuel du mot (ex: "A _ G O _ I T H M E")
     */
    public String getMotAffiche() {
        StringBuilder resultat = new StringBuilder();

        for (int i = 0; i < motComplet.length(); i++) {
            char c = motComplet.charAt(i);

            if (lettresRevelees.contains(c)) {
                resultat.append(c);
            } else {
                resultat.append('_');
            }

            if (i < motComplet.length() - 1) {
                resultat.append(' ');
            }
        }

        return resultat.toString();
    }

    /**
     * Vérifie si le mot est entièrement découvert
     * @return true si toutes les lettres ont été révélées, false sinon
     */
    public boolean estComplet() {
        for (char c : motComplet.toCharArray()) {
            if (!lettresRevelees.contains(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retourne le mot complet en majuscules
     * @return le mot secret
     */
    public String getMotComplet() {
        return motComplet;
    }
}