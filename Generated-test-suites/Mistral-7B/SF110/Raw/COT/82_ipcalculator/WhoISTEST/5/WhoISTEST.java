package ipac.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.junit.*;
import static org.mockito.Mockito.*;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

 private WhoIS whois;
 private WhoISOutput output;

 @Before
 public void setUp() {
 whois = new WhoIS();
 output = mock(WhoISOutput.class);
 whois.setWhoISOutput(output);
 }

 @Test
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertEquals("APNIC", whois.WHOIS_NAME);
 assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
 assertEquals(43, whois.WHOIS_PORT);
 }

 @Test
 public void testQueryPanel() {
 JPanel queryPanel = whois.queryPanel();
 assertTrue(queryPanel instanceof JPanel);
 assertTrue(queryPanel.getLayout() instanceof FlowLayout);
 assertEquals(4, queryPanel.getComponentCount());
 }

 @Test
 public void testAdditionalQueriesPanel() {
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertTrue(additionalQueriesPanel instanceof JPanel);
 assertTrue(additionalQueriesPanel.getLayout() instanceof BoxLayout);
 assertEquals(2, additionalQueriesPanel.getComponentCount());
 }

 @Test
 public void testIPAddressLookups() {
 JPanel lookupPanel = whois.IPAddressLookups();
 assertTrue(lookupPanel instanceof JPanel);
 assertTrue(lookupPanel.getLayout() instanceof BoxLayout);
 assertEquals(7, lookupPanel.getComponentCount());
 }

 @Test
 public void testMiscellaneousQueries() {
 JPanel miscPanel = whois.MiscellaneousQueries();
 assertTrue(miscPanel instanceof JPanel);
 assertTrue(miscPanel.getLayout() instanceof BoxLayout);
 assertEquals(3, miscPanel.getComponentCount());
 }

 @Test
 public void testQueryHintsPanel() {
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertTrue(queryHintsPanel instanceof JPanel);
 assertTrue(queryHintsPanel.getLayout() instanceof FlowLayout);
 assertEquals(4, queryHintsPanel.getComponentCount());
 }

 @Test
 public void testCreatePopupMenu() {
 JPopupMenu popupMenu = whois.popupMenu;
 assertNotNull(popupMenu);
 assertEquals(4, popupMenu.getComponentCount());
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformedWithNullSource() {
 whois.actionPerformed(mock(ActionEvent.class).withNoArgs().createMock());
 }

 @Test
 public void testActionPerformedWithQueryButton() throws IOException, UnknownHostException {
 when(output.displayOutput(anyString())).thenReturn(true);
 whois.actionPerformed(mock(ActionEvent.class).withSource(whois.queryButton).createMock());
 verify(output).displayOutput(anyString());
 }

 @Test
 public void testActionPerformedWithResetButton() {
 whois.actionPerformed(mock(ActionEvent.class).withSource(whois.resetButton).createMock());
 assertEquals("", whois.queryTextField.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartQueryWithEmptyQuery() {
 whois.startQuery();
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithUnknownHost() throws IOException {
 doThrow(UnknownHostException.class).when(output).displayOutput(anyString());
 whois.startQuery();
 }

 @Test(expected = IOException.class)
 public void testQueryWithIOError() throws UnknownHostException {
 doThrow(IOException.class).when(output).displayOutput(anyString());
 whois.startQuery();
 }
}