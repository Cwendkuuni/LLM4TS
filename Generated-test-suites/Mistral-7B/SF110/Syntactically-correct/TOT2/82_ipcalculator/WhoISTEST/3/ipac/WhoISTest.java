import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois = new WhoIS();

 @Test
 public void testConstructor() {
 WhoIS whoisObject = new WhoIS();
 assertNotNull(whoisObject);
 }

 @Test
 public void testQueryPanelCreation() {
 WhoIS whoisObject = new WhoIS();
 JPanel queryPanel = whoisObject.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testQueryTextField() {
 WhoIS whoisObject = new WhoIS();
 JTextField queryTextField = whoisObject.queryTextField;
 assertNotNull(queryTextField);
 assertEquals(40, queryTextField.getColumns());
 }

 @Test
 public void testQueryButton() {
 WhoIS whoisObject = new WhoIS();
 JButton queryButton = whoisObject.queryButton;
 assertNotNull(queryButton);
 }

 @Test
 public void testResetButton() {
 WhoIS whoisObject = new WhoIS();
 JButton resetButton = whoisObject.resetButton;
 assertNotNull(resetButton);
 }

 @Test
 public void testStartQueryWithValidQuery() {
 WhoIS whoisObject = new WhoIS();
 whoisObject.queryTextField.setText("example.com");
 whoisObject.startQuery();
 // Add assertions for successful query response here
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartQueryWithEmptyQuery() {
 WhoIS whoisObject = new WhoIS();
 whoisObject.startQuery();
 }

 @Test
 public void testDisplayError() {
 WhoIS whoisObject = new WhoIS();
 whoisObject.displayError("Error Message");
 // Add assertions for error message display here
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithInvalidHost() {
 WhoIS whoisObject = new WhoIS();
 whoisObject.WHOIS_ADDRESS = "InvalidHost";
 whoisObject.startQuery();
 }

 @Test(expected = IOException.class)
 public void testQueryWithIOException() {
 WhoIS whoisObject = new WhoIS();
 whoisObject.WHOIS_ADDRESS = "valid_host";
 whoisObject.query("InvalidQuery");
 }

 @Test
 public void testReset() {
 WhoIS whoisObject = new WhoIS();
 whoisObject.reset();
 String query = whoisObject.queryTextField.getText();
 assertTrue(query.isEmpty());
 }

}