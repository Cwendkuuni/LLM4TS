import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Nodes;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RuntimeTypeCheckTest {

 private static final String CLASS_NAME = "TestClass";
 private static final String INTERFACE_NAME = "TestInterface";
 private static final String LOG_FUNCTION = "log";
 private static final String UNKNOWN_TYPE = "UnknownType";

 @Test
 public void testNormalFlow() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);
 Node root = NodeUtil.parseJavascript("function test() {}");
 runtimeTypeCheck.process(null, root);
 assertTrue(Nodes.contains(root, NodeUtil.getFunctionName(root, "test")));
 }

 @Test
 public void testTypeCheckingFunctionParameters() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript(
 "function test(x) {" +
 "var y = x;" +
 "}");
 runtimeTypeCheck.process(null, root);

 Node testFunction = Nodes.getFirstChild(root);
 assertTrue(Nodes.getChildCount(testFunction) > 1);
 Node xParam = Nodes.getChild(testFunction, 0);
 Node checkTypeCall = Nodes.getFirstChildAfter(xParam, NodeUtil.isExprStatement(Nodes.getChild(testFunction, 1)));
 assertTrue(Nodes.isFunctionCall(checkTypeCall));
 assertTrue(checkTypeCall.getFirstChild().getFirstChild().getType() == Token.GETPROP);
 assertTrue(Nodes.isString(Nodes.getChild(checkTypeCall.getFirstChild(), 0)).getValue().equals("instance_of__TestClass"));
 }

 @Test
 public void testTypeCheckingFunctionReturnValue() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript(
 "function test() {" +
 "return {};" +
 "}");
 runtimeTypeCheck.process(null, root);

 Node testFunction = Nodes.getFirstChild(root);
 Node checkTypeCall = Nodes.getLastChild(testFunction);
 assertTrue(Nodes.isFunctionCall(checkTypeCall));
 assertTrue(checkTypeCall.getLastChild().getFirstChild().getType() == Token.GETPROP);
 assertTrue(Nodes.isString(Nodes.getChild(checkTypeCall.getLastChild(), 0)).getValue().equals("Object"));
 }

 @Test
 public void testTypeCheckingClassType() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript(
 "function TestClass() {}");
 runtimeTypeCheck.process(null, root);

 Node testClass = Nodes.getFirstChild(root);
 Node prototype = Nodes.getFirstChild(testClass);
 assertTrue(Nodes.contains(prototype, NodeUtil.newQualifiedNameNode("instance_of__TestClass", -1, -1)));
 }

 @Test(expected = AssertionError.class)
 public void testTypeCheckingNullType() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript("function test() { return null; }");
 runtimeTypeCheck.process(null, root);

 Node testFunction = Nodes.getFirstChild(root);
 Node checkTypeCall = Nodes.getLastChild(testFunction);
 assertTrue(Nodes.isFunctionCall(checkTypeCall));
 assertTrue(checkTypeCall.getLastChild().getType() == Token.GETPROP);
 assertTrue(Nodes.isString(Nodes.getChild(checkTypeCall.getLastChild(), 0)).getValue().equals("Null"));
 }

 @Test(expected = AssertionError.class)
 public void testTypeCheckingUndefinedType() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript("function test() { var x; return x; }");
 runtimeTypeCheck.process(null, root);

 Node testFunction = Nodes.getFirstChild(root);
 Node checkTypeCall = Nodes.getLastChild(testFunction);
 assertTrue(Nodes.isFunctionCall(checkTypeCall));
 assertTrue(checkTypeCall.getLastChild().getType() == Token.GETPROP);
 assertTrue(Nodes.isString(Nodes.getChild(checkTypeCall.getLastChild(), 0)).getValue().equals("Undefined"));
 }

 @Test(expected = AssertionError.class)
 public void testTypeCheckingUnknownType() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript(
 "function test() {" +
 "class TestClass {}" +
 "}" +
 "function checkTypeUnknown() {" +
 "var x = new TestClass();" +
 "checkType(x, []);" +
 "}" +
 "checkTypeUnknown();");
 runtimeTypeCheck.process(null, root);

 Node checkTypeUnknownFunction = Nodes.getFirstChild(root);
 Node checkTypeCall = Nodes.getLastChild(checkTypeUnknownFunction);
 assertTrue(Nodes.isFunctionCall(checkTypeCall));
 assertTrue(checkTypeCall.getLastChild().getFirstChild().getType() == Token.GETPROP);
 assertTrue(Nodes.isString(Nodes.getChild(checkTypeCall.getLastChild(), 0)).getValue().equals(UNKNOWN_TYPE));
 }

 @Test(expected = AssertionError.class)
 public void testTypeCheckingInterfaceType() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript(
 "interface TestInterface {" +
 "method()" +
 "}" +
 "function TestClass implements TestInterface {" +
 "method() {}" +
 "}" +
 "function test() {" +
 "var obj = new TestClass();" +
 "checkType(obj, []);" +
 "}" +
 "test();");
 runtimeTypeCheck.process(null, root);

 Node testFunction = Nodes.getLastChild(Nodes.getFirstChild(root));
 Node checkTypeCall = Nodes.getLastChild(testFunction);
 assertTrue(Nodes.isFunctionCall(checkTypeCall));
 assertTrue(checkTypeCall.getLastChild().getFirstChild().getType() == Token.GETPROP);
 assertTrue(Nodes.isString(Nodes.getChild(checkTypeCall.getLastChild(), 0)).getValue().equals("instance_of__TestClass"));
 }

 @Test
 public void testTypeCheckingInterfaceImplementation() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript(
 "interface TestInterface {" +
 "method()" +
 "}" +
 "function TestClass implements TestInterface {" +
 "method() {}" +
 "}" +
 "function TestClassImplementingInterface extends TestClass {" +
 "method() {}" +
 "}" +
 "function test() {" +
 "var obj = new TestClassImplementingInterface();" +
 "checkType(obj, []);" +
 "}" +
 "test();");
 runtimeTypeCheck.process(null, root);

 Node testFunction = Nodes.getLastChild(Nodes.getFirstChild(root));
 Node checkTypeCall = Nodes.getLastChild(testFunction);
 assertTrue(Nodes.isFunctionCall(checkTypeCall));
 assertTrue(checkTypeCall.getLastChild().getFirstChild().getType() == Token.GETPROP);
 assertTrue(Nodes.isString(Nodes.getChild(checkTypeCall.getLastChild(), 0)).getValue().equals("instance_of__TestClassImplementingInterface"));
 }

 @Test
 public void testTypeCheckingInterfaceNotImplemented() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript(
 "interface TestInterface {" +
 "method()" +
 "}" +
 "function TestClass {" +
 "method() {}" +
 "}" +
 "function test() {" +
 "var obj = new TestClass();" +
 "checkType(obj, []);" +
 "}" +
 "test();");
 runtimeTypeCheck.process(null, root);

 Node testFunction = Nodes.getLastChild(Nodes.getFirstChild(root));
 assertTrue(Nodes.isFunctionCall(testFunction));
 assertFalse(Nodes.contains(testFunction, NodeUtil.newQualifiedNameNode("checkType", -1, -1)));
 }

 @Test
 public void testTypeCheckingClassWithExternType() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript(
 "/** @type {!Array<number>} */" +
 "function TestClass() {}" +
 "function test() {" +
 "var x = new TestClass();" +
 "checkType(x, []);" +
 "}" +
 "test();");
 runtimeTypeCheck.process(null, root);

 Node testFunction = Nodes.getLastChild(Nodes.getFirstChild(root));
 Node checkTypeCall = Nodes.getLastChild(testFunction);
 assertTrue(Nodes.isFunctionCall(checkTypeCall));
 assertTrue(checkTypeCall.getLastChild().getFirstChild().getType() == Token.GETPROP);
 assertTrue(Nodes.isString(Nodes.getChild(checkTypeCall.getLastChild(), 0)).getValue().equals("Array"));
 }

 @Test
 public void testTypeCheckingClassWithExternInterface() {
 RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(null, LOG_FUNCTION);

 Node root = NodeUtil.parseJavascript(
 "/** @ interface */" +
 "interface TestInterface {" +
 "method()" +
 "}" +
 "function TestClass implements TestInterface {" +
 "method() {}" +
 "}" +
 "function test() {" +
 "var x = new TestClass();" +
 "checkType(x, []);" +
 "}" +
 "test();");
 runtimeTypeCheck.process(null, root);

 Node testFunction = Nodes.getLastChild(Nodes.getFirstChild(root));
 Node checkTypeCall = Nodes.getLastChild(testFunction);
 assertTrue(Nodes.isFunctionCall(checkTypeCall));
 assertTrue(checkTypeCall.getLastChild().getFirstChild().getType() == Token.GETPROP);
 assertTrue(Nodes.isString(Nodes.getChild(checkTypeCall.getLastChild(), 0)).getValue().equals("instance_of__TestClass"));
 }
}