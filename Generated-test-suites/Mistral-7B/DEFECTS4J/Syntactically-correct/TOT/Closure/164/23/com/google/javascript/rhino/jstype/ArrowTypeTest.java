import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;

public class ArrowTypeTest {

  private static JSTypeRegistry registry;
  private static ErrorReporter t;

  @org.junit.BeforeClass
  public static void setup() {
    registry = new JSTypeRegistry();
    t = ErrorReporter.NULL;
  }

  /**
   * Test constructor with no parameters or return type.
   */
  @org.junit.Test
  public void testEmptyArrowType() {
    Node params = null;
    JSType returnType = null;
    ArrowType arrowType = new ArrowType(registry, params, returnType);
    assertEquals("ArrowType", arrowType.getName());
    assertFalse("should not have parameters", arrowType.hasParameters());
    assertFalse("should not have a return type", arrowType.hasReturnType());
  }

  /**
   * Test constructor with parameters but no return type.
   */
  @org.junit.Test
  public void testArrowTypeWithParamsButNoReturnType() {
    Node params = createArray(createObject("parameterType"), 1);
    ArrowType arrowType = new ArrowType(registry, params, null);
    assertEquals("ArrowType", arrowType.getName());
    assertTrue("should have parameters", arrowType.hasParameters());
    assertEquals(params, arrowType.getParameters());
    assertFalse("should not have a return type", arrowType.hasReturnType());
  }

  /**
   * Test constructor with a return type but no parameters.
   */
  @org.junit.Test
  public void testArrowTypeWithReturnTypeButNoParams() {
    Node returnType = createObject("returnType");
    ArrowType arrowType = new ArrowType(registry, null, returnType, false);
    assertEquals("ArrowType", arrowType.getName());
    assertFalse("should not have parameters", arrowType.hasParameters());
    assertTrue("should have a return type", arrowType.hasReturnType());
    assertEquals(returnType, arrowType.getReturnType());
  }

  /**
   * Test constructor with parameters and a return type.
   */
  @org.junit.Test
  public void testArrowTypeWithParamsAndReturnType() {
    Node params = createArray(createObject("parameterType"), 1);
    Node returnType = createObject("returnType");
    ArrowType arrowType = new ArrowType(registry, params, returnType, false);
    assertEquals("ArrowType", arrowType.getName());
    assertTrue("should have parameters", arrowType.hasParameters());
    assertEquals(params, arrowType.getParameters());
    assertTrue("should have a return type", arrowType.hasReturnType());
    assertEquals(returnType, arrowType.getReturnType());
  }

  /**
   * Test constructor with parameters and inferred return type.
   */
  @org.junit.Test
  public void testArrowTypeWithParamsAndInferredReturnType() {
    Node params = createArray(createObject("parameterType"), 1);
    ArrowType arrowType = new ArrowType(registry, params, null, true);
    assertEquals("ArrowType", arrowType.getName());
    assertTrue("should have parameters", arrowType.hasParameters());
    assertEquals(params, arrowType.getParameters());
    assertFalse("should not have an explicit return type", arrowType.hasReturnType());
    assertNull("should not have a return type", arrowType.getReturnType());
  }

  /**
   * Test equals method.
   */
  @org.junit.Test
  public void testEquals() {
    Node firstParam = createObject("firstParameterType");
    Node secondParam = createObject("secondParameterType");
    Node firstReturn = createObject("firstReturnType");
    Node secondReturn = createObject("secondReturnType");

    ArrowType arrowType1 = new ArrowType(registry, createArray(firstParam, secondParam), firstReturn);
    ArrowType arrowType2 = new ArrowType(registry, createArray(firstParam, secondParam), firstReturn);
    ArrowType arrowType3 = new ArrowType(registry, createArray(firstParam, secondParam), secondReturn);
    ArrowType arrowType4 = new ArrowType(registry, null, firstReturn);

    assertTrue("should be equal", arrowType1.equals(arrowType2));
    assertFalse("should not be equal", arrowType1.equals(arrowType3));
    assertFalse("should not be equal", arrowType1.equals(arrowType4));
  }

  /**
   * Test hashCode method.
   */
  @org.junit.Test
   public void testHashCode() {
    Node firstParam = createObject("firstParameterType");
    Node secondParam = createObject("secondParameterType");
    Node firstReturn = createObject("firstReturnType");
    Node secondReturn = createObject("secondReturnType");

    ArrowType arrowType1 = new ArrowType(registry, createArray(firstParam, secondParam), firstReturn);
    ArrowType arrowType2 = new ArrowType(registry, createArray(firstParam, secondParam), firstReturn);
    ArrowType arrowType3 = new ArrowType(registry, createArray(firstParam, secondParam), secondReturn);
    ArrowType arrowType4 = new ArrowType(registry, null, firstReturn);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    int hashCode3 = arrowType3.hashCode();
    int hashCode4 = arrowType4.hashCode();

    assertEquals("should be same", hashCode1, hashCode2);
    assertNotEquals("should be different", hashCode1, hashCode3);
    assertNotEquals("should be different", hashCode1, hashCode4);
  }

  /**
   * Test isSubtype method.
   */
  @org.junit.Test
  public void testIsSubtype() {
    Node firstParam1 = createObject("firstParameterType1");
    Node secondParam1 = createObject("secondParameterType1");
    Node firstParam2 = createObject("firstParameterType2");
    Node secondParam2 = createObject("secondParameterType2");
    Node firstReturn = createObject("firstReturnType");
    Node secondReturn = createObject("secondReturnType");

    ArrowType arrowType1 = new ArrowType(registry, createArray(firstParam1, secondParam1), firstReturn);
    ArrowType arrowType2 = new ArrowType(registry, createArray(firstParam2, secondParam2), firstReturn);
    ArrowType arrowType3 = new ArrowType(registry, createArray(firstParam1, secondParam1), secondReturn);

    // arrowType1 is subtype of arrowType2 because return type is covariant
    assertTrue("should be subtype", arrowType1.isSubtype(arrowType2));
    // arrowType1 is not subtype of arrowType3 because parameter types are not contravariant
    assertFalse("should not be subtype", arrowType1.isSubtype(arrowType3));
  }

  /**
   * Test hasEqualParameters method.
   */
  @org.junit.Test
  public void testHasEqualParameters() {
    Node firstParam1 = createObject("firstParameterType1");
    Node secondParam1 = createObject("secondParameterType1");
    Node firstParam2 = createObject("firstParameterType2");
    Node secondParam2 = createObject("secondParameterType2");

    ArrowType arrowType1 = new ArrowType(registry, createArray(firstParam1, secondParam1), null);
    ArrowType arrowType2 = new ArrowType(registry, createArray(firstParam2, secondParam2), null);
    ArrowType arrowType3 = new ArrowType(registry, createArray(firstParam1, secondParam1), null);

    // arrowType1 and arrowType3 have equal parameters, so they should be equal
    assertTrue("should be equal", arrowType1.hasEqualParameters(arrowType3));
    // arrowType1 and arrowType2 do not have equal parameters, so they should not be equal
    assertFalse("should not be equal", arrowType1.hasEqualParameters(arrowType2));
  }
}