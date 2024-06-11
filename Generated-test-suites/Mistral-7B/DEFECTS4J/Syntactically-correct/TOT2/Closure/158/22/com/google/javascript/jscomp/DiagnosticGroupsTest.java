
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.parser.Parser;
import com.google.javascript.jscomp.parser.ParserConfiguration;
import com.google.javascript.jscomp.parser.SourceFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(groups);
  }

  @Test
  public void testEmptyMap() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
    assertTrue(groups.isEmpty());
  }

  @Test
  public void testNullMap() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertNull(diagnosticGroups.getRegisteredGroups());
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group = diagnosticGroups.forName("ACCESS_CONTROLS");
    assertNotNull(group);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testUnregisteredGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.forName("NON_EXISTENT_GROUP");
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = Arrays.asList("ACCESS_CONTROLS");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(ACCESS_CONTROLS));
  }

  @Test(expected = NullPointerException.class)
  public void testNullOptions() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    List<String> diagnosticGroupsList = Arrays.asList("ACCESS_CONTROLS");
    diagnosticGroups.setWarningLevels(null, diagnosticGroupsList, CheckLevel.WARNING);
  }

  @Test(expected = NullPointerException.class)
  public void testNullDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, null, CheckLevel.WARNING);
  }

  @Test(expected = NullPointerException.class)
  public void testNullLevel() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = Arrays.asList("ACCESS_CONTROLS");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyDiagnosticGroupsList() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    diagnosticGroups.setWarningLevels(options, Collections.emptyList(), CheckLevel.WARNING);
  }
}
