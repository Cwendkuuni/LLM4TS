import org.junit.Test;
import static org.junit.Assert.*;

public class DevirtualizePrototypeMethodsTest {
  @Test
  public void testAccumulateMethod() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    Node root = new Node(Token.PROGRAM, null);
    root.setJSType(JSType.OBJECT);

    // Given
    Node aNode = new Node(Token.VAR, new Node(Token.NAME, "a"));
    Node prototypeNode = new Node(Token.EXP, new Node(Token.GET_PROP, aNode, "prototype"));
    Node accumulateNode = new Node(Token.GET_PROP, prototypeNode, "accumulate");
    Node aValueNode = new Node(Token.NUM, 2);

    // When
    devirtualizePrototypeMethods.process(null, root);

    // Then
    Node accumulateFunctionNode = findNodeByFunctionName(accumulateNode, root);
    assertEquals(3, accumulateFunctionNode.getChildCount());
    assertEquals("JSCompiler_StaticMethods_accumulate",
        accumulateFunctionNode.getFirstChild().getString());
    assertEquals("this", accumulateFunctionNode.getChild(1).getString());
    Node totalNode = accumulateFunctionNode.getChild(2);
    assertEquals(NodeType.VAR_DECL, totalNode.getType());
    assertEquals("total", totalNode.getFirstChild().getString());

    // Test call
    Node callNode = new Node(Token.CALL, accumulateFunctionNode, aNode, aValueNode);
    Node resultNode = callNode.getLastChild();
    assertEquals(NodeType.NUM, resultNode.getType());
    assertEquals(4, resultNode.getNumber().intValue());
  }

  private Node findNodeByFunctionName(Node functionNode, Node root) {
    for (Node child : root.children()) {
      if (child.getType() == NodeType.FUNCTION) {
        FunctionType functionType = (FunctionType) child.getJSType();
        for (Node parameter : functionType.getParameters()) {
          if (parameter.getString().equals(functionNode.getFirstChild().getString())) {
            return child;
          }
        }
      } else if (child.getFirstChild() != null) {
        Node foundNode = findNodeByFunctionName(functionNode, child);
        if (foundNode != null) {
          return foundNode;
        }
      }
    }
    return null;
  }
}