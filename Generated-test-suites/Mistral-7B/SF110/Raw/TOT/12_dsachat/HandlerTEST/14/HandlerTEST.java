package dsachat.server.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Handler.Entry;
import dsachat.server.InputEvent;
import dsachat.server.UserEvent;
import dsachat.share.Challenge;
import dsachat.share.ChallReq;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.Hero;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint mockConnection;
 private List<Entry> clients;

 @Before
 public void setup() {
 handler = new Handler();
 mockConnection = mock(ConnectionEndpoint.class);
 clients = new ArrayList<>();
 }

 @Test
 public void testAddConnection() {
 Entry entry = new Entry();
 handler.addConnection(mockConnection);
 clients.add(entry);
 assertEquals(1, clients.size());
 verify(mockConnection).send(any(UserEvent.class));
 }

 @Test
 public void testRemoveConnection() {
 Entry entry = new Entry();
 clients.add(entry);
 handler.removeConnection(mockConnection);
 assertEquals(0, clients.size());
 verify(mockConnection, never()).send(any(UserEvent.class));
 }

 @Test
 public void testHandleInput_StringMessage() throws IOException {
 String message = "testMessage";
 InputEvent inputEvent = new InputEvent(this, mockConnection, new ByteArrayInputStream(message.getBytes()));
 handler.handleInput(inputEvent);
 // Verify that message is sent to all clients
 for (Entry client : clients) {
 verify(client.ce).send(eq(new String("*" + handler.getName(client.ce) + ": " + message)));
 }
 }

 @Test
 public void testHandleInput_Challenge() throws IOException {
 Challenge challenge = mock(Challenge.class);
 when(challenge.roll()).thenReturn("testRoll");
 InputEvent inputEvent = new InputEvent(this, mockConnection, challenge);
 handler.handleInput(inputEvent);
 // Verify that message is sent to the target client
 String target = "testTarget";
 clients.get(0).name = target;
 verify(mockConnection).send(eq("testRoll"));
 verify(clients.get(0).ce).send(eq("testRoll"));
 // Verify that message is not sent to other clients
 for (int i = 1; i < clients.size(); i++) {
 verify(clients.get(i).ce).send(never());
 }
 }

 @Test
 public void testHandleInput_ChallReq() throws IOException {
 ChallReq challReq = new ChallReq("testTarget", "testMessage");
 InputEvent inputEvent = new InputEvent(this, mockConnection, challReq);
 handler.handleInput(inputEvent);
 // Verify that message is sent to the target client
 String target = "testTarget";
 for (Entry client : clients) {
 if (client.name.equals(target)) {
 verify(client.ce).send(eq(challReq));
 break;
 }
 }
 }

 @Test
 public void testHandleInput_Hero() throws IOException {
 Hero hero = new Hero("testName");
 InputEvent inputEvent = new InputEvent(this, mockConnection, hero);
 String name = handler.updateHero(mockConnection, hero);
 assertEquals(name, "testName");
 // Verify that name is updated for the connected client
 verify(mockConnection).send(new UserEvent(true, "testName"));
 }

 @Test
 public void testHandleInput_UserEvent() throws IOException {
 UserEvent userEvent = new UserEvent(false, "testName");
 InputEvent inputEvent = new InputEvent(this, mockConnection, userEvent);
 handler.handleInput(inputEvent);
 // Verify that name is updated for all clients, except the Gamemaster
 String name = "testName";
 for (int i = 0; i < clients.size(); i++) {
 if (clients.get(i).name.equals("Gamemaster")) {
 continue;
 }
 assertEquals(name, clients.get(i).name);
 verify(clients.get(i).ce).send(new UserEvent(false, name));
 }
 }

 @Test(expected = IOException.class)
 public void testHandleInput_IOException() throws IOException {
 InputEvent inputEvent = mock(InputEvent.class);
 doThrow(new IOException()).when(inputEvent).getData();
 handler.handleInput(inputEvent);
 }
}