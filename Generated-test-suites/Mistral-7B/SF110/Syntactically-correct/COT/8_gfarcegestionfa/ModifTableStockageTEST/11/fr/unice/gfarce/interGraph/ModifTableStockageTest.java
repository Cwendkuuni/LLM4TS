package fr.unice.gfarce.interGraph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {

    private ModifTableStockage modifTableStockage;
    private TableStockage tableStockage;

    @Before
    public void setUp() {
        tableStockage = new TableStockage(3, 2);
        tableStockage.setColumnName(0, "Name");
        tableStockage.setColumnName(1, "Age");
        tableStockage.setValueAt("John Doe", 0, 0);
        tableStockage.setValueAt(25, 0, 1);
        tableStockage.setValueAt("Jane Doe", 1, 0);
        tableStockage.setValueAt(30, 1, 1);
        modifTableStockage = new ModifTableStockage(tableStockage);
    }

    @Test
    public void testConstructor() {
        assertNotNull(modifTableStockage);
        assertEquals(tableStockage.getColumnCount(), modifTableStockage.getNbColonnes());
        assertEquals(tableStockage.getRowCount() + 1, modifTableStockage.getNbLignes());
    }

    @Test
    public void testAjouterColonne() {
        TableStockage tableS = modifTableStockage.ajouterColonne("Type", String.class);
        assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
        assertEquals("Type", tableS.getColumnName(tableS.getColumnCount() - 1));
        modifTableStockage = new ModifTableStockage(tableS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneWithInvalidType() {
        modifTableStockage.ajouterColonne("Type", Integer.class);
    }

    @Test
    public void testAjouterLigne() {
        TableStockage tableS = modifTableStockage.ajouterLigne();
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount() + 1);
        for (int i = 0; i < tableS.getColumnCount(); ++i) {
            if (tableStockage.getColumnClass(i).equals(String.class)) {
                assertEquals("", tableS.getValueAt(tableS.getRowCount() - 1, i));
            } else if (tableStockage.getColumnClass(i).equals(Double.class)) {
                assertEquals(0.0, tableS.getValueAt(tableS.getRowCount() - 1, i), 0.001);
            } else {
                assertEquals(false, tableS.getValueAt(tableS.getRowCount() - 1, i));
            }
        }
        modifTableStockage = new ModifTableStockage(tableS);
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneWhenNoColumn() {
        modifTableStockage.setNbColonnes(0);
        modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableStockage tableS = modifTableStockage.supprimerColonne(0);
        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
        for (int i = 0; i < tableS.getColumnCount(); ++i) {
            if (i != 0) {
                assertEquals(tableStockage.getColumnName(i), tableS.getColumnName(i));
            }
        }
        modifTableStockage = new ModifTableStockage(tableS);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneWhenInvalidIndex() {
        modifTableStockage.supprimerColonne(tableStockage.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        TableStockage tableS = modifTableStockage.supprimerLigne(0);
        assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
        for (int i = 0; i < tableS.getColumnCount(); ++i) {
            for (int j = 0; j < tableS.getRowCount(); ++j) {
                if (j != 0) {
                    assertEquals(tableStockage.getValueAt(j, i), tableS.getValueAt(j - 1, i));
                }
            }
        }
        modifTableStockage = new ModifTableStockage(tableS);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneWhenInvalidIndex() {
        modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes());
    }

    @Test
    public void testSeparerColonne() {
        TableStockage tableS = modifTableStockage.separerColonne(0);
        assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
        assertEquals("Name", tableS.getColumnName(0));
        assertEquals("", tableS.getColumnName(1));
        for (int i = 0; i < tableS.getRowCount(); ++i) {
            String name = (String)tableStockage.getValueAt(i, 0);
            String[] nameAndAge = name.split(" ");
            assertEquals(nameAndAge[0], tableS.getValueAt(i, 0));
            assertEquals(nameAndAge[1], tableS.getValueAt(i, 1));
            assertEquals(tableStockage.getValueAt(i, 1), tableS.getValueAt(i, 2));
            modifTableStockage = new ModifTableStockage(tableS);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneWhenInvalidColumnType() {
        modifTableStockage.separerColonne(1);
    }

    @Test
    public void testFusionnerColonnes() {
        TableStockage tableS = modifTableStockage.fusionnerColonnes(1);
        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
        assertEquals("Name Age", tableS.getColumnName(0));
        for (int i = 0; i < tableS.getRowCount(); ++i) {
            String nameAge = (String)tableS.getValueAt(i, 0);
            String[] nameAndAge = nameAge.split(" ");
            assertEquals(nameAndAge[0], tableStockage.getValueAt(i, 0));
            assertEquals(nameAndAge[1], tableStockage.getValueAt(i, 1));
            assertEquals(nameAndAge[0] + " " + nameAndAge[1], tableS.getValueAt(i, 0));
            modifTableStockage = new ModifTableStockage(tableS);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesWhenInvalidColumnIndex() {
        modifTableStockage.fusionnerColonnes(modifTableStockage.getNbColonnes());
    }

    @Test
    public void testColonneMail() {
        TableStockage tableS = modifTableStockage.colonneMail();
        assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
        assertEquals("Envoyer E-Mail a:", tableS.getColumnName(tableS.getColumnCount() - 1));
        for (int i = 0; i < tableS.getRowCount(); ++i) {
            assertEquals(true, tableS.getValueAt(i, tableS.getColumnCount() - 1));
            modifTableStockage = new ModifTableStockage(tableS);
        }
    }

    @Test
    public void testSelectMail() {
        TableStockage tableS = modifTableStockage.selectMail();
        assertEquals(tableStockage.getRowCount(), tableS.getRowCount());
        for (int i = 0; i < tableS.getRowCount(); ++i) {
            for (int j = 0; j < tableS.getColumnCount(); ++j) {
                if (j != tableS.getColumnCount() - 1) {
                    assertEquals(tableStockage.getValueAt(i, j), tableS.getValueAt(i, j));
                }
            }
        }
        modifTableStockage = new ModifTableStockage(tableS);
    }
}