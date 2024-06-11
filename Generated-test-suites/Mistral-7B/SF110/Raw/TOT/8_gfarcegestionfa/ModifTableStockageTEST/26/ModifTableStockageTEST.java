package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Test;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Test
 public void testModifTableStockageConstruction() {
 TableStockage ts = new TableStockage(5, 3);
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 assertEquals(5, modifTableStockage.getNbLignes());
 assertEquals(3, modifTableStockage.getNbColonnes());
 }

 @Test
 public void testAjouterColonneString() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 assertEquals(3, modifTableStockage.ajouterColonne("Test", String.class).getNbColonnes());
 }

 @Test
 public void testAjouterColonneInvalidType() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 try {
 modifTableStockage.ajouterColonne("Test", Integer.class);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Type incorrect", e.getMessage());
 }
 }

 @Test
 public void testAjouterColonneWithData() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 2));
 Object[] data = {1.0, 2.0, "Test"};
 TableStockage result = modifTableStockage.ajouterColonne("Test", Double.class);
 for (int i = 0; i < data.length; ++i) {
 assertEquals(data[i], result.getValueAt(i, result.getColumnCount() - 1));
 }
 }

 @Test
 public void testAjouterLigne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 TableStockage result = modifTableStockage.ajouterLigne();
 assertEquals(3, result.getNbLignes());
 }

 @Test
 public void testAjouterLigneNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 0));
 TableStockage result = modifTableStockage.ajouterLigne();
 assertEquals(2, result.getNbLignes());
 }

 @Test
 public void testSupprimerColonne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 TableStockage result = modifTableStockage.supprimerColonne(1);
 assertEquals(2, result.getNbColonnes());
 assertEquals("Test1", result.getColumnName(0));
 assertEquals("Test2", result.getColumnName(1));
 }

 @Test
 public void testSupprimerColonneOutOfRange() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 try {
 modifTableStockage.supprimerColonne(4);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Indice incorrect", e.getMessage());
 }
 }

 @Test
 public void testSupprimerLigne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 TableStockage result = modifTableStockage.supprimerLigne(1);
 assertEquals(2, result.getNbLignes());
 }

 @Test
 public void testSupprimerLigneOutOfRange() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 try {
 modifTableStockage.supprimerLigne(4);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Indice incorrect", e.getMessage());
 }
 }

 @Test
 public void testSeparerColonne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 1));
 modifTableStockage.ajouterColonne("Test1 Test2", String.class);
 TableStockage result = modifTableStockage.separerColonne(0);
 assertEquals(3, result.getNbColonnes());
 assertEquals("Test1", result.getColumnName(0));
 assertEquals("Test2", result.getColumnName(1));
 }

 @Test
 public void testSeparerColonneInvalidType() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 1));
 try {
 modifTableStockage.separerColonne(0);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Type incorrect", e.getMessage());
 }
 }

 @Test
 public void testSeparerColonneNoSeparator() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 1));
 try {
 modifTableStockage.separerColonne(0);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Pas de séparateur", e.getMessage());
 }
 }

 @Test
 public void testFusionnerColonnes() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 2));
 modifTableStockage.ajouterColonne("Colonne1", Boolean.class);
 modifTableStockage.ajouterColonne("Colonne2", Boolean.class);
 TableStockage result = modifTableStockage.fusionnerColonnes(0);
 assertEquals(2, result.getNbColonnes());
 assertEquals("Colonne1 Colonne2", result.getColumnName(0));
 }

 @Test
 public void testFusionnerColonnesInvalidType() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 2));
 try {
 modifTableStockage.fusionnerColonnes(0);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Types incorrects", e.getMessage());
 }
 }

 @Test
 public void testColonneMail() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 TableStockage result = modifTableStockage.colonneMail();
 assertEquals(3, result.getNbColonnes());
 assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
 }

 @Test
 public void testSelectMail() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 modifTableStockage.ajouterColonne("Test", Boolean.class);
 for (int i = 0; i < 2; ++i) {
 modifTableStockage.setValueAt(true, i, modifTableStockage.getColumnCount() - 1);
 }
 TableStockage result = modifTableStockage.selectMail();
 assertEquals(2, result.getNbLignes());
 assertEquals(3, result.getNbColonnes());
 assertEquals(true, result.getValueAt(0, 2));
 assertEquals("Test1", result.getColumnName(0));
 assertEquals("Test2", result.getColumnName(1));
 assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
 }

}