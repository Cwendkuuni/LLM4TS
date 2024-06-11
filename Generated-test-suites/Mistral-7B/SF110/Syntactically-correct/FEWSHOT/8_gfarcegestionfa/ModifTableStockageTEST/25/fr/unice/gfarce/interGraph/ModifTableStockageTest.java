package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import javax.swing.table.TableModel;

import org.junit.Test;

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
        TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(4, tableS.getColumnCount());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(3));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterLigne();
        assertEquals(6, tableS.getRowCount());
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(tableS.getValueAt(i, j), ts.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt("test", 0, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerColonne(2);
        assertEquals(2, tableS.getColumnCount());
        assertNull(tableS.getValueAt(0, 1));
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt("test", 1, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerLigne(1);
        assertEquals(4, tableS.getRowCount());
        assertNull(tableS.getValueAt(1, 0));
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt("test test", 0, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.separerColonne(2);
        assertEquals(5, tableS.getColumnCount());
        assertEquals("test", tableS.getValueAt(0, 2));
        assertEquals("test", tableS.getValueAt(0, 3));
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt(true, 0, 2);
        ts.setValueAt(true, 0, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.fusionnerColonnes(2);
        assertEquals(3, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a: true", tableS.getColumnName(2));
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.colonneMail();
        assertEquals(4, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(3));
        assertTrue(tableS.isCellEditable(3, 0));
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 4);
        ts.setValueAt(true, 0, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(5, tableS.getRowCount());
        assertEquals(4, tableS.getColumnCount());
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                assertTrue(tableS.isCellEditable(i, 3));
                assertEquals(true, tableS.getValueAt(i, 3));
            } else {
                assertFalse(tableS.isCellEditable(i, 3));
                assertNull(tableS.getValueAt(i, 3));
            }
        }
    }
}