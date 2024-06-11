import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    // Test registered groups
    assertEquals(27, registeredGroups.size());
    assertNotNull(registeredGroups.get("accessControls"));
    assertNotNull(registeredGroups.get("ambiguousFunctionDecl"));
    assertNotNull(registeredGroups.get("checkRegExp"));
    // ... and so on for all registered groups
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    // Test getting group by name
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertEquals("accessControls", accessControls.getName());
    assertNotNull(accessControls.getDiagnostics());
    // ... and so on for all registered groups
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForNameUnknownName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName("unknownGroupName");
  }
}