package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {

    private TableModel tsMock;
    private ModifTableStockage modifTableStockage;

    @Before
    public void setUp() {
        tsMock = new Object[][] { { "A1", "1.0" }, { "B1", "2.0" }, { "C1", "3.0" }, { "D1", "4.0" } }
                .length;
        tsMock = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return tsMock.length;
            }

            @Override
            public int getColumnCount() {
                return tsMock[0].length;
            }

            @Override
            public Object getValueAt(int row, int column) {
                return tsMock[row][column];
            }

            @Override
            public String getColumnName(int column) {
                return "ColumnName_" + column;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) {
                    return String.class;
                }
                return Double.class;
            }
        };
        modifTableStockage = new ModifTableStockage(tsMock);
    }

    @Test
    public void testModifTableStockageConstructor() {
        assertSame(tsMock, modifTableStockage.getTableStockage());
        assertEquals(tsMock.getColumnCount(), modifTableStockage.getNbColonnes());
        assertEquals(tsMock.getRowCount() + 1, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        String nomNouvelleColonne = "Nouvelle_Colonne";
        Class<?> type = String.class;
        TableModel tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(modifTableStockage.getNbColonnes() - 1));
        assertEquals(type, tableS.getColumnClass(modifTableStockage.getNbColonnes() - 1));
    }

    @Test
    public void testAjouterLigne() {
        TableModel tableS = modifTableStockage.ajouterLigne();
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
            String defaultValue = null;
            if (tsMock.getColumnClass(i).equals(String.class)) {
                defaultValue = "";
            } else if (tsMock.getColumnClass(i).equals(Double.class)) {
                defaultValue = "0";
            } else if (tsMock.getColumnClass(i).equals(Boolean.class)) {
                defaultValue = "false";
            }
            assertEquals(defaultValue, tableS.getValueAt(modifTableStockage.getNbLignes() - 2, i));
        }
    }

    @Test
    public void testSupprimerColonne() {
        int indice = 0;
        TableModel tableS = modifTableStockage.supprimerColonne(indice);
        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
        for (int i = 0; i < modifTableStockage.getNbColonnes() - 1; ++i) {
            assertEquals(tableS.getColumnName(i), modifTableStockage.getTableStockage().getColumnName(i));
        }
        assertNull(tableS.getColumnName(modifTableStockage.getNbColonnes() - 1));
    }

    @Test
    public void testSupprimerLigne() {
        int indice = 0;
        TableModel tableS = modifTableStockage.supprimerLigne(indice);
        assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
        for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
            Object expectedValue = modifTableStockage.getTableStockage().getValueAt(indice + 1, i);
            Object actualValue = tableS.getValueAt(indice, i);
            assertEquals(expectedValue, actualValue);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneWithInvalidIndex() {
        modifTableStockage.separerColonne(-1);
    }

    @Test
    public void testSeparerColonne() {
        int indice = 0;
        ModifTableStockage mt = new ModifTableStockage(tsMock);
        TableModel tableS = mt.separerColonne(indice);
        assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
        String expectedColonneName1 = modifTableStockage.getTableStockage().getColumnName(indice);
        String expectedColonneName2 = "ColumnName_" + (indice + 1);
        for (int i = 0; i < tableS.getColumnCount(); ++i) {
            if (i < modifTableStockage.getNbColonnes()) {
                assertEquals(expectedColonneName1, tableS.getColumnName(i));
            } else {
                assertEquals(expectedColonneName2, tableS.getColumnName(i));
            }
        }
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            Object expectedValue1 = modifTableStockage.getTableStockage().getValueAt(i, indice);
            Object expectedValue2 = modifTableStockage.getTableStockage().getValueAt(i, indice + 1);
            Object actualValue1 = tableS.getValueAt(i, 0);
            Object actualValue2 = tableS.getValueAt(i, 1);
            assertEquals(expectedValue1, actualValue1);
            assertEquals(expectedValue2, actualValue2);
        }
    }

    @Test
    public void testFusionnerColonnes() {
        int indice = 0;
        ModifTableStockage mt = new ModifTableStockage(tsMock);
        assertThrows(IllegalArgumentException.class, () -> {
            mt.fusionnerColonnes(indice);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            mt.fusionnerColonnes(modifTableStockage.getNbColonnes());
        });
        int indiceColumn1Boolean = modifTableStockage.getTableStockage().getColumnIndices(Boolean.class)[0];
        int indiceColumn2Boolean = modifTableStockage.getTableStockage().getColumnIndices(Boolean.class)[1];
        TableModel tableS = mt.fusionnerColonnes(indiceColumn1Boolean);
        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
        assertEquals("ColumnName_" + indiceColumn1Boolean + " ColumnName_" + indiceColumn2Boolean, tableS.getColumnName(indiceColumn1Boolean));
        for (int i = 0; i < tableS.getRowCount(); ++i) {
            String valueColumn1 = (String) modifTableStockage.getTableStockage().getValueAt(i, indiceColumn1Boolean);
            String valueColumn2 = (String) modifTableStockage.getTableStockage().getValueAt(i, indiceColumn2Boolean);
            Object valueFused = tableS.getValueAt(i, indiceColumn1Boolean);
            assertTrue(valueFused instanceof String);
            assertEquals(valueColumn1.concat(" " + valueColumn2), valueFused);
        }
    }

    @Test
    public void testColonneMail() {
        TableModel tableS = modifTableStockage.colonneMail();
        assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(modifTableStockage.getNbColonnes() - 1));
        assertEquals(Boolean.class, tableS.getColumnClass(modifTableStockage.getNbColonnes() - 1));
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            assertTrue(tableS.getValueAt(i, tableS.getColumnCount() - 1) instanceof Boolean);
            assertEquals(true, (Boolean) tableS.getValueAt(i, tableS.getColumnCount() - 1));
        }
    }

    @Test
    public void testSelectMail() {
        int j = 0;
        for (; j < modifTableStockage.getNbColonnes(); ++j) {
            if (modifTableStockage.getTableStockage().getColumnName(j).equals("Envoyer E-Mail a:")) {
                break;
            }
        }
        TableModel tableS = modifTableStockage.selectMail();
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
        TableCellRenderer renderer = tableS.getDefaultRenderer();
        TableColumn column = tableS.getColumnModel().getColumn(modifTableStockage.getNbColonnes() - 1);
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableS);
        sorter.setSortable(j, false);
        tableS = sorter.getModel();
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            Object bool = (Boolean) modifTableStockage.getTableStockage().getValueAt(i, j);
            Object renderedValue = renderer.getTableCellRendererComponent(tableS, bool, false, false, i, j).getValue();
            assertEquals(bool, renderedValue);
        }
    }
}