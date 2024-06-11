package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.swing.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage table = new TableStockage(5, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        assertEquals(5, modifTable.getNbLignes());
        assertEquals(3, modifTable.getNbColonnes());
        assertSame(table, modifTable.getTableStockage());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        String nomNouvelleColonne = "test";
        Class<?> type = String.class;
        TableStockage result = modifTable.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(4, result.getNbColonnes());
        assertEquals(3, result.getNbLignes());
        assertEquals(nomNouvelleColonne, result.getColumnName(3));
        assertEquals(type, result.getColumnClass(3));
    }

    @Test
    public void testAjouterColonneWithInvalidType() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        String nomNouvelleColonne = "test";
        Class<?> type = Integer.class;
        TableStockage result = modifTable.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(3, result.getNbColonnes());
        assertEquals(3, result.getNbLignes());
        assertNull(result.getColumnName(3));
        assertEquals(null, result.getColumnClass(3));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.ajouterLigne();
        assertEquals(4, result.getNbLignes());
        assertEquals(3, result.getNbColonnes());
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i < 3 && j < 3) {
                    assertEquals(table.getValueAt(i, j), result.getValueAt(i, j));
                } else if (i == 3 && j < 3) {
                    assertNull(result.getValueAt(i, j));
                } else if (i < 3 && j == 3) {
                    assertEquals("", result.getValueAt(i, j));
                } else {
                    assertNull(result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testAjouterLigneWithNoColumns() {
        TableStockage table = new TableStockage(3, 0);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.ajouterLigne();
        assertEquals(3, result.getNbLignes());
        assertEquals(0, result.getNbColonnes());
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage table = new TableStockage(4, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indice = 2;
        TableStockage result = modifTable.supprimerColonne(indice);
        assertEquals(3, result.getNbColonnes());
        assertEquals(4, result.getNbLignes());
        assertNull(result.getColumnName(indice));
        for (int j = 0; j < indice; ++j) {
            assertEquals(table.getColumnName(j), result.getColumnName(j));
        }
        for (int i = 0; i < 4; ++i) {
            for (int k = 0; k < indice; ++k) {
                assertEquals(table.getValueAt(i, k), result.getValueAt(i, k));
            }
            for (int k = indice + 1; k < 4; ++k) {
                assertEquals(table.getValueAt(i, k - 1), result.getValueAt(i, k));
            }
        }
    }

    @Test
    public void testSupprimerColonneWithLastColumn() {
        TableStockage table = new TableStockage(4, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indice = 3;
        TableStockage result = modifTable.supprimerColonne(indice);
        assertEquals(3, result.getNbColonnes());
        assertEquals(4, result.getNbLignes());
        assertNull(result.getColumnName(indice));
        for (int j = 0; j < indice; ++j) {
            assertEquals(table.getColumnName(j), result.getColumnName(j));
        }
        for (int i = 0; i < 4; ++i) {
            for (int k = 0; k < indice; ++k) {
                assertEquals(table.getValueAt(i, k), result.getValueAt(i, k));
            }
            for (int k = indice; k < 4; ++k) {
                assertEquals(table.getValueAt(i, k - 1), result.getValueAt(i, k));
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage table = new TableStockage(4, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indice = 2;
        TableStockage result = modifTable.supprimerLigne(indice);
        assertEquals(3, result.getNbLignes());
        assertEquals(4, result.getNbColonnes());
        for (int i = 0; i < indice; ++i) {
            for (int j = 0; j < 4; ++j) {
                assertEquals(table.getValueAt(i, j), result.getValueAt(i, j));
            }
        }
        for (int i = indice + 1; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                assertEquals(table.getValueAt(i - 1, j), result.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerLigneWithLastLine() {
        TableStockage table = new TableStockage(4, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indice = 3;
        TableStockage result = modifTable.supprimerLigne(indice);
        assertEquals(3, result.getNbLignes());
        assertEquals(4, result.getNbColonnes());
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                assertEquals(table.getValueAt(i, j), result.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage table = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indice = 1;
        TableStockage result = modifTable.separerColonne(indice);
        assertEquals(5, result.getNbColonnes());
        assertEquals(3, result.getNbLignes());
        assertEquals("test1", result.getColumnName(indice));
        assertEquals("test2", result.getColumnName(indice + 1));
        assertEquals("test1", result.getColumnName(indice - 1));
        assertEquals("test2", result.getColumnName(indice));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (j < indice) {
                    assertEquals(table.getValueAt(i, j), result.getValueAt(i, j));
                } else if (j > indice) {
                    assertEquals(table.getValueAt(i, j - 1), result.getValueAt(i, j));
                } else {
                    assertNull(result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSeparerColonneWithNoSpace() {
        TableStockage table = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indice = 1;
        TableStockage result = modifTable.separerColonne(indice);
        assertEquals(5, result.getNbColonnes());
        assertEquals(3, result.getNbLignes());
        assertEquals("test1test2", result.getColumnName(indice));
        assertEquals("", result.getColumnName(indice + 1));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (j < indice) {
                    assertEquals(table.getValueAt(i, j), result.getValueAt(i, j));
                } else {
                    assertEquals(null, result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSeparerColonneWithEmptyString() {
        TableStockage table = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indice = 1;
        TableStockage result = modifTable.separerColonne(indice);
        assertEquals(5, result.getNbColonnes());
        assertEquals(3, result.getNbLignes());
        assertEquals("", result.getColumnName(indice));
        assertEquals("", result.getColumnName(indice + 1));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (j < indice) {
                    assertEquals(table.getValueAt(i, j), result.getValueAt(i, j));
                } else {
                    assertEquals("", result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage table = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indice = 1;
        TableStockage result = modifTable.fusionnerColonnes(indice);
        assertEquals(3, result.getNbColonnes());
        assertEquals(3, result.getNbLignes());
        assertEquals("test1 test2", result.getColumnName(indice));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (j < 2) {
                    assertEquals(table.getValueAt(i, j), result.getValueAt(i, j));
                } else {
                    assertEquals(table.getValueAt(i, j - 1), result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testFusionnerColonnesWithEmptyString() {
        TableStockage table = new TableStockage(3, 2);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        int indice = 1;
        TableStockage result = modifTable.fusionnerColonnes(indice);
        assertEquals(3, result.getNbColonnes());
        assertEquals(3, result.getNbLignes());
        assertEquals("test1  ", result.getColumnName(indice));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (j < 2) {
                    assertEquals(table.getValueAt(i, j), result.getValueAt(i, j));
                } else {
                    assertEquals("", result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.colonneMail();
        assertEquals(4, result.getNbColonnes());
        assertEquals(3, result.getNbLignes());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(3));
        assertEquals(Boolean.class, result.getColumnClass(3));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (j < 3) {
                    assertEquals(table.getValueAt(i, j), result.getValueAt(i, j));
                } else {
                    assertEquals(Boolean.FALSE, result.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage table = new TableStockage(5, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.selectMail();
        assertEquals(5, result.getNbLignes());
        assertEquals(4, result.getNbColonnes());
        for (int i = 0; i < 5; ++i) {
            Boolean bool = (Boolean)modifTable.getValueAt(i, 3);
            if (bool != null && bool) {
                for (int j = 0; j < 4; ++j) {
                    assertEquals(modifTable.getValueAt(i, j), result.getValueAt(i - 5, j));
                }
            }
        }
    }

    @Test
    public void testSelectMailWithNoMail() {
        TableStockage table = new TableStockage(5, 4);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        TableStockage result = modifTable.selectMail();
        assertEquals(5, result.getNbLignes());
        assertEquals(4, result.getNbColonnes());
        for (int i = 0; i < 5; ++i) {
            assertFalse(modifTable.getValueAt(i, 3) instanceof Boolean);
        }
    }
}