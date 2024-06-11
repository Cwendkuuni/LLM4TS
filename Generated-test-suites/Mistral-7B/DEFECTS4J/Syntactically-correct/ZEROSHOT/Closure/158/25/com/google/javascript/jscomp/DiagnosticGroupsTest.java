import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class DiagnosticGroupsTest {

  private static DiagnosticGroups diagnosticGroups;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    List<String> expectedGroups = ImmutableList.of("accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes", "checkVars", "constantProperty", "deprecated", "externsValidation", "fileoverviewTags", "globalThis", "internetExplorerChecks", "invalidCasts", "missingProperties", "nonStandardJsDocs", "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "uselessCode", "visibility");

    assertTrue(registeredGroups.size() == expectedGroups.size());
    for (String groupName : expectedGroups) {
      DiagnosticGroup group = registeredGroups.get(groupName);
      assertNotNull(group);
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");

    assertNotNull(accessControls);
    assertNotNull(deprecated);

    List<DiagnosticType> deprecatedTypes = ImmutableList.of(CheckAccessControls.DEPRECATED_NAME, CheckAccessControls.DEPRECATED_NAME_REASON, CheckAccessControls.DEPRECATED_PROP, CheckAccessControls.DEPRECATED_PROP_REASON, CheckAccessControls.DEPRECATED_CLASS, CheckAccessControls.DEPRECATED_CLASS_REASON);

    assertTrue(deprecated.getDiagnosticTypes().containsAll(deprecatedTypes));
  }
}