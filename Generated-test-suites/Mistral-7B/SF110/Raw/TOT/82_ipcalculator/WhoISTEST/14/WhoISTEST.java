package ipac.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

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
 public void testConstructor() throws Exception {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testQueryPanelCreation() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testQueryTextField() {
 JTextField queryTextField = whois.queryTextField;
 assertNotNull(queryTextField);
 assertEquals(40, queryTextField.getColumns());
 }

 @Test
 public void testQueryButton() {
 JButton queryButton = whois.queryButton;
 assertNotNull(queryButton);
 }

 @Test
 public void testResetButton() {
 JButton resetButton = whois.resetButton;
 assertNotNull(resetButton);
 }

 @Test
 public void testLowercaseLButton() {
 JRadioButton lowercaseL = whois.lowercaseL;
 assertNotNull(lowercaseL);
 }

 @Test
 public void testUppercaseLButton() {
 JRadioButton uppercaseL = whois.uppercaseL;
 assertNotNull(uppercaseL);
 }

 @Test
 public void testLowercaseMButton() {
 JRadioButton lowercaseM = whois.lowercaseM;
 assertNotNull(lowercaseM);
 }

 @Test
 public void testUppercaseMButton() {
 JRadioButton uppercaseM = whois.uppercaseM;
 assertNotNull(uppercaseM);
 }

 @Test
 public void testLowercaseXButton() {
 JRadioButton lowercaseX = whois.lowercaseX;
 assertNotNull(lowercaseX);
 }

 @Test
 public void testResetRadioButton() {
 JRadioButton resetRadioButton = whois.resetRadioButton;
 assertNotNull(resetRadioButton);
 }

 @Test
 public void testPopupMenu() {
 JPopupMenu popupMenu = whois.popupMenu;
 assertNotNull(popupMenu);
 }

 @Test
 public void testInverseQueriesComboBox() {
 JComboBox inverseQueries = whois.inverseQueries;
 assertNotNull(inverseQueries);
 }

 @Test
 public void testMiscList() {
 JList miscList = whois.miscList;
 assertNotNull(miscList);
 }

 @Test
 public void testQueryHintsPanel() {
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 @Test
 public void testCreatePopupMenu() {
 WhoIS whois = new WhoIS();
 JPopupMenu popupMenu = whois.popupMenu;
 assertNotNull(popupMenu);
 JMenuItem cut = popupMenu.getComponent(0);
 JMenuItem copy = popupMenu.getComponent(1);
 JMenuItem paste = popupMenu.getComponent(2);
 JMenuItem selectAll = popupMenu.getComponent(3);
 }

 @Test
 public void testActionPerformedQueryButton() throws IOException {
 String query = "google.com";
 whois.queryTextField.setText(query);
 whois.actionPerformed(new ActionEvent(whois, 0, "QueryButtonActionPerformed"));
 String output = "..."; // expected output
 assertNotNull(whois.getOutput());
 assertEquals(output, whois.getOutput());
 }

 @Test
 public void testActionPerformedResetButton() throws IOException {
 whois.actionPerformed(new ActionEvent(whois, 0, "ResetButtonActionPerformed"));
 String query = "";
 assertEquals(query, whois.queryTextField.getText());
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHostException() throws UnknownHostException {
 WhoIS whois = new WhoIS();
 whois.query("unknownhost.com");
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 WhoIS whois = new WhoIS();
 whois.query("google.com");
 }

 @Test
 public void testInvalidQuery() throws IOException {
 WhoIS whois = new WhoIS();
 whois.query("invalidquery");
 String output = "Invalid query: invalidquery";
 assertEquals(output, whois.getOutput());
 }
}