import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BaseSettingsTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testBaseSettingsConstruction() {
 BaseSettings settings = new BaseSettings(new DefaultClassIntrospector(), new AnnotationIntrospector(),
 JsonAutoDetect.Visibility.ANY(), new PropertyNamingStrategy.Default(), new TypeFactory(),
 new TypeResolverBuilderDefault(), null, new DefaultHandlerInstantiator(), Locale.getDefault(),
 TimeZone.getDefault(), Base64Variants.STANDARD);
 assertEquals(JsonAutoDetect.Visibility.ANY, settings.getVisibilityChecker().getVisibility(PropertyAccessor.FIELD, "fieldName"));
 }

 @Test
 public void testGetters() {
 BaseSettings settings = new BaseSettings(new DefaultClassIntrospector(), new AnnotationIntrospector(),
 JsonAutoDetect.Visibility.ANY(), new PropertyNamingStrategy.Default(), new TypeFactory(),
 new TypeResolverBuilderDefault(), null, new DefaultHandlerInstantiator(), Locale.getDefault(),
 TimeZone.getDefault(), Base64Variants.STANDARD);

 ClassIntrospector ci = settings.getClassIntrospector();
 AnnotationIntrospector ai = settings.getAnnotationIntrospector();
 VisibilityChecker<?> vc = settings.getVisibilityChecker();
 PropertyNamingStrategy pns = settings.getPropertyNamingStrategy();
 TypeFactory tf = settings.getTypeFactory();
 TypeResolverBuilder<?> typer = settings.getTypeResolverBuilder();
 DateFormat df = settings.getDateFormat();
 HandlerInstantiator hi = settings.getHandlerInstantiator();
 Locale locale = settings.getLocale();
 TimeZone timeZone = settings.getTimeZone();
 Base64Variant defaultBase64 = settings.getBase64Variant();

 assertNotNull(ci);
 assertNotNull(ai);
 assertNotNull(vc);
 assertNotNull(pns);
 assertNotNull(tf);
 assertNotNull(typer);
 assertNull(df);
 assertNotNull(hi);
 assertNotNull(locale);
 assertNotNull(timeZone);
 assertSame(Base64Variants.STANDARD, defaultBase64);
 }

 @Test
 public void testSerialization() throws Exception {
 ObjectNode node = MAPPER.writeValueAsTree(new BaseSettings(new DefaultClassIntrospector(), new AnnotationIntrospector(),
 JsonAutoDetect.Visibility.ANY(), new PropertyNamingStrategy.Default(), new TypeFactory(),
 new TypeResolverBuilderDefault(), null, new DefaultHandlerInstantiator(), Locale.getDefault(),
 TimeZone.getDefault(), Base64Variants.STANDARD));
 MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 BaseSettings settings = MAPPER.readValue(node.traverse(), BaseSettings.class);
 assertEquals(JsonAutoDetect.Visibility.ANY, settings.getVisibilityChecker().getVisibility(PropertyAccessor.FIELD, "fieldName"));
 }
}