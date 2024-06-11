import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
  }

  @Test
  public void testIsDefTrueArray() {
    Node condition = createCondition("goog.isDef", new Node[]{"[]"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNotNull(type);
    assertTrue(type.isSubtype(JSTypeNative.ARRAY_TYPE));
  }

  @Test
  public void testIsDefFalseArray() {
    Node condition = createCondition("goog.isDef", new Node[]{"[]"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueObject() {
    Node condition = createCondition("goog.isDef", new Node[]{"{}"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNotNull(type);
    assertTrue(type.isSubtype(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testIsDefFalseObject() {
    Node condition = createCondition("goog.isDef", new Node[]{"{}"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueFunction() {
    Node condition = createCondition("goog.isDef", new Node[]{"function(){}"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNotNull(type);
    assertTrue(type.isSubtype(JSTypeNative.FUNCTION_TYPE));
  }

  @Test
  public void testIsDefFalseFunction() {
    Node condition = createCondition("goog.isDef", new Node[]{"function(){}"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueUndefined() {
    Node condition = createCondition("goog.isDef", new Node[]{"undefined"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefFalseUndefined() {
    Node condition = createCondition("goog.isDef", new Node[]{"undefined"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueNull() {
    Node condition = createCondition("goog.isDef", new Node[]{"null"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefFalseNull() {
    Node condition = createCondition("goog.isDef", new Node[]{"null"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueBoolean() {
    Node condition = createCondition("goog.isDef", new Node[]{"true"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNull(type);
    assertTrue(type.isBoolean());
  }

  @Test
  public void testIsDefFalseBoolean() {
    Node condition = createCondition("goog.isDef", new Node[]{"true"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueNumber() {
    Node condition = createCondition("goog.isDef", new Node[]{"1"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNull(type);
    assertTrue(type.isNumber());
  }

  @Test
  public void testIsDefFalseNumber() {
    Node condition = createCondition("goog.isDef", new Node[]{"1"});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueString() {
    Node condition = createCondition("goog.isDef", new Node[]{"\"test\""});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNull(type);
    assertTrue(type.isString());
  }

  @Test
  public void testIsDefFalseString() {
    Node condition = createCondition("goog.isDef", new Node[]{"\"test\""});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueObjectWithProperty() {
    Node condition = createCondition("goog.isDef", new Node[]{
        createQualifiedName("Object", "prototype"),
        createName("propertyName")});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNotNull(type);
    assertTrue(type.isSubtype(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testIsDefFalseObjectWithProperty() {
    Node condition = createCondition("goog.isDef", new Node[]{
        createQualifiedName("Object", "prototype"),
        createName("propertyName")});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueFunctionWithArguments() {
    Node testFunction = createFunction("function(arg1, arg2) { return arg1 + arg2; }");
    Node condition = createCondition("goog.isDef", new Node[]{"function(arg1, arg2)", testFunction, createName("arg1"), createName("arg2")});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNotNull(type);
    assertTrue(type.isSubtype(JSTypeNative.FUNCTION_TYPE));
  }

  @Test
  public void testIsDefFalseFunctionWithArguments() {
    Node testFunction = createFunction("function(arg1, arg2) { return arg1 + arg2; }");
    Node condition = createCondition("goog.isDef", new Node[]{"function(arg1, arg2)", testFunction, createName("arg1"), createName("arg2")});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefTrueFunctionWithInvalidArguments() {
    Node testFunction = createFunction("function(arg1, arg2) { return arg1 + arg2; }");
    Node condition = createCondition("goog.isDef", new Node[]{"function(arg1, arg2)", testFunction, createName("arg3")});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true).getReturnType();
    assertNull(type);
  }

  @Test
  public void testIsDefFalseFunctionWithInvalidArguments() {
    Node testFunction = createFunction("function(arg1, arg2) { return arg1 + arg2; }");
    Node condition = createCondition("goog.isDef", new Node[]{"function(arg1, arg2)", testFunction, createName("arg3")});
    JSType type = interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false).getReturnType();
    assertNull(type);
  }

  private Node createCondition(String calleeName, Node... parameters) {
    return new Node(CALL, new Node[]{
        new Node(GETPROP, new Node[]{new Node(NAME, "goog"), new Node(STRING, calleeName)}),
        new Node(ARRAY_TYPE.createList(parameters))
    });
  }

  private Node createName(String name) {
    return new Node(NAME, name);
  }

  private Node createQualifiedName(String qualifiedName, String name) {
    return new Node(GETPROP, new Node[]{new Node(STRING, qualifiedName), new Node(NAME, name)});
  }

  private Node createFunction(String functionBody) {
    return new Node(FUNCTION_TYPE.createAnonymous(null, new Node[]{null}, null),
        new Node[]{new Node(NAME, "arguments"), new Node(NAME, "this")}, null, null,
        new Node(TYPE_EXPRESSION, new Node[]{new Node(NAME, "function"), new Node(STRING, functionBody)}));
  }
}