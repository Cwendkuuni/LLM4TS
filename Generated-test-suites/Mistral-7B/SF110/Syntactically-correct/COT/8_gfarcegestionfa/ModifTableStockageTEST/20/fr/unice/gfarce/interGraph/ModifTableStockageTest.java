import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private TableStockage ts;
 private ModifTableStockage modifTableStockage;

 @Before
 public void setUp() {
 ts = new TableStockage(2, 2);
 ts.setColumnName(0, "ColumnName0");
 ts.setColumnName(1, "ColumnName1");
 ts.setValueAt("Value0", 0, 0);
 ts.setValueAt("Value1", 0, 1);
 ts.setValueAt("Value2", 1, 0);
 ts.setValueAt("Value3", 1, 1);
 modifTableStockage = new ModifTableStockage(ts);
 }

 @Test
 public void testConstructeur() {
 assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
 assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
 TableStockage result = modifTableStockage.ajouterColonne("newColumnName", Integer.class);
 assertEquals(modifTableStockage.getNbColonnes(), result.getColumnCount());
 assertFalse(result.getColumnName(modifTableStockage.getNbColonnes() - 1).equals("newColumnName"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithInvalidClass() {
 modifTableStockage.ajouterColonne("newColumnName", "StringClass.class");
 }

 @Test
 public void testAjouterLigne() {
 TableStockage result = modifTableStockage.ajouterLigne();
 assertEquals(modifTableStockage.getNbLignes(), result.getRowCount() + 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWhenNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 TableStockage result = modifTableStockage.supprimerColonne(0);
 assertEquals(modifTableStockage.getNbColonnes() - 1, result.getColumnCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 modifTableStockage.supprimerColonne(2);
 }

 @Test
 public void testSupprimerLigne() {
 TableStockage result = modifTableStockage.supprimerLigne(0);
 assertEquals(modifTableStockage.getNbLignes() - 1, result.getRowCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 modifTableStockage.supprimerLigne(2);
 }

 @Test
 public void testSeparerColonne() {
 TableStockage result = modifTableStockage.separerColonne(1);
 assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 modifTableStockage.separerColonne(2);
 }

 @Test
 public void testFusionnerColonnes() {
 TableStockage result = modifTableStockage.fusionnerColonnes(1);
 assertEquals(modifTableStockage.getNbColonnes() - 1, result.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithInvalidIndices() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 modifTableStockage.fusionnerColonnes(2);
 }

 @Test
 public void testColonneMail() {
 TableStockage result = modifTableStockage.colonneMail();
 assertEquals(modifTableStockage.getNbColonnes(), result.getColumnCount());
 assertTrue(result.getColumnName(modifTableStockage.getNbColonnes() - 1).equals("Envoyer E-Mail a:"));
 }

 @Test
 public void testSelectMail() {
 TableStockage result = modifTableStockage.selectMail();
 assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
 }

 @Test
 public void testSelectMailWhenNoBooleanColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 modifTableStockage.selectMail();
 }
}