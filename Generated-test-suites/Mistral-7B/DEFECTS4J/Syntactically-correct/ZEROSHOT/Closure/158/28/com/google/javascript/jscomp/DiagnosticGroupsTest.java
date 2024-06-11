import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import java.util.List;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    List<String> expectedDiagnosticGroupNames = List.of("accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes", "constantProperty", "deprecated", "externsValidation", "fileoverviewTags", "globalThis", "internetExplorerChecks", "invalidCasts", "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "uselessCode", "visibility");

    assertEquals(expectedDiagnosticGroupNames.size(), registeredGroups.size());

    for (String diagnosticGroupName : expectedDiagnosticGroupNames) {
      DiagnosticGroup group = registeredGroups.get(diagnosticGroupName);
      assertNotNull(group);
      assertEquals(diagnosticGroupName, group.getName());
    }
  }

  @Test
  public void testFindGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup accessControlsGroup = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");

    assertNotNull(accessControlsGroup);
    assertNotNull(deprecatedGroup);

    assertEquals("accessControls", accessControlsGroup.getName());
    assertEquals("deprecated", deprecatedGroup.getName());
  }

  @Test(expected = NullPointerException.class)
  public void testNullGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }
}