package com.example.automaticbean; // replace with your package name

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.exceptions.ConversionException;
import org.apache.commons.beanutils.exceptions.InvocationTargetException;
import org.apache.commons.beanutils.exceptions.NoSuchMethodException;
import org.apache.commons.beanutils.exceptions.ReflectiveOperationException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(Parameterized.class)
public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;

    @Parameterized.Parameters(name = "{index}: testConfigureMethodWithConfiguration-{0}")
    public static List<Object[]> testConfigureMethodData() {
        Configuration config = new Configuration();
        config.addAttribute("testAttribute1", "testValue1");
        config.addAttribute("testAttribute2", "testValue2");
        Object[] testData = new Object[] { config };
        return Arrays.asList(testData);
    }

    @Parameterized.Parameter
    public void setConfiguration(Configuration config) {
        this.configuration = config;
        this.automaticBean = new AutomaticBean();
    }

    @Test
    public void testCreateBeanUtilsBean() {
        BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtilsBean);
    }

    @Test
    public void testConfigureMethod() throws Exception {
        Configuration testConfig = new Configuration();
        testConfig.addAttribute("testAttribute1", "testValue1");
        testConfig.addAttribute("testAttribute2", "testValue2");

        this.automaticBean.configure(testConfig);
        Configuration actualConfig = this.automaticBean.getConfiguration();
        assertEquals(testConfig, actualConfig);

        assertNotNull(FieldUtils.getFieldValue(this.automaticBean, "configuration"));
        assertEquals(testConfig, FieldUtils.getFieldValue(this.automaticBean, "configuration"));

        String[] attributeNames = actualConfig.getAttributeNames();
        assertEquals(2, attributeNames.length);
        assertArrayEquals(new String[]{"testAttribute1", "testAttribute2"}, attributeNames);

        String[] attributeValues = new String[2];
        attributeValues[0] = "testValue1";
        attributeValues[1] = "testValue2";
        Object[] expectedValues = new Object[2];
        expectedValues[0] = "testValue1";
        expectedValues[1] = "testValue2";
        Object[] actualValues = new Object[2];
        for (int i = 0; i < attributeValues.length; i++) {
            PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(this.automaticBean, attributeNames[i]);
            actualValues[i] = PropertyUtils.getPropertyValue(this.automaticBean, attributeNames[i]);
            assertEquals(expectedValues[i], actualValues[i]);
            assertEquals(attributeValues[i], actualValues[i]);
        }

        this.automaticBean.finishLocalSetup();

        Configuration[] children = new Configuration[1];
        Configuration childConfig = new Configuration();
        children[0] = childConfig;

        this.automaticBean.configure(new Configuration(Arrays.asList(testConfig, children)));
        actualConfig = this.automaticBean.getConfiguration();

        assertEquals(3, actualConfig.getChildren().length);
        assertEquals(2, actualConfig.getAttributeNames().length);

        Configuration childConfig1 = actualConfig.getChildren()[0];
        assertEquals("", childConfig1.getName());
        assertNull(childConfig1.getChildren());
        assertEquals(0, childConfig1.getAttributeNames().length);

        Configuration childConfig2 = actualConfig.getChildren()[1];
        assertEquals("childConfig2", childConfig2.getName());
        assertNull(childConfig2.getChildren());
        assertEquals(0, childConfig2.getAttributeNames().length);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureMethodWithNullConfiguration() throws Exception {
        this.automaticBean.configure(null);
        fail("Expected CheckstyleException");
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureMethodWithNullAttributeName() throws Exception {
        this.automaticBean.configure(this.configuration);
        String nullAttributeName = null;
        try {
            this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), nullAttributeName, "testValue", true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            assertEquals("Property 'null' in module " + this.automaticBean.getClass().getName() + " does not exist, please check the documentation", e.getMessage());
        }
    }

    @Test
    public void testTryCopyPropertyWithValidValues() throws Exception {
        String validValue1 = "Valid Value 1";
        String validValue2 = "Valid Value 2";

        this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), "testAttribute1", validValue1, true);
        String attributeValue1 = (String)PropertyUtils.getPropertyValue(this.automaticBean, "testAttribute1");
        assertEquals(validValue1, attributeValue1);

        this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), "testAttribute2", validValue2, true);
        String attributeValue2 = (String)PropertyUtils.getPropertyValue(this.automaticBean, "testAttribute2");
        assertEquals(validValue2, attributeValue2);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullValue() throws Exception {
        this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), "testAttribute1", null, true);
        fail("Expected CheckstyleException");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidValueType() throws Exception {
        final int invalidValue = 123;

        try {
            this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), "testAttribute1", invalidValue, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            assertEquals("illegal value '123' for property 'testAttribute1' of module " + this.automaticBean.getClass().getName(), e.getMessage());
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithWrongPropertyName() throws Exception {
        this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), "nonExistentPropertyName", "testValue", true);
        fail("Expected CheckstyleException");
    }

    @Test(expected = NoSuchMethodException.class)
    public void testTryCopyPropertyWithNonExistentMethod() throws Exception {
        Class<AutomaticBean> clazz = AutomaticBean.class;
        Method nonExistentMethod = clazz.getMethod("nonExistentMethod", null);
        this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), "testAttribute1", nonExistentMethod, true);
        fail("Expected NoSuchMethodException");
    }

    @Test(expected = InvocationTargetException.class)
    public void testTryCopyPropertyWithMethodExecutionException() throws Exception {
        Class<AutomaticBean> clazz = AutomaticBean.class;
        Method methodWithException = clazz.getMethod("methodWithException", null);
        this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), "testAttribute1", methodWithException, true);
        fail("Expected InvocationTargetException");
    }

    @Test(expected = IllegalAccessException.class)
    public void testTryCopyPropertyWithIllegalAccessException() throws Exception {
        Class<AutomaticBean> clazz = AutomaticBean.class;
        Field privateField = clazz.getDeclaredField("privateField");
        privateField.setAccessible(true);
        this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), "testAttribute1", privateField, true);
        fail("Expected IllegalAccessException");
    }

    @Test(expected = ConversionException.class)
    public void testTryCopyPropertyWithInvalidConversion() throws Exception {
        this.automaticBean.tryCopyProperty(this.automaticBean.getClass().getName(), "testAttribute1", new Object() { /* invalid conversion */ }, true);
        fail("Expected ConversionException");
    }

    @Test
    public void testContextualizeMethod() throws Exception {
        Context context = new Context();
        context.put("testAttribute1", "testValue1");
        context.put("testAttribute2", "testValue2");

        this.automaticBean.contextualize(context);

        String[] attributeNames = this.automaticBean.getConfiguration().getAttributeNames();

        assertEquals(2, attributeNames.length);
        assertArrayEquals(new String[]{"testAttribute1", "testAttribute2"}, attributeNames);

        String[] expectedValues = new String[]{"testValue1", "testValue2"};
        Object[] actualValues = new Object[2];
        for (int i = 0; i < expectedValues.length; i++) {
            PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(this.automaticBean, "testAttribute" + (i + 1));
            actualValues[i] = PropertyUtils.getPropertyValue(this.automaticBean, "testAttribute" + (i + 1));
            assertEquals(expectedValues[i], actualValues[i]);
        }
    }

    @Test
    public void testFinishLocalSetupMethod() throws Exception {
        this.automaticBean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildMethodWithNullChildConfiguration() throws Exception {
        Configuration nullChildConfig = null;
        this.automaticBean.setupChild(nullChildConfig);
        fail("Expected CheckstyleException");
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        Configuration config = new Configuration();
        config.addAttribute("testListAttribute", Arrays.toString(new String[]{"testValue1", "testValue2"}));

        this.automaticBean.configure(config);

        Object attributeValue = PropertyUtils.getPropertyValue(this.automaticBean, "testListAttribute");
        List<String> expectedList = Lists.newArrayList("testValue1", "testValue2");
        List<String> actualList = (List<String>)ListUtils.cast(attributeValue);
        assertEquals(expectedList, actualList);

        Object[] expectedValues = new Object[expectedList.size()];
        for (int i = 0; i < expectedValues.length; i++) {
            expectedValues[i] = expectedList.get(i);
        }
        Object[] actualValues = (Object[])PropertyUtils.getPropertyValue(this.automaticBean, "testListAttribute");
        assertArrayEquals(expectedValues, actualValues);
    }
}