import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;

import java.util.Arrays;
import java.util.List;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisterAndGetGroups() {
    // Test registration and retrieval of diagnostic groups
    DiagnosticGroup globalThisGroup = registerGroup("globalThis");
    assertEquals("globalThis", globalThisGroup.getName());

    DiagnosticGroup deprecatedGroup = registerGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);
    assertEquals("deprecated", deprecatedGroup.getName());

    DiagnosticGroup accessControlsGroup = registerGroup("accessControls", DEPRECATED, VISIBILITY);
    assertEquals("accessControls", accessControlsGroup.getName());

    Map<String, DiagnosticGroup> registeredGroups = getRegisteredGroups();
    assertEquals(9, registeredGroups.size()); // 9 groups are defined in the code
    DiagnosticGroup group = registeredGroups.get("accessControls");
    assertEquals(ACCESS_CONTROLS, group);
  }

  @org.junit.Test
  public void testForName() {
    // Test retrieval of diagnostic group by name
    DiagnosticGroup accessControlsGroup = forName("accessControls");
    assertEquals(ACCESS_CONTROLS, accessControlsGroup);

    try {
      forName("nonExistentGroup");
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testSetWarningLevels() {
    // Test setting warning levels for registered diagnostic groups
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("accessControls", "checkTypes");
    DiagnosticGroups diagnostics = new DiagnosticGroups();

    diagnostics.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

    DiagnosticGroup accessControlsGroup = forName("accessControls");
    DiagnosticGroup checkTypesGroup = forName("checkTypes");

    List<DiagnosticType> accessControlsDiagnostics = accessControlsGroup.getDiagnostics();
    List<DiagnosticType> checkTypesDiagnostics = checkTypesGroup.getDiagnostics();

    assertTrue(options.getWarningLevel(accessControlsGroup) == CheckLevel.WARNING);
    assertTrue(options.getWarningLevel(checkTypesGroup) == CheckLevel.WARNING);

    for (DiagnosticType diagnostic : accessControlsDiagnostics) {
      assertTrue(options.isWarningEnabled(diagnostic));
    }

    for (DiagnosticType diagnostic : checkTypesDiagnostics) {
      assertTrue(options.isWarningEnabled(diagnostic));
    }
  }
}