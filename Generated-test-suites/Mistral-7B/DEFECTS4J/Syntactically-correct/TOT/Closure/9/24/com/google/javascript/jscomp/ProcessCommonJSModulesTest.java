import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

 private AbstractCompiler compiler;
 private CompilerInput input;
 private Node root;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 input = new CompilerInput("test.js", "test.js");
 root = NodeFactory.createScript(input);
 }

 @Test
 public void testProcessCommonJSModules() {
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
 p.process(input, root);

 Node module = root.getFirstChild();
 assertEquals("module$test", module.getQualifiedName());
 assertEquals("goog.provide(\"module$test\");\n" +
         "goog.require(\"module$dependency\");\n" +
         "var module$test = module.exports;\n" +
         "module.exports = module$test;\n",
         root.getSource());
 }

 @Test
 public void testProcessCommonJSModulesWithDependencies() {
 input.addRequire("module$dependency");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
 p.process(input, root);

 Node module = root.getFirstChild();
 assertEquals("module$test", module.getQualifiedName());
 assertEquals("goog.provide(\"module$test\");\n" +
         "goog.require(\"module$dependency\");\n" +
         "goog.require(\"module$dependency\");\n" +
         "var module$test = module.exports;\n" +
         "module.exports = module$test;\n",
         root.getSource());
 }

 @Test
 public void testProcessCommonJSModulesWithModuleExports() {
 input.addProvide("module$test");
 input.addProvide("module$dependency");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
 p.process(input, root);

 Node module = root.getFirstChild();
 assertEquals("module$test", module.getQualifiedName());
 assertEquals("goog.provide(\"module$test\");\n" +
         "goog.provide(\"module$dependency\");\n" +
         "goog.require(\"module$dependency\");\n" +
         "var module$dependency = module.module$exports;\n" +
         "var module$test = module.exports;\n" +
         "module.exports = module$test;\n",
         root.getSource());
 }

 @Test
 public void testProcessCommonJSModulesWithRelativeRequires() {
 input.addSource("test.js", "require('./test2');");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
 p.process(input, root);

 Node requireCall = root.getFirstChild().getFirstChild();
 Node requireModule = requireCall.getChildAtIndex(1);
 assertEquals("module$test2", requireModule.getString());
 }

 @Test
 public void testProcessCommonJSModulesWithRequireRelativeToCurrentFile() {
 input.addSource("test.js", "require('test2.js');");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
 p.process(input, root);

 Node requireCall = root.getFirstChild().getFirstChild();
 Node requireModule = requireCall.getChildAtIndex(1);
 assertEquals("module$test2", requireModule.getString());
 }

 @Test(expected = RuntimeException.class)
 public void testProcessCommonJSModulesWithInvalidRequireFormat() {
 input.addSource("test.js", "require test2.js;");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
 try {
 p.process(input, root);
 fail();
 } catch (RuntimeException e) {
 // expected
 }
 }

 @Test
 public void testProcessCommonJSModulesWithModuleNameStartingWithDollarSign() {
 input.addSource("test.js", "var $module = require('$module2');");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
 p.process(input, root);

 Node requireCall = root.getFirstChild().getFirstChild();
 Node requireModule = requireCall.getChildAtIndex(1);
 assertEquals("module$module2", requireModule.getString());
 }

 @Test
 public void testProcessCommonJSModulesWithModuleNameContainingDollarSign() {
 input.addSource("test.js", "var moduleWithDollar = require('module$withDollar');");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
 p.process(input, root);

 Node requireCall = root.getFirstChild().getFirstChild();
 Node requireModule = requireCall.getChildAtIndex(1);
 assertEquals("module$module$withDollar", requireModule.getString());
 }

 @Test
 public void testProcessCommonJSModulesWithModuleNameContainingDot() {
 input.addSource("test.js", "var moduleWithDot = require('module.withDot');");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "");
 p.process(input, root);

 Node requireCall = root.getFirstChild().getFirstChild();
 Node requireModule = requireCall.getChildAtIndex(1);
 assertEquals("module$module.withDot", requireModule.getString());
 }
}