import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Compiler compiler = new Compiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(compiler);

    // Test a simple function that uses 'this' on the left side of an assignment
    String code1 = "function evil() { this = 5; }";
    Node node1 = AbstractCompiler.parseAndCompile(code1, null, null)[0];
    traversal.traverse(node1, checker);
    assertTrue(compiler.getDiagnostics().size() > 0);

    // Test a function that uses 'this' inside an object literal
    String code2 = "var obj = { evil: function() { this = 5; } };";
    Node node2 = AbstractCompiler.parseAndCompile(code2, null, null)[0];
    traversal.traverse(node2, checker);
    assertFalse(compiler.getDiagnostics().size() > 0);

    // Test a function that uses 'this' as a constructor
    String code3 = "function MyConstructor() { this.x = 5; }";
    Node node3 = AbstractCompiler.parseAndCompile(code3, null, null)[0];
    traversal.traverse(node3, checker);
    assertFalse(compiler.getDiagnostics().size() > 0);

    // Test a function that uses 'this' inside a prototype method
    String code4 = "function MyPrototype() {} MyPrototype.prototype.evil = function() { this.x = 5; }";
    Node node4 = AbstractCompiler.parseAndCompile(code4, null, null)[0];
    traversal.traverse(node4, checker);
    assertFalse(compiler.getDiagnostics().size() > 0);

    // Test a function that uses 'this' inside a function with the @this annotation
    String code5 = "/** @this {x: number} */ function MyFunction() { this.x = 5; }";
    Node node5 = AbstractCompiler.parseAndCompile(code5, null, null)[0];
    traversal.traverse(node5, checker);
    assertFalse(compiler.getDiagnostics().size() > 0);

    // Test a function that uses 'this' inside a regular function
    String code6 = "function evil() { this.x = 5; }";
    Node node6 = AbstractCompiler.parseAndCompile(code6, null, null)[0];
    traversal.traverse(node6, checker);
    assertTrue(compiler.getDiagnostics().size() > 0);

    // Test a function that uses 'this' inside a function with no logical place to put a 'this' annotation
    String code7 = "function evil() { return this.x; }";
    Node node7 = AbstractCompiler.parseAndCompile(code7, null, null)[0];
    traversal.traverse(node7, checker);
    assertTrue(compiler.getDiagnostics().size() > 0);
  }
}