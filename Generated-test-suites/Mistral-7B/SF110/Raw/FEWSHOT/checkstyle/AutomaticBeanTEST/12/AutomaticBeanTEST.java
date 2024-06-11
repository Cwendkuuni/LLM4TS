package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.beanutils.BeanUtilsBean;
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
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RunWith(JUnit4.class)
public class AutomaticBeanTest {

    private static final String CONFIG_NAME = "testConfig";
    private static final String[] ATTRIBUTE_NAMES = {"testAttribute1", "testAttribute2"};
    private static final Object[][] TEST_VALUES = {
            {true, true},
            {1, 1},
            {1L, 1L},
            {1.0F, 1.0F},
            {1.0, 1.0},
            {(byte) 1, (byte) 1},
            {(short) 1, (short) 1},
            {"testValue1", "testValue1"},
            {"testValue1, testValue2", new String[]{"testValue1", "testValue2"}},
            {new Boolean[]{true, false}, new Boolean[]{true, false}},
            {new Byte[]{Byte.MIN_VALUE, Byte.MAX_VALUE}, new Byte[]{Byte.MIN_VALUE, Byte.MAX_VALUE}},
            {new Character[]{'a', 'z'}, new Character[]{'a', 'z'}},
            {new Double[]{1.0, 2.0}, new Double[]{1.0, 2.0}},
            {new Float[]{1.0F, 2.0F}, new Float[]{1.0F, 2.0F}},
            {new Integer[]{1, 2}, new Integer[]{1, 2}},
            {new Long[]{1L, 2L}, new Long[]{1L, 2L}},
            {new Short[]{Short.MIN_VALUE, Short.MAX_VALUE}, new Short[]{Short.MIN_VALUE, Short.MAX_VALUE}},
            {new String[]{"testValue1", "testValue2"}, new String[]{"testValue1", "testValue2"}}
    };

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtilsBean);
    }

    @Test
    public void testConfigure() throws Exception {
        final Configuration testConfig = new Configuration();
        testConfig.addAttribute("testAttribute1", "testValue1");
        testConfig.addAttribute("testAttribute2", "testValue2");

        final AutomaticBean testBean = new AutomaticBean();
        testBean.configure(testConfig);

        final Configuration config = testBean.getConfiguration();
        assertNotNull(config);
        assertEquals(ATTRIBUTE_NAMES, config.getAttributeNames());
        assertArrayEquals(new String[]{"testValue1", "testValue2"}, config.getAttributes());

        final AutomaticBean copiedBean = new AutomaticBean();
        copiedBean.configure(config);

        final Configuration copiedConfig = copiedBean.getConfiguration();
        assertEquals(config, copiedConfig);

        for (final String key : config.getAttributeNames()) {
            final Object value = config.getAttribute(key);
            final Object testValue = testBean.getClass().getField(key).get(testBean);
            assertEquals(value, testValue);
        }

        final List<String> listAttribute1 = (List<String>)testBean.getClass().getField("listAttribute1").get(testBean);
        assertNotNull(listAttribute1);
        assertEquals(2, listAttribute1.size());
        assertArrayEquals(new String[]{"testValue1", "testValue2"}, listAttribute1.toArray(new String[0]));

        final List<String> listAttribute2 = (List<String>)testBean.getClass().getField("listAttribute2").get(testBean);
        assertNotNull(listAttribute2);
        assertEquals(2, listAttribute2.size());
        assertArrayEquals(new String[]{"testValue1", "testValue2"}, listAttribute2.toArray(new String[0]));
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        final Configuration testConfig = new Configuration();
        testConfig.addAttribute("testAttribute1", "testValue1");

        final AutomaticBean testBean = new AutomaticBean();

        // Test with valid values
        for (final Object[] testValue : TEST_VALUES) {
            final Class<?> targetType = testValue[0].getClass();
            final Object configValue = testConfig.getAttribute("testAttribute1");
            final Object beanValue = testValue[1];

            testBean.tryCopyProperty(CONFIG_NAME, "testAttribute1", configValue, true);
            assertEquals(beanValue, testBean.getClass().getField("testAttribute1").get(testBean));
        }

        // Test with null value
        testBean.tryCopyProperty(CONFIG_NAME, "testAttribute1", null, true);
        assertNull(testBean.getClass().getField("testAttribute1").get(testBean));

        // Test with invalid value
        final String invalidValue = "";
        testBean.tryCopyProperty(CONFIG_NAME, "testAttribute1", invalidValue, true);
        assertThrows(CheckstyleException.class, () -> testBean.getClass().getField("testAttribute1").get(testBean));

        // Test with recheck and non-existing property
        final String nonExistingProperty = "nonExistingProperty";
        testBean.tryCopyProperty(CONFIG_NAME, nonExistingProperty, "testValue", true);
        assertThrows(CheckstyleException.class, () -> testBean.getClass().getField(nonExistingProperty).get(testBean));
    }

    @Test
    public void testContextualize() throws Exception {
        final Context testContext = Mockito.mock(Context.class);
        final AutomaticBean testBean = new AutomaticBean();

        testContext.putAttribute("testAttribute1", "testValue1");
        testContext.putAttribute("testAttribute2", "testValue2");

        testBean.contextualize(testContext);

        final Configuration config = testBean.getConfiguration();
        assertNull(config);

        final List<String> listAttribute1 = (List<String>)testBean.getClass().getField("listAttribute1").get(testBean);
        assertNotNull(listAttribute1);
        assertEquals(2, listAttribute1.size());
        assertArrayEquals(new String[]{"testValue1", "testValue2"}, listAttribute1.toArray(new String[0]));

        final List<String> listAttribute2 = (List<String>)testBean.getClass().getField("listAttribute2").get(testBean);
        assertNotNull(listAttribute2);
        assertEquals(2, listAttribute2.size());
        assertArrayEquals(new String[]{"testValue1", "testValue2"}, listAttribute2.toArray(new String[0]));
    }

    @Test
    public void testGetConfiguration() {
        final AutomaticBean testBean = new AutomaticBean();
        assertNull(testBean.getConfiguration());

        final Configuration config = new Configuration();
        testBean.configure(config);
        assertEquals(config, testBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        final AutomaticBean testBean = new AutomaticBean();
        assertThrows(CheckstyleException.class, testBean::finishLocalSetup);
    }

    @Test
    public void testSetupChild() throws Exception {
        final Configuration childConfig = new Configuration();

        final AutomaticBean testBean = new AutomaticBean();
        testBean.setupChild(childConfig);

        assertThrows(CheckstyleException.class, () -> childConfig.getName());
    }
}