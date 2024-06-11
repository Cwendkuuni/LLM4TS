import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CompilerOptions;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    assertEquals("Size of registered groups should be 15", 15, registeredGroups.size());
    assertNotNull("globalThis group should be registered", registeredGroups.get("globalThis"));;
    // Add other assertions for other registered groups
  }

  @Test
  public void testGetGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("accessControls");

    assertNotNull("accessControls group should be registered", group);
    assertEquals("Size of accessControls group", 7, group.getDiagnostics().size());
    // Add other assertions to check the diagnostics in the accessControls group
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableList.of("accessControls", "invalidCasts");

    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);

    assertEquals("Warning level for accessControls group should be WARNING", CheckLevel.WARNING,
        options.getWarningLevel(diagnosticGroups.forName("accessControls")));
    assertEquals("Warning level for invalidCasts group should be WARNING", CheckLevel.WARNING,
        options.getWarningLevel(diagnosticGroups.forName("invalidCasts")));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetWarningLevelsInvalidGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableList.of("invalidGroup");

    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);

    // The test should fail with an IllegalArgumentException, as the 'invalidGroup' group does not exist
  }
}