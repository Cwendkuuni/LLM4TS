import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.rhino.*;

public class RenamePrototypesTest {

 private static String testCode =
     "function Foo() {}\n" +
     "Foo.prototype.bar = function() {}\n" +
     "var x = new Foo();\n" +
     "Foo.prototype = {\n" +
     "  getBar: function() {}\n" +
     "}\n" +
     "x.bar();\n" +
     "Foo.prototype.bar();";

 @Test
 public void testRenamePrototypes() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new Compiler(options);
   CompilerPass renamer = new RenamePrototypes(compiler, true, null, null);
   CompilerPass normalizer = new AbstractCompiler.Normalizer();

   CompilerResult result = compiler.compile(new Node(testCode), null, renamer, normalizer);
   TokenStream output = result.toSource();

   String expectedCode =
     "function Foo() {}\n" +
     "Foo.prototype.__c_ = function() {}\n" +
     "var x = new Foo();\n" +
     "Foo.prototype = {\n" +
     "  getBar: function() {}\n" +
     "}\n" +
     "x.__c_();\n" +
     "Foo.prototype.__c_();";

   String actualCode = output.asString();
   assertEquals(expectedCode, actualCode);
 }

 @Test(expected = AssertionError.class)
 public void testAgreesWithExistingNames() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new Compiler(options);
   CompilerPass renamer = new RenamePrototypes(compiler, true, null, null);
   CompilerPass normalizer = new AbstractCompiler.Normalizer();

   String testCode =
     "function Foo() {}\n" +
     "Foo.prototype.toString = function() {};\n";

   CompilerResult result = compiler.compile(new Node(testCode), null, renamer, normalizer);
   TokenStream output = result.toSource();

   // The test should fail, as "toString" is a reserved name.
 }

 @Test(expected = AssertionError.class)
 public void testRenamesImpossibleNames() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new Compiler(options);
   CompilerPass renamer = new RenamePrototypes(compiler, true, null, null);
   CompilerPass normalizer = new AbstractCompiler.Normalizer();

   String testCode =
     "function Foo() {}\n" +
     "Foo.prototype.constructor = function() {};\n";

   // "constructor" is a reserved name and cannot be renamed.
 }

 @Test
 public void testRenamesAggressively() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new Compiler(options);
   CompilerPass renamer = new RenamePrototypes(compiler, true, null, null);
   CompilerPass normalizer = new AbstractCompiler.Normalizer();

   String testCode =
     "function Foo() {}\n" +
     "Foo.prototype.myMethod = function() {};\n";

   CompilerResult result = compiler.compile(new Node(testCode), null, renamer, normalizer);
   TokenStream output = result.toSource();

   String expectedCode =
     "function Foo() {}\n" +
     "Foo.prototype.__c_ = function() {}\n";

   String actualCode = output.asString();
   assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testSkipsPrivateProperties() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new Compiler(options);
   CompilerPass renamer = new RenamePrototypes(compiler, true, null, null);
   CompilerPass normalizer = new AbstractCompiler.Normalizer();

   String testCode =
     "function Foo() {}\n" +
     "Foo.prototype.__privateMethod = function() {};\n";

   CompilerResult result = compiler.compile(new Node(testCode), null, renamer, normalizer);
   TokenStream output = result.toSource();

   String expectedCode =
     "function Foo() {}\n" +
     "Foo.prototype.__privateMethod = function() {}\n";

   String actualCode = output.asString();
   assertEquals(expectedCode, actualCode);
 }
}