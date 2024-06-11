package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

public class ModifTableStockageTest {

 private TableModel ts;
 private ModifTableStockage modifTableStockage;

 @Test
 public void testModifTableStockageConstruction() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
     assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
 }

 @Test
 public void testAjouterColonne() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("NouvelleColonne", String.class);
     assertEquals(modifTableStockage.getNbColonnes(), nueTable.getColumnCount());
     assertEquals("NouvelleColonne", nueTable.getColumnName(modifTableStockage.getNbColonnes() - 1));
 }

 @Test
 public void testAjouterLigne() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterLigne();
     assertEquals(modifTableStockage.getNbLignes(), nueTable.getRowCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWhenNoColumn() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     modifTableStockage.ajouterLigne();
     modifTableStockage.supprimerColonne(0);
     modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Colonne1", String.class);
     nueTable = modifTableStockage.ajouterColonne("Colonne2", String.class);
     nueTable = modifTableStockage.ajouterColonne("Colonne3", String.class);
     nueTable = modifTableStockage.supprimerColonne(1);
     assertEquals(2, nueTable.getColumnCount());
     assertNotEquals("Colonne2", nueTable.getColumnName(1));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testSupprimerColonneWhenLastColumn() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Colonne1", String.class);
     nueTable = modifTableStockage.ajouterColonne("Colonne2", String.class);
     nueTable = modifTableStockage.supprimerColonne(nueTable.getColumnCount() - 1);
 }

 @Test
 public void testSupprimerLigne() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Colonne1", String.class);
     nueTable = modifTableStockage.ajouterColonne("Colonne2", String.class);
     nueTable = modifTableStockage.ajouterLigne();
     nueTable = modifTableStockage.ajouterLigne();
     nueTable = modifTableStockage.supprimerLigne(1);
     assertEquals(3, nueTable.getRowCount());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testSupprimerLigneWhenLastLine() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Colonne1", String.class);
     nueTable = modifTableStockage.ajouterColonne("Colonne2", String.class);
     nueTable = modifTableStockage.ajouterLigne();
     nueTable = modifTableStockage.ajouterLigne();
     nueTable = modifTableStockage.supprimerLigne(nueTable.getRowCount() - 1);
 }

 @Test
 public void testSeparerColonne() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Separer1 Separer2", String.class);
     nueTable = modifTableStockage.separerColonne(0);
     assertEquals(3, nueTable.getColumnCount());
     assertEquals("Separer1", nueTable.getColumnName(0));
     assertEquals("Separer2", nueTable.getColumnName(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWhenNotString() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Separer1 0", Double.class);
     nueTable = modifTableStockage.separerColonne(0);
 }

 @Test
 public void testFusionnerColonnes() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Colonne1", String.class);
     nueTable = modifTableStockage.ajouterColonne("Colonne2", String.class);
     nueTable = modifTableStockage.fusionnerColonnes(1);
     assertEquals(2, nueTable.getColumnCount());
     assertEquals("Colonne1 Concat Colonne2", nueTable.getColumnName(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWhenNotString() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Colonne1", Boolean.class);
     nueTable = modifTableStockage.ajouterColonne("Colonne2", Boolean.class);
     nueTable = modifTableStockage.fusionnerColonnes(1);
 }

 @Test
 public void testColonneMail() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.colonneMail();
     assertEquals(ts.getRowCount() + 1, nueTable.getRowCount());
     assertEquals(ts.getColumnCount() + 1, nueTable.getColumnCount());
     assertEquals("Envoyer E-Mail a:", nueTable.getColumnName(nueTable.getColumnCount() - 1));
 }

 @Test
 public void testSelectMail() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Colonne1", String.class);
     nueTable = modifTableStockage.ajouterColonne("Colonne2", String.class);
     nueTable = modifTableStockage.ajouterColonne("Envoyer E-Mail a:", Boolean.class);
     for (int i = 0; i < nueTable.getRowCount(); ++i) {
         nueTable.setValueAt(true, i, nueTable.getColumnCount() - 1);
     }
     TableModel selectedMail = modifTableStockage.selectMail();
     assertEquals(nueTable.getRowCount(), selectedMail.getRowCount());
     assertEquals(nueTable.getColumnCount() - 1, selectedMail.getColumnCount());
 }

 @Test
 public void testSelectMailWhenNoMailToSend() {
     ts = new DefaultTableModel();
     modifTableStockage = new ModifTableStockage(ts);
     TableModel nueTable = modifTableStockage.ajouterColonne("Colonne1", String.class);
     nueTable = modifTableStockage.ajouterColonne("Colonne2", String.class);
     nueTable = modifTableStockage.ajouterColonne("Envoyer E-Mail a:", Boolean.class);
     for (int i = 0; i < nueTable.getRowCount(); ++i) {
         nueTable.setValueAt(false, i, nueTable.getColumnCount() - 1);
     }
     TableModel selectedMail = modifTableStockage.selectMail();
     assertEquals(0, selectedMail.getRowCount());
 }

}