import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Test
 public void testModifTableStockageConstructor() {
 TableStockage ts = new TableStockage(3, 4);
 modifTableStockage = new ModifTableStockage(ts);
 assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
 assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonneStringClass() {
 String nomNouvelleColonne = "testColonne";
 Class<?> type = String.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
 assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
 assertEquals(nomNouvelleColonne, tableS.getColumnName(modifTableStockage.getNbColonnes() - 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullClass() {
 Class<?> type = null;
 try {
 modifTableStockage.ajouterColonne("testColonne", type);
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidName() {
 Class<?> type = Integer.class;
 try {
 modifTableStockage.ajouterColonne("test", type);
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testAjouterLigne() {
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertEquals(modifTableStockage.getNbLignes() + 1, tableS.getRowCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumns() {
 modifTableStockage.setNbColonnes(0);
 modifTableStockage.ajouterLigne();
 fail("An IllegalStateException should have been thrown.");
 }

 @Test
 public void testSupprimerColonne() {
 int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerColonne(indice);
 assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 int indice = modifTableStockage.getNbColonnes();
 modifTableStockage.supprimerColonne(indice);
 fail("An IndexOutOfBoundsException should have been thrown.");
 }

 @Test
 public void testSupprimerLigne() {
 int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerLigne(indice);
 assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 int indice = modifTableStockage.getNbLignes();
 modifTableStockage.supprimerLigne(indice);
 fail("An IndexOutOfBoundsException should have been thrown.");
 }

 @Test
 public void testSeparerColonne() {
 int indice = 0;
 TableStockage tableS = modifTableStockage.separerColonne(indice);
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidClass() {
 int indice = 0;
 try {
 modifTableStockage.separerColonne(indice

);
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSeparerColonneValidData() {
 int indice = 0;
 String oldColumnName = "testColonne";
 String newColumnName1 = "testColonne1";
 String newColumnName2 = "testColonne2";
 TableStockage tableS = modifTableStockage.separerColonne(indice);
 assertEquals(newColumnName1, tableS.getColumnName(modifTableStockage.getNbColonnes() - 2));
 assertEquals(newColumnName2, tableS.getColumnName(modifTableStockage.getNbColonnes() - 1));
 assertEquals(oldColumnName, tableS.getColumnName(modifTableStockage.getNbColonnes() - 3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidData() {
 int indice = 0;
 try {
 modifTableStockage.separerColonne(indice);
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFusionnerColonnes() {
 int indice = 0;
 TableStockage tableS = modifTableStockage.fusionnerColonnes(indice);
 assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndex() {
 int indice = modifTableStockage.getNbColonnes();
 modifTableStockage.fusionnerColonnes(indice);
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesDifferentTypes() {
 int indice = 0;
 modifTableStockage.setValueAt(true, 0, indice);
 modifTableStockage.setValueAt("test", 0, indice + 1);
 try {
 modifTableStockage.fusionnerColonnes(indice);
 fail("An IllegalArgumentException should have been thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testColonneMail() {
 TableStockage tableS = modifTableStockage.colonneMail();
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 assertEquals("Envoyer E-Mail a:", tableS.getColumnName(modifTableStockage.getNbColonnes() - 1));
 }

 @Test
 public void testSelectMail() {
 boolean[] result = new boolean[modifTableStockage.getNbLignes()];
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 result[i] = true;
 }
 modifTableStockage.setValueAt(result, 0, modifTableStockage.getNbColonnes() - 1);
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 assertTrue(tableS.getValueAt(i, 0) instanceof Boolean);
 assertTrue((Boolean)tableS.getValueAt(i, 0));
 for (int j = 0; j < modifTableStockage.getNbColonnes() - 2; ++j) {
 assertEquals(modifTableStockage.getValueAt(i, j), tableS.getValueAt(i, j));
 }
 }
 }

}