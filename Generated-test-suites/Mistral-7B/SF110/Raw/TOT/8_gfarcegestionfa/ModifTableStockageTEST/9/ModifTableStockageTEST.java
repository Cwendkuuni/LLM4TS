package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage originalTable;
 private TableStockage tableWithData;

 @Before
 public void setUp() {
 Object[][] data = {{"Col1", "Data1"}, {"Col2", "Data2"}, {"Col3", "Data3"}};
 originalTable = new TableStockage(data, "Original Table");
 modifTableStockage = new ModifTableStockage(originalTable);
 tableWithData = modifTableStockage.ajouterColonne("New Column", String.class);
 }

 @Test
 public void testAjouterColonneWithThreeColumns() {
 assertEquals(4, modifTableStockage.getNbColonnes());
 JTable table = modifTableStockage.getTable();
 assertEquals(3, table.getColumnCount());
 assertEquals("New Column", table.getColumnName(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithNegativeNumberOfColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 modifTableStockage.ajouterColonne(-1, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithNullClassType() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 modifTableStockage.ajouterColonne(null, String.class);
 }

 @Test
 public void testAjouterLigneWhenTableIsEmpty() {
 assertEquals(1, modifTableStockage.getNbLignes());
 TableStockage table = modifTableStockage.ajouterLigne();
 assertEquals(4, table.getNbColonnes());
 assertEquals(1, table.getRowCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWhenTableHasNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testAjouterLigneWhenTableHasData() {
 assertEquals(4, modifTableStockage.getNbColonnes());
 assertEquals(3, modifTableStockage.getNbLignes());
 TableStockage table = modifTableStockage.ajouterLigne();
 assertEquals(4, table.getNbColonnes());
 assertEquals(4, table.getRowCount());
 for (int i = 0; i < 3; ++i) {
 for (int j = 0; j < 4; ++j) {
 assertEquals(modifTableStockage.getValueAt(i, j), table.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testSupprimerColonneWhenTableHasNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 modifTableStockage.supprimerColonne(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWhenIndexIsOutOfBounds() {
 modifTableStockage = new ModifTableStockage(tableWithData);
 modifTableStockage.supprimerColonne(4);
 }

 @Test
 public void testSupprimerColonneWithData() {
 assertEquals(4, modifTableStockage.getNbColonnes());
 assertEquals(4, modifTableStockage.getNbLignes());
 TableStockage table = modifTableStockage.supprimerColonne(2);
 assertEquals(3, table.getNbColonnes());
 assertEquals(4, table.getNbLignes());
 for (int i = 0; i < 3; ++i) {
 for (int j = 0; j < 3; ++j) {
 assertEquals(modifTableStockage.getValueAt(i, j), table.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testSupprimerLigneWhenTableHasNoRows() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 modifTableStockage.supprimerLigne(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWhenIndexIsOutOfBounds() {
 modifTableStockage = new ModifTableStockage(tableWithData);
 modifTableStockage.supprimerLigne(4);
 }

 @Test
 public void testSupprimerLigneWithData() {
 assertEquals(4, modifTableStockage.getNbColonnes());
 assertEquals(5, modifTableStockage.getNbLignes());
 TableStockage table = modifTableStockage.supprimerLigne(2);
 assertEquals(4, table.getNbColonnes());
 assertEquals(4, table.getNbLignes());
 for (int i = 0; i < 4; ++i) {
 for (int j = 0; j < 4; ++j) {
 if (i != 2) {
 assertEquals(modifTableStockage.getValueAt(i, j), table.getValueAt(i, j));
 }
 }
 }
 }

 @Test
 public void testSeparerColonneWithData() {
 assertEquals(4, modifTableStockage.getNbColonnes());
 assertEquals(5, modifTableStockage.getNbLignes());
 modifTableStockage.separerColonne(2);
 assertEquals(5, modifTableStockage.getNbColonnes());
 TableStockage table = modifTableStockage.getTable();
 String columnName1 = "Col1";
 String columnName2 = "Data1";
 String columnName3 = "Data2";
 String columnName4 = "Data3";
 String columnName5 = "New Col1";
 String columnName6 = "New Col2";
 for (int i = 0; i < 5; ++i) {
 assertEquals(columnName1, table.getColumnName(i));
 if (i < 3) {
 assertEquals(1, table.getColumnClass(i).getFields().length);
 }
 else {
 assertEquals(2, table.getColumnClass(i).getFields().length);
 }
 }
 for (int i = 0; i < 5; ++i) {
 for (int j = 0; j < 5; ++j) {
 if (i < 3) {
 assertEquals(modifTableStockage.getValueAt(0, i), table.getValueAt(0, i));
 }
 else {
 assertEquals(modifTableStockage.getValueAt(0, i - 1), table.getValueAt(0, i));
 assertEquals(modifTableStockage.getValueAt(0, i), table.getValueAt(0, i + 1));
 }
 }
 }
 }

 @Test
 public void testFusionnerColonnesWithData() {
 assertEquals(5, modifTableStockage.getNbColonnes());
 assertEquals(5, modifTableStockage.getNbLignes());
 modifTableStockage.fusionnerColonnes(3);
 assertEquals(4, modifTableStockage.getNbColonnes());
 TableStockage table = modifTableStockage.getTable();
 for (int i = 0; i < 4; ++i) {
 for (int j = 0; j < 5; ++j) {
 if (i < 3) {
 assertEquals(modifTableStockage.getValueAt(0, i), table.getValueAt(0, i));
 }
 else {
 assertEquals(modifTableStockage.getValueAt(0, i - 1).toString() + " " + modifTableStockage.getValueAt(0, i).toString(), table.getValueAt(0, i).toString());
 }
 }
 }
 }

 @Test
 public void testColonneMail() {
 assertEquals(5, modifTableStockage.getNbColonnes());
 TableStockage table = modifTableStockage.colonneMail();
 assertEquals(6, table.getNbColonnes());
 for (int j = 0; j < 6; ++j) {
 assertEquals(modifTableStockage.getColumnName(j), table.getColumnName(j));
 if (j == 5) {
 assertEquals(Boolean.class, table.getColumnClass(j));
 }
 else {
 assertEquals(modifTableStockage.getColumnClass(j), table.getColumnClass(j));
 }
 }
 }

 @Test
 public void testSelectMail() {
 TableStockage table = modifTableStockage.selectMail();
 assertEquals(3, table.getNbColonnes());
 assertEquals(2, table.getNbLignes());
 for (int i = 0; i < 2; ++i) {
 for (int j = 0; j < 3; ++j) {
 assertEquals(modifTableStockage.getValueAt(i, j), table.getValueAt(i, j));
 }
 }
 }

 @After
 public void tearDown() {
 modifTableStockage = null;
 originalTable = null;
 tableWithData = null;
 }
}