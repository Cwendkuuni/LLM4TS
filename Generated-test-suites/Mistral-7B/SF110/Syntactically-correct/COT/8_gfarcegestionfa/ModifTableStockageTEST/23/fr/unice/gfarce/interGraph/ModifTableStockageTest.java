package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableModel tableModel;

 @Before
 public void setUp() {
 tableModel = new Object[][]{{"A1", "1"}, {"B1", "2.0"}, {"C1", new Boolean(false)}};
 modifTableStockage = new ModifTableStockage(new TableStockage(tableModel));
 }

 @Test
 public void testModifTableStockageConstructor() {
 assertEquals(3, modifTableStockage.getNbColonnes());
 assertEquals(3, modifTableStockage.getNbLignes());
 assertSame(tableModel, modifTableStockage.getTs());
 }

 @Test
 public void testAjouterColonneStringClass() {
 TableStockage result = modifTableStockage.ajouterColonne("NouvelleColonne", String.class);
 assertEquals(4, result.getColumnCount());
 assertEquals("NouvelleColonne", result.getColumnName(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidClass() {
 modifTableStockage.ajouterColonne("NouvelleColonne", Integer.class);
 }

 @Test
 public void testAjouterLigne() {
 TableStockage result = modifTableStockage.ajouterLigne();
 assertEquals(4, result.getRowCount());
 assertNull(result.getValueAt(3, 0));
 assertNull(result.getValueAt(3, 1));
 assertNull(result.getValueAt(3, 2));
 }

 @Test
 public void testSupprimerColonne() {
 TableStockage result = modifTableStockage.supprimerColonne(2);
 assertEquals(2, result.getColumnCount());
 assertNull(result.getValueAt(1, 1));
 }

 @Test
 public void testSupprimerLigne() {
 TableStockage result = modifTableStockage.supprimerLigne(1);
 assertEquals(2, result.getRowCount());
 assertNull(result.getValueAt(1, 0));
 }

 @Test
 public void testSeparerColonne() {
 TableStockage result = modifTableStockage.separerColonne(1);
 assertEquals(5, result.getRowCount());
 assertEquals(5, result.getColumnCount());
 assertEquals("A1", result.getColumnName(0));
 assertEquals("1", result.getColumnName(1));
 assertEquals("Column1", result.getColumnName(2));
 assertEquals("Column2", result.getColumnName(3));
 assertEquals("Column3", result.getColumnName(4));
 }

 @Test
 public void testFusionnerColonnes() {
 TableStockage result = modifTableStockage.fusionnerColonnes(1);
 assertEquals(3, result.getColumnCount());
 assertEquals("Column1 Column2", result.getColumnName(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndex() {
 modifTableStockage.fusionnerColonnes(-1);
 }

 @Test
 public void testColonneMail() {
 TableStockage result = modifTableStockage.colonneMail();
 assertEquals(4, result.getColumnCount());
 assertTrue(result.getValueAt(0, 3) instanceof Boolean);
 }

 @Test
 public void testSelectMail() {
 TableStockage result = modifTableStockage.selectMail();
 assertEquals(3, result.getRowCount());
 assertFalse(result.getValueAt(0, 3) instanceof Boolean);
 assertTrue(result.getValueAt(1, 3) instanceof Boolean);
 assertTrue(result.getValueAt(2, 3) instanceof Boolean);
 }

}