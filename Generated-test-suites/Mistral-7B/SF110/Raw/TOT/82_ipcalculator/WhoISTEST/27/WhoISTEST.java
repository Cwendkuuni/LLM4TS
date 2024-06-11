package ipac.test;

import ipac.WhoIS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class WhoISTest {

 private WhoIS whois;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStream;

 @Before
 public void setUp() {
 whois = new WhoIS();
 outputStream = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream));
 inputStream = new ByteArrayInputStream(new byte[0]);
 System.setIn(inputStream);
 }

 @After
 public void tearDown() {
 System.setOut(System.out);
 System.setIn(System.in);
 }

 @Test
 public void testQueryButtonActionPerformed() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 mockQueryButtonActionEvent(query);
 assertEquals("Expected query to be sent to the server", query, whois.getQuery());
 }

 @Test
 public void testEmptyQueryTextField() {
 mockQueryButtonActionEvent(null);
 assertNull("Expected no query to be sent to the server", whois.getQuery());
 }

 @Test
 public void testErrorQuery() {
 whois.queryTextField.setText("invalid-query");
 mockQueryButtonActionEvent(whois.getQuery());
 assertFalse("Expected error message to be displayed", whois.isQuerySuccessful());
 }

 @Test
 public void testQueriesWithInverseQueries() {
 String query = "example.com admin-c";
 whois.queryTextField.setText(query);
 mockQueryButtonActionEvent(query);
 String expectedOutput = "VRSNOTICE: WhoIsService: query: example.com admin-c\n";
 assertEquals("Expected the inverse query to be included in the query", expectedOutput, whois.getOutput());
 }

 @Test
 public void testQueriesWithLowercaseL() {
 String query = "example.net";
 whois.lowercaseL.doClick();
 whois.queryTextField.setText(query);
 mockQueryButtonActionEvent(query);
 String expectedOutput = "2 objects found from APNIC WHOIS server.\n";
 assertEquals("Expected the '1st level less specific' query to be sent and the correct output to be displayed", expectedOutput, whois.getOutput());
 }

 @Test
 public void testQueriesWithUppercaseL() {
 String query = "example.net";
 whois.uppercaseL.doClick();
 whois.queryTextField.setText(query);
 mockQueryButtonActionEvent(query);
 String expectedOutput = "13 objects found from APNIC WHOIS server.\n";
 assertEquals("Expected the 'All less specific' query to be sent and the correct output to be displayed", expectedOutput, whois.getOutput());
 }

 @Test
 public void testQueriesWithLowercaseM() {
 String query = "example.org";
 whois.lowercaseM.doClick();
 whois.queryTextField.setText(query);
 mockQueryButtonActionEvent(query);
 String expectedOutput = "1 object found from APNIC WHOIS server.\n";
 assertEquals("Expected the '1st level more specific' query to be sent and the correct output to be displayed", expectedOutput, whois.getOutput());
 }

 @Test
 public void testQueriesWithUppercaseM() {
 String query = "example.org";
 whois.uppercaseM.doClick();
 whois.queryTextField.setText(query);
 mockQueryButtonActionEvent(query);
 String expectedOutput = "10 objects found from APNIC WHOIS server.\n";
 assertEquals("Expected the 'All more specific' query to be sent and the correct output to be displayed", expectedOutput, whois.getOutput());
 }

 @Test
 public void testQueriesWithLowercaseX() {
 String query = "example.com";
 whois.lowercaseX.doClick();
 whois.queryTextField.setText(query);
 mockQueryButtonActionEvent(query);
 String expectedOutput = "1 object found from APNIC WHOIS server.\n";
 assertEquals("Expected the 'Exact match only' query to be sent and the correct output to be displayed", expectedOutput, whois.getOutput());
 }

 @Test
 public void testQueriesWithBooleanD() {
 String query = "example.net";
 whois.lowercaseD.doClick();
 whois.queryTextField.setText(query);
 mockQueryButtonActionEvent(query);
 String expectedOutput = "1 object found from APNIC WHOIS server.\n" + "inetnum: 192.0.2.1\n" + "netname: NET-2-2\n" + "descr: Example Network\n";
 assertEquals("Expected the 'Associated reverse domain' query to be sent and the correct output to be displayed", expectedOutput, whois.getOutput());
 }

 @Test
 public void testQueriesWithMiscQueries() {
 String query = "example.com";
 whois.miscList.setSelectedValues(new Object[]{"mntner"});
 whois.queryTextField.setText(query);
 mockQueryButtonActionEvent(query);
 String expectedOutput = "VRSNOTICE: WhoIsService: query: example.com mntner\n" + "mntner: APNIC\n";
 assertEquals("Expected the 'mntner' query to be included in the query and the correct output to be displayed", expectedOutput, whois.getOutput());
 }

 private void mockQueryButtonActionEvent(String query) {
 ActionEvent event = new ActionEvent(whois.queryButton, 0, "Query");
 whois.actionPerformed(event);
 InputStream input = new ByteArrayInputStream(query.getBytes());
 System.setIn(input);
 }

 private String getOutput() {
 String output = outputStream.toString();
 outputStream.reset();
 return output;
 }

 private boolean isQuerySuccessful() {
 String output = getOutput();
 return output.contains("objects found from APNIC WHOIS server.");
 }

}