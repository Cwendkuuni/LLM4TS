import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setup() {
 whois = new WhoIS();
 }

 @Test
 public void testQueryButtonActionPerformedWithValidQuery() {
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 // Verify that the query method was called with the correct query
 String expectedQuery = "-i None -l query -T all,aut-num,domain " + query;
 String actualQuery = whois.getQuery();
 assertEquals(expectedQuery, actualQuery);
 // Verify that the WhoISOutput window is displayed with the correct output
 // (This would typically involve checking the contents of the JList or JTextArea in the output window)
 }

 @Test
 public void testQueryButtonActionPerformedWithEmptyQuery() {
 whois.queryButton.doClick();
 String actualQuery = whois.getQuery();
 assertEquals("", actualQuery);
 }

 @Test
 public void testQueryButtonActionPerformedWithInvalidQuery() {
 whois.queryTextField.setText("invalid query");
 whois.queryButton.doClick();
 String actualQuery = whois.getQuery();
 assertEquals("invalid query", actualQuery);
 }

 @Test
 public void testResetButtonActionPerformed() {
 whois.queryButton.doClick();
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText());
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithUnknownHost() {
 whois.WHOIS_ADDRESS = "unknown.host";
 whois.query("test query");
 }

 @Test(expected = IOException.class)
 public void testQueryWithServerConnectionError() {
 whois.WHOIS_ADDRESS = "valid.host";
 whois.WHOIS_PORT = 1234;
 whois.query("test query");
 }

 @Test
 public void testInverseQueriesWithValidQueries() {
 whois.inverseQueries.setSelectedItem("admin-c,tech-c,zone-c");
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 // Verify that the inverse queries were included in the query
 String expectedQuery = "-i admin-c,tech-c,zone-c -l query -T all,aut-num,domain " + query;
 String actualQuery = whois.getQuery();
 assertEquals(expectedQuery, actualQuery);
 }

 @Test
 public void testMiscQueriesWithValidQueries() {
 whois.miscList.setSelectedValues(new Object[]{"as-set", "person"});
 String query = "example.com";
 whois.queryTextField.setText(query);
 whois.queryButton.doClick();
 // Verify that the misc queries were included in the query
 String expectedQuery = "-i None -l query -T as-set,person all,aut-num,domain " + query;
 String actualQuery = whois.getQuery();
 assertEquals(expectedQuery, actualQuery);
 }
}