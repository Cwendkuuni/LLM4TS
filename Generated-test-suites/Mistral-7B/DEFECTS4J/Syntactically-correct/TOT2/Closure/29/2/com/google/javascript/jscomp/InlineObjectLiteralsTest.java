import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;

public class InlineObjectLiteralsTest {

 private static Compiler compiler;
 private static CompilerOptions options;

 @BeforeClass
 public static void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testInlineObjectLiterals() {
 String input = "function test() {" +
 "  var obj = {" +
 "    a: 1," +
 "    b: {'c': 2}" +
 "  };" +
 "  var x = obj.a;" +
 "  var y = obj.b.c;" +
 "  return x + y;" +
 "}";

 String expectedOutput = "function test() {" +
 "  var a = 1;" +
 "  var c = 2;" +
 "  var x = a;" +
 "  var y = c;" +
 "  return x + y;" +
 "}";

 ReferenceCollectingCallback referenceCollectingCallback =
 new ReferenceCollectingCallback(compiler, new InliningBehavior());

 compiler.process(input, options);
 String output = compiler.getSource();
 assertEquals(expectedOutput, output);

 // Check that all variables were inlined correctly
 List<Reference> references = referenceCollectingCallback.getReferences();
 for (Reference reference : references) {
 if (reference.getAssignedValue() != null) {
 Node node = reference.getAssignedValue();
 Preconditions.checkState(node.isObjectLit(), "Unexpected object literal: " + node);
 if (node.getNumChildren() > 0) {
 fail("Unexpected non-empty object literal: " + node);
 }
 }
 }
 }

 @Test
 public void testInlineObjectLiteralsWithInitialValue() {
 String input = "function test() {" +
 "  var obj = {" +
 "    a:" +
 "      function() {" +
 "        return 'hello';" +
 "      }" +
 "  };" +
 "  var result = obj.a();" +
 "  return result;" +
 "}";

 String expectedOutput = "function test() {" +
 "  var a = function() {" +
 "    return 'hello';" +
 "  };" +
 "  var result = a();" +
 "  return result;" +
 "}";

 ReferenceCollectingCallback referenceCollectingCallback =
 new ReferenceCollectingCallback(compiler, new InliningBehavior());

 compiler.process(input, options);
 String output = compiler.getSource();
 assertEquals(expectedOutput, output);

 // Check that the inlineObjectLiterals pass did not touch the function
 Node functionNode = NodeUtil.findFunctionNodeByName("test");
 assertNotNull(functionNode);
 assertEquals(NodeUtil.NEW_FUNCTION_DECLARATION, functionNode.getType());
 }

 @Test
 public void testInlineObjectLiteralsWithSelfReference() {
 String input = "function test() {" +
 "  var obj = {" +
 "    a: {" +
 "      b: obj" +
 "    }" +
 "  };" +
 "  return obj.a.b;" +
 "}";

 // This test will fail as the current implementation of InlineObjectLiterals
 // does not support self-referential object literals
 // String expectedOutput = "function test() {" +
 // "  var a = {" +
 // "    b: Function.prototype.bind.call(this, 'a')" +
 // "  };" +
 // "  return a.b;" +
 // "}";

 ReferenceCollectingCallback referenceCollectingCallback =
 new ReferenceCollectingCallback(compiler, new InliningBehavior());

 compiler.process(input, options);
 String output = compiler.getSource();
 fail("Expected failure: self-referential object literal not supported");
 }

 @Test
 public void testDelegateToAnotherVariable() {
 String input = "function test() {" +
 "  var obj = {" +
 "    a: 1," +
 "    b: {" +
 "      c: 2" +
 "    }" +
 "  };" +
 "  var delegate = obj.b;" +
 "  delete obj.b;" +
 "  obj.b = delegate;" +
 "  return obj.b.c;" +
 "}";

 String expectedOutput = "function test() {" +
 "  var a = 1;" +
 "  var c = 2;" +
 "  var delegate = c;" +
 "  delete a;" +
 "  a = delegate;" +
 "  return a;" +
 "}";

 ReferenceCollectingCallback referenceCollectingCallback =
 new ReferenceCollectingCallback(compiler, new InliningBehavior());

 compiler.process(input, options);
 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEmptyObjectLiteral() {
 String input = "function test() {" +
 "  var obj = {}";
 String expectedOutput = "function test() {" +
 "  var obj = {}";
 ReferenceCollectingCallback referenceCollectingCallback =
 new ReferenceCollectingCallback(compiler, new InliningBehavior());
 compiler.process(input, options);
 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInlineObjectLiteralsWithArray() {
 String input = "function test() {" +
 "  var obj = {" +
 "    a: [1,2,3]," +
 "    b: {}" +
 "  };" +
 "  var x = obj.a[0];" +
 "  var y = obj.b.c;" +
 "  return x + y;" +
 "}";

 String expectedOutput = "function test() {" +
 "  var a = [1,2,3];" +
 "  var b = {};" +
 "  var c = 2;" +
 "  var x = a[0];" +
 "  var y = c;" +
 "  return x + y;" +
 "}";

 ReferenceCollectingCallback referenceCollectingCallback =
 new ReferenceCollectingCallback(compiler, new InliningBehavior());

 compiler.process(input, options);
 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testInlineObjectLiteralsWithNestedObject() {
 String input = "function test() {" +
 "  var obj = {" +
 "    a: {" +
 "      b: 1," +
 "      c: {" +
 "        d: 2" +
 "      }" +
 "    }" +
 "  };" +
 "  var x = obj.a.b;" +
 "  var y = obj.a.c.d;" +
 "  return x + y;" +
 "}";

 String expectedOutput = "function test() {" +
 "  var a = {" +
 "    b: 1," +
 "    c: {" +
 "      d: 2" +
 "    }" +
 "  };" +
 "  var x = a.b;" +
 "  var y = a.c.d;" +
 "  return x + y;" +
 "}";

 ReferenceCollectingCallback referenceCollectingCallback =
 new ReferenceCollectingCallback(compiler, new InliningBehavior());

 compiler.process(input, options);
 String output = compiler.getSource();
 assertEquals(expectedOutput, output);
 }

 @After
 public void tearDown() {
 compiler = null;
 }
}

class InliningBehavior extends ReferenceCollectingCallback.Behavior {
 // Same as InlineObjectLiterals.InliningBehavior
 // ...
}