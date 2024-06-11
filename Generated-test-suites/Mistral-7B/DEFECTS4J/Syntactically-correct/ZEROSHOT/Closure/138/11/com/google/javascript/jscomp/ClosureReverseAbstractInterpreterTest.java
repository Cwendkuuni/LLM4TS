import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Type;
import com.google.javascript.jscomp.Visitor;
import com.google.javascript.rhino.NodeUtilities;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.ObjectTypeEnvironment;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;
  private AbstractCompiler compiler;
  private Checker checker;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    typeRegistry = new JSTypeRegistry();
    convention = new CodingConvention();
    compiler = new Compiler(typeRegistry, convention);
    checker = new Checker(new ObjectTypeEnvironment(typeRegistry), compiler);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDefTrue() {
    Node node = createNode("goog.isDef(x)");
    FlowScope scope = new FlowScope();
    JSType type = JSTypeFactory.getInstance().getUndefinedType();
    Type actualType = checker.checkType(node, scope, type, null);
    JSType expectedType = interpreter.getPreciserScopeKnowingConditionOutcome(
        node, scope, true).getFirstDeclaredType("x").getType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testIsDefFalse() {
    Node node = createNode("goog.isDef(x)");
    FlowScope scope = new FlowScope();
    JSType nullType = JSTypeFactory.getInstance().getNullType();
    Type actualType = checker.checkType(node, scope, nullType, null);
    assertNull(actualType);
  }

  @Test
  public void testIsDefAndNotNullTrue() {
    Node node = createNode("goog.isDefAndNotNull(x)");
    FlowScope scope = new FlowScope();
    JSType type = JSTypeFactory.getInstance().getUndefinedType();
    Type actualType = checker.checkType(node, scope, type, null);
    JSType expectedType = getRestrictedWithoutUndefined(
        getRestrictedWithoutNull(type));
    assertSame(expectedType, actualType.getValueType());
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    Node node = createNode("goog.isDefAndNotNull(x)");
    FlowScope scope = new FlowScope();
    JSType nullType = JSTypeFactory.getInstance().getNullType();
    Type actualType = checker.checkType(node, scope, nullType, null);
    assertNull(actualType);
  }

  @Test
  public void testIsStringTrue() {
    Node node = createNode("goog.isString(x)");
    FlowScope scope = new FlowScope();
    JSType stringType = JSTypeFactory.getInstance().getStringType();
    Type actualType = checker.checkType(node, scope, null, null);
    assertSame(stringType, actualType.getValueType());
  }

  @Test
  public void testIsStringFalse() {
    Node node = createNode("goog.isString(x)");
    FlowScope scope = new FlowScope();
    JSType anyType = JSTypeFactory.getInstance().getAnyType();
    Type actualType = checker.checkType(node, scope, anyType, null);
    assertNull(actualType);
  }

  @Test
  public void testIsNumberTrue() {
    Node node = createNode("goog.isNumber(x)");
    FlowScope scope = new FlowScope();
    JSType numberType = JSTypeFactory.getInstance().getNumberType();
    Type actualType = checker.checkType(node, scope, null, null);
    assertSame(numberType, actualType.getValueType());
  }

  @Test
  public void testIsNumberFalse() {
    Node node = createNode("goog.isNumber(x)");
    FlowScope scope = new FlowScope();
    JSType anyType = JSTypeFactory.getInstance().getAnyType();
    Type actualType = checker.checkType(node, scope, anyType, null);
    assertNull(actualType);
  }

  @Test
  public void testIsBooleanTrue() {
    Node node = createNode("goog.isBoolean(x)");
    FlowScope scope = new FlowScope();
    JSType booleanType = JSTypeFactory.getInstance().getBooleanType();
    Type actualType = checker.checkType(node, scope, null, null);
    assertSame(booleanType, actualType.getValueType());
  }

  @Test
  public void testIsBooleanFalse() {
    Node node = createNode("goog.isBoolean(x)");
    FlowScope scope = new FlowScope();
    JSType anyType = JSTypeFactory.getInstance().getAnyType();
    Type actualType = checker.checkType(node, scope, anyType, null);
    assertNull(actualType);
  }

  @Test
  public void testIsFunctionTrue() {
    Node node = createNode("goog.isFunction(x)");
    FlowScope scope = new FlowScope();
    JSType functionType = createFunctionType();
    Type actualType = checker.checkType(node, scope, null, null);
    assertSame(functionType, actualType.getValueType());
  }

  @Test
  public void testIsFunctionFalse() {
    Node node = createNode("goog.isFunction(x)");
    FlowScope scope = new FlowScope();
    JSType anyType = JSTypeFactory.getInstance().getAnyType();
    Type actualType = checker.checkType(node, scope, anyType, null);
    assertNull(actualType);
  }

  @Test
  public void testIsArrayTrue() {
    Node node = createNode("goog.isArray(x)");
    FlowScope scope = new FlowScope();
    JSType arrayType = JSTypeFactory.getInstance().getArrayType();
    Type actualType = checker.checkType(node, scope, null, null);
    assertSame(arrayType, actualType.getValueType());
  }

  @Test
  public void testIsArrayFalse() {
    Node node = createNode("goog.isArray(x)");
    FlowScope scope = new FlowScope();
    JSType anyType = JSTypeFactory.getInstance().getAnyType();
    Type actualType = checker.checkType(node, scope, anyType, null);
    assertNull(actualType);
  }

  @Test
  public void testIsObjectTrue() {
    Node node = createNode("goog.isObject(x)");
    FlowScope scope = new FlowScope();
    JSType objectType = JSTypeFactory.getInstance().getObjectType();
    Type actualType = checker.checkType(node, scope, null, null);
    assertSame(objectType, actualType.getValueType());
  }

  @Test
  public void testIsObjectFalse() {
    Node node = createNode("goog.isObject(x)");
    FlowScope scope = new FlowScope();
    JSType anyType = JSTypeFactory.getInstance().getAnyType();
    Type actualType = checker.checkType(node, scope, anyType, null);
    assertNull(actualType);
  }

  private Node createNode(String expression) {
    Compiler compiler = new Compiler(typeRegistry, convention);
    AbstractCompiler abstractCompiler =
        new AbstractCompiler(compiler.getOptions());
    Node ast = NodeUtilities.parseExpression(expression, abstractCompiler);
    return ast;
  }

  private FunctionType createFunctionType() {
    ObjectType functionPrototype = typeRegistry.getGlobalType("Function");
    ObjectTypeEnvironment objectTypeEnvironment = new ObjectTypeEnvironment();
    ObjectType functionType = new ObjectType(
        "Function", JSTypeFactory.getInstance().getFunctionType(
            functionPrototype, new Type[0], new Type[0]));
    objectTypeEnvironment.addDeclaredType("Function", functionType);
    return (FunctionType) functionType;
  }

  private JSType getRestrictedWithoutUndefined(JSType type) {
    return interpreter.restricters.get("isDef").apply(
        new TypeRestriction(type, true));
  }

  private JSType getRestrictedWithoutNull(JSType type) {
    return interpreter.restricters.get("isNull").apply(
        new TypeRestriction(type, false));
  }

  private JSType getRestrictedByTypeOfResult(JSType type, String expectedType,
      boolean outcome) {
    return interpreter.restricters.get(expectedType).apply(
        new TypeRestriction(type, outcome));
  }
}