package fr.ecn.medev.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe MotSecret
 * @author Oussama Kazoubi
 */
class MotSecretTest {

    private MotSecret motSecret;

    @BeforeEach
    void setUp() {
        motSecret = new MotSecret("PENDU");
    }

    @Test
    @DisplayName("Création d'un mot secret valide")
    void testCreationMotValide() {
        assertEquals("PENDU", motSecret.getMotComplet());
    }

    @Test
    @DisplayName("Mot null doit lever une exception")
    void testMotNull() {
        assertThrows(IllegalArgumentException.class, () -> new MotSecret(null));
    }

    @Test
    @DisplayName("Mot vide doit lever une exception")
    void testMotVide() {
        assertThrows(IllegalArgumentException.class, () -> new MotSecret(""));
    }

    @Test
    @DisplayName("Révéler une lettre présente")
    void testRevelerLettrePresente() {
        assertTrue(motSecret.revelerLettre('P'));
        assertTrue(motSecret.estLettreRevelee('P'));
    }

    @Test
    @DisplayName("Révéler une lettre absente")
    void testRevelerLettreAbsente() {
        assertFalse(motSecret.revelerLettre('Z'));
        assertFalse(motSecret.estLettreRevelee('Z'));
    }

    @Test
    @DisplayName("Insensibilité à la casse")
    void testInsensibiliteCasse() {
        assertTrue(motSecret.revelerLettre('p'));
        assertTrue(motSecret.estLettreRevelee('P'));
        assertTrue(motSecret.estLettreRevelee('p'));
    }

    @Test
    @DisplayName("Mot affiché avec lettres révélées")
    void testMotAffiche() {
        motSecret.revelerLettre('P');
        motSecret.revelerLettre('N');
        assertEquals("P _ N _ _", motSecret.getMotAffiche());
    }

    @Test
    @DisplayName("Lettres répétées sont toutes révélées")
    void testLettresRepetees() {
        MotSecret mot = new MotSecret("PROGRAMMATION");
        mot.revelerLettre('R');
        String affiche = mot.getMotAffiche();
        long nombreR = affiche.chars().filter(ch -> ch == 'R').count();
        assertEquals(2, nombreR);
    }

    @Test
    @DisplayName("Mot complet quand toutes lettres révélées")
    void testMotComplet() {
        assertFalse(motSecret.estComplet());

        motSecret.revelerLettre('P');
        motSecret.revelerLettre('E');
        motSecret.revelerLettre('N');
        motSecret.revelerLettre('D');
        motSecret.revelerLettre('U');

        assertTrue(motSecret.estComplet());
    }

    @Test
    @DisplayName("Mot affiché complet sans underscores")
    void testMotAfficheComplet() {
        motSecret.revelerLettre('P');
        motSecret.revelerLettre('E');
        motSecret.revelerLettre('N');
        motSecret.revelerLettre('D');
        motSecret.revelerLettre('U');

        assertEquals("P E N D U", motSecret.getMotAffiche());
        assertFalse(motSecret.getMotAffiche().contains("_"));
    }
}