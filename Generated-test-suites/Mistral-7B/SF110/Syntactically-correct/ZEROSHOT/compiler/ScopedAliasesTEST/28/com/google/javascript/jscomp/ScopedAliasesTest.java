import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private CompilerOptions options;
  private AliasTransformationHandler handler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    handler = new AliasTransformationHandler() {};
    scopedAliases = new ScopedAliases(null, null, handler);
  }

  @Test
  public void testSimpleScopedAlias() {
    Node root = new Node("goog.scope(function(){\n" +
        "  var x = goog.x;\n" +
        "});");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.traverse(root, aliasDefinitionsInOrder, aliasUsages, scopeCalls);

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    AliasUsage aliasUsage = aliasUsages.get(0);
    assertEquals("x", aliasUsage.getName());

    Node aliasNode = aliasUsage.getNode();
    assertEquals("goog.x", aliasNode.getQualifiedName());
  }

  @Test
  public void testScopedAliasWithTypeNode() {
    Node root = new Node("goog.scope(function(){\n" +
        "  /**\n" +
        "   * @type {String}\n" +
        "   */\n" +
        "  var x = goog.x;\n" +
        "});");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.traverse(root, aliasDefinitionsInOrder, aliasUsages, scopeCalls);

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, aliasUsages.size());
    assertEquals(1, scopeCalls.size());

    AliasUsage aliasUsage = aliasUsages.get(0);
    assertEquals("x", aliasUsage.getName());

    Node aliasNode = aliasUsage.getNode();
    assertEquals("goog.x", aliasNode.getQualifiedName());

    Node typeNode = aliasNode.getParent().getFirstChild();
    assertEquals("String", typeNode.getString());
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithExtraParameters() {
    Node root = new Node("goog.scope(function(x, y) {\n" +
        "});");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.traverse(root, aliasDefinitionsInOrder, aliasUsages, scopeCalls);

    fail("Expected GOOG_SCOPE_HAS_BAD_PARAMETERS exception.");
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithoutParameter() {
    Node root = new Node("goog.scope(function() {\n" +
        "});");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.traverse(root, aliasDefinitionsInOrder, aliasUsages, scopeCalls);

    fail("Expected GOOG_SCOPE_USED_IMPROPERLY exception.");
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithExtraStatement() {
    Node root = new Node("goog.scope(function() {\n" +
        "  var x = goog.x;\n" +
        "  return 42;\n" +
        "});");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.traverse(root, aliasDefinitionsInOrder, aliasUsages, scopeCalls);

    fail("Expected GOOG_SCOPE_USES_RETURN exception.");
  }

  @Test(expected = AssertionError.class)
  public void testScopeCallWithThrowStatement() {
    Node root = new Node("goog.scope(function() {\n" +
        "  throw new Error('test');\n" +
        "});");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.traverse(root, aliasDefinitionsInOrder, aliasUsages, scopeCalls);

    fail("Expected GOOG_SCOPE_USES_THROW exception.");
  }

  @Test(expected = AssertionError.class)
  public void testAliasRedefined() {
    Node root = new Node("goog.scope(function(){\n" +
        "  var x = goog.x;\n" +
        "  var x = goog.y;\n" +
        "});");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.traverse(root, aliasDefinitionsInOrder, aliasUsages, scopeCalls);

    fail("Expected GOOG_SCOPE_ALIAS_REDEFINED exception.");
  }

  @Test(expected = AssertionError.class)
  public void testNonAliasLocalVariable() {
    Node root = new Node("goog.scope(function(){\n" +
        "  var x = 42;\n" +
        "});");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.traverse(root, aliasDefinitionsInOrder, aliasUsages, scopeCalls);

    fail("Expected GOOG_SCOPE_NON_ALIAS_LOCAL exception.");
  }

  @Test(expected = AssertionError.class)
  public void testAliasUsageAfterScopeCall() {
    Node root = new Node("goog.scope(function(){\n" +
        "  goog.scope(function(){\n" +
        "    var x = goog.x;\n" +
        "  });\n" +
        "});");

    List<Node> aliasDefinitionsInOrder = Lists.newArrayList();
    List<AliasUsage> aliasUsages = Lists.newArrayList();
    List<Node> scopeCalls = Lists.newArrayList();

    scopedAliases.traverse(root, aliasDefinitionsInOrder, aliasUsages, scopeCalls);

    fail("Expected GOOG_SCOPE_USED_IMPROPERLY exception.");
  }
}