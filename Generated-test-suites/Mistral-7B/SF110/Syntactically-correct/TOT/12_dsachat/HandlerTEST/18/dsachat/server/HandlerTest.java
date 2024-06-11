package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

 private Handler handler;
 private ConnectionEndpoint connection;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStream;

 @Before
 public void setUp() {
 handler = new Handler();
 connection = Mockito.mock(ConnectionEndpoint.class);
 outputStream = new ByteArrayOutputStream();
 System.setOut(new PrintStream(outputStream));
 inputStream = new ByteArrayInputStream(new byte[0]);
 }

 @Test
 public void testAddConnection() {
 // Given: an empty list of clients
 Vector<Entry> clients = handler.clients;
 assertEquals(0, clients.size());

 // When: a new connection is added
 handler.addConnection(connection);

 // Then: the client is added to the list
 assertEquals(1, clients.size());
 // The client's name is empty
 assertNull(clients.elementAt(0).name);
 // The client is connected
 verify(connection).isConnected();

 // Then: a UserEvent is sent to all other clients
 for (int i = 0; i < clients.size(); ++i) {
 if (i != 0) { // Don't send the event to the newly connected client
 UserEvent userEvent = new UserEvent(false, "Client " + (i + 1));
 handler.handleInput(new InputEvent(userEvent, connection));
 verify(clients.elementAt(0).ce).send(userEvent);
 }
 }
 }

 @Test
 public void testRemoveConnection() {
 // Given: a list with one client
 Vector<Entry> clients = new Vector<>(Arrays.asList(new Entry()));
 handler.clients = clients;
 Entry entry = clients.elementAt(0);
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 entry.ce = connection;
 entry.name = "Client";

 // When: the connection is removed
 handler.removeConnection(connection);

 // Then: the client is removed from the list
 assertEquals(0, handler.clients.size());

 // Then: a UserEvent is sent to all other clients announcing the disconnection
 UserEvent userEvent = new UserEvent(true, "Client");
 handler.sendAllObj(userEvent);
 verify(clients.elementAt(0).ce, never()).send(userEvent);
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (i != 0) { // Don't check the disconnected client
 UserEvent userEvent = new UserEvent(false, "Client " + (i + 1));
 handler.handleInput(new InputEvent(userEvent, connection));
 verify(clients.elementAt(i).ce).send(userEvent);
 }
 }
 }

 @Test
 public void testHandleInput_String() throws IOException {
 // Given: a list with one client
 Vector<Entry> clients = new Vector<>(Arrays.asList(new Entry()));
 handler.clients = clients;
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 Entry entry = new Entry();
 entry.ce = connection;
 entry.name = "Client";
 handler.clients.add(entry);

 // When: a String message is received
 String message = "Hello World";
 handler.handleInput(new InputEvent(new Object[]{message}, connection));

 // Then: the message is sent to all clients
 for (int i = 0; i < handler.clients.size(); ++i) {
 UserEvent userEvent = new UserEvent(false, "Client");
 handler.handleInput(new InputEvent(new Object[]{userEvent}, connection));
 assertEquals(message, outputStream.toString());
 }

 // Reset the output stream for the next test case
 System.setOut(System.out);
 }

 @Test
 public void testHandleInput_Challenge() throws IOException {
 // Given: a list with one client
 Vector<Entry> clients = new Vector<>(Arrays.asList(new Entry()));
 handler.clients = clients;
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 Entry entry = new Entry();
 entry.ce = connection;
 entry.name = "Client";
 handler.clients.add(entry);

 // When: a Challenge object is received
 Challenge challenge = new Challenge();
 handler.handleInput(new InputEvent(new Object[]{challenge}, connection));

 // Then: the Challenge's roll method is called and the result is sent to the sender
 verify(challenge).roll();
 String send = challenge.roll();
 UserEvent userEvent = new UserEvent(false, "Client");
 handler.handleInput(new InputEvent(new Object[]{userEvent}, connection));
 assertEquals(send, outputStream.toString());

 // Then: if the Challenge has a To field, the result is also sent to the specified client
 Challenge challengeWithTo = new Challenge();
 challengeWithTo.setTo("Client 2");
 handler.handleInput(new InputEvent(new Object[]{challengeWithTo}, connection));
 UserEvent userEvent2 = new UserEvent(false, "Client 2");
 handler.handleInput(new InputEvent(new Object[]{userEvent2}, connection));
 assertEquals(send, outputStream.toString());
 }

 @Test
 public void testHandleInput_ChallReq() throws IOException {
 // Given: a list with two clients: Client 1 and Client 2
 Vector<Entry> clients = new Vector<>(Arrays.asList(
 new Entry("Client 1", new ConnectionEndpoint(1235)),
 new Entry("Client 2", new ConnectionEndpoint(1236))));
 handler.clients = clients;

 // When: Client 1 sends a ChallengeReq to Client 2
 ConnectionEndpoint client1 = clients.get(0).ce;
 ConnectionEndpoint client2 = clients.get(1).ce;
 ChallReq challReq = new ChallReq("Client 2");
 handler.handleInput(new InputEvent(new Object[]{challReq}, client1));

 // Then: Client 2 receives the ChallengeReq and sends back a Challenge
 UserEvent userEvent1 = new UserEvent(false, "Client 1");
 handler.handleInput(new InputEvent(new Object[]{userEvent1}, client2));
 Challenge challenge = new Challenge();
 UserEvent userEvent2 = new UserEvent(false, "Client 2");
 handler.handleInput(new InputEvent(new Object[]{userEvent2}, client1));
 assertEquals(challenge.getClass(), userEvent2.getData().getClass());
 }

 @Test
 public void testHandleInput_Hero() throws IOException {
 // Given: a list with one client
 Vector<Entry> clients = new Vector<>(Arrays.asList(new Entry()));
 handler.clients = clients;
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 Entry entry = new Entry();
 entry.ce = connection;
 entry.name = "Client";
 handler.clients.add(entry);

 // When: a Hero object is received
 Hero hero = new Hero();
 handler.handleInput(new InputEvent(new Object[]{hero}, connection));

 // Then: the client's hero is updated
 assertEquals(hero.getName(), handler.updateHero(connection, hero).getName());

 // Then: a UserEvent is sent to all clients announcing the new hero
 UserEvent userEvent = new UserEvent(false, hero.getName());
 handler.handleInput(new InputEvent(new Object[]{userEvent}, connection));
 for (int i = 0; i < handler.clients.size(); ++i) {
 if (i != 0) { // Don't check the newly connected client
 String name = handler.clients.get(i).name;
 assertEquals(hero.getName(), name);
 }
 }
 }

 @Test
 public void testHandleInput_UserEvent_notGm() throws IOException {
 // Given: a list with one client (not the Gamemaster)
 Vector<Entry> clients = new Vector<>(Arrays.asList(new Entry()));
 handler.clients = clients;
 ConnectionEndpoint connection = new ConnectionEndpoint(1234);
 Entry entry = new Entry();
 entry.ce = connection;
 handler.clients.add(entry);
 UserEvent userEvent = new UserEvent(true, "Gamemaster");

 // When: a UserEvent from the Gamemaster is received
 handler.handleInput(new InputEvent(new Object[]{userEvent}, connection));

 // Then: the client's name is set to "Gamemaster"
 assertEquals("Gamemaster", handler.clients.get(0).name);
 }
}