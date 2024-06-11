import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private ProcessCommonJSModules processCommonJSModules;

 @Before
 public void setUp() {
 compiler = new Compiler();
 processCommonJSModules = new ProcessCommonJSModules(compiler, "");
 }

 @Test
 public void testProcessSimpleCommonJSModule() {
 Node root = IR.program();
 Node requireCall = IR.call(IR.getprop(IR.name("require"), IR.string("module1")));
 Node module1 = IR.script(IR.string("exports = 1;"));
 root.addChildToFront(module1);
 root.addChildToFront(requireCall);
 processCommonJSModules.process(IR.nullLiteral(), root);
 Node moduleExports = IR.getprop(IR.getprop(IR.name("goog"), IR.string("require")),
 IR.string("module1")).getChildAtIndex(1);
 assertEquals(Integer.valueOf(1), moduleExports.getValue());
 }

 @Test
 public void testProcessCommonJSModuleWithDependency() {
 Node root = IR.program();
 Node module1 = IR.script(IR.string("exports = require('module2');"));
 Node module2 = IR.script(IR.string("exports = 2;"));
 root.addChildToFront(module1);
 root.addChildToFront(module2);
 processCommonJSModules.process(IR.nullLiteral(), root);
 Node moduleExports1 = IR.getprop(IR.getprop(IR.name("goog"), IR.string("require")),
 IR.string("module1")).getChildAtIndex(1);
 Node moduleExports2 = IR.getprop(IR.getprop(IR.name("goog"), IR.string("require")),
 IR.string("module2")).getChildAtIndex(1);
 assertEquals(Integer.valueOf(2), moduleExports1.getValue());
 assertEquals(Integer.valueOf(2), moduleExports2.getValue());
 }

 @Test(expected = RuntimeException.class)
 public void testProcessRelativeRequire() {
 Node root = IR.program();
 Node requireCall = IR.call(IR.getprop(IR.name("require"), IR.string("./module1")));
 Node module1 = IR.script(IR.string("exports = 1;"));
 root.addChildToFront(module1);
 root.addChildToFront(requireCall);
 processCommonJSModules.process(IR.nullLiteral(), root);
 }

 @Test
 public void testProcessModuleWithProvide() {
 Node root = IR.program();
 Node module1 = IR.script(IR.string("goog.provide('myModule');exports = 1;"));
 Node module2 = IR.script(IR.string("goog.require('myModule');exports = 2;"));
 root.addChildToFront(module1);
 root.addChildToFront(module2);
 processCommonJSModules.process(IR.nullLiteral(), root);
 Node myModule = IR.getprop(IR.getprop(IR.name("goog"), IR.string("require")),
 IR.string("myModule")).getChildAtIndex(1);
 assertEquals(Integer.valueOf(1), myModule.getValue());
 }
}