import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.CompilerOptions;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();

    DiagnosticGroup globalThisGroup = diagnosticGroups.GLOBAL_THIS;
    assertNotNull(globalThisGroup);
    assertTrue(globalThisGroup.getName().equals("globalThis"));

    DiagnosticGroup deprecatedGroup = diagnosticGroups.DEPRECATED;
    assertNotNull(deprecatedGroup);
    assertTrue(deprecatedGroup.getName().equals("deprecated"));

    DiagnosticGroup visibilityGroup = diagnosticGroups.VISIBILITY;
    assertNotNull(visibilityGroup);
    assertTrue(visibilityGroup.getName().equals("visibility"));

    // Test getting registered groups
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertTrue(registeredGroups.size() > 0);
    assertTrue(registeredGroups.containsKey(globalThisGroup.getName()));
    assertTrue(registeredGroups.containsKey(deprecatedGroup.getName()));
    assertTrue(registeredGroups.containsKey(visibilityGroup.getName()));
  }

  @Test
  public void testFindGroupByName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup globalThisGroup = diagnosticGroups.GLOBAL_THIS;
    DiagnosticGroup deprecatedGroup = diagnosticGroups.DEPRECATED;
    DiagnosticGroup visibilityGroup = diagnosticGroups.VISIBILITY;

    DiagnosticGroup groupByNameGlobalThis = diagnosticGroups.forName("globalThis");
    DiagnosticGroup groupByNameDEPRECATED = diagnosticGroups.forName("deprecated");
    DiagnosticGroup groupByNameVISIBILITY = diagnosticGroups.forName("visibility");

    assertNotNull(groupByNameGlobalThis);
    assertTrue(groupByNameGlobalThis.equals(globalThisGroup));
    assertNotNull(groupByNameDEPRECATED);
    assertTrue(groupByNameDEPRECATED.equals(deprecatedGroup));
    assertNotNull(groupByNameVISIBILITY);
    assertTrue(groupByNameVISIBILITY.equals(visibilityGroup));
  }
}