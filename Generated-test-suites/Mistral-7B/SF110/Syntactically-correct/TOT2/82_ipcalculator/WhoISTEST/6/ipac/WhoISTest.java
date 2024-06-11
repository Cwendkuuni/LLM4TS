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
 public void testConstructor() {
 WhoIS whoisObject = new WhoIS();
 assertNotNull(whoisObject);
 }

 @Test
 public void testQueryPanelCreation() {
 assertNotNull(whois.queryPanel());
 }

 @Test
 public void testAdditionalQueriesPanelCreation() {
 assertNotNull(whois.additionalQueriesPanel());
 }

 @Test
 public void testIPAddressLookupsCreation() {
 assertNotNull(whois.IPAddressLookups());
 }

 @Test
 public void testMiscellaneousQueriesCreation() {
 assertNotNull(whois.MiscellaneousQueries());
 }

 @Test
 public void testQueryHintsPanelCreation() {
 assertNotNull(whois.queryHintsPanel());
 }

 @Test
 public void testCreatePopupMenu() {
 assertNotNull(whois.popupMenu);
 }

 @Test
 public void testActionPerformedWithQueryButton() {
 whois.startQuery();
 // Add assertions here to check the behavior of the application when query button is clicked
 }

 @Test(expected = NullPointerException.class)
 public void testStartQueryWithEmptyQueryTextField() {
 whois.startQuery();
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithUnknownHost() {
 whois.query("unknownhost.com");
 }

 @Test(expected = IOException.class)
 public void testQueryWithIOException() {
 whois.query("whois.validserver.com");
 }

 @Test
 public void testReset() {
 whois.reset();
 // Add assertions here to check the behavior of the application when reset button is clicked
 }

}