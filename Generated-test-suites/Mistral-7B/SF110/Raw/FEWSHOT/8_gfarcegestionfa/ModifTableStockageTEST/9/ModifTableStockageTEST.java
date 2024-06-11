package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.table.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockage() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        assertEquals(2, modifTableStockage.getNbLignes());
        assertEquals(2, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        String nomNouvelleColonne = "NewCol";
        Class<?> type = Integer.class;
        TableStockage newTableStockage = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(3, newTableStockage.getNbColonnes());
        assertEquals(2, newTableStockage.getColumnName(0).length());
        assertEquals(nomNouvelleColonne, newTableStockage.getColumnName(2));
        assertEquals(1, newTableStockage.getRowCount());
        assertEquals(null, newTableStockage.getValueAt(0, 2));
    }

    @Test
    public void testAjouterLigne() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        TableStockage newTableStockage = modifTableStockage.ajouterLigne();
        assertEquals(3, newTableStockage.getNbLignes());
        assertEquals(2, newTableStockage.getNbColonnes());
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                assertEquals(null, newTableStockage.getValueAt(i, j));
            }
        }
        if (newTableStockage.getColumnClass(0).equals(String.class)) {
            assertEquals("", newTableStockage.getValueAt(2, 0));
        } else if (newTableStockage.getColumnClass(0).equals(Double.class)) {
            assertEquals(0.0, newTableStockage.getValueAt(2, 0));
        } else {
            assertEquals(false, newTableStockage.getValueAt(2, 0));
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        TableStockage newTableStockage = modifTableStockage.supprimerColonne(0);
        assertEquals(2, newTableStockage.getNbColonnes());
        assertEquals(2, newTableStockage.getRowCount());
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 1; ++j) {
                assertEquals(null, newTableStockage.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        TableStockage newTableStockage = modifTableStockage.supprimerLigne(0);
        assertEquals(1, newTableStockage.getNbLignes());
        assertEquals(2, newTableStockage.getNbColonnes());
        for (int i = 0; i < 1; ++i) {
            for (int j = 0; j < 2; ++j) {
                assertEquals(null, newTableStockage.getValueAt(i, j));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSupprimerColonneNegativeIndex() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        modifTableStockage.supprimerColonne(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSupprimerLigneNegativeIndex() {
        TableModel tableModel = new Object[][]{{"A1", "B1"}, {"C1", "D1"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        modifTableStockage.supprimerLigne(-1);
    }

    @Test
    public void testSeparerColonne() {
        TableModel tableModel = new Object[][]{{"A1  B1", "C1  D1"}, {"A2  B2", "C2  D2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        TableStockage newTableStockage = modifTableStockage.separerColonne(0);
        assertEquals(4, newTableStockage.getNbColonnes());
        assertEquals(2, newTableStockage.getRowCount());
        String colonne1 = newTableStockage.getColumnName(0);
        String colonne2 = newTableStockage.getColumnName(1);
        assertEquals("A1", colonne1);
        assertEquals("B1", colonne2);
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (j == 1) {
                    if (newTableStockage.getColumnClass(j).equals(Double.class)) {
                        assertEquals(0.0, newTableStockage.getValueAt(i, j), 0.0);
                    }
                } else {
                    assertEquals(null, newTableStockage.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableModel tableModel = new Object[][]{{false, true}, {true, false}, {true, false}, {false, true}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
        TableStockage newTableStockage = modifTableStockage.fusionnerColonnes(0);
        assertEquals(3, newTableStockage.getNbColonnes());
        assertEquals(4, newTableStockage.getRowCount());
        String colonne1 = newTableStockage.getColumnName(0);
        String colonne2 = newTableStockage.getColumnName(1);
        assertEquals("Envoyer E-Mail a: false, true", colonne1);
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (j == 1) {
                    if (newTableStockage.getValueAt(i, j).toString().equals("false")) {
                        assertEquals("false", newTableStockage.getValueAt(i, j).toString());
                    } else if (newTableStockage.getValueAt(i, j).toString().equals("true")) {
                        assertEquals("true", newTableStockage.getValueAt(i, j).toString());
                    }
                } else {
                    assertEquals(null, newTableStockage.getValueAt(i, j));
                }
            }
        }
    }
}