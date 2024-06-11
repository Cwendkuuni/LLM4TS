import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Before
 public void setup() {
 tableStockage = new TableStockage(3, 3);
 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @Test
 public void testConstruction() {
 assertEquals(tableStockage.getRowCount() + 1, modifTableStockage.getNbLignes());
 assertEquals(tableStockage.getColumnCount(), modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonneStringClass() {
 TableStockage tableS = modifTableStockage.ajouterColonne("NouvelleColonne", String.class);
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterColonneNullClass() {
 modifTableStockage.ajouterColonne(null, Object.class);
 }

 @Test
 public void testAjouterLigne() {
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertEquals(modifTableStockage.getNbLignes() + 1, tableS.getRowCount());
 }

 @Test
 public void testSupprimerColonne() {
 modifTableStockage.supprimerColonne(0);
 assertEquals(modifTableStockage.getNbColonnes() - 1, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testSupprimerLigne() {
 modifTableStockage.supprimerLigne(0);
 assertEquals(modifTableStockage.getNbLignes() - 1, modifTableStockage.getNbLignes());
 }

 @Test
 public void testSeparerColonne() {
 modifTableStockage.separerColonne(0);
 assertEquals(modifTableStockage.getNbColonnes() + 1, modifTableStockage.getNbColonnes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidIndex() {
 modifTableStockage.separerColonne(-1);
 }

 @Test
 public void testSeparerColonneStringClass() {
 modifTableStockage.separerColonne(0);
 assertEquals(modifTableStockage.getNbColonnes() + 1, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testFusionnerColonnes() {
 modifTableStockage.fusionnerColonnes(0);
 assertEquals(modifTableStockage.getNbColonnes() - 1, modifTableStockage.getNbColonnes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndex() {
 modifTableStockage.fusionnerColonnes(-1);
 }

 @Test
 public void testColonneMail() {
 TableStockage tableS = modifTableStockage.colonneMail();
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 }

 @Test
 public void testSelectMail() {
 TableStockage selectedTable = modifTableStockage.selectMail();
 assertEquals(modifTableStockage.getNbLignes() - 1, selectedTable.getRowCount());
 }

}