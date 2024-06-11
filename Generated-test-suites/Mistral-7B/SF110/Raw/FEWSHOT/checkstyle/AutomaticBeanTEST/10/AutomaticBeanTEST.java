package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
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
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.junit.Before;
import org.junit.Test;

public class AutomaticBeanTest {

    private static final String MODULE_NAME = "AutomaticBean";
    private static final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean(), new PropertyUtilsBean());
    private AutomaticBean automaticBean;
    private Configuration configuration;

    @Before
    public void setup() throws ConfigurationException {
        configuration = Configurations.propertiesBuilder()
                .setProperty("foo", "bar")
                .build();
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        automaticBean.configure(configuration);
        assertNotNull(automaticBean.getConfiguration());
        assertEquals(configuration.getString("foo"), automaticBean.getConfiguration().getString("foo"));
    }

    @Test
    public void testConfigureWithNullConfiguration() {
        try {
            automaticBean.configure(null);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testConfigureWithNoAttributes() throws CheckstyleException {
        Configuration configurationNoAttributes = Configurations.propertiesBuilder().build();
        try {
            automaticBean.configure(configurationNoAttributes);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testConfigureWithInvalidAttributeType() throws CheckstyleException {
        Configuration configurationInvalidAttribute = Configurations.propertiesBuilder()
                .setProperty("foo", "bar")
                .setProperty("invalidAttribute", new Object())
                .build();
        try {
            automaticBean.configure(configurationInvalidAttribute);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws CheckstyleException {
        Configuration configurationExistingProperty = Configurations.propertiesBuilder()
                .setProperty("foo.bar", "baz")
                .build();
        automaticBean.configure(configurationExistingProperty);
        try {
            automaticBean.tryCopyProperty(MODULE_NAME, "foo.bar", "qux", true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testTryCopyPropertyWithNullValue() throws CheckstyleException {
        try {
            automaticBean.tryCopyProperty(MODULE_NAME, "foo", null, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testTryCopyPropertyWithInvalidType() throws CheckstyleException {
        try {
            automaticBean.tryCopyProperty(MODULE_NAME, "foo", new Object(), true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testTryCopyPropertyWithInvalidSetter() throws CheckstyleException {
        try {
            AutomaticBean testBean = new AutomaticBean() {
                @Override
                protected void finishLocalSetup() throws CheckstyleException {
                    throw new CheckstyleException("finishLocalSetup cannot be overridden");
                }
            };
            Configuration configurationInvalidSetter = Configurations.propertiesBuilder()
                    .setProperty("foo", "bar")
                    .build();
            testBean.configure(configurationInvalidSetter);
            testBean.tryCopyProperty(MODULE_NAME, "foo", "qux", true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testTryCopyPropertyWithExistingChild() throws CheckstyleException {
        Configuration configurationExistingChild = Configurations.propertiesBuilder()
                .addProperty(Configurations.propertiesBuilder()
                        .setProperty("foo", "bar")
                        .build())
                .build();
        automaticBean.configure(configurationExistingChild);
        try {
            automaticBean.tryCopyProperty(MODULE_NAME, "foo", "qux", true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        Configuration contextConfiguration = Configurations.propertiesBuilder()
                .setProperty("foo", "bar")
                .build();
        automaticBean.contextualize(contextConfiguration);
        assertEquals(contextConfiguration.getString("foo"), automaticBean.getConfiguration().getString("foo"));
    }

    @Test
    public void testContextualizeWithNullContext() {
        try {
            automaticBean.contextualize(null);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testContextualizeWithNoAttributes() {
        Configuration contextConfigurationNoAttributes = Configurations.propertiesBuilder().build();
        try {
            automaticBean.contextualize(contextConfigurationNoAttributes);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testCopyPropertyWithExistingProperty() throws CheckstyleException {
        Configuration configurationExistingProperty = Configurations.propertiesBuilder()
                .setProperty("foo.bar", "baz")
                .build();
        automaticBean.configure(configurationExistingProperty);
        try {
            automaticBean.tryCopyProperty(MODULE_NAME, "foo.bar", "qux", false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testCopyPropertyWithNullValue() throws CheckstyleException {
        try {
            automaticBean.tryCopyProperty(MODULE_NAME, "foo", null, false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testCopyPropertyWithInvalidType() throws CheckstyleException {
        try {
            automaticBean.tryCopyProperty(MODULE_NAME, "foo", new Object(), false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testCopyPropertyWithInvalidSetter() throws CheckstyleException {
        AutomaticBean testBean = new AutomaticBean() {
            @Override
            protected void finishLocalSetup() throws CheckstyleException {
                throw new CheckstyleException("finishLocalSetup cannot be overridden");
            }
        };
        Configuration configurationInvalidSetter = Configurations.propertiesBuilder()
                .setProperty("foo", "bar")
                .build();
        testBean.configure(configurationInvalidSetter);
        try {
            testBean.tryCopyProperty(MODULE_NAME, "foo", "qux", false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testCopyPropertyWithExistingChild() throws CheckstyleException {
        Configuration configurationExistingChild = Configurations.propertiesBuilder()
                .addProperty(Configurations.propertiesBuilder()
                        .setProperty("foo", "bar")
                        .build())
                .build();
        automaticBean.configure(configurationExistingChild);
        try {
            automaticBean.tryCopyProperty(MODULE_NAME, "foo", "qux", false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testTryCopyPropertyWithExistsAndInvalidValue() throws CheckstyleException {
        Configuration configurationExistingAndInvalid = Configurations.propertiesBuilder()
                .setProperty("foo.bar", "baz")
                .setProperty("foo.invalid", "qux")
                .build();
        automaticBean.configure(configurationExistingAndInvalid);
        try {
            automaticBean.tryCopyProperty(MODULE_NAME, "foo.invalid", "quz", false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testTryCopyPropertyWithExistingAndValidValue() throws CheckstyleException {
        Configuration configurationExistingAndValid = Configurations.propertiesBuilder()
                .setProperty("foo.bar", "baz")
                .setProperty("foo.valid", "qux")
                .build();
        automaticBean.configure(configurationExistingAndValid);
        automaticBean.tryCopyProperty(MODULE_NAME, "foo.valid", "quw", false);
        assertEquals("quw", automaticBean.getConfiguration().getString("foo.valid"));
    }

    @Test
    public void testTryCopyPropertyWithNewProperty() throws CheckstyleException {
        Configuration configurationNewProperty = Configurations.propertiesBuilder()
                .setProperty("foo.newProperty", "qux")
                .build();
        automaticBean.configure(configurationNewProperty);
        assertEquals("qux", automaticBean.getConfiguration().getString("foo.newProperty"));
    }

    @Test
    public void testTryCopyPropertyWithNewPropertyArray() throws CheckstyleException {
        Configuration configurationNewPropertyArray = Configurations.propertiesBuilder()
                .setProperty("foo.newPropertyArray[0]", "qux1")
                .setProperty("foo.newPropertyArray[1]", "qux2")
                .build();
        automaticBean.configure(configurationNewPropertyArray);
        Object[] expectedArray = new Object[]{ "qux1", "qux2" };
        Object[] actualArray = automaticBean.getConfiguration().getArray("foo.newPropertyArray");
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testCopyPropertyWithNewProperty() throws CheckstyleException {
        Configuration configurationNewProperty = Configurations.propertiesBuilder()
                .setProperty("foo.newProperty", "qux")
                .build();
        automaticBean.configure(configurationNewProperty);
        automaticBean.tryCopyProperty(MODULE_NAME, "foo.newProperty", "quw", true);
        assertEquals("quw", automaticBean.getConfiguration().getString("foo.newProperty"));
    }

    @Test
    public void testCopyPropertyWithNewPropertyArray() throws CheckstyleException {
        Configuration configurationNewPropertyArray = Configurations.propertiesBuilder()
                .setProperty("foo.newPropertyArray[0]", "qux1")
                .setProperty("foo.newPropertyArray[1]", "qux2")
                .build();
        automaticBean.configure(configurationNewPropertyArray);
        Object[] expectedArray = new Object[]{ "quw1", "quw2" };
        automaticBean.tryCopyProperty(MODULE_NAME, "foo.newPropertyArray", expectedArray, true);
        Object[] actualArray = automaticBean.getConfiguration().getArray("foo.newPropertyArray");
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testTryCopyPropertyWithNewPropertyNested() throws CheckstyleException {
        Configuration configurationNewPropertyNested = Configurations.propertiesBuilder()
                .setProperty("foo.nested.newProperty", "qux")
                .build();
        automaticBean.configure(configurationNewPropertyNested);
        automaticBean.tryCopyProperty(MODULE_NAME, "foo.nested.newProperty", "quw", true);
        assertEquals("quw", automaticBean.getConfiguration().getString("foo.nested.newProperty"));
    }

    @Test
    public void testTryCopyPropertyWithNewPropertyNestedArray() throws CheckstyleException {
        Configuration configurationNewPropertyNestedArray = Configurations.propertiesBuilder()
                .setProperty("foo.nested.newPropertyArray[0]", "qux1")
                .setProperty("foo.nested.newPropertyArray[1]", "qux2")
                .build();
        automaticBean.configure(configurationNewPropertyNestedArray);
        Object[] expectedArray = new Object[]{ "quw1", "quw2" };
        automaticBean.tryCopyProperty(MODULE_NAME, "foo.nested.newPropertyArray", expectedArray, true);
        Object[] actualArray = automaticBean.getConfiguration().getArray("foo.nested.newPropertyArray");
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testContextualizeWithCustomConverter() throws CheckstyleException {
        Configuration contextConfigurationCustomConverter = Configurations.propertiesBuilder()
                .setProperty("foo.list", "[1, 2, 3]")
                .build();
        automaticBean.contextualize(contextConfigurationCustomConverter);
        List<Integer> expectedList = Arrays.asList(1, 2, 3);
        List<Integer> actualList = ListUtils.toList(automaticBean.getConfiguration().getArray("foo.list"));
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testConfigureWithCustomConverter() throws CheckstyleException {
        Configuration configurationCustomConverter = Configurations.propertiesBuilder()
                .setProperty("foo.list", "[1, 2, 3]")
                .build();
        automaticBean.configure(configurationCustomConverter);
        List<Integer> expectedList = Arrays.asList(1, 2, 3);
        List<Integer> actualList = ListUtils.toList(automaticBean.getConfiguration().getArray("foo.list"));
        assertEquals(expectedList, actualList);
    }
}