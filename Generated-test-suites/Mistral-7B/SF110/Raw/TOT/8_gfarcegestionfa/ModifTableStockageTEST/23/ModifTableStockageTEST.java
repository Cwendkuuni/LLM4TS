import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ModifTableStockageTest {

 private ModifTableStockage modifTableStockage;
 private TableStockage tableStockageMock;

 @Before
 public void setUp() {
 tableStockageMock = Mockito.mock(TableModel.class);
 modifTableStockage = new ModifTableStockage(tableStockageMock);
 }

 @Test
 public void testAjouterColonneWithValidArguments() {
 String nomNouvelleColonne = "newColumn";
 Class<?> type = String.class;
 TableStockage expectedTable = createExpectedTableWithOneAddedColumn(nomNouvelleColonne, type);
 TableStockage actualTable = modifTableStockage.ajouterColonne(nomNouvelleColonne, type);
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testAjouterColonneWithInvalidColumnCount() {
 TableStockage table = new TableStockage(1, 1);
 ModifTableStockage modifTable = new ModifTableStockage(table);
 String nomNouvelleColonne = "newColumn";
 Class<?> type = String.class;
 assertThrows(IllegalArgumentException.class, () -> modifTable.ajouterColonne(nomNouvelleColonne, type));
 }

 @Test
 public void testAjouterColonneWithInvalidType() {
 TableStockage table = new TableStockage(1, 1);
 ModifTableStockage modifTable = new ModifTableStockage(table);
 String nomNouvelleColonne = "newColumn";
 Class<?> invalidType = Integer.class;
 assertThrows(IllegalArgumentException.class, () -> modifTable.ajouterColonne(nomNouvelleColonne, invalidType));
 }

 @Test
 public void testAjouterLigne() {
 TableStockage expectedTable = createExpectedTableWithOneAddedRow();
 TableStockage actualTable = modifTableStockage.ajouterLigne();
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testAjouterLigneWithZeroColumns() {
 ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(0, 0));
 TableStockage expectedTable = modifTable.getTableStockage();
 assertThrows(IllegalArgumentException.class, () -> modifTable.ajouterLigne());
 assertEquals(expectedTable, modifTable.getTableStockage());
 }

 @Test
 public void testSupprimerColonneWithValidIndex() {
 int index = 0;
 TableStockage expectedTable = createExpectedTableWithOneColumnRemoved(index);
 TableStockage actualTable = modifTableStockage.supprimerColonne(index);
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testSupprimerColonneWithInvalidIndex() {
 int index = 6;
 TableStockage expectedTable = modifTableStockage.getTableStockage();
 assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.supprimerColonne(index));
 assertEquals(expectedTable, modifTableStockage.getTableStockage());
 }

 @Test
 public void testSupprimerLigneWithValidIndex() {
 int index = 0;
 TableStockage expectedTable = createExpectedTableWithOneRowRemoved(index);
 TableStockage actualTable = modifTableStockage.supprimerLigne(index);
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testSupprimerLigneWithInvalidIndex() {
 int index = 6;
 TableStockage expectedTable = modifTableStockage.getTableStockage();
 assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.supprimerLigne(index));
 assertEquals(expectedTable, modifTableStockage.getTableStockage());
 }

 @Test
 public void testSeparerColonneWithValidIndex() {
 int index = 0;
 TableStockage expectedTable = createExpectedTableWithTwoColumnsAfterSeparation(index);
 TableStockage actualTable = modifTableStockage.separerColonne(index);
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testSeparerColonneWithInvalidIndex() {
 int index = 6;
 TableStockage expectedTable = modifTableStockage.getTableStockage();
 assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.separerColonne(index));
 assertEquals(expectedTable, modifTableStockage.getTableStockage());
 }

 @Test
 public void testFusionnerColonnesWithValidIndices() {
 int index1 = 0;
 int index2 = 1;
 TableStockage expectedTable = createExpectedTableWithFusedColumns(index1, index2);
 TableStockage actualTable = modifTableStockage.fusionnerColonnes(index1);
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testFusionnerColonnesWithInvalidIndices() {
 int index1 = 6;
 int index2 = 7;
 TableStockage expectedTable = modifTableStockage.getTableStockage();
 assertThrows(IndexOutOfBoundsException.class, () -> modifTableStockage.fusionnerColonnes(index1));
 assertEquals(expectedTable, modifTableStockage.getTableStockage());
 }

 @Test
 public void testFusionnerColonnesWithInvalidColumnTypes() {
 int index1 = 0;
 int index2 = 1;
 TableStockage table = new TableStockage(1, 2);
 ModifTableStockage modifTable = new ModifTableStockage(table);
 assertThrows(IllegalArgumentException.class, () -> modifTable.fusionnerColonnes(index1));
 }

 @Test
 public void testColonneMail() {
 TableStockage expectedTable = createExpectedTableWithMailColumn();
 TableStockage actualTable = modifTableStockage.colonneMail();
 assertEquals(expectedTable, actualTable);
 }

 @Test
 public void testSelectMail() {
 boolean[] expectedValues = { true, true, false, true };
 TableStockage expectedTable = createExpectedTableWithMailColumnAndSelectedValues(expectedValues);
 TableStockage actualTable = modifTableStockage.selectMail();
 assertEquals(expectedTable, actualTable);
 }

 private TableStockage createExpectedTableWithOneAddedRow() {
 int newNbColonnes = modifTableStockage.getTableStockage().getColumnCount();
 int newNbLignes = modifTableStockage.getTableStockage().getRowCount() + 1;
 TableStockage expectedTable = new TableStockage(newNbLignes, newNbColonnes);
 for (int i = 0; i < newNbColonnes; ++i) {
 expectedTable.setColumnName(i, modifTableStockage.getTableStockage().getColumnName(i));
 }
 return expectedTable;
 }

 private TableStockage createExpectedTableWithOneColumnRemoved(int index) {
 int newNbColonnes = modifTableStockage.getTableStockage().getColumnCount() - 1;
 int newNbLignes = modifTableStockage.getTableStockage().getRowCount();
 TableStockage expectedTable = new TableStockage(newNbLignes, newNbColonnes);
 for (int i = 0; i < index; ++i) {
 expectedTable.setColumnName(i, modifTableStockage.getTableStockage().getColumnName(i));
 }
 for (int i = index; i < newNbColonnes; ++i) {
 expectedTable.setColumnName(i, modifTableStockage.getTableStockage().getColumnName(i + 1));
 }
 return expectedTable;
 }

 private TableStockage createExpectedTableWithOneAddedColumn(String nomNouvelleColonne, Class<?> type) {
 int newNbColonnes = modifTableStockage.getTableStockage().getColumnCount() + 1;
 int newNbLignes = modifTableStockage.getTableStockage().getRowCount();
 TableStockage expectedTable = new TableStockage(newNbLignes, newNbColonnes);
 for (int i = 0; i < newNbColonnes - 1; ++i) {
 expectedTable.setColumnName(i, modifTableStockage.getTableStockage().getColumnName(i));
 }
 expectedTable.setColumnName(newNbColonnes - 1, nomNouvelleColonne);
 for (int i = 0; i < newNbLignes; ++i) {
 for (int j = 0; j < newNbColonnes - 1; ++j) {
 expectedTable.setValueAt(modifTableStockage.getTableStockage().getValueAt(i, j), i, j);
 }
 }
 return expectedTable;
 }

 private TableStockage createExpectedTableWithTwoColumnsAfterSeparation(int index) {
 int newNbColonnes = modifTableStockage.getTableStockage().getColumnCount() + 1;
 int newNbLignes = modifTableStockage.getTableStockage().getRowCount();
 TableStockage expectedTable = new TableStockage(newNbLignes, newNbColonnes);
 for (int i = 0; i < index; ++i) {
 expectedTable.setColumnName(i, modifTableStockage.getTableStockage().getColumnName(i));
 }
 String colonne = modifTableStockage.getTableStockage().getColumnName(index);
 String[] split = colonne.split(" ", 2);
 String nomColonne1 = split[0];
 String nomColonne2 = "";
 if (split.length > 1) {
 nomColonne2 = split[1];
 }
 expectedTable.setColumnName(index, nomColonne1);
 expectedTable.setColumnName(index + 1, nomColonne2);
 for (int i = 0; i < newNbLignes; ++i) {
 for (int j = 0; j < newNbColonnes; ++j) {
 expectedTable.setValueAt(modifTableStockage.getTableStockage().getValueAt(i, j), i, j);
 }
 }
 return expectedTable;
 }

 private TableStockage createExpectedTableWithFusedColumns(int index1, int index2) {
 int newNbColonnes = modifTableStockage.getTableStockage().getColumnCount() - 1;
 int newNbLignes = modifTableStockage.getTableStockage().getRowCount();
 TableStockage expectedTable = new TableStockage(newNbLignes, newNbColonnes);
 for (int i = 0; i < index1; ++i) {
 expectedTable.setColumnName(i, modifTableStockage.getTableStockage().getColumnName(i));
 }
 expectedTable.setColumnName(index1, modifTableStockage.getTableStockage().getColumnName(index1).concat(" " + modifTableStockage.getTableStockage().getColumnName(index2)));
 for (int i = index1 + 1; i < newNbColonnes; ++i) {
 expectedTable.setColumnName(i, modifTableStockage.getTableStockage().getColumnName(i + 1));
 }
 for (int i = 0; i < newNbLignes; ++i) {
 for (int j = 0; j < newNbColonnes; ++j) {
 expectedTable.setValueAt(modifTableStockage.getTableStockage().getValueAt(i, j), i, j);
 }
 }
 return expectedTable;
 }

 private TableStockage createExpectedTableWithMailColumn() {
 int newNbColonnes = modifTableStockage.getTableStockage().getColumnCount() + 1;
 int newNbLignes = modifTableStockage.getTableStockage().getRowCount();
 TableStockage expectedTable = new TableStockage(newNbLignes, newNbColonnes);
 for (int i = 0; i < newNbColonnes - 1; ++i) {
 expectedTable.setColumnName(i, modifTableStockage.getTableStockage().getColumnName(i));
 }
 expectedTable.setColumnName(newNbColonnes - 1, "Envoyer E-Mail a:");
 for (int i = 0; i < newNbLignes; ++i) {
 for (int j = 0; j < newNbColonnes; ++j) {
 expectedTable.setValueAt(modifTableStockage.getTableStockage().getValueAt(i, j), i, j);
 }
 }
 return expectedTable;
 }

 private TableStockage createExpectedTableWithMailColumnAndSelectedValues(boolean[] values) {
 int newNbColonnes = modifTableStockage.getTableStockage().getColumnCount() + 1;
 int newNbLignes = modifTableStockage.getTableStockage().getRowCount();
 TableStockage expectedTable = new TableStockage(newNbLignes, newNbColonnes);
 for (int i = 0; i < newNbColonnes - 1; ++i) {
 expectedTable.setColumnName(i, modifTableStockage.getTableStockage().getColumnName(i));
 }
 expectedTable.setColumnName(newNbColonnes - 1, "Envoyer E-Mail a:");
 for (int i = 0; i < newNbLignes; ++i) {
 if (values[i]) {
 expectedTable.setValueAt(true, i, newNbColonnes - 1);
 } else {
 expectedTable.setValueAt(false, i, newNbColonnes - 1);
 }
 for (int j = 0; j < newNbColonnes - 1; ++j) {
 expectedTable.setValueAt(modifTableStockage.getTableStockage().getValueAt(i, j), i, j);
 }
 }
 return expectedTable;
 }
}