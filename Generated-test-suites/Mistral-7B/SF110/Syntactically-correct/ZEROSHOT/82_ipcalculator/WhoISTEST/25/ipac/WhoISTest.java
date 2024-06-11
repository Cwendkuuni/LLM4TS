package ipac.test;

import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 @Test
 public void testWhoISConstructor() {
 WhoIS whoIS = new WhoIS();
 assertNull(whoIS.WHOIS_NAME);
 assertEquals("whois.apnic.net", whoIS.WHOIS_ADDRESS);
 assertEquals(43, whoIS.WHOIS_PORT);
 assertNotNull(whoIS.queryTextField);
 assertNotNull(whoIS.queryButton);
 assertNotNull(whoIs.resetButton);
 }

 @Test
 public void testQueryPanel() {
 WhoIS whoIS = new WhoIS();
 JPanel queryPanel = whoIS.queryPanel();
 assertNotNull(queryPanel);
 assertTrue(queryPanel.getLayout() instanceof FlowLayout);
 assertEquals(3, queryPanel.getComponentCount());
 assertTrue(queryPanel.getComponent(0) instanceof JLabel);
 assertTrue(queryPanel.getComponent(1) instanceof JTextField);
 assertTrue(queryPanel.getComponent(2) instanceof JButton);
 }

 @Test
 public void testAdditionalQueriesPanel() {
 WhoIS whoIS = new WhoIS();
 JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 assertTrue(additionalQueriesPanel.getLayout() instanceof FlowLayout);
 assertEquals(2, additionalQueriesPanel.getComponentCount());
 }

 @Test
 public void testIPAddressLookups() {
 WhoIS whoIS = new WhoIS();
 JPanel ipAddressLookups = whoIS.IPAddressLookups();
 assertNotNull(ipAddressLookups);
 assertTrue(ipAddressLookups.getLayout() instanceof BoxLayout);
 assertEquals(7, ipAddressLookups.getComponentCount());
 }

 @Test
 public void testMiscellaneousQueries() {
 WhoIS whoIS = new WhoIS();
 JPanel miscQueries = whoIS.MiscellaneousQueries();
 assertNotNull(miscQueries);
 assertTrue(miscQueries.getLayout() instanceof BoxLayout);
 assertEquals(3, miscQueries.getComponentCount());
 }

 @Test
 public void testQueryHintsPanel() {
 WhoIS whoIS = new WhoIS();
 JPanel queryHintsPanel = whoIS.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 assertTrue(queryHintsPanel.getLayout() instanceof FlowLayout);
 }

 @Test
 public void testCreatePopupMenu() {
 WhoIS whoIS = new WhoIS();
 JPopupMenu popupMenu = whoIS.popupMenu;
 assertNotNull(popupMenu);
 assertEquals(4, popupMenu.getComponentCount());
 assertTrue(popupMenu.getComponent(0) instanceof JMenuItem);
 assertTrue(popupMenu.getComponent(1) instanceof JMenuItem);
 assertTrue(popupMenu.getComponent(2) instanceof JMenuItem);
 assertTrue(popupMenu.getComponent(3) instanceof JMenuItem);
 }
}