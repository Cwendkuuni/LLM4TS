package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableModel ts = new Object[1][1];
        ModifTableStockage modifTableStockage = new ModifTableStockage((TableModel) ts);
        assertEquals(0, modifTableStockage.getNbColonnes());
        assertEquals(0, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonneString() {
        String[] columnNames = {"Column1"};
        Object[][] data = {{1}, {2}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel newTableModel = modifTableStockage.ajouterColonne("Column2", String.class);
        assertEquals(2, newTableModel.getColumnCount());
        assertEquals("Column2", newTableModel.getColumnName(1));
    }

    @Test
    public void testAjouterColonneDouble() {
        String[] columnNames = {"Column1"};
        Object[][] data = {{1.0}, {2.0}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel newTableModel = modifTableStockage.ajouterColonne("Column2", Double.class);
        assertEquals(2, newTableModel.getColumnCount());
        assertEquals("Column2", newTableModel.getColumnName(1));
    }

    @Test
    public void testAjouterColonneBoolean() {
        String[] columnNames = {"Column1"};
        Object[][] data = {{true}, {false}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel newTableModel = modifTableStockage.ajouterColonne("Column2", Boolean.class);
        assertEquals(2, newTableModel.getColumnCount());
        assertEquals("Column2", newTableModel.getColumnName(1));
    }

    @Test
    public void testAjouterLigne() {
        String[] columnNames = {"Column1"};
        Object[][] data = {{1}, {2}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel newTableModel = modifTableStockage.ajouterLigne();
        assertEquals(3, newTableModel.getRowCount());
        assertNull(newTableModel.getValueAt(2, 0));
    }

    @Test
    public void testSupprimerColonne() {
        String[] columnNames = {"Column1", "Column2"};
        Object[][] data = {{1, "A"}, {2, "B"}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel newTableModel = modifTableStockage.supprimerColonne(1);
        assertEquals(1, newTableModel.getColumnCount());
        assertNull(newTableModel.getValueAt(0, 0));
    }

    @Test
    public void testSupprimerLigne() {
        String[] columnNames = {"Column1", "Column2"};
        Object[][] data = {{1, "A"}, {2, "B"}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel newTableModel = modifTableStockage.supprimerLigne(0);
        assertEquals(1, newTableModel.getRowCount());
        assertNull(newTableModel.getValueAt(0, 0));
    }

    @Test
    public void testSeparerColonne() {
        String[] columnNames = {"Column1"};
        Object[][] data = {{1}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel newTableModel = modifTableStockage.separerColonne(0);
        assertEquals(2, newTableModel.getColumnCount());
        assertNull(newTableModel.getValueAt(0, 0));
        assertNull(newTableModel.getValueAt(0, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneFail() {
        String[] columnNames = {"Column1"};
        Object[][] data = {{1}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.separerColonne(-1);
    }

    @Test
    public void testFusionnerColonnes() {
        String[] columnNames = {"Column1", "Column2"};
        Object[][] data = {{1, "A"}, {2, "B"}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel newTableModel = modifTableStockage.fusionnerColonnes(1);
        assertEquals(2, newTableModel.getColumnCount());
        assertEquals("Column1 Column2", newTableModel.getColumnName(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesFail() {
        String[] columnNames = {"Column1"};
        Object[][] data = {{1}};
        TableModel ts = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.fusionnerColonnes(-1);
    }
}