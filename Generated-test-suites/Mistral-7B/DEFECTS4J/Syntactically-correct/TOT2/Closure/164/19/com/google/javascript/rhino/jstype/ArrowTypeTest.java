import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  @org.junit.Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = registry.createParametersWithVarArgs(registry.createNamedType("number"));
    Node parameters2 = registry.createParametersWithVarArgs(registry.createNamedType("string"));
    ArrowType arrowType1 = new ArrowType(registry, parameters1, registry.getNativeType("number"));
    ArrowType arrowType2 = new ArrowType(registry, parameters2, registry.getNativeType("number"));

    assertTrue(arrowType1.isSubtype(arrowType2));
  }

  @org.junit.Test
  public void testIsSubtype_false() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = registry.createParametersWithVarArgs(registry.createNamedType("number"));
    Node parameters2 = registry.createParametersWithVarArgs(registry.createNamedType("string"));
    ArrowType arrowType1 = new ArrowType(registry, parameters1, registry.getNativeType("number"));
    ArrowType arrowType2 = new ArrowType(registry, parameters2, registry.getNativeType("boolean"));

    assertFalse(arrowType1.isSubtype(arrowType2));
  }

  // Add test cases for other methods: hasEqualParameters, isEquivalentTo, hashCode, getLeastSupertype, getGreatestSubtype, testForEquality, visit, getPossibleToBooleanOutcomes, resolveInternal, hasUnknownParamsOrReturn
}