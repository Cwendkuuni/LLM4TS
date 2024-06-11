package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 WhoIS whois = new WhoIS();
 this.whois = whois;
 }

 @Test
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 assertEquals("APNIC", whois.WHOIS_NAME);
 assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
 assertEquals(43, whois.WHOIS_PORT);
 }

 @Test
 public void testQueryPanel() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testAdditionalQueriesPanel() {
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 @Test
 public void testIPAddressLookups() {
 JPanel lookupPanel = whois.IPAddressLookups();
 assertNotNull(lookupPanel);
 }

 @Test
 public void testMiscellaneousQueries() {
 JPanel miscPanel = whois.MiscellaneousQueries();
 assertNotNull(miscPanel);
 }

 @Test
 public void testQueryHintsPanel() {
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartQueryWithEmptyQueryTextField() {
 whois.startQuery();
 }

 @Test
 public void testStartQueryWithValidQuery() throws UnknownHostException, IOException {
 whois.queryTextField.setText("example.com");
 whois.startQuery();
 assertTrue(whois.queryTextField.getText().isEmpty());
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithUnknownHostException() throws UnknownHostException, IOException {
 whois.queryTextField.setText("unknownhost.com");
 whois.startQuery();
 }

 @Test(expected = IOException.class)
 public void testQueryWithIOException() throws UnknownHostException, IOException {
 whois.queryTextField.setText("example.com");
 WhoISOutput output = spy(new WhoISOutput());
 whois.setOutput(output);
 whois.startQuery();
 }

 @Test
 public void testGetQueryWithNoInverseQuerySelected() {
 String query = whois.getQuery();
 assertEquals("example.com", query);
 }

 @Test
 public void testGetQueryWithInverseQuerySelected() {
 whois.inverseQueries.setSelectedIndex(1);
 String query = whois.getQuery();
 assertEquals("-i admin-c example.com", query);
 }

 @Test
 public void testGetQueryWithLowercaseLSelected() {
 whois.lowercaseL.setSelected(true);
 String query = whois.getQuery();
 assertEquals("-l example.com", query);
 }

 @Test
 public void testGetQueryWithUppercaseLSelected() {
 whois.uppercaseL.setSelected(true);
 String query = whois.getQuery();
 assertEquals("-L example.com", query);
 }

 @Test
 public void testGetQueryWithLowercaseMSelected() {
 whois.lowercaseM.setSelected(true);
 String query = whois.getQuery();
 assertEquals("-m example.com", query);
 }

 @Test
 public void testGetQueryWithUppercaseMSelected() {
 whois.uppercaseM.setSelected(true);
 String query = whois.getQuery();
 assertEquals("-M example.com", query);
 }

 @Test
 public void testGetQueryWithLowercaseXSelected() {
 whois.lowercaseX.setSelected(true);
 String query = whois.getQuery();
 assertEquals("-x example.com", query);
 }

 @Test
 public void testGetQueryWithBooleanDSelected() {
 whois.lowercaseD.setSelected(true);
 String query = whois.getQuery();
 assertEquals("-d example.com", query);
 }

 @Test
 public void testGetQueryWithMultipleMiscQueriesSelected() {
 String[] selected = { "as-set", "filter-set", "person" };
 whois.miscList.setSelectedValues(selected);
 String query = whois.getQuery();
 String expected = "-T as-set,filter-set,person ";
 assertEquals(expected, query);
 }

}