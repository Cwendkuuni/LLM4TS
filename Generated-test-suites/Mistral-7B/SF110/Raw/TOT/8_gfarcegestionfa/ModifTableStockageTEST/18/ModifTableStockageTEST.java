package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstruction() {
        TableStockage ts = new TableStockage(3, 4);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(3, modifTableStockage.getNbLignes());
        assertEquals(4, modifTableStockage.getNbColonnes());
    }

    @Test
    public void testAjouterColonne_String_Class() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Gender");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        String nomNouvelleColonne = "City";
        Class<?> type = String.class;

        TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);

        assertEquals(4, result.getColumnCount());
        assertEquals(nomNouvelleColonne, result.getColumnName(3));

        TableStockage expectedTable = new TableStockage(4, 4);
        expectedTable.setColumnName(0, "Name");
        expectedTable.setColumnName(1, "Age");
        expectedTable.setColumnName(2, "Gender");
        expectedTable.setColumnName(3, nomNouvelleColonne);

        for (int i = 0; i < 3; i++) {
            expectedTable.setValueAt(null, i, 3);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(null, result.getValueAt(i, j));
                assertEquals(null, expectedTable.getValueAt(i, j));
            }
        }

        assertEquals(expectedTable, result);
    }

    @Test
    public void testAjouterColonne_String_NotStringClass() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Gender");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        String nomNouvelleColonne = "City";
        Class<?> type = Double.class;

        try {
            TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAjouterLigne() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Gender");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        TableStockage result = modifTableStockage.ajouterLigne();

        assertEquals(4, result.getRowCount());
        assertEquals(3, result.getColumnCount());

        TableStockage expectedTable = new TableStockage(4, 3);
        expectedTable.setColumnName(0, "Name");
        expectedTable.setColumnName(1, "Age");
        expectedTable.setColumnName(2, "Gender");

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (i < 3) {
                    assertEquals(null, result.getValueAt(i, j));
                    assertEquals(null, expectedTable.getValueAt(i, j));
                } else {
                    assertEquals("", result.getValueAt(i, j));
                    assertEquals(null, expectedTable.getValueAt(i, j));
                }
            }
        }

        assertEquals(expectedTable, result);
    }

    @Test
    public void testAjouterLigne_EmptyTable() {
        TableStockage ts = new TableStockage(0, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        try {
            TableStockage result = modifTableStockage.ajouterLigne();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testSupprimerColonne_ValidIndex() {
        TableStockage ts = new TableStockage(4, 4);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Gender");
        ts.setColumnName(3, "City");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int indice = 3;
        TableStockage result = modifTableStockage.supprimerColonne(indice);

        assertEquals(3, result.getColumnCount());

        TableStockage expectedTable = new TableStockage(4, 3);
        expectedTable.setColumnName(0, "Name");
        expectedTable.setColumnName(1, "Age");
        expectedTable.setColumnName(2, "Gender");

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(null, result.getValueAt(i, j));
                assertEquals(null, expectedTable.getValueAt(i, j));
            }
        }

        assertEquals(expectedTable, result);
    }

    @Test
    public void testSupprimerColonne_InvalidIndex() {
        TableStockage ts = new TableStockage(4, 4);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Gender");
        ts.setColumnName(3, "City");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int indice = 5;
        try {
            TableStockage result = modifTableStockage.supprimerColonne(indice);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSupprimerLigne_ValidIndex() {
        TableStockage ts = new TableStockage(4, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Gender");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int indice = 1;
        TableStockage result = modifTableStockage.supprimerLigne(indice);

        assertEquals(3, result.getRowCount());

        TableStockage expectedTable = new TableStockage(3, 3);
        expectedTable.setColumnName(0, "Name");
        expectedTable.setColumnName(1, "Gender");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(null, result.getValueAt(i, j));
                assertEquals(null, expectedTable.getValueAt(i, j));
            }
        }

        assertEquals(expectedTable, result);
    }

    @Test
    public void testSupprimerLigne_InvalidIndex() {
        TableStockage ts = new TableStockage(4, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Gender");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int indice = 5;
        try {
            TableStockage result = modifTableStockage.supprimerLigne(indice);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSeparerColonne_ValidIndex() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setColumnName(0, "Name Age Gender");
        ts.setColumnValueAt("John Doe 30 Male", 0, 0);
        ts.setColumnValueAt("Jane Doe 25 Female", 1, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int indice = 0;
        TableStockage result = modifTableStockage.separerColonne(indice);

        assertEquals(2, result.getColumnCount());
        assertEquals("Name", result.getColumnName(0));
        assertEquals("Age Gender", result.getColumnName(1));

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(null, result.getValueAt(i, j));
            }
        }

        TableStockage expectedNameTable = new TableStockage(2, 1);
        expectedNameTable.setColumnName(0, "Name");
        expectedNameTable.setValueAt("John Doe", 0, 0);
        expectedNameTable.setValueAt("Jane Doe", 1, 0);

        TableStockage expectedAgeGenderTable = new TableStockage(2, 2);
        expectedAgeGenderTable.setColumnName(0, "Age");
        expectedAgeGenderTable.setColumnName(1, "Gender");
        expectedAgeGenderTable.setValueAt("30", 0, 0);
        expectedAgeGenderTable.setValueAt("Female", 0, 1);
        expectedAgeGenderTable.setValueAt("25", 1, 0);
        expectedAgeGenderTable.setValueAt("Male", 1, 1);

        TableStockage expectedTables[] = {expectedNameTable, expectedAgeGenderTable};

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(null, result.getValueAt(i, j));
                assertEquals(null, expectedTables[i].getValueAt(i, j));
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                assertEquals(null, result.getValueAt(i, j), 0.001);
                assertEquals(expectedTables[i].getValueAt(i, j), expectedTables[i].getValueAt(i, j), 0.001);
            }
        }

        assertEquals(expectedTables, new TableStockage[] {result, result});
    }

    @Test
    public void testSeparerColonne_InvalidIndex() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setColumnName(0, "Name Age Gender");
        ts.setColumnValueAt("John Doe 30 Male", 0, 0);
        ts.setColumnValueAt("Jane Doe 25 Female", 1, 0);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int indice = 3;
        try {
            TableStockage result = modifTableStockage.separerColonne(indice);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFusionnerColonnes_ValidIndices() {
        TableStockage ts = new TableStockage(4, 4);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Gender");
        ts.setColumnName(3, "City");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int indice1 = 2;
        int indice2 = 3;

        TableStockage result = modifTableStockage.fusionnerColonnes(indice1);
        TableStockage expectedTable = new TableStockage(4, 3);
        expectedTable.setColumnName(0, "Name");
        expectedTable.setColumnName(1, "Age");
        expectedTable.setColumnName(2, "Gender City");

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(null, result.getValueAt(i, j));
                assertEquals(null, expectedTable.getValueAt(i, j));
            }
        }

        String name1 = "John";
        String name2 = "Jane";
        int age1 = 30;
        int age2 = 25;
        String gender1 = "Male";
        String gender2 = "Female";
        String city1 = "Paris";
        String city2 = "London";

        ts.setValueAt(name1, 0, indice1);
        ts.setValueAt(age1, 0, indice2);
        ts.setValueAt(gender1, 0, indice1 + 1);
        ts.setValueAt(city1, 0, indice2 + 1);

        ts.setValueAt(name2, 1, indice1);
        ts.setValueAt(age2, 1, indice2);
        ts.setValueAt(gender2, 1, indice1 + 1);
        ts.setValueAt(city2, 1, indice2 + 1);

        modifTableStockage = new ModifTableStockage(ts);

        String expectedValue = name1.concat(" ").concat(gender1).concat(" ").concat(city1);

        assertEquals(expectedValue, (String) result.getValueAt(0, indice1));
        assertEquals(expectedValue, (String) expectedTable.getValueAt(0, indice1));

        assertEquals(expectedValue, (String) result.getValueAt(1, indice1));
        assertEquals(expectedValue, (String) expectedTable.getValueAt(1, indice1));
    }

    @Test
    public void testFusionnerColonnes_InvalidIndices() {
        TableStockage ts = new TableStockage(4, 4);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Gender");
        ts.setColumnName(3, "City");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);

        int indice1 = 2;
        int indice2 = 1;

        try {
            TableStockage result = modifTableStockage.fusionnerColonnes(indice1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}