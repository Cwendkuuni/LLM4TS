import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupIterator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup1;
 private ClientGroup subGroup2;
 private Client client1;
 private Client client2;
 private Client client3;
 private ClientHandler handler;

 @Before
 public void setup() {
 rootGroup = new ClientGroup(null);
 subGroup1 = rootGroup.createSubGroup();
 subGroup2 = subGroup1.createSubGroup();
 client1 = new Client("client1");
 client2 = new Client("client2");
 client3 = new Client("client3");
 handler = Mockito.mock(ClientHandler.class);
 rootGroup.addClientHandler(handler);
 }

 @Test
 public void testAddClient() {
 // test adding client to root group
 assertTrue(rootGroup.add(client1));
 assertEquals(1, rootGroup.size());
 // test adding client to sub group
 assertTrue(subGroup1.add(client2));
 assertEquals(1, subGroup1.size());
 // test adding duplicate client to group
 assertFalse(rootGroup.add(client1));
 assertFalse(subGroup1.add(client2));
 }

 @Test
 public void testRemoveClient() {
 // test removing client from root group
 assertTrue(rootGroup.contains(client1));
 rootGroup.remove(client1);
 assertFalse(rootGroup.contains(client1));
 // test removing client from sub group
 assertTrue(subGroup1.contains(client2));
 subGroup1.remove(client2);
 assertFalse(subGroup1.contains(client2));
 }

 @Test
 public void testContainsClient() {
 // test checking if client is in root group
 assertFalse(rootGroup.contains(client1));
 rootGroup.add(client1);
 assertTrue(rootGroup.contains(client1));
 // test checking if client is in sub group
 assertFalse(subGroup1.contains(client1));
 subGroup1.add(client1);
 assertTrue(subGroup1.contains(client1));
 }

 @Test
 public void testSize() {
 Set<Client> clients = new HashSet<>(Arrays.asList(client1, client2, client3));
 rootGroup.addAll(clients);
 subGroup1.addAll(clients);
 subGroup2.addAll(clients);
 assertEquals(3, rootGroup.size());
 assertEquals(3, subGroup1.size());
 assertEquals(3, subGroup2.size());
 }

 @Test
 public void testNotifyMessageSent() {
 // test sending message to root group and its handlers
 Mockito.doAnswer(invocation -> {
 boolean responded = false;
 Client client = invocation.getArgument(0);
 Object message = invocation.getArgument(1);
 System.out.println("Message sent from root group to client " + client + " with message " + message);
 responded = true;
 return responded;
 }).when(handler).messageSent(client, message);
 client1.sendMessage(client2, "test message");
 assertTrue(rootGroup.notifyMessageSent(client1, "test message"));
 // test sending message to sub group and its handlers
 Mockito.doAnswer(invocation -> {
 boolean responded = false;
 Client client = invocation.getArgument(0);
 Object message = invocation.getArgument(1);
 System.out.println("Message sent from sub group to client " + client + " with message " + message);
 responded = true;
 return responded;
 }).when(subGroup1.handler).messageSent(client2, "test message");
 subGroup1.notifyMessageSent(client1, "test message");
 // test sending message to non-existent client
 assertFalse(rootGroup.notifyMessageSent(client3, "test message"));
 }

 @Test
 public void testNotifyMessageReceived() {
 // test receiving message from root group and its handlers
 Mockito.doAnswer(invocation -> {
 boolean responded = false;
 Client client = invocation.getArgument(0);
 Object message = invocation.getArgument(1);
 System.out.println("Message received by client " + client + " with message " + message);
 responded = true;
 return responded;
 }).when(client1).messageReceived(client2, "test message");
 client1.receiveMessage("test message");
 assertTrue(rootGroup.notifyMessageReceived(client1, "test message"));
 // test receiving message from sub group and its handlers
 Mockito.doAnswer(invocation -> {
 boolean responded = false;
 Client client = invocation.getArgument(0);
 Object message = invocation.getArgument(1);
 System.out.println("Message received by client " + client + " with message " + message);
 responded = true;
 return responded;
 }).when(client2).messageReceived(client3, "test message");
 subGroup1.notifyMessageReceived(client1, "test message");
 // test receiving message from non-existent client
 assertFalse(rootGroup.notifyMessageReceived(client3, "test message"));
 }

 @Test
 public void testNotifyExceptionCaught() {
 // test catching exception from root group and its handlers
 Mockito.doAnswer(invocation -> {
 boolean responded = false;
 Client client = invocation.getArgument(0);
 Throwable cause = invocation.getArgument(1);
 System.out.println("Exception caught by client " + client + " with cause " + cause.getMessage());
 responded = true;
 return responded;
 }).when(handler).exceptionCaught(client1, Mockito.any(Throwable.class));
 client1.sendException(new RuntimeException("test exception"));
 assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException("test exception")));
 // test catching exception from sub group and its handlers
 Mockito.doAnswer(invocation -> {
 boolean responded = false;
 Client client = invocation.getArgument(0);
 Throwable cause = invocation.getArgument(1);
 System.out.println("Exception caught by client " + client + " with cause " + cause.getMessage());
 responded = true;
 return responded;
 }).when(client2).exceptionCaught(client3, Mockito.any(Throwable.class));
 subGroup1.notifyExceptionCaught(client1, new RuntimeException("test exception"));
 // test catching exception from non-existent client
 assertFalse(rootGroup.notifyExceptionCaught(client3, new RuntimeException("test exception")));
 }

 @Test
 public void testIterator() {
 Set<Client> clients = new HashSet<>(Arrays.asList(client1, client2, client3));
 rootGroup.addAll(clients);
 subGroup1.addAll(clients);
 subGroup2.addAll(clients);
 ClientGroupIterator iterator = rootGroup.iterator();
 assertTrue(iterator.hasNext());
 assertEquals(client1, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(client2, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(client3, iterator.next());
 assertFalse(iterator.hasNext());
 }
}