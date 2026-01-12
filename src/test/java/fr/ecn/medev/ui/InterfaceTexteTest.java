package fr.ecn.medev.ui;

import fr.ecn.medev.model.EtatPartie;
import fr.ecn.medev.service.DictionnaireService;
import fr.ecn.medev.service.GestionnairePartie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour InterfaceTexte (validation logique sans IO)
 * @author Oussama Kazoubi
 */
class InterfaceTexteTest {

    private DictionnaireService dictionnaire;
    private GestionnairePartie gestionnaire;

    @BeforeEach
    void setUp() {
        dictionnaire = new DictionnaireService();
        dictionnaire.ajouterMot("JAVA");
        dictionnaire.ajouterMot("PYTHON");
        dictionnaire.ajouterMot("RUBY");
        gestionnaire = new GestionnairePartie(dictionnaire, 6);
    }

    @Test
    @DisplayName("Demarrer mode 1 joueur sans erreur")
    void testDemarrerMode1Joueur() {
        assertDoesNotThrow(() -> gestionnaire.demarrerPartieUnJoueur());
        assertNotNull(gestionnaire.getPartieEnCours());
        assertEquals(EtatPartie.EN_COURS, gestionnaire.getPartieEnCours().getEtat());
    }

    @Test
    @DisplayName("Demarrer mode 2 joueurs avec mot valide")
    void testDemarrerMode2JoueursValide() {
        assertDoesNotThrow(() -> gestionnaire.demarrerPartieDeuxJoueurs("ORDINATEUR"));
        assertEquals("ORDINATEUR", gestionnaire.getPartieEnCours().getMotSecret());
    }

    @Test
    @DisplayName("Mot vide doit lever exception")
    void testMotVide() {
        assertThrows(IllegalArgumentException.class,
                () -> gestionnaire.demarrerPartieDeuxJoueurs(""));
    }

    @Test
    @DisplayName("Mot null doit lever exception")
    void testMotNull() {
        assertThrows(IllegalArgumentException.class,
                () -> gestionnaire.demarrerPartieDeuxJoueurs(null));
    }

    @Test
    @DisplayName("Proposer lettre valide")
    void testProposerLettreValide() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");
        assertDoesNotThrow(() -> gestionnaire.proposerLettre('J'));
    }

    @Test
    @DisplayName("Proposer caractere non alphabetique doit lever exception")
    void testProposerCaractereInvalide() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");
        assertThrows(IllegalArgumentException.class,
                () -> gestionnaire.proposerLettre('1'));
        assertThrows(IllegalArgumentException.class,
                () -> gestionnaire.proposerLettre('!'));
        assertThrows(IllegalArgumentException.class,
                () -> gestionnaire.proposerLettre(' '));
    }

    @Test
    @DisplayName("Lettre deja proposee ne compte pas comme erreur")
    void testLettreDejaProposee() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");

        gestionnaire.proposerLettre('Z'); // Erreur
        int erreurs1 = gestionnaire.getPartieEnCours().getNombreErreurs();

        gestionnaire.proposerLettre('Z'); // Meme lettre
        int erreurs2 = gestionnaire.getPartieEnCours().getNombreErreurs();

        assertEquals(erreurs1, erreurs2); // Pas d'erreur supplementaire
    }

    @Test
    @DisplayName("Victoire quand mot complete")
    void testVictoire() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");

        gestionnaire.proposerLettre('J');
        gestionnaire.proposerLettre('A');
        gestionnaire.proposerLettre('V');

        assertEquals(EtatPartie.GAGNEE, gestionnaire.getPartieEnCours().getEtat());
    }

    @Test
    @DisplayName("Defaite quand erreurs max atteintes")
    void testDefaite() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");

        gestionnaire.proposerLettre('Z');
        gestionnaire.proposerLettre('X');
        gestionnaire.proposerLettre('W');
        gestionnaire.proposerLettre('Q');
        gestionnaire.proposerLettre('B');
        gestionnaire.proposerLettre('C');

        assertEquals(EtatPartie.PERDUE, gestionnaire.getPartieEnCours().getEtat());
    }

    @Test
    @DisplayName("Impossible de jouer apres partie terminee")
    void testJouerApresTerminee() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");

        // Terminer la partie
        gestionnaire.proposerLettre('J');
        gestionnaire.proposerLettre('A');
        gestionnaire.proposerLettre('V');

        // Essayer de jouer
        assertThrows(IllegalStateException.class,
                () -> gestionnaire.proposerLettre('E'));
    }

    @Test
    @DisplayName("Mode 1 joueur avec dictionnaire vide doit lever exception")
    void testMode1JoueurDictionnaireVide() {
        DictionnaireService dictoVide = new DictionnaireService();
        GestionnairePartie gest = new GestionnairePartie(dictoVide, 6);

        assertThrows(IllegalStateException.class, gest::demarrerPartieUnJoueur);
    }

    @Test
    @DisplayName("Insensibilite a la casse")
    void testInsensibiliteCasse() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");

        assertTrue(gestionnaire.proposerLettre('j')); // minuscule
        assertTrue(gestionnaire.proposerLettre('A')); // majuscule
    }

    @Test
    @DisplayName("Gestion des lettres repetees dans le mot")
    void testLettresRepetees() {
        gestionnaire.demarrerPartieDeuxJoueurs("PROGRAMMATION");

        gestionnaire.proposerLettre('R');

        String motAffiche = gestionnaire.getPartieEnCours().getMotAffiche();

        // Les deux R doivent etre reveles
        int countR = 0;
        for (char c : motAffiche.toCharArray()) {
            if (c == 'R') countR++;
        }
        assertEquals(2, countR);
    }

    @Test
    @DisplayName("Nombre d'erreurs incremente correctement")
    void testIncrementErreurs() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");

        assertEquals(0, gestionnaire.getPartieEnCours().getNombreErreurs());

        gestionnaire.proposerLettre('Z'); // Erreur 1
        assertEquals(1, gestionnaire.getPartieEnCours().getNombreErreurs());

        gestionnaire.proposerLettre('X'); // Erreur 2
        assertEquals(2, gestionnaire.getPartieEnCours().getNombreErreurs());
    }
}