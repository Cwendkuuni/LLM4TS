import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.parser.javascript.JSDocInfo;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.WarningLevel.QUIRKS, DiagnosticType.warning);
    compiler = new AbstractCompiler(options);
    checker = new Checker(compiler);
  }

  @Test
  public void testSafeUsesOfThis() {
    String source = "function Foo() { this.x = 1; }";
    Node node = parser.parse(source, null, null);
    checker.process(node, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testPrototypePropertyAccess() {
    String source = "var obj = { foo: function() { this.x = 1; } }; obj.foo.prototype.x = 2;";
    Node node = parser.parse(source, null, null);
    checker.process(node, null);
    assertFalse(compiler.hasError());
  }

  @Test
  public void testUnsafeUseOfThisOnLeftSideOfAssignment() {
    String source = "var x = this;";
    Node node = parser.parse(source, null, null);
    checker.process(node, null);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeUseOfThisOnLeftSideOfPropertyAccess() {
    String source = "var x = { getThis: function() { return this; } }; x.getThis().prototype.x = 1;";
    Node node = parser.parse(source, null, null);
    checker.process(node, null);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionAnnotatedWithAtConstructor() {
    String source = "function Foo() { this.x = 1; }";
    Node node = parser.parse(source, null, null);
    checker.process(node, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionAnnotatedWithAtThis() {
    String source = "@this class Foo { x = 1; }";
    Node node = parser.parse(source, null, null);
    checker.process(node, null);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testUnsafeUseOfThisInFunctionWhereThereIsNoLogicalPlaceToPutAtThisAnnotation() {
    String source = "function Foo() { this.x = 1; } function Bar() { this.x = 1; Foo.call(this); }";
    Node node = parser.parse(source, null, null);
    checker.process(node, null);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage());
  }
}