package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

import java.awt.Component;
import java.util.Arrays;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(5, modifTableStockage.getNbLignes());
        assertEquals(3, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        String nomNouvelleColonne = "newColumn";
        Class<?> type = String.class;

        TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(4, tableS.getNbColonnes());
        assertEquals(5, tableS.getNbLignes());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(3));

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 3; ++j) {
                Object value = tableS.getValueAt(i, j);
                Object valueExpected = i < 4 ? modifTableStockage.ts.getValueAt(i, j) : null;
                assertEquals(valueExpected, value);
            }
        }

        String valueExpected = "";
        for (int i = 0; i < 5; ++i) {
            assertEquals(valueExpected, tableS.getValueAt(i, 3));
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage tableS = modifTableStockage.ajouterLigne();
        assertEquals(6, tableS.getNbLignes());
        assertEquals(3, tableS.getNbColonnes());

        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 3; ++j) {
                Object value = tableS.getValueAt(i, j);
                if (i >= 5) {
                    if (j == 0) {
                        assertEquals(Integer.valueOf(0), value);
                    } else {
                        assertEquals(null, value);
                    }
                } else {
                    Object valueExpected = modifTableStockage.ts.getValueAt(i, j);
                    assertEquals(valueExpected, value);
                }
            }
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage tableS = modifTableStockage.supprimerColonne(2);
        assertEquals(3, tableS.getNbColonnes());

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 3; ++j) {
                Object value = tableS.getValueAt(i, j);
                Object valueExpected = i < 3 ? modifTableStockage.ts.getValueAt(i, j) : null;
                assertEquals(valueExpected, value);
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage tableS = modifTableStockage.supprimerLigne(2);
        assertEquals(5, tableS.getNbLignes());

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 4; ++j) {
                Object value = tableS.getValueAt(i, j);
                Object valueExpected = i < 3 ? modifTableStockage.ts.getValueAt(i, j) : null;
                if (i == 2) {
                    if (j == 3) {
                        assertEquals(null, value);
                    } else {
                        assertEquals(valueExpected, value);
                    }
                } else {
                    assertEquals(valueExpected, value);
                }
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(5, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        String expectedCol1Name = "Colonne1";
        String expectedCol2Name = "Colonne2";
        modifTableStockage.ts.setValueAt("Colonne1 Colonne2", 0, 1);

        TableStockage tableS = modifTableStockage.separerColonne(1);
        assertEquals(5, tableS.getNbLignes());
        assertEquals(3, tableS.getNbColonnes());
        assertEquals(expectedCol1Name, tableS.getColumnName(0));
        assertEquals(expectedCol2Name, tableS.getColumnName(1));
        assertEquals(expectedCol1Name, tableS.getColumnName(2));

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 3; ++j) {
                Object value = tableS.getValueAt(i, j);
                if (j == 0) {
                    assertEquals("Colonne1", value);
                } else if (j == 1) {
                    if (i == 0) {
                        assertEquals("Colonne2", value);
                    } else {
                        assertEquals(null, value);
                    }
                } else {
                    Object valueExpected = modifTableStockage.ts.getValueAt(i, j);
                    assertEquals(valueExpected, value);
                }
            }
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        modifTableStockage.ts.setValueAt(true, 0, 1);
        modifTableStockage.ts.setValueAt(true, 0, 2);

        TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
        assertEquals(5, tableS.getNbLignes());
        assertEquals(2, tableS.getNbColonnes());

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 2; ++j) {
                Object value = tableS.getValueAt(i, j);
                Object valueExpected = i < 3 ? modifTableStockage.ts.getValueAt(i, j) : null;
                if (i >= 3) {
                    if (j == 0) {
                        assertEquals("Envoyer E-Mail a: " + Arrays.toString((Object[]) modifTableStockage.ts.getValueAt(i, 1))
                                + " " + Arrays.toString((Object[]) modifTableStockage.ts.getValueAt(i, 2)), value);
                    } else {
                        assertEquals(null, value);
                    }
                } else {
                    assertEquals(valueExpected, value);
                }
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage tableS = modifTableStockage.colonneMail();
        assertEquals(6, tableS.getNbLignes());
        assertEquals(4, tableS.getNbColonnes());

        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 4; ++j) {
                Object value = tableS.getValueAt(i, j);
                if (j == 3) {
                    assertEquals(Boolean.TRUE, value);
                } else {
                    Object valueExpected = i < 5 ? modifTableStockage.ts.getValueAt(i, j) : null;
                    assertEquals(valueExpected, value);
                }
            }
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        modifTableStockage.ts.setValueAt(Boolean.TRUE, 0, 3);
        modifTableStockage.ts.setValueAt(Boolean.TRUE, 1, 3);
        modifTableStockage.ts.setValueAt(Boolean.TRUE, 2, 3);
        modifTableStockage.ts.setValueAt(Boolean.FALSE, 3, 3);
        modifTableStockage.ts.setValueAt(Boolean.FALSE, 4, 3);

        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(3, tableS.getNbLignes());
        assertEquals(4, tableS.getNbColonnes());

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                Object value = tableS.getValueAt(i, j);
                Object valueExpected = modifTableStockage.ts.getValueAt(i, j);
                assertEquals(valueExpected, value);
            }
        }
    }

    @Test
    public void testSelectMailEmptyTable() {
        TableStockage ts = new TableStockage(5, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(0, tableS.getNbLignes());
        assertEquals(4, tableS.getNbColonnes());
    }

    @Test
    public void testSelectMailNoBooleanColumn() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(5, tableS.getNbLignes());
        assertEquals(3, tableS.getNbColonnes());
    }
}