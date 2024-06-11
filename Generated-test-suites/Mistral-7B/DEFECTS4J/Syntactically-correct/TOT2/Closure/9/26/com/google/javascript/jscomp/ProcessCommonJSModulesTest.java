import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProcessCommonJSModulesTest {

 private AbstractCompiler compiler;
 private CompilerInput input;
 private CompilerPassManager passManager;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 input = new CompilerInput("test.js", "test.js");
 passManager = new CompilerPassManager(compiler, input);
 }

 @After
 public void tearDown() {
 passManager = null; // to clear mocks
 }

 @Test
 public void testProcessCommonJSModules() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".");
 passManager.addPass(processCommonJSModules);
 Node root = new Node("program", null);
 passManager.process(root);
 verify(compiler, times(1)).reportCodeChange();
 }

 @Test
 public void testGuessCJSModuleName() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".", false);
 assertEquals("module$test", processCommonJSModules.guessCJSModuleName("test.js"));
 }

 @Test
 public void testToModuleName() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".", false);
 assertEquals("module$test", processCommonJSModules.toModuleName("test.js"));
 assertEquals("module$./lib/moduleB", processCommonJSModules.toModuleName("./lib/moduleB.js"));
 assertEquals("module$../lib/moduleB", processCommonJSModules.toModuleName("../lib/moduleB.js"));
 }

 @Test
 public void testGuessCJSModuleNameWithRelativePath() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "test/", false);
 assertEquals("module$test/moduleA", processCommonJSModules.guessCJSModuleName("moduleA.js"));
 }

 @Test(expected = RuntimeException.class)
 public void testGuessCJSModuleNameWithInvalidPath() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".", false);
 processCommonJSModules.guessCJSModuleName("../invalidPath");
 }

 @Test
 public void testNormalizeSourceName() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".", false);
 assertEquals("test", processCommonJSModules.normalizeSourceName("test.js"));
 assertEquals("test", processCommonJSModules.normalizeSourceName("test/test.js"));
 assertEquals("test", processCommonJSModules.normalizeSourceName("test/test.js/test.js"));
 }

 @Test
 public void testProcessCommonJsModulesCallback() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".", false);
 ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
 passManager.setCallback(callback);
 Node root = new Node("program", null);
 passManager.process(root);
 assertEquals(1, callback.scriptNodeCount);
 }

 @Test
 public void testProcessRequireCall() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".", false);
 ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
 passManager.setCallback(callback);
 Node requireNode = IR.call(IR.getprop(IR.name("require"), null), IR.string("moduleB"));
 Node script = IR.script();
 script.addChild(requireNode.cloneTree());
 Node root = new Node("program", null);
 root.addChild(script);
 passManager.process(root);
 assertEquals("module$require", script.getChildAtIndex(0).getFirstChild().getQualifiedName());
 }

 @Test
 public void testProcessScript() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".", false);
 ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
 passManager.setCallback(callback);
 Node script = IR.script();
 script.addChild(IR.exprResult(IR.assign(IR.name("goog.provide"), IR.string("moduleA"))));
 script.addChild(IR.exprResult(IR.assign(IR.name("moduleA"), IR.objectlit())));
 script.addChild(IR.exprResult(IR.assign(IR.name("moduleA.exports"), IR.objectlit())));
 script.addChild(IR.exprResult(IR.assign(IR.name("moduleA.exports.func"), IR.function(null, null, null, IR.string("func")))));
 Node root = new Node("program", null);
 root.addChild(script);
 passManager.process(root);
 assertEquals("moduleA$func", script.getChildAtIndex(0).getChildAtIndex(0).getQualifiedName());
 }

 @Test
 public void testProcessModuleExports() {
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, ".", false);
 ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
 passManager.setCallback(callback);
 Node exportsNode = IR.getprop(IR.name("module$exports"), null);
 Node script = IR.script();
 script.addChild(IR.exprResult(IR.assign(IR.name("goog.provide"), IR.string("moduleA"))));
 script.addChild(IR.exprResult(IR.assign(IR.name("moduleA"), IR.name("module$exports"))));
 script.addChild(IR.exprResult(IR.assign(IR.name("moduleA.exports"), IR.objectlit())));
 script.addChild(IR.exprResult(IR.assign(IR.name("moduleA.exports.func"), IR.function(null, null, null, IR.string("func")))));
 script.addChild(exportsNode.cloneTree());
 Node root = new Node("program", null);
 root.addChild(script);
 passManager.process(root);
 assertEquals("moduleA$exports$func", script.getChildAtIndex(0).getLastChild().getQualifiedName());
 }
}