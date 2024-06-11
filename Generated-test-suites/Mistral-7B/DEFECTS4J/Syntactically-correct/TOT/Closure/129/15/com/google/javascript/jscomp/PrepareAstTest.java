import static com.google.javascript.jscomp.NodeUtil.isControlStructure;
import static com.google.javascript.jscomp.NodeUtil.isControlStructureCodeBlock;
import static com.google.javascript.jscomp.NodeUtil.isObjectLit;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;

  @org.junit.Before
  public void setup() {
    compiler = new AbstractCompiler();
    options = new CompilerOptions();
  }

  @org.junit.Test
  public void testPrepareAst_NormalizeBlocks() {
    String sourceCode = "function foo() {"
        + "if (true) {"
        + "  var a = 1;"
        + "}"
        + "while (true) {"
        + "  var b = 2;"
        + "}"
        + "}";

    Node root = new IR().parse(sourceCode, null, null);

    PrepareAst prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(null, root);

    Node ifStatement = NodeUtil.findNodeByKind(root, Node.IF);
    Node whileStatement = NodeUtil.findNodeByKind(root, Node.WHILE);

    assertTrue(isControlStructureCodeBlock(ifStatement));
    assertTrue(isControlStructureCodeBlock(whileStatement));
  }

  @org.junit.Test
  public void testPrepareAst_NormalizeNodeTypes() {
    String sourceCode = "function foo() {"
        + "return void 42;"
        + "}";

    Node root = new IR().parse(sourceCode, null, null);

    PrepareAst prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(null, root);

    Node returnStatement = NodeUtil.findNodeByKind(root, Node.RETURN);

    assertFalse(returnStatement.isVoid());
    assertTrue(returnStatement.isExprStatement());
    assertTrue(returnStatement.isResult());
  }

  @org.junit.Test
  public void testPrepareAst_NormalizeObjectLiteralAnnotations() {
    String sourceCode = "function foo() {"
        + "return {"
        + "  a: 1,"
        + "  /** @type {number} */"
        + "  b: 2,"
        + "  c: function() {}"
        + "};"
        + "}";

    Node root = new IR().parse(sourceCode, null, null);

    PrepareAst prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(null, root);

    Node objectLiteral = NodeUtil.findNodeByKind(root, Node.OBJECT_LITERAL);

    Node propertyA = objectLiteral.getFirstChild();
    Node keyA = propertyA.getFirstChild();
    Node valueA = propertyA.getLastChild();

    Node propertyB = objectLiteral.getLastChild();
    Node keyB = propertyB.getFirstChild();
    Node valueB = propertyB.getLastChild();

    assertTrue(valueA.getFirstChild().isNumber());
    assertFalse(valueA.getLastChild().isFunction());

    assertTrue(valueB.isFunction());
    assertTrue(valueB.getFirstChild().isFunction());
  }

  @org.junit.Test
  public void testPrepareAst_PrepareAnnotations() {
    String sourceCode = "function foo(x) {"
        + "/** @this {number} */"
        + "return function(y) {"
        + "  return this.x + y;"
        + "};"
        + "}";

    Node root = new IR().parse(sourceCode, null, null);

    PrepareAst prepareAst = new PrepareAst(compiler, false);
    prepareAst.process(null, root);

    Node functionLiteral = NodeUtil.findNodeByKind(root, Node.FUNCTION);
    Node functionLiteralReturn = functionLiteral.getLastChild();

    assertTrue(functionLiteral.getJSDocInfo().isConstructor());
    assertTrue(functionLiteralReturn.isResult());
    assertTrue(functionLiteralReturn.getJSDocInfo().isMethod());
  }
}