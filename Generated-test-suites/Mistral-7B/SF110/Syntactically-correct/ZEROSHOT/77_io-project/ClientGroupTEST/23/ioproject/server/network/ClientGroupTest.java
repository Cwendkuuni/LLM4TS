import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ioproject.common.network.Client;
import ioproject.common.network.ClientHandler;
import ioproject.server.network.ClientGroup;
import ioproject.server.network.ClientGroupImpl;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private ClientGroup subGroup;
 private Client client;
 private ClientHandler handler;

 @Before
 public void setUp() {
 rootGroup = new ClientGroup(null);
 subGroup = rootGroup.createSubGroup();
 client = Mockito.mock(Client.class);
 handler = Mockito.mock(ClientHandler.class);
 }

 @Test
 public void testParentGroup() {
 Assert.assertNull(rootGroup.parentGroup());
 }

 @Test
 public void testNotifyMessageSent_ClientIsInGroup_ReturnsTrue() {
 Mockito.when(subGroup.notifyMessageSent(client, Mockito.any())).thenReturn(true);
 Assert.assertTrue(rootGroup.notifyMessageSent(client, Mockito.any()));
 }

 @Test
 public void testNotifyMessageSent_ClientIsNotInGroup_ReturnsFalse() {
 Mockito.when(subGroup.notifyMessageSent(client, Mockito.any())).thenReturn(false);
 Assert.assertFalse(rootGroup.notifyMessageSent(client, Mockito.any()));
 }

 @Test
 public void testNotifyMessageSent_ClientIsInSubGroup_PropagatesMessageToHandler() {
 Mockito.when(subGroup.notifyMessageSent(client, Mockito.any())).thenReturn(false);
 Mockito.when(handler.messageSent(client, Mockito.any())).thenReturn(true);
 Assert.assertTrue(rootGroup.notifyMessageSent(client, Mockito.any()));
 }

 @Test
 public void testNotifyMessageReceived_ClientIsInGroup_ReturnsTrue() {
 Assert.assertTrue(rootGroup.notifyMessageReceived(client, Mockito.any()));
 }

 @Test
 public void testNotifyMessageReceived_ClientIsNotInGroup_ReturnsFalse() {
 Assert.assertFalse(rootGroup.notifyMessageReceived(client, Mockito.any()));
 }

 @Test
 public void testNotifyMessageReceived_ClientIsInSubGroup_PropagatesMessageToHandler() {
 Mockito.when(subGroup.notifyMessageReceived(client, Mockito.any())).thenReturn(false);
 Mockito.when(handler.messageReceived(client, Mockito.any())).thenReturn(true);
 Assert.assertTrue(rootGroup.notifyMessageReceived(client, Mockito.any()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsInGroup_ReturnsTrue() {
 Assert.assertTrue(rootGroup.notifyExceptionCaught(client, Mockito.any()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsNotInGroup_ReturnsFalse() {
 Assert.assertFalse(rootGroup.notifyExceptionCaught(client, Mockito.any()));
 }

 @Test
 public void testNotifyExceptionCaught_ClientIsInSubGroup_PropagatesExceptionToHandler() {
 Mockito.when(subGroup.notifyExceptionCaught(client, Mockito.any())).thenReturn(false);
 Mockito.when(handler.exceptionCaught(client, Mockito.any())).thenReturn(true);
 Assert.assertTrue(rootGroup.notifyExceptionCaught(client, Mockito.any()));
 }

 @Test
 public void testAddClient_ClientIsNotInParentGroup_AddsClientToGroup() {
 Assert.assertFalse(rootGroup.contains(client));
 rootGroup.add(client);
 Assert.assertTrue(rootGroup.contains(client));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddClient_ClientIsAlreadyInGroup_ThrowsException() {
 rootGroup.add(client);
 rootGroup.add(client);
 }

 @Test
 public void testAddClientHandler() {
 rootGroup.addClientHandler(handler);
 Assert.assertNotNull(rootGroup.handler);
 }

 @Test
 public void testCreateSubGroup() {
 Assert.assertNotNull(subGroup);
 }

 @Test
 public void testSize() {
 Set<Client> clients = new LinkedHashSet<>();
 clients.add(client);
 rootGroup.add(client);
Assert.assertEquals(1, rootGroup.size());
 }
}