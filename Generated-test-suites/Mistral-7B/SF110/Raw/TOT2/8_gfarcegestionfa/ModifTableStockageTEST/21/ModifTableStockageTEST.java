
package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

public class ModifTableStockageTest {
    @Test
    public void testAjouterColonne() {
        // ...
    }

    @Test
    public void testAjouterLigne() {
        // ...
    }

    @Test
    public void testSupprimerColonne_NonExistentIndice() {
        // ...
    }

    @Test
    public void testSeparerColonne_InvalidColumnIndex() {
        // ...
    }

    @Test
    public void testAjouterColonne_DuplicateColumnName() {
        // ...
    }

    @Test
    public void testAjouterLigne_OutOfBoundsIndex() {
        // ...
    }

    @Test
    public void testSupprimerColonne_NonExistentColumn() {
        // ...
    }

    @Test
    public void testSupprimerLigne_NonExistentRow() {
        // ...
    }

    @Test
    public void testAjouterColonne_InvalidColumnName() {
        // ...
    }

    @Test
    public void testAjouterColonne_InvalidColumnType() {
        // ...
    }

    @Test
    public void testAjouterColonne_MaximumRowsReached() {
        // ...
    }

    @Test
    public void testAjouterColonne_MaximumColumnsReached() {
        // ...
    }

    @Test
    public void testAjouterLigne_EmptyTable() {
        // ...
    }

    // Add other test cases for other methods if required

    @Test
    public void testModifTableStockage_EmptyTable() {
        TableStockage ts = new TableStockage(0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertNotNull(modifTableStockage);
        assertEquals(0, modifTableStockage.getNbColonnes());
        assertEquals(0, modifTableStockage.getNbLignes());
    }

    @Test
    public void testModifTableStockage_ValidInitialTable() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(3, modifTableStockage.getNbColonnes());
        assertEquals(3, modifTableStockage.getNbLignes());
    }

    @Test
    public void testModifTableStockage_ModifiedTable() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage tableS = modifTableStockage.ajouterColonne("New Column", String.class);
        assertEquals(4, tableS.getColumnCount());
        assertEquals("New Column", tableS.getColumnName(3));

        tableS = modifTableStockage.ajouterLigne();
        assertEquals(4, tableS.getRowCount());

        tableS = modifTableStockage.supprimerColonne(0);
        assertEquals(3, tableS.getColumnCount());

        tableS = modifTableStockage.supprimerLigne(0);
        assertEquals(3, tableS.getRowCount());

        tableS = modifTableStockage.separerColonne(0);
        assertEquals(5, tableS.getColumnCount());

        tableS = modifTableStockage.fusionnerColonnes(1);
        assertEquals(3, tableS.getColumnCount());

        tableS = modifTableStockage.colonneMail();
        assertEquals(5, tableS.getColumnCount());

        tableS = modifTableStockage.selectMail();
        assertEquals(3, tableS.getRowCount());
    }
}