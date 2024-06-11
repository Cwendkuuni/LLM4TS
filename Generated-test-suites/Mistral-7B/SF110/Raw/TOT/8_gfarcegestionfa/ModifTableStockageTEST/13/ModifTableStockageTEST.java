package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

 private ModifTableStockage modifTable;
 private TableStockage originalTable;

 @Test
 public void testConstructor() {
 TableStockage ts = new TableStockage(5, 3);
 ModifTableStockage modifTable = new ModifTableStockage(ts);
 assertEquals(5, modifTable.getNbLignes());
 assertEquals(3, modifTable.getNbColonnes());
 }

 @Test
 public void testAjouterColonneStringClass() {
 modifTable = new ModifTableStockage(new TableStockage(2, 2));
 String nomNouvelleColonne = "TestColumn";
 Class<?> type = String.class;
 TableStockage expectedTable = new TableStockage(2, 3);
 expectedTable.setColumnName(2, nomNouvelleColonne);
 modifTable = modifTable.ajouterColonne(nomNouvelleColonne, type);
 TableModel actualTable = modifTable.getTableModel();
 assertEquals(expectedTable, actualTable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullName() {
 modifTable = new ModifTableStockage(new TableStockage(2, 2));
 Class<?> type = String.class;
 modifTable.ajouterColonne(null, type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullType() {
 modifTable = new ModifTableStockage(new TableStockage(2, 2));
 String nomNouvelleColonne = "TestColumn";
 modifTable.ajouterColonne(nomNouvelleColonne, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidType() {
 modifTable = new ModifTableStockage(new TableStockage(2, 2));
 String nomNouvelleColonne = "TestColumn";
 Class<?> type = Integer.class;
 modifTable.ajouterColonne(nomNouvelleColonne, type);
 }

 @Test
 public void testAjouterLigne() {
 modifTable = new ModifTableStockage(new TableStockage(2, 2));
 TableStockage expectedTable = new TableStockage(3, 2);
 expectedTable.setValueAt("", 2, 0);
 modifTable = modifTable.ajouterLigne();
 TableModel actualTable = modifTable.getTableModel();
 assertEquals(expectedTable, actualTable);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumns() {
 modifTable = new ModifTableStockage(new TableStockage(2, 0));
 modifTable.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 modifTable = new ModifTableStockage(new TableStockage(5, 3));
 TableStockage expectedTable = new TableStockage(5, 2);
 modifTable = modifTable.supprimerColonne(2);
 TableModel actualTable = modifTable.getTableModel();
 assertEquals(expectedTable, actualTable);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTable = new ModifTableStockage(new TableStockage(5, 3));
 modifTable.supprimerColonne(6);
 }

 @Test
 public void testSupprimerLigne() {
 modifTable = new ModifTableStockage(new TableStockage(5, 3));
 TableStockage expectedTable = new TableStockage(4, 3);
 modifTable = modifTable.supprimerLigne(2);
 TableModel actualTable = modifTable.getTableModel();
 assertEquals(expectedTable, actualTable);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTable = new ModifTableStockage(new TableStockage(5, 3));
 modifTable.supprimerLigne(6);
 }

 @Test
 public void testSeparerColonne() {
 modifTable = new ModifTableStockage(new TableStockage(3, 1));
 modifTable.separerColonne(0);
 TableStockage expectedTable = new TableStockage(3, 2);
 expectedTable.setColumnName(0, "Nom");
 expectedTable.setColumnName(1, "Prénom");
 modifTable = modifTable.separerColonne(0);
 TableModel actualTable = modifTable.getTableModel();
 assertEquals(expectedTable, actualTable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneNoColumn() {
 modifTable = new ModifTableStockage(new TableStockage(3, 1));
 modifTable.separerColonne(2);
 }

 @Test
 public void testFusionnerColonnes() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 modifTable = modifTable.fusionnerColonnes(1);
 TableStockage expectedTable = new TableStockage(3, 1);
 expectedTable.setColumnName(0, "NomPrénom");
 modifTable = modifTable.fusionnerColonnes(1);
 TableModel actualTable = modifTable.getTableModel();
 assertEquals(expectedTable, actualTable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndex() {
 modifTable = new ModifTableStockage(new TableStockage(3, 2));
 modifTable = modifTable.fusionnerColonnes(3);
 }

 @Test
 public void testColonneMail() {
 modifTable = new ModifTableStockage(new TableStockage(2, 1));
 TableStockage expectedTable = new TableStockage(2, 2);
 expectedTable.setColumnName(1, "Envoyer E-Mail a:");
 modifTable = modifTable.colonneMail();
 TableModel actualTable = modifTable.getTableModel();
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testSelectMail() {
 modifTable = new ModifTableStockage(new TableStockage(4, 2));
 modifTable.colonneMail();
 Boolean[] expectedSelection = {true, false, true, false};
 TableStockage selectedTable = modifTable.selectMail();
 Object[][] actualData = selectedTable.getModel().getData();
 for (int i = 0; i < 4; ++i) {
 assertEquals(expectedSelection[i], actualData[i][3]);
 }
 }
}