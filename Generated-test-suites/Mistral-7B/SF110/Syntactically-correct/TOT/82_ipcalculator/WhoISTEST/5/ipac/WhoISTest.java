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

 @Test
 public void testQueryPanel() {
 JPanel queryPanel = whois.queryPanel();

 // Test queryTextField
 assertTrue(queryPanel.contains(whois.queryTextField));
 assertEquals(40, whois.queryTextField.getColumns());

 // Test queryButton
 assertTrue(queryPanel.contains(whois.queryButton));
 assertTrue(whois.queryButton.isEnabled());

 // Test resetButton
 assertTrue(queryPanel.contains(whois.resetButton));
 assertTrue(whois.resetButton.isEnabled());
 }

 @Test
 public void testAdditionalQueriesPanel() {
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();

 // Test IPAddressLookups
 assertNotNull(additionalQueriesPanel.getComponent(0));
 assertInstanceOf(JPanel.class, additionalQueriesPanel.getComponent(0));

 // Test MiscellaneousQueries
 assertNotNull(additionalQueriesPanel.getComponent(1));
 assertInstanceOf(JPanel.class, additionalQueriesPanel.getComponent(1));

 // Test JComboBox inverseQueries
 assertTrue(additionalQueriesPanel.getComponent(1).getComponent(0) instanceof JLabel);
 assertTrue(additionalQueriesPanel.getComponent(1).getComponent(1) instanceof JComboBox);
 }

 @Test
 public void testQueryHintsPanel() {
 JPanel queryHintsPanel = whois.queryHintsPanel();

 // Test queryHintsPanel layout
 assertNotNull(queryHintsPanel);
 assertInstanceOf(JPanel.class, queryHintsPanel);
 assertInstanceOf(BoxLayout.class, queryHintsPanel.getLayout());

 // Test innerPanel layout
 assertNotNull(queryHintsPanel.getComponent(0));
 assertInstanceOf(JPanel.class, queryHintsPanel.getComponent(0));
 assertInstanceOf(BoxLayout.class, queryHintsPanel.getComponent(0).getLayout());

 // Test queryHintsText1
 assertNotNull(queryHintsPanel.getComponent(0).getComponent(0));
 assertInstanceOf(JLabel.class, queryHintsPanel.getComponent(0).getComponent(0));
 assertEquals("Query hints ", ((JLabel)queryHintsPanel.getComponent(0).getComponent(0)).getText());

 // Test emptyLabel
 assertNotNull(queryHintsPanel.getComponent(0).getComponent(1));
 assertInstanceOf(JLabel.class, queryHintsPanel.getComponent(0).getComponent(1));
 assertEquals("EMPTY LABEL", ((JLabel)queryHintsPanel.getComponent(0).getComponent(1)).getText());
 }

 @Test(expected = IllegalStateException.class)
 public void testQueryWithEmptyQuery() {
 whois.startQuery();
 }

 @Test
 public void testQueryWithValidQuery() {
 whois.queryTextField.setText("example.com");
 whois.queryButton.doClick();
 // Add assertions to verify the output of the query
 }

 @Test(expected = IOException.class)
 public void testQueryWithInvalidHost() {
 whois.WHOIS_ADDRESS = "invalidhost.com";
 whois.startQuery();
 }

 @Test
 public void testQueryWithInvalidPort() {
 whois.WHOIS_PORT = 1234;
 whois.startQuery();
 // Add assertions to verify the error message
 }

 @Test
 public void testReset() {
 whois.reset();
 assertEquals("", whois.queryTextField.getText());
 assertFalse(whois.lowercaseD.isSelected());
 assertEquals(0, whois.inverseQueries.getSelectedIndex());
 assertEquals(0, whois.miscList.getSelectedIndex());
 }
}