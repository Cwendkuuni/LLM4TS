import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.TokenStream;

import java.util.List;

public class MakeDeclaredNamesUniqueTest {

  private static class TestRenamer implements Renamer {
    private final Map<String, String> declarations = Maps.newHashMap();
    private int nextId = 0;

    @Override
    public void addDeclaredName(String name) {
      declarations.put(name, "renamed_" + nextId++);
    }

    @Override
    public String getReplacementName(String oldName) {
      return declarations.get(oldName);
    }

    @Override
    public boolean stripConstIfReplaced() {
      return false;
    }

    @Override
    public Renamer forChildScope() {
      return this;
    }

    @Override
    public int getLastDeclaredNameId() {
      return nextId - 1;
    }
  }

  @org.junit.Test
  public void testRenameDeclaredNames() {
    Compiler compiler = mock(Compiler.class);
    TokenStream tokenStream = mock(TokenStream.class);
    Scope scope = mock(Scope.class);
    NodeTraversal nodeTraversal = spy(new NodeTraversal(compiler, tokenStream, scope));

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique.ContextualRenamer renamer = makeDeclaredNamesUnique.new ContextualRenamer();
    makeDeclaredNamesUnique.setTraversalCallbacks(nodeTraversal);

    // Test empty case.
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq(""), any(Node.class), any(Var.class), any(Node.class));

