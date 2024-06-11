import org.junit.Test;
import static org.junit.Assert.*;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.AgentSetter;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent agent = new GroupAgent() {
            @Override
            Settings getSettings() {
                return new Settings();
            }
            @Override
            GroupCreatorAndElementAdder getGroupCreatorAndElementAdder() {
                return new AgentSetter();
            }
        };
        Agent child1 = new Agent() {
            @Override
            String getFirstName() {
                return "John";
            }
            @Override
            String getLastName() {
                return "Doe";
            }
        };
        agent.addAgent(child1);
        String result = agent.getFirstName();
        assertEquals("John", result);
    }
}