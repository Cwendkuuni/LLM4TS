package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(5, modifTableStockage.getNbLignes());
        assertEquals(3, modifTableStockage.getNbColonnes());
        assertNotNull(modifTableStockage.getTs());
    }

    // Test ajouterColonne method

    @Test
    public void testAjouterColonneWithStringType() {
        TableStockage ts = new TableStockage(3, 2);
        Ts.setValueAt("John", 0, 0);
        Ts.setValueAt("Mike", 1, 0);
        Ts.setValueAt("Doe", 2, 0);
        Ts.setValueAt(25, 0, 1);
        Ts.setValueAt(30, 1, 1);
        Ts.setValueAt(45, 2, 1);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage newTable = modifTableStockage.ajouterColonne("Nom Nouvelle Colonne", String.class);

        assertEquals(3, newTable.getNbLignes());
        assertEquals(4, newTable.getNbColonnes());
        assertEquals("Nom Nouvelle Colonne", newTable.getColumnName(3));
        assertEquals("John", newTable.getValueAt(0, 3));
        assertEquals("Mike", newTable.getValueAt(1, 3));
        assertEquals("Doe", newTable.getValueAt(2, 3));
    }

    @Test
    public void testAjouterColonneWithDoubleType() {
        TableStockage ts = new TableStockage(3, 2);
        Ts.setValueAt(25.5, 0, 0);
        Ts.setValueAt(30.5, 1, 0);
        Ts.setValueAt(45.5, 2, 0);
        Ts.setValueAt(10.0, 0, 1);
        Ts.setValueAt(20.0, 1, 1);
        Ts.setValueAt(30.0, 2, 1);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage newTable = modifTableStockage.ajouterColonne("Nom Nouvelle Colonne", Double.class);

        assertEquals(3, newTable.getNbLignes());
        assertEquals(4, newTable.getNbColonnes());
        assertEquals("Nom Nouvelle Colonne", newTable.getColumnName(3));
        assertEquals(10.0, newTable.getValueAt(0, 3), 0.01);
        assertEquals(20.0, newTable.getValueAt(1, 3), 0.01);
        assertEquals(30.0, newTable.getValueAt(2, 3), 0.01);
    }

    // Test ajouterLigne method

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(2, 3);
        Ts.setValueAt("John", 0, 0);
        Ts.setValueAt("Mike", 0, 1);
        Ts.setValueAt(25, 0, 2);
        Ts.setValueAt("Doe", 1, 0);
        Ts.setValueAt("Smith", 1, 1);
        Ts.setValueAt(45, 1, 2);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage newTable = modifTableStockage.ajouterLigne();

        assertEquals(3, newTable.getNbLignes());
        assertEquals(3, newTable.getNbColonnes());
        assertNull(newTable.getValueAt(2, 0));
        assertNull(newTable.getValueAt(2, 1));
        assertEquals(0.0, newTable.getValueAt(2, 2), 0.01);
    }

    // Test supprimerColonne method

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(3, 4);
        Ts.setValueAt("John", 0, 0);
        Ts.setValueAt("Mike", 0, 1);
        Ts.setValueAt("Doe", 0, 2);
        Ts.setValueAt(25, 0, 3);
        Ts.setValueAt("John", 1, 0);
        Ts.setValueAt("Mike", 1, 1);
        Ts.setValueAt("Doe", 1, 2);
        Ts.setValueAt(30, 1, 3);
        Ts.setValueAt("John", 2, 0);
        Ts.setValueAt("Mike", 2, 1);
        Ts.setValueAt("Doe", 2, 2);
        Ts.setValueAt(45, 2, 3);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage newTable = modifTableStockage.supprimerColonne(1);

        assertEquals(3, newTable.getNbLignes());
        assertEquals(3, newTable.getNbColonnes());
        assertNull(newTable.getValueAt(0, 1));
        assertNull(newTable.getValueAt(1, 1));
        assertNull(newTable.getValueAt(2, 1));
        assertEquals("Doe", newTable.getColumnName(1));
    }

    // Test supprimerLigne method

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(4, 3);
        Ts.setValueAt("John", 0, 0);
        Ts.setValueAt("Mike", 0, 1);
        Ts.setValueAt("Doe", 0, 2);
        Ts.setValueAt(25, 0, 3);
        Ts.setValueAt("John", 1, 0);
        Ts.setValueAt("Mike", 1, 1);
        Ts.setValueAt("Doe", 1, 2);
        Ts.setValueAt(30, 1, 3);
        Ts.setValueAt("John", 2, 0);
        Ts.setValueAt("Mike", 2, 1);
        Ts.setValueAt("Doe", 2, 2);
        Ts.setValueAt(45, 2, 3);
        Ts.setValueAt("John", 3, 0);
        Ts.setValueAt("Mike", 3, 1);
        Ts.setValueAt("Doe", 3, 2);
        Ts.setValueAt(55, 3, 3);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage newTable = modifTableStockage.supprimerLigne(1);

        assertEquals(3, newTable.getNbLignes());
        assertEquals(3, newTable.getNbColonnes());
        assertEquals("John", newTable.getColumnName(0));
        assertEquals("Mike", newTable.getColumnName(1));
        assertEquals("Doe", newTable.getColumnName(2));
        assertNull(newTable.getValueAt(1, 0));
        assertNull(newTable.getValueAt(1, 1));
        assertEquals(45.0, newTable.getValueAt(1, 2), 0.01);
    }

    // Test separerColonne method

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 3);
        Ts.setValueAt("Doe 25 John", 0, 0);
        Ts.setValueAt("Doe 30 Smith", 1, 0);
        Ts.setValueAt("Doe 45 Doe", 2, 0);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage newTable = modifTableStockage.separerColonne(0);

        assertEquals(3, newTable.getNbLignes());
        assertEquals(4, newTable.getNbColonnes());
        assertEquals("Doe", newTable.getColumnName(0));
        assertEquals("25", newTable.getColumnName(1));
        assertEquals("John", newTable.getColumnName(2));
        assertEquals("Doe", newTable.getValueAt(0, 3));
        assertEquals("25", newTable.getValueAt(0, 2));
        assertEquals(25.0, newTable.getValueAt(0, 1), 0.01);
        assertEquals("Doe", newTable.getValueAt(1, 3));
        assertEquals("30", newTable.getValueAt(1, 2));
        assertEquals("Smith", newTable.getValueAt(1, 3));
        assertEquals("Doe", newTable.getValueAt(2, 3));
        assertEquals("45", newTable.getValueAt(2, 2));
        assertEquals("Doe", newTable.getValueAt(2, 3));
    }

    // Test fusionnerColonnes method

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 3);
        Ts.setValueAt(true, 0, 0);
        Ts.setValueAt(false, 0, 1);
        Ts.setValueAt(true, 0, 2);
        Ts.setValueAt(true, 1, 0);
        Ts.setValueAt(false, 1, 1);
        Ts.setValueAt(true, 1, 2);
        Ts.setValueAt(false, 2, 0);
        Ts.setValueAt(true, 2, 1);
        Ts.setValueAt(true, 2, 2);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage newTable = modifTableStockage.fusionnerColonnes(0);

        assertEquals(3, newTable.getNbLignes());
        assertEquals(3, newTable.getNbColonnes());
        assertEquals("Envoyer E-Mail a: false Envoyer E-Mail a:", newTable.getColumnName(0));
        assertEquals("Envoyer E-Mail a: true Envoyer E-Mail a:", newTable.getColumnName(1));
        assertEquals("Envoyer E-Mail a:", newTable.getColumnName(2));
        assertEquals(true, newTable.getValueAt(0, 0));
        assertEquals(false, newTable.getValueAt(0, 1));
        assertEquals(true, newTable.getValueAt(0, 2));
        assertEquals(true, newTable.getValueAt(1, 0));
        assertEquals(false, newTable.getValueAt(1, 1));
        assertEquals(true, newTable.getValueAt(1, 2));
        assertEquals(false, newTable.getValueAt(2, 0));
        assertEquals(true, newTable.getValueAt(2, 1));
        assertEquals(true, newTable.getValueAt(2, 2));
    }

    // Test colonneMail and selectMail methods

    @Test
    public void testColonneMailAndSelectMail() {
        TableStockage ts = new TableStockage(3, 2);
        Ts.setValueAt("John Doe", 0, 0);
        Ts.setValueAt("Mike Smith", 1, 0);
        Ts.setValueAt("Doe Smith", 2, 0);
        Ts.setValueAt(true, 0, 1);
        Ts.setValueAt(false, 1, 1);
        Ts.setValueAt(true, 2, 1);

        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage newTableMail = modifTableStockage.colonneMail();

        assertEquals(3, newTableMail.getNbLignes());
        assertEquals(4, newTableMail.getNbColonnes());
        assertEquals("Envoyer E-Mail a:", newTableMail.getColumnName(3));
        assertEquals(true, newTableMail.getValueAt(0, 3));
        assertEquals(false, newTableMail.getValueAt(1, 3));
        assertEquals(true, newTableMail.getValueAt(2, 3));

        TableStockage newTableSelected = modifTableStockage.selectMail();

        assertEquals(2, newTableSelected.getNbLignes());
        assertEquals(3, newTableSelected.getNbColonnes());
        assertEquals("John Doe", newTableSelected.getColumnName(0));
        assertEquals("Mike Smith", newTableSelected.getColumnName(1));
        assertEquals("Envoyer E-Mail a:", newTableSelected.getColumnName(2));
        assertEquals(true, newTableSelected.getValueAt(0, 2));
        assertEquals(false, newTableSelected.getValueAt(1, 2));
    }
}