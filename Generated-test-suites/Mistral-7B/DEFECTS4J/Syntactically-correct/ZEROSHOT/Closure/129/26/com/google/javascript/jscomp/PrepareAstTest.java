import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

public class PrepareAstTest {

 @Test
 public void testPrepareAst() {
 AbstractCompiler compiler = new AbstractCompiler();
 CompilerOptions options = new CompilerOptions();
 PrepareAst prepareAst = new PrepareAst(compiler, false);

 // Prepare an AST with a function call
 Node root = IR.funcCall(IR.name("testFunction"), IR.list());
 Node testFunction = IR.function("testFunction", IR.list(), IR.list(), IR.block());
 testFunction.setBody(IR.returnStmt(IR.literal(1)));
 root.addChildToParent(testFunction);

 // Run the prepare pass
 prepareAst.process(null, root);

 // Check that the free call flag is set correctly
 assertEquals(true, root.getBooleanProp(Node.FREE_CALL));

 // Prepare an AST with a direct eval call
 root = IR.funcCall(IR.name("eval"), IR.literal("1 + 1"));
 prepareAst.process(null, root);

 // Check that the direct eval flag is set correctly
 assertEquals(true, root.getFirstChild().getBooleanProp(Node.DIRECT_EVAL));

 // Prepare an AST with an object literal
 root = IR.objectLit();
 root.addChild(IR.name("a"));
 root.getChild(0).addChild(IR.string("valueA"));
 root.addChild(IR.name("b"));
 root.getChild(1).addChild(IR.number(2));

 // Run the prepare pass
 prepareAst.process(null, root);

 // Check that JSDoc is propagated to the values
 Node a = root.getChild(0);
 assertEquals("valueA", a.getJSDocInfo().getDescription().getCommentText());
 Node b = root.getChild(1);
 assertEquals(null, b.getJSDocInfo());
 }

 @Test
 public void testPrepareAstCheckOnly() {
 AbstractCompiler compiler = new AbstractCompiler();
 CompilerOptions options = new CompilerOptions();
 PrepareAst prepareAst = new PrepareAst(compiler, true);

 // Prepare an AST with a function call
 Node root = IR.funcCall(IR.name("testFunction"), IR.list());
 Node testFunction = IR.function("testFunction", IR.list(), IR.list(), IR.block());
 testFunction.setBody(IR.returnStmt(IR.literal(1)));
 root.addChildToParent(testFunction);

 // Run the prepare pass in check-only mode
 prepareAst.process(null, root);

 // Check that an exception is thrown due to normalizeNodeType constraints
 // being violated
 // (This test relies on the Preconditions.checkState(false,...) call in the
 // PrepareAst class)
 // Uncomment the following line to see the exception message
 // System.out.println(root.toStringTree());
 }
}