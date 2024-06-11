import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Before
 public void setUp() {
 tableStockage = new TableStockage(3, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 tableStockage.setValueAt("John", 0, 0);
 tableStockage.setValueAt(30, 0, 1);
 tableStockage.setValueAt("Mike", 1, 0);
 tableStockage.setValueAt(25, 1, 1);
 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @Test
 public void testConstructor() {
 assertEquals(tableStockage.getColumnCount(), modifTableStockage.getNbColonnes());
 assertEquals(tableStockage.getRowCount() + 1, modifTableStockage.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
 final String nomNouvelleColonne = "Sex";
 Class<?> type = String.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithNullType() {
 final String nomNouvelleColonne = "Sex";
 Class<?> type = null;
 try {
 modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 }
 catch (IllegalArgumentException e) {
 assertEquals("Type cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithNegativeNumberOfColonnes() {
 final String nomNouvelleColonne = "Sex";
 Class<?> type = String.class;
 try {
 modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 modifTableStockage.setNbColonnes(-1);
 }
 catch (IllegalArgumentException e) {
 assertEquals("The number of columns cannot be negative", e.getMessage());
 }
 }

 @Test
 public void testAjouterLigne() {
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWithNoColumn() {
 modifTableStockage.setNbColonnes(0);
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 final int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerColonne(indice);
 assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithLastColumn() {
 final int indice = modifTableStockage.getNbColonnes() - 1;
 try {
 modifTableStockage.supprimerColonne(indice);
 }
 catch (IndexOutOfBoundsException e) {
 assertEquals("The index is out of bounds", e.getMessage());
 }
 }

 @Test
 public void testSupprimerLigne() {
 final int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerLigne(indice);
 assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithLastLine() {
 final int indice = modifTableStockage.getNbLignes() - 1;
 try {
 modifTableStockage.supprimerLigne(indice);
 }
 catch (IndexOutOfBoundsException e) {
 assertEquals("The index is out of bounds", e.getMessage());
 }
 }

 @Test
 public void testSeparerColonne() {
 final int indice = 0;
 TableStockage tableS = modifTableStockage.separerColonne(indice);
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithNoStringColumn() {
 final int indice = 0;
 try {
 modifTableStockage.separerColonne(indice);
 modifTableStockage.setColumnClass(indice, Integer.class);
 }
 catch (IllegalArgumentException e) {
 assertEquals("The column is not a String", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithNonExistingColumn() {
 final int indice = modifTableStockage.getNbColonnes();
 try {
 modifTableStockage.separerColonne(indice);
 }
 catch (IllegalArgumentException e) {
 assertEquals("The index is out of bounds", e.getMessage());
 }
 }

 @Test
 public void testFusionnerColonnes() {
 final int indice = 0;
 TableStockage tableS = modifTableStockage.fusionnerColonnes(indice);
 assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithNonExistingColumns() {
 final int indice = modifTableStockage.getNbColonnes();
 try {
 modifTableStockage.fusionnerColonnes(indice);
 }
 catch (IllegalArgumentException e) {
 assertEquals("The indices are out of bounds", e.getMessage());
 }
 }

 @Test
 public void testColonneMail() {
 TableStockage tableS = modifTableStockage.colonneMail();
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 }

 @Test
 public void testSelectMail() {
 final int j = modifTableStockage.getNbColonnes() - 1;
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
 }

}