import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

@RunWith(JUnit4.class)
public class DiagnosticGroupsTest {

  @Test
  public void testDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test registering a DiagnosticGroup with a single DiagnosticType
    DiagnosticGroup deprecatedGroup = diagnosticGroups.registerGroup("deprecated",
        new DiagnosticType("DEPRECATED_NAME"));
    assertNotNull(deprecatedGroup);

    // Test registering a DiagnosticGroup with multiple DiagnosticTypes
    DiagnosticGroup accessControlsGroup = diagnosticGroups.registerGroup("accessControls",
        new DiagnosticType("DEPRECATED_NAME"),
        new DiagnosticType("DEPRECATED_NAME_REASON"),
        new DiagnosticType("DEPRECATED_PROP"),
        new DiagnosticType("DEPRECATED_PROP_REASON"),
        new DiagnosticType("DEPRECATED_CLASS"),
        new DiagnosticType("DEPRECATED_CLASS_REASON"));
    assertNotNull(accessControlsGroup);

    // Test getting registered DiagnosticGroups
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);

    // Test getting a DiagnosticGroup by name
    DiagnosticGroup visibilityGroup = diagnosticGroups.forName("visibility");
    assertNotNull(visibilityGroup);
  }
}