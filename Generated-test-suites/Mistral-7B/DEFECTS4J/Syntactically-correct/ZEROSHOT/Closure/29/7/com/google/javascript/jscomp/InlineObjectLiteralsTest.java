import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @org.junit.Before
  public void setUp() {
    this.compiler = mock(AbstractCompiler.class);
    this.safeNameIdSupplier = mock(Supplier.class);
  }

  @org.junit.Test
  public void testInlineObjectLiteralsSuccess() {
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = new Scope();
    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);

    // Create an object literal assignment
    Node obj = new Node(Token.OBJECT_LITERAL);
    Node key1 = new Node(Token.STRING_LITERAL, "key1");
    Node value1 = new Node(Token.NUMBER_LITERAL, "1");
    Node prop1 = new Node(Token.GET_PROP, obj, key1);
    Node assign1 = new Node(Token.ASSIGN, prop1, value1);
    scope.addVar(NodeUtil.newVarNode("obj", obj));
    scope.addVar(NodeUtil.newVarNode("key1", key1));
    scope.addVar(NodeUtil.newVarNode("value1", value1));
    scope.addVar(NodeUtil.newVarNode("1", IR.intLiteral(1)));
    scope.addVar(NodeUtil.newVarNode("assign1", assign1));
    obj.addChild(key1);
    obj.addChild(value1);
    root.addChild(externs);
    externs.addChild(scope.getRootNode());
    scope.getRootNode().addChild(root);

    // Create an object literal reference
    Node getProp = new Node(Token.GET_PROP, new Node(Token.NAME, "obj"), new Node(Token.STRING_LITERAL, "key1"));
    scope.addVar(NodeUtil.newVarNode("getProp", getProp));
    root.addChild(getProp);

    pass.process(externs, root);

    // Check that the object literal assignment has been replaced by
    // individual variable assignments
    Node objReplaced = scope.getVar("obj").getNode();
    Node key1Replaced = scope.getVar("key1").getNode();
    Node value1Replaced = scope.getVar("value1").getNode();
    Node key1AssignReplaced = scope.getVar("assign1").getNode();
    Node key1ValueReplaced = scope.getVar("1").getNode();

    // The object literal node should have been removed
    assertEquals(0, objReplaced.getChildCount());

    // The getProp node should now be a reference to the key1 variable
    assertEquals(getProp.getChildAtIndex(0).getNode().getString(), key1Replaced.getNodeName());
    assertEquals(getProp.getChildAtIndex(1).getNode().getString(), key1ValueReplaced.getNodeName());

    // The key1Assign node should have been replaced by individual variable assignments
    assertEquals(3, key1AssignReplaced.getChildCount());
    Node assign1Replaced = key1AssignReplaced.getChildAtIndex(0);
    assertEquals(assign1Replaced.getChildAtIndex(0).getNode().getString(), key1Replaced.getNodeName());
    assertEquals(assign1Replaced.getChildAtIndex(1).getNode().getString(), value1Replaced.getNodeName());
    assertEquals(Token.ASSIGN, assign1Replaced.getTokenType());

    // Individual variable assignments should have been added
    List<Node> children = scope.getVar("key1").getNode().getChildren();
    assertEquals(2, children.size());
    Node key1Var = children.get(0);
    Node value1Var = children.get(1);
    assertEquals(Token.VAR, key1Var.getTokenType());
    assertEquals(Token.ASSIGN, value1Var.getTokenType());

    Node objVar = scope.getVar("obj").getNode();
    List<Node> objVarChildren = objVar.getChildren();
    assertEquals(1, objVarChildren.size());
    Node key1VarNode = objVarChildren.get(0);
    assertEquals(key1Var, key1VarNode);

    Node value1VarNode = scope.getVar("value1").getNode();
    assertEquals(value1Var, value1VarNode);

    // Check that the object literal reference has been updated to reference
    // the key1 variable instead
    Node getPropReplaced = scope.getVar("getProp").getNode();
    assertEquals(getPropReplaced.getChildAtIndex(0).getNode().getString(), key1Replaced.getNodeName());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsSelfReference() {
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = new Scope();
    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);

    // Create an object literal self-reference
    Node obj = new Node(Token.OBJECT_LITERAL);
    Node key1 = new Node(Token.STRING_LITERAL, "key1");
    Node value1 = new Node(Token.OBJECT_LITERAL);
    Node prop1 = new Node(Token.GET_PROP, obj, key1);
    Node setProp1 = new Node(Token.SET_PROP, obj, key1, value1);
    Node assign1 = new Node(Token.ASSIGN, prop1, setProp1);
    scope.addVar(NodeUtil.newVarNode("obj", obj));
    scope.addVar(NodeUtil.newVarNode("key1", key1));
    scope.addVar(NodeUtil.newVarNode("value1", value1));
    scope.addVar(NodeUtil.newVarNode("assign1", assign1));
    obj.addChild(key1);
    obj.addChild(value1);
    value1.addChild(key1);
    root.addChild(externs);
    externs.addChild(scope.getRootNode());
    scope.getRootNode().addChild(root);

    pass.process(externs, root);

    // Check that the object literal self-reference hasn't been modified
    Node objReplaced = scope.getVar("obj").getNode();
    assertEquals(1, objReplaced.getChildCount());
    Node value1Replaced = objReplaced.getChildAtIndex(0);
    assertEquals(1, value1Replaced.getChildCount());
    Node key1Replaced = value1Replaced.getChildAtIndex(0);
    assertEquals(2, key1Replaced.getChildCount());
    Node selfRef = key1Replaced.getChildAtIndex(0);
    assertEquals(Node.GET_PROP, selfRef.getTokenType());
    assertEquals(Node.NAME, selfRef.getFirstChild().getTokenType());
    assertEquals("obj", selfRef.getFirstChild().getString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsEmptyObject() {
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = new Scope();
    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);

    // Create an empty object literal assignment
    Node obj = new Node(Token.OBJECT_LITERAL);
    Node assign = new Node(Token.ASSIGN, NodeUtil.newVarNode("obj", obj), obj);
    root.addChild(externs);
    externs.addChild(scope.getRootNode());
    scope.getRootNode().addChild(root);
    scope.addVar(NodeUtil.newVarNode("obj", obj));
    scope.addVar(NodeUtil.newVarNode("assign", assign));

    pass.process(externs, root);

    // Check that the empty object literal assignment hasn't been modified
    Node objReplaced = scope.getVar("obj").getNode();
    assertEquals(0, objReplaced.getChildCount());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsComplexObject() {
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = new Scope();
    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);

    // Create a complex object literal assignment
    Node obj = new Node(Token.OBJECT_LITERAL);
    Node key1 = new Node(Token.STRING_LITERAL, "key1");
    Node value1 = new Node(Token.NUMBER_LITERAL, "1");
    Node key2 = new Node(Token.STRING_LITERAL, "key2");
    Node value2 = new Node(Token.OBJECT_LITERAL);
    Node prop1 = new Node(Token.GET_PROP, obj, key1);
    Node setProp1 = new Node(Token.SET_PROP, obj, key1, value1);
    Node prop2 = new Node(Token.GET_PROP, obj, key2);
    Node setProp2 = new Node(Token.SET_PROP, obj, key2, value2);
    Node assign = new Node(Token.ASSIGN, prop1, setProp1);
    Node assign2 = new Node(Token.ASSIGN, prop2, setProp2);
    scope.addVar(NodeUtil.newVarNode("obj", obj));
    scope.addVar(NodeUtil.newVarNode("key1", key1));
    scope.addVar(NodeUtil.newVarNode("value1", value1));
    scope.addVar(NodeUtil.newVarNode("key2", key2));
    scope.addVar(NodeUtil.newVarNode("value2", value2));
    scope.addVar(NodeUtil.newVarNode("assign", assign));
    scope.addVar(NodeUtil.newVarNode("assign2", assign2));
    obj.addChild(key1);
    obj.addChild(value1);
    value1.addChild(key1);
    obj.addChild(key2);
    obj.addChild(value2);
    value2.addChild(key2);
    root.addChild(externs);
    externs.addChild(scope.getRootNode());
    scope.getRootNode().addChild(root);

    pass.process(externs, root);

    // Check that the object literal assignment has been replaced by
    // individual variable assignments
    Node objReplaced = scope.getVar("obj").getNode();
    assertEquals(4, objReplaced.getChildCount());
    Node key1Replaced = objReplaced.getChildAtIndex(0);
    Node value1Replaced = objReplaced.getChildAtIndex(1);
    Node key2Replaced = objReplaced.getChildAtIndex(2);
    Node value2Replaced = objReplaced.getChildAtIndex(3);

    // The object literal node should have been removed
    assertEquals(0, objReplaced.getChildCount());

    // Individual variable assignments should have been added
    List<Node> key1Children = key1Replaced.getChildren();
    assertEquals(2, key1Children.size());
    Node key1Var = key1Children.get(0);
    Node value1Var = key1Children.get(1);
    assertEquals(Token.VAR, key1Var.getTokenType());
    assertEquals(Token.ASSIGN, value1Var.getTokenType());

    List<Node> value1Children = value1Replaced.getChildren();
    assertEquals(1, value1Children.size());
    Node value1Var2 = value1Children.get(0);
    assertEquals(value1Var, value1Var2);

    List<Node> key2Children = key2Replaced.getChildren();
    assertEquals(1, key2Children.size());
    Node key2Var = key2Children.get(0);
    assertEquals(key2Var, key2Replaced);

    List<Node> value2Children = value2Replaced.getChildren();
    assertEquals(1, value2Children.size());
    Node value2Var = value2Children.get(0);
    assertEquals(value2Var, value2Replaced);

    // Check that the object literal references have been updated to reference
    // the key1 and key2 variables instead
    Node getProp1 = scope.getVar("assign").getNode();
    assertEquals(getProp1.getChildAtIndex(0).getNode().getString(), key1Replaced.getNodeName());
    assertEquals(getProp1.getChildAtIndex(1).getNode().getString(), value1Replaced.getNodeName());

    Node getProp2 = scope.getVar("assign2").getNode();
    assertEquals(getProp2.getChildAtIndex(0).getNode().getString(), key2Replaced.getNodeName());
    assertEquals(getProp2.getChildAtIndex(1).getNode().getString(), value2Replaced.getNodeName());
  }

  @org.junit.Test
  public void testInlineObjectLiteralsNoSelfReference() {
    CompilerPass pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = new Scope();
    Node root = new Node(Token.PROGRAM);
    Node externs = new Node(Token.PROGRAM);

    // Create an object literal assignment with no self-reference
    Node obj = new Node(Token.OBJECT_LITERAL);
    Node key1 = new Node(Token.STRING_LITERAL, "key1");
    Node value1 = new Node(Token.NUMBER_LITERAL, "1");
    Node prop1 = new Node(Token.GET_PROP, obj, key1);
    Node assign1 = new Node(Token.ASSIGN, prop1, value1);
    scope.addVar(NodeUtil.newVarNode("obj", obj));
    scope.addVar(NodeUtil.newVarNode("key1", key1));
    scope.addVar(NodeUtil.newVarNode("value1", value1));
    scope.addVar(NodeUtil.newVarNode("assign1", assign1));
    obj.addChild(key1);
    obj.addChild(value1);
    root.addChild(externs);
    externs.addChild(scope.getRootNode());
    scope.getRootNode().addChild(root);

    pass.process(externs, root);

    // Check that the object literal assignment hasn't been modified
    Node objReplaced = scope.getVar("obj").getNode();
    assertEquals(2, objReplaced.getChildCount());
    Node value1Replaced = objReplaced.getChildAtIndex(0);
    assertEquals(1, value1Replaced.getChildCount());
    Node key1Replaced = value1Replaced.getChildAtIndex(0);
    assertEquals(2, key1Replaced.getChildCount());
    Node selfRef = key1Replaced.getChildAtIndex(0);
    assertEquals(Token.GET_PROP, selfRef.getTokenType());
    assertEquals(Token.NAME, selfRef.getFirstChild().getTokenType());
    assertEquals("obj", selfRef.getFirstChild().getString());
  }
}