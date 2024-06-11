import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsage() {
    String jsCode = "function Foo() {}";
    Checker checker = new Checker();
    AbstractCompiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(checker));
    Node rootNode = compiler.compile(jsCode, null, null).ast;
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testConstructor() {
    String jsCode = "function Foo() { this.x = 42; }";
    Checker checker = new Checker();
    AbstractCompiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(checker));
    Node rootNode = compiler.compile(jsCode, null, null).ast;
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testPrototypeMethod() {
    String jsCode = "function Foo() {} Foo.prototype.x = function() { this.y = 42; }";
    Checker checker = new Checker();
    AbstractCompiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(checker));
    Node rootNode = compiler.compile(jsCode, null, null).ast;
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testFunctionWithAnnotation() {
    String jsCode = "/** @this {Object} */ function Foo() { this.x = 42; }";
    Checker checker = new Checker();
    AbstractCompiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(checker));
    Node rootNode = compiler.compile(jsCode, null, null).ast;
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testAssignmentToVariable() {
    String jsCode = "var a = function() { this.x = 42; }";
    Checker checker = new Checker();
    AbstractCompiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(checker));
    Node rootNode = compiler.compile(jsCode, null, null).ast;
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testUnsafeUsageLeftSideAssignment() {
    String jsCode = "var a = {}; a = this;";
    Checker checker = new Checker();
    AbstractCompiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(checker));
    Node rootNode = compiler.compile(jsCode, null, null).ast;
    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals(diagnosticType, checker.getDiagnostics().get(0).getType());
  }

  @Test
  public void testUnsafeUsageLeftSidePropertyAccess() {
    String jsCode = "var a = {}; a.x = this.y;";
    Checker checker = new Checker();
    AbstractCompiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(checker));
    Node rootNode = compiler.compile(jsCode, null, null).ast;
    DiagnosticType diagnosticType = DiagnosticType.warning("JSC_USED_GLOBAL_THIS");
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals(diagnosticType, checker.getDiagnostics().get(0).getType());
  }

  @Test
  public void testUnsafeUsageRightSideAssignment() {
    String jsCode = "var a = {}; this.x = a;";
    Checker checker = new Checker();
    AbstractCompiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(checker));
    Node rootNode = compiler.compile(jsCode, null, null).ast;
    assertEquals(0, checker.getDiagnostics().size());
  }

  @Test
  public void testUnsafeUsageRightSideAssignmentPrototype() {
    String jsCode = "var a = {}; Foo.prototype.x = this;";
    Checker checker = new Checker();
    AbstractCompiler compiler = new Compiler();
    compiler.addPass(new CheckGlobalThis(checker));
    Node rootNode = compiler.compile(jsCode, null, null).ast;
    assertEquals(0, checker.getDiagnostics().size());
  }
}