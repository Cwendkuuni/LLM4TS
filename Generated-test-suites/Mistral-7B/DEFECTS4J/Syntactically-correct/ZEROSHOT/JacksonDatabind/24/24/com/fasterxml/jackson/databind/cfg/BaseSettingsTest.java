package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.HandlerInstantiator;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeResolverBuilder;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector.BaseAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        ClassIntrospector classIntrospector = mock(ClassIntrospector.class);
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        VisibilityChecker<?> visibilityChecker = Std.defaultInstance();
        PropertyNamingStrategy propertyNamingStrategy = mock(PropertyNamingStrategy.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        TypeResolverBuilder<?> typeResolverBuilder = mock(TypeResolverBuilder.class);
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = mock(HandlerInstantiator.class);
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant base64Variant = Base64Variant.STANDARD;

        BaseSettings baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale,
                timeZone, base64Variant);

        // Test getters
        assertNotNull(baseSettings.getClassIntrospector());
        assertNotNull(baseSettings.getAnnotationIntrospector());
        assertNotNull(baseSettings.getVisibilityChecker());
        assertNotNull(baseSettings.getPropertyNamingStrategy());
        assertNotNull(baseSettings.getTypeFactory());
        assertNotNull(baseSettings.getTypeResolverBuilder());
        assertNotNull(baseSettings.getDateFormat());
        assertNotNull(baseSettings.getHandlerInstantiator());
        assertEquals(locale, baseSettings.getLocale());
        assertEquals(timeZone, baseSettings.getTimeZone());
        assertEquals(base64Variant, baseSettings.getBase64Variant());

        // Test with methods
        BaseAnnotationIntrospector baseAnnotationIntrospector = mock(BaseAnnotationIntrospector.class);
        AnnotationIntrospectorPair annotationIntrospectorPair = new AnnotationIntrospectorPair(annotationIntrospector,
                baseAnnotationIntrospector);
        BaseSettings newBaseSettings = baseSettings.withAnnotationIntrospector(annotationIntrospectorPair);
        assertEquals(newBaseSettings, baseSettings.withAnnotationIntrospector(annotationIntrospectorPair));

        // Test with methods with mocks
        PropertyAccessor forMethod = PropertyAccessor.FIELD;
        JsonAutoDetect.Visibility newVisibility = JsonAutoDetect.Visibility.ANY;
        BaseSettings newBaseSettings1 = baseSettings.withVisibility(forMethod, newVisibility);
        assertEquals(newBaseSettings1, baseSettings.withVisibility(forMethod, newVisibility));

        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeFactory.constructTypeIdResolver()).thenReturn(typeIdResolver);
        BaseSettings newBaseSettings2 = baseSettings.withTypeFactory(new TypeFactory() {
            @Override
            public <T> TypeIdResolver<T> constructTypeIdResolver() {
                return typeIdResolver;
            }
        });
        assertEquals(newBaseSettings2, baseSettings.withTypeFactory(new TypeFactory() {
            @Override
            public <T> TypeIdResolver<T> constructTypeIdResolver() {
                return typeIdResolver;
            }
        }));

        // Test withDateFormat method
        DateFormat newDateFormat = new StdDateFormat();
        BaseSettings newBaseSettings3 = baseSettings.withDateFormat(newDateFormat);
        assertEquals(newBaseSettings3, baseSettings.withDateFormat(newDateFormat));

        // Test withHandlerInstantiator method
        HandlerInstantiator newHandlerInstantiator = mock(HandlerInstantiator.class);
        BaseSettings newBaseSettings4 = baseSettings.withHandlerInstantiator(newHandlerInstantiator);
        assertEquals(newBaseSettings4, baseSettings.withHandlerInstantiator(newHandlerInstantiator));

        // Test with method with Locale
        Locale newLocale = Locale.US;
        BaseSettings newBaseSettings5 = baseSettings.with(newLocale);
        assertEquals(newBaseSettings5, baseSettings.with(newLocale));

        // Test with method with TimeZone
        TimeZone newTimeZone = TimeZone.getTimeZone("UTC");
        BaseSettings newBaseSettings6 = baseSettings.with(newTimeZone);
        assertEquals(newBaseSettings6, baseSettings.with(newTimeZone));

        // Test with method with Base64Variant
        Base64Variant newBase64Variant = Base64Variant.BASE64_EXTENDED;
        BaseSettings newBaseSettings7 = baseSettings.with(newBase64Variant);
        assertEquals(newBaseSettings7, baseSettings.with(newBase64Variant));
    }
}