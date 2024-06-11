package fr.unice.gfarce.interGraph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import javax.swing.table.TableModel;

import org.junit.Test;

public class ModifTableStockageTest {

	private TableModel ts = null;
	private ModifTableStockage modifTableStockage = null;

	@Test
	public void testModifTableStockageConstruction() {
		ts = new Object[][] { { "Test1", "1" }, { "Test2", "2" }, { "Test3", "3" } };
		modifTableStockage = new ModifTableStockage(new TableStockage(ts));
		assertEquals(3, modifTableStockage.getNbColonnes());
		assertEquals(3, modifTableStockage.getNbLignes());
	}

	@Test
	public void testAjouterColonne() {
		ts = new Object[][] { { "Test1", "1" }, { "Test2", "2" }, { "Test3", "3" } };
		modifTableStockage = new ModifTableStockage(new TableStockage(ts));
		ModifTableStockage result = modifTableStockage.ajouterColonne("NouvelleColonne", String.class);
		assertEquals(4, result.getNbColonnes());
		assertEquals("NouvelleColonne", result.getColumnName(3));
		assertNull(result.getValueAt(0, 3));
		assertNull(result.getValueAt(1, 3));
		assertNull(result.getValueAt(2, 3));
	}

	@Test
	public void testAjouterLigne() {
		ts = new Object[][] { { "Test1", "1" }, { "Test2", "2" }, { "Test3", "3" } };
		modifTableStockage = new ModifTableStockage(new TableStockage(ts));
		ModifTableStockage result = modifTableStockage.ajouterLigne();
		assertEquals(4, result.getNbLignes());
		assertEquals(4, result.getNbColonnes());
		assertNull(result.getValueAt(3, 0));
		assertNull(result.getValueAt(3, 1));
		assertNull(result.getValueAt(3, 2));
		assertNull(result.getValueAt(3, 3));
	}

	@Test
	public void testSupprimerColonne() {
		ts = new Object[][] { { "Test1", "1" }, { "Test2", "2" }, { "Test3", "3" }, { "Test4", "4" } };
		modifTableStockage = new ModifTableStockage(new TableStockage(ts));
		ModifTableStockage result = modifTableStockage.supprimerColonne(1);
		assertEquals(3, result.getNbColonnes());
		assertFalse(result.getColumnName(1).equals("Test2"));
		assertEquals(3, result.getColumnCount());
		for (int i = 0; i < result.getRowCount(); ++i) {
			assertEquals(2, result.getColumnCount());
		}
	}

	@Test
	public void testSupprimerLigne() {
		ts = new Object[][] { { "Test1", "1" }, { "Test2", "2" }, { "Test3", "3" }, { "Test4", "4" } };
		modifTableStockage = new ModifTableStockage(new TableStockage(ts));
		ModifTableStockage result = modifTableStockage.supprimerLigne(1);
		assertEquals(3, result.getNbLignes());
		assertEquals(4, result.getNbColonnes());
		assertFalse(result.getValueAt(0, 1) instanceof String);
		assertEquals(3, result.getValueAt(0, 1).intValue());
		assertFalse(result.getValueAt(1, 1) instanceof String);
		assertEquals(3, result.getValueAt(1, 1).intValue());
		assertNull(result.getValueAt(1, 2));
		assertNull(result.getValueAt(1, 3));
	}

