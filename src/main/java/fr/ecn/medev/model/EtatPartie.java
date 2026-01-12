package fr.ecn.medev.model;

/**
 * Représente les différents états possibles d'une partie de pendu
 * @author Oussama Kazoubi
 * @version 1.0
 */
public enum EtatPartie {
    /**
     * La partie est en cours
     */
    EN_COURS,

    /**
     * Le joueur a gagné - mot entièrement découvert
     */
    GAGNEE,

    /**
     * Le joueur a perdu - nombre maximal d'erreurs atteint
     */
    PERDUE
}