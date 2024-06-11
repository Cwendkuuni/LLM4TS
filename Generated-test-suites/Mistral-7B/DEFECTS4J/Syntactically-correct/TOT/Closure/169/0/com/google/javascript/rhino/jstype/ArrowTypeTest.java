import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.testing.PropertyChecker;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(getNativeType(STRING));
    JSType returnType = INT;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Test isSubtype
    assertTrue(arrowType.isSubtype(new ArrowType(registry, parameters, INT)));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, parameters, BOOL)));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, parameters, VOID)));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, null, INT)));
    assertFalse(arrowType.isSubtype(new ArrowType(registry, parameters, null)));

    // Test hasEqualParameters
    ArrowType arrowType1 = new ArrowType(registry, parameters, INT);
    ArrowType arrowType2 = new ArrowType(registry, parameters, INT);
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType2, false));

    ArrowType arrowType3 = new ArrowType(registry, parameters, INT, true);
    ArrowType arrowType4 = new ArrowType(registry, parameters, INT, false);
    assertFalse(arrowType3.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType3.hasEqualParameters(arrowType4, false));

    ArrowType arrowType5 = new ArrowType(registry, null, INT);
    ArrowType arrowType6 = new ArrowType(registry, parameters, INT);
    assertFalse(arrowType5.hasEqualParameters(arrowType6, true));
    assertFalse(arrowType5.hasEqualParameters(arrowType6, false));

    ArrowType arrowType7 = new ArrowType(registry, parameters, INT);
    ArrowType arrowType8 = new ArrowType(registry, null, INT);
    assertFalse(arrowType7.hasEqualParameters(arrowType8, true));
    assertFalse(arrowType7.hasEqualParameters(arrowType8, false));

    // Test checkArrowEquivalenceHelper
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowType1, true));
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowType1, false));
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowType2, true));
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowType2, false));

    // Test hashCode
    ArrowType arrowType9 = new ArrowType(registry, parameters, INT);
    ArrowType arrowType10 = new ArrowType(registry, parameters, INT);
    assertEquals(arrowType.hashCode(), arrowType9.hashCode());
    assertEquals(arrowType.hashCode(), arrowType10.hashCode());

    returnType = VOID;
    arrowType = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType.isSubtype(new ArrowType(registry, parameters, INT)));

    parameters = registry.createParametersWithVarArgs(getNativeType(INT));
    arrowType = new ArrowType(registry, parameters, INT);
    assertFalse(arrowType.isSubtype(new ArrowType(registry, null, INT)));

    parameters = registry.createParametersWithVarArgs(getNativeType(INT));
    returnType = STRING;
    arrowType = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType.isSubtype(new ArrowType(registry, null, INT)));

    parameters = registry.createParametersWithVarArgs(getNativeType(INT));
    returnType = NULL;
    arrowType = new ArrowType(registry, parameters, returnType);
    assertFalse(arrowType.isSubtype(new ArrowType(registry, null, INT)));

    parameters = registry.createParametersWithVarArgs(getNativeType(INT));
    arrowType = new ArrowType(registry, parameters, INT, true);
    assertFalse(arrowType.isSubtype(new ArrowType(registry, parameters, INT)));

    ArrowType arrowType11 = new ArrowType(registry, null, INT);
    assertFalse(arrowType.isSubtype(arrowType11));

    ArrowType arrowType12 = new ArrowType(registry, parameters, INT, false);
    assertFalse(arrowType.isSubtype(arrowType12));

    // Test resolveInternal
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(t, null);
    arrowType.resolveInternal(t, scope);
    PropertyChecker.checkPropertyTypes(arrowType, t, scope);
  }
}