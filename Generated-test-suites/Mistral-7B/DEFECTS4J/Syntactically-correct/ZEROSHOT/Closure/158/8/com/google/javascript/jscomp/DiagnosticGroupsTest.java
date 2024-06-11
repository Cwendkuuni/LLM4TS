import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  private static Map<String, DiagnosticGroup> diagnosticGroups;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups().getRegisteredGroups();
  }

  @Test
  public void testRegisteredGroups() {
    assertEquals("Size of registered diagnostic groups should be 16", 16,
        diagnosticGroups.size());
  }

  @Test
  public void testGetByName() {
    DiagnosticGroup deprecated = diagnosticGroups.get("deprecated");
    DiagnosticGroup accessControls = diagnosticGroups.get("accessControls");
    DiagnosticGroup checkTypes = diagnosticGroups.get("checkTypes");

    assertNotNull("deprecated group should not be null", deprecated);
    assertNotNull("accessControls group should not be null", accessControls);
    assertNotNull("checkTypes group should not be null", checkTypes);
  }

  @Test
  public void testRegisterGroup() {
    // Test registering a group with DiagnosticType
    String newGroupName = "newGroupName";
    DiagnosticType newDiagnosticType = new DiagnosticType("New Diagnostic");

    DiagnosticGroup newDiagnosticGroup = DiagnosticGroups.registerGroup(newGroupName, newDiagnosticType);

    DiagnosticGroup retrievedGroup = diagnosticGroups.get(newGroupName);

    assertNotNull("Newly registered group should not be null", newDiagnosticGroup);
    assertEquals("Retrieved group should match the registered group", newDiagnosticGroup, retrievedGroup);

    // Test registering a group with multiple DiagnosticTypes
    String newGroupName2 = "newGroupName2";
    DiagnosticType newDiagnosticType1 = new DiagnosticType("New Diagnostic 1");
    DiagnosticType newDiagnosticType2 = new DiagnosticType("New Diagnostic 2");

    DiagnosticGroup newDiagnosticGroup2 = DiagnosticGroups.registerGroup(newGroupName2, newDiagnosticType1, newDiagnosticType2);

    DiagnosticGroup retrievedGroup2 = diagnosticGroups.get(newGroupName2);

    assertNotNull("Newly registered group should not be null", newDiagnosticGroup2);
    assertEquals("Retrieved group should match the registered group", newDiagnosticGroup2, retrievedGroup2);

    // Test registering a group with another group
    String newGroupName3 = "newGroupName3";
    DiagnosticGroup existingGroup = diagnosticGroups.get("accessControls");

    DiagnosticGroup newDiagnosticGroup3 = DiagnosticGroups.registerGroup(newGroupName3, existingGroup);

    DiagnosticGroup retrievedGroup3 = diagnosticGroups.get(newGroupName3);

    assertNotNull("Newly registered group should not be null", newDiagnosticGroup3);
    assertEquals("Retrieved group should match the registered group", newDiagnosticGroup3, retrievedGroup3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupNullName() {
    String nullName = null;
    DiagnosticType diagnosticType = new DiagnosticType("New Diagnostic");

    DiagnosticGroups.registerGroup(nullName, diagnosticType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupNullDiagnosticType() {
    String name = "newGroupName";
    DiagnosticType nullDiagnosticType = null;

    DiagnosticGroups.registerGroup(name, nullDiagnosticType);
  }

  @Test
  public void testDiagnosticGroupNameList() {
    List<String> diagnosticGroupNameList = ImmutableMap.<String, DiagnosticGroup>copyOf(diagnosticGroups)
        .keySet()
        .stream()
        .collect(ImmutableMap.toImmutableList())
        .get(0)
        .asList();

    assertEquals("Size of diagnostic group names list should be 16", 16, diagnosticGroupNameList.size());
  }
}