package com.google.javascript.jscomp.test;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.test.BaseCompilerTest;

public class RenameLabelsTest extends BaseCompilerTest {

 @Test
 public void testRenameLabels() throws Exception {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setPrintDiagnostics(false);
 options.setLanguageIn(LanguageMode.ECMASCRIPT6);
 options.setWarningLevel(WarningLevel.ERRORS);

 String jsSource = "function test() {" +
 "  a: { " +
 "    b: { " +
 "      foo(); " +
 "    } " +
 "  } " +
 "  a: { " +
 "    b: break a; " +
 "  } " +
 "}";

 Node root = parseJs(jsSource, null, null);
 RenameLabels pass = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), true);
 pass.process(null, root);
 String output = compileToText(compiler, root, options);

 // Check that the labels were renamed correctly
 assertEquals("function test() {" +
 "  a: { " +
 "    b: {" +
 "      foo();" +
 "    }" +
 "  } " +
 "  a: { " +
 "    b: break a;" +
 "  }" +
 "}", output);
 }

 @Test(expected = RuntimeException.class)
 public void testRenameLabelsWithInvalidLabel() throws Exception {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setPrintDiagnostics(true);
 options.setLanguageIn(LanguageMode.ECMASCRIPT6);
 options.setWarningLevel(WarningLevel.ERRORS);

 String jsSource = "function test() {" +
 "  a: { " +
 "    b: 1;" + // label should not be a string
 "  }" +
 "}";

 Node root = parseJs(jsSource, null, null);
 RenameLabels pass = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), true);
 pass.process(null, root);

 // Ensure that an exception is thrown due to the invalid label
 fail("Expected an exception to be thrown");
 }

 @Test
 public void testRenameLabelsWithNestedLabels() throws Exception {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setPrintDiagnostics(false);
 options.setLanguageIn(LanguageMode.ECMASCRIPT6);
 options.setWarningLevel(WarningLevel.ERRORS);

 String jsSource = "function test() {" +
 "  a: { " +
 "    b: { " +
 "      a: { " +
 "        c: { " +
 "          d: { " +
 "            foo(); " +
 "          } " +
 "        } " +
 "      } " +
 "    } " +
 "  } " +
 "}";

 Node root = parseJs(jsSource, null, null);
 RenameLabels pass = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), true);
 pass.process(null, root);
 String output = compileToText(compiler, root, options);

 // Check that the labels were renamed correctly
 assertEquals("function test() {" +
 "  a: { " +
 "    b: { " +
 "      a: { " +
 "        c: { " +
 "          d: {" +
 "            foo();" +
 "          }" +
 "        } " +
 "      } " +
 "    } " +
 "  } " +
 "}", output);
 }

 @Test
 public void testRenameLabelsWithRemovedUnusedLabel() throws Exception {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setPrintDiagnostics(false);
 options.setLanguageIn(LanguageMode.ECMASCRIPT6);
 options.setWarningLevel(WarningLevel.ERRORS);

 String jsSource = "function test() {" +
 "  a: { " +
 "    b: { " +
 "      foo(); " +
 "      break a; " +
 "    } " +
 "  } " +
 "  a: {}" + // unused label
 "}";

 Node root = parseJs(jsSource, null, null);
 RenameLabels pass = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), true);
 pass.process(null, root);
 String output = compileToText(compiler, root, options);

 // Check that the unused label was removed
 assertEquals("function test() {" +
 "  a: { " +
 "    b: {" +
 "      foo(); " +
 "      break a;" +
 "    } " +
 "  }" +
 "}", output);
 }
}