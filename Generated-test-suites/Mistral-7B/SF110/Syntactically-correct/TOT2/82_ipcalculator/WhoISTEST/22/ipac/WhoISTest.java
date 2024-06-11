import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 // Test case for constructor
 @Test
 public void testWhoISConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 // Test case for queryPanel method
 @Test
 public void testQueryPanelMethod() {
 WhoIS whois = new WhoIS();
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 // Test case for queryTextField initialization
 @Test
 public void testQueryTextFieldInitialization() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.queryTextField);
 assertEquals("", whois.queryTextField.getText());
 }

 // Test case for queryTextField key press event
 @Test
 public void testQueryTextFieldKeyPressEvent() {
 WhoIS whois = new WhoIS();
 MockComponentEvent event = new MockComponentEvent(KeyEvent.KEY_PRESSED, 10);
 whois.queryTextField.dispatchEvent(event);
 assertTrue(whois.startQueryCalled); // Assuming startQuery is a method that gets called when enter key is pressed
 }

 // Test case for queryButton action event
 @Test
 public void testQueryButtonActionEvent() {
 WhoIS whois = new WhoIS();
 MockActionEvent event = new MockActionEvent();
 whois.queryButton.addActionListener(whois);
 whois.queryButton.actionPerformed(event);
 assertTrue(whois.startQueryCalled);
 }

 // Test case for getQuery method with valid input
 @Test
 public void testGetQueryValidInput() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("example.com", query);
 }

 // Test case for getQuery method with inverse queries
 @Test
 public void testGetQueryInverseQueries() {
 WhoIS whois = new WhoIS();
 whois.inverseQueries.setSelectedIndex(1);
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("-i tech-c example.com", query);
 }

 // Test case for getQuery method with lowercase L
 @Test
 public void testGetQueryLowercaseL() {
 WhoIS whois = new WhoIS();
 whois.lowercaseL.setSelected(true);
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("-l example.com", query);
 }

 // Test case for getQuery method with uppercase L
 @Test
 public void testGetQueryUppercaseL() {
 WhoIS whois = new WhoIS();
 whois.uppercaseL.setSelected(true);
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("-L example.com", query);
 }

 // Test case for getQuery method with lowercase M
 @Test
 public void testGetQueryLowercaseM() {
 WhoIS whois = new WhoIS();
 whois.lowercaseM.setSelected(true);
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("-m example.com", query);
 }

 // Test case for getQuery method with uppercase M
 @Test
 public void testGetQueryUppercaseM() {
 WhoIS whois = new WhoIS();
 whois.uppercaseM.setSelected(true);
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("-M example.com", query);
 }

 // Test case for getQuery method with lowercase X
 @Test
 public void testGetQueryLowercaseX() {
 WhoIS whois = new WhoIS();
 whois.lowercaseX.setSelected(true);
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("-x example.com", query);
 }

 // Test case for getQuery method with boolean D
 @Test
 public void testGetQueryBooleanD() {
 WhoIS whois = new WhoIS();
 whois.lowercaseD.setSelected(true);
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("-d example.com", query);
 }

 // Test case for getQuery method with miscQueries
 @Test
 public void testGetQueryMiscQueries() {
 WhoIS whois = new WhoIS();
 Object[] selected = {"inetnum", "mntner"};
 whois.miscList.setSelectedValues(selected);
 whois.queryTextField.setText("example.com");
 String query = whois.getQuery();
 assertEquals("-T inetnum,mntner example.com", query);
 }

 // Test case for reset method
 @Test
 public void testResetMethod() {
 WhoIS whois = new WhoIS();
 whois.reset();
 assertTrue(whois.queryTextField.getText().isEmpty());
 assertTrue(whois.resetRadioButton.isSelected());
 assertFalse(whois.lowercaseD.isSelected());
 assertNull(whois.inverseQueries.getSelectedItem());
 assertNull(whois.miscList.getSelectedValue());
 }

 // Test case for query method with valid input
 @Test
 public void testQueryValidInput() throws IOException {
 WhoIS whois = new WhoIS();
 String query = whois.getQuery();
 whois.query(query);
 // Add assertions for expected output here
 }

 // Test case for query method with unknown host exception
 @Test (expected = UnknownHostException.class)
 public void testQueryUnknownHostException() throws IOException {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = "unknown.host";
 String query = whois.getQuery();
 whois.query(query);
 }

 // Test case for query method with IO exception
 @Test (expected = IOException.class)
 public void testQueryIOException() throws IOException {
 WhoIS whois = new WhoIS();
 doReturn(null).when(whois).socket(anyString(), anyInt());
 String query = whois.getQuery();
 whois.query(query);
 }
}