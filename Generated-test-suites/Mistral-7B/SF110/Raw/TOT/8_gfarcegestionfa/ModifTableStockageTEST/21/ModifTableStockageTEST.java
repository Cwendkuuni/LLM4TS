package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Before
 public void setUp() {
 tableStockage = new TableStockage(3, 2);
 tableStockage.setColumnName(0, "Name");
 tableStockage.setColumnName(1, "Age");
 tableStockage.setValueAt("John", 0, 0);
 tableStockage.setValueAt(25, 0, 1);
 tableStockage.setValueAt("Jane", 1, 0);
 tableStockage.setValueAt(30, 1, 1);
 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @After
 public void tearDown() {
 modifTableStockage = null;
 tableStockage = null;
 }

 @Test
 public void testAjouterColonneStringType() {
 final String expectedNewColumnName = "NewColumnName";
 final Class<?> expectedNewColumnType = String.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(expectedNewColumnName, expectedNewColumnType);
 String[] expectedColumnNames = new String[]{"Name", "Age", expectedNewColumnName};
 assertArrayEquals(expectedColumnNames, tableS.getColumnNames());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullName() {
 modifTableStockage.ajouterColonne(null, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullType() {
 modifTableStockage.ajouterColonne("NewColumnName", null);
 }

 @Test
 public void testAjouterColonneDoubleType() {
 final String expectedNewColumnName = "NewColumnName";
 final Class<?> expectedNewColumnType = Double.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(expectedNewColumnName, expectedNewColumnType);
 String[] expectedColumnNames = new String[]{"Name", "Age", expectedNewColumnName};
 assertArrayEquals(expectedColumnNames, tableS.getColumnNames());
 for (int i = 0; i < tableS.getRowCount(); ++i) {
 final double expectedDefaultValue = 0.0;
 assertEquals(expectedDefaultValue, tableS.getValueAt(i, tableS.getColumnCount() - 1), 0.0);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneInvalidType() {
 modifTableStockage.ajouterColonne("NewColumnName", Boolean.class);
 }

 @Test
 public void testAjouterLigne() {
 TableStockage tableS = modifTableStockage.ajouterLigne();
 String[] expectedColumnNames = new String[]{"Name", "Age"};
 assertArrayEquals(expectedColumnNames, tableS.getColumnNames());
 for (int i = 0; i < tableS.getRowCount() - 1; ++i) {
 assertArrayEquals(new Object[]{tableStockage.getValueAt(i, 0), tableStockage.getValueAt(i, 1)}, tableS.getValueAt(i, 0), tableS.getValueAt(i, 1));
 }
 for (int i = 0; i < tableS.getColumnCount(); ++i) {
 if (i < 2) {
 assertArrayEquals(new Object[]{tableStockage.getValueAt(tableS.getRowCount() - 2, i)}, tableS.getValueAt(tableS.getRowCount() - 2, i), null);
 } else {
 assertNull(tableS.getValueAt(tableS.getRowCount() - 2, i));
 }
 }
 }

 @Test
 public void testSupprimerColonne() {
 modifTableStockage = new ModifTableStockage(modifTableStockage.supprimerColonne(1));
 String[] expectedColumnNames = new String[]{"Name"};
 assertArrayEquals(expectedColumnNames, modifTableStockage.getTableStockage().getColumnNames());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTableStockage.supprimerColonne(-1);
 }

 @Test
 public void testSupprimerLigne() {
 modifTableStockage = new ModifTableStockage(modifTableStockage.supprimerLigne(1));
 String[] expectedColumnNames = new String[]{"Name", "Age"};
 assertArrayEquals(expectedColumnNames, modifTableStockage.getTableStockage().getColumnNames());
 for (int i = 0; i < modifTableStockage.getTableStockage().getRowCount() - 1; ++i) {
 assertArrayEquals(new Object[]{tableStockage.getValueAt(i, 0), tableStockage.getValueAt(i, 1)}, modifTableStockage.getTableStockage().getValueAt(i, 0), modifTableStockage.getTableStockage().getValueAt(i, 1));
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTableStockage.supprimerLigne(-1);
 }

 @Test
 public void testSeparerColonne() {
 final int columnIndex = 0;
 modifTableStockage = new ModifTableStockage(modifTableStockage.separerColonne(columnIndex));
 String[] expectedColumnNames = new String[]{"Name", "NewColumnName"};
 assertArrayEquals(expectedColumnNames, modifTableStockage.getTableStockage().getColumnNames());
 String expectedValue1 = "John";
 String expectedValue2 = "Doe";
 for (int i = 0; i < modifTableStockage.getTableStockage().getRowCount(); ++i) {
 String value = (String)modifTableStockage.getTableStockage().getValueAt(i, columnIndex);
 String[] values = value.split(" ");
 assertEquals(expectedValue1, values[0]);
 assertEquals(expectedValue2, values[1]);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testSeparerColonneInvalidColumnType() {
 final int columnIndex = 0;
 modifTableStockage = new ModifTableStockage(modifTableStockage.separerColonne(columnIndex));
 modifTableStockage = new ModifTableStockage(modifTableStockage.separerColonne(columnIndex));
 }

 @Test
 public void testFusionnerColonnes() {
 final int columnIndex = 0;
 final int columnIndexToMerge = 1;
 modifTableStockage = new ModifTableStockage(modifTableStockage.fusionnerColonnes(columnIndexToMerge));
 String[] expectedColumnNames = new String[]{"Name", "Name NewColumnName"};
 assertArrayEquals(expectedColumnNames, modifTableStockage.getTableStockage().getColumnNames());
 for (int i = 0; i < modifTableStockage.getTableStockage().getRowCount(); ++i) {
 String name = (String)modifTableStockage.getTableStockage().getValueAt(i, columnIndex);
 String newNameAndColumn = (String)modifTableStockage.getTableStockage().getValueAt(i, columnIndex + 1);
 String[] names = name.split(" ");
 assertEquals(names[0], newNameAndColumn.substring(0, names[0].length()));
 assertEquals(names[1], newNameAndColumn.substring(names[0].length()));
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFusionnerColonnesInvalidColumnIndex() {
 modifTableStockage.fusionnerColonnes(0);
 }

 @Test(expected = IllegalStateException.class)
 public void testFusionnerColonnesInvalidColumnTypes() {
 modifTableStockage = new ModifTableStockage(modifTableStockage.ajouterColonne("Name2", Boolean.class));
 modifTableStockage = new ModifTableStockage(modifTableStockage.ajouterColonne("Name3", Boolean.class));
 modifTableStockage = new ModifTableStockage(modifTableStockage.fusionnerColonnes(1));
 }

 @Test
 public void testColonneMail() {
 modifTableStockage = modifTableStockage.colonneMail();
 String[] expectedColumnNames = new String[]{"Name", "Age", "Envoyer E-Mail a:"};
 assertArrayEquals(expectedColumnNames, modifTableStockage.getTableStockage().getColumnNames());
 for (int i = 0; i < modifTableStockage.getTableStockage().getRowCount(); ++i) {
 assertTrue((Boolean)modifTableStockage.getTableStockage().getValueAt(i, modifTableStockage.getTableStockage().getColumnCount() - 1));
 }
 }

 @Test
 public void testSelectMail() {
 modifTableStockage = modifTableStockage.colonneMail();
 TableStockage selectedTableStockage = modifTableStockage.selectMail();
 String[] expectedColumnNames = new String[]{"Name", "Age", "Envoyer E-Mail a:"};
 assertArrayEquals(expectedColumnNames, selectedTableStockage.getColumnNames());
 for (int i = 0; i < selectedTableStockage.getRowCount(); ++i) {
 String name = (String)selectedTableStockage.getValueAt(i, 0);
 assertEquals(name, modifTableStockage.getTableStockage().getValueAt(i, 0));
 }
 }

}