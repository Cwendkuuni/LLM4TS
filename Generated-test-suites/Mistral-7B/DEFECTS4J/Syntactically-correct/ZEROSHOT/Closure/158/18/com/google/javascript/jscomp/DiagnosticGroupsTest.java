import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.ParserConfig;
import com.google.common.collect.Lists;

public class DiagnosticGroupsTest {

  private static DiagnosticGroups diagnosticGroups;
  private static CompilerOptions options;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups();
    options = new CompilerOptions();
  }

  @Test
  public void testRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(23, registeredGroups.size());
  }

  @Test
  public void testGetGroupByName() {
    DiagnosticGroup globalThis = diagnosticGroups.forName("globalThis");
    assertNotNull(globalThis);
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertNotNull(accessControls);
  }

  @Test
  public void testGetWarningLevels() {
    List<String> diagnosticGroups = Lists.newArrayList("accessControls", "uselessCode");
    diagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_USELESS_CODE));
  }

  @Test
  public void testInvalidGroupName() {
    assertNull(diagnosticGroups.forName("invalidGroupName"));
  }

  @Test
  public void testRegisterGroup() {
    String customGroupName = "customGroupName";
    DiagnosticType customDiagnosticType = CheckerFactory.getChecker()
        .getTypes().get(DiagnosticType.UNDEFINED_VAR);
    DiagnosticGroup customGroup = new DiagnosticGroup(customGroupName, customDiagnosticType);
    diagnosticGroups.registerGroup(customGroupName, customGroup);
    DiagnosticGroup registeredCustomGroup = diagnosticGroups.forName(customGroupName);
    assertNotNull(registeredCustomGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterNullGroup() {
    diagnosticGroups.registerGroup(null, new DiagnosticType[0]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterEmptyGroup() {
    diagnosticGroups.registerGroup("", new DiagnosticType[0]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithDuplicateName() {
    diagnosticGroups.registerGroup("accessControls", new DiagnosticType[0]);
    try {
      diagnosticGroups.registerGroup("accessControls", new DiagnosticType[0]);
    } catch (IllegalArgumentException e) {
      assertEquals("Diagnostic group name 'accessControls' already exists", e.getMessage());
    }
  }
}