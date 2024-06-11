import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Before
 public void setUp() {
 tableStockage = new TableStockage(3, 3);
 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @Test
 public void testConstructor() {
 assertEquals(3, modifTableStockage.getNbLignes());
 assertEquals(3, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonne() {
 String newColName = "NewColumn";
 Class<?> newColType = String.class;
 ModifTableStockage newModifTableStockage = modifTableStockage.ajouterColonne(newColName, newColType);
 assertEquals(newColName, newModifTableStockage.getColumnName(modifTableStockage.getNbColonnes() - 1));
 }

 @Test
 public void testAjouterLigne() {
 TableStockage newTableStockage = modifTableStockage.ajouterLigne();
 assertEquals(newTableStockage.getRowCount(), modifTableStockage.getNbLignes() - 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWithNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage());
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 int columnIndexToRemove = 0;
 ModifTableStockage newModifTableStockage = modifTableStockage.supprimerColonne(columnIndexToRemove);
 assertEquals(newModifTableStockage.getNbColonnes(), modifTableStockage.getNbColonnes() - 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithNoColumnsToRemove() {
 modifTableStockage = new ModifTableStockage(new TableStockage(1, 1));
 modifTableStockage.supprimerColonne(1);
 }

 @Test
 public void testSupprimerLigne() {
 int lineIndexToRemove = 0;
 ModifTableStockage newModifTableStockage = modifTableStockage.supprimerLigne(lineIndexToRemove);
 assertEquals(newModifTableStockage.getNbLignes(), modifTableStockage.getNbLignes() - 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithNoLinesToRemove() {
 modifTableStockage = new ModifTableStockage(new TableStockage(1, 1));
 modifTableStockage.supprimerLigne(1);
 }

 @Test
 public void testSeparerColonne() {
 int columnIndexToSeparate = 0;
 ModifTableStockage newModifTableStockage = modifTableStockage.separerColonne(columnIndexToSeparate);
 assertEquals(newModifTableStockage.getNbColonnes(), modifTableStockage.getNbColonnes() + 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testSeparerColonneWithNoStringTypeColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(1, new String[]{"BooleanColumn"}));
 modifTableStockage.separerColonne(0);
 }

 @Test
 public void testFusionnerColonnes() {
 int columnIndexToMerge = 0;
 int nextColumnIndexToMerge = 1;
 ModifTableStockage newModifTableStockage = modifTableStockage.fusionnerColonnes(columnIndexToMerge);
 assertEquals(newModifTableStockage.getNbColonnes(), modifTableStockage.getNbColonnes() - 1);
 assertEquals(newModifTableStockage.getColumnName(columnIndexToMerge), newModifTableStockage.getColumnName(columnIndexToMerge).concat(" " + newModifTableStockage.getColumnName(nextColumnIndexToMerge)));
 }

 @Test
 public void testColonneMail() {
 ModifTableStockage newModifTableStockage = modifTableStockage.colonneMail();
 assertEquals(newModifTableStockage.getNbColonnes(), modifTableStockage.getNbColonnes() + 1);
 assertEquals(newModifTableStockage.getColumnName(newModifTableStockage.getNbColonnes() - 1), "Envoyer E-Mail a:");
 }

 @Test
 public void testSelectMail() {
 int columnIndexToSelect = 0;
 TableStockage newTableStockage = modifTableStockage.selectMail();
 assertEquals(newTableStockage.getColumnCount(), modifTableStockage.getNbColonnes() - 1);
 }

 @Test
 public void testSelectMailWithNoEmailsToSelect() {
 int columnIndexToSelect = 0;
 modifTableStockage = new ModifTableStockage(new TableStockage(1, new String[]{"BooleanColumn"}));
 TableStockage newTableStockage = modifTableStockage.selectMail();
 assertEquals(newTableStockage.getRowCount(), 0);
 }
}