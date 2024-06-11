import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private TableStockage ts;
 private ModifTableStockage mt;

 @ org.junit.Before
 public void setUp() {
 ts = new TableStockage(3, 2);
 ts.setColumnName(0, "Name");
 ts.setColumnName(1, "Age");
 ts.setValueAt("John", 0, 0);
 ts.setValueAt(25, 0, 1);
 ts.setValueAt("Jane", 1, 0);
 ts.setValueAt(30, 1, 1);
 mt = new ModifTableStockage(ts);
 }

 @Test
 public void testConstructor() {
 assertEquals(ts.getColumnCount(), mt.getNbColonnes());
 assertEquals(ts.getRowCount() + 1, mt.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
 final String nomNouvelleColonne = "NewColumn";
 final Class<?> type = String.class;
 TableStockage tableS = mt.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(mt.getNbColonnes() + 1, tableS.getColumnCount());
 assertEquals(mt.getNbLignes(), tableS.getRowCount());
 }

 @Test
 public void testAjouterLigne_WhenNoColumns() {
 mt.ajouterLigne();
 assertTrue(JOptionPane.showDialog(null, "PAS DE COLONNE", "ERREUR", 0) instanceof JOptionDialog);
 }

 @Test
 public void testAjouterLigne_WhenColumnsPresent() {
 TableStockage tableS = mt.ajouterLigne();
 assertEquals(mt.getNbLignes() + 1, tableS.getRowCount());
 assertEquals(mt.getNbColonnes(), tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSupprimerColonne_InvalidIndex() {
 mt.supprimerColonne(-1);
 }

 @Test
 public void testSupprimerColonne_ValidIndex() {
 final int indice = 0;
 TableStockage tableS = mt.supprimerColonne(indice);
 assertEquals(mt.getNbColonnes() - 1, tableS.getColumnCount());
 }

 @Test
 public void testSupprimerLigne_InvalidIndex() {
 mt.supprimerLigne(-1);
 assertTrue(JOptionPane.showDialog(null, "PAS DE LIGNE", "ERREUR", 0) instanceof JOptionDialog);
 }

 @Test
 public void testSupprimerLigne_ValidIndex() {
 final int indice = 0;
 TableStockage tableS = mt.supprimerLigne(indice);
 assertEquals(mt.getNbLignes() - 1, tableS.getRowCount());
 }

 @Test
 public void testSeparerColonne_InvalidColumnType() {
 mt.separerColonne(0);
 assertTrue(JOptionPane.showDialog(null, "non separable: PAS DE TYPE STRING", "ERREUR", 0) instanceof JOptionDialog);
 }

 @Test
 public void testSeparerColonne_ValidColumnType() {
 final int indice = 0;
 TableStockage tableS = mt.separerColonne(indice);
 assertEquals(mt.getNbColonnes() + 1, tableS.getColumnCount());
 }

 @Test
 public void testFusionnerColonnes_InvalidColumnIndex() {
 mt.fusionnerColonnes(-1);
 assertTrue(JOptionPane.showDialog(null, "Une seule colonne", "ERREUR", 0) instanceof JOptionDialog);
 }

 @Test
 public void testFusionnerColonnes_ValidColumnIndex() {
 final int indice = 0;
 TableStockage tableS = mt.fusionnerColonnes(indice);
 assertEquals(mt.getNbColonnes() - 1, tableS.getColumnCount());
 }

 @Test
 public void testColonneMail() {
 TableStockage tableS = mt.colonneMail();
 assertEquals(mt.getNbColonnes() + 1, tableS.getColumnCount());
 assertEquals(mt.getNbLignes(), tableS.getRowCount());
 }

 @Test
 public void testSelectMail() {
 final int j = 2;
 TableStockage tableS = mt.selectMail();
 assertEquals(2, tableS.getRowCount());
 }
}