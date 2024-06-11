import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterAndGetGroups() {
    DiagnosticGroup globalThisGroup = registerGroup("globalThis");
    DiagnosticGroup deprecatedGroup = registerGroup("deprecated");
    DiagnosticGroup accessControlsGroup = registerGroup("accessControls");
    DiagnosticGroup constantPropertyGroup = registerGroup("constantProperty");

    Map<String, DiagnosticGroup> registeredGroups = getRegisteredGroups();

    assertEquals(4, registeredGroups.size());
    assertTrue(registeredGroups.containsKey("globalThis"));
    assertTrue(registeredGroups.containsKey("deprecated"));
    assertTrue(registeredGroups.containsKey("accessControls"));
    assertTrue(registeredGroups.containsKey("constantProperty"));

    assertEquals(globalThisGroup, registeredGroups.get("globalThis"));
    assertEquals(deprecatedGroup, registeredGroups.get("deprecated"));
    assertEquals(accessControlsGroup, registeredGroups.get("accessControls"));
    assertEquals(constantPropertyGroup, registeredGroups.get("constantProperty"));
  }

  @Test
  public void testFindGroupByName() {
    DiagnosticGroup globalThisGroup = registerGroup("globalThis");
    DiagnosticGroup deprecatedGroup = registerGroup("deprecated");
    DiagnosticGroup accessControlsGroup = registerGroup("accessControls");
    DiagnosticGroup constantPropertyGroup = registerGroup("constantProperty");

    assertEquals(accessControlsGroup, forName("accessControls"));
    assertEquals(constantPropertyGroup, forName("constantProperty"));
    assertEquals(globalThisGroup, forName("globalThis"));
    assertEquals(deprecatedGroup, forName("deprecated"));
  }

  @Test(expected = NullPointerException.class)
  public void testFindNullGroupByName() {
    forName(null);
  }

  @Test
  public void testDiagnosticGroupNames() {
    List<String> diagnosticGroups = getDiagnosticGroupNames();

    assertEquals(18, diagnosticGroups.size());
    assertTrue(diagnosticGroups.containsAll(DIAGNOSTIC_GROUP_NAMES.split(", ")));
  }
}