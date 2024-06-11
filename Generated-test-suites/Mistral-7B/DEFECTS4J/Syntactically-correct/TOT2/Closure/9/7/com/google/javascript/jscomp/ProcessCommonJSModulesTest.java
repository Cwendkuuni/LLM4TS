import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.ProcessCommonJsModulesCallback;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

 @Test
 public void testProcessCommonJSModules() {
 // Given a valid input file
 CompilerInput input = new CompilerInput("input.js", "input.js");
 AbstractCompiler compiler = new AbstractCompiler();
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

 // When processing the input file
 processor.process(input.getRoot(), input.getExterns());

 // Then the expected changes should have been made
 JSModule module = processor.getModule();
 Node root = input.getRoot();
 assertEquals("module$exports", root.getChildAtIndex(0).getString());
 assertNull(root.getChildAtIndex(1)); // No require calls

 // Given a file with require calls
 input = new CompilerInput("require.js", "require.js");
 root = input.getRoot();
 root.addChildToFront(IR.exprResult(IR.call(IR.getprop(IR.name("goog"),
 IR.string("require")), IR.string("moduleA"))));
 root.addChildToBack(IR.exprResult(IR.call(IR.getprop(IR.name("goog"),
 IR.string("provide")), IR.string("moduleB"))));

 // When processing the input file
 processor = new ProcessCommonJSModules(compiler, "");
 processor.process(input.getRoot(), input.getExterns());

 // Then the expected changes should have been made
 module = processor.getModule();
 assertEquals("moduleB", module.getName());
 assertNotNull(root.getChildAtIndex(0)); // goog.require call
 assertNotNull(root.getChildAtIndex(1)); // moduleA = require("moduleA")
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidURI() {
 // Given an input file with an invalid URI
 CompilerInput input = new CompilerInput("require.js", "require.js");
 Node root = input.getRoot();
 root.addChildToFront(IR.exprResult(IR.call(IR.getprop(IR.name("goog"),
 IR.string("require")), IR.string("moduleA"))));
 root.addChildAtIndex(0, IR.string("invalid://module"));

 // When processing the input file
 ProcessCommonJSModules processor = new ProcessCommonJSModules(new AbstractCompiler(), "");

 // Then an exception should be thrown
 try {
 processor.process(input.getRoot(), input.getExterns());
 fail("Expected an exception to be thrown");
 } catch (RuntimeException e) {
 // Expected exception message
 assertTrue(e.getMessage().contains("Invalid URI for require"));
 }
 }

 @Test
 public void testRelativeRequire() {
 // Given a file with a relative require call
 CompilerInput input = new CompilerInput("fileA.js", "fileA.js");
 Node root = input.getRoot();
 root.addChildToFront(IR.exprResult(IR.call(IR.getprop(IR.name("goog"),
 IR.string("require")), IR.string("./moduleB"))));

 // When processing the input file
 ProcessCommonJSModules processor = new ProcessCommonJSModules(new AbstractCompiler(), "");
 processor.process(input.getRoot(), input.getExterns());

 // Then the expected changes should have been made
 JSModule module = processor.getModule();
 assertEquals("moduleB", module.getName());
 assertNotNull(root.getChildAtIndex(0)); // goog.require call
 }

 @Test
 public void testAbsoluteRequire() {
 // Given a file with an absolute require call
 CompilerInput input = new CompilerInput("fileA.js", "fileA.js");
 Node root = input.getRoot();
 root.addChildToFront(IR.exprResult(IR.call(IR.getprop(IR.name("goog"),
 IR.string("require")), IR.string("http://example.com/moduleB"))));

 // When processing the input file
 ProcessCommonJSModules processor = new ProcessCommonJSModules(new AbstractCompiler(), "");
 processor.process(input.getRoot(), input.getExterns());

 // Then the expected changes should have been made
 JSModule module = processor.getModule();
 assertEquals("moduleB", module.getName());
 assertNotNull(root.getChildAtIndex(0)); // goog.require call
 }

 @Test
 public void testModuleWithExports() {
 // Given a file with a module that exports something
 CompilerInput input = new CompilerInput("moduleA.js", "moduleA.js");
 Node root = input.getRoot();
 root.addChildToFront(IR.exprResult(IR.call(IR.getprop(IR.name("goog"),
 IR.string("provide")), IR.string("moduleA"))));
 root.addChildToBack(IR.exprResult(IR.call(IR.getprop(IR.name("goog"),
 IR.string("require")), IR.string("moduleB"))));
 root.addChildToBack(IR.exprResult(IR.assign(IR.getprop(IR.name("goog"),
 IR.string("moduleA$exports")), IR.string("value"))));

 // When processing the input file
 ProcessCommonJSModules processor = new ProcessCommonJSModules(new AbstractCompiler(), "");
 processor.process(input.getRoot(), input.getExterns());

 // Then the expected changes should have been made
 JSModule module = processor.getModule();
 assertEquals("moduleA", module.getName());
 assertEquals("value", module.getModuleExports().getValue().getString());
 }
}