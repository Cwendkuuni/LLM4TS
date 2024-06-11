import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    CheckLevel level = WARNING;
    TestSourceFile source = new TestSourceFile(
        "CheckGlobalThisTest.js",
        "function safe() {}\n" +
        "function safeConstructor() {}\n" +
        "function safePrototypeMethod() {\n" +
        "  this.x = 0;\n" +
        "}\n" +
        "function safeThisAnnotatedFunction() {\n" +
        "  @this\n" +
        "  this.x = 0;\n" +
        "}"
    );

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public NodeTraversal createTraversal(Node rootNode, CheckLevel level) {
        return new NodeTraversal(rootNode, new CheckGlobalThis(this, level));
      }
    };

    Node root = source.getAST();
    NodeTraversal traversal = compiler.createTraversal(root, level);
    traversal.traverse(root);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testUnsafeUsesOfThis() {
    CheckLevel level = WARNING;
    TestSourceFile source = new TestSourceFile(
        "CheckGlobalThisTest.js",
        "function unsafe() {\n" +
        "  this.x = 0;\n" +
        "}\n" +
        "function unsafePrototypeMethod() {\n" +
        "  this = {}; // assigning this to an object\n" +
        "  this.x = 0;\n" +
        "}"
    );

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public NodeTraversal createTraversal(Node rootNode, CheckLevel level) {
        return new NodeTraversal(rootNode, new CheckGlobalThis(this, level));
      }
    };

    Node root = source.getAST();
    NodeTraversal traversal = compiler.createTraversal(root, level);
    traversal.traverse(root);
    assertEquals(2, compiler.getDiagnostics().size());

    DiagnosticInfo diagnostic = compiler.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType().getName());
    assertEquals(1, diagnostic.getStartPosition().getLineNumber());
    assertEquals(1, diagnostic.getStartPosition().getColumnNumber());

    diagnostic = compiler.getDiagnostics().get(1);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType().getName());
    assertEquals(3, diagnostic.getStartPosition().getLineNumber());
    assertEquals(17, diagnostic.getStartPosition().getColumnNumber());
  }
}