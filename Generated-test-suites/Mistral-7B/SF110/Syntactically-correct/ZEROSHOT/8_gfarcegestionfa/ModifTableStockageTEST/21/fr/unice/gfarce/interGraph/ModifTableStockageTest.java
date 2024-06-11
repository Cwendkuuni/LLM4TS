package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertNotNull(modifTableStockage.ts);
        assertEquals(5, modifTableStockage.nbLignes);
        assertEquals(3, modifTableStockage.nbColonnes);
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("Test", 0, 0);
        ts.setValueAt("Value", 0, 1);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterColonne("Nouvelle colonne", String.class);

        assertEquals(4, tableS.getColumnCount());
        assertEquals("Nouvelle colonne", tableS.getColumnName(3));
        assertEquals("Test", tableS.getValueAt(0, 0));
        assertEquals("Value", tableS.getValueAt(0, 1));
        assertNull(tableS.getValueAt(0, 3));
    }

    @Test
    public void testAjouterColonneWithSameType() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("Test", 0, 0);
        ts.setValueAt("Value", 0, 1);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterColonne("Nouvelle colonne", String.class);

        TableStockage tableS2 = modifTableStockage.ajouterColonne("Nouvelle colonne 2", String.class);

        assertEquals(5, tableS2.getColumnCount());
        assertEquals("Nouvelle colonne 2", tableS2.getColumnName(4));
        assertEquals("Test", tableS2.getValueAt(0, 0));
        assertEquals("Value", tableS2.getValueAt(0, 1));
        assertEquals("", tableS2.getValueAt(0, 3));
        assertEquals("", tableS2.getValueAt(0, 4));
    }

    @Test
    public void testAjouterColonneWithDifferentType() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("Test", 0, 0);
        ts.setValueAt(1.0, 0, 1);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertThrows(IllegalArgumentException.class, () -> {
            modifTableStockage.ajouterColonne("Nouvelle colonne", Double.class);
        });

        TableStockage tableS = modifTableStockage.ajouterColonne("Nouvelle colonne", String.class);

        assertEquals(4, tableS.getColumnCount());
        assertEquals("Nouvelle colonne", tableS.getColumnName(3));
        assertEquals("Test", tableS.getValueAt(0, 0));
        assertEquals(1.0, tableS.getValueAt(0, 1));
        assertNull(tableS.getValueAt(0, 3));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("Test", 0, 0);
        ts.setValueAt("Value", 0, 1);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterLigne();

        assertEquals(4, tableS.getRowCount());
        assertEquals("", tableS.getValueAt(3, 0));
        assertEquals("", tableS.getValueAt(3, 1));
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt("Test1", 0, 0);
        ts.setValueAt("Test2", 0, 1);
        ts.setValueAt("Test3", 0, 2);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerColonne(1);

        assertEquals(2, tableS.getColumnCount());
        assertEquals("Test1", tableS.getValueAt(0, 0));
        assertNull(tableS.getValueAt(0, 1));
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt("Test1", 0, 0);
        ts.setValueAt("Test2", 0, 1);
        ts.setValueAt("Test3", 0, 2);
        ts.setValueAt("Test4", 1, 0);
        ts.setValueAt("Test5", 1, 1);
        ts.setValueAt("Test6", 1, 2);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.supprimerLigne(0);

        assertEquals(4, tableS.getRowCount());
        assertEquals("Test4", tableS.getValueAt(0, 0));
        assertEquals("Test5", tableS.getValueAt(0, 1));
        assertEquals("Test6", tableS.getValueAt(0, 2));
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(5, 2);
        ts.setValueAt("Test1 Test2", 0, 0);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.separerColonne(0);

        assertEquals(3, tableS.getColumnCount());
        assertEquals("Test1", tableS.getValueAt(0, 0));
        assertEquals("Test2", tableS.getValueAt(0, 1));
        assertEquals("", tableS.getValueAt(0, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneWithInvalidType() {
        TableStockage ts = new TableStockage(5, 2);
        ts.setValueAt(true, 0, 0);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.separerColonne(0);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt("Test1", 0, 0);
        ts.setValueAt("Test2", 0, 1);
        ts.setValueAt("Test3", 0, 2);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.fusionnerColonnes(1);

        assertEquals(3, tableS.getColumnCount());
        assertEquals("Test1 Test2", tableS.getValueAt(0, 0).toString());
        assertEquals("", tableS.getValueAt(0, 1));
        assertEquals("Test3", tableS.getValueAt(0, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesWithInvalidTypes() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setValueAt("Test1", 0, 0);
        ts.setValueAt(true, 0, 1);
        ts.setValueAt("Test3", 0, 2);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.fusionnerColonnes(1);
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(5, 2);
        ts.setValueAt("Test1", 0, 0);
        ts.setValueAt("Test2", 0, 1);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.colonneMail();

        assertEquals(3, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(2));
        assertEquals("Test1", tableS.getValueAt(0, 0));
        assertEquals("Test2", tableS.getValueAt(0, 1));
        assertEquals(true, tableS.getValueAt(0, 2));
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(6, 3);
        ts.setValueAt("Test1", 0, 0);
        ts.setValueAt("Test2", 0, 1);
        ts.setValueAt("Test3", 0, 2);
        ts.setValueAt(true, 1, 2);
        ts.setValueAt("Test4", 2, 0);
        ts.setValueAt("Test5", 3, 0);
        ts.setValueAt("Test6", 4, 0);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.selectMail();

        assertEquals(3, tableS.getRowCount());
        assertEquals("Test2", tableS.getValueAt(0, 0));
        assertEquals("Test4", tableS.getValueAt(1, 0));
        assertEquals("Test5", tableS.getValueAt(2, 0));
    }
}