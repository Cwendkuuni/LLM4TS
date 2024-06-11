import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent() {
            private List<Agent> childrenAgents = new ArrayList<Agent>() {{
                add(new Agent() {
                    @Override
                    public String getFirstName() {
                        return "John";
                    }
                });
            }};

            @Override
            public String getFirstName() {
                return super.getFirstName();
            }
        };

        assertEquals("John", groupAgent.getFirstName());
    }
}