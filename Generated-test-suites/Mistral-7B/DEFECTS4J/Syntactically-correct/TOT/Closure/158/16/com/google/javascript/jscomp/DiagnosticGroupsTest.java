import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    // Test for registered groups
    assertNotNull(registeredGroups.get("accessControls"));
    assertNotNull(registeredGroups.get("ambiguousFunctionDecl"));
    assertNotNull(registeredGroups.get("checkRegExp"));
    // Add more assertions for other groups

  }

  @Test
  public void testGetDiagnosticGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup diagnosticGroup = diagnosticGroups.forName("accessControls");
    assertNotNull(diagnosticGroup);
  }

  @Test(expected = NullPointerException.class)
  public void testGetNullDiagnosticGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup diagnosticGroup = diagnosticGroups.forName(null);
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    String[] diagnosticGroupsNames = {"checkRegExp", "checkTypes"};
    diagnosticGroups.setWarningLevels(options, List.of(diagnosticGroupsNames[0], diagnosticGroupsNames[1]), CheckLevel.WARNING);
    // Add more assertions to check warning levels
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWarningLevelsWithInvalidDiagnosticGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    String invalidDiagnosticGroupName = "nonExistentDiagnosticGroupName";
    diagnosticGroups.setWarningLevels(options, List.of(invalidDiagnosticGroupName), CheckLevel.ERROR);
  }
}