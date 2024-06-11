import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import ipac.WhoIS;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class WhoISTest {

 @BeforeEach
 void setUp() {
 WhoIS whois = new WhoIS();
 }

 @AfterEach
 void tearDown() {
 // Add any tear down code if necessary
 }

 @Test
 void testConstructor() {
 WhoIS whois = new WhoIS();
 assertNotNull(whois);
 assertEquals("APNIC", whois.WHOIS_NAME);
 assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
 assertEquals(43, whois.WHOIS_PORT);
 }

 @Test
 void testQueryPanelCreation() {
 WhoIS whois = new WhoIS();
 JPanel queryPanel = whois.queryPanel();
 assertNotNull(queryPanel);
 assertTrue(queryPanel.getComponentCount() > 0);
 }

 @Test
 void testQueryTextField() {
 WhoIS whois = new WhoIS();
 JTextField queryTextField = whois.queryTextField;
 assertNotNull(queryTextField);
 assertEquals(40, queryTextField.getColumns());
 }

 @Test
 void testQueryButton() {
 WhoIS whois = new WhoIS();
 JButton queryButton = whois.queryButton;
 assertNotNull(queryButton);
 assertInstanceOf(ActionListener.class, queryButton.getActionListeners()[0]);
 }

 @Test
 void testResetButton() {
 WhoIS whois = new WhoIS();
 JButton resetButton = whois.resetButton;
 assertNotNull(resetButton);
 assertInstanceOf(ActionListener.class, resetButton.getActionListeners()[0]);
 }

 @Test
 void testStartQueryNormalCase() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("example.com");
 whois.startQuery();
 // Add assertions for the expected output here
 }

 @Test
 void testErrorWhenNoQueryDetected() {
 WhoIS whois = new WhoIS();
 whois.startQuery();
 assertEquals("No Query Detected", whois.displayError());
 }

 @Test
 void testUnknownWhoisHost() {
 WhoIS whois = new WhoIS();
 whois.WHOIS_ADDRESS = "invalid.host";
 whois.startQuery();
 assertEquals("Unknown Whois host: invalid.host", whois.displayError());
 }

 @Test
 void testIOException() {
 WhoIS whois = new WhoIS();
 try {
 whois.query("example.com");
 fail("IOException expected");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 void testQueryWithInvalidQuery() {
 WhoIS whois = new WhoIS();
 whois.queryTextField.setText("invalid");
 whois.startQuery();
 assertEquals("Invalid query", whois.displayError());
 }

 @Test
 void testInverseQueries() {
 WhoIS whois = new WhoIS();
 whois.inverseQueries.setSelectedItem("admin-c");
 assertEquals("admin-c", whois.getQuery().split(" ")[1]);
 }

 @Test
 void testDLevelOptions() {
 WhoIS whois = new WhoIS();
 whois.lowercaseL.setSelected(true);
 assertEquals("-l example.com", whois.getQuery());
 whois.uppercaseL.setSelected(true);
 assertEquals("-L example.com", whois.getQuery());
 }

 @Test
 void testMLevelOptions() {
 WhoIS whois = new WhoIS();
 whois.lowercaseM.setSelected(true);
 assertEquals("-m example.com", whois.getQuery());
 whois.uppercaseM.setSelected(true);
 assertEquals("-M example.com", whois.getQuery());
 }

 @Test
 void testXLevelOption() {
 WhoIS whois = new WhoIS();
 whois.lowercaseX.setSelected(true);
 assertEquals("-x example.com", whois.getQuery());
 }

 @Test
 void testBooleanOption() {
 WhoIS whois = new WhoIS();
 whois.lowercaseD.setSelected(true);
 assertEquals("-d example.com", whois.getQuery());
 }

 @Test
 void testMiscQueries() {
 WhoIS whois = new WhoIS();
 whois.miscList.setSelectedValues(new Object[]{"as-block", "as-set"});
 assertEquals("-T as-block,as-set example.com", whois.getQuery());
 }

}