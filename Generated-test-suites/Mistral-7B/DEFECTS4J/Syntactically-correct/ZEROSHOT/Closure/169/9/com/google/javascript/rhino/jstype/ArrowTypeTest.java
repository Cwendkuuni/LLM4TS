import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Node;
import com.google.javascript.rhino.jstype.UnknownType;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters = registry.createParametersWithVarArgs(
        registry.getFunctionType(registry.getNumberType(), registry.getNumberType()));

    ArrowType arrowType = new ArrowType(registry, parameters, registry.getNumberType());

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertSame(registry, arrowType.getTypeRegistry());
    assertSame(parameters, arrowType.getParameters());
    assertSame(registry.getNumberType(), arrowType.getReturnType());
    assertFalse(arrowType.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeCreationWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters = registry.createParametersWithVarArgs(
        registry.getFunctionType(registry.getNumberType(), registry.getNumberType()));

    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    assertEquals("ArrowType", arrowType.getClass().getSimpleName());
    assertSame(registry, arrowType.getTypeRegistry());
    assertSame(parameters, arrowType.getParameters());
    assertNull(arrowType.getReturnType());
    assertTrue(arrowType.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node leftParameters = registry.createParametersWithVarArgs(
        registry.getFunctionType(registry.getNumberType(), registry.getNumberType()));
    Node rightParameters = registry.createParametersWithVarArgs(
        registry.getFunctionType(registry.getNumberType(), registry.getNumberType()));

    JSType leftReturnType = registry.getNumberType();
    JSType rightReturnType = registry.getBooleanType();

    ArrowType leftArrowType = new ArrowType(registry, leftParameters, leftReturnType);
    ArrowType rightArrowType = new ArrowType(registry, rightParameters, rightReturnType);

    // ArrowType with the same arguments and return type is a subtype of itself
    assertTrue(leftArrowType.isSubtype(leftArrowType));

    // ArrowType with the same arguments and different return type is not a subtype
    assertFalse(leftArrowType.isSubtype(rightArrowType));

    // ArrowType with different arguments is not a subtype
    ArrowType differentArgsArrowType = new ArrowType(registry,
        registry.createParametersWithVarArgs(registry.getFunctionType(registry.getBooleanType(), registry.getBooleanType())),
        leftReturnType);
    assertFalse(leftArrowType.isSubtype(differentArgsArrowType));

    // ArrowType with the same arguments and covariant return type is a subtype
    ArrowType covariantReturnArrowType = new ArrowType(registry, leftParameters, registry.getObjectType());
    assertTrue(leftArrowType.isSubtype(covariantReturnArrowType));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node leftParameters = registry.createParametersWithVarArgs(
        registry.getFunctionType(registry.getNumberType(), registry.getNumberType()));
    Node rightParameters = registry.createParametersWithVarArgs(
        registry.getFunctionType(registry.getNumberType(), registry.getNumberType()));

    ArrowType leftArrowType = new ArrowType(registry, leftParameters, registry.getNumberType());
    ArrowType rightArrowType = new ArrowType(registry, rightParameters, registry.getNumberType());

    // ArrowTypes with the same parameters are equal
    assertTrue(leftArrowType.hasEqualParameters(rightArrowType, false));

    // ArrowTypes with different return types are not equal
    ArrowType differentReturnTypeArrowType = new ArrowType(registry, leftParameters, registry.getBooleanType());
    assertFalse(leftArrowType.hasEqualParameters(differentReturnTypeArrowType, false));

    // ArrowTypes with unknown return types are considered equal
    ArrowType leftArrowTypeWithUnknownReturnType = new ArrowType(registry, leftParameters, null);
    ArrowType rightArrowTypeWithUnknownReturnType = new ArrowType(registry, rightParameters, null);
    assertTrue(leftArrowType.hasEqualParameters(rightArrowTypeWithUnknownReturnType, true));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters = registry.createParametersWithVarArgs(
        registry.getFunctionType(registry.getNumberType(), registry.getNumberType()));

    ArrowType arrowType1 = new ArrowType(registry, parameters, registry.getNumberType());
    ArrowType arrowType2 = new ArrowType(registry, parameters, registry.getNumberType());

    // ArrowTypes with the same arguments and return type have the same hashcode
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    // ArrowTypes with different return types have different hashcodes
    ArrowType differentReturnTypeArrowType = new ArrowType(registry, parameters, registry.getBooleanType());
    assertNotEquals(arrowType1.hashCode(), differentReturnTypeArrowType.hashCode());
  }

  @Test
  public void testArrowTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters = registry.createParametersWithVarArgs(
        registry.getFunctionType(registry.getNumberType(), registry.getNumberType()));

    ArrowType arrowType = new ArrowType(registry, parameters, registry.getNumberType());

    assertEquals("[ArrowType]", arrowType.toStringHelper(false));
  }

  @Test
  public void testArrowTypeHasAnyTemplate() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters = registry.createParametersWithVarArgs(
        registry.getFunctionType(registry.getNumberType(), registry.getNumberType()));

    ArrowType arrowType = new ArrowType(registry, parameters, registry.getNumberType());

    assertFalse(arrowType.hasAnyTemplateInternal());

    // Adding a templated return type does not change the result
    ArrowType arrowTypeWithTemplatedReturnType = new ArrowType(registry, parameters, registry.getFunctionType(registry.getNumberType(), registry.getArrayType(new UnknownType())));
    assertFalse(arrowTypeWithTemplatedReturnType.hasAnyTemplateInternal());

    // Adding a templated parameter type does change the result
    ArrowType arrowTypeWithTemplatedParameterType = new ArrowType(registry,
        registry.createParametersWithVarArgs(
            registry.getFunctionType(registry.createTemplateType(registry.getNumberType(), registry.getNumberType()), registry.getNumberType())),
        registry.getNumberType());
    assertTrue(arrowTypeWithTemplatedParameterType.hasAnyTemplateInternal());
  }
}