import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @After
 public void tearDown() {
 whois = null;
 }

 @Test
 public void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 }

 @Test
 public void testQueryTextFieldInitialValue() {
 assertEquals("", whois.queryTextField.getText().trim());
 }

 @Test
 public void testQueryButtonActionPerformedWithEmptyTextField() {
 whois.actionPerformed(new ActionEvent(new Object(), 0, "queryButtonActionPerformed"));
 assertEquals("No Query Detected", whois.getErrorMessage());
 }

 @Test
 public void testQueryButtonActionPerformedWithNonEmptyTextField() {
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(new Object(), 0, "queryButtonActionPerformed"));
 String expectedOutput = "..."; // Add expected Whois server output here
 String actualOutput = whois.getOutput();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testResetButtonActionPerformed() {
 whois.resetButton.doClick();
 assertEquals("", whois.queryTextField.getText().trim());
 }

 @Test
 public void testInverseQueries() {
 whois.inverseQueries.setSelectedIndex(1);
 String query = whois.getQuery();
 assertEquals("-i admin-c example.com", query);
 }

 @Test
 public void testMiscQueries() {
 Object[] selectedValues = new Object[]{"as-block", "person"};
 whois.miscList.setSelectedValues(selectedValues);
 String query = whois.getQuery();
 assertEquals("-T as-block,person example.com", query);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMiscQueries() {
 Object[] selectedValues = new Object[]{"as-block", "invalid"};
 whois.miscList.setSelectedValues(selectedValues);
 whois.actionPerformed(new ActionEvent(new Object(), 0, "queryButtonActionPerformed"));
 }

 @Test
 public void testStartQueryWithEmptyTextFieldAndQueryHints() {
 whois.queryTextField.setText("");
 whois.queryHintsPanel.getInnerPanel().getQueryHintsText1().doClick();
 whois.queryHintsPanel.getInnerPanel().getQueryHintsText2().doClick();
 whois.actionPerformed(new ActionEvent(new Object(), 0, "queryButtonActionPerformed"));
 assertEquals("AS4808", whois.getQuery());
 }

 @Test
 public void testStartQueryWithNonEmptyTextFieldAndQueryHints() {
 whois.queryTextField.setText("example.com");
 whois.queryHintsPanel.getInnerPanel().getQueryHintsText1().doClick();
 whois.queryHintsPanel.getInnerPanel().getQueryHintsText2().doClick();
 whois.actionPerformed(new ActionEvent(new Object(), 0, "queryButtonActionPerformed"));
 String expectedOutput = "..."; // Add expected Whois server output here
 String actualOutput = whois.getOutput();
 assertEquals(expectedOutput, actualOutput);
 }
}