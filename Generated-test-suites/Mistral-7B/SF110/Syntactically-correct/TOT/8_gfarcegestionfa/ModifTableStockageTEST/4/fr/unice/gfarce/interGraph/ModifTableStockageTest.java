package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Test
 public void testModifTableStockageConstructor() {
 TableStockage expectedTableStockage = new TableStockage(0, 0);
 modifTableStockage = new ModifTableStockage(expectedTableStockage);
 TableStockage actualTableStockage = modifTableStockage.getTs();
 assertEquals("Size should be equal", 0, actualTableStockage.getRowCount());
 assertEquals("Size should be equal", 0, actualTableStockage.getColumnCount());
 }

 @Test
 public void testAjouterColonneStringClass() {
 String nomNouvelleColonne = "NewColumn";
 Class<?> type = String.class;
 TableStockage expectedTableStockage = new TableStockage(2, 3);
 expectedTableStockage.setColumnName(0, "Col0");
 expectedTableStockage.setColumnName(1, "Col1");
 expectedTableStockage.setColumnName(2, "Col2");
 modifTableStockage = new ModifTableStockage(expectedTableStockage);
 TableStockage actualTableStockage = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 String[] expectedColumnNames = {"Col0", "Col1", nomNouvelleColonne, "Col2"};
 String[] actualColumnNames = new String[actualTableStockage.getColumnCount()];
 for (int i = 0; i < actualTableStockage.getColumnCount(); ++i) {
 actualColumnNames[i] = actualTableStockage.getColumnName(i);
 }
 assertArrayEquals("Column names should be equal", expectedColumnNames, actualColumnNames);
 }

 @Test
 public void testAjouterColonneClassOtherThanString() {
 String nomNouvelleColonne = "NewColumn";
 Class<?> type = Double.class;
 TableStockage expectedTableStockage = new TableStockage(2, 3);
 expectedTableStockage.setColumnName(0, "Col0");
 expectedTableStockage.setColumnName(1, "Col1");
 expectedTableStockage.setColumnName(2, "Col2");
 modifTableStockage = new ModifTableStockage(expectedTableStockage);
 try {
 TableStockage actualTableStockage = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 fail("Should throw an exception");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testAjouterLigne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 3));
 TableStockage expectedTableStockage = new TableStockage(3, 3);
 expectedTableStockage.setColumnName(0, "Col0");
 expectedTableStockage.setColumnName(1, "Col1");
 expectedTableStockage.setColumnName(2, "Col2");
 modifTableStockage.ajouterLigne();
 String[] expectedValues = {"", "", ""};
 String[] actualValues = new String[expectedTableStockage.getRowCount()];
 for (int i = 0; i < expectedTableStockage.getRowCount(); ++i) {
 actualValues[i] = (String)expectedTableStockage.getValueAt(i, 0);
 }
 assertArrayEquals("Values should be equal", expectedValues, actualValues);
 }

 @Test
 public void testSupprimerColonneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 3));
 try {
 modifTableStockage.supprimerColonne(-1);
 fail("Should throw an exception");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSupprimerColonneValidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 3));
 TableStockage expectedTableStockage = new TableStockage(2, 2);
 expectedTableStockage.setColumnName(0, "Col0");
 expectedTableStockage.setColumnName(1, "Col1");
 modifTableStockage.ajouterColonne("NewCol", String.class);
 modifTableStockage.supprimerColonne(3);
 String[] expectedColumnNames = {"Col0", "Col1"};
 String[] actualColumnNames = new String[modifTableStockage.getTs().getColumnCount()];
 for (int i = 0; i < actualTableStockage.getColumnCount(); ++i) {
 actualColumnNames[i] = actualTableStockage.getColumnName(i);
 }
 assertArrayEquals("Column names should be equal after column deletion", expectedColumnNames, actualColumnNames);
 }

 @Test
 public void testSupprimerLigneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 3));
 try {
 modifTableStockage.supprimerLigne(-1);
 fail("Should throw an exception");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSeparerColonneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 try {
 modifTableStockage.separerColonne(-1);
 fail("Should throw an exception");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testSeparerColonneValidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 String expectedColName1 = "Col1";
 String expectedColName2 = "Col2";
 String expectedValue1 = "Value1";
 TableStockage expectedTableStockage = new TableStockage(2, 3);
 expectedTableStockage.setColumnName(0, "Col0");
 expectedTableStockage.setColumnName(1, expectedColName1);
 expectedTableStockage.setColumnName(2, expectedColName2);
 expectedTableStockage.setValueAt(expectedValue1, 0, 1);
 modifTableStockage.ajouterColonne("NewCol", String.class);
 modifTableStockage.separerColonne(1);
 String[] expectedColumnNames = {"Col0", expectedColName1, expectedColName2};
 String[] actualColumnNames = new String[modifTableStockage.getTs().getColumnCount()];
 for (int i = 0; i < actualTableStockage.getColumnCount(); ++i) {
 actualColumnNames[i] = actualTableStockage.getColumnName(i);
 }
 assertArrayEquals("Column names should be equal after column splitting", expectedColumnNames, actualColumnNames);
 }
}