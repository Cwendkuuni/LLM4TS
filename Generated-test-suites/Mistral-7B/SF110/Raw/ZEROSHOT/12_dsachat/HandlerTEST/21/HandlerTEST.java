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

import dsachat.server.*;
import dsachat.share.hero.*;
import org.mockito.Mockito;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnectionEndpoint;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStream;
 private PrintStream printStream;

 @Before
 public void setUp() {
 handler = new Handler();
 mockConnectionEndpoint = Mockito.mock(ConnectionEndpoint.class);
 outputStream = new ByteArrayOutputStream();
 inputStream = new ByteArrayInputStream(new byte[0]);
 printStream = new PrintStream(outputStream);
 System.setOut(printStream);
 }

 @After
 public void tearDown() {
 handler = null;
 mockConnectionEndpoint = null;
 outputStream = null;
 inputStream = null;
 printStream = null;
 System.setOut(System.out);
 }

 @Test
 public void testAddConnection() {
 handler.addConnection(mockConnectionEndpoint);
 assertTrue(handler.clients.size() == 1);
 }

 @Test(expected = Exception.class)
 public void testAddConnectionException() {
 handler.addConnection(mockConnectionEndpoint);
 handler.addConnection(mockConnectionEndpoint);
 handler.addConnection(mockConnectionEndpoint);
}

 @Test
 public void testRemoveConnection() {
 handler.addConnection(mockConnectionEndpoint);
 handler.removeConnection(mockConnectionEndpoint);
 assertTrue(handler.clients.size() == 0);
}

 @Test
 public void testHandleInputString() {
 String message = "/whisper test test";
 handler.handleInput(new InputEvent(mockConnectionEndpoint, new String(message.getBytes())));
 String expectedOutput = "test whispers: test";
 String output = outputStream.toString();
 assertEquals(expectedOutput, output);
}

 @Test
 public void testHandleInputChallenge() {
 Challenge challenge = new Challenge(3, 6);
 handler.handleInput(new InputEvent(mockConnectionEndpoint, challenge));
 String expectedOutput = "3(d6)+0: ";
 String output = outputStream.toString();
 assertEquals(expectedOutput, output);
}

 @Test
 public void testHandleInputChallReq() {
 ChallReq challReq = new ChallReq("test", 3, 6);
 handler.handleInput(new InputEvent(mockConnectionEndpoint, challReq));
 String expectedOutput = "test rolled 3(d6)+0: ";
 String output = outputStream.toString();
 assertEquals(expectedOutput, output);
}

 @Test
 public void testHandleInputHero() {
 Hero hero = new Hero("test", new String[]{"STR", "INT", "AGI", "CON", "DEX", "WIS", "CHA"}, new int[]{10, 10, 10, 10, 10, 10, 10});
 handler.handleInput(new InputEvent(mockConnectionEndpoint, hero));
 String expectedOutput = "* test: ";
 String output = outputStream.toString();
 assertEquals(expectedOutput, output);
}

 @Test
 public void testSendAllStr() throws IOException {
 String message = "test message";
 handler.clients.add(new Entry(mockConnectionEndpoint, "test"));
 handler.sendAllStr(mockConnectionEndpoint, message);
 String expectedOutput = "test: test message";
 String output = outputStream.toString();
 assertEquals(expectedOutput, output);
}

 @Test
 public void testSendAllObj() throws IOException, ClassNotFoundException {
 Object obj = new Vector<Hero>();
 handler.clients.add(new Entry(mockConnectionEndpoint, "test"));
 ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream2);
 objectOutputStream.writeObject(obj);
 objectOutputStream.close();
 handler.sendAllObj(mockConnectionEndpoint, outputStream2.toByteArray());
 String expectedOutput = "[Ldsachat.share.hero.Hero;@";
 String output = outputStream.toString();
 assertEquals(expectedOutput, output);
}

 @Test
 public void testSendToOneStr() throws IOException {
 String to = "test2";
 String message = "test message";
 handler.clients.add(new Entry(mockConnectionEndpoint, "test"));
 handler.clients.add(new Entry(new ConnectionEndpoint(1234), to));
 handler.sendToOneStr(mockConnectionEndpoint, to, message);
 String expectedOutput = "test: test message";
 String output = outputStream.toString();
 assertEquals(expectedOutput, output);
}

 @Test
 public void testSendAllObjAndSendToOneStr() throws IOException, ClassNotFoundException {
 Object obj = new Vector<Hero>();
 handler.clients.add(new Entry(mockConnectionEndpoint, "test"));
 handler.clients.add(new Entry(new ConnectionEndpoint(1234), "test2"));
 ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream2);
 objectOutputStream.writeObject(obj);
 objectOutputStream.close();
 handler.handleInput(new InputEvent(mockConnectionEndpoint, new String("/sendall")));
 handler.sendToOneStr(mockConnectionEndpoint, "test2", "test message");
 String expectedOutput = "test: [Ldsachat.share.hero.Hero;@\ntest2: test message";
 String output = outputStream.toString();
 assertEquals(expectedOutput, output);
}

 @Test
 public void testIsGmConnected() {
 handler.addConnection(mockConnectionEndpoint);
 handler.clients.get(0).name = "Gamemaster";
 assertTrue(handler.isGmConnected());
 handler.clients.get(0).name = "";
 assertFalse(handler.isGmConnected());
}
}