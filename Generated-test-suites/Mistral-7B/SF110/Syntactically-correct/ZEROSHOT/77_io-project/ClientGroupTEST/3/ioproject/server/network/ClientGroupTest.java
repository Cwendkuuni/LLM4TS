import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.WeakReference;

import static org.junit.Assert.*;

public class ClientGroupTest {

 private ClientGroup clientGroup;
 private ClientHandler clientHandlerMock;

 @Before
 public void setup() {
 clientGroup = new ClientGroup(null);
 clientHandlerMock = Mockito.mock(ClientHandler.class);
 }

 @Test
 public void testAddClientAndSize() {
 Client client = Mockito.mock(Client.class);
 clientGroup.add(client);
 assertEquals(1, clientGroup.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullClient() {
 clientGroup.add(null);
 }

 @Test
 public void testContainsClient() {
 Client client = Mockito.mock(Client.class);
 clientGroup.add(client);
 assertTrue(clientGroup.contains(client));
 }

 @Test
 public void testContainsNullClient() {
 assertFalse(clientGroup.contains(null));
 }

 @Test
 public void testIterator() {
 Client client1 = Mockito.mock(Client.class);
 Client client2 = Mockito.mock(Client.class);
 clientGroup.add(client1);
 clientGroup.add(client2);
 Set<Client> clients = new LinkedHashSet<>(Arrays.asList(client1, client2));
 Iterator<Client> iterator = clientGroup.iterator();
 assertTrue(iterator.hasNext());
 assertEquals(client1, iterator.next());
 assertTrue(iterator.hasNext());
 assertEquals(client2, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testRemoveClientAndSize() {
 Client client = Mockito.mock(Client.class);
 clientGroup.add(client);
 assertEquals(1, clientGroup.size());
 clientGroup.remove(client);
 assertEquals(0, clientGroup.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullClient() {
 clientGroup.remove(null);
 }

 @Test
 public void testCreateSubGroupAndSize() {
 ClientGroup subGroup = clientGroup.createSubGroup();
 assertEquals(0, subGroup.size());
 assertEquals(1, clientGroup.size());
 }

 @Test
 public void testNotifyMessageSent() {
 Client client = Mockito.mock(Client.class);
 Object message = new Object();
 clientGroup.add(client);
 clientHandlerMock.messageSent(client, message);
 Mockito.verify(client).receiveMessage(message);
 }

 @Test
 public void testNotifyMessageSentToSubGroup() {
 Client client = Mockito.mock(Client.class);
 Object message = new Object();
 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client);
 clientGroup.add(subGroup);
 clientGroup.add(client);
 clientHandlerMock.messageSent(client, message);
 Mockito.verify(subGroup).notifyMessageSent(client, message);
 Mockito.verify(client).receiveMessage(message);
 }

 @Test
 public void testNotifyMessageReceived() {
 Client client = Mockito.mock(Client.class);
 Object message = new Object();
 clientGroup.add(client);
 clientHandlerMock.messageReceived(client, message);
 Mockito.verify(client).sendMessage(message);
 }

 @Test
 public void testNotifyMessageReceivedToSubGroup() {
 Client client = Mockito.mock(Client.class);
 Object message = new Object();
 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client);
 clientGroup.add(subGroup);
 clientGroup.add(client);
 clientHandlerMock.messageReceived(client, message);
 Mockito.verify(client).sendMessage(message);
 Mockito.verify(subGroup).notifyMessageReceived(client, message);
 }

 @Test
 public void testNotifyExceptionCaught() {
 Client client = Mockito.mock(Client.class);
 Throwable cause = new Throwable();
 clientGroup.add(client);
 clientHandlerMock.exceptionCaught(client, cause);
 Mockito.verify(client).handleException(cause);
 }

 @Test
 public void testNotifyExceptionCaughtToSubGroup() {
 Client client = Mockito.mock(Client.class);
 Throwable cause = new Throwable();
 ClientGroup subGroup = clientGroup.createSubGroup();
 subGroup.add(client);
 clientGroup.add(subGroup);
 clientGroup.add(client);
 clientHandlerMock.exceptionCaught(client, cause);
 Mockito.verify(client).handleException(cause);
 Mockito.verify(subGroup).notifyExceptionCaught(client, cause);
 }
}