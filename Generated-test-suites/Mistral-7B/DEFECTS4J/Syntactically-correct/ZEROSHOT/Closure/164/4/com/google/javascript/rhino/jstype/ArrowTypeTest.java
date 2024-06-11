import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.TestHelper;

import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowType() {
    JSTypeRegistry registry = TestHelper.newRegistry();
    Node node = TestHelper.createArrowTypeNode(registry, new Node[] {
        TestHelper.createParameterNode(registry, TypeOf.INT),
        TestHelper.createParameterNode(registry, TypeOf.BOOLEAN)
    }, TypeOf.VOID);

    ArrowType arrowType = new ArrowType(registry, node.getParameters(), null);

    JSType expected = registry.createFunctionType(new JSType[] {
        TypeOf.INT,
        TypeOf.BOOLEAN
    }, TypeOf.VOID);

    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = TestHelper.newRegistry();
    Node node = TestHelper.createArrowTypeNode(registry, new Node[] {
        TestHelper.createParameterNode(registry, TypeOf.INT)
    });

    ArrowType arrowType = new ArrowType(registry, node.getParameters(), null, true);

    JSType expected = registry.createFunctionType(new JSType[] {}, TypeOf.UNKNOWN);

    assertEquals(expected, arrowType);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = TestHelper.newRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, TypeOf.STRING, false);
    ArrowType arrowType2 = new ArrowType(registry, null, TypeOf.INT, false);
    ArrowType arrowType3 = new ArrowType(registry, null, TypeOf.BOOLEAN, false);
    ArrowType arrowType4 = new ArrowType(registry, null, TypeOf.VOID, false);

    ArrowType arrowType5 = new ArrowType(registry, null, TypeOf.STRING, true);

    ArrowType arrowType6 = new ArrowType(registry, TestHelper.createParametersNode(registry), TypeOf.STRING, false);
    ArrowType arrowType7 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT), TypeOf.STRING, false);
    ArrowType arrowType8 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT, TypeOf.BOOLEAN), TypeOf.STRING, false);
    ArrowType arrowType9 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT, TypeOf.BOOLEAN, TypeOf.INT), TypeOf.STRING, false);
    ArrowType arrowType10 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT, TypeOf.BOOLEAN, TypeOf.INT, TypeOf.INT), TypeOf.STRING, false);

    ArrowType arrowType11 = new ArrowType(registry, TestHelper.createParametersNode(registry), TypeOf.STRING, true);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType2.isSubtype(arrowType1));
    assertFalse(arrowType2.isSubtype(arrowType3));
    assertFalse(arrowType2.isSubtype(arrowType4));
    assertTrue(arrowType3.isSubtype(arrowType4));
    assertFalse(arrowType4.isSubtype(arrowType1));
    assertFalse(arrowType4.isSubtype(arrowType2));
    assertFalse(arrowType4.isSubtype(arrowType3));
    assertFalse(arrowType5.isSubtype(arrowType1));
    assertFalse(arrowType5.isSubtype(arrowType2));
    assertFalse(arrowType5.isSubtype(arrowType3));
    assertFalse(arrowType5.isSubtype(arrowType4));
    assertFalse(arrowType5.isSubtype(arrowType6));
    assertFalse(arrowType5.isSubtype(arrowType7));
    assertFalse(arrowType5.isSubtype(arrowType8));
    assertFalse(arrowType5.isSubtype(arrowType9));
    assertFalse(arrowType5.isSubtype(arrowType10));
    assertFalse(arrowType6.isSubtype(arrowType1));
    assertFalse(arrowType6.isSubtype(arrowType2));
    assertFalse(arrowType6.isSubtype(arrowType3));
    assertFalse(arrowType6.isSubtype(arrowType4));
    assertFalse(arrowType6.isSubtype(arrowType5));
    assertTrue(arrowType6.isSubtype(arrowType6));
    assertTrue(arrowType6.isSubtype(arrowType7));
    assertTrue(arrowType7.isSubtype(arrowType6));
    assertTrue(arrowType7.isSubtype(arrowType8));
    assertFalse(arrowType7.isSubtype(arrowType9));
    assertTrue(arrowType8.isSubtype(arrowType7));
    assertTrue(arrowType8.isSubtype(arrowType9));
    assertFalse(arrowType8.isSubtype(arrowType10));
    assertFalse(arrowType9.isSubtype(arrowType6));
    assertFalse(arrowType9.isSubtype(arrowType7));
    assertFalse(arrowType9.isSubtype(arrowType8));
    assertFalse(arrowType9.isSubtype(arrowType10));
    assertTrue(arrowType9.isSubtype(arrowType9));
    assertTrue(arrowType10.isSubtype(arrowType9));
    assertFalse(arrowType10.isSubtype(arrowType11));
    assertFalse(arrowType11.isSubtype(arrowType1));
    assertFalse(arrowType11.isSubtype(arrowType2));
    assertFalse(arrowType11.isSubtype(arrowType3));
    assertFalse(arrowType11.isSubtype(arrowType4));
    assertFalse(arrowType11.isSubtype(arrowType5));
    assertFalse(arrowType11.isSubtype(arrowType6));
    assertFalse(arrowType11.isSubtype(arrowType7));
    assertFalse(arrowType11.isSubtype(arrowType8));
    assertFalse(arrowType11.isSubtype(arrowType9));
    assertFalse(arrowType11.isSubtype(arrowType10));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = TestHelper.newRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, TypeOf.STRING, false);
    ArrowType arrowType2 = new ArrowType(registry, null, TypeOf.STRING, false);

    ArrowType arrowType3 = new ArrowType(registry, TestHelper.createParametersNode(registry), TypeOf.STRING, false);
    ArrowType arrowType4 = new ArrowType(registry, TestHelper.createParametersNode(registry), TypeOf.STRING, false);

    ArrowType arrowType5 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT), TypeOf.STRING, false);
    ArrowType arrowType6 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT), TypeOf.STRING, false);

    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType3.hasEqualParameters(arrowType3));
    assertTrue(arrowType3.hasEqualParameters(arrowType4));
    assertFalse(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType5));
    assertFalse(arrowType3.hasEqualParameters(arrowType5));
  }

  @Test
  public void testArrowTypeIsEquivalentTo() {
    JSTypeRegistry registry = TestHelper.newRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, TypeOf.STRING, false);
    ArrowType arrowType2 = new ArrowType(registry, null, TypeOf.STRING, false);

    ArrowType arrowType3 = new ArrowType(registry, TestHelper.createParametersNode(registry), TypeOf.STRING, false);
    ArrowType arrowType4 = new ArrowType(registry, TestHelper.createParametersNode(registry), TypeOf.STRING, false);

    ArrowType arrowType5 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT), TypeOf.STRING, false);
    ArrowType arrowType6 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT), TypeOf.STRING, false);

    assertTrue(arrowType1.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType3.isEquivalentTo(arrowType3));
    assertTrue(arrowType3.isEquivalentTo(arrowType4));
    assertFalse(arrowType1.isEquivalentTo(arrowType3));
    assertFalse(arrowType1.isEquivalentTo(arrowType5));
    assertFalse(arrowType3.isEquivalentTo(arrowType5));
  }

  @Test
  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = TestHelper.newRegistry();

    ArrowType arrowType1 = new ArrowType(registry, null, TypeOf.STRING, false);
    ArrowType arrowType2 = new ArrowType(registry, null, TypeOf.STRING, false);

    ArrowType arrowType3 = new ArrowType(registry, TestHelper.createParametersNode(registry), TypeOf.STRING, false);
    ArrowType arrowType4 = new ArrowType(registry, TestHelper.createParametersNode(registry), TypeOf.STRING, false);

    ArrowType arrowType5 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT), TypeOf.STRING, false);
    ArrowType arrowType6 = new ArrowType(registry, TestHelper.createParametersNode(registry, TypeOf.INT), TypeOf.STRING, false);

    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType1.hashCode();

    int hashCode3 = arrowType2.hashCode();
    int hashCode4 = arrowType2.hashCode();

    int hashCode5 = arrowType3.hashCode();
    int hashCode6 = arrowType3.hashCode();

    int hashCode7 = arrowType4.hashCode();
    int hashCode8 = arrowType4.hashCode();

    int hashCode9 = arrowType5.hashCode();
    int hashCode10 = arrowType5.hashCode();

    assertEquals(hashCode1, hashCode1);
    assertEquals(hashCode1, hashCode2);
    assertEquals(hashCode3, hashCode3);
    assertEquals(hashCode3, hashCode4);
    assertNotEquals(hashCode1, hashCode3);
    assertNotEquals(hashCode1, hashCode5);
    assertNotEquals(hashCode3, hashCode5);
    assertNotEquals(hashCode5, hashCode6);
    assertNotEquals(hashCode5, hashCode9);
    assertNotEquals(hashCode9, hashCode10);
  }
}