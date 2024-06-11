import org.junit.Test;
import static org.junit.Assert.*;
import ioproject.server.network.ClientGroup;
import ioproject.common.network.Client;

public class ClientGroupTest {
    @Test
    public void testAdd() {
        ClientGroup clientGroup = new ClientGroup();
        Client client = new Client();
        assertTrue(clientGroup.add(client));
    }
}