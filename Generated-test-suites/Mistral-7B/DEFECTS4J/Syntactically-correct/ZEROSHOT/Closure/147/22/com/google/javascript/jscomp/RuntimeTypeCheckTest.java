import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.jstype.InstanceType;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Unit tests for {@link RuntimeTypeCheck}.
 */
public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private Node rootNode;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    rootNode = Node.newBlock(Token.BLOCK);
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testProcess_SimpleFunction() {
    FunctionType functionType = FunctionType.getFunctionType(
        "MyFunction", ImmutableList.of(), new ObjectType("MyReturnType"));
    Node functionNode = NodeUtil.newFunctionNode("MyFunction", functionType);

    Node functionNodeParent = Node.newAssign(Token.ASSIGN,
        new Node(Token.GETELEM, rootNode, new Node(Token.ID, "myFunction")),
        functionNode);

    rootNode.addChildToBack(functionNodeParent);

    runtimeTypeCheck.process(null, rootNode);

    Node checkNode = findCheckerNode(functionNode);
    assertEquals(functionType.getReturnType(), checkNode.getFirstChild().getType());
  }

  @Test
  public void testProcess_FunctionWithParameters() {
    FunctionType functionType = FunctionType.getFunctionType(
        "MyFunction",
        ImmutableList.of(
            new ObjectType("StringParam"),
            new InstanceType(new ObjectType("MyInterface"))),
        new ObjectType("MyReturnType"));
    Node functionNode = NodeUtil.newFunctionNode("MyFunction", functionType);

    Node functionNodeParent = Node.newAssign(Token.ASSIGN,
        new Node(Token.GETELEM, rootNode, new Node(Token.ID, "myFunction")),
        functionNode);

    rootNode.addChildToBack(functionNodeParent);

    runtimeTypeCheck.process(null, rootNode);

    Node checkNode1 = findCheckerNode(functionNode.getChild(1));
    assertEquals("StringParam", checkNode1.getFirstChild().getType().getName());

    Node checkNode2 = findCheckerNode(functionNode.getChild(2));
    assertEquals("MyInterface", checkNode2.getFirstChild().getType().getName());
  }

  @Test
  public void testProcess_FunctionWithUnionReturnType() {
    FunctionType functionType = FunctionType.getFunctionType(
        "MyFunction",
        ImmutableList.of(new ObjectType("StringParam")),
        new UnionType(
            new ObjectType("MyReturnType1"), new ObjectType("MyReturnType2")));
    Node functionNode = NodeUtil.newFunctionNode("MyFunction", functionType);

    Node functionNodeParent = Node.newAssign(Token.ASSIGN,
        new Node(Token.GETELEM, rootNode, new Node(Token.ID, "myFunction")),
        functionNode);

    rootNode.addChildToBack(functionNodeParent);

    runtimeTypeCheck.process(null, rootNode);

    Node checkNode1 = findCheckerNode(functionNode.getChild(1));
    assertEquals("StringParam", checkNode1.getFirstChild().getType().getName());

    Node checkNode2 = findCheckerNode(functionNode);
    FunctionType checkNodeType = (FunctionType) checkNode2.getType();
    assertEquals(2, checkNodeType.getParameters().size());
    assertEquals("MyReturnType1", checkNodeType.getReturnType().getName());
    assertEquals("MyReturnType2", checkNodeType.getReturnTypeAlternates().get(0).getName());
  }

  @Test
  public void testProcess_ClassWithMarker() {
    ObjectType classType = new ObjectType("MyClass");
    Node classNode = NodeUtil.newQualifiedNameNode(classType.getReferenceName(), -1, -1);

    Node classNodeParent = Node.newAssign(Token.ASSIGN,
        new Node(Token.GETELEM, rootNode, new Node(Token.ID, "MyClass")),
        classNode);

    rootNode.addChildToBack(classNodeParent);

    runtimeTypeCheck.process(null, rootNode);

    Node markerNode = findMarkerNode(classNode);
    assertEquals("instance_of__MyClass", markerNode.getType().getName());
  }

  @Test
  public void testProcess_InterfaceWithMarker() {
    ObjectType interfaceType = new ObjectType("MyInterface");
    Node interfaceTypeConstructorNode = NodeUtil.newFunctionNode(
        "MyInterface",
        FunctionType.getConstructorType(interfaceType, Collections.emptyList()));

    Node interfaceTypeNode = new Node(Token.GETELEM,
        NodeUtil.newQualifiedNameNode(interfaceType.getReferenceName(), -1, -1),
        new Node(Token.ID, "prototype"));

    Node interfaceTypeNodeParent = Node.newAssign(Token.ASSIGN,
        new Node(Token.GETELEM, rootNode, new Node(Token.ID, "MyInterface")),
        interfaceTypeNode);

    rootNode.addChildToBack(interfaceTypeNodeParent);

    runtimeTypeCheck.process(null, rootNode);

    Node markerNode = findMarkerNode(interfaceTypeNode);
    assertEquals("implements__MyInterface", markerNode.getType().getName());
  }

  @Test
  public void testProcess_FunctionWithMultipleConstructors() {
    ObjectType classType =
        new ObjectType("MyClass", null, ImmutableList.of(
            new FunctionType(
                "MyClassCtor1", ImmutableList.of(), new ObjectType("MyClass")),
            new FunctionType(
                "MyClassCtor2", ImmutableList.of(new ObjectType("String")), new ObjectType("MyClass"))));

    Node classNode = NodeUtil.newQualifiedNameNode(classType.getReferenceName(), -1, -1);

    Node classNodeParent = Node.newAssign(Token.ASSIGN,
        new Node(Token.GETELEM, rootNode, new Node(Token.ID, "MyClass")),
        classNode);

    rootNode.addChildToBack(classNodeParent);

    runtimeTypeCheck.process(null, rootNode);

    Node markerNode1 = findMarkerNode(classNode);
    assertEquals("instance_of__MyClass", markerNode1.getType().getName());

    Node constructor1CallNode = findNodeByCode(rootNode, "MyClassCtor1.call");
    Node markerNode2 = findMarkerNode(constructor1CallNode.getChild(1));
    assertEquals("instance_of__MyClass", markerNode2.getType().getName());
  }

  @Test
  public void testProcess_FunctionWithInterfaceImplementation() {
    ObjectType interfaceType = new ObjectType("MyInterface");
    ObjectType classType = new ObjectType("MyClass", null, ImmutableList.of(interfaceType));

    Node interfaceTypeNode = NodeUtil.newQualifiedNameNode(interfaceType.getReferenceName(), -1, -1);
    Node classNode = NodeUtil.newQualifiedNameNode(classType.getReferenceName(), -1, -1);

    Node interfaceTypeNodeParent = Node.newAssign(Token.ASSIGN,
        new Node(Token.GETELEM, rootNode, new Node(Token.ID, "MyInterface")),
        interfaceTypeNode);

    Node classNodeParent = Node.newAssign(Token.ASSIGN,
        new Node(Token.GETELEM, rootNode, new Node(Token.ID, "MyClass")),
        classNode);

    rootNode.addChildToBack(interfaceTypeNodeParent);
    rootNode.addChildToBack(classNodeParent);

    runtimeTypeCheck.process(null, rootNode);

    Node markerNode1 = findMarkerNode(interfaceTypeNode);
    assertEquals("implements__MyInterface", markerNode1.getType().getName());

    Node markerNode2 = findMarkerNode(classNode);
    assertEquals("instance_of__MyClass", markerNode2.getType().getName());
  }

  @Test
  public void testProcess_FunctionWithNullCheck() {
    FunctionType functionType = FunctionType.getFunctionType(
        "MyFunction", ImmutableList.of(), new ObjectType("MyReturnType"));
    Node functionNode = NodeUtil.newFunctionNode("MyFunction", functionType);

    Node functionNodeParent = Node.newAssign(Token.ASSIGN,
        new Node(Token.GETELEM, rootNode, new Node(Token.ID, "myFunction")),
        functionNode);

    rootNode.addChildToBack(functionNodeParent);

    runtimeTypeCheck.process(null, rootNode);

    Node checkNode = findCheckerNode(functionNode);
    assertEquals(functionType.getReturnType(), checkNode.getFirstChild().getType());

    Node checkNodeParent = checkNode.getParent();
    Node nullCheckNode = NodeUtil.newBooleanLiteral(checkNodeParent, true);
    checkNodeParent.addChildAfter(nullCheckNode, checkNode);

    runtimeTypeCheck.process(null, rootNode);

    Node nullCheckNode2 = findCheckerNode(nullCheckNode);
    assertEquals(functionType.getReturnType(), nullCheckNode2.getFirstChild().getType());
  }

  private Node findNodeByCode(Node node, String code) {
    for (Node child : node.getChildren()) {
      if (child.getClass() == Node.class) {
        Node childNode = (Node) child;
        if (childNode.getClass() == Node.Call.class) {
          Node callNode = (Node) childNode;
          if (callNode.getCode().equals(code)) {
            return callNode;
          }
        } else if (findNodeByCode(childNode, code) != null) {
          return childNode;
        }
      } else if (findNodeByCode((Node) child, code) != null) {
        return (Node) child;
      }
    }
    return null;
  }

  private Node findMarkerNode(Node node) {
    for (Node child : node.getChildren()) {
      if (child.getType() == Token.GETPROP && child.getFirstChild().getType() == Token.ID) {
        Node markerNode = child.getChild(1);
        if (markerNode.getType() == Token.STRING && markerNode.getStringValue().startsWith("instance_of__")
            || markerNode.getStringValue().startsWith("implements__")) {
          return markerNode;
        }
      }
      if (findMarkerNode((Node) child) != null) {
        return child;
      }
    }
    return null;
  }

  private Node findCheckerNode(Node node) {
    for (Node child : node.getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().getType() == NodeUtil.QUALIFIED) {
        Node functionName = (Node) child.getFirstChild();
        Node argumentNode = child.getChild(1);
        if (functionName.getStringValue().equals("checkType")) {
          return argumentNode;
        }
      }
      if (findCheckerNode((Node) child) != null) {
        return child;
      }
    }
    return null;
  }
}