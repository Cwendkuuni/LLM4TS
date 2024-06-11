import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ClosureReverseAbstractInterpreterTest {
  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testIsDefTrue() {
    JSType type = getNativeType(ObjectType.INSTANCE);
    FlowScope scope = interpreter.createInitialContext();
    when(typeRegistry.getTopType()).thenReturn(type);

    Node condition = createNode("goog.isDef(x)");
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);

    JSType expectedType = getNativeType(ObjectType.INSTANCE);
    assertEquals(expectedType, result.getEntryScope().getTypeOfName("x"));
  }

  @Test
  public void testIsDefFalse() {
    JSType type = getNativeType(ObjectType.INSTANCE);
    FlowScope scope = interpreter.createInitialContext();
    when(typeRegistry.getTopType()).thenReturn(type);

    Node condition = createNode("goog.isDef(null)");
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, result.getEntryScope().getTypeOfName("x"));
  }

  @Test
  public void testIsDefUndefined() {
    JSType type = getNativeType(ObjectType.INSTANCE);
    FlowScope scope = interpreter.createInitialContext();
    when(typeRegistry.getTopType()).thenReturn(type);

    Node condition = createNode("goog.isDef(undefined)");
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, result.getEntryScope().getTypeOfName("x"));
  }

  @Test
  public void testIsDefNull() {
    JSType type = getNativeType(ObjectType.INSTANCE);
    FlowScope scope = interpreter.createInitialContext();
    when(typeRegistry.getTopType()).thenReturn(type);

    Node condition = createNode("goog.isDef(null)");
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, false);

    JSType expectedType = getNativeType(VOID_TYPE);
    assertEquals(expectedType, result.getEntryScope().getTypeOfName("x"));
  }

  @Test
  public void testIsDefFunction() {
    JSType functionType = getFunctionType(new String[]{"x"}, VOID_TYPE);
    JSType type = getNativeType(ObjectType.INSTANCE);
    FlowScope scope = interpreter.createInitialContext();
    when(typeRegistry.getTopType()).thenReturn(type);

    Node condition = createNode("goog.isDef(function() {})");
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, scope, true);

    JSType expectedType = functionType;
    assertEquals(expectedType, result.getEntryScope().getTypeOfName("x"));
  }

  private Node createNode(String expression) {
    Node node = new Node();
    node.setType(Node.CALL);
    node.setFirstChild(Node.newName("goog"));
    node.setLastChild(Node.newString(expression));
    return node;
  }

  private FunctionType getFunctionType(String[] params, JSType returnType) {
    return new FunctionType(params, returnType);
  }
}