import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

 @Test
 public void testBaseSettings() {
 // Create an instance of BaseSettings with default values
 BaseSettings defaultSettings = new BaseSettings(
 new ClassIntrospector(),
 new AnnotationIntrospector(),
 VisibilityChecker.Std(),
 PropertyNamingStrategy.DEFAULT,
 TypeFactory.defaultInstance(),
 TypeResolverBuilder.std(),
 new StdDateFormat(),
 HandlerInstantiator.defaultInstance(),
 Locale.getDefault(),
 TimeZone.getDefault(),
 Base64Variant.DEFAULT);

 // Test getters for default values
 assertEquals(new ClassIntrospector(), defaultSettings.getClassIntrospector());
 assertEquals(new AnnotationIntrospector(), defaultSettings.getAnnotationIntrospector());
 assertEquals(VisibilityChecker.Std(), defaultSettings.getVisibilityChecker());
 assertEquals(PropertyNamingStrategy.DEFAULT, defaultSettings.getPropertyNamingStrategy());
 assertEquals(TypeFactory.defaultInstance(), defaultSettings.getTypeFactory());
 assertEquals(TypeResolverBuilder.std(), defaultSettings.getTypeResolverBuilder());
 assertEquals(new StdDateFormat(), defaultSettings.getDateFormat());
 assertEquals(HandlerInstantiator.defaultInstance(), defaultSettings.getHandlerInstantiator());
 assertEquals(Locale.getDefault(), defaultSettings.getLocale());
 assertEquals(TimeZone.getDefault(), defaultSettings.getTimeZone());
 assertEquals(Base64Variant.DEFAULT, defaultSettings.getBase64Variant());

 // Test construction with custom values
 BaseSettings customSettings = new BaseSettings(
 new ClassIntrospector(),
 new AnnotationIntrospector(),
 VisibilityChecker.Std().withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY),
 PropertyNamingStrategy.CAMEL_CASE,
 TypeFactory.defaultInstance(),
 TypeResolverBuilder.std().withClassResolver(MyCustomClassResolver.class),
 new StdDateFormat(),
 HandlerInstantiator.defaultInstance(),
 Locale.US,
 TimeZone.getTimeZone("UTC"),
 Base64Variant.UTF8);

 // Test getters for custom values
 assertEquals(new ClassIntrospector(), customSettings.getClassIntrospector());
 assertEquals(new AnnotationIntrospector(), customSettings.getAnnotationIntrospector());
 assertEquals(VisibilityChecker.Std().withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY), customSettings.getVisibilityChecker());
 assertEquals(PropertyNamingStrategy.CAMEL_CASE, customSettings.getPropertyNamingStrategy());
 assertEquals(TypeFactory.defaultInstance(), customSettings.getTypeFactory());
 assertEquals(TypeResolverBuilder.std().withClassResolver(MyCustomClassResolver.class), customSettings.getTypeResolverBuilder());
 assertEquals(new StdDateFormat(), customSettings.getDateFormat());
 assertEquals(HandlerInstantiator.defaultInstance(), customSettings.getHandlerInstantiator());
 assertEquals(Locale.US, customSettings.getLocale());
 assertEquals(TimeZone.getTimeZone("UTC"), customSettings.getTimeZone());
 assertEquals(Base64Variant.UTF8, customSettings.getBase64Variant());
 }

 @Test
 public void testWithMethods() {
 // Test withClassIntrospector method
 BaseSettings settingsWithClassIntrospector = defaultSettings.withClassIntrospector(new ClassIntrospector());
 assertEquals(new ClassIntrospector(), settingsWithClassIntrospector.getClassIntrospector());
 assertEquals(defaultSettings.getClassIntrospector(), defaultSettings.getClassIntrospector());

 // Test withAnnotationIntrospector method
 BaseSettings settingsWithAnnotationIntrospector = settingsWithClassIntrospector.withAnnotationIntrospector(new AnnotationIntrospector());
 assertEquals(new AnnotationIntrospector(), settingsWithAnnotationIntrospector.getAnnotationIntrospector());
 assertEquals(new AnnotationIntrospector(), settingsWithClassIntrospector.getAnnotationIntrospector());

 // Test withVisibilityChecker method
 BaseSettings settingsWithVisibilityChecker = settingsWithAnnotationIntrospector.withVisibilityChecker(VisibilityChecker.ANY);
 assertEquals(VisibilityChecker.ANY, settingsWithVisibilityChecker.getVisibilityChecker());
 assertEquals(VisibilityChecker.Std(), defaultSettings.getVisibilityChecker());

 // Test withPropertyNamingStrategy method
 BaseSettings settingsWithPropertyNamingStrategy = settingsWithVisibilityChecker.withPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
 assertEquals(PropertyNamingStrategy.SNAKE_CASE, settingsWithPropertyNamingStrategy.getPropertyNamingStrategy());
 assertEquals(PropertyNamingStrategy.DEFAULT, defaultSettings.getPropertyNamingStrategy());

 // Test withTypeFactory method
 BaseSettings settingsWithTypeFactory = settingsWithPropertyNamingStrategy.withTypeFactory(new TypeFactory());
 assertEquals(new TypeFactory(), settingsWithTypeFactory.getTypeFactory());
 assertEquals(TypeFactory.defaultInstance(), defaultSettings.getTypeFactory());

 // Test withTypeResolverBuilder method
 BaseSettings settingsWithTypeResolverBuilder = settingsWithTypeFactory.withTypeResolverBuilder(TypeResolverBuilder.json());
 assertEquals(TypeResolverBuilder.json(), settingsWithTypeResolverBuilder.getTypeResolverBuilder());
 assertEquals(TypeResolverBuilder.std(), defaultSettings.getTypeResolverBuilder());

 // Test withDateFormat method
 BaseSettings settingsWithDateFormat = settingsWithTypeResolverBuilder.withDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
 assertEquals(new SimpleDateFormat("yyyy-MM-dd"), settingsWithDateFormat.getDateFormat());
 assertEquals(new StdDateFormat(), defaultSettings.getDateFormat());

 // Test withHandlerInstantiator method
 BaseSettings settingsWithHandlerInstantiator = settingsWithDateFormat.withHandlerInstantiator(new HandlerInstantiator() {
 @Override
 public <T> T createHandler(Class<T> handlerType) {
 return null;
 }
 });
 assertEquals(settingsWithDateFormat.getHandlerInstantiator(), settingsWithHandlerInstantiator);
 assertEquals(HandlerInstantiator.defaultInstance(), defaultSettings.getHandlerInstantiator());

 // Test with method with custom values
 BaseSettings settingsWithCustomValues = settingsWithHandlerInstantiator.with(Locale.CHINA)
 .with(TimeZone.getTimeZone("Asia/Shanghai"))
 .with(Base64Variant.URL_SAFE);
 assertEquals(Locale.CHINA, settingsWithCustomValues.getLocale());
 assertEquals(TimeZone.getTimeZone("Asia/Shanghai"), settingsWithCustomValues.getTimeZone());
 assertEquals(Base64Variant.URL_SAFE, settingsWithCustomValues.getBase64Variant());
 assertEquals(defaultSettings.getLocale(), defaultSettings.getLocale());
 assertEquals(defaultSettings.getTimeZone(), defaultSettings.getTimeZone());
 assertEquals(defaultSettings.getBase64Variant(), defaultSettings.getBase64Variant());
 }
}