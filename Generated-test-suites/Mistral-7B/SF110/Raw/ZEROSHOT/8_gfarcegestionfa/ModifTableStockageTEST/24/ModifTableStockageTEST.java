package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

	@Test
	public void testModifTableStockage() {
		TableStockage ts = new TableStockage(2, 2);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		assertEquals(3, modifTableStockage.getNbColonnes());
		assertEquals(3, modifTableStockage.getNbLignes());
	}

	@Test
	public void testAjouterColonne() {
		TableStockage ts = new TableStockage(2, 2);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.ajouterColonne("NouvelleColonne", String.class);
		assertEquals(4, tableS.getNbColonnes());
		assertEquals(3, tableS.getNbLignes());
		assertEquals("NouvelleColonne", tableS.getColumnName(3));
	}

	@Test
	public void testAjouterLigne() {
		TableStockage ts = new TableStockage(2, 2);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.ajouterLigne();
		assertEquals(3, tableS.getNbColonnes());
		assertEquals(4, tableS.getNbLignes());
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (i < 2 && j < 3) {
					assertEquals(ts.getValueAt(i, j), tableS.getValueAt(i, j));
				} else if (i == 2 && j < 3) {
					assertNull(tableS.getValueAt(i, j));
				} else {
					assertEquals(null, tableS.getValueAt(i, j));
				}
			}
		}
	}

	@Test
	public void testSupprimerColonne() {
		TableStockage ts = new TableStockage(2, 3);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.supprimerColonne(1);
		assertEquals(2, tableS.getNbColonnes());
		assertEquals(3, tableS.getNbLignes());
		assertEquals(tableS.getColumnName(0), ts.getColumnName(0));
		assertEquals(tableS.getColumnName(1), ts.getColumnName(2));
	}

	@Test
	public void testSupprimerLigne() {
		TableStockage ts = new TableStockage(3, 3);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.supprimerLigne(1);
		assertEquals(3, tableS.getNbColonnes());
		assertEquals(2, tableS.getNbLignes());
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (i != 1 && j < 3) {
					assertEquals(ts.getValueAt(i, j), tableS.getValueAt(i, j));
				} else {
					assertEquals(null, tableS.getValueAt(i, j));
				}
			}
		}
	}

	@Test
	public void testSeparerColonne() {
		TableStockage ts = new TableStockage(3, 1);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.separerColonne(0);
		assertEquals(2, tableS.getNbColonnes());
		assertEquals(3, tableS.getNbLignes());
		assertEquals("", tableS.getColumnName(0));
		assertEquals(ts.getColumnName(0), tableS.getColumnName(1));
	}

	@Test(expected = AssertionError.class)
	public void testSeparerColonneException() {
		TableStockage ts = new TableStockage(3, 1);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.separerColonne(1);
		assertEquals(2, tableS.getNbColonnes());
		assertEquals(3, tableS.getNbLignes());
		assertEquals("", tableS.getColumnName(0));
		assertEquals("", tableS.getColumnName(1));
		fail("Exception expected: Separateur de colonne non valide");
	}

	@Test
	public void testFusionnerColonnes() {
		TableStockage ts = new TableStockage(3, 3);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
		assertEquals(2, tableS.getNbColonnes());
		assertEquals(3, tableS.getNbLignes());
		assertEquals("Colonne1 Colonne2", tableS.getColumnName(0));
		assertEquals(null, tableS.getValueAt(0, 0));
		assertEquals("Value1 Value2", tableS.getColumnName(1));
		assertEquals("Value1Value2", tableS.getValueAt(0, 1).toString());
	}

	@Test(expected = AssertionError.class)
	public void testFusionnerColonnesException() {
		TableStockage ts = new TableStockage(3, 3);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.fusionnerColonnes(2);
		assertEquals(3, tableS.getNbColonnes());
		assertEquals(3, tableS.getNbLignes());
		assertEquals("Colonne1", tableS.getColumnName(0));
		assertEquals("Colonne2", tableS.getColumnName(1));
		assertEquals("Colonne3", tableS.getColumnName(2));
		fail("Exception expected: Les colonnes doivent être de type Boolean");
	}

	@Test
	public void testColonneMail() {
		TableStockage ts = new TableStockage(3, 2);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.colonneMail();
		assertEquals(4, tableS.getNbColonnes());
		assertEquals(3, tableS.getNbLignes());
		assertEquals("Envoyer E-Mail a:", tableS.getColumnName(3));
		assertEquals(true, tableS.getValueAt(0, 3));
		assertEquals(true, tableS.getValueAt(1, 3));
		assertEquals(true, tableS.getValueAt(2, 3));
	}

	@Test
	public void testSelectMail() {
		TableStockage ts = new TableStockage(3, 3);
		ts.setValueAt(true, 0, 2);
		ts.setValueAt(true, 1, 2);
		ts.setValueAt(true, 2, 2);
		ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
		TableStockage tableS = modifTableStockage.selectMail();
		assertEquals(3, tableS.getNbColonnes());
		assertEquals(2, tableS.getNbLignes());
		assertEquals("Envoyer E-Mail a:", tableS.getColumnName(2));
		assertEquals(true, tableS.getValueAt(0, 2));
		assertEquals(true, tableS.getValueAt(1, 2));
		assertEquals(null, tableS.getValueAt(2, 2));
	}

}