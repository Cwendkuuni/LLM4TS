
import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.ArrayList;

public class DiagnosticGroupsTest {

  // Test cases for getRegisteredGroups() and forName(String name)
  // Test case for registerGroup(String name, DiagnosticGroup group)
  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String name = "myNewGroup";
    DiagnosticGroup group = new DiagnosticGroup("myNewGroup", null);

    diagnosticGroups.registerGroup(name, group);

    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    assertEquals(group, registeredGroup);
  }

  // Test case for registerGroup(String name, DiagnosticType ... types)
  @Test
  public void testRegisterGroup_WithTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String name = "myNewGroupWithTypes";
    DiagnosticType type1 = new DiagnosticType("myType1", "Message1");
    DiagnosticType type2 = new DiagnosticType("myType2", "Message2");

    diagnosticGroups.registerGroup(name, type1, type2);

    DiagnosticGroup registeredGroup = diagnosticGroups.forName(name);
    List<DiagnosticType> registeredTypes = registeredGroup.getTypes();

    assertEquals(2, registeredTypes.size());
    assertTrue(registeredTypes.contains(type1));
    assertTrue(registeredTypes.contains(type2));
  }

  // Test case for duplicate group registration
  @Test(expected = IllegalStateException.class)
  public void testRegisterGroup_DuplicateRegistration() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String name = "myDuplicateGroup";
    DiagnosticGroup group = new DiagnosticGroup("myDuplicateGroup", null);

    diagnosticGroups.registerGroup(name, group);
    diagnosticGroups.registerGroup(name, group);
  }

  // Test case for registering null group
  @Test(expected = NullPointerException.class)
  public void testRegisterGroup_NullGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    String name = "myNullGroup";
    DiagnosticGroup nullGroup = null;

    diagnosticGroups.registerGroup(name, nullGroup);
  }

  // Test case for getRegisteredGroups() with empty groups
  @Test
  public void testGetRegisteredGroups_EmptyGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup emptyGroup = new DiagnosticGroup("emptyGroup", new ArrayList<>());
    diagnosticGroups.registerGroup("emptyGroup", emptyGroup);

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(1, registeredGroups.size());
    assertEquals(emptyGroup, registeredGroups.get("emptyGroup"));
  }

  // Test case for forName(String name) with non-existent group
  @Test
  public void testForName_NonExistentGroupName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    assertNull(diagnosticGroups.forName("nonExistentGroup"));
  }

  // Test case for setWarningLevels()
  // Add test cases for other methods and their edge cases
}

//