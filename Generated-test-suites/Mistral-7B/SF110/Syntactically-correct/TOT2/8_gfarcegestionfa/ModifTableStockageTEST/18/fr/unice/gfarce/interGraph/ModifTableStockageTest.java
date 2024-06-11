import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;

    @Before
    public void setUp() {
        TableStockage tableStockage = new TableStockage(3, 4);
        modifTableStockage = new ModifTableStockage(tableStockage);
    }

    @Test
    public void testModifTableStockageConstructor() {
        assertEquals(3, modifTableStockage.getNbLignes());
        assertEquals(4, modifTableStockage.getNbColonnes());
    }
}