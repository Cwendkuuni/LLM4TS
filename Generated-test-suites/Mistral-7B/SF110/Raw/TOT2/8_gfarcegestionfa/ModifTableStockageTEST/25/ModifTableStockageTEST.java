import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        Class<?> type = String.class;
        String nomNouvelleColonne = "New Column";
        ModifTableStockage modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage expectedTable = new TableStockage(3, 4);
        TableStockage actualTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);

        assertArrayEquals(expectedTable.getColumnNames(), actualTable.getColumnNames());
        assertArrayEquals(expectedTable.getRowData(), actualTable.getRowData());
        assertEquals(4, actualTable.getColumnCount());
    }
}