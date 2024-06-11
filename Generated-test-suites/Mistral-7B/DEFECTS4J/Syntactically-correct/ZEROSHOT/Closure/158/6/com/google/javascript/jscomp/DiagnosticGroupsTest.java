import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import junit.framework.Assert;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test getRegisteredGroups method
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    Assert.assertNotNull(registeredGroups);

    // Test forName method with null name
    assertNull(diagnosticGroups.forName(null));

    // Test forName method with invalid name
    assertNull(diagnosticGroups.forName("InvalidGroup"));

    // Test forName method with valid name
    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecatedGroup);

    // Test setWarningLevels method
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = ImmutableMap.of(
        "deprecated", "accessControls", "checkVars"
    ).entrySet().stream()
        .map(entry -> entry.getKey())
        .collect(Collectors.toList());
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);

    // Test warning levels
    DiagnosticGroup deprecatedDiagnostic = options.getDiagnostics(deprecatedGroup);
    DiagnosticGroup accessControlsDiagnostic = options.getDiagnostics("accessControls");
    DiagnosticGroup checkVarsDiagnostic = options.getDiagnostics("checkVars");
    Assert.assertEquals(CheckLevel.WARNING, deprecatedDiagnostic.getLevel());
    Assert.assertEquals(CheckLevel.WARNING, accessControlsDiagnostic.getLevel());
    Assert.assertEquals(CheckLevel.WARNING, checkVarsDiagnostic.getLevel());
  }
}