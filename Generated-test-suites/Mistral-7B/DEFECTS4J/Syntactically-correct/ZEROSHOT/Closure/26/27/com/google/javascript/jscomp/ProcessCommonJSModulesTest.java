import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.io.File;
import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest {

 private CompilerPass processCommonJSModules;
 private AbstractCompiler compiler;
 private CompilerInput input;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 input = mock(CompilerInput.class);
 nodeFactory = mock(NodeFactory.class);
 processCommonJSModules = new ProcessCommonJSModules(compiler, ".", true);
 }

 @Test
 public void testGuessCJSModuleName() {
 String filename = "test.js";
 assertEquals("module$test", processCommonJSModules.guessCJSModuleName(filename));
 }

 @Test
 public void testToModuleName() {
 String filename = "test.js";
 assertEquals("module$test", processCommonJSModules.toModuleName(filename));
 String requiredFilename = "./test.js";
 assertEquals("module$test", processCommonJSModules.toModuleName(requiredFilename, filename));
 }

 @Test
 public void testNormalizeSourceName() {
 String filename = "test.js";
 assertEquals("test.js", processCommonJSModules.normalizeSourceName(filename));
 String filenameWithPrefix = "./test.js";
 assertEquals("test.js", processCommonJSModules.normalizeSourceName(filenameWithPrefix));
 }

 @Test
 public void testProcess() throws Exception {
 CompilerOptions options = mock(CompilerOptions.class);
 when(options.getSourceRoot()).thenReturn(".");
 when(compiler.getOptions()).thenReturn(options);
 when(compiler.getInput()).thenReturn(input);
 when(input.getSourceFileNode()).thenReturn(nodeFactory.createScriptNode(""));

 processCommonJSModules.process(input, nodeFactory.createScriptNode(""));

 Node root = nodeFactory.createScriptNode("module.exports = 1;");
 NodeTraversal traversal = new NodeTraversal(compiler, root, new CompilerPassManager(), nodeFactory);
 traversal.traverse();

 Node moduleExports = root.getChildAtIndex(0);
 assertEquals("module$exports", moduleExports.getString());
 }

 @Test
 public void testProcessWithModuleProvide() throws Exception {
 CompilerOptions options = mock(CompilerOptions.class);
 when(options.getSourceRoot()).thenReturn(".");
 when(compiler.getOptions()).thenReturn(options);
 when(compiler.getInput()).thenReturn(input);
 when(input.getName()).thenReturn("test.js");
 when(input.getSourceFileNode()).thenReturn(nodeFactory.createScriptNode("module.exports = function foo() {};\ngoog.provide('test.foo')"));

 processCommonJSModules.process(input, nodeFactory.createScriptNode(""));

 Node root = nodeFactory.createScriptNode("1;goog.provide('test.bar')");
 NodeTraversal traversal = new NodeTraversal(compiler, root, new CompilerPassManager(), nodeFactory);
 traversal.traverse();

 Node test = root.getChildAtIndex(0);
 assertEquals("test.bar", test.getString());
 }

 @Test
 public void testProcessWithModuleRequire() throws Exception {
 CompilerOptions options = mock(CompilerOptions.class);
 when(options.getSourceRoot()).thenReturn(".");
 when(compiler.getOptions()).thenReturn(options);
 when(compiler.getInput()).thenReturn(input);
 when(input.getName()).thenReturn("test.js");
 when(input.getSourceFileNode()).thenReturn(nodeFactory.createScriptNode("require('./foo')"));

 processCommonJSModules.process(input, nodeFactory.createScriptNode(""));

 Node root = nodeFactory.createScriptNode("1;goog.require('foo')");
 NodeTraversal traversal = new NodeTraversal(compiler, root, new CompilerPassManager(), nodeFactory);
 traversal.traverse();

 Node foo = root.getChildAtIndex(0);
 assertNull(foo);
 }

 @Test
 public void testProcessWithModuleExports() throws Exception {
 CompilerOptions options = mock(CompilerOptions.class);
 when(options.getSourceRoot()).thenReturn(".");
 when(compiler.getOptions()).thenReturn(options);
 when(compiler.getInput()).thenReturn(input);
 when(input.getName()).thenReturn("test.js");
 when(input.getSourceFileNode()).thenReturn(nodeFactory.createScriptNode("module.exports = function foo() {};\nmodule.exports.bar = 1"));

 processCommonJSModules.process(input, nodeFactory.createScriptNode(""));

 Node root = nodeFactory.createScriptNode("1;goog.require('test.foo');goog.require('test.foo.bar')");
 NodeTraversal traversal = new NodeTraversal(compiler, root, new CompilerPassManager(), nodeFactory);
 traversal.traverse();

 Node testFoo = root.getChildAtIndex(0);
 assertEquals("test.foo", testFoo.getString());
 Node testFooBar = root.getChildAtIndex(1);
 assertEquals("test.foo.bar", testFooBar.getString());
 }

 @Test
 public void testProcessWithDependencies() throws Exception {
 CompilerOptions options = mock(CompilerOptions.class);
 when(options.getSourceRoot()).thenReturn(".");
 when(compiler.getOptions()).thenReturn(options);
 when(compiler.getInput()).thenReturn(input);
 when(input.getName()).thenReturn("test.js");
 when(input.getSourceFileNode()).thenReturn(nodeFactory.createScriptNode("require('./foo')"));
 Set<String> requires = Sets.newHashSet("foo");
 when(input.getRequireList()).thenReturn(requires);

 processCommonJSModules.process(input, nodeFactory.createScriptNode(""));

 Node root = nodeFactory.createScriptNode("1;goog.require('foo')");
 NodeTraversal traversal = new NodeTraversal(compiler, root, new CompilerPassManager(), nodeFactory);
 traversal.traverse();

 Node foo = root.getChildAtIndex(0);
 assertNull(foo);
 }
}