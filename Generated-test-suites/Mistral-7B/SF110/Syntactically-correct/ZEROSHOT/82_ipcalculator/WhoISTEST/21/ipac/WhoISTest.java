package ipac.WhoISTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS.WhoIS;
import ipac.WhoIS.WhoISOutput;

public class WhoISTest {

 private WhoIS whoIS;

 @Before
 public void setUp() {
 whoIS = new WhoIS();
 }

 @After
 public void tearDown() {
 whoIS = null;
 }

 @Test
 public void testWhoISConstructor() {
 WhoIS whoIS = new WhoIS();
 assertNotNull(whoIS);
 }

 @Test
 public void testQueryPanel() {
 JPanel queryPanel = whoIS.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testAdditionalQueriesPanel() {
 JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 @Test
 public void testIPAddressLookups() {
 JPanel ipAddressLookups = whoIS.IPAddressLookups();
 assertNotNull(ipAddressLookups);
 }

 @Test
 public void testMiscellaneousQueries() {
 JPanel miscellaneousQueries = whoIS.MiscellaneousQueries();
 assertNotNull(miscellaneousQueries);
 }

 @Test
 public void testQueryHintsPanel() {
 JPanel queryHintsPanel = whoIS.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 }

 @Test
 public void testCreatePopupMenu() {
 JPopupMenu popupMenu = whoIS.popupMenu;
 assertNotNull(popupMenu);
 }

 @Test
 public void testActionPerformedQueryButton() throws UnknownHostException, IOException {
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, ActionEvent.ACTION_PERFORMED, "Query"));
 WhoISOutput output = new WhoISOutput("Test Output");
 assertEquals(output.toString(), whoIS.getOutput().toString());
 }

 @Test
 public void testActionPerformedResetButton() {
 whoIS.actionPerformed(new ActionEvent(whoIS.resetButton, ActionEvent.ACTION_PERFORMED, "Reset"));
 assertNull(whoIS.queryTextField.getText());
 assertFalse(whoIS.lowercaseD.isSelected());
 assertNull(whoIS.inverseQueries.getSelectedItem());
 assertNull(whoIS.miscList.getSelectedValues());
 }

 @Test
 public void testStartQueryEmptyQuery() {
 whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, ActionEvent.ACTION_PERFORMED, "Query"));
 assertNull(whoIS.getOutput());
 }

 @Test(expected = UnknownHostException.class)
 public void testStartQueryUnknownHost() {
 whoIS.WHOIS_ADDRESS = "unknown.host";
 whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, ActionEvent.ACTION_PERFORMED, "Query"));
 }

 @Test(expected = IOException.class)
 public void testStartQueryIOException() throws UnknownHostException {
 whoIS.WHOIS_ADDRESS = "localhost";
 whoIS.WHOIS_PORT = -1;
 whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, ActionEvent.ACTION_PERFORMED, "Query"));
 }

}