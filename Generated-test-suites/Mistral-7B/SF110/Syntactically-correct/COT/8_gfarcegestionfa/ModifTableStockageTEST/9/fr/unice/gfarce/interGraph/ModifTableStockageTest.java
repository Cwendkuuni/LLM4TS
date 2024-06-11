import static org.junit.Assert.*;
import javax.swing.TableModel;

import org.junit.Test;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage expectedTable;

 @Test
 public void testModifTableStockageConstructor() {
 TableStockage ts = new TableStockage(5, 4);
 modifTableStockage = new ModifTableStockage(ts);
 assertEquals(5, modifTableStockage.getNbLignes());
 assertEquals(4, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 String newColName = "NewColName";
 Class<?> newColType = Double.class;
 TableStockage expected = new TableStockage(3, 4);
 expected.setColumnName(3, newColName);
 expected.setColumnClass(3, newColType);
 modifTableStockage = modifTableStockage.ajouterColonne(newColName, newColType);
 TableModel tableModel = modifTableStockage.getTableModel();
 expectedTable = new TableStockage(3, 4);
 expectedTable.setColumnName(3, newColName);
 expectedTable.setColumnClass(3, newColType);
 for (int i = 0; i < 3; i++) {
 for (int j = 0; j < 3; j++) {
 assertEquals(expected.getValueAt(i, j), tableModel.getValueAt(i, j));
 }
 }
 assertEquals(expected, modifTableStockage.getTable());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidName() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 Class<?> newColType = Double.class;
 String invalidName = "";
 modifTableStockage.ajouterColonne(invalidName, newColType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidType() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 String newColName = "NewColName";
 Class<?> invalidType = new Object()[0].getClass();
 modifTableStockage.ajouterColonne(newColName, invalidType);
 }

 @Test
 public void testAjouterLigne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 TableStockage expected = new TableStockage(4, 3);
 modifTableStockage = modifTableStockage.ajouterLigne();
 TableModel tableModel = modifTableStockage.getTableModel();
 expectedTable = new TableStockage(4, 3);
 for (int i = 0; i < 3; i++) {
 for (int j = 0; j < 3; j++) {
 assertEquals(expected.getValueAt(i, j), tableModel.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(4, 4));
 int index = 2;
 TableStockage expected = new TableStockage(4, 3);
 modifTableStockage = modifTableStockage.supprimerColonne(index);
 TableModel tableModel = modifTableStockage.getTableModel();
 expectedTable = new TableStockage(4, 3);
 for (int i = 0; i < 4; i++) {
 for (int j = 0; j < 3; j++) {
 assertEquals(expected.getValueAt(i, j), tableModel.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(4, 4));
 int invalidIndex = 5;
 modifTableStockage.supprimerColonne(invalidIndex);
 }

 @Test
 public void testSupprimerLigne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(4, 3));
 int index = 2;
 TableStockage expected = new TableStockage(3, 3);
 modifTableStockage = modifTableStockage.supprimerLigne(index);
 TableModel tableModel = modifTableStockage.getTableModel();
 expectedTable = new TableStockage(3, 3);
 for (int i = 0; i < 3; i++) {
 for (int j = 0; j < 3; j++) {
 assertEquals(expected.getValueAt(i, j), tableModel.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 int invalidIndex = 5;
 modifTableStockage.supprimerLigne(invalidIndex);
 }

 @Test
 public void testSeparerColonne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(4, 1));
 modifTableStockage = modifTableStockage.separerColonne(0);
 String expectedCol1 = "";
 String expectedCol2 = "";
 TableStockage expected = new TableStockage(4, 2);
 expected.setColumnName(0, expectedCol1);
 expected.setColumnName(1, expectedCol2);
 TableModel tableModel = modifTableStockage.getTableModel();
 TableStockage actual = modifTableStockage.getTable();
 assertEquals(expected.getRowCount(), actual.getRowCount());
 assertEquals(expected.getColumnCount(), actual.getColumnCount());
 for (int i = 0; i < 4; i++) {
 for (int j = 0; j < 2; j++) {
 assertEquals(expected.getValueAt(i, j), actual.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneNoStringType() {
 modifTableStockage = new ModifTableStockage(new TableStockage(4, 1));
 modifTableStockage = modifTableStockage.separerColonne(0);
 String expectedCol1 = "";
 String expectedCol2 = "";
 TableStockage expected = new TableStockage(4, 2);
 expected.setColumnName(0, expectedCol1);
 expected.setColumnName(1, expectedCol2);
 expected.setColumnClass(1, Integer.class);
 modifTableStockage = modifTableStockage.separerColonne(0);
 TableModel tableModel = modifTableStockage.getTableModel();
 TableStockage actual = modifTableStockage.getTable();
 assertEquals(expected.getRowCount(), actual.getRowCount());
 assertEquals(expected.getColumnCount(), actual.getColumnCount());
 for (int i = 0; i < 4; i++) {
 for (int j = 0; j < 2; j++) {
 assertEquals(expected.getValueAt(i, j), actual.getValueAt(i, j));
 }
 }
 }

 @Test
 public void testFusionnerColonnes() {
 modifTableStockage = new ModifTableStockage(new TableStockage(4, 2));
 int index = 0;
 TableStockage expected = new TableStockage(4, 1);
 modifTableStockage = modifTableStockage.fusionnerColonnes(index);
 TableModel tableModel = modifTableStockage.getTableModel();
 expectedTable = new TableStockage(4, 1);
 for (int i = 0; i < 4; i++) {
 assertEquals(expected.getValueAt(i, 0), tableModel.getValueAt(i, 0));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(4, 2));
 int invalidIndex = 5;
 modifTableStockage.fusionnerColonnes(invalidIndex);
 }

 @Test
 public void testColonneMail() {
 modifTableStockage = new ModifTableStockage(new TableStockage(4, 3));
 TableStockage expected = new TableStockage(4, 4);
 expected.setColumnName(3, "Envoyer E-Mail a:");
 expected.setColumnClass(3, Boolean.class);
 for (int i = 0; i < 4; i++) {
 expected.setValueAt(true, i, 3);
 }
 modifTableStockage = modifTableStockage.colonneMail();
 TableModel tableModel = modifTableStockage.getTableModel();
 assertEquals(expected.getRowCount(), tableModel.getRowCount());
 assertEquals(expected.getColumnCount(), tableModel.getColumnCount());
 for (int i = 0; i < 4; i++) {
 for (int j = 0; j < 4; j++) {
 assertEquals(expected.getValueAt(i, j), tableModel.getValueAt(i, j));
 }
 }
 }

 @Test
 public void testSelectMail() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 4));
 boolean[] selectedRows = new boolean[5];
 Arrays.fill(selectedRows, true);
 modifTableStockage = modifTableStockage.selectMail();
 TableModel tableModel = modifTableStockage.getTableModel();
 TableStockage expected = new TableStockage(5, 4);
 for (int i = 0; i < 5; i++) {
 if (selectedRows[i]) {
 for (int j = 0; j < 4; j++) {
 expected.setValueAt(modifTableStockage.getTable().getValueAt(i, j), i, j);
 }
 }
 }
 for (int i = 0; i < 5; i++) {
 for (int j = 0; j < 4; j++) {
 assertEquals(expected.getValueAt(i, j), tableModel.getValueAt(i, j));
 }
 }
 }

}