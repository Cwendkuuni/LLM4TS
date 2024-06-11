package ipac.test;

import static org.junit.Assert.*;

import ipac.WhoIS;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @After
 public void tearDown() {
 whois = null;
 }

 @Test
 public void testWhoISConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testQueryPanel() {
 WhoIS whois = new WhoIS();
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testAdditionalQueriesPanel() {
 WhoIS whois = new WhoIS();
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 @Test
 public void testIPAddressLookups() {
 WhoIS whois = new WhoIS();
 JPanel lookupPanel = whois.IPAddressLookups();
 assertNotNull(lookupPanel);
 }

 @Test
 public void testMiscellaneousQueries() {
 WhoIS whois = new WhoIS();
 JPanel miscPanel = whois.MiscellaneousQueries();
 assertNotNull(miscPanel);
 }

 @Test
 public void testQueryHintsPanel() {
 WhoIS whois = new WhoIS();
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 @Test(expected = NullPointerException.class)
 public void testStartQueryWithEmptyQuery() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 }

 @Test
 public void testStartQueryWithValidQuery() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.startQuery();
 }

 @Test(expected = UnknownHostException.class)
 public void testConnectionToUnknownHost() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = "unknown.host";
 whois.startQuery();
 }

 @Test(expected = IOException.class)
 public void testConnectionToWhoisServer() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 }

 @Test
 public void testDisplayErrorWithNoQueryDetected() {
 WhoIS whois = new WhoIS();
 whois.displayError("No Query Detected");
 String expected = "Error";
 String actual = JOptionPane.showInputDialog(whois, "Error", "Error", 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testDisplayErrorWithUnknownWhoisHost() {
 WhoIS whois = new WhoIS();
 whois.displayError("Unknown Whois host: unknown.host");
 String expected = "Unknown Whois host: unknown.host";
 String actual = JOptionPane.showInputDialog(whois, "Error", "Error", 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testDisplayErrorWithWhoisServerCouldNotBeContacted() {
 WhoIS whois = new WhoIS();
 whois.displayError("Whois server could not be contacted.\nPlease check your internet connection.");
 String expected = "Whois server could not be contacted.\nPlease check your internet connection.";
 String actual = JOptionPane.showInputDialog(whois, "Error", "Error", 0);
 assertEquals(expected, actual);
 }

}