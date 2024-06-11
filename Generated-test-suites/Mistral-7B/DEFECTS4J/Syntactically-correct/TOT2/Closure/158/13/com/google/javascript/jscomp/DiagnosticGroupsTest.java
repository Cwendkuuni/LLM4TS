import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup() {
        DiagnosticGroup newGroup = new DiagnosticGroup("TestGroup");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("TestGroup", newGroup);
        assertEquals(newGroup, DiagnosticGroups.forName("TestGroup"));
    }
}