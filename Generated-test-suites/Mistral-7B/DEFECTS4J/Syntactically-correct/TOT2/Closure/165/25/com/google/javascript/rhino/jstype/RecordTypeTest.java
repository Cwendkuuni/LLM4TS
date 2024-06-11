import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, recordProperties);

    Assert.assertEquals(String.class, recordType.getPropertyType("name"));
    Assert.assertEquals(Integer.class, recordType.getPropertyType("age"));
    Assert.assertTrue(recordType.isEquivalentTo(recordType));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new HashMap<>();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordPropertiesB = new HashMap<>();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Integer.class));
    recordPropertiesB.put("city", new RecordProperty("city", String.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    Assert.assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, recordProperties);

    recordType.defineProperty("city", String.class, false, Node.NULL);
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new HashMap<>();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordPropertiesB = new HashMap<>();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Integer.class));
    recordPropertiesB.put("city", new RecordProperty("city", String.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);

    Assert.assertEquals(recordTypeB, greatestSubtype);
  }

  @Test
  public void testRecordTypeIsEquivalentTo() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> recordPropertiesA = new HashMap<>();
    recordPropertiesA.put("name", new RecordProperty("name", String.class));
    recordPropertiesA.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> recordPropertiesB = new HashMap<>();
    recordPropertiesB.put("name", new RecordProperty("name", String.class));
    recordPropertiesB.put("age", new RecordProperty("age", Integer.class));
    recordPropertiesB.put("city", new RecordProperty("city", String.class));

    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    RecordType recordTypeC = new RecordType(registry, recordPropertiesB);

    RecordType recordTypeD = new RecordType(registry, recordPropertiesA);

    Assert.assertTrue(recordTypeA.isEquivalentTo(recordTypeD));
    Assert.assertTrue(recordTypeA.isEquivalentTo(recordTypeA));
    Assert.assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
  }
}