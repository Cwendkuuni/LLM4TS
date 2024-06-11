import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

  private JSTypeRegistry registry;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeWithParametersAndReturnType() {
    Node parameters = Node.newList(
        Node.newIdent("x"),
        Node.newIdent("y")
    );
    JSType returnType = registry.createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType expected = new ArrowType(registry, parameters, returnType, false);
    assertEquals(expected, arrowType);

    // Test equals
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    assertTrue(arrowType1.equals(arrowType2));
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

    // Test isSubtype
    ArrowType arrowType3 = new ArrowType(registry, Node.NULL, Node.NULL);
    assertFalse(arrowType.isSubtype(arrowType3));

    JSType arrowType4 = new ArrowType(registry, Node.NULL, returnType);
    assertTrue(arrowType.isSubtype(arrowType4));

    JSType arrowType5 = new ArrowType(registry, parameters, Node.NULL);
    assertTrue(arrowType5.isSubtype(arrowType));

    JSTypeFunctionType functionType = new JSTypeFunctionType(
        registry, registry.createParametersWithVarArgs(JSTypeNative.VOID),
        JSTypeNative.VOID);
    assertFalse(arrowType.isSubtype(functionType));

    JSType arrowType6 = new ArrowType(registry, parameters, returnType, true);
    assertTrue(arrowType.isSubtype(arrowType6));

    // Test hasEqualParameters
    ArrowType arrowType7 = new ArrowType(registry, Node.NULL, returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType7, false));

    ArrowType arrowType8 = new ArrowType(registry, Node.NULL, Node.NULL);
    assertTrue(arrowType.hasEqualParameters(arrowType8, false));

    ArrowType arrowType9 = new ArrowType(registry, parameters, Node.NULL, true);
    assertTrue(arrowType.hasEqualParameters(arrowType9, false));

    ArrowType arrowType10 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newIdent("y")),
        returnType
    );
    assertTrue(arrowType.hasEqualParameters(arrowType10, true));

    // Test checkArrowEquivalenceHelper
    ArrowType arrowType11 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newIdent("y")),
        returnType,
        true
    );
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowType11, false));

    ArrowType arrowType12 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newIdent("y"),
        Node.newIdent("z")
    ), returnType);
    assertFalse(arrowType.checkArrowEquivalenceHelper(arrowType11, false));
  }

  @Test
  public void testArrowTypeWithUnknownType() {
    Node parameters = Node.NULL;
    ArrowType arrowType = new ArrowType(registry, parameters, Node.NULL);

    JSType expected = new ArrowType(registry, parameters, Node.NULL, true);
    assertEquals(expected, arrowType);

    // Test isSubtype
    ArrowType arrowType1 = new ArrowType(registry, Node.NULL, Node.NULL);
    assertFalse(arrowType.isSubtype(arrowType1));

    ArrowType arrowType2 = new ArrowType(registry, Node.NULL, Node.NULL, true);
    assertTrue(arrowType.isSubtype(arrowType2));

    // Test hasEqualParameters
    ArrowType arrowType3 = new ArrowType(registry, Node.NULL, Node.NULL);
    assertTrue(arrowType.hasEqualParameters(arrowType3, true));

    // Test checkArrowEquivalenceHelper
    ArrowType arrowType4 = new ArrowType(registry, Node.NULL, Node.NULL, true);
    assertTrue(arrowType.checkArrowEquivalenceHelper(arrowType4, true));
  }

  @Test
  public void testArrowTypeWithVarArgs() {
    Node parameters = Node.newList(
        Node.newIdent("x"),
        Node.newIdent("y"),
        Node.newIdent("args")
    );
    JSType returnType = registry.createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType expected = new ArrowType(registry, parameters, returnType, false);
    assertEquals(expected, arrowType);

    // Test hasEqualParameters
    ArrowType arrowType1 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newIdent("y")
    ), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType1, false));

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newIdent("y"),
        Node.newIdent("args")
    ), returnType);
    assertTrue(arrowType.hasEqualParameters(arrowType2, true));

    ArrowType arrowType3 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newIdent("y"),
        Node.newList(Node.newIdent("arg1"), Node.newIdent("arg2"))
    ), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType3, false));

    ArrowType arrowType4 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newIdent("y"),
        Node.newList(Node.newIdent("arg1"), Node.newIdent("arg2"), Node.newIdent("arg3"))
    ), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType4, false));

    ArrowType arrowType5 = new ArrowType(registry, parameters, Node.NULL, true);
    assertTrue(arrowType.hasEqualParameters(arrowType5, false));
  }

  @Test
  public void testArrowTypeWithOptionalArgs() {
    Node parameters = Node.newList(
        Node.newIdent("x"),
        Node.newOptionalArg(Node.newIdent("y"))
    );
    JSType returnType = registry.createNumberType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType expected = new ArrowType(registry, parameters, returnType, false);
    assertEquals(expected, arrowType);

    // Test isSubtype
    ArrowType arrowType1 = new ArrowType(registry, Node.NULL, Node.NULL);
    assertFalse(arrowType.isSubtype(arrowType1));

    ArrowType arrowType2 = new ArrowType(registry, Node.newList(
        Node.newIdent("x")
    ), returnType);
    assertTrue(arrowType.isSubtype(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newOptionalArg(Node.newIdent("y"))
    ), returnType);
    assertTrue(arrowType.isSubtype(arrowType3));

    ArrowType arrowType4 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newOptionalArg(Node.newIdent("z"))
    ), returnType);
    assertFalse(arrowType.isSubtype(arrowType4));

    // Test hasEqualParameters
    ArrowType arrowType5 = new ArrowType(registry, Node.newList(
        Node.newIdent("x")
    ), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType5, false));

    ArrowType arrowType6 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newOptionalArg(Node.newIdent("y"))
    ), returnType);
    assertTrue(arrowType.hasEqualParameters(arrowType6, true));

    ArrowType arrowType7 = new ArrowType(registry, Node.newList(
        Node.newIdent("x"),
        Node.newOptionalArg(Node.newIdent("y")),
        Node.newIdent("z")
    ), returnType);
    assertFalse(arrowType.hasEqualParameters(arrowType7, false));
  }
}