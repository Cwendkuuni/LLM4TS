import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        TableStockage table = new TableStockage(3, 3);
        ModifTableStockage modifTable = new ModifTableStockage(table);

        ModifTableStockage newModifTable = modifTable.ajouterColonne("New Column", Integer.class);

        assertEquals(4, newModifTable.nbColonnes);
        assertEquals("New Column", newModifTable.ts.getColumnName(3));
    }
}