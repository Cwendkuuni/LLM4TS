package ipac.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoITest {

 private WhoIS whois;
 private Socket socket;
 private OutputStream outputStream;
 private InputStream inputStream;

 @Before
 public void setup() {
 whois = new WhoIS();
 socket = mock(Socket.class);
 outputStream = mock(OutputStream.class);
 when(socket.getOutputStream()).thenReturn(outputStream);
 inputStream = new ByteArrayInputStream(new byte[0]);
 when(socket.getInputStream()).thenReturn(inputStream);
 }

 @Test
 public void testQueryWithValidInput() {
 String query = "example.com";
 whois.startQuery();
 whois.query(query);
 verify(outputStream).println(query);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueryWithEmptyInput() {
 whois.startQuery();
 whois.query("");
 }

 @Test(expected = UnknownHostException.class)
 public void testQueryWithUnknownHost() {
 whois.WHOIS_ADDRESS = "unknown.host";
 whois.startQuery();
 whois.query("example.com");
 verify(socket).connect(anyString(), anyInt());
 verify(socket).close();
 }

 @Test
 public void testQueryWithError() {
 when(socket.isClosed()).thenReturn(true);
 whois.startQuery();
 whois.query("example.com");
 verify(socket).isClosed();
 }

 @Test
 public void testReset() {
 whois.reset();
 assertEquals("", whois.queryTextField.getText());
 }

 @Test
 public void testInverseQueries() {
 whois.inverseQueries.setSelectedIndex(1);
 String query = "example.com";
 whois.startQuery();
 whois.query(query);
 String expectedOutput = "admin-c,tech-c,zone-c " + query + "\n";
 verify(outputStream).println(query);
 verify(outputStream).println(expectedOutput);
 }

 @Test(expected = IOException.class)
 public void testQueryWithIOError() throws IOException {
 ByteArrayOutputStream outputStreamSpy = spy(new ByteArrayOutputStream());
 when(outputStream).thenReturn(outputStreamSpy);
 whois.startQuery();
 whois.query("example.com");
 outputStreamSpy.writeTo(System.out);
 }

 @Test
 public void testQueryHints() {
 whois.queryHintsPanel();
 assertTrue(whois.queryHintsPanel.getComponentCount() > 0);
 }

}