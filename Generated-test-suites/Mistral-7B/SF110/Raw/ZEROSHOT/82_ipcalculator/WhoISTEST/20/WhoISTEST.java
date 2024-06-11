package ipac.whois;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;

import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

	@Test
	public void testConstructor() {
		WhoIS whois = new WhoIS();
		assertNotNull(whois);
	}

	@Test
	public void testQueryPanel() {
		WhoIS whois = new WhoIS();
		JPanel queryPanel = whois.queryPanel();
		assertNotNull(queryPanel);
		assertEquals(5, queryPanel.getComponentCount());
		assertTrue(queryPanel instanceof JPanel);
		assertTrue(whois.queryTextField instanceof JTextField);
		assertTrue(whois.queryButton instanceof JButton);
		assertTrue(whois.resetButton instanceof JButton);
		assertEquals(WhoisActionEvents.QUERY, whois.queryButton.getActionCommand());
		assertEquals(WhoisActionEvents.RESET, whois.resetButton.getActionCommand());
	}

	@Test
	public void testAdditionalQueriesPanel() {
		WhoIS whois = new WhoIS();
		JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
		assertNotNull(additionalQueriesPanel);
		assertEquals(2, additionalQueriesPanel.getComponentCount());
		assertTrue(additionalQueriesPanel instanceof JPanel);
		assertNotNull(whois.IPAddressLookups());
		assertNotNull(whois.MiscellaneousQueries());
		assertTrue(additionalQueriesPanel.getComponent(0) instanceof JPanel);
		assertTrue(additionalQueriesPanel.getComponent(1) instanceof JPanel);
	}

	@Test
	public void testIPAddressLookups() {
		WhoIS whois = new WhoIS();
		JPanel lookupPanel = whois.IPAddressLookups();
		assertNotNull(lookupPanel);
		assertEquals(9, lookupPanel.getComponentCount());
		assertTrue(lookupPanel instanceof JPanel);
		assertTrue(whois.lowercaseL instanceof JRadioButton);
		assertTrue(whois.uppercaseL instanceof JRadioButton);
		assertTrue(whois.lowercaseM instanceof JRadioButton);
		assertTrue(whois.uppercaseM instanceof JRadioButton);
		assertTrue(whois.lowercaseX instanceof JRadioButton);
		assertTrue(whois.resetRadioButton instanceof JRadioButton);
		assertEquals("1st level less specific", whois.lowercaseL.getText());
		assertEquals("All less specific", whois.uppercaseL.getText());
		assertEquals("1st level more specific", whois.lowercaseM.getText());
		assertEquals("All more specific", whois.uppercaseM.getText());
		assertEquals("Exact match only", whois.lowercaseX.getText());
		assertEquals("Reset Radio Button", whois.resetRadioButton.getText());
	}

	@Test
	public void testMiscellaneousQueries() {
		WhoIS whois = new WhoIS();
		JPanel miscPanel = whois.MiscellaneousQueries();
		assertNotNull(miscPanel);
		assertEquals(3, miscPanel.getComponentCount());
		assertTrue(miscPanel instanceof JPanel);
		assertNotNull(whois.inverseQueries);
		assertNotNull(whois.miscList);
		assertEquals("Inverse Queries", ((JLabel) miscPanel.getComponent(0)).getText());
		assertTrue(whois.inverseQueries instanceof JComboBox);
		assertEquals(14, whois.inverseQueries.getItemCount());
		assertEquals("Object Types", ((JLabel) miscPanel.getComponent(1)).getText());
		assertTrue(whois.miscList instanceof JList);
		Object[] expectedItems = {"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"};
		assertArrayEquals(expectedItems, whois.miscList.getModel().toArray());
	}

	@Test
	public void testQueryHintsPanel() {
		WhoIS whois = new WhoIS();
		JPanel queryHintsPanel = whois.queryHintsPanel();
		assertNotNull(queryHintsPanel);
		assertEquals(1, queryHintsPanel.getComponentCount());
		assertTrue(queryHintsPanel instanceof JPanel);
		assertNotNull(queryHintsPanel.getComponent(0));
		assertTrue(queryHintsPanel.getComponent(0) instanceof JPanel);
		assertEquals(3, ((JPanel) queryHintsPanel.getComponent(0)).getComponentCount());
		assertEquals("Query hints ", ((JLabel) ((JPanel) queryHintsPanel.getComponent(0)).getComponent(0)).getText());
		assertEquals("EMPTY LABEL", ((JLabel) ((JPanel) queryHintsPanel.getComponent(0)).getComponent(1)).getText());
		assertEquals("* Include \"AS\" in front of an AS number.", ((JLabel) ((JPanel) queryHintsPanel.getComponent(0)).getComponent(2)).getText());
	}

	@Test
	public void testCreatePopupMenu() {
		WhoIS whois = new WhoIS();
		JPopupMenu popupMenu = whois.popupMenu;
		assertNotNull(popupMenu);
		assertEquals(4, popupMenu.getComponentCount());
		assertTrue(popupMenu instanceof JPopupMenu);
		assertTrue(whois.popupMenu.getComponent(0) instanceof JMenuItem);
		assertTrue(whois.popupMenu.getComponent(1) instanceof JMenuItem);
		assertTrue(whois.popupMenu.getComponent(2) instanceof JMenuItem);
		assertTrue(whois.popupMenu.getComponent(3) instanceof JMenuItem);
		assertEquals("Cut", ((JMenuItem) whois.popupMenu.getComponent(0)).getText());
		assertEquals("Copy", ((JMenuItem) whois.popupMenu.getComponent(1)).getText());
		assertEquals("Paste", ((JMenuItem) whois.popupMenu.getComponent(2)).getText());
		assertEquals("Select All", ((JMenuItem) whois.popupMenu.getComponent(3)).getText());
	}

	@Test
	public void testActionPerformedQueryButton() throws IOException, SocketException {
		String whoisServerAddress = "whois.apnic.net";
		String whoisServerPort = "43";
		String expectedOutput = "TOS:\t0\r\n" +
				"Msg-Id:\t12345678901234567890\r\n" +
				"Message-Rcode:\tNOERROR\r\n" +
				"Message:\t.....\r\n";

		WhoIS whois = spy(new WhoIS());
		doReturn(whoisServerAddress).when(whois).getWHOIS_ADDRESS();
		doReturn(Integer.parseInt(whoisServerPort)).when(whois).getWHOIS_PORT();
		doAnswer(invocation -> {
			String query = invocation.getArgument(0);
			WhoISOutput output = new WhoISOutput(expectedOutput);
			whois.query(query);
			return null;
		}).when(whois).query(eq("query"));

		whoIs.startQuery();

		verify(whois, times(1)).query("query");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStartQueryEmptyQuery() {
		WhoIS whois = new WhoIS();
		whoIs.startQuery();
	}

	@Test
	public void testReset() {
		WhoIS whois = new WhoIS();
		whoIs.reset();
		assertEquals("", whois.queryTextField.getText());
		assertFalse(whois.booleanD);
		assertEquals(0, whois.inverseQueries.getSelectedIndex());
		assertEquals(0, whois.miscList.getSelectedIndex());
	}

	@Test
	public void testGetQuery() {
		WhoIS whois = new WhoIS();
		String query = "example.com";
		whois.queryTextField.setText(query);
		whois.lowercaseL.setSelected(true);
		whois.lowercaseD.setSelected(true);
		String expectedQuery = "-l example.com -d";
		assertEquals(expectedQuery, whois.getQuery());
	}

	private String getWHOIS_ADDRESS(WhoIS whois) {
		return whois.WHOIS_ADDRESS;
	}

	private int getWHOIS_PORT(WhoIS whois) {
		return whois.WHOIS_PORT;
	}
}

class WhoISOutput {
	private final String output;

	public WhoISOutput(String output) {
		this.output = output;
	}
}