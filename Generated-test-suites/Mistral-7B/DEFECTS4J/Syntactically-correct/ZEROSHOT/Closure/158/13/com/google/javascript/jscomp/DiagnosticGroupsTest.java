import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class DiagnosticGroupsTest {

  private static DiagnosticGroups diagnosticGroups;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testRegisterAndGetGroups() {
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

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    List<String> groupNames = java.util.Arrays.asList(ACCESS_CONTROLS.getName(), DEPRECATED.getName(), GLOBAL_THIS.getName());
    assertEquals(groupNames, registeredGroups.keySet());
  }

  @Test
  public void testDiagnosticGroupNames() {
    assertEquals(DIAGNOSTIC_GROUP_NAMES, diagnosticGroups.DIAGNOSTIC_GROUP_NAMES);
  }

  @Test
  public void testGetGroupByName() {
    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecatedGroup);

    DiagnosticGroup unknownGroup = diagnosticGroups.forName("unknownGroup");
    assertNull(unknownGroup);
  }

}