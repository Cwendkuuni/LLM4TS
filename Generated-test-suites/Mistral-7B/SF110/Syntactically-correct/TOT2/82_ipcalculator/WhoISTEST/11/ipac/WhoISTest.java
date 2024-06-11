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
 public void testQueryPanelIsCreated() {
 assertNotNull(whois.queryPanel());
 }

 @Test
 public void testTextfieldsAreEmptyInitially() {
 assertEquals("", whois.queryTextField.getText());
 }

 @Test
 public void testQueryTextFieldListensToEnterKey() {
 WhoIS spyWhoIS = spy(WhoIS.class);
 doCallRealMethod().when(spyWhoIS).startQuery();
 MockEventMouseEvent mouseEvent = new MockEventMouseEvent(
 MouseEvent.MOUSE_CLICKED, 1, 1, 1, 1, 1, MouseEvent.NOBUTTONS);
 spyWhoIS.queryTextField.mousePressed(mouseEvent);
 verify(spyWhoIS).startQuery();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyQueryThrowsException() {
 whois.startQuery();
 }

 @Test
 public void testValidQueryIsProcessed() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 // Add assertions here to check the response
 }

 @Test
 public void testInverseQueriesIsSelectedNoneInitially() {
 assertEquals("None", whois.inverseQueries.getSelectedItem().toString());
 }

 @Test
 public void testMiscQueriesIsSelectedAllInitially() {
 String[] selectedItems = whois.miscList.getSelectedValues();
 assertEquals("All", selectedItems[0]);
 }

 @Test(expected = NullPointerException.class)
 public void testNullWHOIS_ADDRESSThrowsException() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = null;
 whois.query();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPortNumberThrowsException() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_PORT = -1;
 whois.query();
 }

 @Test
 public void testQueryStringIsFormedCorrectly() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.lowercaseL.setSelected(true);
 whois.inverseQueries.setSelectedItem("admin-c");
 whois.miscList.setSelectedValue("aut-num");
 String expectedQuery = "-l example.com -i admin-c -T aut-num";
 assertEquals(expectedQuery, whois.getQuery());
 }
}