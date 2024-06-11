import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.jscomp.RuntimeTypeCheck.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  private NodeFactory nodeFactory;
  private CompilerPassManager passManager;
  private CompilerOptions options;

  @Before
  public void setUp() {
    nodeFactory = NodeFactory.newInstance();
    options = new CompilerOptions();
    passManager = new CompilerPassManager(options);
  }

  @Test
  public void testRuntimeTypeCheck() throws IOException {
    String sourceCode = "function Foo() {}";
    Node externs = Node.newScript(nodeFactory, SourceName.getScriptName("externs.js"));
    Node root = Node.newScript(nodeFactory, SourceName.getScriptName("main.js"), sourceCode);

    passManager.addPass(new RuntimeTypeCheck(passManager.getCompiler(), null));
    CompilerPass result = passManager.run(externs, root, options);

    Node functionNode = NodeUtil.getFunctionByName(root, "Foo");
    Node constructorNode = functionNode.getLastChild();

    // Check that the marker properties have been added to the constructor.
    Node prototypeNode = NodeUtil.getPrototypeNode(constructorNode);
    assertNotNull(prototypeNode);
    Node instanceMarkerNode = NodeUtil.getGetProperty(prototypeNode, "instance_of__Foo");
    assertNotNull(instanceMarkerNode);

    // Check that the checkType calls have been added to the constructor.
    Node checkTypeCall = NodeUtil.getCall(prototypeNode, "checkType");
    assertNotNull(checkTypeCall);

    // Check that the checkType call has the correct arguments.
    Node checkTypeArguments = checkTypeCall.getLastChild();
    assertEquals(1, checkTypeArguments.getNumChildren());
    Node instanceCheckerNode = checkTypeArguments.getFirstChild();
    assertNotNull(instanceCheckerNode);
    Node instanceNode = NodeUtil.getGetProperty(constructorNode, "prototype");
    assertSame(instanceCheckerNode, NodeUtil.getCall(instanceNode, "instance_of__Foo"));

    // Check that there is no runtime type checking for primitive types.
    Node numberNode = newNode(Token.NUMBER, 42);
    Node numberCheckerCall = NodeUtil.getCall(numberNode, "checkType");
    assertNull(numberCheckerCall);

    // Check that there is no runtime type checking for void.
    Node voidNode = newNode(Token.VOID, null);
    Node voidCheckerCall = NodeUtil.getCall(voidNode, "checkType");
    assertNull(voidCheckerCall);

    // Check that there is runtime type checking for a class.
    Node barFunctionNode = newNode(Token.FUNCTION, null, ImmutableList.of(), null);
    barFunctionNode.setJSType(new ObjectType("Bar"));
    Node barConstructorNode = barFunctionNode.getLastChild();
    passManager.run(externs, Node.newScript(nodeFactory, SourceName.getScriptName(""), ImmutableList.of(barFunctionNode)), options);
    Node barPrototypeNode = NodeUtil.getPrototypeNode(barConstructorNode);
    Node barInstanceMarkerNode = NodeUtil.getGetProperty(barPrototypeNode, "instance_of__Bar");
    assertNotNull(barInstanceMarkerNode);
    Node barCheckTypeCall = NodeUtil.getCall(barPrototypeNode, "checkType");
    assertNotNull(barCheckTypeCall);
    Node barInstanceCheckerNode = barCheckTypeCall.getLastChild();
    assertNotNull(barInstanceCheckerNode);
    Node barInstanceNode = NodeUtil.getGetProperty(barConstructorNode, "prototype");
    assertSame(barInstanceCheckerNode, NodeUtil.getCall(barInstanceNode, "instance_of__Bar"));

    // Check that there is runtime type checking for an interface.
    Node bazFunctionNode = newNode(Token.FUNCTION, null, ImmutableList.of("I"), null);
    bazFunctionNode.setJSType(new FunctionType(new ObjectType("Baz"), ImmutableList.of(new ObjectType("I"))));
    Node bazConstructorNode = bazFunctionNode.getLastChild();
    passManager.run(externs, Node.newScript(nodeFactory, SourceName.getScriptName(""), ImmutableList.of(bazFunctionNode)), options);
    Node bazPrototypeNode = NodeUtil.getPrototypeNode(bazConstructorNode);
    Node bazInstanceMarkerNode = NodeUtil.getGetProperty(bazPrototypeNode, "instance_of__Baz");
    assertNotNull(bazInstanceMarkerNode);
    Node bazCheckTypeCall = NodeUtil.getCall(bazPrototypeNode, "checkType");
    assertNotNull(bazCheckTypeCall);
    Node bazInterfaceCheckerNodes = bazCheckTypeCall.getLastChild();
    assertEquals(1, bazInterfaceCheckerNodes.getNumChildren());
    Node bazInterfaceCheckerNode = bazInterfaceCheckerNodes.getFirstChild();
    assertNotNull(bazInterfaceCheckerNode);
    TreeSet<String> interfaceCheckerNames = new TreeSet<String>(Comparator.naturalOrder());
    for (Node ifaceChecker : Iterables.getReversed(NodeUtil.getChildren(bazFunctionNode))) {
      if (ifaceChecker.getType() == Token.OBJECT && ifaceChecker.getFirstChild().getJSType() instanceof ObjectType) {
        ObjectType ifaceType = (ObjectType) ifaceChecker.getFirstChild().getJSType();
        interfaceCheckerNames.add(ifaceType.getReferenceName());
      }
    }
    for (String ifaceName : interfaceCheckerNames) {
      assertSame(bazInterfaceCheckerNode, NodeUtil.getCall(bazPrototypeNode, "implements__" + ifaceName));
    }

    // Check that there is runtime type checking for a union type.
    Node quxFunctionNode = newNode(Token.FUNCTION, null, ImmutableList.of("A", "B"), null);
    quxFunctionNode.setJSType(new UnionType(new ObjectType("A"), new ObjectType("B")));
    Node quxConstructorNode = quxFunctionNode.getLastChild();
    passManager.run(externs, Node.newScript(nodeFactory, SourceName.getScriptName(""), ImmutableList.of(quxFunctionNode)), options);
    Node quxPrototypeNode = NodeUtil.getPrototypeNode(quxConstructorNode);
    Node quxCheckTypeCall = NodeUtil.getCall(quxPrototypeNode, "checkType");
    assertNotNull(quxCheckTypeCall);
    Node quxCheckTypeArguments = quxCheckTypeCall.getLastChild();
    assertEquals(2, quxCheckTypeArguments.getNumChildren());
    for (Node checkerNode : quxCheckTypeArguments.getChildren()) {
      assertNotNull(checkerNode);
      if (checkerNode.getType() == Token.OBJECT && checkerNode.getFirstChild().getJSType() instanceof ObjectType) {
        ObjectType checkerType = (ObjectType) checkerNode.getFirstChild().getJSType();
        String checkerName = checkerType.getReferenceName();
        if (checkerName.equals("A")) {
          Node quxInstanceCheckerA = NodeUtil.getCall(quxPrototypeNode, "instance_of__A");
          assertSame(checkerNode, quxInstanceCheckerA);
        } else if (checkerName.equals("B")) {
          Node quxInstanceCheckerB = NodeUtil.getCall(quxPrototypeNode, "instance_of__B");
          assertSame(checkerNode, quxInstanceCheckerB);
        }
      }
    }
  }

  private static Node newNode(int type, @Nullable Node firstChild) {
    return newNode(type, ImmutableList.of(), firstChild);
  }

  private static Node newNode(int type, Iterable<Node> children, @Nullable Node firstChild) {
    Node node = NodeFactory.getFactory().newNodeFromToken(type);
    if (children != null) {
      for (Node child : children) {
        node.addChild(child);
      }
    }
    if (firstChild != null) {
      node.setFirstChild(firstChild);
      node.setLastChild(firstChild);
    }
    return node;
  }
}