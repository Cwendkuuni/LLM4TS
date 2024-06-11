import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

  private Compiler compiler;
  private CheckSideEffects checkSideEffects;
  private CompilerPassManager passManager;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    passManager = mock(CompilerPassManager.class);
    when(compiler.getPassManager()).thenReturn(passManager);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @Test
  public void testCheckSideEffects_VoidStatement() {
    Node root = createNode("void statement");
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.hasErrors());
  }

  @Test
  public void testCheckSideEffects_ExpressionResult() {
    Node root = createNode("expression result");
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.hasErrors());
  }

  @Test
  public void testCheckSideEffects_QualifiedNameWithJSDocInfo() {
    Node root = createNode("qualified name with JSDocInfo");
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.hasErrors());
  }

  @Test
  public void testCheckSideEffects_SimpleOperator() {
    Node root = createNode("simple operator");
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.hasErrors());
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffects() {
    Node root = createNode("statement with side effects");
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.hasErrors());
  }

  @Test
  public void testCheckSideEffects_StatementWithSideEffects_Protected() {
    checkSideEffects.protectSideEffects();
    Node root = createNode("statement with side effects (protected)");
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.hasErrors());
  }

  @Test
  public void testProtectSideEffects() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, true);
    checkSideEffects.process(null, createNode("node to protect"));
    checkSideEffects.protectSideEffects();
    Node protectedNode = checkSideEffects.problemNodes.get(0);
    Node expectedNode = createNode("JSCOMPILER_PRESERVE(node to protect)");
    assertTrue(NodeUtil.isCall(protectedNode));
    assertTrue(expectedNode.equals(protectedNode.getLastChild()));
  }

  @Test
  public void testStripProtection() {
    Node protectedNode = createNode("JSCOMPILER_PRESERVE(node to protect)");
    Node root = createNode("node with protected node as a child");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, false);
    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();

    StripProtection stripProtection = new StripProtection(compiler);
    stripProtection.process(null, root);

    ArgumentCaptor<CompilerPass> argumentCaptor = ArgumentCaptor.forClass(CompilerPass.class);
    passManager.addPasses(argumentCaptor.capture());
    List<CompilerPass> passes = argumentCaptor.getValue();
    assertTrue(passes.contains(checkSideEffects.stripProtection));

    assertTrue(NodeUtil.isVoid(protectedNode.getParent()));
    assertTrue(protectedNode.isDetached());
  }

  private Node createNode(String name) {
    Node node = new Node();
    node.setType(Token.BLOCK);
    node.setName(name);
    return node;
  }
}

class CheckSideEffectsTest$CheckSideEffectsStripProtection {
  static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";
}