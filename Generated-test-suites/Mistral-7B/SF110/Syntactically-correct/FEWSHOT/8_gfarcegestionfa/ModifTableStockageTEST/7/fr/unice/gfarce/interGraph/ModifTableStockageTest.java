package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.swing.table.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage tableToModify = new TableStockage(2, 2);
        ModifTableStockage modifTable = new ModifTableStockage(tableToModify);
        assertEquals(2, modifTable.getNbColonnes());
        assertEquals(2, modifTable.getNbLignes());
        assertEquals(tableToModify, modifTable.getTableStockage());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage initialTable = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);

        String newColumnName = "TestColumn";
        Class<?> newColumnType = Integer.class;

        TableStockage newTable = modifTable.ajouterColonne(newColumnName, newColumnType);

        assertEquals(4, newTable.getColumnCount());
        assertEquals(3, newTable.getRowCount());
        assertEquals(newColumnName, newTable.getColumnName(3));
        assertEquals(newColumnType, newTable.getColumnClass(3));

        TableModel originalTableModel = initialTable.getColumnModel();
        TableModel newTableModel = newTable.getColumnModel();

        assertEquals(originalTableModel.getColumnCount(), newTableModel.getColumnCount());
        assertNotSame(originalTableModel, newTableModel);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage initialTable = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);

        TableStockage newTable = modifTable.ajouterLigne();

        assertEquals(3, newTable.getColumnCount());
        assertEquals(4, newTable.getRowCount());
        assertNull(newTable.getValueAt(2, 0));
        assertNull(newTable.getValueAt(2, 1));
        assertNull(newTable.getValueAt(2, 2));
        assertFalse(newTable.isCellEditable(2, 0));
        assertFalse(newTable.isCellEditable(2, 1));
        assertFalse(newTable.isCellEditable(2, 2));

        TableModel originalTableModel = initialTable.getColumnModel();
        TableModel newTableModel = newTable.getColumnModel();

        assertEquals(originalTableModel.getColumnCount(), newTableModel.getColumnCount());
        assertNotSame(originalTableModel, newTableModel);
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage initialTable = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        int columnToRemoveIndex = 1;

        TableStockage newTable = modifTable.supprimerColonne(columnToRemoveIndex);

        assertEquals(2, newTable.getColumnCount());
        assertEquals(3, newTable.getRowCount());
        assertNull(newTable.getValueAt(0, 0));
        assertNull(newTable.getValueAt(0, 1));
        assertNull(newTable.getValueAt(0, 2));
        assertFalse(newTable.isCellEditable(0, 0));
        assertFalse(newTable.isCellEditable(0, 1));
        assertFalse(newTable.isCellEditable(0, 2));

        TableModel originalTableModel = initialTable.getColumnModel();
        TableModel newTableModel = newTable.getColumnModel();

        assertEquals(originalTableModel.getColumnCount(), newTableModel.getColumnCount());
        assertNotSame(originalTableModel, newTableModel);
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage initialTable = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        int lineToRemoveIndex = 1;

        TableStockage newTable = modifTable.supprimerLigne(lineToRemoveIndex);

        assertEquals(3, newTable.getColumnCount());
        assertEquals(2, newTable.getRowCount());
        assertNull(newTable.getValueAt(0, 0));
        assertNull(newTable.getValueAt(0, 1));
        assertNull(newTable.getValueAt(0, 2));
        assertFalse(newTable.isCellEditable(0, 0));
        assertFalse(newTable.isCellEditable(0, 1));
        assertFalse(newTable.isCellEditable(0, 2));

        TableModel originalTableModel = initialTable.getColumnModel();
        TableModel newTableModel = newTable.getColumnModel();

        assertEquals(originalTableModel.getColumnCount(), newTableModel.getColumnCount());
        assertNotSame(originalTableModel, newTableModel);
    }

    @Test
    public void testSeparerColonne() {
        TableStockage initialTable = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        int columnToSeparateIndex = 1;

        TableStockage newTable = modifTable.separerColonne(columnToSeparateIndex);

        assertEquals(4, newTable.getColumnCount());
        assertEquals(3, newTable.getRowCount());
        assertNull(newTable.getValueAt(0, 0));
        assertNull(newTable.getValueAt(0, 1));
        assertNull(newTable.getValueAt(0, 2));
        assertNull(newTable.getValueAt(0, 3));
        assertNull(newTable.getValueAt(0, 4));

        TableModel originalTableModel = initialTable.getColumnModel();
        TableModel newTableModel = newTable.getColumnModel();

        assertEquals(originalTableModel.getColumnCount(), newTableModel.getColumnCount());
        assertNotSame(originalTableModel, newTableModel);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage initialTable = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        int columnToMergeIndex = 1;

        TableStockage newTable = modifTable.fusionnerColonnes(columnToMergeIndex);

        assertEquals(3, newTable.getColumnCount());
        assertEquals(3, newTable.getRowCount());
        assertNull(newTable.getValueAt(0, 1));
        assertEquals("Envoyer E-Mail a:  Colonne2", newTable.getColumnName(1));

        TableModel originalTableModel = initialTable.getColumnModel();
        TableModel newTableModel = newTable.getColumnModel();

        assertEquals(originalTableModel.getColumnCount(), newTableModel.getColumnCount());
        assertNotSame(originalTableModel, newTableModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testColonneMailWithEmptyTable() {
        TableStockage initialTable = new TableStockage(0, 0);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        modifTable.colonneMail();
    }

    @Test
    public void testColonneMail() {
        TableStockage initialTable = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);

        TableStockage newTable = modifTable.colonneMail();

        assertEquals(4, newTable.getColumnCount());
        assertEquals(3, newTable.getRowCount());
        assertEquals("Envoyer E-Mail a:", newTable.getColumnName(3));
        assertEquals(Boolean.class, newTable.getColumnClass(3));
        assertNull(newTable.getValueAt(0, 3));
        assertTrue(newTable.isCellEditable(0, 3));
    }

    @Test
    public void testSelectMail() {
        TableStockage initialTable = new TableStockage(4, 4);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);

        String columnToSelect = "Envoyer E-Mail a:";

        TableStockage newTable = modifTable.selectMail();

        assertEquals(3, newTable.getColumnCount());
        assertEquals(3, newTable.getRowCount());
        assertEquals(columnToSelect, newTable.getColumnName(0));
        assertFalse(newTable.isCellEditable(0, 0));

        Object[] newTableData = new Object[3];
        for (int i = 0; i < 3; i++) {
            newTableData[i] = new Boolean(true);
        }

        assertArrayEquals(newTableData, newTable.getRowData());
    }
}