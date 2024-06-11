package ipac.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

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
 public void testConstructor() {
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
 public void testActionPerformedQueryButton() {
 whoIS.queryButton.doClick();
 assertTrue(whoIS.startQueryCalled); // Assuming startQuery() has a flag to check if it was called
 }

 @Test
 public void testActionPerformedResetButton() {
 whoIS.resetButton.doClick();
 assertTrue(whoIS.queryTextField.getText().isEmpty());
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryUnknownHost() {
 whoIS.query("unknownhost.com");
 fail("Query to unknownhost.com should have thrown UnknownHostException");
 }

 @Test(expected = IOException.class)
 public void testQueryIOException() {
 whoIS.query("validwhois.com");
 try {
 Thread.sleep(1000); // Simulate query taking some time
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 fail("Query to validwhois.com should have thrown IOException");
 }

 @Test
 public void testGetQuery() {
 String query = whoIS.getQuery();
 assertNotNull(query);
 }

 @Test
 public void testReset() {
 whoIS.reset();
 assertTrue(whoIS.queryTextField.getText().isEmpty());
 }
}