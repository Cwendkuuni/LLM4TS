import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.TableModel;

import static org.junit.Assert.*;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableStockage tableStockage;

    @Before
    public void setUp() {
        tableStockage = new TableStockage(3, 2);
        tableStockage.setColumnName(0, "Name");
        tableStockage.setColumnName(1, "Age");

        modifTableStockage = new ModifTableStockage(tableStockage);
    }

    @After
    public void tearDown() {
        modifTableStockage = null;
        tableStockage = null;
    }

    @Test
    public void testModifTableStockageConstruction() {
        assertEquals("Unexpected number of columns", 2, modifTableStockage.getNbColonnes());
        assertEquals("Unexpected number of rows", 4, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonneStringClass() {
        TableStockage result = modifTableStockage.ajouterColonne("New Column", String.class);
        assertEquals("Unexpected number of columns", 3, result.getColumnCount());
        assertEquals("Unexpected column name", "New Column", result.getColumnName(2));
    }

    @Test
    public void testAjouterColonneDoubleClass() {
        TableStockage result = modifTableStockage.ajouterColonne("New Column", Double.class);
        assertEquals("Unexpected number of columns", 3, result.getColumnCount());
        assertEquals("Unexpected column name", "New Column", result.getColumnName(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneInvalidClass() {
        modifTableStockage.ajouterColonne("New Column", Integer.TYPE);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals("Unexpected number of rows", 5, result.getRowCount());
        assertEquals("Unexpected value at (0, 0)", "", result.getValueAt(4, 0));
        assertEquals("Unexpected value at (0, 1)", "", result.getValueAt(4, 1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneNoColumns() {
        modifTableStockage = new ModifTableStockage(new TableModel() {});
        modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage result = modifTableStockage.ajouterColonne("New Column", String.class);
        modifTableStockage.supprimerColonne(2);
        assertEquals("Unexpected number of columns", 2, modifTableStockage.getNbColonnes());
        assertFalse("Unexpected column name", modifTableStockage.getColumnName(1).equals("New Column"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneInvalidIndex() {
        modifTableStockage.supprimerColonne(3);
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage result = modifTableStockage.ajouterLigne();
        modifTableStockage.supprimerLigne(0);
        assertEquals("Unexpected number of rows", 4, modifTableStockage.getNbLignes());
        assertNull("Unexpected value at (0, 0)", modifTableStockage.getValueAt(0, 0));
        assertNull("Unexpected value at (0, 1)", modifTableStockage.getValueAt(0, 1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneInvalidIndex() {
        modifTableStockage.supprimerLigne(5);
    }

    @Test
    public void testSeparerColonne() {
        modifTableStockage = new ModifTableStockage(new TableModel() {
            {
                setValueAt("James  25", 0, 0);
                setValueAt("Mary  30 John 35", 1, 0);
                setValueAt("Tom  28 Susan 32", 2, 0);
            }
        });
        TableStockage result = modifTableStockage.separerColonne(0);
        assertEquals("Unexpected number of columns", 3, result.getColumnCount());
        assertArrayEquals("Unexpected values in the first table",
                new Object[]{"James", "25"}, result.getValues(0));
        assertArrayEquals("Unexpected values in the second table",
                new Object[]{"", "25"}, result.getValues(1));
        assertArrayEquals("Unexpected values in the third table",
                new Object[]{"Mary", "John"}, result.getValues(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneInvalidIndex() {
        modifTableStockage.separerColonne(3);
    }

    @Test
    public void testFusionnerColonnes() {
        modifTableStockage = new ModifTableStockage(new TableModel() {
            {
                setValueAt("true", 0, 0);
                setValueAt("false", 1, 0);
                setValueAt("false", 0, 1);
                setValueAt("true", 1, 1);
            }
        });
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        assertEquals("Unexpected number of columns", 1, result.getColumnCount());
        assertEquals("Unexpected value at (0, 0)", "false,false", result.getValueAt(0, 0).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesInvalidIndex() {
        modifTableStockage.fusionnerColonnes(1);
    }
}