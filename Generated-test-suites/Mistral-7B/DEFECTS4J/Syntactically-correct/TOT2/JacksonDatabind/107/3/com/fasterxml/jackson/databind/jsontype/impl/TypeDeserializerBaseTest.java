import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

private ObjectMapper objectMapper;

@Before
public void setup() {
objectMapper = new ObjectMapper();
objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
}

@Test
public void testForProperty() throws Exception {
JsonParser jsonParser = objectMapper.getFactory().createParser("{\"foo\": \"bar\"}");
TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(jsonParser.getCodec().getTypeFactory().constructType(MyType.class), null, "foo", true, MyDefaultType.class);
TypeDeserializer<MyType> typeDeserializer = typeDeserializerBase.forProperty(null);
assertEquals(MyType.class, typeDeserializer.getType(null));
}

@Test
public void testToString() throws Exception {
TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
assertEquals("[TypeDeserializerBase; base-type:null; id-resolver:null]", typeDeserializerBase.toString());
}

@Test
public void testFindDeserializerUnknownTypeId() throws Exception {
DeserializationContext context = objectMapper.getDeserializationContext();
TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
assertNull(typeDeserializerBase._findDeserializer(context, "unknownTypeId"));
}

@Test
public void testFindDeserializerDefaultImpl() throws Exception {
DeserializationContext context = objectMapper.getDeserializationContext();
TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
assertEquals(NullifyingDeserializer.instance, typeDeserializerBase._findDeserializer(context, null));
}

@Test
public void testFindDeserializerWithTypeId() throws Exception {
DeserializationContext context = objectMapper.getDeserializationContext();
TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
MyJsonDeserializer myJsonDeserializer = new MyJsonDeserializer();
context.setValueHandler(new StdDateFormatValueHandler(new SimpleDateFormat("yyyy-MM-dd")));
typeDeserializerBase._deserializers.put("myTypeId", myJsonDeserializer);
assertEquals(myJsonDeserializer, typeDeserializerBase._findDeserializer(context, "myTypeId"));
}

@Test(expected = IOException.class)
public void testHandleUnknownTypeId() throws Exception {
DeserializationContext context = objectMapper.getDeserializationContext();
TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
String extraDesc = "custom error message";
typeDeserializerBase._handleUnknownTypeId(context, "unknownTypeId");
}

@Test
public void testHandleMissingTypeId() throws Exception {
DeserializationContext context = objectMapper.getDeserializationContext();
TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
String extraDesc = "custom error message";
typeDeserializerBase._handleMissingTypeId(context, extraDesc);
}
}

class MyType {}
class MyTypeDeserializer extends TypeDeserializer {
@Override
public MyType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
return new MyType();
}
}
class MyJsonDeserializer extends JsonDeserializer<MyType> {
@Override
public MyType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
return new MyType();
}
}