package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableModel tableModel;

    @Before
    public void setUp() {
        tableModel = new Object[5][5];
        modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
    }

    @Test
    public void testModifTableStockageConstruction() {
        assertEquals(0, modifTableStockage.getNbColonnes());
        assertEquals(0, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        modifTableStockage.ajouterColonne("Nom", String.class);
        assertEquals(1, modifTableStockage.getNbColonnes());

        String[] columnNames = modifTableStockage.ts.getColumnNames();
        assertArrayEquals(new String[]{"Nom"}, columnNames);

        TableModel newTableModel = modifTableStockage.ts;
        assertEquals(1, newTableModel.getColumnCount());
        assertEquals("Nom", newTableModel.getColumnName(0));
    }

    @Test
    public void testAjouterLigne() {
        modifTableStockage.ajouterColonne("Nom", String.class);
        TableModel newTableModel = modifTableStockage.ajouterLigne();

        assertEquals(2, modifTableStockage.getNbLignes());

        String[] columnNames = modifTableStockage.ts.getColumnNames();
        assertArrayEquals(new String[]{"Nom"}, columnNames);

        Object[] data1 = newTableModel.getDataVector().getElementAt(0);
        Object[] data2 = newTableModel.getDataVector().getElementAt(1);

        assertArrayEquals(new Object[]{"", ""}, data1);
        assertArrayEquals(new Object[]{"", ""}, data2);
    }

    @Test
    public void testSupprimerColonne() {
        modifTableStockage.ajouterColonne("Nom", String.class);
        modifTableStockage.ajouterColonne("Prénom", String.class);
        TableModel newTableModel = modifTableStockage.supprimerColonne(1);

        assertEquals(2, modifTableStockage.getNbColonnes());

        String[] columnNames = modifTableStockage.ts.getColumnNames();
        assertArrayEquals(new String[]{"Nom"}, columnNames);

        Object[] data1 = newTableModel.getDataVector().getElementAt(0);
        Object[] data2 = newTableModel.getDataVector().getElementAt(1);

        assertArrayEquals(new Object[]{"", ""}, data1);
        assertArrayEquals(new Object[]{"", ""}, data2);
    }

    @Test
    public void testSupprimerLigne() {
        modifTableStockage.ajouterColonne("Nom", String.class);
        modifTableStockage.ajouterColonne("Prénom", String.class);
        modifTableStockage.ajouterLigne();
        modifTableStockage.ajouterLigne();

        TableModel newTableModel = modifTableStockage.supprimerLigne(1);

        assertEquals(4, modifTableStockage.getNbLignes());

        String[] columnNames = modifTableStockage.ts.getColumnNames();
        assertArrayEquals(new String[]{"Nom", "Prénom"}, columnNames);

        Object[] data1 = newTableModel.getDataVector().getElementAt(0);
        Object[] data2 = newTableModel.getDataVector().getElementAt(1);
        Object[] data3 = newTableModel.getDataVector().getElementAt(2);
        Object[] data4 = newTableModel.getDataVector().getElementAt(3);

        assertArrayEquals(new Object[]{"", ""}, data1);
        assertArrayEquals(new Object[]{"", ""}, data2);
        assertArrayEquals(new Object[]{"", ""}, data3);
        assertArrayEquals(new Object[]{"", ""}, data4);
    }

    @Test
    public void testSeparerColonne() {
        modifTableStockage.ajouterColonne("Nom Prénom", String.class);

        TableModel newTableModel = modifTableStockage.separerColonne(0);

        assertEquals(3, modifTableStockage.getNbColonnes());

        String[] columnNames = modifTableStockage.ts.getColumnNames();
        assertArrayEquals(new String[]{"Nom", "Prénom"}, columnNames);

        Object[] data1 = newTableModel.getDataVector().getElementAt(0);
        Object[] data2 = newTableModel.getDataVector().getElementAt(1);

        assertArrayEquals(new Object[]{"", ""}, data1);
        assertArrayEquals(new Object[]{"", ""}, data2);
    }

    @Test(expected = IllegalStateException.class)
    public void testSeparerColonneNoString() {
        modifTableStockage.ajouterColonne("Nom", Integer.class);
        modifTableStockage.separerColonne(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesInvalidIndex() {
        modifTableStockage.ajouterColonne("Nom", String.class);
        modifTableStockage.ajouterColonne("Prénom", String.class);
        modifTableStockage.fusionnerColonnes(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesInvalidTypes() {
        modifTableStockage.ajouterColonne("Nom", String.class);
        modifTableStockage.ajouterColonne("Prénom", Boolean.class);
        modifTableStockage.fusionnerColonnes(1);
    }

    @Test
    public void testFusionnerColonnes() {
        modifTableStockage.ajouterColonne("Nom", String.class);
        modifTableStockage.ajouterColonne("Prénom", String.class);
        modifTableStockage.fusionnerColonnes(1);

        TableModel newTableModel = modifTableStockage.ts;

        assertEquals(2, newTableModel.getColumnCount());
        assertEquals("Nom Prénom", newTableModel.getColumnName(1));

        Object[] data1 = newTableModel.getDataVector().getElementAt(0);
        Object[] data2 = newTableModel.getDataVector().getElementAt(1);

        assertArrayEquals(new Object[]{"", ""}, data1);
        assertArrayEquals(new Object[]{"", ""}, data2);
    }

    @Test
    public void testColonneMail() {
        modifTableStockage.colonneMail();

        assertEquals(5, modifTableStockage.getNbColonnes());

        String[] columnNames = modifTableStockage.ts.getColumnNames();
        assertArrayEquals(new String[]{"Nom", "Prénom", "Envoyer E-Mail a:"}, columnNames);

        Object[] data1 = modifTableStockage.ts.getDataVector().getElementAt(0);
        Object[] data2 = modifTableStockage.ts.getDataVector().getElementAt(1);
        Object[] data3 = modifTableStockage.ts.getDataVector().getElementAt(2);
        Object[] data4 = modifTableStockage.ts.getDataVector().getElementAt(3);
        Object[] data5 = modifTableStockage.ts.getDataVector().getElementAt(4);

        assertArrayEquals(new Object[]{new Boolean(false), new Boolean(false), new Boolean(true)}, data5);
    }

    @Test
    public void testSelectMail() {
        modifTableStockage.ajouterColonne("Nom", String.class);
        modifTableStockage.ajouterColonne("Prénom", String.class);
        modifTableStockage.ajouterColonne("Envoyer E-Mail a:", Boolean.class);
        modifTableStockage.ajouterLigne();
        modifTableStockage.ajouterLigne();
        modifTableStockage.setValueAt(true, 0, 2);
        modifTableStockage.setValueAt(true, 1, 2);

        TableModel newTableModel = modifTableStockage.selectMail();

        assertEquals(2, newTableModel.getRowCount());

        String[] columnNames = newTableModel.getColumnNames();
        assertArrayEquals(new String[]{"Nom", "Prénom"}, columnNames);

        Object[] data1 = newTableModel.getDataVector().getElementAt(0);
        Object[] data2 = newTableModel.getDataVector().getElementAt(1);

        assertArrayEquals(new Object[]{"", ""}, data1);
        assertArrayEquals(new Object[]{"", ""}, data2);
    }
}