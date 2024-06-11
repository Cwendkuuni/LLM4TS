package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        JTable table = new JTable();
        ModifTableStockage modifTableStockage = new ModifTableStockage(table.getModel());
        assertEquals(table.getModel().getColumnCount(), modifTableStockage.getNbColonnes());
        assertEquals(table.getModel().getRowCount() + 1, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        String nomNouvelleColonne = "newColumn";
        Class<?> type = String.class;
        TableStockage expectedTable = new DefaultTableModel();
        expectedTable.addColumn(new String[]{"Column1", "newColumn"});
        for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
            expectedTable.addRow(new Object[]{String.valueOf(i + 1), ""});
        }
        TableStockage actualTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(expectedTable.getColumnCount(), actualTable.getColumnCount());
        assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());
        for (int i = 0; i < expectedTable.getRowCount(); ++i) {
            for (int j = 0; j < expectedTable.getColumnCount(); ++j) {
                assertEquals(expectedTable.getValueAt(i, j), actualTable.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testAjouterColonneInvalidType() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        String nomNouvelleColonne = "newColumn";
        Class<?> type = Double.class;
        try {
            TableStockage actualTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAjouterLigne() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        TableStockage expectedTable = new DefaultTableModel();
        expectedTable.addColumn(new String[]{"Column1", "Column2"});
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            expectedTable.addRow(new Object[]{String.valueOf(i + 1), String.valueOf(i + 1)});
        }
        expectedTable.setValueAt("", modifTableStockage.getNbLignes(), 0);
        TableStockage actualTable = modifTableStockage.ajouterLigne();
        assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());
        assertEquals(expectedTable.getColumnCount(), actualTable.getColumnCount());
        for (int i = 0; i < expectedTable.getRowCount(); ++i) {
            for (int j = 0; j < expectedTable.getColumnCount(); ++j) {
                assertEquals(expectedTable.getValueAt(i, j), actualTable.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        int indice = 0;
        TableStockage expectedTable = new DefaultTableModel();
        expectedTable.addColumn(new String[]{"Column2"});
        for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
            expectedTable.addRow(new Object[]{String.valueOf(i + 1)});
        }
        TableStockage actualTable = modifTableStockage.supprimerColonne(indice);
        assertEquals(expectedTable.getColumnCount(), actualTable.getColumnCount());
        assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());
        for (int i = 0; i < expectedTable.getRowCount(); ++i) {
            for (int j = 0; j < expectedTable.getColumnCount(); ++j) {
                assertEquals(expectedTable.getValueAt(i, j), actualTable.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerColonneInvalidIndex() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        int indice = modifTableStockage.getNbColonnes() + 1;
        try {
            TableStockage actualTable = modifTableStockage.supprimerColonne(indice);
            fail("Exception expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSeparerColonne() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        String separator = " ";
        int indice = 0;
        String expectedColumnName1 = "Column1";
        String expectedColumnName2 = "Column2";
        TableStockage expectedTable = new DefaultTableModel();
        expectedTable.addColumn(new String[]{expectedColumnName1, expectedColumnName2});
        for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
            expectedTable.addRow(new Object[]{String.valueOf(i + 1), String.valueOf(i + 1)});
        }
        TableStockage actualTable = modifTableStockage.separerColonne(indice);
        assertEquals(expectedTable.getColumnCount(), actualTable.getColumnCount());
        assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());
        for (int i = 0; i < expectedTable.getRowCount(); ++i) {
            for (int j = 0; j < expectedTable.getColumnCount(); ++j) {
                assertEquals(expectedTable.getValueAt(i, j), actualTable.getValueAt(i, j));
            }
        }
        assertEquals(expectedColumnName1, actualTable.getColumnName(0));
        assertEquals(expectedColumnName2, actualTable.getColumnName(1));
    }

    @Test
    public void testSeparerColonneInvalidIndex() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        String separator = " ";
        int indice = modifTableStockage.getNbColonnes() + 1;
        try {
            TableStockage actualTable = modifTableStockage.separerColonne(indice);
            fail("Exception expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSeparerColonneInvalidSeparator() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        String separator = "ABC";
        int indice = 0;
        try {
            TableStockage actualTable = modifTableStockage.separerColonne(indice);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFusionnerColonnes() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        int indice = 0;
        String expectedColumnName = "Column1_Column2";
        TableStockage expectedTable = new DefaultTableModel();
        expectedTable.addColumn(new String[]{expectedColumnName});
        for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
            expectedTable.addRow(new Object[]{String.valueOf(i + 1), String.valueOf(i + 1)});
        }
        TableStockage actualTable = modifTableStockage.fusionnerColonnes(indice);
        assertEquals(expectedTable.getColumnCount(), actualTable.getColumnCount());
        assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());
        for (int i = 0; i < expectedTable.getRowCount(); ++i) {
            for (int j = 0; j < expectedTable.getColumnCount(); ++j) {
                assertEquals(expectedTable.getValueAt(i, j), actualTable.getValueAt(i, j));
            }
        }
        assertEquals(expectedColumnName, actualTable.getColumnName(0));
    }

    @Test
    public void testFusionnerColonnesInvalidIndex() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        int indice = modifTableStockage.getNbColonnes() + 1;
        try {
            TableStockage actualTable = modifTableStockage.fusionnerColonnes(indice);
            fail("Exception expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testFusionnerColonnesInvalidTypes() {
        JTable table = new JTable();
        TableModel model = new DefaultTableModel();
        table.setModel(model);
        ModifTableStockage modifTableStockage = new ModifTableStockage(model);
        int indice = 0;
        try {
            TableStockage actualTable = modifTableStockage.fusionnerColonnes(indice);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}