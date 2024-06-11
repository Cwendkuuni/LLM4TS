package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.table.TableStockage;

public class ModifTableStockageTest {
    // Test for ajouterColonne

    @Test
    public void testAjouterColonneWithValidParams() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage expected = new TableStockage(3, 3);
        TableStockage actual = modifTable.ajouterColonne("newCol", String.class);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithNullTs() {
        ModifTableStockage modifTable = new ModifTableStockage(null);
        TableStockage actual = modifTable.ajouterColonne("newCol", String.class);
        fail("Expected IllegalArgumentException, but got: " + actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithEmptyStringForNomNouvelleColonne() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage actual = modifTable.ajouterColonne("", String.class);
        fail("Expected IllegalArgumentException, but got: " + actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithNullType() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage actual = modifTable.ajouterColonne("newCol", null);
        fail("Expected IllegalArgumentException, but got: " + actual);
    }

    // Test for ajouterLigne

    @Test
    public void testAjouterLigneWithEmptyTs() {
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage actual = modifTable.ajouterLigne();
        assertTrue(actual.equals(modifTable.ts));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterLigneWithNoColumns() {
        TableStockage ts = new TableStockage();
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage actual = modifTable.ajouterLigne();
        fail("Expected IllegalArgumentException, but got: " + actual);
    }

    // Test for supprimerColonne

    @Test
    public void testSupprimerColonneWithValidIndex() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage expected = new TableStockage(3, 2);
        TableStockage actual = modifTable.supprimerColonne(1);
        assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneWithNegativeIndex() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        modifTable.supprimerColonne(-1);
        fail("Expected IndexOutOfBoundsException, but got: " + modifTable.ts);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneWithIndexGreaterThanNumberOfColumns() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        modifTable.supprimerColonne(4);
        fail("Expected IndexOutOfBoundsException, but got: " + modifTable.ts);
    }

    // Test for supprimerLigne

    @Test
    public void testSupprimerLigneWithValidIndex() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage expected = new TableStockage(2, 3);
        TableStockage actual = modifTable.supprimerLigne(1);
        assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneWithNegativeIndex() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        modifTable.supprimerLigne(-1);
        fail("Expected IndexOutOfBoundsException, but got: " + modifTable.ts);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneWithIndexGreaterThanNumberOfRows() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        modifTable.supprimerLigne(4);
        fail("Expected IndexOutOfBoundsException, but got: " + modifTable.ts);
    }

    // Test for separerColonne

    @Test
    public void testSeparerColonneWithValidIndex() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("Colonne1 Colonne2", 0, 0);
        ts.setValueAt("2 3", 0, 1);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage expected = new TableStockage(3, 3);
        expected.setValueAt("Colonne1", 0, 0);
        expected.setValueAt("", 0, 1);
        expected.setValueAt("", 0, 2);
        TableStockage actual = modifTable.separerColonne(1);
        assertEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSeparerColonneWithIndexOutOfBounds() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        modifTable.separerColonne(4);
        fail("Expected IndexOutOfBoundsException, but got: " + modifTable.ts);
    }

    // Test for fusionnerColonnes

    @Test
    public void testFusionnerColonnesWithValidIndex() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setValueAt("True", 0, 0);
        ts.setValueAt("False", 0, 1);
        ts.setValueAt("True", 0, 2);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage expected = new TableStockage(3, 2);
        expected.setValueAt("True False", 0, 0);
        TableStockage actual = modifTable.fusionnerColonnes(1);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesWithInvalidIndex() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        modifTable.fusionnerColonnes(4);
        fail("Expected IllegalArgumentException, but got: " + modifTable.ts);
    }

    @Test
    public void testFusionnerColonnesWithNonBooleanColumns() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setValueAt("Colonne1", 0, 0);
        ts.setValueAt("Colonne2", 0, 1);
        ts.setValueAt("Colonne3", 0, 2);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage actual = modifTable.fusionnerColonnes(1);
        assertEquals(ts, actual);
    }

    // Test for colonneMail

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage expected = new TableStockage(3, 3);
        expected.setColumnName(2, "Envoyer E-Mail a:");
        expected.setColumnClass(2, Boolean.class);
        TableStockage actual = modifTable.colonneMail();
        assertEquals(expected, actual);
    }

    @Test
    public void testColonneMailWithNullTs() {
        ModifTableStockage modifTable = new ModifTableStockage(null);
        TableStockage actual = modifTable.colonneMail();
        assertTrue(actual.equals(modifTable.ts));
    }

    // Test for selectMail

    @Test
    public void testSelectMailWithEmptyTs() {
        TableStockage ts = new TableStockage();
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage actual = modifTable.selectMail();
        assertTrue(actual.equals(modifTable.ts));
    }

    @Test
    public void testSelectMailWithNoEnvoyerEMailAColumn() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage expected = new TableStockage(1, 2);
        expected.setValueAt(true, 0, 0);
        TableStockage actual = modifTable.selectMail();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSelectMailWithNoEnvoyerEMailAColumnAndUnselectedRows() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("Unselected", 0, 0);
        ts.setValueAt("Unselected", 0, 1);
        ModifTableStockage modifTable = new ModifTableStockage(ts);
        TableStockage actual = modifTable.selectMail();
        fail("Expected IllegalStateException, but got: " + actual);
    }
}