package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.TableModel;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testConstructor() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", "C2" }, { "A3", "B3", "C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        assertEquals(3, modifTable.getNbLignes());
        assertEquals(3, modifTable.getNbColonnes());
    }

    @Test
    public void testAjouterColonneStringClass() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", "C2" }, { "A3", "B3", "C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        String nomNouvelleColonne = "D";
        Class<?> type = String.class;
        TableStockage newTable = modifTable.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(4, newTable.getNbColonnes());
        assertEquals(3, newTable.getNbLignes());
        assertEquals(nomNouvelleColonne, newTable.getColumnName(3));
        assertNull(newTable.getValueAt(0, 3));
        assertNull(newTable.getValueAt(1, 3));
        assertNull(newTable.getValueAt(2, 3));
    }

    @Test
    public void testAjouterColonneDoubleClass() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", new Double("2.5") }, { "A3", "B3", new Double("3.5") } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        String nomNouvelleColonne = "D";
        Class<?> type = Double.class;
        TableStockage newTable = modifTable.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(4, newTable.getNbColonnes());
        assertEquals(3, newTable.getNbLignes());
        assertEquals(nomNouvelleColonne, newTable.getColumnName(3));
        assertEquals(0.0, newTable.getValueAt(0, 3), 0.0);
        assertEquals(2.5, newTable.getValueAt(1, 3), 0.0);
        assertEquals(3.5, newTable.getValueAt(2, 3), 0.0);
    }

    @Test
    public void testAjouterColonneBooleanClass() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", new Boolean(true) }, { "A3", "B3", new Boolean(false) } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        String nomNouvelleColonne = "D";
        Class<?> type = Boolean.class;
        TableStockage newTable = modifTable.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(4, newTable.getNbColonnes());
        assertEquals(3, newTable.getNbLignes());
        assertEquals(nomNouvelleColonne, newTable.getColumnName(3));
        assertFalse((Boolean)newTable.getValueAt(0, 3));
        assertTrue((Boolean)newTable.getValueAt(1, 3));
        assertFalse((Boolean)newTable.getValueAt(2, 3));
    }

    @Test
    public void testAjouterLigne() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", "C2" }, { "A3", "B3", "C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        TableStockage newTable = modifTable.ajouterLigne();
        assertEquals(4, newTable.getNbLignes());
        assertEquals(3, newTable.getNbColonnes());
        assertNull(newTable.getValueAt(3, 0));
        assertNull(newTable.getValueAt(3, 1));
        assertNull(newTable.getValueAt(3, 2));
        if (newTable.getColumnClass(0).equals(String.class)) {
            assertEquals("", newTable.getValueAt(3, 0));
        } else if (newTable.getColumnClass(0).equals(Double.class)) {
            assertEquals(0.0, newTable.getValueAt(3, 0), 0.0);
        } else {
            assertEquals(false, newTable.getValueAt(3, 0));
        }
        if (newTable.getColumnClass(1).equals(String.class)) {
            assertNull(newTable.getValueAt(3, 1));
        } else if (newTable.getColumnClass(1).equals(Double.class)) {
            assertEquals(0.0, newTable.getValueAt(3, 1), 0.0);
        } else {
            assertEquals(false, newTable.getValueAt(3, 1));
        }
        if (newTable.getColumnClass(2).equals(String.class)) {
            assertNull(newTable.getValueAt(3, 2));
        } else if (newTable.getColumnClass(2).equals(Double.class)) {
            assertEquals(0.0, newTable.getValueAt(3, 2), 0.0);
        } else {
            assertEquals(false, newTable.getValueAt(3, 2));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterLigneWithoutColumns() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", "C2" }, { "A3", "B3", "C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        modifTable.setNbColonnes(0);
        TableStockage newTable = modifTable.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", "C2" }, { "A3", "B3", "C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        TableStockage newTable = modifTable.ajouterColonne("D", Double.class);
        modifTable.setNbColonnes(5);
        TableStockage tableToRemove = modifTable.supprimerColonne(3);
        assertEquals(4, tableToRemove.getNbColonnes());
        assertEquals(3, tableToRemove.getNbLignes());
        assertEquals("D", modifTable.getColumnName(3));
    }

    @Test
    public void testSupprimerLigne() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", "C2" }, { "A3", "B3", "C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        TableStockage newTable = modifTable.ajouterLigne();
        modifTable.setNbLignes(4);
        TableStockage tableToRemove = modifTable.supprimerLigne(2);
        assertEquals(3, tableToRemove.getNbLignes());
        assertEquals(3, tableToRemove.getNbColonnes());
        assertEquals("A3", modifTable.getColumnName(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneOutOfBounds() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", "C2" }, { "A3", "B3", "C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        modifTable.setNbColonnes(3);
        modifTable.supprimerColonne(4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneOutOfBounds() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", "C2" }, { "A3", "B3", "C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        modifTable.setNbLignes(3);
        modifTable.supprimerLigne(4);
    }

    @Test
    public void testSeparerColonne() {
        TableModel oldTable = new Object[][] { { "A1 A2", "B1 B2", "C1 C2" }, { "A2 A3 A3", "B2 B3 B3", "C2 C3 C3" }, { "A3 A3 A3", "B3 B3 B3", "C3 C3 C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        TableStockage newTable = modifTable.separerColonne(0);
        assertEquals(3, newTable.getNbColonnes());
        assertEquals(3, newTable.getNbLignes());
        assertEquals("A1", newTable.getColumnName(0));
        assertEquals("A2", newTable.getColumnName(1));
        assertEquals("A3", newTable.getColumnName(2));
        for (int i = 0; i < 3; ++i) {
            assertEquals("A1", newTable.getColumnName(i));
            assertEquals("A2", newTable.getColumnName(i + 1));
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                assertEquals(oldTable.getValueAt(i, j), newTable.getValueAt(i, j));
                assertEquals(oldTable.getValueAt(i, j + 1), newTable.getValueAt(i, j + 1));
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableModel oldTable = new Object[][] { { "A1 A1", "B1 B1", "C1 C1" }, { "A2 A2 A2", "B2 B2 B2", "C2 C2 C2" }, { "A3 A3 A3", "B3 B3 B3", "C3 C3 C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        TableStockage newTable = modifTable.fusionnerColonnes(0);
        assertEquals(3, newTable.getNbColonnes());
        assertEquals(3, newTable.getNbLignes());
        assertEquals("A1 A1 B1 B1", newTable.getColumnName(0));
        assertEquals("A2 A2 B2 B2", newTable.getColumnName(1));
        assertEquals("A3 A3 B3 B3", newTable.getColumnName(2));
        for (int i = 0; i < 3; ++i) {
            assertEquals("A1 A1", newTable.getColumnName(i));
            assertEquals("B1 B1", newTable.getColumnName(i + 1));
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                assertEquals(oldTable.getValueAt(i, j), newTable.getValueAt(i, j));
                assertEquals(oldTable.getValueAt(i, j + 1), newTable.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1" }, { "A2", "B2", "C2" }, { "A3", "B3", "C3" } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        TableStockage newTable = modifTable.colonneMail();
        assertEquals(4, newTable.getNbColonnes());
        assertEquals(3, newTable.getNbLignes());
        assertEquals("Envoyer E-Mail a:", newTable.getColumnName(3));
        for (int i = 0; i < 3; ++i) {
            assertNull(newTable.getValueAt(i, 3));
        }
    }

    @Test
    public void testSelectMail() {
        TableModel oldTable = new Object[][] { { "A1", "B1", "C1", true }, { "A2", "B2", "C2", true }, { "A3", "B3", "C3", false } };
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(oldTable));
        TableStockage newTable = modifTable.selectMail();
        assertEquals(3, newTable.getNbColonnes());
        assertEquals(2, newTable.getNbLignes());
        assertEquals("A2", newTable.getColumnName(0));
        assertEquals("B2", newTable.getColumnName(1));
        assertEquals("A2", newTable.getValueAt(0, 0));
        assertEquals("B2", newTable.getValueAt(0, 1));
        assertEquals("A3", newTable.getValueAt(1, 0));
        assertEquals("B3", newTable.getValueAt(1, 1));
    }
}