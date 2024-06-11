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
 assertNotNull(whois);
 }

 @Test
 public void testQueryPanelCreation() {
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testAdditionalQueriesPanelCreation() {
 JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
 assertNotNull(additionalQueriesPanel);
 }

 @Test
 public void testIPAddressLookupsCreation() {
 JPanel lookupPanel = whois.IPAddressLookups();
 assertNotNull(lookupPanel);
 }

 @Test
 public void testMiscellaneousQueriesCreation() {
 JPanel miscPanel = whois.MiscellaneousQueries();
 assertNotNull(miscPanel);
 }

 @Test
 public void testQueryTextFieldCreation() {
 JTextField queryTextField = whois.queryTextField;
 assertNotNull(queryTextField);
 }

 @Test
 public void testQueryButtonCreation() {
 JButton queryButton = whois.queryButton;
 assertNotNull(queryButton);
 }

 @Test
 public void testResetButtonCreation() {
 JButton resetButton = whois.resetButton;
 assertNotNull(resetButton);
 }

 @Test
 public void testCreatePopupMenu() {
 JPopupMenu popupMenu = whois.popupMenu;
 assertNotNull(popupMenu);
 }

 @Test
 public void testActionPerformedQueryButton() {
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, "Query Button Clicked"));
 whois.startQuery();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyQueryTextField() {
 whois.queryTextField.setText("");
 whois.startQuery();
 }

 @Test
 public void testInvalidQuery() {
 whois.queryTextField.setText("Invalid Query");
 whois.startQuery();
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownWhoisHost() {
 whois.WHOIS_ADDRESS = "InvalidHost";
 whois.queryTextField.setText("ValidQuery");
 whois.startQuery();
 }

 @Test(expected = IOException.class)
 public void testWhoisServerCouldNotBeContacted() {
 whois.WHOIS_PORT = -1;
 whois.queryTextField.setText("ValidQuery");
 whois.startQuery();
 }

 @Test
 public void testDisplayError() {
 whois.displayError("Error Message");
 String errorMessage = JOptionPane.showInputDialog(whois, "An Error has Occurred", "Error", JOptionPane.ERROR_MESSAGE);
 assertEquals("Error Message", errorMessage);
 }

 @Test
 public void testGetQuery_NoInverseQueries() {
 whois.queryTextField.setText("ValidQuery");
 String query = whois.getQuery();
 assertEquals("ValidQuery", query);
 }

 @Test
 public void testGetQuery_WithInverseQueries() {
 whois.inverseQueries.setSelectedItem("admin-c");
 whois.queryTextField.setText("ValidQuery");
 String query = whois.getQuery();
 assertEquals("-i admin-c ValidQuery", query);
 }

 @Test
 public void testGetQuery_WithLowerCaseL() {
 whois.lowercaseL.setSelected(true);
 whois.queryTextField.setText("ValidQuery");
 String query = whois.getQuery();
 assertEquals("-l ValidQuery", query);
 }

 @Test
 public void testGetQuery_WithUpperCaseL() {
 whois.uppercaseL.setSelected(true);
 whois.queryTextField.setText("ValidQuery");
 String query = whois.getQuery();
 assertEquals("-L ValidQuery", query);
 }

 @Test
 public void testGetQuery_WithLowerCaseM() {
 whois.lowercaseM.setSelected(true);
 whois.queryTextField.setText("ValidQuery");
 String query = whois.getQuery();
 assertEquals("-m ValidQuery", query);
 }

 @Test
 public void testGetQuery_WithUpperCaseM() {
 whois.uppercaseM.setSelected(true);
 whois.queryTextField.setText("ValidQuery");
 String query = whois.getQuery();
 assertEquals("-M ValidQuery", query);
 }

 @Test
 public void testGetQuery_WithLowerCaseX() {
 whois.lowercaseX.setSelected(true);
 whois.queryTextField.setText("ValidQuery");
 String query = whois.getQuery();
 assertEquals("-x ValidQuery", query);
 }

 @Test
 public void testGetQuery_WithBooleanD() {
 whois.lowercaseD.setSelected(true);
 whois.queryTextField.setText("ValidQuery");
 String query = whois.getQuery();
 assertEquals("-d ValidQuery", query);
 }

 @Test
 public void testGetQuery_WithMiscQueries() {
 whois.miscList.setSelectedValues(new Object[]{"as-set"});
 whois.queryTextField.setText("ValidQuery");
 String query = whois.getQuery();
 assertEquals("-T as-set ValidQuery", query);
 }
}