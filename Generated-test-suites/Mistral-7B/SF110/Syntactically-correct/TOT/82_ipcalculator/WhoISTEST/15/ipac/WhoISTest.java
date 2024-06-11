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
 public void testQueryPanelIsCreated() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testQueryTextFieldIsCreated() {
 JTextField queryTextField = whois.queryTextField;
 assertNotNull(queryTextField);
 }

 @Test
 public void testQueryButtonIsCreated() {
 JButton queryButton = whois.queryButton;
 assertNotNull(queryButton);
 }

 @Test
 public void testResetButtonIsCreated() {
 JButton resetButton = whois.resetButton;
 assertNotNull(resetButton);
 }

 @Test
 public void testQueryIsEmpty() {
 assertEquals("", whois.getQuery());
 }

 @Test
 public void testQueryIsSet() {
 whois.queryTextField.setText("example.com");
 assertEquals("example.com", whois.getQuery());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueryIsNull() {
 whois.queryTextField.setText(null);
 whois.startQuery();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueryIsEmptyString() {
 whois.queryTextField.setText("");
 whois.startQuery();
 }

 @Test
 public void testStartQueryWithValidQuery() {
 whois.queryTextField.setText("example.com");
 whois.startQuery();
 // Add assertions to verify the response from the Whois server
 }

 @Test
 public void testStartQueryWithInvalidQuery() {
 whois.queryTextField.setText("invalidquery");
 whois.startQuery();
 // Add assertions to verify the error message displayed
 }

 @Test
 public void testReset() {
 whois.reset();
 assertEquals("", whois.queryTextField.getText());
 }
}