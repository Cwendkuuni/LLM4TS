package ipac.WhoISTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @Test
 public void testConstructor() {
 WhoIS whoisInstance = new WhoIS();
 assertNotNull(whoisInstance);
 }

 @Test
 public void testWHOIS_NAME() {
 String expected = "APNIC";
 assertEquals(expected, whois.WHOIS_NAME);
 }

 @Test
 public void testWHOIS_ADDRESS() {
 String expected = "whois.apnic.net";
 assertEquals(expected, whois.WHOIS_ADDRESS);
 }

 @Test
 public void testWHOIS_PORT() {
 int expected = 43;
 assertEquals(expected, whois.WHOIS_PORT);
 }

 @Test
 public void testQueryTextField() {
 assertNotNull(whois.queryTextField);
 }

 @Test(expected = NullPointerException.class)
 public void testQueryTextFieldNull() {
 WhoIS whoisNull = new WhoIS() {
 @Override
 private JPanel queryPanel() {
 return null;
 }
 };
 WhoIS.QueryTextField queryTextField = whoisNull.queryTextField;
 assertNull(queryTextField);
 }

 @Test
 public void testQueryButton() {
 assertNotNull(whois.queryButton);
 }

 @Test
 public void testResetButton() {
 assertNotNull(whois.resetButton);
 }

 @Test
 public void testLowercaseD() {
 assertNotNull(whois.lowercaseD);
 }

 @Test
 public void testLowercaseL() {
 assertNotNull(whois.lowercaseL);
 }

 @Test
 public void testUppercaseL() {
 assertNotNull(whois.uppercaseL);
 }

 @Test
 public void testLowercaseM() {
 assertNotNull(whois.lowercaseM);
 }

 @Test
 public void testUppercaseM() {
 assertNotNull(whois.uppercaseM);
 }

 @Test
 public void testLowercaseX() {
 assertNotNull(whois.lowercaseX);
 }

 @Test
 public void testResetRadioButton() {
 assertNotNull(whois.resetRadioButton);
 }

 @Test
 public void testRadioButtonGroup() {
 assertNotNull(whois.radioButtonGroup);
 }

 @Test
 public void testPopupMenu() {
 assertNotNull(whois.popupMenu);
 }

 @Test
 public void testInverseQueries() {
 assertNotNull(whois.inverseQueries);
 }

 @Test
 public void testMiscList() {
 assertNotNull(whois.miscList);
 }

 @Test
 public void testCreatePopupMenu() {
 assertNotNull(whois.popupMenu);
 }

 @Test
 public void testActionPerformed() {
 WhoISSpy spy = new WhoISSpy();
 whois.addMouseListener(spy);
 whois.queryButton.doClick();
 spy.assertQueryButtonClicked();
 }

 @Test
 public void testStartQuery() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.startQuery();
 // TODO: Implement verification of query execution
 }

 @Test
 public void testDisplayError() {
 String error = "Unknown error";
 whois.displayError(error);
 // TODO: Implement verification of error message display
 }

 @Test
 public void testQuery() {
 String query = "example.com";
 String expectedOutput = "..."; // Replace with expected output
 whois.queryTextField.setText(query);
 WhoISOutput output = whois.query(query);
 assertEquals(expectedOutput, output.getOutput());
 }

 @Test
 public void testGetQuery() {
 String query = "example.com";
 String inverseQueries = "person";
 String lowercaseL = "1st level less specific";
 String lowercaseM = "1st level more specific";
 String lowercaseX = "Exact match only";
 String booleanD = "true";
 String miscQueries = "route,mntner";
 WhoISQuery whoisQuery = new WhoISQuery(query, inverseQueries, lowercaseL, lowercaseM, lowercaseX, booleanD, miscQueries);
 String expected = "-i person -l example.com -m 1st level more specific -x Exact match only -d true -T route,mntner example.com";
 String actual = whois.getQuery();
 assertEquals(expected, actual);
 }

 @Test
 public void testReset() {
 whois.reset();
 String expected = "";
 assertEquals(expected, whois.queryTextField.getText());
 }
}

class WhoISOutput {

 private String output;

 public WhoISOutput(String output) {
 this.output = output;
 }

 public String getOutput() {
 return output;
 }
}

class WhoISQuery {

 private String query;
 private String inverseQueries;
 private String lowercaseL;
 private String lowercaseM;
 private String lowercaseX;
 private String booleanD;
 private String miscQueries;

 public WhoISQuery(String query, String inverseQueries, String lowercaseL, String lowercaseM, String lowercaseX, String booleanD, String miscQueries) {
 this.query = query;
 this.inverseQueries = inverseQueries;
 this.lowercaseL = lowercaseL;
 this.lowercaseM = lowercaseM;
 this.lowercaseX = lowercaseX;
 this.booleanD = booleanD;
 this.miscQueries = miscQueries;
 }
}

class WhoISSpy extends MouseAdapter {

 private int clickCount = 0;

 @Override
 public void mousePressed(MouseEvent e) {
 clickCount++;
 }

 @Override
 public void mouseReleased(MouseEvent e) {
 clickCount++;
 }

 public void assertQueryButtonClicked() {
 assertEquals(2, clickCount);
 clickCount = 0;
 }
}