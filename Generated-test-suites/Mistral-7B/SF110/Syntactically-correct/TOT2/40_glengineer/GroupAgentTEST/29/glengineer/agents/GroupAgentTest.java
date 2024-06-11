import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.agents.Agent;
import glengineer.agents.GroupAgent;

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

    @Test
    public void testGetFirstName_EmptyList() {
        GroupAgent agent = new GroupAgent();

        String result = agent.getFirstName();
        assertEquals(null, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFirstName_AllChildrenHaveNullFirstName() {
        GroupAgent agent = new GroupAgent();

        Agent child1 = new Agent();
        child1.setFirstName(null);
        agent.addAgent(child1);

        Agent child2 = new Agent();
        child2.setFirstName(null);
        agent.addAgent(child2);

        String result = agent.getFirstName();
    }

    // Add test cases for other methods
}