import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import glengineer.agents.GroupAgent;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            public List<Agent> getChildren() {
                LinkedList<Agent> children = new LinkedList<>();
                children.add(new Agent() {
                    @Override
                    public String getFirstName() {
                        return "John";
                    }
                });
                return children;
            }
        };
        String firstName = groupAgent.getFirstName();
        assertNotNull(firstName);
        assertEquals("John", firstName);
    }
}