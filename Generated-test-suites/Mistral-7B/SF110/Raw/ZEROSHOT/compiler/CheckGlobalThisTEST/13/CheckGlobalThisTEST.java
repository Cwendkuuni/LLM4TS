import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestingLibrary.testCompilation;
import static com.google.javascript.jscomp.TestingLibrary.checkError;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    TestCompiler compiler = new TestCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(compiler.getFactory().getAbstractCompiler());
    compiler.addPhase(checker);

    Node code = testCompilation(compiler,
        "function evil() { this.useful = undefined; }",
        "function good() { this.useful = undefined; }", // constructor
        "function good() { @constructor } function evil() { this.useful = undefined; }",
        "function good() { @this } function evil() { this.useful = undefined; }",
        "function good() { @this } function evil() { var a = this; this.useful = undefined; }",
        "function good() { @this } function evil() { this.a = { use: function() {} }; this.useful = undefined; }",
        "function good() { @this } function evil() { this.useful = this; }",
        "function good() { @interface } function evil() { this.useful = undefined; }",
        "var a = function() { this.useful = undefined; }",
        "var a = function() { @constructor } function evil() { this.useful = undefined; }",
        "var a = function() { @this } function evil() { this.useful = undefined; }",
        "var a = function() { @this } function evil() { var b = this; this.useful = undefined; }",
        "var a = function() { @this } function evil() { this.a = { use: function() {} }; this.useful = undefined; }",
        "var a = function() { @this } function evil() { this.useful = this; }",
        "var a = function() { @interface } function evil() { this.useful = undefined; }"
        );

    checkError(compiler, code, CheckGlobalThis.GLOBAL_THIS.getExternalName());

    Node evil1 = code.getFirstChild().getFirstChild();
    Node evil2 = code.getChild(10).getFirstChild().getFirstChild();
    Node good1 = code.getChild(1).getFirstChild().getFirstChild();
    Node good2 = code.getChild(12).getFirstChild().getFirstChild();
    Node good3 = code.getChild(13).getFirstChild().getFirstChild();
    Node good4 = code.getChild(14).getFirstChild().getFirstChild();
    Node good5 = code.getChild(15).getFirstChild().getFirstChild();
    Node good6 = code.getChild(16).getFirstChild().getFirstChild();
    Node good7 = code.getChild(17).getFirstChild().getFirstChild();

    assertFalse("No error for good case 1", checker.shouldReportThis(good1));
    assertFalse("No error for good case 2 (constructor)", checker.shouldReportThis(good2));
    assertFalse("No error for good case 3 (@constructor)", checker.shouldReportThis(good3));
    assertFalse("No error for good case 4 (@this)", checker.shouldReportThis(good4));
    assertFalse("No error for good case 5 (@this with var)", checker.shouldReportThis(good5));
    assertFalse("No error for good case 6 (@this with object literal)", checker.shouldReportThis(good6));
    assertFalse("No error for good case 7 (@this with prototype)", checker.shouldReportThis(good7));

    assertTrue("Error for evil case 1", checker.shouldReportThis(evil1));
    assertTrue("Error for evil case 2", checker.shouldReportThis(evil2));
  }
}