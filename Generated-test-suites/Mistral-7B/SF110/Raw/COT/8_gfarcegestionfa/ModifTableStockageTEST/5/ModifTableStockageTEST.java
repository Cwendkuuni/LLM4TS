import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Before
 public void setUp() {
 tableStockage = new TableStockage(5, 5);
 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @Test
 public void testModifTableStockageConstructor() {
 assertNotNull(modifTableStockage);
 assertEquals(tableStockage.getColumnCount(), modifTableStockage.getNbColonnes());
 assertEquals(tableStockage.getRowCount() + 1, modifTableStockage.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
 TableStockage result = modifTableStockage.ajouterColonne("newColumn", String.class);
 assertEquals(modifTableStockage.getNbColonnes(), result.getColumnCount());
 assertEquals("newColumn", result.getColumnName(result.getColumnCount() - 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithEmptyName() {
 modifTableStockage.ajouterColonne("", String.class);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithInvalidType() {
 modifTableStockage.ajouterColonne("newColumn", Double.TYPE);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAjouterLigne() {
 TableStockage result = modifTableStockage.ajouterLigne();
 assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
 for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
 if (modifTableStockage.getColumnClass(i).equals(String.class)) {
 assertEquals("", result.getValueAt(result.getRowCount() - 1, i));
 } else if (modifTableStockage.getColumnClass(i).equals(Double.class)) {
 assertEquals(0.0d, result.getValueAt(result.getRowCount() - 1, i), 0.001);
 } else if (modifTableStockage.getColumnClass(i).equals(Boolean.class)) {
 assertEquals(Boolean.FALSE, result.getValueAt(result.getRowCount() - 1, i));
 }
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWhenNoColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage());
 modifTableStockage.ajouterLigne();
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testSupprimerColonne() {
 TableStockage result = modifTableStockage.supprimerColonne(0);
 assertEquals(modifTableStockage.getNbColonnes() - 1, result.getColumnCount());
 for (int i = 0; i < result.getColumnCount(); ++i) {
 if (i != 0) {
 assertEquals(modifTableStockage.getColumnName(i), result.getColumnName(i));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWhenLastColumn() {
 modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes() - 1);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSupprimerLigne() {
 TableStockage result = modifTableStockage.supprimerLigne(0);
 assertEquals(modifTableStockage.getNbLignes() - 1, result.getRowCount());
 for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbLignes() - 1; ++j) {
 assertEquals(modifTableStockage.getValueAt(j, i), result.getValueAt(j, i));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWhenLastLine() {
 modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes() - 1);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSeparerColonne() {
 modifTableStockage.separerColonne(1);
 assertEquals(modifTableStockage.getNbColonnes() + 1, modifTableStockage.getTableStockage().getColumnCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testSeparerColonneWhenNoStringColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 5, new Object[][]{{"1", "2"}, {"3", "4"}, {"5", "6"}, {"7", "8"}, {"9", "10"}}));
 modifTableStockage.separerColonne(0);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testFusionnerColonnes() {
 modifTableStockage.fusionnerColonnes(1);
 assertEquals(modifTableStockage.getNbColonnes() - 1, modifTableStockage.getTableStockage().getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWhenInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 5, new Object[][]{{"1", "2"}, {"3", "4"}, {"5", "6"}, {"7", "8"}, {"9", "10"}}));
 modifTableStockage.fusionnerColonnes(2);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testColonneMail() {
 TableStockage result = modifTableStockage.colonneMail();
 assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
 assertEquals("Envoyer E-Mail a:", result.getColumnName(result.getColumnCount() - 1));
 for (int i = 0; i < result.getRowCount(); ++i) {
 assertEquals(Boolean.TRUE, result.getValueAt(i, result.getColumnCount() - 1));
 }
 }

 @Test
 public void testSelectMail() {
 TableStockage result = modifTableStockage.selectMail();
 assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
 assertEquals(modifTableStockage.getNbColonnes(), result.getColumnCount());
 for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
 assertEquals(modifTableStockage.getColumnName(i), result.getColumnName(i));
 }
 }

}