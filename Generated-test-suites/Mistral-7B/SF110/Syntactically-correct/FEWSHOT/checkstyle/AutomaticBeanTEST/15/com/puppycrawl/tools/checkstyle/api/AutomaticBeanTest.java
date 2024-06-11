package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.collections4.ListUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;
    private BeanUtilsBean beanUtilsBean;
    private ConvertUtilsBean convertUtilsBean;
    private PropertyUtilsBean propertyUtilsBean;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        configuration = new Configuration();
        automaticBean = new AutomaticBean();
        beanUtilsBean = new BeanUtilsBean(convertUtilsBean, propertyUtilsBean);
    }

    @Test
    public void testConfigure() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("foo.bar", "true");
        configuration.setProperties(properties);

        automaticBean.configure(configuration);

        assertEquals(true, automaticBean.getConfiguration().getAttribute("foo.bar"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidProperty() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("foo.bar", "not a boolean");
        configuration.setProperties(properties);

        automaticBean.configure(configuration);

        fail();
    }

    @Test
    public void testContextualize() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("foo.bar", "true");

        doReturn(properties).when(automaticBean).getConfiguration();

        automaticBean.contextualize(mock(Context.class));

        assertEquals(true, automaticBean.getConfiguration().getAttribute("foo.bar"));
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        final Object target = new Object() {
            private String property;

            public String getProperty() {
                return property;
            }

            public void setProperty(String property) {
                this.property = property;
            }
        };

        automaticBean.tryCopyProperty("test", "property", "foo", false);
        assertEquals("foo", target.getProperty());

        automaticBean.tryCopyProperty("test", "property", null, false);
        assertNull(target.getProperty());

        automaticBean.tryCopyProperty("test", null, "foo", false);
        assertNull(target.getProperty());

        automaticBean.tryCopyProperty("test", "property", 1, false);
        assertNull(target.getProperty());

        automaticBean.tryCopyProperty("test", "property", true, false);
        assertTrue(target instanceof String);
        assertEquals("true", target.toString());

        automaticBean.tryCopyProperty("test", "property", new String[]{"foo", "bar"}, false);
        assertEquals(Arrays.asList("foo", "bar"), Arrays.asList((String[])target));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyInvalidProperty() throws Exception {
        automaticBean.tryCopyProperty("test", null, "foo", false);

        fail();
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyInvalidValue() throws Exception {
        automaticBean.tryCopyProperty("test", "property", 1, false);

        fail();
    }

    @Test(expected = InvocationTargetException.class)
    public void testTryCopyPropertyReflectionFailure() throws Exception {
        doThrow(new IllegalAccessException("Reflection failure")).when(beanUtilsBean).copyProperty(automaticBean, "property", "foo");

        automaticBean.tryCopyProperty("test", "property", "foo", true);

        fail();
    }

    @Test(expected = ConversionException.class)
    public void testTryCopyPropertyConversionFailure() throws Exception {
        doThrow(new ConversionException("Conversion failure")).when(convertUtilsBean).convert(boolean[].class, "foo");

        automaticBean.tryCopyProperty("test", "property", "foo", true);

        fail();
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureChild() throws Exception {
        final Configuration childConfiguration = new Configuration();
        automaticBean.setupChild(childConfiguration);

        fail();
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final String input = "foo,bar,baz";
        final Converter converter = new AutomaticBean().new RelaxedStringArrayConverter();

        final Object converted = converter.convert(String[].class, input);
        final String[] result = (String[])converted;

        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }
}