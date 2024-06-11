package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private TableStockage ts;
 private ModifTableStockage modifTableStockage;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 ts = new TableStockage(2, 3);
 ts.setColumnName(0, "Name");
 ts.setColumnName(1, "Age");
 ts.setColumnName(2, "Gender");
 ts.setValueAt("John Doe", 0, 0);
 ts.setValueAt(23, 0, 1);
 ts.setValueAt("Male", 0, 2);
 ts.setValueAt("Jane Doe", 1, 0);
 ts.setValueAt(30, 1, 1);
 ts.setValueAt("Female", 1, 2);
 modifTableStockage = new ModifTableStockage(ts);
 }

 @Test
 public void testModifTableStockageConstructor() {
 assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
 assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonne() {
 final String nomNouvelleColonne = "NewColumn";
 final Class<?> type = String.class;
 TableStockage result = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
 assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 if (j != modifTableStockage.getNbColonnes() - 1) {
 assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
 }
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneNullType() {
 modifTableStockage.ajouterColonne(null, null);
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testAjouterLigne() {
 TableStockage result = modifTableStockage.ajouterLigne();
 assertEquals(modifTableStockage.getNbLignes() + 1, result.getRowCount());
 assertEquals(modifTableStockage.getColumnCount(), result.getColumnCount());
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 if (i != modifTableStockage.getNbLignes() - 2) {
 assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
 }
 }
 }
 }

 @Test
 public void testSupprimerColonne() {
 final int indice = 1;
 TableStockage result = modifTableStockage.supprimerColonne(indice);
 assertEquals(modifTableStockage.getNbColonnes() - 1, result.getColumnCount());
 assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes() - 1; ++j) {
 assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes());
 fail("An IndexOutOfBoundsException should have been thrown.");
 }

 @Test
 public void testSupprimerLigne() {
 final int indice = 0;
 TableStockage result = modifTableStockage.supprimerLigne(indice);
 assertEquals(modifTableStockage.getNbLignes() - 1, result.getRowCount());
 assertEquals(modifTableStockage.getColumnCount(), result.getColumnCount());
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 assertEquals(ts.getValueAt(i + 1, j), result.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes());
 fail("An IndexOutOfBoundsException should have been thrown.");
 }

 @Test
 public void testSeparerColonne() {
 final int indice = 1;
 TableStockage result = modifTableStockage.separerColonne(indice);
 assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
 assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 if (j != modifTableStockage.getNbColonnes() - 2) {
 assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
 }
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSeparerColonneInvalidIndex() {
 modifTableStockage.separerColonne(modifTableStockage.getNbColonnes());
 fail("An IndexOutOfBoundsException should have been thrown.");
 }

 @Test
 public void testFusionnerColonnes() {
 final int indice = 1;
 TableStockage result = modifTableStockage.fusionnerColonnes(indice);
 assertEquals(modifTableStockage.getNbColonnes() - 1, result.getColumnCount());
 assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes() - 1; ++j) {
 assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
 }
 assertEquals(ts.getValueAt(i, indice).toString() + " " + ts.getValueAt(i, indice + 1).toString(), result.getValueAt(i, indice).toString());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidIndex() {
 modifTableStockage.fusionnerColonnes(modifTableStockage.getNbColonnes());
 fail("An IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testColonneMail() {
 TableStockage result = modifTableStockage.colonneMail();
 assertEquals(modifTableStockage.getNbColonnes() + 1, result.getColumnCount());
 assertEquals(modifTableStockage.getNbLignes(), result.getRowCount());
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 if (j != modifTableStockage.getNbColonnes() - 1) {
 assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
 }
 else {
 assertEquals(Boolean.FALSE, result.getValueAt(i, j));
 }
 }
 }
 }

 @Test
 public void testSelectMail() {
 TableStockage result = modifTableStockage.selectMail();
 assertEquals(2, result.getRowCount());
 for (int i = 0; i < result.getRowCount(); ++i) {
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 assertEquals(ts.getValueAt(i, j), result.getValueAt(i, j));
 }
 }
 }

}