package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage table = new TableStockage(2, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        assertEquals(table.getRowCount() + 1, modifTable.getNbLignes());
        assertEquals(table.getColumnCount(), modifTable.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage table = new TableStockage(2, 2);
        TableModel model = new Object[][]{{"1", "2"}, {"3", "4"}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.ajouterColonne("Test", String.class);
        assertEquals(table.getColumnCount() + 1, result.getColumnCount());
        assertEquals("Test", result.getColumnName(result.getColumnCount() - 1));
        assertArrayEquals(new Object[]{"1", "2", ""}, result.getColumnValues(0));
        assertArrayEquals(new Object[]{""}, result.getColumnValues(1));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage table = new TableStockage(2, 2);
        TableModel model = new Object[][]{{"1", "2"}, {"3", "4"}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.ajouterLigne();
        assertEquals(table.getRowCount() + 1, result.getRowCount());
        assertEquals(table.getColumnCount(), result.getColumnCount());
        assertArrayEquals(new Object[]{"", "", ""}, result.getColumnValues(result.getRowCount() - 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterLigneWhenNoColumns() {
        TableStockage table = new TableStockage(2, 0);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage table = new TableStockage(2, 3);
        TableModel model = new Object[][]{{"1", "2", "3"}, {"4", "5", "6"}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.supprimerColonne(1);
        assertEquals(table.getColumnCount() - 1, result.getColumnCount());
        assertEquals("2", result.getColumnName(0));
        assertArrayEquals(new Object[]{"1", ""}, result.getColumnValues(0));
        assertArrayEquals(new Object[]{"", ""}, result.getColumnValues(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneWhenLastColumn() {
        TableStockage table = new TableStockage(2, 2);
        TableModel model = new Object[][]{{"1", "2"}, {"3", "4"}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.supprimerColonne(2);
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage table = new TableStockage(2, 2);
        TableModel model = new Object[][]{{"1", "2"}, {"3", "4"}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.supprimerLigne(0);
        assertEquals(table.getRowCount() - 1, result.getRowCount());
        assertEquals("1", result.getColumnName(0));
        assertArrayEquals(new Object[]{"3", "4"}, result.getColumnValues(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneWhenLastLine() {
        TableStockage table = new TableStockage(2, 2);
        TableModel model = new Object[][]{{"1", "2"}, {"3", "4"}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.supprimerLigne(2);
    }

    @Test
    public void testSeparerColonne() {
        TableStockage table = new TableStockage(2, 1);
        TableModel model = new Object[][]{{"1 2"}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.separerColonne(0);
        assertEquals(2, result.getColumnCount());
        assertEquals("1", result.getColumnName(0));
        assertEquals("2", result.getColumnName(1));
        assertArrayEquals(new Object[]{"1", ""}, result.getColumnValues(0));
        assertArrayEquals(new Object[]{"", ""}, result.getColumnValues(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneWhenNoSpace() {
        TableStockage table = new TableStockage(2, 1);
        TableModel model = new Object[][]{{"12"}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.separerColonne(0);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage table = new TableStockage(2, 2);
        TableModel model = new Object[][]{{new Boolean(true), new Boolean(false)},
                {new Boolean(false), new Boolean(true)}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.fusionnerColonnes(0);
        assertEquals("Envoyer E-Mail a: false Envoyer E-Mail a:", result.getColumnName(0));
        assertEquals("Envoyer E-Mail a:", result.getColumnName(1));
        assertArrayEquals(new Object[]{new Boolean(true), new Boolean(false)}, result.getColumnValues(0));
        assertArrayEquals(new Object[]{new Boolean(false), new Boolean(true)}, result.getColumnValues(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesWhenNotBoolean() {
        TableStockage table = new TableStockage(2, 2);
        TableModel model = new Object[][]{{"1", "2"}, {"3", "4"}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.fusionnerColonnes(0);
    }

    @Test
    public void testColonneMail() {
        TableStockage table = new TableStockage(2, 1);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.colonneMail();
        assertEquals("Envoyer E-Mail a:", result.getColumnName(result.getColumnCount() - 1));
        assertEquals(Boolean.class, result.getColumnClass(result.getColumnCount() - 1));
        assertArrayEquals(new Object[]{new Boolean(true), new Boolean(true)}, result.getColumnValues(0));
    }

    @Test
    public void testSelectMail() {
        TableStockage table = new TableStockage(3, 3);
        TableModel model = new Object[][]{{new Boolean(true), "John Doe", new Double(12.3)},
                {new Boolean(false), "Jane Doe", new Double(45.6)},
                {new Boolean(true), "Bob Smith", new Double(78.9)}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.selectMail();
        assertEquals(2, result.getRowCount());
        assertEquals("John Doe", result.getColumnName(0));
        assertEquals("Jane Doe", result.getColumnName(1));
        assertEquals("Jane Doe", result.getValueAt(0, 0).toString());
        assertEquals("Bob Smith", result.getValueAt(1, 0).toString());
    }

    @Test
    public void testSelectMailWhenNoEmailAddress() {
        TableStockage table = new TableStockage(3, 3);
        TableModel model = new Object[][]{{new Boolean(true), "John Doe", new Double(12.3)},
                {new Boolean(false), "Jane Doe", new Double(45.6)},
                {new Boolean(true), "Bob Smith", new Double(78.9)}};
        table.setModel(model);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.selectMail();
        assertEquals(3, result.getRowCount());
        assertEquals("John Doe", result.getValueAt(0, 0).toString());
        assertEquals("Jane Doe", result.getValueAt(1, 0).toString());
        assertEquals("Bob Smith", result.getValueAt(2, 0).toString());
    }
}