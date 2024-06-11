import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;

public class VarCheckTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private VarCheck varCheck;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    nodeFactory = new NodeFactory(compiler);
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testUndefinedVariable() {
    CompilerInput input = new CompilerInput("Test.js", "testContent", null);
    Node root = Node.newBlock(Node.NEW_ARRAY(Token.BLOCK, 2));
    Node varNode = Node.newName("foo");
    root.addChildToBack(Node.newVar("foo"));
    root.addChildToBack(varNode);
    root.addChildToBack(Node.newName("bar").get(0));

    when(compiler.newExternInput("")).thenReturn(new CompilerInput("", "", null));
    when(compiler.newExternInput("{SyntheticVarsDecl}"))
        .thenReturn(new CompilerInput("", "syntheticVarsDecl", null));
    doNothing().when(compiler).newExternInput("{SyntheticVarsDecl}");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    varCheck.process(null, root);

    NodeTraversal.traverse(compiler, root, varCheck);
    traversal.run();

    verify(compiler).report(varNode, UNDEFINED_VAR_ERROR, "foo");
  }

  @Test
  public void testVariableAccessInSameFile() {
    CompilerInput input = new CompilerInput("Test.js", "testContent", null);
    Node root = Node.newBlock(Node.NEW_ARRAY(Token.BLOCK, 2));
    Node varNode = Node.newVar("foo");
    root.addChildToBack(varNode);
    root.addChildToBack(Node.newName("foo"));

    when(compiler.newExternInput("")).thenReturn(new CompilerInput("", "", null));
    when(compiler.newExternInput("{SyntheticVarsDecl}"))
        .thenReturn(new CompilerInput("", "syntheticVarsDecl", null));
    doNothing().when(compiler).newExternInput("{SyntheticVarsDecl}");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    varCheck.process(null, root);
    varCheck.process(null, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(null, root), varCheck);

    traversal.run();

    verify(compiler, times(0)).report(varNode, UNDEFINED_VAR_ERROR, "foo");
  }

  @Test
  public void testMissingModuleDependency() {
    CompilerInput input1 = new CompilerInput("A.js", "contentA", null);
    Node rootA = Node.newBlock(Node.NEW_ARRAY(Token.BLOCK, 3));
    rootA.addChildToBack(Node.newVar("x"));
    rootA.addChildToBack(Node.newName("y").get(0));
    rootA.addChildToBack(Node.newName("z").get(0));

    CompilerInput inputB = new CompilerInput("B.js", "contentB", null);
    Node rootB = Node.newBlock(Node.NEW_ARRAY(Token.BLOCK, 2));
    rootB.addChildToBack(Node.newName("x"));
    rootB.addChildToBack(Node.newName("z").get(0));

    when(compiler.newExternInput(""))
        .thenReturn(new CompilerInput("", "", null));
    when(compiler.newExternInput("{SyntheticVarsDeclA}"))
        .thenReturn(new CompilerInput("", "syntheticVarsDeclA", null));
    when(compiler.newExternInput("{SyntheticVarsDeclB}"))
        .thenReturn(new CompilerInput("", "syntheticVarsDeclB", null));

    NodeTraversal traversal = new NodeTraversal(compiler, inputB, rootB, null);
    varCheck.process(new CompilerInput("A.js", "contentA", null), null);
    varCheck.process(null, null);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(null, rootB), varCheck);

    traversal.run();

    verify(compiler).report(Node.newName("x"), MISSING_MODULE_DEP_ERROR,
        "B.js", "A.js", "x");
  }

  @Test
  public void testStrictModuleDependency() {
    CompilerInput input1 = new CompilerInput("A.js", "contentA", null);
    Node rootA = Node.newBlock(Node.NEW_ARRAY(Token.BLOCK, 3));
    rootA.addChildToBack(Node.newVar("x"));
    rootA.addChildToBack(Node.newName("y").get(0));
    rootA.addChildToBack(Node.newName("z").get(0));

    CompilerInput inputB = new CompilerInput("B.js", "contentB", null);
    Node rootB = Node.newBlock(Node.NEW_ARRAY(Token.BLOCK, 2));
    rootB.addChildToBack(Node.newName("x"));
    rootB.addChildToBack(Node.newName("z").get(0));

    when(compiler.newExternInput(""))
        .thenReturn(new CompilerInput("", "", null));
    when(compiler.newExternInput("{SyntheticVarsDeclA}"))
        .thenReturn(new CompilerInput("", "syntheticVarsDeclA", null));
    when(compiler.newExternInput("{SyntheticVarsDeclB}"))
        .thenReturn(new CompilerInput("", "syntheticVarsDeclB", null));

    doThrow(new IllegalStateException("Expected error"))
        .when(compiler).getErrorLevel(anyString());

    NodeTraversal traversal = new NodeTraversal(compiler, inputB, rootB, null);
    varCheck.process(new CompilerInput("A.js", "contentA", null), null);
    varCheck.process(null, null);

    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(null, rootB), varCheck);

    traversal.run();

    verify(compiler, never()).report(any(Node.class), STRICT_MODULE_DEP_ERROR,
        anyString(), anyString(), anyString());

    verify(compiler).throwing(new IllegalStateException("Expected error"));
  }

  @Test
  public void testNameReferenceInExterns() {
    CompilerInput input = new CompilerInput("Test.js", "testContent", null);
    Node root = Node.newBlock(Node.NEW_ARRAY(Token.BLOCK, 2));
    Node varNode = Node.newVar("foo");
    root.addChildToBack(varNode);
    root.addChildToBack(Node.newName("foo"));

    when(compiler.newExternInput(""))
        .thenReturn(new CompilerInput("", "", null));
    when(compiler.newExternInput("{SyntheticVarsDecl}"))
        .thenReturn(new CompilerInput("", "syntheticVarsDecl", null));
    doNothing().when(compiler).newExternInput("{SyntheticVarsDecl}");

    NodeTraversal traversal = new NodeTraversal(compiler, input, root, null);
    varCheck.process(null, root);
    varCheck.process(null, null);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(null, root), varCheck);

    traversal.run();

    verify(compiler, times(1)).report(varNode, NAME_REFERENCE_IN_EXTERNS_ERROR,
        "foo");
  }
}