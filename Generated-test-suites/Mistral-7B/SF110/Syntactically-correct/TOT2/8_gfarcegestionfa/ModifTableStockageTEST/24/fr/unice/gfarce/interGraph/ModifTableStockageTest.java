import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage tableStockage = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(tableStockage);
        assertEquals(tableStockage.getRowCount() + 1, modifTableStockage.nbLignes);
        assertEquals(tableStockage.getColumnCount(), modifTableStockage.nbColonnes);
    }
}