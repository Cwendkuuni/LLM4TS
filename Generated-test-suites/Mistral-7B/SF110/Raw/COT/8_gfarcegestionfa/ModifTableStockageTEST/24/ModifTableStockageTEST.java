package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.table.TableStockage;

public class ModifTableStockageTest {

private ModifTableStockage modifTableStockage;
private TableStockage tableStockage;

@Test
public void testModifTableStockageConstuctor() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
assertEquals(5, modifTableStockage.getNbLignes());
assertEquals(5, modifTableStockage.getNbColonnes());
}

@Test
public void testAjouterColonne() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
tableStockage = modifTableStockage.ajouterColonne("test", String.class);
assertEquals(6, tableStockage.getColumnCount());
assertEquals("test", tableStockage.getColumnName(5));
}

@Test
public void testAjouterLigne() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
tableStockage = modifTableStockage.ajouterLigne();
assertEquals(6, tableStockage.getRowCount());
assertNull(tableStockage.getValueAt(5, 0));
}

@Test(expected = IllegalArgumentException.class)
public void testSupprimerColonneNegativeIndex() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
modifTableStockage.supprimerColonne(-1);
}

@Test(expected = IllegalArgumentException.class)
public void testSupprimerLigneNegativeIndex() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
modifTableStockage.supprimerLigne(-1);
}

@Test
public void testSupprimerColonneValidIndex() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
tableStockage = modifTableStockage.supprimerColonne(0);
assertEquals(4, tableStockage.getColumnCount());
}

@Test
public void testSupprimerLigneValidIndex() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
tableStockage = modifTableStockage.supprimerLigne(0);
assertEquals(4, tableStockage.getRowCount());
}

@Test
public void testSeparerColonne() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
modifTableStockage.ts.setValueAt("test test", 0, 2);
TableStockage tableS = modifTableStockage.separerColonne(2);
assertEquals(3, tableS.getColumnCount());
assertEquals("test", tableS.getColumnName(0));
assertEquals("test", tableS.getColumnName(1));
assertEquals("", tableS.getColumnName(2));
}

@Test(expected = IllegalArgumentException.class)
public void testSeparerColonneInvalidColumnIndex() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
modifTableStockage.ts.setValueAt("test test", 0, 5);
modifTableStockage.separerColonne(5);
}

@Test
public void testFusionnerColonnes() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
modifTableStockage.ts.setValueAt(true, 0, 0);
modifTableStockage.ts.setValueAt(true, 0, 1);
TableStockage tableS = modifTableStockage.fusionnerColonnes(0);
assertEquals(4, tableS.getColumnCount());
assertEquals("Envoyer E-Mail a: true, false", tableS.getColumnName(0));
}

@Test(expected = IllegalArgumentException.class)
public void testFusionnerColonnesInvalidColumnIndex() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
modifTableStockage.ts.setValueAt(true, 0, 0);
modifTableStockage.ts.setValueAt(true, 0, 2);
modifTableStockage.fusionnerColonnes(2);
}

@Test
public void testColonneMail() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
TableStockage tableS = modifTableStockage.colonneMail();
assertEquals(6, tableS.getColumnCount());
assertEquals("Envoyer E-Mail a:", tableS.getColumnName(5));
}

@Test
public void testSelectMail() {
modifTableStockage = new ModifTableStockage(new TableStockage(5, 5));
modifTableStockage.ts.setValueAt(true, 0, 5);
modifTableStockage.ts.setValueAt(true, 1, 5);
modifTableStockage.ts.setValueAt(false, 2, 5);
modifTableStockage.ts.setValueAt(false, 3, 5);
modifTableStockage.ts.setValueAt(false, 4, 5);
TableStockage tableS = modifTableStockage.selectMail();
assertEquals(3, tableS.getRowCount());
}

}