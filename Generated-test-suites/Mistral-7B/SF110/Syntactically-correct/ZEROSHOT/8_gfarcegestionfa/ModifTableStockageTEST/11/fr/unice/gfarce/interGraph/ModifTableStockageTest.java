package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Test
 public void testModifTableStockageConstruction() {
     tableStockage = new TableStockage(3, 2);
     modifTableStockage = new ModifTableStockage(tableStockage);
     assertEquals(3, modifTableStockage.getNbLignes());
     assertEquals(2, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     String nomNouvelleColonne = "Test";
     Class<?> type = String.class;
     TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
     assertEquals(3, result.getColumnCount());
     assertEquals("Test", result.getColumnName(2));
 }

 @Test
 public void testAjouterLigne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     TableStockage result = modifTableStockage.ajouterLigne();
     assertEquals(4, result.getRowCount());
     assertEquals("", result.getValueAt(3, 0).toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWhenNoColumn() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.supprimerToutesLesColonnes();
     modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Test1", Double.class);
     modifTableStockage.ajouterColonne("Test2", Boolean.class);
     TableStockage result = modifTableStockage.supprimerColonne(1);
     assertEquals(2, result.getColumnCount());
     assertFalse(result.getColumnName(1).equals("Test2"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWhenLastColumn() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Test1", Double.class);
     modifTableStockage.ajouterColonne("Test2", Boolean.class);
     modifTableStockage.supprimerColonne(2);
 }

 @Test
 public void testSupprimerLigne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Test1", Double.class);
     modifTableStockage.ajouterColonne("Test2", Boolean.class);
     modifTableStockage.ajouterLigne();
     TableStockage result = modifTableStockage.supprimerLigne(0);
     assertEquals(3, result.getRowCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWhenLastLine() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Test1", Double.class);
     modifTableStockage.ajouterColonne("Test2", Boolean.class);
     modifTableStockage.ajouterLigne();
     modifTableStockage.ajouterLigne();
     modifTableStockage.supprimerLigne(3);
 }

 @Test
 public void testSeparerColonne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Test1 Test2", String.class);
     TableStockage result = modifTableStockage.separerColonne(0);
     assertEquals(3, result.getColumnCount());
     assertEquals("Test1", result.getColumnName(0));
     assertEquals("Test2", result.getColumnName(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWhenNotStringType() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne(1.0, Double.class);
     modifTableStockage.separerColonne(0);
 }

 @Test
 public void testFusionnerColonnes() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Test1", Boolean.class);
     modifTableStockage.ajouterColonne("Test2", Boolean.class);
     TableStockage result = modifTableStockage.fusionnerColonnes(0);
     assertEquals(2, result.getColumnCount());
     assertEquals("Test1 Test2", result.getColumnName(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWhenNotBooleanType() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Test1", Double.class);
     modifTableStockage.ajouterColonne("Test2", Integer.class);
     modifTableStockage.fusionnerColonnes(0);
 }

 @Test
 public void testColonneMail() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     TableStockage result = modifTableStockage.colonneMail();
     assertEquals(4, result.getRowCount());
     assertTrue(result.getValueAt(0, 3).toString().equals("true"));
 }

 @Test
 public void testSelectMail() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Test1", Double.class);
     modifTableStockage.ajouterColonne("Test2", Boolean.class);
     modifTableStockage.ajouterColonne("Envoyer E-Mail a:", Boolean.class);
     modifTableStockage.ajouterLigne();
     modifTableStockage.ajouterLigne();
     modifTableStockage.setValueAt(true, 0, 2);
     modifTableStockage.setValueAt(true, 1, 2);
     TableStockage result = modifTableStockage.selectMail();
     assertEquals(2, result.getRowCount());
     assertTrue(result.getValueAt(0, 0).equals(tableStockage.getValueAt(0, 0)));
     assertTrue(result.getValueAt(1, 0).equals(tableStockage.getValueAt(1, 0)));
 }

}