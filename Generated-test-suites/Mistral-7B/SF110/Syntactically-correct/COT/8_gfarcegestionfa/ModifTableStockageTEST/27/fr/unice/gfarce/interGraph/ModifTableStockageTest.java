import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.TableModel;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        Assert.assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
        Assert.assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterColonne("newCol", String.class);
        Assert.assertEquals(modifTableStockage.getNbColonnes(), result.getColumnCount());
        Assert.assertEquals("newCol", result.getColumnName(result.getColumnCount() - 1));
    }

    @Test
    public void testAjouterColonneWithInvalidType() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        try {
            modifTableStockage.ajouterColonne("newCol", Integer.class);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Type invalide pour ajouter une colonne : Integer", e.getMessage());
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterLigne();
        if (modifTableStockage.getNbColonnes() > 0) {
            Assert.assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
            for (int i = 0; i < result.getRowCount() - 1; ++i) {
                for (int j = 0; j < result.getColumnCount(); ++j) {
                    if (modifTableStockage.getColumnClass(j).equals(String.class)) {
                        Assert.assertEquals("", result.getValueAt(i, j).toString());
                    } else if (modifTableStockage.getColumnClass(j).equals(Double.class)) {
                        Assert.assertEquals(0.0, ((Number) result.getValueAt(i, j)).doubleValue(), 0.001);
                    } else {
                        Assert.assertEquals(false, ((Boolean) result.getValueAt(i, j)).booleanValue());
                    }
                }
            }
        } else {
            Assert.assertEquals(ts, result);
        }
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerColonne(1);
        Assert.assertEquals(modifTableStockage.getNbColonnes() - 1, result.getColumnCount());
        for (int i = 0; i < result.getRowCount(); ++i) {
            for (int j = 0; j < result.getColumnCount(); ++j) {
                Assert.assertEquals(modifTableStockage.getValueAt(i, j), result.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerLigne(1);
        Assert.assertEquals(modifTableStockage.getNbLignes() - 1, result.getRowCount());
        for (int i = 0; i < result.getRowCount(); ++i) {
            for (int j = 0; j < result.getColumnCount(); ++j) {
                Assert.assertEquals(modifTableStockage.getValueAt(i, j), result.getValueAt(i, j));
            }
        }
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt("1 2", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(0);
        Assert.assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
        Assert.assertEquals("1", result.getColumnName(0));
        Assert.assertEquals("2", result.getColumnName(1));
        for (int i = 0; i < result.getRowCount(); ++i) {
            for (int j = 0; j < result.getColumnCount(); ++j) {
                if (j == 0) {
                    Assert.assertEquals("1", result.getValueAt(i, j).toString());
                } else {
                    Assert.assertEquals("2", result.getValueAt(i, j).toString());
                }
            }
        }
    }

    @Test
    public void testSeparerColonneWithEmptyString() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt("", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(0);
        Assert.assertEquals(modifTableStockage.getNbColonnes(), result.getColumnCount());
        Assert.assertEquals("", result.getColumnName(0));
    }

    @Test
    public void testSeparerColonneWithNoSpace() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt("12", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        try {
            modifTableStockage.separerColonne(0);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Impossible de séparer une colonne invalide : 12", e.getMessage());
        }
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt(true, 0, 0);
        ts.setValueAt(false, 0, 1);
        ts.setValueAt(true, 1, 0);
        ts.setValueAt(false, 1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.fusionnerColonnes(0);
        Assert.assertEquals(modifTableStockage.getNbColonnes() - 1, result.getColumnCount());
        Assert.assertEquals("Envoyer E-Mail a: true, false", result.getColumnName(0));
        for (int i = 0; i < result.getRowCount(); ++i) {
            Assert.assertEquals(modifTableStockage.getValueAt(i, 0).toString() + ", " + modifTableStockage.getValueAt(i, 1).toString(), result.getValueAt(i, 0).toString());
        }
    }

    @Test
    public void testFusionnerColonnesWithEmptyString() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("", 0, 0);
        ts.setValueAt("", 0, 1);
        ts.setValueAt(true, 1, 0);
        ts.setValueAt(false, 1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        try {
            modifTableStockage.fusionnerColonnes(0);
            Assert.fail("Exception expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("PAS DE TYPE STRING", e.getMessage());
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.colonneMail();
        Assert.assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
        Assert.assertEquals("Envoyer E-Mail a:", result.getColumnName(modifTableStockage.getNbColonnes() - 1));
        for (int i = 0; i < result.getRowCount(); ++i) {
            Assert.assertEquals(true, ((Boolean) result.getValueAt(i, modifTableStockage.getNbColonnes() - 1)).booleanValue());
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt("true", 0, 2);
        ts.setValueAt("false", 1, 2);
        ts.setValueAt("true", 2, 2);
        ts.setValueAt("false", 3, 2);
        ts.setValueAt("true", 4, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        Assert.assertEquals(3, result.getRowCount());
        for (int i = 0; i < result.getRowCount(); ++i) {
            Assert.assertEquals(true, ((Boolean) result.getValueAt(i, 2)).booleanValue());
        }
    }

    @Test
    public void testSelectMailWithNoBooleanColumn() {
        TableStockage ts = new TableStockage(5, 2);
        ts.setValueAt("name1", 0, 0);
        ts.setValueAt("name2", 1, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        Assert.assertEquals(0, result.getRowCount());
    }
}