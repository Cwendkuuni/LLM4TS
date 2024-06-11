package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.TableModel;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Test
 public void testModifTableStockageConstruction() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
 assertEquals(5, modifTableStockage.getNbLignes());
 assertEquals(5, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonneStringClass() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
 tableStockage = modifTableStockage.ajouterColonne("New Column", String.class);
 assertEquals(6, modifTableStockage.getNbColonnes());
 assertEquals("New Column", tableStockage.getColumnName(5));
 }

 @Test
 public void testAjouterColonneClassException() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
 try {
 modifTableStockage.ajouterColonne("New Column", Integer.class);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 assertEquals(5, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterLigne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
 tableStockage = modifTableStockage.ajouterLigne();
 assertEquals(6, modifTableStockage.getNbLignes());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 0));
 modifTableStockage.ajouterLigne();
 fail("Should throw an IllegalStateException");
 }

 @Test
 public void testSupprimerColonne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(6, 6));
 TableStockage tableS = modifTableStockage.supprimerColonne(5);
 assertEquals(5, modifTableStockage.getNbColonnes());
 assertEquals("Column5", tableS.getColumnName(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneOutOfBounds() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
 modifTableStockage.supprimerColonne(6);
 fail("Should throw an IndexOutOfBoundsException");
 }

 @Test
 public void testSupprimerLigne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(6, 6));
 TableStockage tableS = modifTableStockage.supprimerLigne(5);
 assertEquals(5, modifTableStockage.getNbLignes());
 assertEquals(TableStockage.class, tableS.getClass());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneOutOfBounds() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
 modifTableStockage.supprimerLigne(6);
 fail("Should throw an IndexOutOfBoundsException");
 }

 @Test
 public void testSeparerColonne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(6, 6));
 modifTableStockage.separerColonne(2);
 assertEquals(7, modifTableStockage.getNbColonnes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidType() {
 modifTableStockage = new ModifTableStockage(new TableStockage(6, 6));
 modifTableStockage.separerColonne(2);
 try {
 modifTableStockage.separerColonne(2);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 assertEquals(7, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testFusionnerColonnes() {
 modifTableStockage = new ModifTableStockage(new TableStockage(6, 6));
 modifTableStockage.fusionnerColonnes(2);
 assertEquals(5, modifTableStockage.getNbColonnes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidTypes() {
 modifTableStockage = new ModifTableStockage(new TableStockage(6, 6));
 modifTableStockage.fusionnerColonnes(2);
 try {
 modifTableStockage.fusionnerColonnes(2);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 assertEquals(5, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testColonneMail() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
 TableStockage tableS = modifTableStockage.colonneMail();
 assertEquals("Envoyer E-Mail a:", tableS.getColumnName(5));
 }

 @Test
 public void testSelectMail() {
 modifTableStockage = new ModifTableStockage(new TableStockage(6, 6));
 modifTableStockage.setValueAt(true, 0, 5);
 modifTableStockage.setValueAt(true, 1, 5);
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(3, tableS.getRowCount());
 }

 @Test
 public void testSelectMailNoSelectedRows() {
 modifTableStockage = new ModifTableStockage(new TableStockage(6, 6));
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(0, tableS.getRowCount());
 }
}