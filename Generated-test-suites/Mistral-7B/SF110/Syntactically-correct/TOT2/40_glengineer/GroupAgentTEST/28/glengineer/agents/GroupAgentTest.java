import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent();
        Agent child1 = new Agent();
        child1.setFirstName("John");
        groupAgent.addAgent(child1);

        String firstName = groupAgent.getFirstName();
        assertEquals("John", firstName);

        Agent child2 = new Agent();
        groupAgent.addAgent(child2);

        firstName = groupAgent.getFirstName();
        assertNull(firstName);
    }
}