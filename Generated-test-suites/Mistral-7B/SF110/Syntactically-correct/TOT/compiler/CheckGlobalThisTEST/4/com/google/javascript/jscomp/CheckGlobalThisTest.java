import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.parser.bare.BareParser;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private Compiler compiler;
  private AbstractCompiler abstractCompiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    abstractCompiler = new AbstractCompiler(compiler);
  }

  @Test
  public void testSafeUsage() {
    String source = "function Foo() {}";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testUnsafeAssignment() {
    String source = "var a = this;";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertTrue(abstractCompiler.hasErrors());
    assertEquals(1, abstractCompiler.getErrors().size());
    assertEquals("dangerous use of the global 'this' object", abstractCompiler.getErrors().get(0).getMessage());
  }

  @Test
  public void testUnsafePropertyAccess() {
    String source = "var a = {}; a.this = 5;";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertTrue(abstractCompiler.hasErrors());
    assertEquals(1, abstractCompiler.getErrors().size());
    assertEquals("dangerous use of the global 'this' object", abstractCompiler.getErrors().get(0).getMessage());
  }

  @Test
  public void testSafePrototypeMethod() {
    String source = "function Foo() {} Foo.prototype.bar = function() {};";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testSafeConstructor() {
    String source = "function Foo() {}";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testSafeFunctionWithNoAnnotation() {
    String source = "function Foo() {}";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testSafeFunctionWithThisAnnotation() {
    String source = "@this class Foo { function bar() {} }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testSafeFunctionWithConstructorAnnotation() {
    String source = "@constructor class Foo { function bar() {} }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionLentToPrototype() {
    String source = "function Foo() {} Foo.prototype.bar = function baz() {};";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionWithPropertyAccess() {
    String source = "function Foo() { this.bar = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionWithGetProperty() {
    String source = "function Foo() { getThis().bar = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionWithGetPropertyChain() {
    String source = "function Foo() { getThis().prototype.bar.baz = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionWithLhsOfAssignment() {
    String source = "function Foo() { this = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertTrue(abstractCompiler.hasErrors());
    assertEquals(1, abstractCompiler.getErrors().size());
    assertEquals("dangerous use of the global 'this' object", abstractCompiler.getErrors().get(0).getMessage());
  }

  @Test
  public void testFunctionWithLhsOfAssignmentAndPropertyAccess() {
    String source = "function Foo() { this.bar = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionWithLhsOfAssignmentAndGetProperty() {
    String source = "function Foo() { getThis().bar = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionWithLhsOfAssignmentAndGetPropertyChain() {
    String source = "function Foo() { getThis().prototype.bar.baz = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionWithLhsOfAssignmentAndPrototypePropertyAccess() {
    String source = "function Foo() { this.prototype.bar = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertTrue(abstractCompiler.hasErrors());
    assertEquals(1, abstractCompiler.getErrors().size());
    assertEquals("dangerous use of the global 'this' object", abstractCompiler.getErrors().get(0).getMessage());
  }

  @Test
  public void testFunctionWithLhsOfAssignmentAndPrototypeGetProperty() {
    String source = "function Foo() { getThis().prototype.bar = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionWithLhsOfAssignmentAndPrototypeGetPropertyChain() {
    String source = "function Foo() { getThis().prototype.bar.baz = 5; }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }

  @Test
  public void testFunctionWithLhsOfAssignmentAndPrototypeSetter() {
    String source = "function Foo() { Object.defineProperty(this.prototype, 'bar', { get: function() { return 5; } }); }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertTrue(abstractCompiler.hasErrors());
    assertEquals(1, abstractCompiler.getErrors().size());
    assertEquals("dangerous use of the global 'this' object", abstractCompiler.getErrors().get(0).getMessage());
  }

  @Test
  public void testFunctionWithLhsOfAssignmentAndPrototypeSetterChain() {
    String source = "function Foo() { Object.defineProperty(this.prototype, 'bar', { get: function() { return { baz: 5 }; } }); }";
    Node ast = new BareParser(null).parseCompilationUnit(source, null, null);
    new CheckGlobalThis(abstractCompiler).process(ast, null);
    assertFalse(abstractCompiler.hasErrors());
  }
}