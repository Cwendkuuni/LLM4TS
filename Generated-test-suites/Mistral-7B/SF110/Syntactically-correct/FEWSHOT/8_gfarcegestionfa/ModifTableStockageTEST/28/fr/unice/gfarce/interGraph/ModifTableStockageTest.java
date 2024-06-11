package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableStockage tableStockage;
    private JTable jTable;

    @Before
    public void setUp() {
        tableStockage = new TableStockage(5, 3);
        modifTableStockage = new ModifTableStockage(tableStockage);
        jTable = new JTable(tableStockage);
    }

    @Test
    public void testModifTableStockageConstructor() {
        assertEquals(tableStockage, modifTableStockage.getTableStockage());
        assertEquals(tableStockage.getColumnCount(), modifTableStockage.getNbColonnes());
        assertEquals(tableStockage.getRowCount(), modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        String nomNouvelleColonne = "NewColumn";
        Class<?> type = String.class;
        TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);

        assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(tableS.getColumnCount() - 1));
        assertEquals(type, tableS.getColumnClass(tableS.getColumnCount() - 1));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage tableS = modifTableStockage.ajouterLigne();
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
            Object value = null;
            if (i < modifTableStockage.getNbColonnes() - 1) {
                value = modifTableStockage.getValueAt(0, i);
            }
            assertEquals(value, tableS.getValueAt(tableS.getRowCount() - 1, i));
        }
    }

    @Test
    public void testSupprimerColonne() {
        int indice = 0;
        TableStockage tableS = modifTableStockage.supprimerColonne(indice);

        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
        for (int i = 0; i < tableS.getColumnCount(); ++i) {
            if (i != indice) {
                assertEquals(modifTableStockage.getColumnName(i), tableS.getColumnName(i));
                assertEquals(modifTableStockage.getColumnClass(i), tableS.getColumnClass(i));
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        int indice = 0;
        TableStockage tableS = modifTableStockage.supprimerLigne(indice);

        assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
        for (int i = 0; i < tableS.getRowCount(); ++i) {
            for (int j = 0; j < tableS.getColumnCount(); ++j) {
                assertEquals(modifTableStockage.getValueAt(i, j), tableS.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        int indice = 0;
        TableStockage tableS = modifTableStockage.separerColonne(indice);

        assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
        for (int i = 0; i < tableS.getColumnCount(); ++i) {
            if (i < modifTableStockage.getNbColonnes()) {
                assertEquals(modifTableStockage.getColumnName(i), tableS.getColumnName(i));
                assertEquals(modifTableStockage.getColumnClass(i), tableS.getColumnClass(i));
            } else {
                assertEquals("", tableS.getColumnName(i));
                assertEquals(String.class, tableS.getColumnClass(i));
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        int inde1 = 0;
        int inde2 = 1;
        TableStockage tableS = modifTableStockage.fusionnerColonnes(inde1);

        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
        for (int i = 0; i < tableS.getColumnCount(); ++i) {
            if (i != inde1 && i != inde1 + 1) {
                assertEquals(modifTableStockage.getColumnName(i), tableS.getColumnName(i));
                assertEquals(modifTableStockage.getColumnClass(i), tableS.getColumnClass(i));
            } else {
                String name = modifTableStockage.getColumnName(inde1).concat(" " + modifTableStockage.getColumnName(inde2));
                assertEquals(name, tableS.getColumnName(i));
                assertEquals(modifTableStockage.getColumnClass(inde1), tableS.getColumnClass(i));
                assertEquals(modifTableStockage.getColumnClass(inde2), tableS.getColumnClass(i));
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage tableS = modifTableStockage.colonneMail();
        assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(tableS.getColumnCount() - 1));
        assertEquals(Boolean.class, tableS.getColumnClass(tableS.getColumnCount() - 1));

        for (int i = 0; i < tableS.getRowCount(); ++i) {
            assertTrue(tableS.isCellEditable(i, tableS.getColumnCount() - 1));
        }
    }

    @Test
    public void testSelectMail() {
        int j = -1;
        for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
            if (modifTableStockage.getColumnName(i).equals("Envoyer E-Mail a:")) {
                j = i;
                break;
            }
        }

        assertTrue(j != -1);

        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());

        for (int i = 0; i < tableS.getRowCount(); ++i) {
            for (int j2 = 0; j2 < tableS.getColumnCount(); ++j2) {
                if (i < tableS.getRowCount() - 1) {
                    assertEquals(modifTableStockage.getValueAt(i, j), tableS.getValueAt(i, j2));
                }
            }
        }
    }

    @Test
    public void testTableModelChange() {
        TableModelChangeListener listener = new TableModelChangeListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                TableColumn column = jTable.getColumnModel().getColumn(modifTableStockage.getColumnIndex("Envoyer E-Mail a:"));
                TableCellRenderer renderer = jTable.getDefaultRenderer(Boolean.class);
                Object value = jTable.getValueAt(0, modifTableStockage.getColumnIndex("Envoyer E-Mail a:"));
                Component comp = tableS.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(null, null, false, false, 0, 0);

                assertEquals(value, (Boolean) renderer.getValueAt(jTable, 0, modifTableStockage.getColumnIndex("Envoyer E-Mail a:")));
                assertEquals(column.getHeaderValue(), comp.getText());
            }
        };

        modifTableStockage.addTableModelChangeListener(listener);
        modifTableStockage.setValueAt(true, 0, modifTableStockage.getColumnIndex("Envoyer E-Mail a:"));
        modifTableStockage.setValueAt(false, 1, modifTableStockage.getColumnIndex("Envoyer E-Mail a:"));
        assertEquals(2, listener.getCallCount());

        modifTableStockage.removeTableModelChangeListener(listener);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(tableStockage, modifTableStockage.getTableStockage());
        assertEquals(modifTableStockage.getNbColonnes(), 0);
        assertEquals(modifTableStockage.getNbLignes(), 0);

        modifTableStockage.setTableStockage(tableStockage);
        modifTableStockage.setNbColonnes(3);
        modifTableStockage.setNbLignes(5);

        assertEquals(tableStockage, modifTableStockage.getTableStockage());
        assertEquals(3, modifTableStockage.getNbColonnes());
        assertEquals(5, modifTableStockage.getNbLignes());

        modifTableStockage.setTableStockage(null);
        assertNull(modifTableStockage.getTableStockage());
    }
}