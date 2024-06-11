package ipac.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @After
 public void tearDown() {
 whois = null;
 }

 @Test
 public void testWhoISConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testQueryPanelCreation() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.queryPanel());
 }

 @Test
 public void testAdditionalQueriesPanelCreation() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.additionalQueriesPanel());
 }

 @Test
 public void testIPAddressLookupsCreation() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.IPAddressLookups());
 }

 @Test
 public void testMiscellaneousQueriesCreation() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.MiscellaneousQueries());
 }

 @Test
 public void testQueryHintsPanelCreation() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.queryHintsPanel());
 }

 @Test(expected = NullPointerException.class)
 public void testStartQueryNullQuery() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 }

 @Test
 public void testStartQueryValidQuery() throws IOException, UnknownHostException {
 WhoIS whois = new WhoIS();
 String query = "example.com";
 whois.startQuery(query);
 verify(whois.getOutput(), contains("example.com"));
 }

 @Test(expected = IOException.class)
 public void testQueryIOException() {
 WhoIS whois = new WhoIS();
 whois.query("example.com");
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryUnknownHostException() {
 WhoIS whois = new WhoIS();
 whois.query("unknownhost.com");
 }

 @Test
 public void testReset() {
 WhoIS whois = new WhoIS();
 whois.reset();
 assertEquals("", whois.queryTextField.getText());
 }
}