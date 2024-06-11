import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.node.Node;

public class ArrowTypeTest {

  @org.junit.Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    JSType returnType = Node.newNull();

    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType, true);

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(Node.newNumberLiteral(1)), Node.newNumberType());
    ArrowType arrowType3 = new ArrowType(registry, Node.newList(Node.newStringLiteral("str"), Node.newList(Node.newNumberLiteral(1), Node.newStringType("string"))), Node.newBooleanType());

    assertEquals(arrowType, arrowType1);
    assertNotEquals(arrowType, arrowType2);
    assertNotEquals(arrowType, arrowType3);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testArrowTypeInvalidConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newList();
    JSType returnType = Node.newNull();

    ArrowType arrowType = new ArrowType(registry, parameters, null, false);
  }

  @org.junit.Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.newList(Node.newNumberLiteral(1), Node.newNumberLiteral(2));
    JSType returnType1 = Node.newNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = Node.newList(Node.newNumberLiteral(1));
    JSType returnType2 = Node.newBooleanType();
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    Node parameters3 = Node.newList();
    JSType returnType3 = Node.newStringType();
    ArrowType arrowType3 = new ArrowType(registry, parameters3, returnType3);

    ArrowType arrowType4 = new ArrowType(registry, Node.newList(), Node.newNullType());

    ArrowType arrowType5 = new ArrowType(registry, Node.newList(Node.newStringLiteral("str"), Node.newList(Node.newNumberLiteral(1), Node.newStringType("string"))), Node.newBooleanType());

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType1.isSubtype(arrowType3));
    assertFalse(arrowType1.isSubtype(arrowType4));
    assertFalse(arrowType1.isSubtype(arrowType5));
    assertFalse(arrowType2.isSubtype(arrowType1));
    assertFalse(arrowType2.isSubtype(arrowType3));
    assertFalse(arrowType2.isSubtype(arrowType4));
    assertFalse(arrowType2.isSubtype(arrowType5));
    assertFalse(arrowType3.isSubtype(arrowType1));
    assertFalse(arrowType3.isSubtype(arrowType2));
    assertFalse(arrowType3.isSubtype(arrowType4));
    assertFalse(arrowType3.isSubtype(arrowType5));
    assertFalse(arrowType4.isSubtype(arrowType1));
    assertFalse(arrowType4.isSubtype(arrowType2));
    assertFalse(arrowType4.isSubtype(arrowType3));
    assertFalse(arrowType4.isSubtype(arrowType5));
    assertFalse(arrowType5.isSubtype(arrowType1));
    assertFalse(arrowType5.isSubtype(arrowType2));
    assertFalse(arrowType5.isSubtype(arrowType3));
    assertFalse(arrowType5.isSubtype(arrowType4));
  }

  @org.junit.Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.newList(Node.newNumberLiteral(1), Node.newNumberLiteral(2));
    JSType returnType1 = Node.newNumberType();

    Node parameters2 = Node.newList(Node.newNumberLiteral(1), Node.newNumberLiteral(2));
    JSType returnType2 = Node.newNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    Node parameters3 = Node.newList(Node.newNumberLiteral(1), Node.newNumberLiteral(3));
    JSType returnType3 = Node.newNumberType();
    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType3);

    Node parameters4 = Node.newList(Node.newNumberLiteral(1), Node.newNumberLiteral(2), Node.newStringLiteral("str"));
    JSType returnType4 = Node.newStringType();
    ArrowType arrowType4 = new ArrowType(registry, parameters4, returnType4);

    Node parameters5 = Node.newList(Node.newNumberLiteral(1), Node.newNumberLiteral(2), Node.newStringLiteral("str"), Node.newNumberLiteral(3));
    JSType returnType5 = Node.newStringType();
    ArrowType arrowType5 = new ArrowType(registry, parameters5, returnType5);

    assertTrue(arrowType1.hasEqualParameters(arrowType1, true));
    assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType3, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType1.hasEqualParameters(arrowType5, true));
    assertFalse(arrowType2.hasEqualParameters(arrowType1, true));
    assertFalse(arrowType2.hasEqualParameters(arrowType3, true));
    assertFalse(arrowType2.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType2.hasEqualParameters(arrowType5, true));
    assertFalse(arrowType3.hasEqualParameters(arrowType1, true));
    assertFalse(arrowType3.hasEqualParameters(arrowType2, true));
    assertFalse(arrowType3.hasEqualParameters(arrowType4, true));
    assertFalse(arrowType3.hasEqualParameters(arrowType5, true));
    assertFalse(arrowType4.hasEqualParameters(arrowType1, true));
    assertFalse(arrowType4.hasEqualParameters(arrowType2, true));
    assertFalse(arrowType4.hasEqualParameters(arrowType3, true));
    assertFalse(arrowType4.hasEqualParameters(arrowType5, true));
    assertFalse(arrowType5.hasEqualParameters(arrowType1, true));
    assertFalse(arrowType5.hasEqualParameters(arrowType2, true));
    assertFalse(arrowType5.hasEqualParameters(arrowType3, true));
    assertFalse(arrowType5.hasEqualParameters(arrowType4, true));
  }
}