import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private Node root;

  @Before
  public void setUp() throws IOException {
    compiler = new Compiler();
    root = Node.parse("function test() {}\n" +
            "function foo() { return null; }\n" +
            "function bar(x) { return x; }\n" +
            "function baz(x) { return new Foo(); }\n" +
            "class Foo {}");
    compiler.process(new CompilerInput("test.js", "test.js", root), null);
  }

  @Test
  public void testRuntimeTypeCheck() {
    RuntimeTypeCheck check = new RuntimeTypeCheck(compiler, null);
    check.process(null, root);

    // Test null check
    Node test = compiler.getNodeForCodeInsertion(null).getFirstChild();
    Node nullCheck = NodeUtil.findFirstChildByType(test, Token.CALL);
    assertEquals("checkType(null, [nullChecker])", nullCheck.toString());

    // Test number check
    Node numberCheck = NodeUtil.findFirstChildByType(test, Token.EXPR_LIST);
    Node numberNode = NodeUtil.findFirstChildByType(numberCheck, Token.NUM);
    assertEquals("42", numberNode.toString());
    assertEquals("checkType(42, [valueChecker(\"number\")])",
        NodeUtil.getParent(numberCheck).toString());

    // Test string check
    Node stringCheck = NodeUtil.findFirstChildAfter(numberCheck, test);
    Node stringNode = NodeUtil.findFirstChildByType(stringCheck, Token.STRING);
    assertEquals("\"hello world\"", stringNode.toString());
    assertEquals("checkType(\"hello world\", [valueChecker(\"string\")])",
        NodeUtil.getParent(stringCheck).toString());

    // Test class check
    Node classCheck = NodeUtil.findFirstChildAfter(stringCheck, test);
    Node classNode = NodeUtil.findFirstChildByType(classCheck, Token.GETNEW);
    assertEquals("new Foo()", classNode.toString());
    assertEquals("checkType(new Foo(), [classChecker(\"Foo\")])",
        NodeUtil.getParent(classCheck).toString());

    // Test interface check
    Node interfaceCheck = NodeUtil.findFirstChildAfter(classCheck, test);
    Node interfaceNode = NodeUtil.findFirstChildByType(interfaceCheck, Token.GETPROPERTY);
    assertEquals("obj.foo", interfaceNode.toString());
    assertEquals("checkType(obj.foo, [interfaceChecker(\"IFoo\")])",
        NodeUtil.getParent(interfaceCheck).toString());

    // Test function check
    Node functionCheck = NodeUtil.findFirstChildAfter(interfaceCheck, test);
    Node functionNode = NodeUtil.findFirstChildByType(functionCheck, Token.CALL);
    assertEquals("foo()", functionNode.toString());
    assertEquals("checkType(foo(), [valueChecker(\"function\")])",
        NodeUtil.getParent(functionCheck).toString());

    // Test constructor check
    Node constructorCheck = NodeUtil.findFirstChildAfter(functionCheck, test);
    Node constructorNode = NodeUtil.findFirstChildByType(constructorCheck, Token.GETNEW);
    assertEquals("new Foo()", constructorNode.toString());
    assertEquals("checkType(new Foo(), [classChecker(\"Foo\")])",
        NodeUtil.getParent(constructorCheck).toString());

    // Test object check
    Node objectCheck = NodeUtil.findFirstChildAfter(constructorCheck, test);
    Node objectNode = NodeUtil.findFirstChildByType(objectCheck, Token.OBJECT);
    assertEquals("{ foo: 42, bar: \"hello world\" }", objectNode.toString());
    assertEquals("checkType({ foo: 42, bar: \"hello world\" }, [valueChecker(\"object\")])",
        NodeUtil.getParent(objectCheck).toString());

    // Test union check
    Node unionCheck = NodeUtil.findFirstChildAfter(objectCheck, test);
    Node unionNode = NodeUtil.findFirstChildByType(unionCheck, Token.CALL);
    assertEquals("foo() || new Foo()", unionNode.toString());
    assertEquals("checkType(foo() || new Foo(), [unionChecker(\"(function|Foo)\")])",
        NodeUtil.getParent(unionCheck).toString());

    // Test error case: unknown type
    Node unknownCheck = NodeUtil.findFirstChildAfter(unionCheck, test);
    Node unknownNode = NodeUtil.findFirstChildByType(unknownCheck, Token.GETELEM);
    assertEquals("unknown[0]", unknownNode.toString());
    assertEquals("checkType(unknown[0], [unknownChecker()])",
        NodeUtil.getParent(unknownCheck).toString());
  }

  /** Compiler for testing */
  private static class Compiler extends AbstractCompiler {
  }
}