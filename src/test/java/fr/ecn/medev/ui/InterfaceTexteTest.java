package fr.ecn.medev.ui;

import fr.ecn.medev.service.DictionnaireService;
import fr.ecn.medev.service.GestionnairePartie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour InterfaceTexte avec simulation IO
 * @author Oussama Kazoubi
 */
class InterfaceTexteTest {

    private DictionnaireService dictionnaire;
    private GestionnairePartie gestionnaire;
    private InterfaceTexte interfaceTexte;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        dictionnaire = new DictionnaireService();
        dictionnaire.ajouterMot("JAVA");
        dictionnaire.ajouterMot("PYTHON");
        dictionnaire.ajouterMot("RUBY");
        gestionnaire = new GestionnairePartie(dictionnaire, 6);

        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Construction de l'interface")
    void testConstruction() {
        interfaceTexte = new InterfaceTexte(gestionnaire);
        assertNotNull(interfaceTexte);
    }

    @Test
    @DisplayName("Menu principal - choix quitter")
    void testMenuQuitter() {
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("JEU DU PENDU") || output.contains("MENU"));
    }

    @Test
    @DisplayName("Menu principal - choix invalide puis quitter")
    void testMenuChoixInvalide() {
        String input = "5\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("invalide"));
    }

    @Test
    @DisplayName("Mode 2 joueurs - mot vide")
    void testMode2JoueursMotVide() {
        String input = "2\n\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("vide"));
    }

    @Test
    @DisplayName("Mode 2 joueurs - victoire simple")
    void testMode2JoueursVictoire() {
        // Mot JAVA: J, A, V suffisent pour gagner
        String input = "2\nJAVA\nJ\nA\nV\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("LICITATIONS") || output.contains("gagne"));
    }

    @Test
    @DisplayName("Mode 2 joueurs - defaite complete")
    void testMode2JoueursDefaite() {
        // 6 erreurs pour perdre
        String input = "2\nJAVA\nZ\nX\nW\nQ\nB\nC\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("PERDU"));
    }


    @Test
    @DisplayName("Saisie multiple caracteres")
    void testSaisieMultipleCaracteres() {
        // ABC puis vraies lettres
        String input = "2\nJAVA\nABC\nJ\nA\nV\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("seule"));
    }

    @Test
    @DisplayName("Caractere invalide")
    void testCaractereInvalide() {
        // Chiffre puis vraies lettres
        String input = "2\nJAVA\n1\nJ\nA\nV\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("lettres") || output.contains("alphabet"));
    }

    @Test
    @DisplayName("Affichage etat partie")
    void testAffichageEtatPartie() {
        String input = "2\nJAVA\nJ\nA\nV\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("Mot :"));
        assertTrue(output.contains("Erreurs :"));
    }

    @Test
    @DisplayName("Affichage lettres proposees")
    void testAffichageLettresProposees() {
        // Propose J et Z
        String input = "2\nJAVA\nJ\nZ\nA\nV\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("Lettres") || output.contains("propose"));
    }

    @Test
    @DisplayName("Lettre presente - message positif")
    void testLettrePresente() {
        String input = "2\nJAVA\nJ\nA\nV\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("Bien") || output.contains("presente"));
    }

    @Test
    @DisplayName("Lettre absente - message negatif")
    void testLettreAbsente() {
        String input = "2\nJAVA\nZ\nJ\nA\nV\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("Dommage") || output.contains("pas"));
    }

    @Test
    @DisplayName("Affichage pendu ASCII")
    void testAffichagePendu() {
        String input = "2\nJAVA\nZ\nJ\nA\nV\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("+---+") || output.contains("==="));
    }

    @Test
    @DisplayName("Message victoire contient mot")
    void testMessageVictoireMot() {
        String input = "2\nJAVA\nJ\nA\nV\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("JAVA"));
    }

    @Test
    @DisplayName("Message defaite contient mot")
    void testMessageDefaiteMot() {
        String input = "2\nJAVA\nZ\nX\nW\nQ\nB\nC\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("JAVA"));
    }

    @Test
    @DisplayName("Fermeture scanner")
    void testFermerScanner() {
        interfaceTexte = new InterfaceTexte(gestionnaire);
        assertDoesNotThrow(() -> interfaceTexte.fermer());
    }

    @Test
    @DisplayName("Mode 1 joueur avec dictionnaire vide")
    void testMode1JoueurDictionnaireVide() {
        DictionnaireService dictoVide = new DictionnaireService();
        GestionnairePartie gest = new GestionnairePartie(dictoVide, 6);

        String input = "1\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        InterfaceTexte inter = new InterfaceTexte(gest);
        inter.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("Erreur") || output.contains("vide"));
    }

    @Test
    @DisplayName("Menu affiche toutes options")
    void testMenuOptions() {
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
        assertTrue(output.contains("3."));
    }

    @Test
    @DisplayName("Mode 2 joueurs enregistre mot")
    void testMode2JoueursEnregistreMot() {
        String input = "2\nORDINATEUR\nO\nR\nD\nI\nN\nA\nT\nE\nU\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        interfaceTexte = new InterfaceTexte(gestionnaire);
        interfaceTexte.afficherMenuPrincipal();

        String output = outContent.toString();
        assertTrue(output.contains("ORDINATEUR") || output.contains("LICITATIONS"));
    }
}