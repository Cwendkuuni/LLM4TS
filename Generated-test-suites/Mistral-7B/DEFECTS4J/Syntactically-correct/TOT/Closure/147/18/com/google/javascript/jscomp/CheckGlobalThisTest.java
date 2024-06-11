import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.js.Parser;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;

 @BeforeClass
 public static void setUpBeforeClass() {
 CompilerOptions options = new CompilerOptions(null);
 options.setWarningLevel(DiagnosticGroup.GLOBAL, CheckLevel.WARNING);
 Parser parser = new Parser(options);
 }

 @Before
 public void setUp() {
 compiler = new AbstractCompiler(null);
 compiler.setOptions(new CompilerOptions(null));
 }

 @Test
 public void testSafeUsage() {
 String code = "function Foo() { this.x = 42; }";
 Node node = parser.parse(code, null, null).getTree();
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(compiler, checker, null);
 traversal.traverse(node, null);
 assertFalse(compiler.hasErrors());
 }

 @Test
 public void testConstructorUsage() {
 String code = "function Foo() { this.constructor = Foo; }";
 Node node = parser.parse(code, null, null).getTree();
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(compiler, checker, null);
 traversal.traverse(node, null);
 assertFalse(compiler.hasErrors());
 }

 @Test
 public void testPrototypeMethodUsage() {
 String code = "Foo.prototype.bar = function() { this.x = 42; }";
 Node node = parser.parse(code, null, null).getTree();
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(compiler, checker, null);
 traversal.traverse(node, null);
 assertFalse(compiler.hasErrors());
 }

 @Test
 public void testThisAnnotationUsage() {
 String code = "/** @this {Foo} */ function Foo() { this.x = 42; }";
 Node node = parser.parse(code, null, null).getTree();
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(compiler, checker, null);
 traversal.traverse(node, null);
 assertFalse(compiler.hasErrors());
 }

 @Test
 public void testGlobalThisWarning() {
 String code = "this.x = 42";
 Node node = parser.parse("function test() {" + code + "}", null, null).getTree();
 NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING), null);
 traversal.traverse(node, null);
 assertFalse(compiler.hasErrors());
 assertTrue(compiler.getDiagnostics().getErrorCount(DiagnosticGroup.GLOBAL, CheckLevel.WARNING) > 0);
 }

 @Test
 public void testGetPropertyWithPrototype() {
 String code = "foo.prototype.x = function() { this.x = 42; }";
 Node node = parser.parse(code, null, null).getTree();
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(compiler, checker, null);
 traversal.traverse(node, null);
 assertFalse(compiler.hasErrors());
 }

 @Test
 public void testGetPropertyWithPrototypeAndThisAnnotation() {
 String code = "/** @this {Foo} */ foo.prototype.x = function() { this.x = 42; }";
 Node node = parser.parse(code, null, null).getTree();
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(compiler, checker, null);
 traversal.traverse(node, null);
 assertFalse(compiler.hasErrors());
 }
}