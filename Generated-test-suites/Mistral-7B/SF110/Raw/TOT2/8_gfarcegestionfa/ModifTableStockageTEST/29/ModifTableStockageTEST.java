import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage tableStockage = new TableStockage(new Object()[0]);
        ModifTableStockage modifTableStockage = new ModifTableStockage(tableStockage);
        assertNotNull(modifTableStockage);
    }
}