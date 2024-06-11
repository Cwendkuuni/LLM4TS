import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois = new WhoIS();

 @Test
 public void testConstructor() {
 WhoIS whoIsObject = new WhoIS();
 assertNotNull(whoIsObject);
 }

 @Test
 public void testQueryPanelCreation() {
 WhoIS whoIsObject = new WhoIS();
 assertNotNull(whoIsObject.queryPanel());
 }

 @Test
 public void testAdditionalQueriesPanelCreation() {
 WhoIS whoIsObject = new WhoIS();
 assertNotNull(whoIsObject.additionalQueriesPanel());
 }

 @Test
 public void testIPAddressLookupsCreation() {
 WhoIS whoIsObject = new WhoIS();
 assertNotNull(whoIsObject.IPAddressLookups());
 }

 @Test
 public void testMiscellaneousQueriesCreation() {
 WhoIS whoIsObject = new WhoIS();
 assertNotNull(whoIsObject.MiscellaneousQueries());
 }

 @Test
 public void testQueryTextFieldEmpty() {
 WhoIS whoIsObject = new WhoIS();
 assertFalse(whoIsObject.startQuery());
 }

 @Test
 public void testQueryTextFieldWithQuery() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.queryTextField.setText("example.com");
 assertTrue(whoIsObject.startQuery());
 }

 @Test(expected = NullPointerException.class)
 public void testQueryNullQuery() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.queryTextField.setText(null);
 whoIsObject.startQuery();
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHost() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.WHOIS_ADDRESS = "unknownhost.com";
 try {
 whoIsObject.query("example.com");
 } catch (UnknownHostException e) {
 // Expected exception
 }
 }

 @Test(expected = IOException.class)
 public void testIOException() {
 WhoIS whoIsObject = new WhoIS();
 try {
 whoIsObject.query("example.com");
 } catch (IOException e) {
 // Expected exception
 }
 }

 @Test
 public void testQueryWithInvalidPort() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.WHOIS_PORT = -1;
 assertFalse(whoIsObject.startQuery());
 }

 @Test
 public void testQueryWithValidPort() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.WHOIS_PORT = 43;
 assertTrue(whoIsObject.startQuery());
 }

 @Test
 public void testQueryWithEmptyIPAddress() {
 WhoIS whoIsObject = new WhoIS();
 assertFalse(whoIsObject.query(""));
 }

 @Test
 public void testQueryWithValidIPAddress() {
 WhoIS whoIsObject = new WhoIS();
 assertTrue(whoIsObject.query("192.168.1.1"));
 }

 @Test
 public void testGetQueryWithNoOptions() {
 WhoIS whoIsObject = new WhoIS();
 assertEquals("example.com", whoIsObject.getQuery());
 }

 @Test
 public void testGetQueryWithLowercaseL() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.lowercaseL.setSelected(true);
 assertEquals("-l example.com", whoIsObject.getQuery());
 }

 @Test
 public void testGetQueryWithUppercaseL() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.uppercaseL.setSelected(true);
 assertEquals("-L example.com", whoIsObject.getQuery());
 }

 @Test
 public void testGetQueryWithLowercaseM() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.lowercaseM.setSelected(true);
 assertEquals("-m example.com", whoIsObject.getQuery());
 }

 @Test
 public void testGetQueryWithUppercaseM() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.uppercaseM.setSelected(true);
 assertEquals("-M example.com", whoIsObject.getQuery());
 }

 @Test
 public void testGetQueryWithLowercaseX() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.lowercaseX.setSelected(true);
 assertEquals("-x example.com", whoIsObject.getQuery());
 }

 @Test
 public void testGetQueryWithBooleanD() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.lowercaseD.setSelected(true);
 assertEquals("-d example.com", whoIsObject.getQuery());
 }

 @Test
 public void testGetQueryWithMiscQueriesNone() {
 WhoIS whoIsObject = new WhoIS();
 assertEquals("example.com", whoIsObject.getQuery());
 }

 @Test
 public void testGetQueryWithMiscQueriesAll() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.miscList.setSelectedValues(new Object[]{"All"});
 assertEquals("-T all example.com", whoIsObject.getQuery());
 }

 @Test
 public void testGetQueryWithMiscQueriesSpecific() {
 WhoIS whoIsObject = new WhoIS();
 whoIsObject.miscList.setSelectedValues(new Object[]{"route-set"});
 assertEquals("-T route-set example.com", whoIsObject.getQuery());
 }

}