package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import javax.swing.table.TableModel;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        assertEquals(5, mts.getNbLignes());
        assertEquals(3, mts.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.ajouterColonne("nomNouvelleColonne", String.class);
        assertEquals(4, tableS.getColumnCount());
        assertEquals("nomNouvelleColonne", tableS.getColumnName(3));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.ajouterLigne();
        assertEquals(6, tableS.getRowCount());
        assertNull(tableS.getValueAt(5, 0));
        for (int i = 0; i < 3; i++) {
            assertEquals(tableS.getValueAt(5, i), "");
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.supprimerColonne(2);
        assertEquals(3, tableS.getColumnCount());
        for (int i = 0; i < 3; i++) {
            if (i != 1) {
                assertEquals(tableS.getColumnName(i), ts.getColumnName(i));
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.supprimerLigne(2);
        assertEquals(4, tableS.getRowCount());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != 2 && j != 3) {
                    assertEquals(tableS.getValueAt(i, j), ts.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(5, 2);
        ts.setValueAt(" 123 456 ", 0, 0);
        ts.setValueAt(" 789 1011", 1, 0);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.separerColonne(0);
        assertEquals(3, tableS.getColumnCount());
        assertEquals("123", tableS.getValueAt(0, 1).toString());
        assertEquals("456", tableS.getValueAt(0, 2).toString());
        assertEquals("789", tableS.getValueAt(1, 1).toString());
        assertEquals("1011", tableS.getValueAt(1, 2).toString());
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt(true, 0, 0);
        ts.setValueAt("A", 0, 1);
        ts.setValueAt("B", 0, 2);
        ts.setValueAt(false, 1, 0);
        ts.setValueAt("C", 1, 1);
        ts.setValueAt("D", 1, 2);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.fusionnerColonnes(1);
        assertEquals(3, tableS.getColumnCount());
        assertEquals("A B", tableS.getColumnName(1));
        assertEquals(true, tableS.getValueAt(0, 1));
        assertEquals("AC", tableS.getValueAt(0, 2).toString());
        assertEquals("BD", tableS.getValueAt(1, 2).toString());
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.colonneMail();
        assertEquals(4, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(3));
        for (int i = 0; i < 5; i++) {
            assertEquals(true, tableS.getValueAt(i, 3));
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 4);
        ts.setValueAt(true, 0, 3);
        ts.setValueAt(true, 1, 3);
        ts.setValueAt(false, 2, 3);
        ts.setValueAt(true, 3, 3);
        ts.setValueAt(true, 4, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.selectMail();
        assertEquals(5, tableS.getRowCount());
        assertEquals(4, tableS.getColumnCount());
        for (int i = 0; i < 5; i++) {
            if (i != 2) {
                for (int j = 0; j < 4; j++) {
                    if (j != 3) {
                        assertEquals(ts.getValueAt(i, j), tableS.getValueAt(i, j));
                    }
                }
            }
        }
    }
}