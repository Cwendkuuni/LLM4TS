import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

  @Test
  public void testSimpleTypeConstructor() {
    SimpleType simpleType = new SimpleType(String.class);
    assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.String>", simpleType.toString());
  }

  @Test
  public void testSimpleTypeCopyConstructor() {
    SimpleType originalType = new SimpleType(String.class);
    SimpleType copiedType = new SimpleType(originalType);
    assertEquals(originalType.toString(), copiedType.toString());
  }

  @Test
  public void testSimpleTypeStaticConstructor() {
    SimpleType simpleType = SimpleType.construct(String.class);
    assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.String>", simpleType.toString());
  }

  @Test
  public void testSimpleTypeUnsafeConstructor() {
    SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.String>", simpleType.toString());
  }

  @Test
  public void testSimpleTypeWithValueHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType withValueHandler = simpleType.withValueHandler(new MyStringValueHandler());
    assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.String>", simpleType.toString());
    assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.String> with value handler: MyStringValueHandler", withValueHandler.toString());
  }

  @Test
  public void testSimpleTypeWithTypeHandler() {
    SimpleType simpleType = new SimpleType(String.class);
    SimpleType withTypeHandler = simpleType.withTypeHandler(new MyStringTypeHandler());
    assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.String>", simpleType.toString());
    assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.String> with type handler: MyStringTypeHandler", withTypeHandler.toString());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSimpleTypeWithInvalidClass() {
    SimpleType simpleType = new SimpleType(Map.class);
  }

  @Test
  public void testSimpleTypeWithInvalidArguments() {
    try {
      SimpleType simpleType = new SimpleType(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      SimpleType simpleType = new SimpleType(new Object());
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testSimpleTypeToString() {
    SimpleType simpleType = new SimpleType(String.class);
    assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.String>", simpleType.toString());

    SimpleType simpleTypeWithBindings = new SimpleType(String.class, new TypeBindings());
    assertEquals("class com.fasterxml.jackson.databind.type.SimpleType<java.lang.String>", simpleTypeWithBindings.toString());
  }

  @Test
  public void testSimpleTypeEquals() {
    SimpleType simpleType1 = new SimpleType(String.class);
    SimpleType simpleType2 = new SimpleType(String.class);
    assertTrue(simpleType1.equals(simpleType2));

    SimpleType simpleType3 = new SimpleType(Integer.class);
    assertFalse(simpleType1.equals(simpleType3));
  }
}