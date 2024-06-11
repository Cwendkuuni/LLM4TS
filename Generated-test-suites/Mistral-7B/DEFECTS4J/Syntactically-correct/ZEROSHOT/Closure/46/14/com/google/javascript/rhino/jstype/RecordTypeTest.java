import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeFactory.newRecordType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.newType;
import static com.google.javascript.rhino.jstype.JSTypeFactory.unionType;
import static com.google.javascript.rhino.node.AbstractNode.newProperty;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.UnionType;

import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  private static JSTypeRegistry registry;
  private static ErrorReporter reporter;
  private static StaticScope<JSType> scope;

  static {
    registry = new JSTypeRegistry();
    reporter = new ErrorReporter();
    scope = new StaticScope<>(registry);
  }

  @Test
  public void testCreateRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", newType(Number.class)));
    properties.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType = newRecordType(registry, properties);
    assertNotNull(recordType);
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", newType(Number.class)));
    properties1.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType1 = newRecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", newType(Number.class)));
    properties2.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType2 = newRecordType(registry, properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsNotEquivalentToDifferentProperties() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", newType(Number.class)));
    properties1.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType1 = newRecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", newType(Number.class)));
    properties2.put("c", new RecordProperty("c", newType(Boolean.class)));

    RecordType recordType2 = newRecordType(registry, properties2);

    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", newType(Number.class)));
    properties1.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType1 = newRecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", newType(Number.class)));

    RecordType recordType2 = newRecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", newType(String.class)));

    RecordType recordType3 = newRecordType(registry, properties3);

    assertTrue(recordType1.isSubtype(recordType3));

    RecordType recordType4 = newRecordType(registry, properties1);
    recordType4.freeze();

    Map<String, RecordProperty> properties5 = Maps.newHashMap();
    properties5.put("a", new RecordProperty("a", newType(Number.class)));
    properties5.put("c", new RecordProperty("c", newType(Boolean.class)));

    RecordType recordType5 = newRecordType(registry, properties5);

    assertFalse(recordType1.isSubtype(recordType5));
  }

  @Test
  public void testGetLeastSupertype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", newType(Number.class)));
    properties1.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType1 = newRecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", newType(Number.class)));
    properties2.put("c", new RecordProperty("c", newType(Boolean.class)));

    RecordType recordType2 = newRecordType(registry, properties2);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);

    assertEquals(recordType2.getClass(), leastSupertype.getClass());
  }

  @Test
  public void testGetLeastSupertypeSameRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", newType(Number.class)));

    RecordType recordType1 = newRecordType(registry, properties);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType1);

    assertEquals(recordType1.getClass(), leastSupertype.getClass());
  }

  @Test
  public void testGetLeastSupertypeDifferentRecordTypesWithCommonProperties() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", newType(Number.class)));
    properties1.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType1 = newRecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", newType(Number.class)));
    properties2.put("c", new RecordProperty("c", newType(Boolean.class)));

    RecordType recordType2 = newRecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", newType(Number.class)));
    properties3.put("b", new RecordProperty("b", newType(Boolean.class)));
    properties3.put("d", new RecordProperty("d", newType(String.class)));

    RecordType recordType3 = newRecordType(registry, properties3);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType3);

    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("a", newType(Number.class), newProperty("a"));
    builder.addProperty("b", newType(Boolean.class), newProperty("b"));

    RecordType expectedLeastSupertype = builder.build();

    assertEquals(expectedLeastSupertype.getClass(), leastSupertype.getClass());
    assertEquals(expectedLeastSupertype, leastSupertype);
  }

  @Test
  public void testGetLeastSupertypeDifferentRecordTypesWithoutCommonProperties() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", newType(Number.class)));
    properties1.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType1 = newRecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("c", new RecordProperty("c", newType(Boolean.class)));
    properties2.put("d", new RecordProperty("d", newType(String.class)));

    RecordType recordType2 = newRecordType(registry, properties2);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getClass(),
        leastSupertype.getClass());
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", newType(Number.class)));
    properties1.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType1 = newRecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", newType(Number.class)));
    properties2.put("c", new RecordProperty("c", newType(Boolean.class)));

    RecordType recordType2 = newRecordType(registry, properties2);

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("a", new RecordProperty("a", newType(Number.class)));

    RecordType recordType3 = newRecordType(registry, properties3);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(recordType1.getClass(), greatestSubtype.getClass());

    greatestSubtype = recordType1.getGreatestSubtype(recordType3);

    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("a", newType(Number.class), newProperty("a"));

    UnionType unionType = unionType(new JSType[]{recordType2, recordType3});

    builder.addProperty("c", unionType.getBaseType(), newProperty("c"));

    RecordType expectedGreatestSubtype = builder.build();

    assertEquals(expectedGreatestSubtype.getClass(), greatestSubtype.getClass());
    assertEquals(expectedGreatestSubtype, greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeSameRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", newType(Number.class)));

    RecordType recordType1 = newRecordType(registry, properties);

    RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType1);

    assertEquals(recordType1.getClass(), greatestSubtype.getClass());
  }

  @Test
  public void testGetGreatestSubtypeNoCommonProperties() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", newType(Number.class)));
    properties1.put("b", new RecordProperty("b", newType(Boolean.class)));

    RecordType recordType1 = newRecordType(registry, properties1);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("c", new RecordProperty("c", newType(Boolean.class)));
    properties2.put("d", new RecordProperty("d", newType(String.class)));

    RecordType recordType2 = newRecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE).getClass(),
        greatestSubtype.getClass());
  }
}