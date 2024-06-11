import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.annotations.ExpectedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilderConstructor() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    assertFalse(recordTypeBuilder.isEmpty());
  }

  @Test
  public void testAddProperty() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    String name = "testName";
    JSType type = new IntType();
    Node propertyNode = new Node();
    RecordProperty expectedRecordProperty = new RecordProperty(type, propertyNode);
    RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertEquals(expectedRecordProperty, actualRecordProperty);
  }

  @Test
  public void testBuildEmptyRecordType() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    JSType actualRecordType = recordTypeBuilder.build();
    JSType expectedRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testBuildRecordType() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    String name = "testName";
    JSType type = new IntType();
    Node propertyNode = new Node();
    recordTypeBuilder.addProperty(name, type, propertyNode);
    JSType actualRecordType = recordTypeBuilder.build();
    RecordType expectedRecordType = new RecordType(
        registry, Collections.singletonMap(name, new RecordProperty(type, propertyNode)));
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test(expected = IllegalStateException.class)
  public void testAddSamePropertyName() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    String name = "testName";
    JSType type = new IntType();
    Node propertyNode = new Node();
    RecordProperty expectedRecordProperty1 = new RecordProperty(type, propertyNode);
    RecordProperty actualRecordProperty1 = recordTypeBuilder.addProperty(name, type, propertyNode);
    RecordProperty actualRecordProperty2 = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertNull(actualRecordProperty2);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullName() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    String name = null;
    JSType type = new IntType();
    Node propertyNode = new Node();
    RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertNull(actualRecordProperty);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullType() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    String name = "testName";
    JSType type = null;
    Node propertyNode = new Node();
    RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertNull(actualRecordProperty);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyWithNullPropertyNode() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    String name = "testName";
    JSType type = new IntType();
    Node propertyNode = null;
    RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);
    assertNull(actualRecordProperty);
  }

  @Test
  public void testBuildRecordTypeWithEmptyProperties() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    JSType actualRecordType = recordTypeBuilder.build();
    JSType expectedRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testBuildRecordTypeWithDifferentPropertyTypes() {
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry(Collections.emptyMap()));
    String name1 = "testName1";
    JSType type1 = new IntType();
    Node propertyNode1 = new Node();
    String name2 = "testName2";
    JSType type2 = new StringType();
    Node propertyNode2 = new Node();
    recordTypeBuilder.addProperty(name1, type1, propertyNode1);
    recordTypeBuilder.addProperty(name2, type2, propertyNode2);
    JSType actualRecordType = recordTypeBuilder.build();
    RecordType expectedRecordType = new RecordType(
        registry, Collections.unmodifiableMap(
            Maps.newHashMap(
                ImmutableMap.of(name1, new RecordProperty(type1, propertyNode1), name2, new RecordProperty(type2, propertyNode2))
            )
        ));
    assertEquals(expectedRecordType, actualRecordType);
  }
}