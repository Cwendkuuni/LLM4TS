import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTable;
 private TableStockage expectedTable;

 @Test
 public void testAjouterColonneString() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 expectedTable = modifTable.ajouterColonne("NouvelleColonne", String.class);
 assertEquals(3, expectedTable.getColumnCount());
 assertEquals("NouvelleColonne", expectedTable.getColumnName(2));
 }

 @Test
 public void testAjouterColonneDouble() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 expectedTable = modifTable.ajouterColonne("NouvelleColonne", Double.class);
 assertEquals(3, expectedTable.getColumnCount());
 assertEquals("NouvelleColonne", expectedTable.getColumnName(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNull() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 modifTable.ajouterColonne(null, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidType() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 modifTable.ajouterColonne("NouvelleColonne", Integer.class);
 }

 @Test
 public void testAjouterLigne() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 expectedTable = modifTable.ajouterLigne();
 assertEquals(4, expectedTable.getRowCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumn() {
 modifTable = new ModifTableStockage(new TableStockage(3, 0));
 modifTable.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 modifTable = new ModifTableStockage(new TableStockage(4, 3));
 expectedTable = modifTable.supprimerColonne(1);
 assertEquals(2, expectedTable.getColumnCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTable = new ModifTableStockage(new TableStockage(4, 3));
 modifTable.supprimerColonne(4);
 }

 @Test
 public void testSupprimerLigne() {
 modifTable = new ModifTableStockage(new TableStockage(4, 3));
 expectedTable = modifTable.supprimerLigne(1);
 assertEquals(3, expectedTable.getRowCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTable = new ModifTableStockage(new TableStockage(4, 3));
 modifTable.supprimerLigne(4);
 }

 @Test
 public void testSeparerColonne() {
 modifTable = new ModifTableStockage(new TableStockage(3, 1));
 modifTable.separerColonne(0);
 assertEquals(2, modifTable.getTable().getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidColumnType() {
 modifTable = new ModifTableStockage(new TableStockage(3, 1));
 modifTable.separerColonne(0);
 modifTable.separerColonne(1);
 }

 @Test
 public void testFusionnerColonnes() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 expectedTable = modifTable.fusionnerColonnes(1);
 assertEquals(2, expectedTable.getColumnCount());
 assertEquals("Colonne1 Colonne2", expectedTable.getColumnName(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidColumnType() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 modifTable.fusionnerColonnes(0);
 modifTable.fusionnerColonnes(1);
 }

 @Test
 public void testColonneMail() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 expectedTable = modifTable.colonneMail();
 assertEquals(3, expectedTable.getColumnCount());
 assertEquals("Envoyer E-Mail a:", expectedTable.getColumnName(2));
 }

 @Test
 public void testSelectMail() {
 modifTable = new ModifTableStockage(new TableStockage(3, 3));
 modifTable.colonneMail();
 assertEquals(3, modifTable.getTable().getRowCount());
 modifTable.setValueAt(true, 1, modifTable.getTable().getColumnCount() - 1);
 expectedTable = modifTable.selectMail();
 assertEquals(1, expectedTable.getRowCount());
 }

}