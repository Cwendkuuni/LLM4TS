import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Test
 public void testModifTableStockageConstruction() {
 TableStockage ts = new TableStockage(3, 3);
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
 assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonneString() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 String expectedColumnName = "NewColumn";
 Class<?> expectedColumnType = String.class;
 TableStockage result = modifTableStockage.ajouterColonne(expectedColumnName, expectedColumnType);
 assertEquals("NewColumn", result.getColumnName(result.getNbColonnes() - 1));
 }

 @Test
 public void testAjouterColonneClassWrongType() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 Class<?> expectedColumnType = Double.class;
 assertThrows(IllegalArgumentException.class, () -> {modifTableStockage.ajouterColonne("NewColumn", expectedColumnType);});
 }

 @Test
 public void testAjouterLigne() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage expectedTable = new TableStockage(3, 2);
 expectedTable.setColumnName(2, "");
 TableStockage result = modifTableStockage.ajouterLigne();
 assertEquals(expectedTable, result);
 }

 @Test
 public void testAjouterLigneNoColumns() {
 tableStockage = new TableStockage(0, 0);
 ModifTableStockage modifTableStockage = new ModifTableStockage(tableStockage);
 assertThrows(IllegalArgumentException.class, () -> {modifTableStockage.ajouterLigne();});
 }

 @Test
 public void testSupprimerColonneWrongIndex() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 assertThrows(IndexOutOfBoundsException.class, () -> {modifTableStockage.supprimerColonne(-1);});
 }

 @Test
 public void testSupprimerColonneLastColumn() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 assertThrows(IllegalArgumentException.class, () -> {modifTableStockage.supprimerColonne(tableStockage.getNbColonnes());});
 }

 @Test
 public void testSupprimerColonne() {
 tableStockage = new TableStockage(3, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 tableStockage.setColumnName(2, "Weight");
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage expectedTable = new TableStockage(2, 2);
 expectedTable.setColumnName(1, "Age");
 TableStockage result = modifTableStockage.supprimerColonne(0);
 assertEquals(expectedTable, result);
 }

 @Test
 public void testSupprimerLigneWrongIndex() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 assertThrows(IndexOutOfBoundsException.class, () -> {modifTableStockage.supprimerLigne(-1);});
 }

 @Test
 public void testSupprimerLigneLastLine() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 assertThrows(IllegalArgumentException.class, () -> {modifTableStockage.supprimerLigne(tableStockage.getNbLignes());});
 }

 @Test
 public void testSupprimerLigne() {
 tableStockage = new TableStockage(3, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 tableStockage.setValueAt("John", 0, 0);
 tableStockage.setValueAt(30, 0, 1);
 tableStockage.setValueAt("Mary", 1, 0);
 tableStockage.setValueAt(25, 1, 1);
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage expectedTable = new TableStockage(2, 2);
 expectedTable.setColumnName(0, "Name");
 expectedTable.setColumnName(1, "Age");
 expectedTable.setValueAt("Mary", 0, 0);
 expectedTable.setValueAt(25, 0, 1);
 TableStockage result = modifTableStockage.supprimerLigne(0);
 assertEquals(expectedTable, result);
 }

 @Test
 public void testSeparerColonneWrongType() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 assertThrows(IllegalArgumentException.class, () -> {modifTableStockage.separerColonne(0);});
 }

 @Test
 public void testSeparerColonne() {
 tableStockage = new TableStockage(3, 2);
 tableStockage.setColumnName(0, "Name Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage expectedTable1 = new TableStockage(3, 3);
 expectedTable1.setColumnName(0, "Name");
 expectedTable1.setColumnName(1, "Age");
 expectedTable1.setColumnName(2, " ");
 TableStockage expectedTable2 = new TableStockage(3, 3);
 expectedTable2.setColumnName(0, "Name");
 expectedTable2.setColumnName(1, " ");
 expectedTable2.setColumnName(2, "Age");
 TableStockage result1 = modifTableStockage.separerColonne(1);
 TableStockage result2 = modifTableStockage.separerColonne(2);
 assertEquals(expectedTable1, result1);
 assertEquals(expectedTable2, result2);
 }

 @Test
 public void testFusionnerColonnesWrongTypes() {
 tableStockage = new TableStockage(3, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Boolean");
 tableStockage.setColumnName(2, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 assertThrows(IllegalArgumentException.class, () -> {modifTableStockage.fusionnerColonnes(1);});
 }

 @Test
 public void testFusionnerColonnes() {
 tableStockage = new TableStockage(4, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Boolean1");
 tableStockage.setColumnName(2, "Boolean2");
 tableStockage.setColumnName(3, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage expectedTable = new TableStockage(3, 3);
 expectedTable.setColumnName(0, "NameBoolean1Boolean2");
 expectedTable.setColumnName(1, "Boolean1");
 expectedTable.setColumnName(2, "Boolean2");
 TableStockage result = modifTableStockage.fusionnerColonnes(1);
 assertEquals(expectedTable, result);
 }

 @Test
 public void testColonneMail() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage expectedTable = new TableStockage(2, 3);
 expectedTable.setColumnName(0, "Name");
 expectedTable.setColumnName(1, "Age");
 expectedTable.setColumnName(2, "Envoyer E-Mail a:");
 TableStockage result = modifTableStockage.colonneMail();
 assertEquals(expectedTable, result);
 }

 @Test
 public void testSelectMail() {
 tableStockage = new TableStockage(3, 3);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 tableStockage.setColumnName(2, "Envoyer E-Mail a:");
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage expectedTable = new TableStockage(2, 3);
 expectedTable.setColumnName(0, "Name");
 expectedTable.setColumnName(1, "Age");
 expectedTable.setValueAt("John", 0, 2);
 expectedTable.setValueAt(30, 0, 2);
 expectedTable.setValueAt("Mary", 1, 2);
 expectedTable.setValueAt(25, 1, 2);
 TableStockage result = modifTableStockage.selectMail();
 assertEquals(expectedTable, result);
 }

 @Test
 public void testSelectMailNoMailColumn() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 modifTableStockage = new ModifTableStockage(tableStockage);
 assertThrows(IllegalStateException.class, () -> {modifTableStockage.selectMail();});
 }
}