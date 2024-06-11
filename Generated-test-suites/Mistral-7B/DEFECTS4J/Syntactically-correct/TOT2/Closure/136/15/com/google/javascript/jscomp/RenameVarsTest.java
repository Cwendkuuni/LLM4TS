import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class RenameVarsTest {

 private static final String TEST_CODE = "function foo(a, b) {" +
         "var c = 1;" +
         "var d = function(e, f) {" +
         "return a;" +
         "}" +
         "return c;" +
         "}";

 @Test
 public void testRenameVars() {
 RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.initialize();
 CompilerInput input = new CompilerInput(TEST_CODE, null);
 Node externs = Nodeops.parse(TEST_CODE);
 Node root = Nodeops.parseTopLevel(TEST_CODE, null);

 renamer.process(externs, root);
 String result = compiler.getJSWriter().getJs();

 String expectedResult = "function foo(a, b) {" +
         "var aa = 1;" +
         "var g = function(e, f) {" +
         "return a;" +
         "}" +
         "return aa;" +
         "}";

 assertEquals(expectedResult, result);
 }

 @Test(expected = AssertionError.class)
 public void testRenameReservedWords() {
 RenameVars renamer = new RenameVars(null, null, false, false, false, null, null,
         Arrays.asList("break", "case", "catch", "class", "const", "continue",
                 "debugger", "default", "delete", "do", "else", "export",
                 "extends", "finally", "for", "function", "if", "import",
                 "in", "instanceof", "new", "return", "super", "switch",
                 "this", "throw", "try", "typeof", "var", "void", "with",
                 "while", "yield", "abstract", "boolean", "byte", "char",
                 "class", "double", "enum", "float", "int", "long", "native",
                 "package", "private", "protected", "public", "short", "static",
                 "synchronized", "transient", "volatile", "goto"));
 }

 @Test
 public void testRenameAnonymousFunction() {
 RenameVars renamer = new RenameVars(null, null, false, true, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.initialize();
 CompilerInput input = new CompilerInput(TEST_CODE, null);
 Node externs = Nodeops.parse(TEST_CODE);
 Node root = Nodeops.parseTopLevel(TEST_CODE, null);

 renamer.process(externs, root);
 String result = compiler.getJSWriter().getJs();

 String expectedResult = "function foo(a, b) {" +
         "var c = function g(e, f) {" +
         "return a;" +
         "}" +
         "var aa = 1;" +
         "return aa;" +
         "}";

 assertEquals(expectedResult, result);
 }

 @Test
 public void testRenameLocalVariables() {
 RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.initialize();
 CompilerInput input = new CompilerInput(TEST_CODE, null);
 Node externs = Nodeops.parse(TEST_CODE);
 Node root = Nodeops.parseTopLevel(TEST_CODE, null);

 renamer.process(externs, root);
 String result = compiler.getJSWriter().getJs();

 String expectedResult = "function foo(a, b) {" +
         "var aa = 1;" +
         "var cc = function(e, f) {" +
         "return a;" +
         "}" +
         "return aa;" +
         "}";

 assertEquals(expectedResult, result);
 }

 @Test
 public void testRenameGlobalVariables() {
 RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.initialize();
 CompilerInput input = new CompilerInput("var a = 1;", null);
 Node externs = Nodeops.parse("var a = 1;");
 Node root = Nodeops.parseTopLevel("var a = 1;", null);

 renamer.process(externs, root);
 String result = compiler.getJSWriter().getJs();

 String expectedResult = "var aa = 1;";

 assertEquals(expectedResult, result);
 }

 @Test
 public void testRenameGlobalVariablesWithPrefix() {
 RenameVars renamer = new RenameVars(null, "prefix_", false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.initialize();
 CompilerInput input = new CompilerInput("var a = 1;", null);
 Node externs = Nodeops.parse("var a = 1;");
 Node root = Nodeops.parseTopLevel("var a = 1;", null);

 renamer.process(externs, root);
 String result = compiler.getJSWriter().getJs();

 String expectedResult = "var prefix_aa = 1;";

 assertEquals(expectedResult, result);
 }

 @Test
 public void testRenameLocalVariablesWithSameName() {
 RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.initialize();
 CompilerInput input = new CompilerInput("function foo(a) {" +
         "var a = 1;" +
         "}", null);
 Node externs = Nodeops.parse(input.getCode());
 Node root = Nodeops.parseTopLevel(input.getCode(), null);

 renamer.process(externs, root);
 String result = compiler.getJSWriter().getJs();

 String expectedResult = "function foo(aa) {" +
         "var aa_1 = 1;" +
         "}";

 assertEquals(expectedResult, result);
 }

 @Test
 public void testRenameLocalVariablesWithSameName2() {
 RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.initialize();
 CompilerInput input = new CompilerInput("function foo(a) {" +
         "var a = 1;" +
         "var a = 2;" +
         "}", null);
 Node externs = Nodeops.parse(input.getCode());
 Node root = Nodeops.parseTopLevel(input.getCode(), null);

 renamer.process(externs, root);
 String result = compiler.getJSWriter().getJs();

 String expectedResult = "function foo(aa) {" +
         "var aa_1 = 1;" +
         "var aa_2 = 2;" +
         "}";

 assertEquals(expectedResult, result);
 }

 @Test
 public void testRenameLocalVariablesWithSameName3() {
 RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);
 Compiler compiler = new Compiler();
 compiler.initialize();
 CompilerInput input = new CompilerInput("function foo(a) {" +
         "var a = 1;" +
         "var a = 2;" +
         "var a = 3;" +
         "}", null);
 Node externs = Nodeops.parse(input.getCode());
 Node root = Nodeops.parseTopLevel(input.getCode(), null);

 renamer.process(externs, root);
 String result = compiler.getJSWriter().getJs();

 String expectedResult = "function foo(aa) {" +
         "var aa_1 = 1;" +
         "var aa_2 = 2;" +
         "var aa_3 = 3;" +
         "}";

 assertEquals(expectedResult, result);
 }
}