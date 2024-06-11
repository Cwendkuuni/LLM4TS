import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = new Scope();

    Node externs = Node.newBlock("externs");
    Node root = Node.newBlock("root");

    inlineObjectLiterals.process(externs, root);

    NodeTraversal traversal = new NodeTraversal(root, null);
    inlineObjectLiterals.new InliningBehavior().afterExitScope(traversal, null);

    // Test that a simple object literal is correctly inlined.
    Node objectLiteral = root.getChildAtIndex(0);
    Node objectLiteralAssign = objectLiteral.getChildAtIndex(0);
    assertEquals("JSCompiler_object_inline_x_0_", objectLiteralAssign.getFirstChild().getString());
    assertEquals("JSCompiler_object_inline_y_0_", objectLiteralAssign.getChildAtIndex(1).getString());
    assertEquals("JSCompiler_object_inline_z_0_", objectLiteralAssign.getChildAtIndex(2).getString());

    // Test that a complex object literal is correctly inlined.
    Node complexObjectLiteral = root.getChildAtIndex(1);
    Node complexObjectLiteralAssign = complexObjectLiteral.getChildAtIndex(0);
    assertEquals("JSCompiler_object_inline_a_1_", complexObjectLiteralAssign.getFirstChild().getString());
    Node complexObjectLiteralGetProp = complexObjectLiteralAssign.getChildAtIndex(1);
    assertEquals("JSCompiler_object_inline_b_1_", complexObjectLiteralGetProp.getChildAtIndex(1).getString());
    assertEquals("JSCompiler_object_inline_c_1_", complexObjectLiteralGetProp.getChildAtIndex(3).getString());

    // Test that an object literal with self-referential assignment is not inlined.
    Node selfReferentialObjectLiteral = root.getChildAtIndex(2);
    assertEquals(null, selfReferentialObjectLiteral.getFirstChild());

    // Test that a var that has already been inlined is skipped.
    Node alreadyInlinedVar = root.getChildAtIndex(3);
    assertEquals("JSCompiler_object_inline_x_3_", alreadyInlinedVar.getFirstChild().getString());

    // Test that a var that is used in a full reference is skipped.
    Node usedVar = root.getChildAtIndex(4);
    assertEquals("x", usedVar.getFirstChild().getString());
  }
}