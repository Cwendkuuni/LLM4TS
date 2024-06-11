import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 CompilerOptions options = new CompilerOptions(null);
 CheckerFactory checkerFactory = new CheckerFactory(options);
 checkerFactory.addChecker(new CheckGlobalThis(compiler, CheckLevel.WARNING));
 compiler.addChecker(checkerFactory.getCheckers().get(0));
 }

 @Test
 public void testSafeUsage() {
 String source = "function MyConstructor() {}";
 Node node = new Parser().parse(source, null, null).getTree();
 NodeTraversal traversal = new NodeTraversal(compiler, node);
 traversal.traverse();
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testUnsafeAssignment() {
 String source = "var a = this;";
 Node node = new Parser().parse(source, null, null).getTree();
 NodeTraversal traversal = new NodeTraversal(compiler, node);
 traversal.traverse();
 assertEquals(1, compiler.getDiagnostics().size());
 DiagnosticGroup diagnostics = compiler.getDiagnostics();
 DiagnosticType diagnostic = diagnostics.get(0).getType();
 assertTrue(diagnostic.equals(CheckGlobalThis.GLOBAL_THIS));
 }

 @Test
 public void testUnsafeMethodCall() {
 String source = "function MyClass() { this.method(); }";
 String methodSource = "function MyClass() {} MyClass.prototype.method = function() {};";
 String script = "new MyClass().method(); " + methodSource;
 Node tree = new Parser().parse(script, null, null).getTree();
 NodeTraversal traversal = new NodeTraversal(compiler, tree);
 traversal.traverse();
 assertEquals(1, compiler.getDiagnostics().size());
 DiagnosticType diagnostic = compiler.getDiagnostics().get(0).getType();
 assertTrue(diagnostic.equals(CheckGlobalThis.GLOBAL_THIS));
 }

 @Test
 public void testSafePrototypeMethod() {
 String source = "function MyClass() { this.method = function() {}; }";
 String methodSource = "function MyClass() {} MyClass.prototype.method = function() {};";
 String script = "new MyClass().method(); " + methodSource;
 Node tree = new Parser().parse(script, null, null).getTree();
 NodeTraversal traversal = new NodeTraversal(compiler, tree);
 traversal.traverse();
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testConstructorAnnotation() {
 String source = "function MyConstructor() {}";
 Node node = new Parser().parse(source, null, null).getTree();
 node.setJSDocInfo(new JSDocInfo(new JSDocInfo.Tag("constructor", null)));
 NodeTraversal traversal = new NodeTraversal(compiler, node);
 traversal.traverse();
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testThisAnnotation() {
 String source = "function MyFunction(@this) {}";
 Node node = new Parser().parse(source, null, null).getTree();
 node.setJSDocInfo(new JSDocInfo(new JSDocInfo.Tag("this", null)));
 NodeTraversal traversal = new NodeTraversal(compiler, node);
 traversal.traverse();
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testFunctionWithNoAnnotation() {
 String source = "function MyFunction() {}";
 Node node = new Parser().parse(source, null, null).getTree();
 NodeTraversal traversal = new NodeTraversal(compiler, node);
 traversal.traverse();
 assertTrue(compiler.getDiagnostics().isEmpty());
 }
}