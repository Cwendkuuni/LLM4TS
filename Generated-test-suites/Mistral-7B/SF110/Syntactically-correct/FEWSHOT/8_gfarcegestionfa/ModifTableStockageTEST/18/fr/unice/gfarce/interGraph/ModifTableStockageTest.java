package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import java.awt.Component;
import java.util.Arrays;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(ts.getRowCount(), modifTableStockage.getNbLignes() - 1);
        assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        String nomNouvelleColonne = "newColumn";
        Class<?> type = String.class;
        TableStockage newTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
        String[] columnNames = newTable.getColumnNames();
        Arrays.sort(columnNames);
        assertArrayEquals(new String[]{"", "type1", "type2", nomNouvelleColonne}, columnNames);
    }

    @Test
    public void testAjouterColonneWithExistingName() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        String nomNouvelleColonne = "type1";
        Class<?> type = String.class;
        TableStockage newTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
        String[] columnNames = newTable.getColumnNames();
        Arrays.sort(columnNames);
        assertArrayEquals(new String[]{"", "type1", "type1", "type2"}, columnNames);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithNullName() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        String nomNouvelleColonne = null;
        Class<?> type = String.class;
        modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithNullType() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        String nomNouvelleColonne = "newColumn";
        Class<?> type = null;
        modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        TableStockage newTable = modifTableStockage.ajouterLigne();
        assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
        Object[] values = newTable.getValues();
        Arrays.fill(values, "");
        for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
            for (int k = 0; k < modifTableStockage.getNbColonnes(); ++k) {
                assertEquals(modifTableStockage.getValueAt(i, k), newTable.getValueAt(i, k));
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage originalTable = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        originalTable.setValueAt("test test", 0, 0);
        originalTable.setValueAt("test  test", 1, 0);
        originalTable.setValueAt("test", 2, 0);
        TableStockage newTable = modifTableStockage.separerColonne(0);
        assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
        assertEquals(2, newTable.getColumnCount());
        String[] columnNames = newTable.getColumnNames();
        Arrays.sort(columnNames);
        assertArrayEquals(new String[]{"", "newColumn1", "newColumn2"}, columnNames);
        Object[] values2 = newTable.getValues();
        Arrays.fill(values2, "");
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            assertEquals(modifTableStockage.getValueAt(i, 0), newTable.getValueAt(i, 0));
            assertEquals(modifTableStockage.getValueAt(i, 1).toString().split(" ")[0], newTable.getValueAt(i, 1).toString());
            assertEquals(modifTableStockage.getValueAt(i, 1).toString().split(" ")[1], newTable.getValueAt(i, 2).toString());
        }
    }

    @Test
    public void testSeparerColonneWithOnlyOneSpace() {
        TableStockage originalTable = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        originalTable.setValueAt("test test", 0, 0);
        originalTable.setValueAt("test test", 1, 0);
        originalTable.setValueAt("test", 2, 0);
        TableStockage newTable = modifTableStockage.separerColonne(0);
        assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
        assertEquals(2, newTable.getColumnCount());
        String[] columnNames = newTable.getColumnNames();
        Arrays.sort(columnNames);
        assertArrayEquals(new String[]{"", "newColumn1", "newColumn2"}, columnNames);
        Object[] values2 = newTable.getValues();
        Arrays.fill(values2, "");
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            assertEquals(modifTableStockage.getValueAt(i, 0), newTable.getValueAt(i, 0));
            assertEquals(modifTableStockage.getValueAt(i, 0), newTable.getValueAt(i, 1));
            assertNull(newTable.getValueAt(i, 2));
        }
    }

    @Test
    public void testSeparerColonneWithNoSpace() {
        TableStockage originalTable = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        originalTable.setValueAt("testtest", 0, 0);
        originalTable.setValueAt("testtest", 1, 0);
        originalTable.setValueAt("test", 2, 0);
        try {
            modifTableStockage.separerColonne(0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(modifTableStockage.getNbLignes(), originalTable.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), originalTable.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        originalTable.setValueAt(true, 0, 0);
        originalTable.setValueAt(false, 0, 1);
        originalTable.setValueAt(true, 0, 2);
        originalTable.setValueAt(false, 1, 0);
        originalTable.setValueAt(true, 1, 1);
        originalTable.setValueAt(false, 1, 2);
        originalTable.setValueAt(true, 2, 0);
        originalTable.setValueAt(true, 2, 1);
        originalTable.setValueAt(false, 2, 2);
        TableStockage newTable = modifTableStockage.fusionnerColonnes(0);
        assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes() - 1, newTable.getColumnCount());
        String[] columnNames = newTable.getColumnNames();
        Arrays.sort(columnNames);
        assertArrayEquals(new String[]{"", "newColumn"}, columnNames);
        Object[] values = newTable.getValues();
        Arrays.fill(values, "");
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            for (int k = 0; k < modifTableStockage.getNbColonnes() - 1; ++k) {
                assertEquals(modifTableStockage.getValueAt(i, k) + " " + modifTableStockage.getValueAt(i, k + 1), newTable.getValueAt(i, k));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesWithDifferentTypes() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        originalTable.setValueAt(true, 0, 0);
        originalTable.setValueAt(false, 0, 1);
        originalTable.setValueAt(true, 0, 2);
        originalTable.setValueAt("test", 1, 0);
        originalTable.setValueAt(3.0, 1, 1);
        originalTable.setValueAt(false, 1, 2);
        originalTable.setValueAt(true, 2, 0);
        originalTable.setValueAt(true, 2, 1);
        originalTable.setValueAt(false, 2, 2);
        modifTableStockage.fusionnerColonnes(0);
    }

    @Test
    public void testColonneMail() {
        TableStockage originalTable = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        TableStockage newTable = modifTableStockage.colonneMail();
        assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes() + 1, newTable.getColumnCount());
        String[] columnNames = newTable.getColumnNames();
        Arrays.sort(columnNames);
        assertArrayEquals(new String[]{"", "type1", "type2", "Envoyer E-Mail a:"}, columnNames);
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            assertEquals(true, newTable.getValueAt(i, newTable.getColumnCount() - 1));
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        originalTable.setValueAt(true, 0, 2);
        originalTable.setValueAt(true, 1, 2);
        originalTable.setValueAt(false, 2, 2);
        TableStockage newTable = modifTableStockage.selectMail();
        assertEquals(1, newTable.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
        for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
            assertEquals(originalTable.getValueAt(0, i), newTable.getValueAt(0, i));
        }
    }

    @Test
    public void testSelectMailWithNoMailToSend() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        originalTable.setValueAt(false, 0, 2);
        originalTable.setValueAt(false, 1, 2);
        originalTable.setValueAt(false, 2, 2);
        TableStockage newTable = modifTableStockage.selectMail();
        assertEquals(0, newTable.getRowCount());
    }

    @Test
    public void testGettersAndSetters() {
        TableStockage originalTable = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(originalTable);
        assertEquals(originalTable, modifTableStockage.getTableStockage());
        modifTableStockage.setTableStockage(new TableStockage(3, 3));
        assertNotEquals(originalTable, modifTableStockage.getTableStockage());
        assertEquals(3, modifTableStockage.getNbLignes());
        assertEquals(3, modifTableStockage.getNbColonnes());
    }
}