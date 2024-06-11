import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private CheckerFactory checkerFactory;

 @Before
 public void setUp() throws Exception {
 compiler = new Compiler();
 checkerFactory = new CheckerFactory(compiler);
 }

 @Test
 public void testNoGlobalThis() throws Exception {
 String code = "function foo() {}";
 Node tree = Parser.parse(code, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(checkerFactory.getCheckers().get(CheckerNames.CHECK_GLOBAL_THIS));
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testGlobalThisInFunction() throws Exception {
 String code = "function foo() { this.x = 1; }";
 Node tree = Parser.parse(code, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(checkerFactory.getCheckers().get(CheckerNames.CHECK_GLOBAL_THIS));
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertTrue(checker.hasErrors());
 }

 @Test
 public void testGlobalThisInPrototypeProperty() throws Exception {
 String code = "var obj = { foo: function() { this.x = 1; } };";
 Node tree = Parser.parse(code, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(checkerFactory.getCheckers().get(CheckerNames.CHECK_GLOBAL_THIS));
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertTrue(checker.hasErrors());
 }

 @Test
 public void testGlobalThisInLocalVariable() throws Exception {
 String code = "function foo() { var self = this; }";
 Node tree = Parser.parse(code, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(checkerFactory.getCheckers().get(CheckerNames.CHECK_GLOBAL_THIS));
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testGlobalThisInAssignment() throws Exception {
 String code = "var x = this;";
 Node tree = Parser.parse(code, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(checkerFactory.getCheckers().get(CheckerNames.CHECK_GLOBAL_THIS));
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testGlobalThisInPrototypeMethod() throws Exception {
 String code = "var obj = { __proto__: {constructor: function() { this.x = 1; }}};";
 Node tree = Parser.parse(code, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(checkerFactory.getCheckers().get(CheckerNames.CHECK_GLOBAL_THIS));
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testGlobalThisInFunctionAnnotation() throws Exception {
 String code = "/** @constructor */ function Foo() { this.x = 1; }";
 Node tree = Parser.parse(code, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(checkerFactory.getCheckers().get(CheckerNames.CHECK_GLOBAL_THIS));
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testGlobalThisInFunctionWithThisAnnotation() throws Exception {
 String code = "function Foo() { /** @this {x: number} */ this.x = 1; }";
 Node tree = Parser.parse(code, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(checkerFactory.getCheckers().get(CheckerNames.CHECK_GLOBAL_THIS));
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertFalse(checker.hasErrors());
 }

 @Test
 public void testGlobalThisInFunctionWithNoPlaceToPutThisAnnotation() throws Exception {
 String code = "function foo(x) { this.x = x; }";
 Node tree = Parser.parse(code, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(checkerFactory.getCheckers().get(CheckerNames.CHECK_GLOBAL_THIS));
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertTrue(checker.hasErrors());
 }
}