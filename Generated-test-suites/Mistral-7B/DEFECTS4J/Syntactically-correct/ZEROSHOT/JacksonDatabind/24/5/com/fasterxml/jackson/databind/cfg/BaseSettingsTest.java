package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsInstance() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = JsonAutoDetect.Visibility.ANY;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = StdDateFormat.INSTANCE;
        HandlerInstantiator hi = null;
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.getDefaultVariant();

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        assertNotNull(baseSettings);
    }

    @Test
    public void testSettersAndGetters() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = JsonAutoDetect.Visibility.ANY;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = StdDateFormat.INSTANCE;
        HandlerInstantiator hi = null;
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.getDefaultVariant();

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(ci, newBaseSettings._classIntrospector);
        assertEquals(newClassIntrospector, newBaseSettings.getClassIntrospector());

        newBaseSettings = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(ai, newBaseSettings._annotationIntrospector);
        assertEquals(newAnnotationIntrospector, newBaseSettings.getAnnotationIntrospector());

        newBaseSettings = baseSettings.withInsertedAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(ai, newBaseSettings._annotationIntrospector);
        assertEquals(newAnnotationIntrospector, newBaseSettings.getAnnotationIntrospector());

        newBaseSettings = baseSettings.withAppendedAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(ai, newBaseSettings._annotationIntrospector);
        assertEquals(newAnnotationIntrospector, newBaseSettings.getAnnotationIntrospector());

        newBaseSettings = baseSettings.withVisibilityChecker(new VisibilityChecker<Object>() {});
        assertNotSame(vc, newBaseSettings._visibilityChecker);
        assertEquals(newVisibilityChecker, newBaseSettings.getVisibilityChecker());

        newBaseSettings = baseSettings.withVisibility(JsonAutoDetect.Method.getAnnotation(Object.class, "foo"), JsonAutoDetect.Visibility.NONE);
        assertNotSame(vc, newBaseSettings._visibilityChecker);
        assertEquals(newVisibilityChecker, newBaseSettings.getVisibilityChecker());

        newBaseSettings = baseSettings.withPropertyNamingStrategy(new PropertyNamingStrategy() {});
        assertNotSame(pns, newBaseSettings._propertyNamingStrategy);
        assertEquals(newPropertyNamingStrategy, newBaseSettings.getPropertyNamingStrategy());

        newBaseSettings = baseSettings.withTypeFactory(new TypeFactory());
        assertNotSame(tf, newBaseSettings._typeFactory);
        assertEquals(newTypeFactory, newBaseSettings.getTypeFactory());

        newBaseSettings = baseSettings.withTypeResolverBuilder(new TypeResolverBuilder<Object>() {});
        assertNotSame(typer, newBaseSettings._typeResolverBuilder);
        assertEquals(newTypeResolverBuilder, newBaseSettings.getTypeResolverBuilder());

        newBaseSettings = baseSettings.withDateFormat(new StdDateFormat(Locale.US, "dd/MM/yyyy"));
        assertNotSame(dateFormat, newBaseSettings._dateFormat);
        assertEquals(newStdDateFormat, newBaseSettings.getDateFormat());

        newBaseSettings = baseSettings.withHandlerInstantiator(new HandlerInstantiator() {});
        assertNotSame(hi, newBaseSettings._handlerInstantiator);
        assertEquals(newHandlerInstantiator, newBaseSettings.getHandlerInstantiator());

        newBaseSettings = baseSettings.with(Locale.US);
        assertNotSame(locale, newBaseSettings._locale);
        assertEquals(newLocale, newBaseSettings.getLocale());

        newBaseSettings = baseSettings.with(TimeZone.getTimeZone("UTC"));
        assertNotSame(tz, newBaseSettings._timeZone);
        assertEquals(newTimeZone, newBaseSettings.getTimeZone());

        newBaseSettings = baseSettings.with(Base64Variants.US_ASCII);
        assertNotSame(defaultBase64, newBaseSettings._defaultBase64);
        assertEquals(newBase64Variant, newBaseSettings.getBase64Variant());
    }

    private ClassIntrospector newClassIntrospector = new ClassIntrospector();
    private AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
    private VisibilityChecker<Object> newVisibilityChecker = new VisibilityChecker<Object>() {};
    private PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy() {};
    private TypeFactory newTypeFactory = new TypeFactory();
    private TypeResolverBuilder<Object> newTypeResolverBuilder = new TypeResolverBuilder<Object>() {};
    private DateFormat newStdDateFormat = new StdDateFormat(Locale.US, "dd/MM/yyyy");
    private HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator() {};
    private Locale newLocale = Locale.US;
    private TimeZone newTimeZone = TimeZone.getTimeZone("UTC");
    private Base64Variant newBase64Variant = Base64Variants.US_ASCII;
}