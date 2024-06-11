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
 public void testCreatePopupMenu() {
 whois.createPopupMenu();
 assertNotNull(whois.popupMenu);
 }

 @Test
 public void testQueryPanelCreate() {
 WhoIS whois = new WhoIS();
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testAdditionalQueriesPanelCreate() {
 WhoIS whois = new WhoIS();
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 @Test
 public void testIPAddressLookupsCreate() {
 WhoIS whois = new WhoIS();
 JPanel IPAddressLookups = whois.IPAddressLookups();
 assertNotNull(IPAddressLookups);
 }

 @Test
 public void testMiscellaneousQueriesCreate() {
 WhoIS whois = new WhoIS();
 JPanel MiscellaneousQueries = whois.MiscellaneousQueries();
 assertNotNull(MiscellaneousQueries);
 }

 @Test
 public void testQueryHintsPanelCreate() {
 WhoIS whois = new WhoIS();
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 @Test
 public void testQueryTextFieldIsEmpty() {
 WhoIS whois = new WhoIS();
 assertTrue(whois.queryTextField.getText().isEmpty());
 }

 @Test
 public void testQueryTextFieldIsNotEmpty() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 assertFalse(whois.queryTextField.getText().isEmpty());
 }

 @Test
 public void testStartQueryWithEmptyTextField() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 assertFalse(whois.queryTextField.getText().isEmpty());
 }

 @Test
 public void testStartQueryWithNonEmptyTextField() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.startQuery();
 // Check if the query method was called
 // Add assertions for the expected query output here
 }

 @Test
 public void testReset() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.startQuery();
 whois.reset();
 assertTrue(whois.queryTextField.getText().isEmpty());
 }

 @Test
 public void testStartQueryWithInvalidWhoisAddress() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = "invalid.address";
 whois.startQuery();
 assertThrows(UnknownHostException.class, () -> whois.query(whois.getQuery()));
 }

 @Test
 public void testStartQueryWithInvalidWhoisPort() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_PORT = -1;
 whois.startQuery();
 assertThrows(IOException.class, () -> whois.query(whois.getQuery()));
 }
}