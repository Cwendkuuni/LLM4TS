package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableModel tableModel = new Object[1][1];
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        assertEquals(0, modifTableStockage.getNbColonnes());
        assertEquals(0, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        String[] columnNames = {"Column1", "Column2"};
        Object[][] data = {{"Data1_1", "Data1_2"}, {"Data2_1", "Data2_2"}};
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        String newColumnName = "New Column";
        Class<?> newColumnType = String.class;
        TableModel newTableModel = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
        assertEquals(3, newTableModel.getColumnCount());
        assertEquals(newColumnName, newTableModel.getColumnName(2));
    }

    @Test
    public void testAjouterLigne() {
        String[] columnNames = {"Column1", "Column2"};
        Object[][] data = {{"Data1_1", "Data1_2"}, {"Data2_1", "Data2_2"}};
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        TableModel newTableModel = modifTableStockage.ajouterLigne();
        assertEquals(3, newTableModel.getRowCount());
        assertEquals(columnNames[0], newTableModel.getColumnName(0));
        assertEquals(columnNames[1], newTableModel.getColumnName(1));
        assertEquals("", newTableModel.getValueAt(2, 0).toString());
        assertEquals("", newTableModel.getValueAt(2, 1).toString());
    }

    @Test
    public void testSupprimerColonne() {
        String[] columnNames = {"Column1", "Column2", "Column3"};
        Object[][] data = {{"Data1_1", "Data1_2", "Data1_3"}, {"Data2_1", "Data2_2", "Data2_3"}};
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        int indexToRemove = 1;
        TableModel newTableModel = modifTableStockage.supprimerColonne(indexToRemove);
        assertEquals(2, newTableModel.getColumnCount());
        assertNotEquals(columnNames[1], newTableModel.getColumnName(1));
    }

    @Test
    public void testSupprimerLigne() {
        String[] columnNames = {"Column1", "Column2"};
        Object[][] data = {{"Data1_1", "Data1_2"}, {"Data2_1", "Data2_2"}};
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        int indexToRemove = 0;
        TableModel newTableModel = modifTableStockage.supprimerLigne(indexToRemove);
        assertEquals(1, newTableModel.getRowCount());
        assertNotEquals(data[0][0], newTableModel.getValueAt(0, 0).toString());
    }

    @Test
    public void testSeparerColonne() {
        String[] columnNames = {"Column1 Column2", "Data"};
        Object[][] data = {{"Data1_1 Data1_2", "Data1"}, {"Data2_1 Data2_2", "Data2"}};
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        int indexToSeparate = 0;
        TableModel newTableModel = modifTableStockage.separerColonne(indexToSeparate);
        assertEquals(3, newTableModel.getColumnCount());
        assertEquals("Column1", newTableModel.getColumnName(0));
        assertEquals("Column2", newTableModel.getColumnName(1));
        assertEquals("Data", newTableModel.getColumnName(2));
    }

    @Test
    public void testFusionnerColonnes() {
        String[] columnNames = {"Column1", "Column2", "Column3"};
        Object[][] data = {{"Data1_1", "true", "Data1_3"}, {"Data2_1", "false", "Data2_3"}};
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        int indexToFuse = 1;
        TableModel newTableModel = modifTableStockage.fusionnerColonnes(indexToFuse);
        assertEquals(3, newTableModel.getColumnCount());
        assertEquals("Column1 Column2", newTableModel.getColumnName(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneInvalidType() {
        String[] columnNames = {"Column1", 1, "Column3"};
        Object[][] data = {{"Data1_1", "Data1_2", "Data1_3"}, {"Data2_1", "Data2_2", "Data2_3"}};
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        int indexToSeparate = 1;
        modifTableStockage.separerColonnes(indexToSeparate);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSupprimerColonneInvalidIndex() {
        String[] columnNames = {"Column1", "Column2", "Column3"};
        Object[][] data = {{"Data1_1", "Data1_2", "Data1_3"}, {"Data2_1", "Data2_2", "Data2_3"}};
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        int indexToRemove = 4;
        modifTableStockage.supprimerColonne(indexToRemove);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSupprimerLigneInvalidIndex() {
        String[] columnNames = {"Column1", "Column2"};
        Object[][] data = {{"Data1_1", "Data1_2"}, {"Data2_1", "Data2_2"}};
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) tableModel);
        int indexToRemove = 4;
        modifTableStockage.supprimerLigne(indexToRemove);
    }
}