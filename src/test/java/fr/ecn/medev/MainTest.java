package fr.ecn.medev;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Main
 * @author Oussama Kazoubi
 */
class MainTest {

    @Test
    @DisplayName("Test simple - vérification que JUnit fonctionne")
    void testJUnitSetup() {
        assertTrue(true);
        assertEquals(2, 1 + 1);
    }

    @Test
    @DisplayName("Test arithmétique basique")
    void testBasicArithmetic() {
        assertEquals(4, 2 + 2);
        assertEquals(10, 5 * 2);
        assertNotEquals(5, 2 + 2);
    }

    @Test
    @DisplayName("Test de boucle - vérification compteur")
    void testLoopCounter() {
        int expectedIterations = 5;
        int counter = 0;

        for (int i = 1; i <= 5; i++) {
            counter++;
        }

        assertEquals(expectedIterations, counter);
    }

    @Test
    @DisplayName("Test chaînes de caractères")
    void testStringOperations() {
        String greeting = "Hello and welcome!";

        assertNotNull(greeting);
        assertTrue(greeting.contains("Hello"));
        assertTrue(greeting.contains("welcome"));
        assertEquals(18, greeting.length());
    }
}