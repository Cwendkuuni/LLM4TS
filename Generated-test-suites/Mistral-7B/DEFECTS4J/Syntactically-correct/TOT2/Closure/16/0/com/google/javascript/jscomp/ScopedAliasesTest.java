import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

 @Mock private AbstractCompiler compiler;
 @Mock private PreprocessorSymbolTable preprocessorSymbolTable;
 @Mock private CompilerOptions options;
 @Mock private AliasTransformationHandler transformationHandler;

 private HotSwapCompilerPass scopedAliases;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
 }

 @Test
 public void testValidScopedAliases() {
 Node rootNode = NodeUtil.parseString("goog.scope(function() {" +
         "var x = goog.x; goog.dom.createElement('DIV');" +
         "});");

 scopedAliases.process(null, rootNode);

 // Check that the alias 'x' was replaced with 'goog.x'
 assertEquals("goog.x", rootNode.getChildAtIndex(0).getQualifiedName());

 // Check that 'goog.dom.createElement' was not changed
 assertEquals("goog.dom.createElement",
     rootNode.getChildAtIndex(1).getChildAtIndex(0).getQualifiedName());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidScopingMethodCall() {
 Node rootNode = NodeUtil.parseString("goog.scope function() {" +
         "var x = goog.x; goog.scope function() {};" +
         "}");

 scopedAliases.process(null, rootNode);
 }

 @Test(expected = AssertionError.class)
 public void testScopingMethodCallWithParameters() {
 Node rootNode = NodeUtil.parseString("goog.scope(function(x) {" +
         "var y = goog.x; goog.dom.createElement('DIV');" +
         "})");

 scopedAliases.process(null, rootNode);
 }

 @Test(expected = AssertionError.class)
 public void testScopingMethodCallWithReturn() {
 Node rootNode = NodeUtil.parseString("goog.scope(function() {" +
         "return goog.x; goog.dom.createElement('DIV');" +
         "})");

 scopedAliases.process(null, rootNode);
 }

 @Test(expected = AssertionError.class)
 public void testScopingMethodCallWithThis() {
 Node rootNode = NodeUtil.parseString("goog.scope(function() {" +
         "this.x = goog.x; goog.dom.createElement('DIV');" +
         "})");

 scopedAliases.process(null, rootNode);
 }

 @Test(expected = AssertionError.class)
 public void testScopingMethodCallWithThrow() {
 Node rootNode = NodeUtil.parseString("goog.scope(function() {" +
         "throw goog.x; goog.dom.createElement('DIV');" +
         "})");

 scopedAliases.process(null, rootNode);
 }

 @Test
 public void testValidScopedAliasWithType() {
 Node rootNode = NodeUtil.parseString("goog.scope(function() {" +
         "var x: number = goog.x; goog.dom.createElement('DIV');" +
         "})");

 scopedAliases.process(null, rootNode);

 // Check that the alias 'x' was replaced with 'goog.x'
 assertEquals("goog.x", rootNode.getChildAtIndex(0).getChildAtIndex(0).getFirstChild().getString());
 // Check that the type node was not changed
 assertEquals("number", rootNode.getChildAtIndex(0).getChildAtIndex(1).getQualifiedName());
 }

 @Test
 public void testAliasRedefined() {
 Node rootNode = NodeUtil.parseString("goog.scope(function() {" +
         "var x = goog.x; var x = 'foo';" +
         "})");

 try {
 scopedAliases.process(null, rootNode);
 fail();
 } catch (AssertionError e) {
 // Expected error
 assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", e.getMessage());
 }
 }

 @Test
 public void testAliasWithNonAliasLocal() {
 Node rootNode = NodeUtil.parseString("goog.scope(function() {" +
         "var x = goog.x; var y = x; goog.dom.createElement('DIV');" +
         "})");

 try {
 scopedAliases.process(null, rootNode);
 fail();
 } catch (AssertionError e) {
 // Expected error
 assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", e.getMessage());
 }
 }

 @Test
 public void testAliasShadowingNamespace() {
 Node rootNode = NodeUtil.parseString("goog.scope(function() {" +
         "var goog = {dom: {createElement: function() {}}};" +
         "var x = goog.x; goog.dom.createElement('DIV');" +
         "})");

 scopedAliases.process(null, rootNode);

 // Check that the alias 'x' was replaced with 'goog.x'
 assertEquals("goog.x", rootNode.getChildAtIndex(0).getQualifiedName());
 // Check that 'goog.dom.createElement' was not changed
 assertEquals("goog.dom.createElement",
     rootNode.getChildAtIndex(1).getChildAtIndex(0).getQualifiedName());
}

 @Test
 public void testAliasWithLocalShadow() {
 Node rootNode = NodeUtil.parseString("goog.scope(function() {" +
         "var goog = {x: 0}; var x = goog.x; goog.dom.createElement('DIV');" +
         "})");

 scopedAliases.process(null, rootNode);

 // Check that the alias 'x' was replaced with 'goog.x'
 assertEquals("goog.x", rootNode.getChildAtIndex(0).getQualifiedName());
 // Check that 'goog' was renamed to 'goog_1'
 assertEquals("goog_1", rootNode.getChildAtIndex(0).getChildAtIndex(0).getQualifiedName());
}
}