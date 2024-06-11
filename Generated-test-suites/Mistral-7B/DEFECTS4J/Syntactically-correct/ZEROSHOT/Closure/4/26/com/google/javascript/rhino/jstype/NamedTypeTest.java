import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.PolymerTestRunner;
import com.google.javascript.rhino.testing.StaticScope;

import java.util.List;

public class NamedTypeTest {

  @org.junit.BeforeClass
  public static void setup() throws Exception {
    PolymerTestRunner.setupClasspath();
  }

  @org.junit.AfterClass
  public static void teardown() throws Exception {
    PolymerTestRunner.teardownClasspath();
  }

  @org.junit.Test
  public void testNamedTypeResolutionViaRegistry() throws Exception {
    String jsSource =
        "var registry = jscomp.TypeRegistry.getInstance();" +
        "registry.registerType('String', String);" +
        "var myString = new NamedType(registry, 'String', 'MyFile.js', 1, 10);";

    JSTypeRegistry registry = new JSTypeRegistry();
    PolymerTestRunner runner = new PolymerTestRunner(registry);
    StaticScope<JSType> enclosing = new StaticScope<>();
    runner.eval(jsSource, enclosing);

    assertEquals("String", myString.getReferenceName());
    assertEquals("String", myString.getReferencedType().getName());
  }

  @org.junit.Test
  public void testNamedTypeResolutionViaProperties() throws Exception {
    String jsSource =
        "var ob = {foo: 42};" +
        "var myType = new NamedType(null, 'ob.foo', 'MyFile.js', 1, 10);";

    JSTypeRegistry registry = new JSTypeRegistry();
    PolymerTestRunner runner = new PolymerTestRunner(registry);
    StaticScope<JSType> enclosing = new StaticScope<>();
    runner.eval(jsSource, enclosing);

    assertEquals("number", myType.getReferenceName());
    assertEquals(numberType, myType.getReferencedType());
  }

  @org.junit.Test
  public void testInvalidNamedTypeResolution() throws Exception {
    String jsSource =
        "var myType = new NamedType(null, 'InvalidType', 'MyFile.js', 1, 10);";

    JSTypeRegistry registry = new JSTypeRegistry();
    PolymerTestRunner runner = new PolymerTestRunner(registry);
    StaticScope<JSType> enclosing = new StaticScope<>();
    runner.eval(jsSource, enclosing);

    assertNull(myType.getReferencedType());
  }

  @org.junit.Test
  public void testNamedTypeDefinition() throws Exception {
    String jsSource =
        "var registry = jscomp.TypeRegistry.getInstance();" +
        "registry.registerType('String', String);" +
        "var myString = new NamedType(registry, 'String', 'MyFile.js', 1, 10);" +
        "MyFile.prototype.foo = {\n" +
        "  get getFoo() {\n" +
        "    return myString;\n" +
        "  }\n" +
        "};";

    JSTypeRegistry registry = new JSTypeRegistry();
    PolymerTestRunner runner = new PolymerTestRunner(registry);
    StaticScope<JSType> enclosing = new StaticScope<>();
    runner.eval(jsSource, enclosing);

    Object obj = runner.getGlobalScope().get("MyFile");
    assertEquals(1, ((List<PropertyContinuation>) obj.getProperty("foo")).size());
    PropertyContinuation continuation =
        (PropertyContinuation) obj.getProperty("foo").getValue();
    assertEquals("getFoo", continuation.propertyName);
    assertEquals(myString.getClass(), continuation.type.getClass());
    assertTrue(continuation.inferred);
  }

  @org.junit.Test
  public void testNamedTypeResolveWithValidator() throws Exception {
    String jsSource =
        "var registry = jscomp.TypeRegistry.getInstance();" +
        "registry.registerType('String', String);" +
        "function isString(type) {\n" +
        "  return type instanceof StringType;\n" +
        "}\n" +
        "var myString = new NamedType(registry, 'String', 'MyFile.js', 1, 10);" +
        "myString.setValidator(isString);";

    JSTypeRegistry registry = new JSTypeRegistry();
    PolymerTestRunner runner = new PolymerTestRunner(registry);
    StaticScope<JSType> enclosing = new StaticScope<>();
    runner.eval(jsSource, enclosing);

    assertEquals("String", myString.getReferenceName());
    assertEquals("String", myString.getReferencedType().getName());
  }

  @org.junit.Test
  public void testNamedTypeResolveWithInvalidValidator() throws Exception {
    String jsSource =
        "var registry = jscomp.TypeRegistry.getInstance();" +
        "registry.registerType('String', String);" +
        "function isNumber(type) {\n" +
        "  return type instanceof NumberType;\n" +
        "}\n" +
        "var myString = new NamedType(registry, 'String', 'MyFile.js', 1, 10);" +
        "myString.setValidator(isNumber);";

    JSTypeRegistry registry = new JSTypeRegistry();
    PolymerTestRunner runner = new PolymerTestRunner(registry);
    StaticScope<JSType> enclosing = new StaticScope<>();
    runner.eval(jsSource, enclosing);

    assertEquals(unknownType, myString.getReferencedType());
  }

  @org.junit.Test
  public void testNamedTypeResolutionWithImplicitPrototypeCycle() throws Exception {
    String jsSource =
        "var registry = jscomp.TypeRegistry.getInstance();" +
        "registry.registerType('String', String);" +
        "function MyType() {}" +
        "MyType.prototype.foo = new NamedType(registry, 'MyType', 'MyFile.js', 1, 10);" +
        "MyType.prototype.bar = new NamedType(registry, 'MyType', 'MyFile.js', 1, 10);";

    JSTypeRegistry registry = new JSTypeRegistry();
    PolymerTestRunner runner = new PolymerTestRunner(registry);
    StaticScope<JSType> enclosing = new StaticScope<>();
    runner.eval(jsSource, enclosing);

    assertEquals(unknownType, registry.getType("MyType"));
  }

  @org.junit.Test
  public void testNamedTypeResolutionWithEnumElementCycle() throws Exception {
    String jsSource =
        "var registry = jscomp.TypeRegistry.getInstance();" +
        "registry.registerType('String', String);" +
        "enum MyEnum {\n" +
        "  Foo = new NamedType(registry, 'MyEnum', 'MyFile.js', 1, 10),\n" +
        "  Bar = new NamedType(registry, 'MyEnum', 'MyFile.js', 1, 10)\n" +
        "}" +
        "MyEnum.prototype.foo = MyEnum.Foo;" +
        "MyEnum.prototype.bar = MyEnum.Bar;";

    JSTypeRegistry registry = new JSTypeRegistry();
    PolymerTestRunner runner = new PolymerTestRunner(registry);
    StaticScope<JSType> enclosing = new StaticScope<>();
    runner.eval(jsSource, enclosing);

    assertEquals(unknownType, registry.getType("MyEnum"));
  }
}