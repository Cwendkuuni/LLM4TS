import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.SourceFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() throws Exception {
    CompilerOptions options = new CompilerOptions(ParserOptions.standard());
    options.setWarningLevel(CheckLevel.QUIRKS, CheckSideEffects.class);
    compiler = new Compiler();
    compiler.init(options);

    input = new CompilerInput("test.js", new SourceFile("test.js"));
    root = new Node(Token.BLOCK);
  }

  @After
  public void tearDown() throws Exception {
    compiler.dispose();
  }

  @Test
  public void testNoSideEffect() {
    Node varNode = new Node(Token.VAR, new Node(Token.IDENTIFIER, "x"));
    Node assignNode = new Node(Token.ASSIGN, null, varNode, new Node(Token.NUMBER_LITERAL, 5));
    root.addChildToBack(assignNode);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    checkSideEffects.process(input, root);
    NodeUtil.assertNoDiagnostics(compiler);
  }

  @Test
  public void testSideEffect() {
    Node varNode = new Node(Token.VAR, new Node(Token.IDENTIFIER, "x"));
    Node assignNode = new Node(Token.ASSIGN, null, varNode, new Node(Token.NAME, "document.write"));
    Node argNode = new Node(Token.STRING_LITERAL, "Hello, World!");
    assignNode.setJSDocInfo(new JSDocInfoBuilder(true).build(assignNode));
    assignNode.getFirstChild().putJSDocInfo(new JSDocInfoBuilder(true)
        .param("message", argNode).build(argNode));
    root.addChildToBack(assignNode);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeUtil.assertDiagnostics(compiler, CheckSideEffects.USELESS_CODE_ERROR, 1);
  }

  @Test
  public void testProtectedSideEffect() {
    Node varNode = new Node(Token.VAR, new Node(Token.IDENTIFIER, "x"));
    Node protectNode = new Node(Token.CALL, null, new Node(Token.NAME, "JSCOMPILER_PRESERVE"), varNode);
    Node assignNode = new Node(Token.ASSIGN, null, varNode, new Node(Token.NUMBER_LITERAL, 5));
    protectNode.addChildToBack(assignNode);
    root.addChildToBack(protectNode);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    NodeUtil.assertNoDiagnostics(compiler);
  }

  @Test
  public void testProtectedSideEffectRemoval() {
    Node varNode = new Node(Token.VAR, new Node(Token.IDENTIFIER, "x"));
    Node protectNode = new Node(Token.CALL, null, new Node(Token.NAME, "JSCOMPILER_PRESERVE"), varNode);
    Node assignNode = new Node(Token.ASSIGN, null, varNode, new Node(Token.NUMBER_LITERAL, 5));
    protectNode.addChildToBack(assignNode);
    root.addChildToBack(protectNode);

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    // CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    // NodeTraversal.traverse(compiler, root, stripProtection);
    NodeUtil.assertNoDiagnostics(compiler);
  }
}