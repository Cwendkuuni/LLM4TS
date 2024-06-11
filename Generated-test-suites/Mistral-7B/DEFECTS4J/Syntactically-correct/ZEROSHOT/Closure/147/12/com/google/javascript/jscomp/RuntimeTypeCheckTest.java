import static org.junit.Assert.*;
import static com.google.common.truth.Truth.assertThat;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RuntimeTypeCheckTest {

  private static AbstractCompiler createCompiler() {
    return new AbstractCompiler() {
      @Override
      public Node getNodeForCodeInsertion(Node node) {
        return node;
      }
    };
  }

  @Test
  public void testRuntimeTypeCheck() throws IOException {
    AbstractCompiler compiler = createCompiler();
    String logFunction = "function(warning, expr) { print('Warning: ' + expr); }";

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(compiler, logFunction);

    String code =
        "function Bar() {}"
        + "function Foo() {}"
        + "function FooBar(x) {}"
        + "FooBar(null)"
        + "function foo(x) {}"
        + "foo('a')";

    CompilerInput input = new CompilerInput("test", null, null, code);
    Node root = NodeUtil.parse(code);

    Node result = NodeUtil.parse("null");

    typeChecker.process(input, root);

    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isFunction(n) && n.getJSType().getClassName().equals("FooBar")) {
          Node checkNode = n.getLastChild().getLastChild();
          assertNotNull(checkNode);
          Node checkTypeCall = (Node) checkNode.getFirstChild();
          assertEquals(checkTypeCall.getChild(0).getType(), Token.CALL);
          assertEquals(checkTypeCall.getChild(0).getChild(0).getType(), Token.IDENT);
          assertEquals(checkTypeCall.getChild(0).getChild(0).getString(), "checkType");
          List<Node> checkers = Arrays.asList(checkTypeCall.getChild(1));
          checkChecker(compiler, checkers.get(0), "nullChecker");
          checkChecker(compiler, checkers.get(1), "classChecker");
          checkChecker(compiler, checkers.get(2), "interfaceChecker");
          Node param = NodeUtil.getFnParameters(n.getFirstChild()).getFirstChild();
          Node checkParam = checkNode.getChild(2);
          assertEquals(checkParam, param.cloneTree());
        }
      }
    });

    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (NodeUtil.isFunction(n) && n.getJSType().getClassName().equals("foo")) {
          Node checkNode = n.getLastChild();
          assertNotNull(checkNode);
          Node checkTypeCall = (Node) checkNode.getFirstChild();
          assertEquals(checkTypeCall.getChild(0).getType(), Token.CALL);
          assertEquals(checkTypeCall.getChild(0).getChild(0).getType(), Token.IDENT);
          assertEquals(checkTypeCall.getChild(0).getChild(0).getString(), "checkType");
          List<Node> checkers = Arrays.asList(checkNode.getChildren().get(1));
          checkChecker(compiler, checkers.get(0), "valueChecker");
          checkChecker(compiler, checkers.get(1), "stringChecker");
          Node arg = n.getLastChild().getLastChild();
          Node checkArg = checkNode.getChild(2);
          assertEquals(checkArg, arg.cloneTree());
        }
      }
    });

    byte[] expected =
        ("function checkType(value, checkers) {"
         + "  for (var i = 0; i < checkers.length; ++i) {"
         + "    if (checkTypeImpl(value, checkers[i])) {"
         + "      return value;"
         + "    }"
         + "  }"
         + "  throw new Error('checkType: no match found')"
         + "}"
         + "function checkTypeImpl(value, checker) {"
         + "  if (checker instanceof StringChecker) {"
         + "    return checkString(value, checker.value)"
         + "  } else if (checker instanceof ValueChecker) {"
         + "    return checkValue(value, checker.valueType)"
         + "  } else if (checker instanceof ClassChecker) {"
         + "    return checkObjectType(value, checker.className)"
         + "  } else if (checker instanceof InterfaceChecker) {"
         + "    return checkInterfaceImplementation(value, checker.className)"
         + "  } else {"
         + "    throw new Error('Unknown checker type')"
         + "  }"
         + "}"
         + "function checkString(value, expected) {"
         + "  return value === expected?"
         + "      value:"
         + "    (new Error('Unexpected string type: ' + value))"
         + "}"
         + "function checkValue(value, expectedValueType) {"
         + "  return expectedValueType.isInstance(value)?"
         + "      value:"
         + "    (new Error('Unexpected value type: ' + value.getClass().getName()))"
         + "}"
         + "function checkObjectType(value, expectedClassName) {"
         + "  return value instanceof Function &&"
         + "    (value.constructor && value.constructor.className === expectedClassName)?"
         + "      value:"
         + "    (new Error('Expected object of type ' + expectedClassName + ', got ' + value.getClass().getName()))"
         + "}"
         + "function checkInterfaceImplementation(value, expectedInterface) {"
         + "  return value instanceof Object &&"
         + "    (value.constructor && value.constructor.getName() === expectedInterface &&"
         + "     ('__implements__' in value.constructor.prototype &&"
         + "      Array.isArray(value.constructor.prototype.__implements__) &&"
         + "      value.constructor.prototype.__implements__.indexOf(expectedInterface) >= 0))?"
         + "      value:"
         + "    (new Error('Expected object implementing interface ' + expectedInterface + ', got ' + value.getClass().getName()))"
         + "}").getBytes();

    String resultCode = CharStreams.toString(new InputStreamReader(
        new ByteArrayInputStream(compiler.getGeneratedCode(null)), Charsets.UTF_8));

    String actual = resultCode.substring(0, resultCode.lastIndexOf("}"));
    String expectedResult = new String(expected);
    assertEquals(expectedResult, actual);
  }

  private void checkChecker(AbstractCompiler compiler, Node checker, String checkerName) {
    if (checker.getFirstChild().getType() != Token.CALL) {
      fail("Expected call to checker function");
    }
    assertEquals(checker.getFirstChild().getChild(0).getType(), Token.IDENT);
    assertEquals(checker.getFirstChild().getFirstChild().getString(), checkerName);
  }
}