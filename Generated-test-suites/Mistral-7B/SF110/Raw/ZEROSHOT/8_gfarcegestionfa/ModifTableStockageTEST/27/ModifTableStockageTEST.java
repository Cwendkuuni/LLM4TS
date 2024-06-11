package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(ts.getColumnCount(), modifTableStockage.nbColonnes);
        assertEquals(ts.getRowCount(), modifTableStockage.nbLignes - 1);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterColonne("newColumn", String.class);
        TableModel tableModel = result.getTableModel();
        assertEquals(result.getColumnCount(), modifTableStockage.nbColonnes + 1);
        assertEquals(tableModel.getColumnName(modifTableStockage.nbColonnes - 1), "newColumn");
        for (int i = 0; i < modifTableStockage.nbLignes - 1; ++i) {
            for (int j = 0; j < modifTableStockage.nbColonnes; ++j) {
                Object value = tableModel.getValueAt(i, j);
                assertEquals(value, modifTableStockage.ts.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterLigne();
        TableModel tableModel = result.getTableModel();
        assertEquals(result.getRowCount(), modifTableStockage.nbLignes);
        for (int i = 0; i < modifTableStockage.nbLignes - 1; ++i) {
            for (int j = 0; j < modifTableStockage.nbColonnes; ++j) {
                Object value = tableModel.getValueAt(i, j);
                assertEquals(value, modifTableStockage.ts.getValueAt(i, j));
            }
        }
        for (int j = 0; j < modifTableStockage.nbColonnes; ++j) {
            if (modifTableStockage.ts.getColumnClass(j).equals(String.class)) {
                assertEquals("", tableModel.getValueAt(modifTableStockage.nbLignes - 1, j));
            } else if (modifTableStockage.ts.getColumnClass(j).equals(Double.class)) {
                assertEquals(0.0, tableModel.getValueAt(modifTableStockage.nbLignes - 1, j).doubleValue());
            } else {
                assertEquals(false, tableModel.getValueAt(modifTableStockage.nbLignes - 1, j));
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerColonne(0);
        TableModel tableModel = result.getTableModel();
        assertEquals(result.getColumnCount(), modifTableStockage.nbColonnes - 1);
        for (int i = 0; i < modifTableStockage.nbLignes - 1; ++i) {
            for (int j = 0; j < modifTableStockage.nbColonnes - 1; ++j) {
                Object value = tableModel.getValueAt(i, j);
                assertEquals(value, modifTableStockage.ts.getValueAt(i, j));
            }
        }
        for (int j = 0; j < modifTableStockage.nbColonnes - 1; ++j) {
            assertEquals(modifTableStockage.ts.getColumnName(j), tableModel.getColumnName(j));
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerLigne(0);
        TableModel tableModel = result.getTableModel();
        assertEquals(result.getRowCount(), modifTableStockage.nbLignes - 1);
        for (int i = 0; i < modifTableStockage.nbLignes - 1; ++i) {
            for (int j = 0; j < modifTableStockage.nbColonnes; ++j) {
                Object value = tableModel.getValueAt(i, j);
                assertEquals(value, modifTableStockage.ts.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt("1 2", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(0);
        TableModel tableModel = result.getTableModel();
        assertEquals(result.getColumnCount(), modifTableStockage.nbColonnes + 1);
        for (int i = 0; i < modifTableStockage.nbLignes - 1; ++i) {
            for (int j = 0; j < modifTableStockage.nbColonnes; ++j) {
                Object value = tableModel.getValueAt(i, j);
                assertEquals(value, modifTableStockage.ts.getValueAt(i, j));
            }
        }
        assertEquals("1", tableModel.getValueAt(0, modifTableStockage.nbColonnes - 1));
        assertEquals("2", tableModel.getValueAt(0, modifTableStockage.nbColonnes - 2));
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt(true, 0, 0);
        ts.setValueAt(false, 0, 1);
        ts.setValueAt(true, 1, 0);
        ts.setValueAt(false, 1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        TableModel tableModel = result.getTableModel();
        assertEquals(result.getColumnCount(), modifTableStockage.nbColonnes - 1);
        for (int i = 0; i < modifTableStockage.nbLignes - 1; ++i) {
            assertEquals("true false", tableModel.getValueAt(i, 0).toString());
            for (int j = 1; j < modifTableStockage.nbColonnes; ++j) {
                assertEquals(modifTableStockage.ts.getValueAt(i, j), tableModel.getValueAt(i, j - 1));
            }
        }
    }

    @Test(expected = AssertionError.class)
    public void testSeparerColonneError() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt("1", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.separerColonne(0);
    }

    @Test(expected = AssertionError.class)
    public void testFusionnerColonnesError() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt("1", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.fusionnerColonnes(0);
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("name", 0, 0);
        ts.setValueAt("email", 0, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.colonneMail();
        TableModel tableModel = result.getTableModel();
        assertEquals(result.getColumnCount(), modifTableStockage.nbColonnes + 1);
        assertEquals("name", tableModel.getColumnName(0));
        assertEquals("Envoyer E-Mail a:", tableModel.getColumnName(modifTableStockage.nbColonnes - 1));
        for (int i = 0; i < modifTableStockage.nbLignes - 1; ++i) {
            for (int j = 0; j < modifTableStockage.nbColonnes; ++j) {
                Object value = tableModel.getValueAt(i, j);
                assertEquals(value, modifTableStockage.ts.getValueAt(i, j));
            }
            assertEquals(true, tableModel.getValueAt(i, modifTableStockage.nbColonnes - 1));
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setValueAt("name1", 0, 0);
        ts.setValueAt("name2", 1, 0);
        ts.setValueAt("name3", 2, 0);
        ts.setValueAt(true, 0, 2);
        ts.setValueAt(true, 1, 2);
        ts.setValueAt(false, 2, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        TableModel tableModel = result.getTableModel();
        assertEquals(result.getRowCount(), 2);
        assertEquals("name1", tableModel.getValueAt(0, 0));
        assertEquals("name2", tableModel.getValueAt(1, 0));
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < modifTableStockage.nbColonnes; ++j) {
                Object value = tableModel.getValueAt(i, j);
                if (j != modifTableStockage.nbColonnes - 1) {
                    assertEquals(value, modifTableStockage.ts.getValueAt(i, j));
                }
            }
        }
    }
}