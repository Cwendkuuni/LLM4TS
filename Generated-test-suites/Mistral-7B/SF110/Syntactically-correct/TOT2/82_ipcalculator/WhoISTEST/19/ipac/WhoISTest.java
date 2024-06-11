import org.junit.Assert;
import org.junit.Test;
import ipac.WhoIS;

import java.io.IOException;
import java.net.UnknownHostException;

public class WhoISTest {

 private WhoIS whois;

 @SuppressWarnings("static-access")
 @Before
 public void setUp() {
 whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 }

 @Test
 public void testQueryButtonActionPerformed() {
 whois.queryButton.doClick();
 String expectedOutput = "WhoIS output here"; // Replace with expected output
 String actualOutput = whois.getWhoISOutput();
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownHostException() {
 whois.WHOIS_ADDRESS = "non-existent-host.com";
 whois.queryButton.doClick();
 }

 @Test(expected = IOException.class)
 public void testIOException() {
 whois.WHOIS_ADDRESS = "whois.example.com";
 whois.WHOIS_PORT = 0;
 whois.queryButton.doClick();
 }

 @Test
 public void testResetButtonActionPerformed() {
 whois.queryButton.doClick();
 whois.resetButton.doClick();
 Assert.assertEquals("", whois.queryTextField.getText());
 }

 @Test
 public void testInverseQueries() {
 whois.inverseQueries.setSelectedIndex(1);
 whois.queryTextField.setText("example.com");
 whois.queryButton.doClick();
 String expectedOutput = "Inverse queries output here"; // Replace with expected output
 String actualOutput = whois.getWhoISOutput();
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testMiscQueries() {
 WhoIS whois2 = new WhoIS();
 whois2.inverseQueries.setSelectedIndex(0);
 whois2.miscList.setSelectedValue("as-block");
 whois2.queryTextField.setText("AS12345");
 whois2.queryButton.doClick();
 String expectedOutput = "AS-Block output here"; // Replace with expected output
 String actualOutput = whois2.getWhoISOutput();
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEmptyQuery() {
 WhoIS whois3 = new WhoIS();
 whois3.queryTextField.setText("");
 whois3.queryButton.doClick();
 String expectedOutput = "Empty query output here"; // Replace with expected output
 String actualOutput = whois3.getWhoISOutput();
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @After
 public void tearDown() {
 whois = null;
 }

 private String getWhoISOutput() {
 String output = "";
 for (Component component : whois.getComponentTree().descendants()) {
 if (component instanceof JTextPane) {
 JTextPane textPane = (JTextPane) component;
 output += textPane.getText();
 }
 }
 return output;
 }
}