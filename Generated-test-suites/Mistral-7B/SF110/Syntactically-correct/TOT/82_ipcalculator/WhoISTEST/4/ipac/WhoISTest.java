package ipac.test;

import static org.junit.Assert.*;
import ipac.WhoIS;

import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class WhoISTest {

 private WhoIS whois;
 private ByteArrayOutputStream outputStream;
 private InputStream inputStream;
 private Socket socket;

 @Before
 public void setUp() {
 whois = new WhoIS();
 outputStream = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream));
 try {
 socket = new Socket("localhost", 0); // get a free port
 } catch (IOException e) {
 e.printStackTrace();
 }
 whois.queryButton.addActionListener((ActionEvent e) -> whois.startQuery());
 }

 @Test
 public void testQuerySuccess() {
 whois.queryTextField.setText("example.com");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "VRSN: 2 03/03/15 13:37:12\nORG: ARIN\nADMIN-C: ARIN-ADMIN\nTECH-C: ARIN-TECH\nNAME: Example, Inc.\nNETNAME: EXAMPLE\nNETTYPE: Direct Assignment\nNETNUM: 192.0.2.0\nNETNAME: EXAMPLE-inet6num\nNETTYPE: Direct Assignment\nNETNUM: 2001:0db8:85a3:0:0:8a2e:370:7334\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalStateException.class)
 public void testQueryEmptyTextField() {
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 }

 @Test(expected = IOException.class)
 public void testQueryFailedConnection() throws IOException {
 socket.close();
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 }

 @Test
 public void testQueryInvalidQuery() {
 whois.queryTextField.setText("invalidquery");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedError = "Error: WhoIS server returned an error\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertTrue(actualOutput.startsWith(expectedError));
 }

 @Test
 public void testQuerySlashCommand() {
 whois.queryTextField.setText("/misc -T role person");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "ROLE: RFC1918\nPERSON: Person Name\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testQueryInverseQueries() {
 whois.queryTextField.setText("admin-c");
 whois.inverseQueries.setSelectedIndex(1);
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "ORG: ARIN\nADMIN-C: ARIN-ADMIN\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testQueryLowerCaseOptions() {
 whois.queryTextField.setText("192.0.2.1");
 whois.lowercaseL.setSelected(true);
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "NETNUM: 192.0.2.0\nNETNUM: 192.0.2.1\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testQueryUpperCaseOptions() {
 whois.queryTextField.setText("192.0.2.1");
 whois.uppercaseL.setSelected(true);
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "NETNUM: 192.0.2.0\nNETNUM: 192.0.2.1\nNETNUM: 192.0.2.1\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testQueryMoreSpecificOptions() {
 whois.queryTextField.setText("192.0.2.1");
 whois.lowercaseM.setSelected(true);
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "NETNUM: 192.0.2.1\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testQueryMoreGeneralOptions() {
 whois.queryTextField.setText("192.0.2.0");
 whois.uppercaseM.setSelected(true);
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "NETNUM: 192.0.2.0\nNETNUM: 192.0.2.1\nNETNUM: 192.0.2.2\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testQueryExactMatchOnly() {
 whois.queryTextField.setText("192.0.2.1");
 whois.lowercaseX.setSelected(true);
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "NETNUM: 192.0.2.1\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testQueryBooleanOption() {
 whois.queryTextField.setText("example.com");
 whois.lowercaseD.setSelected(true);
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "VRSN: 2 03/03/15 13:37:12\nORG: ARIN\nADMIN-C: ARIN-ADMIN\nTECH-C: ARIN-TECH\nNAME: Example, Inc.\nNETNAME: EXAMPLE\nNETTYPE: Direct Assignment\nNETNUM: 192.0.2.0\nNETNAME: EXAMPLE-inet6num\nNETTYPE: Direct Assignment\nNETNUM: 2001:0db8:85a3:0:0:8a2e:370:7334\nINETNUM: 192.0.2.1\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testQueryMultipleObjectTypes() {
 whois.queryTextField.setText("example.com");
 whois.inverseQueries.setSelectedIndex(0);
 whois.miscList.setSelectedValue("role");
 whois.miscList.setSelectedValue("person");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "VRSN: 2 03/03/15 13:37:12\nORG: ARIN\nROLE: RFC1918\nPERSON: Person Name\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testQueryAllObjectTypes() {
 whois.queryTextField.setText("example.com");
 whois.inverseQueries.setSelectedIndex(0);
 whois.miscList.setSelectedValue("All");
 whois.actionPerformed(new ActionEvent(whois.queryButton, 0, 0));
 String expectedOutput = "VRSN: 2 03/03/15 13:37:12\nORG: ARIN\nROLE: RFC1918\nPERSON: Person Name\nAUT-NUM: Autonomous System\nDOMAIN: example.com\nFILTER-SET: Filter Set\nINET-RTR: Internet Router\nINET6NUM: 2001:0db8:85a3:0:0:8a2e:370:7334\nKEY-CERT: Key Certification\nMNTNER: MNT Entry\nPEERING-SET: Peering Set\nPERSON: Person Name\nROLE: Role\nROUTE: Route\nROUTE-SET: Route Set\nRTR-SET: Router Set\n";
 String actualOutput = new String(outputStream.toByteArray());
 assertEquals(expectedOutput, actualOutput);
 }

 @After
 public void tearDown() {
 whois.actionPerformed(new ActionEvent(whois.resetButton, 0, 0));
 System.setOut(System.out);
 try {
 socket.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

}