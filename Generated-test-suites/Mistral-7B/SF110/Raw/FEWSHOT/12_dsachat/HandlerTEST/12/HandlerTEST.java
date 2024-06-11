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
import java.util.Vector;

import dsachat.share.hero.Hero;
import dsachat.share.hero.HeroFactory;
import dsachat.share.UserEvent;

import org.junit.Before;
import org.junit.Test;

public class HandlerTest {

    private Handler handler;
    private ConnectionEndpoint mockConnection;
    private Vector<Entry> mockClients;

    @Before
    public void setup() {
        handler = new Handler();
        mockConnection = mock(ConnectionEndpoint.class);
        mockClients = new Vector<Entry>();
        handler.clients = mockClients;
    }

    @Test
    public void testAddConnection() throws IOException {
        final ConnectionEndpoint cl = new ConnectionEndpoint("localhost", 8080);
        final Entry entry = new Entry();
        entry.ce = cl;
        entry.name = "";

        doReturn(true).when(cl).isConnected();
        handler.addConnection(cl);

        verify(cl).send(any(UserEvent.class));
        assertEquals(1, mockClients.size());
    }

    @Test(expected = IOException.class)
    public void testAddConnection_exception() throws IOException {
        final ConnectionEndpoint cl = new ConnectionEndpoint("localhost", 8080);
        handler.addConnection(cl);

        verify(cl, times(0)).send(any(UserEvent.class));
        throw new IOException();
    }

    @Test
    public void testRemoveConnection() {
        final ConnectionEndpoint cl = new ConnectionEndpoint("localhost", 8080);
        final String name = "testName";
        final Entry entry = new Entry();
        entry.ce = cl;
        entry.name = name;
        mockClients.add(entry);

        handler.removeConnection(cl);

        verify(mockClients, times(1)).removeElementAt(anyInt());
        assertEquals(0, mockClients.size());
    }

    @Test
    public void testRemoveConnection_null() {
        final ConnectionEndpoint cl = null;
        handler.removeConnection(cl);

        verify(mockClients, times(0)).removeElementAt(anyInt());
    }

    @Test
    public void testSendAllObj() throws IOException {
        final Object obj = new Object();
        handler.sendAllObj(obj);

        for (int i = 0; i < mockClients.size(); ++i) {
            verify(mockClients.elementAt(i).ce).send(eq(obj));
        }
    }

    @Test
    public void testSendAllStr() throws IOException {
        final String str = "TestString";
        handler.sendAllStr(mockConnection, str);

        for (int i = 0; i < mockClients.size(); ++i) {
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            try {
                pw.println(str);
                pw.flush();
            } finally {
                pw.close();
            }
            byte[] expected = writer.toString().getBytes();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);
            try {
                objOutStream.writeObject(new String(expected));
                objOutStream.flush();
            } finally {
                objOutStream.close();
            }

            byte[] actual = mockConnection.receive();
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testSendOneSystemMsg() {
        final String msg = "TestMessage";
        handler.sendOneSystemMsg(mockConnection, msg);

        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        try {
            pw.println(msg);
            pw.flush();
        } finally {
            pw.close();
        }

        byte[] expected = writer.toString().getBytes();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);
        try {
            objOutStream.writeObject(new String(expected));
            objOutStream.flush();
        } finally {
            objOutStream.close();
        }

        byte[] actual = mockConnection.receive();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testHandleInput_string() throws IOException {
        final ConnectionEndpoint con = mockConnection;
        final String msg = "/roll 3d6";
        handler.handleInput(new InputEvent(con, new String(msg.getBytes())));

        verify(con).send(anyString());
    }

    @Test
    public void testHandleInput_challenge() throws IOException {
        final ConnectionEndpoint con = mockConnection;
        final Challenge challenge = mock(Challenge.class);
        handler.handleInput(new InputEvent(con, challenge));

        verify(con).send(eq(challenge.roll()));
        verify(challenge).setTo(anyString());
    }

    @Test
    public void testHandleInput_challreq() throws IOException {
        final ConnectionEndpoint senderConnection = mockConnection;
        final ConnectionEndpoint receiverConnection = mockConnection;
        final String to = "testName";
        final String msg = "/challenge " + to + " TestMessage";
        final ChallReq challengeRequest = new ChallReq("TestMessage", to);

        doReturn(receiverConnection).when(handler).getClientByUsername(to);
        doReturn(senderConnection).when(handler).getConnectionByClientPort(receiverConnection.getClientPort());

        handler.handleInput(new InputEvent(senderConnection, new String(msg.getBytes())));

        verify(senderConnection).send(challengeRequest);
        verify(receiverConnection).send(challengeRequest);
    }

    @Test
    public void testHandleInput_hero() throws IOException {
        final ConnectionEndpoint con = mockConnection;
        final Hero hero = new HeroFactory().createHero("TestName");

        handler.handleInput(new InputEvent(con, hero));

        String name = handler.updateHero(con, hero);
        assertEquals(name, hero.getName());
        assertEquals(1, mockClients.size());
        assertEquals(hero.getName(), mockClients.get(0).name);
    }

    @Test
    public void testHandleInput_userEvent() throws IOException {
        final ConnectionEndpoint senderConnection = mockConnection;
        final String name = "TestName";
        final UserEvent userEvent = new UserEvent(true, name);

        doReturn(senderConnection).when(handler).getConnectionByClientPort(senderConnection.getClientPort());

        handler.handleInput(new InputEvent(senderConnection, userEvent));

        assertEquals(name, "Gamemaster");
    }

    @Test
    public void testWhisper() throws IOException {
        final ConnectionEndpoint senderConnection = mockConnection;
        final ConnectionEndpoint receiverConnection = mockConnection;
        final String to = "testName";
        final String msg = "TestMessage";

        doReturn(receiverConnection).when(handler).getClientByUsername(to);
        doReturn(senderConnection).when(handler).getConnectionByClientPort(receiverConnection.getClientPort());

        handler.handleInput(new InputEvent(senderConnection, new String("/whisper " + to + " " + msg)));

        verify(senderConnection).send(anyString());
        verify(receiverConnection).send(anyString());
    }
}