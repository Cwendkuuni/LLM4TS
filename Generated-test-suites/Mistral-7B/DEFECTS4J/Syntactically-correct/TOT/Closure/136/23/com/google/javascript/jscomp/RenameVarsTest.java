import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameVars;

public class RenameVarsTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testRenameVarsSimple() {
 String sourceCode = "function foo(a, b) {" +
 "var c = a + b;" +
 "return c;" +
 "}";

 CompilerInput input = new CompilerInput(null, null, null, null, sourceCode, null);

 CompilerPass pass = new RenameVars(compiler, "", false, false, false, null, null, null);
 pass.process(compiler.getExterns(), input.getRootNode());

 String expectedOutput = "function x(a, b) {" +
 "var d = a + b;" +
 "return d;" +
 "}";

 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testRenameVarsWithExterns() {
 String sourceCode = "var x = 5;" +
 "function foo(a, b) {" +
 "var c = a + b;" +
 "return c + x;" +
 "}";

 CompilerInput input = new CompilerInput(null, null, null, null, sourceCode, null);

 CompilerPass pass = new RenameVars(compiler, "", false, false, false, null, null, null);
 pass.process(compiler.getExterns(), input.getRootNode());

 String expectedOutput = "var x = 5;" +
 "function foo(a, b) {" +
 "var e = a + b;" +
 "return e + x;" +
 "}";

 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testRenameVarsWithPreserveAnonymousFunctionNames() {
 String sourceCode = "function Foo(a, b) {" +
 "var c = function(d, e) { return a; }";
 "return c;" +
 "}";

 CompilerInput input = new CompilerInput(null, null, null, null, sourceCode, null);

 CompilerPass pass = new RenameVars(compiler, "", false, true, false, null, null, null);
 pass.process(compiler.getExterns(), input.getRootNode());

 String expectedOutput = "function Foo(a, b) {" +
 "var c = function(d, e) { return a; }";
 "return c;" +
 "}";

 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testRenameVarsWithGeneratePseudoNames() {
 String sourceCode = "function foo(a, b) {" +
 "var c = a + b;" +
 "return c;" +
 "}";

 CompilerInput input = new CompilerInput(null, null, null, null, sourceCode, null);

 CompilerPass pass = new RenameVars(compiler, "", false, false, true, null, null, null);
 pass.process(compiler.getExterns(), input.getRootNode());

 String expectedOutput = "function foo(a, b) {" +
 "var $a = a, $b = b, d = $a + $b;" +
 "return d;" +
 "}";

 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testRenameVarsWithLocalRenamingOnly() {
 String sourceCode = "function foo(a, b) {" +
 "var c = a + b;" +
 "return c;" +
 "function bar() {}" +
 "}" +
 "function bar() {}";

 CompilerInput input = new CompilerInput(null, null, null, null, sourceCode, null);

 CompilerPass pass = new RenameVars(compiler, "", true, false, false, null, null, null);
 pass.process(compiler.getExterns(), input.getRootNode());

 String expectedOutput = "function foo(a, b) {" +
 "function x() {}" +
 "var d = a + b;" +
 "return d;" +
 "function x() {}" +
 "}" +
 "function bar() {}";

 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testRenameVarsWithReservedNames() {
 String sourceCode = "function foo() {" +
 "function bar() {}" +
 "var x = 5;" +
 "var y = 10;" +
 "var x = 15;" +
 "}" +
 "function bar() {}";

 CompilerInput input = new CompilerInput(null, null, null, null, sourceCode, null);

 CompilerPass pass = new RenameVars(compiler, "", false, false, false, null, null, Sets.newHashSet("x", "y"));
 pass.process(compiler.getExterns(), input.getRootNode());

 String expectedOutput = "function foo() {" +
 "function bar() {}" +
 "var a = 5;" +
 "var b = 10;" +
 "var a = 15;" +
 "}" +
 "function bar() {}";

 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }
}