package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage table = new TableStockage(3, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        assertEquals(4, modifTable.nbColonnes);
        assertEquals(5, modifTable.nbLignes);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.ajouterColonne("Test", String.class);
        assertEquals(4, result.getColumnCount());
        assertEquals("Test", result.getColumnName(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithInvalidType() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.ajouterColonne("Test", Integer.class);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.ajouterLigne();
        assertEquals(4, result.getRowCount());
        assertEquals("", result.getValueAt(3, 0).toString());
        assertEquals(0.0, result.getValueAt(3, 1), 0.001);
        assertEquals(false, result.getValueAt(3, 2));
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneWithoutColumns() {
        TableStockage table = new TableStockage(0, 0);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage table = new TableStockage(3, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.ajouterColonne("Test1", String.class);
        modifTable.ajouterColonne("Test2", Double.class);
        TableStockage result = modifTable.supprimerColonne(1);
        assertEquals(3, result.getColumnCount());
        assertNull(result.getColumnName(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneWithInvalidIndex() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.supprimerColonne(4);
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.ajouterLigne();
        modifTable.ajouterLigne();
        TableStockage result = modifTable.supprimerLigne(1);
        assertEquals(2, result.getRowCount());
        assertEquals(0.0, result.getValueAt(0, 0), 0.001);
        assertEquals("", result.getValueAt(0, 1).toString());
        assertEquals(false, result.getValueAt(0, 2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneWithInvalidIndex() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.ajouterLigne();
        modifTable.ajouterLigne();
        modifTable.supprimerLigne(3);
    }

    @Test
    public void testSeparerColonne() {
        TableStockage table = new TableStockage(3, 1);
        table.setValueAt("Test 1 Test 2", 0, 0);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.separerColonne(0);
        assertEquals(2, result.getColumnCount());
        assertEquals("Test 1", result.getColumnName(0));
        assertEquals("Test 2", result.getColumnName(1));
        assertEquals("Test 1", result.getValueAt(0, 0).toString());
        assertEquals("Test 2", result.getValueAt(0, 1).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneWithInvalidIndex() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.separerColonne(3);
    }

    @Test
    public void testSeparerColonneWithEmptyString() {
        TableStockage table = new TableStockage(3, 1);
        table.setValueAt("", 0, 0);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.separerColonne(0);
        assertEquals(1, result.getColumnCount());
        assertEquals("", result.getColumnName(0));
        assertEquals("", result.getValueAt(0, 0).toString());
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage table = new TableStockage(3, 2);
        table.setColumnName(0, "Col1");
        table.setColumnName(1, "Col2");
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.fusionnerColonnes(0);
        assertEquals(2, result.getColumnCount());
        assertEquals("Col1 Col2", result.getColumnName(0));
        assertEquals(table.getValueAt(0, 0).toString(), result.getValueAt(0, 0).toString());
        assertEquals(table.getValueAt(0, 1).toString(), result.getValueAt(0, 1).toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testFusionnerColonnesWithInvalidIndex() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.fusionnerColonnes(3);
    }

    @Test
    public void testColonneMail() {
        TableStockage table = new TableStockage(2, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.colonneMail();
        assertEquals(4, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(3));
        assertTrue(result.getValueAt(0, 3) instanceof Boolean);
        assertTrue(result.getValueAt(1, 3) instanceof Boolean);
    }

    @Test
    public void testSelectMail() {
        TableStockage table = new TableStockage(3, 4);
        table.setValueAt(true, 0, 3);
        table.setValueAt(true, 1, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.selectMail();
        assertEquals(2, result.getRowCount());
        assertEquals(3, result.getColumnCount());
        assertTrue(result.getValueAt(0, 0) instanceof Object);
        assertEquals("", result.getValueAt(0, 1).toString());
        assertEquals(0.0, result.getValueAt(0, 2), 0.001);
        assertFalse(result.getValueAt(0, 3) instanceof Boolean);
        assertEquals("", result.getValueAt(1, 1).toString());
        assertEquals(1.0, result.getValueAt(1, 2), 0.001);
        assertTrue(result.getValueAt(1, 3) instanceof Boolean);
    }

    @Test
    public void testSelectMailWithNoBooleanColumn() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.selectMail();
        assertEquals(3, result.getRowCount());
        assertEquals(3, result.getColumnCount());
        assertNull(result.getValueAt(0, 3));
        assertNull(result.getValueAt(1, 3));
        assertNull(result.getValueAt(2, 3));
    }
}