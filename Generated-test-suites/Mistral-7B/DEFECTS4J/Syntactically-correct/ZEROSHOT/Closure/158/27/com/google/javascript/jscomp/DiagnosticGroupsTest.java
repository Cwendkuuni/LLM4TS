import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroup.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterAndGetGroups() {
    DiagnosticGroup globalThis = DiagnosticGroups.GLOBAL_THIS;
    DiagnosticGroup deprecated = DiagnosticGroups.DEPRECATED;
    DiagnosticGroup visibility = DiagnosticGroups.VISIBILITY;
    DiagnosticGroup constantProperty = DiagnosticGroups.CONSTANT_PROPERTY;
    DiagnosticGroup nonStandardJsDoc = DiagnosticGroups.NON_STANDARD_JSDOC;
    DiagnosticGroup accessControls = DiagnosticGroups.ACCESS_CONTROLS;
    DiagnosticGroup invalidCasts = DiagnosticGroups.INVALID_CASTS;
    DiagnosticGroup fileoverviewJsdoc = DiagnosticGroups.FILEOVERVIEW_JSDOC;
    DiagnosticGroup strictModuleDepCheck = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
    DiagnosticGroup externsValidation = DiagnosticGroups.EXTERNS_VALIDATION;
    DiagnosticGroup ambiguousFunctionDecl = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
    DiagnosticGroup unknownDefines = DiagnosticGroups.UNKNOWN_DEFINES;
    DiagnosticGroup tweaks = DiagnosticGroups.TWEAKS;
    DiagnosticGroup missingProperties = DiagnosticGroups.MISSING_PROPERTIES;
    DiagnosticGroup internetExplorerChecks = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
    DiagnosticGroup undefinedVariables = DiagnosticGroups.UNDEFINED_VARIABLES;
    DiagnosticGroup checkRegExp = DiagnosticGroups.CHECK_REGEXP;
    DiagnosticGroup checkTypes = DiagnosticGroups.CHECK_TYPES;
    DiagnosticGroup checkVariables = DiagnosticGroups.CHECK_VARIABLES;
    DiagnosticGroup checkUselessCode = DiagnosticGroups.CHECK_USELESS_CODE;
    DiagnosticGroup typeInvalidation = DiagnosticGroups.TYPE_INVALIDATION;

    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();

    assertTrue(registeredGroups.containsKey(globalThis.getName()));
    assertTrue(registeredGroups.containsKey(deprecated.getName()));
    assertTrue(registeredGroups.containsKey(visibility.getName()));
    assertTrue(registeredGroups.containsKey(constantProperty.getName()));
    assertTrue(registeredGroups.containsKey(nonStandardJsDoc.getName()));
    assertTrue(registeredGroups.containsKey(accessControls.getName()));
    assertTrue(registeredGroups.containsKey(invalidCasts.getName()));
    assertTrue(registeredGroups.containsKey(fileoverviewJsdoc.getName()));
    assertTrue(registeredGroups.containsKey(strictModuleDepCheck.getName()));
    assertTrue(registeredGroups.containsKey(externsValidation.getName()));
    assertTrue(registeredGroups.containsKey(ambiguousFunctionDecl.getName()));
    assertTrue(registeredGroups.containsKey(unknownDefines.getName()));
    assertTrue(registeredGroups.containsKey(tweaks.getName()));
    assertTrue(registeredGroups.containsKey(missingProperties.getName()));
    assertTrue(registeredGroups.containsKey(internetExplorerChecks.getName()));
    assertTrue(registeredGroups.containsKey(undefinedVariables.getName()));
    assertTrue(registeredGroups.containsKey(checkRegExp.getName()));
    assertTrue(registeredGroups.containsKey(checkTypes.getName()));
    assertTrue(registeredGroups.containsKey(checkVariables.getName()));
    assertTrue(registeredGroups.containsKey(checkUselessCode.getName()));
    assertTrue(registeredGroups.containsKey(typeInvalidation.getName()));

    DiagnosticGroup group = DiagnosticGroups.forName("unknownGroup");
    assertNull(group);
  }

  @Test
  public void testDiagnosticGroupNames() {
    String diagnosticGroupNames = DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES;
    String[] groups = diagnosticGroupNames.split(", ");

    for (String groupName : groups) {
      DiagnosticGroup group = DiagnosticGroups.forName(groupName);
      assertNotNull(group);
    }
  }
}