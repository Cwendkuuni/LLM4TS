package dsachat.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.commons.cli.UserEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {

    @Mock
    private ConnectionEndpoint connectionEndpoint;

    @Mock
    private InputStream inputStream;

    @Mock
    private OutputStream outputStream;

    @Mock
    private Hero hero;

    @InjectMocks
    private Handler handler;

    @Test
    public void testAddConnection() {
        final ConnectionEndpoint connectionEndpointSpy = mock(ConnectionEndpoint.class);
        final Entry entry = Mockito.spy(new Entry());

        handler.addConnection(connectionEndpointSpy);
        Mockito.verify(handler.clients).addElement(entry);
    }

    @Test
    public void testRemoveConnection() {
        final ConnectionEndpoint connectionEndpoint = mock(ConnectionEndpoint.class);
        final Entry entry = new Entry();
        entry.ce = connectionEndpoint;
        entry.name = "testName";

        handler.clients.addElement(entry);

        handler.removeConnection(connectionEndpoint);
        Mockito.verify(handler.clients).removeElement(entry);
    }

    @Test
    public void testHandleInputStringData() {
        when(connectionEndpoint.getClientPort()).thenReturn(1);

        handler.handleInput(new InputEvent(connectionEndpoint, new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(new byte[0])));

        final UserEvent newCon = new UserEvent(false, "testName");
        handler.sendAllObj(newCon);
        Mockito.verify(connectionEndpoint).send(newCon);
    }

    @Test
    public void testHandleInputChallengeData() {
        when(connectionEndpoint.getClientPort()).thenReturn(1);

        final Challenge challenge = Mockito.mock(Challenge.class);
        handler.handleInput(new InputEvent(connectionEndpoint, new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(new byte[0])));
        Mockito.verify(handler).sendToOneStr(connectionEndpoint, "testName", challenge.roll());
    }

    @Test
    public void testHandleInputChallReqData() {
        final ConnectionEndpoint con = mock(ConnectionEndpoint.class);
        final ConnectionEndpoint targetConnection = mock(ConnectionEndpoint.class);
        final String targetName = "targetName";
        final ChallReq challReq = Mockito.mock(ChallReq.class);
        when(challReq.getTo()).thenReturn(targetName);

        handler.handleInput(new InputEvent(con, new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(new byte[0])));
        handler.clients.addElement(new Entry());
        handler.clients.addElement(new Entry());

        handler.clients.get(0).ce = targetConnection;
        handler.clients.get(0).name = targetName;

        handler.handleInput(new InputEvent(con, new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(new byte[0])));
        handler.clients.get(1).ce.send(challReq);
    }

    @Test
    public void testHandleInputHeroData() {
        when(connectionEndpoint.getClientPort()).thenReturn(1);

        handler.handleInput(new InputEvent(connectionEndpoint, new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(new byte[0])));
        handler.clients.addElement(new Entry());

        handler.clients.get(0).name = "testName";
        handler.updateHero(connectionEndpoint, hero);

        assertEquals("testName", hero.getName());
    }

    @Test
    public void testHandleInputGmConnection() {
        when(connectionEndpoint.getClientPort()).thenReturn(1);

        handler.handleInput(new InputEvent(connectionEndpoint, new ByteArrayInputStream(new byte[0]), new ByteArrayOutputStream(new byte[0])));

        userEvent(connectionEndpoint, "Gamemaster");

        assertEquals("Gamemaster", connectionEndpoint.getName());
    }

    @Test
    public void testSendAllSystemMsg() {
        final String message = "testMessage";

        handler.sendAllSystemMsg(message);

        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                handler.clients.elementAt(i).ce.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testWhisper() {
        when(connectionEndpoint.getClientPort()).thenReturn(1);

        final String from = "fromName";
        final String to = "toName";
        final String message = "testMessage";

        handler.whisper(connectionEndpoint, from + " " + to + " " + message);

        handler.clients.get(0).name = to;
        handler.sendOneSystemMsg(connectionEndpoint, from + " whispers: " + message);
    }

    @Test
    public void testRollDice() {
        when(connectionEndpoint.getClientPort()).thenReturn(1);

        final String rollCommand = "1d20";

        handler.rollDice(connectionEndpoint, rollCommand);

        assertEquals(handler.clients.size(), 2);

        final String expectedOutput = handler.getName(connectionEndpoint) + " rolled 1d20 --> ";
        String output = "";

        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                output += handler.clients.elementAt(i).ce.receiveLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assertEquals(expectedOutput, output);
        }
    }

    @Test
    public void testRollDiceWithMod() {
        when(connectionEndpoint.getClientPort()).thenReturn(1);

        final String rollCommand = "3d6+2";

        handler.rollDice(connectionEndpoint, rollCommand);

        assertEquals(handler.clients.size(), 2);

        final String expectedOutput = handler.getName(connectionEndpoint) + " rolled 3d6+2 --> ";
        String output = "";

        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                output += handler.clients.elementAt(i).ce.receiveLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assertEquals(expectedOutput, output);
        }
    }

    @Test
    public void testSendAllStr() {
        when(connectionEndpoint.getClientPort()).thenReturn(1);

        final String message = "testMessage";

        handler.sendAllStr(connectionEndpoint, message);

        for (int i = 0; i < handler.clients.size(); ++i) {
            try {
                handler.clients.elementAt(i).ce.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testSendHeros() {
        when(connectionEndpoint.getClientPort()).thenReturn(1);

        final Vector<Hero> heroes = new Vector<>();
        heroes.add(hero);

        handler.sendHeros(connectionEndpoint);

        handler.clients.get(0).ce.send(heroes);
    }

    @Test
    public void testIsGmConnected() {
        for (int i = 0; i < handler.clients.size(); ++i) {
            handler.clients.elementAt(i).name = "Gamemaster";
        }

        assertEquals(true, handler.isGmConnected());
    }

    @Test
    public void testIsGmConnectedFalse() {
        for (int i = 0; i < handler.clients.size(); ++i) {
            handler.clients.elementAt(i).name = "testName";
        }

        assertEquals(false, handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() {
        final ConnectionEndpoint con = mock(ConnectionEndpoint.class);
        final String message = "testMessage";

        handler.sendOneSystemMsg(con, message);

        try {
            con.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateHero() {
        final ConnectionEndpoint con = mock(ConnectionEndpoint.class);

        handler.updateHero(con, hero);

        assertNull(handler.getName(con));
    }

    @Test
    public void testUpdateHeroWithName() {
        final ConnectionEndpoint con = mock(ConnectionEndpoint.class);

        handler.addConnection(con);
        handler.clients.get(0).name = "testName";

        handler.updateHero(con, hero);

        assertEquals("testName", handler.getName(con));
    }

    private void userEvent(ConnectionEndpoint connectionEndpoint, String name) {
        final UserEvent newCon = new UserEvent(false, name);
        handler.sendAllObj(newCon);
        Mockito.when(connectionEndpoint.getName()).thenReturn(name);
    }
}