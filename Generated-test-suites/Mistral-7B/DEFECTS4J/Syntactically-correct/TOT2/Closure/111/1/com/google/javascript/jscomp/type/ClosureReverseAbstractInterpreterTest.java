import static org.junit.Assert.*;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private static final NodeFactory NODE_FACTORY = NodeFactory.newInstance();
  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(CodingConvention.standard(), typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = createTestCondition();
    FlowScope blindScope = createTestBlindScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(ObjectType.getEmptyType(), result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_ValidCondition() {
    Node condition = createTestCondition();
    FlowScope blindScope = createTestBlindScope();
    boolean outcome = true;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertEquals(ObjectType.getEmptyType(), result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_InvalidCondition() {
    Node condition = NODE_FACTORY.literal(1);
    FlowScope blindScope = createTestBlindScope();
    boolean outcome = true;
    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_InvalidCallee() {
    Node condition = createTestCondition();
    FlowScope blindScope = createTestBlindScope();
    boolean outcome = true;
    Node callee = NODE_FACTORY.identifier("invalid");
    Node parameter = NODE_FACTORY.stringLiteral("isDef");
    Node param = NODE_FACTORY.qualifiedName("goog", parameter);
    Node rightChild = NODE_FACTORY.stringLiteral("invalid");
    condition.setLastChild(param);
    param.setLastChild(rightChild);
    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_InvalidLeftChild() {
    Node condition = createTestCondition();
    FlowScope blindScope = createTestBlindScope();
    boolean outcome = true;
    Node callee = NODE_FACTORY.getProp(NODE_FACTORY.identifier("goog"), 
        NODE_FACTORY.stringLiteral("isArray"));
    Node parameter = NODE_FACTORY.identifier("invalid");
    Node param = NODE_FACTORY.qualifiedName("goog", parameter);
    Node rightChild = NODE_FACTORY.stringLiteral("isDef");
    condition.setLastChild(param);
    param.setLastChild(callee);
    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_InvalidRightChild() {
    Node condition = createTestCondition();
    FlowScope blindScope = createTestBlindScope();
    boolean outcome = true;
    Node callee = NODE_FACTORY.getProp(NODE_FACTORY.identifier("goog"), 
        NODE_FACTORY.stringLiteral("isArray"));
    Node parameter = NODE_FACTORY.identifier("isDef");
    Node param = NODE_FACTORY.qualifiedName("goog", parameter);
    Node rightChild = NODE_FACTORY.literal(1);
    condition.setLastChild(param);
    param.setLastChild(callee);
    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_UnrecognizedMethodName() {
    Node condition = createTestCondition();
    FlowScope blindScope = createTestBlindScope();
    boolean outcome = true;
    Node callee = NODE_FACTORY.getProp(NODE_FACTORY.identifier("goog"), 
        NODE_FACTORY.stringLiteral("invalidMethod"));
    Node parameter = NODE_FACTORY.identifier("isDef");
    Node param = NODE_FACTORY.qualifiedName("goog", parameter);
    Node rightChild = NODE_FACTORY.stringLiteral("isDef");
    condition.setLastChild(param);
    param.setLastChild(callee);
    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_NullCondition() {
    Node condition = null;
    FlowScope blindScope = createTestBlindScope();
    boolean outcome = true;
    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_NullBlindScope() {
    Node condition = createTestCondition();
    FlowScope blindScope = null;
    boolean outcome = true;
    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_NullOutcome() {
    Node condition = createTestCondition();
    FlowScope blindScope = createTestBlindScope();
    boolean outcome = false;
    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  private Node createTestBlindScope() {
    return NODE_FACTORY.objectLiteral();
  }

  private Node createTestCondition() {
    Node callee = NODE_FACTORY.getProp(NODE_FACTORY.identifier("goog"), 
        NODE_FACTORY.stringLiteral("isArray"));
    Node parameter = NODE_FACTORY.identifier("myArray");
    Node param = NODE_FACTORY.qualifiedName("goog", parameter);
    Node rightChild = NODE_FACTORY.boolLiteral(true);
    Node condition = NODE_FACTORY.call(callee, param, rightChild);
    return condition;
  }

}