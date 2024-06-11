package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.junit.Test;

public class ModifTableStockageTest {

    private static TableStockage createTable(int rows, int cols) {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        table.setModel(model);
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < cols; i++) {
            columnModel.getColumn(i).setCellEditor(null);
            columnModel.getColumn(i).setCellRenderer(null);
        }
        return new TableStockage(rows, cols);
    }

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage table = new TableStockage(createTable(3, 2));
        assertEquals(3, table.getNbLignes());
        assertEquals(2, table.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage table = createTable(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage newTable = modifTable.ajouterColonne("Nouvelle Colonne", String.class);
        assertEquals(4, newTable.getNbColonnes());
        assertEquals("Nouvelle Colonne", newTable.getColumnName(3));

        for (int i = 0; i < newTable.getNbLignes(); i++) {
            for (int j = 0; j < newTable.getNbColonnes(); j++) {
                if (j < 3) {
                    assertEquals(table.getValueAt(i, j), newTable.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage table = createTable(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage newTable = modifTable.ajouterLigne();
        assertEquals(4, newTable.getNbLignes());

        for (int i = 0; i < newTable.getNbLignes(); i++) {
            for (int j = 0; j < newTable.getNbColonnes(); j++) {
                if (i < newTable.getNbLignes() - 1) {
                    assertEquals(table.getValueAt(i, j), newTable.getValueAt(i, j));
                }
                if (j == newTable.getNbColonnes() - 1) {
                    if (table.getColumnClass(j).equals(String.class)) {
                        assertEquals("", newTable.getValueAt(i, j));
                    } else if (table.getColumnClass(j).equals(Double.class)) {
                        assertEquals(0.0, newTable.getValueAt(i, j), 0.0);
                    } else {
                        assertEquals(false, newTable.getValueAt(i, j));
                    }
                }
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage table = createTable(4, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage newTable = modifTable.supprimerColonne(2);
        assertEquals(3, newTable.getNbColonnes());

        for (int i = 0; i < newTable.getNbLignes(); i++) {
            for (int j = 0; j < newTable.getNbColonnes(); j++) {
                if (j < 2) {
                    assertEquals(table.getValueAt(i, j), newTable.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage table = createTable(4, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage newTable = modifTable.supprimerLigne(2);
        assertEquals(3, newTable.getNbLignes());

        for (int i = 0; i < newTable.getNbLignes(); i++) {
            for (int j = 0; j < newTable.getNbColonnes(); j++) {
                if (i < newTable.getNbLignes() - 1) {
                    assertEquals(table.getValueAt(i, j), newTable.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage table = createTable(3, 1);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage newTable = modifTable.separerColonne(0);
        assertEquals(2, newTable.getNbColonnes());

        for (int i = 0; i < newTable.getNbLignes(); i++) {
            for (int j = 0; j < newTable.getNbColonnes(); j++) {
                assertEquals(table.getValueAt(i, 0), newTable.getValueAt(i, j));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneInvalidIndex() {
        TableStockage table = createTable(3, 1);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.separerColonne(3);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage table = createTable(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage newTable = modifTable.fusionnerColonnes(1);
        assertEquals(2, newTable.getNbColonnes());

        for (int i = 0; i < newTable.getNbLignes(); i++) {
            for (int j = 0; j < newTable.getNbColonnes(); j++) {
                if (j == 0) {
                    assertEquals(table.getValueAt(i, 0), newTable.getValueAt(i, j));
                } else {
                    assertEquals(table.getValueAt(i, 1), newTable.getValueAt(i, j));
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesInvalidIndex() {
        TableStockage table = createTable(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        modifTable.fusionnerColonnes(3);
    }

    @Test
    public void testColonneMail() {
        TableStockage table = createTable(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage newTable = modifTable.colonneMail();
        assertEquals(3, newTable.getNbColonnes());

        for (int i = 0; i < newTable.getNbLignes(); i++) {
            for (int j = 0; j < newTable.getNbColonnes(); j++) {
                if (j == newTable.getNbColonnes() - 1) {
                    assertEquals(true, newTable.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage table = createTable(4, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage newTable = modifTable.selectMail();
        assertEquals(2, newTable.getNbColonnes());

        for (int i = 0; i < newTable.getNbLignes(); i++) {
            for (int j = 0; j < newTable.getNbColonnes(); j++) {
                assertEquals(table.getValueAt(i, j), newTable.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSelectMailNoMailColumn() {
        TableStockage table = createTable(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage newTable = modifTable.selectMail();
        assertEquals(2, newTable.getNbColonnes());

        JFrame frame = new JFrame();
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(frame, "Le mail sera envoye a toute la liste", "Information", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Test
    public void testShowMessageDialog() {
        TableStockage table = createTable(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);

        Class<?> clazz = JOptionPane.class;
        Method method = null;
        try {
            method = clazz.getMethod("showMessageDialog", Component.class, String.class, int.class, Object.class);
        } catch (NoSuchMethodException e) {
            fail("showMessageDialog method not found");
        }

        String title = "ERREUR";
        String message = "PAS DE COLONNE";
        Object[] options = new Object[0];
        int option = 0;

        SwingUtilities.invokeLater(() -> {
            try {
                method.invoke(null, new Object[] { null, message, JOptionPane.ERROR_MESSAGE, options });
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}