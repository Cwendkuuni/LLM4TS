package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
import com.fasterxml.jackson.databind.util.StdHandlerInstantiator;

public class BaseSettingsTest {

    @Mock
    private ClassIntrospector classIntrospector;

    @Mock
    private AnnotationIntrospector annotationIntrospector;

    @Mock
    private VisibilityChecker<?> visibilityChecker;

    @Mock
    private PropertyNamingStrategy propertyNamingStrategy;

    @Mock
    private TypeFactory typeFactory;

    @Mock
    private TypeResolverBuilder<?> typeResolverBuilder;

    @Mock
    private DateFormat dateFormat;

    @Mock
    private HandlerInstantiator handlerInstantiator;

    @Mock
    private Locale locale;

    @Mock
    private TimeZone timeZone;

    @Mock
    private Base64Variant defaultBase64;

    @InjectMocks
    private BaseSettings baseSettings;

    @Test
    public void testConstructor() {
        BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);
        assertSame(classIntrospector, settings.getClassIntrospector());
        assertSame(annotationIntrospector, settings.getAnnotationIntrospector());
        assertSame(visibilityChecker, settings.getVisibilityChecker());
        assertSame(propertyNamingStrategy, settings.getPropertyNamingStrategy());
        assertSame(typeFactory, settings.getTypeFactory());
        assertSame(typeResolverBuilder, settings.getTypeResolverBuilder());
        assertSame(dateFormat, settings.getDateFormat());
        assertSame(handlerInstantiator, settings.getHandlerInstantiator());
        assertSame(locale, settings.getLocale());
        assertSame(timeZone, settings.getTimeZone());
        assertSame(defaultBase64, settings.getBase64Variant());
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector newClassIntrospector = mock(ClassIntrospector.class);
        BaseSettings newSettings = baseSettings.withClassIntrospector(newClassIntrospector);
        assertSame(newClassIntrospector, newSettings.getClassIntrospector());
        assertNotSame(classIntrospector, newSettings.getClassIntrospector());
    }

    // Add tests for other setter methods here

    @Test
    public void testGetters() {
        when(classIntrospector.getClassAnnotations()).thenReturn(new Class[] { new JsonAutoDetect() });
        when(annotationIntrospector.findAnnotationValues(any(Class.class), any(AnnotationIntrospector.AnnotatedElement.class), anyInt()))
                .thenReturn(new Object[] { new JsonAutoDetect.Visibility(JsonAutoDetect.Visibility.ANY), null });
        when(visibilityChecker.isVisible(any(), any()))
                .thenAnswer(i -> i.getArguments()[0] instanceof Method ? true : false);
        when(propertyNamingStrategy.findPropertyName(anyString())).thenReturn("testProperty");
        when(typeFactory.constructType(anyString())).thenReturn(Mockito.mock(JavaType.class));
        when(typeResolverBuilder.buildTypeDescriptor(any(Class.class), any()))
                .thenReturn(new TypeDeserializer.Empty());
        when(typeResolverBuilder.buildValueSerializer(any(Class.class), any(), any()))
                .thenReturn(new ObjectMapper.SerializerProvider().getSerializerProvider().findValueSerializer(any(Class.class), null));
        when(dateFormat.getInstance(anyString())).thenReturn(dateFormat);
        when(handlerInstantiator.findHandler(any(Object.class), any(Type.class), any(DeserializationConfig.class)))
                .thenAnswer(i -> Mockito.mock(ObjectMapper.Handler.class));
        when(locale.getDisplayLanguage(any(), any())).thenReturn("en");
        when(locale.getDisplayCountry(any(), any())).thenReturn("US");
        when(timeZone.getID()).thenReturn("UTC");
        when(defaultBase64.getDefault()).thenReturn("base64");

        assertSame("testProperty", baseSettings.getPropertyNamingStrategy().findPropertyName("test"));
        assertSame(classIntrospector, baseSettings.getClassIntrospector());
        assertSame(annotationIntrospector, baseSettings.getAnnotationIntrospector());
        assertSame(visibilityChecker, baseSettings.getVisibilityChecker());
        assertSame(propertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
        assertSame(typeFactory, baseSettings.getTypeFactory());
        assertSame(typeResolverBuilder, baseSettings.getTypeResolverBuilder());
        assertSame(dateFormat, baseSettings.getDateFormat());
        assertSame(handlerInstantiator, baseSettings.getHandlerInstantiator());
        assertSame(locale, baseSettings.getLocale());
        assertSame(timeZone, baseSettings.getTimeZone());
        assertSame(defaultBase64, baseSettings.getBase64Variant());
        assertSame(JsonAutoDetect.Visibility.ANY, baseSettings.getVisibilityChecker().isVisible(any(), any()));
    }
}