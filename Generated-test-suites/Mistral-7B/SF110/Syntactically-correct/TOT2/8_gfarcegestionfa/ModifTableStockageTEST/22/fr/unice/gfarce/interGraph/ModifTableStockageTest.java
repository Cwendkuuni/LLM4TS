import org.junit.Test;
import static org.junit.Assert.assertEquals;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        TableStockage originalTable = new TableStockage(2, 2);
        originalTable.setColumnName(0, "Name");
        originalTable.setColumnName(1, "Age");
        ModifTableStockage modifTable = new ModifTableStockage(originalTable);
        TableStockage expectedTable = new TableStockage(2, 3);
        expectedTable.setColumnName(0, "Name");
        expectedTable.setColumnName(1, "Age");
        expectedTable.setColumnName(2, "NewColumn");
        ModifTableStockage.ClassType newColumnType = ModifTableStockage.ClassType.String; // You can change this to test other types
        expectedTable.setColumnClass(2, newColumnType);

        TableStockage actualTable = modifTable.ajouterColonne("NewColumn", newColumnType);

        assertEquals(expectedTable, actualTable);
    }
}