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

        String result = groupAgent.getFirstName();
        assertEquals("John", result);
    }
}

class DummyAgent extends Agent {
    private String firstName;
    private String lastName;

    public DummyAgent(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }
}