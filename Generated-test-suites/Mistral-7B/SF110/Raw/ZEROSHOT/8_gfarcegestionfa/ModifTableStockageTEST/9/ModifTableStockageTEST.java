package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(modifTableStockage.getNbColonnes(), ts.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), ts.getRowCount() + 1);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterColonne("TestColonne", Integer.class);
        assertEquals(result.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
        assertEquals(result.getValueAt(0, result.getColumnCount() - 1), null);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(result.getRowCount(), modifTableStockage.getNbLignes() + 1);
        for (int i = 0; i < result.getColumnCount(); ++i) {
            assertEquals(result.getValueAt(result.getRowCount() - 1, i), result.getValueAt(modifTableStockage.getNbLignes() - 1, i));
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerColonne(2);
        assertEquals(result.getColumnCount(), modifTableStockage.getNbColonnes() - 1);
        for (int i = 0; i < result.getColumnCount(); ++i) {
            assertEquals(result.getColumnName(i), modifTableStockage.getColumnName(i + 1));
        }
        for (int i = 0; i < result.getRowCount(); ++i) {
            for (int j = 0; j < result.getColumnCount(); ++j) {
                assertEquals(result.getValueAt(i, j), modifTableStockage.getValueAt(i, j + 1));
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(4, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerLigne(1);
        assertEquals(result.getRowCount(), modifTableStockage.getNbLignes() - 1);
        for (int i = 0; i < result.getColumnCount(); ++i) {
            assertEquals(result.getColumnName(i), modifTableStockage.getColumnName(i));
        }
        for (int i = 0; i < result.getRowCount(); ++i) {
            for (int j = 0; j < result.getColumnCount(); ++j) {
                assertEquals(result.getValueAt(i, j), modifTableStockage.getValueAt(i + 1, j));
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("Test 1 10", 0, 0);
        ts.setValueAt("Test 2 20", 1, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(0);
        assertEquals(result.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
        assertEquals(result.getValueAt(0, result.getColumnCount() - 1), "10");
        assertEquals(result.getValueAt(1, result.getColumnCount() - 1), "20");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneInvalidIndice() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.separerColonne(-1);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 4);
        ts.setValueAt(true, 0, 2);
        ts.setValueAt(true, 0, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.fusionnerColonnes(2);
        assertEquals(result.getColumnCount(), modifTableStockage.getNbColonnes() - 1);
        assertEquals(result.getValueAt(0, 2), "true");
        assertEquals(result.getValueAt(0, 3), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesInvalidIndice() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.fusionnerColonnes(-1);
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.colonneMail();
        assertEquals(result.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
        assertEquals(result.getValueAt(0, result.getColumnCount() - 1), true);
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 5);
        ts.setValueAt(true, 3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        assertEquals(result.getRowCount(), 1);
        for (int i = 0; i < result.getColumnCount(); ++i) {
            assertEquals(result.getColumnName(i), modifTableStockage.getColumnName(i));
        }
        assertEquals(result.getValueAt(0, 4), true);
    }

}