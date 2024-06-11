package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testCreatePopupMenu() {
 JPopupMenu popupMenu = whois.popupMenu;
 assertNotNull(popupMenu);
 }

 @Test
 public void testQueryPanelCreation() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testAdditionalQueriesPanelCreation() {
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 @Test
 public void testQueryHintsPanelCreation() {
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 @Test
 public void testQueryTextFieldLengthZero() {
 whois.startQuery();
 assertEquals("No Query Detected", whois.getErrorMessage());
 }

 @Test
 public void testQueryValidQuery() {
 String query = "Google.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertNotNull(whois.getResponse());
 }

 @Test
 public void testQueryWithInverseQueries() {
 String query = "mnt-by";
 whois.inverseQueries.setSelectedItem("mnt-by");
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertNotNull(whois.getResponse());
 }

 @Test
 public void testQueryWithLowerCaseL() {
 whois.lowercaseL.doClick();
 String query = "APNIC";
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertNotNull(whois.getResponse());
 }

 @Test
 public void testQueryWithUpperCaseL() {
 whois.uppercaseL.doClick();
 String query = "APNIC";
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertNotNull(whois.getResponse());
 }

 @Test
 public void testQueryWithLowerCaseM() {
 whois.lowercaseM.doClick();
 String query = "192.168.0.1";
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertNotNull(whois.getResponse());
 }

 @Test
 public void testQueryWithUpperCaseM() {
 whois.uppercaseM.doClick();
 String query = "192.168.0.1";
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertNotNull(whois.getResponse());
 }

 @Test
 public void testQueryWithLowerCaseX() {
 whois.lowercaseX.doClick();
 String query = "192.168.0.1";
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertNotNull(whois.getResponse());
 }

 @Test
 public void testQueryWithBooleanD() {
 whois.lowercaseD.doClick();
 String query = "Google.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertNotNull(whois.getResponse());
 }

 @Test
 public void testQueryWithMiscQueries() {
 String[] selected = { "as-block" };
 whois.miscList.setSelectedValues(selected);
 String query = "Google.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 assertNotNull(whois.getResponse());
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithInvalidHost() {
 String query = "InvalidHost.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 fail("Unexpected successful query");
 }

 @Test(expected = IOException.class)
 public void testQueryWithInvalidServer() {
 whois.WHOIS_ADDRESS = "InvalidServer.com";
 String query = "Google.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 fail("Unexpected successful query");
 }
}