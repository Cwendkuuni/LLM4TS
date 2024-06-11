import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.DiagnosticsCategory;

import java.util.Arrays;
import java.util.List;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    List<String> expectedGroups = Arrays.asList("accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes", "checkVars", "constantProperty", "deprecated", "externsValidation", "fileoverviewTags", "globalThis", "internetExplorerChecks", "invalidCasts", "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "uselessCode", "visibility");

    assertEquals(expectedGroups.size(), registeredGroups.size());
    assertTrue(registeredGroups.containsKey("accessControls"));
    // Add more assertions for other groups
  }

  @org.junit.Test
  public void testGetDiagnosticGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");

    assertNotNull(deprecatedGroup);
    assertTrue(deprecatedGroup.getName().equals("deprecated"));
    // Add more assertions for other groups
  }

  @org.junit.Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("checkTypes", "checkVars");
    DiagnosticGroups diagnosticGroupsObj = new DiagnosticGroups();

    diagnosticGroupsObj.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

    DiagnosticGroup checkTypesGroup = diagnosticGroupsObj.forName("checkTypes");
    DiagnosticGroup checkVarsGroup = diagnosticGroupsObj.forName("checkVars");

    assertEquals(CheckLevel.WARNING, options.getWarningLevel(checkTypesGroup));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(checkVarsGroup));
  }

  @org.junit.Test
  public void testGetWarningLevelForUnregisteredGroup() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    assertNull(diagnosticGroups.forName("nonexistentGroup"));
    assertEquals(CompilerOptions.WarningLevel.OFF, options.getWarningLevel(diagnosticGroups.forName("nonexistentGroup")));
  }
}