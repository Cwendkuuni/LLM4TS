package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.table.TableModel;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

	@Test
	public void testModifTableStockage() {
		Class<?> type = String.class;
		TableStockage ts = new TableStockage(1, 1);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		assertEquals(modifTable.getNbColonnes(), ts.getColumnCount() + 1);
		assertEquals(modifTable.getNbLignes(), ts.getRowCount() + 1);
	}

	@Test
	public void testAjouterColonne() {
		Class<?> type = String.class;
		TableStockage ts = new TableStockage(1, 1);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		TableStockage tableS = modifTable.ajouterColonne("nomColonne", type);
		assertEquals(tableS.getColumnCount(), modifTable.getNbColonnes());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAjouterColonneWithInvalidType() {
		Class<?> type = Integer.class;
		TableStockage ts = new TableStockage(1, 1);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		modifTable.ajouterColonne("nomColonne", type);
	}

	@Test
	public void testAjouterLigne() {
		TableStockage ts = new TableStockage(1, 1);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		TableStockage tableS = modifTable.ajouterLigne();
		assertEquals(tableS.getRowCount(), modifTable.getNbLignes());
	}

	@Test(expected = IllegalStateException.class)
	public void testAjouterLigneWithNoColumns() {
		TableStockage ts = new TableStockage(1, 0);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		modifTable.ajouterLigne();
	}

	@Test
	public void testSupprimerColonne() {
		Class<?> type = String.class;
		TableStockage ts = new TableStockage(2, 2);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		TableStockage tableS = modifTable.supprimerColonne(0);
		assertEquals(tableS.getColumnCount(), modifTable.getNbColonnes() - 1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSupprimerColonneWithInvalidIndex() {
		Class<?> type = String.class;
		TableStockage ts = new TableStockage(2, 2);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		modifTable.supprimerColonne(ts.getColumnCount());
	}

	@Test
	public void testSupprimerLigne() {
		TableStockage ts = new TableStockage(2, 2);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		TableStockage tableS = modifTable.supprimerLigne(0);
		assertEquals(tableS.getRowCount(), modifTable.getNbLignes() - 1);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSupprimerLigneWithInvalidIndex() {
		TableStockage ts = new TableStockage(2, 2);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		modifTable.supprimerLigne(ts.getRowCount());
	}

	@Test
	public void testSeparerColonne() {
		Class<?> type = Double.class;
		TableStockage ts = new TableStockage(1, 1);
		ts.setValueAt("10 m2 20 m2", 0, 0);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		TableStockage tableS = modifTable.separerColonne(0);
		assertEquals(tableS.getColumnCount(), modifTable.getNbColonnes() + 1);
		assertEquals(tableS.getValueAt(0, 0).toString(), "10");
		assertEquals(tableS.getValueAt(0, 1).toString(), "20");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSeparerColonneWithInvalidType() {
		Class<?> type = Boolean.class;
		TableStockage ts = new TableStockage(1, 1);
		ts.setValueAt(true, 0, 0);
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		modifTable.separerColonne(0);
	}

	@Test
	public void testFusionnerColonnes() {
		Class<?> type = String.class;
		TableStockage ts = new TableStockage(1, 2);
		ts.setColumnName(0, "Colonne1");
		ts.setColumnName(1, "Colonne2");
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		TableStockage tableS = modifTable.fusionnerColonnes(1);
		assertEquals(tableS.getColumnCount(), modifTable.getNbColonnes() - 1);
		assertEquals(tableS.getColumnName(0), "Colonne1");
		assertEquals(tableS.getColumnName(1), "Colonne1 Colonne2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFusionnerColonnesWithInvalidIndices() {
		Class<?> type = String.class;
		TableStockage ts = new TableStockage(1, 2);
		ts.setColumnName(0, "Colonne1");
		ts.setColumnName(1, "Colonne2");
		ModifTableStockage modifTable = new ModifTableStockage(ts);
		modifTable.fusionnerColonnes(0);
	}
}