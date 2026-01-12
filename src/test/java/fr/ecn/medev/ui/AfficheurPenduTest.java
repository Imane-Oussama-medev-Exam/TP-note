package fr.ecn.medev.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour AfficheurPendu
 * @author Oussama Kazoubi
 */
class AfficheurPenduTest {

    @Test
    @DisplayName("Dessin initial sans erreur (6 erreurs max)")
    void testDessinInitial6Erreurs() {
        AfficheurPendu afficheur = new AfficheurPendu(6);
        String dessin = afficheur.obtenirDessin(0);

        assertNotNull(dessin);
        assertTrue(dessin.contains("+---+"));
        assertTrue(dessin.contains("========="));
        assertFalse(dessin.contains("O")); // Pas de tête
    }

    @Test
    @DisplayName("Dessin avec 1 erreur - tête seulement")
    void testDessin1Erreur() {
        AfficheurPendu afficheur = new AfficheurPendu(6);
        String dessin = afficheur.obtenirDessin(1);

        assertTrue(dessin.contains("O")); // Tête présente
        assertFalse(dessin.contains("/|\\")); // Pas de bras complets
        assertFalse(dessin.contains("/ \\")); // Pas de jambes
    }

    @Test
    @DisplayName("Dessin avec 2 erreurs - tête et corps")
    void testDessin2Erreurs() {
        AfficheurPendu afficheur = new AfficheurPendu(6);
        String dessin = afficheur.obtenirDessin(2);

        assertTrue(dessin.contains("O"));   // Tête
        assertTrue(dessin.contains("  |   |")); // Corps
        assertFalse(dessin.contains("/|\\")); // Pas encore de bras
    }

    @Test
    @DisplayName("Dessin avec 4 erreurs - corps complet avec bras")
    void testDessin4Erreurs() {
        AfficheurPendu afficheur = new AfficheurPendu(6);
        String dessin = afficheur.obtenirDessin(4);

        assertTrue(dessin.contains("O"));       // Tête
        assertTrue(dessin.contains(" /|\\  |")); // Bras complets
        assertFalse(dessin.contains("/ \\")); // Pas encore de jambes
    }

    @Test
    @DisplayName("Dessin avec 6 erreurs - pendu complet")
    void testDessinComplet6Erreurs() {
        AfficheurPendu afficheur = new AfficheurPendu(6);
        String dessin = afficheur.obtenirDessin(6);

        assertTrue(dessin.contains("O"));       // Tête
        assertTrue(dessin.contains(" /|\\  |")); // Bras
        assertTrue(dessin.contains(" / \\  |")); // Jambes complètes
    }

    @Test
    @DisplayName("Configuration 7 erreurs - dessin initial")
    void testDessinInitial7Erreurs() {
        AfficheurPendu afficheur = new AfficheurPendu(7);
        String dessin = afficheur.obtenirDessin(0);

        assertNotNull(dessin);
        assertFalse(dessin.contains("O"));
    }

    @Test
    @DisplayName("Configuration 7 erreurs - dessin complet")
    void testDessinComplet7Erreurs() {
        AfficheurPendu afficheur = new AfficheurPendu(7);
        String dessin = afficheur.obtenirDessin(7);

        assertTrue(dessin.contains("O"));
        assertTrue(dessin.contains(" /|\\  |"));
        assertTrue(dessin.contains(" / \\  |"));
    }

    @Test
    @DisplayName("Dessin générique - configuration personnalisée")
    void testDessinGenerique() {
        AfficheurPendu afficheur = new AfficheurPendu(10);
        String dessin = afficheur.obtenirDessin(5);

        assertNotNull(dessin);
        assertTrue(dessin.contains("+---+"));
        assertTrue(dessin.contains("========="));
    }

    @Test
    @DisplayName("Structure du dessin valide")
    void testStructureDessin() {
        AfficheurPendu afficheur = new AfficheurPendu(6);
        String dessin = afficheur.obtenirDessin(3);

        // Vérifie la présence des éléments de structure
        assertTrue(dessin.contains("+---+"));
        assertTrue(dessin.contains("|"));
        assertTrue(dessin.contains("========="));
    }

    @Test
    @DisplayName("Progression du dessin - éléments ajoutés")
    void testProgressionDessin() {
        AfficheurPendu afficheur = new AfficheurPendu(6);

        String dessin0 = afficheur.obtenirDessin(0);
        String dessin3 = afficheur.obtenirDessin(3);
        String dessin6 = afficheur.obtenirDessin(6);

        // Vérifie que les éléments s'ajoutent progressivement
        assertFalse(dessin0.contains("O"));  // Pas de tête au début
        assertTrue(dessin3.contains("O"));   // Tête à 3 erreurs
        assertTrue(dessin6.contains("O"));   // Tête à 6 erreurs

        assertTrue(dessin6.contains("/ \\")); // Jambes à 6 erreurs
        assertFalse(dessin3.contains("/ \\")); // Pas de jambes à 3 erreurs
    }
}