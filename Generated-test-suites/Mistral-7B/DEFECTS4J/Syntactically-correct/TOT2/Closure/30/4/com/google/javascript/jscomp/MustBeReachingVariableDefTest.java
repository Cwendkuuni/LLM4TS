import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

 private static final String TEST_CODE =
     "function foo() {" +
     "  var a = 1;" +
     "  var b = 2;" +
     "  if (true) {" +
     "    var a = 3;" +
     "  } else {" +
     "    var a = 4;" +
     "  }" +
     "  return a;" +
     "}" +
     "function main() {" +
     "  assertEq(foo(), 1);" +
     "}";

 @Test
 public void testMustBeReachingVariableDef() {
   Scope jsScope = new Scope();
   AbstractCompiler compiler = new AbstractCompiler();
   ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, compiler);
   MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

   Node root = AbstractCompiler.parseCompilationUnit(TEST_CODE, null);
   analyzer.compute(root);

   Node useNode = NodeUtil.findReturnNode(root);
   assertEquals("1", analyzer.getDef("a", useNode).toString());

   // Test for escaped variables
   String escapedCode =
       "function foo() {" +
       "  var x = 1;" +
       "  return function(x) {" +
       "  }(10);" +
       "}" +
       "function main() {" +
       "  assertEq(foo().toString(), 'function(x){}');" +
       "}";
   analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
   root = AbstractCompiler.parseCompilationUnit(escapedCode, null);
   analyzer.compute(root);
   useNode = NodeUtil.findReturnNode(root);
   assertNull(analyzer.getDef("x", useNode));

   // Test for local variables that are not defined
   String notDefinedCode = "function foo() {} function main() { assertEq(foo().a, undefined); }";
   analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
   root = AbstractCompiler.parseCompilationUnit(notDefinedCode, null);
   analyzer.compute(root);
   useNode = NodeUtil.findReturnNode(root);
   assertNull(analyzer.getDef("a", useNode));

   // Test for dependencies on outer scope variables
   String outerScopeCode =
       "function foo() {" +
       "  var a = 1;" +
       "  return function(a) {" +
       "    var b = 2;" +
       "    return a + b;" +
       "  }(2);" +
       "}" +
       "function main() {" +
       "  var x = 1;" +
       "  assertEq(foo(), 3);" +
       "}";
   analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
   root = AbstractCompiler.parseCompilationUnit(outerScopeCode, null);
   analyzer.compute(root);
   useNode = NodeUtil.findReturnNode(root);
   assertTrue(analyzer.dependsOnOuterScopeVars("x", useNode));
 }

}