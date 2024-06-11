package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private TableStockage createTableStockage() {
 TableModel model = new DefaultTableModel();
 JTable table = new JTable(model);
 ModifTableStockage modifTableStockage = new ModifTableStockage(table);
 return modifTableStockage;
 }

 @Test
 public void testModifTableStockageConstructor() {
 ModifTableStockage modifTableStockage = new ModifTableStockage(null);
 assertNull(modifTableStockage.ts);
 assertFalse(modifTableStockage.nbColonnes > 0);
 assertFalse(modifTableStockage.nbLignes > 0);

 TableStockage tableStockage = new TableStockage(1, 1);
 ModifTableStockage modifTableStockage1 = new ModifTableStockage(tableStockage);
 assertTrue(modifTableStockage1.nbColonnes > 0);
 assertTrue(modifTableStockage1.nbLignes > 0);
 assertNotNull(modifTableStockage1.ts);
 }

 @Test
 public void testAjouterColonne() {
 ModifTableStockage modifTableStockage = createTableStockage();
 String nomNouvelleColonne = "Test";
 Class<?> type = String.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(modifTableStockage.nbColonnes, tableS.getColumnCount());
 assertEquals(modifTableStockage.nbLignes, tableS.getRowCount());
 for (int j = 0; j < modifTableStockage.nbColonnes; ++j) {
 String expectedColumnName = null;
 if (j < modifTableStockage.nbColonnes - 1) {
 expectedColumnName = modifTableStockage.ts.getColumnName(j);
 }
 else {
 expectedColumnName = nomNouvelleColonne;
 }
 assertEquals(expectedColumnName, tableS.getColumnName(j));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidType() {
 ModifTableStockage modifTableStockage = createTableStockage();
 String nomNouvelleColonne = "Test";
 Class<?> type = Integer.class;
 modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAjouterLigne() {
 ModifTableStockage modifTableStockage = createTableStockage();
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertEquals(modifTableStockage.nbLignes, tableS.getRowCount());
 for (int i = 0; i < modifTableStockage.nbLignes - 1; ++i) {
 for (int k = 0; k < modifTableStockage.nbColonnes; ++k) {
 assertEquals(modifTableStockage.ts.getValueAt(i, k), tableS.getValueAt(i, k));
 }
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColonnes() {
 ModifTableStockage modifTableStockage = new ModifTableStockage(null);
 modifTableStockage.ajouterLigne();
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testSupprimerColonne() {
 ModifTableStockage modifTableStockage = createTableStockage();
 TableStockage tableS = modifTableStockage.supprimerColonne(0);
 assertEquals(modifTableStockage.nbColonnes - 1, tableS.getColumnCount());
 for (int j = 0; j < modifTableStockage.nbColonnes - 1; ++j) {
 assertEquals(modifTableStockage.ts.getColumnName(j), tableS.getColumnName(j));
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 ModifTableStockage modifTableStockage = createTableStockage();
 modifTableStockage.supprimerColonne(modifTableStockage.nbColonnes);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSupprimerLigne() {
 ModifTableStockage modifTableStockage = createTableStockage();
 TableStockage tableS = modifTableStockage.supprimerLigne(0);
 assertEquals(modifTableStockage.nbLignes - 1, tableS.getRowCount());
 for (int i = 0; i < modifTableStockage.nbLignes - 1; ++i) {
 for (int k = 0; k < modifTableStockage.nbColonnes; ++k) {
 assertEquals(modifTableStockage.ts.getValueAt(i, k), tableS.getValueAt(i, k));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 ModifTableStockage modifTableStockage = createTableStockage();
 modifTableStockage.supprimerLigne(modifTableStockage.nbLignes);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSeparerColonne() {
 ModifTableStockage modifTableStockage = createTableStockage();
 String expectedColonneName = "Test1";
 TableStockage tableS1 = modifTableStockage.separerColonne(0);
 assertEquals(modifTableStockage.nbColonnes + 1, tableS1.getColumnCount());
 assertEquals(modifTableStockage.nbLignes, tableS1.getRowCount());
 assertEquals(expectedColonneName, tableS1.getColumnName(modifTableStockage.nbColonnes - 1));

 String expectedColonneName2 = "Test2";
 TableStockage tableS2 = modifTableStockage.separerColonne(1);
 assertEquals(modifTableStockage.nbColonnes + 1, tableS2.getColumnCount());
 assertEquals(modifTableStockage.nbLignes, tableS2.getRowCount());
 assertEquals(expectedColonneName2, tableS2.getColumnName(modifTableStockage.nbColonnes - 2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidIndex() {
 ModifTableStockage modifTableStockage = createTableStockage();
 modifTableStockage.separerColonne(-1);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSeparerColonneEmptyString() {
 ModifTableStockage modifTableStockage = createTableStockage();
 String expectedColonneName1 = "Test1";
 TableStockage tableS1 = modifTableStockage.separerColonne(0);
 assertEquals(modifTableStockage.nbColonnes + 1, tableS1.getColumnCount());
 assertEquals(modifTableStockage.nbLignes, tableS1.getRowCount());
 assertEquals(expectedColonneName1, tableS1.getColumnName(modifTableStockage.nbColonnes - 1));

 String expectedColonneName2 = "";
 TableStockage tableS2 = modifTableStockage.separerColonne(1);
 assertEquals(modifTableStockage.nbColonnes + 1, tableS2.getColumnCount());
 assertEquals(modifTableStockage.nbLignes, tableS2.getRowCount());
 assertEquals(expectedColonneName2, tableS2.getColumnName(modifTableStockage.nbColonnes - 1));
 }

 @Test
 public void testSeparerColonneNoSeparator() {
 ModifTableStockage modifTableStockage = createTableStockage();
 String expectedColonneName1 = "Test1";
 TableStockage tableS1 = modifTableStockage.separerColonne(0);
 assertEquals(modifTableStockage.nbColonnes + 1, tableS1.getColumnCount());
 assertEquals(modifTableStockage.nbLignes, tableS1.getRowCount());
 assertEquals(expectedColonneName1, tableS1.getColumnName(modifTableStockage.nbColonnes - 1));

 String expectedColonneName2 = "Test2";
 TableStockage tableS2 = modifTableStockage.separerColonne(1);
 assertEquals(modifTableStockage.nbColonnes + 1, tableS2.getColumnCount());
 assertEquals(modifTableStockage.nbLignes, tableS2.getRowCount());
 assertEquals(expectedColonneName2, tableS2.getColumnName(modifTableStockage.nbColonnes - 2));
 }

 @Test
 public void testFusionnerColonnes() {
 ModifTableStockage modifTableStockage = createTableStockage();
 TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
 assertEquals(modifTableStockage.nbColonnes - 1, tableS.getColumnCount());
 for (int j = 0; j < modifTableStockage.nbColonnes - 1; ++j) {
 if (j != 1) {
 assertEquals(modifTableStockage.ts.getColumnName(j), tableS.getColumnName(j));
 }
 }
 String expectedColumnName = "Test1 Test2";
 assertEquals(expectedColumnName, tableS.getColumnName(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndex() {
 ModifTableStockage modifTableStockage = createTableStockage();
 modifTableStockage.fusionnerColonnes(-1);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesStringType() {
 ModifTableStockage modifTableStockage = createTableStockage();
 modifTableStockage.fusionnerColonnes(0);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testColonneMail() {
 ModifTableStockage modifTableStockage = createTableStockage();
 TableStockage tableS = modifTableStockage.colonneMail();
 assertEquals("Envoyer E-Mail a:", tableS.getColumnName(modifTableStockage.nbColonnes - 1));
 }

 @Test
 public void testSelectMail() {
 ModifTableStockage modifTableStockage = createTableStockage();
 int j = 0;
 for (; j < modifTableStockage.nbColonnes && !modifTableStockage.ts.getColumnName(j).equals("Envoyer E-Mail a:"); ++j) {}
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(1, tableS.getRowCount());
 for (int i = 0; i < modifTableStockage.nbColonnes; ++i) {
 if (i != modifTableStockage.nbColonnes - 1) {
 assertEquals(modifTableStockage.ts.getValueAt(0, i), tableS.getValueAt(0, i));
 }
 }
 }

 @Test
 public void testSelectMailNoMailColumn() {
 ModifTableStockage modifTableStockage = createTableStockage();
 int j = 0;
 for (; j < modifTableStockage.nbColonnes; ++j) {
 if (modifTableStockage.ts.getColumnName(j).equals("Envoyer E-Mail a:")) {
 break;
 }
 }
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(0, tableS.getRowCount());
 }
}