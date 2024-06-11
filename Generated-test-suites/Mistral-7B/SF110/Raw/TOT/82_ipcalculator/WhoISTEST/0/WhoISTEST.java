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
 String query = "example.com";
 whois.queryTextField.setText(query);
 ActionEvent queryButtonEvent = new ActionEvent(whois.queryButton, 0, "actionPerformed");
 whois.actionPerformed(queryButtonEvent);
 assertEquals("Query:\n" + query + "\n", outContent.toString());
 }

 @Test
 public void testResetButtonActionPerformed() {
 whois.resetButton.doClick();
 assertTrue(whois.queryTextField.getText().isEmpty());
 }

 @Test
 public void testEmptyQuery() {
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String error = outContent.toString();
 assertTrue(error.contains("No Query Detected"));
 }

 @Test
 public void testInvalidHost() {
 whois.queryTextField.setText("invalidhost.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String error = outContent.toString();
 assertTrue(error.contains("Unknown Whois host: invalidhost.com"));
 }

 @Test
 public void testConnectionError() throws IOException {
 inContent = new ByteArrayInputStream(new byte[0]);
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String error = outContent.toString();
 assertTrue(error.contains("Whois server could not be contacted.\nPlease check your internet connection."));
 }

 @Test
 public void testInverseQueries() {
 whois.inverseQueries.setSelectedIndex(1);
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n-i tech-c example.com\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMultipleSelectedOptions() {
 whois.inverseQueries.setSelectedIndices(new int[] { 1, 2, 3 });
 whois.miscList.setSelectedValues(Arrays.asList("as-block", "filter-set").toArray());
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n-i tech-c, filter-set, as-block example.com\n-T as-block, filter-set example.com\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testQueryHints() {
 whois.queryTextField.setText("AS4808");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\nAS4808\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testQueryHintsWithTemplate() {
 whois.queryTextField.setText("inetnum");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n-t inetnum\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMultipleQueries() throws IOException {
 whois.queryTextField.setText("example1.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String firstOutput = outContent.toString();
 whois.queryTextField.setText("example2.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String secondOutput = outContent.toString();
 assertTrue(firstOutput.contains("example1.com"));
 assertTrue(secondOutput.contains("example2.com"));
 }

 @Test
 public void testLowercaseQueries() {
 whois.lowercaseL.doClick();
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n-l example.com\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testUppercaseQueries() {
 whois.uppercaseL.doClick();
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n-L example.com\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMiscellaneousQueries() {
 whois.lowercaseM.doClick();
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n-m example.com\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMiscellaneousQueriesUppercase() {
 whois.uppercaseM.doClick();
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n-M example.com\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testBooleanOption() {
 whois.lowercaseD.doClick();
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n-d example.com\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testMultipleBooleanOptions() {
 whois.lowercaseD.doClick();
 whois.lowercaseL.doClick();
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n-d example.com -l example.com\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testReset() {
 whois.resetButton.doClick();
 assertTrue(whois.queryTextField.getText().isEmpty());
 }

 @Test
 public void testIPv6Query() {
 whois.queryTextField.setText("2001:db8:0:1234:0:567:8:1");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String expectedOutput = "Query:\n2001:db8:0:1234:0:567:8:1\n";
 assertEquals(expectedOutput, outContent.toString());
 }

 @Test
 public void testSocketException() throws IOException {
 Socket socket = new Socket("localhost", 1234);
 socket.close();
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "actionPerformed"));
 String error = outContent.toString();
 assertTrue(error.contains("Socket closed"));
 }
}