package dsachat.server; // replace with the actual package name if necessary

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import dsachat.share.ConnectionEndpoint;
import dsachat.share.Entry;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connectionEndpointMock;
    private Hero heroMock;
    private PrintStream originalSystemOut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new Handler();
        connectionEndpointMock = mock(ConnectionEndpoint.class);
        heroMock = mock(Hero.class);
        originalSystemOut = System.out;
        System.out = new PrintStream(new ByteArrayOutputStream());
    }

    

    @Test
    public void testAddConnection() {
        handler.addConnection(connectionEndpointMock);
        verify(connectionEndpointMock).send(any(UserEvent.class));
    }

    @Test
    public void testAddConnectionWhenClientIsNotConnected() {
        when(connectionEndpointMock.isConnected()).thenReturn(false);
        handler.addConnection(connectionEndpointMock);
        verifyNoMoreInteractions(connectionEndpointMock);
    }

    @Test
    public void testAddConnectionWhenClientAlreadyExists() {
        Entry existingEntry = new Entry();
        existingEntry.ce = connectionEndpointMock;
        handler.clients.addElement(existingEntry);
        handler.addConnection(connectionEndpointMock);
        verifyNoMoreInteractions(connectionEndpointMock);
    }

    @Test
    public void testRemoveConnection() {
        Entry entry = new Entry();
        entry.ce = connectionEndpointMock;
        handler.clients.addElement(entry);
        String name = handler.removeConnection(connectionEndpointMock);
        assertEquals("", name);
        verify(connectionEndpointMock).send(any(UserEvent.class));
    }

    @Test
    public void testRemoveConnectionWhenClientDoesNotExist() {
        handler.removeConnection(connectionEndpointMock);
        verifyNoInteractions(connectionEndpointMock);
    }

    @Test
    public void testRemoveConnectionAndSendMessageToOtherClients() {
        Entry existingEntry = new Entry();
        existingEntry.ce = connectionEndpointMock;
        existingEntry.name = "existingClient";
        handler.clients.addElement(existingEntry);
        handler.addConnection(connectionEndpointMock);
        String name = handler.removeConnection(connectionEndpointMock);
        assertEquals("existingClient", name);
        UserEvent newCon = new UserEvent(true, "existingClient");
        handler.sendAllObj(newCon);
        verify(connectionEndpointMock).send(newCon);
    }

    @Test
    public void testHandleInputWithStringMessage() {
        handler.handleInput(new InputEvent(connectionEndpointMock, new String("hello world")));
        String expectedOutput = handler.getName(connectionEndpointMock) + ": hello world";
        verify(connectionEndpointMock, times(handler.clients.size())).send(expectedOutput);
    }

    @Test
    public void testHandleInputWithChallengeObject() {
        Challenge challengeMock = mock(Challenge.class);
        when(challengeMock.roll()).thenReturn("roll result");
        handler.handleInput(new InputEvent(connectionEndpointMock, challengeMock));
        String to = "toClient";
        handler.sendToOneStr(connectionEndpointMock, to, "roll result");
        verify(connectionEndpointMock).send(new UserEvent(true, "toClient") + ": roll result");
    }

    @Test
    public void testHandleInputWithChallReqObject() {
        ConnectionEndpoint targetConnectionMock = mock(ConnectionEndpoint.class);
        ChallReq challReqMock = mock(ChallReq.class);
        when(challReqMock.getTo()).thenReturn("targetClient");
        handler.handleInput(new InputEvent(connectionEndpointMock, challReqMock));
        handler.handleInput(new InputEvent(targetConnectionMock, challReqMock));
        verify(targetConnectionMock).send(challReqMock);
    }

    @Test
    public void testHandleInputWithHeroObject() {
        handler.handleInput(new InputEvent(connectionEndpointMock, heroMock));
        String name = handler.updateHero(connectionEndpointMock, heroMock);
        verify(connectionEndpointMock).send(new UserEvent(false, name));
    }

    @Test
    public void testHandleInputWithUserEventObject() {
        UserEvent userEventMock = mock(UserEvent.class);
        when(userEventMock.isGmConnected()).thenReturn(false);
        handler.handleInput(new InputEvent(connectionEndpointMock, userEventMock));
        Entry entry = new Entry();
        entry.ce = connectionEndpointMock;
        entry.name = "Gamemaster";
        handler.clients.addElement(entry);
        userEventMock = new UserEvent(false, "Gamemaster");
        handler.handleInput(new InputEvent(connectionEndpointMock, userEventMock));
        verify(connectionEndpointMock).send(userEventMock);
    }

    @Test
    public void testSendHeros() {
        Vector<Hero> heroes = new Vector<Hero>();
        Hero hero = new Hero();
        heroes.addElement(hero);
        handler.sendHeros(connectionEndpointMock);
        verify(connectionEndpointMock).send(heroes);
    }

    @Test
    public void testIsGmConnected() {
        handler.addConnection(new Entry());
        handler.addConnection(new Entry("Gamemaster", connectionEndpointMock));
        assertTrue(handler.isGmConnected());
        handler.removeConnection(connectionEndpointMock);
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendSystemMessageToAllClients() {
        String message = "test system message";
        handler.sendAllSystemMsg(message);
        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                handler.clients.elementAt(i).ce.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        verify(connectionEndpointMock, times(handler.clients.size())).send(message);
    }

    @Test
    public void testWhisper() {
        String from = "fromUser";
        String to = "toUser";
        String message = "test whisper message";
        handler.handleInput(new InputEvent(connectionEndpointMock, new String("/w " + to + " " + message)));
        String expectedMessage = from + " whispers: " + message;
        for (int i = 0; i < handler.clients.size(); ++i) {
            if (handler.clients.elementAt(i).name.equals(to)) {
                try {
                    handler.clients.elementAt(i).ce.send(expectedMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        verify(connectionEndpointMock).send(expectedMessage);
    }

    @Test
    public void testRollDiceWithSingleDice() {
        String command = "1d6";
        handler.handleInput(new InputEvent(connectionEndpointMock, new String(command)));
        String expectedOutput = this.getName(connectionEndpointMock) + " rolls 1d6 --> ";
        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                handler.clients.elementAt(i).ce.send(expectedOutput + Challenge.dice(6));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        verify(connectionEndpointMock, times(handler.clients.size())).send(expectedOutput + Challenge.dice(6));
    }

    @Test
    public void testRollDiceWithMultipleDice() {
        String command = "2d6+1";
        handler.handleInput(new InputEvent(connectionEndpointMock, new String(command)));
        String expectedOutput = this.getName(connectionEndpointMock) + " rolls 2d6+1 --> ";
        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                handler.clients.elementAt(i).ce.send(expectedOutput + Challenge.dice(6) + " " + Challenge.dice(6) + " ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        verify(connectionEndpointMock, times(handler.clients.size())).send(expectedOutput + Challenge.dice(6) + " " + Challenge.dice(6) + " ");
    }

    @Test
    public void testRollDiceWithInvalidCommand() {
        String command = "3d2";
        handler.handleInput(new InputEvent(connectionEndpointMock, new String(command)));
        String expectedOutput = this.getName(connectionEndpointMock) + " rolled command /3d2 not found";
        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                handler.clients.elementAt(i).ce.send(expectedOutput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        verify(connectionEndpointMock, times(handler.clients.size())).send(expectedOutput);
    }

    @Test
    public void testSendToOneStr() {
        String from = this.getName(connectionEndpointMock);
        String to = "toUser";
        String message = "test message";
        handler.handleInput(new InputEvent(connectionEndpointMock, new String("/w " + to + " " + message)));
        handler.sendToOneStr(connectionEndpointMock, to, message);
        String expectedMessage = from + ": " + message;
        try {
            connectionEndpointMock.send(expectedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        verify(connectionEndpointMock).send(expectedMessage);
    }

    @Test
    public void testUpdateHero() {
        Hero newHero = new Hero();
        String name = handler.updateHero(connectionEndpointMock, newHero);
        assertEquals(newHero.getName(), name);
        verify(connectionEndpointMock).hero = newHero;
        verify(connectionEndpointMock).send(new UserEvent(false, newHero.getName()));
    }

    @Test
    public void testUpdateHeroWhenClientAlreadyExists() {
        Hero newHero = new Hero();
        Entry existingEntry = new Entry();
        existingEntry.ce = connectionEndpointMock;
        handler.clients.addElement(existingEntry);
        handler.handleInput(new InputEvent(connectionEndpointMock, new Hero()));
        handler.updateHero(connectionEndpointMock, newHero);
        verifyNoMoreInteractions(connectionEndpointMock);
    }

    @Test
    public void testSendAllStr() {
        String message = "test message";
        handler.handleInput(new InputEvent(connectionEndpointMock, new String(message)));
        handler.sendAllStr(connectionEndpointMock, message);
        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                handler.clients.elementAt(i).ce.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        verify(connectionEndpointMock, times(handler.clients.size())).send(message);
    }

    @Test
    public void testSendAllObj() {
        Object obj = new Object();
        handler.sendAllObj(obj);
        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                handler.clients.elementAt(i).ce.send(obj);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        verify(connectionEndpointMock, times(handler.clients.size())).send(obj);
    }

    @Test
    public void testgetName() {
        String name = handler.getName(connectionEndpointMock);
        assertNull(name);
    }

    @Test
    public void testgetNameWhenClientExists() {
        Entry entry = new Entry();
        entry.ce = connectionEndpointMock;
        entry.name = "testName";
        handler.clients.addElement(entry);
        String expectedName = "testName";
        String name = handler.getName(connectionEndpointMock);
        assertEquals(expectedName, name);
    }

    

    @After
    public void tearDown() {
        System.out = originalSystemOut;
    }
}