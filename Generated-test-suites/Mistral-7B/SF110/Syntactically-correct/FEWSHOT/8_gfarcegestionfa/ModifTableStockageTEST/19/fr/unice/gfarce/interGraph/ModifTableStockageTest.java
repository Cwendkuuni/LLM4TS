package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ModifTableStockageTest {

    private static final Object[][] TEST_DATA_AJOUTER_COLONNE = {
            {new Object[]{"ts", new String[]{"name1", "name2"}, Integer.TYPE, 2}, new Object[]{"name", "type", 2}, new Object[]{"newTableStockage", new String[]{"name1", "name2", "name"}, Integer.TYPE, Integer.TYPE}},
            {new Object[]{"ts", new String[]{"name1", "name2"}, Integer.TYPE, 3}, new Object[]{"name3", Integer.TYPE, 3}, new Object[]{"newTableStockage", new String[]{"name1", "name2", "name3"}, Integer.TYPE, Integer.TYPE, Integer.TYPE}}};

    @Test
    public void testAjouterColonne() {
        for (Object[] testData : TEST_DATA_AJOUTER_COLONNE) {
            TableStockage ts = (TableStockage) testData[0];
            String nomNouvelleColonne = (String) testData[1][0];
            Class<?> type = (Class<?>) testData[1][1];
            int expectedNbColonnes = (Integer) testData[1][2];
            ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
            TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
            assertEquals(expectedNbColonnes, result.getColumnCount());
            for (int i = 0; i < expectedNbColonnes; ++i) {
                assertEquals(ts.getColumnName(i), result.getColumnName(i));
            }
        }
    }

    private static final Object[][] TEST_DATA_AJOUTER_LIGNE = {
            {new Object[]{"ts", 2}, new Object[]{"newTableStockage", new Object[]{"", "", "", "", "", "", "", ""}, Integer.TYPE, Integer.TYPE, String.class, String.class, Double.TYPE, Double.TYPE, Boolean.TYPE}},
            {new Object[]{"ts", 3}, new Object[]{"newTableStockage", new Object[]{"", "", "", "", "", "", "", "", ""}, Integer.TYPE, Integer.TYPE, String.class, String.class, Double.TYPE, Double.TYPE, Boolean.TYPE, "value"},
                    new Object[]{"newTableStockage2", new Object[]{"", "", "", "", "", "", "", "", "", "value2"}, Integer.TYPE, Integer.TYPE, String.class, String.class, Double.TYPE, Double.TYPE, Boolean.TYPE}}};

    @Test
    public void testAjouterLigne() {
        for (Object[] testData : TEST_DATA_AJOUTER_LIGNE) {
            TableStockage ts = (TableStockage) testData[0];
            int expectedNbLignes = (Integer) testData[1][0];
            TableStockage result1 = new ModifTableStockage(ts).ajouterLigne();
            assertEquals(expectedNbLignes, result1.getRowCount());

            TableStockage result2 = new ModifTableStockage(result1).ajouterLigne();
            if (testData[1].length > 2) {
                assertEquals(expectedNbLignes + 1, result2.getRowCount());
                for (int i = 0; i < expectedNbLignes; ++i) {
                    for (int j = 0; j < result1.getColumnCount(); ++j) {
                        assertEquals(result1.getValueAt(i, j), result2.getValueAt(i, j));
                    }
                }
                for (int j = 0; j < result2.getColumnCount(); ++j) {
                    if (j < result1.getColumnCount()) {
                        assertEquals(result1.getValueAt(expectedNbLignes - 1, j), result2.getValueAt(expectedNbLignes, j));
                    } else {
                        assertEquals(testData[1][2][j], result2.getValueAt(expectedNbLignes, j));
                    }
                }
            }
        }
    }

    private static final Object[][] TEST_DATA_SUPPRIMER_COLONNE = {
            {new Object[]{"ts", new String[]{"name1", "name2"}, Integer.TYPE, 2}, new Object[]{"newTableStockage", new String[]{"name1"}, Integer.TYPE}},
            {new Object[]{"ts", new String[]{"name1", "name2"}, Integer.TYPE, 3}, new Object[]{"newTableStockage", new String[]{"name1", "name2"}, Integer.TYPE}}};

    @Test
    public void testSupprimerColonne() {
        for (Object[] testData : TEST_DATA_SUPPRIMER_COLONNE) {
            TableStockage ts = (TableStockage) testData[0];
            int index = (Integer) testData[0].length;
            TableStockage result = new ModifTableStockage(ts).supprimerColonne(index);
            assertEquals(ts.getColumnCount() - 1, result.getColumnCount());
            for (int i = 0; i < result.getColumnCount(); ++i) {
                assertEquals(ts.getColumnName(i), result.getColumnName(i));
            }
        }
    }

    private static final Object[][] TEST_DATA_SUPPRIMER_LIGNE = {
            {new Object[]{"ts", new String[]{"name1", "name2"}, Integer.TYPE, 2}, new Object[]{"newTableStockage", new Object[]{"value1", "value2"}, Integer.TYPE, Integer.TYPE}},
            {new Object[]{"ts", new String[]{"name1", "name2"}, Integer.TYPE, 3}, new Object[]{"newTableStockage", new Object[]{"value1", "value2", "value3"}, Integer.TYPE, Integer.TYPE, Integer.TYPE}}};

    @Test
    public void testSupprimerLigne() {
        for (Object[] testData : TEST_DATA_SUPPRIMER_LIGNE) {
            TableStockage ts = (TableStockage) testData[0];
            int index = (Integer) testData[0].length;
            TableStockage result = new ModifTableStockage(ts).supprimerLigne(index);
            assertEquals(ts.getRowCount() - 1, result.getRowCount());
            for (int i = 0; i < result.getRowCount(); ++i) {
                for (int j = 0; j < result.getColumnCount(); ++j) {
                    assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
                }
            }
        }
    }

    private static final Object[][] TEST_DATA_SEPARER_COLONNE = {
            {new Object[]{"ts", new String[]{"name1 name2"}, Integer.TYPE, 1}, new Object[]{"newTableStockage1", new String[]{"name1", "name2"}, Integer.TYPE, Integer.TYPE}, new Object[]{"newTableStockage2", new String[]{"name2"}, Integer.TYPE}},
            {new Object[]{"ts", new String[]{"name1 name2"}, Integer.TYPE, 1}, new Object[]{null, new Object[]{"newTableStockage1", new String[]{"name1"}, Integer.TYPE}, new Object[]{"newTableStockage2", new String[]{"name2"}, Integer.TYPE}},
                    new Object[]{"newTableStockage1", new Object[]{"value1", ""}, Integer.TYPE, String.class}, new Object[]{"newTableStockage2", new Object[]{"value2"}, Integer.TYPE, Double.class}}};

    @Test
    public void testSeparerColonne() {
        for (Object[] testData : TEST_DATA_SEPARER_COLONNE) {
            TableStockage ts = (TableStockage) testData[0];
            int index = (Integer) testData[0].length;
            TableStockage result1 = new ModifTableStockage(ts).separerColonne(index);
            assertEquals(ts.getColumnCount() + 1, result1.getColumnCount());
            assertEquals(ts.getRowCount(), result1.getRowCount());
            for (int i = 0; i < result1.getColumnCount(); ++i) {
                if (i < index) {
                    assertEquals(ts.getColumnName(i), result1.getColumnName(i));
                } else {
                    assertEquals(testData[1][i], result1.getColumnName(i));
                }
            }
            TableStockage result2 = null;
            if (testData.length > 2) {
                result2 = (TableStockage) testData[1][1];
                assertSame(result1, result2);
                for (int i = 0; i < result1.getRowCount(); ++i) {
                    Object value1 = result1.getValueAt(i, index);
                    Object value2 = testData[1][2][i];
                    if (value1 instanceof String) {
                        assertEquals(((String) value1).trim(), (String) value2);
                    } else {
                        assertEquals(value1, value2);
                    }
                }
            }
        }
    }

    private static final Object[][] TEST_DATA_FUSIONNER_COLONNES = {
            {new Object[]{"ts", new String[]{"col1", "col2", "col3"}, Boolean.TYPE, 2}, new Object[]{"newTableStockage1", new String[]{"col1", "col2"}, Boolean.TYPE, Boolean.TYPE},
                    new Object[]{"newTableStockage2", new String[]{"col1 col2"}, Boolean.TYPE}, new Object[]{"newTableStockage3", new String[]{"col1", "col2"}, Boolean.TYPE}},
            {new Object[]{"ts", new String[]{"col1", "col2", "col3"}, Boolean.TYPE, 2}, null, new Object[]{"newTableStockage", new String[]{"col1 col2"}, Boolean.TYPE}}};

    @Test
    public void testFusionnerColonnes() {
        for (Object[] testData : TEST_DATA_FUSIONNER_COLONNES) {
            TableStockage ts = (TableStockage) testData[0];
            int index = (Integer) testData[0].length;
            TableStockage result = null;
            if (testData.length > 1) {
                result = new ModifTableStockage(ts).fusionnerColonnes(index);
            }
            assertEquals(ts.getColumnCount() - 1, result.getColumnCount());
            for (int i = 0; i < result.getColumnCount(); ++i) {
                if (i < index) {
                    assertEquals(ts.getColumnName(i), result.getColumnName(i));
                }
            }
            if (testData.length > 2) {
                String expectedName = ts.getColumnName(index).concat(" ").concat(ts.getColumnName(index + 1));
                assertEquals(expectedName, result.getColumnName(index));
                for (int i = 0; i < result.getRowCount(); ++i) {
                    String value1 = ts.getValueAt(i, index).toString();
                    String value2 = ts.getValueAt(i, index + 1).toString();
                    assertEquals(value1.concat(" ").concat(value2), result.getValueAt(i, index).toString());
                }
            }
        }
    }

    @Test
    public void testColonneMail() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Email");
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.colonneMail();
        assertEquals(3, result.getColumnCount());
        assertEquals("Name", result.getColumnName(0));
        assertEquals("Age", result.getColumnName(1));
        assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
        assertTrue(result.getValueAt(0, 2).equals(Boolean.TRUE));
        assertTrue(result.getValueAt(1, 2).equals(Boolean.TRUE));
        assertTrue(result.getValueAt(2, 2).equals(Boolean.TRUE));
    }

    @Test
    public void testSelectMail() {
        TableStockage ts = new TableStockage(3, 3);
        ts.setColumnName(0, "Name");
        ts.setColumnName(1, "Age");
        ts.setColumnName(2, "Email");
        ts.setValueAt("John Doe", 0, 2);
        ts.setValueAt("Jane Doe", 1, 2);
        ts.setValueAt("john.doe@example.com", 0, 3);
        ts.setValueAt("jane.doe@example.com", 1, 3);
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableStockage result = modifTableStockage.selectMail();
        assertEquals(2, result.getRowCount());
        assertEquals("Name", result.getColumnName(0));
        assertEquals("Age", result.getColumnName(1));
        assertEquals("Email", result.getColumnName(2));
        assertEquals("John Doe", result.getValueAt(0, 0));
        assertEquals(30, result.getValueAt(0, 1).intValue());
        assertEquals("john.doe@example.com", result.getValueAt(0, 2));
        assertEquals("Jane Doe", result.getValueAt(1, 0));
        assertEquals(25, result.getValueAt(1, 1).intValue());
        assertEquals("jane.doe@example.com", result.getValueAt(1, 2));
    }
}