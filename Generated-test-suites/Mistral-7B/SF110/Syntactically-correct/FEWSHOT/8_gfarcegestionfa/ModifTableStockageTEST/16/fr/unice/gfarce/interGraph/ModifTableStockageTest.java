package fr.unice.gfarce.interGraph.test;

import static org.junit.Assert.*;

import javax.swing.TableModel;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testModifTableStockageConstructor() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        assertEquals(ts.getColumnCount(), modifTableStockage.getNbColonnes());
        assertEquals(ts.getRowCount() + 1, modifTableStockage.getNbLignes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testModifTableStockageConstructorNullTableModel() {
        new ModifTableStockage(null);
    }

    @Test
    public void testAjouterColonne() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        String nomNouvelleColonne = "newColumn";
        Class<?> type = String.class;
        TableModel tableS = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
        assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        assertEquals(nomNouvelleColonne, tableS.getColumnName(tableS.getColumnCount() - 1));
        assertEquals(type, tableS.getColumnClass(tableS.getColumnCount() - 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneNullNomNouvelleColonne() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        Class<?> type = String.class;
        modifTableStockage.ajouterColonne(null, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAjouterColonneNullType() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        String nomNouvelleColonne = "newColumn";
        modifTableStockage.ajouterColonne(nomNouvelleColonne, null);
    }

    @Test
    public void testAjouterLigne() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel tableS = modifTableStockage.ajouterLigne();
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
            assertEquals(tableS.getValueAt(0, j), modifTableStockage.ts.getValueAt(0, j));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testAjouterLigneNoColumns() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.setNbColonnes(0);
        modifTableStockage.ajouterLigne();
    }

    @Test
    public void testSupprimerColonne() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        int indice = 0;
        TableModel tableS = modifTableStockage.supprimerColonne(indice);
        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
        for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
            for (int k = 0; k < tableS.getColumnCount(); ++k) {
                assertEquals(tableS.getValueAt(i, k), modifTableStockage.ts.getValueAt(i, k));
            }
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerColonneInvalidIndex() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.supprimerColonne(modifTableStockage.getNbColonnes());
    }

    @Test
    public void testSupprimerLigne() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        int indice = 0;
        TableModel tableS = modifTableStockage.supprimerLigne(indice);
        assertEquals(modifTableStockage.getNbLignes() - 1, tableS.getRowCount());
        for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
            assertEquals(tableS.getValueAt(0, j), modifTableStockage.ts.getValueAt(1, j));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSupprimerLigneInvalidIndex() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.supprimerLigne(modifTableStockage.getNbLignes());
    }

    @Test
    public void testSeparerColonne() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        int indice = 0;
        TableModel tableS = modifTableStockage.separerColonne(indice);
        assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            for (int j = 0; j < tableS.getColumnCount(); ++j) {
                if (j == indice) {
                    assertEquals(((String)modifTableStockage.ts.getValueAt(i, indice)).split(" ")[0], tableS.getColumnName(j));
                    assertEquals(((String)modifTableStockage.ts.getValueAt(i, indice)).split(" ")[1], tableS.getColumnName(j + 1));
                } else {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), tableS.getValueAt(i, j));
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSeparerColonneInvalidIndex() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.separerColonne(modifTableStockage.getNbColonnes());
    }

    @Test
    public void testFusionnerColonnes() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        int indice = 0;
        TableModel tableS = modifTableStockage.fusionnerColonnes(indice);
        assertEquals(modifTableStockage.getNbColonnes() - 1, tableS.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            for (int j = 0; j < tableS.getColumnCount(); ++j) {
                if (j < indice) {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j), tableS.getValueAt(i, j));
                } else {
                    assertEquals(modifTableStockage.ts.getValueAt(i, j).toString() + " " + modifTableStockage.ts.getValueAt(i, j + 1).toString(), tableS.getValueAt(i, j).toString());
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFusionnerColonnesInvalidIndex() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        modifTableStockage.fusionnerColonnes(modifTableStockage.getNbColonnes());
    }

    @Test
    public void testColonneMail() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        TableModel tableS = modifTableStockage.colonneMail();
        assertEquals(modifTableStockage.getNbColonnes() + 1, tableS.getColumnCount());
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
            assertEquals(new Boolean(true), tableS.getValueAt(i, tableS.getColumnCount() - 1));
        }
    }

    @Test
    public void testSelectMail() {
        TableModel ts = new JTable().getModel();
        ModifTableStockage modifTableStockage = new ModifTableStockage(ts);
        int j = modifTableStockage.ts.getColumnCount();
        TableModel tableS = modifTableStockage.selectMail();
        assertEquals(modifTableStockage.getNbLignes(), tableS.getRowCount());
        assertEquals(modifTableStockage.getNbColonnes(), tableS.getColumnCount());
        for (int row = 0; row < modifTableStockage.getNbLignes(); ++row) {
            Boolean bool = (Boolean)modifTableStockage.ts.getValueAt(row, j);
            if (bool) {
                for (int column = 0; column < modifTableStockage.getNbColonnes(); ++column) {
                    assertEquals(modifTableStockage.ts.getValueAt(row, column), tableS.getValueAt(row, column));
                }
            }
        }
    }
}