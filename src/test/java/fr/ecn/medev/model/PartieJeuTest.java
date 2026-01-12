package fr.ecn.medev.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe PartieJeu
 * @author Oussama Kazoubi
 */
class PartieJeuTest {

    private PartieJeu partie;

    @BeforeEach
    void setUp() {
        partie = new PartieJeu("JAVA", 6);
    }

    @Test
    @DisplayName("Création partie avec paramètres valides")
    void testCreationPartieValide() {
        assertEquals(EtatPartie.EN_COURS, partie.getEtat());
        assertEquals(0, partie.getNombreErreurs());
        assertEquals(6, partie.getErreursRestantes());
        assertEquals(6, partie.getErreursMaximales());
    }

    @Test
    @DisplayName("Erreurs maximales doit être au moins 1")
    void testErreursInvalides() {
        assertThrows(IllegalArgumentException.class, () -> new PartieJeu("TEST", 0));
        assertThrows(IllegalArgumentException.class, () -> new PartieJeu("TEST", -1));
    }

    @Test
    @DisplayName("Proposer une lettre présente dans le mot")
    void testProposerLettrePresente() {
        assertTrue(partie.proposerLettre('J'));
        assertEquals(0, partie.getNombreErreurs());
        assertEquals(EtatPartie.EN_COURS, partie.getEtat());
        assertTrue(partie.estLettreDejaProposee('J'));
    }

    @Test
    @DisplayName("Proposer une lettre absente du mot")
    void testProposerLettreAbsente() {
        assertFalse(partie.proposerLettre('Z'));
        assertEquals(1, partie.getNombreErreurs());
        assertEquals(5, partie.getErreursRestantes());
        assertTrue(partie.estLettreDejaProposee('Z'));
    }

    @Test
    @DisplayName("Lettre déjà proposée ne compte pas comme erreur supplémentaire")
    void testLettreDejaProposee() {
        partie.proposerLettre('Z');
        assertEquals(1, partie.getNombreErreurs());

        partie.proposerLettre('Z');
        assertEquals(1, partie.getNombreErreurs());
    }

    @Test
    @DisplayName("Caractère non alphabétique est rejeté")
    void testCaractereInvalide() {
        assertThrows(IllegalArgumentException.class, () -> partie.proposerLettre('1'));
        assertThrows(IllegalArgumentException.class, () -> partie.proposerLettre('!'));
        assertThrows(IllegalArgumentException.class, () -> partie.proposerLettre(' '));
    }

    @Test
    @DisplayName("Victoire quand le mot est complètement découvert")
    void testVictoire() {
        partie.proposerLettre('J');
        partie.proposerLettre('A');
        partie.proposerLettre('V');

        assertEquals(EtatPartie.GAGNEE, partie.getEtat());
    }

    @Test
    @DisplayName("Défaite quand nombre maximal d'erreurs est atteint")
    void testDefaite() {
        partie.proposerLettre('Z');
        partie.proposerLettre('X');
        partie.proposerLettre('W');
        partie.proposerLettre('Q');
        partie.proposerLettre('B');
        partie.proposerLettre('C');

        assertEquals(EtatPartie.PERDUE, partie.getEtat());
        assertEquals(6, partie.getNombreErreurs());
    }

    @Test
    @DisplayName("Impossible de jouer après partie terminée (victoire)")
    void testJouerApresVictoire() {
        partie.proposerLettre('J');
        partie.proposerLettre('A');
        partie.proposerLettre('V');

        assertThrows(IllegalStateException.class, () -> partie.proposerLettre('E'));
    }

    @Test
    @DisplayName("Impossible de jouer après partie terminée (défaite)")
    void testJouerApresDefaite() {
        partie.proposerLettre('Z');
        partie.proposerLettre('X');
        partie.proposerLettre('W');
        partie.proposerLettre('Q');
        partie.proposerLettre('B');
        partie.proposerLettre('C');

        assertThrows(IllegalStateException.class, () -> partie.proposerLettre('J'));
    }

    @Test
    @DisplayName("Lettres proposées sont enregistrées correctement")
    void testLettresProposees() {
        partie.proposerLettre('J');
        partie.proposerLettre('Z');
        partie.proposerLettre('A');

        assertEquals(3, partie.getLettresProposees().size());
        assertTrue(partie.getLettresProposees().contains('J'));
        assertTrue(partie.getLettresProposees().contains('Z'));
        assertTrue(partie.getLettresProposees().contains('A'));
    }

    @Test
    @DisplayName("Affichage du mot évolue correctement")
    void testMotAfficheEvolution() {
        assertEquals("_ _ _ _", partie.getMotAffiche());

        partie.proposerLettre('J');
        assertEquals("J _ _ _", partie.getMotAffiche());

        partie.proposerLettre('A');
        assertEquals("J A _ A", partie.getMotAffiche());

        partie.proposerLettre('V');
        assertEquals("J A V A", partie.getMotAffiche());
    }
}