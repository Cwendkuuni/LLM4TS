package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableModel ts = new JTableModel(); // Assuming JTableModel is a valid TableModel implementation
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
        assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        TableModel ts = new JTableModel(); // Assuming JTableModel is a valid TableModel implementation
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        String nomNouvelleColonne = "TestColumn";
        Class<?> type = String.class;

        TableModel tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(ts.getColumnCount() + 1, tableS.getColumnCount());
        assertEquals(ts.getRowCount() + 1, tableS.getRowCount());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(tableS.getColumnCount() - 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneNegativeNbLignes() {
        TableModel ts = new JTableModel(); // Assuming JTableModel is a valid TableModel implementation
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        String nomNouvelleColonne = "TestColumn";
        Class<?> type = String.class;

        modifTableStockage.setNbLignes(-1);
        modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
    }

    // Add more test methods for other methods in the ModifTableStockage class here.

    // Ensure to test all possible scenarios, including edge cases and invalid inputs.
}