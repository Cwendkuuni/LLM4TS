import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.CompilerOption;
import com.google.javascript.jscomp.CompilerOptions.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformationHandler() {
      @Nullable
      @Override
      public AliasTransformation getAliasTransformation(
          SourcePosition pos, String name, String replacement) {
        return new AliasTransformation(pos, name, replacement);
      }
    });

    AbstractCompiler compiler = new AbstractCompiler() {};
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, options.getAliasTransformationHandler());

    // Test input code
    Node root = Node.parse("goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});");

    // Act: Traverse and apply aliases
    scopedAliases.process(null, root);

    // Assert: No errors were reported
    assertFalse(compiler.hasErrors());

    // Assert: The correct aliases were applied
    Node expected = Node.parse("goog.dom.createElement(goog.dom.TagName.DIV);");
    Node actual = root;
    for (int i = 0; i < root.getChildCount(); i++) {
      if (NodeUtil.isBlock(actual)) {
        actual = actual.getLastChild();
      }
      Node expectedChild = expected.getChildAtIndex(i);
      Node actualChild = actual.getChildAtIndex(i);
      assertEquals(expectedChild, actualChild);
    }
  }
}