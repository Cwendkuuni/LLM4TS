package fr.unice.gfarce.interGraph;

import org.junit.Test;
import static org.junit.Assert.*;

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
 public void testAjouterColonneStringClass() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 2));
 String nomNouvelleColonne = "New Column";
 Class<?> type = String.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(3, tableS.getColumnCount());
 assertEquals(nomNouvelleColonne, tableS.getColumnName(2));
 }

 @Test
 public void testAjouterColonneDoubleClass() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 2));
 String nomNouvelleColonne = "New Column";
 Class<?> type = Double.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(3, tableS.getColumnCount());
 assertEquals(nomNouvelleColonne, tableS.getColumnName(2));
 }

 @Test
 public void testAjouterColonneBooleanClassWithOneColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(1, 0));
 String nomNouvelleColonne = "New Column";
 Class<?> type = Boolean.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(1, tableS.getColumnCount());
 assertEquals(nomNouvelleColonne, tableS.getColumnName(0));
 }

 @Test
 public void testAjouterColonneBooleanClassWithTwoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 2));
 String nomNouvelleColonne = "New Column";
 Class<?> type = Boolean.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(3, tableS.getColumnCount());
 assertEquals(nomNouvelleColonne, tableS.getColumnName(2));
 }

 @Test
 public void testAjouterLigneWithZeroColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertEquals(0, tableS.getRowCount());
 }

 @Test
 public void testAjouterLigneWithTwoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertEquals(3, tableS.getRowCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterLigneWithNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 TableStockage tableS = modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonneWithZeroColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerColonne(indice);
 assertEquals(0, tableS.getColumnCount());
 }

 @Test
 public void testSupprimerColonneWithOneColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(1, 1));
 int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerColonne(indice);
 assertEquals(0, tableS.getColumnCount());
 }

 @Test
 public void testSupprimerColonneWithTwoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 int indice = 1;
 TableStockage tableS = modifTableStockage.supprimerColonne(indice);
 assertEquals(1, tableS.getColumnCount());
 }

 @Test
 public void testSupprimerLigneWithZeroRows() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerLigne(indice);
 assertEquals(0, tableS.getRowCount());
 }

 @Test
 public void testSupprimerLigneWithOneRow() {
 modifTableStockage = new ModifTableStockage(new TableStockage(1, 1));
 int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerLigne(indice);
 assertEquals(0, tableS.getRowCount());
 }

 @Test
 public void testSupprimerLigneWithTwoRows() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 int indice = 1;
 TableStockage tableS = modifTableStockage.supprimerLigne(indice);
 assertEquals(1, tableS.getRowCount());
 }

 @Test
 public void testSeparerColonneWithOneColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(1, 1));
 int indice = 0;
 TableStockage tableS = modifTableStockage.separerColonne(indice);
 assertEquals(1, tableS.getColumnCount());
 }

 @Test
 public void testSeparerColonneWithTwoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 int indice = 1;
 TableStockage tableS = modifTableStockage.separerColonne(indice);
 assertEquals(2, tableS.getColumnCount());
 }

 @Test
 public void testSeparerColonneWithThreeColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 3));
 int indice = 1;
 TableStockage tableS = modifTableStockage.separerColonne(indice);
 assertEquals(3, tableS.getColumnCount());
 }

 @Test
 public void testFusionnerColonnesWithOneColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 1));
 int indice = 0;
 TableStockage tableS = modifTableStockage.fusionnerColonnes(indice);
 assertEquals(1, tableS.getColumnCount());
 }

 @Test
 public void testFusionnerColonnesWithTwoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 int indice = 0;
 TableStockage tableS = modifTableStockage.fusionnerColonnes(indice);
 assertEquals(1, tableS.getColumnCount());
 }

 @Test
 public void testFusionnerColonnesWithStringAndBooleanColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(2, 2));
 int indice = 0;
 TableStockage tableS = modifTableStockage.fusionnerColonnes(indice);
 assertEquals(1, tableS.getColumnCount());
 }

 @Test
 public void testColonneMailWithZeroColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 TableStockage tableS = modifTableStockage.colonneMail();
 assertEquals(0, tableS.getColumnCount());
 }

 @Test
 public void testColonneMailWithOneColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(1, 1));
 TableStockage tableS = modifTableStockage.colonneMail();
 assertEquals(2, tableS.getColumnCount());
 }

 @Test
 public void testSelectMailWithZeroRows() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 2));
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(0, tableS.getRowCount());
 }

 @Test
 public void testSelectMailWithOneRow() {
 modifTableStockage = new ModifTableStockage(new TableStockage(1, 2));
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(1, tableS.getRowCount());
 }

 @Test
 public void testSelectMailWithMultipleRows() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 2));
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(2, tableS.getColumnCount());
 assertEquals(1, tableS.getRowCount());
 }

 @Test
 public void testSelectMailWithNoCheckbox() {
 modifTableStockage = new ModifTableStockage(new TableStockage(3, 3));
 TableStockage tableS = modifTableStockage.selectMail();
 assertEquals(2, tableS.getColumnCount());
 assertEquals(0, tableS.getRowCount());
 }
}