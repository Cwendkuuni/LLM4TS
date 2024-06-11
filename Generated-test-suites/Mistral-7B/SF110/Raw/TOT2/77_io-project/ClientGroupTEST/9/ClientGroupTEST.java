import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientGroupTest {
    @Test
    public void testNotifyMessageSent() {
        ClientGroup clientGroup = new ClientGroup(null);
        Client client = new Client();
        Object message = new Object();

        assertTrue(clientGroup.notifyMessageSent(client, message));
    }
}