package ipac.test;

import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 // Test constructor
 @Test
 public void testWhoISConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 // Test queryPanel method
 @Test
 public void testQueryPanel() {
 WhoIS whois = new WhoIS();
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 // Test additionalQueriesPanel method
 @Test
 public void testAdditionalQueriesPanel() {
 WhoIS whois = new WhoIS();
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 // Test IPAddressLookups method
 @Test
 public void testIPAddressLookups() {
 WhoIS whois = new WhoIS();
 JPanel lookupPanel = whois.IPAddressLookups();
 assertNotNull(lookupPanel);
 }

 // Test MiscellaneousQueries method
 @Test
 public void testMiscellaneousQueries() {
 WhoIS whois = new WhoIS();
 JPanel miscPanel = whois.MiscellaneousQueries();
 assertNotNull(miscPanel);
 }

 // Test queryHintsPanel method
 @Test
 public void testQueryHintsPanel() {
 WhoIS whois = new WhoIS();
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 // Test createPopupMenu method
 @Test
 public void testCreatePopupMenu() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.popupMenu);
 }

 // Test actionPerformed method
 @Test
 public void testActionPerformed() {
 WhoIS whois = new WhoIS();
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "Query");
 whois.actionPerformed(actionEvent);
 whois.startQuery();
 }

 // Test startQuery method - no query
 @Test(expected = AssertionError.class)
 public void testStartQueryNoQuery() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 }

 // Test startQuery method - valid query
 @Test
 public void testStartQueryValidQuery() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.startQuery();
 }

 // Test displayError method
 @Test
 public void testDisplayError() {
 WhoIS whois = new WhoIS();
 whois.displayError("Error message");
 }

 // Test query method - unknown host
 @Test(expected = UnknownHostException.class)
 public void testQueryUnknownHost() {
 WhoIS whois = new WhoIS();
 whois.query("unknown.host");
 }

 // Test query method - I/O exception
 @Test(expected = IOException.class)
 public void testQueryIOException() {
 WhoIS whois = new WhoIS();
 whois.query("example.com");
 }

 // Test getQuery method - no inverse queries
 @Test
 public void testGetQueryNoInverseQueries() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("example.com", query);
 }

 // Test getQuery method - inverse queries
 @Test
 public void testGetQueryInverseQueries() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.inverseQueries.setSelectedItem("admin-c");
 String query = whois.getQuery();
 assertTrue(query.startsWith("-i admin-c"));
 }

 // Test getQuery method - lowercase query
 @Test
 public void testGetQueryLowerCase() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.lowercaseL.doClick();
 String query = whois.getQuery();
 assertTrue(query.startsWith("-l"));
 }

 // Test getQuery method - uppercase query
 @Test
 public void testGetQueryUpperCase() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.uppercaseL.doClick();
 String query = whois.getQuery();
 assertTrue(query.startsWith("-L"));
 }

 // Test getQuery method - more specific query
 @Test
 public void testGetQueryMoreSpecific() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com/24");
 whois.lowercaseM.doClick();
 String query = whois.getQuery();
 assertTrue(query.startsWith("-m"));
 }

 // Test getQuery method - more specific query
 @Test
 public void testGetQueryMoreSpecificUpperCase() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com/24");
 whois.uppercaseM.doClick();
 String query = whois.getQuery();
 assertTrue(query.startsWith("-M"));
 }

 // Test getQuery method - exact match query
 @Test
 public void testGetQueryExactMatch() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.lowercaseX.doClick();
 String query = whois.getQuery();
 assertTrue(query.startsWith("-x"));
 }

 // Test getQuery method - boolean query
 @Test
 public void testGetQueryBoolean() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.lowercaseD.doClick();
 String query = whois.getQuery();
 assertTrue(query.startsWith("-d"));
 }

 // Test getQuery method - misc queries
 @Test
 public void testGetQueryMiscQueries() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 String[] selectedValues = { "inetnum", "as-set" };
 whois.miscList.setSelectedValues(selectedValues);
 String query = whois.getQuery();
 assertTrue(query.startsWith("-T inetnum,as-set"));
 }

 // Test reset method
 @Test
 public void testReset() {
 WhoIS whois = new WhoIS();
 whois.reset();
 assertTrue(whois.queryTextField.getText().isEmpty());
 }
}