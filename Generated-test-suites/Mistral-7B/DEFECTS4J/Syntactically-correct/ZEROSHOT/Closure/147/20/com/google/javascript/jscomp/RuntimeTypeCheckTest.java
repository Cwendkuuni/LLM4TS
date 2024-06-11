import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.jstype.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.SourceName;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws Exception {
    String sourceCode = "function Foo() {}";

    AbstractCompiler compiler = new Compiler();
    CompilerInput input = compiler.addSourceUnit(new SourceName("test.js"), sourceCode);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    CompilerPass pass = runtimeTypeCheck;
    Node root = NodeUtil.parseJs(sourceCode, null);
    pass.process(input.getExterns(), root);

    FunctionType fooType = (FunctionType) NodeUtil.getFunctionType(root, "Foo");
    Node constructor = NodeUtil.getFunctionDeclaration(root, "Foo");
    Node classMarkerNode = NodeUtil.getMarkerNode(constructor, "instance_of__Foo");
    Node instanceOfCall = NodeUtil.newCallExpression(
        NodeUtil.getIdentifier("jscomp.typecheck.instanceof"),
        Node.newArray(classMarkerNode));
    Node expectedResult = NodeUtil.newBinaryOperator(
        Node.newIdentifier("assert"),
        Token.TYPES_EQUALS,
        NodeUtil.getBooleanLiteral(true),
        instanceOfCall);

    // Check that instance_of__Foo marker was added to the constructor.
    Node actualResult = NodeUtil.getFirstChild(classMarkerNode);
    assertEquals(expectedResult.toSourceString(), actualResult.toSourceString());

    // Test that the checkType function call is inserted at the expected position.
    Node checkTypeCall = NodeUtil.getFirstChild(constructor);
    assertEquals(NodeType.CALL, checkTypeCall.getType());
    assertSame(NodeUtil.getIdentifier("checkType"), checkTypeCall.getFirstChild());

    // Test that the checkType function call has the expected arguments.
    Node argumentList = checkTypeCall.getLastChild();
    assertEquals(NodeType.ARRAY_LITERAL, argumentList.getType());
    ImmutableList<Node> arguments = NodeUtil.getArrayElements(argumentList);
    assertEquals(2, arguments.size());

    Node checkerNode = arguments.get(0);
    assertEquals(NodeType.CALL, checkerNode.getType());
    assertSame(NodeUtil.getIdentifier("jscomp.typecheck.valueChecker"), checkerNode.getFirstChild());
    assertEquals(NodeType.STRING, checkerNode.getLastChild().getType());
    assertEquals("Foo", checkerNode.getLastChild().getString());

    Node markerNode = arguments.get(1);
    assertEquals(NodeType.GET_PROPERTY, markerNode.getType());
    assertSame(constructor, markerNode.getFirstChild());
    assertEquals("prototype", markerNode.getLastChild().getString());
  }
}