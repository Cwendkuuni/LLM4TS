import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GroupAgentTest {
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent(); // create a new instance of GroupAgent
        String expectedFirstName = "John"; // an expected first name
        String actualFirstName = groupAgent.getFirstName(); // call the getFirstName() method
        assertEquals(expectedFirstName, actualFirstName); // check if the returned first name matches the expected one
    }
}