    // Test rename a single declared name.
    String name1 = "a";
    Var var1 = mock(Var.class);
    Node node1 = mock(Node.class);
    var1.setNameNode(node1);
    scope.declare(name1, node1, null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_0"), any(Node.class), eq(var1), any(Node.class));

    // Test rename multiple declared names.
    String name2 = "b";
    Var var2 = mock(Var.class);
    Node node2 = mock(Node.class);
    var2.setNameNode(node2);
    scope.declare(name2, node2, null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_1"), any(Node.class), eq(var2), any(Node.class));

    // Test rename multiple declared names with the same name.
    String name3 = "c";
    Var var3 = mock(Var.class);
    Node node3 = mock(Node.class);
    var3.setNameNode(node3);
    scope.declare(name3, node3, null, null);
    scope.declare(name3, null, null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_2"), any(Node.class), eq(var3), any(Node.class));

    // Test getLastDeclaredNameId.
    TestRenamer testRenamer = new TestRenamer();
    MakeDeclaredNamesUnique makeDeclaredNamesUniqueTest = new MakeDeclaredNamesUnique(testRenamer);
    assertEquals(1, makeDeclaredNamesUniqueTest.getRenamer().getLastDeclaredNameId());
  }

  @org.junit.Test
  public void testRenameFunctionName() {
    Compiler compiler = mock(Compiler.class);
    TokenStream tokenStream = mock(TokenStream.class);
    Scope scope = mock(Scope.class);
    NodeTraversal nodeTraversal = spy(new NodeTraversal(compiler, tokenStream, scope));

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique.ContextualRenamer renamer = makeDeclaredNamesUnique.new ContextualRenamer();
    makeDeclaredNamesUnique.setTraversalCallbacks(nodeTraversal);

    String functionName = "foo";
    Node functionNode = mock(Node.class);
    functionNode.setType(Token.FUNCTION);
    Node bodyNode = mock(Node.class);
    functionNode.setLastChild(bodyNode);
    Node paramsNode = mock(Node.class);
    functionNode.setFirstChild(paramsNode);
    Node paramNode = mock(Node.class);
    paramsNode.setFirstChild(paramNode);
    String paramName = "bar";
    paramNode.setString(paramName);

    nodeTraversal.setCurrentNode(functionNode);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq(functionName), any(Node.class), null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_" + renamer.getLastDeclaredNameId()), any(Node.class), null, null);

    nodeTraversal.setCurrentNode(paramNode);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq(paramName), any(Node.class), null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_" + renamer.getLastDeclaredNameId()), any(Node.class), null, null);
  }

  @org.junit.Test
  public void testRenameCatchVariableName() {
    Compiler compiler = mock(Compiler.class);
    TokenStream tokenStream = mock(TokenStream.class);
    Scope scope = mock(Scope.class);
    NodeTraversal nodeTraversal = spy(new NodeTraversal(compiler, tokenStream, scope));

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique.ContextualRenamer renamer = makeDeclaredNamesUnique.new ContextualRenamer();
    makeDeclaredNamesUnique.setTraversalCallbacks(nodeTraversal);

    String catchName = "catchVariable";
    Node catchNode = mock(Node.class);
    catchNode.setType(Token.CATCH);
    Node variableNode = mock(Node.class);
    variableNode.setString(catchName);
    catchNode.setFirstChild(variableNode);

    nodeTraversal.setCurrentNode(catchNode);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq(catchName), any(Node.class), null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_" + renamer.getLastDeclaredNameId()), any(Node.class), null, null);
  }

  @org.junit.Test
  public void testRenameFunctionRecursiveName() {
    Compiler compiler = mock(Compiler.class);
    TokenStream tokenStream = mock(TokenStream.class);
    Scope scope = mock(Scope.class);
    NodeTraversal nodeTraversal = spy(new NodeTraversal(compiler, tokenStream, scope));

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique.ContextualRenamer renamer = makeDeclaredNamesUnique.new ContextualRenamer();
    makeDeclaredNamesUnique.setTraversalCallbacks(nodeTraversal);

    String name = "recursiveFunction";
    Node functionNode = mock(Node.class);
    functionNode.setType(Token.FUNCTION);
    Node bodyNode = mock(Node.class);
    functionNode.setLastChild(bodyNode);
    Node paramNode = mock(Node.class);
    Node paramNameNode = mock(Node.class);
    paramNode.setFirstChild(paramNameNode);
    paramNameNode.setString(name);
    functionNode.setFirstChild(paramNode);
    Node recursiveNameNode = mock(Node.class);
    recursiveNameNode.setString(name);
    functionNode.setFirstChild(recursiveNameNode);

    nodeTraversal.setCurrentNode(functionNode);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq(name), any(Node.class), null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_" + renamer.getLastDeclaredNameId()), any(Node.class), null, null);

    nodeTraversal.setCurrentNode(recursiveNameNode);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq(name), any(Node.class), null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_" + renamer.getLastDeclaredNameId()), any(Node.class), null, null);
  }

  @org.junit.Test
  public void testRenameCatchExceptionName() {
    Compiler compiler = mock(Compiler.class);
    TokenStream tokenStream = mock(TokenStream.class);
    Scope scope = mock(Scope.class);
    NodeTraversal nodeTraversal = spy(new NodeTraversal(compiler, tokenStream, scope));

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique.ContextualRenamer renamer = makeDeclaredNamesUnique.new ContextualRenamer();
    makeDeclaredNamesUnique.setTraversalCallbacks(nodeTraversal);

    String catchName = "catchException";
    Node catchNode = mock(Node.class);
    catchNode.setType(Token.CATCH);
    Node variableNode = mock(Node.class);
    variableNode.setString(catchName);
    Node exceptionNameNode = mock(Node.class);
    exceptionNameNode.setString("Exception");
    variableNode.setFirstChild(exceptionNameNode);
    catchNode.setFirstChild(variableNode);

    nodeTraversal.setCurrentNode(catchNode);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq(catchName), any(Node.class), null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_" + renamer.getLastDeclaredNameId()), any(Node.class), null, null);

    nodeTraversal.setCurrentNode(exceptionNameNode);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("Exception"), any(Node.class), null, null);
    nodeTraversal.traverse(null, null);
    verify(scope).declare(eq("renamed_" + renamer.getLastDeclaredNameId() + "_Exception"), any(Node.class), null, null);
  }
}