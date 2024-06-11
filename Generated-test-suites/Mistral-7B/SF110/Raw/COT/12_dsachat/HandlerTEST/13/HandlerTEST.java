package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.server.Entry;
import dsachat.server.Handler;
import dsachat.server.Hero;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connectionEndpoint;
 private Vector<Entry> clients;

 @Before
 public void setUp() {
 handler = new Handler();
 connectionEndpoint = new ConnectionEndpoint(12345);
 clients = new Vector<Entry>();
 handler.addConnection(connectionEndpoint);
 }

 @After
 public void tearDown() {
 handler.removeConnection(connectionEndpoint);
 }

 @Test
 public void testAddConnection() {
 assertEquals(1, handler.clients.size());
 }

 @Test
 public void testHandleInputString() {
 String msg = "/wuser Hello";
 handler.handleInput(new InputEvent(new StringData(msg), connectionEndpoint));
 verify(clients).size(); // Verify the size of the clients vector has increased
 }

 @Test(expected = IOException.class)
 public void testHandleInputStringWithIOException() {
 String msg = "/getheros";
 handler.handleInput(new InputEvent(new StringData(msg), connectionEndpoint));
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream));
 handler.handleInput(new InputEvent(new StringData("/getheros"), connectionEndpoint));
 String actualOutput = outputStream.toString();
 assertEquals("command /getheros not found", actualOutput);
 System.setOut(System.out);
}

 @Test
 public void testHandleInputChallenge() {
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(new ChallengeData(challenge), connectionEndpoint));
}

 @Test(expected = IOException.class)
 public void testHandleInputChallengeWithIOException() {
 Challenge challenge = new Challenge();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream));
 handler.handleInput(new InputEvent(new ChallengeData(challenge), connectionEndpoint));
 String actualOutput = outputStream.toString();
 assertEquals("", actualOutput);
 System.setOut(System.out);

 try {
 connectionEndpoint.send(new StringData(""));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 handler.handleInput(new InputEvent(new ChallengeData(challenge), connectionEndpoint));
 String send = challenge.roll();
 String expectedOutput = handler.getName(connectionEndpoint) + ": " + send;
 ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream2));
 handler.handleInput(new InputEvent(new StringData(send), connectionEndpoint));
 String actualOutput2 = outputStream2.toString();
 assertEquals(expectedOutput, actualOutput2);
 System.setOut(System.out);
}

 @Test
 public void testHandleInputChallReq() {
 ConnectionEndpoint targetConnectionEndpoint = new ConnectionEndpoint(67890);
 ChallReq challReq = new ChallReq("user1", (short) 1, ChallengeType.ROLL_DICE, (short) 6, (short) 3);
 handler.handleInput(new InputEvent(new ChallReqData(challReq), connectionEndpoint));
 handler.handleInput(new InputEvent(new ChallReqData(challReq), targetConnectionEndpoint));
}

 @Test
 public void testHandleInputHero() {
 ConnectionEndpoint targetConnectionEndpoint = new ConnectionEndpoint(67890);
 Hero hero = new Hero("testHero", 10, 10, 10);
 handler.handleInput(new InputEvent(new HeroData(hero), connectionEndpoint));
 assertEquals("testHero", handler.getName(targetConnectionEndpoint));
 handler.handleInput(new InputEvent(new HeroData(hero), targetConnectionEndpoint));
}

 @Test
 public void testIsGmConnected() {
 assertFalse(handler.isGmConnected());
 ConnectionEndpoint gmConnectionEndpoint = new ConnectionEndpoint(21000);
 handler.addConnection(gmConnectionEndpoint);
 assertTrue(handler.isGmConnected());
}

 @Test
 public void testSendAllSystemMsg() {
 String msg = "System message";
 handler.sendAllSystemMsg(msg);
 for (int i = 0; i < handler.clients.size(); ++i) {
 Entry entry = handler.clients.elementAt(i);
 assertNotNull(entry.ce.getInputStream());
 ByteArrayOutputStream inputStreamOutput = new ByteArrayOutputStream();
 entry.ce.setInputStream(inputStreamOutput);
 String receivedMsg = readLineFromInputStream(entry.ce.getInputStream());
 assertEquals(msg, receivedMsg);
 }
}

 private String readLineFromInputStream(InputStream inputStream) {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[1024];
 int bytesRead = 0;
 try {
 while ((bytesRead = inputStream.read(buffer)) != -1) {
 outputStream.write(buffer, 0, bytesRead);
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 return outputStream.toString();
}

}