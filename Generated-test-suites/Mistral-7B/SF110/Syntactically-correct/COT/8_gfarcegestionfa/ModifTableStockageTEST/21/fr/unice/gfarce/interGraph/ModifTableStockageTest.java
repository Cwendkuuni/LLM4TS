import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {

 private TableStockage ts;
 private ModifTableStockage modifTableStockage;

 @Test
 public void testModifTableStockageConstructor() {
 ts = new TableStockage(5, 5);
 modifTableStockage = new ModifTableStockage(ts);
 assertEquals(5, modifTableStockage.getNbColonnes());
 assertEquals(6, modifTableStockage.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
 String nomNouvelleColonne = "New Column";
 Class<?> type = String.class;
 TableStockage expectedTable = new TableStockage(6, 6);
 modifTableStockage = new ModifTableStockage(ts);
 TableStockage actualTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(expectedTable, actualTable);
 assertEquals(nomNouvelleColonne, actualTable.getColumnName(5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithNullType() {
 String nomNouvelleColonne = "New Column";
 Class<?> type = null;
 modifTableStockage = new ModifTableStockage(ts);
 modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 fail("Exception expected: IllegalArgumentException");
 }

 @Test
 public void testAjouterLigne() {
 TableStockage expectedTable = new TableStockage(6, 5);
 modifTableStockage = new ModifTableStockage(ts);
 TableStockage actualTable = modifTableStockage.ajouterLigne();
 assertEquals(expectedTable, actualTable);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWithoutColumns() {
 modifTableStockage = new ModifTableStockage(ts);
 modifTableStockage.ajouterLigne();
 fail("Exception expected: IllegalStateException");
 }

 @Test
 public void testSupprimerColonne() {
 int indice = 0;
 TableStockage expectedTable = new TableStockage(6, 4);
 modifTableStockage = new ModifTableStockage(ts);
 TableStockage actualTable = modifTableStockage.supprimerColonne(indice);
 assertEquals(expectedTable, actualTable);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithInvalidIndex() {
 int indice = 5;
 modifTableStockage = new ModifTableStockage(ts);
 modifTableStockage.supprimerColonne(indice);
 fail("Exception expected: IndexOutOfBoundsException");
 }

 @Test
 public void testSupprimerLigne() {
 int indice = 0;
 TableStockage expectedTable = new TableStockage(5, 5);
 modifTableStockage = new ModifTableStockage(ts);
 TableStockage actualTable = modifTableStockage.supprimerLigne(indice);
 assertEquals(expectedTable, actualTable);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithInvalidIndex() {
 int indice = 5;
 modifTableStockage = new ModifTableStockage(ts);
 modifTableStockage.supprimerLigne(indice);
 fail("Exception expected: IndexOutOfBoundsException");
 }

 @Test
 public void testSeparerColonne() {
 int indice = 0;
 modifTableStockage = new ModifTableStockage(ts);
 TableStockage actualTable = modifTableStockage.separerColonne(indice);
 fail("This method is not implemented and cannot be tested.");
 }

 @Test
 public void testFusionnerColonnes() {
 int indice = 0;
 modifTableStockage = new ModifTableStockage(ts);
 modifTableStockage.fusionnerColonnes(indice);
 fail("This method is not implemented and cannot be tested.");
 }

 @Test
 public void testColonneMail() {
 TableStockage expectedTable = new TableStockage(6, 6);
 modifTableStockage = new ModifTableStockage(ts);
 TableStockage actualTable = modifTableStockage.colonneMail();
 assertEquals(expectedTable, actualTable);
 assertEquals("Envoyer E-Mail a:", actualTable.getColumnName(5));
 }

 @Test
 public void testSelectMail() {
 int j;
 for (j = 0; j < modifTableStockage.getNbColonnes() && !modifTableStockage.getColumnName(j).equals("Envoyer E-Mail a:"); ++j) {}
 TableStockage expectedTable = new TableStockage(1, 6);
 TableStockage actualTable = modifTableStockage.selectMail();
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testSelectMailWithNoMails() {
 modifTableStockage = new ModifTableStockage(ts);
 modifTableStockage.colonneMail();
 TableStockage actualTable = modifTableStockage.selectMail();
 assertEquals(new TableStockage(0, 6), actualTable);
 }

}