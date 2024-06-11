import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

import java.util.Arrays;
import java.util.List;

public class DiagnosticGroupsTest {

  @org.junit.BeforeClass
  public static void setup() {
    DiagnosticGroups.registerGroup("accessControls",
        DiagnosticGroups.DEPRECATED, DiagnosticGroups.VISIBILITY);
    DiagnosticGroups.registerGroup("invalidCasts", DiagnosticGroups.INVALID_CAST);
    DiagnosticGroups.registerGroup("fileoverviewTags",
        DiagnosticGroups.EXTRA_FILEOVERVIEW);
    DiagnosticGroups.registerGroup("strictModuleDepCheck",
        DiagnosticGroups.STRICT_MODULE_DEP_ERROR,
        DiagnosticGroups.STRICT_MODULE_DEP_QNAME);
    DiagnosticGroups.registerGroup("externsValidation",
        DiagnosticGroups.NAME_REFERENCE_IN_EXTERNS_ERROR,
        DiagnosticGroups.UNDEFINED_EXTERN_VAR_ERROR);
    DiagnosticGroups.registerGroup("ambiguousFunctionDecl",
        DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
    DiagnosticGroups.registerGroup("unknownDefines",
        DiagnosticGroups.UNKNOWN_DEFINE_WARNING);
    DiagnosticGroups.registerGroup("tweakValidation",
        DiagnosticGroups.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
        DiagnosticGroups.TWEAK_WRONG_GETTER_TYPE_WARNING,
        DiagnosticGroups.UNKNOWN_TWEAK_WARNING);
    DiagnosticGroups.registerGroup("missingProperties",
        DiagnosticGroups.INEXISTENT_PROPERTY);
    DiagnosticGroups.registerGroup("internetExplorerChecks",
        DiagnosticGroups.TRAILING_COMMA);
    DiagnosticGroups.registerGroup("undefinedVars",
        DiagnosticGroups.UNDEFINED_VAR_ERROR);
    DiagnosticGroups.registerGroup("checkRegExp", DiagnosticGroups.REGEXP_REFERENCE);
    DiagnosticGroups.registerGroup("checkTypes",
        DiagnosticGroups.INVALID_CAST, DiagnosticGroups.INEXISTENT_PROPERTY);
    DiagnosticGroups.registerGroup("checkVars",
        DiagnosticGroups.UNDEFINED_VAR_ERROR,
        DiagnosticGroups.VAR_MULTIPLY_DECLARED_ERROR);
    DiagnosticGroups.registerGroup("uselessCode",
        DiagnosticGroups.USELESS_CODE_ERROR, DiagnosticGroups.UNREACHABLE_CODE);
    DiagnosticGroups.registerGroup("typeInvalidation",
        DiagnosticGroups.WARNINGS.INVALIDATION);
  }

  @org.junit.Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> groups = DiagnosticGroups.getRegisteredGroups();
    assertNotNull(groups);
    assertEquals(17, groups.size());
  }

  @org.junit.Test
  public void testForName() {
    DiagnosticGroup group = DiagnosticGroups.forName("accessControls");
    assertNotNull(group);
    assertEquals("accessControls", group.getName());
    DiagnosticType[] types = group.getTypes();
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(DiagnosticGroups.DEPRECATED.getClass(), types[0].getClass());
    assertEquals(DiagnosticGroups.VISIBILITY.getClass(), types[1].getClass());

    group = DiagnosticGroups.forName("unknownGroup");
    assertNull(group);
  }

  @org.junit.Test
  public void testRegisterGroup() {
    DiagnosticGroup newGroup = new DiagnosticGroup("newGroup");
    DiagnosticType newType = new DiagnosticType("newType");
    List<DiagnosticType> types = Arrays.asList(newType);

    DiagnosticGroups.registerGroup("newGroup", newType);
    DiagnosticGroup result = DiagnosticGroups.forName("newGroup");
    assertNotNull(result);
    assertEquals("newGroup", result.getName());
    assertEquals(1, result.getTypes().length);
    assertEquals(newType.getClass(), result.getTypes()[0].getClass());

    DiagnosticGroups.registerGroup("newGroup", newGroup);
    result = DiagnosticGroups.forName("newGroup");
    assertNotNull(result);
    assertEquals("newGroup", result.getName());
    assertNotNull(result.getTypes());
    assertEquals(newGroup.getTypes().length, result.getTypes().length);
  }
}