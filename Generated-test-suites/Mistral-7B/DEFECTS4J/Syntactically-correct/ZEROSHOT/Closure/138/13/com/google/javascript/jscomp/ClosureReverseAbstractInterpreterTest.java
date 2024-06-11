import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Type;
import com.google.javascript.jscomp.jstype.JSType;
import com.google.javascript.jscomp.jstype.JSTypeRegistry;
import com.google.javascript.jscomp.jstype.ObjectType;
import com.google.javascript.jscomp.jstype.Visitor;
import com.google.javascript.rhino.Node;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
  private static final ObjectType objectType = mock(ObjectType.class);
  private static final FunctionType functionType = mock(FunctionType.class);
  private static final Function<TypeRestriction, JSType> restrictToArrayVisitor =
      mock(Function.class);
  private static final Function<TypeRestriction, JSType> restrictToNotArrayVisitor =
      mock(Function.class);
  private static final Function<TypeRestriction, JSType> restrictToObjectVisitor =
      mock(Function.class);
  private static final Function<TypeRestriction, JSType> restrictToNotObjectVisitor =
      mock(Function.class);

  @org.junit.Before
  public void setUp() {
    when(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE)).thenReturn(
        new ObjectType("Array", null, null, null, null));
    when(typeRegistry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(
        objectType);
    when(typeRegistry.getNativeType(JSTypeNative.NULL_TYPE)).thenReturn(
        new ObjectType("Null", null, null, null, null));
    when(typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE)).thenReturn(
        functionType);

    when(restrictToArrayVisitor.apply(any(TypeRestriction.class)))
        .then(invocation -> invocation.getArgument(0).getType());

    when(restrictToNotArrayVisitor.apply(any(TypeRestriction.class)))
        .thenReturn(null);

    when(restrictToObjectVisitor.apply(any(TypeRestriction.class)))
        .then(invocation -> objectType);

    when(restrictToNotObjectVisitor.apply(any(TypeRestriction.class)))
        .thenReturn(null);
  }

  @org.junit.Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToArrayVisitor;

    JSType arrayType = visitor.visit(TYPE);
    assertSame(TYPE.getNativeType(JSTypeNative.ARRAY_TYPE), arrayType);

    JSType type = new ObjectType("Object", null, null, null, null);
    assertNull(visitor.visit(type));
  }

  @org.junit.Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToNotArrayVisitor;

    JSType arrayType = visitor.visit(TYPE.getNativeType(JSTypeNative.ARRAY_TYPE));
    assertNull(arrayType);

    JSType type = new ObjectType("Object", null, null, null, null);
    assertNull(visitor.visit(type));
  }

  @org.junit.Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToObjectVisitor;

    assertSame(functionType, visitor.visit(TYPE.getNativeType(JSTypeNative.OBJECT_TYPE)));

    JSType type = new ObjectType("Object", null, null, null, null);
    assertSame(type, visitor.visit(type));

    FunctionType functionType1 = new FunctionType(new ObjectType("Function", null, null, null, null), null);
    assertSame(functionType1, visitor.visit(functionType1));
  }

  @org.junit.Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToNotObjectVisitor;

    JSType arrayType = visitor.visit(TYPE.getNativeType(JSTypeNative.ARRAY_TYPE));
    assertNull(arrayType);

    JSType type = new ObjectType("Object", null, null, null, null);
    assertNull(visitor.visit(type));

    FunctionType functionType = new FunctionType(new ObjectType("Function", null, null, null, null), null);
    assertNull(visitor.visit(functionType));
  }

  @org.junit.Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);

    Node parameter = createNode("name");
    JSType type = new ObjectType("Object", null, null, null, null);
    TypeRestriction restriction = new TypeRestriction(type, true);

    FlowScope flowScope = interpreter.restrictParameter(parameter, type, null, restriction, true);
    assertNotNull(flowScope);
    assertSame(type, flowScope.getVariableType(parameter));
  }

  @org.junit.Test
  public void testRestrictParameterNullRestriction() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);

    Node parameter = createNode("name");
    JSType type = new ObjectType("Object", null, null, null, null);

    FlowScope flowScope = interpreter.restrictParameter(parameter, type, null, null, true);
    assertNull(flowScope);
  }

  @org.junit.Test
  public void testRestrictParameterFalseOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);

    Node parameter = createNode("name");
    JSType type = new ObjectType("Object", null, null, null, null);
    TypeRestriction restriction = new TypeRestriction(type, false);

    FlowScope flowScope = interpreter.restrictParameter(parameter, type, null, restriction, true);
    assertNull(flowScope);
  }

  @org.junit.Test
  public void testRestrictParameterNullType() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, typeRegistry);

    Node parameter = createNode("name");
    TypeRestriction restriction = new TypeRestriction(null, true);

    FlowScope flowScope = interpreter.restrictParameter(parameter, null, null, restriction, true);
    assertNull(flowScope);
  }

  private Node createNode(String name) {
    Map<String, Object> properties = ImmutableMap.of("callee", createNode("goog"),
        "arguments", createArrayNode(), "type", createNode("Name", name));
    return new Node("GetProperty", null, null, null, null, null, null, properties);
  }

  private Node createArrayNode() {
    return new Node("ArrayLiteral", null, null, null, null);
  }

  private void declareNameInScope(FlowScope scope, Node name, JSType type) {
    scope.declareVariable(name, type);
  }

  private JSType getTypeIfRefinable(Node node, FlowScope scope) {
    return null;
  }
}