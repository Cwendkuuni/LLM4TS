package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

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

/**
 * Test class for BaseSettings
 */
public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Test
    public void testBaseSettings() {
        // Test initialization with default values
        ClassIntrospector ci = null;
        AnnotationIntrospector ai = null;
        VisibilityChecker<?> vc = null;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = null;
        TypeResolverBuilder<?> typer = null;
        DateFormat df = null;
        HandlerInstantiator hi = null;
        Locale locale = null;
        TimeZone timeZone = null;
        Base64Variant defaultBase64 = null;

        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, timeZone, defaultBase64);

        // Test getters
        assertNull(baseSettings.getClassIntrospector());
        assertNull(baseSettings.getAnnotationIntrospector());
        assertNull(baseSettings.getVisibilityChecker());
        assertNull(baseSettings.getPropertyNamingStrategy());
        assertNull(baseSettings.getTypeFactory());
        assertNull(baseSettings.getTypeResolverBuilder());
        assertNull(baseSettings.getDateFormat());
        assertNull(baseSettings.getHandlerInstantiator());
        assertNull(baseSettings.getLocale());
        assertNull(baseSettings.getTimeZone());
        assertNull(baseSettings.getBase64Variant());

        // Test setters
        ci = new ClassIntrospector();
        ai = new AnnotationIntrospector();
        vc = new VisibilityChecker<>();
        pns = new PropertyNamingStrategy();
        tf = new TypeFactory();
        typer = new SimpleTypeResolverBuilder();
        df = new StdDateFormat();
        hi = new SimpleHandlerInstantiator();
        locale = Locale.getDefault();
        timeZone = TimeZone.getDefault();

        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, timeZone, defaultBase64);

        BaseSettings withClassIntrospector = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, withClassIntrospector.getClassIntrospector());
        assertNotSame(baseSettings, withClassIntrospector);

        BaseSettings withAnnotationIntrospector = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, withAnnotationIntrospector.getAnnotationIntrospector());
        assertNotSame(baseSettings, withAnnotationIntrospector);

        BaseSettings withInsertedAnnotationIntrospector = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertEquals(ai, withInsertedAnnotationIntrospector.getAnnotationIntrospector());
        assertNotSame(baseSettings, withInsertedAnnotationIntrospector);

        BaseSettings withAppendedAnnotationIntrospector = baseSettings.withAppendedAnnotationIntrospector(ai);
        assertEquals(ai, withAppendedAnnotationIntrospector.getAnnotationIntrospector());
        assertNotSame(baseSettings, withAppendedAnnotationIntrospector);

        BaseSettings withVisibilityChecker = baseSettings.withVisibilityChecker(vc);
        assertEquals(vc, withVisibilityChecker.getVisibilityChecker());
        assertNotSame(baseSettings, withVisibilityChecker);

        BaseSettings withPropertyNamingStrategy = baseSettings.withPropertyNamingStrategy(pns);
        assertEquals(pns, withPropertyNamingStrategy.getPropertyNamingStrategy());
        assertNotSame(baseSettings, withPropertyNamingStrategy);

        BaseSettings withTypeFactory = baseSettings.withTypeFactory(tf);
        assertEquals(tf, withTypeFactory.getTypeFactory());
        assertNotSame(baseSettings, withTypeFactory);

        BaseSettings withTypeResolverBuilder = baseSettings.withTypeResolverBuilder(typer);
        assertEquals(typer, withTypeResolverBuilder.getTypeResolverBuilder());
        assertNotSame(baseSettings, withTypeResolverBuilder);

        DateFormat dateFormat = new StdDateFormat();
        BaseSettings withDateFormat = baseSettings.withDateFormat(dateFormat);
        assertEquals(dateFormat, withDateFormat.getDateFormat());
        assertNotSame(baseSettings, withDateFormat);

        HandlerInstantiator handlerInstantiator = new SimpleHandlerInstantiator();
        BaseSettings withHandlerInstantiator = baseSettings.withHandlerInstantiator(handlerInstantiator);
        assertEquals(handlerInstantiator, withHandlerInstantiator.getHandlerInstantiator());
        assertNotSame(baseSettings, withHandlerInstantiator);

        BaseSettings withLocale = baseSettings.with(locale);
        assertEquals(locale, withLocale.getLocale());
        assertNotSame(baseSettings, withLocale);

        BaseSettings withTimeZone = baseSettings.with(timeZone);
        assertEquals(timeZone, withTimeZone.getTimeZone());
        assertNotSame(baseSettings, withTimeZone);

        BaseSettings withBase64Variant = baseSettings.with(Base64Variant.STANDARD);
        assertEquals(Base64Variant.STANDARD, withBase64Variant.getBase64Variant());
        assertNotSame(baseSettings, withBase64Variant);
    }
}

class SimpleTypeResolverBuilder implements TypeResolverBuilder<Object> {
    @Override
    public Object build(JavaType type) {
        return null;
    }
}

class SimpleHandlerInstantiator implements HandlerInstantiator {
    @Override
    public Object createHandler(Class<?> handlerType) {
        return null;
    }
}