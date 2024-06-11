import org.junit.Assert;
import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        Class<?> type = String.class; // Can be any valid class
        String nomNouvelleColonne = "New Column";
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.ajouterColonne(nomNouvelleColonne, type);
        int newNbColonnes = tableS.getColumnCount();
        Assert.assertEquals(newNbColonnes, mts.nbColonnes); // Check if the number of columns has been updated
        Assert.assertEquals(nomNouvelleColonne, tableS.getColumnName(newNbColonnes - 1)); // Check if the new column name is correct
    }

    @Test
    public void testAjouterColonneWithEmptyTable() {
        TableStockage ts = new TableStockage(0, 0);
        ModifTableStockage mts = new ModifTableStockage(ts);
        TableStockage tableS = mts.ajouterColonne("New Column", String.class);
        int newNbColonnes = tableS.getColumnCount();
        Assert.assertEquals(newNbColonnes, mts.nbColonnes); // Check if the number of columns has been updated
        Assert.assertEquals("New Column", tableS.getColumnName(newNbColonnes - 1)); // Check if the new column name is correct
    }
}