	@Test
	public void testSeparerColonne() {
		ts = new Object[][] { { "Test1 Test2 1 2", "Test3 Test2 3 4" }, { "Test4 Test2 5 6", "Test5 Test2 7 8" } };
		modifTableStockage = new ModifTableStockage(new TableStockage(ts));
		ModifTableStockage result = modifTableStockage.separerColonne(1);
		assertEquals(5, result.getNbColonnes());
		assertEquals("Test2", result.getColumnName(1));
		assertEquals("Test1", result.getColumnName(2));
		assertEquals("1", result.getColumnName(3));
		assertEquals("Test3 Test2", result.getColumnName(4));
		assertEquals("Test4 Test2", result.getColumnName(5));
		assertEquals("Test1", result.getValueAt(0, 1).toString());
		assertEquals("1", result.getValueAt(0, 2).toString());
		assertEquals("Test3", result.getValueAt(0, 3).toString());
		assertEquals("4", result.getValueAt(0, 4).toString());
		assertEquals("Test4", result.getValueAt(0, 5).toString());
		assertEquals("6", result.getValueAt(1, 4).toString());
		assertEquals("8", result.getValueAt(1, 5).toString());
		assertEquals(5, result.getColumnCount());
		for (int i = 0; i < result.getRowCount(); ++i) {
			assertEquals(5, result.getColumnCount());
		}
		ModifTableStockage result2 = modifTableStockage.separerColonne(1);
		assertEquals(5, result2.getNbColonnes());
		assertEquals("Test2", result2.getColumnName(1));
		assertEquals("Test1", result2.getColumnName(2));
		assertEquals("1", result2.getColumnName(3));
		assertEquals("Test3 Test2", result2.getColumnName(4));
		assertEquals("Test4 Test2", result2.getColumnName(5));
		assertEquals("Test1", result2.getValueAt(0, 1).toString());
		assertEquals("1", result2.getValueAt(0, 2).toString());
		assertEquals("Test3", result2.getValueAt(0, 3).toString());
		assertEquals("4", result2.getValueAt(0, 4).toString());
		assertEquals("Test4", result2.getValueAt(0, 5).toString());
		assertEquals("5", result2.getValueAt(1, 4).toString());
		assertEquals("8", result2.getValueAt(1, 5).toString());
		assertEquals(5, result2.getColumnCount());
		for (int i = 0; i < result2.getRowCount(); ++i) {
			assertEquals(5, result2.getColumnCount());
		}
		assertNotSame(result, result2);
	}

	@Test
	public void testFusionnerColonnes() {
		ts = new Object[][] { { true, false }, { false, true }, { true, true }, { false, false } };
		modifTableStockage = new ModifTableStockage(new TableStockage(ts));
		ModifTableStockage result = modifTableStockage.fusionnerColonnes(0);
		assertEquals(2, result.getNbColonnes());
		assertEquals("Envoyer E-Mail a: false", result.getColumnName(0));
		assertEquals("Envoyer E-Mail a: true", result.getColumnName(1));
		assertEquals(4, result.getRowCount());
		assertEquals("Envoyer E-Mail a: false Test1", result.getValueAt(0, 0).toString());
		assertEquals("Envoyer E-Mail a: false Test2", result.getValueAt(1, 0).toString());
		assertEquals("Envoyer E-Mail a: false Test3", result.getValueAt(2, 0).toString());
		assertEquals("Envoyer E-Mail a: false Test4", result.getValueAt(3, 0).toString());
		assertEquals("Envoyer E-Mail a: true Test1", result.getValueAt(0, 1).toString());
		assertEquals("Envoyer E-Mail a: true Test2", result.getValueAt(1, 1).toString());
		assertEquals("Envoyer E-Mail a: true Test3", result.getValueAt(2, 1).toString());
		assertEquals("Envoyer E-Mail a: true Test4", result.getValueAt(3, 1).toString());
	}

	@Test
	public void testColonneMail() {
		ts = new Object[][] { { "Test1", "1" }, { "Test2", "2" }, { "Test3", "3" } };
		modifTableStockage = new ModifTableStockage(new TableStockage(ts));
		ModifTableStockage result = modifTableStockage.colonneMail();
		assertEquals(4, result.getNbColonnes());
		assertEquals("Envoyer E-Mail a:", result.getColumnName(3));
		assertEquals(Boolean.class, result.getColumnClass(3));
		assertEquals(Boolean.TRUE, result.getValueAt(0, 3));
		assertEquals(Boolean.TRUE, result.getValueAt(1, 3));
		assertEquals(Boolean.TRUE, result.getValueAt(2, 3));
	}

	@Test
	public void testSelectMail() {
		ts = new Object[][] { { "Test1", true, "1" }, { "Test2", false, "2" }, { "Test3", true, "3" }, { "Test4", false, "4" } };
		modifTableStockage = new ModifTableStockage(new TableStockage(ts));
		ModifTableStockage result = modifTableStockage.selectMail();
		assertEquals(2, result.getNbColonnes());
		assertEquals("Test1", result.getColumnName(0));
		assertEquals("Test2", result.getColumnName(1));
		assertEquals(2, result.getRowCount());
		assertEquals("Test1", result.getValueAt(0, 0).toString());
		assertEquals("Test3", result.getValueAt(1, 0).toString());
		assertEquals("1", result.getValueAt(0, 1).toString());
		assertEquals("3", result.getValueAt(1, 1).toString());
	}
}