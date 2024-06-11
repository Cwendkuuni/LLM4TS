import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.agents.GroupAgent;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            public List<Agent> getChildren() {
                return Arrays.asList(new Agent() {
                    @Override
                    public String getFirstName() {
                        return "John";
                    }
                }, new Agent() {
                    @Override
                    public String getFirstName() {
                        return "Mike";
                    }
                });
            }
        };
        assertEquals("John", groupAgent.getFirstName());
    }
}