package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ModifTableStockageTest {

    private TableStockage createTableStockage() {
        Object[][] data = {{"A1", "1"}, {"B1", "2"}, {"C1", "3"}};
        return new TableStockage(3, 2, data);
    }

    private ModifTableStockage createModifTableStockage(TableStockage ts) {
        return new ModifTableStockage(ts);
    }

    @Test
    public void testConstruction() {
        TableStockage table = createTableStockage();
        ModifTableStockage modifTable = new ModifTableStockage(table);
        assertEquals(table.getRowCount(), modifTable.getNbLignes());
        assertEquals(table.getColumnCount(), modifTable.getNbColonnes());
        assertEquals(table, modifTable.getTs());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage table = createTableStockage();
        ModifTableStockage modifTable = createModifTableStockage(table);
        String name = "New Column";
        Class<?> type = Integer.class;
        TableStockage result = modifTable.ajouterColonne(name, type);
        assertEquals(modifTable.getNbColonnes() + 1, result.getColumnCount());
        assertEquals(name, result.getColumnName(modifTable.getNbColonnes() - 1));
        assertEquals(type, result.getColumnClass(modifTable.getNbColonnes() - 1));
        for (int i = 0; i < modifTable.getNbLignes(); ++i) {
            assertNull(result.getValueAt(i, modifTable.getNbColonnes() - 1));
        }

        String oldName = result.getColumnName(modifTable.getNbColonnes() - 2);
        int oldType = result.getColumnClass(modifTable.getNbColonnes() - 2).hashCode();
        for (int j = 0; j < modifTable.getNbColonnes() - 1; ++j) {
            assertEquals(oldName, result.getColumnName(j));
            assertEquals(oldType, result.getColumnClass(j).hashCode());
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage table = createTableStockage();
        ModifTableStockage modifTable = createModifTableStockage(table);
        TableStockage result = modifTable.ajouterLigne();
        assertEquals(modifTable.getNbLignes() + 1, result.getRowCount());
        for (int i = 0; i < modifTable.getNbLignes(); ++i) {
            for (int j = 0; j < modifTable.getNbColonnes(); ++j) {
                if (i < modifTable.getNbLignes() - 1) {
                    assertEquals(modifTable.getTs().getValueAt(i, j), result.getValueAt(i, j));
                }
                if (j < modifTable.getNbColonnes() - 1) {
                    assertEquals(modifTable.getTs().getValueAt(i, j), result.getValueAt(i, j));
                }
                if (j == modifTable.getNbColonnes() - 1) {
                    if (modifTable.getTs().getColumnClass(j).equals(String.class)) {
                        assertEquals("", result.getValueAt(i, j));
                    } else if (modifTable.getTs().getColumnClass(j).equals(Double.class)) {
                        assertEquals(0.0, result.getValueAt(i, j), 0.001);
                    } else {
                        assertFalse(result.getValueAt(i, j) instanceof Boolean);
                    }
                }
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage table = createTableStockage();
        ModifTableStockage modifTable = createModifTableStockage(table);
        int index = modifTable.getNbColonnes() - 1;
        TableStockage result = modifTable.supprimerColonne(index);
        assertEquals(modifTable.getNbColonnes() - 1, result.getColumnCount());
        assertFalse(result.getColumnName(index).isPresent());
        for (int i = 0; i < modifTable.getNbLignes(); ++i) {
            for (int j = 0; j < index; ++j) {
                assertEquals(modifTable.getTs().getValueAt(i, j), result.getValueAt(i, j));
            }
            for (int j = index; j < modifTable.getNbColonnes(); ++j) {
                assertEquals(modifTable.getTs().getValueAt(i, j - 1), result.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage table = createTableStockage();
        ModifTableStockage modifTable = createModifTableStockage(table);
        int index = modifTable.getNbLignes() - 1;
        TableStockage result = modifTable.supprimerLigne(index);
        assertEquals(modifTable.getNbLignes() - 1, result.getRowCount());
        assertFalse(result.getValueAt(index, 0).isPresent());
        for (int i = 0; i < index; ++i) {
            for (int j = 0; j < modifTable.getNbColonnes(); ++j) {
                assertEquals(modifTable.getTs().getValueAt(i, j), result.getValueAt(i, j));
            }
        }
        for (int j = 0; j < modifTable.getNbColonnes(); ++j) {
            assertEquals(modifTable.getTs().getValueAt(index, j), result.getValueAt(index - 1, j));
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage table = createTableStockage();
        ModifTableStockage modifTable = createModifTableStockage(table);
        int index = 1;
        TableStockage result = modifTable.separerColonne(index);
        assertEquals(modifTable.getNbColonnes() + 1, result.getColumnCount());
        assertEquals("", result.getColumnName(index).get());
        assertEquals("", result.getColumnName(index + 1).get());
        assertEquals(modifTable.getTs().getColumnName(index), result.getColumnName(index).get() + " " + result.getColumnName(index + 1).get());
        for (int i = 0; i < modifTable.getNbLignes(); ++i) {
            String value1 = (String) modifTable.getTs().getValueAt(i, index);
            String value2 = new String();
            int k = 0;
            for (; value1.charAt(k) != ' '; ++k) {
                value2 += value1.charAt(k);
            }
            ++k;
            String value3 = value1.substring(k, value1.length());
            Object value4 = modifTable.getTs().getValueAt(i, index + 1);
            if (value3.isEmpty()) {
                assertEquals("", value4);
            } else {
                assertEquals(value3, value4);
            }
            assertEquals(value1, result.getValueAt(i, index).toString());
            assertEquals(value2, result.getValueAt(i, index + 1).toString());
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage table = createTableStockage();
        ModifTableStockage modifTable = createModifTableStockage(table);
        int index = 1;
        TableStockage result = modifTable.fusionnerColonnes(index);
        assertEquals(modifTable.getNbColonnes() - 1, result.getColumnCount());
        assertEquals("Envoyer E-Mail a: " + modifTable.getTs().getColumnName(index) + " " + modifTable.getTs().getColumnName(index + 1), result.getColumnName(index).get());
        for (int i = 0; i < modifTable.getNbLignes(); ++i) {
            String value1 = (String) modifTable.getTs().getValueAt(i, index);
            String value2 = (String) modifTable.getTs().getValueAt(i, index + 1);
            assertEquals(value1.concat(" " + value2), result.getValueAt(i, index).toString());
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage table = createTableStockage();
        ModifTableStockage modifTable = createModifTableStockage(table);
        TableStockage result = modifTable.colonneMail();
        assertEquals(modifTable.getNbColonnes() + 1, result.getColumnCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(modifTable.getNbColonnes() - 1).get());
        assertEquals(Boolean.class, result.getColumnClass(modifTable.getNbColonnes() - 1));
        for (int i = 0; i < modifTable.getNbLignes(); ++i) {
            assertEquals(true, result.getValueAt(i, modifTable.getNbColonnes() - 1));
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage table = createTableStockage();
        ModifTableStockage modifTable = createModifTableStockage(table);
        int index = modifTable.getNbColonnes() - 1;
        TableStockage result = modifTable.selectMail();
        assertEquals(modifTable.getNbLignes(), result.getRowCount());
        assertEquals(modifTable.getNbColonnes(), result.getColumnCount());
        for (int i = 0; i < modifTable.getNbLignes(); ++i) {
            Boolean value = (Boolean) modifTable.getTs().getValueAt(i, index);
            if (value) {
                assertSame(modifTable.getTs(), result.getTableStockage());
                for (int j = 0; j < modifTable.getNbColonnes(); ++j) {
                    assertEquals(modifTable.getTs().getValueAt(i, j), result.getValueAt(i, j));
                }
                break;
            }
        }
    }
}