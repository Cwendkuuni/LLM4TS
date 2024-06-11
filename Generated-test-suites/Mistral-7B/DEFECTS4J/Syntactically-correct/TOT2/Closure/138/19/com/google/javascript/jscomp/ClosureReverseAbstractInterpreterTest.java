import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    Node condition = createNode("goog.isDef(foo)");
    FlowScope blindScope = new FlowScope(null, null);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.OBJECT_TYPE);
    JSType actualType = informedScope.getVariable("foo").getType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_null() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    Node condition = createNode("goog.isDef(null)");
    FlowScope blindScope = new FlowScope(null, null);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    Node condition = createNode("goog.isArray(foo)");
    FlowScope blindScope = new FlowScope(null, null);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType expectedType = interpreter.getNativeType(JSTypeNative.ARRAY_TYPE);
    JSType actualType = informedScope.getVariable("foo").getType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_null() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
    Node condition = createNode("goog.isArray(null)");
    FlowScope blindScope = new FlowScope(null, null);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope);
  }

  // Add test cases for other methods and edge cases

  private Node createNode(String nodeString) {
    // Implement this method to create a Node instance from a string
  }
}