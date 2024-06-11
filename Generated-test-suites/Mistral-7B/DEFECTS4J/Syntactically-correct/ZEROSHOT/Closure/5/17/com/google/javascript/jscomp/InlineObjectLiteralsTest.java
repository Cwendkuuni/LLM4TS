import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Behavior;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    Behavior behavior = mock(Behavior.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    Scope scope = mock(Scope.class);
    Node root = Node.newBlock(ImmutableList.of());
    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);

    when(referenceCollection.getReferences(mock(Var.class))).thenReturn(ImmutableList.of());
    when(referenceCollection.isWellDefined()).thenReturn(true);
    when(referenceCollection.getInitializingReference()).thenReturn(mock(Reference.class));

    when(behavior.afterExitScope(mock(NodeTraversal.class), referenceCollection)).thenCallRealMethod();

    inlineObjectLiterals.process(null, root);

    verify(behavior).afterExitScope(mock(NodeTraversal.class), referenceCollection);
  }

  @Test
  public void testInlineObjectLiteralWithSingleProperty() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    Behavior behavior = mock(Behavior.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    Scope scope = mock(Scope.class);
    Node root = Node.newBlock(ImmutableList.of());
    Node objNode = Node.newObjectLit("{}");
    Var var = mock(Var.class);
    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);

    when(referenceCollection.getReferences(var)).thenReturn(ImmutableList.of(
        new Reference(Node.newName("obj"), objNode),
        new Reference(Node.newName("prop"), Node.newString("prop"))));
    when(referenceCollection.isWellDefined()).thenReturn(true);
    when(referenceCollection.getInitializingReference()).thenReturn(null);

    when(behavior.afterExitScope(mock(NodeTraversal.class), referenceCollection)).thenCallRealMethod();

    inlineObjectLiterals.process(null, root);

    verify(behavior).afterExitScope(mock(NodeTraversal.class), referenceCollection);

    Node expected = Node.newAssign(
        Node.newName("prop"),
        Node.newString("prop"));

    assertEquals(expected, objNode.getFirstChild());
  }

  @Test
  public void testInlineObjectLiteralWithMultipleProperties() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    Behavior behavior = mock(Behavior.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    Scope scope = mock(Scope.class);
    Node root = Node.newBlock(ImmutableList.of());
    Node objNode = Node.newObjectLit("{prop1: 1, prop2: 2}");
    Var var = mock(Var.class);
    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);

    when(referenceCollection.getReferences(var)).thenReturn(ImmutableList.of(
        new Reference(Node.newName("obj"), objNode),
        new Reference(Node.newName("prop1"), Node.newString("prop1")),
        new Reference(Node.newName("prop2"), Node.newString("prop2"))));
    when(referenceCollection.isWellDefined()).thenReturn(true);
    when(referenceCollection.getInitializingReference()).thenReturn(null);

    when(behavior.afterExitScope(mock(NodeTraversal.class), referenceCollection)).thenCallRealMethod();

    inlineObjectLiterals.process(null, root);

    verify(behavior).afterExitScope(mock(NodeTraversal.class), referenceCollection);

    ImmutableMap<String, Node> expectedInitialValues = ImmutableMap.of(
        "prop1", Node.newNumber(1),
        "prop2", Node.newNumber(2)
    );

    for (int i = 0; i < objNode.getNumChildren(); i++) {
      Node keyNode = objNode.getChildAtIndex(i);
      Node valueNode = objNode.getChildAtIndex(i + 1);
      Node expectedValueNode = expectedInitialValues.get(keyNode.getString());

      if (valueNode == null) {
        continue;
      }

      assertEquals(expectedValueNode, valueNode);
    }
  }

  @Test
  public void testInlineObjectLiteralWithSelfReferentialAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    Behavior behavior = mock(Behavior.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    Scope scope = mock(Scope.class);
    Node root = Node.newBlock(ImmutableList.of());
    Node objNode = Node.newObjectLit(
        "{\n" +
        "  a: 1,\n" +
        "  b: function() {\n" +
        "    return this.a;\n" +
        "  }\n" +
        "}");
    Var var = mock(Var.class);
    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);

    when(referenceCollection.getReferences(var)).thenReturn(ImmutableList.of(
        new Reference(Node.newName("obj"), objNode),
        new Reference(Node.newName("a"), Node.newNumber(1))));
    when(referenceCollection.isWellDefined()).thenReturn(false);
    when(referenceCollection.getInitializingReference()).thenReturn(null);

    when(behavior.afterExitScope(mock(NodeTraversal.class), referenceCollection)).thenCallRealMethod();

    inlineObjectLiterals.process(null, root);

    verify(behavior, never()).afterExitScope(mock(NodeTraversal.class), referenceCollection);
  }

  @Test
  public void testInlineObjectLiteralWithCircularReference() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    Behavior behavior = mock(Behavior.class);

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    Scope scope = mock(Scope.class);
    Node root = Node.newBlock(ImmutableList.of());
    Node obj1Node = Node.newObjectLit("{a: {}}");
    Node obj2Node = Node.newObjectLit("{{}}");
    Var var1 = mock(Var.class);
    Var var2 = mock(Var.class);
    ReferenceCollection referenceCollection = mock(ReferenceCollection.class);

    when(referenceCollection.getReferences(var1)).thenReturn(ImmutableList.of(
        new Reference(Node.newName("obj1"), obj1Node),
        new Reference(Node.newName("a"), obj2Node)
    ));
    when(referenceCollection.getReferences(var2)).thenReturn(ImmutableList.of(
        new Reference(Node.newName("obj2"), obj2Node),
        new Reference(Node.newName("a"), obj1Node)
    ));
    when(referenceCollection.isWellDefined()).thenReturn(false);
    when(referenceCollection.getInitializingReference()).thenReturn(null);

    when(behavior.afterExitScope(mock(NodeTraversal.class), referenceCollection)).thenCallRealMethod();

    inlineObjectLiterals.process(null, root);

    verify(behavior, never()).afterExitScope(mock(NodeTraversal.class), referenceCollection);
  }

}