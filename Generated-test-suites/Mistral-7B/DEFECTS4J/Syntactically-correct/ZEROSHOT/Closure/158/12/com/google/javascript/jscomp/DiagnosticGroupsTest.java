import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  private static CompilerOptions options;

  @BeforeClass
  public static void setup() {
    options = new CompilerOptions();
  }

  @Test
  public void testRegisteredGroupsAreNotNull() {
    DiagnosticGroups diagnostics = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnostics.getRegisteredGroups();

    for (String groupName : DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(",")) {
      DiagnosticGroup group = groups.get(groupName);
      assertNotNull(group);
    }
  }

  @Test
  public void testGroupContainsExpectedTypes() {
    DiagnosticGroups diagnostics = new DiagnosticGroups();

    List<DiagnosticType> deprecatedTypes = List.of(
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON);

    DiagnosticGroup deprecatedGroup = diagnostics.forName("deprecated");
    assertTrue(deprecatedGroup.getDiagnostics().containsAll(deprecatedTypes));

    List<DiagnosticType> checkTypesTypes = List.of(TypeValidator.INVALID_CAST);
    DiagnosticGroup checkTypesGroup = diagnostics.forName("checkTypes");
    assertTrue(checkTypesGroup.getDiagnostics().containsAll(checkTypesTypes));
  }

  @Test
  public void testGroupsCanBeNested() {
    DiagnosticGroups diagnostics = new DiagnosticGroups();

    DiagnosticGroup accessControlsGroup = diagnostics.forName("accessControls");
    assertTrue(accessControlsGroup.getGroupName().equals("accessControls"));

    DiagnosticGroup deprecatedGroup = accessControlsGroup.getSubGroups().get(0);
    assertTrue(deprecatedGroup.getGroupName().equals("deprecated"));

    DiagnosticGroup visibilityGroup = accessControlsGroup.getSubGroups().get(1);
    assertTrue(visibilityGroup.getGroupName().equals("visibility"));
  }
}