import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerUtils.compileTypeCheck;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private ParserOptions parserOptions;
  private IdGenerator idGenerator;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    parserOptions = new ParserOptions();
    compiler = new TestCompiler(options, parserOptions);
    idGenerator = compiler.getIdGenerator();
  }

  @After
  public void tearDown() {
    compiler = null;
  }

  @Test
  public void testMoveMethodAcrossModules() {
    String sourceCode = "(" +
        "function Foo() {" +
        "  this.bar = function() {" +
        "    return 'foo';" +
        "  };" +
        "}" +
        ")" +
        "(" +
        "function Bar() {" +
        "  this.baz = function() {" +
        "    return 'bar';" +
        "  };" +
        "  Foo.prototype.qux = function() {" +
        "    return this.bar() + ' ' + this.baz();" +
        "  };" +
        "}" +
        ")";

    Node root = compiler.parseTree(sourceCode);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.process(null, root);

    JSModule fooModule = moduleGraph.getModuleForName("Foo");
    JSModule barModule = moduleGraph.getModuleForName("Bar");

    Node fooRoot = compiler.getCompilerOutput(fooModule);
    Node barRoot = compiler.getCompilerOutput(barModule);

    // Check that Foo.prototype.qux was moved to Bar.
    Node quxProperty = fooRoot.getFirstChildByName("prototype");
    assertNotNull(quxProperty);
    Node quxFunction = quxProperty.getFirstChildByName("qux");
    assertNotNull(quxFunction);
    assertNull(quxFunction.getParent());

    Node barQuxFunction = barRoot.getFirstChildByName("prototype");
    assertNotNull(barQuxFunction);
    Node barQuxProperty = barQuxFunction.getParent();
    assertNotNull(barQuxProperty);
    assertEquals("qux", barQuxProperty.getName());

    // Check that Foo.prototype.qux was replaced with a stub in Foo.
    Node fooQuxStub = fooRoot.getFirstChildByName("prototype");
    assertNotNull(fooQuxStub);
    Node fooQuxStubCall = fooQuxStub.getFirstChild();
    assertNotNull(fooQuxStubCall);
    assertEquals(STUB_METHOD_NAME, fooQuxStubCall.getName());

    // Check that the stub was unstubbed in Bar.
    Node barQuxStubCall = barQuxFunction.getFirstChild();
    assertNotNull(barQuxStubCall);
    Node barQuxStubBody = barQuxStubCall.getFirstChild();
    assertNotNull(barQuxStubBody);
    assertEquals("JSCompiler_unstubMethod_body", barQuxStubBody.getName());

    // Check that Foo.prototype.bar and Bar.prototype.baz are not moved.
    Node fooBarFunction = fooRoot.getFirstChildByName("prototype").getFirstChildByName("bar");
    assertNotNull(fooBarFunction);
    assertSame(fooBarFunction, barRoot.getFirstChildByName("prototype").getFirstChildByName("bar"));

    Node fooBazFunction = fooRoot.getFirstChildByName("prototype").getFirstChildByName("baz");
    assertNotNull(fooBazFunction);
    assertSame(fooBazFunction, barRoot.getFirstChildByName("prototype").getFirstChildByName("baz"));
  }

  @Test
  public void testMoveMethodToSameModule() {
    String sourceCode = "(" +
        "function Foo() {" +
        "  this.bar = function() {" +
        "    return 'foo';" +
        "  };" +
        "}" +
        ")" +
        "(" +
        "function Foo() {" +
        "  this.baz = function() {" +
        "    return 'bar';" +
        "  };" +
        "  this.qux = function() {" +
        "    return this.bar() + ' ' + this.baz();" +
        "  };" +
        "}" +
        ")";

    Node root = compiler.parseTree(sourceCode);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.process(null, root);

    JSModule fooModule = moduleGraph.getModuleForName("Foo");
    Node fooRoot = compiler.getCompilerOutput(fooModule);

    // Check that Foo.prototype.qux was not moved since it's already
    // in the same module.
    Node quxProperty = fooRoot.getFirstChildByName("prototype");
    assertNotNull(quxProperty);
    Node quxFunction = quxProperty.getFirstChildByName("qux");
    assertNotNull(quxFunction);
    assertSame(quxFunction, fooRoot.getFirstChildByName("prototype").getFirstChildByName("qux"));
  }

  @Test
  public void testMoveMethodToNonExistentModule() {
    String sourceCode = "(" +
        "function Foo() {" +
        "  this.bar = function() {" +
        "    return 'foo';" +
        "  };" +
        "}" +
        ")" +
        "(" +
        "function Bar() {" +
        "}" +
        ")" +
        "(" +
        "function Baz() {" +
        "  this.baz = function() {" +
        "    return 'bar';" +
        "  };" +
        "  Foo.prototype.qux = function() {" +
        "    return this.bar() + ' ' + this.baz();" +
        "  };" +
        "}" +
        ")";

    Node root = compiler.parseTree(sourceCode);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.process(null, root);

    JSModule fooModule = moduleGraph.getModuleForName("Foo");
    Node fooRoot = compiler.getCompilerOutput(fooModule);

    // Check that Foo.prototype.qux was not moved since Bar is not a
    // valid module name.
    Node quxProperty = fooRoot.getFirstChildByName("prototype");
    assertNotNull(quxProperty);
    Node quxFunction = quxProperty.getFirstChildByName("qux");
    assertNotNull(quxFunction);
    assertSame(quxFunction, fooRoot.getFirstChildByName("prototype").getFirstChildByName("qux"));
  }

  @Test
  public void testMoveMethodWithClosureVariables() {
    String sourceCode = "(" +
        "function Foo(x) {" +
        "  this.bar = function() {" +
        "    return x;" +
        "  };" +
        "}" +
        ")" +
        "(" +
        "function Bar() {" +
        "  this.baz = function() {" +
        "    return 1;" +
        "  };" +
        "  Foo.prototype.qux = function() {" +
        "    return this.bar() + this.baz();" +
        "  };" +
        "}" +
        ")";

    Node root = compiler.parseTree(sourceCode);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.process(null, root);

    JSModule fooModule = moduleGraph.getModuleForName("Foo");
    Node fooRoot = compiler.getCompilerOutput(fooModule);

    // Check that Foo.prototype.qux was not moved since it references x.
    Node quxProperty = fooRoot.getFirstChildByName("prototype");
    assertNotNull(quxProperty);
    Node quxFunction = quxProperty.getFirstChildByName("qux");
    assertNotNull(quxFunction);
    assertSame(quxFunction, fooRoot.getFirstChildByName("prototype").getFirstChildByName("qux"));
  }

  @Test(expected = NullPointerException.class)
  public void testMoveMethodWithNullDeepestCommonModuleRef() {
    String sourceCode = "(" +
        "function Foo() {" +
        "  this.bar = function() {" +
        "    return 'foo';" +
        "  };" +
        "}" +
        ")" +
        "(" +
        "null" +
        ")";

    Node root = compiler.parseTree(sourceCode);
    CompilerPass pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    pass.process(null, root);

    // This test should fail with a NullPointerException since the
    // deepestCommonModuleRef for the anonymous module is null.
  }
}