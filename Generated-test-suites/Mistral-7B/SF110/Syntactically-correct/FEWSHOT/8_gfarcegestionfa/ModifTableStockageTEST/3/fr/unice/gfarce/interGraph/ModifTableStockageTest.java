package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import javax.swing.table.TableModel;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage table = new TableStockage(3, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        assertEquals(3, modifTable.getNbLignes());
        assertEquals(4, modifTable.getNbColonnes());
    }

    @Test
    public void testAjouterColonneStringType() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        String newColumnName = "newColumn";
        Class<?> newColumnType = String.class;
        TableStockage expectedNewTable = new TableStockage(3, 4);

        for (int j = 0; j < 3; ++j) {
            expectedNewTable.setColumnName(j, table.getColumnName(j));
        }

        expectedNewTable.setColumnName(3, newColumnName);
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 3; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }
            expectedNewTable.setColumnClass(3, newColumnType);
        }

        TableStockage resultTable = modifTable.ajouterColonne(newColumnName, newColumnType);
        assertSame(expectedNewTable, resultTable);
    }

    @Test
    public void testAjouterColonneOtherType() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        String newColumnName = "newColumn";
        Class<?> newColumnType = Double.class;

        TableStockage expectedNewTable = new TableStockage(3, 4);

        for (int j = 0; j < 3; ++j) {
            expectedNewTable.setColumnName(j, table.getColumnName(j));
        }

        expectedNewTable.setColumnName(3, newColumnName);
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 3; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }
            expectedNewTable.setColumnClass(3, newColumnType);
        }

        for (int i = 0; i < 3; ++i) {
            if (table.getColumnClass(0).equals(Double.class)) {
                expectedNewTable.setValueAt(new Double("0.0"), i, 3);
            } else {
                expectedNewTable.setValueAt("", i, 3);
            }
        }

        TableStockage resultTable = modifTable.ajouterColonne(newColumnName, newColumnType);
        assertSame(expectedNewTable, resultTable);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage table = new TableStockage(2, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage expectedNewTable = new TableStockage(3, 3);

        for (int j = 0; j < 3; ++j) {
            expectedNewTable.setColumnName(j, table.getColumnName(j));
        }

        for (int i = 0; i < 2; ++i) {
            for (int k = 0; k < 3; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }
        }

        for (int i = 0; i < 3; ++i) {
            if (table.getColumnClass(0).equals(String.class)) {
                expectedNewTable.setValueAt("", 2, i);
            } else {
                expectedNewTable.setValueAt(new Double("0"), 2, i);
            }
        }

        TableStockage resultTable = modifTable.ajouterLigne();
        assertSame(expectedNewTable, resultTable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterLigneNoColumns() {
        TableStockage table = new TableStockage(0, 0);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);

        TableStockage expectedNewTable = new TableStockage(3, 2);

        for (int j = 0; j < 2; ++j) {
            expectedNewTable.setColumnName(j, table.getColumnName(j));
        }

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 2; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }
        }

        int indexToRemove = 1;
        TableStockage resultTable = modifTable.supprimerColonne(indexToRemove);
        assertSame(expectedNewTable, resultTable);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneInvalidIndex() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.supprimerColonne(3);
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);

        TableStockage expectedNewTable = new TableStockage(2, 3);

        for (int j = 0; j < 3; ++j) {
            expectedNewTable.setColumnName(j, table.getColumnName(j));
        }

        for (int i = 0; i < 2; ++i) {
            for (int k = 0; k < 3; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }
        }

        int indexToRemove = 1;
        TableStockage resultTable = modifTable.supprimerLigne(indexToRemove);
        assertSame(expectedNewTable, resultTable);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneInvalidIndex() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.supprimerLigne(3);
    }

    @Test
    public void testSeparerColonne() {
        TableStockage table = new TableStockage(2, 1);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        String separator = " ";
        int indexToSeparate = 0;

        TableStockage expectedNewTable = new TableStockage(2, 2);
        expectedNewTable.setColumnName(0, table.getColumnName(0));
        expectedNewTable.setColumnName(1, "newColumn");

        for (int i = 0; i < 2; ++i) {
            for (int k = 0; k < 1; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }

            for (int l = 0; l < 2; ++l) {
                expectedNewTable.setValueAt(table.getValueAt(i, l), i, l);
            }
        }

        TableStockage resultTable = modifTable.separerColonne(indexToSeparate);
        assertSame(expectedNewTable, resultTable);
    }

    @Test
    public void testSeparerColonneInvalidIndex() {
        TableStockage table = new TableStockage(2, 1);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        String separator = " ";
        int indexToSeparate = 1;

        TableStockage expectedNewTable = new TableStockage(2, 2);
        expectedNewTable.setColumnName(0, table.getColumnName(0));
        expectedNewTable.setColumnName(1, table.getColumnName(0));

        for (int i = 0; i < 2; ++i) {
            for (int k = 0; k < 1; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }

            for (int l = 0; l < 2; ++l) {
                expectedNewTable.setValueAt(table.getValueAt(i, l), i, l);
            }
        }

        TableStockage resultTable = modifTable.separerColonne(indexToSeparate);
        assertSame(expectedNewTable, resultTable);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage table = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indexToMerge = 0;
        int indexToMerge2 = 1;

        TableStockage expectedNewTable = new TableStockage(3, 1);
        expectedNewTable.setColumnName(0, "ColumnName0ColumnName1");

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 1; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }
        }

        TableStockage resultTable = modifTable.fusionnerColonnes(indexToMerge);
        assertSame(expectedNewTable, resultTable);
    }

    @Test
    public void testFusionnerColonnesInvalidIndex() {
        TableStockage table = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indexToMerge = 3;

        TableStockage expectedNewTable = new TableStockage(3, 2);
        expectedNewTable.setColumnName(0, table.getColumnName(0));
        expectedNewTable.setColumnName(1, table.getColumnName(1));
        expectedNewTable.setColumnName(2, table.getColumnName(2));

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 2; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }
        }

        TableStockage resultTable = modifTable.fusionnerColonnes(indexToMerge);
        assertSame(expectedNewTable, resultTable);
    }

    @Test
    public void testColonneMail() {
        TableStockage table = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);

        TableStockage expectedNewTable = new TableStockage(3, 3);

        for (int j = 0; j < 3; ++j) {
            expectedNewTable.setColumnName(j, table.getColumnName(j));
        }

        for (int j = 0; j < 3; ++j) {
            expectedNewTable.setColumnName(3, "Envoyer E-Mail a:");
        }

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 2; ++k) {
                expectedNewTable.setValueAt(table.getValueAt(i, k), i, k);
            }

            expectedNewTable.setValueAt(new Boolean(true), i, 3);
        }

        TableStockage resultTable = modifTable.colonneMail();
        assertSame(expectedNewTable, resultTable);
    }

    @Test
    public void testSelectMail() {
        TableStockage table = new TableStockage(3, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);

        for (int j = 0; j < 4; ++j) {
            table.setColumnName(j, "ColumnName" + j);
        }

        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 4; ++k) {
                table.setValueAt("Value" + i + "_" + k, i, k);
            }
        }

        int expectedSelectedRowIndex = 1;
        TableStockage expectedSelectedTable = new TableStockage(2, 4);

        for (int j = 0; j < 4; ++j) {
            expectedSelectedTable.setColumnName(j, table.getColumnName(j));
        }

        expectedSelectedTable.setValueAt(table.getValueAt(expectedSelectedRowIndex, 3), 0, 3);

        for (int i = 0; i < 2; ++i) {
            for (int k = 0; k < 4; ++k) {
                expectedSelectedTable.setValueAt(table.getValueAt(i, k), i, k);
            }
        }

        TableStockage resultTable = modifTable.selectMail();
        assertSame(expectedSelectedTable, resultTable);
    }
}