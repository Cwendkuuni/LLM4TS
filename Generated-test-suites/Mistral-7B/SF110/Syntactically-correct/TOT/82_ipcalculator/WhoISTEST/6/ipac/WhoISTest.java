package ipac.test;

import static org.junit.Assert.*;

import ipac.WhoIS;
import ipac.events.WhoISOutput;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;

import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

 private WhoIS whoIS;
 private WhoISOutput output;

 @Before
 public void setUp() {
 whoIS = new WhoIS();
 output = new WhoISOutput();
 }

 @Test
 public void testQueryButtonActionPerformed() {
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.queryButton.doClick();

 assertEquals("query:" + query, whoIS.getQuery());
 assertTrue(whoIS.isQuerying());
 // Add assertions for expected query output here
 }

 @Test
 public void testEmptyQueryTextField() {
 whoIS.queryButton.doClick();
 assertFalse(whoIS.isQuerying());
 }

 @Test
 public void testInvalidQuery() {
 whoIS.queryTextField.setText("invalid query");
 whoIS.queryButton.doClick();
 assertFalse(whoIS.isQuerying());
 }

 @Test
 public void testExceptionOnQuery() {
 whoIS.WHOIS_ADDRESS = "invalid.address";
 whoIS.queryButton.doClick();
 assertFalse(whoIS.isQuerying());
 }

 @Test
 public void testResetButtonActionPerformed() {
 whoIS.queryButton.doClick();
 whoIS.resetButton.doClick();
 assertEquals("", whoIS.queryTextField.getText());
 }

 @Test
 public void testInverseQueries() {
 whoIS.inverseQueries.setSelectedIndex(1);
 String query = "example.com";
 whoIS.queryTextField.setText(query);
 whoIS.queryButton.doClick();

 String expectedOutput = "// INVERSE QUERY //\n" +
 "admin-c: example.com\n" +
 "tech-c: example.com\n" +
 "zone-c: example.com\n";

 System.setIn(new ByteArrayInputStream(expectedOutput.getBytes()));
 WhoISOutput capturedOutput = output;
 WhoISOutput expectedOutputObj = new WhoISOutput();
 whoIS.query(expectedOutputObj);
 assertEquals(expectedOutputObj.getOutput(), capturedOutput.getOutput());
 }

 @Test
 public void testMiscQueries() {
 WhoIS.setDefaults(); // Reset to defaults
 whoIS.inverseQueries.setSelectedIndex(0);
 whoIS.miscList.setSelectedValue("inetnum");
 String query = "192.168.0.1";
 whoIS.queryTextField.setText(query);
 whoIS.queryButton.doClick();

 String expectedOutput = "inetnum: 192.168.0.1\n";

 System.setIn(new ByteArrayInputStream(expectedOutput.getBytes()));
 WhoISOutput capturedOutput = output;
 WhoISOutput expectedOutputObj = new WhoISOutput();
 whoIS.query(expectedOutputObj);
 assertEquals(expectedOutputObj.getOutput(), capturedOutput.getOutput());
 }
}