package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

import java.awt.Dimension;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableStockage tableStockage;

    @Before
    public void setUp() {
        Object[][] data = {{"Col1", "Type1"}, {"Col2", "Type2"}};
        tableStockage = new TableStockage(2, 2, data);
        modifTableStockage = new ModifTableStockage(tableStockage);
    }

    @After
    public void tearDown() {
        modifTableStockage = null;
        tableStockage = null;
    }

    @Test
    public void testModifTableStockageConstruction() {
        assertEquals(tableStockage.getRowCount(), modifTableStockage.getNbLignes());
        assertEquals(tableStockage.getColumnCount(), modifTableStockage.getNbColonnes());
        assertEquals(tableStockage, modifTableStockage.getTableStockage());
    }

    @Test
    public void testAjouterColonne() {
        final String nomNouvelleColonne = "Col3";
        final Class<?> type = String.class;

        ModifTableStockage newModifTableStockage = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(modifTableStockage.getNbColonnes(), newModifTableStockage.getNbColonnes() + 1);
        assertEquals(nomNouvelleColonne, newModifTableStockage.getColumnName(newModifTableStockage.getNbColonnes() - 1));
        assertEquals(type, newModifTableStockage.getColumnClass(newModifTableStockage.getNbColonnes() - 1));

        // Check that original table is unchanged
        TableStockage originalTable = modifTableStockage.getTableStockage();
        assertEquals(originalTable, newModifTableStockage.getTableStockage());
    }

    @Test
    public void testAjouterLigne() {
        ModifTableStockage newModifTableStockage = modifTableStockage.ajouterLigne();
        assertEquals(modifTableStockage.getNbLignes(), newModifTableStockage.getNbLignes() + 1);

        for (int i = 0; i < newModifTableStockage.getNbColonnes(); ++i) {
            if (tableStockage.getColumnClass(i).equals(String.class)) {
                assertEquals("", newModifTableStockage.getValueAt(newModifTableStockage.getNbLignes() - 1, i));
            } else if (tableStockage.getColumnClass(i).equals(Double.class)) {
                assertEquals(0.0, newModifTableStockage.getValueAt(newModifTableStockage.getNbLignes() - 1, i), 0.001);
            } else {
                assertEquals(false, newModifTableStockage.getValueAt(newModifTableStockage.getNbLignes() - 1, i));
            }
        }

        // Check that original table is unchanged
        TableStockage originalTable = modifTableStockage.getTableStockage();
        assertEquals(originalTable, newModifTableStockage.getTableStockage());
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterColonneWhenNoColumns() {
        modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
        modifTableStockage.ajouterColonne("Col1", String.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneWhenNoRows() {
        modifTableStockage = new ModifTableStockage(new TableStockage(0, 2));
        modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        int indice = 1;
        ModifTableStockage newModifTableStockage = modifTableStockage.supprimerColonne(indice);
        assertEquals(modifTableStockage.getNbColonnes(), newModifTableStockage.getNbColonnes() - 1);

        for (int i = 0; i < newModifTableStockage.getNbColonnes(); ++i) {
            if (i != indice) {
                assertEquals(modifTableStockage.getColumnName(i), newModifTableStockage.getColumnName(i));
                assertEquals(modifTableStockage.getColumnClass(i), newModifTableStockage.getColumnClass(i));
            } else {
                assertNull(newModifTableStockage.getColumnName(i));
                assertEquals(null, newModifTableStockage.getColumnClass(i));
            }
        }

        // Check that original table is unchanged
        TableStockage originalTable = modifTableStockage.getTableStockage();
        assertEquals(originalTable, newModifTableStockage.getTableStockage());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneWhenNoSuchColumn() {
        modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes());
    }

    @Test
    public void testSupprimerLigne() {
        int indice = 1;
        ModifTableStockage newModifTableStockage = modifTableStockage.supprimerLigne(indice);
        assertEquals(modifTableStockage.getNbLignes(), newModifTableStockage.getNbLignes() - 1);

        for (int i = 0; i < newModifTableStockage.getNbLignes(); ++i) {
            for (int j = 0; j < newModifTableStockage.getNbColonnes(); ++j) {
                if (i != indice) {
                    assertEquals(modifTableStockage.getValueAt(i, j), newModifTableStockage.getValueAt(i, j));
                } else {
                    assertNull(newModifTableStockage.getValueAt(i, j));
                }
            }
        }

        // Check that original table is unchanged
        TableStockage originalTable = modifTableStockage.getTableStockage();
        assertEquals(originalTable, newModifTableStockage.getTableStockage());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneWhenNoSuchRow() {
        modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes());
    }

    @Test
    public void testSeparerColonne() {
        int indice = 1;
        String expectedColumnName1 = "Col1";
        String expectedColumnName2 = "Col2";
        String valueBeforeSeparation = "Col1 Col2";

        ModifTableStockage newModifTableStockage = modifTableStockage.separerColonne(indice);
        assertEquals(modifTableStockage.getNbColonnes(), newModifTableStockage.getNbColonnes() + 1);
        assertEquals(expectedColumnName1, newModifTableStockage.getColumnName(indice));
        assertEquals(expectedColumnName2, newModifTableStockage.getColumnName(indice + 1));

        for (int i = 0; i < newModifTableStockage.getNbLignes(); ++i) {
            Object cellValueBeforeSeparation = newModifTableStockage.getValueAt(i, indice);
            String cellValueAfterSeparation = cellValueBeforeSeparation.toString();
            String[] values = cellValueAfterSeparation.split(" ");
            assertEquals(values.length, 2);
            assertEquals(expectedColumnName1, newModifTableStockage.getColumnName(i, indice));
            assertEquals(values[0], newModifTableStockage.getValueAt(i, indice));
            assertEquals(expectedColumnName2, newModifTableStockage.getColumnName(i, indice + 1));
            assertEquals(values[1], newModifTableStockage.getValueAt(i, indice + 1));
        }

        // Check that original table is unchanged
        TableStockage originalTable = modifTableStockage.getTableStockage();
        assertEquals(originalTable, newModifTableStockage.getTableStockage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneWhenColumnIsNotString() {
        int indice = 1;
        ModifTableStockage modifTableStockageWithBooleanColumn = new ModifTableStockage(new TableStockage(2, 2, new Object[][]{
                {"Col1", Boolean.class}, {"Col2", Double.class}
        }));
        modifTableStockageWithBooleanColumn.separerColonne(indice);
    }

    @Test
    public void testFusionnerColonnes() {
        int indice = 1;
        String expectedColumnName = "Col1 Col2";

        ModifTableStockage newModifTableStockage = modifTableStockage.fusionnerColonnes(indice);
        assertEquals(modifTableStockage.getNbColonnes(), newModifTableStockage.getNbColonnes() - 1);
        assertEquals(expectedColumnName, newModifTableStockage.getColumnName(indice));

        for (int i = 0; i < newModifTableStockage.getNbLignes(); ++i) {
            Object cellValueBeforeFusion = newModifTableStockage.getValueAt(i, indice);
            String cellValueAfterFusion = cellValueBeforeFusion.toString();
            assertEquals(cellValueAfterFusion, newModifTableStockage.getValueAt(i, indice));
        }

        // Check that original table is unchanged
        TableStockage originalTable = modifTableStockage.getTableStockage();
        assertEquals(originalTable, newModifTableStockage.getTableStockage());
    }

    @Test(expected = IllegalStateException.class)
    public void testFusionnerColonnesWhenNoSuchColumn() {
        modifTableStockage.fusionnerColonnes(modifTableStockage.getNbColonnes());
    }

    @Test
    public void testColonneMail() {
        ModifTableStockage newModifTableStockage = modifTableStockage.colonneMail();
        assertEquals(modifTableStockage.getNbColonnes(), newModifTableStockage.getNbColonnes() + 1);
        assertEquals("Envoyer E-Mail a:", newModifTableStockage.getColumnName(newModifTableStockage.getNbColonnes() - 1));
        assertEquals(Boolean.class, newModifTableStockage.getColumnClass(newModifTableStockage.getNbColonnes() - 1));

        for (int i = 0; i < newModifTableStockage.getNbLignes(); ++i) {
            assertEquals(true, newModifTableStockage.getValueAt(i, newModifTableStockage.getNbColonnes() - 1));
        }

        // Check that original table is unchanged
        TableStockage originalTable = modifTableStockage.getTableStockage();
        assertEquals(originalTable, newModifTableStockage.getTableStockage());
    }

    @Test
    public void testSelectMail() {
        int j;
        for (j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
            if (modifTableStockage.getColumnName(j).equals("Envoyer E-Mail a:")) {
                break;
            }
        }

        int newNbLignes = 0;
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            Boolean bool = (Boolean)modifTableStockage.getValueAt(i, j);
            if (bool) {
                ++newNbLignes;
            }
        }

        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(newNbLignes + 1, tableS.getRowCount());
        assertEquals(modifTableStockage.getColumnCount(), tableS.getColumnCount());

        for (int i = 0; i < newNbLignes; ++i) {
            for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
                if (i < newNbLignes) {
                    assertEquals(modifTableStockage.getValueAt(i, j), tableS.getValueAt(i, j));
                } else {
                    assertEquals(null, tableS.getValueAt(i, j));
                }
            }
        }

        // Check that original table is unchanged
        TableStockage originalTable = modifTableStockage.getTableStockage();
        assertEquals(originalTable, modifTableStockage.getTableStockage());
        assertEquals(originalTable, tableS.getTableModel());
    }
}