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
 WhoIS whois = new WhoIS();
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testQueryTextField() {
 WhoIS whois = new WhoIS();
 JTextField queryTextField = whois.queryPanel().getComponents()[1];
 assertInstanceof(JTextField.class, queryTextField);
 assertEquals(40, queryTextField.getColumns());
 }

 @Test
 public void testQueryButton() {
 WhoIS whois = new WhoIS();
 JButton queryButton = whois.queryPanel().getComponents()[3];
 assertInstanceof(JButton.class, queryButton);
 assertEquals("Query", queryButton.getText());
 }

 @Test
 public void testResetButton() {
 WhoIS whois = new WhoIS();
 JButton resetButton = whois.queryPanel().getComponents()[4];
 assertInstanceof(JButton.class, resetButton);
 assertEquals("Reset", resetButton.getText());
 }

 @Test
 public void testQueryTextFieldKeyEvent() throws KeyEvent {
 WhoIS whois = new WhoIS();
 JTextField queryTextField = whois.queryPanel().getComponents()[1];
 Robot robot = new Robot();
 robot.keyPress(KeyEvent.VK_ENTER);
 whois.startQuery();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyQuery() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 }

 @Test
 public void testInvalidQuery() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("InvalidQuery");
 whois.startQuery();
 }

 @Test
 public void testInvalidIPAddress() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("256.256.256.256");
 whois.startQuery();
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHost() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = "unknown.host.com";
 whois.startQuery();
 }

 @Test(expected = IOException.class)
 public void testIOException() {
 WhoIS whois = new WhoIS();
 doReturn(null).when(whois).getQuery();
 whois.startQuery();
 }

 @Test(expected = SocketException.class)
 public void testSocketException() {
 WhoIS whois = new WhoIS();
 doThrow(new SocketException()).when(whois).query(whois.getQuery());
 whois.startQuery();
 }

 @Test
 public void testQueryWithInverseQueries() {
 WhoIS whois = new WhoIS();
 whois.inverseQueries.setSelectedIndex(1);
 whois.startQuery();
 }

 @Test
 public void testQueryWithCustomObjectTypes() {
 WhoIS whois = new WhoIS();
 Object[] customObjectTypes = {"as-block", "mntner"};
 whois.miscList.setSelectedValues(customObjectTypes);
 whois.startQuery();
 }

 @Test
 public void testQueryWithLowercaseSearch() {
 WhoIS whois = new WhoIS();
 whois.lowercaseL.setSelected(true);
 whois.startQuery();
 }

 @Test
 public void testQueryWithUppercaseSearch() {
 WhoIS whois = new WhoIS();
 whois.uppercaseL.setSelected(true);
 whois.startQuery();
 }

 @Test
 public void testQueryWithExactMatch() {
 WhoIS whois = new WhoIS();
 whois.lowercaseX.setSelected(true);
 whois.startQuery();
 }

 @Test
 public void testQueryWithBooleanParameter() {
 WhoIS whois = new WhoIS();
 whois.lowercaseD.setSelected(true);
 whois.startQuery();
 }
}