import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.CompilerOptions;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    // Test getting registered diagnostic groups
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(23, registeredGroups.size());

    // Test getting diagnostic group by name
    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecatedGroup);
    assertEquals("deprecated", deprecatedGroup.getName());

    DiagnosticGroup checkTypesGroup = diagnosticGroups.forName("checkTypes");
    assertNotNull(checkTypesGroup);
    assertEquals("checkTypes", checkTypesGroup.getName());
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    // Set warning level for a specific diagnostic group
    List<String> diagnosticGroupsToSet = ImmutableList.of("checkTypes");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToSet, CheckLevel.WARNING);

    // Check that the warning level for the specified diagnostic group is set to WARNING
    assertTrue(options.isWarningEnabled(checkTypesGroup));

    // Set warning level for multiple diagnostic groups
    List<String> diagnosticGroupsToSetMultiple = ImmutableList.of("deprecated", "checkTypes");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToSetMultiple, CheckLevel.ERROR);

    // Check that the warning level for the specified diagnostic groups are set to ERROR
    assertTrue(options.isErrorEnabled(deprecatedGroup));
    assertTrue(options.isErrorEnabled(checkTypesGroup));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWarningLevelsWithInvalidGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    // Set warning level for an invalid diagnostic group
    List<String> diagnosticGroupsToSetInvalid = ImmutableList.of("invalidGroup");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsToSetInvalid, CheckLevel.ERROR);
  }

  @Test
  public void testForNameWithInvalidGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test getting diagnostic group by name for an invalid group
    DiagnosticGroup invalidGroup = diagnosticGroups.forName("invalidGroup");
    assertNull(invalidGroup);
  }
}