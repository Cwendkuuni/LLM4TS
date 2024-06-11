package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {

    private TableModel originalTableModel;
    private JTable originalTable;
    private ModifTableStockage modifTableStockage;

    @Before
    public void setUp() {
        // Set up original data
        Object[][] data = {{"A1", "1"}, {"B1", "2"}, {"C1", "3"}};
        String[] columnNames = {"Column 1", "Column 2"};
        originalTableModel = new DefaultTableModel(data, columnNames);
        originalTable = new JTable(originalTableModel);
        originalTable.setPreferredSize(new Dimension(640, 480));

        // Set up test object
        modifTableStockage = new ModifTableStockage(originalTableModel);
    }

    @After
    public void tearDown() {
        // Reset original data
        ((DefaultTableModel) originalTableModel).setDataVector(null, null);
    }

    @Test
    public void testModifTableStockageConstruction() {
        assertEquals(modifTableStockage.getTableStockage(), originalTableModel);
        assertEquals(modifTableStockage.getNbColonnes(), 2);
        assertEquals(modifTableStockage.getNbLignes(), 3);
    }

    @Test
    public void testAjouterColonne() {
        String newColumnName = "New Column";
        Class<?> newColumnType = String.class;
        TableStockage newTable = modifTableStockage.ajouterColonne(newColumnName, newColumnType);

        assertEquals(newTable.getNbColonnes(), 3);
        assertEquals(newTable.getColumnName(0), "Column 1");
        assertEquals(newTable.getColumnName(1), "Column 2");
        assertEquals(newTable.getColumnName(2), newColumnName);
        assertEquals(newTable.getValueAt(0, 2), null);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                assertEquals(newTable.getValueAt(i, j), originalTableModel.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage newTable = modifTableStockage.ajouterLigne();

        assertEquals(newTable.getNbColonnes(), 2);
        assertEquals(newTable.getNbLignes(), 4);

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (i == 3) {
                    if (j == 0) {
                        assertNull(newTable.getValueAt(i, j));
                    } else {
                        assertEquals(newTable.getValueAt(i, j), originalTableModel.getValueAt(i - 1, j));
                    }
                } else {
                    assertEquals(newTable.getValueAt(i, j), originalTableModel.getValueAt(i, j));
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSupprimerColonneNegativeIndex() {
        modifTableStockage.supprimerColonne(-1);
    }

    @Test
    public void testSupprimerColonneValidIndex() {
        int columnIndexToRemove = 1;
        TableStockage newTable = modifTableStockage.supprimerColonne(columnIndexToRemove);

        assertEquals(newTable.getNbColonnes(), 1);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 1; ++j) {
                assertNull(newTable.getValueAt(i, j));
            }
            for (int j = 1; j < 2; ++j) {
                assertEquals(newTable.getValueAt(i, j), originalTableModel.getValueAt(i, j - 1));
            }
        }
    }

    @Test
    public void testSupprimerLigneNegativeIndex() {
        modifTableStockage.supprimerLigne(-1);
    }

    @Test
    public void testSupprimerLigneValidIndex() {
        int rowIndexToRemove = 1;
        TableStockage newTable = modifTableStockage.supprimerLigne(rowIndexToRemove);

        assertEquals(newTable.getNbLignes(), 2);

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (i == rowIndexToRemove) {
                    for (int k = 0; k < 2; ++k) {
                        assertNull(newTable.getValueAt(i, k));
                    }
                } else {
                    assertEquals(newTable.getValueAt(i, j), originalTableModel.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        int columnIndexToSeparate = 1;
        TableStockage newTable = modifTableStockage.separerColonne(columnIndexToSeparate);

        assertEquals(newTable.getNbColonnes(), 3);

        String oldColumnName = originalTableModel.getColumnName(columnIndexToSeparate);
        String[] expectedColumnNames = oldColumnName.split(" ", 2);
        String[] columnNames = newTable.getColumnNames();

        assertEquals(columnNames.length, 3);
        assertTrue(Arrays.equals(columnNames, new String[] {expectedColumnNames[0], expectedColumnNames[1], "New Column"}));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i == 0 && j == columnIndexToSeparate) {
                    assertNull(newTable.getValueAt(i, j));
                } else if (i == 0) {
                    assertEquals(newTable.getValueAt(i, j), originalTableModel.getValueAt(i, j));
                } else if (j == columnIndexToSeparate) {
                    String expectedValue = originalTableModel.getValueAt(i, columnIndexToSeparate).toString();
                    String[] valueParts = expectedValue.split(" ", 2);
                    assertEquals(newTable.getValueAt(i, j), valueParts[0]);
                    assertEquals(newTable.getValueAt(i, j + 1), valueParts[1]);
                } else {
                    assertEquals(newTable.getValueAt(i, j), originalTableModel.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        int columnIndexToMerge = 1;
        TableStockage newTable = modifTableStockage.fusionnerColonnes(columnIndexToMerge);

        assertEquals(newTable.getNbColonnes(), 2);
        assertEquals(newTable.getColumnName(columnIndexToMerge), "Column 1 New Column");

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (i == 0 && j == columnIndexToMerge) {
                    String expectedValue = originalTableModel.getValueAt(i, columnIndexToMerge).toString() + " " + originalTableModel.getValueAt(i, columnIndexToMerge + 1).toString();
                    assertEquals(newTable.getValueAt(i, j), expectedValue);
                } else {
                    assertEquals(newTable.getValueAt(i, j), originalTableModel.getValueAt(i, j));
                }
            }
        }
    }
}