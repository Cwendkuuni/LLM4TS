import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import static com.google.javascript.rhino.NodeType.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private CompilerOptions options;
 private Node rootNode;

 @Before
 public void setUp() throws IOException {
 compiler = new Compiler();
 options = new CompilerOptions();
 options.setPrintStackTraces(true);
 options.setProcessorName("ProcessCommonJSModulesTest");

 String inputPath = "src/test/java/";
 String inputFile = "ProcessCommonJSModulesInput.js";
 File input = new File(inputPath + inputFile);
 rootNode = compiler.compile(Arrays.asList(input), options).getAST();
 }

 @Test
 public void testProcessCommonJSModules() {
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", true);
 p.process(options.getDefaultExterns(), rootNode);

 JSModule module = p.getModule();
 assertNotNull(module);
 assertEquals("module$Test", module.getName());

 Set<String> requiredModules = module.getRequiredModules();
 assertEquals(2, requiredModules.size());
 assertEquals("module$Module1", requiredModules.iterator().next());
 assertEquals("module$Module2", requiredModules.iterator().next());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidSourceName() {
 String inputPath = "src/test/java/";
 String inputFile = "ProcessCommonJSModulesInvalidSourceNameInput.js";
 File input = new File(inputPath + inputFile);
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", true);
 p.process(options.getDefaultExterns(), compiler.parse(input, null, null));
 fail("Should have thrown a RuntimeException");
 }

 @Test
 public void testInvalidRequireCall1() {
 replaceNode(rootNode, 0, 1, "require", "require1");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", true);
 p.process(options.getDefaultExterns(), rootNode);

 Node requireCall = rootNode.getChildAtIndex(0);
 assertNotNull(requireCall);
 assertEquals("require1", requireCall.getFirstChild().getString());

 Node script = rootNode.getChildAtIndex(1);
 Node require = script.getChildAtIndex(1);
 Node moduleRef = require.getChildAtIndex(0);
 assertEquals("module$Require1", moduleRef.getString());
 }

 @Test
 public void testInvalidRequireCall2() {
 replaceNode(rootNode, 0, 1, "require", "requireModule");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", true);
 p.process(options.getDefaultExterns(), rootNode);
 Node requireCall = rootNode.getChildAtIndex(0);
 assertNotNull(requireCall);

 Node script = rootNode.getChildAtIndex(1);
 Node require = script.getChildAtIndex(1);
 Node moduleRef = require.getChildAtIndex(0);
 assertNull(moduleRef);
 }

 @Test
 public void testModuleExports() {
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", true);
 p.process(options.getDefaultExterns(), rootNode);
 Node moduleExports = rootNode.getChildAtIndex(2);
 assertNotNull(moduleExports);
 assertEquals("module$exports", moduleExports.getChildAtIndex(0).getString());
 }

 @Test
 public void testScriptWithoutRequire() {
 replaceNode(rootNode, 1, 0, "script", "scriptWithoutRequire");
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", true);
 p.process(options.getDefaultExterns(), rootNode);
 Node script = rootNode.getChildAtIndex(1);
 assertNull(script.getChildAtIndex(1));
 }

 private void replaceNode(Node node, int index, int count, String oldName,
 String newName) {
 Node parent = node.getParent();
 Node child = node.getChildAtIndex(index);
 Node newNode = IR.name(newName).copyInformationFromForTree(child);
 parent.replaceChild(child, newNode);
 for (int i = index + count; i < node.getChildCount(); i++) {
 Node next = node.getChildAtIndex(i);
 next.setIndex(next.getIndex() + count);
 }
}

}