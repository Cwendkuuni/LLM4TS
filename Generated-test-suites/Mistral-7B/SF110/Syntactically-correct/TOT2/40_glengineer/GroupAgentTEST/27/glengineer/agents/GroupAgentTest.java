import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent();
        Agent child1 = new DummyAgent("John", "Doe");
        Agent child2 = new DummyAgent("Jane", "Doe");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        String expected = child1.getFirstName();
        String actual = groupAgent.getFirstName();
        assertEquals(expected, actual);
    }
}

// DummyAgent is a placeholder class for the actual Agent type, assume it has a getFirstName() method that returns the first name string