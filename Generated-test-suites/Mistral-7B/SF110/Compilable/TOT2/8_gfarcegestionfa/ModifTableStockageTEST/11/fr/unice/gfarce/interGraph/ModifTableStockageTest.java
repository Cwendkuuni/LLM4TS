package fr.unice.gfarce.interGraph;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        TableStockage expectedTable = new TableStockage(3, 2);
        expectedTable.setColumnName(0, "Name");
        expectedTable.setColumnName(1, "Age");

        ModifTableStockage modifTable = new ModifTableStockage(expectedTable);
        TableStockage actualTable = modifTable.ajouterColonne("NewColumn", Integer.class);

        // Basic Checks
        assertEquals(expectedTable.getRowCount() + 1, actualTable.getRowCount());
        assertEquals(expectedTable.getColumnCount() + 1, actualTable.getColumnCount());
        assertNotNull(actualTable.getColumnName(expectedTable.getColumnCount() - 1));
        assertSame(Integer.class, actualTable.getColumnClass(expectedTable.getColumnCount() - 1));

        // Checking the copied data
        for (int i = 0; i < expectedTable.getRowCount(); ++i) {
            for (int j = 0; j < expectedTable.getColumnCount() - 1; ++j) {
                assertEquals(expectedTable.getValueAt(i, j), actualTable.getValueAt(i, j));
            }
        }

        // Checking the new column values are set correctly
        for (int i = 0; i < expectedTable.getRowCount(); ++i) {
            assertNull(actualTable.getValueAt(i, expectedTable.getColumnCount() - 1));
        }
    }
}