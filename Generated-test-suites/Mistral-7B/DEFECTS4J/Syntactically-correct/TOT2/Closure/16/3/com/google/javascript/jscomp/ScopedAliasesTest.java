import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.code;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

 private Compiler compiler;
 private AbstractCompiler abstractCompiler;
 private PreprocessorSymbolTable preprocessorSymbolTable;
 private AliasTransformationHandler transformationHandler;

 @Before
 public void setUp() {
 compiler = new Compiler();
 abstractCompiler = compiler.getAbstractCompiler();
 preprocessorSymbolTable = new PreprocessorSymbolTable();
 transformationHandler = new AliasTransformationHandler();
 }

 @Test
 public void testBasicScopedAlias() {
 Preconditions.checkState(transformationHandler != null, "transformationHandler is null");

 String input = "goog.scope(function() {\n" +
         "  var dom = goog.dom;\n" +
         "  var DIV = dom.TagName.DIV;\n" +
         "  dom.createElement(DIV);\n" +
         "});";

 Node root = parseTree(input);
 ScopedAliases scopedAliases = new ScopedAliases(
     abstractCompiler, preprocessorSymbolTable, transformationHandler);
 scopedAliases.process(null, root);

 // Check for errors and warnings
 Traversal traversal = abstractCompiler.getTraversal();
 assertTrue(traversal.hasErrors());
 assertEquals(5, traversal.getErrors().size());

 // Check for errors related to goog.scope
 assertTrue(traversal.getErrors().contains(code(GOOG_SCOPE_USED_IMPROPERLY)));
 assertTrue(traversal.getErrors().contains(code(GOOG_SCOPE_HAS_BAD_PARAMETERS)));

 // Check for alias transformations
 AliasTransformation aliasTransformation = transformationHandler.getLastAliasTransformation();
 assertEquals("goog.dom.createElement", aliasTransformation.getAliasName());
 assertEquals("Element", aliasTransformation.getQualifiedName());
 }

 @Test
 public void testScopedAliasesWithMultipleAliases() {
 Preconditions.checkState(transformationHandler != null, "transformationHandler is null");

 String input = "goog.scope(function() {\n" +
         "  var dom = goog.dom;\n" +
         "  var DIV = dom.TagName.DIV;\n" +
         "  var myDom = goog.dom;\n" +
         "  var MY_DIV = myDom.TagName.DIV;\n" +
         "  dom.createElement(DIV);\n" +
         "  dom.createElement(MY_DIV);\n" +
         "});";

 Node root = parseTree(input);
 ScopedAliases scopedAliases = new ScopedAliases(
     abstractCompiler, preprocessorSymbolTable, transformationHandler);
 scopedAliases.process(null, root);

 // Check for errors and warnings
 Traversal traversal = abstractCompiler.getTraversal();
 assertTrue(traversal.hasErrors());
 assertEquals(5, traversal.getErrors().size());

 // Check for alias transformations
 List<AliasTransformation> aliasTransformations = transformationHandler.getAliasTransformations();
 assertEquals(2, aliasTransformations.size());

 AliasTransformation firstAliasTransformation = aliasTransformations.get(0);
 assertEquals("goog.dom.createElement", firstAliasTransformation.getAliasName());
 assertEquals("Element", firstAliasTransformation.getQualifiedName());

 AliasTransformation secondAliasTransformation = aliasTransformations.get(1);
 assertEquals("goog.dom.TagName.DIV", secondAliasTransformation.getAliasName());
 assertEquals("DIV", secondAliasTransformation.getQualifiedName());
 }

 @Test
 public void testScopedAliasWithRedefinition() {
 Preconditions.checkState(transformationHandler != null, "transformationHandler is null");

 String input = "goog.scope(function() {\n" +
         "  var dom = goog.dom;\n" +
         "  var DIV = dom.TagName.DIV;\n" +
         "  var DIV = 'div';\n" +
         "  dom.createElement(DIV);\n" +
         "});";

 Node root = parseTree(input);
 ScopedAliases scopedAliases = new ScopedAliases(
     abstractCompiler, preprocessorSymbolTable, transformationHandler);
 scopedAliases.process(null, root);

 // Check for errors and warnings
 Traversal traversal = abstractCompiler.getTraversal();
 assertTrue(traversal.hasErrors());
 assertEquals(6, traversal.getErrors().size());

 // Check for error related to alias redefinition
 assertTrue(traversal.getErrors().contains(code(GOOG_SCOPE_ALIAS_REDEFINED, "DIV")));
 }

 @Test
 public void testScopedAliasWithNonAliasLocalVariable() {
 Preconditions.checkState(transformationHandler != null, "transformationHandler is null");

 String input = "goog.scope(function() {\n" +
         "  var dom = goog.dom;\n" +
         "  var x = 10;\n" +
         "  var DIV = dom.TagName.DIV;\n" +
         "  dom.createElement(DIV);\n" +
         "});";

 Node root = parseTree(input);
 ScopedAliases scopedAliases = new ScopedAliases(
     abstractCompiler, preprocessorSymbolTable, transformationHandler);
 scopedAliases.process(null, root);

 // Check for errors and warnings
 Traversal traversal = abstractCompiler.getTraversal();
 assertTrue(traversal.hasErrors());
 assertEquals(6, traversal.getErrors().size());

 // Check for error related to non-alias local variable
 assertTrue(traversal.getErrors().contains(code(GOOG_SCOPE_NON_ALIAS_LOCAL, "x")));
 }

 @Test
 public void testScopedAliasWithReturnStatement() {
 Preconditions.checkState(transformationHandler != null, "transformationHandler is null");

 String input = "goog.scope(function() {\n" +
         "  var dom = goog.dom;\n" +
         "  var DIV = dom.TagName.DIV;\n" +
         "  return DIV;\n" +
         "});";

 Node root = parseTree(input);
 ScopedAliases scopedAliases = new ScopedAliases(
     abstractCompiler, preprocessorSymbolTable, transformationHandler);
 scopedAliases.process(null, root);

 // Check for errors and warnings
 Traversal traversal = abstractCompiler.getTraversal();
 assertTrue(traversal.hasErrors());
 assertEquals(6, traversal.getErrors().size());

 // Check for error related to use of return statement
 assertTrue(traversal.getErrors().contains(code(GOOG_SCOPE_USES_RETURN)));
 }

 @Test
 public void testScopedAliasWithThrowStatement() {
 Preconditions.checkState(transformationHandler != null, "transformationHandler is null");

 String input = "goog.scope(function() {\n" +
         "  var dom = goog.dom;\n" +
         "  var DIV = dom.TagName.DIV;\n" +
         "  throw new Error('Test error');\n" +
         "  dom.createElement(DIV);\n" +
         "});";

 Node root = parseTree(input);
 ScopedAliases scopedAliases = new ScopedAliases(
     abstractCompiler, preprocessorSymbolTable, transformationHandler);
 scopedAliases.process(null, root);

 // Check for errors and warnings
 Traversal traversal = abstractCompiler.getTraversal();
 assertTrue(traversal.hasErrors());
 assertEquals(6, traversal.getErrors().size());

 // Check for error related to use of throw statement
 assertTrue(traversal.getErrors().contains(code(GOOG_SCOPE_USES_THROW)));
 }

 private Node parseTree(String input) {
 Node root = new JsNode(new SourcePosition(), JsToken.SKIP_TO_EOL);
 SourcePosition position = root.getSourcePosition();
 position.setFilename("input.js");
 position.setLineno(1);
 position.setCharno(1);
 new RhinoJsParser(new ParseContext()).parse(input, root, null);
 return root;
 }
}