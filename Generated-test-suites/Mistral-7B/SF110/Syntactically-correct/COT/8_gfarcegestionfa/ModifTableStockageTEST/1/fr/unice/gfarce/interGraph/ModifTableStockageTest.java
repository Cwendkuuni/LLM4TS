import org.junit.Test;
import static org.junit.Assert.*;
import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

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
 public void testModifTableStockageConstructor() {
 ModifTableStockage expectedModifTableStockage = new ModifTableStockage(tableStockage);
 ModifTableStockage actualModifTableStockage = new ModifTableStockage(tableStockage);
 assertEquals(expectedModifTableStockage, actualModifTableStockage);
 }

 @Test
 public void testAjouterColonne() {
 String nomNouvelleColonne = "Email";
 Class<?> type = String.class;
 TableStockage expectedTableStockage = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() + 1);
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 expectedTableStockage.setColumnName(j, modifTableStockage.ts.getColumnName(j));
 }
 expectedTableStockage.setColumnName(modifTableStockage.getNbColonnes(), nomNouvelleColonne);
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int k = 0; k < modifTableStockage.getNbColonnes(); ++k) {
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, k), i, k);
 }
 expectedTableStockage.setValueAt("", i, modifTableStockage.getNbColonnes() - 1);
 }
 TableStockage actualTableStockage = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(expectedTableStockage, actualTableStockage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithNullTableStockage() {
 modifTableStockage = null;
 try {
 modifTableStockage.ajouterColonne("Email", String.class);
 } catch (IllegalArgumentException e) {
 // Test passed, expected IllegalArgumentException
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAjouterColonneWithNullNomNouvelleColonne() {
 String nomNouvelleColonne = null;
 try {
 modifTableStockage.ajouterColonne(nomNouvelleColonne, String.class);
 } catch (IllegalArgumentException e) {
 // Test passed, expected IllegalArgumentException
 }
 }

 @Test
 public void testAjouterColonneWithNullType() {
 Class<?> type = null;
 try {
 modifTableStockage.ajouterColonne("Email", type);
 } catch (IllegalArgumentException e) {
 // Test passed, expected IllegalArgumentException
 }
 }

 @Test
 public void testAjouterLigne() {
 TableStockage expectedTableStockage = new TableStockage(modifTableStockage.getNbLignes() + 1, modifTableStockage.getNbColonnes());
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 expectedTableStockage.setColumnName(j, modifTableStockage.ts.getColumnName(j));
 }
 for (int i = 0; i < modifTableStockage.getNbLignes(); ++i) {
 for (int k = 0; k < modifTableStockage.getNbColonnes(); ++k) {
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, k), i, k);
 }
 }
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 if (modifTableStockage.ts.getColumnClass(j).equals(String.class)) {
 expectedTableStockage.setValueAt("", modifTableStockage.getNbLignes(), j);
 }
 else if (modifTableStockage.ts.getColumnClass(j).equals(Double.class)) {
 expectedTableStockage.setValueAt(new Double("0"), modifTableStockage.getNbLignes(), j);
 }
 else {
 expectedTableStockage.setValueAt(new Boolean(false), modifTableStockage.getNbLignes(), j);
 }
 }
 TableStockage actualTableStockage = modifTableStockage.ajouterLigne();
 assertEquals(expectedTableStockage, actualTableStockage);
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneWithNoColumns() {
 modifTableStockage = new ModifTableStockage(new TableStockage(0, 0));
 try {
 modifTableStockage.ajouterLigne();
 } catch (IllegalStateException e) {
 // Test passed, expected IllegalStateException
 }
 }

 @Test
 public void testSupprimerColonne() {
 int indice = 0;
 TableStockage expectedTableStockage = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() - 1);
 for (int j = 0; j < modifTableStockage.getNbColonnes() - 1; ++j) {
 expectedTableStockage.setColumnName(j, modifTableStockage.ts.getColumnName(j));
 }
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int k = 0; k < modifTableStockage.getNbColonnes() - 1; ++k) {
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, k), i, k);
 }
 }
 for (int j = 0; j < modifTableStockage.getNbColonnes() - 1; ++j) {
 if (j == indice) {
 continue;
 }
 expectedTableStockage.setColumnName(j, modifTableStockage.ts.getColumnName(j + 1));
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int k = 0; k < modifTableStockage.getNbColonnes() - 1; ++k) {
 if (k == indice) {
 continue;
 }
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, k + 1), i, k);
 }
 }
 }
 TableStockage actualTableStockage = modifTableStockage.supprimerColonne(indice);
 assertEquals(expectedTableStockage, actualTableStockage);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneWithLastColumn() {
 int indice = modifTableStockage.getNbColonnes() - 1;
 try {
 modifTableStockage.supprimerColonne(indice);
 } catch (IndexOutOfBoundsException e) {
 // Test passed, expected IndexOutOfBoundsException
 }
 }

 @Test
 public void testSupprimerLigne() {
 int indice = 0;
 TableStockage expectedTableStockage = new TableStockage(modifTableStockage.getNbLignes() - 1, modifTableStockage.getNbColonnes());
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 expectedTableStockage.setColumnName(j, modifTableStockage.ts.getColumnName(j));
 }
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int k = 0; k < modifTableStockage.getNbColonnes(); ++k) {
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, k), i, k);
 }
 }
 for (int i = 0; i < modifTableStockage.getNbLignes() - 2; ++i) {
 for (int k = 0; k < modifTableStockage.getNbColonnes(); ++k) {
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i + 1, k), i, k);
 }
 }
 TableStockage actualTableStockage = modifTableStockage.supprimerLigne(indice);
 assertEquals(expectedTableStockage, actualTableStockage);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneWithLastLine() {
 int indice = modifTableStockage.getNbLignes() - 1;
 try {
 modifTableStockage.supprimerLigne(indice);
 } catch (IndexOutOfBoundsException e) {
 // Test passed, expected IndexOutOfBoundsException
 }
 }

 @Test
 public void testSeparerColonne() {
 int indice = 1;
 String expectedColonne1 = "Name";
 String expectedColonne2 = "Age";
 TableStockage expectedTableStockage = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() + 2);
 for (int j = 0; j < expectedTableStockage.getNbColonnes() - 2; ++j) {
 expectedTableStockage.setColumnName(j, modifTableStockage.ts.getColumnName(j));
 }
 expectedTableStockage.setColumnName(expectedTableStockage.getNbColonnes() - 2, expectedColonne1);
 expectedTableStockage.setColumnName(expectedTableStockage.getNbColonnes() - 1, expectedColonne2);
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int k = 0; k < expectedTableStockage.getNbColonnes() - 2; ++k) {
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, k), i, k);
 }
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, indice), i, expectedTableStockage.getNbColonnes() - 2);
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, indice + 1), i, expectedTableStockage.getNbColonnes() - 1);
 }
 TableStockage actualTableStockage = modifTableStockage.separerColonne(indice);
 assertEquals(expectedTableStockage, actualTableStockage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithNoStringColumn() {
 int indice = 0;
 try {
 modifTableStockage.separerColonne(indice);
 } catch (IllegalArgumentException e) {
 // Test passed, expected IllegalArgumentException
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithNoSpaceInColumnName() {
 int indice = 1;
 String columnName = "Age";
 try {
 modifTableStockage.separerColonne(indice);
 } catch (IllegalArgumentException e) {
 // Test passed, expected IllegalArgumentException
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneWithInvalidColumnName() {
 int indice = 1;
 String columnName = "NameAge";
 try {
 modifTableStockage.separerColonne(indice);
 } catch (IllegalArgumentException e) {
 // Test passed, expected IllegalArgumentException
 }
 }

 @Test
 public void testFusionnerColonnes() {
 int indice = 1;
 String expectedColonneName = "NameAge";
 TableStockage expectedTableStockage = new TableStockage(modifTableStockage.getNbLignes(), modifTableStockage.getNbColonnes() + 1);
 for (int j = 0; j < modifTableStockage.getNbColonnes(); ++j) {
 expectedTableStockage.setColumnName(j, modifTableStockage.ts.getColumnName(j));
 }
 expectedTableStockage.setColumnName(expectedTableStockage.getNbColonnes() - 1, expectedColonneName);
 for (int i = 0; i < modifTableStockage.getNbLignes() - 1; ++i) {
 for (int k = 0; k < modifTableStockage.getNbColonnes(); ++k) {
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, k), i, k);
 }
 expectedTableStockage.setValueAt(modifTableStockage.ts.getValueAt(i, indice).toString() + " " + modifTableStockage.ts.getValueAt(i, indice + 1).toString(), i, expectedTableStockage.getNbColonnes() - 1);
 }
 TableStockage actualTableStockage = modifTableStockage.fusionnerColonnes(indice);
 assertEquals(expectedTableStockage, actualTableStockage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesWithNoBooleanColumns() {
 int indice = 0;
 try {
 modifTableStockage.fusionnerColonnes(indice);
 } catch (IllegalArgumentException e) {
 // Test passed, expected IllegalArgumentException
 }
 }
}