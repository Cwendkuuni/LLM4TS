import org.junit.Test;
import static org.junit.Assert.*;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.addAgent(new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }

            @Override
            public String getLastName() {
                return "Doe";
            }
        });
        String result = groupAgent.getFirstName();
        assertTrue(result.equals("John"));
    }
}