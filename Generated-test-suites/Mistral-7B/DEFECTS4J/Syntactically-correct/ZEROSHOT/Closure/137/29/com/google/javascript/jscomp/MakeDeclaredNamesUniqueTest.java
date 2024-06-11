import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.ScopedCallback.SCOPED_CALLBACK;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MakeDeclaredNamesUniqueTest {

  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    compiler.init();
    rootNode = compiler.parse("function f(x) { return x; }", "", null);
  }

  @After
  public void tearDown() throws Exception {
    compiler.dispose();
  }

  @Test
  public void testNoNameConflicts() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, compiler, null, makeDeclaredNamesUnique, SCOPED_CALLBACK);
    nodeTraversal.traverse();

    Scope globalScope = compiler.getGlobalScope();
    List<String> expectedNames = Lists.newArrayList("x");
    Map<String, Node> actualNames = NodeUtil.getAllLocalVarNames(rootNode);
    assertEquals(expectedNames.size(), actualNames.size());
    Iterator<Map.Entry<String, Node>> actualIterator = actualNames.entrySet().iterator();
    for (String expectedName : expectedNames) {
      String actualName = actualIterator.next().getKey();
      assertEquals(expectedName, actualName);
    }
  }

  @Test
  public void testNameConflicts() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Node functionNode = NodeUtil.createFunctionNode(Token.FUNCTION, null, "g", ImmutableSet.of(), Lists.newArrayList(
        NodeUtil.createVarNode(Token.VAR, null, "x"),
        NodeUtil.createBlockNode(Token.BLOCK, null, Lists.newArrayList(
            NodeUtil.createReturnNode(Token.RETURN, null, NodeUtil.createNameNode(Token.NAME, null, "x"))))));
    rootNode.setLastChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, compiler, null, makeDeclaredNamesUnique, SCOPED_CALLBACK);
    nodeTraversal.traverse();

    Scope globalScope = compiler.getGlobalScope();
    List<String> expectedNames = Lists.newArrayList("x_1", "x");
    Map<String, Node> actualNames = NodeUtil.getAllLocalVarNames(rootNode);
    assertEquals(expectedNames.size(), actualNames.size());
    Iterator<Map.Entry<String, Node>> actualIterator = actualNames.entrySet().iterator();
    int i = 0;
    String actualName;
    for (String expectedName : expectedNames) {
      actualName = actualIterator.next().getKey();
      if (expectedName.equals("x")) {
        assertTrue(actualName.matches("x_\\d+"));
      } else {
        assertEquals(expectedName, actualName);
      }
      i++;
    }

    // Check that the global name "x" isn't changed.
    Node globalVarNode = globalScope.getVar("x");
    assertEquals("x", globalVarNode.getString());
  }

  @Test
  public void testFunctionNameConflicts() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Node functionNode1 = NodeUtil.createFunctionNode(Token.FUNCTION, null, "f", ImmutableSet.of(), Lists.newArrayList(
        NodeUtil.createVarNode(Token.VAR, null, "x")));
    rootNode.setLastChild(functionNode1);

    Node functionNode2 = NodeUtil.createFunctionNode(Token.FUNCTION, null, "f", ImmutableSet.of(), Lists.newArrayList(
        NodeUtil.createVarNode(Token.VAR, null, "x")));
    functionNode1.getLastChild().setPreviousSibling(functionNode2);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, compiler, null, makeDeclaredNamesUnique, SCOPED_CALLBACK);
    nodeTraversal.traverse();

    Scope globalScope = compiler.getGlobalScope();
    List<String> expectedNames = Lists.newArrayList("x_1", "x_2");
    Map<String, Node> actualNames = NodeUtil.getAllLocalVarNames(rootNode);
    assertEquals(expectedNames.size(), actualNames.size());
    Iterator<Map.Entry<String, Node>> actualIterator = actualNames.entrySet().iterator();
    int i = 0;
    String actualName;
    for (String expectedName : expectedNames) {
      actualName = actualIterator.next().getKey();
      if (expectedName.equals("x")) {
        assertTrue(actualName.matches("x_\\d+"));
      } else {
        assertEquals(expectedName, actualName);
      }
      i++;
    }

    // Check that the global names aren't changed.
    Node globalVarNode1 = globalScope.getVar("f");
    assertEquals("f", globalVarNode1.getString());
    Node globalVarNode2 = globalScope.getVar("f");
    assertEquals("f", globalVarNode2.getString());
  }

  @Test
  public void testFunctionWithInsideFunctionNameConflicts() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Node outerFunctionNode = NodeUtil.createFunctionNode(Token.FUNCTION, null, "outerFn", ImmutableSet.of(), Lists.newArrayList(
        NodeUtil.createVarNode(Token.VAR, null, "x"),
        NodeUtil.createBlockNode(Token.BLOCK, null, Lists.newArrayList(
            NodeUtil.createFunctionNode(Token.FUNCTION, null, "innerFn", ImmutableSet.of(), Lists.newArrayList(
                NodeUtil.createVarNode(Token.VAR, null, "x")
            )),
            NodeUtil.createReturnNode(Token.RETURN, null, NodeUtil.createNameNode(Token.NAME, null, "x"))
        ))
    ));
    rootNode.setLastChild(outerFunctionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, compiler, null, makeDeclaredNamesUnique, SCOPED_CALLBACK);
    nodeTraversal.traverse();

    Scope globalScope = compiler.getGlobalScope();
    List<String> expectedNames = Lists.newArrayList("x_1", "x_2");
    Map<String, Node> actualNames = NodeUtil.getAllLocalVarNames(outerFunctionNode);
    assertEquals(expectedNames.size(), actualNames.size());
    Iterator<Map.Entry<String, Node>> actualIterator = actualNames.entrySet().iterator();
    int i = 0;
    String actualName;
    for (String expectedName : expectedNames) {
      actualName = actualIterator.next().getKey();
      if (expectedName.equals("x")) {
        assertTrue(actualName.matches("x_\\d+"));
      } else {
        assertEquals(expectedName, actualName);
      }
      i++;
    }

    // Check that the global names aren't changed.
    Node globalVarNode1 = globalScope.getVar("outerFn");
    assertEquals("outerFn", globalVarNode1.getString());
    Node globalVarNode2 = globalScope.getVar("innerFn");
    assertEquals("innerFn", globalVarNode2.getString());
  }

  @Test
  public void testCatchBlockNameConflicts() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Node functionNode = NodeUtil.createFunctionNode(Token.FUNCTION, null, "f", ImmutableSet.of(), Lists.newArrayList(
        NodeUtil.createVarNode(Token.VAR, null, "x"),
        NodeUtil.createBlockNode(Token.BLOCK, null, Lists.newArrayList(
            NodeUtil.createTryNode(Token.TRY, null, Lists.newArrayList(
                NodeUtil.createNameNode(Token.NAME, null, "x"),
                NodeUtil.createCatchNode(Token.CATCH, null, null, null, Lists.newArrayList(
                    NodeUtil.createVarNode(Token.VAR, null, "x"))))),
            NodeUtil.createReturnNode(Token.RETURN, null, NodeUtil.createNameNode(Token.NAME, null, "x"))
        ))
    ));
    rootNode.setLastChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, compiler, null, makeDeclaredNamesUnique, SCOPED_CALLBACK);
    nodeTraversal.traverse();

    Scope globalScope = compiler.getGlobalScope();
    List<String> expectedNames = Lists.newArrayList("x_1", "x_2");
    Map<String, Node> actualNames = NodeUtil.getAllLocalVarNames(functionNode);
    assertEquals(expectedNames.size(), actualNames.size());
    Iterator<Map.Entry<String, Node>> actualIterator = actualNames.entrySet().iterator();
    int i = 0;
    String actualName;
    for (String expectedName : expectedNames) {
      actualName = actualIterator.next().getKey();
      if (expectedName.equals("x")) {
        assertTrue(actualName.matches("x_\\d+"));
      } else {
        assertEquals(expectedName, actualName);
      }
      i++;
    }

    // Check that the global names aren't changed.
    Node globalVarNode = globalScope.getVar("x");
    assertEquals("x", globalVarNode.getString());
  }

  @Test
  public void testNameConflictsWithFunction() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    Node functionNode = NodeUtil.createFunctionNode(Token.FUNCTION, null, "g", ImmutableSet.of(), Lists.newArrayList(
        NodeUtil.createVarNode(Token.VAR, null, "x")));
    rootNode.setLastChild(functionNode);

    Node nameNode = NodeUtil.createNameNode(Token.NAME, null, "x");
    Node assignmentNode = NodeUtil.createAssignmentNode(Token.ASSIGN, null, nameNode, NodeUtil.createNameNode(Token.NAME, null, "x"));
    Node blockNode = NodeUtil.createBlockNode(Token.BLOCK, null, Lists.newArrayList(assignmentNode, NodeUtil.createReturnNode(Token.RETURN, null, NodeUtil.createNameNode(Token.NAME, null, "x"))));
    functionNode.setLastChild(blockNode);

    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, compiler, null, makeDeclaredNamesUnique, SCOPED_CALLBACK);
    nodeTraversal.traverse();

    Scope globalScope = compiler.getGlobalScope();
    List<String> expectedNames = Lists.newArrayList("x_1", "x_2");
    Map<String, Node> actualNames = NodeUtil.getAllLocalVarNames(rootNode);
    assertEquals(expectedNames.size(), actualNames.size());
    Iterator<Map.Entry<String, Node>> actualIterator = actualNames.entrySet().iterator();
    int i = 0;
    String actualName;
    for (String expectedName : expectedNames) {
      actualName = actualIterator.next().getKey();
      if (expectedName.equals("x")) {
        assertTrue(actualName.matches("x_\\d+"));
      } else {
        assertEquals(expectedName, actualName);
      }
      i++;
    }

    // Check that the global names aren't changed.
    Node globalVarNode1 = globalScope.getVar("x");
    assertEquals("x", globalVarNode1.getString());
    Node globalVarNode2 = globalScope.getVar("g");
    assertEquals("g", globalVarNode2.getString());
  }
}