package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing. table. TableModel;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Before
 public void setUp() {
 tableStockage = new TableStockage(5, 3);
 String[] columnNames = {"Column1", "Column2", "Column3"};
 tableStockage.setColumnNames(columnNames);
 tableStockage.setValueAt("Test1", 0, 0);
 tableStockage.setValueAt("Test2", 0, 1);
 tableStockage.setValueAt("Test3", 0, 2);
 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @Test
 public void testAjouterColonne() {
 TableStockage tableReturned = modifTableStockage.ajouterColonne("New Column", String.class);
 assertEquals(4, tableReturned.getColumnCount());
 assertEquals("New Column", tableReturned.getColumnName(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullClass() {
 modifTableStockage.ajouterColonne(null, String.class);
 }

 @Test
 public void testAjouterColonneEmptyString() {
 TableStockage tableReturned = modifTableStockage.ajouterColonne("", String.class);
 assertEquals(3, tableReturned.getColumnCount());
 }

 @Test
 public void testAjouterLigne() {
 TableStockage tableReturned = modifTableStockage.ajouterLigne();
 assertEquals(6, tableReturned.getRowCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableModel() {
 @Override
 public int getRowCount() {
 return 0;
 }

 @Override
 public int getColumnCount() {
 return 0;
 }
 });
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 TableStockage tableReturned = modifTableStockage.supprimerColonne(2);
 assertEquals(2, tableReturned.getColumnCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTableStockage.supprimerColonne(3);
 }

 @Test
 public void testSupprimerLigne() {
 TableStockage tableReturned = modifTableStockage.supprimerLigne(1);
 assertEquals(4, tableReturned.getRowCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTableStockage.supprimerLigne(5);
 }

 @Test
 public void testSeparerColonne() {
 TableStockage tableReturned = modifTableStockage.separerColonne(1);
 assertEquals(5, tableReturned.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneNullColumnIndex() {
 modifTableStockage.separerColonne(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneNoColumnSeparator() {
 modifTableStockage.separerColonne(0);
 }

 @Test
 public void testFusionnerColonnes() {
 TableStockage tableReturned = modifTableStockage.fusionnerColonnes(1);
 assertEquals(3, tableReturned.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidColumnIndex() {
 modifTableStockage.fusionnerColonnes(3);
 }

 @Test
 public void testColonneMail() {
 TableStockage tableReturned = modifTableStockage.colonneMail();
 assertEquals(5, tableReturned.getColumnCount());
 }

 @Test
 public void testSelectMail() {
 TableStockage tableReturned = modifTableStockage.selectMail();
 assertEquals(3, tableReturned.getColumnCount());
 assertNotEquals(tableStockage, tableReturned);
 }
}