import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import static org.junit.Assert.*;

/**
 * Tests for the RuntimeTypeCheck class.
 */
public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws Exception {
    String testCode = "function MyClass() {} " +
        "function MyClassWithInterface() {} " +
        "MyClass.prototype.myMethod = function(x) {} " +
        "MyClassWithInterface.prototype.implements__IMyInterface = true; " +
        "function IMyInterface() {} " +
        "function MyFunction(x) {} " +
        "MyFunction.prototype.myMethod = function() {} " +
        "MyFunction.prototype.checkType = function() {}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RuntimeTypeCheck(compiler, null);
    Node root = Parser.parse(testCode, null);
    pass.process(null, root);

    // Check that marker properties were added to the class prototypes.
    Node myClassNode = NodeUtil.newQualifiedNameNode("MyClass", -1, -1);
    Node myClassMarker = NodeUtil.getFirstChildOfType(
        NodeUtil.getFirstChildOfType(myClassNode, Token.BLOCK),
        Token.GETPROP);
    assertEquals("instance_of__MyClass", myClassMarker.getFirstChild().toString());

    Node myClassWithInterfaceNode = NodeUtil.newQualifiedNameNode(
        "MyClassWithInterface", -1, -1);
    Node myClassWithInterfaceMarker = NodeUtil.getFirstChildOfType(
        NodeUtil.getFirstChildOfType(myClassWithInterfaceNode, Token.BLOCK),
        Token.GETPROP);
    assertEquals("instance_of__MyClassWithInterface",
        myClassWithInterfaceMarker.getFirstChild().toString());
    Node myClassWithInterfaceImplMarker = NodeUtil.getFirstChildOfType(
        myClassWithInterfaceMarker.getNext(), Token.GETPROP);
    assertEquals("implements__IMyInterface",
        myClassWithInterfaceImplMarker.getFirstChild().toString());

    // Check that checkType calls were added to the functions.
    Node myFunctionNode = NodeUtil.newQualifiedNameNode("MyFunction", -1, -1);
    Node myFunctionCheckTypeCall = NodeUtil.getFirstChildOfType(
        NodeUtil.getLastChildOfType(myFunctionNode, Token.BLOCK),
        Token.CALL);
    assertEquals("checkType", myFunctionCheckTypeCall.getFirstChild().toString());

    Node myClassMethodNode = NodeUtil.newQualifiedNameNameNode(
        "MyClass.prototype.myMethod", -1, -1);
    Node myClassMethodCheckTypeCall = NodeUtil.getFirstChildOfType(
        NodeUtil.getLastChildOfType(myClassMethodNode, Token.BLOCK),
        Token.CALL);
    assertEquals("checkType", myClassMethodCheckTypeCall.getFirstChild().toString());

    Node myFunctionMethodNode = NodeUtil.newQualifiedNameNameNode(
        "MyFunction.prototype.myMethod", -1, -1);
    Node myFunctionMethodCheckTypeCall = NodeUtil.getFirstChildOfType(
        NodeUtil.getLastChildOfType(myFunctionMethodNode, Token.BLOCK),
        Token.CALL);
    assertEquals("checkType", myFunctionMethodCheckTypeCall.getFirstChild().toString());

    // Check that checkType calls are not added to the log function.
    Node logFunctionNode = NodeUtil.newQualifiedNameNameNode(
        "MyFunction.prototype.log", -1, -1);
    Node logFunctionCheckTypeCall = NodeUtil.getFirstChildOfType(
        NodeUtil.getLastChildOfType(logFunctionNode, Token.BLOCK), Token.CALL);
    assertNull(logFunctionCheckTypeCall);
  }
}