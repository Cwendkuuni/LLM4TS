import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation.Handler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

public class ScopedAliasesTest {

 private AbstractCompiler compiler;
 private PreprocessorSymbolTable preprocessorSymbolTable;
 private Handler transformationHandler;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 preprocessorSymbolTable = new PreprocessorSymbolTable();
 transformationHandler = new Handler(compiler, preprocessorSymbolTable);
 }

 @Test
 public void testSimpleAliasing() {
 Node root = NodeUtil.parse("goog.scope(function() {\n" +
 "var dom = goog.dom;\n" +
 "var DIV = dom.TagName.DIV;\n" +
 "dom.createElement(DIV);\n" +
 "});");

 ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
 transformationHandler);
 scopedAliases.process(null, root);
 assertNull(root.getChildAtIndex(0).getNext());
 assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
 root.toString());
 }

 @Test
 public void testMultipleAliases() {
 Node root = NodeUtil.parse("goog.scope(function() {\n" +
 "var dom = goog.dom;\n" +
 "var DIV = dom.TagName.DIV;\n" +
 "var BODY = document.body;\n" +
 "dom.createElement(DIV);\n" +
 "document.body = BODY;\n" +
 "});");

 ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
 transformationHandler);
 scopedAliases.process(null, root);
 assertNull(root.getChildAtIndex(0).getNext());
 assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
 root.toString());
 assertEquals("document.body", root.getChildAtIndex(2).toString());
 }

 @Test
 public void testErrorCases() {
 Node root = NodeUtil.parse("goog.scope(function() {\n" +
 "var dom = goog.dom;\n" +
 "var DIV = dom.TagName.DIV;\n" +
 "dom.createElement(DIV);\n" +
 "goog.scope(function() {\n" +
 "var dom2 = goog.dom;\n" +
 "var DIV2 = dom2.TagName.DIV;\n" +
 "dom2.createElement(DIV2);\n" +
 "});\n" +
 "});");

 ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
 transformationHandler);
 List<String> errors = Lists.newArrayList();
 try {
 scopedAliases.process(null, root);
 } catch (Exception e) {
 errors.add(e.getMessage());
 }

 assertTrue(errors.size() > 0);
 assertEquals(GOOG_SCOPE_USED_IMPROPERLY.getMessage(), errors.get(0));
 }

 @Test
 public void testAliasRedefinition() {
 Node root = NodeUtil.parse("goog.scope(function() {\n" +
 "var dom = goog.dom;\n" +
 "var DIV = dom.TagName.DIV;\n" +
 "goog.scope(function() {\n" +
 "var DIV = goog.dom.ElementType.DIV;\n" +
 "});\n" +
 "dom.createElement(DIV);\n" +
 "});");
 ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
 transformationHandler);
 List<String> errors = Lists.newArrayList();
 try {
 scopedAliases.process(null, root);
 } catch (Exception e) {
 errors.add(e.getMessage());
 }

 assertTrue(errors.size() > 0);
 assertEquals(GOOG_SCOPE_ALIAS_REDEFINED.getMessage(), errors.get(0));
 }
}