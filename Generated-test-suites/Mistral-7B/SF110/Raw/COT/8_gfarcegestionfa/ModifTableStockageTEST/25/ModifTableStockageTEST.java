import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        Assert.assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
        Assert.assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableResult = modifTableStockage.ajouterColonne("NewColumn", String.class);
        Assert.assertEquals(tableResult.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
        Assert.assertEquals(tableResult.getColumnName(modifTableStockage.getNbColonnes() - 1), "NewColumn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneNullClass() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneEmptyString() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("", String.class);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableResult = modifTableStockage.ajouterLigne();
        Assert.assertEquals(tableResult.getRowCount(), modifTableStockage.getNbLignes() + 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneNoColumn() {
        TableStockage ts = new TableStockage(0, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("Column1", String.class);
        modifTableStockage.ajouterColonne("Column2", String.class);
        TableStockage tableResult = modifTableStockage.supprimerColonne(0);
        Assert.assertEquals(tableResult.getColumnCount(), modifTableStockage.getNbColonnes() - 1);
        Assert.assertNotEquals(tableResult.getColumnName(0), "Column1");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneInvalidIndex() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("Column1", String.class);
        modifTableStockage.ajouterColonne("Column2", String.class);
        modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes());
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("Column1", String.class);
        modifTableStockage.ajouterColonne("Column2", String.class);
        modifTableStockage.ajouterLigne();
        modifTableStockage.ajouterLigne();
        TableStockage tableResult = modifTableStockage.supprimerLigne(1);
        Assert.assertEquals(tableResult.getRowCount(), modifTableStockage.getNbLignes() - 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneInvalidIndex() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("Column1", String.class);
        modifTableStockage.ajouterColonne("Column2", String.class);
        modifTableStockage.ajouterLigne();
        modifTableStockage.ajouterLigne();
        modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes());
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("Column1 Column2", String.class);
        TableStockage tableResult = modifTableStockage.separerColonnes(1);
        Assert.assertEquals(tableResult.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
        Assert.assertEquals(tableResult.getColumnName(modifTableStockage.getNbColonnes() - 1), "Column2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneInvalidIndex() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("Column1 Column2", String.class);
        modifTableStockage.separerColonnes(0);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("Column1", String.class);
        modifTableStockage.ajouterColonne("Column2", String.class);
        modifTableStockage.fusionnerColonnes(1);
        Assert.assertEquals(modifTableStockage.getNbColonnes(), 4);
        Assert.assertEquals(modifTableStockage.getColumnName(1), "Column1 Column2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesInvalidType() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.ajouterColonne("Column1", String.class);
        modifTableStockage.ajouterColonne("Column2", Boolean.class);
        modifTableStockage.fusionnerColonnes(1);
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(5, 5);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableResult = modifTableStockage.colonneMail();
        Assert.assertEquals(tableResult.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
        Assert.assertEquals(tableResult.getColumnName(tableResult.getNbColonnes() - 1), "Envoyer E-Mail a:");
        Assert.assertTrue(tableResult.getColumnClass(tableResult.getNbColonnes() - 1).equals(Boolean.class));
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 5);
        ts.setValueAt("test@example.com", 0, 0);
        ts.setValueAt("recipient1@example.com", 1, 0);
        ts.setValueAt("recipient2@example.com", 2, 0);
        Ts.setValueAt(true, 0, modifTableStockage.getNbColonnes() - 1);
        TableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableResult = modifTableStockage.selectMail();
        Assert.assertEquals(tableResult.getRowCount(), 1);
        Assert.assertEquals(tableResult.getValueAt(0, 0), "test@example.com");
        Assert.assertNull(tableResult.getValueAt(0, modifTableStockage.getNbColonnes() - 1));
    }

    @Test
    public void testSelectMailNoMail() {
        TableStockage ts = new TableStockage(5, 5);
        ts.setValueAt("test@example.com", 0, 0);
        ts.setValueAt("recipient1@example.com", 1, 0);
        ts.setValueAt("recipient2@example.com", 2, 0);
        TableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableResult = modifTableStockage.selectMail();
        Assert.assertEquals(tableResult.getRowCount(), 0);
    }
}