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
 public void testQueryPanelTextFields() {
 // Test queryTextField
 WhoIS.JTextField queryTextField = whois.queryTextField;
 assertEquals("Enter Whois database object you would like to query", queryTextField.getToolTipText());
 assertEquals(40, queryTextField.getColumns());
 // Test queryButton
 WhoIS.JButton queryButton = whois.queryButton;
 assertTrue(queryButton.isEnabled());
 // Test resetButton
 WhoIS.JButton resetButton = whois.resetButton;
 assertTrue(resetButton.isEnabled());
 }

 @Test
 public void testEmptyQuery() {
 // Set up
 WhoIS.JTextField queryTextField = whois.queryTextField;
 WhoIS.JButton queryButton = whois.queryButton;
 // Exercise
 queryButton.doClick();
 // Verify
 assertTrue(whois.displayErrorCalled); // Assuming displayError has a boolean flag to check if it was called
 }

 @Test
 public void testValidQuery() {
 // Set up
 WhoIS.JTextField queryTextField = whois.queryTextField;
 WhoIS.JButton queryButton = whois.queryButton;
 String query = "example.com";
 queryTextField.setText(query);
 // Exercise
 queryButton.doClick();
 // Verify
 // TODO: Implement verification logic here
 }

 // Add more test cases for other methods and functionality, including edge cases and error scenarios

}