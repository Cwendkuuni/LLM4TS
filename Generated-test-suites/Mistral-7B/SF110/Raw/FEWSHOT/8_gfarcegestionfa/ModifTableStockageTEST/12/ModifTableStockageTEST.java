package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.table.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableModel ts = new Object[][] {};
        ModifTableStockage mts = new ModifTableStockage(ts);
        assertEquals(0, mts.getNbColonnes());
        assertEquals(0, mts.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        String nomNouvelleColonne = "NewColumn";
        Class<?> type = String.class;
        TableModel ts = new Object[][] {{ "Column1", "Value1" }, { "Column2", "Value2" }};
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(3, tableS.getColumnCount());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(2));
    }

    @Test
    public void testAjouterColonneWithSameName() {
        String nomNouvelleColonne = "Column1";
        Class<?> type = String.class;
        TableModel ts = new Object[][] {{ "Column1", "Value1" }, { "Column2", "Value2" }};
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(2, tableS.getColumnCount());
        assertEquals("Error: Column name already exists", mts.getMessage());
    }

    @Test
    public void testAjouterLigne() {
        TableModel ts = new Object[][] {{ "Column1", "Value1" }, { "Column2", "Value2" }};
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.ajouterLigne();
        assertEquals(3, tableS.getRowCount());
        assertNull(tableS.getValueAt(2, 0));
    }

    @Test
    public void testSupprimerColonne() {
        String[] columns = { "Column1", "Column2", "Column3" };
        Object[][] data = { { "Value1_1", "Value2_1", "Value3_1" }, { "Value1_2", "Value2_2", "Value3_2" }, { "Value1_3", "Value2_3", "Value3_3" } };
        TableModel ts = new DefaultTableModel(data, columns);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.supprimerColonne(1);
        assertEquals(2, tableS.getColumnCount());
        assertEquals("Column2", mts.getMessage());
    }

    @Test
    public void testSupprimerLigne() {
        String[] columns = { "Column1", "Column2" };
        Object[][] data = { { "Value1_1", "Value2_1" }, { "Value1_2", "Value2_2" }, { "Value1_3", "Value2_3" } };
        TableModel ts = new DefaultTableModel(data, columns);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.supprimerLigne(1);
        assertEquals(2, tableS.getRowCount());
        assertEquals("Error: Index out of range", mts.getMessage());
    }

    @Test
    public void testSeparerColonne() {
        String[] columns = { "Column1 Column2", "Value1 Value2", "Value3 Value4" };
        Object[][] data = { { "Value1_1 Value1_2", "Value2_1 Value2_2", "Value3_1 Value4_1" }, { "Value1_3 Value1_4", "Value2_3 Value2_4", "Value3_2 Value4_2" } };
        TableModel ts = new DefaultTableModel(data, columns);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.separerColonnes(0);
        assertEquals(3, tableS.getColumnCount());
        assertEquals("Column1", tableS.getColumnName(0));
        assertEquals("Column2", tableS.getColumnName(1));
        assertEquals("Error: Column count doesn't match", mts.getMessage());
    }

    @Test
    public void testSeparerColonneWithSingleSpace() {
        String[] columns = { "Column1 Column2", "Value1 Value2", "Value3 Value4" };
        Object[][] data = { { "Value1_1 Value1_2", "Value2_1 Value2_2", "Value3_1 Value4_1" }, { "Value1_3 Value1_4", "Value2_3 Value2_4", "Value3_2 Value4_2" } };
        TableModel ts = new DefaultTableModel(data, columns);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.separerColonne(0);
        assertEquals(3, tableS.getColumnCount());
        assertEquals("Column1", tableS.getColumnName(0));
        assertEquals("Column2", tableS.getColumnName(1));
        assertEquals("Value1_1", tableS.getValueAt(0, 0).toString());
        assertEquals("Value1_2", tableS.getValueAt(0, 1).toString());
    }

    @Test
    public void testSeparerColonneWithMultipleSpaces() {
        String[] columns = { "Column1 Column2", "Value1 Value2", "Value3 Value4" };
        Object[][] data = { { "Value1_1 Value1_2", "Value2_1 Value2_2", "Value3_1 Value4_1" }, { "Value1_3 Value1_4", "Value2_3 Value2_4", "Value3_2 Value4_2" } };
        TableModel ts = new DefaultTableModel(data, columns);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.separerColonne(0);
        assertEquals(3, tableS.getColumnCount());
        assertEquals("Column1", tableS.getColumnName(0));
        assertEquals("Column2", tableS.getColumnName(1));
        assertEquals("Value1_1", tableS.getValueAt(0, 0).toString());
        assertEquals("Value1_2", tableS.getValueAt(0, 1).toString());
    }

    @Test
    public void testFusionnerColonnes() {
        String[] columns = { "Column1", "Boolean1", "Column2" };
        Object[][] data = { { "Value1_1", true, "Value2_1" }, { "Value1_2", false, "Value2_2" }, { "Value1_3", true, "Value2_3" } };
        TableModel ts = new DefaultTableModel(data, columns);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.fusionnerColonnes(1);
        assertEquals(2, tableS.getColumnCount());
        assertEquals("Column1 Boolean1", tableS.getColumnName(0));
    }

    @Test
    public void testFusionnerColonnesWithDifferentTypes() {
        String[] columns = { "Column1", "Boolean1", "Column2" };
        Object[][] data = { { "Value1_1", true, "Value2_1" }, { "Value1_2", 2.5, "Value2_2" }, { "Value1_3", true, "Value2_3" } };
        TableModel ts = new DefaultTableModel(data, columns);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.fusionnerColonnes(1);
        assertEquals(2, tableS.getColumnCount());
        assertEquals("Error: Column types don't match", mts.getMessage());
    }

    @Test
    public void testColonneMail() {
        String[] columns = { "Column1", "Boolean1" };
        Object[][] data = { { "Value1_1", true }, { "Value1_2", false }, { "Value1_3", true } };
        TableModel ts = new DefaultTableModel(data, columns);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.colonneMail();
        assertEquals(3, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(2));
        assertEquals(true, tableS.getValueAt(0, 2).equals(Boolean.TRUE));
        assertEquals(true, tableS.getValueAt(1, 2).equals(Boolean.FALSE));
        assertEquals(true, tableS.getValueAt(2, 2).equals(Boolean.TRUE));
    }

    @Test
    public void testSelectMail() {
        String[] columns = { "Column1", "Boolean1", "Envoyer E-Mail a:" };
        Object[][] data = { { "Value1_1", true, true }, { "Value1_2", false, true }, { "Value1_3", true, false } };
        TableModel ts = new DefaultTableModel(data, columns);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableModel tableS = mts.selectMail();
        assertEquals(2, tableS.getRowCount());
        assertEquals("Value1_1", tableS.getValueAt(0, 0).toString());
        assertEquals(true, tableS.getValueAt(0, 1).equals(Boolean.TRUE));
        assertEquals("Value1_2", tableS.getValueAt(1, 0).toString());
        assertEquals(true, tableS.getValueAt(1, 1).equals(Boolean.TRUE));
    }
}