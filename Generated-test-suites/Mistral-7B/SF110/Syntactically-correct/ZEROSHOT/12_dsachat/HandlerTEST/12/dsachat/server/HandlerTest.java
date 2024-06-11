package dsachat.server.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import dsachat.server.Handler;
import dsachat.server.Entry;
import dsachat.share.ConnectionEndpoint;
import dsachat.share.hero.Hero;
import dsachat.share.UserEvent;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint connection;
    private ByteArrayOutputStream outContent;
    private ByteArrayInputStream inContent;
    private Socket socket;
    private PrintWriter printWriter;

    @Before
    public void setUp() {
        handler = new Handler();
        connection = new ConnectionEndpoint();
        outContent = new ByteArrayOutputStream();
        inContent = new ByteArrayInputStream(new byte[0]);
        socket = new Socket("localhost", 0);
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        connection.setClientPort(socket.getLocalPort());
        connection.setSocket(socket);
        connection.setInput(new InputStream() {
            private ByteArrayInputStream inputStream = inContent;

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public int available() throws IOException {
                return inputStream.available();
            }

            @Override
            public synchronized void close() throws IOException {
                inputStream.close();
            }

            @Override
            public synchronized void mark(int readlimit) {
                inputStream.mark(readlimit);
            }

            @Override
            public boolean markSupported() {
                return inputStream.markSupported();
            }
        });
        handler.addConnection(connection);
    }

    @Test
    public void testAddConnection() {
        assertEquals(1, handler.clients.size());
    }

    @Test
    public void testRemoveConnection() {
        handler.removeConnection(connection);
        assertEquals(0, handler.clients.size());
    }

    @Test
    public void testHandleInput_String() throws IOException {
        String message = "hello";
        handler.handleInput(new InputEvent(connection, message));
        assertTrue(handler.sendAllSystemMsgCalledWith("*Gamemaster: hello"));;
    }

    @Test
    public void testHandleInput_String_CommandNotFound() throws IOException {
        String message = "/nonexistentcommand";
        handler.handleInput(new InputEvent(connection, message));
        assertTrue(handler.sendAllSystemMsgCalledWith("command /nonexistentcommand not found"));
    }

    @Test
    public void testHandleInput_Challenge() throws IOException {
        Hero hero = new Hero("test", 10, 10, 10, 10, 10, 10, 10, 10, 10);
        Challenge challenge = new Challenge(hero, null, null);

        handler.handleInput(new InputEvent(connection, challenge));

        String sender = this.getName(connection);
        String expected = sender + " rolled " + challenge.getCommand() + " --> ";

        for (int i = 0; i < challenge.getCount(); ++i) {
            expected += Challenge.dice(challenge.getSides()) + "  ";
        }

        assertTrue(handler.sendAllSystemMsgCalledWith(expected));

        if (challenge.isSilent()) {
            assertTrue(handler.sendAllCalledWith(expected));
        }

        if (!challenge.getTo().isEmpty()) {
            String recipient = "recipient";
            handler.handleInput(new InputEvent(connection, new UserEvent(true, recipient)));
            String recipientName = this.getName(new ConnectionEndpoint(recipient, 0));
            assertTrue(handler.sendToOneSystemMsgCalledWith(connection, recipientName, expected));
        }
    }

    @Test
    public void testHandleInput_ChallReq() throws IOException {
        String recipient = "recipient";
        ChallReq challReq = new ChallReq("/rollDice", recipient);

        handler.handleInput(new InputEvent(connection, challReq));

        String expected = "*Gamemaster: recipient rolled dice --> ";
        for (int i = 0; i < challReq.getCount(); ++i) {
            expected += Challenge.dice(challReq.getSides()) + "  ";
        }

        assertTrue(handler.sendAllSystemMsgCalledWith(expected));
        assertTrue(handler.sendToOneCalledWith(recipient, expected));
    }

    @Test
    public void testHandleInput_Hero() throws IOException {
        Hero hero = new Hero("test", 10, 10, 10, 10, 10, 10, 10, 10, 10);

        handler.handleInput(new InputEvent(connection, hero));

        String expected = "*" + this.getName(connection) + ": updated his hero";
        assertTrue(handler.sendAllSystemMsgCalledWith(expected));

        Hero updatedHero = handler.updateHero(connection, hero);
        assertEquals(hero, updatedHero);

        String name = this.getNameUpdate(connection);
        assertEquals(hero.getName(), name);
    }

    @Test
    public void testHandleInput_UserEvent_GmConnected() throws IOException {
        UserEvent userEvent = new UserEvent(true, "Gamemaster");

        handler.handleInput(new InputEvent(connection, userEvent));

        String expected = "Gamemaster connected";
        assertTrue(handler.sendAllObjectCalledWith(userEvent));
        assertTrue(handler.sendAllSystemMsgCalledWith(expected));
    }

    @Test
    public void testSendHeros() throws IOException {
        Hero hero1 = new Hero("hero1", 1, 1, 1, 1, 1, 1, 1, 1, 1);
        Hero hero2 = new Hero("hero2", 2, 2, 2, 2, 2, 2, 2, 2, 2);

        handler.sendHeros(connection);

        Object received = new ObjectInputStream(connection.getInputStream()).readObject();

        assertTrue(received instanceof Vector);

        Vector<Hero> heroes = (Vector<Hero>) received;
        assertEquals(2, heroes.size());
        assertEquals(hero1, heroes.get(0));
        assertEquals(hero2, heroes.get(1));
    }

    @Test
    public void testIsGmConnected() {
        assertTrue(handler.isGmConnected());
        handler.handleInput(new InputEvent(connection, new UserEvent(true, "Gamemaster")));
        assertTrue(handler.isGmConnected());
        handler.handleInput(new InputEvent(connection, new UserEvent(false, "Gamemaster")));
        assertFalse(handler.isGmConnected());
    }

    @Test
    public void testSendOneSystemMsg() throws IOException {
        String message = "test message";

        handler.sendOneSystemMsg(connection, message);

        assertTrue(handler.sendAllSystemMsgCalledWith(message));
    }

    @Test
    public void testSendAllSystemMsg() {
        String message = "test message";

        handler.sendAllSystemMsg(message);

        for (int i = 0; i < handler.clients.size(); ++i) {
            String clientName = this.getName(handler.clients.get(i).ce);
            assertTrue(handler.sendAllSystemMsgCalledWith(clientName + ": " + message));
        }
    }

    @Test
    public void testWhisper() throws IOException {
        String sender = "sender";
        String recipient = "recipient";
        String message = "test message";

        handler.handleInput(new InputEvent(connection, new UserEvent(true, recipient)));
        handler.handleInput(new InputEvent(new ConnectionEndpoint(sender, 0), new UserEvent(true, sender)));

        handler.whisper(connection, recipient + " " + message);

        String senderName = this.getName(connection);
        assertEquals(senderName, sender);

        String recipientName = this.getName(new ConnectionEndpoint(recipient, 0));
        assertEquals(recipientName, recipient);

        String expected = senderName + " whispers: " + message;
        assertTrue(handler.sendOneSystemMsgCalledWith(recipient, expected));
        assertTrue(handler.sendAllSystemMsgCalledWith(expected));
    }

    @Test
    public void testRollDice() throws IOException {
        String sender = "sender";
        String message = "3d10+2";

        handler.handleInput(new InputEvent(connection, new UserEvent(true, sender)));
        handler.handleInput(new InputEvent(connection, message));

        int total = 0;
        for (int i = 0; i < 3; ++i) {
            total += Challenge.dice(10);
        }
        total += 2;

        String expected = sender + " rolled " + message + " --> " + total;
        assertTrue(handler.sendAllSystemMsgCalledWith(expected));
    }

    @Test
    public void testSendOne() throws IOException {
        String sender = "sender";
        String recipient = "recipient";
        String message = "test message";

        handler.handleInput(new InputEvent(connection, new UserEvent(true, recipient)));
        handler.handleInput(new InputEvent(new ConnectionEndpoint(sender, 0), new UserEvent(true, sender)));

        handler.sendOne(connection, recipient, message);

        String recipientName = this.getName(new ConnectionEndpoint(recipient, 0));
        assertEquals(recipientName, recipient);

        String expected = sender + ": " + message;
        assertTrue(handler.sendToOneSystemMsgCalledWith(connection, recipientName, expected));
        assertTrue(handler.sendAllSystemMsgCalledWith(expected));
    }

    @Test
    public void testSendAll() throws IOException {
        String sender = "sender";
        String message = "test message";

        handler.handleInput(new InputEvent(connection, new UserEvent(true, sender)));

        handler.sendAll(message);

        for (int i = 0; i < handler.clients.size(); ++i) {
            String clientName = this.getName(handler.clients.get(i).ce);
            assertEquals(clientName, sender);
            String expected = sender + ": " + message;
            assertTrue(handler.sendAllSystemMsgCalledWith(clientName, expected));
        }
    }

    @Test
    public void testGetName() {
        String name = this.getName(connection);
        assertNull(name);
    }

    @Test
    public void testGetNameUpdate() throws IOException {
        Hero hero = new Hero("test", 1, 1, 1, 1, 1, 1, 1, 1, 1);

        handler.handleInput(new InputEvent(connection, hero));

        String name = this.getNameUpdate(connection);
        assertEquals(hero.getName(), name);
    }

    @Test
    public void testSendAllCalledWith() {
        String message = "test message";

        handler.sendAll(message);

        for (int i = 0; i < handler.clients.size(); ++i) {
            String clientName = this.getName(handler.clients.get(i).ce);
            assertTrue(handler.sendAllCalledWith(clientName, message));
        }
    }

    @Test
    public void testSendToOneCalledWith() {
        String sender = "sender";
        String recipient = "recipient";
        String message = "test message";

        handler.handleInput(new InputEvent(new ConnectionEndpoint(recipient, 0), new UserEvent(true, recipient)));
        handler.handleInput(new InputEvent(new ConnectionEndpoint(sender, 0), new UserEvent(true, sender)));

        handler.sendToOne(connection, recipient, message);

        String recipientName = this.getName(new ConnectionEndpoint(recipient, 0));
        assertEquals(recipientName, recipient);

        assertTrue(handler.sendToOneCalledWith(connection, recipientName, message));
        assertTrue(handler.sendAllSystemMsgCalledWith(message));
    }

    @Test
    public void testSendSystemMsgCalledWith() {
        String message = "test message";

        handler.sendSystemMsg(message);

        for (int i = 0; i < handler.clients.size(); ++i) {
            String clientName = this.getName(handler.clients.get(i).ce);
            assertTrue(handler.sendAllSystemMsgCalledWith(clientName, message));
        }
    }

    private boolean sendAllCalledWith(Object obj, String expected) {
        for (int i = 0; i < handler.clients.size(); ++i) {
            if (handler.clients.get(i).ce.equals(connection)) {
                String actual = handler.sendAllCalledArgs[i];
                return actual.equals(expected);
            }
        }
        return false;
    }

    private boolean sendToOneCalledWith(Object obj, String sender, String expected) {
        if (sender != null) {
            for (int i = 0; i < handler.clients.size(); ++i) {
                if (handler.clients.get(i).name.equals(sender)) {
                    String actual = handler.sendToOneCalledArgs[i];
                    return actual.equals(expected);
                }
            }
        }
        for (int i = 0; i < handler.clients.size(); ++i) {
            if (handler.clients.get(i).ce.equals(connection)) {
                String actual = handler.sendToOneCalledArgs[i];
                return actual.startsWith(expected);
            }
        }
        return false;
    }

    private boolean sendAllSystemMsgCalledWith(String expected) {
        for (int i = 0; i < handler.sendAllSystemMsgCalledArgs.length; ++i) {
            if (handler.sendAllSystemMsgCalledArgs[i].equals(expected)) {
                return true;
            }
        }
        return false;
    }

    private boolean sendAllSystemMsgCalledWith(Object sender, String expected) {
        for (int i = 0; i < handler.sendAllSystemMsgCalledArgs.length; ++i) {
            if (handler.sendAllSystemMsgCalledArgs[i].startsWith(sender + ": " + expected)) {
                return true;
            }
        }
        return false;
    }

    private boolean sendOneSystemMsgCalledWith(String expected) {
        for (int i = 0; i < handler.sendOneSystemMsgCalledArgs.length; ++i) {
            if (handler.sendOneSystemMsgCalledArgs[i].equals(expected)) {
                return true;
            }
        }
        return false;
    }

    private String getName() {
        for (int i = 0; i < handler.clients.size(); ++i) {
            if (handler.clients.get(i).ce.equals(connection)) {
                return handler.clients.get(i).name;
            }
        }
        return null;
    }

    private String getNameUpdate(ConnectionEndpoint connection) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
        output.writeObject(new UserEvent(true, ""));
        ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
        return (String) input.readObject();
    }
}

class Entry {
    ConnectionEndpoint ce;
    String name;
    Hero hero;
}