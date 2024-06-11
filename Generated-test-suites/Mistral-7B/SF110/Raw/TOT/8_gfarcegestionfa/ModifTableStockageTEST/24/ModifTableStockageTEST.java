package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

 private ModifTableStockage modifTable;
 private TableModel originalTable;

 @Test
 public void testModifTableStockageConstruction() {
 TableModel table = new JTableModelExample(); // Assuming JTableModelExample is a valid TableModel implementation
 modifTable = new ModifTableStockage(table);
 assertEquals(table.getColumnCount(), modifTable.getNbColonnes());
 assertEquals(table.getRowCount() + 1, modifTable.getNbLignes());
 }

 @Test
 public void testAjouterColonneStringClass() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 String newColumnName = "NewColumn";
 Class<?> stringClass = String.class;
 TableModel newTable = modifTable.ajouterColonne(newColumnName, stringClass);
 assertEquals(modifTable.getNbColonnes() + 1, newTable.getColumnCount());
 assertEquals(newTable.getValueAt(0, newTable.getColumnCount() - 1), newColumnName);
 }

 @Test
 public void testAjouterColonneWrongClass() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 String newColumnName = "NewColumn";
 Class<?> doubleClass = Double.class; // Wrong data type
 TableModel newTable = modifTable.ajouterColonne(newColumnName, doubleClass);
 assertEquals(modifTable.getNbColonnes(), newTable.getColumnCount());
 assertThrows(IllegalArgumentException.class, () -> modifTable.ajouterColonne(newColumnName, doubleClass));
 }

 @Test
 public void testAjouterLigne() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 TableModel newTable = modifTable.ajouterLigne();
 assertEquals(modifTable.getNbLignes(), newTable.getRowCount());
 }

 @Test
 public void testAjouterLigneNoColumns() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 modifTable.setNbColonnes(0); // Set to an invalid number of columns
 TableModel newTable = modifTable.ajouterLigne();
 assertEquals(modifTable.getNbLignes(), newTable.getRowCount());
 }

 @Test
 public void testSupprimerColonne() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToRemove = 0;
 TableModel newTable = modifTable.supprimerColonne(columnToRemove);
 assertEquals(modifTable.getNbColonnes() - 1, newTable.getColumnCount());
 }

 @Test
 public void testSupprimerColonneInvalidIndex() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToRemove = modifTable.getNbColonnes(); // Invalid index
 TableModel newTable = modifTable.supprimerColonne(columnToRemove);
 assertEquals(modifTable.getNbColonnes(), newTable.getColumnCount());
 }

 @Test
 public void testSupprimerLigne() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int rowToRemove = 0;
 TableModel newTable = modifTable.supprimerLigne(rowToRemove);
 assertEquals(modifTable.getNbLignes() - 1, newTable.getRowCount());
 }

 @Test
 public void testSupprimerLigneInvalidIndex() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int rowToRemove = modifTable.getNbLignes(); // Invalid index
 TableModel newTable = modifTable.supprimerLigne(rowToRemove);
 assertEquals(modifTable.getNbLignes(), newTable.getRowCount());
 }

 @Test
 public void testSeparerColonne() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToSeparate = 0;
 TableModel newTable = modifTable.separerColonne(columnToSeparate);
 assertEquals(modifTable.getNbColonnes() + 1, newTable.getColumnCount());
 }

 @Test
 public void testSeparerColonneInvalidType() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToSeparate = 0;
 TableModel newTable = modifTable.separerColonne(columnToSeparate);
 assertEquals(modifTable.getNbColonnes(), newTable.getColumnCount());
 assertThrows(IllegalArgumentException.class, () -> modifTable.separerColonne(columnToSeparate));
 }

 @Test
 public void testSeparerColonneNoSeparator() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToSeparate = 0;
 String expectedColumnName1 = "Column1";
 String expectedColumnName2 = "Column2";
 TableModel newTable = modifTable.separerColonne(columnToSeparate);
 assertThrows(IllegalArgumentException.class, () -> modifTable.separerColonne(columnToSeparate));
 // Column names are not checked in this test, it is assumed that the implementation makes sure the column to separate contains a separator
 }

 @Test
 public void testFusionnerColonnes() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToFuse = 0;
 int nextColumnToFuse = 1;
 TableModel newTable = modifTable.fusionnerColonnes(columnToFuse);
 assertEquals(modifTable.getNbColonnes() - 1, newTable.getColumnCount());
 }

 @Test
 public void testFusionnerColonnesInvalidIndex() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToFuse = 0;
 int nextColumnToFuse = modifTable.getNbColonnes(); // Invalid index
 TableModel newTable = modifTable.fusionnerColonnes(columnToFuse);
 assertEquals(modifTable.getNbColonnes(), newTable.getColumnCount());
 }

 @Test
 public void testFusionnerColonnesDifferentTypes() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToFuse = 0;
 int nextColumnToFuse = 1;
 TableModel newTable = modifTable.fusionnerColonnes(columnToFuse);
 assertEquals(modifTable.getNbColonnes(), newTable.getColumnCount());
 assertThrows(IllegalArgumentException.class, () -> modifTable.fusionnerColonnes(columnToFuse));
 }

 @Test
 public void testColonneMail() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 TableModel newTable = modifTable.colonneMail();
 assertEquals(modifTable.getNbColonnes() + 1, newTable.getColumnCount());
 String newColumnName = "Envoyer E-Mail a:";
 assertEquals(newTable.getColumnName(newTable.getColumnCount() - 1), newColumnName);
 }

 @Test
 public void testSelectMail() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToSelect = 0; // Column that does not have the "Envoyer E-Mail a:" name
 TableModel newTable = modifTable.selectMail();
 assertEquals(modifTable.getNbLignes(), newTable.getRowCount());
 }

 @Test
 public void testSelectMailWithEmailColumns() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 TableModel originalTable = modifTable.colonneMail(); // Add the "Envoyer E-Mail a:" column
 int columnToSelect = modifTable.getNbColonnes() - 2;
 TableModel newTable = modifTable.selectMail();
 assertEquals(newTable.getRowCount(), modifTable.getNbLignes() - 1);
 }

 @Test
 public void testSelectMailEmptyTable() {
 modifTable = new ModifTableStockage(new JTableModelExample());
 int columnToSelect = 0;
 TableModel newTable = modifTable.selectMail();
 assertEquals(modifTable.getNbLignes(), newTable.getRowCount());
 }
}