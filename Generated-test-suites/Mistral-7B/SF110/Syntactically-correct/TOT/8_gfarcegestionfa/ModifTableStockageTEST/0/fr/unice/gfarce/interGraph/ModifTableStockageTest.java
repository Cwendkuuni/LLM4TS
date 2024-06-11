import org.junit.Test;
import static org.junit.Assert.*;

import fr.unice.gfarce.interGraph.ModifTableStockage;
import fr.unice.gfarce.interGraph.TableStockage;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockage;

 @Test
 public void testModifTableStockageConstruction() {
 TableStockage ts = new TableStockage(5, 3);
 ModifTableStockage mt = new ModifTableStockage(ts);
 assertEquals(5, mt.getNbLignes());
 assertEquals(3, mt.getNbColonnes());
 }

 @Test
 public void testAjouterColonneStringClass() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 String nomNouvelleColonne = "newColumn";
 Class<?> type = String.class;
 TableStockage newTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(4, newTable.getNbColonnes());
 assertEquals(nomNouvelleColonne, newTable.getColumnName(3));
 }

 @Test
 public void testAjouterColonneDoubleClass() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 String nomNouvelleColonne = "newColumn";
 Class<?> type = Double.class;
 TableStockage newTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(4, newTable.getNbColonnes());
 assertEquals(nomNouvelleColonne, newTable.getColumnName(3));
 for (int i = 0; i < 5; ++i) {
 newTable.setValueAt(0.0, i, 3);
 }
 }

 @Test
 public void testAjouterColonneBooleanClass() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 String nomNouvelleColonne = "newColumn";
 Class<?> type = Boolean.class;
 TableStockage newTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(4, newTable.getNbColonnes());
 assertEquals(nomNouvelleColonne, newTable.getColumnName(3));
 for (int i = 0; i < 5; ++i) {
 newTable.setValueAt(false, i, 3);
 }
 }

 @Test
 public void testAjouterLigne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 TableStockage newTable = modifTableStockage.ajouterLigne();
 assertEquals(6, newTable.getNbLignes());
 for (int j = 0; j < 3; ++j) {
 assertEquals(newTable.getColumnName(j), modifTableStockage.getColumnName(j));
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAjouterLigneNoColumn() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 modifTableStockage.ajouterLigne();
 modifTableStockage.supprimerColonne(0);
 TableStockage newTable = modifTableStockage.ajouterLigne();
 fail("should throw an IllegalStateException");
 }

 @Test
 public void testSupprimerColonne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 4));
 TableStockage newTable = modifTableStockage.supprimerColonne(2);
 assertEquals(3, newTable.getNbColonnes());
 for (int j = 0; j < 2; ++j) {
 assertEquals(newTable.getColumnName(j), modifTableStockage.getColumnName(j));
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerColonneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 4));
 modifTableStockage.supprimerColonne(5);
 fail("should throw an IndexOutOfBoundsException");
 }

 @Test
 public void testSupprimerLigne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(6, 3));
 TableStockage newTable = modifTableStockage.supprimerLigne(2);
 assertEquals(5, newTable.getNbLignes());
 for (int j = 0; j < 3; ++j) {
 for (int i = 0; i < 5; ++i) {
 assertEquals(newTable.getValueAt(i, j), modifTableStockage.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSupprimerLigneInvalidIndex() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 modifTableStockage.supprimerLigne(5);
 fail("should throw an IndexOutOfBoundsException");
 }

 @Test
 public void testSeparerColonne() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 modifTableStockage.setValueAt("1 2", 0, 2);
 TableStockage newTable = modifTableStockage.separerColonne(2);
 assertEquals(5, newTable.getNbLignes());
 assertEquals(4, newTable.getNbColonnes());
 assertEquals("1", newTable.getColumnName(2));
 assertEquals("2", newTable.getColumnName(3));
 for (int i = 0; i < 5; ++i) {
 for (int j = 0; j < 4; ++j) {
 assertEquals(newTable.getValueAt(i, j), modifTableStockage.getValueAt(i, j));
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSeparerColonneInvalidColumnType() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 modifTableStockage.setValueAt(1.0, 0, 2);
 TableStockage newTable = modifTableStockage.separerColonne(2);
 fail("should throw an IllegalArgumentException");
 }

 @Test
 public void testFusionnerColonnes() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 4));
 TableStockage newTable = modifTableStockage.fusionnerColonnes(2);
 assertEquals(3, newTable.getNbColonnes());
 assertEquals("Envoyer E-Mail a: boolean", newTable.getColumnName(2));
 for (int i = 0; i < 5; ++i) {
 assertEquals(newTable.getValueAt(i, 2), modifTableStockage.getValueAt(i, 2).toString() + " " + modifTableStockage.getValueAt(i, 3).toString());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFusionnerColonnesInvalidColumnType() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 modifTableStockage.setValueAt("true", 0, 2);
 modifTableStockage.setValueAt("false", 0, 3);
 TableStockage newTable = modifTableStockage.fusionnerColonnes(2);
 fail("should throw an IllegalArgumentException");
 }

 @Test
 public void testColonneMail() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 3));
 TableStockage newTable = modifTableStockage.colonneMail();
 assertEquals(6, newTable.getNbColonnes());
 assertEquals("Envoyer E-Mail a:", newTable.getColumnName(5));
 for (int i = 0; i < 5; ++i) {
 newTable.setValueAt(true, i, 5);
 }
 }

 @Test
 public void testSelectMail() {
 modifTableStockage = new ModifTableStockage(new TableStockage(5, 6));
 for (int i = 0; i < 5; ++i) {
 modifTableStockage.setValueAt(true, i, 5);
 }
 TableStockage newTable = modifTableStockage.selectMail();
 assertEquals(5, newTable.getNbLignes());
 for (int j = 0; j < 6; ++j) {
 assertEquals(newTable.getColumnName(j), modifTableStockage.getColumnName(j));
 }
 for (int i = 0; i < 5; ++i) {
 for (int j = 0; j < 6; ++j) {
 assertEquals(newTable.getValueAt(i, j), modifTableStockage.getValueAt(i, j));
 }
 }
 }
}