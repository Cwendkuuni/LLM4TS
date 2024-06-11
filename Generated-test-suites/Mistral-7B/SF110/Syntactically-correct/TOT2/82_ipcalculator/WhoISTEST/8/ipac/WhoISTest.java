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
 WhoISPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 }

 @Test
 public void testQueryTextFieldInitialValue() {
 WhoIS whois = new WhoIS();
 assertEquals("", whois.queryTextField.getText());
 }

 @Test
 public void testQueryButtonActionPerformed() {
 WhoIS whois = new WhoIS();
 when(whois.queryTextField.getText()).thenReturn("example.com");
 whois.queryButton.doClick();
 verify(whois).startQuery();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartQueryWithEmptyQuery() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 }

 @Test
 public void testQueryWithValidInput() {
 WhoIS whois = new WhoIS();
 when(whois.queryTextField.getText()).thenReturn("example.com");
 whois.startQuery();
 // Add assertions to verify the expected output here
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithInvalidHost() {
 WhoIS whois = new WhoIS();
 when(whois.queryTextField.getText()).thenReturn("invalid.host");
 whois.startQuery();
 }

 @Test(expected = IOException.class)
 public void testQueryWithServerConnectionError() {
 WhoIS whois = new WhoIS();
 when(whois.queryTextField.getText()).thenReturn("example.com");
 when(whois.socketFactory.createSocket( eq("whois.example.com"), anyInt())).thenThrow( new IOException() );
 whois.startQuery();
 }

 @Test
 public void testResetButtonActionPerformed() {
 WhoIS whois = new WhoIS();
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText());
 }

 @Test
 public void testInverseQueriesComboBoxItems() {
 WhoIS whois = new WhoIS();
 assertEquals(20, whois.inverseQueries.getItemCount());
 }

 @Test
 public void testMiscListItems() {
 WhoIS whois = new WhoIS();
 assertEquals(16, whois.miscList.getItemCount());
 }

 @After
 public void tearDown() {
 whois = null;
 }
}