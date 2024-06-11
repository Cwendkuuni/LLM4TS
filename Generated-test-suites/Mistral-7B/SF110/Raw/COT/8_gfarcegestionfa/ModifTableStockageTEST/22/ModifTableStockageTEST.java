import static org.junit.Assert.*;
import javax.swing.table.TableModel;

import org.junit.Test;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
  Object[][] data = {{"A1", "1"}, {"B1", "2"}, {"C1", "3.0"}};
  String[] columnNames = {"Col1", "Col2"};
  tableStockage = new TableStockage(data, columnNames);
  modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @Test
 public void testModifTableStockageConstructor() {
  assertEquals(tableStockage.getRowCount() + 1, modifTableStockage.getNbLignes());
  assertEquals(tableStockage.getColumnCount(), modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonne() {
  String newColumnsName = "NewCol";
  Class<?> newColumnsType = Double.class;
  TableStockage result = modifTableStockage.ajouterColonne(newColumnsName, newColumnsType);
  assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
  assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
  assertEquals(newColumnsName, result.getColumnName(modifTableStockage.getNbColonnes() - 1));
  assertEquals(newColumnsType, result.getColumnClass(modifTableStockage.getNbColonnes() - 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullName() {
  Class<?> newColumnsType = Double.class;
  modifTableStockage.ajouterColonne(null, newColumnsType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullType() {
  String newColumnsName = "NewCol";
  modifTableStockage.ajouterColonne(newColumnsName, null);
 }

 @Test
 public void testAjouterLigne() {
  TableStockage result = modifTableStockage.ajouterLigne();
  assertEquals(modifTableStockage.getNbLignes() + 1, result.getRowCount());
  for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
   if (modifTableStockage.getColumnClass(i).equals(String.class)) {
    assertEquals("", result.getValueAt(modifTableStockage.getNbLignes() - 1, i).toString());
   } else if (modifTableStockage.getColumnClass(i).equals(Double.class)) {
    assertEquals(0.0, result.getValueAt(modifTableStockage.getNbLignes() - 1, i), 0.0);
   } else {
    assertEquals(false, result.getValueAt(modifTableStockage.getNbLignes() - 1, i));
   }
  }
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumns() {
  modifTableStockage = new ModifTableStockage(new TableModel(){});
  modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
  modifTableStockage.supprimerColonne(0);
  assertEquals(modifTableStockage.getNbColonnes() - 1, tableStockage.getColumnCount());
  for (int i = 0; i < modifTableStockage.getNbColonnes() - 1; ++i) {
   assertEquals(modifTableStockage.getColumnName(i), tableStockage.getColumnName(i));
   assertEquals(modifTableStockage.getColumnClass(i), tableStockage.getColumnClass(i));
  }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
  modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes());
 }

 @Test
 public void testSupprimerLigne() {
  modifTableStockage.supprimerLigne(0);
  assertEquals(modifTableStockage.getNbLignes() - 1, tableStockage.getRowCount());
  for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
   for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
    assertEquals(modifTableStockage.getValueAt(i, j), tableStockage.getValueAt(i, j));
   }
  }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
  modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes());
 }

 @Test
 public void testSeparerColonne() {
  String expectedColName1 = "Col1";
  String expectedColName2 = "Col2";
  String expectedValue1 = "1";
  double expectedValue2 = 2.0;
  TableStockage result = modifTableStockage.separerColonne(1);
  assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
  assertEquals(expectedColName1, result.getColumnName(0));
  assertEquals(expectedColName2, result.getColumnName(1));
  for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
   String value1 = result.getValueAt(i, 0).toString();
   double value2 = ((Double)result.getValueAt(i, 1)).doubleValue();
   assertEquals(expectedValue1, value1);
   assertEquals(expectedValue2, value2, 0.0);
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneNoStringColumn() {
  modifTableStockage = new ModifTableStockage(new TableModel() {
    @Override
    public Class<?> getColumnClass(int column) {
      return Double.class;
    }
  });
  modifTableStockage.separerColonne(0);
 }

 @Test
 public void testFusionnerColonnes() {
  String expectedColName = "Col1 Col2";
  TableStockage result = modifTableStockage.fusionnerColonnes(1);
  assertEquals(modifTableStockage.getNbColonnes() - 1, result.getColumnCount());
  assertEquals(expectedColName, result.getColumnName(0));
  for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
   String value = result.getValueAt(i, 0).toString();
   String[] parts = value.split(" ");
   assertEquals(modifTableStockage.getValueAt(i, 0).toString(), parts[0]);
   assertEquals(modifTableStockage.getValueAt(i, 1).toString(), parts[1]);
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesNoBooleanColumns() {
  modifTableStockage = new ModifTableStockage(new TableModel() {
    @Override
    public Class<?> getColumnClass(int column) {
      if (column == 1) {
        return String.class;
      }
      return Double.class;
    }
  });
  modifTableStockage.fusionnerColonnes(1);
 }

 @Test
 public void testColonneMail() {
  TableStockage result = modifTableStockage.colonneMail();
  assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
  assertEquals("Envoyer E-Mail a:", result.getColumnName(result.getColumnCount() - 1));
  assertEquals(Boolean.class, result.getColumnClass(result.getColumnCount() - 1));
  for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
   assertEquals(Boolean.FALSE, result.getValueAt(i, result.getColumnCount() - 1));
  }
 }

 @Test
 public void testSelectMail() {
  int selectedColumnIndex = 1;
  TableStockage result = modifTableStockage.selectMail();
  assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
  assertEquals(modifTableStockage.getNbColonnes(), result.getColumnCount());
  for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
   for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
    if (j == selectedColumnIndex) {
     assertEquals(modifTableStockage.getValueAt(i, j), result.getValueAt(i, j));
    } else {
     assertEquals(modifTableStockage.getValueAt(i, j), result.getValueAt(i, j));
    }
   }
  }
 }

}