package ipac.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @Test
 public void testStartQuery() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 verify(whois).query(query);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartQueryEmptyQuery() {
 whois.startQuery();
 }

 @Test
 public void testDisplayError() {
 whois.displayError("Error Message");
 assertEquals("Error Message", JOptionPane.showMessageDialog(whois, "Error Message", "Error", 0).getMessage());
 }

 @Test
 public void testQuery() throws IOException {
 String expectedOutput = "..."; // Replace with expected output from WhoIS server
 String query = "example.com";
 Socket socket = mock(Socket.class);
 when(socket.getInputStream()).thenReturn(createMockInputStream(expectedOutput));
 when(socket.getOutputStream()).thenReturn(mock(PrintStream.class));
 when(socket.connect(argThat(isA(InetSocketAddress.class)))).thenReturn(socket);
 whois.query(query);
 assertEquals(expectedOutput, new String(whois.queryTextField.getText().getBytes()));
 }

 @Test
 public void testGetQuery() {
 String query = "example.com";
 String expectedQuery = "-i None -l example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testGetQueryWithInverseQueries() {
 String query = "example.com";
 whois.inverseQueries.setSelectedIndex(1);
 String expectedQuery = "-i admin-c -l example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testGetQueryWithLowercaseL() {
 String query = "example.com";
 whois.lowercaseL.setSelected(true);
 String expectedQuery = "-l example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testGetQueryWithUppercaseL() {
 String query = "example.com";
 whois.uppercaseL.setSelected(true);
 String expectedQuery = "-L example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testGetQueryWithLowercaseM() {
 String query = "example.com";
 whois.lowercaseM.setSelected(true);
 String expectedQuery = "-m example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testGetQueryWithUppercaseM() {
 String query = "example.com";
 whois.uppercaseM.setSelected(true);
 String expectedQuery = "-M example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testGetQueryWithLowercaseX() {
 String query = "example.com";
 whois.lowercaseX.setSelected(true);
 String expectedQuery = "-x example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testGetQueryWithBooleanD() {
 String query = "example.com";
 whois.lowercaseD.setSelected(true);
 String expectedQuery = "-d example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testGetQueryWithMiscQueries() {
 String query = "example.com";
 Object[] selected = {"inetnum"};
 whois.miscList.setSelectedValues(selected);
 String expectedQuery = "-T inetnum -l example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testGetQueryWithAllMiscQueries() {
 String query = "example.com";
 Object[] selected = {"All"};
 whois.miscList.setSelectedValues(selected);
 String expectedQuery = "-T All -l example.com";
 assertEquals(expectedQuery, whois.getQuery());
 }

 @Test
 public void testReset() {
 whois.reset();
 assertEquals("", whois.queryTextField.getText());
 }

 private InputStream createMockInputStream(String input) {
 InputStream inputStream = new ByteArrayInputStream(input.getBytes());
 return inputStream;
 }

}