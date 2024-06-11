import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UnreachableCodeEliminationTest {

  private CompilerOptions options;
  private AbstractCompiler compiler;
  private CompilerPassManager passManager;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setLanguageIn(LanguageMode.ECMASCRIPT6);
    compiler = mock(AbstractCompiler.class);
    passManager = mock(CompilerPassManager.class);
    when(compiler.getOptions()).thenReturn(options);
    when(compiler.getPassManager()).thenReturn(passManager);
  }

  @Test
  public void testUnreachableCodeElimination() {
    Node externs = createNode("return 1;"); // Unreachable return statement
    Node toplevel = createNode("if (true) { return; } alert('unreachable');"); // Unreachable alert statement

    // UnreachableCodeElimination should remove both return statements and the alert statement
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);
    assertEquals("", toplevel.toString());
  }

  @Test
  public void testNoOpStatements() {
    Node externs = createNode("foo.bar.MyClass.prototype.propertyName;"); // No-op statement
    Node toplevel = createNode("if (true) { true; }"); // No-op statement

    // UnreachableCodeElimination should not remove no-op statements if removeNoOpStatements is false
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(externs, toplevel);
    assertEquals(externs.toString(), "foo.bar.MyClass.prototype.propertyName;");
    assertEquals(toplevel.toString(), "if (true) { true; }");
  }

  @Test
  public void testDeadCodeWithSideEffects() {
    Node externs = createNode("var x = 0; return x++;"); // Dead statement with side effects
    Node toplevel = createNode("if (true) { return; } x++;"); // Dead statement with side effects

    // UnreachableCodeElimination should not remove dead statements with side effects
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);
    assertEquals(externs.toString(), "var x = 0;");
    assertEquals(toplevel.toString(), "if (true) { return; }");
  }

  @Test
  public void testDeadCodeWithFunctionCall() {
    Node externs = createNode("function foo() { return; } foo();"); // Dead statement with function call
    Node toplevel = createNode("if (true) { return; } foo();"); // Dead statement with function call

    // UnreachableCodeElimination should not remove dead statements with function calls
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);
    assertEquals(externs.toString(), "function foo() { return; }");
    assertEquals(toplevel.toString(), "if (true) { return; }");
  }

  @Test
  public void testDeadCodeWithBreak() {
    Node externs = createNode("for (var i = 0; i < 10; i++) { if (i === 5) break; }"); // Dead break statement
    Node toplevel = createNode("if (true) { for (var i = 0; i < 10; i++) { if (i === 5) break; } }"); // Dead break statement

    // UnreachableCodeElimination should not remove dead break statements
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);
    assertEquals(externs.toString(), "for (var i = 0; i < 10; i++) { if (i === 5) break; }");
    assertEquals(toplevel.toString(), "if (true) { for (var i = 0; i < 10; i++) { if (i === 5) break; } }");
  }

  @Test(expected = AssertionError.class)
  public void testCompilationError() {
    Node externs = createNode("// Compilation error: Unexpected token(1:1) : '('"); // Compilation error
    Node toplevel = createNode(""); // Empty node

    // UnreachableCodeElimination should not compile code with errors
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);
  }

  private Node createNode(String code) {
    Node jsNode = new Node(code);
    Node root = new Node(Token.BLOCK);
    root.appendChild(jsNode);
    return root;
  }
}