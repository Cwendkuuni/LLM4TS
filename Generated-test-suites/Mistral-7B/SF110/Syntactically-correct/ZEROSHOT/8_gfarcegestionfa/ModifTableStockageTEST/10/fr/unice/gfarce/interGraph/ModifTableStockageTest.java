package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableStockage tableStockage;

    @Before
    public void setUp() {
        String[] columnNames = {"Colonne1", "Colonne2", "Colonne3"};
        Object[][] data = {{"Valeur1_1", "Valeur2_1", "Valeur3_1"}, {"Valeur1_2", "Valeur2_2", "Valeur3_2"}};
        tableStockage = new TableStockage(data, columnNames);
        modifTableStockage = new ModifTableStockage(tableStockage);
    }

    @Test
    public void testConstructor() {
        assertEquals(2, modifTableStockage.getNbLignes());
        assertEquals(3, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        String newColumnName = "NouvelleColonne";
        Class<?> newColumnType = String.class;
        TableStockage newTableStockage = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
        assertEquals(4, newTableStockage.getColumnCount());
        assertEquals(newColumnName, newTableStockage.getColumnName(3));

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 3; ++j) {
                assertEquals(modifTableStockage.getTableStockage().getValueAt(i, j), newTableStockage.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage newTableStockage = modifTableStockage.ajouterLigne();
        assertEquals(3, newTableStockage.getRowCount());
        assertNull(newTableStockage.getValueAt(2, 0));
        assertNull(newTableStockage.getValueAt(2, 1));
        assertNull(newTableStockage.getValueAt(2, 2));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i < 2 && j < 3) {
                    assertEquals(modifTableStockage.getTableStockage().getValueAt(i, j), newTableStockage.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        modifTableStockage = new ModifTableStockage(new TableStockage(modifTableStockage.getTableStockage().getModelData(), modifTableStockage.getTableStockage().getColumnNames()));

        int columnToRemoveIndex = 1;
        TableStockage newTableStockage = modifTableStockage.supprimerColonne(columnToRemoveIndex);

        assertEquals(2, newTableStockage.getColumnCount());
        assertNotSame(columnToRemoveIndex, newTableStockage.getColumnName(0).indexOf("Colonne"));
        assertNull(newTableStockage.getValueAt(0, columnToRemoveIndex));

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                assertEquals(modifTableStockage.getTableStockage().getValueAt(i, j), newTableStockage.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        modifTableStockage = new ModifTableStockage(new TableStockage(modifTableStockage.getTableStockage().getModelData(), modifTableStockage.getTableStockage().getColumnNames()));

        int rowToRemoveIndex = 1;
        TableStockage newTableStockage = modifTableStockage.supprimerLigne(rowToRemoveIndex);

        assertEquals(2, newTableStockage.getRowCount());
        assertNull(newTableStockage.getValueAt(1, 0));
        assertNull(newTableStockage.getValueAt(1, 1));
        assertNull(newTableStockage.getValueAt(1, 2));

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i != rowToRemoveIndex) {
                    assertEquals(modifTableStockage.getTableStockage().getValueAt(i, j), newTableStockage.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        modifTableStockage = new ModifTableStockage(new TableStockage(modifTableStockage.getTableStockage().getModelData(), modifTableStockage.getTableStockage().getColumnNames()));

        int columnToSeparateIndex = 0;
        TableStockage newTableStockage = modifTableStockage.separerColonne(columnToSeparateIndex);

        assertEquals(4, newTableStockage.getColumnCount());
        assertEquals("Colonne1", newTableStockage.getColumnName(0));
        assertEquals("", newTableStockage.getColumnName(1));
        assertEquals("Colonne2", newTableStockage.getColumnName(2));

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 4; ++j) {
                assertEquals(modifTableStockage.getTableStockage().getValueAt(i, j), newTableStockage.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        modifTableStockage = new ModifTableStockage(new TableStockage(modifTableStockage.getTableStockage().getModelData(), modifTableStockage.getTableStockage().getColumnNames()));

        int columnToFuseIndex = 1;
        int nextColumnToFuseIndex = 2;
        TableStockage newTableStockage = modifTableStockage.fusionnerColonnes(columnToFuseIndex);

        assertEquals(3, newTableStockage.getColumnCount());
        assertEquals("Colonne1 Colonne2", newTableStockage.getColumnName(columnToFuseIndex));

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (j != columnToFuseIndex && j != nextColumnToFuseIndex) {
                    assertEquals(modifTableStockage.getTableStockage().getValueAt(i, j), newTableStockage.getValueAt(i, j));
                }
                else {
                    String value = (String) newTableStockage.getValueAt(i, j);
                    String[] parts = value.split(" ");
                    assertEquals(parts[0], modifTableStockage.getTableStockage().getValueAt(i, columnToFuseIndex).toString());
                    assertEquals(parts[1], modifTableStockage.getTableStockage().getValueAt(i, nextColumnToFuseIndex).toString());
                }
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage newTableStockage = modifTableStockage.colonneMail();

        assertEquals(4, newTableStockage.getColumnCount());
        assertEquals("Envoyer E-Mail a:", newTableStockage.getColumnName(3));

        for (int i = 0; i < newTableStockage.getRowCount(); ++i) {
            assertTrue((Boolean) newTableStockage.getValueAt(i, 3));
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage newTableStockage = modifTableStockage.selectMail();

        assertEquals(2, newTableStockage.getRowCount());

        int columnToSelectIndex = 1;
        Boolean[] expectedValues = {true, false};

        for (int i = 0; i < newTableStockage.getRowCount(); ++i) {
            for (int j = 0; j < newTableStockage.getColumnCount(); ++j) {
                if (i == 0 && j == columnToSelectIndex) {
                    assertEquals(expectedValues[i], (Boolean) newTableStockage.getValueAt(i, j));
                }
            }
        }
    }
}