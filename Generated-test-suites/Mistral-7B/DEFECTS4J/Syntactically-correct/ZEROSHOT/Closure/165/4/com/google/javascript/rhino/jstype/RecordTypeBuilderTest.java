package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddPropertyWithDuplicateName() {
    recordTypeBuilder.addProperty("name", JSType.STRING, mock(Node.class));
    assertNull(recordTypeBuilder.addProperty("name", JSType.NUMBER, mock(Node.class)));
  }

  @Test
  public void testAddPropertyWithValidArguments() {
    recordTypeBuilder.addProperty("name1", JSType.STRING, mock(Node.class));
    recordTypeBuilder.addProperty("name2", JSType.NUMBER, mock(Node.class));
    JSType result = recordTypeBuilder.build();
    assertNotNull(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithNullName() {
    recordTypeBuilder.addProperty(null, JSType.STRING, mock(Node.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithNullType() {
    recordTypeBuilder.addProperty("name", null, mock(Node.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithNullNode() {
    recordTypeBuilder.addProperty("name", JSType.STRING, null);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType result = recordTypeBuilder.build();
    assertNotNull(result);
    JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertNotNull(expected);
    assertNotNull(result.getClass());
    assertNotNull(result.getClass().getSuperclass());
    assertNotNull(result.getClass().getSuperclass().getSuperclass());
    assertSame(expected.getClass(), result.getClass().getSuperclass());
  }

  @Test
  public void testBuildRecordWithProperties() {
    recordTypeBuilder.addProperty("name1", JSType.STRING, mock(Node.class));
    recordTypeBuilder.addProperty("name2", JSType.NUMBER, mock(Node.class));
    JSType result = recordTypeBuilder.build();
    assertNotNull(result);
    Map<String, JSType> expectedProperties = new HashMap<>();
    expectedProperties.put("name1", JSType.STRING);
    expectedProperties.put("name2", JSType.NUMBER);
    RecordType expectedRecord = new RecordType(registry, expectedProperties);
    assertSame(expectedRecord.getClass(), result.getClass());
  }
}