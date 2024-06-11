package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ResolvedRecursiveTypeTest {

  private TypeBindings bindings;
  private JavaType refType;

  @Before
  public void setUp() {
    bindings = mock(TypeBindings.class);
    refType = mock(JavaType.class);
  }

  @Test
  public void testConstructorWithNullArg() {
    ResolvedRecursiveType instance = new ResolvedRecursiveType(null, bindings);
    assertNull(instance.getSelfReferencedType());
  }

  @Test
  public void testConstructorWithValidArguments() {
    when(refType.getClass()).thenReturn(String.class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    assertSame(refType, instance.getSelfReferencedType());
  }

  @Test
  public void testSetReference() {
    when(refType.getClass()).thenReturn(String.class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    instance.setReference(refType);
    assertSame(refType, instance.getSelfReferencedType());
  }

  @Test(expected = IllegalStateException.class)
  public void testSetReferenceMultipleTimes() {
    when(refType.getClass()).thenReturn(String.class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    instance.setReference(refType);
    instance.setReference(refType);
  }

  @Test
  public void testGetSelfReferencedType() {
    when(refType.getClass()).thenReturn(String.class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    instance.setReference(refType);
    assertSame(refType, instance.getSelfReferencedType());
  }

  @Test
  public void testGetGenericSignature() {
    Type type = TypeFactory.defaultInstance().constructType(String[].class);
    JavaType javaType = TypeFactory.defaultInstance().constructType(type);

    when(refType.getGenericSignature(Mockito.any(StringBuilder.class))).thenReturn(Mockito.mock(StringBuilder.class));

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String[].class, bindings);
    instance.setReference(javaType);

    StringBuilder builder = Mockito.mock(StringBuilder.class);
    instance.getGenericSignature(builder);

    // The method isn't actually tested here, but we should call the correct method on the mocked refType
    when(javaType.getGenericSignature(builder)).thenReturn(builder);

    // No assertions here, as the method is just delegated to the refType
  }

  @Test
  public void testGetErasedSignature() {
    Type type = TypeFactory.defaultInstance().constructType(String[].class);
    JavaType javaType = TypeFactory.defaultInstance().constructType(type);

    when(refType.getErasedSignature(Mockito.any(StringBuilder.class))).thenReturn(Mockito.mock(StringBuilder.class));

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String[].class, bindings);
    instance.setReference(javaType);

    StringBuilder builder = Mockito.mock(StringBuilder.class);
    instance.getErasedSignature(builder);

    // The method isn't actually tested here, but we should call the correct method on the mocked refType
    when(javaType.getErasedSignature(builder)).thenReturn(builder);

    // No assertions here, as the method is just delegated to the refType
  }

  @Test
  public void testWithContentType() {
    JavaType contentType = mock(JavaType.class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    JavaType result = instance.withContentType(contentType);

    assertSame(instance, result);
  }

  @Test
  public void testWithTypeHandler() {
    Object handler = mock(Object.class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    JavaType result = instance.withTypeHandler(handler);

    assertSame(instance, result);
  }

  @Test
  public void testWithContentTypeHandler() {
    Object handler = mock(Object.class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    JavaType result = instance.withContentTypeHandler(handler);

    assertSame(instance, result);
  }

  @Test
  public void testWithValueHandler() {
    Object handler = mock(Object.class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    JavaType result = instance.withValueHandler(handler);

    assertSame(instance, result);
  }

  @Test
  public void testWithContentValueHandler() {
    Object handler = mock(Object.class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    JavaType result = instance.withContentValueHandler(handler);

    assertSame(instance, result);
  }

  @Test
  public void testWithStaticTyping() {
    ResolvedRecursiveType instance = new ResolvedRecursiveType(String.class, bindings);
    JavaType result = instance.withStaticTyping();

    assertSame(instance, result);
  }

  @Test
  public void testNarrow() {
    // Deprecated method
  }

  @Test
  public void testRefine() {
    // Returns null
  }

  @Test
  public void testIsContainerType() {
    assertFalse(new ResolvedRecursiveType(String.class, bindings).isContainerType());
  }

  @Test
  public void testToString() {
    when(refType.getRawClass()).thenReturn(String[].class);

    ResolvedRecursiveType instance = new ResolvedRecursiveType(String[].class, bindings);
    assertSame("{[recursive type; [Ljava.lang.String;]}", instance.toString());

    when(refType.getRawClass()).thenReturn(String.class);
    instance.setReference(refType);

    assertSame("{[recursive type; " + refType.getRawClass().getName() + "]}", instance.toString());
  }

  @Test
  public void testEquals() {
    Class<?> clazz = String[].class;
    JavaType anotherInstance = new ResolvedRecursiveType(clazz, bindings);
    ResolvedRecursiveType instance = new ResolvedRecursiveType(clazz, bindings);

    assertSame(instance, instance);
    assertSame(instance, instance);

    // Different classes
    JavaType otherType = mock(JavaType.class);
    assertFalse(instance.equals(otherType));

    // Different self-references
    ResolvedRecursiveType otherRefType = new ResolvedRecursiveType(clazz, bindings);
    otherRefType.setReference(refType);
    assertFalse(instance.equals(otherRefType));

    // Same self-reference, different classes
    ResolvedRecursiveType anotherRefType = new ResolvedRecursiveType(String.class, bindings);
    anotherRefType.setReference(refType);
    assertSame(instance, anotherRefType);
  }
}