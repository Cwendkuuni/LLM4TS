import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private Scope scope;
  private ReferenceCollectingCallback callback;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    passManager = mock(CompilerPassManager.class);
    scope = mock(Scope.class);
    callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
  }

  @Test
  public void testInlineSimpleObjectLiteral() {
    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    // Object literal Assignment
    Node obj = new Node(Token.OBJECTLIT, null);
    obj.addChild(new Node(Token.PROPERTIES, null));
    obj.getLastChild().addChild(new Node(Token.PROPERTY, null,
        new Node(Token.NAME, "key1"), new Node(Token.NAME, "value1")));
    obj.getLastChild().addChild(new Node(Token.PROPERTIES, null));
    obj.getLastChild().getLastChild().addChild(new Node(Token.PROPERTIES, null));
    obj.getLastChild().getLastChild().getLastChild().addChild(
        new Node(Token.PROPERTY, null,
            new Node(Token.NAME, "key2"), new Node(Token.NAME, "value2")));
    Node assign = new Node(Token.ASSIGN, null, Node.newVarNode("obj"), obj);
    root.addChild(assign);

    when(passManager.getCompiler()).thenReturn(compiler);
    when(passManager.pass(externs, root)).thenReturn(null);

    // Call the pass
    callback.process(externs, root);

    // Check the results
    List<Reference> refs = callback.getReferences(scope.getVar("obj"));
    Preconditions.checkState(refs.size() > 0);
    ReferenceCollection referenceInfo = refs.get(0).getReferenceCollection();

    Map<String, String> varmap = callback.getInliningBehavior().computeVarList(
        scope.getVar("obj"), referenceInfo);
    List<Node> nodes = Lists.newArrayList();

    for (Map.Entry<String, String> entry : varmap.entrySet()) {
      String var = entry.getValue();
      Node varNode = scope.getVarNode(var);
      nodes.add(new Node(Token.ASSIGN, null, varNode,
          NodeUtil.newUndefinedNode(null)));
    }

    Node replacement = new Node(Token.COMMA);
    Node cur = replacement;
    int i;
    for (i = 0; i < nodes.size() - 2; i++) {
      cur.addChildToFront(nodes.get(i));
      Node t = new Node(Token.COMMA);
      cur.addChildToFront(t);
      cur = t;
    }
    cur.addChildToFront(nodes.get(i));
    cur.addChildToFront(nodes.get(i + 1));

    // Check that the object literal was replaced by assignments
    Node object = referenceInfo.getInitializingReference().getAssignedValue();
    Preconditions.checkState(object.getType() == Token.OBJECTLIT);
    Node objectAssign = referenceInfo.getInitializingReference().getParent();
    Preconditions.checkState(objectAssign.getType() == Token.ASSIGN);
    Node objReplace = objectAssign.getParent();
    Preconditions.checkState(objReplace.getType() == Token.EXPR_STATEMENT);

    objReplace.replaceChild(objReplace, replacement);
    assertThat(objReplace.toString()).isEqualTo(nodes.toString());
  }

  @Test
  public void testInlineObjectLiteralWithFunction() {
    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    // Object literal Assignment with a function
    Node obj = new Node(Token.OBJECTLIT, null);
    obj.addChild(new Node(Token.PROPERTIES, null));
    obj.getLastChild().addChild(new Node(Token.PROPERTY, null,
        new Node(Token.NAME, "key1"), new Node(Token.NAME, "value1")));
    obj.getLastChild().addChild(new Node(Token.PROPERTIES, null));
    obj.getLastChild().getLastChild().addChild(new Node(Token.PROPERTIES, null));
    obj.getLastChild().getLastChild().getLastChild().addChild(
        new Node(Token.PROPERTY, null,
            new Node(Token.NAME, "key3"), new Node(Token.FUNCTION, null, null,
                new Node(Token.BLOCK, null))));
    obj.getLastChild().getLastChild().getLastChild().getLastChild().addChild(
        new Node(Token.PROPERTY, null,
            new Node(Token.NAME, "key4"), new Node(Token.NAME, "value4")));
    Node assign = new Node(Token.ASSIGN, null, Node.newVarNode("obj"), obj);
    root.addChild(assign);

    when(passManager.getCompiler()).thenReturn(compiler);
    when(passManager.pass(externs, root)).thenReturn(null);

    // Call the pass
    callback.process(externs, root);

    // Check the results
    List<Reference> refs = callback.getReferences(scope.getVar("obj"));
    Preconditions.checkState(refs.size() > 0);
    ReferenceCollection referenceInfo = refs.get(0).getReferenceCollection();

    Map<String, String> varmap = callback.getInliningBehavior().computeVarList(
        scope.getVar("obj"), referenceInfo);

    // Function is not inlined
    Node function = referenceInfo.getReferences().get(2).getNode();
    Preconditions.checkState(function.getType() == Token.FUNCTION);
    Preconditions.checkState(function.getParent().getType() == Token.GETPROP);
    Node getprop = function.getParent();
    Node replacement = NodeUtil.newFunctionNode(
        Token.FUNCTION, null, null, null, null);
    getprop.replaceChild(function, replacement);

    List<Node> nodes = Lists.newArrayList();
    for (Map.Entry<String, String> entry : varmap.entrySet()) {
      String var = entry.getValue();
      Node varNode = scope.getVarNode(var);
      nodes.add(new Node(Token.ASSIGN, null, varNode,
          NodeUtil.newUndefinedNode(null)));
    }

    Node replacementObj = new Node(Token.OBJECTLIT, null);
    Node cur = replacementObj;
    for (int i = 0; i < nodes.size(); i++) {
      if (i == nodes.size() - 1) {
        cur.addChild(nodes.get(i));
        continue;
      }
      cur.addChild(new Node(Token.COMMA));
      cur = cur.getLastChild();
      cur.addChild(nodes.get(i));
    }

    // Check that the object literal was replaced by assignments
    Node object = referenceInfo.getInitializingReference().getAssignedValue();
    Preconditions.checkState(object.getType() == Token.OBJECTLIT);
    Node objectAssign = referenceInfo.getInitializingReference().getParent();
    Preconditions.checkState(objectAssign.getType() == Token.ASSIGN);
    Node objReplace = objectAssign.getParent();
    Preconditions.checkState(objReplace.getType() == Token.EXPR_STATEMENT);

    objReplace.replaceChild(objReplace, replacementObj);
    assertThat(objReplace.toString()).isEqualTo(nodes.toString());
  }

  @Test
  public void testInlineObjectLiteralWithCycle() {
    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    // Object literal Assignment with a cycle
    Node obj1 = new Node(Token.OBJECTLIT, null);
    obj1.addChild(new Node(Token.PROPERTIES, null));
    obj1.getLastChild().addChild(new Node(Token.PROPERTY, null,
        new Node(Token.NAME, "key1"), new Node(Token.NAME, "value1")));
    obj1.getLastChild().addChild(new Node(Token.PROPERTIES, null));
    obj1.getLastChild().getLastChild().addChild(new Node(Token.PROPERTY, null,
        new Node(Token.NAME, "key2"), new Node(Token.GETPROP, null,
            new Node(Token.NAME, "obj1"))));
    Node assign1 = new Node(Token.ASSIGN, null, Node.newVarNode("obj1"), obj1);
    root.addChild(assign1);

    Node obj2 = new Node(Token.OBJECTLIT, null);
    obj2.addChild(new Node(Token.PROPERTIES, null));
    obj2.getLastChild().addChild(new Node(Token.PROPERTY, null,
        new Node(Token.NAME, "key2"), new Node(Token.NAME, "value2")));
    obj2.getLastChild().addChild(new Node(Token.PROPERTIES, null));
    obj2.getLastChild().getLastChild().addChild(new Node(Token.PROPERTY, null,
        new Node(Token.NAME, "key1"), new Node(Token.GETPROP, null,
            new Node(Token.NAME, "obj2"))));
    Node assign2 = new Node(Token.ASSIGN, null, Node.newVarNode("obj2"), obj2);
    root.addChild(assign2);

    when(passManager.getCompiler()).thenReturn(compiler);
    when(passManager.pass(externs, root)).thenReturn(null);

    // Call the pass
    callback.process(externs, root);

    // Check the results
    List<Reference> refs = callback.getReferences(scope.getVar("obj1"));
    Preconditions.checkState(refs.size() > 0);
    ReferenceCollection referenceInfo1 = refs.get(0).getReferenceCollection();

    List<Reference> refs2 = callback.getReferences(scope.getVar("obj2"));
    Preconditions.checkState(refs2.size() > 0);
    ReferenceCollection referenceInfo2 = refs2.get(0).getReferenceCollection();

    Map<String, String> varmap1 = callback.getInliningBehavior().computeVarList(
        scope.getVar("obj1"), referenceInfo1);
    Map<String, String> varmap2 = callback.getInliningBehavior().computeVarList(
        scope.getVar("obj2"), referenceInfo2);

    // Variables with cycle reference are not inlined
    Preconditions.checkState(varmap1.containsKey("key2") &&
        varmap1.get("key2").equals("obj2_" + safeNameIdSupplier.get()));
    Preconditions.checkState(varmap2.containsKey("key1") &&
        varmap2.get("key1").equals("obj1_" + safeNameIdSupplier.get()));

    // Check that the object literals were not replaced
    Node object1 = referenceInfo1.getInitializingReference().getAssignedValue();
    Node object2 = referenceInfo2.getInitializingReference().getAssignedValue();
    Preconditions.checkState(object1.getType() == Token.OBJECTLIT);
    Preconditions.checkState(object2.getType() == Token.OBJECTLIT);
    Preconditions.checkState(object1.toString().contains("key2: obj2") &&
        object2.toString().contains("key1: obj1"));

  }

  @Test
  public void testInlineObjectLiteralWithExportedVariable() {
    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    // Object literal Assignment with an exported variable
    Node obj = new Node(Token.OBJECTLIT, null);
    obj.addChild(new Node(Token.PROPERTIES, null));
    obj.getLastChild().addChild(new Node(Token.PROPERTY, null,
        new Node(Token.NAME, "key"), new Node(Token.NAME, "value")));
    Node assign = new Node(Token.ASSIGN, null, Node.newVarNode("obj"), obj);
    root.addChild(assign);
    Node exportDecl =
        new Node(Token.EXPORT, null, Node.newNameNode("obj"));
    root.addChild(exportDecl);

    when(passManager.getCompiler()).thenReturn(compiler);
    when(passManager.pass(externs, root)).thenReturn(null);

    // Call the pass
    callback.process(externs, root);

    // Check the results
    List<Reference> refs = callback.getReferences(scope.getVar("obj"));
    Preconditions.checkState(refs.size() > 0);
    ReferenceCollection referenceInfo = refs.get(0).getReferenceCollection();

    Map<String, String> varmap = callback.getInliningBehavior().computeVarList(
        scope.getVar("obj"), referenceInfo);

    // Exported variable is not inlined
    Node exportName = referenceInfo.getReferences().get(1).getNode();
    Preconditions.checkState(exportName.getType() == Token.NAME);
    Preconditions.checkState(exportName.getString().equals("obj"));
    Preconditions.checkState(varmap.isEmpty());

  }
}