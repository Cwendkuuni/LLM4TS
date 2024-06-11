import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilderConstructor() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    assertNotNull(builder);
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "testName";
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    RecordProperty expectedProperty = new RecordProperty(type, propertyNode);
    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(type);

    RecordProperty actualProperty = builder.addProperty(name, type, propertyNode);

    assertEquals(expectedProperty, actualProperty);
    assertNull(builder.build());

    // Test for duplicate property name
    builder.addProperty(name, type, propertyNode);
    assertNull(builder.build());

    // Test for null name
    assertNull(builder.addProperty(null, type, propertyNode));

    // Test for null type
    assertNull(builder.addProperty(name, null, propertyNode));

    // Test for null propertyNode
    assertNull(builder.addProperty(name, type, null));
  }

  @Test
  public void testBuildEmptyRecordType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType actualRecordType = builder.build();

    JSType expectedRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testBuildRecordTypeWithProperties() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name1 = "testName1";
    String name2 = "testName2";
    JSType type1 = mock(JSType.class);
    JSType type2 = mock(JSType.class);
    Node propertyNode1 = mock(Node.class);
    Node propertyNode2 = mock(Node.class);

    RecordProperty property1 = new RecordProperty(type1, propertyNode1);
    RecordProperty property2 = new RecordProperty(type2, propertyNode2);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(type1);
    when(registry.getRecordType(Collections.singletonMap(name1, type1), Collections.singletonMap(name2, type2)))
        .thenReturn(new RecordType(registry, Maps.newHashMap(name1, property1, name2, property2)));

    builder.addProperty(name1, type1, propertyNode1);
    builder.addProperty(name2, type2, propertyNode2);

    JSType actualRecordType = builder.build();
    JSType expectedRecordType = registry.getRecordType(Collections.singletonMap(name1, type1), Collections.singletonMap(name2, type2));

    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testBuildRecordTypeMultipleTimes() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name1 = "testName1";
    String name2 = "testName2";
    JSType type1 = mock(JSType.class);
    JSType type2 = mock(JSType.class);
    Node propertyNode1 = mock(Node.class);
    Node propertyNode2 = mock(Node.class);

    RecordProperty property1 = new RecordProperty(type1, propertyNode1);
    RecordProperty property2 = new RecordProperty(type2, propertyNode2);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(type1);
    when(registry.getRecordType(Collections.singletonMap(name1, type1), Collections.singletonMap(name2, type2)))
        .thenReturn(new RecordType(registry, Maps.newHashMap(name1, property1, name2, property2)));

    builder.addProperty(name1, type1, propertyNode1);
    builder.addProperty(name2, type2, propertyNode2);

    JSType actualRecordType1 = builder.build();
    assertNotNull(actualRecordType1);

    JSType actualRecordType2 = builder.build();
    assertEquals(actualRecordType1, actualRecordType2);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildRecordTypeAfterAddingPropertiesTwice() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String name1 = "testName1";
    String name2 = "testName2";
    JSType type1 = mock(JSType.class);
    JSType type2 = mock(JSType.class);
    Node propertyNode1 = mock(Node.class);
    Node propertyNode2 = mock(Node.class);

    RecordProperty property1 = new RecordProperty(type1, propertyNode1);
    RecordProperty property2 = new RecordProperty(type2, propertyNode2);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(type1);
    when(registry.getRecordType(Collections.singletonMap(name1, type1), Collections.singletonMap(name2, type2)))
        .thenReturn(new RecordType(registry, Maps.newHashMap(name1, property1, name2, property2)));

    builder.addProperty(name1, type1, propertyNode1);
    builder.addProperty(name1, type1, propertyNode1);

    builder.build();
  }

  @Test(expected = NullPointerException.class)
  public void testRecordTypeBuilderConstructorWithNullRegistry() {
    JSTypeRegistry registry = null;
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullType() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    Node propertyNode = mock(Node.class);

    builder.addProperty(null, mock(JSType.class), propertyNode);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullPropertyNode() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = mock(JSType.class);

    builder.addProperty(null, type, null);
  }
}