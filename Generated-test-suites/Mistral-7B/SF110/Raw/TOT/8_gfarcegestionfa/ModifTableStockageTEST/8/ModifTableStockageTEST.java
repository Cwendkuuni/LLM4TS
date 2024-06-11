package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.util.Random;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTable;
 private TableStockage originalTable;

 @Before
 public void setUp() {
 String[] columnNames = { "Name", "Age", "City" };
 Object[][] data = { { "John Doe", 30, "Paris" }, { "Jane Doe", 25, "Marseille" }, { "Alice Smith", 35, "Lyon" } };
 originalTable = new TableStockage(data, columnNames);
 modifTable = new ModifTableStockage(originalTable);
 }

 @Test
 public void testAjouterColonne() {
 String newColumnName = "Country";
 Class<?> columnType = String.class;
 TableStockage newTable = modifTable.ajouterColonne(newColumnName, columnType);
 String[] expectedColumnNames = { "Name", "Age", "City", "Country" };
 assertArrayEquals(expectedColumnNames, newTable.getColumnNames());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithInvalidColumnType() {
 String newColumnName = "Country";
 Class<?> columnType = Boolean.class;
 modifTable.ajouterColonne(newColumnName, columnType);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testAjouterLigne() {
 TableStockage newTable = modifTable.ajouterLigne();
 Object[] expectedData = { null, null, null, null };
 DefaultTableModel model = (DefaultTableModel) newTable.getModel();
 assertArrayEquals(expectedData, model.getDataVector().get(model.getRowCount() - 1));
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWithNoColumns() {
 modifTable.nbColonnes = 0;
 modifTable.ajouterLigne();
 fail("Should throw an IllegalStateException");
 }

 @Test
 public void testSupprimerColonne() {
 int columnToRemove = 1;
 TableStockage newTable = modifTable.supprimerColonne(columnToRemove);
 String[] expectedColumnNames = { "Name", "City" };
 assertArrayEquals(expectedColumnNames, newTable.getColumnNames());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithInvalidIndex() {
 int columnToRemove = -1;
 modifTable.supprimerColonne(columnToRemove);
 fail("Should throw an IndexOutOfBoundsException");
 }

 @Test
 public void testSupprimerLigne() {
 int rowToRemove = 1;
 TableStockage newTable = modifTable.supprimerLigne(rowToRemove);
 Object[][] expectedData = { { "John Doe", 30, "Paris" }, { "Alice Smith", 35, "Lyon" } };
 assertArrayEquals(expectedData, newTable.getData());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithInvalidIndex() {
 int rowToRemove = -1;
 modifTable.supprimerLigne(rowToRemove);
 fail("Should throw an IndexOutOfBoundsException");
 }

 @Test
 public void testSeparerColonne() {
 int columnToSeparate = 0;
 TableStockage newTable = modifTable.separerColonne(columnToSeparate);
 String[] expectedColumnNames = { "Name", "First Name", "Age" };
 assertArrayEquals(expectedColumnNames, newTable.getColumnNames());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithInvalidColumnIndex() {
 int columnToSeparate = -1;
 modifTable.separerColonne(columnToSeparate);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testFusionnerColonnes() {
 int firstColumnIndex = 0;
 int secondColumnIndex = 1;
 TableStockage newTable = modifTable.fusionnerColonnes(firstColumnIndex);
 String[] expectedColumnNames = { "NameCity" };
 assertArrayEquals(expectedColumnNames, newTable.getColumnNames());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithInvalidColumnIndices() {
 int firstColumnIndex = 0;
 int secondColumnIndex = 2;
 modifTable.fusionnerColonnes(firstColumnIndex);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testColonneMail() {
 TableStockage newTable = modifTable.colonneMail();
 String[] expectedColumnNames = { "Name", "Age", "City", "Envoyer E-Mail a:" };
 assertArrayEquals(expectedColumnNames, newTable.getColumnNames());
 }

 @Test
 public void testSelectMail() {
 int columnIndexToSelect = 3;
 TableStockage newTable = modifTable.selectMail();
 Object[][] expectedData = { { "John Doe", 30, "Paris", true }, { "Alice Smith", 35, "Lyon", true } };
 assertArrayEquals(expectedData, newTable.getData());
 }

 @Test
 public void testSelectMailWithNoEmailColumn() {
 modifTable.nbColonnes = 3;
 modifTable.supprimerColonne(3);
 TableStockage newTable = modifTable.selectMail();
 Object[][] expectedData = { { "John Doe", 30, "Paris" }, { "Alice Smith", 35, "Lyon" } };
 assertArrayEquals(expectedData, newTable.getData());
 }
}