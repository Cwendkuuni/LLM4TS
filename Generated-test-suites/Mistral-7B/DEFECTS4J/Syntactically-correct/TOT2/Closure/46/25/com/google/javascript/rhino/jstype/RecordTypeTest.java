import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testCreateRecordType() {
    // Basic functionality test
    Assert.assertEquals(JSType.createRecordTypeType(Collections.emptyMap()), recordType.getClass());
    Assert.assertEquals(recordType.getProperties().size(), 2);

    // Check getPropertyType
    Assert.assertEquals(recordType.getPropertyType("name"), registry.getStringType());
    Assert.assertEquals(recordType.getPropertyType("age"), registry.getNumberType());

    // Check isEquivalentTo
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    Assert.assertTrue(recordType1.isEquivalentTo(recordType2));

    // Check getImplicitPrototype
    Assert.assertEquals(recordType.getImplicitPrototype(), registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    // Check defineProperty
    recordType.defineProperty("newProperty", registry.getStringType(), false, null);
    Assert.assertTrue(recordType.hasProperty("newProperty"));

    // Check getLeastSupertype
    RecordType recordType3 = new RecordType(registry, Arrays.asList(
        new RecordProperty("newProperty", registry.getStringType()),
        new RecordProperty("name", registry.getStringType()),
        new RecordProperty("age", registry.getNumberType())).asMap());
    JSType leastSupertype = recordType.getLeastSupertype(recordType3);
    Assert.assertEquals(leastSupertype, recordType3);

    // Check getGreatestSubtypeHelper
    RecordType recordType4 = new RecordType(registry, Arrays.asList(
        new RecordProperty("newProperty", registry.getStringType()),
        new RecordProperty("age", registry.getNumberType())).asMap());
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType4);
    Assert.assertEquals(greatestSubtype, recordType);

    // Check isSubtype
    RecordType recordType5 = new RecordType(registry, Arrays.asList(
        new RecordProperty("name", registry.getStringType()),
        new RecordProperty("age", registry.getNumberType())).asMap());
    Assert.assertTrue(recordType.isSubtype(recordType5));

    // Check isSubtype with conflicting property types
    RecordType recordType6 = new RecordType(registry, Arrays.asList(
        new RecordProperty("name", registry.getNumberType()),
        new RecordProperty("age", registry.getStringType())).asMap());
    Assert.assertFalse(recordType.isSubtype(recordType6));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyFrozen() {
    recordType.defineProperty("newProperty", registry.getStringType(), false, null);
    recordType.isFrozen = true;
    recordType.defineProperty("newProperty", registry.getStringType(), false, null);
  }
}