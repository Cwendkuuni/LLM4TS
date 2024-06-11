import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 @Test
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testQueryPanelCreation() {
 WhoIS whois = new WhoIS();
 WhoISPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testQueryTextField() {
 WhoIS whois = new WhoIS();
 JTextField queryTextField = whois.queryTextField;
 assertNotNull(queryTextField);
 }

 @Test
 public void testQueryButton() {
 WhoIS whois = new WhoIS();
 JButton queryButton = whois.queryButton;
 assertNotNull(queryButton);
 }

 @Test
 public void testResetButton() {
 WhoIS whois = new WhoIS();
 JButton resetButton = whois.resetButton;
 assertNotNull(resetButton);
 }

 @Test
 public void testCreatePopupMenu() {
 WhoIS whois = new WhoIS();
 JPopupMenu popupMenu = whois.popupMenu;
 assertNotNull(popupMenu);
 }

 @Test
 public void testActionPerformed() {
 WhoIS whois = new WhoIS();
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "actionPerformed");
 whois.actionPerformed(actionEvent);
 }

 @Test
 public void testStartQueryWithEmptyQuery() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 assertTrue(whois.displayErrorCalled); // Assuming displayError has a flag to check if it was called
 }

 @Test
 public void testQueryWithValidQuery() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.startQuery();
 assertFalse(whois.displayErrorCalled);
 }

 @Test
 public void testQueryWithInvalidQuery() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("invalid_query");
 whois.startQuery();
 assertTrue(whois.displayErrorCalled);
 }

 @Test
 public void testQueryWithInvalidWhoisServer() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = "invalid_whois_server";
 whois.startQuery();
 assertTrue(whois.displayErrorCalled);
 }

 @Test
 public void testQueryWithInvalidPort() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_PORT = -1;
 whois.startQuery();
 assertTrue(whois.displayErrorCalled);
 }

 @Test
 public void testQueryWithNullInput() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText(null);
 whois.startQuery();
 assertTrue(whois.displayErrorCalled);
 }

 @Test
 public void testQueryWithEmptyInput() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("");
 whois.startQuery();
 assertTrue(whois.displayErrorCalled);
 }

 @Test
 public void testInverseQueries() {
 WhoIS whois = new WhoIS();
 whois.inverseQueries.setSelectedItem("admin-c");
 String query = whois.getQuery();
 assertEquals("-i admin-c <query_text>", query);
 }

 @Test
 public void testIPAddressLookups() {
 WhoIS whois = new WhoIS();
 whois.lowercaseL.doClick();
 String query = whois.getQuery();
 assertEquals("-l <query_text>", query);
 }

 @Test
 public void testMiscellaneousQueries() {
 WhoIS whois = new WhoIS();
 String[] selectedValues = {"as-block"};
 whois.miscList.setSelectedValues(selectedValues);
 String query = whois.getQuery();
 assertEquals("-T as-block <query_text>", query);
 }
}