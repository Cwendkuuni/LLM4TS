package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableStockage tableStockage;

    @Before
    public void setup() {
        Object[][] data = {{"Name1", "Value1"}, {"Name2", "Value2"}};
        tableStockage = new TableStockage(2, 2);
        tableStockage.setData(data);
        modifTableStockage = new ModifTableStockage(tableStockage);
    }

    @Test
    public void testModifTableStockageConstructor() {
        assertNotNull(modifTableStockage);
        assertEquals(tableStockage.getRowCount(), modifTableStockage.getNbLignes());
        assertEquals(tableStockage.getColumnCount(), modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage tableS = modifTableStockage.ajouterColonne("New Column", String.class);
        assertNotNull(tableS);
        assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithInvalidType() {
        modifTableStockage.ajouterColonne("New Column", int.class);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage tableS = modifTableStockage.ajouterLigne();
        assertNotNull(tableS);
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
    }

    @Test
    public void testSupprimerColonne() {
        modifTableStockage.ajouterColonne("New Column", String.class);
        TableStockage tableS = modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes() - 1);
        assertNull(tableS.getColumnName(modifTableStockage.getNbColonnes() - 1));
        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage tableS = modifTableStockage.supprimerLigne(0);
        assertNull(tableS.getValueAt(0, 0));
        assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        modifTableStockage.ajouterColonne("Name Value", String.class);
        modifTableStockage.setValueAt("Name Value  Value1 Value2", 0, modifTableStockage.getNbColonnes() - 1);
        TableStockage tableS = modifTableStockage.separerColonne(modifTableStockage.getNbColonnes() - 1);
        Object[][] expectedData = {{"Name", "Value1"}, {"Name", "Value2"}};
        assertArrayEquals(expectedData, tableS.getModel().getData());
        assertEquals(2, tableS.getRowCount());
        assertEquals(2, tableS.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        modifTableStockage.ajouterColonne("Boolean1", Boolean.class);
        modifTableStockage.ajouterColonne("Boolean2", Boolean.class);
        TableStockage tableS = modifTableStockage.fusionnerColonnes(modifTableStockage.getNbColonnes() - 2);
        Object[][] expectedData = {{"Boolean1", "Boolean2"}, {"true, true", "true, true"}, {"true, false", "true, false"}, {"false, true", "false, true"}, {"false, false", "false, false"}};
        assertArrayEquals(expectedData, tableS.getModel().getData());
        assertEquals(4, tableS.getRowCount());
        assertEquals(2, tableS.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        TableStockage tableS = modifTableStockage.colonneMail();
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
        assertNotNull(tableS.getColumnName(modifTableStockage.getNbColonnes()));
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(modifTableStockage.getNbColonnes()));
    }

    @Test
    public void testSelectMail() {
        modifTableStockage.ajouterColonne("Send Email", Boolean.class);
        TableStockage tableS = modifTableStockage.selectMail();
        assertNotNull(tableS);
        assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
    }
}