import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.enterScope;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.exitScope;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.process;
import static com.google.javascript.jscomp.NodeTraversal.NodeTraversalType.TRAVERSE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UnreachableCodeEliminationTest {

 private static final String TEST_CODE =
     "function testUnreachableCode() {"
       + "if (true) {"
       + "  return;"
       + "  alert('should not be shown');"
       + "}"
       + "alert('should be shown');"
       + "}";

 @Test
 public void testBasicFunctionality() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new SimpleCompiler(options);
   AstRoot root = new AstParser(options).parse(TEST_CODE, null);

   UnreachableCodeElimination elimination =
       new UnreachableCodeElimination(compiler, true);
   elimination.process(null, root.getTree());

   Node expected = new AstPrinter(options).print(root.getTree());
   Node actual = compiler.getCompiledSource();
   assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testRemoveDeadCodeFollowingReturn() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new SimpleCompiler(options);
   AstRoot root = new AstParser(options).parse(TEST_CODE, null);

   UnreachableCodeElimination elimination =
       new UnreachableCodeElimination(compiler, true);
   elimination.process(null, root.getTree());

   Node expected = new AstPrinter(options).print(root.getTree());
   Node actual = compiler.getCompiledSource();
   assertTrue(actual.toString().contains("should be shown"));
   assertFalse(actual.toString().contains("should not be shown"));
 }

 @Test
 public void testRemoveNoOpStatements() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new SimpleCompiler(options);
   AstRoot root = new AstParser(options).parse(
       "function testNoOpStatements() {"
           + "var x = 1;"
           + "true;"
           + "}",
       null);

   UnreachableCodeElimination elimination =
       new UnreachableCodeElimination(compiler, true);
   elimination.process(null, root.getTree());

   Node expected = new AstPrinter(options).print(root.getTree());
   Node actual = compiler.getCompiledSource();
   assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = RuntimeException.class)
 public void testRemoveCodeInsideCatchBlock() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new SimpleCompiler(options);
   AstRoot root = new AstParser(options).parse(
       "function testRemoveCodeInsideCatchBlock() {"
           + "try {"
           + "  throw new Error();"
           + "} catch (e) {"
           + "  // Unreachable code elimination should not remove code inside catch block"
           + "  return;"
           + "}"
           + "}",
       null);

   UnreachableCodeElimination elimination =
       new UnreachableCodeElimination(compiler, true);
   elimination.process(null, root.getTree());

   Node actual = compiler.getCompiledSource();
   fail("Exception should be thrown but none was");
 }

 @Test
 public void testRemoveDeadCodeAfterBreak() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new SimpleCompiler(options);
   AstRoot root = new AstParser(options).parse(
       "function testRemoveDeadCodeAfterBreak() {"
           + "for (var i = 0; i < 10; i++) {"
           + "  if (i > 5) {"
           + "    break;"
           + "  }"
           + "  alert('i = " + i + "');"
           + "}",
       null);

   UnreachableCodeElimination elimination =
       new UnreachableCodeElimination(compiler, true);
   elimination.process(null, root.getTree());

   Node expected = new AstPrinter(options).print(root.getTree());
   Node actual = compiler.getCompiledSource();
   assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testRemoveDeadCodeAfterContinue() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new SimpleCompiler(options);
   AstRoot root = new AstParser(options).parse(
       "function testRemoveDeadCodeAfterContinue() {"
           + "for (var i = 0; i < 10; i++) {"
           + "  if (i % 2 == 0) {"
           + "    continue;"
           + "  }"
           + "  alert('i = " + i + "');"
           + "}",
       null);

   UnreachableCodeElimination elimination =
       new UnreachableCodeElimination(compiler, true);
   elimination.process(null, root.getTree());

   Node expected = new AstPrinter(options).print(root.getTree());
   Node actual = compiler.getCompiledSource();
   assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testRemoveUnconditionalBranching() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new SimpleCompiler(options);
   AstRoot root = new AstParser(options).parse(
       "function testRemoveUnconditionalBranching() {"
           + "if (true) {"
           + "  break;"
           + "}"
           + "if (true) {"
           + "  break;"
           + "}"
           + "}",
       null);

   UnreachableCodeElimination elimination =
       new UnreachableCodeElimination(compiler, true);
   elimination.process(null, root.getTree());

   Node expected = new AstPrinter(options).print(root.getTree());
   Node actual = compiler.getCompiledSource();
   assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testRemoveDeadCodeInsideBlock() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new SimpleCompiler(options);
   AstRoot root = new AstParser(options).parse(
       "function testRemoveDeadCodeInsideBlock() {"
           + "if (true) {"
           + "  if (true) {"
           + "    return;"
           + "  }"
           + "  var x = 1;"
           + "}"
           + "}",
       null);

   UnreachableCodeElimination elimination =
       new UnreachableCodeElimination(compiler, true);
   elimination.process(null, root.getTree());

   Node expected = new AstPrinter(options).print(root.getTree());
   Node actual = compiler.getCompiledSource();
   assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testRemoveDeadCodeInsideFunction() {
   CompilerOptions options = new CompilerOptions();
   Compiler compiler = new SimpleCompiler(options);
   AstRoot root = new AstParser(options).parse(
       "function testRemoveDeadCodeInsideFunction() {"
           + "function innerFunction() {"
           + "  return;"
           + "}"
           + "}",
       null);

   UnreachableCodeElimination elimination =
       new UnreachableCodeElimination(compiler, true);
   elimination.process(null, root.getTree());

   Node expected = new AstPrinter(options).print(root.getTree());
   Node actual = compiler.getCompiledSource();
   assertEquals(expected.toString(), actual.toString());
 }
}