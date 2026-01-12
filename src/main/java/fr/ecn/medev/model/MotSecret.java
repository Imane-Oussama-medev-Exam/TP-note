package fr.ecn.medev.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Gère le mot secret et les lettres révélées
 * @author Oussama Kazoubi
 * @version 1.0
 */
public class MotSecret {

    private final String motComplet;
    private final Set<Character> lettresRevelees;

    /**
     * Constructeur du mot secret
     * @param mot le mot à deviner
     */
    public MotSecret(String mot) {
        if (mot == null || mot.isEmpty()) {
            throw new IllegalArgumentException("Le mot ne peut pas être null ou vide");
        }
        this.motComplet = mot.toUpperCase();
        this.lettresRevelees = new HashSet<>();
    }

    public boolean revelerLettre(char lettre) {
        char lettreMaj = Character.toUpperCase(lettre);

        if (motComplet.indexOf(lettreMaj) >= 0) {
            lettresRevelees.add(lettreMaj);
            return true;
        } else {
            return false;
        }
    }

    public boolean estLettreRevelee(char lettre) {
        return lettresRevelees.contains(Character.toUpperCase(lettre));
    }

    /**
     * Retourne le mot avec les lettres révélées
     * @return l'état actuel (ex: "A _ G O _ I T H M E")
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

    public boolean estComplet() {
        for (char c : motComplet.toCharArray()) {
            if (!lettresRevelees.contains(c)) {
                return false;
            }
        }
        return true;
    }

    public String getMotComplet() {
        return motComplet;
    }
}