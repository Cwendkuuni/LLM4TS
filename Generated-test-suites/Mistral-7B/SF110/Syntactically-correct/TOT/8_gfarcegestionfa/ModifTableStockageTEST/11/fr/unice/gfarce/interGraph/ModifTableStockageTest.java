import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTable;
 private TableStockage initialTable;

 @Test
 public void testModifTableStockageConstruction() {
 TableStockage ts = new TableStockage(3, 3);
 ModifTableStockage modifTable = new ModifTableStockage(ts);
 assertNotNull(modifTable);
 assertEquals(ts.getRowCount() + 1, modifTable.getNbLignes());
 assertEquals(ts.getColumnCount(), modifTable.getNbColonnes());
 }

 @Test
 public void testAjouterColonneString() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 String newColumnName = "TestColumn";
 modifTable.ajouterColonne(newColumnName, String.class);
 TableStockage newTable = modifTable.getTable();
 assertEquals(newColumnName, newTable.getColumnName(modifTable.getNbColonnes() - 1));
 }

 @Test
 public void testAjouterColonneWrongType() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 Class<?> wrongType = Integer.class;
 try {
 modifTable.ajouterColonne("TestColumn", wrongType);
 fail("AjouterColonne with wrong type should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAjouterLigne() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 TableStockage newTable = modifTable.ajouterLigne();
 assertEquals(modifTable.getNbLignes(), newTable.getRowCount());
 }

 @Test
 public void testAjouterLigneNoColumns() {
 modifTable = new ModifTableStockage(new TableStockage(1, 0));
 TableStockage newTable = modifTable.ajouterLigne();
 assertEquals(1, newTable.getRowCount());
 }

 @Test
 public void testSupprimerColonne() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 int columnIndex = 1;
 TableStockage newTable = modifTable.supprimerColonne(columnIndex);
 assertEquals(modifTable.getNbColonnes() - 1, newTable.getColumnCount());
 assertNotEquals(newTable.getColumnName(columnIndex), newTable.getColumnName(columnIndex - 1));
 }

 @Test
 public void testSupprimerColonneWrongIndex() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 int wrongIndex = 5;
 TableStockage newTable = modifTable.supprimerColonne(wrongIndex);
 assertEquals(modifTable.getNbColonnes(), newTable.getColumnCount());
 }

 @Test
 public void testSupprimerLigne() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 int rowIndex = 1;
 TableStockage newTable = modifTable.supprimerLigne(rowIndex);
 assertEquals(modifTable.getNbLignes() - 1, newTable.getRowCount());
 }

 @Test
 public void testSupprimerLigneWrongIndex() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 int wrongIndex = 5;
 TableStockage newTable = modifTable.supprimerLigne(wrongIndex);
 assertEquals(modifTable.getNbLignes(), newTable.getRowCount());
 }

 @Test
 public void testSeparerColonne() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 modifTable.ts.setValueAt("Test Value 1 Test Value 2", 0, 1);
 TableStockage newTable = modifTable.separerColonne(1);
 assertEquals(2, newTable.getColumnCount());
 assertEquals("Test Value 1", newTable.getColumnName(0));
 assertEquals("Test Value 2", newTable.getColumnName(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneNoStringType() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 modifTable.ts.setValueAt(1, 1, new Double(3.14));
 modifTable.separerColonne(1);
 }

 @Test
 public void testFusionnerColonnes() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 modifTable.ts.setValueAt(true, 0, 1);
 modifTable.ts.setValueAt(true, 0, 2);
 TableStockage newTable = modifTable.fusionnerColonnes(1);
 assertEquals(2, newTable.getColumnCount());
 assertEquals("Envoyer E-Mail a: true", newTable.getColumnName(0));
 assertEquals("", newTable.getColumnName(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesNotBooleanTypes() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 modifTable.ts.setValueAt("true", 0, 1);
 modifTable.ts.setValueAt("false", 0, 2);
 modifTable.fusionnerColonnes(1);
 }

 @Test
 public void testColonneMail() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 TableStockage newTable = modifTable.colonneMail();
 assertEquals(modifTable.getNbColonnes() + 1, newTable.getColumnCount());
 assertEquals("Envoyer E-Mail a:", newTable.getColumnName(modifTable.getNbColonnes()));
 }

 @Test
 public void testSelectMail() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 modifTable.ts.setValueAt(true, 0, modifTable.getNbColonnes() - 1);
 modifTable.ts.setValueAt(true, 1, modifTable.getNbColonnes() - 1);
 TableStockage newTable = modifTable.selectMail();
 assertEquals(2, newTable.getRowCount());
 }

 @Test
 public void testSelectMailEmptyTable() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 TableStockage newTable = modifTable.selectMail();
 assertEquals(0, newTable.getRowCount());
 }
}