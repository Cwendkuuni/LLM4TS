import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import org.junit.Test;

public class RenameVarsTest {

 private static final String ORIGINAL_CODE =
     "function foo(a, b) {" +
     "  var c = function(d, e) { return a; }" +
     "  var f = 'bar';" +
     "  var g = null;" +
     "  return c;" +
     "}" ;

 @Test
 public void testRenameVars() {
   CompilerOptions options = new CompilerOptions(new CompilerInput(ORIGINAL_CODE, "Test", null));
   Compiler compiler = new Compiler();
   compiler.setOptions(options);

   RenameVars renameVars = new RenameVars(compiler, "", false, true, true, null, null, null);
   renameVars.process(compiler.getExterns(), compiler.getMainModule());

   assertEquals("function foo(a, b) {" +
       "  var a0 = function(d, e) { return a; }" +
       "  var f0 = 'bar';" +
       "  var g0 = null;" +
       "  return a0;" +
       "}", compiler.getSourceCode());
 }

 @Test
 public void testPreserveAnonymousFunctionNames() {
   CompilerOptions options = new CompilerOptions(new CompilerInput(ORIGINAL_CODE, "Test", null));
   Compiler compiler = new Compiler();
   compiler.setOptions(options);

   RenameVars renameVars = new RenameVars(compiler, "", false, true, true, null, null, null);
   renameVars.preserveAnonymousFunctionNames = true;
   renameVars.process(compiler.getExterns(), compiler.getMainModule());

   assertEquals("function foo(a, b) {" +
       "  var anonymousFunction = function(d, e) { return a; };" +
       "  var f = 'bar';" +
       "  var g = null;" +
       "  return anonymousFunction;" +
       "}", compiler.getSourceCode());
 }

 @Test
 public void testLocalRenamingOnly() {
   CompilerOptions options = new CompilerOptions(new CompilerInput(ORIGINAL_CODE, "Test", null));
   Compiler compiler = new Compiler();
   compiler.setOptions(options);

   RenameVars renameVars = new RenameVars(compiler, "", true, true, true, null, null, null);
   renameVars.process(compiler.getExterns(), compiler.getMainModule());

   assertEquals("function foo(a, b) {" +
       "  var a0 = function(d, e) { return a; }" +
       "  var f = 'bar';" +
       "  var g = null;" +
       "  return a0;" +
       "}", compiler.getSourceCode());

   assertEquals("function foo(a, b) {" +
       "  function c(d, e) { return a; }" +
       "  var f = 'bar';" +
       "  var g = null;" +
       "  return c;" +
       "}", compiler.getDebugInfo().getCode());
 }

 @Test(expected = AssertionError.class)
 public void testRenameGlobalVars() {
   CompilerOptions options = new CompilerOptions(new CompilerInput(ORIGINAL_CODE, "Test", null));
   Compiler compiler = new Compiler();
   compiler.setOptions(options);

   RenameVars renameVars = new RenameVars(compiler, "renamed_", false, true, true, null, null, null);
   renameVars.process(compiler.getExterns(), compiler.getMainModule());

   // Expected error: "RenameVars: Cannot rename global variables when localRenamingOnly is false."
 }

 @Test
 public void testRenameVarsWithReservedNames() {
   CompilerOptions options = new CompilerOptions(new CompilerInput(ORIGINAL_CODE, "Test", null));
   Compiler compiler = new Compiler();
   compiler.setOptions(options);

   Set<String> reservedNames = Sets.newHashSet("function", "var", "return");
   RenameVars renameVars = new RenameVars(compiler, "", false, true, true, null, null, reservedNames);
   renameVars.process(compiler.getExterns(), compiler.getMainModule());

   assertEquals("function foo(a, b) {" +
       "  var a0 = function(d, e) { return a; }" +
       "  var f0 = 'bar';" +
       "  var g0 = null;" +
       "  return a0;" +
       "}", compiler.getSourceCode());

   assertEquals("function foo(a, b) {" +
       "  var a0 = function(d, e) { return a; }" +
       "  var f = 'bar';" +
       "  var g = null;" +
       "  return a0;" +
       "}", compiler.getDebugInfo().getCode());
 }

 @Test
 public void testRenameVarsWithExterns() {
   CompilerOptions options = new CompilerOptions(new CompilerInput(ORIGINAL_CODE, "Test", null));
   Compiler compiler = new Compiler();
   compiler.setOptions(options);

   Set<String> externNames = Sets.newHashSet("foo", "c");
   RenameVars renameVars = new RenameVars(compiler, "", false, true, true, null, null, null);
   renameVars.process(compiler.getExterns(), compiler.getMainModule());

   // Expected error: "RenameVars: Variable 'foo' is an extern and cannot be renamed."

   // Expected error: "RenameVars: Function 'c' is an extern and cannot be renamed."
 }

 @Test
 public void testRenameVarsWithReservedChars() {
   CompilerOptions options = new CompilerOptions(new CompilerInput(ORIGINAL_CODE, "Test", null));
   Compiler compiler = new Compiler();
   compiler.setOptions(options);

   char[] reservedCharacters = {'-', '.', '$'};
   RenameVars renameVars = new RenameVars(compiler, "", false, true, true, null, reservedCharacters, null);
   renameVars.process(compiler.getExterns(), compiler.getMainModule());

   assertEquals("function foo(a, b) {" +
       "  var a0 = function(d, e) { return a; }" +
       "  var f = 'bar';" +
       "  var g = null;" +
       "  return a0;" +
       "}", compiler.getSourceCode());

   assertEquals("function foo(a, b) {" +
       "  var a0 = function(d, e) { return a; }" +
       "  var f = 'bar';" +
       "  var g = null;" +
       "  return a0;" +
       "}", compiler.getDebugInfo().getCode());
 }

 @Test
 public void testRenameVarsWithNamedAnonymousFunctions() {
   CompilerOptions options = new CompilerOptions(new CompilerInput(ORIGINAL_CODE, "Test", null));
   Compiler compiler = new Compiler();
   compiler.setOptions(options);

   RenameVars renameVars = new RenameVars(compiler, "", false, false, true, null, null, null);
   renameVars.process(compiler.getExterns(), compiler.getMainModule());

   assertEquals("function foo(a, b) {" +
       "  var anonymousFunction = function anonymousFunction(d, e) { return a; };" +
       "  var f = 'bar';" +
       "  var g = null;" +
       "  return anonymousFunction;" +
       "}", compiler.getSourceCode());
 }

 @Test
 public void testRenameVarsWithEmptyPrefix() {
   CompilerOptions options = new CompilerOptions(new CompilerInput(ORIGINAL_CODE, "Test", null));
   Compiler compiler = new Compiler();
   compiler.setOptions(options);

   RenameVars renameVars = new RenameVars(compiler, "", false, true, true, null, null, null);
   renameVars.process(compiler.getExterns(), compiler.getMainModule());

   assertEquals("function foo(a, b) {" +
       "  var a0 = function(d, e) { return a; }" +
       "  var f = 'bar';" +
       "  var g = null;" +
       "  return a0;" +
       "}", compiler.getSourceCode());
 }
}