import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Supplier;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.MapDifference;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test class for FunctionToBlockMutator.
 */
public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;
  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setup() {
    this.compiler = new AbstractCompiler() {
      @Override
      public Node getUniqueNameIdSupplier() {
        return new Supplier<String>() {
          int counter = 0;

          @Override
          public String get() {
            return "test_" + Integer.toString(counter++);
          }
        };
      }
    };

    this.safeNameIdSupplier = this.compiler.getUniqueNameIdSupplier();
    this.functionToBlockMutator = new FunctionToBlockMutator(
        this.compiler, this.safeNameIdSupplier);
  }

  /**
   * Test method for mutate.
   */
  @Test
  public void testMutate() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.createFunction(fnName, null, null, null, null);
    Node callNode = NodeFactory.createCall(NodeFactory.createName("callFunction"),
        Lists.newArrayList(fnNode));
    String resultName = "testResult";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node result = this.functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(result);
    // Add more asserts here as needed to check the result.
  }

  /**
   * Test method for makeLocalNamesUnique.
   */
  @Test
  public void testMakeLocalNamesUnique() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.createFunction(fnName, null, null, null, null);

    this.functionToBlockMutator.makeLocalNamesUnique(fnNode, false);

    NodeTraversal.traverse(this.compiler, fnNode, new NodeVisitor() {
      private int counter = 0;

      @Override
      public void visitVar(Node node) {
        String name = node.getFirstChild().getString();
        assertEquals("test_" + Integer.toString(counter++), name);
      }
    });
  }

  /**
   * Test method for aliasAndInlineArguments.
   */
  @Test
  public void testAliasAndInlineArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.createFunction(fnName, null, null, null, null);

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeFactory.createNumber(1));
    argMap.put("arg2", NodeFactory.createString("testString"));

    Set<String> namesToAlias = Sets.newHashSet("arg1");

    Node result = this.functionToBlockMutator.aliasAndInlineArguments(
        fnNode, argMap, namesToAlias);

    assertNotNull(result);
    // Add more asserts here as needed to check the result.
  }

  /**
   * Test method for replaceReturns.
   */
  @Test
  public void testReplaceReturns() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.createFunction(fnName, null, null, null, null);

    String resultName = "testResult";
    String labelName = "testLabel";
    boolean resultMustBeSet = true;

    Node result = this.functionToBlockMutator.replaceReturns(
        fnNode, resultName, labelName, resultMustBeSet);

    assertNotNull(result);
    // Add more asserts here as needed to check the result.
  }

  /**
   * Test method for fixUnitializedVarDeclarations.
   */
  @Test
  public void testFixUnitializedVarDeclarations() {
    String fnName = "testFunction";
    Node fnNode = NodeFactory.createFunction(fnName, null, null, null, null);

    Node varNode = NodeFactory.createVar("testVar");

    fnNode.addChildToBack(varNode);

    this.functionToBlockMutator.fixUnitializedVarDeclarations(fnNode);

    assertEquals(1, fnNode.getChildCount());

    Node child = fnNode.getFirstChild();
    assertNotNull(child.getFirstChild());
    assertEquals("undefined", child.getFirstChild().toString());
  }
}