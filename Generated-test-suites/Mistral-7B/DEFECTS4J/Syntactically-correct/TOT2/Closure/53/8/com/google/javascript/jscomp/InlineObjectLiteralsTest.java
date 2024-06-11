import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  public static void main(String[] args) {
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler);
    Supplier<String> safeNameIdSupplier = () -> "";

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        abstractCompiler, safeNameIdSupplier);

    String sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: 3\n" +
        "  };\n" +
        "  var x = obj.a;\n" +
        "  var y = obj.b;\n" +
        "  var z = obj.c;\n" +
        "  return x + y + z;\n" +
        "}";

    Node externs = Node.parse(null, sourceCode);
    Node root = Node.parse(null, "test()");

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getLastChild().getContext();

    Behavior behavior = inlineObjectLiterals.new InliningBehavior();
    ReferenceCollection referenceCollection = new ReferenceCollection();

    Behavior callback = new Behavior() {
      @Override
      public void afterExitScope(NodeTraversal t, ReferenceMap referenceMap) {
        behavior.afterExitScope(t, referenceMap);
      }
    };
    ReferenceCollectingCallback referenceCollectingCallback =
        new ReferenceCollectingCallback(
            abstractCompiler, callback);
    referenceCollectingCallback.process(externs, root);

    List<Reference> references = referenceCollection.getReferences(scope.getVar("obj"));
    assertNotNull(references);
    assertEquals(3, references.size());

    Map<String, Node> initialValues = Maps.newHashMap();
    fillInitialValues(references.get(0), initialValues);

    // Test basic functionality
    String result = compiler.process(externs, root, null).toString();
    assertEquals("1+2+3", result);

    // Test edge cases: empty object literal
    sourceCode = "function test() {\n" +
        "  var obj = {}\n" +
        "  return obj.a;\n" +
        "}";
    externs = Node.parse(null, sourceCode);
    root = Node.parse(null, "test()");

    inlineObjectLiterals.process(externs, root);
    references = referenceCollection.getReferences(scope.getVar("obj"));
    assertNotNull(references);
    assertEquals(0, references.size());

    // Test edge cases: object literal assigned to a variable that is then reused
    sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: 3\n" +
        "  };\n" +
        "  var x = obj.a;\n" +
        "  var obj2 = obj;\n" +
        "  return x + obj2.b;\n" +
        "}";
    externs = Node.parse(null, sourceCode);
    root = Node.parse(null, "test()");

    inlineObjectLiterals.process(externs, root);
    references = referenceCollection.getReferences(scope.getVar("obj"));
    assertNotNull(references);
    assertEquals(3, references.size());

    references = referenceCollection.getReferences(scope.getVar("obj2"));
    assertNotNull(references);
    assertEquals(3, references.size());

    result = compiler.process(externs, root, null).toString();
    assertEquals("1+2", result);

    // Test edge cases: object literal with a cyclic reference
    sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: {b: obj}, b: {a: obj}\n" +
        "  };\n" +
        "  return obj.a.b.a.b;\n" +
        "}";
    externs = Node.parse(null, sourceCode);
    root = Node.parse(null, "test()");

    inlineObjectLiterals.process(externs, root);
    references = referenceCollection.getReferences(scope.getVar("obj"));
    assertNotNull(references);
    assertEquals(2, references.size());

    result = compiler.process(externs, root, null).toString();
    assertEquals("obj", result);

    // Test exception scenarios: object literal with a self-referential assignment
    sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: {b: obj.a}, b: 2\n" +
        "  };\n" +
        "  return obj.a.b;\n" +
        "}";
    externs = Node.parse(null, sourceCode);
    root = Node.parse(null, "test()");

    inlineObjectLiterals.process(externs, root);
    references = referenceCollection.getReferences(scope.getVar("obj"));
    assertNotNull(references);
    assertEquals(1, references.size());

    result = compiler.process(externs, root, null);
    assertNull(result);

    // Test exception scenarios: object literal with a circular reference
    sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: {b: obj}, b: {a: obj}\n" +
        "  };\n" +
        "  return obj.a.b;\n" +
        "}";
    externs = Node.parse(null, sourceCode);
    root = Node.parse(null, "test()");

    inlineObjectLiterals.process(externs, root);
    references = referenceCollection.getReferences(scope.getVar("obj"));
    assertNotNull(references);
    assertEquals(2, references.size());

    result = compiler.process(externs, root, null);
    assertNull(result);

    // Test exception scenarios: object literal with a circular reference and a property assigned to itself
    sourceCode = "function test() {\n" +
        "  var obj = {\n" +
        "    a: {b: obj, c: obj}, b: {}, c: {}\n" +
        "  };\n" +
        "  obj.b.a = obj;\n" +
        "  obj.c.a = obj;\n" +
        "  return obj.b.a.c.a.b.a.c.a.b.a.c.a.b.a.c.a.b.a.c.a.b.a.c.a.b.a.c.a.b.a;\n" +
        "}";
    externs = Node.parse(null, sourceCode);
    root = Node.parse(null, "test()");

    inlineObjectLiterals.process(externs, root);
    references = referenceCollection.getReferences(scope.getVar("obj"));
    assertNotNull(references);
    assertEquals(3, references.size());

    result = compiler.process(externs, root, null);
    assertNull(result);
  }

  private void fillInitialValues(Reference init, Map<String, Node> initvals) {
    // ... implementation omitted for brevity
  }
}