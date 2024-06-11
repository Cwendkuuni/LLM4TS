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
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois.WHOIS_NAME);
 assertNotNull(whois.WHOIS_ADDRESS);
 assertNotNull(whois.WHOIS_PORT);
 assertNotNull(whois.queryTextField);
 assertNotNull(whois.queryButton);
 assertNotNull(whois.resetButton);
 assertNotNull(whois.lowercaseL);
 assertNotNull(whois.uppercaseL);
 assertNotNull(whois.lowercaseM);
 assertNotNull(whois.uppercaseM);
 assertNotNull(whois.lowercaseX);
 assertNotNull(whois.resetRadioButton);
 assertNotNull(whois.radioButtonGroup);
 assertNotNull(whois.popupMenu);
 assertNotNull(whois.inverseQueries);
 assertNotNull(whois.miscList);
 }

 @Test
 public void testQueryPanelCreation() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 assertNotNull(queryPanel.getLayout());
 assertNotNull(queryLabel);
 assertNotNull(queryTextField);
 assertNotNull(queryButton);
 assertNotNull(resetButton);
 }

 @Test
 public void testAdditionalQueriesPanelCreation() {
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 assertNotNull(additionalQueriesPanel.getLayout());
 assertNotNull(Lookups);
 assertNotNull(MiscellaneousQueries);
 }

 @Test
 public void testQueryHintsPanelCreation() {
 JPanel queryHintsPanel = whois.queryHintsPanel();
 assertNotNull(queryHintsPanel);
 assertNotNull(queryHintsPanel.getLayout());
 assertNotNull(queryHintsText1);
 assertNotNull(emptyLabel);
 assertNotNull(queryHintsText2);
 assertNotNull(queryHintsText3);
 assertNotNull(queryHintsText4);
 assertNotNull(queryHintsText5);
 }

 @Test
 public void testCreatePopupMenu() {
 assertNotNull(whois.popupMenu);
 assertNotNull(cut);
 assertNotNull(copy);
 assertNotNull(paste);
 assertNotNull(selectAll);
 }

 @Test
 public void testActionPerformedQueryButton() {
 whois.actionPerformed(new ActionEvent(queryButton, 0, "Query Button Clicked"));
 assertFalse(whois.queryTextField.getText().isEmpty());
 whois.reset();
 }

 @Test
 public void testActionPerformedResetButton() {
 whois.actionPerformed(new ActionEvent(resetButton, 0, "Reset Button Clicked"));
 assertTrue(whois.queryTextField.getText().isEmpty());
 }

 @Test
 public void testStartQueryWithEmptyTextField() {
 whois.displayError = mock(JOptionPane.class);
 whois.startQuery();
 verify(whois.displayError).showMessageDialog(whois, "No Query Detected", "Error", 0);
 }

 @Test
 public void testStartQueryWithValidQuery() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 verify(whois.displayError, never()).showMessageDialog(whois, anyString(), anyString(), anyInt());
 }

 @Test
 public void testQueryWithInvalidWhoisAddress() {
 whois.WHOIS_ADDRESS = "invalid.address";
 whois.displayError = mock(JOptionPane.class);
 whois.startQuery();
 verify(whois.displayError).showMessageDialog(whois, "Whois server could not be contacted.\nPlease check your internet connection.", "Error", 0);
 }

 @Test
 public void testQueryWithUnknownWhoisName() {
 whois.WHOIS_NAME = "UnknownWhois";
 try {
 whois.query("example.com");
 fail("Expected UnknownHostException");
 } catch (UnknownHostException e) {
 // Expected
 }
 }

 @Test
 public void testGetQueryWithNoInverseQueries() {
 String query = "example.com";
 whois.inverseQueries.setSelectedIndex(0);
 String result = whois.getQuery();
 assertEquals("example.com", result);
 }

 @Test
 public void testGetQueryWithInverseQueries() {
 String query = "example.com";
 whois.inverseQueries.setSelectedIndex(1);
 String result = whois.getQuery();
 assertEquals("-i admin-c example.com", result);
 }

 @Test
 public void testGetQueryWithLowerCaseL() {
 String query = "example.com";
 whois.lowercaseL.setSelected(true);
 String result = whois.getQuery();
 assertEquals("-l example.com", result);
 }

 @Test
 public void testGetQueryWithUpperCaseL() {
 String query = "example.com";
 whois.uppercaseL.setSelected(true);
 String result = whois.getQuery();
 assertEquals("-L example.com", result);
 }

 @Test
 public void testGetQueryWithLowerCaseM() {
 String query = "example.com";
 whois.lowercaseM.setSelected(true);
 String result = whois.getQuery();
 assertEquals("-m example.com", result);
 }

 @Test
 public void testGetQueryWithUpperCaseM() {
 String query = "example.com";
 whois.uppercaseM.setSelected(true);
 String result = whois.getQuery();
 assertEquals("-M example.com", result);
 }

 @Test
 public void testGetQueryWithLowerCaseX() {
 String query = "example.com";
 whois.lowercaseX.setSelected(true);
 String result = whois.getQuery();
 assertEquals("-x example.com", result);
 }

 @Test
 public void testGetQueryWithBooleanD() {
 String query = "example.com";
 whois.lowercaseD.setSelected(true);
 String result = whois.getQuery();
 assertEquals("-d example.com", result);
 }

 @Test
 public void testGetQueryWithMiscQueries() {
 String query = "example.com";
 Object[] selected = { "inetnum" };
 whois.miscList.setSelectedValues(selected);
 String result = whois.getQuery();
 assertEquals("-T inetnum example.com", result);
 }

 @Test
 public void testGetQueryWithAllMiscQueries() {
 String query = "example.com";
 Object[] selected = { "All" };
 whois.miscList.setSelectedValues(selected);
 String result = whois.getQuery();
 assertEquals("example.com", result);
 }
}