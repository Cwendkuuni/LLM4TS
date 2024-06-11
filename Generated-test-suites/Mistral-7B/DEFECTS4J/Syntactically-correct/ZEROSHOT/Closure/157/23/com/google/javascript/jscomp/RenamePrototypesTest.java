import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    // Setup
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(mockCodingConvention());
    when(compiler.getCompilerOptions()).thenReturn(options);
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    NodeFactory nodeFactory = NodeFactory.getFactory();

    // Test 1: Simple prototype property
    Node externs = createNode("var Foo =function() {};");
    Node root = createNode("Foo.prototype.bar = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("bar => _bar", renamePrototypes.getPropertyMap().lookupNewName("bar"));

    // Test 2: Prototype property with leading underscore
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype._bar = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("_bar", renamePrototypes.getPropertyMap().lookupNewName("_bar"));

    // Test 3: Prototype property with trailing underscore
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype.bar_ = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("bar_", renamePrototypes.getPropertyMap().lookupNewName("bar_"));

    // Test 4: Prototype property with neither leading nor trailing underscores
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype.baz = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals(null, renamePrototypes.getPropertyMap().lookupNewName("baz"));

    // Test 5: Prototype property with a single non-lowercase character
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype.Baz = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("Baz", renamePrototypes.getPropertyMap().lookupNewName("Baz"));

    // Test 6: Prototype property with multiple non-lowercase characters
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype.FooBar = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("FooBar", renamePrototypes.getPropertyMap().lookupNewName("FooBar"));

    // Test 7: Prototype property with a name that starts with a reserved word
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype.indexOf = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("indexOf", renamePrototypes.getPropertyMap().lookupNewName("indexOf"));

    // Test 8: Prototype property with a name that ends with a reserved word
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype.toString = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("toString", renamePrototypes.getPropertyMap().lookupNewName("toString"));

    // Test 9: Prototype property with a name that contains a reserved word
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype.lastIndexOf = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("lastIndexOf", renamePrototypes.getPropertyMap().lookupNewName("lastIndexOf"));

    // Test 10: Prototype property with a name that matches a previously used name
    externs = createNode("var Foo =function() {};");
    VariableMap prevUsedRenameMap = new VariableMap(Collections.singletonMap("baz", "qux"));
    renamePrototypes = new RenamePrototypes(compiler, false, null, prevUsedRenameMap);
    root = createNode("Foo.prototype.baz = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("qux", renamePrototypes.getPropertyMap().lookupNewName("baz"));

    // Test 11: Prototype property with a name that matches a previously used name, but the previously used name is now an extern
    externs = createNode("var Foo =function() {};");
    externs = createNode("var qux = function() {};");
    renamePrototypes = new RenamePrototypes(compiler, false, null, prevUsedRenameMap);
    root = createNode("Foo.prototype.baz = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals(null, renamePrototypes.getPropertyMap().lookupNewName("baz"));

    // Test 12: Prototype property with aggressive renaming enabled
    externs = createNode("var Foo =function() {};");
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    root = createNode("Foo.prototype.baz = function() {};");
    renamePrototypes.process(externs, root);
    assertEquals("baz", renamePrototypes.getPropertyMap().lookupNewName("baz"));

    // Test 13: Object literal property
    externs = createNode("var Foo =function() {};");
    root = createNode("var obj = { bar: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals(null, renamePrototypes.getPropertyMap().lookupNewName("bar"));

    // Test 14: Object literal property with leading underscore
    externs = createNode("var Foo =function() {};");
    root = createNode("var obj = { _bar: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("_bar", renamePrototypes.getPropertyMap().lookupNewName("_bar"));

    // Test 15: Object literal property with trailing underscore
    externs = createNode("var Foo =function() {};");
    root = createNode("var obj = { bar_: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("bar_", renamePrototypes.getPropertyMap().lookupNewName("bar_"));

    // Test 16: Object literal property with neither leading nor trailing underscores
    externs = createNode("var Foo =function() {};");
    root = createNode("var obj = { baz: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals(null, renamePrototypes.getPropertyMap().lookupNewName("baz"));

    // Test 17: Object literal property with a single non-lowercase character
    externs = createNode("var Foo =function() {};");
    root = createNode("var obj = { Baz: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("Baz", renamePrototypes.getPropertyMap().lookupNewName("Baz"));

    // Test 18: Object literal property with multiple non-lowercase characters
    externs = createNode("var Foo =function() {};");
    root = createNode("var obj = { FooBar: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("FooBar", renamePrototypes.getPropertyMap().lookupNewName("FooBar"));

    // Test 19: Object literal property with a name that starts with a reserved word
    externs = createNode("var Foo =function() {};");
    root = createNode("var obj = { indexOf: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("indexOf", renamePrototypes.getPropertyMap().lookupNewName("indexOf"));

    // Test 20: Object literal property with a name that ends with a reserved word
    externs = createNode("var Foo =function() {};");
    root = createNode("var obj = { toString: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("toString", renamePrototypes.getPropertyMap().lookupNewName("toString"));

    // Test 21: Object literal property with a name that contains a reserved word
    externs = createNode("var Foo =function() {};");
    root = createNode("var obj = { lastIndexOf: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("lastIndexOf", renamePrototypes.getPropertyMap().lookupNewName("lastIndexOf"));

    // Test 22: Object literal property with a name that matches a previously used name
    externs = createNode("var Foo =function() {};");
    prevUsedRenameMap = new VariableMap(Collections.singletonMap("baz", "qux"));
    renamePrototypes = new RenamePrototypes(compiler, false, null, prevUsedRenameMap);
    root = createNode("var obj = { baz: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("qux", renamePrototypes.getPropertyMap().lookupNewName("baz"));

    // Test 23: Object literal property with a name that matches a previously used name, but the previously used name is now an extern
    externs = createNode("var Foo =function() {};");
    externs = createNode("var qux = function() {};");
    renamePrototypes = new RenamePrototypes(compiler, false, null, prevUsedRenameMap);
    root = createNode("var obj = { baz: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals(null, renamePrototypes.getPropertyMap().lookupNewName("baz"));

    // Test 24: Object literal property with aggressive renaming enabled
    externs = createNode("var Foo =function() {};");
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    root = createNode("var obj = { baz: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("baz", renamePrototypes.getPropertyMap().lookupNewName("baz"));

    // Test 25: Prototype property and object literal property with the same name
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype.bar = function() {};");
    root = createNode("var obj = { bar: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("bar", renamePrototypes.getPropertyMap().lookupNewName("bar"));

    // Test 26: Prototype property and object literal property with different names
    externs = createNode("var Foo =function() {};");
    root = createNode("Foo.prototype.bar = function() {};");
    root = createNode("var obj = { baz: 1 };");
    renamePrototypes.process(externs, root);
    assertEquals("bar", renamePrototypes.getPropertyMap().lookupNewName("bar"));
    assertEquals(null, renamePrototypes.getPropertyMap().lookupNewName("baz"));
  }

  private CompilerCodingConvention mockCodingConvention() {
    return mock(CompilerCodingConvention.class);
  }

  private Node createNode(String code) {
    TokenStream ts = new TokenStream(new CharSequenceInput(code), 0);
    Token firstToken = ts.nextToken();
    Node n = NodeFactory.compile(firstToken);
    ts.pushBack(firstToken);
    NodeTraversal.traverse(null, n, new NodeTraversal.Skip());
    return n;
  }
}

class CompilerCodingConvention {
  public boolean isExported(String name) {
    return false;
  }

  public boolean isPrivate(String name) {
    return false;
  }
}