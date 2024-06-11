import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.rhino.TokenStream;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
  }

  @Test
  public void testRenamePrototypeProperties() {
    String source = "function Foo() {} Foo.prototype.bar = function() {};";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node fooProto = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype");
    Node fooProtoBar = findNodeByType(root, NodeType.GETPROP, fooProto, "bar");

    assertEquals("Renamed bar to _bar", "_bar", fooProtoBar.getString());
  }

  @Test
  public void testDontRenamePrototypePropertiesWithLeadingUnderscore() {
    String source = "function Foo() {} Foo.prototype._bar = function() {};";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node fooProtoBar = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "_bar");

    assertEquals("Did not rename _bar", "_bar", fooProtoBar.getString());
  }

  @Test
  public void testDontRenamePrototypePropertiesWithTrailingUnderscore() {
    String source = "function Foo() {} Foo.prototype.bar_ = function() {};";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node fooProtoBar = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "bar");

    assertEquals("Did not rename bar", "bar", fooProtoBar.getString());
  }

  @Test
  public void testRenameObjLitProperties() {
    String source = "var obj = { foo: 1 };";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node objFoo = findNodeByType(root, NodeType.GETELEM, "obj", "foo");

    assertEquals("Renamed foo to _foo", "_foo", objFoo.getString());
  }

  @Test
  public void testDontRenameObjLitPropertiesWithLeadingUnderscore() {
    String source = "var obj = { _foo: 1 };";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node objFoo = findNodeByType(root, NodeType.GETELEM, "obj", "_foo");

    assertEquals("Did not rename _foo", "_foo", objFoo.getString());
  }

  @Test
  public void testDontRenameObjLitPropertiesWithTrailingUnderscore() {
    String source = "var obj = { foo_: 1 };";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node objFoo = findNodeByType(root, NodeType.GETELEM, "obj", "foo");

    assertEquals("Did not rename foo", "foo", objFoo.getString());
  }

  @Test
  public void testRenamePrototypePropertiesWithAggressiveRenaming() {
    String source = "function Foo() {} Foo.prototype.foo = function() {};";

    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root, Node input) {
        renamePrototypes = new RenamePrototypes(compiler, true, null, null);
        renamePrototypes.process(externs, root);
      }
    });

    passManager.runPasses(source, LifeCycleStage.NORMALIZED);

    Node fooProtoFoo = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "foo");

    assertEquals("Renamed foo to foo_", "foo_", fooProtoFoo.getString());
  }

  @Test
  public void testDontRenamePrototypePropertiesWithUppercaseLetter() {
    String source = "function Foo() {} Foo.prototype.Foo = function() {};";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node fooProtoFoo = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "Foo");

    assertEquals("Did not rename Foo", "Foo", fooProtoFoo.getString());
  }

  @Test
  public void testDontRenamePrototypePropertiesWithReservedNames() {
    String source = "function Foo() {} Foo.prototype.indexOf = function() {};";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node fooProtoIndexOf = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "indexOf");

    assertEquals("Did not rename indexOf", "indexOf", fooProtoIndexOf.getString());
  }

  @Test
  public void testDontRenamePropertyAccesses() {
    String source = "function Foo() {} var obj = {}; obj.foo = Foo.prototype.foo;";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node objFoo = findNodeByType(root, NodeType.GETELEM, "obj", "foo");

    assertEquals("Did not rename foo", "foo", objFoo.getString());
  }

  @Test
  public void testRenamePrototypeAssignments() {
    String source = "function Foo() {} Foo.prototype = { bar: function() {} };";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node fooProtoBar = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "bar");

    assertEquals("Renamed bar to _bar", "_bar", fooProtoBar.getString());
  }

  @Test
  public void testRenamePrototypeAssignmentsWithLeadingUnderscore() {
    String source = "function Foo() {} Foo.prototype = { _bar: function() {} };";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node fooProtoBar = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "_bar");

    assertEquals("Did not rename _bar", "_bar", fooProtoBar.getString());
  }

  @Test
  public void testRenamePrototypeAssignmentsWithTrailingUnderscore() {
    String source = "function Foo() {} Foo.prototype = { bar_: function() {} };";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node fooProtoBar = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "bar");

    assertEquals("Did not rename bar", "bar", fooProtoBar.getString());
  }

  @Test
  public void testRenamePrototypeAssignmentsWithReservedNames() {
    String source = "function Foo() {} Foo.prototype = { indexOf: function() {} };";

    Node root = new TokenStream(source).parseProgram();

    renamePrototypes.process(null, root);

    Node fooProtoIndexOf = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "indexOf");

    assertEquals("Did not rename indexOf", "indexOf", fooProtoIndexOf.getString());
  }

  @Test
  public void testRenamePrototypeAssignmentsWithAggressiveRenaming() {
    String source = "function Foo() {} Foo.prototype = { foo: function() {} };";

    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root, Node input) {
        renamePrototypes = new RenamePrototypes(compiler, true, null, null);
        renamePrototypes.process(externs, root);
      }
    });

    passManager.runPasses(source, LifeCycleStage.NORMALIZED);

    Node fooProtoFoo = findNodeByType(root, NodeType.GETPROP, "Foo", "prototype", "foo");

    assertEquals("Renamed foo to foo_", "foo_", fooProtoFoo.getString());
  }

  @Test
  public void testRenamePrototypeAssignmentsWithComplexObjectStructure() {
    String source = "function Foo() {} Foo.prototype = { foo: { bar: function() {} } };";

    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root, Node input) {
        renamePrototypes = new RenamePrototypes(compiler, false, null, null);
        renamePrototypes.process(externs, root);
      }
    });

    passManager.runPasses(source, LifeCycleStage.NORMALIZED);

    Node fooProtoFooBar = findNodeByType(root, NodeType.GET, "Foo", "prototype", "foo", "bar");

    assertEquals("Renamed bar to _bar", "_bar", fooProtoFooBar.getString());
  }

  @Test
  public void testRenamePrototypeAssignmentsWithComplexObjectStructureAndAggressiveRenaming() {
    String source = "function Foo() {} Foo.prototype = { foo: { bar: function() {} } };";

    CompilerPassManager passManager = new CompilerPassManager(compiler, options);
    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root, Node input) {
        renamePrototypes = new RenamePrototypes(compiler, true, null, null);
        renamePrototypes.process(externs, root);
      }
    });

    passManager.runPasses(source, LifeCycleStage.NORMALIZED);

    Node fooProtoFooBar = findNodeByType(root, NodeType.GET, "Foo", "prototype", "foo", "bar");

    assertEquals("Renamed bar to bar_", "bar_", fooProtoFooBar.getString());
  }

  private Node findNodeByType(Node root, int type, String... ids) {
    Node currentNode = root;

    for (String id : ids) {
      if (currentNode.getType() != type) {
        fail("Expected node of type " + type + " but found node of type " + currentNode.getType());
      }

      currentNode = currentNode.getFirstChild();

      if (currentNode.getType() != Token.GETPROP && currentNode.getType() != Token.GETELEM) {
        fail("Expected node with IDs " + Arrays.toString(ids) + " to be a property or element access but was " + currentNode.getType());
      }

      currentNode = currentNode.getNext();

      if (!currentNode.getString().equals(id)) {
        fail("Expected node with IDs " + Arrays.toString(ids) + " to have ID " + id + " but was " + currentNode.getString());
      }
    }

    return currentNode;
  }

  private static final int NodeType_ASSIGN = 6;
  private static final int NodeType_CALL = 2;
  private static final int NodeType_GETELEM = 9;
  private static final int NodeType_GETPROP = 8;
  private static final int NodeType_OBJECTLIT = 11;
}