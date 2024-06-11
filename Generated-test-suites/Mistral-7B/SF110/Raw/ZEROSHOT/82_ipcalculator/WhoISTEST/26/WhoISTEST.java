package ipac.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

	WhoIS whois;
	WhoISOutput output;

	@Before
	public void setUp() {
		whois = new WhoIS();
	}

	@After
	public void tearDown() {
		whois = null;
		output = null;
	}

	@Test
	public void testWhoISConstruction() {
		assertNotNull(whois);
		assertEquals("APNIC", whois.WHOIS_NAME);
		assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
		assertEquals(43, whois.WHOIS_PORT);
	}

	@Test(expected = NullPointerException.class)
	public void testQueryNullQuery() {
		whois.startQuery();
	}

	@Test
	public void testQueryValidQuery() throws IOException, UnknownHostException {
		whois.queryTextField.setText("google.com");
		whois.startQuery();
		assertTrue(output.getOutput().contains("Google"));
	}

	@Test
	public void testQueryWithInverseQueries() throws IOException, UnknownHostException {
		whois.inverseQueries.setSelectedIndex(1);
		whois.queryTextField.setText("Google");
		whois.startQuery();
		assertTrue(output.getOutput().contains("person: Google Inc."));
	}

	@Test
	public void testQueryWithTypeFiltering() throws IOException, UnknownHostException {
		Object[] selected = new Object[] {"as-block"};
		whois.miscList.setSelectedValues(selected);
		whois.queryTextField.setText("AS1234");
		whois.startQuery();
		assertTrue(output.getOutput().contains("as-block: [AS1234]"));
	}

	@Test
	public void testQueryWithDFlag() throws IOException, UnknownHostException {
		whois.lowercaseD.setSelected(true);
		whois.queryTextField.setText("192.168.0.1");
		whois.startQuery();
		String outputText = output.getOutput();
		assertTrue(outputText.contains("inet num: 192.168.0.1"));
		assertTrue(outputText.contains("netname: 192.168.0.0"));
		assertTrue(outputText.contains("netname server: jwns1.rri.rit.edu"));
	}

	@Test
	public void testQueryWithReset() {
		whois.startQuery();
		whois.reset();
		assertEquals("", whois.queryTextField.getText());
		assertFalse(whois.lowercaseD.isSelected());
		assertEquals(0, whois.inverseQueries.getSelectedIndex());
		assertEquals(0, whois.miscList.getSelectedIndex());
	}
}