import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;

public class PrepareAstTest {

  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler(new CompilerOptions());
  }

  @org.junit.Test
  public void testProcess_ExternsAndRoot_NormalizesNodeTypes() {
    Node externs = IR.objectLit().setName("externs").setJSDocInfo(
        new JSDocInfo("externs")).addChild(IR.propertySetter("x", IR.stringLiteral("")));
    Node root = IR.name("root");

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(externs, root);

    NodeTraversal.traverse(compiler, root, new NodeVisitor() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return n.isBlock();
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        assertNotNull(n.getFirstChild());
      }
    });
  }

  @org.junit.Test
  public void testProcess_Root_AddsAnnotations() {
    Node root = IR.name("root").setJSDocInfo(new JSDocInfo("root"));

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    assertEquals("root", root.getJSDocInfo().getName());
  }

  @org.junit.Test
  public void testProcess_Root_NormalizesBlocks() {
    Node root = IR.if_(IR.boolLiteral(true), IR.name("a"), IR.name("b"));

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    Node blockA = root.getChild(0);
    Node blockB = root.getChild(1);

    assertEquals(1, blockA.getNumChildren());
    assertEquals(0, blockB.getNumChildren());
  }

  @org.junit.Test
  public void testProcess_RootAndExterns_AddsAnnotations() {
    Node externs = IR.objectLit().setName("externs");
    Node root = IR.name("root").setJSDocInfo(new JSDocInfo("root"));

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(externs, root);

    assertEquals("root", root.getJSDocInfo().getName());
  }

  @org.junit.Test
  public void testProcess_RootAndExterns_NormalizesBlocks() {
    Node externs = IR.objectLit().setName("externs");
    Node root = IR.if_(IR.boolLiteral(true), IR.name("a"), IR.name("b"));

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(externs, root);

    Node blockA = root.getChild(0);
    Node blockB = root.getChild(1);

    assertEquals(1, blockA.getNumChildren());
    assertEquals(0, blockB.getNumChildren());
  }

  @org.junit.Test
  public void testProcess_RootAndExterns_AddsDispatcherAnnotation() {
    Node externs = IR.objectLit().setName("externs")
        .addChild(IR.propertySetter("dispatch", IR.function(
            IR.name("dispatch"), null,
            IR.list(IR.parameter("context", IR.anyType()),
                IR.parameter("methodName", IR.stringLiteral("")),
                IR.parameter("args", IR.arrayType(IR.anyType())))))
        );
    Node root = IR.name("root");

    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(externs, root);

    Node dispatch = externs.getChild(0).getLastChild();
    assertTrue(dispatch.isFunction());
    assertTrue(dispatch.getBooleanProp(Node.IS_DISPATCHER));
  }

  private static class NodeVisitor implements NodeTraversal.Callback {
    @Override
    public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
      return true;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      // Custom logic here
    }
  }
}