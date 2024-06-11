import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope() {
    // given
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(null, null);

    // when
    Scope scope = sc.createScope(new Node(), parent);

    // then
    assertNotNull(scope);
  }

  @Test
  public void testCreateScopeWithFunction() {
    // given
    AbstractCompiler compiler = new AbstractCompiler() {};
    SyntacticScopeCreator sc = new SyntacticScopeCreator(compiler);
    Scope parent = new Scope(null, null);
    Node functionNode = new Node(); // a mock function node

    // when
    Scope scope = sc.createScope(functionNode, parent);

    // then
    assertTrue(scope.getName().equals("function"));
  }

  // Add tests for each method and edge cases here
}