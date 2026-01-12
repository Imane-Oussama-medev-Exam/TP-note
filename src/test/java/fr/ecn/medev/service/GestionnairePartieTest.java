package fr.ecn.medev.service;

import fr.ecn.medev.model.EtatPartie;
import fr.ecn.medev.model.PartieJeu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour GestionnairePartie
 * @author Imane Laasri
 */
class GestionnairePartieTest {
    
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
    @DisplayName("Création avec dictionnaire null doit lever exception")
    void testCreationDictionnaireNull() {
        assertThrows(IllegalArgumentException.class, 
            () -> new GestionnairePartie(null, 6));
    }
    
    @Test
    @DisplayName("Création avec erreurs invalides doit lever exception")
    void testCreationErreursInvalides() {
        assertThrows(IllegalArgumentException.class, 
            () -> new GestionnairePartie(dictionnaire, 0));
        assertThrows(IllegalArgumentException.class, 
            () -> new GestionnairePartie(dictionnaire, -1));
    }
    
    @Test
    @DisplayName("Pas de partie en cours au départ")
    void testPasDePartieAuDepart() {
        assertNull(gestionnaire.getPartieEnCours());
        assertFalse(gestionnaire.partieEnCours());
        assertFalse(gestionnaire.partieTerminee());
    }
    
    @Test
    @DisplayName("Démarrer partie 1 joueur")
    void testDemarrerPartieUnJoueur() {
        gestionnaire.demarrerPartieUnJoueur();
        
        assertNotNull(gestionnaire.getPartieEnCours());
        assertTrue(gestionnaire.partieEnCours());
        assertEquals(EtatPartie.EN_COURS, gestionnaire.getPartieEnCours().getEtat());
    }
    
    @Test
    @DisplayName("Démarrer partie 1 joueur avec dictionnaire vide doit lever exception")
    void testDemarrerPartieDictionnaireVide() {
        DictionnaireService dictoVide = new DictionnaireService();
        GestionnairePartie gest = new GestionnairePartie(dictoVide, 6);
        
        assertThrows(IllegalStateException.class, gest::demarrerPartieUnJoueur);
    }
    
    @Test
    @DisplayName("Mot de partie 1 joueur est dans le dictionnaire")
    void testMotPartieUnJoueurDansDictionnaire() {
        gestionnaire.demarrerPartieUnJoueur();
        
        String motSecret = gestionnaire.getPartieEnCours().getMotSecret();
        assertTrue(dictionnaire.getMots().contains(motSecret));
    }
    
    @Test
    @DisplayName("Démarrer partie 2 joueurs avec mot valide")
    void testDemarrerPartieDeuxJoueurs() {
        gestionnaire.demarrerPartieDeuxJoueurs("ORDINATEUR");
        
        assertNotNull(gestionnaire.getPartieEnCours());
        assertTrue(gestionnaire.partieEnCours());
        assertEquals("ORDINATEUR", gestionnaire.getPartieEnCours().getMotSecret());
    }
    
    @Test
    @DisplayName("Démarrer partie 2 joueurs avec mot null doit lever exception")
    void testDemarrerPartieDeuxJoueursMotNull() {
        assertThrows(IllegalArgumentException.class, 
            () -> gestionnaire.demarrerPartieDeuxJoueurs(null));
    }
    
    @Test
    @DisplayName("Démarrer partie 2 joueurs avec mot vide doit lever exception")
    void testDemarrerPartieDeuxJoueursMotVide() {
        assertThrows(IllegalArgumentException.class, 
            () -> gestionnaire.demarrerPartieDeuxJoueurs(""));
        assertThrows(IllegalArgumentException.class, 
            () -> gestionnaire.demarrerPartieDeuxJoueurs("   "));
    }
    
    @Test
    @DisplayName("Démarrer partie avec erreurs personnalisées")
    void testDemarrerPartieAvecErreursPersonnalisees() {
        gestionnaire.demarrerPartieUnJoueurAvecErreurs(10);
        
        assertNotNull(gestionnaire.getPartieEnCours());
        assertEquals(10, gestionnaire.getPartieEnCours().getErreursMaximales());
    }
    
    @Test
    @DisplayName("Proposer lettre sans partie doit lever exception")
    void testProposerLettreSansPartie() {
        assertThrows(IllegalStateException.class, 
            () -> gestionnaire.proposerLettre('A'));
    }
    
    @Test
    @DisplayName("Proposer lettre dans partie en cours")
    void testProposerLettrePartieEnCours() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");
        
        assertTrue(gestionnaire.proposerLettre('J'));
        assertTrue(gestionnaire.partieEnCours());
    }
    
    @Test
    @DisplayName("Partie terminée après victoire")
    void testPartieTermineeApresVictoire() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");
        
        gestionnaire.proposerLettre('J');
        gestionnaire.proposerLettre('A');
        gestionnaire.proposerLettre('V');
        
        assertFalse(gestionnaire.partieEnCours());
        assertTrue(gestionnaire.partieTerminee());
        assertEquals(EtatPartie.GAGNEE, gestionnaire.getPartieEnCours().getEtat());
    }
    
    @Test
    @DisplayName("Partie terminée après défaite")
    void testPartieTermineeApresDefaite() {
        gestionnaire.demarrerPartieDeuxJoueurs("JAVA");
        
        gestionnaire.proposerLettre('Z');
        gestionnaire.proposerLettre('X');
        gestionnaire.proposerLettre('W');
        gestionnaire.proposerLettre('Q');
        gestionnaire.proposerLettre('B');
        gestionnaire.proposerLettre('C');
        
        assertFalse(gestionnaire.partieEnCours());
        assertTrue(gestionnaire.partieTerminee());
        assertEquals(EtatPartie.PERDUE, gestionnaire.getPartieEnCours().getEtat());
    }
    
    @Test
    @DisplayName("Obtenir le dictionnaire")
    void testObtenirDictionnaire() {
        assertEquals(dictionnaire, gestionnaire.getDictionnaire());
    }
}