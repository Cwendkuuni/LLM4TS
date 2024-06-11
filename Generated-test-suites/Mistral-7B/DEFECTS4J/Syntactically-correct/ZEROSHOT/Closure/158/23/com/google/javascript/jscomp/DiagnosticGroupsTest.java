import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterAndGetGroups() {
    DiagnosticGroup globalThisGroup = registerGroup("globalThis", CheckGlobalThis.GLOBAL_THIS);
    DiagnosticGroup deprecatedGroup = registerGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);
    DiagnosticGroup visibilityGroup = registerGroup("visibility",
        CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
        CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
        CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
        CheckAccessControls.PRIVATE_OVERRIDE,
        CheckAccessControls.VISIBILITY_MISMATCH);

    Map<String, DiagnosticGroup> registeredGroups = getRegisteredGroups();

    assertEquals(6, registeredGroups.size());
    assertTrue(registeredGroups.containsKey("globalThis"));
    assertTrue(registeredGroups.containsKey("deprecated"));
    assertTrue(registeredGroups.containsKey("visibility"));

    DiagnosticGroup group = registeredGroups.get("deprecated");
    assertEquals("deprecated", group.getName());

    List<DiagnosticType> types = group.getTypes();
    assertEquals(7, types.size());

    // Check some types
    DiagnosticType deprecatedNameType = types.get(0);
    assertEquals(CheckAccessControls.DEPRECATED_NAME, deprecatedNameType);
  }

  @Test
  public void testRegisterGroupWithTypes() {
    DiagnosticGroup constantPropertyGroup = registerGroup("constantProperty",
        CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE);

    Map<String, DiagnosticGroup> registeredGroups = getRegisteredGroups();

    assertEquals(7, registeredGroups.size());
    assertTrue(registeredGroups.containsKey("constantProperty"));

    DiagnosticGroup group = registeredGroups.get("constantProperty");
    assertEquals("constantProperty", group.getName());

    List<DiagnosticType> types = group.getTypes();
    assertEquals(1, types.size());

    DiagnosticType type = types.get(0);
    assertEquals(CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE, type);
  }

  @Test
  public void testRegisterGroupWithOtherGroups() {
    DiagnosticGroup accessControlsGroup = registerGroup("accessControls", DEPRECATED, VISIBILITY);

    Map<String, DiagnosticGroup> registeredGroups = getRegisteredGroups();

    assertEquals(8, registeredGroups.size());
    assertTrue(registeredGroups.containsKey("accessControls"));

    DiagnosticGroup group = registeredGroups.get("accessControls");
    assertEquals("accessControls", group.getName());

    List<DiagnosticGroup> groups = group.getGroups();
    assertEquals(2, groups.size());

    DiagnosticGroup deprecatedGroup = groups.get(0);
    assertEquals("deprecated", deprecatedGroup.getName());

    DiagnosticGroup visibilityGroup = groups.get(1);
    assertEquals("visibility", visibilityGroup.getName());
  }

  @Test
  public void testDiagnosticGroupNames() {
    List<String> diagnosticGroups = ImmutableList.of(
        "accessControls", "ambiguousFunctionDecl", "checkRegExp", "checkTypes", "checkVars",
        "constantProperty", "deprecated", "externsValidation", "fileoverviewTags", "globalThis",
        "internetExplorerChecks", "invalidCasts", "missingProperties", "nonStandardJsDocs",
        "strictModuleDepCheck", "typeInvalidation", "undefinedVars", "uselessCode", "visibility");

    Map<String, DiagnosticGroup> registeredGroups = getRegisteredGroups();

    for (String name : diagnosticGroups) {
      DiagnosticGroup group = registeredGroups.get(name);
      assertNotNull(group);
    }
  }
}