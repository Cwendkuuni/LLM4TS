import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() throws Exception {
    String sourceCode = "function A() {} A.prototype.accumulate = function(value) { return this.total += value }";
    Compiler compiler = new Compiler();
    CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);

    Node root = NodeFactory.compile(sourceCode);
    devirtualizePrototypeMethods.process(null, root);

    Node accumulateFunction = findNodeByName("accumulate", root);
    assertEquals("function(self, value) { return self.total += value }", accumulateFunction.toSource());

    Node a = findNodeByName("A", root).getFirstChild();
    Node total = NodeFactory.newExpressionStatement(
        NodeFactory.newCallExpression(findNodeByName("accumulate", root), a, NodeFactory.newNumberLiteral(2)));
    assertEquals(4, (int) compiler.compileToText(NodeFactory.newBlock(total), null).code.execute().callMethod("eval", null, null).toInt32());
  }

  private Node findNodeByName(String name, Node root) {
    for (Node child = root; child != null; child = child.getNextSibling()) {
      if (child.getNodeType() == Token.NAME && child.getString().equals(name)) {
        return child;
      }
    }
    throw new RuntimeException("Node not found: " + name);
  }

  
  // Add any additional tests here
  
}