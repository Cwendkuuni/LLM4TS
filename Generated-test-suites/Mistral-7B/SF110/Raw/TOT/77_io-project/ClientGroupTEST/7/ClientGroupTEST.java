import org.junit.*;
import static org.junit.Assert.*;
import ioproject.server.network.*;
import java.util.*;

public class ClientGroupTest {

    

    private ClientGroup group;
    private Client client;

    @Before
    public void setUp() {
        this.group = new ClientGroup(null);
        this.client = new Client();
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.group);
        assertNull(this.group.parentGroup());
        assertNotNull(this.group.clients());
        assertFalse(this.group.contains(this.client));
    }

    @Test
    public void testContains() {
        this.group.add(this.client);
        assertTrue(this.group.contains(this.client));
    }

    @Test
    public void testDoesNotContainNonExistentClient() {
        assertFalse(this.group.contains(this.client));
    }

    
}