package fr.ecn.medev.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour DictionnaireService
 * @author Oussama Kazoubi
 */
class DictionnaireServiceTest {

    private DictionnaireService dictionnaire;

    @TempDir
    Path dossierTemp;

    @BeforeEach
    void setUp() {
        dictionnaire = new DictionnaireService();
    }

    @Test
    @DisplayName("Dictionnaire vide au départ")
    void testDictionnaireVideAuDepart() {
        assertTrue(dictionnaire.estVide());
        assertEquals(0, dictionnaire.getNombreMots());
    }

    @Test
    @DisplayName("Ajouter un mot valide")
    void testAjouterMotValide() {
        dictionnaire.ajouterMot("PENDU");

        assertFalse(dictionnaire.estVide());
        assertEquals(1, dictionnaire.getNombreMots());
        assertTrue(dictionnaire.getMots().contains("PENDU"));
    }

    @Test
    @DisplayName("Ajouter mot null doit lever exception")
    void testAjouterMotNull() {
        assertThrows(IllegalArgumentException.class, () -> dictionnaire.ajouterMot(null));
    }

    @Test
    @DisplayName("Ajouter mot vide doit lever exception")
    void testAjouterMotVide() {
        assertThrows(IllegalArgumentException.class, () -> dictionnaire.ajouterMot(""));
        assertThrows(IllegalArgumentException.class, () -> dictionnaire.ajouterMot("   "));
    }

    @Test
    @DisplayName("Ajouter mot avec chiffres doit lever exception")
    void testAjouterMotAvecChiffres() {
        assertThrows(IllegalArgumentException.class, () -> dictionnaire.ajouterMot("TEST123"));
    }

    @Test
    @DisplayName("Ajouter mot avec caractères spéciaux doit lever exception")
    void testAjouterMotAvecCaracteresSpeciaux() {
        assertThrows(IllegalArgumentException.class, () -> dictionnaire.ajouterMot("TEST-MOT"));
        assertThrows(IllegalArgumentException.class, () -> dictionnaire.ajouterMot("TEST!"));
    }

    @Test
    @DisplayName("Mot ajouté est converti en majuscules")
    void testMotConvertEnMajuscules() {
        dictionnaire.ajouterMot("pendu");

        assertTrue(dictionnaire.getMots().contains("PENDU"));
        assertFalse(dictionnaire.getMots().contains("pendu"));
    }

    @Test
    @DisplayName("Ajouter mot en double n'augmente pas la taille")
    void testAjouterMotDouble() {
        dictionnaire.ajouterMot("PENDU");
        dictionnaire.ajouterMot("PENDU");

        assertEquals(1, dictionnaire.getNombreMots());
    }

    @Test
    @DisplayName("Obtenir mot aléatoire d'un dictionnaire vide doit lever exception")
    void testMotAleatoireDictionnaireVide() {
        assertThrows(IllegalStateException.class, () -> dictionnaire.obtenirMotAleatoire());
    }

    @Test
    @DisplayName("Obtenir mot aléatoire retourne un mot du dictionnaire")
    void testObtenirMotAleatoire() {
        dictionnaire.ajouterMot("JAVA");
        dictionnaire.ajouterMot("PYTHON");
        dictionnaire.ajouterMot("RUBY");

        String mot = dictionnaire.obtenirMotAleatoire();

        assertNotNull(mot);
        assertTrue(dictionnaire.getMots().contains(mot));
    }

    @Test
    @DisplayName("Charger depuis fichier valide")
    void testChargerDepuisFichierValide() throws IOException {
        File fichier = dossierTemp.resolve("test.txt").toFile();

        try (FileWriter writer = new FileWriter(fichier)) {
            writer.write("JAVA\n");
            writer.write("PYTHON\n");
            writer.write("RUBY\n");
        }

        dictionnaire.chargerDepuisFichier(fichier.getAbsolutePath());

        assertEquals(3, dictionnaire.getNombreMots());
        assertTrue(dictionnaire.getMots().contains("JAVA"));
        assertTrue(dictionnaire.getMots().contains("PYTHON"));
        assertTrue(dictionnaire.getMots().contains("RUBY"));
    }

    @Test
    @DisplayName("Charger depuis fichier ignore lignes vides")
    void testChargerIgnoreLignesVides() throws IOException {
        File fichier = dossierTemp.resolve("test.txt").toFile();

        try (FileWriter writer = new FileWriter(fichier)) {
            writer.write("JAVA\n");
            writer.write("\n");
            writer.write("   \n");
            writer.write("PYTHON\n");
        }

        dictionnaire.chargerDepuisFichier(fichier.getAbsolutePath());

        assertEquals(2, dictionnaire.getNombreMots());
    }

    @Test
    @DisplayName("Charger depuis fichier ignore mots invalides")
    void testChargerIgnoreMotsInvalides() throws IOException {
        File fichier = dossierTemp.resolve("test.txt").toFile();

        try (FileWriter writer = new FileWriter(fichier)) {
            writer.write("JAVA\n");
            writer.write("TEST123\n");
            writer.write("MOT-INVALIDE\n");
            writer.write("PYTHON\n");
        }

        dictionnaire.chargerDepuisFichier(fichier.getAbsolutePath());

        assertEquals(2, dictionnaire.getNombreMots());
        assertTrue(dictionnaire.getMots().contains("JAVA"));
        assertTrue(dictionnaire.getMots().contains("PYTHON"));
    }

    @Test
    @DisplayName("Charger depuis fichier inexistant doit lever exception")
    void testChargerFichierInexistant() {
        assertThrows(IOException.class,
                () -> dictionnaire.chargerDepuisFichier("fichier_inexistant.txt"));
    }

    @Test
    @DisplayName("Charger depuis fichier vide doit lever exception")
    void testChargerFichierVide() throws IOException {
        File fichier = dossierTemp.resolve("vide.txt").toFile();
        fichier.createNewFile();

        assertThrows(IllegalArgumentException.class,
                () -> dictionnaire.chargerDepuisFichier(fichier.getAbsolutePath()));
    }

    @Test
    @DisplayName("Vider le dictionnaire")
    void testVider() {
        dictionnaire.ajouterMot("JAVA");
        dictionnaire.ajouterMot("PYTHON");

        assertFalse(dictionnaire.estVide());

        dictionnaire.vider();

        assertTrue(dictionnaire.estVide());
        assertEquals(0, dictionnaire.getNombreMots());
    }

    @Test
    @DisplayName("Charger remplace les mots existants")
    void testChargerRemplaceMots() throws IOException {
        dictionnaire.ajouterMot("ANCIEN");

        File fichier = dossierTemp.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(fichier)) {
            writer.write("NOUVEAU\n");
        }

        dictionnaire.chargerDepuisFichier(fichier.getAbsolutePath());

        assertEquals(1, dictionnaire.getNombreMots());
        assertTrue(dictionnaire.getMots().contains("NOUVEAU"));
        assertFalse(dictionnaire.getMots().contains("ANCIEN"));
    }
}