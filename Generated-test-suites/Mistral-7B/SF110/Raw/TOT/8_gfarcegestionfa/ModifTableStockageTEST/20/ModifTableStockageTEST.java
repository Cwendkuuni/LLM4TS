package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage originalTable;

 @Before
 public void setUp() {
 originalTable = new TableStockage(5, 2);
 originalTable.setValueAt("John Doe", 0, 0);
 originalTable.setValueAt("30", 0, 1);
 originalTable.setValueAt("Jane Doe", 1, 0);
 originalTable.setValueAt("25", 1, 1);
 modifTableStockage = new ModifTableStockage(originalTable);
 }

 @Test
 public void testAjouterColonneString() {
 TableStockage table = modifTableStockage.ajouterColonne("New Column", String.class);
 assertEquals(table.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
 assertEquals(table.getColumnName(modifTableStockage.getNbColonnes() - 1), "New Column");
 }

 @Test
 public void testAjouterColonneDouble() {
 TableStockage table = modifTableStockage.ajouterColonne("New Column", Double.class);
 assertEquals(table.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
 assertEquals(table.getColumnClass(modifTableStockage.getNbColonnes() - 1), Double.class);
 }

 @Test
 public void testAjouterColonneBoolean() {
 TableStockage table = modifTableStockage.ajouterColonne("New Column", Boolean.class);
 assertEquals(table.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
 assertEquals(table.getColumnClass(modifTableStockage.getNbColonnes() - 1), Boolean.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidClass() {
 modifTableStockage.ajouterColonne("New Column", Integer.class);
 }

 @Test
 public void testAjouterLigne() {
 TableStockage table = modifTableStockage.ajouterLigne();
 assertEquals(table.getRowCount(), modifTableStockage.getNbLignes() + 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonneValidIndex() {
 TableStockage table = modifTableStockage.supprimerColonne(0);
 assertEquals(table.getColumnCount(), modifTableStockage.getNbColonnes() - 1);
 assertNotEquals(table.getColumnName(0), "Envoyer E-Mail a:");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 2));
 modifTableStockage.supprimerColonne(5);
 }

 @Test
 public void testSupprimerLigneValidIndex() {
 TableStockage table = modifTableStockage.supprimerLigne(0);
 assertEquals(table.getRowCount(), modifTableStockage.getNbLignes() - 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableModel() {
 @Override public int getRowCount() {
 return 5;
 }

 @Override public int getColumnCount() {
 return 2;
 }
 });
 modifTableStockage.supprimerLigne(5);
 }

 @Test
 public void testSeparerColonneValidIndex() {
 TableStockage table = modifTableStockage.separerColonne(0);
 assertEquals(table.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
 assertNotEquals(table.getColumnName(0), "Envoyer E-Mail a:");
 assertNotEquals(table.getColumnName(1), "Envoyer E-Mail a:");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSeparerColonneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableModel() {
 @Override public int getRowCount() {
 return 5;
 }

 @Override public int getColumnCount() {
 return 2;
 }
 });
 modifTableStockage.separerColonne(2);
 }

 @Test
 public void testFusionnerColonnesValidIndex() {
 TableStockage table = modifTableStockage.fusionnerColonnes(1);
 assertEquals(table.getColumnCount(), modifTableStockage.getNbColonnes() - 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFusionnerColonnesInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableModel() {
 @Override public int getRowCount() {
 return 5;
 }

 @Override public int getColumnCount() {
 return 2;
 }
 });
 modifTableStockage.fusionnerColonnes(2);
 }

 @Test
 public void testColonneMail() {
 TableStockage table = modifTableStockage.colonneMail();
 assertEquals(table.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
 assertEquals(table.getColumnName(modifTableStockage.getNbColonnes() - 1), "Envoyer E-Mail a:");
 }

 @Test
 public void testSelectMail() {
 TableStockage table = modifTableStockage.selectMail();
 assertEquals(table.getRowCount(), 2);
 }
}