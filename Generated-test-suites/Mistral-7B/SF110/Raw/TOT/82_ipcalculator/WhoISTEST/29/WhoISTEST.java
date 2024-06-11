package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.event.ActionEvent;
import java.io.IOException;

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
 public void testQueryPanel() {
 // Test queryTextField
 whois.queryTextField.setText("example.com");
 assertEquals("example.com", whois.queryTextField.getText().trim());

 // Test queryButton
 ActionEvent queryButtonEvent = new ActionEvent(whois.queryButton, 0, "QUERY_BUTTON_CLICKED");
 whois.actionPerformed(queryButtonEvent);
 // Test if query method is called
 // Add assertions for expected behavior after query method call
 }

 @Test
 public void testEmptyQuery() {
 // Test empty queryTextField
 whois.queryTextField.setText("");
 whois.startQuery();
 // Test error message displayed
 String errorMessage = "No Query Detected";
 assertEquals("Error", whois.getLastErrorMessage());
 assertEquals(errorMessage, whois.getLastErrorMessage());
 }

 @Test
 public void testInvalidQuery() {
 // Test queryTextField with invalid query
 whois.queryTextField.setText("hello world");
 whois.startQuery();
 // Test error message displayed
 String errorMessage = "Invalid query: hello world";
 assertEquals("Error", whois.getLastErrorMessage());
 assertEquals(errorMessage, whois.getLastErrorMessage());
 }

 @Test
 public void testUppercaseQuery() {
 // Test queryTextField with uppercase query
 whois.queryTextField.setText("INETNUM 192.168.1.1");
 whois.queryTextField.setCaretPosition(whois.queryTextField.getText().length());
 whois.resetButton.doClick();
 whois.queryTextField.setText("INETNUM 192.168.1.1");
 whois.startQuery();
 // Test if query method is called with uppercase query
 // Add assertions for expected behavior after query method call
 }

 @Test
 public void testInverseQueries() throws IOException {
 // Test selecting different inverse queries
 whois.inverseQueries.setSelectedIndex(1);
 whois.startQuery();
 // Test if inverseQueries is correctly used in query method
 // Add assertions for expected behavior after query method call
 }

 @Test
 public void testMiscQueries() throws IOException {
 // Test selecting different misc queries
 whois.miscList.setSelectedValues(new Object[]{"as-block"});
 whois.startQuery();
 // Test if miscQueries is correctly used in query method
 // Add assertions for expected behavior after query method call
 }

 @Test
 public void testErrorConnection() throws IOException {
 // Test error connection to Whois server
 whois.WHOIS_ADDRESS = "invalid_whois_server";
 whois.startQuery();
 // Test error message displayed
 String errorMessage = "Whois server could not be contacted.\nPlease check your internet connection.";
 assertEquals("Error", whois.getLastErrorMessage());
 assertEquals(errorMessage, whois.getLastErrorMessage());
 }

 @Test
 public void testUnknownQuery() throws IOException {
 // Test unknown query to Whois server
 whois.WHOIS_ADDRESS = "whois.example.com";
 whois.startQuery();
 // Test error message displayed
 String errorMessage = "Unknown Whois query: unknown_query";
 assertEquals("Error", whois.getLastErrorMessage());
 assertEquals(errorMessage, whois.getLastErrorMessage());
 }
}