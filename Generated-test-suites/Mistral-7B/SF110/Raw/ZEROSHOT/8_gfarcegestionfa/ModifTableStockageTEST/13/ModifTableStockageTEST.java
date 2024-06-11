package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.TableModel;

import org.junit.Before;
import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Before
 public void setUp() {
 tableStockage = new TableStockage(2, 2);
 tableStockage.setColumnName(0, "Nom");
 tableStockage.setColumnName(1, "Age");
 tableStockage.setValueAt("Alice", 0, 0);
 tableStockage.setValueAt(25, 0, 1);
 tableStockage.setValueAt("Bob", 1, 0);
 tableStockage.setValueAt(30, 1, 1);
 modifTableStockage = new ModifTableStockage(tableStockage);
 }

 @Test
 public void testAjouterColonne() {
 TableStockage tableS = modifTableStockage.ajouterColonne("NouvelleColonne", String.class);
 assertEquals(tableS.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
 assertEquals(tableS.getColumnName(tableS.getColumnCount() - 1), "NouvelleColonne");
 }

 @Test
 public void testAjouterLigne() {
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertEquals(tableS.getRowCount(), modifTableStockage.getNbLignes() + 1);
 for (int i = 0; i < modifTableStockage.getNbColonnes(); ++i) {
 if (modifTableStockage.getColumnClass(i).equals(String.class)) {
 assertEquals("", tableS.getValueAt(modifTableStockage.getNbLignes() - 2, i));
 } else if (modifTableStockage.getColumnClass(i).equals(Double.class)) {
 assertEquals(0.0, tableS.getValueAt(modifTableStockage.getNbLignes() - 2, i), 0.0);
 } else {
 assertEquals(false, tableS.getValueAt(modifTableStockage.getNbLignes() - 2, i));
 }
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterColonneWithSameName() {
 modifTableStockage.ajouterColonne("Nom", String.class);
 modifTableStockage.ajouterColonne("Nom", String.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWithNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 modifTableStockage.ajouterColonne("ColonneB", Double.class);
 TableStockage tableS = modifTableStockage.supprimerColonne(1);
 assertEquals(tableS.getColumnCount(), modifTableStockage.getNbColonnes() - 1);
 assertNotEquals("ColonneB", tableS.getColumnName(tableS.getColumnCount() - 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithFirstIndex() {
 modifTableStockage.ajouterColonne("ColonneA", Double.class);
 modifTableStockage.supprimerColonne(0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithLastIndex() {
 modifTableStockage.ajouterColonne("ColonneC", Boolean.class);
 modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes());
 }

 @Test
 public void testSupprimerLigne() {
 modifTableStockage.ajouterLigne();
 TableStockage tableS = modifTableStockage.supprimerLigne(0);
 assertEquals(tableS.getRowCount(), modifTableStockage.getNbLignes() - 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithFirstIndex() {
 modifTableStockage.ajouterLigne();
 modifTableStockage.supprimerLigne(0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithLastIndex() {
 modifTableStockage.ajouterLigne();
 modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes() - 1);
 }

 @Test
 public void testSeparerColonne() {
 modifTableStockage.ajouterColonne("SeparableColumn", String.class);
 TableStockage tableS = modifTableStockage.separerColonne(1);
 assertEquals(tableS.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
 assertEquals(tableS.getValueAt(0, modifTableStockage.getNbColonnes() - 1).toString(), "Nom");
 assertEquals(tableS.getValueAt(0, modifTableStockage.getNbColonnes()).toString(), "Age");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithNoSpace() {
 modifTableStockage.ajouterColonne("NonSeparableColumn", String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithNoColumn() {
 modifTableStockage.ajouterColonne("SeparableColumn", String.class);
 modifTableStockage.separerColonne(modifTableStockage.getNbColonnes());
 }

 @Test
 public void testFusionnerColonnes() {
 modifTableStockage.ajouterColonne("ColumnA", Boolean.class);
 modifTableStockage.ajouterColonne("ColumnB", Boolean.class);
 TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
 assertEquals(tableS.getColumnCount(), modifTableStockage.getNbColonnes() - 1);
 assertEquals(tableS.getColumnName(0), "ColumnA ColumnB");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithNoBooleanColumns() {
 modifTableStockage.ajouterColonne("ColumnA", String.class);
 modifTableStockage.ajouterColonne("ColumnB", Double.class);
 modifTableStockage.fusionnerColonnes(1);
 }

 @Test
 public void testColonneMail() {
 TableStockage tableS = modifTableStockage.colonneMail();
 assertEquals(tableS.getColumnCount(), modifTableStockage.getNbColonnes() + 1);
 assertEquals(tableS.getColumnName(tableS.getColumnCount() - 1), "Envoyer E-Mail a:");
 for (int i = 0; i < tableS.getRowCount(); ++i) {
 assertEquals(true, tableS.getValueAt(i, tableS.getColumnCount() - 1));
 }
 }

 @Test
 public void testSelectMail() {
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(tableS.getRowCount(), modifTableStockage.getNbLignes());
 for (int i = 0; i < tableS.getRowCount(); ++i) {
 for (int j = 0; j < tableS.getColumnCount() - 1; ++j) {
 assertEquals(modifTableStockage.getValueAt(i, j), tableS.getValueAt(i, j));
 }
 }
 }
}