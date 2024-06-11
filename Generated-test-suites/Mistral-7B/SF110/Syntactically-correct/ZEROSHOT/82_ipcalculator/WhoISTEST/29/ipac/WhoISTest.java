package ipac.test;

import org.junit.After;
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
 public void testQueryPanel() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 assertTrue(queryPanel.getLayout() instanceof FlowLayout);
 }

 @Test
 public void testAdditionalQueriesPanel() {
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 assertTrue(additionalQueriesPanel.getLayout() instanceof BoxLayout);
 }

 @Test
 public void testIPAddressLookups() {
 JPanel lookupPanel = whois.IPAddressLookups();
 assertNotNull(lookupPanel);
 assertTrue(lookupPanel.getLayout() instanceof BoxLayout);
 }

 @Test
 public void testMiscellaneousQueries() {
 JPanel miscPanel = whois.MiscellaneousQueries();
 assertNotNull(miscPanel);
 assertTrue(miscPanel.getLayout() instanceof BoxLayout);
 }

 @Test
 public void testQueryHintsPanel() {
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 assertTrue(queryHintsPanel.getLayout() instanceof FlowLayout);
 }

 @Test
 public void testCreatePopupMenu() {
 JPopupMenu popupMenu = whois.popupMenu;
 assertNotNull(popupMenu);
 }

 @Test
 public void testActionPerformedQueryButton() {
 whois.queryButton.doClick();
 assertTrue(whois.queryTextField.getText().length() > 0);
 }

 @Test
 public void testActionPerformedResetButton() {
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText());
 }
}