import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(2, modifTableStockage.getNbLignes());
        assertEquals(2, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonneStringClass() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.ajouterColonne("NewCol", String.class);
        assertEquals(3, result.getColumnCount());
        assertEquals("NewCol", result.getColumnName(2));
    }

    @Test
    public void testAjouterColonneClassNotFound() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        try {
            modifTableStockage.ajouterColonne("NewCol", new Object().getClass());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(2, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterLigne() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.ajouterLigne();
        assertEquals(3, result.getRowCount());
        assertNull(result.getValueAt(2, 0));
        assertNull(result.getValueAt(2, 1));
    }

    @Test
    public void testAjouterLigneNoColumns() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.setNbColonnes(0);
        TableModel result = modifTableStockage.ajouterLigne();
        assertEquals(2, result.getRowCount());
        assertEquals("ERREUR", modifTableStockage.getErrorMessage());
    }

    @Test
    public void testSupprimerColonneIndexOutOfBounds() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.supprimerColonne(-1);
        assertEquals(2, result.getColumnCount());
        assertEquals("ERREUR", modifTableStockage.getErrorMessage());
    }

    @Test
    public void testSupprimerColonneLastCol() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.supprimerColonne(1);
        assertEquals(1, result.getColumnCount());
        assertEquals("Col1", result.getColumnName(0));
    }

    @Test
    public void testSupprimerLigneIndexOutOfBounds() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.supprimerLigne(-1);
        assertEquals(2, result.getRowCount());
        assertEquals("ERREUR", modifTableStockage.getErrorMessage());
    }

    @Test
    public void testSeparerColonneSuccess() {
        TableModel ts = new Object[][]{{"Col1 Col2", "Val1 Val2"}, {"Col1 Col2", "Val3 Val4"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.separerColonne(0);
        assertEquals(4, result.getColumnCount());
        assertEquals("Col1", result.getColumnName(0));
        assertEquals("Col2", result.getColumnName(1));
        assertEquals("Val1", result.getValueAt(0, 0).toString());
        assertEquals("Val2", result.getValueAt(0, 1).toString());
    }

    @Test
    public void testSeparerColonneFailure() {
        TableModel ts = new Object[][]{{"Col1 Col2", "Val1 Val2"}, {"Col1 Col2", "Val3 Val4"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.separerColonne(1);
        assertEquals(2, result.getColumnCount());
        assertEquals("ERREUR", modifTableStockage.getErrorMessage());
    }

    @Test
    public void testFusionnerColonnesSuccess() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"true", "false"}, {"true", "false"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.fusionnerColonnes(1);
        assertEquals(3, result.getColumnCount());
        assertEquals("Col1 Col2", result.getColumnName(0));
        assertEquals("false", result.getValueAt(0, 2).toString());
    }

    @Test
    public void testFusionnerColonnesFailure() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"true", "false"}, {"true", "false"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.fusionnerColonnes(0);
        assertEquals(2, result.getColumnCount());
        assertEquals("ERREUR", modifTableStockage.getErrorMessage());
    }

    @Test
    public void testColonneMail() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.colonneMail();
        assertEquals(3, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
        assertTrue(result.getValueAt(0, 2) instanceof Boolean);
        assertTrue((Boolean)result.getValueAt(0, 2));
    }

    @Test
    public void testSelectMail() {
        TableModel ts = new Object[][]{{"Col1", "Col2", "Envoyer E-Mail a:"}, {"Val1", "Val2", true}, {"Val3", "Val4", false}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.selectMail();
        assertEquals(2, result.getRowCount());
        assertEquals("Col1", result.getColumnName(0));
        assertEquals("Col2", result.getColumnName(1));
        assertTrue(result.getValueAt(0, 2) instanceof Boolean);
        assertTrue((Boolean)result.getValueAt(0, 2));
        assertNull(result.getValueAt(1, 2));
    }

    @Test
    public void testSelectMailNoMailColumn() {
        TableModel ts = new Object[][]{{"Col1", "Col2"}, {"Val1", "Val2"}};
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel result = modifTableStockage.selectMail();
        assertEquals(2, result.getRowCount());
        assertEquals("Col1", result.getColumnName(0));
        assertEquals("Col2", result.getColumnName(1));
        assertFalse(result.getValueAt(0, 2) instanceof Boolean);
        assertEquals("", result.getValueAt(0, 2).toString());
    }

    private String getErrorMessage(ModifTableStockage modifTableStockage) {
        return (String)modifTableStockage.getErrorMessage();
    }
}