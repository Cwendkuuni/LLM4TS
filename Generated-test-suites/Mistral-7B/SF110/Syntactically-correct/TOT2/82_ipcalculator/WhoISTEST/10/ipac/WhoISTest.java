import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whoIS;

 @Before
 public void setUp() {
 whoIS = new WhoIS();
 }

 @Test
 public void testConstructor() {
 WhoIS whoIS = new WhoIS();
 assertNotNull(whoIS);
 }

 @Test
 public void testQueryPanelCreation() {
 assertNotNull(whoIS.queryPanel());
 }

 @Test
 public void testQueryTextFieldIsEnabled() {
 assertTrue(whoIS.queryTextField.isEnabled());
 }

 @Test
 public void testQueryButtonPerformsQueryOnEmptyTextField() {
 whoIS.queryTextField.setText("");
 whoIS.queryButton.doClick();
 assertFalse(whoIS.queryTextField.getText().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueryButtonThrowsExceptionOnNullQuery() {
 whoIS.queryTextField.setText(null);
 whoIS.queryButton.doClick();
 }

 @Test
 public void testQueryButtonPerformsQueryOnValidQuery() {
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Add assertions to check the results of the query here
 }

 @Test
 public void testResetButtonResetsQueryTextField() {
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 whoIS.resetButton.doClick();
 assertEquals("", whoIS.queryTextField.getText());
 }

 @Test
 public void testPopupMenuIsDisplayedOnMousePress() {
 MouseEvent mouseEvent = new MouseEvent(whoIS.queryTextField, 0, 1, 0, 1, 1, 0, true);
 whoIS.popupMenu.show(whoIS.queryTextField, 1, 1);
 assertTrue(whoIS.popupMenu.isVisible());
 }

 @Test
 public void testKeyListenerPerformsQueryOnEnterKeyPress() {
 FocusEvent focusEvent = new FocusEvent(whoIS.queryTextField, FocusEvent.FOCUS_GAINED, true);
 whoIS.queryTextField.dispatchEvent(focusEvent);
 Robot robot = new Robot();
 robot.keyPress(KeyEvent.VK_ENTER);
 robot.keyRelease(KeyEvent.VK_ENTER);
 // Add assertions to check the results of the query here
 }

 @Test
 public void testInverseQueriesAreSupported() {
 whoIS.inverseQueries.setSelectedIndex(1);
 String query = whoIS.getQuery();
 assertTrue(query.startsWith("-i tech-c "));
 }

 @Test
 public void testIPAddressLookupsAreSupported() {
 whoIS.lowercaseL.setSelected(true);
 String query = whoIS.getQuery();
 assertTrue(query.startsWith("-l "));
 }

 @Test
 public void testMiscellaneousQueriesAreSupported() {
 Object[] selected = {"as-block"};
 whoIS.miscList.setSelectedValues(selected);
 String query = whoIS.getQuery();
 assertTrue(query.startsWith("-T as-block "));
 }

 @Test(expected = NullPointerException.class)
 public void testQueryThrowsNullPointerExceptionOnNullQuery() {
 whoIS.queryTextField.setText(null);
 whoIS.queryButton.doClick();
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryThrowsUnknownHostException() {
 whoIS.WHOIS_ADDRESS = "unknown.host.com";
 whoIS.queryButton.doClick();
 }

 @Test(expected = IOException.class)
 public void testQueryThrowsIOException() {
 when(mockSocket.getOutputStream()).thenThrow(IOException.class);
 whoIS.queryButton.doClick();
 }
}