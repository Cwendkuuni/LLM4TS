package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setup() {
 whois = new WhoIS();
 }

 @Test
 public void testQueryPanel() {
 // Test queryTextField
 whois.queryTextField.setText("example.com");
 assertEquals("example.com", whois.queryTextField.getText());

 // Test queryButton
 ActionEvent queryButtonEvent = new ActionEvent(whois.queryButton, 0, "QUERY");
 whois.actionPerformed(queryButtonEvent);
 assertTrue(whois.startQueryCalled);

 // Test resetButton
 ActionEvent resetButtonEvent = new ActionEvent(whois.resetButton, 0, "RESET");
 whois.actionPerformed(resetButtonEvent);
 assertEquals("", whois.queryTextField.getText());
 }

 @Test
 public void testAdditionalQueriesPanel() {
 // Test IPAddressLookups
 assertFalse(whois.lowercaseL.isSelected());
 assertFalse(whois.uppercaseL.isSelected());
 assertFalse(whois.lowercaseM.isSelected());
 assertFalse(whois.uppercaseM.isSelected());
 assertFalse(whois.lowercaseX.isSelected());
 assertFalse(whois.lowercaseD.isSelected());

 whois.lowercaseL.doClick();
 assertTrue(whois.lowercaseL.isSelected());
 assertFalse(whois.uppercaseL.isSelected());
 assertFalse(whois.lowercaseM.isSelected());
 assertFalse(whois.uppercaseM.isSelected());
 assertFalse(whois.lowercaseX.isSelected());
 assertFalse(whois.lowercaseD.isSelected());

 whois.uppercaseL.doClick();
 assertFalse(whois.lowercaseL.isSelected());
 assertTrue(whois.uppercaseL.isSelected());
 assertFalse(whois.lowercaseM.isSelected());
 assertFalse(whois.uppercaseM.isSelected());
 assertFalse(whois.lowercaseX.isSelected());
 assertFalse(whois.lowercaseD.isSelected());

 whois.lowercaseM.doClick();
 assertFalse(whois.lowercaseL.isSelected());
 assertFalse(whois.uppercaseL.isSelected());
 assertTrue(whois.lowercaseM.isSelected());
 assertFalse(whois.uppercaseM.isSelected());
 assertFalse(whois.lowercaseX.isSelected());
 assertFalse(whois.lowercaseD.isSelected());

 whois.uppercaseM.doClick();
 assertFalse(whois.lowercaseL.isSelected());
 assertFalse(whois.uppercaseL.isSelected());
 assertFalse(whois.lowercaseM.isSelected());
 assertTrue(whois.uppercaseM.isSelected());
 assertFalse(whois.lowercaseX.isSelected());
 assertFalse(whois.lowercaseD.isSelected());

 whois.lowercaseX.doClick();
 assertFalse(whois.lowercaseL.isSelected());
 assertFalse(whois.uppercaseL.isSelected());
 assertFalse(whois.lowercaseM.isSelected());
 assertFalse(whois.uppercaseM.isSelected());
 assertTrue(whois.lowercaseX.isSelected());
 assertFalse(whois.lowercaseD.isSelected());

 whois.lowercaseD.doClick();
 assertFalse(whois.lowercaseL.isSelected());
 assertFalse(whois.uppercaseL.isSelected());
 assertFalse(whois.lowercaseM.isSelected());
 assertFalse(whois.uppercaseM.isSelected());
 assertFalse(whois.lowercaseX.isSelected());
 assertTrue(whois.lowercaseD.isSelected());

 // Test inverseQueries
 whois.inverseQueries.setSelectedIndex(0);
 assertEquals("None", whois.inverseQueries.getSelectedItem().toString());

 whois.inverseQueries.setSelectedIndex(1);
 assertEquals("admin-c", whois.inverseQueries.getSelectedItem().toString());

 // Test MiscellaneousQueries
 Object[] miscQueries = {"All", "as-block", "as-set", "aut-num", "domain", "filter-set", "inet-rtr", "inet6num", "inetnum", "irt", "key-cert", "mntner", "peering-set", "person", "role", "route", "route-set", "rtr-set"};
 whois.miscList.setListData(miscQueries);
 whois.miscList.setSelectedIndex(0);
 assertEquals("All", whois.miscList.getSelectedValue().toString());

 whois.miscList.setSelectedValue("as-block", false);
 assertFalse(whois.miscList.isSelectionEmpty());
 assertEquals("as-block", whois.miscList.getSelectedValue().toString());
 }

 @Test
 public void testQueryHintsPanel() {
 // Test queryHintsText1
 assertNotNull(whois.queryHintsText1);

 // Test queryHintsText2
 assertNotNull(whois.queryHintsText2);
 assertEquals("Include \"AS\" in front of an AS number.", whois.queryHintsText2.getText());

 // Test queryHintsText3
 assertNotNull(whois.queryHintsText3);
 assertEquals("Example: AS4808", whois.queryHintsText3.getText());

 // Test queryHintsText4
 assertNotNull(whois.queryHintsText4);
 assertEquals("* Include \"AS\" in front of an AS number.", whois.queryHintsText4.getText().substring(0, 32));
 assertEquals("Example: -t inetnum", whois.queryHintsText4.getText().substring(32));

 // Test queryHintsText5
 assertNotNull(whois.queryHintsText5);
 assertEquals("Example: -t inetnum", whois.queryHintsText5.getText());
 }

 @Test
 public void testCreatePopupMenu() {
 // Test cut
 ActionEvent cutEvent = new ActionEvent(whois.popupMenu.getComponent(0), 0, "CUT");
 whois.popupMenu.getComponent(0).addActionListener(e -> whois.queryTextField.cut());
 whois.popupMenu.show(whois, 10, 10);
 whois.popupMenu.getComponent(0).dispatchEvent(cutEvent);
 assertFalse(whois.queryTextField.getText().isEmpty());

 // Test copy
 ActionEvent copyEvent = new ActionEvent(whois.popupMenu.getComponent(1), 0, "COPY");
 whois.popupMenu.getComponent(1).addActionListener(e -> whois.queryTextField.copy());
 whois.popupMenu.show(whois, 10, 10);
 whois.popupMenu.getComponent(1).dispatchEvent(copyEvent);
 assertTrue(whois.queryTextField.getText().contains("example.com"));

 // Test paste
 ActionEvent pasteEvent = new ActionEvent(whois.popupMenu.getComponent(2), 0, "PASTE");
 whois.popupMenu.getComponent(2).addActionListener(e -> whois.queryTextField.paste());
 whois.popupMenu.show(whois, 10, 10);
 whois.popupMenu.getComponent(2).dispatchEvent(pasteEvent);
 assertTrue(whois.queryTextField.getText().contains("example.comexample.com"));

 // Test selectAll
 ActionEvent selectAllEvent = new ActionEvent(whois.popupMenu.getComponent(3), 0, "SELECT ALL");
 whois.popupMenu.getComponent(3).addActionListener(e -> whois.queryTextField.selectAll());
 whois.popupMenu.show(whois, 10, 10);
 whois.popupMenu.getComponent(3).dispatchEvent(selectAllEvent);
 assertEquals(whois.queryTextField.getText().length(), whois.queryTextField.getSelectionEnd());
 }

 @Test
 public void testStartQuery() throws IOException {
 String expectedOutput = "WhoIs Server:\n" +
 "APNIC Whois Server\n" +
 "Domain ID: example.com\n" +
 "Origin: APNIC\n" +
 "Address: 192.0.2.1\n" +
 "Name Server: ns1.example.net\n" +
 "Name Server: ns2.example.net\n" +
 "Status: client transfer ok\n" +
 "Updated Date: 2012-01-01T00:00:00Z\n" +
 "Creation Date: 1995-01-01T00:00:00Z\n" +
 "Expiry Date: 2022-01-01T00:00:00Z\n" +
 "";

 ByteArrayOutputStream out = new ByteArrayOutputStream();
 InputStream in = new ByteArrayInputStream(expectedOutput.getBytes());
 Socket socket = new Socket("localhost", 0);
 socket.getOutputStream().write(whois.getQuery().getBytes());
 socket.getOutputStream().flush();
 socket.getInputStream().transferTo(out);
 socket.close();

 String actualOutput = out.toString();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = UnknownHostException.class)
 public void testUnknownWhoisHost() {
 whois.query("unknown.host");
 }

 @Test(expected = IOException.class)
 public void testWhoisServerCouldNotBeContacted() {
 whois.query("whois.server:80");
 }
}