package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;
import dsachat.server.Handler;
import dsachat.server.HandlerTest;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint ce1;
 private ConnectionEndpoint ce2;
 private Hero hero1;
 private Hero hero2;
 private Vector<Entry> clients;

 @Before
 public void setUp() {
 handler = new Handler();
 ce1 = new ConnectionEndpoint(1234);
 ce2 = new ConnectionEndpoint(5678);
 hero1 = new Hero("Hero1");
 hero2 = new Hero("Hero2");
 clients = handler.getClients();
 handler.addConnection(ce1);
 handler.addConnection(ce2);
 }

 @After
 public void tearDown() {
 handler.removeConnection(ce1);
 handler.removeConnection(ce2);
 handler = null;
 ce1 = null;
 ce2 = null;
 hero1 = null;
 hero2 = null;
 clients = null;
 }

 @Test
 public void testAddConnection() {
 Entry entry1 = clients.elementAt(0);
 assertEquals(ce1, entry1.ce);
 assertEquals("", entry1.name);

 handler.addConnection(ce1); // add connection again
 Entry entry2 = clients.elementAt(1);
 assertEquals(ce1, entry2.ce);
 assertEquals("", entry2.name);

 handler.removeConnection(ce1); // remove connection
 assertEquals(1, clients.size());
 }

 @Test
 public void testRemoveConnection() {
 clients.add(new Entry());
 Entry entry = clients.elementAt(2);
 handler.removeConnection(ce2); // remove non-existing connection
 assertEquals(2, clients.size());

 handler.addConnection(ce2); // add connection
 handler.removeConnection(ce2); // remove connection
 assertEquals(2, clients.size());

 handler.addConnection(ce1); // add connection
 handler.removeConnection(ce1); // remove connection
 assertEquals(1, clients.size());

 handler.addConnection(ce1); // add connection
 String name = "TestName";
 handler.setName(ce1, name);
 handler.removeConnection(ce1); // remove connection
 assertEquals(1, clients.size());
 assertEquals("", clients.elementAt(0).name);
 }

 @Test
 public void testHandleInput_String() throws IOException {
 String msg = "/hello world";
 handler.handleInput(new InputEvent(ce1, new String("" + msg)));
 String expected = "*TestName: hello world";
 assertTrue(msg.startsWith("/") || msg.startsWith("\\/") || msg.startsWith("//"));
 assertEquals(expected, handler.getName(ce1) + ": " + msg.substring(1));
 }

 @Test
 public void testHandleInput_Challenge() throws IOException {
 Challenge challenge = new Challenge(4, 6);
 MessageOutputStream output = new MessageOutputStream();
 ObjectOutputStream objectOutput = new ObjectOutputStream(output);
 objectOutput.writeObject(challenge);
 handler.handleInput(new InputEvent(ce1, new ByteArrayInputStream(output.toByteArray())));
 assertEquals(6, challenge.getRoll()[0]);
 assertEquals(4, challenge.getRoll()[1]);
 assertEquals(10, challenge.getRoll()[2]);
 }

 @Test
 public void testHandleInput_ChallReq() throws IOException {
 ChallengeReq challengeReq = new ChallengeReq("ToName", 4, 6);
 MessageOutputStream output = new MessageOutputStream();
 ObjectOutputStream objectOutput = new ObjectOutputStream(output);
 objectOutput.writeObject(challengeReq);
 handler.handleInput(new InputEvent(ce2, new ByteArrayInputStream(output.toByteArray())));
 assertEquals(4, challengeReq.getSides());
 assertEquals(6, challengeReq.getTargetRoll());
 assertEquals("ToName", challengeReq.getTo());
 }

 @Test
 public void testHandleInput_Hero() throws IOException {
 MessageOutputStream output = new MessageOutputStream();
 ObjectOutputStream objectOutput = new ObjectOutputStream(output);
 objectOutput.writeObject(hero1);
 handler.handleInput(new InputEvent(ce1, new ByteArrayInputStream(output.toByteArray())));
 assertEquals(hero1, clients.elementAt(0).hero);
 assertEquals(hero1.getName(), clients.elementAt(0).name);
 }

 @Test
 public void testHandleInput_UserEvent() throws IOException {
 UserEvent userEvent = new UserEvent(true, "Gamemaster");
 MessageOutputStream output = new MessageOutputStream();
 ObjectOutputStream objectOutput = new ObjectOutputStream(output);
 objectOutput.writeObject(userEvent);
 handler.handleInput(new InputEvent(ce1, new ByteArrayInputStream(output.toByteArray())));
 assertEquals("Gamemaster", clients.elementAt(0).name);
 }

 @Test
 public void testSendAllObj() throws IOException {
 Vector<Object> vector = new Vector<Object>();
 vector.add(new String("TestString"));
 handler.sendAllObj(vector);
 for (int i = 0; i < clients.size(); ++i) {
 assertEquals("TestString", clients.elementAt(i).ce.receive().toString());
 }
 }

 @Test
 public void testSendAllStr() throws IOException {
 String str = "TestString";
 handler.sendAllStr(ce1, str);
 for (int i = 0; i < clients.size(); ++i) {
 assertEquals(str, clients.elementAt(i).ce.receive().toString());
 }
 }

 @Test
 public void testSendToOneStr() throws IOException {
 String toName = "ToName";
 String str = "TestString";
 handler.sendToOneStr(ce1, toName, str);
 assertEquals(str, clients.elementAt(clients.size()-1).ce.receive().toString());
 }

}