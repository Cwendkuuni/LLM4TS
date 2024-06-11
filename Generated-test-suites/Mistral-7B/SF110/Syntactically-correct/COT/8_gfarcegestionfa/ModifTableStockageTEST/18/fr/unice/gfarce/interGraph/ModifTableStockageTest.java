package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(4, modifTableStockage.getNbColonnes());
        assertEquals(5, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage expectedTable = new TableStockage(5, 4);

        String newColumnName = "NouvelleColonne";
        Class<?> newColumnType = String.class;

        TableStockage resultTable = modifTableStockage.ajouterColonne(newColumnName, newColumnType);

        assertEquals(expectedTable.getColumnName(3), newColumnName);
        assertEquals(expectedTable.getColumnClass(3), newColumnType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithNullColumnName() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        Class<?> newColumnType = String.class;
        String nullColumnName = null;

        modifTableStockage.ajouterColonne(nullColumnName, newColumnType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithNullColumnType() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        String newColumnName = "NouvelleColonne";
        Class<?> nullColumnType = null;

        modifTableStockage.ajouterColonne(newColumnName, nullColumnType);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage expectedTable = new TableStockage(4, 3);

        TableStockage resultTable = modifTableStockage.ajouterLigne();

        assertEquals(expectedTable.getRowCount(), resultTable.getRowCount());
        assertNotNull(resultTable.getValueAt(resultTable.getRowCount() - 1, 0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneWithZeroColumns() {
        TableStockage ts = new TableStockage(3, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int columnIndex = 2;
        TableStockage expectedTable = new TableStockage(3, 3);

        TableStockage resultTable = modifTableStockage.supprimerColonne(columnIndex);

        assertEquals(expectedTable.getColumnCount(), resultTable.getColumnCount());
        assertFalse(resultTable.isCellEditable(columnIndex, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneWithInvalidIndex() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int invalidIndex = 5;

        modifTableStockage.supprimerColonne(invalidIndex);
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int rowIndex = 2;
        TableStockage expectedTable = new TableStockage(2, 4);

        TableStockage resultTable = modifTableStockage.supprimerLigne(rowIndex);

        assertEquals(expectedTable.getRowCount(), resultTable.getRowCount());
        assertNull(resultTable.getValueAt(rowIndex, 0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneWithInvalidIndex() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int invalidIndex = 5;

        modifTableStockage.supprimerLigne(invalidIndex);
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt("Nom Prénom 12 Ans", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int columnIndex = 0;
        TableStockage expectedTable = new TableStockage(3, 2);

        TableStockage resultTable = modifTableStockage.separerColonne(columnIndex);

        assertEquals(expectedTable.getColumnCount(), resultTable.getColumnCount());
        assertEquals(expectedTable.getValueAt(0, 1).toString(), "Nom");
        assertEquals(expectedTable.getValueAt(0, 2).toString(), "Prénom");
    }

    @Test
    public void testSeparerColonneWithEmptyString() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt("", 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int columnIndex = 0;
        TableStockage expectedTable = new TableStockage(3, 2);

        TableStockage resultTable = modifTableStockage.separerColonne(columnIndex);

        assertEquals(expectedTable.getColumnCount(), resultTable.getColumnCount());
        assertEquals(expectedTable.getValueAt(0, 1).toString(), "");
        assertEquals(expectedTable.getValueAt(0, 2).toString(), "");
    }

    @Test(expected = IllegalStateException.class)
    public void testSeparerColonneWithNonStringType() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt(12, 0, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int columnIndex = 0;
        modifTableStockage.separerColonne(columnIndex);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("false", 0, 0);
        ts.setValueAt("true", 0, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int columnIndex = 0;
        TableStockage expectedTable = new TableStockage(3, 1);
        expectedTable.setColumnName(0, "Envoyer E-Mail a:");

        TableStockage resultTable = modifTableStockage.fusionnerColonnes(columnIndex);

        assertEquals(expectedTable.getColumnCount(), resultTable.getColumnCount());
        assertEquals(expectedTable.getColumnName(0), resultTable.getColumnName(0));
        assertEquals(expectedTable.getValueAt(0, 0).toString(), "false true");
    }

    @Test(expected = IllegalStateException.class)
    public void testFusionnerColonnesWithNonBooleanType() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setValueAt("Nom Prénom", 0, 0);
        ts.setValueAt("Ancien Prénom", 0, 1);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int columnIndex = 0;
        modifTableStockage.fusionnerColonnes(columnIndex);
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage expectedTable = new TableStockage(3, 4);
        expectedTable.setColumnName(3, "Envoyer E-Mail a:");
        expectedTable.setColumnClass(3, Boolean.class);

        for (int i = 0; i < 3; ++i) {
            expectedTable.setValueAt(true, i, 3);
        }

        TableStockage resultTable = modifTableStockage.colonneMail();

        assertEquals(expectedTable.getColumnCount(), resultTable.getColumnCount());
        assertEquals(expectedTable.getColumnName(3), resultTable.getColumnName(3));
        assertEquals(expectedTable.getColumnClass(3), resultTable.getColumnClass(3));
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(3, 4);
        ts.setColumnName(0, "ID");
        ts.setColumnName(1, "Nom");
        ts.setColumnName(2, "Age");
        ts.setColumnName(3, "Envoyer E-Mail a:");
        ts.setValueAt(1, 0, 0);
        ts.setValueAt("Alice", 1, 1);
        ts.setValueAt(35, 1, 2);
        ts.setValueAt(true, 1, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int columnIndex = 3;

        TableStockage expectedTable = new TableStockage(1, 4);
        expectedTable.setColumnName(0, "ID");
        expectedTable.setColumnName(1, "Nom");
        expectedTable.setColumnName(2, "Age");
        expectedTable.setColumnName(3, "Envoyer E-Mail a:");
        expectedTable.setValueAt(1, 0, 0);
        expectedTable.setValueAt("Alice", 0, 1);
        expectedTable.setValueAt(35, 0, 2);
        expectedTable.setValueAt(true, 0, 3);

        TableStockage resultTable = modifTableStockage.selectMail();

        assertEquals(expectedTable.getRowCount(), resultTable.getRowCount());
        assertEquals(expectedTable.getValueAt(0, 0).toString(), "1");
        assertEquals(expectedTable.getValueAt(0, 1).toString(), "Alice");
        assertEquals(expectedTable.getValueAt(0, 2).toString(), "35");
        assertEquals(expectedTable.getValueAt(0, 3).toString(), "true");
    }
}