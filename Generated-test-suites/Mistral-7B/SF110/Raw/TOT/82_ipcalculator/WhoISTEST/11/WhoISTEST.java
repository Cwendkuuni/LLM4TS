package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.InputEvent;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setup() {
 whois = new WhoIS();
 }

 @Test
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.WHOIS_NAME);
 assertNotNull(whois.WHOIS_ADDRESS);
 assertNotNull(whois.WHOIS_PORT);
 }

 @Test
 public void testQueryPanelCreation() {
 assertNotNull(whois.queryPanel());
 }

 @Test
 public void testTextFieldKeyPressedEvent() {
 whois.queryTextField.setText("example.com");
 whois.queryTextField.dispatchEvent(new KeyEvent(whois.queryTextField, 10, false, false, KeyEvent.CHAR_UNDEFINED, 0));
 assertEquals("example.com", whois.getQuery());
 }

 @Test
 public void testTextFieldMousePressedEvent() {
 whois.queryTextField.setText("example.com");
 MouseEvent mouseEvent = new MouseEvent(whois.queryTextField, 1, 1, 1, 1, InputEvent.BUTTON1_DOWN_MASK, false);
 whois.queryTextField.dispatchEvent(mouseEvent);
 assertTrue(whois.popupMenu.isVisible());
 }

 @Test
 public void testResetButtonActionPerformed() {
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText());
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithInvalidHost() {
 whois.query("invalidhost.com");
 }

 @Test(expected = IOException.class)
 public void testQueryWithUnreachableServer() {
 whois.query("whois.google.com:1234");
 }

 @Test
 public void testInverseQuery() {
 whois.inverseQueries.setSelectedIndex(1);
 whois.query("example.com");
 assertTrue(whois.getQuery().startsWith("-i tech-c,zone-c "));
 }

 @Test
 public void testMultipleMiscQueries() {
 whois.miscList.setSelectedValues(new Object[]{"as-set", "role"});
 whois.query("example.com");
 assertTrue(whois.getQuery().endsWith(" -T as-set,role "));
 }

 @Test
 public void testAllMiscQueries() {
 whois.miscList.setSelectedIndex(13); // select "zone-c"
 whois.query("example.com");
 assertTrue(whois.getQuery().endsWith(" -T all "));
 }

 @Test
 public void testBooleanDCheckbox() {
 whois.lowercaseD.doClick();
 whois.query("example.com");
 assertTrue(whois.getQuery().startsWith("-d "));
 }
}