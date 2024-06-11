import org.junit.Test;
import static org.junit.Assert.*;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent agent = new GroupAgent();
        Agent child1 = new Agent();
        child1.setFirstName("John");
        agent.addAgent(child1);
        String result = agent.getFirstName();
        assertEquals("John", result);
    }
}