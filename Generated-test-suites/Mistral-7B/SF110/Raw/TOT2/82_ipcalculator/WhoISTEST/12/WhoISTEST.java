import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setup() {
 whois = new WhoIS();
 }

 @Test
 public void testQueryButtonActionPerformed() {
 String query = "google.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 String expectedOutput = "WhoIS output for 'google.com'"; // Replace this with expected output for a valid query
 String actualOutput = ""; // Initialize this with the actual output from the test
 try {
 WhoISOutput output = new WhoISOutput(actualOutput);
 assertEquals(expectedOutput, output.getOutput());
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testEmptyQuery() {
 whois.queryButton.doClick();
 String actualOutput = ""; // Initialize this with the actual output from the test
 try {
 WhoISOutput output = new WhoISOutput(actualOutput);
 fail("An exception should have been thrown for an empty query");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testInvalidHost() {
 whois.queryTextField.setText("invalid-host.com");
 whois.queryButton.doClick();
 String actualOutput = ""; // Initialize this with the actual output from the test
 try {
 WhoISOutput output = new WhoISOutput(actualOutput);
 fail("An exception should have been thrown for an invalid host");
 } catch (UnknownHostException e) {
 // Expected exception
 } catch (IOException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testConnectionFailure() {
 whois.queryTextField.setText("google.com");
 when(mockSocketFactory.createSocket(anyString(), anyInt())).thenThrow(new IOException());
 whois.queryButton.doClick();
 String actualOutput = ""; // Initialize this with the actual output from the test
 try {
 WhoISOutput output = new WhoISOutput(actualOutput);
 fail("An exception should have been thrown for a connection failure");
 } catch (IOException e) {
 // Expected exception
 }
 }

}