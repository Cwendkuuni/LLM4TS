package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(5, modifTableStockage.getNbLignes());
        assertEquals(3, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        String nomNouvelleColonne = "newColumn";
        Class<?> type = String.class;
        TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(4, result.getColumnCount());
        assertEquals(nomNouvelleColonne, result.getColumnName(3));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(6, result.getRowCount());
        for (int i = 0; i < 5; ++i) {
            for (int k = 0; k < 3; ++k) {
                Object value = result.getValueAt(i, k);
                if (k < 3) {
                    assertEquals(ts.getValueAt(i, k), value);
                } else {
                    if (ts.getColumnClass(k).equals(String.class)) {
                        assertEquals("", value);
                    } else if (ts.getColumnClass(k).equals(Double.class)) {
                        assertEquals(0d, value, 0.001);
                    } else {
                        assertEquals(false, value);
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
        for (int j = 0; j < 3; ++j) {
            String columnName = result.getColumnName(j);
            if (j < 2) {
                assertEquals(ts.getColumnName(j), columnName);
            } else {
                assertEquals(ts.getColumnName(j + 1), columnName);
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(6, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerLigne(4);
        assertEquals(5, result.getRowCount());
        for (int i = 0; i < 5; ++i) {
            for (int k = 0; k < 3; ++k) {
                Object value = result.getValueAt(i, k);
                if (i < 4) {
                    assertEquals(ts.getValueAt(i, k), value);
                }
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(5, 2);
        ts.setValueAt("column1  column2", 0, 1);
        Ts.setValueAt("12.5 column3 15.3", 1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(1);
        assertEquals(4, result.getColumnCount());
        assertEquals("column1", result.getColumnName(0));
        assertEquals("column2", result.getColumnName(1));
        assertEquals("column3", result.getColumnName(2));
        assertEquals("column3", result.getColumnName(3));
        for (int i = 0; i < 2; ++i) {
            for (int k = 0; k < 4; ++k) {
                Object value = result.getValueAt(i, k);
                if (i < 1) {
                    if (k < 2) {
                        assertEquals(ts.getValueAt(i, k), value);
                    } else {
                        if (i == 0) {
                            assertEquals(12.5, value, 0.001);
                        } else {
                            assertEquals(15.3, value, 0.001);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt(true, 0, 1);
        ts.setValueAt(false, 1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.fusionnerColonnes(1);
        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a: false column2", result.getColumnName(1));
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 3; ++k) {
                Object value = result.getValueAt(i, k);
                if (i < 2 || k < 1) {
                    assertEquals(ts.getValueAt(i, k), value);
                } else if (i < 1) {
                    assertEquals(false, value);
                } else {
                    assertEquals(false, value);
                }
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.colonneMail();
        assertEquals(6, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(5));
        for (int i = 0; i < 5; ++i) {
            assertEquals(false, result.getValueAt(i, 5));
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 4);
        ts.setValueAt(true, 0, 3);
        ts.setValueAt(true, 1, 3);
        ts.setValueAt(false, 2, 3);
        ts.setValueAt(false, 3, 3);
        ts.setValueAt(true, 4, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        assertEquals(2, result.getRowCount());
        for (int i = 0; i < 2; ++i) {
            for (int k = 0; k < 4; ++k) {
                Object value = result.getValueAt(i, k);
                if (i < 1) {
                    if (k < 3) {
                        assertEquals(ts.getValueAt(i, k), value);
                    } else {
                        assertEquals(true, value);
                    }
                }
            }
        }
    }
}