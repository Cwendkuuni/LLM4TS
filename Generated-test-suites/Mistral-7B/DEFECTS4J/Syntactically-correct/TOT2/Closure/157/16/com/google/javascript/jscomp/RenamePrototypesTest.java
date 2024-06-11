import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperty() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.oldName = 1;\n" +
                   "Foo.prototype.oldName = 2;\n" +
                   "var foo = new Foo();\n" +
                   "foo.oldName = 3;\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerResult result = compiler.compile(options, input, null, pass);

    Node root = result.ast;

    // Check that the prototype property was renamed
    Node prototype = root.getFirstChild();
    Node prototypeOldName = prototype.getFirstChild();
    Node prototypeNewName = prototypeOldName.getNext();
    assertNotNull(prototypeOldName);
    assertNotNull(prototypeNewName);
    assertEquals("oldName_", prototypeOldName.getString());
    assertEquals("newName", prototypeNewName.getString());

    // Check that the instance variable was not renamed
    Node foo = root.getLastChild();
    Node fooOldName = foo.getFirstChild();
    assertNotNull(fooOldName);
    assertEquals("oldName", fooOldName.getString());
  }

  @Test
  public void testRenameObjLitProperty() {
    String input = "var obj = {\n" +
                   "  oldName: 1,\n" +
                   "};\n" +
                   "obj.oldName = 2;\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerResult result = compiler.compile(options, input, null, pass);

    Node root = result.ast;

    // Check that the obj lit property was renamed
    Node obj = root.getFirstChild();
    Node objOldName = obj.getFirstChild();
    Node objNewName = objOldName.getNext();
    assertNotNull(objOldName);
    assertNotNull(objNewName);
    assertEquals("oldName_", objOldName.getString());
    assertEquals("newName", objNewName.getString());

    // Check that the assignment did not change the value
    Node oldName = root.getLastChild();
    assertNotNull(oldName);
    assertEquals(2, oldName.getInt());
  }

  @Test
  public void testRenameAggressivePrototypeProperty() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.ObFoo = 1;\n" +
                   "Foo.prototype.ObFoo = 2;\n" +
                   "var foo = new Foo();\n" +
                   "foo.ObFoo = 3;\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerResult result = compiler.compile(options, input, null, pass);

    Node root = result.ast;

    // Check that the prototype property was aggressively renamed
    Node prototype = root.getFirstChild();
    Node prototypeOldName = prototype.getFirstChild();
    Node prototypeNewName = prototypeOldName.getNext();
    assertNotNull(prototypeOldName);
    assertNotNull(prototypeNewName);
    assertEquals("ObFoo_", prototypeOldName.getString());
    assertEquals("newName", prototypeNewName.getString());

    // Check that the instance variable was not renamed
    Node foo = root.getLastChild();
    Node fooOldName = foo.getFirstChild();
    assertNotNull(fooOldName);
    assertEquals("ObFoo", fooOldName.getString());
  }

  @Test(expected = AssertionError.class)
  public void testRenameExternedProperty() {
    String input = "String.prototype.indexOf = function() {}\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerResult result = compiler.compile(options, input, null, pass);

    Node root = result.ast;
  }

  @Test(expected = AssertionError.class)
  public void testRenamePrivateProperty() {
    String input = "var _foo = {};\n" +
                   "_foo._bar = 1;\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerResult result = compiler.compile(options, input, null, pass);

    Node root = result.ast;
  }

  @Test
  public void testRenamePrototypePropertiesInObjectLit() {
    String input = "var obj = {\n" +
                   "  Foo: function() {}\n" +
                   "};\n" +
                   "obj.Foo.prototype.oldName = 1;\n" +
                   "obj.Foo.prototype.oldName = 2;\n" +
                   "var foo = new obj.Foo();\n" +
                   "foo.oldName = 3;\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    CompilerResult result = compiler.compile(options, input, null, pass);

    Node root = result.ast;

    // Check that the prototype property was renamed
    Node prototype = root.getFirstChild();
    Node prototypeOldName = prototype.getFirstChild();
    Node prototypeNewName = prototypeOldName.getNext();
    Node constructor = prototype.getLastChild();
    assertNotNull(prototypeOldName);
    assertNotNull(prototypeNewName);
    assertEquals("oldName_", prototypeOldName.getString());
    assertEquals("newName", prototypeNewName.getString());
    assertEquals("Foo_", constructor.getString());

    // Check that the instance variable was not renamed
    Node foo = root.getLastChild();
    Node fooOldName = foo.getFirstChild();
    assertNotNull(fooOldName);
    assertEquals("oldName", fooOldName.getString());
  }
}