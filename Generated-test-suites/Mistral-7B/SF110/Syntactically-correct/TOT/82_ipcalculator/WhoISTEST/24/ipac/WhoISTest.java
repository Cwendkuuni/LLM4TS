import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @Test
 public void testQueryButtonActionPerformed() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 // Verify that the query method is called and returns the expected response
 // For a complete test, you may need to mock the network connection and the WhoISOutput
 // class and verify their interactions
 }

 @Test
 public void testEmptyQuery() {
 whois.queryButton.doClick();
 String errorMessage = whois.displayError("");
 assertEquals("Error", "Error", errorMessage);
 }

 @Test
 public void testInvalidQuery() {
 whois.queryTextField.setText("invalid-query");
 whois.queryButton.doClick();
 String errorMessage = whois.displayError("");
 assertEquals("Error", "Error", errorMessage);
 }

 @Test
 public void testMalformedQuery() {
 whois.queryTextField.setText("query-error: invalid-query");
 whois.queryButton.doClick();
 String errorMessage = whois.displayError("");
 assertEquals("Error", "Error", errorMessage);
 }

 @Test
 public void testInverseQueries() {
 whois.inverseQueries.setSelectedIndex(2);
 whois.queryTextField.setText("example.com");
 whois.queryButton.doClick();
 // Verify that the inverse queries are added to the query string
 // and that the query method returns the expected response
 }

 @Test
 public void testIPv6Query() {
 whois.queryTextField.setText("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
 whois.queryButton.doClick();
 // Verify that the query method is called with the correct IPv6 address
 // and that it returns the expected response
 }

 @Test
 public void testErrorHandling() {
 whois.WHOIS_ADDRESS = "non-existent-whois-server.com";
 whois.queryButton.doClick();
 String errorMessage = whois.displayError("");
 assertEquals("Error", "Whois server could not be contacted.\nPlease check your internet connection.", errorMessage);
 }

 @Test
 public void testCutCopyPaste() {
 whois.queryTextField.setText("example.com");
 whois.queryTextField.cut();
 String cutText = whois.queryTextField.getText();
 whois.queryTextField.paste();
 String pasteText = whois.queryTextField.getText();
 assertEquals("example.com", cutText);
 assertEquals("example.comexample.com", pasteText);
 }
}