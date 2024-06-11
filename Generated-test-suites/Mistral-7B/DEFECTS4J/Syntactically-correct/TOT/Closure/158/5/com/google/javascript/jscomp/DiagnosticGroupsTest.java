import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.CompilerOptions;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    // Test registering a diagnostic group with a single diagnostic type
    DiagnosticType type1 = new DiagnosticType("TestType1");
    DiagnosticGroup group1 = diagnosticGroups.registerGroup("TestGroup1", type1);
    assertEquals("TestGroup1", group1.getName());
    assertEquals(1, group1.getTypes().size());
    assertEquals(type1, group1.getTypes().get(0));

    // Test registering a diagnostic group with multiple diagnostic types
    DiagnosticType type2 = new DiagnosticType("TestType2");
    DiagnosticGroup group2 = diagnosticGroups.registerGroup("TestGroup2", type1, type2);
    assertEquals("TestGroup2", group2.getName());
    assertEquals(2, group2.getTypes().size());
    assertTrue(group2.getTypes().contains(type1));
    assertTrue(group2.getTypes().contains(type2));

    // Test registering a diagnostic group with other diagnostic groups
    DiagnosticGroup subGroup1 = new DiagnosticGroup("SubGroup1");
    DiagnosticGroup group3 = diagnosticGroups.registerGroup("TestGroup3", subGroup1);
    assertEquals("TestGroup3", group3.getName());
    assertEquals(1, group3.getGroups().size());
    assertEquals(subGroup1, group3.getGroups().get(0));
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticType type1 = new DiagnosticType("TestType1");
    DiagnosticGroup group1 = diagnosticGroups.registerGroup("TestGroup1", type1);

    DiagnosticType type2 = new DiagnosticType("TestType2");
    DiagnosticGroup group2 = diagnosticGroups.registerGroup("TestGroup2", type1, type2);

    DiagnosticGroup subGroup1 = new DiagnosticGroup("SubGroup1");
    DiagnosticGroup group3 = diagnosticGroups.registerGroup("TestGroup3", subGroup1);

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(3, registeredGroups.size());
    assertTrue(registeredGroups.containsKey("TestGroup1"));
    assertTrue(registeredGroups.containsKey("TestGroup2"));
    assertTrue(registeredGroups.containsKey("TestGroup3"));

    assertEquals(group1, registeredGroups.get("TestGroup1"));
    assertEquals(group2, registeredGroups.get("TestGroup2"));
    assertEquals(group3, registeredGroups.get("TestGroup3"));
  }

  @Test
  public void testFindDiagnosticGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticType type1 = new DiagnosticType("TestType1");
    DiagnosticGroup group1 = diagnosticGroups.registerGroup("TestGroup1", type1);

    DiagnosticType type2 = new DiagnosticType("TestType2");
    DiagnosticGroup group2 = diagnosticGroups.registerGroup("TestGroup2", type1, type2);

    DiagnosticGroup subGroup1 = new DiagnosticGroup("SubGroup1");
    DiagnosticGroup group3 = diagnosticGroups.registerGroup("TestGroup3", subGroup1);

    DiagnosticGroup foundGroup = diagnosticGroups.forName("TestGroup2");
    assertEquals(group2, foundGroup);
  }
}