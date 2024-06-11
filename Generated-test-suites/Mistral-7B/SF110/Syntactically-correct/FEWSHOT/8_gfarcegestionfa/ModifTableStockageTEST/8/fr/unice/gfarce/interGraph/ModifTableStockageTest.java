package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.swing.table.TableModel;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockage_Constructor() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        assertEquals(2, modifTableStockage.nbColonnes);
        assertEquals(2, modifTableStockage.nbLignes);
        assertEquals(tableModel, modifTableStockage.ts);
    }

    @Test
    public void testAjouterColonne() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        String nomNouvelleColonne = "Nom";
        Class<?> type = String.class;
        TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);

        assertEquals(3, result.getRowCount());
        assertEquals(3, result.getColumnCount());
        assertEquals(nomNouvelleColonne, result.getColumnName(2));
        assertArrayEquals(new Object[]{"A1", "B1", nomNouvelleColonne}, result.getColumnNames());
        assertEquals("", result.getValueAt(0, 2));
        assertEquals("", result.getValueAt(1, 2));
        assertTrue(result instanceof TableStockage);
    }

    @Test
    public void testAjouterColonne_NullName() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        Class<?> type = String.class;
        assertNull(modifTableStockage.ajouterColonne(null, type));
    }

    @Test
    public void testAjouterColonne_NullType() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        String nomNouvelleColonne = "Nom";
        assertNull(modifTableStockage.ajouterColonne(nomNouvelleColonne, null));
    }

    @Test
    public void testAjouterLigne() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        TableStockage result = modifTableStockage.ajouterLigne();

        assertEquals(3, result.getRowCount());
        assertEquals(3, result.getColumnCount());
        assertSame(modifTableStockage.ts, result);
        assertNull(result.getValueAt(2, 0));
        assertNull(result.getValueAt(2, 1));
        if (modifTableStockage.ts.getColumnClass(0).equals(String.class)) {
            assertEquals("", result.getValueAt(2, 0));
        } else if (modifTableStockage.ts.getColumnClass(0).equals(Double.class)) {
            assertEquals(0.0, result.getValueAt(2, 0));
        } else if (modifTableStockage.ts.getColumnClass(0).equals(Boolean.class)) {
            assertEquals(false, result.getValueAt(2, 0));
        }
        if (modifTableStockage.ts.getColumnClass(1).equals(String.class)) {
            assertEquals("", result.getValueAt(2, 1));
        } else if (modifTableStockage.ts.getColumnClass(1).equals(Double.class)) {
            assertEquals(0.0, result.getValueAt(2, 1));
        } else if (modifTableStockage.ts.getColumnClass(1).equals(Boolean.class)) {
            assertEquals(false, result.getValueAt(2, 1));
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        TableStockage result = modifTableStockage.supprimerColonne(0);

        assertEquals(2, result.getRowCount());
        assertEquals(2, result.getColumnCount());
        assertSame(modifTableStockage.ts, result);
        assertNull(result.getColumnName(0));
        for (int i = 0; i < result.getRowCount(); i++) {
            for (int j = 0; j < result.getColumnCount(); j++) {
                if (j < 1) {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), result.getValueAt(i, j));
                } else {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j + 1), result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        TableStockage result = modifTableStockage.supprimerLigne(0);

        assertEquals(1, result.getRowCount());
        assertEquals(2, result.getColumnCount());
        assertSame(modifTableStockage.ts, result);
        assertNull(result.getValueAt(0, 0));
        assertNull(result.getValueAt(0, 1));
    }

    @Test
    public void testSeparerColonne() {
        TableModel tableModel = new Object[][]{{"A1  B1  C1", "D1  E1  F1"}, {"G1  H1  I1", "J1  K1  L1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        TableStockage result = modifTableStockage.separerColonne(0);

        assertEquals(4, result.getRowCount());
        assertEquals(3, result.getColumnCount());
        assertSame(modifTableStockage.ts, result);
        assertNull(result.getColumnName(0));
        assertEquals("A1", result.getColumnName(1));
        assertEquals("B1", result.getColumnName(2));

        for (int i = 0; i < result.getRowCount(); i++) {
            for (int j = 0; j < result.getColumnCount(); j++) {
                if (j == 0) {
                    assertNull(result.getValueAt(i, j));
                } else {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j * 2), result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableModel tableModel = new Object[][]{{true, true}, {false, false}, {true, false}, {false, true}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        TableStockage result = modifTableStockage.fusionnerColonnes(0);

        assertEquals(2, result.getRowCount());
        assertEquals(2, result.getColumnCount());
        assertSame(modifTableStockage.ts, result);
        assertTrue(result.getValueAt(0, 0) instanceof String);
        assertEquals("true false", result.getValueAt(0, 0).toString());
        assertTrue(result.getValueAt(1, 0) instanceof String);
        assertEquals("false true", result.getValueAt(1, 0).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnes_InvalidColumnIndex() {
        TableModel tableModel = new Object[][]{{true, true}, {false, false}, {true, false}, {false, true}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        modifTableStockage.fusionnerColonnes(-1);
    }

    @Test
    public void testColonneMail() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        TableStockage result = modifTableStockage.colonneMail();

        assertEquals(3, result.getRowCount());
        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
        for (int i = 0; i < result.getRowCount(); i++) {
            for (int j = 0; j < result.getColumnCount(); j++) {
                if (j == 2) {
                    assertTrue(result.getValueAt(i, j) instanceof Boolean);
                } else {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSelectMail() {
        TableModel tableModel = new Object[][]{{"A1", "B1", true}, {"C1", "D1", true}, {"E1", "F1", false}, {"G1", "H1", true}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        TableStockage result = modifTableStockage.selectMail();

        assertEquals(2, result.getRowCount());
        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
        for (int i = 0; i < result.getRowCount(); i++) {
            for (int j = 0; j < result.getColumnCount(); j++) {
                if (j == 2) {
                    assertTrue(result.getValueAt(i, j) instanceof Boolean);
                    assertEquals(true, ((Boolean) result.getValueAt(i, j)).booleanValue());
                } else {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSelectMail_EmptyTable() {
        TableModel tableModel = new Object[][]{{}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));

        TableStockage result = modifTableStockage.selectMail();

        assertEquals(0, result.getRowCount());
        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
    }
}