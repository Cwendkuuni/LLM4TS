package ipac.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setup() {
 whois = new WhoIS();
 }

 @Test
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.queryTextField);
 assertNotNull(whois.queryButton);
 assertNotNull(whois.resetButton);
 assertNotNull(whois.lowercaseL);
 assertNotNull(whois.uppercaseL);
 assertNotNull(whois.lowercaseM);
 assertNotNull(whois.uppercaseM);
 assertNotNull(whois.lowercaseX);
 assertNotNull(whois.resetRadioButton);
 assertNotNull(whois.radioButtonGroup);
 assertNotNull(whois.popupMenu);
 assertNotNull(whois.inverseQueries);
 assertNotNull(whois.miscList);
 }

 @Test
 public void testQueryPanel() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 assertTrue(queryPanel.getBackground() == Color.WHITE);
 assertEquals(queryPanel.getLayout(), new FlowLayout(0));
 }

 @Test
 public void testAdditionalQueriesPanel() {
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 assertTrue(additionalQueriesPanel.getBackground() == Color.WHITE);
 }

 @Test
 public void testIPAddressLookups() {
 JPanel lookupPanel = whois.IPAddressLookups();
 assertNotNull(lookupPanel);
 assertTrue(lookupPanel.getBackground() == Color.WHITE);
 }

 @Test
 public void testMiscellaneousQueries() {
 JPanel miscPanel = whois.MiscellaneousQueries();
 assertNotNull(miscPanel);
 assertTrue(miscPanel.getBackground() == Color.WHITE);
 }

 @Test
 public void testQueryHintsPanel() {
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 assertTrue(queryHintsPanel.getBackground() == Color.WHITE);
 }

 @Test
 public void testCreatePopupMenu() throws IOException {
 whois.createPopupMenu();
 assertTrue(whois.popupMenu.isPopupMenu());
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryException() {
 whois.query("127.0.0.1");
 fail("Should throw UnknownHostException");
 }

 @Test(expected = IOException.class)
 public void testQueryIOException() {
 whois.query("google.com");
 fail("Should throw IOException");
 }

 @Test
 public void testStartQuery() {
 String query = "google.com";
 whois.startQuery();
 assertEquals(whois.getQuery(), query);
 }

 @Test
 public void testReset() {
 whois.reset();
 assertTrue(whois.queryTextField.getText().isEmpty());
 }

 @Test
 public void testGetQuery() {
 String query = "google.com";
 whois.queryTextField.setText(query);
 WhoISOutput output = new WhoISOutput("test output");
 whois.inverseQueries.setSelectedIndex(1);
 whois.miscList.setSelectedValue("route", true);
 whois.lowercaseL.setSelected(true);
 whois.lowercaseD.setSelected(true);
 String expected = "-i tech-c -l " + query + " -T route -d";
 assertEquals(whois.getQuery(), expected);
 }
}