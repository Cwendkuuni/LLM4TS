import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.compiler.Compiler;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(21, registeredGroups.size());
    // Check for existence of some registered groups
    assertNotNull(ACCESS_CONTROLS);
    assertNotNull(INVALID_CASTS);
    assertNotNull(FILEOVERVIEW_JSDOC);
    assertNotNull(STRICT_MODULE_DEP_CHECK);
    assertNotNull(EXTERNS_VALIDATION);
  }

  @org.junit.Test
  public void testGetDiagnosticGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    DiagnosticGroup constantProperty = diagnosticGroups.forName("constantProperty");
    assertNotNull(deprecated);
    assertNotNull(constantProperty);
    // Test with non-existent group name
    assertNull(diagnosticGroups.forName("nonExistentGroup"));
  }

  @org.junit.Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    String[] diagnosticGroupsToSet = {"ACCESS_CONTROLS", "INVALID_CASTS"};
    diagnosticGroups.setWarningLevels(options, Arrays.asList(diagnosticGroupsToSet), CheckLevel.WARNING);
    assertTrue(options.hasWarning(ACCESS_CONTROLS));
    assertTrue(options.hasWarning(INVALID_CASTS));
    assertFalse(options.hasWarning(CHECK_VARIABLES));
  }

  @org.junit.Test
  public void testCompileWithWarningGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    String[] diagnosticGroupsToSet = {"ACCESS_CONTROLS", "INVALID_CASTS"};
    diagnosticGroups.setWarningLevels(options, Arrays.asList(diagnosticGroupsToSet), CheckLevel.WARNING);
    Compiler compiler = new Compiler(options);
    String sourceCode = "function test() { var a = 1; var b = 1; a = 5; }"; // Reassign constant property
    String expectedError = "TypeError: Cannot reassign a constant property.";
    try {
      compiler.compile(sourceCode, "test.js", null);
      fail("Expected compilation error.");
    } catch (Compiler.CompilationFailedException e) {
      List<String> compilationErrors = e.getDiagnostics();
      assertTrue(compilationErrors.size() > 0);
      String actualError = compilationErrors.get(0).getMessage();
      assertEquals(expectedError, actualError);
    }
  }
}