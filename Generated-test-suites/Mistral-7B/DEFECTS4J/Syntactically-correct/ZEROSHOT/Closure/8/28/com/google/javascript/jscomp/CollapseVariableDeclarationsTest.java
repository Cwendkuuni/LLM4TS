import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private Compiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testCollapseVariableDeclarations() {
    String sourceCode =
        "function f() {\n"
            + "  var a;\n"
            + "  var b = 1;\n"
            + "  var c = 2;\n"
            + "  var d;\n"
            + "  var e = 3;\n"
            + "  var f = 4;\n"
            + "  var g = 5;\n"
            + "  var h = 6;\n"
            + "  var i = 7;\n"
            + "  var j = 8;\n"
            + "  var k = 9;\n"
            + "}";

    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);
    compiler.parse(sourceCode, externs, root);

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(externs, root);

    Node f = findNodeByName(root, "f");
    List<Node> vars = Lists.newArrayList();
    for (Node child = f.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isVar()) {
        vars.add(child);
      }
    }

    assertEquals(6, vars.size());

    Node varA = vars.get(0);
    Node varB = vars.get(1);
    Node varC = vars.get(2);
    Node varD = vars.get(3);
    Node varE = vars.get(4);
    Node varF = vars.get(5);

    Node expected = new Node(Token.VAR);
    expected.copyInformationFrom(varA);
    expected.setNext(new Node(Token.COMMA));
    expected.addChildToBack(varB);
    expected.addChildToBack(new Node(Token.COMMA));
    expected.addChildToBack(varC);
    expected.addChildToBack(new Node(Token.COMMA));
    expected.addChildToBack(varD);
    expected.addChildToBack(new Node(Token.COMMA));
    expected.addChildToBack(varE);
    expected.addChildToBack(new Node(Token.COMMA));
    expected.addChildToBack(varF);

    assertEquals(expected.toSource(), varA.toSource());
    assertEquals(expected.getNext().getValue(), varB.getNext().getValue());
    assertEquals(expected.getNext().getValue(), varC.getNext().getValue());
    assertEquals(expected.getNext().getValue(), varD.getNext().getValue());
    assertEquals(expected.getNext().getValue(), varE.getNext().getValue());
    assertEquals(expected.getNext().getValue(), varF.getNext().getValue());
  }

  private Node findNodeByName(Node root, String name) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isFunction()) {
        for (Node grandChild = child.getFirstChild(); grandChild != null;
            grandChild = grandChild.getNext()) {
          if (grandChild.isName() && grandChild.getString().equals(name)) {
            return grandChild;
          }
        }
      }
    }
    return null;
  }

  @After
  public void tearDown() {
    compiler = null;
  }
}

class NodeUtil {
  static boolean isExprAssign(Node node) {
    return node.getFirstChild().isName()
        && Node.isAssignable(node.getFirstChild().getType());
  }
}