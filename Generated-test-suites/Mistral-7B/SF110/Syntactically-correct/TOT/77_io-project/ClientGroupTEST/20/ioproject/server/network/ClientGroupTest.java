import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {
    // 
    
    @Test
    public void testClientGroupConstructor() {
        ClientGroup clientGroup = new ClientGroup(null);
        assertNotNull(clientGroup);
        assertEquals(0, clientGroup.size());
    }

    // 
}