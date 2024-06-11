import com.google.javascript.jscomp.info.CodeInfo;
import com.google.javascript.jscomp.info.CompilationInfo;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TestDriver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

  @Test
  public void testNamedType() {
    CompilationInfo info = new CompilationInfo();
    // Create a new JSTypeRegistry.
    JSTypeRegistry registry = new JSTypeRegistry(info, null, null);

    // Test creating a new NamedType with a valid reference.
    String reference = "MyNamedType";
    String sourceName = "MySourceFile.js";
    int lineno = 10;
    int charno = 20;
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals("MyNamedType", namedType.getReferenceName());
    assertEquals(reference, namedType.toString());

    // Test defining a property on the NamedType.
    String propertyName = "myProperty";
    JSType propertyType = Type.INT;
    Node propertyNode = new Node_Property();
    namedType.defineProperty(propertyName, propertyType, false, propertyNode);
    assertNotNull(namedType.getOwnPropertyDescriptor(propertyName));

    // Test getting the referenced type.
    JSType referencedType = new IntType();
    namedType.setReferencedType(referencedType);
    assertEquals(referencedType, namedType.getReferencedType());

    // Test resolving the NamedType.
    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.setSlot("MyNamedType", namedType);
    namedType.resolve(new ErrorReporter() {}, enclosing);
    assertEquals(referencedType, namedType.getReferencedType());

    // Test resolving the NamedType via registry.
    JSType registryType = new IntType();
    registry.putType(reference, registryType);
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.resolve(new ErrorReporter() {}, enclosing);
    assertEquals(registryType, namedType.getReferencedType());

    // Test resolving the NamedType via properties.
    ObjectType parentType = new ObjectType(registry, "MyParentType");
    parentType.defineProperty("MyNamedType", namedType, false, new Node_Property());
    enclosing.setSlot("MyParentType", parentType);
    namedType = new NamedType(registry, "MyParentType.MyNamedType", sourceName, lineno, charno);
    namedType.resolve(new ErrorReporter() {}, enclosing);
    assertEquals(namedType, namedType.getReferencedType());

    // Test handling unresolved types.
    namedType = new NamedType(registry, "MyUnknownType", sourceName, lineno, charno);
    namedType.resolve(new ErrorReporter() {}, enclosing);
    assertNull(namedType.getReferencedType());

    // Test handling cycles in inheritance chain.
    ObjectType cycleType = new ObjectType(registry, "MyCycleType");
    cycleType.setPrototypeOf(cycleType);
    enclosing.setSlot("MyCycleType", cycleType);
    namedType = new NamedType(registry, "MyCycleType.MyCycleType", sourceName, lineno, charno);
    namedType.resolve(new ErrorReporter() {}, enclosing);
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());

    // Test validating the NamedType.
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType type) {
        return type.isIntType();
      }
    };
    namedType.setValidator(validator);
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.resolve(new ErrorReporter() {}, enclosing);
    assertTrue(validator.apply(namedType.getReferencedType()));
  }
}