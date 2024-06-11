import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EnumSerializerTest {

 private EnumSerializer serializer;
 private EnumSerializer withIndexSerializer;
 private EnumSerializer withToStringSerializer;
 private EnumValues enumValues;
 private ObjectNode schemaNode;

 @Before
 public void setUp() throws Exception {
 EnumSerializer.EnumSerializerBuilder builder = new EnumSerializer.EnumSerializerBuilder(MyEnum.class);
 this.serializer = builder.build();
 this.enumValues = builder.getEnumValues();

 // Test with index serialization
 EnumSerializer.EnumSerializerBuilder indexBuilder = new EnumSerializer.EnumSerializerBuilder(MyEnum.class, true);
 this.withIndexSerializer = indexBuilder.build();

 // Test with toString serialization
 EnumSerializer.EnumSerializerBuilder toStringBuilder = new EnumSerializer.EnumSerializerBuilder(MyEnum.class, false);
 this.withToStringSerializer = toStringBuilder.build();
 }

 @Test
 public void testSerializeEnum() throws JsonProcessingException {
 JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0, null);

 // Test serialization with index
 generator.writeStartObject();
 this.withIndexSerializer.serialize(MyEnum.VALUE1, generator, null);
 generator.writeEndObject();
 ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutputFullBuffer();
 assertEquals("[\"0\"]", byteArrayOutputStream.toString());

 // Test serialization with toString
 generator.writeStartObject();
 this.withToStringSerializer.serialize(MyEnum.VALUE1, generator, null);
 generator.writeEndObject();
 ByteArrayOutputStream toStringByteArrayOutputStream = (ByteArrayOutputStream) generator.getOutputFullBuffer();
 assertEquals("[\"" + MyEnum.VALUE1.toString() + "\"]", toStringByteArrayOutputStream.toString());

 // Test serialization with name
 generator.writeStartObject();
 this.serializer.serialize(MyEnum.VALUE1, generator, null);
 generator.writeEndObject();
 ByteArrayOutputStream nameByteArrayOutputStream = (ByteArrayOutputStream) generator.getOutputFullBuffer();
 assertEquals("[\"" + this.enumValues.serializedValueFor(MyEnum.VALUE1) + "\"]", nameByteArrayOutputStream.toString());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode tree = mapper.createObjectNode();
 JsonFormat.Value format = new JsonFormat.Value();
 JsonFormatVisitorWrapper visitorWrapper = mapper.schemaFactory().getJsonParser().acceptRootValue();

 // Test serialization with index
 visitorWrapper.acceptObjectFormat(MyEnum.class, new ClassType(ObjectNode.class), format);
 JsonStringFormatVisitor stringVisitor = visitorWrapper.expectStringFormat(MyEnum.class);
 assertNull(stringVisitor);

 // Test serialization with toString
 visitorWrapper.acceptObjectFormat(MyEnum.class, new ClassType(ObjectNode.class), null);
 stringVisitor = visitorWrapper.expectStringFormat(MyEnum.class);
 assertNotNull(stringVisitor);
 Set<String> enums = stringVisitor.enumTypes();
 assertEquals(EnumSet.allOf(MyEnum.class), EnumSet.copyOf(enums));
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 JsonNode schema = this.serializer.getSchema(mapper.getSerializationConfig(), MyEnum.class);
 assertNotNull(schema);
 assertEquals("{\"type\":\"object\",\"properties\":{\"type\":\"array\",\"items\":{\"type\":\"string\"}},\"discriminatorPropertyObject\":false}", schema.toString());
 }
}

 enum MyEnum {
 @JsonFormat(shape = JsonFormat.Shape.STRING)
 VALUE1,
 @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 VALUE2;
}

 class EnumSerializerBuilder {
 private final Class<Enum<?>> enumClass;
 private final Boolean serializeAsIndex;

 public EnumSerializerBuilder(Class<Enum<?>> enumClass) {
 this.enumClass = enumClass;
 this.serializeAsIndex = null;
 }

 public EnumSerializerBuilder(Class<Enum<?>> enumClass, Boolean serializeAsIndex) {
 this.enumClass = enumClass;
 this.serializeAsIndex = serializeAsIndex;
 }

 public EnumSerializer build() {
 try {
 ObjectMapper mapper = new ObjectMapper();
 JavaSerializerContext context = mapper.getSerializerProvider().getSerializerProvider().getSerializerProviderContext();
 return new EnumSerializer(EnumValues.constructFromName(context, this.enumClass), this.serializeAsIndex);
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 public EnumValues getEnumValues() {
 return EnumValues.constructFromName(MyEnum.class);
 }
}