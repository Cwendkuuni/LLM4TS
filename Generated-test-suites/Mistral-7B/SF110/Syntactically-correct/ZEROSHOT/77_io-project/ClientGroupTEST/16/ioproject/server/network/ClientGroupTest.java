import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class ClientGroupTest {

 private ClientGroup rootGroup;
 private Client client1;
 private Client client2;
 private Client client3;
 private Client handlerClient;
 private ClientHandler handler;

 @Before
 public void setup() {
 rootGroup = new ClientGroup(null);
 client1 = Mockito.spy(new Client("client1"));
 client2 = Mockito.spy(new Client("client2"));
 client3 = Mockito.spy(new Client("client3"));
 handlerClient = Mockito.spy(new Client("handlerClient"));
 handler = Mockito.mock(ClientHandler.class);
 }

 @Test
 public void testNotifyMessageSent_whenClientIsNotInGroup_thenFalse() {
 assertFalse(rootGroup.notifyMessageSent(client1, "message"));
 }

 @Test
 public void testNotifyMessageSent_whenClientIsInGroup_thenTrue() {
 rootGroup.add(client1);
 assertTrue(rootGroup.notifyMessageSent(client1, "message"));
 }

 @Test
 public void testNotifyMessageSent_whenClientIsInSubGroup_thenTrue() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 rootGroup.add(subGroup);
 rootGroup.add(client1);
 subGroup.add(client1);
 assertTrue(rootGroup.notifyMessageSent(client1, "message"));
 }

 @Test
 public void testNotifyMessageSent_whenHandlerIsSet_thenTrue() {
 rootGroup.addClientHandler(handler);
 rootGroup.add(client1);
 handler.messageSent(client1, "message");
 assertTrue(rootGroup.notifyMessageSent(client1, "message"));
 }

 @Test
 public void testNotifyMessageReceived_whenClientIsNotInGroup_thenFalse() {
 assertFalse(rootGroup.notifyMessageReceived(client1, "message"));
 }

 @Test
 public void testNotifyMessageReceived_whenClientIsInGroup_thenTrue() {
 rootGroup.add(client1);
 assertTrue(rootGroup.notifyMessageReceived(client1, "message"));
 }

 @Test
 public void testNotifyMessageReceived_whenClientIsInSubGroup_thenTrue() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 rootGroup.add(subGroup);
 rootGroup.add(client1);
 subGroup.add(client1);
 assertTrue(rootGroup.notifyMessageReceived(client1, "message"));
 }

 @Test
 public void testNotifyMessageReceived_whenHandlerIsSet_thenTrue() {
 rootGroup.addClientHandler(handler);
 rootGroup.add(client1);
 handler.messageReceived(client1, "message");
 assertTrue(rootGroup.notifyMessageReceived(client1, "message"));
 }

 @Test
 public void testNotifyExceptionCaught_whenClientIsNotInGroup_thenFalse() {
 assertFalse(rootGroup.notifyExceptionCaught(client1, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_whenClientIsInGroup_thenTrue() {
 rootGroup.add(client1);
 assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_whenClientIsInSubGroup_thenTrue() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 rootGroup.add(subGroup);
 rootGroup.add(client1);
 subGroup.add(client1);
 assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException()));
 }

 @Test
 public void testNotifyExceptionCaught_whenHandlerIsSet_thenTrue() {
 rootGroup.addClientHandler(handler);
 rootGroup.add(client1);
 handler.exceptionCaught(client1, new RuntimeException());
 assertTrue(rootGroup.notifyExceptionCaught(client1, new RuntimeException()));
 }

 @Test
 public void testAdd_whenClientIsAlreadyInParentGroup_thenFalse() {
 rootGroup.add(client1);
 assertFalse(rootGroup.add(client1));
 }

 @Test
 public void testAdd_whenClientIsNotInParentGroup_thenTrue() {
 assertFalse(rootGroup.contains(client2));
 assertTrue(rootGroup.add(client2));
 }

 @Test
 public void testRemove_whenClientIsNotInGroup_thenFalse() {
 assertFalse(rootGroup.remove(client1));
 }

 @Test
 public void testRemove_whenClientIsInGroup_thenTrue() {
 rootGroup.add(client1);
 assertTrue(rootGroup.remove(client1));
 }

 @Test
 public void testRemove_whenClientIsInSubGroupAndParentGroup_thenTrue() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 rootGroup.add(subGroup);
 rootGroup.add(client1);
 subGroup.add(client1);
 assertTrue(rootGroup.remove(client1));
 }

 @Test
 public void testRemove_whenClientIsOnlyInSubGroup_thenFalse() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 rootGroup.add(subGroup);
 subGroup.add(client1);
 assertFalse(rootGroup.remove(client1));
 }

 @Test
 public void testCreateSubGroup_whenParentGroupIsNotNull_thenNewSubGroupIsAddedToParentGroup() {
 ClientGroup subGroup = rootGroup.createSubGroup();
 assertTrue(rootGroup.contains(subGroup));
 }

 @Test
 public void testCreateSubGroup_whenParentGroupIsNull_thenNewSubGroupIsRootGroup() {
 ClientGroup subGroup = new ClientGroup(null).createSubGroup();
 assertEquals(subGroup, subGroup.parentGroup());
 }

 @Test
 public void testAddClientHandler_whenHandlerIsSet_thenHandlerIsSetInGroup() {
 ClientGroup group = new ClientGroup(null);
 ClientHandler handler = Mockito.mock(ClientHandler.class);
 group.addClientHandler(handler);
 assertEquals(handler, group.handler);
 }
}