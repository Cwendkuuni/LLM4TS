package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;
import ipac. WhoISOutput;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

 private WhoIS whois;
 private String expectedOutput;

 @Before
 public void setup() {
 whois = new WhoIS();
 expectedOutput = "WhoIS output example"; // Replace this with the expected output for the given query
 }

 @Test
 public void testQueryButton() {
 final String query = "example.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 assertNotNull(whois.getQuery());
 assertEquals(expectedOutput, whois.getWhoISOutput().getOutput());
 }

 @Test
 public void testEmptyQuery() {
 whois.queryButton.doClick();
 assertNull(whois.getQuery());
 }

 @Test
 public void testInvalidQuery() {
 whois.queryTextField.setText("invalid query");
 whois.queryButton.doClick();
 assertNull(whois.getQuery());
 }

 @Test
 public void testInvalidHost() {
 whois.queryTextField.setText("invalidhost.com");
 whois.queryButton.doClick();
 assertNull(whois.getQuery());
 }

 @Test
 public void testInverseQueries() {
 whois.inverseQueries.setSelectedIndex(1);
 whois.queryTextField.setText("example.com");
 whois.queryButton.doClick();
 assertEquals("admin-c,tech-c,zone-c", whois.getWhoISOutput().getInverseQueries());
 }

 @Test
 public void testMiscQueries() {
 whois.miscList.setSelectedValue("mntner", false);
 whois.queryTextField.setText("example.com");
 whois.queryButton.doClick();
 assertEquals("mntner", whois.getWhoISOutput().getMiscQueries());
 }

 @Test
 public void testErrorConnection() {
 final String errorMessage = "Whois server could not be contacted.\nPlease check your internet connection.";
 whois.WHOIS_ADDRESS = "nonexistent.com";
 whois.queryButton.doClick();
 assertEquals(errorMessage, whois.getErrorMessage());
 }

 @Test
 public void testErrorUnknownHost() {
 final String errorMessage = "Unknown Whois host: nonexistent.com";
 whois.queryButton.doClick();
 assertEquals(errorMessage, whois.getErrorMessage());
 }

 private String getErrorMessage() {
 String errorMessage = "";
 try {
 whois.query("nonexistent.com");
 }
 catch (UnknownHostException e) {
 errorMessage = e.getMessage();
 }
 catch (IOException e) {
 errorMessage = "Whois server could not be contacted.\nPlease check your internet connection.";
 }
 return errorMessage;
 }

 @Test
 public void testGetQuery() {
 final String query = "example.com";
 whois.queryTextField.setText(query);
 whois.inverseQueries.setSelectedIndex(1);
 whois.lowercaseL.doClick();
 whois.miscList.setSelectedValue("as-set", false);
 whois.queryButton.doClick();
 String actualQuery = whois.getQuery();
 String expectedQuery = "-i admin-c,tech-c,zone-c -l example.com -T as-set " + query;
 assertEquals(expectedQuery, actualQuery);
 }

 @Test
 public void testSocketConnection() throws IOException {
 final String query = "example.com";
 Socket socket = mock(Socket.class);
 when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(expectedOutput.getBytes()));
 when(socket.getOutputStream()).thenReturn(mock(PrintStream.class));
 whois.query(query);
 verify(socket).connect(new InetSocketAddress(whois.WHOIS_ADDRESS, whois.WHOIS_PORT));
 }

}