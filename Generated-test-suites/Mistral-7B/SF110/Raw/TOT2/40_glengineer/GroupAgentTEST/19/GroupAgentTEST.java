import static org.junit.Assert.assertEquals;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;

public class GroupAgentTest {
    @org.junit.Test
    public void testGetFirstName() {
        GroupAgent agent = new GroupAgent() {
            @Override
            List<Agent> getChildren() {
                return Arrays.asList(new Agent() {
                    @Override
                    public String getFirstName() {
                        return "John";
                    }
                }, new Agent() {
                    @Override
                    public String getFirstName() {
                        return "Jane";
                    }
                });
            }
        };
        assertEquals("John", agent.getFirstName());
    }
}