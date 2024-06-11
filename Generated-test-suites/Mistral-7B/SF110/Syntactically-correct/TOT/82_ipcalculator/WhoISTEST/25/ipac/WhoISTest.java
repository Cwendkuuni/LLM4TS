import org.junit.Before;
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
 public void testQueryButtonActionPerformed() {
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.queryButton.doClick();
 // Verify that query method is called with the correct query
 assertTrue(whoIS.getQuery().contains(query));
 // Verify that WhoISOutput is displayed correctly
 // Add assertions to check the contents of WhoISOutput
 }

 @Test
 public void testEmptyQuery() {
 whoIS.queryButton.doClick();
 String errorMessage = whoIS.getDisplayedErrorMessage();
 assertEquals("No Query Detected", errorMessage);
 }

 @Test
 public void testInvalidQuery() {
 whoIS.queryTextField.setText("invalidQuery");
 whoIS.queryButton.doClick();
 String errorMessage = whoIS.getDisplayedErrorMessage();
 assertEquals("Unknown query type: invalidQuery", errorMessage);
 }

 @Test
 public void testMalformedQueryWithInverse() {
 whoIS.queryTextField.setText("asn1234 -i invalid");
 whoIS.queryButton.doClick();
 String errorMessage = whoIS.getDisplayedErrorMessage();
 assertEquals("Invalid inverse query type: invalid", errorMessage);
 }

 @Test
 public void testMalformedQueryWithMiscType() {
 String[] miscTypes = {"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"};
 miscTypes[6] = "invalidType";
 whoIS.inverseQueries.setSelectedItems(miscTypes);
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 String errorMessage = whoIS.getDisplayedErrorMessage();
 assertEquals("Invalid misc type: invalidType", errorMessage);
 }

 @Test
 public void testValidQueryWithAllMiscTypes() {
 String[] miscTypes = {"All"};
 whoIS.inverseQueries.setSelectedItems(miscTypes);
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Verify that all misc types are included in the query
 assertTrue(whoIS.getQuery().contains("-T All"));
 }

 @Test
 public void testValidQueryWithCustomMiscTypes() {
 String[] miscTypes = {"as-set", "filter-set", "rtr-set"};
 whoIS.inverseQueries.setSelectedItems(miscTypes);
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Verify that only selected misc types are included in the query
 assertTrue(whoIS.getQuery().contains("-T as-set"));
 assertTrue(whoIS.getQuery().contains("-T filter-set"));
 assertTrue(whoIS.getQuery().contains("-T rtr-set"));
 }

 @Test
 public void testValidQueryWithInverse() {
 whoIS.inverseQueries.setSelectedItem("tech-c");
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Verify that inverse query is included in the query
 assertTrue(whoIS.getQuery().contains("-i tech-c"));
 }

 @Test
 public void testValidQueryWithLowercaseL() {
 whoIS.lowercaseL.doClick();
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Verify that lowercase L query option is included in the query
 assertTrue(whoIS.getQuery().contains("-l"));
 }

 @Test
 public void testValidQueryWithUppercaseL() {
 whoIS.uppercaseL.doClick();
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Verify that uppercase L query option is included in the query
 assertTrue(whoIS.getQuery().contains("-L"));
 }

 @Test
 public void testValidQueryWithLowercaseM() {
 whoIS.lowercaseM.doClick();
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Verify that lowercase M query option is included in the query
 assertTrue(whoIS.getQuery().contains("-m"));
 }

 @Test
 public void testValidQueryWithUppercaseM() {
 whoIS.uppercaseM.doClick();
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Verify that uppercase M query option is included in the query
 assertTrue(whoIS.getQuery().contains("-M"));
 }

 @Test
 public void testValidQueryWithLowercaseX() {
 whoIS.lowercaseX.doClick();
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Verify that exact match query option is included in the query
 assertTrue(whoIS.getQuery().contains("-x"));
 }

 @Test
 public void testValidQueryWithBooleanD() {
 whoIS.lowercaseD.doClick();
 whoIS.queryTextField.setText("example.com");
 whoIS.queryButton.doClick();
 // Verify that boolean D query option is included in the query
 assertTrue(whoIS.getQuery().contains("-d"));
 }
}