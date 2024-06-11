package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

 private TableModel ts;
 private ModifTableStockage modifTableStockage;

 @Test
 public void testModifTableStockageConstruction() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
  assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  String nomNouvelleColonne = "newColumn";
  Class<?> type = String.class;
  TableModel newTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
  assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
  assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
  assertEquals(nomNouvelleColonne, newTable.getColumnName(modifTableStockage.getNbColonnes() - 1));
 }

 @Test
 public void testAjouterColonneWithExistingName() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  String nomNouvelleColonne = "Column1";
  Class<?> type = String.class;
  modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
  modifTableStockage = new ModifTableStockage(ts);
  try {
   modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
   fail("Should throw IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   // Expected exception
  }
 }

 @Test
 public void testAjouterLigne() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  TableModel newTable = modifTableStockage.ajouterLigne();
  assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
  assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
  for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
   assertEquals(modifTableStockage.getColumnName(i), newTable.getColumnName(i));
  }
 }

 @Test
 public void testSupprimerColonne() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  int indice = 0;
  TableModel newTable = modifTableStockage.supprimerColonne(indice);
  assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
  assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
  try {
   String colonne = modifTableStockage.getColumnName(indice);
   fail("Should throw IndexOutOfBoundsException");
  } catch (IndexOutOfBoundsException e) {
   // Expected exception
  }
 }

 @Test
 public void testSupprimerLigne() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  int indice = 0;
  TableModel newTable = modifTableStockage.supprimerLigne(indice);
  assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
  assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
  try {
   Object valeur = newTable.getValueAt(indice, 0);
   fail("Should throw IndexOutOfBoundsException");
  } catch (IndexOutOfBoundsException e) {
   // Expected exception
  }
 }

 @Test
 public void testSeparerColonne() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  String value = "Value 1 Value 2";
  int indice = 0;
  TableModel newTable = modifTableStockage.separerColonne(indice);
  assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
  assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
  assertEquals(value.substring(0, value.indexOf(" ")), newTable.getColumnName(indice));
  assertEquals(value.substring(value.indexOf(" ") + 1), newTable.getColumnName(indice + 1));
  for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
   String newValue1 = newTable.getValueAt(i, indice).toString();
   String newValue2 = newTable.getValueAt(i, indice + 1).toString();
   String oldValue = modifTableStockage.getValueAt(i, indice).toString();
   assertEquals(oldValue.substring(0, oldValue.indexOf(" ")), newValue1);
   assertEquals(oldValue.substring(oldValue.indexOf(" ") + 1), newValue2);
  }
 }

 @Test
 public void testSeparerColonneWithNoSpace() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  String value = "Value1Value2";
  int indice = 0;
  TableModel newTable = modifTableStockage.separerColonne(indice);
  assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
  assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
  assertFalse(newTable.getColumnName(indice).equals(""));
  assertFalse(newTable.getColumnName(indice + 1).equals(""));
  for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
   String newValue1 = newTable.getValueAt(i, indice).toString();
   String newValue2 = newTable.getValueAt(i, indice + 1).toString();
   String oldValue = modifTableStockage.getValueAt(i, indice).toString();
   assertEquals(oldValue.substring(0, oldValue.length() / 2), newValue1);
   assertEquals(oldValue.substring(oldValue.length() / 2), newValue2);
  }
 }

 @Test
 public void testFusionnerColonnes() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  int indice = 0;
  String column1 = "Column1";
  String column2 = "Column2";
  TableModel newTable = modifTableStockage.fusionnerColonnes(indice);
  assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
  assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
  assertEquals(column1 + " " + column2, newTable.getColumnName(indice));
  for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
   Object oldValue1 = modifTableStockage.getValueAt(i, indice);
   Object oldValue2 = modifTableStockage.getValueAt(i, indice + 1);
   Object newValue = newTable.getValueAt(i, indice);
   assertEquals(oldValue1.toString() + " " + oldValue2.toString(), newValue.toString());
  }
 }

 @Test
 public void testFusionnerColonnesWithDifferentTypes() {
  ts = new DefaultTableModel();
  modifTableStockage = new ModifTableStockage(ts);
  int indice = 0;
  String column1 = "BooleanColumn";
  String column2 = "DoubleColumn";
  TableModel newTable = modifTableStockage.fusionnerColonnes(indice);
  assertEquals(modifTableStockage.getNbColonnes(), newTable.getColumnCount());
  assertEquals(modifTableStockage.getNbLignes(), newTable.getRowCount());
  assertFalse(newTable.getColumnName(indice).equals(""));
  fail("Should throw IllegalArgumentException");
 }
}