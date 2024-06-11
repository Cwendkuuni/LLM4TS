package ipac.test;

import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 WhoIS whoIS = new WhoIS();

 @Test
 public void testConstructor() {
 WhoIS whoISObject = new WhoIS();
 assertNotNull(whoISObject);
 }

 @Test
 public void testQueryPanel() {
 JPanel queryPanel = whoIS.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testAdditionalQueriesPanel() {
 JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 @Test
 public void testIPAddressLookups() {
 JPanel ipAddressLookups = whoIS.IPAddressLookups();
 assertNotNull(ipAddressLookups);
 }

 @Test
 public void testMiscellaneousQueries() {
 JPanel miscQueries = whoIS.MiscellaneousQueries();
 assertNotNull(miscQueries);
 }

 @Test
 public void testQueryHintsPanel() {
 JPanel queryHintsPanel = whoIS.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 @Test
 public void testCreatePopupMenu() {
 JPopupMenu popupMenu = whoIS.popupMenu;
 assertNotNull(popupMenu);
 }

 @Test
 public void testActionPerformedQueryButton() {
 whoIS.queryButton.doClick();
 // Add assertions for expected behavior after query button click
 }

 @Test
 public void testActionPerformedResetButton() {
 whoIS.resetButton.doClick();
 // Add assertions for expected behavior after reset button click
 }

 @Test
 public void testStartQueryWithValidQuery() {
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.queryButton.doClick();
 // Add assertions for expected behavior after starting query with valid query
 }

 @Test
 public void testStartQueryWithEmptyQuery() {
 whoIS.queryButton.doClick();
 // Add assertions for expected behavior after starting query with empty query
 }

 @Test
 public void testDisplayError() {
 whoIS.displayError("Error Message");
 // Add assertions for expected behavior after displaying error message
 }

 @Test
 public void testQuery() {
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.queryButton.doClick();
 String expectedOutput = "Expected output from query";
 String output = whoIS.getOutput(); // Assuming getOutput() method is implemented in WhoIS class to get the output from the query
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testGetQuery() {
 String query = "example.com";
 String expectedQuery = "-i none -l example.com";
 String actualQuery = whoIS.getQuery();
 assertEquals(expectedQuery, actualQuery);
 }

 @Test
 public void testReset() {
 whoIS.reset();
 String expectedQuery = "";
 String actualQuery = whoIS.queryTextField.getText();
 assertEquals(expectedQuery, actualQuery);
 }
}