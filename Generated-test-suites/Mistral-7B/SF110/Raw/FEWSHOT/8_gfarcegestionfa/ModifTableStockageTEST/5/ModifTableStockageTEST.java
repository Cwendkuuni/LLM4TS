package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockage() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(3, modifTableStockage.getNbColonnes());
        assertEquals(3, modifTableStockage.getNbLignes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModifTableStockageNullTableStockage() {
        new ModifTableStockage(null);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 3);
        String nomNouvelleColonne = "newCol";
        Class<?> type = Integer.class;
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(4, tableS.getColumnCount());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(3));
        assertEquals(type, tableS.getColumnClass(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneNullNom() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne(null, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneNullType() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("newCol", null);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterLigne();
        assertEquals(4, tableS.getRowCount());
        for (int i = 0; i < tableS.getRowCount(); i++) {
            for (int j = 0; j < tableS.getColumnCount(); j++) {
                Object value = null;
                if (j < 3) {
                    value = ts.getValueAt(i, j);
                }
                assertEquals(value, tableS.getValueAt(i, j));
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneEmptyTable() {
        TableStockage ts = new TableStockage(0, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(4, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerColonne(1);
        assertEquals(3, tableS.getColumnCount());
        for (int j = 0; j < tableS.getColumnCount(); j++) {
            assertFalse(tableS.getColumnName(j).equals("newCol"));
        }
        TableStockage ts2 = new TableStockage(4, 4);
        ModifTableStockage modifTableStockage2 = new ModifTableStockage(ts2);
        tableS = modifTableStockage2.supprimerColonne(3);
        assertTrue(tableS.getColumnCount() == 3);
        assertTrue(tableS.getColumnName(2).equals("newCol"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneNegativeIndex() {
        TableStockage ts = new TableStockage(4, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.supprimerColonne(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneLastColumn() {
        TableStockage ts = new TableStockage(4, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.supprimerColonne(3);
        assertEquals(3, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(4, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerLigne(1);
        assertEquals(3, tableS.getRowCount());
        for (int i = 0; i < tableS.getRowCount(); i++) {
            for (int j = 0; j < tableS.getColumnCount(); j++) {
                Object value = null;
                if (i < 2) {
                    value = ts.getValueAt(i, j);
                }
                assertEquals(value, tableS.getValueAt(i, j));
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneNegativeIndex() {
        TableStockage ts = new TableStockage(4, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.supprimerLigne(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneLastLine() {
        TableStockage ts = new TableStockage(4, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.supprimerLigne(3);
        assertEquals(3, modifTableStockage.getNbLignes());
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(4, 1);
        ts.setValueAt("1 2", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.separerColonne(0);
        assertEquals(2, tableS.getColumnCount());
        assertEquals("1", tableS.getColumnName(0));
        assertEquals("2", tableS.getColumnName(1));
        assertArrayEquals(new Object[] { null, null }, tableS.getValueAt(0, 1));

        Object[] expected = { "1", "2" };
        Object[] actual = (Object[]) tableS.getValueAt(0, 0);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSeparerColonneNoSpace() {
        TableStockage ts = new TableStockage(4, 1);
        ts.setValueAt("12", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.separerColonne(0);
        assertEquals(1, tableS.getColumnCount());
        assertEquals("12", tableS.getColumnName(0));
        assertEquals("12", tableS.getValueAt(0, 0).toString());
    }

    @Test
    public void testSeparerColonneEmptyCell() {
        TableStockage ts = new TableStockage(4, 1);
        ts.setValueAt("", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.separerColonne(0);
        assertEquals(0, tableS.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(4, 2);
        ts.setColumnName(0, "col1");
        ts.setColumnName(1, "col2");
        ts.setValueAt("true", 0, 1);
        ts.setValueAt("false", 1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
        assertEquals(3, tableS.getColumnCount());
        assertEquals("col1", tableS.getColumnName(0));
        assertEquals("col1 col2", tableS.getColumnName(1));
        assertEquals(2, tableS.getRowCount());
        assertArrayEquals(new Object[] { "true", "true" }, tableS.getColumnValues(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesDifferentTypes() {
        TableStockage ts = new TableStockage(4, 2);
        ts.setColumnName(0, "col1");
        ts.setColumnName(1, "col2");
        ts.setValueAt("true", 0, 1);
        ts.setValueAt(1.0, 1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.fusionnerColonnes(1);
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.colonneMail();
        assertEquals(4, tableS.getColumnCount());
        assertTrue(tableS.getColumnName(3).equals("Envoyer E-Mail a:"));
        assertTrue(tableS.getColumnClass(3).equals(Boolean.class));
        for (int i = 0; i < tableS.getRowCount(); i++) {
            assertEquals(true, tableS.getValueAt(i, 3));
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 4);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Email");
        ts.setColumnName(3, "Send Mail");
        ts.setValueAt("Alice", 0, 0);
        ts.setValueAt(30, 0, 1);
        ts.setValueAt("alice@example.com", 0, 2);
        ts.setValueAt(true, 0, 3);
        ts.setValueAt("Bob", 1, 0);
        ts.setValueAt(25, 1, 1);
        ts.setValueAt("bob@example.com", 1, 2);
        ts.setValueAt(false, 1, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(2, tableS.getRowCount());
        TableStockage expectedTable = new TableStockage(2, 4);
        expectedTable.setColumnName(0, "Name");
        expectedTable.setColumnName(1, "Age");
        expectedTable.setColumnName(2, "Email");
        expectedTable.setColumnName(3, "Send Mail");
        expectedTable.setValueAt("Alice", 0, 0);
        expectedTable.setValueAt(30, 0, 1);
        expectedTable.setValueAt("alice@example.com", 0, 2);
        expectedTable.setValueAt(true, 0, 3);
        assertEquals(expectedTable, tableS);
    }

    @Test
    public void testSelectMailNoEmailColumn() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setValueAt("Alice", 0, 0);
        ts.setValueAt(30, 0, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.selectMail();
        JOptionPane.showMessageDialog(null, "Le mail sera envoye a toute la liste", "Information", 1);
        assertEquals(ts, tableS);
    }
}