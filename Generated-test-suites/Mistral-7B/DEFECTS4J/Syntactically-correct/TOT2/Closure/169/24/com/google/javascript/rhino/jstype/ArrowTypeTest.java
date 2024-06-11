import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

public class ArrowTypeTest {

  @org.junit.Test
  public void testArrowTypeBasicFunctionality() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSType.INT));
    JSType returnType = getNativeType(JSType.STRING);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.isSubtype(arrowType));
    assertFalse(arrowType.isSubtype(new FunctionType(registry, null, getNativeType(JSType.VOID))));

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType, true);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType, false);
    assertFalse(arrowType1.equals(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, null, null);
    assertFalse(arrowType.isSubtype(arrowType3));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testArrowTypeNullParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, getNativeType(JSType.STRING));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testArrowTypeNullReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(getNativeType(JSType.INT));
    ArrowType arrowType = new ArrowType(registry, parameters, null);
  }

  @org.junit.Test
  public void testArrowTypeParameterTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = registry.createParametersWithVarArgs(
        new ArrayType(registry, getNativeType(JSType.INT)),
        new ArrayType(registry, getNativeType(JSType.BOOLEAN))
    );
    JSType returnType = getNativeType(JSType.STRING);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType, true);
    assertEquals(arrowType, arrowType1);

    ArrowType arrowType2 = new ArrowType(registry, new ArrayType(registry, getNativeType(JSType.INT)), returnType);
    assertFalse(arrowType.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, new ArrayType(registry, getNativeType(JSType.BOOLEAN)), returnType);
    assertFalse(arrowType.isSubtype(arrowType3));

    ArrowType arrowType4 = new ArrowType(registry, new ArrayType(registry, getNativeType(JSType.INT)), getNativeType(JSType.VOID));
    assertFalse(arrowType.isSubtype(arrowType4));
  }

  @org.junit.Test
  public void testArrowTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Node parameters1 = registry.createParametersWithVarArgs(getNativeType(JSType.INT));
    Node parameters2 = registry.createParametersWithVarArgs(getNativeType(JSType.NUMBER));

    JSType returnType1 = getNativeType(JSType.INT);
    JSType returnType2 = getNativeType(JSType.NUMBER);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue(arrowType1.isSubtype(arrowType2));

    JSType returnType3 = getNativeType(JSType.STRING);
    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType3);
    assertFalse(arrowType1.isSubtype(arrowType3));

    ArrowType arrowType4 = new ArrowType(registry, new ArrayType(registry, getNativeType(JSType.STRING)), returnType1);
    assertFalse(arrowType1.isSubtype(arrowType4));

    ArrowType arrowType5 = new ArrowType(registry, parameters1, returnType2);
    assertTrue(arrowType1.isSubtype(arrowType5));
  }
}