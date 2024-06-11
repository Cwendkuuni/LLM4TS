package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Test
 public void testModifTableStockageConstruction() {
     tableStockage = new TableStockage(3, 4);
     modifTableStockage = new ModifTableStockage(tableStockage);
     assertEquals(4, modifTableStockage.nbColonnes);
     assertEquals(3, modifTableStockage.nbLignes);
     assertEquals(tableStockage, modifTableStockage.ts);
 }

 @Test
 public void testAjouterColonne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("NouvelleColonne", String.class);
     TableStockage newTableStockage = modifTableStockage.getTableStockage();
     assertEquals(5, newTableStockage.getColumnCount());
     assertEquals("NouvelleColonne", newTableStockage.getColumnName(4));
 }

 @Test
 public void testAjouterLigne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     TableStockage newTableStockage = modifTableStockage.ajouterLigne();
     assertEquals(4, newTableStockage.getColumnCount());
     assertEquals(4, newTableStockage.getRowCount());
     for (int i = 0; i < 4; ++i) {
         for (int j = 0; j < 4; ++j) {
             if (i < 3 && j < 4) {
                 assertEquals(modifTableStockage.ts.getValueAt(i, j), newTableStockage.getValueAt(i, j));
             }
             else if (i == 3 && j < 4) {
                 assertEquals(null, newTableStockage.getValueAt(i, j));
             }
             else {
                 assertEquals(modifTableStockage.ts.getValueAt(2, j), newTableStockage.getValueAt(i, j));
             }
         }
     }
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWhenNoColumns() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.supprimerToutesLesColonnes();
     modifTableStockage.ajouterLigne();
 }

 @Test
 public void testSupprimerColonne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("NouvelleColonne1", String.class);
     modifTableStockage.ajouterColonne("NouvelleColonne2", String.class);
     modifTableStockage.supprimerColonne(1);
     TableStockage newTableStockage = modifTableStockage.getTableStockage();
     assertEquals(2, newTableStockage.getColumnCount());
     assertNotEquals("NouvelleColonne1", newTableStockage.getColumnName(1));
     assertNotEquals("NouvelleColonne2", newTableStockage.getColumnName(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWhenFirstColumn() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("NouvelleColonne1", String.class);
     modifTableStockage.supprimerColonne(0);
 }

 @Test
 public void testSupprimerLigne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterLigne();
     modifTableStockage.ajouterLigne();
     modifTableStockage.supprimerLigne(1);
     TableStockage newTableStockage = modifTableStockage.getTableStockage();
     assertEquals(4, newTableStockage.getRowCount());
     for (int i = 0; i < 4; ++i) {
         if (i < 1) {
             for (int j = 0; j < 4; ++j) {
                 assertEquals(modifTableStockage.ts.getValueAt(i, j), newTableStockage.getValueAt(i, j));
             }
         }
         else {
             for (int j = 0; j < 4; ++j) {
                 assertEquals(modifTableStockage.ts.getValueAt(i - 1, j), newTableStockage.getValueAt(i, j));
             }
         }
     }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWhenLastLine() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterLigne();
     modifTableStockage.supprimerDerniereLigne();
 }

 @Test
 public void testSeparerColonne() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Nom Prenom Numero", String.class);
     TableStockage newTableStockage = modifTableStockage.separerColonne(2);
     assertEquals(3, newTableStockage.getColumnCount());
     assertEquals("Nom", newTableStockage.getColumnName(0));
     assertEquals("Prenom", newTableStockage.getColumnName(1));
     assertEquals("Numero", newTableStockage.getColumnName(2));
     for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 3; ++j) {
             if (i < 3 && j < 3) {
                 assertEquals(modifTableStockage.ts.getValueAt(i, j), newTableStockage.getValueAt(i, j));
             }
             else if (i < 3 && j == 3) {
                 int k = (int)modifTableStockage.ts.getValueAt(i, j).charAt(0);
                 assertEquals(Character.toString((char)(k + 32)), newTableStockage.getValueAt(i, j).toString());
             }
             else if (i == 3 && j < 3) {
                 assertEquals(modifTableStockage.ts.getValueAt(i, j), newTableStockage.getValueAt(i, j));
             }
             else {
                 double d = (double)modifTableStockage.ts.getValueAt(i, j).toString().split(" ")[1].charAt(0);
                 assertEquals(d, Double.parseDouble(newTableStockage.getValueAt(i, j).toString()));
             }
         }
     }
 }

 @Test
 public void testFusionnerColonnes() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Nom", String.class);
     modifTableStockage.ajouterColonne("Prenom", String.class);
     TableStockage newTableStockage = modifTableStockage.fusionnerColonnes(1);
     assertEquals(2, newTableStockage.getColumnCount());
     assertEquals("Nom Prenom", newTableStockage.getColumnName(0));
     for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 2; ++j) {
             assertEquals(modifTableStockage.ts.getValueAt(i, j), newTableStockage.getValueAt(i, j));
         }
         String value = (String)modifTableStockage.ts.getValueAt(i, 1);
         String[] valueParts = value.split(" ");
         assertEquals(valueParts[0], newTableStockage.getValueAt(i, 0).toString().split(" ")[0]);
         assertEquals(valueParts[1], newTableStockage.getValueAt(i, 0).toString().split(" ")[1]);
     }
 }

 @Test
 public void testColonneMail() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     TableStockage newTableStockage = modifTableStockage.colonneMail();
     assertEquals(5, newTableStockage.getColumnCount());
     assertEquals("Envoyer E-Mail a:", newTableStockage.getColumnName(4));
     for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 5; ++j) {
             if (j < 4) {
                 assertEquals(modifTableStockage.ts.getValueAt(i, j), newTableStockage.getValueAt(i, j));
             }
             else {
                 assertEquals(true, newTableStockage.getValueAt(i, j));
             }
         }
     }
 }

 @Test
 public void testSelectMail() {
     modifTableStockage = new ModifTableStockage(tableStockage);
     modifTableStockage.ajouterColonne("Envoyer E-Mail a:", Boolean.class);
     modifTableStockage.ajouterLigne();
     modifTableStockage.ajouterLigne();
     modifTableStockage.ajouterLigne();
     modifTableStockage.setValueAt(true, 1, 4);
     modifTableStockage.setValueAt(true, 2, 4);
     TableStockage newTableStockage = modifTableStockage.selectMail();
     assertEquals(3, newTableStockage.getRowCount());
     for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 4; ++j) {
             if (i < 2) {
                 assertEquals(modifTableStockage.ts.getValueAt(i, j), newTableStockage.getValueAt(i, j));
             }
             else {
                 assertEquals(modifTableStockage.ts.getValueAt(i, j - 1), newTableStockage.getValueAt(i, j));
             }
         }
     }
 }

}