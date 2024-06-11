package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage ts = new TableStockage(2, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(ts.getRowCount(), modifTableStockage.getNbLignes());
        assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(2, 2);
        String nomNouvelleColonne = "newColumn";
        Class<?> type = String.class;
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(ts.getRowCount() + 1, result.getRowCount());
        assertEquals(ts.getColumnCount() + 1, result.getColumnCount());
        assertEquals(nomNouvelleColonne, result.getColumnName(result.getColumnCount() - 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneNullType() {
        TableStockage ts = new TableStockage(2, 2);
        String nomNouvelleColonne = "newColumn";
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne(nomNouvelleColonne, null);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(ts.getRowCount() + 1, result.getRowCount());
        assertEquals(ts.getColumnCount(), result.getColumnCount());
        for (int i = 0; i < result.getRowCount(); i++) {
            for (int j = 0; j < result.getColumnCount(); j++) {
                if (i == result.getRowCount() - 1 && j < result.getColumnCount() - 1) {
                    assertNull(result.getValueAt(i, j));
                } else {
                    assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerColonne(0);
        assertEquals(ts.getRowCount(), result.getRowCount());
        assertEquals(ts.getColumnCount() - 1, result.getColumnCount());
        for (int i = 0; i < result.getRowCount(); i++) {
            for (int j = 0; j < result.getColumnCount(); j++) {
                if (j == result.getColumnCount() - 1) {
                    assertNull(result.getValueAt(i, j));
                } else {
                    assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerLigne(0);
        assertEquals(ts.getRowCount() - 1, result.getRowCount());
        assertEquals(ts.getColumnCount(), result.getColumnCount());
        for (int i = 0; i < result.getRowCount(); i++) {
            for (int j = 0; j < result.getColumnCount(); j++) {
                assertEquals(ts.getValueAt(i + 1, j), result.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 1);
        String initialValue = "col1 1 col2 2";
        ts.setValueAt(initialValue, 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(0);
        assertEquals(ts.getRowCount(), result.getRowCount());
        assertEquals(ts.getColumnCount() + 1, result.getColumnCount());
        assertEquals(initialValue, result.getColumnName(0));
        assertEquals("col1", result.getColumnName(1));
        assertEquals("1", result.getValueAt(0, 1).toString());
        assertEquals("2", result.getValueAt(0, 2).toString());

        // Test for empty string
        ts = new TableStockage(3, 1);
        initialValue = "";
        ts.setValueAt(initialValue, 0, 0);
        modifTableStockage = new ModifTableStockage(ts);
        result = modifTableStockage.separerColonne(0);
        assertNull(result.getValueAt(0, 1));
        assertNull(result.getValueAt(0, 2));

        // Test for no separator
        ts = new TableStockage(3, 1);
        initialValue = "col1";
        ts.setValueAt(initialValue, 0, 0);
        modifTableStockage = new ModifTableStockage(ts);
        try {
            result = modifTableStockage.separerColonne(0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setColumnName(0, "Col1");
        ts.setColumnName(1, "Bool1");
        ts.setColumnName(2, "Col2");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.fusionnerColonnes(1);
        assertEquals(ts.getRowCount(), result.getRowCount());
        assertEquals(ts.getColumnCount() - 1, result.getColumnCount());
        assertEquals("Col1 ", result.getColumnName(0));
        assertEquals("Bool1 Col2", result.getColumnName(1));
        for (int i = 0; i < result.getRowCount(); i++) {
            for (int j = 0; j < result.getColumnCount(); j++) {
                if (j == 0) {
                    assertEquals(ts.getValueAt(i, 0), result.getValueAt(i, 0));
                } else {
                    String value = ts.getValueAt(i, 1).toString();
                    String expectedValue = value.isEmpty() ? "" : value.concat(" ") + ts.getValueAt(i, 2).toString();
                    assertEquals(expectedValue, result.getValueAt(i, j).toString());
                }
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(2, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.colonneMail();
        assertEquals(ts.getRowCount() + 1, result.getRowCount());
        assertEquals(ts.getColumnCount() + 1, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(result.getColumnCount() - 1));
        for (int i = 0; i < result.getRowCount(); i++) {
            assertEquals(Boolean.TRUE, result.getValueAt(i, result.getColumnCount() - 1));
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(3, 3);
        String columnNameToSelect = "Envoyer E-Mail a:";
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        if (modifTableStockage.getNbColonnes() > 1 && modifTableStockage.getColumnName(modifTableStockage.getNbColonnes() - 1).equals(columnNameToSelect)) {
            int j = modifTableStockage.getNbColonnes() - 1;
            int newNbLignes = 0;
            for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
                final Boolean bool = (Boolean)modifTableStockage.getValueAt(i, j);
                if (bool) {
                    ++newNbLignes;
                }
            }
            assertEquals(newNbLignes, result.getRowCount());
            assertEquals(modifTableStockage.getColumnCount(), result.getColumnCount());
            for (int i = 0; i < result.getRowCount(); i++) {
                for (int j = 0; j < result.getColumnCount(); j++) {
                    if (modifTableStockage.getValueAt(i, j) != null) {
                        assertEquals(modifTableStockage.getValueAt(i, j), result.getValueAt(i, j));
                    }
                }
            }
        } else {
            assertFalse(true);
        }
    }
}