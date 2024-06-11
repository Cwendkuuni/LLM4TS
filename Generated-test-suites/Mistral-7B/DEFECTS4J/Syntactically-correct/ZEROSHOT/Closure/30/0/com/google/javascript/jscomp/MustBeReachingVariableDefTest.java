import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef() {
    // Given
    String jsSource = "function test() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  var z = x + y;" +
        "  alert(x);" +
        "  alert(z);" +
        "}";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.analyze();

    // When
    Node entryNode = cfg.getEntryNode();
    Node useX = cfg.getNode("test_entry_11");
    Node useZ = cfg.getNode("test_entry_13");

    // Then
    Node expectedXDef = cfg.getNode("test_1_1");
    Node expectedZDef = cfg.getNode("test_1_3");

    assertEquals(expectedXDef, analysis.getDef("x", useX));
    assertEquals(expectedZDef, analysis.getDef("z", useZ));

    assertFalse(analysis.dependsOnOuterScopeVars("x", useX));
    assertFalse(analysis.dependsOnOuterScopeVars("z", useZ));
  }

  @Test
  public void testMustBeReachingVariableDefWithFunctionCall() {
    // Given
    String jsSource = "function foo() {" +
        "  var x = 1;" +
        "  return x;" +
        "}" +
        "function test() {" +
        "  var y = 2;" +
        "  var z = foo() + y;" +
        "  alert(z);" +
        "}" +
        "test();";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.analyze();

    // When
    Node entryNode = cfg.getEntryNode();
    Node useZ = cfg.getNode("test_entry_3");

    // Then
    Node expectedZDef = cfg.getNode("test_1_3");

    assertEquals(expectedZDef, analysis.getDef("z", useZ));
    assertFalse(analysis.dependsOnOuterScopeVars("z", useZ));
  }

  @Test
  public void testMustBeReachingVariableDefWithEscapedVariable() {
    // Given
    String jsSource = "function test() {" +
        "  var x = 1;" +
        "  var y = 2;" +
        "  function foo() {" +
        "    var z = x;" +
        "  }" +
        "  foo();" +
        "  alert(y);" +
        "}" +
        "test();";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.analyze();

    // When
    Node entryNode = cfg.getEntryNode();
    Node useY = cfg.getNode("test_entry_5");

    // Then
    assertNull(analysis.getDef("x", useY));
    assertNull(analysis.getDef("z", useY)); // z is an escaped variable
    assertEquals(jsScope.getRootNode(), analysis.getDef("y", useY));
    assertFalse(analysis.dependsOnOuterScopeVars("y", useY));
  }

  @Test
  public void testMustBeReachingVariableDefWithCatch() {
    // Given
    String jsSource = "function foo() {" +
        "  try {" +
        "    throw new Error('foo');" +
        "  } catch (e) {" +
        "    var x = 1;" +
        "  }" +
        "  alert(x);" +
        "}" +
        "foo();";

    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    analysis.analyze();

    // When
    Node entryNode = cfg.getEntryNode();
    Node useX = cfg.getNode("foo_entry_1");

    // Then
    assertNull(analysis.getDef("x", useX));
  }
}