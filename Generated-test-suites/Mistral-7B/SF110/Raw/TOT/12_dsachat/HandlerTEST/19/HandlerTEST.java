package dsachat.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpoint;
    private Entry entry;
    private Hero hero;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new Handler();
        connectionEndpoint = mock(ConnectionEndpoint.class);
        entry = new Entry();
        entry.ce = connectionEndpoint;
        hero = new Hero("TestHero");
    }

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpoint);
        assertEquals(1, handler.clients.size());
        verify(connectionEndpoint, times(1)).send(any(UserEvent.class));
    }

    @Test(expected = IOException.class)
    public void testAddConnection_IOException() {
        when(connectionEndpoint.send(any(UserEvent.class))).thenThrow(new IOException());
        handler.addConnection(connectionEndpoint);
    }

    @Test
    public void testRemoveConnection() {
        handler.addConnection(connectionEndpoint);
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
        verify(connectionEndpoint, times(1)).getClientPort();
    }

    @Test
    public void testRemoveConnection_NullName() {
        handler.addConnection(connectionEndpoint);
        handler.clients.get(0).name = null;
        handler.removeConnection(connectionEndpoint);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInput_StringData() {
        handler.addConnection(connectionEndpoint);
        String testMessage = "Hello world!";
        handler.handleInput(new InputEvent("TestUser", new ByteArrayInputStream(testMessage.getBytes())));
        verify(connectionEndpoint, times(1)).send(new String("TestUser: " + testMessage));
    }

    @Test
    public void testHandleInput_ChallengeData() {
        handler.addConnection(connectionEndpoint);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream out = outputStream;
        Hero hero = new Hero("TestHero");
        Challenge challenge = new Challenge(2, 6, hero);

        handler.handleInput(new InputEvent("TestUser", new ByteArrayInputStream(new byte[0]), outputStream));
        handler.handleInput(new InputEvent("TestUser", new ByteArrayInputStream(challenge.toByteArray())));

        String expectedMessage = "TestUser rolled 2d6+0: 3 5";
        String actualMessage = new String(outputStream.toByteArray());
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testHandleInput_ChallReqData() {
        handler.addConnection(connectionEndpoint);
        handler.addConnection(new ConnectionEndpoint("TestUser2"));
        handler.handleInput(new InputEvent("TestUser", new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream()));
        handler.handleInput(new InputEvent("TestUser2", new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChallReq challReq = new ChallReq("TestUser", "TestUser2");
        handler.handleInput(new InputEvent("TestUser", new ByteArrayInputStream(challReq.toByteArray()), outputStream));

        String expectedMessage = "TestUser challenges TestUser2 to a dice roll.";
        String actualMessage = new String(outputStream.toByteArray());
        assertEquals(expectedMessage, actualMessage);

        verify(connectionEndpoint, times(1)).send(challReq);
        verify(connectionEndpoint, times(1)).send(new String("TestUser: You challenged TestUser2 to a dice roll."));
        verify(connectionEndpoint, times(1)).send(new String("TestUser2: TestUser challenged you to a dice roll."));
    }

    @Test
    public void testHandleInput_HeroData() {
        handler.addConnection(connectionEndpoint);
        handler.handleInput(new InputEvent("TestUser", new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream()));
        handler.handleInput(new InputEvent("TestUser", new ByteArrayInputStream(hero.toByteArray())));

        verify(connectionEndpoint, times(1)).send(new String("TestUser updated his hero"));
        verify(connectionEndpoint, times(1)).send("Welcome TestHero");
    }

    @Test
    public void testSendHeros() {
        handler.addConnection(connectionEndpoint);
        Vector<Hero> heroes = new Vector<>();
        heroes.add(hero);

        handler.sendHeros(connectionEndpoint);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream out = outputStream;
        handler.handleInput(new InputEvent("TestUser", new ByteArrayInputStream(new byte[0]), outputStream));

        String expectedMessage = "Heroes: [TestHero]";
        String actualMessage = new String(outputStream.toByteArray());
        assertEquals(expectedMessage, actualMessage);
    }
}