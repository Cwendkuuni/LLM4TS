import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerUnit;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private CompilerPass runtimeTypeCheck;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    rootNode = NodeUtil.createRootNode("function foo() {}");
  }

  @Test
  public void testAddMarkers() {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);
    nodeTraversal.traverse(runtimeTypeCheck);

    Node fooConstructor = findNode("foo", rootNode);
    assertEquals(2, fooConstructor.getChildCount());
    assertEquals("instance_of__foo", fooConstructor.getChild(0).getFirstChild().getString());
  }

  @Test
  public void testAddChecks() {
    Node fooFunction = new Node(Token.FUNCTION, null, null, null, null);
    fooFunction.setJSType(new FunctionType(new ObjectType("foo"), null));
    Node fooBlock = Node.newBlock(null);
    Node fooReturn = new Node(Token.RETURN, null, null);
    fooBlock.addChildToFront(fooReturn);
    fooFunction.setLastChild(fooBlock);
    rootNode.replaceChild(rootNode.getLastChild(), fooFunction);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);
    nodeTraversal.traverse(runtimeTypeCheck);

    Node fooReturnChecker = findCheckerNode("foo", rootNode);
    assertEquals("returnChecker", fooReturnChecker.toString());
  }

  @Test
  public void testAddBoilerplateCode() {
    Node js = runtimeTypeCheck.getBoilerplateCode(compiler, null);
    rootNode.addChildToFront(js);

    String expectedBoilerplateCode = "" +
        "function checkType(expr, checkers) {\n" +
        "  for (var i = 0, len = checkers.length; i < len; ++i) {\n" +
        "    var checker = checkers[i];\n" +
        "    if (checker(expr)) {\n" +
        "      return expr;\n" +
        "    }\n" +
        "  }\n" +
        "  throw new Error('Expression is not assignable to any of the given types.');\n" +
        "}\n" +
        "\n" +
        "function valueChecker(typeName) {\n" +
        "  return function(expr) {\n" +
        "    return typeof expr === typeName;\n" +
        "  };\n" +
        "}\n" +
        "\n" +
        "function externClassChecker(className) {\n" +
        "  return function(expr) {\n" +
        "    return expr instanceof window[className];\n" +
        "  };\n" +
        "}\n" +
        "\n" +
        "function classChecker(className) {\n" +
        "  var constructor = window[className].constructor;\n" +
        "  return function(expr) {\n" +
        "    return expr instanceof constructor;\n" +
        "  };\n" +
        "}\n" +
        "\n" +
        "function interfaceChecker(className) {\n" +
        "  var prototype = window[className].prototype;\n" +
        "  return function(expr) {\n" +
        "    for (var i = 0, len = prototype.__implementedInterfaces__.length; i < len; ++i) {\n" +
        "      if (expr instanceof prototype.__implementedInterfaces__[i]) {\n" +
        "        return true;\n" +
        "      }\n" +
        "    }\n" +
        "    return false;\n" +
        "  };\n" +
        "}\n" +
        "\n" +
        "function log(warning, expr) {\n" +
        "  if (%%LOG%%) {\n" +
        "    %%LOG%.warning(warning, expr);\n" +
        "  }\n" +
        "}\n";

    assertEquals(expectedBoilerplateCode, CharStreams.toString(js));
  }

  private Node findNode(String name, Node root) {
    Node node = NodeUtil.findDeepestNode(root, new ArgumentMatcher<Node>() {
      @Override
      public boolean matches(Node node) {
        return NodeUtil.getFunctionName(node) != null &&
            NodeUtil.getFunctionName(node).getText().equals(name);
      }
    });
    return node;
  }

  private Node findCheckerNode(String name, Node root) {
    Node node = NodeUtil.findDeepestNode(root, new ArgumentMatcher<Node>() {
      @Override
      public boolean matches(Node node) {
        if (node.getType() != Token.CALL) {
          return false;
        }

        Node callee = node.getFirstChild();
        if (callee.getType() != Token.STRING) {
          return false;
        }

        return callee.getString().equals("checkType") &&
            node.getChildCount() > 1 &&
            NodeUtil.getFunctionName(node.getChild(1)).getText().equals(name);
      }
    });
    return node;
  }
}