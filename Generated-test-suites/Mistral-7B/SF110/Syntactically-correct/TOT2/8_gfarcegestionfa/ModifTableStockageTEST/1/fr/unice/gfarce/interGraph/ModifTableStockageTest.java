import static org.junit.Assert.assertEquals;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        final String nomNouvelleColonne = "NewColumn";
        final Class<?> type = String.class;
        final ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(3, 2));
        final TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() + 1);
        expectedTable.setColumnName(modifTableStockage.getNbColonnes() - 1, nomNouvelleColonne);
        final TableStockage actualTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(expectedTable, actualTable);
    }
}