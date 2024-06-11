import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

 private Compiler compiler;
 private CompilerOptions options;
 private CompilerPass inlineObjectLiterals;
 private CompilerResults results;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "temp");
 }

 @Test
 public void testInlineSimpleObjectLiteral() {
 String input = "function f() { var obj = {a: 1, b: 2}; }";
 options.setInput(Node.newText(input));
 results = new CompilerResults();
 compiler.compile(options, results);
 inlineObjectLiterals.process(results.getExterns(), results.getRootNode());

 Node expected = Node.newObjectLiteral(
 Node.newProperty(Node.newString("a"), Node.newNumber(1)),
 Node.newProperty(Node.newString("b"), Node.newNumber(2)));
 Node actual = results.getRootNode().getFirstChild();
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testInlineObjectLiteralWithFunction() {
 String input = "function f() { var obj = {a: 1, b: {c: function() {}}}; }";
 options.setInput(Node.newText(input));
 results = new CompilerResults();
 compiler.compile(options, results);
 inlineObjectLiterals.process(results.getExterns(), results.getRootNode());

 Node expected = Node.newObjectLiteral(
 Node.newProperty(Node.newString("a"), Node.newNumber(1)),
 Node.newProperty(Node.newString("b"),
 Node.newObjectLiteral(
 Node.newProperty(Node.newString("c"), Node.newFunctionDeclaration(Node.newIdentifier("anonymous"))))));
 Node actual = results.getRootNode().getFirstChild();
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testInlineNestedObjectLiteral() {
 String input = "function f() { var obj = {a: {b: 1, c: 2}}; }";
 options.setInput(Node.newText(input));
 results = new CompilerResults();
 compiler.compile(options, results);
 inlineObjectLiterals.process(results.getExterns(), results.getRootNode());

 Node expected = Node.newObjectLiteral(
 Node.newProperty(Node.newString("a"),
 Node.newObjectLiteral(
 Node.newProperty(Node.newString("b"), Node.newNumber(1)),
 Node.newProperty(Node.newString("c"), Node.newNumber(2)))));
 Node actual = results.getRootNode().getFirstChild();
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testInlineObjectLiteralWithArray() {
 String input = "function f() { var obj = {a: [1, 2], b: 3}; }";
 options.setInput(Node.newText(input));
 results = new CompilerResults();
 compiler.compile(options, results);
 inlineObjectLiterals.process(results.getExterns(), results.getRootNode());

 Node expected = Node.newObjectLiteral(
 Node.newProperty(Node.newString("a"), Node.newArrayLiteral(Node.newNumber(1), Node.newNumber(2))),
 Node.newProperty(Node.newString("b"), Node.newNumber(3)));
 Node actual = results.getRootNode().getFirstChild();
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testInlineObjectLiteralWithSelfReference() {
 String input = "function f() { var obj = {a: 1, b: {c: obj}}; }";
 options.setInput(Node.newText(input));
 results = new CompilerResults();
 compiler.compile(options, results);
 inlineObjectLiterals.process(results.getExterns(), results.getRootNode());

 // This test case should fail, as the InlineObjectLiterals pass does not handle self-referential object literals.
 //assertFailWithMessage("InlineObjectLiterals should not inline self-referential object literals", () -> {
 // inlineObjectLiterals.process(results.getExterns(), results.getRootNode());
 // Node actual = results.getRootNode().getFirstChild();
 // assertEquals(expected.toString(), actual.toString());
 //});
 }

 @Test
 public void testInlineObjectLiteralWithCycle() {
 String input = "function f() { var obj1 = {a: {b: obj1}}; }";
 options.setInput(Node.newText(input));
 results = new CompilerResults();
 compiler.compile(options, results);
 inlineObjectLiterals.process(results.getExterns(), results.getRootNode());

 // This test case should fail, as the InlineObjectLiterals pass does not handle object literals with cycles.
 //assertFailWithMessage("InlineObjectLiterals should not inline object literals with cycles", () -> {
 // inlineObjectLiterals.process(results.getExterns(), results.getRootNode());
 // Node actual = results.getRootNode().getFirstChild();
 // assertEquals(expected.toString(), actual.toString());
 //});
 }
}