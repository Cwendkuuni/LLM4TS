import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VarCheck;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    input = new CompilerInput("test.js", "test.js");
    root = Node.newRootNode();
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testValidVariable() {
    Node variableNode = Node.newVariable("x");
    root.addChildToBack(variableNode);
    varCheck.process(input, root);
    assertFalse(varCheck.varsToDeclareInExterns.contains("x"));
  }

  @Test
  public void testUndefinedVariable() {
    Node variableNode = Node.newVariable("y");
    root.addChildToBack(variableNode);
    varCheck.process(input, root);
    assertTrue(varCheck.varsToDeclareInExterns.contains("y"));
  }

  @Test
  public void testDuplicateVariable() {
    Node firstVariableNode = Node.newVariable("x");
    Node secondVariableNode = Node.newVariable("x");
    root.addChildToBack(firstVariableNode);
    root.addChildToBack(secondVariableNode);
    varCheck.process(input, root);
    assertFalse(varCheck.varsToDeclareInExterns.contains("x"));
  }

  @Test
  public void testGlobalVariable() {
    Node variableNode = Node.newVariable("x");
    root.addChildToBack(variableNode);
    varCheck.process(input, root);
    assertNotNull(varCheck.getSynthesizedExternsInput());
    assertNotNull(varCheck.getSynthesizedExternsRoot());
  }

  @Test
  public void testFunctionVariable() {
    Node functionNode = Node.newFunction("myFunction", new Node[0], new Node[0]);
    Node variableNode = Node.newVariable("x");
    functionNode.addChildToBack(variableNode);
    root.addChildToBack(functionNode);
    varCheck.process(input, root);
    assertNotNull(varCheck.getSynthesizedExternsInput());
    assertNotNull(varCheck.getSynthesizedExternsRoot());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedExternVariable() {
    varCheck.process(input, root);
  }

  @Test
  public void testModuleDependency() {
    Node dependenciesNode = new Node(Token.LP, null);
    Node firstModuleNode = Node.newExpressionStatement(
        Node.newCall(Node.newIdentifier("require"), new Node[] {
            Node.newString("first_module")
        }));
    dependenciesNode.addChildToBack(firstModuleNode);
    Node secondModuleNode = Node.newExpressionStatement(
        Node.newCall(Node.newIdentifier("require"), new Node[] {
            Node.newString("second_module")
        }));
    dependenciesNode.addChildToBack(secondModuleNode);
    Node rootNode = Node.newRootNode();
    rootNode.addChildToBack(dependenciesNode);
    varCheck.process(input, rootNode);
    assertNotNull(varCheck.getSynthesizedExternsInput());
    assertNotNull(varCheck.getSynthesizedExternsRoot());
  }

  @Test
  public void testMissingModuleDependency() {
    Node dependenciesNode = new Node(Token.LP, null);
    Node firstModuleNode = Node.newExpressionStatement(
        Node.newCall(Node.newIdentifier("require"), new Node[] {
            Node.newString("first_module")
        }));
    Node secondModuleNode = Node.newExpressionStatement(
        Node.newCall(Node.newIdentifier("require"), new Node[] {
            Node.newString("second_module"),
            Node.newString("undefined_module")
        }));
    dependenciesNode.addChildToBack(firstModuleNode);
    dependenciesNode.addChildToBack(secondModuleNode);
    Node rootNode = Node.newRootNode();
    rootNode.addChildToBack(dependenciesNode);
    varCheck.process(input, rootNode);
    assertNotNull(varCheck.getSynthesizedExternsInput());
    assertNotNull(varCheck.getSynthesizedExternsRoot());
  }

  @Test
  public void testStrictModuleDependency() {
    Node dependenciesNode = new Node(Token.LP, null);
    Node firstModuleNode = Node.newExpressionStatement(
        Node.newCall(Node.newIdentifier("require"), new Node[] {
            Node.newString("first_module")
        }));
    Node secondModuleNode = Node.newExpressionStatement(
        Node.newCall(Node.newIdentifier("require"), new Node[] {
            Node.newString("second_module"),
            Node.newExpressionStatement(
                Node.newCall(Node.newIdentifier("assert"), new Node[] {
                    Node.newBooleanLiteral(false)
                }))
        }));
    dependenciesNode.addChildToBack(firstModuleNode);
    dependenciesNode.addChildToBack(secondModuleNode);
    Node rootNode = Node.newRootNode();
    rootNode.addChildToBack(dependenciesNode);
    varCheck.process(input, rootNode);
    assertNotNull(varCheck.getSynthesizedExternsInput());
    assertNotNull(varCheck.getSynthesizedExternsRoot());
  }
}