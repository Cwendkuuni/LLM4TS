import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

import java.awt.Component;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Before
 public void setUp() {
 tableStockage = new TableStockage(3, 3);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 tableStockage.setColumnName(2, "IsStudent");

 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @After
 public void tearDown() {
 modifTableStockage = null;
 tableStockage = null;
 }

 @Test
 public void testAjouterColonne() {
 ModifTableStockage newModifTableStockage = modifTableStockage.ajouterColonne("New Column", Integer.class);
 assertEquals(modifTableStockage.nbColonnes, 4);
 assertEquals(newModifTableStockage.getColumnName(3), "New Column");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithNullName() {
 ModifTableStockage newModifTableStockage = modifTableStockage.ajouterColonne(null, Integer.class);
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithNullType() {
 ModifTableStockage newModifTableStockage = modifTableStockage.ajouterColonne("New Column", null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testAjouterColonneWithStringType() {
 ModifTableStockage newModifTableStockage = modifTableStockage.ajouterColonne("New Column", String.class);
 assertEquals(newModifTableStockage.getColumnCount(), 4);
 assertEquals(newModifTableStockage.getColumnName(3), "New Column");
 }

 @Test
 public void testAjouterColonneWithDoubleType() {
 ModifTableStockage newModifTableStockage = modifTableStockage.ajouterColonne("New Column", Double.class);
 assertEquals(newModifTableStockage.getColumnCount(), 4);
 assertEquals(newModifTableStockage.getColumnName(3), "New Column");
 }

 @Test
 public void testAjouterColonneWithBooleanType() {
 ModifTableStockage newModifTableStockage = modifTableStockage.ajouterColonne("New Column", Boolean.class);
 assertEquals(newModifTableStockage.getColumnCount(), 4);
 assertEquals(newModifTableStockage.getColumnName(3), "New Column");
 }

 @Test
 public void testAjouterLigne() {
 TableStockage newTableStockage = modifTableStockage.ajouterLigne();
 assertEquals(newTableStockage.getRowCount(), modifTableStockage.nbLignes);
 }

 @Test
 public void testSupprimerColonne() {
 ModifTableStockage newModifTableStockage = modifTableStockage.supprimerColonne(0);
 assertEquals(newModifTableStockage.getColumnCount(), 2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithInvalidIndex() {
 ModifTableStockage newModifTableStockage = modifTableStockage.supprimerColonne(3);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSupprimerLigne() {
 TableStockage newTableStockage = modifTableStockage.supprimerLigne(0);
 assertEquals(newTableStockage.getRowCount(), modifTableStockage.nbLignes - 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithInvalidIndex() {
 TableStockage newTableStockage = modifTableStockage.supprimerLigne(3);
 fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSeparerColonne() {
 ModifTableStockage newModifTableStockage = modifTableStockage.separerColonne(0);
 assertEquals(newModifTableStockage.getColumnCount(), 3);
 assertEquals(newModifTableStockage.getColumnName(1), "Name");
 assertEquals(newModifTableStockage.getColumnName(2), "Age");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithInvalidIndex() {
 ModifTableStockage newModifTableStockage = modifTableStockage.separerColonne(3);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testFusionnerColonnes() {
 ModifTableStockage newModifTableStockage = modifTableStockage.fusionnerColonnes(1);
 assertEquals(newModifTableStockage.getColumnCount(), 2);
 assertEquals(newModifTableStockage.getColumnName(0), "Name Age");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithInvalidColumnIndex() {
 ModifTableStockage newModifTableStockage = modifTableStockage.fusionnerColonnes(3);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testColonneMail() {
 TableStockage newTableStockage = modifTableStockage.colonneMail();
 assertEquals(newTableStockage.getColumnCount(), modifTableStockage.getColumnCount() + 1);
 assertTrue(newTableStockage.getColumnClass(newTableStockage.getColumnCount() - 1).isAssignableFrom(Boolean.class));
 }

 @Test
 public void testSelectMail() {
 TableStockage newTableStockage = modifTableStockage.selectMail();
 assertEquals(newTableStockage.getRowCount(), modifTableStockage.getRowCount());
 assertTrue(newTableStockage instanceof TableStockage);
 }
}