import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureReverseAbstractInterpreter.*;

public class ClosureReverseAbstractInterpreterTest {
  @org.junit.Test
  public void testIsDef() {
    // Test case for isDef function when the condition is true
    JSType type = getNativeType(OBJECT_TYPE);
    FlowScope blindScope = new FlowScope(new CompilationUnit());
    Node condition = makeNode("goog.isDef(obj)", new ObjectType("obj"));
    FlowScope informedScope = getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(getNativeType(OBJECT_TYPE), informedScope.getVariable("obj").getType());

    // Test case for isDef function when the condition is false
    condition = makeNode("goog.isDef(null)", nullType);
    informedScope = getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getVariable("obj"));
  }

  @org.junit.Test
  public void testIsDefAndNotNull() {
    // Test case for isDefAndNotNull function when the condition is true
    JSType type = getNativeType(OBJECT_TYPE);
    FlowScope blindScope = new FlowScope(new CompilationUnit());
    Node condition = makeNode("goog.isDefAndNotNull(obj)", new ObjectType("obj"));
    FlowScope informedScope = getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(getNativeType(OBJECT_TYPE), informedScope.getVariable("obj").getType());

    // Test case for isDefAndNotNull function when the condition is false
    condition = makeNode("goog.isDefAndNotNull(null)", nullType);
    informedScope = getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope);
  }

  @org.junit.Test
  public void testIsString() {
    // Test case for isString function when the condition is true
    FlowScope blindScope = new FlowScope(new CompilationUnit());
    Node condition = makeNode("goog.isString('str')", nullType);
    FlowScope informedScope = getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertEquals(getNativeType("string"), informedScope.getVariable("str").getType());

    // Test case for isString function when the condition is false
    condition = makeNode("goog.isString(1)", nullType);
    informedScope = getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope);
  }

  // Add more test cases for other functions here
}