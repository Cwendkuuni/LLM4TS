package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableModel;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private TableStockage initTableStockage() {
 final TableModel model = new Object[][] { { "A1", "1", "2" }, { "B1", "3", "4" }, { "C1", "5", "6" } };
 return new TableStockage(model);
 }

 @Test
 public void testModifTableStockageConstruction() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 assertEquals(ts.getColumnCount(), modifTableStockage.nbColonnes);
 assertEquals(ts.getRowCount(), modifTableStockage.nbLignes);
 }

 @Test
 public void testAjouterColonne() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 String nomNouvelleColonne = "newColumn";
 Class<?> type = String.class;
 TableStockage tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(modifTableStockage.nbColonnes, tableS.getColumnCount());
 assertEquals(modifTableStockage.nbLignes, tableS.getRowCount());
 assertEquals(nomNouvelleColonne, tableS.getColumnName(modifTableStockage.nbColonnes - 1));
 }

 @Test
 public void testAjouterLigne() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 TableStockage tableS = modifTableStockage.ajouterLigne();
 assertEquals(modifTableStockage.nbLignes, tableS.getRowCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterLigneWhenNoColumn() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 modifTableStockage.nbColonnes = 0;
 modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerColonne(indice);
 assertEquals(modifTableStockage.nbColonnes - 1, tableS.getColumnCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWhenIndexOutOfBounds() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 int indice = ts.getColumnCount();
 modifTableStockage.supprimerColonne(indice);
 }

 @Test
 public void testSupprimerLigne() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 int indice = 0;
 TableStockage tableS = modifTableStockage.supprimerLigne(indice);
 assertEquals(modifTableStockage.nbLignes - 1, tableS.getRowCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWhenIndexOutOfBounds() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 int indice = ts.getRowCount();
 modifTableStockage.supprimerLigne(indice);
 }

 @Test
 public void testSeparerColonne() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 int indice = 0;
 TableStockage tableS = modifTableStockage.separerColonne(indice);
 assertEquals(modifTableStockage.nbColonnes + 1, tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWhenColumnTypeIsNotString() {
 TableStockage ts = new TableStockage(new Object[][] { { 1, 2.0 }, { 3, 4.0 }, { 5, 6.0 } });
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 int indice = 0;
 modifTableStockage.separerColonne(indice);
 }

 @Test
 public void testFusionnerColonnes() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 int indice = 1;
 TableStockage tableS = modifTableStockage.fusionnerColonnes(indice);
 assertEquals(modifTableStockage.nbColonnes - 1, tableS.getColumnCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWhenColumnsTypesAreNotBoolean() {
 TableStockage ts = initTableStockage();
 ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
 int indice = 0;
 modifTableStockage.fusionnerColonnes(indice);
 }
}