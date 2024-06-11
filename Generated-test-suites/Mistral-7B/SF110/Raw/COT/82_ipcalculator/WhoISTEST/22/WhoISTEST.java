package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

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
 public void testWhoISConstructor() {
 WhoIS whoisInstance = new WhoIS();
 assertNotNull(whoisInstance);
 }

 @Test(expected = NullPointerException.class)
 public void testNullQueryTextField() {
 whois.query(null);
 }

 @Test
 public void testEmptyQuery() {
 whois.startQuery();
 assertNull(whois.getQuery());
 }

 @Test
 public void testQueryWithValidInput() {
 String query = "example.com";
 whois.startQuery();
 assertEquals(query, whois.getQuery());
 }

 @Test
 public void testQueryWithInverseQueries() {
 whois.inverseQueries.setSelectedIndex(1);
 whois.startQuery();
 String query = "-i admin-c example.com";
 assertEquals(query, whois.getQuery());
 }

 @Test
 public void testQueryWithLowerCaseL() {
 whois.lowercaseL.doClick();
 whois.startQuery();
 String query = "-l example.com";
 assertEquals(query, whois.getQuery());
 }

 @Test
 public void testQueryWithUpperCaseL() {
 whois.uppercaseL.doClick();
 whois.startQuery();
 String query = "-L example.com";
 assertEquals(query, whois.getQuery());
 }

 @Test
 public void testQueryWithLowerCaseM() {
 whois.lowercaseM.doClick();
 whois.startQuery();
 String query = "-m example.com";
 assertEquals(query, whois.getQuery());
 }

 @Test
 public void testQueryWithUpperCaseM() {
 whois.uppercaseM.doClick();
 whois.startQuery();
 String query = "-M example.com";
 assertEquals(query, whois.getQuery());
 }

 @Test
 public void testQueryWithLowerCaseX() {
 whois.lowercaseX.doClick();
 whois.startQuery();
 String query = "-x example.com";
 assertEquals(query, whois.getQuery());
 }

 @Test
 public void testQueryWithBooleanD() {
 whois.lowercaseD.doClick();
 whois.startQuery();
 String query = "-d example.com";
 assertEquals(query, whois.getQuery());
 }

 @Test
 public void testQueryWithMiscQueries() {
 Object[] selected = {"as-set", "filter-set"};
 whois.miscList.setSelectedValues(selected);
 whois.startQuery();
 String query = "-T as-set,filter-set example.com";
 assertEquals(query, whois.getQuery());
 }

 @Test
 public void testResetMethod() {
 whois.reset();
 assertEquals("", whois.queryTextField.getText());
 assertTrue(whois.resetRadioButton.isSelected());
 assertFalse(whois.lowercaseD.isSelected());
 assertNull(whois.inverseQueries.getSelectedItem());
 assertNull(whois.miscList.getSelectedValue());
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHostException() {
 whois.startQuery();
 }

 @Test(expected = IOException.class)
 public void testIOException() {
 whois.startQuery();
 }
}