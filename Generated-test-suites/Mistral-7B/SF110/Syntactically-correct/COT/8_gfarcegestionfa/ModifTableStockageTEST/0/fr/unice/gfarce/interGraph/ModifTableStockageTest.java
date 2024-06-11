import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import javax.swing.TableModel;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableModel tableModel;

 @org.junit.Before
 public void setUp() {
 tableModel = new TableStockage(3, 4);
 modifTableStockage = new ModifTableStockage(tableModel);
 }

 @Test
 public void testModifTableStockageConstructor() {
 assertNotNull(modifTableStockage);
 }

 @Test
 public void testAjouterColonneStringClass() {
 TableStockage tableS = modifTableStockage.ajouterColonne("nouvelleColonne", String.class);
 assertNotNull(tableS);
 assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullName() {
 modifTableStockage.ajouterColonne(null, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidType() {
 assertThrows(IllegalArgumentException.class, () -> modifTableStockage.ajouterColonne("nouvelleColonne", Integer.class));
 }

 @Test
 public void testAjouterLigne() {
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertNotNull(tableS);
 assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumns() {
 modifTableStockage.setNbColonnes(0);
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 modifTableStockage.ajouterColonne("colA", String.class);
 modifTableStockage.ajouterColonne("colB", Double.class);
 TableStockage tableS = modifTableStockage.supprimerColonne(1);
 assertNotNull(tableS);
 assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTableStockage.ajouterColonne("colA", String.class);
 modifTableStockage.ajouterColonne("colB", Double.class);
 modifTableStockage.supprimerColonne(2);
 }

 @Test
 public void testSupprimerLigne() {
 modifTableStockage.ajouterColonne("colA", String.class);
 modifTableStockage.ajouterColonne("colB", Double.class);
 modifTableStockage.ajouterLigne();
 TableStockage tableS = modifTableStockage.supprimerLigne(0);
 assertNotNull(tableS);
 assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTableStockage.ajouterColonne("colA", String.class);
 modifTableStockage.ajouterColonne("colB", Double.class);
 modifTableStockage.ajouterLigne();
 modifTableStockage.ajouterLigne();
 modifTableStockage.supprimerLigne(2);
 }

 @Test
 public void testSeparerColonne() {
 modifTableStockage.ajouterColonne("colA colB", String.class);
 TableStockage tableS = modifTableStockage.separerColonne(0);
 assertNotNull(tableS);
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneEmptyColumn() {
 modifTableStockage.ajouterColonne("colA", String.class);
 TableStockage tableS = modifTableStockage.separerColonne(0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneNoSeparator() {
 modifTableStockage.ajouterColonne("colA", String.class);
 modifTableStockage.ajouterColonne("colB", String.class);
 TableStockage tableS = modifTableStockage.separerColonne(1);
 }

 @Test
 public void testFusionnerColonnes() {
 modifTableStockage.ajouterColonne("colA", String.class);
 modifTableStockage.ajouterColonne("colB", String.class);
 TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
 assertNotNull(tableS);
 assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndices() {
 modifTableStockage.ajouterColonne("colA", String.class);
 modifTableStockage.ajouterColonne("colB", String.class);
 TableStockage tableS = modifTableStockage.fusionnerColonnes(2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesDifferentTypes() {
 modifTableStockage.ajouterColonne("colA", String.class);
 modifTableStockage.ajouterColonne("colB", Integer.class);
 TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
 }

}