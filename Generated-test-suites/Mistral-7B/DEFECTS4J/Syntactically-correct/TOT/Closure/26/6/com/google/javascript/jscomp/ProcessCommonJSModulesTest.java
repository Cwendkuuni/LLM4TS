import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ProcessingEnvironment;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private ProcessingEnvironment env;
 private Node rootNode;

 @Before
 public void setup() {
 compiler = new Compiler();
 env = new ProcessingEnvironment();
 env.setSourceFileName("input.js");
 rootNode = IR.script(Node.EMPTY_NODE_LIST);
 compiler.compile(rootNode, env, new CompilerOptions());
 }

 @Test
 public void testProcessCommonJSModules() {
 // Given: A CommonJS module file
 String inputJs = "var foo = require('./bar.js');";
 Node inputNode = IR.script(
 Node.newText("var foo = require('./bar.js');"), env.createSourceInfo(0, 0));
 Node inputScript = IR.script(
 Node.newText("// # sourceURL=input.js")
 .concat(inputNode), env.createSourceInfo(0, 0));

 // When: ProcessCommonJSModules is called
 ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
 pcm.process(env.getExterns(), rootNode);
 NodeTraversal nt = new NodeTraversal(compiler, rootNode, null);
 nt.traverse();

 // Then: The require call is replaced with a direct reference
 Node foo = nt.getRoot().getFirstChild();
 assertEquals("require('./bar.js')", foo.getString());
 assertEquals("bar$", foo.getFirstChild().getString());

 // And: The module exports are renamed to prevent conflicts
 Node exports = nt.getRoot().getLastChild();
 assertEquals("exports", exports.getString());
 assertEquals("module$exports", exports.getChildAtIndex(0).getString());
 }

 @Test
 public void testProcessCommonJSModulesWithRelativeRequire() {
 // Given: A CommonJS module file with relative require
 String inputJs = "var foo = require('../bar.js');";
 Node inputNode = IR.script(
 Node.newText("var foo = require('../bar.js');"), env.createSourceInfo(0, 0));
 Node inputScript = IR.script(
 Node.newText("// # sourceURL=input.js")
 .concat(inputNode), env.createSourceInfo(0, 0));

 // When: ProcessCommonJSModules is called
 ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
 pcm.process(env.getExterns(), rootNode);
 NodeTraversal nt = new NodeTraversal(compiler, rootNode, null);
 nt.traverse();

 // Then: The require call is replaced with a direct reference
 Node foo = nt.getRoot().getFirstChild();
 assertEquals("require('../bar.js')", foo.getString());
 assertEquals("bar$", foo.getFirstChild().getString());

 // And: The relative require is resolved correctly
 assertFalse(nt.getRoot().containsName("../../"));
 }

 @Test
 public void testProcessCommonJSModulesWithModuleProvide() {
 // Given: A CommonJS module file with module.exports
 String inputJs = "exports.foo = 'bar';";
 Node inputNode = IR.script(
 Node.newText("exports.foo = 'bar';"), env.createSourceInfo(0, 0));
 Node inputScript = IR.script(
 Node.newText("// # sourceURL=input.js")
 .concat(inputNode), env.createSourceInfo(0, 0));

 // When: ProcessCommonJSModules is called
 ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, "");
 pcm.process(env.getExterns(), rootNode);
 NodeTraversal nt = new NodeTraversal(compiler, rootNode, null);
 nt.traverse();

 // Then: The module exports are renamed and assigned to the module name
 Node exports = nt.getRoot().getLastChild();
 assertEquals("exports", exports.getString());
 Node moduleName = nt.getRoot().getLastChildBefore(exports);
 assertEquals("module$exports", moduleName.getLastChild().getString());
 assertEquals("bar", exports.getChildAtIndex(0).getString());
 }
}