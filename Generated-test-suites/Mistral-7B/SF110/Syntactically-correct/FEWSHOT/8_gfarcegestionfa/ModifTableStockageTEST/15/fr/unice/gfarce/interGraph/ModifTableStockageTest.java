package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(5, modifTableStockage.getNbColonnes());
        assertEquals(5, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonneStringClass() {
        TableStockage ts = new TableStockage(3, 2);
        String nomNouvelleColonne = "New Column";
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, String.class);
        assertEquals(4, tableS.getColumnCount());
        assertEquals(3, tableS.getRowCount());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(3));
        assertTrue(tableS.isCellEditable(3, 0));
        assertTrue(tableS.isCellEditable(0, 3));
    }

    @Test
    public void testAjouterColonneClassOtherThanString() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        Class<?> type = Double.class;
        String nomNouvelleColonne = "New Column";
        TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(4, tableS.getColumnCount());
        assertEquals(3, tableS.getRowCount());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(3));
        assertFalse(tableS.isCellEditable(3, 0));
        assertTrue(tableS.isCellEditable(0, 3));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterLigne();
        assertEquals(4, tableS.getRowCount());
        assertNull(tableS.getValueAt(3, 0));
        assertNull(tableS.getValueAt(3, 1));
        assertNull(tableS.getValueAt(3, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterLigneWithoutColumns() {
        TableStockage ts = new TableStockage(0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(4, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerColonne(1);
        assertEquals(3, tableS.getColumnCount());
        assertNull(tableS.getValueAt(0, 1));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j != 1) {
                    assertEquals(tableS.getValueAt(0, j), ts.getValueAt(0, j + 1));
                }
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneOutOfBounds() {
        TableStockage ts = new TableStockage(4, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerColonne(5);
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerLigne(1);
        assertEquals(4, tableS.getRowCount());
        assertNull(tableS.getValueAt(1, 0));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != 1) {
                    assertEquals(tableS.getValueAt(i, j), ts.getValueAt(i, j));
                }
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneOutOfBounds() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerLigne(5);
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(4, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.separerColonne(1);
        assertEquals(5, tableS.getColumnCount());
        assertEquals("", tableS.getColumnName(4));
        assertEquals("", tableS.getColumnName(5));
        for (int i = 0; i < 4; i++) {
            String val1 = (String)ts.getValueAt(0, i);
            String val2 = (String)ts.getValueAt(0, i + 1);
            String val1Colonne = tableS.getColumnName(i);
            String val2Colonne = tableS.getColumnName(i + 1);
            assertEquals(val1, tableS.getValueAt(0, i));
            assertEquals(val2, tableS.getValueAt(0, i + 1));
            assertEquals(val1Colonne, tableS.getColumnName(i));
            assertEquals(val2Colonne, tableS.getColumnName(i + 1));
        }
    }

    @Test
    public void testSeparerColonneWithEmptyString() {
        TableStockage ts = new TableStockage(4, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.separerColonne(1);
        assertEquals(5, tableS.getColumnCount());
        assertEquals("", tableS.getColumnName(4));
        assertEquals("", tableS.getColumnName(5));
        for (int i = 0; i < 4; i++) {
            String val1 = (String)ts.getValueAt(0, i);
            if (val1.isEmpty()) {
                String val2 = (String)ts.getValueAt(0, i + 1);
                String val1Colonne = tableS.getColumnName(i);
                String val2Colonne = tableS.getColumnName(i + 1);
                assertEquals("", tableS.getValueAt(0, i));
                assertEquals(val2, tableS.getValueAt(0, i + 1));
                assertEquals(val1Colonne, tableS.getColumnName(i));
                assertEquals(val2Colonne, tableS.getColumnName(i + 1));
                break;
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(6, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
        assertEquals(3, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a: Boolean", tableS.getColumnName(0));
        assertEquals("Column1 String", tableS.getColumnName(1));
        assertEquals("Column2 String", tableS.getColumnName(2));
        for (int i = 0; i < 5; i++) {
            Object val1 = ts.getValueAt(i, 1);
            Object val2 = ts.getValueAt(i, 2);
            assertEquals(val1 + " " + val2, tableS.getValueAt(i, 0));
        }
    }

    @Test
    public void testFusionnerColonnesWithBoolean() {
        TableStockage ts = new TableStockage(6, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
        assertEquals(3, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a: Boolean", tableS.getColumnName(0));
        assertEquals("Column1 String", tableS.getColumnName(1));
        assertTrue(tableS.isCellEditable(0, 0));
        for (int i = 0; i < 5; i++) {
            Object val1 = ts.getValueAt(i, 1);
            Object val2 = ts.getValueAt(i, 2);
            assertEquals(val1.toString() + " " + val2.toString(), tableS.getValueAt(i, 0).toString());
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.colonneMail();
        assertEquals(4, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(3));
        for (int i = 0; i < 3; i++) {
            assertFalse(tableS.isCellEditable(i, 3));
        }
        for (int i = 3; i < 4; i++) {
            assertTrue(tableS.isCellEditable(i, 0));
        }
        for (int i = 0; i < 3; i++) {
            assertNull(tableS.getValueAt(i, 3));
        }
        for (int i = 3; i < 4; i++) {
            assertTrue(tableS.getValueAt(i, 3) instanceof Boolean);
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 2);
        ts.setValueAt("John Doe", 0, 0);
        ts.setValueAt("john.doe@example.com", 0, 1);
        ts.setValueAt("Jane Doe", 1, 0);
        ts.setValueAt("jane.doe@example.com", 1, 1);
        ts.setValueAt("Bob Smith", 2, 0);
        ts.setValueAt("bob.smith@example.com", 2, 1);
        ts.setValueAt("Alice Johnson", 3, 0);
        ts.setValueAt("alice.johnson@example.com", 3, 1);
        ts.setValueAt("Tom Brown", 4, 0);
        ts.setValueAt("tom.brown@example.com", 4, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(5, tableS.getRowCount());
        for (int i = 0; i < 5; i++) {
            if (i < 3) {
                assertEquals(ts.getValueAt(i, 1), tableS.getValueAt(i, 0));
            }
            assertTrue(tableS.getValueAt(i, 1) instanceof String);
        }
    }
}