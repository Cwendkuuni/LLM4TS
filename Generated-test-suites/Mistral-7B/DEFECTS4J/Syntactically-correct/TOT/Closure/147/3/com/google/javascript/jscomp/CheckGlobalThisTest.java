import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  private static class TestCompiler extends AbstractCompiler {
    TestCompiler() {
      super(new Checker(), new NodeTraversal());
    }
  }

  @org.junit.Test
  public void testGlobalThis_Safe() {
    String code = "function Foo() {}";
    TestCompiler compiler = new TestCompiler();
    DiagnosticType diagType = CheckGlobalThis.GLOBAL_THIS;
    CheckLevel level = WARNING;
    int numDiagnostics = compiler.check(code, level).length;
    assertTrue("No diagnostics expected", numDiagnostics == 0);
  }

  @org.junit.Test
  public void testGlobalThis_Unsafe_Assignment() {
    String code = "var x = this;";
    TestCompiler compiler = new TestCompiler();
    DiagnosticType diagType = CheckGlobalThis.GLOBAL_THIS;
    CheckLevel level = WARNING;
    int numDiagnostics = compiler.check(code, level).length;
    assertTrue("Diagnostics expected", numDiagnostics > 0);
  }

  @org.junit.Test
  public void testGlobalThis_Unsafe_PropertyAccess() {
    String code = "var x = {}; x.prop = this;";
    TestCompiler compiler = new TestCompiler();
    DiagnosticType diagType = CheckGlobalThis.GLOBAL_THIS;
    CheckLevel level = WARNING;
    int numDiagnostics = compiler.check(code, level).length;
    assertTrue("Diagnostics expected", numDiagnostics > 0);
  }

  @org.junit.Test
  public void testGlobalThis_Unsafe_Constructor() {
    String code = "function Foo() { this.prop = 1; }";
    TestCompiler compiler = new TestCompiler();
    DiagnosticType diagType = CheckGlobalThis.GLOBAL_THIS;
    CheckLevel level = WARNING;
    int numDiagnostics = compiler.check(code, level).length;
    assertTrue("No diagnostics expected", numDiagnostics == 0);
  }

  @org.junit.Test
  public void testGlobalThis_Safe_PrototypeMethod() {
    String code = "function Foo() {} Foo.prototype.bar = function() { this.prop = 1; }";
    TestCompiler compiler = new TestCompiler();
    DiagnosticType diagType = CheckGlobalThis.GLOBAL_THIS;
    CheckLevel level = WARNING;
    int numDiagnostics = compiler.check(code, level).length;
    assertTrue("No diagnostics expected", numDiagnostics == 0);
  }

  @org.junit.Test
  public void testGlobalThis_Safe_FunctionWithNoAnnotation() {
    String code = "function Foo() { this.prop = 1; }";
    TestCompiler compiler = new TestCompiler();
    DiagnosticType diagType = CheckGlobalThis.GLOBAL_THIS;
    CheckLevel level = WARNING;
    int numDiagnostics = compiler.check(code, level).length;
    assertTrue("No diagnostics expected", numDiagnostics > 0);
  }

  @org.junit.Test
  public void testGlobalThis_Safe_FunctionWithThisAnnotation() {
    String code = "function Foo@this() { this.prop = 1; }";
    TestCompiler compiler = new TestCompiler();
    DiagnosticType diagType = CheckGlobalThis.GLOBAL_THIS;
    CheckLevel level = WARNING;
    int numDiagnostics = compiler.check(code, level).length;
    assertTrue("No diagnostics expected", numDiagnostics == 0);
  }

  @org.junit.Test
  public void testGlobalThis_Safe_FunctionInsideObjectLiteral() {
    String code = "var obj = { foo: function() { this.prop = 1; } }";
    TestCompiler compiler = new TestCompiler();
    DiagnosticType diagType = CheckGlobalThis.GLOBAL_THIS;
    CheckLevel level = WARNING;
    int numDiagnostics = compiler.check(code, level).length;
    assertTrue("No diagnostics expected", numDiagnostics == 0);
  }

  @org.junit.Test
  public void testGlobalThis_Safe_FunctionInsideBlock() {
    String code = "function Foo() {} var x = function() { this.prop = 1; }";
    TestCompiler compiler = new TestCompiler();
    DiagnosticType diagType = CheckGlobalThis.GLOBAL_THIS;
    CheckLevel level = WARNING;
    int numDiagnostics = compiler.check(code, level).length;
    assertTrue("No diagnostics expected", numDiagnostics == 0);
  }
}