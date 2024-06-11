import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Test file for CollapseVariableDeclarations class.
 */
public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;
  private Scope scope;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    scope = mock(Scope.class);
  }

  @org.junit.Test
  public void testCollapseSingleVariableDeclaration() {
    Node root = new Node(Token.PROGRAM);
    Node node = new Node(Token.VAR, "a");
    node.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());
  }

  @org.junit.Test
  public void testCollapseMultipleVariableDeclarations() {
    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, "a");
    Node node2 = new Node(Token.VAR, "b");
    Node node3 = new Node(Token.VAR, "c");
    node1.setParent(root);
    node2.setParent(root);
    node3.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a, b, c", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationAndAssignment() {
    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, "a");
    Node assign1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"));
    assign1.setParent(node1);
    node1.setParent(root);
    Node node2 = new Node(Token.VAR, "b");
    node2.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a = a, b", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());
  }

  @org.junit.Test
  public void testCollapseMultipleVariableDeclarationsAndAssignments() {
    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, "a");
    Node assign1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"));
    assign1.setParent(node1);
    node1.setParent(root);
    Node node2 = new Node(Token.VAR, "b");
    Node assign2 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "b"));
    assign2.setParent(node2);
    node2.setParent(root);
    Node node3 = new Node(Token.VAR, "c");
    node3.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a = a, b = b, c", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationInsideFunction() {
    Node root = new Node(Token.PROGRAM);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    functionNode.setParent(root);
    Node node1 = new Node(Token.VAR, "a");
    Node assign1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"));
    assign1.setParent(node1);
    node1.setParent(functionNode);
    Node node2 = new Node(Token.VAR, "b");
    node2.setParent(functionNode);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    Node function = root.getFirstChild();
    List<Node> children = Lists.newArrayList(function.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.BLOCK, varNode.getTokenType());
    List<Node> functionChildren = Lists.newArrayList(functionNode.getChildren());
    assertEquals(2, functionChildren.size());
    Node collapsedVarNode = functionChildren.get(0);
    assertEquals(Token.VAR, collapsedVarNode.getTokenType());
    assertEquals("a = a, b", collapsedVarNode.getString());
    assertEquals(0, collapsedVarNode.getChildrenSize());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationWithJSDoc() {
    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, "a");
    node1.setJSDocInfo(new JSDocInfo());
    node1.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationWithJSDocAndAssignment() {
    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, "a");
    node1.setJSDocInfo(new JSDocInfo());
    Node assign1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"));
    assign1.setParent(node1);
    node1.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a = a", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationWithJSDocAndAssignmentWithDuplicate() {
    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, "a");
    node1.setJSDocInfo(new JSDocInfo());
    Node assign1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"));
    assign1.setParent(node1);
    Node node2 = new Node(Token.VAR, "a");
    node2.setJSDocInfo(new JSDocInfo());
    node2.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationWithJSDocAndAssignmentWithDuplicateAndCollapse() {
    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, "a");
    node1.setJSDocInfo(new JSDocInfo());
    Node assign1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"));
    assign1.setParent(node1);
    node1.setParent(root);
    Node node2 = new Node(Token.VAR, "a");
    node2.setJSDocInfo(new JSDocInfo());
    node2.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());

    varNode = varNode.getFirstChild();
    assertEquals(Token.ASSIGN, varNode.getTokenType());
    assertEquals(2, varNode.getChildrenSize());
    Node aNode1 = varNode.getFirstChild();
    assertEquals(Token.NAME, aNode1.getTokenType());
    assertEquals("a", aNode1.getString());
    Node aNode2 = varNode.getLastChild();
    assertEquals(Token.NAME, aNode2.getTokenType());
    assertEquals("a", aNode2.getString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationWithJSDocAndAssignmentWithDuplicateAndCollapseWithSuppressWarning() {
    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, "a");
    node1.setJSDocInfo(new JSDocInfo());
    node1.setJSDocInfo(new JSDocInfo()); // duplicate JSDocInfo
    Node assign1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"));
    assign1.setParent(node1);
    node1.setParent(root);
    Node node2 = new Node(Token.VAR, "a");
    node2.setJSDocInfo(new JSDocInfo());
    node2.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());

    varNode = varNode.getFirstChild();
    assertEquals(Token.ASSIGN, varNode.getTokenType());
    assertEquals(2, varNode.getChildrenSize());
    Node aNode1 = varNode.getFirstChild();
    assertEquals(Token.NAME, aNode1.getTokenType());
    assertEquals("a", aNode1.getString());
    Node aNode2 = varNode.getLastChild();
    assertEquals(Token.NAME, aNode2.getTokenType());
    assertEquals("a", aNode2.getString());

    JSDocInfo jsDocInfo = varNode.getJSDocInfo();
    assertEquals(1, jsDocInfo.getSuppressionList().size());
    assertEquals("duplicate", jsDocInfo.getSuppressionList().get(0));
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationWithJSDocAndAssignmentWithDuplicateAndCollapseWithSuppressWarnings() {
    Node root = new Node(Token.PROGRAM);
    Node node1 = new Node(Token.VAR, "a");
    node1.setJSDocInfo(new JSDocInfo());
    node1.setJSDocInfo(new JSDocInfo()); // duplicate JSDocInfo
    Node assign1 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "a"));
    assign1.setParent(node1);
    node1.setParent(root);
    Node node2 = new Node(Token.VAR, "a");
    node2.setJSDocInfo(new JSDocInfo());
    node2.setJSDocInfo(new JSDocInfo()); // duplicate JSDocInfo
    node2.setParent(root);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    List<Node> children = Lists.newArrayList(root.getChildren());
    assertEquals(1, children.size());
    Node varNode = children.get(0);
    assertEquals(Token.VAR, varNode.getTokenType());
    assertEquals("a", varNode.getString());
    assertEquals(0, varNode.getChildrenSize());

    varNode = varNode.getFirstChild();
    assertEquals(Token.ASSIGN, varNode.getTokenType());
    assertEquals(2, varNode.getChildrenSize());
    Node aNode1 = varNode.getFirstChild();
    assertEquals(Token.NAME, aNode1.getTokenType());
    assertEquals("a", aNode1.getString());
    Node aNode2 = varNode.getLastChild();
    assertEquals(Token.NAME, aNode2.getTokenType());
    assertEquals("a", aNode2.getString());

    JSDocInfo jsDocInfo1 = node1.getJSDocInfo();
    assertEquals(1, jsDocInfo1.getSuppressionList().size());
    assertEquals("duplicate", jsDocInfo1.getSuppressionList().get(0));

    JSDocInfo jsDocInfo2 = node2.getJSDocInfo();
    assertEquals(0, jsDocInfo2.getSuppressionList().size());
  }
}