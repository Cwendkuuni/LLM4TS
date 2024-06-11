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
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testQueryPanel() {
 WhoIS whois = new WhoIS();
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testAdditionalQueriesPanel() {
 WhoIS whois = new WhoIS();
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 @Test
 public void testIPAddressLookups() {
 WhoIS whois = new WhoIS();
 JPanel lookupPanel = whois.IPAddressLookups();
 assertNotNull(lookupPanel);
 }

 @Test
 public void testMiscellaneousQueries() {
 WhoIS whois = new WhoIS();
 JPanel miscPanel = whois.MiscellaneousQueries();
 assertNotNull(miscPanel);
 }

 @Test
 public void testQueryHintsPanel() {
 WhoIS whois = new WhoIS();
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 @Test
 public void testCreatePopupMenu() {
 WhoIS whois = new WhoIS();
 JPopupMenu popupMenu = whois.popupMenu;
 assertNotNull(popupMenu);
 }

 @Test
 public void testActionPerformedQueryButton() {
 WhoIS whois = new WhoIS();
 String testQuery = "example.com";
 whois.queryTextField.setText(testQuery);
 whois.queryButton.doClick();
 // Add assertions here to test the output of the query
 }

 @Test
 public void testActionPerformedResetButton() {
 WhoIS whois = new WhoIS();
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText());
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHostException() {
 WhoIS whois = new WhoIS();
 whois.query("non-existent-server.com");
 }

 @Test(expected = IOException.class)
 public void testIOException() {
 WhoIS whois = new WhoIS();
 whois.query("whois.example.com");
 }

 @Test
 public void testGetQuery() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("test.com");
 whois.inverseQueries.setSelectedIndex(1);
 whois.lowercaseL.setSelected(true);
 whois.lowercaseD.setSelected(true);
 whois.miscList.setSelectedValue("aut-num", false);
 whois.miscList.setSelectedValue("filter-set", true);
 String expectedQuery = "-i admin-c -l test.com -d";
 String actualQuery = whois.getQuery();
 assertEquals(expectedQuery, actualQuery);
 }
}