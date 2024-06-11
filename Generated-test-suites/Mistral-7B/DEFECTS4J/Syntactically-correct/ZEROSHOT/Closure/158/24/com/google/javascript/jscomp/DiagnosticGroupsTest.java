import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CheckerFactory.CheckerNames;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();

    List<String> expectedGroups = ImmutableList.of("accessControls",
        "ambiguousFunctionDecl", "checkRegExp", "checkTypes",
        "constantProperty", "deprecated", "externsValidation",
        "fileoverviewTags", "globalThis", "internetExplorerChecks",
        "invalidCasts", "missingProperties", "nonStandardJsDocs",
        "strictModuleDepCheck", "typeInvalidation", "undefinedVars",
        "uselessCode", "visibility", "tweakValidation");

    for (String groupName : expectedGroups) {
      DiagnosticGroup group = groups.get(groupName);
      assertNotNull(group);
    }
  }

  @Test
  public void testGetGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();

    for (String groupName : groups.keySet()) {
      DiagnosticGroup group = diagnosticGroups.forName(groupName);
      assertNotNull(group);
      assertSame(group, groups.get(groupName));
    }

    assertNull(diagnosticGroups.forName("unknownGroup"));
  }

  @Test
  public void testRegisteringGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String customGroup1 = "customGroup1";
    DiagnosticType customType1 = new DiagnosticType(CheckerNames.CHECK_EXPR, 1);
    String customGroup2 = "customGroup2";

    DiagnosticGroup customGroup1Group = diagnosticGroups.registerGroup(
        customGroup1, customType1);
    DiagnosticGroup customGroup2Group = diagnosticGroups.registerGroup(
        customGroup2, customGroup1Group);

    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();

    List<String> expectedGroups = ImmutableList.of("accessControls",
        "ambiguousFunctionDecl", "checkRegExp", "checkTypes",
        "constantProperty", "customGroup1", "customGroup2",
        "deprecated", "externsValidation", "fileoverviewTags",
        "globalThis", "internetExplorerChecks", "invalidCasts",
        "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck",
        "typeInvalidation", "undefinedVars", "uselessCode", "visibility",
        "tweakValidation");

    for (String groupName : expectedGroups) {
      DiagnosticGroup group = groups.get(groupName);
      assertNotNull(group);
    }

    assertSame(customGroup1Group, groups.get(customGroup1));
    assertSame(customGroup2Group, groups.get(customGroup2));
  }
}