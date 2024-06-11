package dsachat.server.test;

import static org.junit.Assert.*;
import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.server.ConnectionEndpoint;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class HandlerTest {

@Test
public void testAddConnection() {
Handler handler = new Handler();
ConnectionEndpoint connection = new ConnectionEndpoint();
Entry expectedEntry = new Entry();
expectedEntry.name = "";

handler.addConnection(connection);

assertEquals(expectedEntry, handler.clients.lastElement());
}

@Test
public void testRemoveConnection() {
Handler handler = new Handler();
ConnectionEndpoint connection = new ConnectionEndpoint();
String name = "TestUser";
handler.addConnection(connection);
handler.clients.get(0).name = name;

handler.removeConnection(connection);

assertEquals(null, handler.clients.lastElement().name);
}

@Test
public void testHandleInputValidMessage() {
Handler handler = new Handler();
ConnectionEndpoint connection = mock(ConnectionEndpoint.class);
when(connection.send(anyString())).thenReturn(true);

String message = "/whisper TestUser Hello";

handler.handleInput(new InputEvent(connection, new StringData(message)));

verify(connection, times(1)).send("You whisper to TestUser: Hello");
}

@Test
public void testHandleInputInvalidMessage() {
Handler handler = new Handler();
ConnectionEndpoint connection = mock(ConnectionEndpoint.class);
when(connection.send(anyString())).thenReturn(true);

String message = "/invalidCommand";

handler.handleInput(new InputEvent(connection, new StringData(message)));

verifyNoMoreInteractions(connection);
}
}