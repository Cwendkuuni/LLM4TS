package ipac.whois;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @Test
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.queryTextField);
 assertNotNull(whois.queryButton);
 assertNotNull(whois.resetButton);
 assertNotNull(whois.lowercaseL);
 assertNotNull(whois.uppercaseL);
 assertNotNull(whois.lowercaseM);
 assertNotNull(whois.uppercaseM);
 assertNotNull(whois.lowercaseX);
 assertNotNull(whois.resetRadioButton);
 assertNotNull(whois.radioButtonGroup);
 assertNotNull(whois.popupMenu);
 assertNotNull(whois.inverseQueries);
 assertNotNull(whois.miscList);
 }

 @Test
 public void testQueryPanel() {
 assertEquals("Search for : ", whois.queryPanel().getcomponents()[0].toString());
 assertNotNull(whois.queryPanel().getcomponents()[1]);
 assertTrue(whois.queryPanel().getcomponents()[1] instanceof JTextField);
 assertNotNull(whois.queryPanel().getcomponents()[2]);
 assertTrue(whois.queryPanel().getcomponents()[2] instanceof JButton);
 assertNotNull(whois.queryPanel().getcomponents()[3]);
 assertTrue(whois.queryPanel().getcomponents()[3] instanceof JButton);
 }

 @Test
 public void testAdditionalQueriesPanel() {
 assertNotNull(whois.additionalQueriesPanel());
 }

 @Test
 public void testIPAddressLookups() {
 assertNotNull(whois.IPAddressLookups());
 }

 @Test
 public void testMiscellaneousQueries() {
 assertNotNull(whois.MiscellaneousQueries());
 }

 @Test
 public void testQueryHintsPanel() {
 assertNotNull(whois.queryHintsPanel());
 }

 @Test
 public void testCreatePopupMenu() {
 assertNotNull(whois.popupMenu);
 }

 @Test
 public void testActionPerformedQueryButton() {
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "Query"));
 whois.startQuery();
 }

 @Test
 public void testActionPerformedResetButton() {
 whois.actionPerformed(new ActionEvent(whois.resetButton, 0, "Reset"));
 whois.reset();
 }

 @Test
 public void testGetQuery() {
 String query = whois.getQuery();
 assertNotNull(query);
 }

 @Test
 public void testStartQueryWithEmptyQuery() {
 whois.startQuery();
 assertNotNull(whois.getErrorMessage());
 }

 @Test
 public void testStartQueryWithValidQuery() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertFalse(whois.hasError());
 }

 @Test
 public void testDisplayError() {
 whois.displayError("Error Message");
 assertNotNull(whois.getErrorMessage());
 }

 @Test
 public void testQuery() throws IOException {
 String query = "example.com";
 String expectedOutput = "..."; // expected output from whois query
 whois.queryTextField.setText(query);
 whois.startQuery();
 String output = whois.getOutput();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testReset() {
 whois.reset();
 assertTrue(whois.queryTextField.getText().isEmpty());
 }
}