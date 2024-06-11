import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import java.util.List;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();

    // Test registered diagnostic groups
    assertEquals(38, groups.size());
    assertTrue(groups.containsKey("accessControls"));
    assertTrue(groups.containsKey("ambiguousFunctionDecl"));
    assertTrue(groups.containsKey("checkRegExp"));
    // ... add assertions for all other registered groups

    DiagnosticGroup accessControls = groups.get("accessControls");
    assertEquals(7, accessControls.getDiagnosticTypes().size());
    // ... add assertions for all diagnostic types in the 'accessControls' group
  }

  @Test
  public void testGetDiagnosticTypeByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticType deprecated = accessControls.getDiagnosticTypes().get(0);
    assertEquals(CheckAccessControls.DEPRECATED_NAME, deprecated.getName());
  }

  @Test(expected = NullPointerException.class)
  public void testGetNullDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }
}