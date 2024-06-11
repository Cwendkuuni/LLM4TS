import static org.junit.Assert.assertThat;
import static com.google.common.collect.Iterables.get;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.DiagnosticsGroup;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TypeChecker;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private static Node createNode(int type, int line, int col) {
    return NodeFactory.createTokenNode(type, line, col);
  }

  private static Node createFunctionNode() {
    return NodeFactory.createFunctionDeclaration(
        "functionName",
        createNode(Token.FUNCTION, 1, 1),
        null,
        null,
        new Node[0],
        null,
        null,
        new Node[0]);
  }

  private static JSDocInfo getFunctionJsDocInfo(Node node) {
    return node.getJSDocInfo();
  }

  private Checker createChecker() {
    Checker checker = new Checker();
    TypeChecker typeChecker = new TypeChecker(checker);
    typeChecker.init();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(checker, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(typeChecker, checkGlobalThis);
    nodeTraversal.traverse(createNode(Token.SCRIPT, 1, 1), null);
    return checker;
  }

  @Test
  public void testCheckGlobalThis() {
    Checker checker = createChecker();
    Diagnostics diagnostics = checker.getDiagnostics();
    DiagnosticsGroup diagnosticsGroup = diagnostics.getGroup(CheckGlobalThis.GLOBAL_THIS);
    assertThat(diagnosticsGroup.getLength(), is(3));

    Node function1 = createFunctionNode();
    Node function2 = NodeFactory.createFunctionDeclaration(
        "function2",
        createNode(Token.FUNCTION, 1, 1),
        null,
        null,
        new Node[0],
        null,
        null,
        new Node[0]);
    Node prototype = NodeFactory.createGetProp(
        NodeFactory.createThis(),
        createNode(Token.IDENTIFIER, "prototype", 1, 1));
    Node function3 = NodeFactory.createFunctionDeclaration(
        "function3",
        createNode(Token.FUNCTION, 1, 1),
        null,
        prototype,
        new Node[0],
        null,
        null,
        new Node[0]);

    Node assignment1 = NodeFactory.createAssign(
        NodeFactory.createThis(),
        NodeFactory.createIdentifier("x"));
    Node assignment2 = NodeFactory.createAssign(
        NodeFactory.createGetProp(createNode(Token.VAR, "a", 1, 1), "x"),
        NodeFactory.createThis());
    Node assignment3 = NodeFactory.createAssign(
        NodeFactory.createGetProp(NodeFactory.createThis(), "x"),
        NodeFactory.createIdentifier("c"));

    Node block = NodeFactory.createBlock(new Node[0]);
    Node script = NodeFactory.createScript(new Node[0], block);

    Node parentBlock = NodeFactory.createBlock(
        new Node[] { function1, NodeFactory.createSemicolon(), function2, NodeFactory.createSemicolon(),
            function3, NodeFactory.createSemicolon(), NodeFactory.createAssign(NodeFactory.createThis(), NodeFactory.createIdentifier("x")),
            NodeFactory.createSemicolon(), NodeFactory.createAssign(NodeFactory.createIdentifier("a"), function3),
            NodeFactory.createSemicolon(), NodeFactory.createAssign(NodeFactory.createGetProp(NodeFactory.createThis(), "x"), NodeFactory.createIdentifier("c")),
            NodeFactory.createSemicolon(), NodeFactory.createSemicolon(), script });

    NodeTraversal nodeTraversal = new NodeTraversal(typeChecker, checkGlobalThis);
    nodeTraversal.traverse(script, parentBlock);

    int i = 0;
    Node currentNode = script;
    for (Node node : diagnosticsGroup) {
      String expectedMessage = "dangerous use of the global 'this' object";
      String actualMessage = get(node.getMessage().getArguments(), 0).getString();
      assertThat(actualMessage, is(expectedMessage));
      currentNode = get(node.getLocation().getParent(), 0);
      i++;
    }

    // Check that the first warning is on the function1 "this" keyword.
    Node function1This = get(function1.getChildren(), 0);
    Node function1ThisLocation = function1This.getLocation();
    assertThat(function1ThisLocation.getLineNumber(), is(1));
    assertThat(function1ThisLocation.getColumnNumber(), is(1));

    // Check that the second warning is on the assignment1 "this" keyword.
    Node assignment1This = get(assignment1.getChildren(), 0);
    Node assignment1ThisLocation = assignment1This.getLocation();
    assertThat(assignment1ThisLocation.getLineNumber(), is(1));
    assertThat(assignment1ThisLocation.getColumnNumber(), is(5));

    // Check that the third warning is on the assignment2 "this" keyword.
    Node assignment2This = get(assignment2.getChildren(), 0);
    Node assignment2ThisLocation = assignment2This.getLocation();
    assertThat(assignment2ThisLocation.getLineNumber(), is(1));
    assertThat(assignment2ThisLocation.getColumnNumber(), is(32));
  }
}