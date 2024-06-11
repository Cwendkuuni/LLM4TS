import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @BeforeEach
 public void setUp() {
 whois = new WhoIS();
 }

 @Test
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testQueryPanelCreation() {
 assertNotNull(whois.queryPanel());
 }

 @Test
 public void testQueryTextField() {
 JTextField queryTextField = whois.queryPanel().getQueryTextField();
 assertEquals(40, queryTextField.getColumns());
 }

 @Test
 public void testQueryButtonActionPerformed() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 // Add assertions here to check the output of the query
 }

 @Test
 public void testResetButtonActionPerformed() {
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText());
 }

 @Test
 public void testIPAddressLookups() {
 // Test for all radio buttons being checked and unchecked
 whois.lowercaseL.doClick();
 assertTrue(whois.lowercaseL.isSelected());
 assertFalse(whois.uppercaseL.isSelected());
 // ... repeat for other radio buttons
 }

 @Test
 public void testMiscellaneousQueries() {
 // Test for all items in the inverseQueries ComboBox being selected
 whois.inverseQueries.setSelectedIndex(1);
 assertEquals("admin-c", whois.inverseQueries.getSelectedItem().toString());
 // ... repeat for other items
 }

 @Test
 public void testQueryHintsPanel() {
 // Add assertions here to check the contents of the queryHintsPanel
 }

 @Test
 public void testCreatePopupMenu() {
 // Add assertions here to check the contents of the popupMenu
 }

 @Test
 public void testActionPerformed() {
 // Add assertions here to check the behavior of the actionPerformed method
 }

 @Test
 public void testStartQuery() {
 // Add assertions here to check the behavior of the startQuery method
 }

 @Test
 public void testDisplayError() {
 // Add assertions here to check the behavior of the displayError method
 }

 @Test
 public void testQuery() throws UnknownHostException, IOException {
 // Add mocking and assertions here to check the behavior of the query method
 }

 @Test
 public void testGetQuery() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 String expectedQuery = "-i admin-c,tech-c,zone-c example.com";
 String actualQuery = whois.getQuery();
 assertEquals(expectedQuery, actualQuery);
 }

 @Test
 public void testReset() {
 whois.reset();
 assertEquals("", whois.queryTextField.getText());
 }

 @AfterEach
 public void tearDown() {
 whois = null;
 }

}