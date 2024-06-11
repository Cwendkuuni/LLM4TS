package com.example.automaticbean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
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
import org.apache.commons.beanutils.exceptions.BeanUtilsBeanException;
import org.apache.commons.beanutils.exceptions.ConversionException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

public class AutomaticBeanTest {

    private static final Log LOGGER = LogFactory.getLog(AutomaticBeanTest.class);

    private AutomaticBean automaticBean;
    private Configuration configuration;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
        configuration = new Configuration();
    }

    // Test method: configure()
    
    @Test
    public void testConfigure() throws Exception {
        configuration.addAttribute("attribute1", "value1");
        configuration.addAttribute("attribute2", "value2");

        automaticBean.configure(configuration);

        assertEquals("value1", automaticBean.getConfiguration().getAttribute("attribute1"));
        assertEquals("value2", automaticBean.getConfiguration().getAttribute("attribute2"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfiguration() {
        automaticBean.configure(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithInvalidAttributeName() {
        configuration.addAttribute(null, "value1");
        automaticBean.configure(configuration);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithMissingAttributeValue() {
        configuration.addAttribute("attribute1", null);
        automaticBean.configure(configuration);
    }

    // Test method: tryCopyProperty()

    @Test
    public void testTryCopyPropertyWithValidPropertyNameAndValue() throws Exception {
        automaticBean.tryCopyProperty("moduleName", "propertyName", "value", true);

        final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "propertyName");
        assertEquals("value", propertyDescriptor.getReadMethod().invoke(automaticBean));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullModuleName() throws Exception {
        automaticBean.tryCopyProperty(null, "propertyName", "value", true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullPropertyName() throws Exception {
        automaticBean.tryCopyProperty("moduleName", null, "value", true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullValue() throws Exception {
        automaticBean.tryCopyProperty("moduleName", "propertyName", null, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidPropertyName() throws Exception {
        automaticBean.tryCopyProperty("moduleName", "invalidPropertyName", "value", true);
    }

    @Test(expected = BeanUtilsBeanException.class)
    public void testTryCopyPropertyWithUnsupportedType() throws Exception {
        automaticBean.tryCopyProperty("moduleName", "propertyName", new Object(), true);
    }

    @Test(expected = IllegalAccessException.class)
    public void testTryCopyPropertyWithIllegalAccess() throws Exception {
        automaticBean = new AutomaticBean() {
            @Override
            public void finishLocalSetup() throws CheckstyleException {
                throw new IllegalAccessException();
            }
        };
        automaticBean.tryCopyProperty("moduleName", "propertyName", "value", true);
    }

    @Test(expected = InvocationTargetException.class)
    public void testTryCopyPropertyWithInvocationTargetException() throws Exception {
        automaticBean = new AutomaticBean() {
            @Override
            public void finishLocalSetup() throws CheckstyleException {
                throw new InvocationTargetException(new RuntimeException());
            }
        };
        automaticBean.tryCopyProperty("moduleName", "propertyName", "value", true);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testTryCopyPropertyWithNoSuchMethodException() throws Exception {
        automaticBean = new AutomaticBean() {
            @Override
            public void finishLocalSetup() throws CheckstyleException {
            }

            @Deprecated
            public void setPropertyName(String value) {
                // empty implementation
            }
        };
        automaticBean.tryCopyProperty("moduleName", "propertyName", "value", true);
    }

    @Test(expected = ConversionException.class)
    public void testTryCopyPropertyWithConversionException() throws Exception {
        automaticBean.tryCopyProperty("moduleName", "propertyName", new Integer(1), true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTryCopyPropertyWithIllegalArgumentException() throws Exception {
        automaticBean = new AutomaticBean() {
            @Override
            public void finishLocalSetup() throws CheckstyleException {
            }

            @Deprecated
            public void setPropertyName(int value) {
                throw new IllegalArgumentException();
            }
        };
        automaticBean.tryCopyProperty("moduleName", "propertyName", "value", true);
    }

    // Test method: contextualize()

    @Test
    public void testContextualizeWithValidContext() throws Exception {
        final Map<String, Object> context = new HashMap<>();
        context.put("contextAttribute1", "value1");
        context.put("contextAttribute2", "value2");

        automaticBean.contextualize(context);

        assertEquals("value1", automaticBean.getConfiguration().getAttribute("contextAttribute1"));
        assertEquals("value2", automaticBean.getConfiguration().getAttribute("contextAttribute2"));
    }

    @Test
    public void testContextualizeWithEmptyContext() throws Exception {
        automaticBean.contextualize(Collections.emptyMap());

        assertNull(automaticBean.getConfiguration().getAttribute("nonexistentAttribute"));
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeWithNullContext() throws Exception {
        automaticBean.contextualize(null);
    }

    @Test(expected = NullPointerException.class)
    public void testContextualizeWithNullContextAttributeValue() throws Exception {
        final Map<String, Object> context = new HashMap<>();
        context.put("contextAttribute1", null);

        automaticBean.contextualize(context);

        assertNull(automaticBean.getConfiguration().getAttribute("contextAttribute1"));
    }

    @Test(expected = ConversionException.class)
    public void testContextualizeWithInvalidContextAttributeValue() throws Exception {
        final Map<String, Object> context = new HashMap<>();
        context.put("contextAttribute1", new String[]{"value1", "value2"});

        automaticBean.contextualize(context);

        assertNull(automaticBean.getConfiguration().getAttribute("contextAttribute1"));
    }

    // Test method: getConfiguration()

    @Test
    public void testGetConfiguration() {
        automaticBean.configure(configuration);

        assertNotNull(automaticBean.getConfiguration());
    }

    // Test method: finishLocalSetup()

    @Test
    public void testFinishLocalSetup() throws Exception {
        automaticBean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testFinishLocalSetupWithException() throws Exception {
        automaticBean = new AutomaticBean() {
            @Override
            protected void finishLocalSetup() throws CheckstyleException {
                throw new CheckstyleException();
            }
        };

        assertThrows(CheckstyleException.class, automaticBean::finishLocalSetup);
    }

    // Test method: setupChild()

    @Test
    public void testSetupChildWithNullChildConfiguration() throws Exception {
        automaticBean.setupChild(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithValidChildConfiguration() throws Exception {
        final Configuration childConfiguration = new Configuration();
        automaticBean.setupChild(childConfiguration);

        assertThrows(CheckstyleException.class, () -> automaticBean.getConfiguration().getChild(childConfiguration.getName()));
    }

    // Test method: RelaxedStringArrayConverter

    @Test
    public void testRelaxedStringArrayConverter() {
        final List<String> result = Arrays.asList(new RelaxedStringArrayConverter().convert(String[].class, "value,value1,value2"));

        assertEquals(3, result.size());
        assertEquals("value", result.get(0));
        assertEquals("value1", result.get(1));
        assertEquals("value2", result.get(2));
    }
}