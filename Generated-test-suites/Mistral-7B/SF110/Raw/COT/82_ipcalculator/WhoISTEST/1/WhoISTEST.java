package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @Test
 public void testConstructor() {
 WhoIS whoisInstance = new WhoIS();
 assertNotNull(whoisInstance);
 }

 @Test
 public void testQueryPanelMethod() {
 WhoIS whoisInstance = new WhoIS();
 assertEquals("Query", whoisInstance.queryButton.getText());
 assertTrue(whoisInstance.queryButton.isEnabled());
 }

 @Test
 public void testAdditionalQueriesPanelMethod() {
 WhoIS whoisInstance = new WhoIS();
 assertTrue(whoisInstance.lowercaseL.isSelected());
 }

 @Test(expected = NullPointerException.class)
 public void testGetQueryWithNullQueryTextField() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.queryTextField = null;
 whoisInstance.getQuery();
 }

 @Test
 public void testGetQueryWithValidQuery() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.queryTextField.setText("example.com");
 String query = whoisInstance.getQuery();
 assertTrue(query.contains("example.com"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetQueryWithEmptyInverseQueriesComboBox() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.inverseQueries.setSelectedIndex(-1);
 whoisInstance.getQuery();
 }

 @Test
 public void testGetQueryWithNonEmptyInverseQueriesComboBox() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.inverseQueries.setSelectedIndex(1);
 String query = whoisInstance.getQuery();
 assertTrue(query.contains("-i admin-c"));
 }

 @Test
 public void testGetQueryWithMultipleSelectedMiscQueries() {
 WhoIS whoisInstance = new WhoIS();
 Object[] selected = {"inetnum", "route"};
 whoisInstance.miscList.setSelectedValues(selected);
 String query = whoisInstance.getQuery();
 assertTrue(query.contains("-T inetnum,route "));
 }

 @Test
 public void testStartQueryWithNoQuery() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.startQuery();
 assertFalse(whoisInstance.isDisplayingResults());
 }

 @Test
 public void testStartQueryWithValidQuery() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.queryTextField.setText("example.com");
 whoisInstance.startQuery();
 assertTrue(whoisInstance.isDisplayingResults());
 }

 @Test(expected = IOException.class)
 public void testQueryWithInvalidHost() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.WHOIS_ADDRESS = "invalidhost.com";
 whoisInstance.startQuery();
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithUnknownHost() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.WHOIS_ADDRESS = "unknownhost.com";
 whoisInstance.startQuery();
 }

 @Test
 public void testDisplayErrorWithEmptyErrorMessage() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.displayError("");
 assertFalse(JOptionPane.showConfirmDialog(whoisInstance, "", "Error", 0) > JOptionPane.CLOSED_OPTION);
 }

 @Test
 public void testDisplayErrorWithValidErrorMessage() {
 WhoIS whoisInstance = new WhoIS();
 String errorMessage = "An error occurred.";
 whoisInstance.displayError(errorMessage);
 assertTrue(JOptionPane.showConfirmDialog(whoisInstance, errorMessage, "Error", 0) > JOptionPane.CLOSED_OPTION);
 }

 @Test
 public void testResetMethod() {
 WhoIS whoisInstance = new WhoIS();
 whoisInstance.reset();
 assertEquals("", whoisInstance.queryTextField.getText());
 assertFalse(whoisInstance.lowercaseD.isSelected());
 assertEquals(0, whoisInstance.inverseQueries.getSelectedIndex());
 assertEquals(0, whoisInstance.miscList.getSelectedIndex());
 }

}