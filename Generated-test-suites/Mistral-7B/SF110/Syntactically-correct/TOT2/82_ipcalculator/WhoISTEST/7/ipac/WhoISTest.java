import org.junit.Test;
import ipac.WhoIS;
import static org.junit.Assert.*;

public class WhoISTest {

 @Test
 public void testConstructor() {
 WhoIS whoIS = new WhoIS();
 // Check if the initial values of instance variables are set correctly
 assertEquals("APNIC", whoIS.WHOIS_NAME);
 assertEquals("whois.apnic.net", whoIS.WHOIS_ADDRESS);
 assertEquals(43, whoIS.WHOIS_PORT);
 // Check if queryPanel, additionalQueriesPanel, and queryHintsPanel are created correctly
 assertNotNull(whoIS.queryPanel());
 assertNotNull(whoIS.additionalQueriesPanel());
 assertNotNull(whoIS.queryHintsPanel());
 }

 @Test
 public void testQueryButtonActionPerformed() {
 WhoIS whoIS = new WhoIS();
 // Set up mock queryTextField and queryButton
 MockComponent queryTextFieldMock = mock(JTextField.class);
 MockComponent queryButtonMock = mock(JButton.class);
 when(queryButtonMock.getActionCommands()).thenReturn("Query");
 // Set up the mock JFrame and add the mock components
 JFrame mockFrame = mock(JFrame.class);
 when(mockFrame.getContentPane()).thenReturn(whoIS);
 whoIS.setJFrame(mockFrame);
 // Set up the mock QueryPanel and add the mock components
 JPanel queryPanelMock = mock(JPanel.class);
 when(whoIS.queryPanel()).thenReturn(queryPanelMock);
 // Set up the mock queryTextField and queryButton in the mock queryPanel
 when(queryPanelMock.getComponent(0)).thenReturn(queryTextFieldMock);
 when(queryPanelMock.getComponent(1)).thenReturn(queryButtonMock);
 // Set up the mock JTextArea to receive the query output
 MockComponent outputAreaMock = mock(JTextArea.class);
 // Set up the WhoISOutput to display the output in the mock JTextArea
 WhoISOutput whoISOutput = new WhoISOutput(outputAreaMock);
 whoIS.setWhoISOutput(whoISOutput);
 // Simulate the user typing a query and pressing the query button
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.queryButton.doClick();
 // Check if the query method is called with the correct query
 verify(whoIS).query(query);
 // Check if the output is displayed in the output area
 verify(outputAreaMock).setText(anyString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartQueryWithEmptyQuery() {
 WhoIS whoIS = new WhoIS();
 whoIS.startQuery();
 }

 @Test
 public void testStartQueryWithValidQuery() {
 WhoIS whoIS = new WhoIS();
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.startQuery();
 }

 @Test
 public void testDisplayError() {
 WhoIS whoIS = new WhoIS();
 whoIS.displayError("Error message");
 // Check if the error message is displayed in a JOptionPane
 verify(new JOptionPane()).showMessageDialog(any(JFrame.class), "Error message", "Error", 0);
 }

 @Test
 public void testQueryWithInvalidWhoisAddress() {
 WhoIS whoIS = new WhoIS();
 whoIS.WHOIS_ADDRESS = "invalid.whois.address";
 String query = "example.com";
 try {
 whoIS.query(query);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testQueryWithInvalidWhoisPort() {
 WhoIS whoIS = new WhoIS();
 whoIS.WHOIS_PORT = -1;
 String query = "example.com";
 try {
 whoIS.query(query);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testQueryWithInvalidInput() {
 WhoIS whoIS = new WhoIS();
 String query = "invalidquery";
 try {
 whoIS.query(query);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testReset() {
 WhoIS whoIS = new WhoIS();
 whoIS.reset();
 // Check if the queryTextField is empty
 assertEquals("", whoIS.queryTextField.getText());
 // Check if the lowercaseD checkbox is unselected
 assertFalse(whoIS.lowercaseD.isSelected());
 // Check if the inverseQueries and miscList are reset to their initial states
 assertEquals("None", whoIS.inverseQueries.getSelectedItem().toString());
 assertEquals("All", whoIS.miscList.getSelectedValue());
 }
}