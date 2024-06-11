import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

 private static class TestCompiler extends AbstractCompiler {

 @Override
 public void process(Node externs, Node rootNode, CompilerOptions options)
 throws Exception {
 // No-op
 }

 @Override
 public void process(Node ast, Node rootNode, CompilerOptions options)
 throws Exception {
 new CheckGlobalThis(this, CheckLevel.WARNING).process(ast, rootNode);
 }
 }

 @Test
 public void testSafeUsesOfThis() {
 // Test case for safe uses of 'this' within a prototype method
 String sourceCode1 = "function Foo() {}\n" +
 "Foo.prototype.bar = function() {\n" +
 " this.x = 1;\n" +
 "}";

 String expectedDiagnostics1 = "";

 Node ast1 = Parser.parse(sourceCode1, null, null);
 TestCompiler compiler = new TestCompiler();
 NodeTraversal traversal = new NodeTraversal(compiler, ast1);
 compiler.process(ast1, null, null);
 assertEquals(expectedDiagnostics1, compiler.getDiagnostics().toString());

 // Test case for safe uses of 'this' within a constructor
 String sourceCode2 = "function Foo(x) {\n" +
 " this.x = x;\n" +
 "}";

 String expectedDiagnostics2 = "";

 Node ast2 = Parser.parse(sourceCode2, null, null);
 TestCompiler compiler2 = new TestCompiler();
 NodeTraversal traversal2 = new NodeTraversal(compiler2, ast2);
 compiler2.process(ast2, null, null);
 assertEquals(expectedDiagnostics2, compiler2.getDiagnostics().toString());

 // Test case for safe uses of 'this' within a function annotated with '@constructor'
 String sourceCode3 = "function Foo() {\n" +
 " @constructor\n" +
 " this.x = 1;\n" +
 "}";

 String expectedDiagnostics3 = "";

 Node ast3 = Parser.parse(sourceCode3, null, null);
 TestCompiler compiler3 = new TestCompiler();
 NodeTraversal traversal3 = new NodeTraversal(compiler3, ast3);
 compiler3.process(ast3, null, null);
 assertEquals(expectedDiagnostics3, compiler3.getDiagnostics().toString());

 // Test case for safe uses of 'this' within a function where there's no logical place to put a '@this' annotation
 String sourceCode4 = "function Foo(x) {\n" +
 " this.x = x;\n" +
 " function bar() {\n" +
 " this.y = 2;\n" +
 " }\n" +
 " bar();\n" +
 "}";

 String expectedDiagnostics4 = "";

 Node ast4 = Parser.parse(sourceCode4, null, null);
 TestCompiler compiler4 = new TestCompiler();
 NodeTraversal traversal4 = new NodeTraversal(compiler4, ast4);
 compiler4.process(ast4, null, null);
 assertEquals(expectedDiagnostics4, compiler4.getDiagnostics().toString());
 }

 @Test
 public void testUnsafeUsesOfThis() {
 // Test case for unsafe use of 'this' on the left side of an assignment
 String sourceCode5 = "function Foo() {\n" +
 " this.x = 1;\n" +
 " this.x = 2;\n" +
 "}";

 String expectedDiagnostics5 = "dangerous use of the global 'this' object\n";

 Node ast5 = Parser.parse(sourceCode5, null, null);
 TestCompiler compiler5 = new TestCompiler();
 NodeTraversal traversal5 = new NodeTraversal(compiler5, ast5);
 compiler5.process(ast5, null, null);
 assertNotNull(compiler5.getDiagnostics());
 assertEquals(1, compiler5.getDiagnostics().length());
 assertEquals(DiagnosticType.WARNING, compiler5.getDiagnostics()[0].getSeverity());
 assertEquals("JSC_USED_GLOBAL_THIS", compiler5.getDiagnostics()[0].getType().getName());

 // Test case for unsafe use of 'this' with a property access
 String sourceCode6 = "function Foo() {\n" +
 " function bar() {\n" +
 " this.x.y = 1;\n" +
 "}";

 String expectedDiagnostics6 = "dangerous use of the global 'this' object\n";

 Node ast6 = Parser.parse(sourceCode6, null, null);
 TestCompiler compiler6 = new TestCompiler();
 NodeTraversal traversal6 = new NodeTraversal(compiler6, ast6);
 compiler6.process(ast6, null, null);
 assertNotNull(compiler6.getDiagnostics());
 assertEquals(1, compiler6.getDiagnostics().length());
 assertEquals(DiagnosticType.WARNING, compiler6.getDiagnostics()[0].getSeverity());
 assertEquals("JSC_USED_GLOBAL_THIS", compiler6.getDiagnostics()[0].getType().getName());
 }
}