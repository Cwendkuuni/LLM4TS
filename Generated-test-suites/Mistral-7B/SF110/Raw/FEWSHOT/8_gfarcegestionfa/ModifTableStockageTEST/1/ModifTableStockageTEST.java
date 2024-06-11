package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockage() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(5, modifTableStockage.getNbColonnes());
        assertEquals(3, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        final String nomNouvelleColonne = "newCol";
        final Class<?> type = String.class;
        ModifTableStockage newModifTableStockage = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(6, newModifTableStockage.getNbColonnes());
        assertEquals(3, newModifTableStockage.getNbLignes());
        assertEquals(nomNouvelleColonne, newModifTableStockage.getColumnName(5));
        assertEquals(type, newModifTableStockage.getColumnClass(5));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        ModifTableStockage newModifTableStockage = modifTableStockage.ajouterLigne();
        assertEquals(4, newModifTableStockage.getNbLignes());
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (i < 3) {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), newModifTableStockage.ts.getValueAt(i, j));
                }
                if (j < 5 - 1) {
                    assertEquals(modifTableStockage.ts.getColumnClass(j), newModifTableStockage.ts.getColumnClass(j));
                } else {
                    assertEquals(modifTableStockage.ts.getColumnClass(j - 1), newModifTableStockage.ts.getColumnClass(j));
                }
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        ModifTableStockage newModifTableStockage = modifTableStockage.supprimerColonne(0);
        assertEquals(4, newModifTableStockage.getNbColonnes());
        assertEquals(3, newModifTableStockage.getNbLignes());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (j != 0) {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j - 1), newModifTableStockage.ts.getValueAt(i, j));
                    assertEquals(modifTableStockage.ts.getColumnClass(j - 1), newModifTableStockage.ts.getColumnClass(j));
                }
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        ModifTableStockage newModifTableStockage = modifTableStockage.supprimerLigne(0);
        assertEquals(3, newModifTableStockage.getNbLignes());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                if (i != 0) {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), newModifTableStockage.ts.getValueAt(i - 1, j));
                }
                if (i < 2) {
                    assertEquals(modifTableStockage.ts.getColumnClass(j), newModifTableStockage.ts.getColumnClass(j));
                } else {
                    assertEquals(modifTableStockage.ts.getColumnClass(j - 1), newModifTableStockage.ts.getColumnClass(j));
                }
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        ModifTableStockage newModifTableStockage = modifTableStockage.separerColonne(1);
        assertEquals(6, newModifTableStockage.getNbColonnes());
        assertEquals(3, newModifTableStockage.getNbLignes());
        assertEquals(modifTableStockage.getColumnName(1), newModifTableStockage.getColumnName(1));
        assertEquals(modifTableStockage.getColumnName(2), newModifTableStockage.getColumnName(2));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                if (i < 3 && j < 6 - 1) {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), newModifTableStockage.ts.getValueAt(i, j));
                }
                if (j == 5) {
                    assertNull(newModifTableStockage.ts.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 6);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        ModifTableStockage newModifTableStockage = modifTableStockage.fusionnerColonnes(3);
        assertEquals(5, newModifTableStockage.getNbColonnes());
        assertEquals(3, newModifTableStockage.getNbLignes());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                if (j < 3) {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), newModifTableStockage.ts.getValueAt(i, j));
                } else {
                    String value = (String) modifTableStockage.ts.getValueAt(i, j - 1);
                    String valueFused = value.concat(" " + value);
                    assertEquals(valueFused, (String) newModifTableStockage.ts.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        ModifTableStockage newModifTableStockage = modifTableStockage.colonneMail();
        assertEquals(6, newModifTableStockage.getNbColonnes());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                if (j == 5) {
                    assertEquals(Boolean.TRUE, newModifTableStockage.ts.getValueAt(i, j));
                } else {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), newModifTableStockage.ts.getValueAt(i, j));
                }
            }
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(3, 6);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        ModifTableStockage newModifTableStockage = modifTableStockage.selectMail();
        assertEquals(3, newModifTableStockage.getNbLignes());
        assertEquals(6, newModifTableStockage.getNbColonnes());
        String columnName = "Envoyer E-Mail a:";
        int indexOfColumn = -1;
        for (int i = 0; i < 6; i++) {
            if (modifTableStockage.ts.getColumnName(i).equals(columnName)) {
                indexOfColumn = i;
                break;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                if (i < 3 && j != indexOfColumn) {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), newModifTableStockage.ts.getValueAt(i, j));
                }
            }
        }
    }
}