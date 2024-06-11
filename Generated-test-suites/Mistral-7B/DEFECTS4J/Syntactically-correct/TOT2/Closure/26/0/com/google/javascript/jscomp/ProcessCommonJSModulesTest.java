import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessingEnvironment;
import com.google.javascript.rhino.SourceFile;

import java.io.File;
import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private ProcessingEnvironment env;
 private SourceFile sourceFile;
 private String filename;
 private ProcessCommonJSModules processor;

 @Before
 public void setUp() {
 compiler = new Compiler();
 compiler.init();
 env = new ProcessingEnvironment(new CompilerOptions());
 sourceFile = new SourceFile(new URI("test:/test.js"), null, null, env);
 filename = "/test.js";
 processor = new ProcessCommonJSModules(compiler, filename);
 }

 @Test
 public void testProcessCommonJSModules_SimpleRequire() {
 Node root = IR.block(
 IR.name("goog.require"),
 IR.string("module1"),
 IR.name("module1.doSomething")).srcref(sourceFile);
 sourceFile.parse(root);
 processor.process(env.getExterns(), root);
 Node expected = IR.call(IR.getprop(IR.name("module1"), "doSomething"));
 assertEquals(expected, root.getFirstChild().getFirstChild());
 }

 @Test
 public void testProcessCommonJSModules_MultipleRequires() {
 Node root = IR.block(
 IR.name("goog.require"),
 IR.string("module1"),
 IR.name("module1.doSomething"),
 IR.name("goog.require"),
 IR.string("module2"),
 IR.name("module2.doSomethingElse")).srcref(sourceFile);
 sourceFile.parse(root);
 processor.process(env.getExterns(), root);
 Node expected = IR.call(IR.getprop(IR.name("module2"), "doSomethingElse"));
 assertEquals(expected, root.getLastChild().getFirstChild());
 }

 @Test
 public void testProcessCommonJSModules_RequiringModuleWithDependency() {
 Node dependencyNode = IR.block(
 IR.name("goog.require"),
 IR.string("dependencyModule")).srcref(new File("test:/dependency.js"));
 sourceFile.parse(IR.name("goog.require", "module1").srcref(sourceFile));
 sourceFile.parse(dependencyNode);
 processor.process(env.getExterns(), IR.name("module1").srcref(sourceFile));
 Node expected = IR.call(IR.getprop(IR.name("dependencyModule"), "doSomething"));
 assertEquals(expected, sourceFile.getTree().getLastChild().getFirstChild().getLastChild()
     .getFirstChild());
 }

 @Test(expected = RuntimeException.class)
 public void testProcessCommonJSModules_RelativeRequire() {
 Node root = IR.block(
 IR.name("goog.require"),
 IR.string("./module1")).srcref(sourceFile);
 sourceFile.parse(root);
 processor.process(env.getExterns(), root);
 }

 @Test
 public void testProcessCommonJSModules_ModuleExports() {
 Node root = IR.block(
 IR.name("goog.require"),
 IR.string("module1"),
 IR.name("module1.exports.doSomething")).srcref(sourceFile);
 Node module1 = IR.name("module1").srcref(sourceFile);
 module1.putProp("module$exports", IR.objectlit()
     .setProp("doSomething", IR.string("expectedValue"))
     .copyInformationFromForTree(module1));
 sourceFile.parse(root);
 processor.process(env.getExterns(), root);
 Node expected = IR.string("expectedValue");
 assertEquals(expected, root.getLastChild().getFirstChild().getFirstChild());
 }

 @Test
 public void testProcessCommonJSModules_ModuleExportsWithVariables() {
 SourceFile dependencySourceFile = new SourceFile(new URI("test:/dependency.js"),
 null, null, env);
 Node dependencyRoot = IR.block(
 IR.name("goog.require"),
 IR.string("dependencyModule"),
 IR.name("dependencyModule.exports.x"),
 IR.name("x"),
 IR.name("x").set(IR.number(10))).srcref(dependencySourceFile);
 dependencySourceFile.parse(dependencyRoot);
 Node root = IR.block(
 IR.name("goog.require"),
 IR.string("module1"),
 IR.name("module1.exports.y = dependencyModule.exports.x")).srcref(sourceFile);
 sourceFile.parse(root);
 processor.process(env.getExterns(), root);
 Node expected = IR.number(10);
 assertEquals(expected, sourceFile.getTree()
     .getLastChild().getLastChild().getLastChild().getLastChild());
 }

 @Test
 public void testProcessCommonJSModules_CircularDependencies() {
 Node module1 = IR.name("module1").srcref(sourceFile);
 Node module2 = IR.name("module2").srcref(sourceFile);
 module1.putProp("exports", IR.objectlit()
     .setProp("require", IR.name("require"))
     .setProp("doSomething", IR.name("module2")))
     .copyInformationFromForTree(module1);
 module2.putProp("exports", IR.objectlit()
     .setProp("require", IR.name("require"))
     .setProp("doSomething", IR.name("module1")))
     .copyInformationFromForTree(module2);
 sourceFile.parse(IR.name("goog.require", "module1").srcref(sourceFile));
 sourceFile.parse(IR.name("goog.require", "module2").srcref(sourceFile));
 processor.process(env.getExterns(), root);
 assertTrue(root.getTree().getLastChild().getLastChild()
     .getLastChild().isCall());
 }

 @Test
 public void testProcessCommonJSModules_ModuleNotFound() {
 Node root = IR.block(
 IR.name("goog.require"),
 IR.string("moduleNotExist")).srcref(sourceFile);
 processor.process(env.getExterns(), root);
 assertNull(sourceFile.getTree().getLastChild().getFirstChild());
 }

 @Test
 public void testProcessCommonJSModules_ModuleAlreadyImported() {
 Node root = IR.block(
 IR.name("goog.require"),
 IR.string("module1"),
 IR.name("module1.exports.doSomething"),
 IR.name("goog.require"),
 IR.string("module1")).srcref(sourceFile);
 processor.process(env.getExterns(), root);
 assertNull(sourceFile.getTree().getLastChild().getLastChild()
     .getLastChild().getLastChild());
 }

 @Test
 public void testProcessCommonJSModules_EmptySourceFile() {
 Node root = IR.block().srcref(sourceFile);
 processor.process(env.getExterns(), root);
 assertNull(sourceFile.getTree().getLastChild());
 }

 @Test
 public void testProcessCommonJSModules_ProcessCommonJSModulesCallback_VisitRequireCall() {
 Node requireCall = IR.call(IR.getprop(IR.name("goog"), "require"),
 IR.string("module1")).srcref(sourceFile);
 Node expected = IR.name("module1").srcref(sourceFile);
 processor.process(env.getExterns(), root);
 assertEquals(expected, requireCall.getParent().getFirstChild());
 }

 @Test
 public void testProcessCommonJSModules_ProcessCommonJSModulesCallback_VisitScript() {
 Node script = IR.script(IR.name("module1"), IR.string("doSomething();")).srcref(sourceFile);
 processor.process(env.getExterns(), root);
 Node expected = IR.name("module1").srcref(sourceFile);
 assertEquals(expected, IR.getProp(script, Node.ORIGINALNAME_PROP));
 }

 @Test
 public void testProcessCommonJSModules_ProcessCommonJSModulesCallback_VisitModuleExports() {
 Node root = IR.block(
 IR.name("goog.require"),
 IR.string("module1"),
 IR.name("doSomething")).srcref(sourceFile);
 Node module1 = IR.name("module1").srcref(sourceFile);
 module1.putProp("exports", IR.objectlit()
     .setProp("doSomething", IR.name("doSomething")))
     .copyInformationFromForTree(module1);
 processor.process(env.getExterns(), root);
 Node expected = IR.name("doSomething");
 assertEquals(expected, IR.getProp(IR.getProp(IR.getProp(sourceFile.getTree(), Node.ORIGINALNAME_PROP), "module1"),
 "exports").getLastChild().getLastChild().getLastChild());
 }

 @Test
 public void testProcessCommonJSModules_ProcessCommonJSModulesCallback_SuffixVarsCallback() {
 Node originalVar = IR.name("x").srcref(sourceFile);
 Node suffixVar = IR.name("x$$moduleName").srcref(sourceFile);
 processor.process(env.getExterns(), root);
 assertEquals(originalVar.getSourceName(), suffixVar.getSourceName());
 assertEquals(originalVar.getString(), suffixVar.getString());
 }
}