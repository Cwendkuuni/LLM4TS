package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private JTable table;

 @Before
 public void setup() {
 Object[][] data = {{"A1", "1"}, {"B1", "2.1"}, {"C1", "3"}, {"D1", "4.2"}};
 DefaultTableModel model = new DefaultTableModel(data, new String[]{"Col1", "Col2"});
 table = new JTable(model);
 modifTableStockage = new ModifTableStockage(table);
 }

 @Test
 public void testAjouterColonne() {
 String newColumnName = "Col3";
 Class<?> newColumnType = Double.class;
 TableStockage result = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
 assertEquals(3, result.getColumnCount());
 assertEquals(newColumnName, result.getColumnName(2));
 assertEquals(Double.class, result.getColumnClass(2));
 Arrays.stream(result.getValueAtAll()).forEach(e -> assertNull(e));
 }

 @Test
 public void testAjouterColonneWithSameName() {
 String newColumnName = "Col1";
 Class<?> newColumnType = Integer.class;
 modifTableStockage = new ModifTableStockage(table); // Reset table
 TableStockage result = modifTableStockage.ajouterColonne(newColumnName, newColumnType);
 assertEquals(1, result.getColumnCount());
 JOptionPane.showMessageDialog(null, "ERREUR: Meme nom de colonne", "Erreur", JOptionPane.ERROR_MESSAGE);
 }

 @Test
 public void testAjouterColonneWithNullName() {
 Class<?> newColumnType = Integer.class;
 modifTableStockage = new ModifTableStockage(table); // Reset table
 assertThrows(IllegalArgumentException.class, () -> modifTableStockage.ajouterColonne(null, newColumnType));
 }

 @Test
 public void testAjouterColonneWithNullType() {
 String newColumnName = "Col3";
 modifTableStockage = new ModifTableStockage(table); // Reset table
 assertThrows(IllegalArgumentException.class, () -> modifTableStockage.ajouterColonne(newColumnName, null));
 }

 @Test
 public void testAjouterLigne() {
 TableStockage result = modifTableStockage.ajouterLigne();
 assertEquals(5, result.getRowCount());
 assertEquals("", result.getValueAt(4, 0));
 assertEquals(null, result.getValueAt(4, 1));
 assertEquals(null, result.getValueAt(4, 2));
 }

 @Test
 public void testAjouterLigneWithNoColumns() {
 modifTableStockage = new ModifTableStockage(table); // Reset table
 JOptionPane.showMessageDialog(null, "ERREUR: Pas de colonnes", "Erreur", JOptionPane.ERROR_MESSAGE);
 }

 @Test
 public void testSupprimerColonne() {
 int columnIndex = 0;
 TableStockage result = modifTableStockage.supprimerColonne(columnIndex);
 assertEquals(2, result.getColumnCount());
 assertEquals("Col1", result.getColumnName(0));
 assertEquals("Col2", result.getColumnName(1));
 Arrays.stream(result.getValueAtAll()).forEach(e -> assertNull(e));
 }

 @Test
 public void testSupprimerColonneWithInvalidIndex() {
 int columnIndex = 3;
 modifTableStockage = new ModifTableStockage(table); // Reset table
 JOptionPane.showMessageDialog(null, "ERREUR: Indice de colonne invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
 }

 @Test
 public void testSupprimerLigne() {
 int rowIndex = 0;
 TableStockage result = modifTableStockage.supprimerLigne(rowIndex);
 assertEquals(3, result.getRowCount());
 assertEquals("", result.getValueAt(1, 0));
 assertEquals("1", result.getValueAt(1, 1));
 assertEquals("3", result.getValueAt(1, 2));
 }

 @Test
 public void testSupprimerLigneWithInvalidIndex() {
 int rowIndex = 4;
 modifTableStockage = new ModifTableStockage(table); // Reset table
 JOptionPane.showMessageDialog(null, "ERREUR: Indice de ligne invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
 }

 @Test
 public void testSeparerColonne() {
 int columnIndex = 0;
 TableStockage result = modifTableStockage.separerColonne(columnIndex);
 assertEquals(4, result.getColumnCount());
 assertEquals("Col1", result.getColumnName(0));
 assertEquals("Col2", result.getColumnName(1));
 assertEquals("", result.getColumnName(2));
 assertEquals("", result.getColumnName(3));
 Arrays.stream(result.getValueAtAll()).forEach(e -> assertNull(e));
 }

 @Test
 public void testSeparerColonneWithInvalidIndex() {
 int columnIndex = 3;
 modifTableStockage = new ModifTableStockage(table); // Reset table
 JOptionPane.showMessageDialog(null, "ERREUR: Indice de colonne invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
 }

 @Test
 public void testSeparerColonneWithStringType() {
 int columnIndex = 1;
 String cellValue = "A B";
 modifTableStockage = new ModifTableStockage(table); // Reset table
 modifTableStockage.setValueAt(cellValue, 0, columnIndex);
 TableStockage result = modifTableStockage.separerColonne(columnIndex);
 assertEquals(3, result.getColumnCount());
 assertEquals("Col1", result.getColumnName(0));
 assertEquals("Col2", result.getColumnName(1));
 assertEquals("", result.getColumnName(2));
 assertEquals("", result.getValueAt(0, 2));
 assertEquals("A", result.getValueAt(0, 1));
 assertEquals("B", result.getValueAt(0, 2));
 }

 @Test
 public void testFusionnerColonnes() {
 int firstColumnIndex = 1;
 int secondColumnIndex = 2;
 TableStockage result = modifTableStockage.fusionnerColonnes(firstColumnIndex);
 assertEquals(2, result.getColumnCount());
 assertEquals("Col1 Col2", result.getColumnName(0));
 assertEquals(Arrays.asList("1 2.1", "2 3", "3 4.2"), Arrays.asList(Arrays.stream(result.getValueAtAll()).map(e -> e.toString()).toArray()));
 }

 @Test
 public void testFusionnerColonnesWithInvalidFirstIndex() {
 int firstColumnIndex = -1;
 modifTableStockage = new ModifTableStockage(table); // Reset table
 JOptionPane.showMessageDialog(null, "ERREUR: Indice de colonne invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
 }

 @Test
 public void testFusionnerColonnesWithInvalidSecondIndex() {
 int secondColumnIndex = 3;
 modifTableStockage = new ModifTableStockage(table); // Reset table
 JOptionPane.showMessageDialog(null, "ERREUR: Indice de colonne invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
 }

 @Test
 public void testColonneMail() {
 TableStockage result = modifTableStockage.colonneMail();
 assertEquals(5, result.getRowCount());
 assertEquals(6, result.getColumnCount());
 assertEquals("Envoyer E-Mail a:", result.getColumnName(5));
 Arrays.stream(result.getValueAtAll()).forEach(e -> assertNull(e));
 }

 @Test
 public void testSelectMail() {
 int columnIndexToSelect = 5;
 TableStockage result = modifTableStockage.selectMail();
 assertEquals(4, result.getRowCount());
 assertEquals("", result.getValueAt(0, columnIndexToSelect));
 assertEquals(true, (boolean)result.getValueAt(1, columnIndexToSelect));
 assertEquals("", result.getValueAt(2, columnIndexToSelect));
 assertEquals("", result.getValueAt(3, columnIndexToSelect));
 }
}