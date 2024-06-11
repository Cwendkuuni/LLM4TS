package ioproject.server.network.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.common.network.*;
import ioproject.server.network.*;
import java.util.*;

public class ClientGroupTest {

    ClientGroup parentGroup;
    ClientGroup clientGroup;
    Client client;
    ClientHandler handler;

    @Before
    public void setup() {
        parentGroup = new ClientGroup(null);
        clientGroup = new ClientGroup(parentGroup);
        client = new DummyClient();
        handler = new DummyClientHandler();
        parentGroup.add(clientGroup);
        clientGroup.addClientHandler(handler);
    }

    //Tests go here

    //...

    @Test
    public void testAddClientToEmptyGroupAndNotifyParent() {
        //Test implementation goes here
    }

    //...
}