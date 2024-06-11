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
 public void setUp() throws Exception {
 whois = new WhoIS();
 outContent = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outContent));
 inContent = new ByteArrayInputStream(new byte[0]);
 System.setIn(inContent);
 }

 @Test
 public void testQueryButtonActionPerformed_ValidQuery() throws IOException {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 String expectedOutput = "..."; // Expected output from WhoIS server
 String output = outContent.toString();
 assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testQueryButtonActionPerformed_EmptyQuery() {
 whois.queryTextField.setText("");
 whois.queryButton.doClick();
 String output = outContent.toString();
 assertEquals("", output);
 }

 @Test
 public void testQueryButtonActionPerformed_InvalidQuery() {
 whois.queryTextField.setText("invalidQuery");
 whois.queryButton.doClick();
 String output = outContent.toString();
 assertTrue(output.isEmpty());
 }

 @Test
 public void testQueryButtonActionPerformed_ErrorConnection() throws IOException {
 whois.WHOIS_ADDRESS = "invalidWhoisServer.com";
 whois.queryTextField.setText("example.com");
 whois.queryButton.doClick();
 String output = outContent.toString();
 assertEquals("Unknown Whois host: invalidWhoisServer.com", output);
 }

 @Test
 public void testQueryButtonActionPerformed_ErrorQuery() throws IOException, InterruptedException {
 whois.WHOIS_ADDRESS = "whois.example.com";
 Socket socket = new Socket(whois.WHOIS_ADDRESS, WhoIS.WHOIS_PORT);
 InputStream input = socket.getInputStream();
 PrintStream output = new PrintStream(socket.getOutputStream());
 output.println("Error Query");
 socket.shutdownOutput();
 whois.queryTextField.setText("example.com");
 whois.queryButton.doClick();
 String expectedOutput = "Error Query";
 String outputContent = new String(inContent.readAllBytes());
 assertEquals(expectedOutput, outputContent);
 }

 @Test
 public void testResetButtonActionPerformed() {
 whois.queryTextField.setText("example.com");
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText());
 }

 @Test
 public void testInverseQueries_ValidQueries() {
 whois.queryTextField.setText("author");
 whois.inverseQueries.setSelectedIndex(1);
 whois.queryButton.doClick();
 String expectedOutput = "..."; // Expected output from WhoIS server
 String output = outContent.toString();
 assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testInverseQueries_MultipleQueries() {
 whois.queryTextField.setText("admin-c,tech-c,zone-c");
 whois.inverseQueries.setSelectedIndex(2);
 whois.queryButton.doClick();
 String expectedOutput = "..."; // Expected output from WhoIS server
 String output = outContent.toString();
 assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testInverseQueries_None() {
 whois.queryTextField.setText("example.com");
 whois.inverseQueries.setSelectedIndex(0);
 whois.queryButton.doClick();
 String output = outContent.toString();
 assertTrue(output.isEmpty());
 }

 @Test
 public void testMiscQueries_All() {
 whois.queryTextField.setText("example.com");
 whois.miscList.setSelectedValue("All");
 whois.queryButton.doClick();
 String expectedOutput = "..."; // Expected output from WhoIS server
 String output = outContent.toString();
 assertTrue(output.contains(expectedOutput));
 }

 @Test
 public void testMiscQueries_Specific() {
 whois.queryTextField.setText("example.com");
 String[] specificQueries = { "inetnum", "mntner" };
 Arrays.stream(specificQueries).forEach(q -> whois.miscList.setSelectedValue(q));
 whois.queryButton.doClick();
 String expectedOutput = "..."; // Expected output from WhoIS server
 String output = outContent.toString();
 assertTrue(output.contains(expectedOutput));
 }
}