package ipac.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
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
 public void testQueryPanelCreation() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 assertTrue(queryPanel.getLayout() instanceof FlowLayout);
 }

 @Test
 public void testQueryTextFieldCreation() {
 JTextField queryTextField = whois.queryTextField;
 assertNotNull(queryTextField);
 assertEquals(40, queryTextField.getColumns());
 }

 @Test
 public void testQueryButtonCreation() {
 JButton queryButton = whois.queryButton;
 assertNotNull(queryButton);
 }

 @Test
 public void testResetButtonCreation() {
 JButton resetButton = whois.resetButton;
 assertNotNull(resetButton);
 }

 @Test
 public void testIPAddressLookupsCreation() {
 JPanel ipAddressLookups = whois.IPAddressLookups();
 assertNotNull(ipAddressLookups);
 }

 @Test
 public void testMiscellaneousQueriesCreation() {
 JPanel miscellaneousQueries = whois.MiscellaneousQueries();
 assertNotNull(miscellaneousQueries);
 }

 @Test
 public void testStartQuery() throws IOException, UnknownHostException {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 // Add assertions here to check the response from the Whois server
 }

 @Test
 public void testDisplayError() {
 whois.displayError("Error message");
 // Add assertions here to check the JOptionPane message
 }

 @Test
 public void testQuery() throws IOException, UnknownHostException {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 String expectedOutput = "Expected WhoIS output";
 String actualOutput = whois.new WhoISOutput(getResourceAsStream("/expected_output.txt")).getOutput();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetQuery() {
 String query = "example.com";
 String expectedQuery = "-i inverse-queries -l lowercase-level -m more-specific -x exact-match -d boolean-d -T type1,type2 example.com";
 String actualQuery = whois.getQuery();
 assertEquals(expectedQuery, actualQuery);
 }

 @Test
 public void testReset() {
 whois.reset();
 assertEquals("", whois.queryTextField.getText());
 assertTrue(whois.resetRadioButton.isSelected());
 assertFalse(whois.lowercaseD.isSelected());
 assertEquals(0, whois.inverseQueries.getSelectedIndex());
 assertEquals(0, whois.miscList.getSelectedIndex());
 }
}