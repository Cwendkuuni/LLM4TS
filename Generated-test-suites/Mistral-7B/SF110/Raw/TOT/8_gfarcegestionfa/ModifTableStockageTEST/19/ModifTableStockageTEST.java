package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javax.swing.table.DefaultTableModel;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage originalTable;

 @Test
 public void testModifTableStockageConstruction() {
 TableStockage ts = new TableStockage(5, 5);
 ModifTableStockage modifTable = new ModifTableStockage(ts);
 assertNotNull(modifTable);
 assertEquals(ts.getRowCount() + 1, modifTable.getNbLignes());
 assertEquals(ts.getColumnCount(), modifTable.getNbColonnes());
 }

 @Test
 public void testAjouterColonneWithStringType() {
 String newColumnName = "newColumn";
 Class<?> newColumnType = String.class;
 modifTableStockage = new ModifTableStockage(originalTable);
 TableStockage newTable = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
 assertEquals(getColumnCount(newTable), getColumnCount(originalTable) + 1);
 assertEquals(newColumnName, newTable.getColumnName(getColumnCount(newTable) - 1));
 }

 @Test
 public void testAjouterColonneWithDoubleType() {
 String newColumnName = "newColumn";
 Class<?> newColumnType = Double.class;
 modifTableStockage = new ModifTableStockage(originalTable);
 TableStockage newTable = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
 assertEquals(getColumnCount(newTable), getColumnCount(originalTable) + 1);
 assertEquals(newColumnName, newTable.getColumnName(getColumnCount(newTable) - 1));
 for (int i = 0; i < getRowCount(newTable) - 1; ++i) {
 for (int j = 0; j < getColumnCount(newTable) - 1; ++j) {
 assertEquals(originalTable.getValueAt(i, j), newTable.getValueAt(i, j));
 }
 }
 }

 @Test
 public void testAjouterColonneWithBooleanType() {
 String newColumnName = "newColumn";
 Class<?> newColumnType = Boolean.class;
 modifTableStockage = new ModifTableStockage(originalTable);
 TableStockage newTable = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
 assertEquals(getColumnCount(newTable), getColumnCount(originalTable) + 1);
 assertEquals(newColumnName, newTable.getColumnName(getColumnCount(newTable) - 1));
 for (int i = 0; i < getRowCount(newTable) - 1; ++i) {
 for (int j = 0; j < getColumnCount(newTable) - 1; ++j) {
 assertEquals(originalTable.getValueAt(i, j), newTable.getValueAt(i, j));
 }
 newTable.setValueAt(true, i, getColumnCount(newTable) - 1);
 assertFalse(originalTable.getValue(i, getColumnCount(originalTable) - 1) instanceof Boolean);
 assertTrue(newTable.getValue(i, getColumnCount(newTable) - 1) instanceof Boolean);
 }
 }

 @Test
 public void testAjouterLigne() {
 modifTableStockage = new ModifTableStockage(originalTable);
 TableStockage newTable = modifTableStockage.ajouterLigne();
 assertEquals(getRowCount(newTable), getRowCount(originalTable) + 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterLigneWithoutColumns() {
 modifTableStockage = new ModifTableStockage(originalTable);
 modifTableStockage.setNbColonnes(0);
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 int columnToRemove = 0;
 modifTableStockage = new ModifTableStockage(originalTable);
 TableStockage newTable = modifTableStockage.supprimerColonne(columnToRemove);
 assertEquals(getColumnCount(newTable), getColumnCount(originalTable) - 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneOutOfBounds() {
 int columnToRemove = getColumnCount(originalTable);
 modifTableStockage = new ModifTableStockage(originalTable);
 modifTableStockage.supprimerColonne(columnToRemove);
 }

 @Test
 public void testSupprimerLigne() {
 int lineToRemove = 0;
 modifTableStockage = new ModifTableStockage(originalTable);
 TableStockage newTable = modifTableStockage.supprimerLigne(lineToRemove);
 assertEquals(getRowCount(newTable), getRowCount(originalTable) - 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneOutOfBounds() {
 int lineToRemove = getRowCount(originalTable);
 modifTableStockage = new ModifTableStockage(originalTable);
 modifTableStockage.supprimerLigne(lineToRemove);
 }

 @Test
 public void testSeparerColonne() {
 int columnToSeparate = 0;
 modifTableStockage = new ModifTableStockage(originalTable);
 TableStockage newTable = modifTableStockage.separerColonne(columnToSeparate);
 assertEquals(getColumnCount(newTable), getColumnCount(originalTable) + 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithNoSpace() {
 int columnToSeparate = 0;
 modifTableStockage = new ModifTableStockage(originalTable);
 modifTableStockage.separerColonne(columnToSeparate);
 }

 @Test
 public void testSeparerColonneWithMultipleSpaces() {
 int columnToSeparate = 0;
 modifTableStockage = new ModifTableStockage(originalTable);
 String columnName = "ColumnNameWithMultipleSpaces";
 TableStockage newTable = modifTableStockage.separerColonne(columnToSeparate);
 String[] columnNames = newTable.getColumnNames();
 assertEquals(columnNames.length, 3);
 assertEquals(columnNames[0], "ColumnName");
 assertEquals(columnNames[1], "With");
 assertEquals(columnNames[2], "MultipleSpaces");
 }

 @Test
 public void testFusionnerColonnes() {
 int column1Index = 0;
 int column2Index = 1;
 modifTableStockage = new ModifTableStockage(originalTable);
 TableStockage newTable = modifTableStockage.fusionnerColonnes(column2Index);
 assertEquals(getColumnCount(newTable), getColumnCount(originalTable) - 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithWrongTypes() {
 int column1Index = 0;
 int column2Index = 1;
 modifTableStockage = new ModifTableStockage(originalTable);
 modifTableStockage.fusionnerColonnes(column2Index);
 }

 @Test
 public void testColonneMail() {
 modifTableStockage = new ModifTableStockage(originalTable);
 TableStockage newTable = modifTableStockage.colonneMail();
 assertEquals(getColumnCount(newTable), getColumnCount(originalTable) + 1);
 assertEquals("Envoyer E-Mail a:", newTable.getColumnName(getColumnCount(newTable) - 1));
 }

 @Test
 public void testSelectMail() {
 modifTableStockage = new ModifTableStockage(originalTable);
 int columnIndex = getColumnIndex("Envoyer E-Mail a:", originalTable);
 TableStockage newTable = modifTableStockage.selectMail();
 assertEquals(getRowCount(newTable), getRowCount(originalTable) - 1);
 for (int i = 0; i < getRowCount(newTable); ++i) {
 assertEquals(originalTable.getValueAt(i, columnIndex), newTable.getValueAt(i, 0));
 }
 }

 private int getColumnCount(TableStockage table) {
 return table.getColumnCount();
 }

 private int getRowCount(TableStockage table) {
 return table.getRowCount();
 }

 private int getColumnIndex(String columnName, TableStockage table) {
 for (int i = 0; i < table.getColumnCount(); ++i) {
 if (table.getColumnName(i).equals(columnName)) {
 return i;
 }
 }
 return -1;
 }
}