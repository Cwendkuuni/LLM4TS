import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeChecker.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.TypeChecker;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.testing.TypeCheckTestBase;

import java.util.Map;

public class RecordTypeTest extends TypeCheckTestBase {

  @Override
  public JSTypeRegistry getJSTypeRegistry() {
    return new JSTypeRegistry() {
      @Override
      public JSTypeRegistry getNativeType(String name) {
        if (name.equals(JSTypeNative.OBJECT_TYPE.getName())) {
          return this;
        }
        return super.getNativeType(name);
      }

      @Override
      public JSType getNativeObjectType(String name) {
        if (name.equals(JSTypeNative.OBJECT_TYPE.getName())) {
          return JSTypeNative.OBJECT_TYPE;
        }
        return super.getNativeObjectType(name);
      }

      @Override
      public JSTypeRegistry getJSNamespace(String ns) {
        if (ns.equals("JSTypeTest")) {
          return this;
        }
        return super.getJSNamespace(ns);
      }
    };
  }

  @org.junit.Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", getIntType()));
    properties.put("b", new RecordProperty("b", getStringType()));

    JSTypeRegistry registry = getJSTypeRegistry();
    RecordType recordType = new RecordType(registry, properties);

    assertEquals("{ a: int, b: string }", recordType.toString());
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", getIntType()));
    propertiesA.put("b", new RecordProperty("b", getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", getIntType()));
    propertiesB.put("b", new RecordProperty("b", getStringType()));

    JSTypeRegistry registry = getJSTypeRegistry();
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", getIntType()));
    propertiesA.put("b", new RecordProperty("b", getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", getIntType()));
    propertiesB.put("b", new RecordProperty("b", getIntType()));

    JSTypeRegistry registry = getJSTypeRegistry();
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @org.junit.Test
  public void testRecordTypeMismatchedProperties() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", getIntType()));
    propertiesA.put("b", new RecordProperty("b", getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", getStringType()));
    propertiesB.put("c", new RecordProperty("c", getIntType()));

    JSTypeRegistry registry = getJSTypeRegistry();
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @org.junit.Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", getIntType()));
    properties.put("b", new RecordProperty("b", getStringType()));

    JSTypeRegistry registry = getJSTypeRegistry();
    RecordType recordType = new RecordType(registry, properties);

    recordType.defineProperty("c", getIntType(), false, null);

    assertFalse(recordType.isFrozen());

    recordType.freeze();

    try {
      recordType.defineProperty("c", getIntType(), false, null);
      fail("Expected definition of property 'c' to fail.");
    } catch (IllegalStateException e) {
      // Expected
    }

    assertTrue(recordType.isFrozen());
  }

  @org.junit.Test
  public void testRecordTypeIsEquivalentTo() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", getIntType()));
    propertiesA.put("b", new RecordProperty("b", getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty("b", getStringType()));
    propertiesB.put("a", new RecordProperty("a", getIntType()));

    JSTypeRegistry registry = getJSTypeRegistry();
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @org.junit.Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", getIntType()));
    propertiesA.put("b", new RecordProperty("b", getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", getIntType()));
    propertiesB.put("b", new RecordProperty("b", getStringType()));
    propertiesB.put("c", new RecordProperty("c", getBooleanType()));

    JSTypeRegistry registry = getJSTypeRegistry();
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
    assertEquals("{ a: int, b: string, c: boolean }", greatestSubtype.toString());
  }

  @org.junit.Test
  public void testRecordTypeLeastSupertype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", getIntType()));
    propertiesA.put("b", new RecordProperty("b", getStringType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty("b", getStringType()));
    propertiesB.put("c", new RecordProperty("c", getIntType()));

    JSTypeRegistry registry = getJSTypeRegistry();
    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    JSType leastSupertype = recordTypeB.getLeastSupertype(recordTypeA);
    assertEquals("{ a: int, b: string }", leastSupertype.toString());
  }
}