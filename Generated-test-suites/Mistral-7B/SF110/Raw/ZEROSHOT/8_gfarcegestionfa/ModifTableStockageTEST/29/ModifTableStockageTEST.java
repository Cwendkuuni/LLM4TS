package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableModel initialTableModel;

    @Before
    public void setUp() {
        String[] columnNames = {"Column1", "Column2", "Column3"};
        Object[][] data = {{1, "A1", "1.0"}, {2, "B1", "2.0"}, {3, "C1", "3.0"}};
        initialTableModel = new DefaultTableModel(data, columnNames);
        ModifTableStockage modifTableStockage = new ModifTableStockage(new DefaultTableModel(data, columnNames));
        this.modifTableStockage = modifTableStockage;
    }

    @Test
    public void testModifTableStockageConstruction() {
        assertNotNull(modifTableStockage);
        assertEquals(initialTableModel.getColumnCount(), modifTableStockage.getNbColonnes());
        assertEquals(initialTableModel.getRowCount(), modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        String newColumnName = "NewColumn";
        Class<?> newColumnClass = String.class;
        TableModel newTableModel = modifTableStockage.ajouterColonne(newColumnName, newColumnClass);
        assertEquals(modifTableStockage.getNbColonnes(), newTableModel.getColumnCount());
        assertEquals(newColumnName, newTableModel.getColumnName(modifTableStockage.getNbColonnes() - 1));
    }

    @Test
    public void testAjouterLigne() {
        TableModel newTableModel = modifTableStockage.ajouterLigne();
        assertEquals(modifTableStockage.getNbLignes(), newTableModel.getRowCount());
        for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
            assertEquals(modifTableStockage.getColumnName(i), newTableModel.getColumnName(i));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneWhenNoColumn() {
        modifTableStockage.supprimerColonne(0);
        modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        String columnNameToRemove = "Column2";
        TableModel newTableModel = modifTableStockage.supprimerColonne(modifTableStockage.getColumnIndex(columnNameToRemove));
        assertEquals(modifTableStockage.getNbColonnes() - 1, newTableModel.getColumnCount());
        for (int i = 0; i < modifTableStockage.getNbColonnes() - 1; ++i) {
            assertNotSame(columnNameToRemove, newTableModel.getColumnName(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneWhenInvalidIndex() {
        modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes());
    }

    @Test
    public void testSupprimerLigne() {
        TableModel newTableModel = modifTableStockage.supprimerLigne(0);
        assertEquals(modifTableStockage.getNbLignes() - 1, newTableModel.getRowCount());
        for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
            assertEquals(modifTableStockage.getColumnName(i), newTableModel.getColumnName(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneWhenInvalidIndex() {
        modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes());
    }

    @Test
    public void testSeparerColonne() {
        int columnIndexToSplit = 0;
        TableModel newTableModel = modifTableStockage.separerColonne(columnIndexToSplit);
        assertEquals(modifTableStockage.getNbColonnes() + 1, newTableModel.getColumnCount());
        assertEquals(modifTableStockage.getColumnName(columnIndexToSplit), newTableModel.getColumnName(columnIndexToSplit));
        assertEquals(modifTableStockage.getColumnName(columnIndexToSplit + 1), newTableModel.getColumnName(columnIndexToSplit + 1));

        for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
            if (i != columnIndexToSplit) {
                assertEquals(modifTableStockage.getColumnName(i), newTableModel.getColumnName(i));
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testSeparerColonneWhenInvalidType() {
        int columnIndexToSplit = 0;
        modifTableStockage.setColumnClass(columnIndexToSplit, Double.class);
        modifTableStockage.separerColonne(columnIndexToSplit);
    }

    @Test
    public void testFusionnerColonnes() {
        int columnIndexToMerge = 0;
        int nextColumnIndexToMerge = 1;
        TableModel newTableModel = modifTableStockage.fusionnerColonnes(columnIndexToMerge);
        assertEquals(modifTableStockage.getNbColonnes() - 1, newTableModel.getColumnCount());
        assertEquals(modifTableStockage.getColumnName(columnIndexToMerge), newTableModel.getColumnName(columnIndexToMerge));
        assertEquals(modifTableStockage.getColumnName(nextColumnIndexToMerge), newTableModel.getColumnName(columnIndexToMerge));

        for (int i = 0; i < modifTableStockage.getNbColonnes() - 1; ++i) {
            if (i != nextColumnIndexToMerge) {
                assertEquals(modifTableStockage.getColumnName(i), newTableModel.getColumnName(i));
            }
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testFusionnerColonnesWhenInvalidType() {
        int columnIndexToMerge = 0;
        int nextColumnIndexToMerge = 1;
        modifTableStockage.setColumnClass(columnIndexToMerge, Boolean.class);
        modifTableStockage.fusionnerColonnes(columnIndexToMerge);
    }

    @Test
    public void testColonneMail() {
        TableModel newTableModel = modifTableStockage.colonneMail();
        assertEquals(modifTableStockage.getNbColonnes() + 1, newTableModel.getColumnCount());
        assertEquals("Envoyer E-Mail a:", newTableModel.getColumnName(modifTableStockage.getNbColonnes() - 1));
    }

    @Test
    public void testSelectMail() {
        int columnIndexOfColonneMail = modifTableStockage.getColumnIndex("Envoyer E-Mail a:");
        TableModel newTableModel = modifTableStockage.selectMail();
        assertEquals(modifTableStockage.getNbLignes(), newTableModel.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), newTableModel.getColumnCount());

        for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
            if (i != columnIndexOfColonneMail) {
                assertEquals(modifTableStockage.getColumnName(i), newTableModel.getColumnName(i));
            }
        }
    }
}