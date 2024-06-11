package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;

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
 public void testQueryButtonActionPerformedWithValidQuery() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 // Assuming the test for query method is implemented separately
 // Add assertions based on the output of query method here
 }

 @Test
 public void testQueryButtonActionPerformedWithEmptyQuery() {
 whois.queryButton.doClick();
 // Add assertions based on the expected error message here
 }

 @Test
 public void testQueryButtonActionPerformedWithInvalidQuery() {
 whois.queryTextField.setText("invalidQuery");
 whois.queryButton.doClick();
 // Add assertions based on the expected error message here
 }

 @Test
 public void testResetButtonActionPerformed() {
 whois.resetButton.doClick();
 // Add assertions based on the expected reset state here
 }

 @Test
 public void testMouseEventOnQueryTextField() {
 MouseEvent mouseEvent = new MouseEvent(whois.queryTextField, 1, 1, 1, 1);
 whois.queryTextField.dispatchEvent(mouseEvent);
 // Add assertions based on the expected popup menu behavior here
 }

 @Test
 public void testSocketConnectionException() {
 whois.WHOIS_ADDRESS = "nonexistent.com";
 whois.startQuery();
 // Add assertions based on the expected error message here
 }

 @Test
 public void testIOException() {
 whois.WHOIS_ADDRESS = "whois.apnic.net";
 whois.WHOIS_PORT = 1234; // Port number not used by Whois
 whois.startQuery();
 // Add assertions based on the expected error message here
 }

}