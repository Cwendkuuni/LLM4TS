package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableStockage originalTable;

    @Before
    public void setUp() {
        // Initialize the original TableStockage
        Object[][] data = {{"Col1", "Val1", "Val2"}, {"Col1", "Val3", "Val4"}, {"Col1", "Val5", "Val6"}};
        String[] columnNames = {"Col1", "Col2", "Col3"};
        originalTable = new TableStockage(data, columnNames);

        // Initialize the ModifTableStockage with the original TableStockage
        modifTableStockage = new ModifTableStockage(originalTable);
    }

    @After
    public void tearDown() {
        modifTableStockage = null;
        originalTable = null;
    }

    @Test
    public void testAjouterColonne() {
        // Test adding a new column with a valid column name and data type
        String newColumnName = "Col4";
        Class<?> newColumnType = String.class;
        TableStockage newTable = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
        assertEquals(4, newTable.getColumnCount());
        assertEquals(newColumnName, newTable.getColumnName(3));

        // Test adding a new column with an existing column name
        String existingColumnName = "Col1";
        TableStockage newTable2 = null;
        try {
            newTable2 = modifTableStockage.ajouterColonne(existingColumnName, newColumnType);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        assertNull(newTable2);

        // Test adding a new column with a null column name
        String nullColumnName = null;
        try {
            modifTableStockage.ajouterColonne(nullColumnName, newColumnType);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test adding a new column with a null data type
        Class<?> nullColumnType = null;
        try {
            modifTableStockage.ajouterColonne(newColumnName, nullColumnType);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testAjouterLigne() {
        // Test adding a new row with valid data for all columns
        Object[] newRowData = {"", "", "", ""};
        TableStockage newTable = modifTableStockage.ajouterLigne();
        assertEquals(5, newTable.getRowCount());
        for (int i = 0; i < newTable.getColumnCount(); ++i) {
            assertEquals("", newTable.getValueAt(newTable.getRowCount() - 1, i));
        }

        // Test adding a new row with valid data for some columns and null data for others
        Object[] newRowData2 = {"", null, "", null};
        TableStockage newTable2 = modifTableStockage.ajouterLigne();
        assertEquals(5, newTable2.getRowCount());
        for (int i = 0; i < newTable2.getColumnCount(); ++i) {
            if (i != 1 && i != 3) {
                assertEquals("", newTable2.getValueAt(newTable2.getRowCount() - 1, i));
            } else {
                assertNull(newTable2.getValueAt(newTable2.getRowCount() - 1, i));
            }
        }

        // Test adding a new row when there are no columns
        modifTableStockage.supprimerToutesLesColonnes();
        TableStockage newTable3 = modifTableStockage.ajouterLigne();
        assertEquals(1, newTable3.getRowCount());
        assertEquals(0, newTable3.getColumnCount());
    }

    @Test
    public void testSupprimerColonne() {
        // Test removing the first column
        TableStockage newTable = modifTableStockage.supprimerColonne(0);
        assertEquals(2, newTable.getColumnCount());
        assertNotEquals("Col1", newTable.getColumnName(0));

        // Test removing the last column
        newTable = modifTableStockage.supprimerColonne(newTable.getColumnCount() - 1);
        assertEquals(1, newTable.getColumnCount());
        assertNotEquals("Col3", newTable.getColumnName(0));

        // Test removing a column that does not exist
        newTable = modifTableStockage.supprimerColonne(newTable.getColumnCount());
        assertEquals(1, newTable.getColumnCount());

        // Test removing all columns
        newTable = modifTableStockage.supprimerToutesLesColonnes();
        assertEquals(0, newTable.getColumnCount());
        assertEquals(0, newTable.getRowCount());
    }

    @Test
    public void testSupprimerLigne() {
        // Test removing the first row
        TableStockage newTable = modifTableStockage.supprimerLigne(0);
        assertEquals(2, newTable.getRowCount());
        assertArrayEquals(new Object[]{""}, newTable.getValueAt(0, 0));

        // Test removing the last row
        newTable = modifTableStockage.supprimerLigne(newTable.getRowCount() - 1);
        assertEquals(2, newTable.getRowCount());
        assertArrayEquals(new Object[]{"", ""}, newTable.getValueAt(1, 0));

        // Test removing a row that does not exist
        newTable = modifTableStockage.supprimerLigne(newTable.getRowCount());
        assertEquals(2, newTable.getRowCount());

        // Test removing all rows
        newTable = modifTableStockage.supprimerToutesLesLignes();
        assertEquals(0, newTable.getRowCount());
        assertEquals(0, newTable.getColumnCount());
    }

    @Test
    public void testSeparerColonne() {
        // Test separating a column with a valid separator character
        TableStockage newTable = modifTableStockage.separerColonne(1);
        assertEquals(3, newTable.getColumnCount());
        assertEquals("Col1", newTable.getColumnName(0));
        assertEquals("Col2", newTable.getColumnName(1));
        assertEquals("Col3", newTable.getColumnName(2));

        // Test separating a column with an invalid separator character
        modifTableStockage.supprimerColonne(1);
        modifTableStockage.ajouterColonne("Col1 Col2", String.class);
        newTable = modifTableStockage.separerColonne(1);
        assertEquals(2, newTable.getColumnCount());
        assertEquals("Col1", newTable.getColumnName(0));
        assertEquals("Col1 Col2", newTable.getColumnName(1));

        // Test separating a column with an empty value
        modifTableStockage.supprimerColonne(1);
        modifTableStockage.ajouterColonne("", String.class);
        newTable = modifTableStockage.separerColonne(0);
        assertEquals(2, newTable.getColumnCount());
        assertEquals("", newTable.getColumnName(0));
        assertEquals("", newTable.getColumnName(1));

        // Test separating a column with a null value
        modifTableStockage.supprimerColonne(1);
        modifTableStockage.ajouterColonne(null, String.class);
        newTable = modifTableStockage.separerColonne(0);
        assertEquals(2, newTable.getColumnCount());
        assertNull(newTable.getColumnName(0));
        assertNull(newTable.getColumnName(1));
    }

    @Test
    public void testFusionnerColonnes() {
        // Test merging two columns with valid data
        TableStockage newTable = modifTableStockage.fusionnerColonnes(1);
        assertEquals(2, newTable.getColumnCount());
        assertEquals("Col1 Col2", newTable.getColumnName(0));

        // Test merging two columns with invalid data types
        modifTableStockage.supprimerColonne(1);
        modifTableStockage.ajouterColonne("Col1", Boolean.class);
        modifTableStockage.ajouterColonne("Col2", Boolean.class);
        newTable = modifTableStockage.fusionnerColonnes(1);
        assertEquals(1, newTable.getColumnCount());
        assertNull(newTable.getColumnName(0));

        // Test merging two columns with an empty column
        modifTableStockage.supprimerColonne(1);
        modifTableStockage.ajouterColonne("Col1", String.class);
        modifTableStockage.ajouterColonne("", String.class);
        newTable = modifTableStockage.fusionnerColonnes(1);
        assertEquals(1, newTable.getColumnCount());
        assertNull(newTable.getColumnName(0));

        // Test merging two columns with null columns
        modifTableStockage.supprimerColonne(1);
        modifTableStockage.ajouterColonne("Col1", String.class);
        modifTableStockage.ajouterColonne(null, String.class);
        newTable = modifTableStockage.fusionnerColonnes(1);
        assertEquals(1, newTable.getColumnCount());
        assertNull(newTable.getColumnName(0));
    }

    @Test
    public void testColonneMail() {
        // Test adding the "Envoyer E-Mail a:" column
        TableStockage newTable = modifTableStockage.colonneMail();
        assertEquals(3, newTable.getColumnCount());
        assertEquals("Envoyer E-Mail a:", newTable.getColumnName(2));

        // Test adding the "Envoyer E-Mail a:" column when there are no columns
        modifTableStockage.supprimerToutesLesColonnes();
        newTable = modifTableStockage.colonneMail();
        assertEquals(1, newTable.getColumnCount());
        assertNull(newTable.getColumnName(0));
    }

    @Test
    public void testSelectMail() {
        // Test selecting all rows with the "Envoyer E-Mail a:" column set to true
        TableStockage newTable = modifTableStockage.selectMail();
        assertEquals(3, newTable.getRowCount());
        for (int i = 0; i < newTable.getRowCount(); ++i) {
            assertTrue((boolean)newTable.getValueAt(i, newTable.getColumnCount() - 1));
        }

        // Test selecting all rows with the "Envoyer E-Mail a:" column set to false
        modifTableStockage.supprimerToutesLesColonnes();
        modifTableStockage.ajouterColonne("Col1", Boolean.class);
        newTable = modifTableStockage.selectMail();
        assertEquals(1, newTable.getRowCount());
        assertFalse((boolean)newTable.getValueAt(0, newTable.getColumnCount() - 1));

        // Test selecting no rows when there are no rows with the "Envoyer E-Mail a:" column set to true
        newTable = modifTableStockage.selectMail();
        assertEquals(0, newTable.getRowCount());
    }
}