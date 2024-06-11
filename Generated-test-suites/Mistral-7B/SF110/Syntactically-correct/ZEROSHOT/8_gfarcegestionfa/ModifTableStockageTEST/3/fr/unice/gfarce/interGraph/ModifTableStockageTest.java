package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private JTable table;
    private TableModel originalTableModel;

    @Before
    public void setUp() {
        Object[][] data = {{"A1", "1"}, {"B1", "2"}, {"C1", "3.0"}};
        String[] columns = {"Colonne1", "Colonne2"};
        originalTableModel = new DefaultTableModel(data, columns);
        table = new JTable(originalTableModel);
        modifTableStockage = new ModifTableStockage(new TableStockage(table));
    }

    @Test
    public void testModifTableStockageConstruction() {
        assertNotNull(modifTableStockage);
    }

    @Test
    public void testGetNbColonnes() {
        assertEquals(2, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testGetNbLignes() {
        assertEquals(3, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        String newColumnName = "NewColumnName";
        Class<?> newColumnType = String.class;
        TableStockage newTable = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
        assertEquals(3, newTable.getNbColonnes());
        assertEquals(newColumnName, newTable.getColumnName(2));
        assertArrayEquals(new Object[]{"A1", "1", "", ""}, newTable.getRowData(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneNullColumnName() {
        modifTableStockage.ajouterColonne(null, Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneNullColumnType() {
        modifTableStockage.ajouterColonne("NewColumnName", null);
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterColonneNoSpaceLeft() {
        for (int i = 0; i < 10; i++) {
            modifTableStockage.ajouterColonne("NewColumn" + i, Integer.class);
        }
        modifTableStockage.ajouterColonne("NewColumn11", Integer.class);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage newTable = modifTableStockage.ajouterLigne();
        assertEquals(4, newTable.getNbLignes());
        assertNull(newTable.getValueAt(3, 0));
        assertNull(newTable.getValueAt(3, 1));
        assertTrue(newTable.getValueAt(3, 2) instanceof String);
        assertEquals("", (String)newTable.getValueAt(3, 2));
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneNoSpaceLeft() {
        for (int i = 0; i < 10; i++) {
            modifTableStockage.ajouterLigne();
        }
        modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        int columnToRemoveIndex = 1;
        TableStockage newTable = modifTableStockage.supprimerColonne(columnToRemoveIndex);
        assertEquals(2, newTable.getNbColonnes());
        assertNull(newTable.getValueAt(0, columnToRemoveIndex));
        assertTrue(newTable.getValueAt(0, 1) instanceof String);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneInvalidIndex() {
        modifTableStockage.supprimerColonne(3);
    }

    @Test
    public void testSupprimerLigne() {
        int rowToRemoveIndex = 1;
        TableStockage newTable = modifTableStockage.supprimerLigne(rowToRemoveIndex);
        assertEquals(2, newTable.getNbLignes());
        assertNull(newTable.getValueAt(rowToRemoveIndex, 0));
        assertNull(newTable.getValueAt(rowToRemoveIndex, 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneInvalidIndex() {
        modifTableStockage.supprimerLigne(3);
    }

    @Test
    public void testSeparerColonne() {
        int columnToSplitIndex = 0;
        TableStockage newTable = modifTableStockage.separerColonne(columnToSplitIndex);
        assertEquals(4, newTable.getNbColonnes());
        assertEquals("Colonne1", newTable.getColumnName(0));
        assertEquals("Colonne2", newTable.getColumnName(1));
        assertEquals("", (String)newTable.getValueAt(0, 2));
        assertEquals("", (String)newTable.getValueAt(0, 3));
        assertArrayEquals(new Object[]{"A1", "1", "", ""}, newTable.getRowData(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSeparerColonneInvalidIndex() {
        modifTableStockage.separerColonne(3);
    }

    @Test
    public void testSeparerColonneInvalidColumnContent() {
        modifTableStockage.separerColonne(1);
    }

    @Test
    public void testFusionnerColonnes() {
        int columnToMergeIndex = 1;
        TableStockage newTable = modifTableStockage.fusionnerColonnes(columnToMergeIndex);
        assertEquals(2, newTable.getNbColonnes());
        assertEquals("Colonne1 Colonne2", newTable.getColumnName(0));
        assertEquals("", newTable.getColumnName(1));
        assertArrayEquals(new Object[]{"A1 1", "B1 2", "C1 3.0"}, newTable.getRowData(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFusionnerColonnesInvalidIndex() {
        modifTableStockage.fusionnerColonnes(3);
    }

    @Test
    public void testGetTable() {
        TableStockage newTable = modifTableStockage.getTable();
        assertEquals(originalTableModel, newTable.getModel());
    }

    @Test
    public void testColonneMail() {
        TableStockage newTable = modifTableStockage.colonneMail();
        assertEquals(4, newTable.getNbColonnes());
        assertArrayEquals(new Object[]{"Colonne1", "Colonne2", "Envoyer E-Mail a:", true}, newTable.getRowData(0));
    }

    @Test
    public void testSelectMail() {
        TableCellRenderer renderer = table.getDefaultRenderer(Boolean.class);
        TableColumnModel columnModel = table.getColumnModel();
        int columnToSelectIndex = columnModel.getColumnIndex("Envoyer E-Mail a:");
        JTable selectedTable = modifTableStockage.selectMail();
        assertEquals(3, selectedTable.getModel().getRowCount());
        for (int row = 0; row < selectedTable.getModel().getRowCount(); row++) {
            Component component = table.prepareRenderer(renderer, 0, row, false);
            assertTrue(component.isOpaque());
            assertFalse(renderer.getTableCellRendererComponent(selectedTable, selectedTable.getValueAt(row, columnToSelectIndex), false, false, row, columnToSelectIndex).isSelected());
        }
    }
}