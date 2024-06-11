package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.table.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(5, modifTableStockage.getNbLignes());
        assertEquals(3, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterColonne("Test", String.class);
        assertEquals(4, result.getColumnCount());
        assertEquals("Test", result.getColumnName(3));
        assertEquals(5, result.getRowCount());
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                Object value = ts.getValueAt(i, j);
                Object resultValue = result.getValueAt(i, j);
                if (j < 3) {
                    assertEquals(value, resultValue);
                }
            }
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(6, result.getRowCount());
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                if (i < 5) {
                    Object value = ts.getValueAt(i, j);
                    Object resultValue = result.getValueAt(i, j);
                    if (j < 3) {
                        assertEquals(value, resultValue);
                    }
                } else {
                    if (j == 0) {
                        assertNull(resultValue);
                    } else {
                        if (result.getColumnClass(j).equals(String.class)) {
                            assertEquals("", resultValue);
                        } else if (result.getColumnClass(j).equals(Double.class)) {
                            assertEquals(0.0, resultValue, 0.001);
                        } else if (result.getColumnClass(j).equals(Boolean.class)) {
                            assertEquals(false, resultValue);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerColonne(2);
        assertEquals(3, result.getColumnCount());
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                Object value = ts.getValueAt(i, j);
                Object resultValue = result.getValueAt(i, j);
                if (j < 3) {
                    assertEquals(value, resultValue);
                }
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerLigne(2);
        assertEquals(4, result.getRowCount());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                Object value = ts.getValueAt(i, j);
                Object resultValue = result.getValueAt(i, j);
                assertEquals(value, resultValue);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSupprimerColonneInvalidIndex() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.supprimerColonne(5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSupprimerLigneInvalidIndex() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.supprimerLigne(5);
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(5, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(0);
        assertEquals(3, result.getColumnCount());
        assertEquals("", result.getColumnName(1));
        assertEquals("", result.getValueAt(0, 1));
        assertEquals("", result.getValueAt(1, 1));
        assertEquals("", result.getValueAt(2, 1));
        assertEquals("", result.getValueAt(3, 1));
        assertEquals("", result.getValueAt(4, 1));
        assertEquals(1.0, result.getValue(0, 2), 0.001);
        assertEquals(2.0, result.getValue(1, 2), 0.001);
        assertEquals(3.0, result.getValue(2, 2), 0.001);
        assertEquals(4.0, result.getValue(3, 2), 0.001);
        assertEquals(5.0, result.getValue(4, 2), 0.001);

        ts.setValueAt("Test", 0, 0);
        ts.setValueAt("Test2 Test", 1, 0);
        ts.setValueAt("Test3 Test3", 2, 0);
        ts.setValueAt("Test4 Test4", 3, 0);
        ts.setValueAt("Test5 Test5", 4, 0);

        result = modifTableStockage.separerColonne(0);

        assertEquals("Test", result.getColumnName(0));
        assertEquals("Test2", result.getColumnName(1));
        assertEquals("Test3", result.getColumnName(2));
        assertEquals("Test4", result.getColumnName(3));
        assertEquals("Test5", result.getColumnName(4));

        assertEquals("Test", result.getValueAt(0, 0).toString());
        assertEquals("Test2", result.getValueAt(0, 1).toString());
        assertEquals("Test3", result.getValueAt(1, 0).toString());
        assertEquals("Test4", result.getValueAt(1, 1).toString());
        assertEquals("Test5", result.getValueAt(2, 0).toString());

        assertEquals(1.0, result.getValue(0, 1), 0.001);
        assertEquals(2.0, result.getValue(1, 1), 0.001);
        assertEquals(3.0, result.getValue(2, 1), 0.001);
        assertEquals(4.0, result.getValue(3, 1), 0.001);
        assertEquals(5.0, result.getValue(4, 1), 0.001);

        assertEquals(1.0, result.getValue(0, 2), 0.001);
        assertEquals(2.0, result.getValue(1, 2), 0.001);
        assertEquals(3.0, result.getValue(2, 2), 0.001);
        assertEquals(4.0, result.getValue(3, 2), 0.001);
        assertEquals(5.0, result.getValue(4, 2), 0.001);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.fusionnerColonnes(1);
        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a: Test1 Test2", result.getColumnName(1));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                Object value = ts.getValueAt(i, j);
                Object resultValue = result.getValueAt(i, j);
                if (j < 1) {
                    assertEquals(value, resultValue);
                } else {
                    if (result.getColumnClass(j).equals(String.class)) {
                        String valueStr = value.toString();
                        String resultValueStr = resultValue.toString();
                        assertEquals(valueStr.substring(0, valueStr.indexOf(" ")), resultValueStr);
                        assertEquals(valueStr.substring(valueStr.indexOf(" ") + 1), resultValueStr.substring(resultValueStr.indexOf(" ") + 1));
                    }
                }
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.colonneMail();
        assertEquals(4, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(3));
        assertEquals(Boolean.class, result.getColumnClass(3));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                Object value = ts.getValueAt(i, j);
                Object resultValue = result.getValueAt(i, j);
                if (j < 3) {
                    assertEquals(value, resultValue);
                } else {
                    assertEquals(true, resultValue);
                }
            }
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        assertEquals(5, result.getRowCount());
        assertEquals(4, result.getColumnCount());
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                Object value = ts.getValueAt(i, j);
                Object resultValue = result.getValueAt(i, j);
                if (j < 4) {
                    assertEquals(value, resultValue);
                }
            }
        }
    }

}