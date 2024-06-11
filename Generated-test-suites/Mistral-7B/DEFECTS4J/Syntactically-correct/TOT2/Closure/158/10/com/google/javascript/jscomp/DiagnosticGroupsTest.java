import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.DiagnosticGroups;
import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> expectedGroups = Maps.newHashMap();
    expectedGroups.put("GLOBAL_THIS", DiagnosticGroups.GLOBAL_THIS);
    expectedGroups.put("DEPRECATED", DiagnosticGroups.DEPRECATED);
    expectedGroups.put("VISIBILITY", DiagnosticGroups.VISIBILITY);
    // Add other groups here

    Map<String, DiagnosticGroup> actualGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(expectedGroups, actualGroups);
  }
}