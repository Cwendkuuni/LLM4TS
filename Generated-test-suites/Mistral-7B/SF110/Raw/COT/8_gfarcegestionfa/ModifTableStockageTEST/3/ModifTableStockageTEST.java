import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.TableStockage;
import fr.unice.gfarce.interGraph.ModifTableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Before
 public void setUp() {
 tableStockage = new TableStockage(5, 5);
 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @Test
 public void testConstructor() {
 assertEquals(tableStockage.getRowCount() + 1, modifTableStockage.getNbLignes());
 assertEquals(tableStockage.getColumnCount(), modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonne() {
 final String nomNouvelleColonne = "TestColumn";
 final Class<?> type = String.class;
 TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() + 1);
 final TableStockage resultTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(expectedTable, resultTable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithInvalidType() {
 final String nomNouvelleColonne = "TestColumn";
 final Class<?> type = Integer.class;
 modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 }

 @Test
 public void testAjouterLigne() {
 final TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes() + 1, modifTableStockage.getNbColonnes());
 final TableStockage resultTable;
 if (modifTableStockage.getNbColonnes() > 0) {
 resultTable = modifTableStockage.ajouterLigne();
 } else {
 JOptionPane.showMessageDialog(null, "PAS DE COLONNE", "ERREUR", 0);
 resultTable = modifTableStockage.getTableStockage();
 }
 assertEquals(expectedTable, resultTable);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWithNoColumns() {
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 final int indice = 0;
 final TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() - 1);
 final TableStockage resultTable = modifTableStockage.supprimerColonne(indice);
 assertEquals(expectedTable, resultTable);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithInvalidIndex() {
 modifTableStockage.supprimerColonne(-1);
 }

 @Test
 public void testSupprimerLigne() {
 final int indice = 0;
 final TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes() - 1, modifTableStockage.getNbColonnes());
 final TableStockage resultTable = modifTableStockage.supprimerLigne(indice);
 assertEquals(expectedTable, resultTable);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithInvalidIndex() {
 modifTableStockage.supprimerLigne(-1);
 }

 @Test
 public void testSeparerColonne() {
 final int indice = 0;
 final ModifTableStockage expectedModifTableStockage = new ModifTableStockage(new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() + 2));
 final TableStockage resultTable = modifTableStockage.separerColonne(indice);
 assertEquals(expectedModifTableStockage.getTableStockage(), resultTable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithInvalidIndexType() {
 modifTableStockage.separerColonne(-1);
 }

 @Test
 public void testFusionnerColonnes() {
 final int indice = 0;
 final ModifTableStockage expectedModifTableStockage = new ModifTableStockage(new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() - 1));
 final TableStockage resultTable = modifTableStockage.fusionnerColonnes(indice);
 assertEquals(expectedModifTableStockage.getTableStockage(), resultTable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithInvalidIndicesType() {
 modifTableStockage.fusionnerColonnes(-1);
 }

 @Test
 public void testColonneMail() {
 final TableStockage expectedTable = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() + 1);
 final TableStockage resultTable = modifTableStockage.colonneMail();
 assertEquals(expectedTable, resultTable);
 }

 @Test
 public void testSelectMail() {
 final int j = modifTableStockage.getTableStockage().getColumnCount() - 1;
 final int newNbLignes = 0;
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 final Boolean bool = (Boolean)modifTableStockage.getTableStockage().getValueAt(i, j);
 if (bool) {
 ++newNbLignes;
 }
 }
 final TableStockage expectedTable = new TableStockage(newNbLignes + 1, modifTableStockage.getNbColonnes());
 final TableStockage resultTable = modifTableStockage.selectMail();
 assertEquals(expectedTable, resultTable);
 }
}