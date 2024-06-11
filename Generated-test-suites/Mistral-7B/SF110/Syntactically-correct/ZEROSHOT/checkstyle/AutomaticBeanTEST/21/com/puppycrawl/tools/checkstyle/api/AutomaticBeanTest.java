package com.example.automaticbean;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutomaticBeanTest {

    private static final String CONFIG_MODULE_NAME = "testModule";
    private static final String[] CONFIG_ATTRIBUTE_NAMES = {"booleanAttribute", "byteAttribute", "charAttribute", "doubleAttribute", "floatAttribute", "integerAttribute", "longAttribute", "shortAttribute", "stringArrayAttribute"};
    private static final Object[] CONFIG_ATTRIBUTE_VALUES = {true, (byte) 127, 'a', 3.14, 1.23f, 42, 42L, (short) 32767, new String[]{"test1", "test2"}};
    private static final Configuration config = new Configuration();

    private AutomaticBean automaticBean;
    private BeanUtilsBean beanUtilsBean;
    private ConvertUtilsBean convertUtilsBean;
    private PropertyUtilsBean propertyUtilsBean;

    @Before
    public void setUp() throws Exception {
        convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new BooleanConverter(), Boolean.TYPE);
        convertUtilsBean.register(new BooleanConverter(), Boolean.class);
        convertUtilsBean.register(new ByteConverter(), Byte.TYPE);
        convertUtilsBean.register(new ByteConverter(), Byte.class);
        convertUtilsBean.register(new CharacterConverter(), Character.TYPE);
        convertUtilsBean.register(new CharacterConverter(), Character.class);
        convertUtilsBean.register(new DoubleConverter(), Double.TYPE);
        convertUtilsBean.register(new DoubleConverter(), Double.class);
        convertUtilsBean.register(new FloatConverter(), Float.TYPE);
        convertUtilsBean.register(new FloatConverter(), Float.class);
        convertUtilsBean.register(new IntegerConverter(), Integer.TYPE);
        convertUtilsBean.register(new IntegerConverter(), Integer.class);
        convertUtilsBean.register(new LongConverter(), Long.TYPE);
        convertUtilsBean.register(new LongConverter(), Long.class);
        convertUtilsBean.register(new ShortConverter(), Short.TYPE);
        convertUtilsBean.register(new ShortConverter(), Short.class);
        convertUtilsBean.register(new RelaxedStringArrayConverter(), String[].class);

        beanUtilsBean = new BeanUtilsBean(convertUtilsBean, propertyUtilsBean = new PropertyUtilsBean());
        automaticBean = new AutomaticBean();
        automaticBean.configure(config);
    }

    @Test
    public void testConfigure() throws Exception {
        Configuration expectedConfig = new Configuration();
        expectedConfig.setAttributes(ListUtils.asMap(CONFIG_ATTRIBUTE_NAMES, CONFIG_ATTRIBUTE_VALUES));
        assertEquals(expectedConfig, automaticBean.getConfiguration());

        checkProperties(automaticBean);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_ChildConfig() throws Exception {
        Configuration childConfig = new Configuration();
        automaticBean.configure(childConfig);
        fail("Child configuration is not allowed");
    }

    @Test
    public void testContextualize() throws Exception {
        Properties contextProperties = new Properties();
        contextProperties.setProperty("booleanAttribute", String.valueOf(true));
        contextProperties.setProperty("byteAttribute", String.valueOf((byte) 127));
        contextProperties.setProperty("charAttribute", "'a'");
        contextProperties.setProperty("doubleAttribute", String.valueOf(3.14));
        contextProperties.setProperty("floatAttribute", String.valueOf(1.23f));
        contextProperties.setProperty("integerAttribute", String.valueOf(42));
        contextProperties.setProperty("longAttribute", String.valueOf(42L));
        contextProperties.setProperty("shortAttribute", String.valueOf((short) 32767));
        contextProperties.setProperty("stringArrayAttribute[]", StringUtils.join(Arrays.asList(new String[]{"test1", "test2"}).toArray(new String[0]), ","));

        automaticBean.contextualize(contextProperties);

        checkProperties(automaticBean);
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualize_InvalidAttribute() throws Exception {
        Properties contextProperties = new Properties();
        contextProperties.setProperty("invalidAttribute", "invalidValue");
        automaticBean.contextualize(contextProperties);

        fail("Invalid attribute should not be set");
    }

    private void checkProperties(AutomaticBean automaticBean) throws IllegalAccessException {
        for (String attributeName : CONFIG_ATTRIBUTE_NAMES) {
            Object attributeValue = PropertyUtils.getProperty(automaticBean, attributeName);
            Object configAttributeValue = PropertyUtils.getProperty(config, attributeName);
            assertEquals(configAttributeValue, attributeValue);
        }
    }
}

class RelaxedStringArrayConverter extends Converter {
    @Override
    public Object convert(Class type, Object value) {
        if (!(type == String[].class)) {
            throw new UnsupportedOperationException("This converter only supports String[] type");
        }

        String tokenizer = value.toString().trim();
        String[] tokens = StringUtils.split(tokenizer, ",");
        List<String> result = Arrays.asList(tokens);
        return tokens;
    }
}