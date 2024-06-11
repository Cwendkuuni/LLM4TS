import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    String sourceCode = "function test() {"
        + "  if (x) return;"
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (i > 5) continue;"
        + "    console.log(i);"
        + "  }"
        + "  if (!y) return;"
        + "  try {"
        + "    throw new Error('Test Error');"
        + "  } catch (e) {"
        + "    console.log('Caught error: ' + e.message);"
        + "    return;"
        + "  } finally {"
        + "    console.log('Finally block');"
        + "  }"
        + "}";

    Compiler compiler = new Compiler();
    compiler.init();
    Node ast = compiler.parse(sourceCode, null, null, null);
    Node root = NodeUtil.getRootNode(ast);

    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(null, root);

    Node optimizedCode = NodeUtil.getRootNode(compiler.getProgramText());

    // Test that the number of explicit exits has been minimized
    int numExitsBefore = countExits(root);
    int numExitsAfter = countExits(optimizedCode);
    assertEquals("Number of explicit exits should be minimized", numExitsBefore - 2, numExitsAfter);

    // Test that the structure of the AST has been transformed correctly
    Node optimizedRoot = assertStructureEqual(root, optimizedCode);

    // Test that the transformation does not change the semantics of the code
    Node executionContext = NodeUtil.createEmptyGlobalContext();
    Node executionResult = compiler.processNode(optimizedCode, executionContext, null, null, null);
    assertEquals("Output of the optimized code should be the same as the original", NodeUtil.getRootNode(ast).getChild(0), executionResult.getChild(0));
  }

  private int countExits(Node node) {
    int count = 0;
    if (node.isIf()) {
      count += countExits(node.getFirstChild().getNext()) + countExits(node.getLastChild());
    } else if (node.isLabel()) {
      count += countExits(node.getLastChild());
    } else if (node.isBlock()) {
      for (Node child : node.children()) {
        count += countExits(child);
      }
    } else if (node.isFunction()) {
      count += countExits(node.getLastChild());
    } else if (node.isTry()) {
      count += countExits(NodeUtil.getTryBlock(node)) + countExits(NodeUtil.getCatchBlock(node));
      if (NodeUtil.hasFinally(node)) {
        count += countExits(NodeUtil.getFinallyBlock(node));
      }
    }
    return count;
  }

  private Node assertStructureEqual(Node a, Node b) {
    if (a.getType() != b.getType()) {
      fail("structure types do not match: " + getNodeTypeName(a.getType()) + " != " + getNodeTypeName(b.getType()));
    }

    if (a.isBlock() && b.isBlock()) {
      for (int i = 0; i < a.children.length; i++) {
        Node childA = a.children[i];
        Node childB = b.children[i];
        Node result = assertStructureEqual(childA, childB);
        if (result != null) {
          return result;
        }
      }
    }

    return null;
  }

  private String getNodeTypeName(int type) {
    switch (type) {
      case Token.LABEL:
        return "label";
      case Token.FOR:
        return "for";
      case Token.WHILE:
        return "while";
      case Token.DO:
        return "do-while";
      case Token.FUNCTION:
        return "function";
      case Token.TRY:
        return "try";
      case Token.IF:
        return "if";
      case Token.ELSE:
        return "else";
      case Token.ELSEIF:
        return "else if";
      case Token.BREAK:
        return "break";
      case Token.CONTINUE:
        return "continue";
      case Token.RETURN:
        return "return";
      default:
        return "unknown";
    }
  }
}