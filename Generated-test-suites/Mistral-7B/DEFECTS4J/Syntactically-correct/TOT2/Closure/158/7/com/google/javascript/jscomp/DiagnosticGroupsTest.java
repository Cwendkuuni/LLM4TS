import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    assertNotNull(registeredGroups);
    // Add a few groups for testing
    diagnosticGroups.registerGroup("testGroup1", new DiagnosticGroup("Test Group 1"));
    diagnosticGroups.registerGroup("testGroup2", new DiagnosticGroup("Test Group 2"));

    List<String> groupNames = List.of("testGroup1", "testGroup2");
    Map<String, DiagnosticGroup> expectedGroups = Maps.newHashMap();
    expectedGroups.put("testGroup1", new DiagnosticGroup("Test Group 1"));
    expectedGroups.put("testGroup2", new DiagnosticGroup("Test Group 2"));

    Map<String, DiagnosticGroup> actualGroups = diagnosticGroups.getRegisteredGroups();

    assertEquals(expectedGroups, actualGroups);
  }
}