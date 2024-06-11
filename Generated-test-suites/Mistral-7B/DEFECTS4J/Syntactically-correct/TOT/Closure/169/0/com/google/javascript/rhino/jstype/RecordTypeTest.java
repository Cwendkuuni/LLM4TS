import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;

import static com.google.common.truth.Truth.assertThat;

public class RecordTypeTest {

  @Test
  public void testRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("x", new RecordProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
    properties.put("y", new RecordProperty("y", registry.getNativeType(JSTypeNative.STRING_TYPE)));

    RecordType recordType = new RecordType(registry, properties, true);

    // Test property access
    assertThat(recordType.getPropertyType("x")).isInstanceOf(JSType.class);
    assertThat(recordType.getPropertyType("x")).isEqualTo(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    assertThat(recordType.getPropertyType("y")).isInstanceOf(JSType.class);
    assertThat(recordType.getPropertyType("y")).isEqualTo(registry.getNativeType(JSTypeNative.STRING_TYPE));

    // Test record type equivalence
    RecordProperty xProp = new RecordProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE));
    RecordProperty yProp = new RecordProperty("y", registry.getNativeType(JSTypeNative.STRING_TYPE));
    Map<String, RecordProperty> otherProperties = Maps.newHashMap();
    otherProperties.put("x", xProp);
    otherProperties.put("y", yProp);
    RecordType otherRecordType = new RecordType(registry, otherProperties, false);
    TypeEquivalenceTester.assertTypesAreEquivalent(recordType, otherRecordType);

    // Test record type subtyping
    RecordType subtypeRecord = new RecordType(registry, properties, false);
    RecordType supertypeRecord = new RecordType(registry, Arrays.asList("x", "y", "z").stream()
        .collect(Maps.toMap(s -> s, i -> new RecordProperty(s, registry.getNativeType(JSTypeNative.NUMBER_TYPE)))));
    JSType supertype = supertypeRecord.getImplicitPrototype();
    assertThat(subtypeRecord.isSubtype(supertype)).isTrue();
    JSType subtype = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    JSType subtypeUnion = new UnionType(Arrays.asList(subtype, registry.getNativeType(JSTypeNative.STRING_TYPE)));
    JSType supertypeRecordSubtype = supertypeRecord.getGreatestSubtypeHelper(subtypeUnion);
    assertThat(supertypeRecordSubtype.isEquivalentTo(subtypeRecord)).isTrue();

    // Test record type compatibility
    RecordType recordWithInt = new RecordType(registry, properties, true);
    RecordType recordWithString = new RecordType(registry, properties, true);
    RecordType recordWithIntAndString = new RecordType(registry, Arrays.asList("x", "y").stream()
        .collect(Maps.toMap(s -> s, i -> new RecordProperty(s, registry.getNativeType(JSTypeNative.NUMBER_TYPE)))));
    JSType recordWithIntType = recordWithInt.getImplicitPrototype();
    JSType recordWithStringType = recordWithString.getImplicitPrototype();
    JSType recordWithIntAndStringType = recordWithIntAndString.getImplicitPrototype();
    assertThat(recordWithInt.isSubtype(recordWithIntType)).isTrue();
    assertThat(recordWithInt.isSubtype(recordWithIntAndStringType)).isTrue();
    assertThat(recordWithString.isSubtype(recordWithIntType)).isFalse();
    assertThat(recordWithString.isSubtype(recordWithIntAndStringType)).isFalse();
    assertThat(recordWithIntAndString.isSubtype(recordWithIntType)).isTrue();
    assertThat(recordWithIntAndString.isSubtype(recordWithIntAndStringType)).isTrue();
    assertThat(recordWithInt.isSubtype(recordWithString)).isFalse();
    assertThat(recordWithInt.isSubtype(recordWithIntAndString)).isTrue();
    assertThat(recordWithString.isSubtype(recordWithInt)).isFalse();
    assertThat(recordWithString.isSubtype(recordWithIntAndString)).isFalse();
  }

  @Test
  public void testRecordTypeWithIncompatibleProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("x", new RecordProperty("x", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
    properties1.put("y", new RecordProperty("y", registry.getNativeType(JSTypeNative.STRING_TYPE)));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("x", new RecordProperty("x", registry.getNativeType(JSTypeNative.STRING_TYPE)));
    properties2.put("z", new RecordProperty("z", registry.getNativeType(JSTypeNative.NUMBER_TYPE)));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertThat(recordType1.checkRecordEquivalenceHelper(recordType2, false)).isFalse();
  }
}