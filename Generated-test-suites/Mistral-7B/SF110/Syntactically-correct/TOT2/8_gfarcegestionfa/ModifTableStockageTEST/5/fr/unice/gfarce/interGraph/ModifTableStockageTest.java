import static org.junit.Assert.*;

import javax.swing.table.TableModel;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testConstructor() {
        TableStockage table = new TableStockage(2, 3); // create a TableStockage instance for testing
        ModifTableStockage modifTable = new ModifTableStockage(table);

        assertEquals(table.getRowCount() + 1, modifTable.getNbLignes());
        assertEquals(table.getColumnCount(), modifTable.getNbColonnes());
        assertEquals(table, modifTable.getTs());
    }
}