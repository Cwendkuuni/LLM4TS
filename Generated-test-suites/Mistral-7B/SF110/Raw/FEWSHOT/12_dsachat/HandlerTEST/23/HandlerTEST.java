package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Hero;
import dsachat.share.hero.IHero;
import dsachat.share.hero.Warrior;
import dsachat.share.user.UserEvent;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint1;
    private ConnectionEndpoint connectionEndpoint2;
    private static final int PORT1 = 1234;
    private static final int PORT2 = 5678;
    private static final String NAME1 = "user1";
    private static final String NAME2 = "user2";
    private static final String GAMEMASTER_NAME = "Gamemaster";
    private static final String CHALLENGE_RESPONSE = "Challenge response";
    private static final String CHALLENGE_REQUEST = "Challenge request";
    private Vector<Entry> clients;

    @Before
    public void setUp() {
        handler = new Handler();
        connectionEndpoint1 = mock(ConnectionEndpoint.class);
        connectionEndpoint2 = mock(ConnectionEndpoint.class);
        clients = handler.clients;
        when(connectionEndpoint1.getClientPort()).thenReturn(PORT1);
        when(connectionEndpoint2.getClientPort()).thenReturn(PORT2);
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
    }

    @After
    public void tearDown() {
        handler.removeConnection(connectionEndpoint1);
        handler.removeConnection(connectionEndpoint2);
    }

    @Test
    public void testAddConnection() {
        assertNotNull(handler.clients);
        assertEquals(0, handler.clients.size());
        handler.addConnection(connectionEndpoint1);
        assertEquals(1, handler.clients.size());
        handler.addConnection(connectionEndpoint2);
        assertEquals(2, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        assertNotNull(handler.clients);
        assertEquals(0, handler.clients.size());
        handler.addConnection(connectionEndpoint1);
        handler.addConnection(connectionEndpoint2);
        assertEquals(2, handler.clients.size());
        handler.removeConnection(connectionEndpoint1);
        assertEquals(1, handler.clients.size());
        handler.removeConnection(connectionEndpoint2);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInputStringMessage() {
        String message = "/whisper user2 hello";
        handler.handleInput(new InputEvent(connectionEndpoint1, new String(message.getBytes())));
        verify(connectionEndpoint2, times(1)).send(eq(new ChallengeRequest(NAME1, NAME2, CHALLENGE_REQUEST)));
    }

    @Test
    public void testHandleInputChallenge() {
        final Challenge challenge = new Challenge();
        clients.add(new Entry(connectionEndpoint1, new Hero(new Warrior("User1")), "User1"));
        when(connectionEndpoint1.send(anyObject())).thenAnswer(invocation -> {
            Object arg = invocation.getArgument(0);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(outputStream);
            output.writeObject(arg);
            output.close();
            return new String(outputStream.toByteArray());
        });
        handler.handleInput(new InputEvent(connectionEndpoint1, challenge));
        verify(connectionEndpoint1, times(1)).send(anyString());
        verify(connectionEndpoint2, times(1)).send(eq(CHALLENGE_RESPONSE));
    }

    @Test
    public void testHandleInputChallReq() {
        final ChallReq challReq = new ChallReq(NAME2, "hello");
        clients.add(new Entry(connectionEndpoint2, null, "User2"));
        when(connectionEndpoint2.send(anyObject())).thenAnswer(invocation -> {
            Object arg = invocation.getArgument(0);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(outputStream);
            output.writeObject(arg);
            output.close();
            return new String(outputStream.toByteArray());
        });
        handler.handleInput(new InputEvent(connectionEndpoint1, challReq));
        verify(connectionEndpoint2, times(1)).send(eq(CHALLENGE_REQUEST));
    }

    @Test
    public void testUpdateHero() {
        final Hero hero = new Warrior("User1");
        clients.add(new Entry(connectionEndpoint1, hero, "User1"));
        String newName = "NewUserName";
        handler.updateHero(connectionEndpoint1, new Hero(new Warrior("NewUserName")));
        assertEquals(newName, clients.get(0).name);
    }

    @Test
    public void testSendAllObj() {
        final Object obj = new Object();
        when(connectionEndpoint1.send(anyObject())).thenAnswer(invocation -> {
            Object arg = invocation.getArgument(0);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(outputStream);
            output.writeObject(arg);
            output.close();
            return new String(outputStream.toByteArray());
        });
        handler.sendAllObj(obj);
        for (int i = 0; i < clients.size(); i++) {
            verify(clients.get(i).ce, times(1)).send(eq(obj));
        }
    }

    @Test
    public void testSendAllStr() {
        final String message = "test message";
        when(connectionEndpoint1.send(anyString())).thenAnswer(invocation -> {
            String arg = invocation.getArgument(0);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            writer.print(arg);
            writer.flush();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            StringWriter stringWriter = new StringWriter();
            new ObjectInputStream(inputStream).readObject();
            return stringWriter.toString();
        });
        handler.sendAllStr(connectionEndpoint1, message);
        for (int i = 0; i < clients.size(); i++) {
            verify(clients.get(i).ce, times(1)).send(eq(message));
        }
    }

    private static class Entry {
        ConnectionEndpoint ce;
        Hero hero;
        String name;

        Entry() {
            ce = null;
            hero = null;
            name = "";
        }
    }
}