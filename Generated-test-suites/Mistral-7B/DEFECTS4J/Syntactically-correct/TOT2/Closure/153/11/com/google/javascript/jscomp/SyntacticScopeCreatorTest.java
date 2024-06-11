import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    AbstractCompiler compiler = new NoOpCompiler(); // Replace with real compiler
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);

    Node node = Node.newNode("function foo() {}");
    Scope parent = null;
    Scope scope = sc.createScope(node, parent);

    assertNotNull(scope);
  }

  @Test
  public void testCreateScopeWithFunctionName() {
    AbstractCompiler compiler = new NoOpCompiler();
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);

    Node node = Node.newNode("function foo() {}", "foo");
    Scope parent = null;
    Scope scope = sc.createScope(node, parent);

    assertNotNull(scope);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeWithNullParent() {
    AbstractCompiler compiler = new NoOpCompiler();
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);

    Node node = Node.newNode("function foo() {}", null);
    Scope scope = sc.createScope(node, null);
  }

  @Test
  public void testCreateScopeWithFunctionExpression() {
    AbstractCompiler compiler = new NoOpCompiler();
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);

    Node node = Node.newNode("function foo() {}", null, Token.FUNCTION);
    Scope parent = null;
    Scope scope = sc.createScope(node, parent);

    assertNotNull(scope);
  }

  // Add test cases for scanRoot, scanVars, declareVar, and redeclarationHandler methods
}