package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private TableStockage table;
 private ModifTableStockage modifTable;

 @Test
 public void testModifTableStockageConstruction() {
 table = new TableStockage(3, 4);
 modifTable = new ModifTableStockage(table);
 assertEquals(3, modifTable.getNbLignes());
 assertEquals(4, modifTable.getNbColonnes());
 }

 @Test
 public void testAjouterColonne() {
 table = new TableStockage(3, 3);
 modifTable = new ModifTableStockage(table);
 String nomNouvelleColonne = "newColumn";
 Class<?> type = String.class;
 TableStockage expectedTable = new TableStockage(4, 4);
 expectedTable.setColumnName(3, nomNouvelleColonne);
 for (int i = 0; i < 3; ++i) {
 expectedTable.setValueAt(table.getValueAt(i, 0), i, 0);
 expectedTable.setValueAt(table.getValueAt(i, 1), i, 1);
 expectedTable.setValueAt(table.getValueAt(i, 2), i, 2);
 expectedTable.setValueAt("", i, 3);
 }
 TableStockage resultTable = modifTable.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(expectedTable, resultTable);
 }

 @Test
 public void testAjouterLigne() {
 table = new TableStockage(3, 3);
 modifTable = new ModifTableStockage(table);
 TableStockage expectedTable = new TableStockage(4, 3);
 for (int i = 0; i < 3; ++i) {
 for (int j = 0; j < 3; ++j) {
 expectedTable.setValueAt(table.getValueAt(i, j), i, j);
 }
 expectedTable.setValueAt("", 3, j);
 }
 TableStockage resultTable = modifTable.ajouterLigne();
 assertEquals(expectedTable, resultTable);
 }

 @Test
 public void testSupprimerColonne() {
 table = new TableStockage(3, 3);
 modifTable = new ModifTableStockage(table);
 TableStockage expectedTable = new TableStockage(3, 2);
 for (int i = 0; i < 3; ++i) {
 for (int j = 0; j < 2; ++j) {
 expectedTable.setValueAt(table.getValueAt(i, j), i, j);
 }
 }
 TableStockage resultTable = modifTable.supprimerColonne(2);
 assertEquals(expectedTable, resultTable);
 }

 @Test
 public void testSupprimerLigne() {
 table = new TableStockage(3, 3);
 modifTable = new ModifTableStockage(table);
 TableStockage expectedTable = new TableStockage(2, 3);
 for (int i = 0; i < 2; ++i) {
 for (int j = 0; j < 3; ++j) {
 expectedTable.setValueAt(table.getValueAt(i, j), i, j);
 }
 }
 TableStockage resultTable = modifTable.supprimerLigne(2);
 assertEquals(expectedTable, resultTable);
 }

 @Test
 public void testSeparerColonne() {
 table = new TableStockage(3, 1);
 table.setValueAt("Julien 25 ans", 0, 0);
 modifTable = new ModifTableStockage(table);
 TableStockage expectedTable = new TableStockage(3, 2);
 expectedTable.setColumnName(0, "Nom");
 expectedTable.setColumnName(1, "Age");
 expectedTable.setValueAt("Julien", 0, 0);
 expectedTable.setValueAt("25", 0, 1);
 TableStockage resultTable = modifTable.separerColonne(0);
 assertEquals(expectedTable, resultTable);
 }

 @Test(expected = AssertionError.class)
 public void testSeparerColonneFail() {
 table = new TableStockage(3, 1);
 table.setValueAt("Julien 25 ans", 0, 0);
 modifTable = new ModifTableStockage(table);
 TableStockage resultTable = modifTable.separerColonne(0);
 fail("SeparerColonne should throw an AssertionError when given a non-String column");
 }

 @Test
 public void testFusionnerColonnes() {
 table = new TableStockage(3, 2);
 table.setColumnName(0, "Nom");
 table.setColumnName(1, "Age");
 table.setValueAt("Julien", 0, 0);
 table.setValueAt("25", 0, 1);
 table.setValueAt("Marie", 1, 0);
 table.setValueAt("30", 1, 1);
 modifTable = new ModifTableStockage(table);
 TableStockage expectedTable = new TableStockage(2, 2);
 expectedTable.setColumnName(0, "Nom Age");
 expectedTable.setValueAt("Julien 25", 0, 0);
 expectedTable.setValueAt("Marie 30", 1, 0);
 TableStockage resultTable = modifTable.fusionnerColonnes(1);
 assertEquals(expectedTable, resultTable);
 }

 @Test
 public void testColonneMail() {
 table = new TableStockage(3, 3);
 modifTable = new ModifTableStockage(table);
 TableStockage expectedTable = new TableStockage(3, 4);
 expectedTable.setColumnName(3, "Envoyer E-Mail a:");
 for (int i = 0; i < 3; ++i) {
 expectedTable.setValueAt(table.getValueAt(i, 0), i, 0);
 expectedTable.setValueAt(table.getValueAt(i, 1), i, 1);
 expectedTable.setValueAt(table.getValueAt(i, 2), i, 2);
 expectedTable.setValueAt(true, i, 3);
 }
 TableStockage resultTable = modifTable.colonneMail();
 assertEquals(expectedTable, resultTable);
 }

 @Test
 public void testSelectMail() {
 table = new TableStockage(3, 4);
 table.setColumnName(3, "Envoyer E-Mail a:");
 for (int i = 0; i < 3; ++i) {
 table.setValueAt("Julien", i, 0);
 table.setValueAt("Marie", i, 1);
 table.setValueAt("25", i, 2);
 table.setValueAt(true, i, 3);
 }
 TableStockage expectedTable = new TableStockage(2, 4);
 expectedTable.setColumnName(0, "Nom");
 expectedTable.setColumnName(1, "Prénom");
 expectedTable.setColumnName(2, "Age");
 expectedTable.setColumnName(3, "Envoyer E-Mail a:");
 Boolean[] bools = { true, false };
 for (int i = 0; i < 2; ++i) {
 expectedTable.setValueAt(table.getValueAt(i, 0), i, 0);
 expectedTable.setValueAt(table.getValueAt(i, 1), i, 1);
 expectedTable.setValueAt(table.getValueAt(i, 2), i, 2);
 expectedTable.setValueAt(bools[i], i, 3);
 }
 TableStockage resultTable = modifTable.selectMail();
 assertEquals(expectedTable, resultTable);
 }
}