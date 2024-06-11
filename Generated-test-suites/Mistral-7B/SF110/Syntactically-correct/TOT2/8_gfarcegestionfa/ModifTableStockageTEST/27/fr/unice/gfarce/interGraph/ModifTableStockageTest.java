import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testAddColumn() {
        TableStockage initialTable = new TableStockage(5, 3);
        ModifTableStockage modifTable = new ModifTableStockage(initialTable);
        String newColumnName = "NewColumnName";
        Class<?> newColumnType = String.class;
        TableStockage expectedTable = new TableStockage(5, 4);

        TableStockage actualTable = modifTable.ajouterColonne(newColumnName, newColumnType);

        assertEquals(expectedTable, actualTable);
    }
}