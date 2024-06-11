package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final ConvertUtilsBean cub = new ConvertUtilsBean();
        final BeanUtilsBean beanUtils = new BeanUtilsBean(cub, new PropertyUtilsBean());
        assertEquals(35, beanUtils.getConvertedTypes().length);
        assertEquals(BeanUtilsBean.class, beanUtils.getClass());
    }

    @Test
    public void testConfigurationSetUp() {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertNull(automaticBean.getConfiguration());

        config.addAttribute("configProperty", "test");
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
        assertEquals("test", automaticBean.getConfiguration().getAttribute("configProperty"));
    }

    @Test
    public void testCopyProperty() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();

        // Test null values
        config.setAttribute("testNull", null);
        automaticBean.configure(config);
        assertNull(automaticBean.getConfiguration().getAttribute("testNull"));
        assertNull(automaticBean.getConfiguration().getAttribute("testNull", String.class));

        // Test setting strings
        config.setAttribute("testString", "testString");
        automaticBean.configure(config);
        assertEquals("testString", automaticBean.getConfiguration().getAttribute("testString"));

        // Test setting numbers
        config.setAttribute("testInt", 123);
        automaticBean.configure(config);
        assertEquals(123, automaticBean.getConfiguration().getAttribute("testInt", Integer.class).intValue());

        // Test setting arrays
        final Object testArray = new Object[] {"test0", "test1"};
        config.setAttribute("testArray", testArray);
        automaticBean.configure(config);
        final Object configArray = automaticBean.getConfiguration().getAttribute("testArray");
        assertEquals(testArray.getClass(), configArray.getClass());
        assertArrayEquals((Object[]) testArray, (Object[]) configArray);

        // Test setting nested arrays
        final Object nestedArray = new Object[][] {{"test0", "test1"}, {"test2", "test3"}};
        config.setAttribute("testNestedArray", nestedArray);
        automaticBean.configure(config);
        final Object configNestedArray = automaticBean.getConfiguration().getAttribute("testNestedArray");
        assertEquals(nestedArray.getClass(), configNestedArray.getClass());
        assertArrayEquals((Object[][]) nestedArray, (Object[][]) configNestedArray);

        // Test setting booleans
        config.setAttribute("testBoolean", true);
        automaticBean.configure(config);
        assertTrue(automaticBean.getConfiguration().getAttribute("testBoolean", Boolean.class).booleanValue());

        config.setAttribute("testBoolean", "true");
        automaticBean.configure(config);
        assertTrue(automaticBean.getConfiguration().getAttribute("testBoolean", Boolean.class).booleanValue());

        config.setAttribute("testBoolean", "false");
        automaticBean.configure(config);
        assertFalse(automaticBean.getConfiguration().getAttribute("testBoolean", Boolean.class).booleanValue());

        // Test invalid set property
        automaticBean.configure(new Configuration());
        assertThrows(IllegalArgumentException.class, () -> automaticBean.tryCopyProperty("AutomaticBean", "nonExistentProperty", null));

        // Test recheck property
        automaticBean.configure(config);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testBoolean");
        assertNotNull(descriptor);
        assertThrows(CheckstyleException.class, () -> automaticBean.tryCopyProperty("AutomaticBean", "testBoolean", null, true));

        // Test invalid set property value - illegalArgumentException
        automaticBean.configure(config);
        assertThrows(IllegalArgumentException.class, () -> automaticBean.tryCopyProperty("AutomaticBean", "testBoolean", "invalid", true));

        // Test invalid set property value - conversionException
        config.setAttribute("testBoolean", "invalid");
        assertThrows(ConversionException.class, () -> automaticBean.tryCopyProperty("AutomaticBean", "testBoolean", "invalid", true));

        // Test invalid set property value - invocationTargetException
        config.setAttribute("testBoolean", "invalid");
        assertThrows(InvocationTargetException.class, () -> automaticBean.tryCopyProperty("AutomaticBean", "testBoolean", "invalid", true));

        // Test invalid set property value - noSuchMethodException
        config.setAttribute("testBoolean", "invalid");
        assertThrows(NoSuchMethodException.class, () -> automaticBean.tryCopyProperty("AutomaticBean", "testBoolean", "invalid", true));
    }

    @Test
    public void testContextualize() {
        final Context context = new Context();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.contextualize(context);

        // Test empty context
        assertNull(automaticBean.getConfiguration());

        // Test context with attributes
        context.putAttribute("testString", "test");
        automaticBean.contextualize(context);
        assertEquals("test", automaticBean.getConfiguration().getAttribute("testString"));

        context.putAttribute("testInt", 123);
        automaticBean.contextualize(context);
        assertEquals(123, automaticBean.getConfiguration().getAttribute("testInt", Integer.class).intValue());

        context.putAttribute("testArray", new String[] {"test0", "test1"});
        automaticBean.contextualize(context);
        final Object configArray = automaticBean.getConfiguration().getAttribute("testArray");
        assertEquals(configArray.getClass(), String[].class);
        assertArrayEquals((Object[]) new String[] {"test0", "test1"}, (Object[]) configArray);

        context.putAttribute("testNestedArray", new String[][] {{"test0", "test1"}, {"test2", "test3"}});
        automaticBean.contextualize(context);
        final Object configNestedArray = automaticBean.getConfiguration().getAttribute("testNestedArray");
        assertEquals(configNestedArray.getClass(), String[][].class);
        assertArrayEquals((Object[][]) new String[][] {{"test0", "test1"}, {"test2", "test3"}}, (Object[][]) configNestedArray);

        context.putAttribute("testBoolean", "true");
        automaticBean.contextualize(context);
        assertTrue(automaticBean.getConfiguration().getAttribute("testBoolean", Boolean.class).booleanValue());

        context.putAttribute("testBoolean", "false");
        automaticBean.contextualize(context);
        assertFalse(automaticBean.getConfiguration().getAttribute("testBoolean", Boolean.class).booleanValue());

        context.putAttribute("testBoolean", "invalid");
        automaticBean.contextualize(context);
        assertNull(automaticBean.getConfiguration());
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();
        final AutomaticBean automaticBean = new AutomaticBean();

        // Test empty string
        String emptyString = "";
        Object converted = beanUtils.convert(String[].class, emptyString);
        assertArrayEquals(new String[0], (String[]) converted);

        // Test empty comma-separated string
        String emptyCommaSeparatedString = ",";
        converted = beanUtils.convert(String[].class, emptyCommaSeparatedString);
        assertArrayEquals(new String[0], (String[]) converted);

        // Test comma-separated string with quotes around each element
        String commaSeparatedStringWithQuotes = "\"test1\",\"test2\",\"test3\"";
        converted = beanUtils.convert(String[].class, commaSeparatedStringWithQuotes);
        assertArrayEquals(new String[] {"test1", "test2", "test3"}, (String[]) converted);

        // Test comma-separated string without quotes around each element
        String commaSeparatedStringNoQuotes = "test1,test2,test3";
        converted = beanUtils.convert(String[].class, commaSeparatedStringNoQuotes);
        assertArrayEquals(new String[] {"test1", "test2", "test3"}, (String[]) converted);

        // Test comma-separated string with leading and trailing quotes
        String commaSeparatedStringWithQuotesLeadingTrailing = "\"test1\",\"test2\",\"test3\"";
        converted = beanUtils.convert(String[].class, commaSeparatedStringWithQuotesLeadingTrailing);
        assertArrayEquals(new String[] {"test1", "test2", "test3"}, (String[]) converted);

        // Test comma-separated string with internal quotes around an element
        String commaSeparatedStringInternalQuotes = "test1,\"test2 with quotes\",test3";
        converted = beanUtils.convert(String[].class, commaSeparatedStringInternalQuotes);
        assertArrayEquals(new String[] {"test1", "test2 with quotes", "test3"}, (String[]) converted);

        // Test comma-separated string with an empty element
        String commaSeparatedStringWithEmptyElement = "test1, ,test3";
        converted = beanUtils.convert(String[].class, commaSeparatedStringWithEmptyElement);
        assertArrayEquals(new String[] {"test1", "", "test3"}, (String[]) converted);

        // Test comma-separated string with an empty element and quotes around it
        String commaSeparatedStringWithQuotesEmptyElement = "test1,\"\",test3";
        converted = beanUtils.convert(String[].class, commaSeparatedStringWithQuotesEmptyElement);
        assertArrayEquals(new String[] {"test1", "", "test3"}, (String[]) converted);
    }

    @Test
    public void testGetConfiguration() {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        assertDoesNotThrow(() -> automaticBean.finishLocalSetup());
    }

    @Test
    public void testSetupChild() throws Exception {
        final Configuration childConfig = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> automaticBean.setupChild(childConfig));
    }
}