package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private JTable table;

 @Before
 public void setUp() {
 Object[][] data = { { "Colonne1", "Type1" }, { "Colonne2", "Type2" }, { "Colonne3", "Type3" } };
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 2));
 modifTableStockage.ajouterColonne("Colonne1", String.class);
 modifTableStockage.ajouterColonne("Colonne2", Double.class);
 modifTableStockage.ajouterColonne("Colonne3", Boolean.class);
 for (int i = 0; i < 3; ++i) {
 for (int j = 0; j < 2; ++j) {
 modifTableStockage.setValueAt("Value", i, j);
 }
 }
 table = modifTableStockage.getTable();
 }

 @Test
 public void testAjouterColonneStringType() {
 TableStockage expected = new TableStockage(3, 3);
 expected.setColumnName(2, "NouvelleColonne");
 TableStockage actual = modifTableStockage.ajouterColonne("NouvelleColonne", String.class);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullType() {
 modifTableStockage.ajouterColonne(null, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneSameTypeAlreadyExist() {
 modifTableStockage.ajouterColonne("Colonne1", String.class);
 TableStockage actual = modifTableStockage.ajouterColonne("Colonne1", String.class);
 assertSame(modifTableStockage.getTable(), actual.getTable());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testAjouterColonneNoSpaceLeft() {
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 modifTableStockage.setValueAt("Value", i, j);
 }
 }
 modifTableStockage.ajouterColonne("NewColumn", String.class);
 }

 @Test
 public void testAjouterLigne() {
 TableStockage expected = new TableStockage(4, 3);
 TableStockage actual = modifTableStockage.ajouterLigne();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 int columnToRemoveIndex = 1;
 TableStockage expected = new TableStockage(3, 1);
 TableStockage actual = modifTableStockage.supprimerColonne(columnToRemoveIndex);
 assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTableStockage.supprimerColonne(3);
 }

 @Test
 public void testSupprimerLigne() {
 int rowToRemoveIndex = 1;
 TableStockage expected = new TableStockage(2, 3);
 TableStockage actual = modifTableStockage.supprimerLigne(rowToRemoveIndex);
 assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTableStockage.supprimerLigne(3);
 }

 @Test
 public void testSeparerColonne() {
 int columnToSeparatorIndex = 0;
 TableStockage expected = new TableStockage(3, 3);
 TableStockage actual = modifTableStockage.separerColonne(columnToSeparatorIndex);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidColumnType() {
 modifTableStockage.separerColonne(1);
 }

 @Test
 public void testFusionnerColonnes() {
 int columnToFuseIndex = 1;
 TableStockage expected = new TableStockage(3, 2);
 TableStockage actual = modifTableStockage.fusionnerColonnes(columnToFuseIndex);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesDifferentTypes() {
 modifTableStockage.ajouterColonne("Column1", Boolean.class);
 modifTableStockage.ajouterColonne("Column2", Integer.class);
 modifTableStockage.fusionnerColonnes(1);
 }

 @Test
 public void testColonneMail() {
 TableStockage expected = new TableStockage(3, 3);
 expected.setColumnName(2, "Envoyer E-Mail a:");
 TableStockage actual = modifTableStockage.colonneMail();
 assertEquals(expected, actual);
 }

 @Test
 public void testSelectMail() {
 int columnToSelectIndex = 2;
 int numberOfSelectedRows = 2;
 Object[][] selectedRowsData = { { "Value1", "Value2", true }, { "Value3", "Value4", true } };
 TableStockage expected = new TableStockage(numberOfSelectedRows + 1, 3);
 for (int i = 0; i < numberOfSelectedRows; ++i) {
 for (int j = 0; j < 2; ++j) {
 expected.setValueAt("Value" + (i + 1), i, j);
 }
 expected.setValueAt(true, i, 2);
 }
 TableStockage actual = modifTableStockage.selectMail();
 for (int i = 0; i < numberOfSelectedRows; ++i) {
 for (int j = 0; j < 2; ++j) {
 assertEquals("Value" + (i + 1), actual.getValueAt(i, j));
 }
 }
 for (int i = 0; i < numberOfSelectedRows; ++i) {
 assertEquals(true, actual.getValueAt(i, 2));
 }
 }

 @Test
 public void testSelectMailEmptyTable() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.colonneMail();
 assertFalse(modifTableStockage.selectMail().isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testSelectMailNullTable() {
 TableStockage table = null;
 ModifTableStockage modifTableStockage = new ModifTableStockage(table);
 modifTableStockage.colonneMail();
 modifTableStockage.selectMail();
 }
}