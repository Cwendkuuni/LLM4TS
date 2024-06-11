import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.agents.*;
import java.util.*;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent();
        Agent child1 = new DummyAgent("John", "Doe");
        Agent child2 = new DummyAgent("Jane", "Doe");
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        String expected = "John";
        String result = groupAgent.getFirstName();
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFirstName_EmptyGroupAgent() {
        GroupAgent groupAgent = new GroupAgent();
        String result = groupAgent.getFirstName();
        assertNull(result);
    }

    @Test
    public void testAddAgent_NullAgent() {
        GroupAgent groupAgent = new GroupAgent();
        assertThrows(IllegalArgumentException.class, () -> groupAgent.addAgent(null));
    }

    @Test
    public void testGetFirstName_EmptyList() {
        GroupAgent groupAgent = new GroupAgent();
        String result = groupAgent.getFirstName();
        assertNull(result);
    }
}

class DummyAgent extends Agent {
    public DummyAgent(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }
}