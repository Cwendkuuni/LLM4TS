package org.junit.test; // Replace with your test package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliasesTraversal() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = new Node("goog.scope(function() { " +
        "var x = goog.x; " +
        "goog.scope('var y = goog.y;'); " +
        "goog.scope('goog.foo = goog.bar;'); " +
        "var z = goog.z; " +
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();

    // Check number of alias usages, definitions, and scope calls.
    assertEquals(3, aliasUsages.size());
    assertEquals(3, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());

    // Check alias usages.
    AliasUsage xUsage = aliasUsages.get(0);
    AliasUsage yUsage = aliasUsages.get(1);
    AliasUsage zUsage = aliasUsages.get(2);

    // Check x usage is an AliasedNode.
    assertTrue(xUsage instanceof ScopedAliases.AliasedNode);
    ScopedAliases.AliasedNode xAliasedNode = (ScopedAliases.AliasedNode) xUsage;
    assertEquals("goog.x", xAliasedNode.aliasReference.getString());
    assertEquals("x", xAliasedNode.aliasDefinition.getString());

    // Check y usage is an AliasedNode.
    assertTrue(yUsage instanceof ScopedAliases.AliasedNode);
    ScopedAliases.AliasedNode yAliasedNode = (ScopedAliases.AliasedNode) yUsage;
    assertEquals("goog.y", yAliasedNode.aliasReference.getString());
    assertEquals("y", yAliasedNode.aliasDefinition.getString());

    // Check z usage is an AliasedTypeNode.
    assertTrue(zUsage instanceof ScopedAliases.AliasedTypeNode);
    ScopedAliases.AliasedTypeNode zAliasedTypeNode = (ScopedAliases.AliasedTypeNode) zUsage;
    assertEquals("goog.z", zAliasedTypeNode.typeReference.getString());
    assertEquals("z", zAliasedTypeNode.aliasName);
    assertEquals("goog.z", zAliasedTypeNode.aliasDefinition.getQualifiedName());

    // Check alias definitions.
    assertEquals("goog.x", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("x", aliasDefinitionsInOrder.get(1).getString());
    assertEquals("goog.foo", aliasDefinitionsInOrder.get(2).getString());

    // Check scope call.
    assertEquals("goog.scope", scopeCalls.get(0).getString());

    // Check no errors reported.
    assertFalse(traversal.hasErrors());
  }

  @Test
  public void testScopedAliasesErrors() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = new Node("goog.scope(function() { " +
        "var x = this; " +
        "goog.scope('function() { return ''; }'); " +
        "goog.scope('goog.scope'); " +
        "return; " +
        "});");

    scopedAliases.process(null, root);

    Traversal traversal = scopedAliases.getTraversal();

    // Check errors reported.
    assertTrue(traversal.hasErrors());
    List<Node> errors = traversal.getErrors();
    assertEquals(4, errors.size());

    // Check first error is GOOG_SCOPE_REFERENCES_THIS.
    DiagnosticType firstErrorType = errors.get(0).getType();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", firstErrorType.getName());
    Node firstErrorNode = errors.get(0).getNode();
    assertEquals("this", firstErrorNode.getString());

    // Check second error is GOOG_SCOPE_HAS_BAD_PARAMETERS.
    DiagnosticType secondErrorType = errors.get(1).getType();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", secondErrorType.getName());
    Node secondErrorNode = errors.get(1).getNode();
    assertEquals("function() { return ''; }", secondErrorNode.getString());

    // Check third error is GOOG_SCOPE_USED_IMPROPERLY.
    DiagnosticType thirdErrorType = errors.get(2).getType();
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", thirdErrorType.getName());
    Node thirdErrorNode = errors.get(2).getNode();
    assertEquals("goog.scope", thirdErrorNode.getString());

    // Check fourth error is GOOG_SCOPE_HAS_BAD_PARAMETERS.
    DiagnosticType fourthErrorType = errors.get(3).getType();
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", fourthErrorType.getName());
    Node fourthErrorNode = errors.get(3).getNode();
    assertEquals("goog.scope", fourthErrorNode.getString());
  }

  @Test
  public void testScopedAliasesTraversalWithJSDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = new Node("/** @suppress {checkTypes} */\n" +
        "goog.scope(function() { " +
        "  /** @type {number} */ var x = goog.x; " +
        "  /** @type {string} */ var y = goog.y; " +
        "  /** @type {!MyType} */ var z = goog.z; " +
        "});");

    Node myTypeNode = new Node("function MyType() {}\n");

    Node rootWithMyType = new Node("goog.scope(function() { " +
        "  /** @type {number} */ var x = goog.x; " +
        "  /** @type {string} */ var y = goog.y; " +
        "  /** @type {!MyType} */ var z = goog.z; " +
        "  " + myTypeNode.getString() +
        "});");

    scopedAliases.process(null, rootWithMyType);

    Traversal traversal = scopedAliases.getTraversal();
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = traversal.getScopeCalls();
    List<Node> errors = traversal.getErrors();

    // Check no errors reported.
    assertFalse(traversal.hasErrors());

    // Check alias usages.
    AliasUsage xUsage = aliasUsages.get(0);
    AliasUsage yUsage = aliasUsages.get(1);
    AliasUsage zUsage = aliasUsages.get(2);

    // Check x usage is an AliasedNode.
    assertTrue(xUsage instanceof ScopedAliases.AliasedNode);
    ScopedAliases.AliasedNode xAliasedNode = (ScopedAliases.AliasedNode) xUsage;
    assertEquals("goog.x", xAliasedNode.aliasReference.getString());
    assertEquals("x", xAliasedNode.aliasDefinition.getString());

    // Check y usage is an AliasedNode.
    assertTrue(yUsage instanceof ScopedAliases.AliasedNode);
    ScopedAliases.AliasedNode yAliasedNode = (ScopedAliases.AliasedNode) yUsage;
    assertEquals("goog.y", yAliasedNode.aliasReference.getString());
    assertEquals("y", yAliasedNode.aliasDefinition.getString());

    // Check z usage is an AliasedTypeNode.
    assertTrue(zUsage instanceof ScopedAliases.AliasedTypeNode);
    ScopedAliases.AliasedTypeNode zAliasedTypeNode = (ScopedAliases.AliasedTypeNode) zUsage;
    assertEquals("goog.z", zAliasedTypeNode.typeReference.getString());
    assertEquals("z", zAliasedTypeNode.aliasName);
    assertEquals("goog.z", zAliasedTypeNode.aliasDefinition.getQualifiedName());

    // Check alias definitions.
    assertEquals("goog.x", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("x", aliasDefinitionsInOrder.get(1).getString());
    assertEquals("goog.y", aliasDefinitionsInOrder.get(2).getString());
    assertEquals("goog.z", aliasDefinitionsInOrder.get(3).getString());

    // Check scope call.
    assertEquals("goog.scope", scopeCalls.get(0).getString());

    // Check JSDocInfo for x is fixed.
    assertEquals(1, aliasDefinitionsInOrder.get(0).getNumChildren());
    Node xAliasDefinitionJSDocInfo = aliasDefinitionsInOrder.get(0).getChildAtIndex(0);
    assertEquals("number", xAliasDefinitionJSDocInfo.getString());

    // Check JSDocInfo for y is fixed.
    assertEquals(1, aliasDefinitionsInOrder.get(1).getNumChildren());
    Node yAliasDefinitionJSDocInfo = aliasDefinitionsInOrder.get(1).getChildAtIndex(0);
    assertEquals("string", yAliasDefinitionJSDocInfo.getString());

    // Check JSDocInfo for z is fixed.
    assertEquals(1, aliasDefinitionsInOrder.get(2).getNumChildren());
    Node zAliasDefinitionJSDocInfo = aliasDefinitionsInOrder.get(2).getChildAtIndex(0);
    assertEquals("!MyType", zAliasDefinitionJSDocInfo.getString());

    // Check no errors reported.
    assertFalse(traversal.hasErrors());
  }
}