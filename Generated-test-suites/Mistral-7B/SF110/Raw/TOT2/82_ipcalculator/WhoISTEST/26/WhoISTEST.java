import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whoIS;

 @BeforeEach
 void setup() {
 whoIS = new WhoIS();
 }

 @Test
 void testConstructor() {
 WhoIS whoIS = new WhoIS();
 assertNotNull(whoIS);
 }

 @Test
 void testQueryPanelCreation() {
 assertNotNull(whoIS.queryPanel());
 }

 @Test
 void testQueryTextField() {
 JTextField queryTextField = whoIS.queryPanel().getQueryTextField();
 assertNotNull(queryTextField);
 assertEquals(40, queryTextField.getColumns());
 }

 @Test
 void testQueryTextFieldMouseEvent() {
 MouseEvent mouseEvent = new MouseEvent(whoIS.queryTextField, 1, 1, 0, 1);
 whoIS.queryTextField.mousePressed(mouseEvent);
 whoIS.popupMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
 }

 @Test
 void testQueryTextFieldKeyPressEvent() {
 KeyEvent keyEvent = new KeyEvent(whoIS.queryTextField, 10, true, false, KeyEvent.CHAR_UNDEFINED, ' ');
 whoIS.queryTextField.dispatchEvent(keyEvent);
 whoIS.startQuery();
 }

 @Test
 void testQueryButtonActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(whoIS.queryButton, ActionEvent.ACTION_PERFORMED, "Query");
 whoIS.actionPerformed(actionEvent);
 whoIS.startQuery();
 }

 @Test
 void testResetButtonActionPerformed() {
 ActionEvent actionEvent = new ActionEvent(whoIS.resetButton, ActionEvent.ACTION_PERFORMED, "Reset");
 whoIS.actionPerformed(actionEvent);
 assertEquals("", whoIS.queryTextField.getText());
 }

 @Test
 void testEmptyQuery() {
 whoIS.startQuery();
 assertFalse(whoIS.getQuery().length() > 0);
 }

 @Test
 void testQueryWithInvalidWhoisAddress() {
 whoIS.WHOIS_ADDRESS = "invalid.whois.address";
 whoIS.startQuery();
 assertTrue(whoIS.queryTextField.getText().isEmpty());
 }

 @Test
 void testQueryWithInvalidWhoisPort() {
 whoIS.WHOIS_PORT = 123;
 whoIS.startQuery();
 assertTrue(whoIS.queryTextField.getText().isEmpty());
 }

 @Test
 void testQueryWithInvalidInput() {
 whoIS.queryTextField.setText("invalid input");
 whoIS.startQuery();
 assertTrue(whoIS.queryTextField.getText().isEmpty());
 }

 @Test
 void testQueryWithValidInput() {
 whoIS.queryTextField.setText("example.com");
 whoIS.startQuery();
 assertFalse(whoIS.queryTextField.getText().isEmpty());
 }

 @Test
 void testQueryWithInverseQueries() {
 whoIS.inverseQueries.setSelectedIndex(1);
 whoIS.queryTextField.setText("example.com");
 whoIS.startQuery();
 // TODO: Add assertions for inverse queries output
 }

 @Test
 void testQueryWithFilterFlags() {
 whoIS.lowercaseL.setSelected(true);
 whoIS.queryTextField.setText("example.com");
 whoIS.startQuery();
 // TODO: Add assertions for filter flags output
 }

 @AfterEach
 void teardown() {
 whoIS = null;
 }

}