import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeMockFactory;
import com.google.javascript.rhino.testing.type.RecordType;
import com.google.javascript.rhino.testing.type.RecordTypeBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;
  private RecordType recordedType;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testBuilderWithEmptyRecord() {
    JSType emptyRecord = recordTypeBuilder.build();
    assertNotNull(emptyRecord);
    assertNotNull(emptyRecord.getClass());
    assertInstanceOf(JSTypeNative.class, emptyRecord);
    assertInstanceOf(JSTypeNative.OBJECT_TYPE, emptyRecord);
  }

  @Test
  public void testBuilderWithSingleProperty() {
    Node propertyNode = TypeMockFactory.createMockNode();
    JSType stringType = mock(JSType.class);
    when(registry.getBuiltInType("String")).thenReturn(stringType);

    RecordProperty recordProperty = recordTypeBuilder.addProperty("name", stringType, propertyNode)
        .build().getProperties().values().stream().findFirst().get();

    JSType recordType = recordTypeBuilder.build();
    assertNotNull(recordType);
    assertInstanceOf(RecordType.class, recordType);
    Map<String, RecordProperty> propertiesMap = recordType.getProperties();
    assertNotNull(propertiesMap);
    assertFalse(propertiesMap.isEmpty());
    assertEquals(1, propertiesMap.size());
    RecordProperty returnedRecordProperty = propertiesMap.values().stream().findFirst().get();
    assertNotNull(returnedRecordProperty);
    assertEquals(recordProperty, returnedRecordProperty);
    assertEquals("name", returnedRecordProperty.getName());
    assertEquals(stringType, returnedRecordProperty.getType());
    assertEquals(propertyNode, returnedRecordProperty.getPropertyNode());
  }

  @Test
  public void testBuilderWithDuplicatedProperty() {
    Node propertyNode = TypeMockFactory.createMockNode();
    JSType stringType = mock(JSType.class);
    when(registry.getBuiltInType("String")).thenReturn(stringType);

    RecordProperty addedProperty = recordTypeBuilder.addProperty("name", stringType, propertyNode)
        .build().getProperties().values().stream().findFirst().get();

    assertNull(recordTypeBuilder.addProperty("name", stringType, propertyNode));
  }

  @Test
  public void testBuilderWithMultipleProperties() {
    Node propertyNode1 = TypeMockFactory.createMockNode();
    Node propertyNode2 = TypeMockFactory.createMockNode();

    JSType stringType1 = mock(JSType.class);
    JSType intType = mock(JSType.class);
    when(registry.getBuiltInType("String")).thenReturn(stringType1);
    when(registry.getBuiltInType("Number")).thenReturn(intType);

    RecordProperty recordProperty1 = recordTypeBuilder.addProperty("name1", stringType1, propertyNode1)
        .build().getProperties().values().stream().findFirst().get();
    RecordProperty recordProperty2 = recordTypeBuilder.addProperty("name2", intType, propertyNode2)
        .build().getProperties().values().stream().findFirst().get();

    JSType recordType = recordTypeBuilder.build();
    assertNotNull(recordType);
    assertInstanceOf(RecordType.class, recordType);
    Map<String, RecordProperty> propertiesMap = recordType.getProperties();
    assertNotNull(propertiesMap);
    assertFalse(propertiesMap.isEmpty());
    assertEquals(2, propertiesMap.size());
    RecordProperty name1RecordProperty = propertiesMap.values().stream()
        .filter(property -> property.getName().equals("name1")).findFirst().get();
    RecordProperty name2RecordProperty = propertiesMap.values().stream()
        .filter(property -> property.getName().equals("name2")).findFirst().get();
    assertNotNull(name1RecordProperty);
    assertNotNull(name2RecordProperty);
    assertEquals("name1", name1RecordProperty.getName());
    assertEquals("name2", name2RecordProperty.getName());
    assertEquals(stringType1, name1RecordProperty.getType());
    assertEquals(intType, name2RecordProperty.getType());
    assertEquals(propertyNode1, name1RecordProperty.getPropertyNode());
    assertEquals(propertyNode2, name2RecordProperty.getPropertyNode());
  }
}