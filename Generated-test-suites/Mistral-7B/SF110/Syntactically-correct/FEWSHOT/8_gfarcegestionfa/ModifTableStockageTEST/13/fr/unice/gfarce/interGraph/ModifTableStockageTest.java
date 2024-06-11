package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableStockage ts = new TableStockage(5, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(5, modifTableStockage.getNbLignes());
        assertEquals(3, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterColonne("NewColumn", String.class);
        assertEquals(4, result.getNbColonnes());
        assertEquals("NewColumn", result.getColumnName(3));
    }

    @Test
    public void testAjouterColonneWithInvalidType() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertThrows(IllegalArgumentException.class, () -> modifTableStockage.ajouterColonne("NewColumn", Integer.class));
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(4, result.getNbLignes());
        assertNull(result.getValueAt(3, 0));
        assertNull(result.getValueAt(3, 1));
    }

    @Test
    public void testAjouterLigneWithEmptyTable() {
        TableStockage ts = new TableStockage(0, 2);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.ajouterLigne();
        assertEquals(1, result.getNbLignes());
        assertNull(result.getValueAt(0, 0));
        assertNull(result.getValueAt(0, 1));
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Email");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerColonne(2);
        assertEquals(2, result.getNbColonnes());
        assertNull(result.getColumnName(2));
    }

    @Test
    public void testSupprimerColonneWithInvalidIndex() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Email");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.supprimerColonne(3));
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Email");
        for (int i = 0; i < 5; i++) {
            ts.setValueAt(i, 0, "Test " + i);
            ts.setValueAt(i, 1, i * 2);
            ts.setValueAt(i, 2, "test@example.com");
        }
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.supprimerLigne(2);
        assertEquals(4, result.getNbLignes());
        assertNull(result.getValueAt(2, 0));
        assertNull(result.getValueAt(2, 1));
        assertNull(result.getValueAt(2, 2));
    }

    @Test
    public void testSupprimerLigneWithInvalidIndex() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Email");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.supprimerLigne(5));
    }

    @Test
    public void testSeparerColonne() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt(0, 0, "Name Age");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(0);
        assertEquals(2, result.getNbColonnes());
        assertEquals("Name", result.getColumnName(0));
        assertEquals("Age", result.getColumnName(1));
    }

    @Test
    public void testSeparerColonneWithEmptyColumn() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt(0, 0, "");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.separerColonne(0);
        assertEquals(1, result.getNbColonnes());
        assertNull(result.getColumnName(0));
    }

    @Test
    public void testSeparerColonneWithInvalidColumnIndex() {
        TableStockage ts = new TableStockage(3, 1);
        ts.setValueAt(0, 0, "Name Age");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.separerColonne(1));
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "FirstName");
        ts.setValueAt(0, 0, "John Doe");
        ts.setValueAt(0, 1, "John");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.fusionnerColonnes(1);
        assertEquals(2, result.getNbColonnes());
        assertEquals("Name", result.getColumnName(0));
        assertEquals("FirstName", result.getColumnName(1));
        assertEquals("John Doe", result.getValueAt(0, 0));
        assertEquals("John Doe", result.getValueAt(0, 1));
    }

    @Test
    public void testFusionnerColonnesWithInvalidColumnIndex() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "FirstName");
        ts.setValueAt(0, 0, "John Doe");
        ts.setValueAt(0, 1, "John");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.fusionnerColonnes(2));
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 2);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Email");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.colonneMail();
        assertEquals(3, result.getNbColonnes());
        assertEquals("Name", result.getColumnName(0));
        assertEquals("Envoyer E-Mail a:", result.getColumnName(1));
        assertTrue(Arrays.stream(result.getColumnClasses()).anyMatch(Boolean.class::equals));
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(5, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Email");
        ts.setColumnName(2, "Age");
        for (int i = 0; i < 5; i++) {
            ts.setValueAt(i, 0, "Test " + i);
            ts.setValueAt(i, 1, "test" + i + "@example.com");
            ts.setValueAt(i, 2, i * 2);
        }
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        assertEquals(3, result.getNbLignes());
        assertFalse(result.getValueAt(0, 1).toString().isEmpty());
        assertFalse(result.getValueAt(1, 1).toString().isEmpty());
        assertFalse(result.getValueAt(2, 1).toString().isEmpty());
    }
}