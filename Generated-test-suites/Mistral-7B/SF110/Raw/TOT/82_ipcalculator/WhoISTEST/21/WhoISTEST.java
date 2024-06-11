package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

 private WhoIS whois;
 private ByteArrayOutputStream outContent;
 private ByteArrayInputStream inContent;

 @Before
 public void setUp() {
 whois = new WhoIS();
 outContent = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContent));
 inContent = new ByteArrayInputStream(new byte[0]);
 System.setIn(inContent);
 }

 @Test
 public void testQueryButtonActionPerformed() {
 String query = "google.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("google.com");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testEmptyQueryTextField() {
 whois.queryButton.doClick();
 String expectedError = "No Query Detected";
 assertEquals(expectedError, outContent.toString());
 }

 @Test
 public void testInvalidQuery() {
 whois.queryTextField.setText("invalidquery");
 whois.queryButton.doClick();
 String expectedError = "Unknown command invalidquery.";
 assertEquals(expectedError, outContent.toString());
 }

 @Test
 public void testInverseQueries() {
 whois.inverseQueries.setSelectedIndex(1);
 whois.queryTextField.setText("APNIC");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("APNIC admin-c");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testLowercaseSearch() {
 whois.lowercaseL.doClick();
 whois.queryTextField.setText("google.com");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("google.com -l");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testUppercaseSearch() {
 whois.uppercaseL.doClick();
 whois.queryTextField.setText("google.com");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("google.com -L");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMoreSpecificSearch() {
 whois.lowercaseM.doClick();
 whois.queryTextField.setText("192.168.1.1");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("192.168.1.1 -m");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMoreGeneralSearch() {
 whois.uppercaseM.doClick();
 whois.queryTextField.setText("192.168");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("192.168 -M");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testExactMatch() {
 whois.lowercaseX.doClick();
 whois.queryTextField.setText("Google");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("Google -x");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testBooleanSearch() {
 whois.lowercaseD.doClick();
 whois.queryTextField.setText("APNIC");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("APNIC -d");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMiscQueriesAll() {
 whois.miscList.setSelectedValues(new Object[]{"All"});
 whois.queryTextField.setText("google.com");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("google.com");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMiscQueriesAS() {
 whois.miscList.setSelectedValues(new Object[]{"as-block"});
 whois.queryTextField.setText("APNIC");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("APNIC as-block");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMiscQueriesFilterSet() {
 whois.miscList.setSelectedValues(new Object[]{"filter-set"});
 whois.queryTextField.setText("Google");
 whois.queryButton.doClick();
 String expectedOutput = getExpectedOutput("Google filter-set");
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testReset() {
 whois.resetButton.doClick();
 assertTrue(whois.queryTextField.getText().isEmpty());
 }

 @Test
 public void testInvalidSocketConnection() {
 when(socket.isConnected()).thenReturn(false);
 whois.query(query);
 String expectedError = "Whois server could not be contacted.\nPlease check your internet connection.";
 assertEquals(expectedError, outContent.toString());
 }

 private String getExpectedOutput(String query) {
 // Provide expected output for different test cases here
 return "Expected output for query: " + query;
 }

}