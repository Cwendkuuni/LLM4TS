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
 public void testQueryPanelCreation() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 assertTrue(queryPanel.getComponentCount() > 0);
 assertEquals(TextField.class, queryPanel.getComponent(0).getClass());
 assertEquals(JButton.class, queryPanel.getComponent(queryPanel.getComponentCount() - 1).getClass());
 }

 @Test
 public void testQueryTextFieldFocus() {
 whois.queryTextField.requestFocus();
 assertTrue(whois.queryTextField.hasFocus());
 }

 @Test
 public void testQueryTextFieldKeyPress() {
 whois.queryTextField.setText("example.com");
 whois.queryTextField.dispatchEvent(new KeyEvent(whois.queryTextField, KeyEvent.KEY_PRESSED, 0, KeyEvent.VK_ENTER, 1));
 whois.startQuery();
 // Add assertions for expected query and response here
 }

 @Test
 public void testQueryButtonAction() {
 whois.queryTextField.setText("example.com");
 whois.queryButton.doClick();
 whois.startQuery();
 // Add assertions for expected query and response here
 }

 @Test
 public void testResetButtonAction() {
 whois.reset();
 assertEquals("", whois.queryTextField.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPortNumber() {
 WhoIS whois2 = new WhoIS();
 whois2.WHOIS_PORT = -1;
 try {
 whois2.query("example.com");
 fail("IllegalArgumentException expected");
 }
 catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHost() {
 try {
 WhoIS whois3 = new WhoIS();
 whois3.WHOIS_ADDRESS = "invalidhost.com";
 whois3.query("example.com");
 fail("UnknownHostException expected");
 }
 catch (UnknownHostException e) {
 // Pass
 }
 }

 @Test(expected = IOException.class)
 public void testIOException() {
 try {
 WhoIS whois4 = new WhoIS();
 whois4.query("example.com");
 fail("IOException expected");
 }
 catch (IOException e) {
 // Pass
 }
 }

}