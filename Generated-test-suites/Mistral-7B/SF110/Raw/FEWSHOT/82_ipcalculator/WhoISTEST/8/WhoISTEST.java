package ipac.test;

import static org.junit.Assert.*;

import ipac.WhoIS;
import java.util.Arrays;

public class WhoISTest {

 // Test for constructor
 @Test
 public void testWhoIS() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 // Test for queryPanel method
 @Test
 public void testQueryPanel() {
 WhoIS whois = new WhoIS();
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 // Test for additionalQueriesPanel method
 @Test
 public void testAdditionalQueriesPanel() {
 WhoIS whois = new WhoIS();
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 // Test for IPAddressLookups method
 @Test
 public void testIPAddressLookups() {
 WhoIS whois = new WhoIS();
 JPanel ipAddressLookups = whois.IPAddressLookups();
 assertNotNull(ipAddressLookups);
 }

 // Test for MiscellaneousQueries method
 @Test
 public void testMiscellaneousQueries() {
 WhoIS whois = new WhoIS();
 JPanel miscellaneousQueries = whois.MiscellaneousQueries();
 assertNotNull(miscellaneousQueries);
 }

 // Test for queryHintsPanel method
 @Test
 public void testQueryHintsPanel() {
 WhoIS whois = new WhoIS();
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 // Test for createPopupMenu method
 @Test
 public void testCreatePopupMenu() {
 WhoIS whois = new WhoIS();
 JPopupMenu popupMenu = whois.popupMenu;
 assertNotNull(popupMenu);
 }

 // Test for getQuery method with no inverse queries, lowercase L, no other options
 @Test
 public void testGetQueryNoInverseQueries() {
 WhoIS whois = new WhoIS();
 String query = whois.getQuery();
 String expected = "query";
 assertTrue(query.startsWith(expected));
 }

 // Test for getQuery method with inverse queries, lowercase L
 @Test
 public void testGetQueryInverseQueries() {
 WhoIS whois = new WhoIS();
 whois.inverseQueries.setSelectedIndex(1);
 String query = whois.getQuery();
 String expected = "-i admin-c query";
 assertTrue(query.startsWith(expected));
 }

 // Test for getQuery method with inverse queries, lowercase L and other options
 @Test
 public void testGetQueryInverseQueriesOtherOptions() {
 WhoIS whois = new WhoIS();
 whois.lowercaseL.setSelected(false);
 whois.uppercaseL.setSelected(true);
 whois.lowercaseM.setSelected(true);
 whois.lowercaseX.setSelected(true);
 whois.inverseQueries.setSelectedIndex(2);
 String query = whois.getQuery();
 String expected = "-T person -M all -L -x query";
 assertEquals(expected, query);
 }

 // Test for getQuery method with no inverse queries, lowercase X
 @Test
 public void testGetQueryNoInverseQueriesLowercaseX() {
 WhoIS whois = new WhoIS();
 whois.lowercaseX.setSelected(true);
 String query = whois.getQuery();
 String expected = "-x query";
 assertTrue(query.startsWith(expected));
 }

 // Test for getQuery method with no inverse queries, no other options
 @Test
 public void testGetQueryNoOptions() {
 WhoIS whois = new WhoIS();
 String query = whois.getQuery();
 String expected = "query";
 assertTrue(query.startsWith(expected));
 }

 // Test for query method
 @Test
 public void testQuery() {
 WhoIS whois = new WhoIS();
 whois.query("192.168.1.1");
 }

 // Test for getQuery method with all options
 @Test
 public void testGetQueryAllOptions() {
 WhoIS whois = new WhoIS();
 whois.lowercaseL.setSelected(false);
 whois.uppercaseL.setSelected(true);
 whois.lowercaseM.setSelected(true);
 whois.uppercaseM.setSelected(true);
 whois.lowercaseX.setSelected(true);
 whois.inverseQueries.setSelectedIndex(3); // person
 whois.booleanD.setSelected(true);
 String[] selected = {"as-set", "inetnum", "mntner"};
 WhoIS.JList miscList = whois.miscList;
 for (String option : selected) {
 miscList.setSelectedValue(option, true);
 }
 String query = whois.getQuery();
 String expected = "-T person -M all -L -x query -d -T as-set -T inetnum -T mntner";
 assertEquals(expected, query);
 }

}