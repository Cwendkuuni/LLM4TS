import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(1, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        Class<?> type = String.class;
        String nomNouvelleColonne = "TestColumn";
        TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertNotNull(result);
        assertEquals(modifTableStockage.getNbColonnes(), result.getNbColonnes());
        assertEquals(nomNouvelleColonne, result.getColumnName(result.getNbColonnes() - 1));
    }
}