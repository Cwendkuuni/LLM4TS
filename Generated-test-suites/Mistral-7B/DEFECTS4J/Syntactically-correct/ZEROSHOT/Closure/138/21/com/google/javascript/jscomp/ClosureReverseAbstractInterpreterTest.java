import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType arrayType = getNativeType(ARRAY_TYPE);
    JSType intType = typeRegistry.getBuiltins().getIntType();

    Node condition = makeNode("goog.isArray", new Node[] {
        makeNode("[]")
    });

    FlowScope blindScope = new FlowScope();

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType result = scope.getVariable("goog.isArray")
        .getType().getDefinition();

    assertEquals(arrayType, result);
  }

  @Test
  public void testRestrictToNotArray() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType intType = typeRegistry.getBuiltins().getIntType();

    Node condition = makeNode("goog.isArray", new Node[] {
        makeNode("{}")
    });

    FlowScope blindScope = new FlowScope();

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType result = scope.getVariable("goog.isArray")
        .getType().getDefinition();

    assertNull(result);
  }

  @Test
  public void testRestrictToObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    JSType objectType = getNativeType(OBJECT_TYPE);

    Node condition = makeNode("goog.isObject", new Node[] {
        makeNode("function() {}")
    });

    FlowScope blindScope = new FlowScope();

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, true);

    JSType result = scope.getVariable("goog.isObject")
        .getType().getDefinition();

    assertEquals(objectType, result);
  }

  @Test
  public void testRestrictToNotObject() {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    CodingConvention convention = new CodingConvention();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = makeNode("goog.isObject", new Node[] {
        makeNode("null")
    });

    FlowScope blindScope = new FlowScope();

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false);

    JSType result = scope.getVariable("goog.isObject")
        .getType().getDefinition();

    assertNull(result);
  }

  private Node makeNode(String name, Node... children) {
    Node node = new Node();
    node.setType(CALL);
    node.setLineNumber(0);
    node.setLineStartPosition(0);
    node.setLineEndPosition(0);
    node.setFirstChild(makeNode(NAME, name));
    node.setLastChild(makeList(children));
    return node;
  }

  private Node makeList(Node... nodes) {
    Node list = new Node();
    list.setType(NAME);
    list.setLineNumber(0);
    list.setLineStartPosition(0);
    list.setLineEndPosition(0);
    list.setFirstChild(makeList(nodes).getLastChild());
    for (Node node : nodes) {
      list.setLastChild(list.getLastChild().getNextSibling());
      list.setLastChild(list.getLastChild().getNextSibling());
      list.setLastChild(node);
    }
    return list;
  }

  private JSType getNativeType(int type) {
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    return typeRegistry.getBuiltins().getNativeType(type);
  }
}