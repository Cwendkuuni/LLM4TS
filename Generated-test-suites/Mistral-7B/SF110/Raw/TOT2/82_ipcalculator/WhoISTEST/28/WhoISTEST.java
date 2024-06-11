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
 assertTrue(whois.lowercaseL.isVisible());
 assertTrue(whois.uppercaseL.isVisible());
 assertTrue(whois.lowercaseM.isVisible());
 assertTrue(whois.uppercaseM.isVisible());
 assertTrue(whois.lowercaseX.isVisible());
 assertTrue(whois.lowercaseD.isVisible());
 }

 @Test
 public void testMiscellaneousQueries() {
 WhoIS whois = new WhoIS();
 JPanel miscPanel = whois.MiscellaneousQueries();
 assertNotNull(miscPanel);
 assertNotNull(whois.inverseQueries);
 assertNotNull(whois.miscList);
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
 assertNotNull(whois.popupMenu);
 }

 @Test
 public void testActionPerformed() {
 WhoIS whois = new WhoIS();
 MockActionEvent event = new MockActionEvent("Query");
 whois.actionPerformed(event);
 whois.startQuery();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartQueryEmptyQuery() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 }

 @Test
 public void testQuery() {
 WhoIS whois = new WhoIS();
 whois.query("google.com");
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryUnknownHost() {
 WhoIS whois = new WhoIS();
 whois.query("unknown.host");
 }

 @Test(expected = IOException.class)
 public void testQueryIOException() {
 WhoIS whois = new WhoIS();
 whois.query("google.com");
 }
}