package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private Vector<Entry> clients;
 private Socket socket;
 private PrintWriter out;
 private ByteArrayOutputStream inStream;
 private ObjectInputStream in;

 @Before
 public void setUp() {
 handler = new Handler();
 clients = handler.getClients();
 }

 @After
 public void tearDown() throws Exception {
 handler = null;
 clients = null;
 socket.close();
 }

 @Test
 public void testAddConnection() {
 ConnectionEndpoint cl = new ConnectionEndpoint(0, "localhost", 1234);
 handler.addConnection(cl);
 assertEquals(1, clients.size());
 }

 @Test
 public void testRemoveConnection() {
 ConnectionEndpoint cl = new ConnectionEndpoint(0, "localhost", 1234);
 handler.addConnection(cl);
 handler.removeConnection(cl);
 assertEquals(0, clients.size());
 }

 @Test
 public void testHandleInputStringData() throws IOException {
 socket = new Socket("localhost", 0);
 out = new PrintWriter(socket.getOutputStream(), true);
 inStream = new ByteArrayOutputStream();
 in = new ObjectInputStream(new ByteArrayInputStream(inStream.toByteArray()));
 handler.addConnection(new ConnectionEndpoint(socket.getPort(), "localhost", socket.getPort()));
 out.println("hello world");
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket.getOutputStream()), "hello world"));
 assertEquals("localhost:5851: hello world", in.readObject().toString());
 }

 @Test
 public void testHandleInputChallengeData() throws IOException, ClassNotFoundException {
 socket = new Socket("localhost", 0);
 out = new PrintWriter(socket.getOutputStream(), true);
 inStream = new ByteArrayOutputStream();
 in = new ObjectInputStream(new ByteArrayInputStream(inStream.toByteArray()));
 Challenge challenge = new Challenge(6, 2);
 handler.addConnection(new ConnectionEndpoint(socket.getPort(), "localhost", socket.getPort()));
 out.println("challenge 3d6+4");
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket.getOutputStream()), "challenge 3d6+4"));
 assertEquals("localhost:5851: rolled 3d6+4 --> 7 10", in.readObject().toString());
 }

 @Test
 public void testHandleInputChallengeDataToSpecificUser() throws IOException, ClassNotFoundException {
 socket = new Socket("localhost", 0);
 socket2 = new Socket("localhost", 0);
 out = new PrintWriter(socket.getOutputStream(), true);
 out2 = new PrintWriter(socket2.getOutputStream(), true);
 inStream = new ByteArrayOutputStream();
 in = new ObjectInputStream(new ByteArrayInputStream(inStream.toByteArray()));
 inStream2 = new ByteArrayOutputStream();
 in2 = new ObjectInputStream(new ByteArrayInputStream(inStream2.toByteArray()));
 handler.addConnection(new ConnectionEndpoint(socket.getPort(), "localhost", socket.getPort()));
 handler.addConnection(new ConnectionEndpoint(socket2.getPort(), "localhost", socket2.getPort()));
 out.println("challenge 3d6 to user1");
 out2.println("hello world");
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket.getOutputStream()), "challenge 3d6 to user1"));
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket2.getOutputStream()), "hello world"));
 assertEquals("user1: 5 6", in.readObject().toString());
 assertEquals("", in2.readObject().toString());
 }

 @Test
 public void testHandleInputChallengeDataToAllUsers() throws IOException, ClassNotFoundException {
 socket = new Socket("localhost", 0);
 socket2 = new Socket("localhost", 0);
 out = new PrintWriter(socket.getOutputStream(), true);
 out2 = new PrintWriter(socket2.getOutputStream(), true);
 inStream = new ByteArrayOutputStream();
 in = new ObjectInputStream(new ByteArrayInputStream(inStream.toByteArray()));
 inStream2 = new ByteArrayOutputStream();
 in2 = new ObjectInputStream(new ByteArrayInputStream(inStream2.toByteArray()));handler.addConnection(new ConnectionEndpoint(socket.getPort(), "localhost", socket.getPort()));
 handler.addConnection(new ConnectionEndpoint(socket2.getPort(), "localhost", socket2.getPort()));
 out.println("challenge 3d6 to all");
 out2.println("hello world");
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket.getOutputStream()), "challenge 3d6 to all"));
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket2.getOutputStream()), "hello world"));
 assertEquals("localhost:5851: rolled 3d6 --> 3 1 5", in.readObject().toString());
 assertEquals("", in2.readObject().toString());
 }

 @Test
 public void testHandleInputChallReqData() throws IOException, ClassNotFoundException {
 socket = new Socket("localhost", 0);
 socket2 = new Socket("localhost", 0);
 out = new PrintWriter(socket.getOutputStream(), true);
 out2 = new PrintWriter(socket.getOutputStream(), true);
 inStream = new ByteArrayOutputStream();
 in = new ObjectInputStream(new ByteArrayInputStream(inStream.toByteArray()));
 inStream2 = new ByteArrayOutputStream();
 in2 = new ObjectInputStream(new ByteArrayInputStream(inStream2.toByteArray()));
 handler.addConnection(new ConnectionEndpoint(socket.getPort(), "localhost", socket.getPort()));
 handler.addConnection(new ConnectionEndpoint(socket2.getPort(), "localhost", socket2.getPort()));
 out.println("challenge 3d6 to user2");
 out2.println("challengeResponse 2d6 result: 3 4");
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket.getOutputStream()), "challenge 3d6 to user2"));
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket2.getOutputStream()), "challengeResponse 2d6 result: 3 4"));
 assertEquals("user2: 3 4", in.readObject().toString());
 }

 @Test
 public void testHandleInputHeroData() throws IOException {
 socket = new Socket("localhost", 0);
 out = new PrintWriter(socket.getOutputStream(), true);
 inStream = new ByteArrayOutputStream();
 in = new ObjectInputStream(new ByteArrayInputStream(inStream.toByteArray()));
 Hero hero = new Hero("Test Hero");
 handler.addConnection(new ConnectionEndpoint(socket.getPort(), "localhost", socket.getPort()));
 out.println("hero Test Hero");
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket.getOutputStream()), "hero Test Hero"));
 assertEquals("Test Hero", in.readObject().toString());
 }

 @Test
 public void testHandleInputUserEventData() throws IOException {
 socket = new Socket("localhost", 0);
 out = new PrintWriter(socket.getOutputStream(), true);
 inStream = new ByteArrayOutputStream();
 in = new ObjectInputStream(new ByteArrayInputStream(inStream.toByteArray()));
 handler.addConnection(new ConnectionEndpoint(socket.getPort(), "localhost", socket.getPort()));
 out.println("me I am the Gamemaster");
 handler.handleInput(new InputEvent(new ObjectOutputStream(socket.getOutputStream()), new UserEvent(true, "Gamemaster")));
 assertEquals("*Gamemaster: I am the Gamemaster", in.readObject().toString());
 }

 @Test
 public void testSendAllSystemMsg() {
 String msg = "This is a system message";
 handler.sendAllSystemMsg(msg);
 for (int i = 0; i < clients.size(); ++i) {
 assertEquals(msg, clients.get(i).ce.getMessageQueue()[0].getData().toString());
 }
 }
}