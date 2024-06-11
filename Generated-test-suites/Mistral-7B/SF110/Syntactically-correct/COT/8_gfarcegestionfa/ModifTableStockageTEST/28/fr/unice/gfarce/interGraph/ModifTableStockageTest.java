import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private TableStockage ts;
 private ModifTableStockage modifTableStockage;

 @Before
 public void setUp() {
 ts = new TableStockage(3, 2);
 ts.setColumnName(0, "Name");
 ts.setColumnName(1, "Age");
 ts.setValueAt("Alice", 0, 0);
 ts.setValueAt(30, 0, 1);
 ts.setValueAt("Bob", 1, 0);
 ts.setValueAt(25, 1, 1);
 modifTableStockage = new ModifTableStockage(ts);
 }

 @Test
 public void testConstructor() {
 assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
 assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
 TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() + 1);
 expectedTable.setColumnName(modifTableStockage.getNbColonnes() - 1, "New Column");
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 expectedTable.setValueAt(modifTableStockage.ts.getValueAt(i, j), i, j);
 }
 }
 TableStockage resultTable = modifTableStockage.ajouterColonne("New Column", Double.class);
 assertEquals(expectedTable, resultTable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithInvalidType() {
 modifTableStockage.ajouterColonne("New Column", Integer.TYPE);
 }

 @Test
 public void testAjouterLigne() {
 TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes() + 1, modifTableStockage.getNbColonnes());
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 expectedTable.setValueAt(modifTableStockage.ts.getValueAt(i, j), i, j);
 }
 }
 TableStockage resultTable = modifTableStockage.ajouterLigne();
 assertEquals(expectedTable, resultTable);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWhenNoColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 0));
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 int initialNbColonnes = modifTableStockage.getNbColonnes();
 TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes(), initialNbColonnes - 1);
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int j = 0; j < initialNbColonnes - 1; ++j) {
 expectedTable.setValueAt(modifTableStockage.ts.getValueAt(i, j), i, j);
 }
 }
 TableStockage resultTable = modifTableStockage.supprimerColonne(0);
 assertEquals(expectedTable, resultTable);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithInvalidIndex() {
 modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes());
 }

 @Test
 public void testSupprimerLigne() {
 int initialNbLignes = modifTableStockage.getNbLignes();
 TableStockage expectedTable = new TableStockage(initialNbLignes - 1, modifTableStockage.getNbColonnes());
 for (int i = 0; i < initialNbLignes - 1; ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 expectedTable.setValueAt(modifTableStockage.ts.getValueAt(i, j), i, j);
 }
 }
 TableStockage resultTable = modifTableStockage.supprimerLigne(0);
 assertEquals(expectedTable, resultTable);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithInvalidIndex() {
 modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes());
 }

 @Test
 public void testSeparerColonne() {
 // TODO: Implement test for separerColonne method
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithInvalidIndex() {
 modifTableStockage.separerColonne(-1);
 }

 @Test
 public void testFusionnerColonnes() {
 // TODO: Implement test for fusionnerColonnes method
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithInvalidIndices() {
 modifTableStockage.fusionnerColonnes(-1);
 }

 @Test
 public void testColonneMail() {
 TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() + 1);
 expectedTable.setColumnName(modifTableStockage.getNbColonnes() - 1, "Envoyer E-Mail a:");
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 expectedTable.setValueAt(modifTableStockage.ts.getValueAt(i, j), i, j);
 }
 }
 TableStockage resultTable = modifTableStockage.colonneMail();
 assertEquals(expectedTable, resultTable);
 }

 @Test
 public void testSelectMail() {
 // TODO: Implement test for selectMail method
 }
}