package ipac.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whoIS;

 @Before
 public void setUp() {
 whoIS = new WhoIS();
 }

 @After
 public void tearDown() {
 whoIS = null;
 }

 @Test
 public void testConstructor() {
 WhoIS whoIS = new WhoIS();
 assertNotNull(whoIS);
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
 JPanel lookupPanel = whoIS.IPAddressLookups();
 assertNotNull(lookupPanel);
 }

 @Test
 public void testMiscellaneousQueries() {
 JPanel miscPanel = whoIS.MiscellaneousQueries();
 assertNotNull(miscPanel);
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
 public void testActionPerformed() {
 WhoIS spyWhoIS = spy(whoIS);
 doCallRealMethod().when(spyWhoIS).actionPerformed(any(ActionEvent.class));
 spyWhoIS.actionPerformed(new ActionEvent(new Object(), 0, "Query"));
 verify(spyWhoIS).startQuery();
 }

 @Test
 public void testStartQuery() {
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.actionPerformed(new ActionEvent(new Object(), 0, "Query"));
 // Add assertions for the expected behavior of the query method here
 }

 @Test
 public void testDisplayError() {
 whoIS.displayError("Test Error");
 // Add assertions for the expected behavior of the displayError method here
 }

 @Test
 public void testQuery() {
 String query = "example.com";
 String expectedOutput = "Expected output from the WhoIS server";
 String output = whoIS.query(query);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testGetQuery() {
 String query = "example.com";
 String expectedQuery = "-i None -l example.com";
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