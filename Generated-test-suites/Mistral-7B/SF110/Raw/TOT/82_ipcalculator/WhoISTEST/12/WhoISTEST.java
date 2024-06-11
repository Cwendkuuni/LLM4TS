import org.junit.Assert;
import org.junit.Test;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @Test
 public void testWhoISInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertEquals("APNIC", whois.WHOIS_NAME);
 Assert.assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
 Assert.assertEquals(43, whois.WHOIS_PORT);
 }

 @Test
 public void testQueryTextFieldInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.queryTextField);
 Assert.assertEquals(40, whois.queryTextField.getColumns());
 }

 @Test
 public void testQueryButtonInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.queryButton);
 }

 @Test
 public void testResetButtonInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.resetButton);
 }

 @Test
 public void testLowercaseLInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.lowercaseL);
 Assert.assertEquals("1st level less specific", whois.lowercaseL.getText());
 }

 @Test
 public void testUppercaseLInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.uppercaseL);
 Assert.assertEquals("All less specific", whois.uppercaseL.getText());
 }

 @Test
 public void testLowercaseMInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.lowercaseM);
 Assert.assertEquals("1st level more specific", whois.lowercaseM.getText());
 }

 @Test
 public void testUppercaseMInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.uppercaseM);
 Assert.assertEquals("All more specific", whois.uppercaseM.getText());
 }

 @Test
 public void testLowercaseXInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.lowercaseX);
 Assert.assertEquals("Exact match only", whois.lowercaseX.getText());
 }

 @Test
 public void testResetRadioButtonInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.resetRadioButton);
 }

 @Test
 public void testCreatePopupMenuInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.popupMenu);
 }

 @Test
 public void testInverseQueriesInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.inverseQueries);
 Assert.assertEquals(15, whois.inverseQueries.getItemCount());
 }

 @Test
 public void testMiscListInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.miscList);
 }

 @Test
 public void testQueryHintsPanelInitialization() {
 WhoIS whois = new WhoIS();
 Assert.assertNotNull(whois.queryHintsPanel);
 }

 @Test(expected = NullPointerException.class)
 public void testNullQueryTextField() {
 WhoIS whois = new WhoIS();
 whois.queryTextField = null;
 whois.startQuery();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyQueryTextField() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("");
 whois.startQuery();
 }

 @Test(expected = IllegalStateException.class)
 public void testCheckForTriggerEventNullComponent() {
 WhoIS whois = new WhoIS();
 MouseEvent e = null;
 whois.popupMenu.show(e, 0, 0);
 }

 @Test(expected = IOException.class)
 public void testQueryIOException() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = "invalid-address.com";
 whois.startQuery();
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHostException() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = "unknown-host.com";
 whois.startQuery();
 }

 @Test(expected = SecurityException.class)
 public void testQuerySecurityException() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = "localhost";
 whois.queryTextField.setText("127.0.0.1");
 whois.startQuery();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPort() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_PORT = 1234;
 whois.startQuery();
 }

 @Test
 public void testValidQuery() {
 WhoIS whois = new WhoIS();whois.queryTextField.setText("google.com");
 whois.startQuery();
 // Assuming the query returns valid output, add assertions to check the output here
 }
}