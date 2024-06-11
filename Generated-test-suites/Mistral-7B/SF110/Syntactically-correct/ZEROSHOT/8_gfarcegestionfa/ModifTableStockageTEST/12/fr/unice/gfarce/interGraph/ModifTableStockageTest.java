package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private TableStockage tableStockage;
 private ModifTableStockage modifTableStockage;

 @Test
 public void testModifTableStockageConstruction() {
 tableStockage = new TableStockage(3, 3);
 modifTableStockage = new ModifTableStockage(tableStockage);
 assertEquals(3, modifTableStockage.getNbColonnes());
 assertEquals(3, modifTableStockage.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
 tableStockage = new TableStockage(3, 3);
 modifTableStockage = new ModifTableStockage(tableStockage);
 String nomNouvelleColonne = "TestColumn";
 Class<?> type = String.class;
 TableStockage nouveauTableau = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(4, nouveauTableau.getColumnCount());
 assertEquals(nomNouvelleColonne, nouveauTableau.getColumnName(3));
 }

 @Test
 public void testAjouterLigne() {
 tableStockage = new TableStockage(3, 3);
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage nouveauTableau = modifTableStockage.ajouterLigne();
 assertEquals(4, nouveauTableau.getRowCount());
 for (int i = 0; i < 3; ++i) {
 for (int j = 0; j < 3; ++j) {
 assertEquals(null, nouveauTableau.getValueAt(3, j));
 }
 }
 }

 @Test
 public void testSupprimerColonne() {
 tableStockage = new TableStockage(3, 3);
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage nouveauTableau = modifTableStockage.supprimerColonne(0);
 assertEquals(2, nouveauTableau.getColumnCount());
 for (int i = 0; i < 3; ++i) {
 assertEquals(null, nouveauTableau.getValueAt(i, 0));
 }
 }

 @Test
 public void testSupprimerLigne() {
 tableStockage = new TableStockage(3, 3);
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage nouveauTableau = modifTableStockage.supprimerLigne(0);
 assertEquals(2, nouveauTableau.getRowCount());
 for (int i = 0; i < 2; ++i) {
 for (int j = 0; j < 3; ++j) {
 assertEquals(null, nouveauTableau.getValueAt(i, j));
 }
 }
 }

 @Test
 public void testSeparerColonne() {
 tableStockage = new TableStockage(new Object[][] { { "Test1  Test2", new Double(4.5) }, { "Test3  Test4", new Double(2.3) } });
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage nouveauTableau = modifTableStockage.separerColonne(0);
 assertEquals(3, nouveauTableau.getColumnCount());
 assertEquals("Test1", nouveauTableau.getColumnName(0));
 assertEquals("Test2", nouveauTableau.getColumnName(1));
 for (int i = 0; i < 2; ++i) {
 for (int j = 0; j < 2; ++j) {
 assertEquals(null, nouveauTableau.getValueAt(i, j));
 }
 }
 for (int i = 0; i < 2; ++i) {
 assertEquals(4.5, nouveauTableau.getValueAt(i, 0), 0.001);
 assertEquals(null, nouveauTableau.getValueAt(i, 1));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidType() {
 tableStockage = new TableStockage(new Object[][] { { "Test1  Test2", new Double(4.5) }, { "Test3  Test4", new Double(2.3) } });
 modifTableStockage = new ModifTableStockage(tableStockage);
 modifTableStockage.separerColonne(1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSeparerColonneOutOfBounds() {
 tableStockage = new TableStockage(new Object[][] { { "Test1  Test2", new Double(4.5) }, { "Test3  Test4", new Double(2.3) } });
 modifTableStockage = new ModifTableStockage(tableStockage);
 modifTableStockage.separerColonne(2);
 }

 @Test
 public void testFusionnerColonnes() {
 tableStockage = new TableStockage(new Object[][] { { "Col1_1", true }, { "Col1_2", false }, { "Col2_1", true }, { "Col2_2", false } });
 modifTableStockage = new ModifTableStockage(tableStockage);
 TableStockage nouveauTableau = modifTableStockage.fusionnerColonnes(0);
 assertEquals(3, nouveauTableau.getColumnCount());
 assertEquals("Col1_1 Col1_2", nouveauTableau.getColumnName(0));
 for (int i = 0; i < 4; ++i) {
 for (int j = 0; j < 2; ++j) {
 assertEquals(null, nouveauTableau.getValueAt(i, j));
 }
 }
 for (int i = 0; i < 4; ++i) {
 assertEquals(true, nouveauTableau.getValueAt(i, 0), 0.001);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidType() {
 tableStockage = new TableStockage(new Object[][] { { "Col1_1", new Double(4.5) }, { "Col1_2", new Double(2.3) }, { "Col2_1", new Boolean(true) }, { "Col2_2", new Boolean(false) } });
 modifTableStockage = new ModifTableStockage(tableStockage);
 modifTableStockage.fusionnerColonnes(1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFusionnerColonnesOutOfBounds() {
 tableStockage = new TableStockage(new Object[][] { { "Col1_1", new Double(4.5) }, { "Col1_2", new Double(2.3) }, { "Col2_1", new Boolean(true) }, { "Col2_2", new Boolean(false) } });
 modifTableStockage = new ModifTableStockage(tableStockage);
 modifTableStockage.fusionnerColonnes(3);
 }
}