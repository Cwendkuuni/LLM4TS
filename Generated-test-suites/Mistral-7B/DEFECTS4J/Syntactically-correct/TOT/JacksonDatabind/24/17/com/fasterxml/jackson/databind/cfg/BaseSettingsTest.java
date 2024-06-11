package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.HandlerInstantiator;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setup() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = JsonAutoDetect.Visibility.NONE;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder();
        DateFormat dateFormat = StdDateFormat.DEFAULT;
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.STANDARD;

        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
    }

    @Test
    public void testGetClassIntrospector() {
        ClassIntrospector ci = baseSettings.getClassIntrospector();
        assertNotNull(ci);
        assertEquals(ci.getClass(), ClassIntrospector.class);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector ai = baseSettings.getAnnotationIntrospector();
        assertNotNull(ai);
        assertEquals(ai.getClass(), AnnotationIntrospector.class);
    }

    @Test
    public void testGetVisibilityChecker() {
        VisibilityChecker<?> vc = baseSettings.getVisibilityChecker();
        assertNotNull(vc);
        assertEquals(vc.getClass(), JsonAutoDetect.VisibilityChecker.class);
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        PropertyNamingStrategy pns = baseSettings.getPropertyNamingStrategy();
        assertNotNull(pns);
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory tf = baseSettings.getTypeFactory();
        assertNotNull(tf);
    }

    @Test
    public void testGetTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = baseSettings.getTypeResolverBuilder();
        assertNotNull(typer);
    }

    @Test
    public void testGetDateFormat() {
        DateFormat dateFormat = baseSettings.getDateFormat();
        assertNotNull(dateFormat);
        assertEquals(dateFormat.getClass(), StdDateFormat.class);
    }

    @Test
    public void testGetHandlerInstantiator() {
        HandlerInstantiator hi = baseSettings.getHandlerInstantiator();
        assertNotNull(hi);
    }

    @Test
    public void testGetLocale() {
        Locale locale = baseSettings.getLocale();
        assertNotNull(locale);
        assertEquals(locale, Locale.getDefault());
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = baseSettings.getTimeZone();
        assertNotNull(timeZone);
        assertEquals(timeZone, TimeZone.getDefault());
    }

    @Test
    public void testGetBase64Variant() {
        Base64Variant base64 = baseSettings.getBase64Variant();
        assertNotNull(base64);
        assertEquals(base64, Base64Variants.STANDARD);
    }

    @Test
    public void testWithClassIntrospector() {
        BaseSettings baseSettingsWithNewCi = baseSettings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(baseSettings, baseSettingsWithNewCi);
        assertEquals(baseSettingsWithNewCi.getClassIntrospector(), new ClassIntrospector());
    }
}