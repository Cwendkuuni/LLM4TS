import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeVisitor;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;

import org.junit.Test;

public class ArrowTypeTest {
  @Test
  public void testArrowTypeWithEmptyParamsAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node emptyParameters = Node.NULL;
    ArrowType arrowType = new ArrowType(registry, emptyParameters, UNKNOWN_TYPE);

    arrowType.resolveInternal(t, null);

    assertEquals(UNKNOWN_TYPE, arrowType.getReturnType());
  }

  @Test
  public void testArrowTypeWithOneParamAndStringReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node oneParameterNode = Node.newString("param");
    JSType paramType = new StringType(registry, oneParameterNode);
    Node parameters = Node.sequence(oneParameterNode);

    ArrowType arrowType = new ArrowType(registry, parameters, new StringType(registry, Node.NULL));

    arrowType.resolveInternal(t, null);

    assertEquals(paramType, arrowType.getParameters().get(0).getJSType());
    assertEquals(String.class, arrowType.getReturnType().getClass());
  }

  @Test
  public void testArrowTypeWithVarArgsAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node varArgsNode = Node.sequence(
        Node.sequence(Node.NEW_IDENT("args"), Node.IDENT("length")),
        Node.IDENT("..."));

    ArrowType arrowType = new ArrowType(registry, Node.NULL, UNKNOWN_TYPE, true);
    arrowType.parameters = Node.sequence(
        Node.sequence(Node.IDENT("params"), Node.IDENT("length")),
        varArgsNode);

    arrowType.resolveInternal(t, null);

    assertTrue(arrowType.getParameters().get(1).isVarArgs());
  }

  @Test
  public void testArrowTypeWithOptionalParamAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node optionalParamNode = Node.sequence(
        Node.IDENT("param"),
        Node.IDENT("optional"));

    Node parameters = Node.sequence(
        Node.sequence(Node.IDENT("requiredParam"), Node.NULL),
        optionalParamNode);

    ArrowType arrowType = new ArrowType(registry, parameters, UNKNOWN_TYPE);

    arrowType.resolveInternal(t, null);

    assertFalse(arrowType.getParameters().get(1).isOptionalArg());
  }

  @Test
  public void testArrowTypeWithOptionalParamAndStringReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node optionalParamNode = Node.sequence(
        Node.IDENT("param"),
        Node.IDENT("optional"));

    Node parameters = Node.sequence(
        Node.sequence(Node.IDENT("requiredParam"), Node.NULL),
        optionalParamNode);

    ArrowType arrowType = new ArrowType(registry, parameters, new StringType(registry, Node.NULL));

    arrowType.resolveInternal(t, null);

    assertTrue(arrowType.getParameters().get(1).isOptionalArg());
    assertEquals(String.class, arrowType.getReturnType().getClass());
  }

  @Test
  public void testArrowTypeWithTwoParamsAndNumberReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node param1Node = Node.newInt(1);
    Node param2Node = Node.newInt(2);
    Node parameters = Node.sequence(param1Node, param2Node);

    ArrowType arrowType = new ArrowType(registry, parameters, new NumberType(registry, Node.NULL));

    arrowType.resolveInternal(t, null);

    assertEquals(Number.class, arrowType.getReturnType().getClass());
  }

  @Test
  public void testArrowTypeWithTwoParamsAndFunctionReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node param1Node = Node.IDENT("param1");
    Node param2Node = Node.IDENT("param2");
    Node parameters = Node.sequence(param1Node, param2Node);

    FunctionType functionType = new FunctionType(registry, null, null, null, null);

    ArrowType arrowType = new ArrowType(registry, parameters, functionType);

    arrowType.resolveInternal(t, null);

    assertEquals(functionType, arrowType.getReturnType());
  }

  @Test
  public void testArrowTypeWithTwoParamsAndStringReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node param1Node = Node.IDENT("param1");
    Node param2Node = Node.IDENT("param2");
    Node parameters = Node.sequence(param1Node, param2Node);

    ArrowType arrowType = new ArrowType(registry, parameters, new StringType(registry, Node.NULL));

    arrowType.resolveInternal(t, null);

    assertEquals(String.class, arrowType.getReturnType().getClass());
  }

  @Test
  public void testArrowTypeWithTwoParamsAndWithDifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node param1Node = Node.IDENT("param1");
    Node param2Node = Node.IDENT("param2");
    Node parameters = Node.sequence(param1Node, param2Node);

    ArrowType stringArrowType = new ArrowType(registry, parameters, new StringType(registry, Node.NULL));

    ArrowType numberArrowType = new ArrowType(registry, parameters, new NumberType(registry, Node.NULL));

    stringArrowType.resolveInternal(t, null);
    numberArrowType.resolveInternal(t, null);

    assertFalse(stringArrowType.isSubtype(numberArrowType));
    assertFalse(numberArrowType.isSubtype(stringArrowType));
  }

  @Test
  public void testArrowTypeWithVarArgsAndStringReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node varArgsNode = Node.sequence(
        Node.sequence(Node.IDENT("args"), Node.IDENT("length")),
        Node.IDENT("..."));

    ArrowType arrowType = new ArrowType(registry, Node.NULL, new StringType(registry, Node.NULL), true);
    arrowType.parameters = Node.sequence(
        Node.sequence(Node.IDENT("params"), Node.IDENT("length")),
        varArgsNode);

    arrowType.resolveInternal(t, null);

    assertTrue(arrowType.getParameters().get(1).isVarArgs());
    assertEquals(String.class, arrowType.getReturnType().getClass());
  }

  @Test
  public void testArrowTypeWithTwoParamsAndDifferentParameterTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node param1Node1 = Node.IDENT("param1Int");
    Node param1Node2 = Node.IDENT("param1String");
    Node param2Node = Node.IDENT("param2");

    Node parameters1 = Node.sequence(param1Node1, param2Node);
    Node parameters2 = Node.sequence(param1Node2, param2Node);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, new NumberType(registry, Node.NULL));
    ArrowType arrowType2 = new ArrowType(registry, parameters2, new StringType(registry, Node.NULL));

    arrowType1.resolveInternal(t, null);
    arrowType2.resolveInternal(t, null);

    assertFalse(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeWithTwoParamsAndOneOptionalParamAndStringReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node optionalParamNode = Node.sequence(
        Node.IDENT("param"),
        Node.IDENT("optional"));

    Node paramsNode = Node.sequence(Node.IDENT("requiredParam"), optionalParamNode);

    ArrowType arrowType = new ArrowType(registry, paramsNode, new StringType(registry, Node.NULL));

    arrowType.resolveInternal(t, null);

    assertTrue(arrowType.getParameters().get(1).isOptionalArg());
    assertEquals(String.class, arrowType.getReturnType().getClass());
  }

  @Test
  public void testArrowTypeWithTwoParamsAndOneOptionalParamAndFunctionReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node optionalParamNode = Node.sequence(
        Node.IDENT("param"),
        Node.IDENT("optional"));

    Node paramsNode = Node.sequence(Node.IDENT("requiredParam"), optionalParamNode);

    FunctionType functionType = new FunctionType(registry, null, null, null, null);

    ArrowType arrowType = new ArrowType(registry, paramsNode, functionType);

    arrowType.resolveInternal(t, null);

    assertTrue(arrowType.getParameters().get(1).isOptionalArg());
    assertEquals(functionType, arrowType.getReturnType());
  }

  @Test
  public void testArrowTypeWithTwoParamsAndOneOptionalParamAndDifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    Node optionalParamNode = Node.sequence(
        Node.IDENT("param"),
        Node.IDENT("optional"));

    Node paramsNode = Node.sequence(Node.IDENT("requiredParam"), optionalParamNode);

    ArrowType stringArrowType = new ArrowType(registry, paramsNode, new StringType(registry, Node.NULL));

    ArrowType numberArrowType = new ArrowType(registry, paramsNode, new NumberType(registry, Node.NULL));

    stringArrowType.resolveInternal(t, null);
    numberArrowType.resolveInternal(t, null);

    assertFalse(stringArrowType.isSubtype(numberArrowType));
    assertFalse(numberArrowType.isSubtype(stringArrowType));
  }

  @Test
  public void testArrowTypeWithIncompleteParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    ArrowType arrowType = new ArrowType(registry, Node.NULL, new StringType(registry, Node.NULL));

    try {
      arrowType.resolveInternal(t, null);
      fail("Expected an exception");
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testArrowTypeWithIncompleteParametersAndUnknownReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();

    ArrowType arrowType = new ArrowType(registry, Node.NULL, UNKNOWN_TYPE);

    try {
      arrowType.resolveInternal(t, null);
      fail("Expected an exception");
    } catch (Exception e) {
      // Expected
    }
  }
}