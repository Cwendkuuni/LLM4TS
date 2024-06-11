import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class DevirtualizePrototypeMethodsTest {

 private Compiler compiler;
 private DevirtualizePrototypeMethods devirtualizePrototypeMethods;

 @Before
 public void setUp() {
 compiler = new Compiler();
 compiler.addPass(new DevirtualizePrototypeMethods(compiler));
 devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
 }

 @Test
 public void testPrototypeMethodRewrittenAsStatic() {
 String jsCode = "function A() {}\n" +
 "A.prototype.accumulate = function(value) { this.total += value; return this.total }\n" +
 "var total = new A().accumulate(2);\n";

 Node root = Parser.parse(jsCode, null);
 devirtualizePrototypeMethods.process(root, null);

 String expectedJsCode = "var accumulate = function(self, value) { self.total += value; return self.total }\n" +
 "var total = accumulate(new A(), 2);\n";

 String actualJsCode = getCompiledCode(compiler, root);
 assertThat(actualJsCode).isEqualTo(expectedJsCode);
 }

 @Test
 public void testPrototypeMethodWithArguments() {
 String jsCode = "function A() {}\n" +
 "A.prototype.accumulate = function(value1, value2) { this.total += value1 + value2; return this.total }\n" +
 "var total = new A().accumulate(1, 2);\n";

 Node root = Parser.parse(jsCode, null);
 devirtualizePrototypeMethods.process(root, null);

 String expectedJsCode = "var accumulate = function(self, value1, value2) { self.total += value1 + value2; return self.total }\n" +
 "var total = accumulate(new A(), 1, 2);\n";

 String actualJsCode = getCompiledCode(compiler, root);
 assertThat(actualJsCode).isEqualTo(expectedJsCode);
 }

 @Test
 public void testPrototypeMethodWithVarArgs() {
 String jsCode = "function A() {}\n" +
 "A.prototype.accumulate = function() { var args = arguments; this.total += args.length; return this.total }\n" +
 "var total = new A().accumulate(1, 2, 3, 4);\n";

 Node root = Parser.parse(jsCode, null);
 devirtualizePrototypeMethods.process(root, null);

 String expectedJsCode = "var accumulate = function(self) { var args = arguments; self.total += args.length; return self.total }\n" +
 "var total = accumulate(new A());\n" +
 "total.apply(total, [1, 2, 3, 4]);\n";

 String actualJsCode = getCompiledCode(compiler, root);
 assertThat(actualJsCode).isEqualTo(expectedJsCode);
 }

 @Test
 public void testPrototypeMethodWithSameNameInDifferentModules() {
 String jsCode1 = "function A() {}\n" +
 "A.prototype.accumulate = function() { this.total += 1 }\n";
 String jsCode2 = "function B() {}\n" +
 "B.prototype.accumulate = function() { this.total += 2 }\n";

 Node root1 = Parser.parse(jsCode1, null);
 Node root2 = Parser.parse(jsCode2, null);

 devirtualizePrototypeMethods.process(root1, null);
 devirtualizePrototypeMethods.process(root2, null);

 String expectedJsCode = "var A_accumulate = function(self) { self.total += 1 };\n" +
 "var B_accumulate = function(self) { self.total += 2 }\n" +
 "var total1 = A_accumulate.call(new A());\n" +
 "var total2 = B_accumulate.call(new B());\n";

 String actualJsCode = getCompiledCode(compiler, root1) + "\n" +
 getCompiledCode(compiler, root2);
 assertThat(actualJsCode).isEqualTo(expectedJsCode);
 }

 @Test(expected = RuntimeException.class)
 public void testPrototypeMethodWithMoreThanOneDefinition() {
 String jsCode = "function A() {}\n" +
 "A.prototype.accumulate = function() { this.total += 1 }\n" +
 "A.prototype.accumulate = function() { this.total += 2 }\n";

 Node root = Parser.parse(jsCode, null);
 devirtualizePrototypeMethods.process(root, null);
 }

 @Test(expected = RuntimeException.class)
 public void testPrototypeMethodWithNonFunctionDefinition() {
 String jsCode = "function A() {}\n" +
 "A.prototype.accumulate = 42";

 Node root = Parser.parse(jsCode, null);
 devirtualizePrototypeMethods.process(root, null);
 }

 private String getCompiledCode(Compiler compiler, Node root) {
 StringWriter stringWriter = new StringWriter();
 Printer printer = new Printer(new PrintStream(new StringOutputStream(stringWriter)));
 compiler.printCompiledCode(root, printer, null);
 return stringWriter.toString();
 }
}