package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(3, modifTableStockage.getNbLignes());
        assertEquals(4, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        String nomNouvelleColonne = "newColonne";
        Class<?> type = Double.TYPE;
        TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(5, tableS.getNbColonnes());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(4));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterLigne();
        assertEquals(4, tableS.getNbColonnes());
        assertEquals(4, tableS.getNbLignes());
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (i < 3 && j < 4) {
                    assertEquals(ts.getValueAt(i, j), tableS.getValueAt(i, j));
                }
                if (i == 3 && j < 4) {
                    if (ts.getColumnClass(j).equals(String.class)) {
                        assertEquals("", tableS.getValueAt(3, j));
                    } else if (ts.getColumnClass(j).equals(Double.class)) {
                        assertEquals(0.0, tableS.getValueAt(3, j));
                    } else if (ts.getColumnClass(j).equals(Boolean.class)) {
                        assertEquals(false, tableS.getValueAt(3, j));
                    }
                }
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerColonne(0);
        assertEquals(3, tableS.getNbColonnes());
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                assertEquals(ts.getValueAt(i, j), tableS.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerLigne(0);
        assertEquals(3, tableS.getNbLignes());
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                assertEquals(ts.getValueAt(i + 1, j), tableS.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 4);
        ts.setValueAt("nom1 10", 0, 0);
        ts.setValueAt("nom2 20", 1, 0);
        ts.setValueAt("nom3 30", 2, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.separerColonne(0);
        assertEquals(5, tableS.getNbColonnes());
        assertEquals("nom1", tableS.getColumnName(0));
        assertEquals("10", tableS.getColumnName(1));
        assertEquals("nom2", tableS.getColumnName(2));
        assertEquals("20", tableS.getColumnName(3));
        assertEquals("nom3", tableS.getColumnName(4));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (j < 2) {
                    assertEquals(ts.getValueAt(i, j), tableS.getValueAt(i, j));
                } else {
                    if (i == 0) {
                        assertEquals("10", tableS.getValueAt(i, j).toString());
                    } else if (i == 1) {
                        assertEquals("20", tableS.getValueAt(i, j).toString());
                    } else {
                        assertEquals("30", tableS.getValueAt(i, j).toString());
                    }
                }
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 4);
        ts.setValueAt(true, 0, 2);
        ts.setValueAt(false, 1, 2);
        ts.setValueAt(true, 2, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.fusionnerColonnes(2);
        assertEquals(3, tableS.getNbColonnes());
        assertEquals("Boolean", tableS.getColumnClass(0).getName());
        assertEquals("Boolean", tableS.getColumnClass(1).getName());
        assertEquals("Boolean", tableS.getColumnClass(2).getName());
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (j < 2) {
                    assertEquals(ts.getValueAt(i, j), tableS.getValueAt(i, j));
                } else {
                    assertEquals(ts.getValueAt(i, j) + " " + ts.getValueAt(i, j + 1), tableS.getValueAt(i, j).toString());
                }
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.colonneMail();
        assertEquals(5, tableS.getNbColonnes());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(4));
        for (int i = 0; i < 3; ++i) {
            tableS.setValueAt(true, i, 4);
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(3, 5);
        ts.setValueAt("nom1", 0, 0);
        ts.setValueAt("nom2", 1, 0);
        ts.setValueAt("nom3", 2, 0);
        ts.setValueAt(true, 0, 4);
        ts.setValueAt(true, 1, 4);
        ts.setValueAt(false, 2, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(3, tableS.getNbLignes());
        assertEquals(5, tableS.getNbColonnes());
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (i < 3) {
                    assertEquals(ts.getValueAt(i, j), tableS.getValueAt(i, j));
                }
            }
        }
    }
}