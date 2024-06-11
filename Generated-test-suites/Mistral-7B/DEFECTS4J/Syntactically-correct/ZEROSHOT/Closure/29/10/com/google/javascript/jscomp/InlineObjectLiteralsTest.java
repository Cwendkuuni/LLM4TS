import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  private static final String VAR_PREFIX = "JSCompiler_object_inline_";

  @Override
  protected CompilerPass getPass() {
    return new InlineObjectLiterals(compiler, new Supplier<String>() {
      private int counter = 0;
      @Override
      public String get() {
        return VAR_PREFIX + ++counter;
      }
    });
  }

  public void testInlineObjectLiterals() {
    String js = "function test() {" +
        "  var o = {" +
        "    a: 1," +
        "    b: function() { return 2; }," +
        "    c: o," +
        "    d: []," +
        "  };" +
        "  var x = o.a;" +
        "  var y = o.b();" +
        "  var z = o.c;" +
        "  o.d.push(3);" +
        "  test2();" +
        "}" +
        "function test2() {}";

    Node root = parse(js);
    AbstractCompiler compiler = newCompiler();
    compiler.addPass(getPass());
    compiler.compile(root, null, null);

    Node newRoot = compiler.getOutput();

    Node test = NodeUtil.findFunctionByName(newRoot, "test");
    Node body = test.getBody();
    List<Node> statements = Lists.newArrayList(body.getStatements());

    // Check that the object was split up into individual variables.
    Map<String, Node> expectedVars = Maps.newHashMap();
    expectedVars.put("a", NodeUtil.newNumberNode(1));
    expectedVars.put("c", NodeUtil.newNameNode(VAR_PREFIX + "1"));

    Node varDecl = statements.get(1);
    Node varDeclList = varDecl.getFirstChild();
    for (Node varDeclNode : varDeclList) {
      String name = varDeclNode.getString();
      Node init = varDeclNode.getNext();
      Node varNode = init.getFirstChild();
      if (expectedVars.containsKey(name)) {
        Node expectedVar = expectedVars.get(name);
        assertEquals(expectedVar.getType(), varNode.getType());
        assertEquals(expectedVar.getSourcePosition(), varNode.getSourcePosition());
      }
      expectedVars.remove(name);
    }

    // Check that the object literals references were updated.
    Node oNode = NodeUtil.findVarByName(newRoot, "o");
    Node oAssign = oNode.getAssignments().get(0);
    Node newOAssign = NodeUtil.newObjectLiteralNode(
        Lists.newArrayList(
            NodeUtil.newNameNode(VAR_PREFIX + "1"),
            NodeUtil.newNumberNode(1),
            NodeUtil.newNameNode(VAR_PREFIX + "2"),
            NodeUtil.newFunctionNode(
                NodeUtil.createFunctionDeclarationFromBody(
                    "b",
                    null,
                    NodeUtil.newReturnNode(NodeUtil.newNumberNode(2)))),
            NodeUtil.newNameNode(VAR_PREFIX + "3"),
            NodeUtil.newArrayLiteralNode(Lists.newArrayList(
                NodeUtil.newNumberNode(3)))
        ));
    oAssign.setRightChild(newOAssign);

    // Check that the uses of the object literals were updated.
    Node xAssign = statements.get(3).getAssignments().get(0);
    Node x = xAssign.getLeftHandSide();
    Node xUse = xAssign.getRightHandSide();
    assertEquals(VAR_PREFIX + "1", x.getString());
    assertEquals(VAR_PREFIX + "1", xUse.getString());

    Node yCall = statements.get(4).getAssignments().get(0).getRightHandSide();
    Node y = yCall.getFirstChild();
    assertEquals(VAR_PREFIX + "2", y.getString());

    Node zAssign = statements.get(5).getAssignments().get(0);
    Node z = zAssign.getLeftHandSide();
    Node zUse = zAssign.getRightHandSide();
    assertEquals(VAR_PREFIX + "3", z.getString());
    assertEquals(VAR_PREFIX + "3", zUse.getString());

    Node dAssign = statements.get(6).getAssignments().get(0).getRightHandSide();
    Node d = dAssign.getFirstChild();
    assertEquals(VAR_PREFIX + "4", d.getString());

    Node test2Call = statements.get(7);
    assertNotNull(test2Call);
  }
}

class InlineObjectLiteralsTest extends CompilerPassTestBase {
  // ...
}