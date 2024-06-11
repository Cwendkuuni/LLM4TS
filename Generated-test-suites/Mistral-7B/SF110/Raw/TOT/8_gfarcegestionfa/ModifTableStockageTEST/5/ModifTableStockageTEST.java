package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    @Test
    public void testConstructor() {
        TableStockage originalTable = new TableStockage(5, 3);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        assertEquals(originalTable.getRowCount(), newTable.getNbLignes());
        assertEquals(originalTable.getColumnCount(), newTable.getNbColonnes());
    }

    @Test
    public void testAjouterColonneStringClass() {
        TableStockage originalTable = new TableStockage(3, 2);
        String newColumnName = "NewColumnName";
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        TableStockage resultTable = newTable.ajouterColonne(newColumnName, String.class);
        assertEquals(originalTable.getRowCount(), resultTable.getRowCount());
        assertEquals(originalTable.getColumnCount() + 1, resultTable.getColumnCount());
        assertEquals(newColumnName, resultTable.getColumnName(resultTable.getColumnCount() - 1));
    }

    @Test
    public void testAjouterColonneWrongType() {
        TableStockage originalTable = new TableStockage(3, 2);
        String newColumnName = "NewColumnName";
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        try {
            newTable.ajouterColonne(newColumnName, Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage originalTable = new TableStockage(3, 2);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        TableStockage resultTable = newTable.ajouterLigne();
        assertEquals(originalTable.getRowCount() + 1, resultTable.getRowCount());
        assertEquals(originalTable.getColumnCount(), resultTable.getColumnCount());
        for (int i = 0; i < originalTable.getRowCount(); ++i) {
            for (int j = 0; j < originalTable.getColumnCount(); ++j) {
                assertEquals(originalTable.getValueAt(i, j), resultTable.getValueAt(i, j));
            }
        }
        for (int j = 0; j < originalTable.getColumnCount(); ++j) {
            if (originalTable.getColumnClass(j).equals(String.class)) {
                assertEquals("", resultTable.getValueAt(originalTable.getRowCount() - 1, j));
            } else if (originalTable.getColumnClass(j).equals(Double.class)) {
                assertEquals(0.0, resultTable.getValueAt(originalTable.getRowCount() - 1, j), 0.001);
            } else {
                assertEquals(false, resultTable.getValueAt(originalTable.getRowCount() - 1, j));
            }
        }
    }

    @Test
    public void testSupprimerColonneValidIndex() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        TableStockage resultTable = newTable.supprimerColonne(1);
        assertEquals(originalTable.getRowCount(), resultTable.getRowCount());
        assertEquals(originalTable.getColumnCount() - 1, resultTable.getColumnCount());
        for (int i = 0; i < originalTable.getRowCount(); ++i) {
            for (int j = 0; j < originalTable.getColumnCount() - 1; ++j) {
                assertEquals(originalTable.getValueAt(i, j), resultTable.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerColonneInvalidIndex() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        try {
            newTable.supprimerColonne(originalTable.getColumnCount());
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSupprimerLigneValidIndex() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        TableStockage resultTable = newTable.supprimerLigne(1);
        assertEquals(originalTable.getRowCount() - 1, resultTable.getRowCount());
        assertEquals(originalTable.getColumnCount(), resultTable.getColumnCount());
        for (int i = 0; i < originalTable.getRowCount() - 1; ++i) {
            for (int j = 0; j < originalTable.getColumnCount(); ++j) {
                assertEquals(originalTable.getValueAt(i, j), resultTable.getValueAt(i, j));
            }
        }
        for (int i = 0; i < originalTable.getRowCount() - 1; ++i) {
            for (int j = 0; j < originalTable.getColumnCount(); ++j) {
                if (i > 0) {
                    assertEquals(originalTable.getValueAt(i - 1, j), resultTable.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSupprimerLigneInvalidIndex() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        try {
            newTable.supprimerLigne(originalTable.getRowCount());
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSeparerColonneValidIndex() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        newTable.setColumnClass(1, String.class);
        String columnValue = "ColumnValue 1 ColumnValue 2";
        newTable.setValueAt(columnValue, 0, 1);
        TableStockage resultTable = newTable.separerColonne(1);
        assertEquals(originalTable.getRowCount(), resultTable.getRowCount());
        assertEquals(originalTable.getColumnCount() + 1, resultTable.getColumnCount());
        assertEquals(originalTable.getValueAt(0, 0), resultTable.getValueAt(0, 0));
        assertEquals(columnValue.split(" ")[0], resultTable.getValueAt(0, resultTable.getColumnCount() - 2).toString());
        assertEquals(columnValue.split(" ")[1], resultTable.getValueAt(0, resultTable.getColumnCount() - 1).toString());
    }

    @Test
    public void testSeparerColonneInvalidIndex() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        try {
            newTable.separerColonne(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testFusionnerColonnesValidIndices() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        newTable.setColumnClass(1, Boolean.class);
        newTable.setColumnClass(2, Boolean.class);
        newTable.setValueAt(true, 0, 1);
        newTable.setValueAt(true, 0, 2);
        TableStockage resultTable = newTable.fusionnerColonnes(1);
        assertEquals(originalTable.getRowCount(), resultTable.getRowCount());
        assertEquals(originalTable.getColumnCount() - 1, resultTable.getColumnCount());
        assertEquals(originalTable.getValueAt(0, 0), resultTable.getValueAt(0, 0));
        assertEquals("true true", resultTable.getValueAt(0, 1).toString());
    }

    @Test
    public void testFusionnerColonnesInvalidIndices() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        try {
            newTable.fusionnerColonnes(1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage originalTable = new TableStockage(3, 2);
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        TableStockage resultTable = newTable.colonneMail();
        assertEquals(originalTable.getRowCount(), resultTable.getRowCount());
        assertEquals(originalTable.getColumnCount() + 1, resultTable.getColumnCount());
        assertEquals("Envoyer E-Mail a:", resultTable.getColumnName(resultTable.getColumnCount() - 1));
        for (int i = 0; i < originalTable.getRowCount(); ++i) {
            resultTable.setValueAt(true, i, resultTable.getColumnCount() - 1);
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage originalTable = new TableStockage(3, 3);
        originalTable.setColumnName(1, "Envoyer E-Mail a:");
        ModifTableStockage newTable = new ModifTableStockage(originalTable);
        newTable.setValueAt(true, 0, 1);
        newTable.setValueAt(true, 1, 1);
        newTable.setValueAt(false, 2, 1);
        TableStockage resultTable = newTable.selectMail();
        assertEquals(2, resultTable.getRowCount());
        assertEquals(originalTable.getColumnCount(), resultTable.getColumnCount());
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < originalTable.getColumnCount(); ++j) {
                if (i == 0) {
                    assertEquals(originalTable.getValueAt(0, j), resultTable.getValueAt(i, j));
                } else {
                    assertEquals(originalTable.getValueAt(1, j), resultTable.getValueAt(i, j));
                }
            }
        }
    }

}