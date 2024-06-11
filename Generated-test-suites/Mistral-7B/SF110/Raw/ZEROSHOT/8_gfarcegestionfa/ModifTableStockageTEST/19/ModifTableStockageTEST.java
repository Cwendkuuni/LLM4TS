package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

	@Test
	public void testModifTableStockage() {
		TableStockage ts = new TableStockage(5, 3);
		ts.setColumnName(0, "Name");
		ts.setColumnName(1, "Age");
		ts.setColumnName(2, "Weight");
		ts.setValueAt("John Doe", 0, 0);
		ts.setValueAt(25, 0, 1);
		ts.setValueAt(75.5, 0, 2);
		ts.setValueAt("Jane Doe", 1, 0);
		ts.setValueAt(30, 1, 1);
		ts.setValueAt(60.5, 1, 2);
		ts.setValueAt("Bob Smith", 2, 0);
		ts.setValueAt(45, 2, 1);
		ts.setValueAt(85.5, 2, 2);

		ModifTableStockage modifTS = new ModifTableStockage(ts);

		assertEquals(3, modifTS.nbColonnes);
		assertEquals(3, modifTS.nbLignes);
		assertTrue(modifTS.ts instanceof TableStockage);
	}

	@Test
	public void testAjouterColonne() {
		TableStockage ts = new TableStockage(5, 3);
		ts.setColumnName(0, "Name");
		ts.setColumnName(1, "Age");
		ts.setColumnName(2, "Weight");
		ModifTableStockage modifTS = new ModifTableStockage(ts);

		String nomNouvelleColonne = "Gender";
		Class<String> type = String.class;

		TableStockage result = modifTS.ajouterColonne(nomNouvelleColonne, type);

		assertEquals(4, result.getColumnCount());
		assertTrue(result.getColumnName(3).equals(nomNouvelleColonne));
		assertArrayEquals(new String[] { "Name", "Age", "Weight", nomNouvelleColonne }, result.getColumnNames());
	}

	@Test
	public void testAjouterLigne() {
		TableStockage ts = new TableStockage(5, 3);
		ts.setColumnName(0, "Name");
		ts.setColumnName(1, "Age");
		ts.setColumnName(2, "Weight");
		ModifTableStockage modifTS = new ModifTableStockage(ts);

		TableStockage result = modifTS.ajouterLigne();

		assertEquals(6, result.getRowCount());
		assertEquals(3, result.getColumnCount());
		assertTrue(result.getValueAt(5, 0) instanceof String);
		assertTrue(result.getValueAt(5, 1) instanceof Number);
		assertTrue(result.getValueAt(5, 2) instanceof Boolean);
		assertEquals("", result.getValueAt(5, 0));
		assertEquals(0.0, result.getValueAt(5, 1).doubleValue());
		assertEquals(false, result.getValueAt(5, 2));
	}

	@Test
	public void testSupprimerColonne() {
		TableStockage ts = new TableStockage(5, 3);
		ts.setColumnName(0, "Name");
		ts.setColumnName(1, "Age");
		ts.setColumnName(2, "Weight");
		ModifTableStockage modifTS = new ModifTableStockage(ts);

		TableStockage result = modifTS.supprimerColonne(1);

		assertEquals(2, result.getColumnCount());
		assertArrayEquals(new String[] {"Name", "Weight"}, result.getColumnNames());
	}

	@Test
	public void testSupprimerLigne() {
		TableStockage ts = new TableStockage(5, 3);
		ts.setColumnName(0, "Name");
		ts.setColumnName(1, "Age");
		ts.setColumnName(2, "Weight");
		ModifTableStockage modifTS = new ModifTableStockage(ts);

		TableStockage result = modifTS.supprimerLigne(1);

		assertEquals(4, result.getRowCount());
		assertEquals(3, result.getColumnCount());
		assertEquals("Bob Smith", result.getValueAt(1, 0));
		assertEquals(45, result.getValueAt(1, 1).intValue());
		assertEquals(85.5, result.getValueAt(1, 2).doubleValue());
	}

	@Test
	public void testSeparerColonne() {
		TableStockage ts = new TableStockage(5, 3);
		ts.setColumnName(0, "Name Surname Age Weight");
		ts.setColumnClass(0, String.class);
		ts.setValueAt("John Doe 25 75.5", 0, 0);
		ModifTableStockage modifTS = new ModifTableStockage(ts);

		TableStockage result = modifTS.separerColonne(0);

		assertEquals(6, result.getRowCount());
		assertEquals(3, result.getColumnCount());
		assertEquals("Name", result.getColumnName(0));
		assertEquals("Surname", result.getColumnName(1));
		assertEquals("Age", result.getColumnName(2));
		assertEquals("John", result.getValueAt(0, 0));
		assertEquals("Doe", result.getValueAt(0, 1));
		assertEquals(25, result.getValueAt(0, 2).intValue());
		assertEquals("", result.getValueAt(1, 0));
		assertEquals("", result.getValueAt(1, 1));
		assertEquals(75.5, result.getValueAt(1, 2).doubleValue());
	}

	@Test
	public void testFusionnerColonnes() {
		TableStockage ts = new TableStockage(5, 3);
		ts.setColumnName(0, "Name");
		ts.setColumnName(1, "Firstname");
		ts.setColumnName(2, "Lastname");
		ts.setValueAt("John Doe", 0, 0);
		ts.setValueAt("John", 0, 1);
		ts.setValueAt("Doe", 0, 2);
		ts.setValueAt("Jane Smith", 1, 0);
		ts.setValueAt("Jane", 1, 1);
		ts.setValueAt("Smith", 1, 2);
		ModifTableStockage modifTS = new ModifTableStockage(ts);

		TableStockage result = modifTS.fusionnerColonnes(1);

		assertEquals(3, result.getColumnCount());
		assertEquals("Name Firstname Lastname", result.getColumnName(0));
		assertEquals("", result.getColumnName(1));
		assertEquals("John Doe Jane Smith", result.getValueAt(0, 0));
		assertEquals("John Doe Jane Smith", result.getValueAt(1, 0));
		assertEquals("John Doe", result.getValueAt(0, 1));
		assertEquals("Jane Smith", result.getValueAt(1, 1));
	}

	@Test
	public void testColonneMail() {
		TableStockage ts = new TableStockage(5, 3);
		ts.setColumnName(0, "Name");
		ts.setColumnName(1, "Age");
		ts.setColumnName(2, "Weight");
		ModifTableStockage modifTS = new ModifTableStockage(ts);

		TableStockage result = modifTS.colonneMail();

		assertEquals(4, result.getColumnCount());
		assertEquals("Name", result.getColumnName(0));
		assertEquals("Age", result.getColumnName(1));
		assertEquals("Weight", result.getColumnName(2));
		assertEquals("Envoyer E-Mail a:", result.getColumnName(3));
		assertEquals(true, result.getValueAt(0, 3));
		assertEquals(true, result.getValueAt(1, 3));
		assertEquals(false, result.getValueAt(2, 3));
		assertEquals(false, result.getValueAt(3, 3));
		assertEquals(false, result.getValueAt(4, 3));
	}

	@Test
	public void testSelectMail() {
		TableStockage ts = new TableStockage(5, 4);
		ts.setColumnName(0, "Name");
		ts.setColumnName(1, "Age");
		ts.setColumnName(2, "Weight");
		ts.setColumnName(3, "Envoyer E-Mail a:");
		ts.setValueAt("John Doe", 0, 0);
		ts.setValueAt(25, 0, 1);
		ts.setValueAt(75.5, 0, 2);
		ts.setValueAt(true, 0, 3);
		ts.setValueAt("Jane Doe", 1, 0);
		ts.setValueAt(30, 1, 1);
		ts.setValueAt(60.5, 1, 2);
		ts.setValueAt(false, 1, 3);
		ts.setValueAt("Bob Smith", 2, 0);
		ts.setValueAt(45, 2, 1);
		ts.setValueAt(85.5, 2, 2);
		ts.setValueAt(true, 2, 3);
		ts.setValueAt("Alice Johnson", 3, 0);
		ts.setValueAt(50, 3, 1);
		ts.setValueAt(55.5, 3, 2);
		ts.setValueAt(false, 3, 3);
		ModifTableStockage modifTS = new ModifTableStockage(ts);

		TableStockage result = modifTS.selectMail();

		assertEquals(3, result.getRowCount());
		assertEquals("Name", result.getColumnName(0));
		assertEquals("Age", result.getColumnName(1));
		assertEquals("Weight", result.getColumnName(2));
		assertEquals("John Doe", result.getValueAt(0, 0));
		assertEquals(25, result.getValueAt(0, 1).intValue());
		assertEquals(75.5, result.getValueAt(0, 2).doubleValue());
		assertEquals("Jane Doe", result.getValueAt(1, 0));
		assertEquals(30, result.getValueAt(1, 1).intValue());
		assertEquals(60.5, result.getValueAt(1, 2).doubleValue());
		assertEquals("Bob Smith", result.getValueAt(2, 0));
		assertEquals(45, result.getValueAt(2, 1).intValue());
		assertEquals(85.5, result.getValueAt(2, 2).doubleValue());
	}
}