package fr.unice.gfarce.interGraph;
import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        TableStockage table = new TableStockage(0, 0);
        ModifTableStockage modifTable = new ModifTableStockage(table);
        String nomNouvelleColonne = "NouvelleColonne";
        Class<?> type = String.class;
        TableStockage expectedTable = new TableStockage(1, 1);

        TableStockage resultTable = modifTable.ajouterColonne(nomNouvelleColonne, type);

        assertEquals(expectedTable, resultTable);
    }
}