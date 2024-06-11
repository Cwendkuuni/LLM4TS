import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RuntimeTypeCheck.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;

import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {

  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() throws IOException {
    compiler = new Compiler();
    rootNode = Node.parseJs(
        "function Foo() {}" +
        "function Bar() {}" +
        "Bar.prototype.qux = function() {}" +
        "function Baz() {}" +
        "Baz.prototype.quux = function() {}" +
        "function Quux() {}",
        1, null).getChild(Node.CHILD_CODE);
  }

  @Test
  public void testMarkerInsertion() {
    new RuntimeTypeCheck(compiler, null).process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.create(compiler);
    traversal.traverse(rootNode);

    TreeSet<String> markers = new TreeSet<String>(Comparator.naturalOrder());
    Node currentNode = rootNode;
    while (currentNode != null) {
      if (NodeUtil.isFunction(currentNode) || currentNode.getType() == Token.GETPROP) {
        String markerName = null;
        if (NodeUtil.isFunction(currentNode)) {
          FunctionType functionType = (FunctionType) currentNode.getJSType();
          markerName = functionType.isConstructor() ?
              functionType.getReferenceName() : "";
        } else if (NodeUtil.isGetProp(currentNode)) {
          markerName = ((Node) currentNode.getFirstChild()).getString();
        }

        if (markerName != null) {
          markers.add("prototype." + markerName);
        }
      }
      currentNode = currentNode.getNext();
    }

    ImmutableList<String> expectedMarkers = ImmutableList.of(
        "Foo.prototype.instance_of__Foo",
        "Bar.prototype.instance_of__Bar",
        "Baz.prototype.instance_of__Baz",
        "Bar.prototype.implements__Quux",
        "Baz.prototype.implements__Quux");

    assertEquals(expectedMarkers, markers);
  }

  @Test
  public void testTypeChecking() {
    new RuntimeTypeCheck(compiler, null).process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.create(compiler);
    traversal.traverse(rootNode);

    Node fooConstructorNode = NodeUtil.getFunction(rootNode, "Foo");
    Node barConstructorNode = NodeUtil.getFunction(rootNode, "Bar");
    Node bazConstructorNode = NodeUtil.getFunction(rootNode, "Baz");

    FunctionType fooFunctionType = (FunctionType) fooConstructorNode.getJSType();
    FunctionType barFunctionType = (FunctionType) barConstructorNode.getJSType();
    FunctionType bazFunctionType = (FunctionType) bazConstructorNode.getJSType();

    // Checking constructor types.
    Node fooInstance = new Node(Token.NEW, fooConstructorNode, null);
    Node fooTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getReturnType(), fooInstance);
    assertEquals(fooInstance, fooTypeCheckCall.getFirstChild());

    Node barInstance = new Node(Token.NEW, barConstructorNode, null);
    Node barTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getReturnType(), barInstance);
    assertEquals(barInstance, barTypeCheckCall.getFirstChild());

    Node bazInstance = new Node(Token.NEW, bazConstructorNode, null);
    Node bazTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getReturnType(), bazInstance);
    assertEquals(bazInstance, bazTypeCheckCall.getFirstChild());

    // Checking constructor return types.
    Node fooReturn = new Node(Token.RETURN, null, fooInstance);
    Node fooReturnTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getReturnType(), fooReturn);
    assertEquals(fooInstance, fooReturnTypeCheckCall.getFirstChild());

    Node barReturn = new Node(Token.RETURN, null, new Node(Token.NULL));
    Node barReturnTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getReturnType(), barReturn);
    assertNull(barReturnTypeCheckCall.getFirstChild());

    Node bazReturn = new Node(Token.RETURN, null, barInstance);
    Node bazReturnTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getReturnType(), bazReturn);
    assertEquals(barInstance, bazReturnTypeCheckCall.getFirstChild());

    // Checking function parameters.
    Node fooFunctionCallNode = new Node(Token.CALL, fooConstructorNode, null);
    Node fooFunctionCall = new Node(Token.CALL, fooFunctionNode, ImmutableList.of(new Node(Token.STRING, "foo")));
    Node fooFunctionCallTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getParameters().get(0).getJSType(), fooFunctionCall);
    assertEquals(fooFunctionCall, fooFunctionCallTypeCheckCall.getFirstChild());

    Node barFunctionCallNode = new Node(Token.CALL, barConstructorNode, null);
    Node barFunctionCall = new Node(Token.CALL, barFunctionNode, ImmutableList.of(new Node(Token.NULL)));
    Node barFunctionCallTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getParameters().get(0).getJSType(), barFunctionCall);
    assertNull(barFunctionCallTypeCheckCall.getFirstChild());

    Node bazFunctionCallNode = new Node(Token.CALL, bazConstructorNode, null);
    Node bazFunctionCall = new Node(Token.CALL, bazFunctionNode, ImmutableList.of(new Node(Token.NULL)));
    Node bazFunctionCallTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getParameters().get(0).getJSType(), bazFunctionCall);
    assertEquals(bazFunctionCall, bazFunctionCallTypeCheckCall.getFirstChild());

    // Checking function return types.
    Node fooFunctionReturn = new Node(Token.RETURN, null, new Node(Token.STRING, "bar"));
    Node fooFunctionReturnTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getReturnType(), fooFunctionReturn);
    assertEquals(new Node(Token.STRING, "bar"), fooFunctionReturnTypeCheckCall.getFirstChild());

    Node barFunctionReturn = new Node(Token.RETURN, null, new Node(Token.NUMBER, 42));
    Node barFunctionReturnTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getReturnType(), barFunctionReturn);
    assertEquals(new Node(Token.NUMBER, 42), barFunctionReturnTypeCheckCall.getFirstChild());

    Node bazFunctionReturn = new Node(Token.RETURN, null, new Node(Token.STRING, "baz"));
    Node bazFunctionReturnTypeCheckCall = createCheckTypeCallNode(fooFunctionType.getReturnType(), bazFunctionReturn);
    assertEquals(new Node(Token.STRING, "baz"), bazFunctionReturnTypeCheckCall.getFirstChild());
  }

  private Node createCheckTypeCallNode(JSType expectedType, Node node) {
    return new RuntimeTypeCheck(compiler, null).createCheckTypeCallNode(expectedType, node);
  }
}