package com.google.javascript.jscomp;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link MakeDeclaredNamesUnique} class.
 */
public class MakeDeclaredNamesUniqueTest {
  @Test
  public void testMakeDeclaredNamesUnique() {
    String jsCode = "function foo(x) {\n" +
        "  var a = 1;\n" +
        "  var b = x;\n" +
        "  var c = a;\n" +
        "  var d = b;\n" +
        "  function bar(x) {\n" +
        "    var a = 1;\n" +
        "    var b = x;\n" +
        "  }\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIRKS);

    Node root = new TokenStream(jsCode).parseProgram();
    NodeTraversal traverser = new NodeTraversal(root, options, compiler);
    new MakeDeclaredNamesUnique().traverse(traverser, root);
    Node traversedCode = traverser.getTree();

    // Checking that no name conflicts occurred and that all names are unique
    List<String> expectedNames = Arrays.asList(
        "a0", "b0", "c0", "d0", "x0", "bar0");
    Predicate<Node> isVarDeclaration = new Predicate<Node>() {
      @Override
      public boolean apply(Node input) {
        return NodeUtil.isVarDeclaration(input);
      }
    };
    Function<Node, String> getName = new Function<Node, String>() {
      @Override
      public String apply(Node input) {
        return input.getString();
      }
    };
    List<String> actualNames = Lists.newArrayList();
    for (Node c = traversedCode.getFirstChild(); c != null; c = c.getNext()) {
      if (isVarDeclaration.apply(c)) {
        actualNames.add(getName.apply(c));
      }
    }
    assertEquals(expectedNames, actualNames);
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithFunctionInjection() {
    String jsCode = "function foo(x) {\n" +
        "  var a = 1;\n" +
        "  var b = x;\n" +
        "  var c = a;\n" +
        "  var d = b;\n" +
        "  function bar(x) {\n" +
        "    var a = 1;\n" +
        "    var b = x;\n" +
        "  }\n" +
        "}";

    String injectedCode = "function injectedFunction() {\n" +
        "  var a = 3;\n" +
        "  var b = 4;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIRKS);
    options.setGenerateErrors(true);
    options.setKeepNames(true);

    Node root = new TokenStream(jsCode).parseProgram();
    Node injectedRoot = new TokenStream(injectedCode).parseProgram();

    NodeTraversal traverser = new NodeTraversal(root, options, compiler);
    new MakeDeclaredNamesUnique().traverse(traverser, root);

    // Injecting the injected code into the original code
    Node injectedNode = traverser.getTree().getLastChild();
    Node injectedCodeNode = injectedNode.getFirstChild();

    Node injectedTraverser = new NodeTraversal(injectedCodeNode);
    new InlineRenamer(
        new Supplier<String>() {
          private int counter = 0;

          @Override
          public String get() {
            return String.valueOf(counter++);
          }
        },
        "_injected_",
        true).traverse(injectedTraverser, injectedCodeNode);

    NodeTraversal mergedTraverser = new NodeTraversal(traverser.getTree());
    mergedTraverser.pushCallback(traverser);
    mergedTraverser.traverse(injectedTraverser, injectedCodeNode);

    Node mergedCode = mergedTraverser.getTree();
    List<String> expectedNames = Arrays.asList(
        "a0", "b0", "c0", "d0", "x0", "foo0", "_injected_0", "bar0", "a1", "b1");
    List<String> actualNames = Lists.newArrayList();
    for (Node c = mergedCode.getFirstChild(); c != null; c = c.getNext()) {
      if (c.getType() == Token.NAME && !c.isProperty()) {
        actualNames.add(c.getString());
      }
    }
    assertEquals(expectedNames, actualNames);

    // Checking if errors were generated
    Iterator<Error> errors = compiler.getErrors().iterator();
    assertTrue(errors.hasNext());
    Error firstError = errors.next();
    String expectedErrorMessage = "ERROR: ";
    String actualErrorMessage = firstError.getMessage();
    assertTrue(actualErrorMessage.startsWith(expectedErrorMessage));
    int indexOfFirstErrorMessage = actualErrorMessage.indexOf(": ");
    int expectedFirstErrorLineNumber = 12;
    int actualFirstErrorLineNumber = Integer.parseInt(
        actualErrorMessage.substring(indexOfFirstErrorMessage + 2,
            indexOfFirstErrorMessage + 11));
    assertEquals(expectedFirstErrorLineNumber, actualFirstErrorLineNumber);
  }
}