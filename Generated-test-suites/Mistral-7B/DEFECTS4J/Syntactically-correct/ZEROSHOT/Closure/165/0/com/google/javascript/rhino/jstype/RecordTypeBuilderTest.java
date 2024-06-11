import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testaddPropertyWithValidArguments() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String propertyName = "propertyName";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(
        new JSTypeRegistry.NativeObjectType(JSTypeNative.OBJECT_TYPE));

    RecordTypeBuilder returnedRecordTypeBuilder = recordTypeBuilder.addProperty(
        propertyName, propertyType, propertyNode);

    assertEquals(recordTypeBuilder, returnedRecordTypeBuilder);
  }

  @Test
  public void testaddPropertyWithDuplicateName() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String propertyName = "propertyName";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    RecordProperty recordProperty = mock(RecordProperty.class);
    when(recordProperty.getType()).thenReturn(propertyType);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(
        new JSTypeRegistry.NativeObjectType(JSTypeNative.OBJECT_TYPE));

    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

    assertNull(recordTypeBuilder.addProperty(propertyName, propertyType,
        propertyNode));
  }

  @Test
  public void testBuildEmptyRecord() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    JSType returnedRecordType = recordTypeBuilder.build();

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
        returnedRecordType);
  }

  @Test
  public void testBuildRecordWithOneProperty() {
    JSTypeRegistry registry = mock(JSTypeRegistry.class);
    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

    String propertyName = "propertyName";
    JSType propertyType = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    RecordProperty recordProperty = mock(RecordProperty.class);
    when(recordProperty.getType()).thenReturn(propertyType);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(
        new JSTypeRegistry.NativeObjectType(JSTypeNative.OBJECT_TYPE));

    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

    Map<String, JSType> properties = Maps.newHashMap();
    properties.put(propertyName, propertyType);

    JSType returnedRecordType = recordTypeBuilder.build();

    assertEquals(new RecordType(registry, properties), returnedRecordType);
  }
}