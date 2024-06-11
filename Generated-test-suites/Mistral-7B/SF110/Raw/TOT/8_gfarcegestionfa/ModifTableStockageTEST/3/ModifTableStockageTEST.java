package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.DefaultTableModel;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage originalTable;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 Object[][] data = {{"A1", "1"}, {"B1", "2.5"}, {"C1", false}, {"D1", "3"}};
 originalTable = new TableStockage(4, 2);
 DefaultTableModel model = (DefaultTableModel) originalTable.getModel();
 model.setDataVector(data, getColumnNames());
 modifTableStockage = new ModifTableStockage(originalTable);
 }

 @Test
 public void testAjouterColonneStringClass() {
 String nomNouvelleColonne = "newColumn";
 Class<?> type = String.class;
 TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals("newColumn", result.getColumnName(result.getColumnCount() - 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneIllegalArgumentException() {
 String nomNouvelleColonne = null;
 Class<?> type = Integer.class;
 TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 fail("Should throw an IllegalArgumentException when null argument is passed");
 }

 @Test
 public void testAjouterColonneClassDifferentFromString() {
 String nomNouvelleColonne = "newColumn";
 Class<?> type = Integer.class;
 TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertNotSame(originalTable, result);
 assert NotEquals(result.getColumnCount(), originalTable.getColumnCount());
 }

 @Test
 public void testAjouterLigne() {
 TableStockage result = modifTableStockage.ajouterLigne();
 assertEquals(5, result.getRowCount());
 for (int i = 0; i < 5; ++i) {
 for (int j = 0; j < 2; ++j) {
 if (j != result.getColumnCount() - 1) {
 assertEquals(originalTable.getValueAt(i, j), result.getValueAt(i, j));
 }
 }
 }
 }

 @Test
 public void testSupprimerColonneValidIndex() {
 int index = 0;
 TableStockage result = modifTableStockage.supprimerColonne(index);
 assertEquals(2, result.getColumnCount());
 assertNotEquals(originalTable.getColumnName(index), result.getColumnName(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 int index = originalTable.getColumnCount();
 TableStockage result = modifTableStockage.supprimerColonne(index);
 fail("Should throw an IndexOutOfBoundsException when index out of bounds");
 }

 @Test
 public void testSupprimerLigneValidIndex() {
 int index = 0;
 TableStockage result = modifTableStockage.supprimerLigne(index);
 assertEquals(3, result.getRowCount());
 for (int j = 0; j < result.getColumnCount(); ++j) {
 for (int i = 0; i < result.getRowCount() - 1; ++i) {
 assertEquals(originalTable.getValueAt(i, j), result.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 int index = originalTable.getRowCount();
 TableStockage result = modifTableStockage.supprimerLigne(index);
 fail("Should throw an IndexOutOfBoundsException when index out of bounds");
 }

 @Test
 public void testSeparerColonneWithSpace() {
 int index = 0;
 TableStockage result = modifTableStockage.separerColonne(index);
 assertEquals(3, result.getColumnCount());
 assertEquals("Column1", result.getColumnName(0));
 assertEquals("Column2", result.getColumnName(1));
 assertEquals(originalTable.getValueAt(0, index), result.getValueAt(0, 0));
 assertEquals(originalTable.getValueAt(0,index+1), result.getValueAt(0, 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneNoSpace() {
 int index = 0;
 TableStockage result = modifTableStockage.separerColonne(index);
 fail("Should throw an IllegalArgumentException when column name has no space");
 }

 @Test
 public void testFusionnerColonnesValidIndexes() {
 int index1 = 1;
 int index2 = 2;
 TableStockage result = modifTableStockage.fusionnerColonnes(index1);
 assertEquals(3, result.getColumnCount());
 assertEquals("Column1 Column2", result.getColumnName(index1));
 for (int i = 0; i < result.getRowCount(); ++i) {
 for (int j = 0; j < result.getColumnCount(); ++j) {
 if (j != index1) {
 assertEquals(originalTable.getValueAt(i, j), result.getValueAt(i, j));
 }
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndexes() {
 int index1 = 2;
 int index2 = 1;
 TableStockage result = modifTableStockage.fusionnerColonnes(index1);
 fail("Should throw an IllegalArgumentException when invalid indexes passed");
 }

 @Test
 public void testColonneMail() {
 TableStockage result = modifTableStockage.colonneMail();
 assertEquals(4, result.getColumnCount());
 assertEquals("Envoyer E-Mail a:", result.getColumnName(3));
 }

 @Test
 public void testSelectMail() {
 int index = originalTable.getColumnCount() - 1;
 TableStockage result = modifTableStockage.selectMail();
 assertEquals(3, result.getRowCount());
 for (int i = 0; i < result.getRowCount(); ++i) {
 for (int j = 0; j < result.getColumnCount(); ++j) {
 if (j != result.getColumnCount() - 1) {
 assertEquals(originalTable.getValueAt(i, j), result.getValueAt(i, j));
 }
 }
 }
 }
}