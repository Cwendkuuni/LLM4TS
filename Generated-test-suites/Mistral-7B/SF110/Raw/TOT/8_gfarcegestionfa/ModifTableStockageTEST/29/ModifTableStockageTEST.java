package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.junit.After;
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
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 tableStockage.setColumnName(2, "Is Married");
 tableStockage.setValueAt("John Doe", 0, 0);
 tableStockage.setValueAt(25, 0, 1);
 tableStockage.setValueAt(true, 0, 2);
 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @After
 public void tearDown() {
 modifTableStockage = null;
 tableStockage = null;
 }

 @Test
 public void testAjouterColonneStringClass() {
 final String nameNewColumn = "NewColumn";
 final Class<?> type = String.class;
 final TableStockage expectedTable = new TableStockage(5, 4);
 expectedTable.setColumnName(3, nameNewColumn);
 final TableStockage actualTable = modifTableStockage.ajouterColonne(nameNewColumn, type);
 assertEquals("Unexpected number of columns", 4, actualTable.getColumnCount());
 assertEquals("Unexpected column name", nameNewColumn, actualTable.getColumnName(3));
 assertArrayEquals("Unexpected table data", expectedTable.getValues(), actualTable.getValues());
 }

 @Test
 public void testAjouterColonneStringClass_EmptyTable() {
 final String nameNewColumn = "NewColumn";
 final Class<?> type = String.class;
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 final TableStockage expectedTable = new TableStockage(0, 1);
 final TableStockage actualTable = modifTableStockage.ajouterColonne(nameNewColumn, type);
 assertEquals("Unexpected number of columns", 1, actualTable.getColumnCount());
 assertEquals("Unexpected column name", nameNewColumn, actualTable.getColumnName(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullName() {
 modifTableStockage.ajouterColonne(null, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullType() {
 modifTableStockage.ajouterColonne("NewColumn", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidType() {
 modifTableStockage.ajouterColonne("NewColumn", int.class);
 }

 @Test
 public void testAjouterLigne() {
 final TableStockage expectedTable = new TableStockage(6, 3);
 expectedTable.setValueAt("", 5, 0);
 expectedTable.setValueAt(26, 5, 1);
 expectedTable.setValueAt(true, 5, 2);
 final TableStockage actualTable = modifTableStockage.ajouterLigne();
 assertEquals("Unexpected number of rows", 6, actualTable.getRowCount());
 assertArrayEquals("Unexpected table data", expectedTable.getValues(), actualTable.getValues());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 final int index = 1;
 final TableStockage expectedTable = new TableStockage(5, 2);
 expectedTable.setColumnName(index - 1, "Name");
 final TableStockage actualTable = modifTableStockage.supprimerColonne(index);
 assertEquals("Unexpected number of columns", 2, actualTable.getColumnCount());
 assertEquals("Unexpected column name", "Name", actualTable.getColumnName(index - 1));
 assertArrayEquals("Unexpected table data", expectedTable.getValues(), actualTable.getValues());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneLastColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 modifTableStockage.supprimerColonne(3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneNegativeIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 modifTableStockage.supprimerColonne(-1);
 }

 @Test
 public void testSupprimerLigne() {
 final int index = 1;
 final TableStockage expectedTable = new TableStockage(4, 3);
 final TableStockage actualTable = modifTableStockage.supprimerLigne(index);
 assertEquals("Unexpected number of rows", 4, actualTable.getRowCount());
 assertArrayEquals("Unexpected table data", expectedTable.getValues(), actualTable.getValues());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneLastRow() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 modifTableStockage.supprimerLigne(5);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneNegativeIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 modifTableStockage.supprimerLigne(-1);
 }

 @Test
 public void testSeparerColonne() {
 final int index = 0;
 final String expectedColumnName1 = "Name";
 final String expectedColumnName2 = "Age";
 final TableStockage expectedTable = new TableStockage(5, 5);
 expectedTable.setColumnName(index, expectedColumnName1);
 expectedTable.setColumnName(index + 1, expectedColumnName2);
 final TableStockage actualTable = modifTableStockage.separerColonne(index);
 assertEquals("Unexpected number of columns", 5, actualTable.getColumnCount());
 assertEquals("Unexpected column names", Arrays.asList(expectedColumnName1, expectedColumnName2), Arrays.asList(actualTable.getColumnName(index), actualTable.getColumnName(index + 1)));
 assertArrayEquals("Unexpected table data", expectedTable.getValues(), actualTable.getValues());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidIndex() {
 modifTableStockage.separerColonne(3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.separerColonne(0);
 }

 @Test
 public void testFusionnerColonnes() {
 final int index = 1;
 final String expectedColumnName = "Name Age";
 final TableStockage expectedTable = new TableStockage(5, 2);
 expectedTable.setColumnName(index, expectedColumnName);
 final TableStockage actualTable = modifTableStockage.fusionnerColonnes(index);
 assertEquals("Unexpected number of columns", 2, actualTable.getColumnCount());
 assertEquals("Unexpected column name", expectedColumnName, actualTable.getColumnName(index));
 assertArrayEquals("Unexpected table data", expectedTable.getValues(), actualTable.getValues());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndex() {
 modifTableStockage.fusionnerColonnes(3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.fusionnerColonnes(0);
 }

 @Test
 public void testColonneMail() {
 final boolean[] expectedValues = new boolean[5];
 Arrays.fill(expectedValues, true);
 final TableStockage expectedTable = new TableStockage(5, 4);
 expectedTable.setColumnName(3, "Envoyer E-Mail a:");
 expectedTable.setColumnClass(3, Boolean.class);
 for (int i = 0; i < 5; ++i) {
 expectedTable.setValueAt(expectedValues[i], i, 3);
 }
 final TableStockage actualTable = modifTableStockage.colonneMail();
 assertEquals("Unexpected number of rows", 5, actualTable.getRowCount());
 assertEquals("Unexpected number of columns", 4, actualTable.getColumnCount());
 assertArrayEquals("Unexpected table data", expectedTable.getValues(), actualTable.getValues());
 }

 @Test
 public void testSelectMail() {
 final int[] indices = new int[2];
 final boolean[] expectedValues = new boolean[2];
 indices[0] = 1;
 indices[1] = 3;
 expectedValues[0] = true;
 expectedValues[1] = false;
 final TableStockage expectedTable = new TableStockage(3, 4);
 expectedTable.setValueAt(expectedValues[0], 0, 3);
 expectedTable.setValueAt(expectedValues[1], 1, 3);
 final TableStockage actualTable = modifTableStockage.selectMail();
 assertEquals("Unexpected number of rows", 3, actualTable.getRowCount());
 assertEquals("Unexpected number of columns", 4, actualTable.getColumnCount());
 assertArrayEquals("Unexpected table data", expectedTable.getValues(), actualTable.getValues());
 }

 @Test
 public void testSelectMailEmptyTable() {
 final TableStockage actualTable = modifTableStockage.selectMail();
 assertEquals("Unexpected number of rows", 0, actualTable.getRowCount());
 assertEquals("Unexpected number of columns", 4, actualTable.getColumnCount());
 }

 @Test
 public void testSelectMailAllRowsSelected() {
 modifTableStockage.colonneMail();
 final TableStockage expectedTable = new TableStockage(5, 4);
 final TableStockage actualTable = modifTableStockage.selectMail();
 assertEquals("Unexpected number of rows", 5, actualTable.getRowCount());
 assertEquals("Unexpected number of columns", 4, actualTable.getColumnCount());
 assertArrayEquals("Unexpected table data", expectedTable.getValues(), actualTable.getValues());
 }
}