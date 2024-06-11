import org.junit.Test;
import static org.junit.Assert.*;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent();
        Agent child1 = new DummyAgent("John", "Doe");
        groupAgent.addAgent(child1);
        String result = groupAgent.getFirstName();
        assertEquals("John", result);
    }
}

class DummyAgent extends Agent {
    private final String firstName;
    private final String lastName;

    public DummyAgent(String firstName, String lastName) {
        super();
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