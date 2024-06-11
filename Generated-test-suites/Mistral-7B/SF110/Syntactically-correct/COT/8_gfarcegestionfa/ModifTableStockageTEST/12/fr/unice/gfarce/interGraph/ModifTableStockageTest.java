import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.awt.Component;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ModifTableStockageTest {

 private TableStockage ts;
 private ModifTableStockage modifTableStockage;

 @Before
 public void setUp() {
 Object[][] data = {{1, "test1"}, {2, "test2"}, {3, "test3"}};
 ts = new TableStockage(data);
 modifTableStockage = new ModifTableStockage(ts);
 }

 @Test
 public void testModifTableStockageConstructor() {
 assertNotNull(modifTableStockage);
 assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
 assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonneStringClass() {
 TableStockage tableS = modifTableStockage.ajouterColonne("testColumn", String.class);
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 assertNotNull(tableS.getColumnName(tableS.getColumnCount() - 1));
 }

 @Test
 public void testAjouterColonneClassWithNullTable() {
 Assert.assertThrows(NullPointerException.class, () -> modifTableStockage.ajouterColonne(null, String.class));
 }

 @Test
 public void testAjouterColonneClassWithInvalidType() {
 Assert.assertThrows(IllegalArgumentException.class, () -> modifTableStockage.ajouterColonne("testColumn", int[].class));
 }

 @Test
 public void testAjouterLigne() {
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertEquals(modifTableStockage.getNbLignes() + 1, tableS.getRowCount());
 }

 @Test
 public void testAjouterLigneWithNoColumns() {
 JOptionPane.showMessageDialog(new JFrame(), "PAS DE COLONNE", "ERREUR", JOptionPane.ERROR_MESSAGE);
 modifTableStockage = new ModifTableStockage(new TableStockage(new Object[0][0]));
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertSame(modifTableStockage.getTableStockage(), tableS);
 }

 @Test
 public void testSupprimerColonne() {
 int index = 0;
 TableStockage tableS = modifTableStockage.supprimerColonne(index);
 assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
 assertNull(tableS.getColumnName(tableS.getColumnCount()));
 }

 @Test
 public void testSupprimerColonneWithNegativeIndex() {
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.supprimerColonne(-1));
 }

 @Test
 public void testSupprimerColonneWithLastColumn() {
 int index = modifTableStockage.getNbColonnes() - 1;
 TableStockage tableS = modifTableStockage.supprimerColonne(index);
 assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
 assertNull(tableS.getColumnName(tableS.getColumnCount()));
 }

 @Test
 public void testSupprimerLigne() {
 int index = 0;
 TableStockage tableS = modifTableStockage.supprimerLigne(index);
 assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
 }

 @Test
 public void testSupprimerLigneWithNegativeIndex() {
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.supprimerLigne(-1));
 }

 @Test
 public void testSupprimerLigneWithLastLine() {
 int index = modifTableStockage.getNbLignes() - 1;
 TableStockage tableS = modifTableStockage.supprimerLigne(index);
 assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
 }

 @Test
 public void testSeparerColonne() {
 int index = 0;
 TableStockage tableS = modifTableStockage.separerColonne(index);
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 assertNotNull(tableS.getColumnName(tableS.getColumnCount() - 1));
 }

 @Test
 public void testSeparerColonneWithInvalidColumnType() {
 int index = 0;
 TableStockage tableS = modifTableStockage.separerColonne(index);
 assertThrows(IllegalArgumentException.class, () -> modifTableStockage.separerColonne(index));
 }

 @Test
 public void testSeparerColonneWithEmptySeparator() {
 int index = 0;
 TableStockage tableS = modifTableStockage.separerColonne(index);
 assertThrows(IllegalArgumentException.class, () -> modifTableStockage.separerColonne(index));
 }

 @Test
 public void testFusionnerColonnes() {
 int index = 0;
 TableStockage tableS = modifTableStockage.fusionnerColonnes(index);
 assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
 }

 @Test
 public void testFusionnerColonnesWithInvalidTypes() {
 int index = 0;
 Assert.assertThrows(IllegalArgumentException.class, () -> modifTableStockage.fusionnerColonnes(index));
 }

 @Test
 public void testColonneMail() {
 TableStockage tableS = modifTableStockage.colonneMail();
 assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
 assertNotNull(tableS.getColumnName(tableS.getColumnCount() - 1));
 }

 @Test
 public void testSelectMail() {
 int index = 0;
 int j;
 for (j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 if (modifTableStockage.getColumnName(j).equals("Envoyer E-Mail a:")) {
 break;
 }
 }
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(1, tableS.getRowCount());
 for (int column = 0; column < modifTableStockage.getNbColonnes(); ++column) {
 Object expected = null;
 if (column == j) {
 expected = new Boolean(true);
 }
 Object actual = tableS.getValueAt(0, column);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testSelectMailWithNoBooleanColumn() {
 TableStockage tableS = new TableStockage(new Object[0][0]);
 ModifTableStockage modifTableStockage = new ModifTableStockage(tableS);
 assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.selectMail());
 }
}