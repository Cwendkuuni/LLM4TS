import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StandardConverter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

public class DatabindContextTest {

 private DeserializationContext context;
 private ObjectMapper objectMapper;

 @Before
 public void setup() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.disable(MapperFeature.AUTO_DETECT_TYPES);
 mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 objectMapper = mapper.getDeserializationConfig().withContext(new BaseContext());
 context = objectMapper.getDeserializationContext();
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> config = context.getConfig();
 Assert.assertNotNull(config);
 }

 @Test
 public void testIsEnabled() {
 Assert.assertTrue(context.isEnabled(MapperFeature.DEFAULT_LOCALE));
 Assert.assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATOR));
 }

 @Test
 public void testCanOverrideAccessModifiers() {
 Assert.assertTrue(context.canOverrideAccessModifiers());
 }

 @Test
 public void testGetActiveView() {
 Assert.assertNull(context.getActiveView());
 }

 @Test
 public void testGetLocale() {
 Assert.assertEquals(Locale.getDefault(), context.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 Assert.assertEquals(TimeZone.getDefault(), context.getTimeZone());
 }

 @Test
 public void testgetDefaultPropertyFormat() throws IOException {
 JavaType javaType = context.getTypeFactory().constructType(String.class);
 Assert.assertNotNull(context.getDefaultPropertyFormat(javaType));
 }

 @Test
 public void testSetAttribute() {
 Object key = new Object();
 Object value = new Object();
 context.setAttribute(key, value);
 Object testValue = context.getAttribute(key);
 Assert.assertEquals(value, testValue);
 }

 @Test
 public void testConstructType() throws IOException {
 Type type = new TypeReference<Object>() {}.getType();
 JavaType javaType = context.getTypeFactory().constructType(type);
 Assert.assertNotNull(javaType);
 }

 @Test
 public void testResolveSubType() throws IOException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 JavaType subType = context.resolveSubType(baseType, "java.util.ArrayList");
 Assert.assertNotNull(subType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws IOException {
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 String typeId = "java.util.ArrayList";
 context.invalidTypeIdException(baseType, typeId, null);
 }

 @Test
 public void testTypeFactory() throws IOException {
 TypeFactory typeFactory = context.getTypeFactory();
 Assert.assertNotNull(typeFactory);
 }

 @Test
 public void testObjectIdGeneratorInstance() throws IOException {
 Annotated annotated = null;
 ObjectIdInfo objectIdInfo = null;
 ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
 Assert.assertNull(objectIdGenerator);
 }

 @Test
 public void testObjectIdResolverInstance() throws IOException {
 Annotated annotated = null;
 ObjectIdInfo objectIdInfo = null;
 ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
 Assert.assertNull(objectIdResolver);
 }

 @Test
 public void testConverterInstance() throws IOException {
 Annotated annotated = null;
 Object converterDef = StandardConverter.Instance.class;
 Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
 Assert.assertNotNull(converter);
 }

 @Test
 public void testReportBadDefinition() throws IOException {
 JavaType type = context.getTypeFactory().constructType(String.class);
 String msg = "bad definition";
 Object badDef = new Object();
 Assert.assertNull(context.reportBadDefinition(type, msg));
 }

 @Test(expected = JsonMappingException.class)
 public void testReportBadDefinitionWithException() throws IOException {
 JavaType type = context.getTypeFactory().constructType(String.class);
 String msg = "bad definition";
 Object badDef = new Object();
 context.reportBadDefinition(type, msg, Id.CLASS);
 }

 @Test
 public void testFormat() {
 String msg = "message";
 Object[] msgArgs = {"Arg1", "Arg2"};
 String formattedMsg = context._format(msg, msgArgs);
 Assert.assertEquals("message: Arg1, Arg2", formattedMsg);
 }

 @Test
 public void testTruncate() {
 String desc = "description longer than max length";
 String truncatedDesc = context._truncate(desc);
 Assert.assertEquals("description longer than max length]...[description longer than max length", truncatedDesc);
 }

 @Test
 public void testQuotedString() {
 String desc = "description with double quotes";
 String quotedDesc = context._quotedString(desc);
 Assert.assertEquals("\"description with double quotes\"", quotedDesc);
 }

 @Test
 public void testColonConcat() {
 String msgBase = "message base";
 String extra = "extra description";
 String concatenatedMsg = context._colonConcat(msgBase, extra);
 Assert.assertEquals(msgBase + ": extra description", concatenatedMsg);
 }

 @Test
 public void testDesc() {
 String desc = "description";
 String truncatedDesc = context._desc(desc);
 Assert.assertEquals("description", truncatedDesc);
 }
}