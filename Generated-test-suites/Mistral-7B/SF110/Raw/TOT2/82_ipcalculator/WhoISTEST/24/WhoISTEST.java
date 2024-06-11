import org.junit.Assert;
import org.junit.Test;
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
 // Check that the constructor initializes the class variables correctly
 Assert.assertEquals("APNIC", whoIS.WHOIS_NAME);
 Assert.assertEquals("whois.apnic.net", whoIS.WHOIS_ADDRESS);
 Assert.assertEquals(43, whoIS.WHOIS_PORT);
 }

 @Test
 public void testQueryPanel() {
 // Check that the queryPanel method returns a valid JPanel
 JPanel queryPanel = whoIS.queryPanel();
 Assert.assertNotNull(queryPanel);
 // Check that the queryTextField and queryButton are added to the queryPanel
 Assert.assertEquals(3, queryPanel.getComponentCount());
 Assert.assertTrue(queryPanel.getComponent(0) instanceof JLabel);
 Assert.assertTrue(queryPanel.getComponent(1) instanceof JTextField);
 Assert.assertTrue(queryPanel.getComponent(2) instanceof JButton);
 }

 @Test
 public void testAdditionalQueriesPanel() {
 // Check that the additionalQueriesPanel method returns a valid JPanel
 JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
 Assert.assertNotNull(additionalQueriesPanel);
 // Check that the IPAddressLookups and MiscellaneousQueries components are added to the additionalQueriesPanel
 Assert.assertEquals(2, additionalQueriesPanel.getComponentCount());
 Assert.assertTrue(additionalQueriesPanel.getComponent(0) instanceof JPanel);
 Assert.assertTrue(additionalQueriesPanel.getComponent(1) instanceof JPanel);
 }

 @Test
 public void testIPAddressLookups() {
 JPanel lookupsPanel = whoIS.IPAddressLookups();
 // Check that the lookupsPanel method returns a valid JPanel
 Assert.assertNotNull(lookupsPanel);
 // Check that the radio buttons are added to the lookupsPanel
 Assert.assertEquals(7, lookupsPanel.getComponentCount());
 for (int i = 0; i < 7; i++) {
 Assert.assertTrue(lookupsPanel.getComponent(i) instanceof JRadioButton);
 }
 }

 @Test
 public void testMiscellaneousQueries() {
 JPanel miscPanel = whoIS.MiscellaneousQueries();
 // Check that the miscPanel method returns a valid JPanel
 Assert.assertNotNull(miscPanel);
 // Check that the JComboBox and JList are added to the miscPanel
 Assert.assertEquals(2, miscPanel.getComponentCount());
 Assert.assertTrue(miscPanel.getComponent(0) instanceof JPanel);
 Assert.assertTrue(miscPanel.getComponent(1) instanceof JScrollPane);
 }

 @Test
 public void testQueryHintsPanel() {
 JPanel queryHintsPanel = whoIS.queryHintsPanel();
 // Check that the queryHintsPanel method returns a valid JPanel
 Assert.assertNotNull(queryHintsPanel);
 // Check that the innerPanel and JLabel components are added to the queryHintsPanel
 Assert.assertEquals(2, queryHintsPanel.getComponentCount());
 Assert.assertTrue(queryHintsPanel.getComponent(0) instanceof JPanel);
 Assert.assertTrue(queryHintsPanel.getComponent(1) instanceof JLabel);
 }

 @Test
 public void testCreatePopupMenu() {
 // Check that the createPopupMenu method initializes the popupMenu with the correct items
 Assert.assertEquals(5, whoIS.popupMenu.getComponentCount());
 for (int i = 0; i < 5; i++) {
 Assert.assertTrue(whoIS.popupMenu.getComponent(i) instanceof JMenuItem);
 }
 }

 @Test
 public void testActionPerformed() {
 // Create a mock ActionEvent and set the source to the queryButton
 ActionEvent event = new ActionEvent(whoIS.queryButton, 0, "Query Clicked");
 // Call the actionPerformed method and check that the startQuery method is called
 whoIS.actionPerformed(event);
 Assert.assertTrue(whoIS.startQueryCalled);
 }

 @Test(expected = NullPointerException.class)
 public void testStartQueryWithEmptyQuery() {
 whoIS.startQuery();
 // Check that an exception is thrown when there is no query
 }

 @Test
 public void testQuery() {
 // Create a mock Socket, PrintStream, and BufferedReader
 Socket connection = new MockSocket();
 PrintStream out = new MockPrintStream(connection.getOutputStream());
 BufferedReader in = new MockBufferedReader();
 // Set up the mocks to simulate the Whois server response
 out.println("Test Response");
 // Call the query method and check that it returns the expected response
 String output = whoIS.query("Test Query");
 Assert.assertEquals("Test Response\n", output);
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithUnknownHost() {
 whoIS.query("Test Query");
 // Check that an exception is thrown when the Whois server cannot be reached
 }

 @Test(expected = IOException.class)
 public void testQueryWithServerError() {
 whoIS.query("Test Query");
 // Create a mock Socket, PrintStream, and BufferedReader with an error
 Socket connection = new MockSocketWithError();
 PrintStream out = new MockPrintStream(connection.getOutputStream());
 BufferedReader in = new MockBufferedReader();
 // Call the query method and check that an exception is thrown
 whoIS.query("Test Query");
 }
}