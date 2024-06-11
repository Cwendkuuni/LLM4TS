import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableModel tableModel;

 @Before
 public void setUp() {
 tableModel = new Object[][]{{"col1", "1"}, {"col2", "2"}, {"col3", "3"}};
 modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
 }

 @After
 public void tearDown() {
 // TODO: Add tear down logic here
 }

 @Test
 public void testConstructor() {
 assertEquals(3, modifTableStockage.getNbColonnes());
 assertEquals(3, modifTableStockage.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
 String newColumnName = "newColumn";
 Class<?> newColumnType = String.class;
 TableStockage newTable = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
 assertEquals(4, newTable.getColumnCount());
 assertEquals(newColumnName, newTable.getColumnName(3));
 }

 @Test
 public void testAjouterLigne() {
 TableStockage newTable = modifTableStockage.ajouterLigne();
 assertEquals(4, newTable.getRowCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testSupprimerColonneNegativeIndex() {
 modifTableStockage.supprimerColonne(-1);
 }

 @Test(expected = IllegalStateException.class)
 public void testSupprimerColonneIndexOutOfBounds() {
 modifTableStockage.supprimerColonne(3);
 }

 @Test
 public void testSupprimerColonne() {
 int numColToRemove = 1;
 TableStockage newTable = modifTableStockage.supprimerColonne(numColToRemove);
 assertEquals(3, newTable.getColumnCount());
 assertNotEquals("col1", newTable.getColumnName(numColToRemove));
 }

 @Test(expected = IllegalStateException.class)
 public void testSupprimerLigneNegativeIndex() {
 modifTableStockage.supprimerLigne(-1);
 }

 @Test(expected = IllegalStateException.class)
 public void testSupprimerLigneIndexOutOfBounds() {
 modifTableStockage.supprimerLigne(3);
 }

 @Test
 public void testSupprimerLigne() {
 int numRowToRemove = 1;
 TableStockage newTable = modifTableStockage.supprimerLigne(numRowToRemove);
 assertEquals(3, newTable.getRowCount());
 assertNotEquals("1", newTable.getValueAt(numRowToRemove - 1, 0).toString());
 }

 @Test
 public void testSeparerColonne() {
 String columnToSplit = "col1 col2";
 int indice = modifTableStockage.getColumnIndex("col1");
 TableStockage newTable = modifTableStockage.separerColonne(indice);
 assertEquals(5, newTable.getColumnCount());
 assertEquals("col1", newTable.getColumnName(0));
 assertEquals("col2", newTable.getColumnName(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithInvalidType() {
 String columnToSplit = "col1 123";
 int indice = modifTableStockage.getColumnIndex("col1");
 TableStockage newTable = modifTableStockage.separerColonne(indice);
 fail();
 }

 @Test
 public void testFusionnerColonnes() {
 int firstColIndex = 0;
 int secondColIndex = 1;
 TableStockage newTable = modifTableStockage.fusionnerColonnes(firstColIndex);
 assertEquals(3, newTable.getColumnCount());
 assertEquals("col1 col2", newTable.getColumnName(firstColIndex));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithInvalidTypes() {
 int firstColIndex = 0;
 int secondColIndex = 2;
 TableStockage newTable = modifTableStockage.fusionnerColonnes(firstColIndex);
 fail();
 }
}