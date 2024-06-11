package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() throws Exception {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Locale locale = Locale.ENGLISH;
        Base64Variant base64 = Base64Variant.STANDARD;
        DateFormat dateFormat = StdDateFormat.DEFAULT_INSTANCE;

        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public <T> T createHandler(Class<T> handlerType) {
                return (T) new JacksonAnnotationIntrospector();
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationFeature(SerializationFeature.FailOnEmptyBeans, false);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        mapper.setVisibilityChecker(mapper.getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE));
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategyDefaultImpl());
        mapper.setTypeFactory(new TypeFactoryBuilder().build());
        mapper.setTypeResolverBuilder(new JacksonTypeResolverBuilder());
        mapper.setDateFormat(dateFormat);
        mapper.setHandlerInstantiator(handlerInstantiator);
        mapper.setLocale(locale);
        mapper.setTimeZone(tz);
        mapper.setDefaultBase64Variant(base64);

        BaseSettings settings = mapper.readValue(getClass().getResource("/BaseSettingsTest.json"), BaseSettings.class);
        assertNotNull(settings._classIntrospector);
        assertNotNull(settings._annotationIntrospector);
        assertNotNull(settings._visibilityChecker);
        assertNotNull(settings._propertyNamingStrategy);
        assertNotNull(settings._typeFactory);
        assertNotNull(settings._typeResolverBuilder);
        assertNotNull(settings._dateFormat);
        assertNotNull(settings._handlerInstantiator);
        assertNotNull(settings._locale);
        assertNotNull(settings._timeZone);
        assertNotNull(settings._defaultBase64);

        assertEquals(locale, settings.getLocale());
        assertEquals(tz, settings.getTimeZone());
        assertEquals(base64, settings.getDefaultBase64());

        NamedType nestedType = TypeFactory.defaultInstance().constructType(MyNested.class);
        assertEquals(nestedType, settings.getTypeFactory().constructType(MyNested.class));

        MyNested nested = mapper.readValue(getClass().getResource("/MyNested.json"), MyNested.class);
        assertEquals(1, nested.getIntField());
        assertEquals("Hello", nested.getStringField());
        assertEquals("World", nested.getStringField2());
    }

    static class MyNested {
        @JsonProperty("intField")
        int intField;
        String stringField;
        @JsonProperty("stringField2")
        String stringField2;
    }
}