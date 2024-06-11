import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();

    String code = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {'c': 2}," +
        "    d: function() { return 3; }," +
        "    e: []" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b.c;" +
        "  var z = obj.d();" +
        "}" +
        "foo();";

    Node externs = Node.newTextNode("");
    Node root = parser.parse(code, null, null);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "inlineVarPrefix");
    pass.process(externs, root);

    NodeTraversal traverser = new NodeTraversal(root);
    traverser.traverse();

    Node foo = traverser.getRootNode().getLastChild();
    Node body = foo.getFirstChild();

    Map<String, Node> expected = Maps.newLinkedHashMap();
    expected.put("a", NodeUtil.newNumberNode(1));
    expected.put("b_c", NodeUtil.newNumberNode(2));
    expected.put("c", NodeUtil.newVarNode("b_c_c", null));
    expected.put("d", NodeUtil.newFunctionNode(
        Token.NAME, "d", null, Lists.newArrayList(
            new Node(Token.RETURN, NodeUtil.newNumberNode(3))), null));

    for (int i = 0; i < body.getChildCount(); i++) {
      Node child = body.getChildAtIndex(i);
      if (child.getType() == Token.VAR) {
        Var var = (Var) child.getFirstChild();
        String name = var.name;
        Node value = var.getInitializer();
        if (expected.containsKey(name)) {
          Node expectedValue = expected.get(name);
          assertThat(value, is(expectedValue));
        }
      }
    }
  }
}