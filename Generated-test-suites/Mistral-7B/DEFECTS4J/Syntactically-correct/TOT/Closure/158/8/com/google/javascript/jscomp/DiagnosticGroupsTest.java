import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;

import java.util.Arrays;
import java.util.List;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisterAndRetrieveDiagnosticGroups() {
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", DiagnosticType.WARNING);
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", DiagnosticType.ERROR);

    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    List<DiagnosticGroup> groups = Arrays.asList(group1, group2);

    assertEquals("Size should be equal", 2, registeredGroups.size());
    assertEquals("Retrieved groups should match registered groups", groups, registeredGroups.values());
  }

  @org.junit.Test
  public void testForName() {
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", DiagnosticType.WARNING);
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", DiagnosticType.ERROR);

    DiagnosticGroup retrievedGroup1 = DiagnosticGroups.forName("group1");
    DiagnosticGroup retrievedGroup2 = DiagnosticGroups.forName("group2");

    assertNotNull("Retrieved group should not be null", retrievedGroup1);
    assertNotNull("Retrieved group should not be null", retrievedGroup2);
    assertEquals("Retrieved groups should match registered groups", group1, retrievedGroup1);
    assertEquals("Retrieved groups should match registered groups", group2, retrievedGroup2);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups.forName(null);
  }

  @org.junit.Test
  public void testDiagnosticGroupNames() {
    List<String> diagnosticGroups = Arrays.asList(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp",
        "checkTypes", "checkVars", "constantProperty", "deprecated",
        "externsValidation", "fileoverviewTags", "globalThis",
        "internetExplorerChecks", "invalidCasts", "missingProperties",
        "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation",
        "undefinedVars", "unknownDefines", "uselessCode", "visibility"
    );

    assertEquals("Diagnostic group names should match the defined ones", diagnosticGroups, DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(", "));
  }
}