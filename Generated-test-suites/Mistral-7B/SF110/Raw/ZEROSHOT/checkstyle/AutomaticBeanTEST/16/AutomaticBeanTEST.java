package com.example.automaticbean;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.support.annotation.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({AutomaticBean.class, PropertyUtils.class, BeanUtilsBean.class, ConvertUtilsBean.class})
public class AutomaticBeanTest {

    @Test
    public void testAutomaticBeanCreation() {
        final AutomaticBean bean = new AutomaticBean();
        assertNull(bean.getConfiguration());
    }

    @Test
    public void testCreateBeanUtilsBean() {
        final ConvertUtilsBean cub = new ConvertUtilsBean();
        final List<Class<? extends Converter>> converters = Arrays.asList(
                BooleanConverter.class, ByteConverter.class, CharacterConverter.class,
                DoubleConverter.class, FloatConverter.class, IntegerConverter.class,
                LongConverter.class, ShortConverter.class, RelaxedStringArrayConverter.class);

        for (final Class<? extends Converter> converter : converters) {
            final Converter beanConverter = converter.newInstance();
            cub.register(beanConverter, converter.getType());
        }

        final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(cub, new PropertyUtilsBean());
        assertNotNull(beanUtilsBean);
    }

    @Test
    public void testConfigure() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();

        config.addAttribute("testBoolean", "true");
        config.addAttribute("testByte", "127");
        config.addAttribute("testCharacter", "'A'");
        config.addAttribute("testDouble", "3.14159265358979323846");
        config.addAttribute("testFloat", "3.14159265f");
        config.addAttribute("testInteger", "42");
        config.addAttribute("testLong", "9223372036854775807");
        config.addAttribute("testShort", "Short.MIN_VALUE");
        config.addAttribute("testStringArray", "element1,element2");

        bean.configure(config);

        final Configuration configuration = bean.getConfiguration();
        assertEquals("true", configuration.getAttribute("testBoolean"));
        assertEquals((Byte)Byte.MAX_VALUE, configuration.getAttribute("testByte"));
        assertEquals((Character)'A', configuration.getAttribute("testCharacter"));
        assertEquals(3.14159265358979323846d, configuration.getAttribute("testDouble"), 0.00001);
        assertEquals(3.14159265f, configuration.getAttribute("testFloat"), 0.00001f);
        assertEquals(42, configuration.getAttribute("testInteger"));
        assertEquals(Long.MAX_VALUE, configuration.getAttribute("testLong"));
        assertEquals((Short)Short.MIN_VALUE, configuration.getAttribute("testShort"));
        assertArrayEquals(new String[]{"element1", "element2"}, (String[])configuration.getAttribute("testStringArray"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_NonExistentProperty() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();

        bean.configure(config);

        final String message = "Property 'nonExistentProperty' in module AutomaticBean does not exist, please check the documentation";
        throw new AssertionError(message);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_InvalidType_Boolean() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();

        config.addAttribute("testBoolean", "invalidType");

        bean.configure(config);

        final String message = "illegal value 'invalidType' for property 'testBoolean' of module AutomaticBean";
        throw new AssertionError(message);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_InvalidType_Integer() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();

        config.addAttribute("testInteger", "invalidType");

        bean.configure(config);

        final String message = "Cannot set property 'testInteger' to 'invalidType' in module AutomaticBean";
        throw new AssertionError(message);
    }

    @Test
    public void testContextualize() throws Exception {
        final AutomaticBean bean = new AutomaticBean();

        final Configuration context = new Configuration();
        context.setName("testContext");
        context.addAttribute("testBoolean", "true");
        context.addAttribute("testByte", "127");
        context.addAttribute("testCharacter", "'A'");
        context.addAttribute("testDouble", "3.14159265358979323846");
        context.addAttribute("testFloat", "3.14159265f");
        context.addAttribute("testInteger", "42");
        context.addAttribute("testLong", "9223372036854775807");
        context.addAttribute("testShort", "Short.MIN_VALUE");
        context.addAttribute("testStringArray", "element1,element2");

        bean.contextualize(context);

        final Configuration configuration = bean.getConfiguration();
        assertEquals("true", configuration.getAttribute("testBoolean"));
        assertEquals((Byte)Byte.MAX_VALUE, configuration.getAttribute("testByte"));
        assertEquals((Character)'A', configuration.getAttribute("testCharacter"));
        assertEquals(3.14159265358979323846d, configuration.getAttribute("testDouble"), 0.00001);
        assertEquals(3.14159265f, configuration.getAttribute("testFloat"), 0.00001f);
        assertEquals(42, configuration.getAttribute("testInteger"));
        assertEquals(Long.MAX_VALUE, configuration.getAttribute("testLong"));
        assertEquals((Short)Short.MIN_VALUE, configuration.getAttribute("testShort"));
        assertArrayEquals(new String[]{"element1", "element2"}, (String[])configuration.getAttribute("testStringArray"));
    }

    @Test
    public void testContextualize_NullContext() throws Exception {
        final AutomaticBean bean = new AutomaticBean();

        bean.contextualize(null);

        assertNull(bean.getConfiguration());
    }

    @Test
    public void testContextualize_NullConfigAttribute() throws Exception {
        final AutomaticBean bean = new AutomaticBean();

        final Configuration context = new Configuration();
        context.setName("testContext");

        bean.contextualize(context);

        assertNull(bean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        final AutomaticBean bean = new AutomaticBean() {
            @Override
            protected void finishLocalSetup() throws CheckstyleException {
                throw new AssertionError("finishLocalSetup should not be called");
            }
        };

        final Configuration config = new Configuration();
        config.addAttribute("testBoolean", "true");

        bean.configure(config);

        assertFalse(bean.getClass().isInstance(bean));
    }

    @Test
    public void testTryCopyProperty_ValidType() throws Exception {
        final AutomaticBean bean = new AutomaticBean();

        final Configuration config = new Configuration();
        config.setName("testModule");
        config.addAttribute("testBoolean", "true");

        bean.tryCopyProperty("testModule", "testBoolean", true);

        assertTrue(bean.getClass().isInstance(bean));
    }

    @Test
    public void testTryCopyProperty_InvalidType() throws Exception {
        final AutomaticBean bean = new AutomaticBean();

        final Configuration config = new Configuration();
        config.setName("testModule");
        config.addAttribute("testBoolean", "invalidType");

        bean.tryCopyProperty("testModule", "testBoolean", true);

        assertEquals("illegal value 'invalidType' for property 'testBoolean' of module testModule", bean.getMessage());
    }

    @Test
    public void testTryCopyProperty_NullConfig() throws Exception {
        final AutomaticBean bean = new AutomaticBean();

        bean.tryCopyProperty(null, "testBoolean", true);

        assertNull(bean.getConfiguration());
    }

    @Test
    public void testTryCopyProperty_NonExistentProperty() throws Exception {
        final AutomaticBean bean = new AutomaticBean();

        final Configuration config = new Configuration();
        config.setName("testModule");

        bean.tryCopyProperty("testModule", "nonExistentProperty", true);

        assertNull(bean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_InvalidType_Boolean() throws Exception {
        final AutomaticBean bean = new AutomaticBean();

        final Configuration config = new Configuration();
        config.setName("testModule");
        config.addAttribute("testBoolean", "invalidType");

        bean.tryCopyProperty("testModule", "testBoolean", true);

        assertNull(bean.getConfiguration());
        final String message = "illegal value 'invalidType' for property 'testBoolean' of module testModule";
        throw new AssertionError(message);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_InvalidType_Integer() throws Exception {
        final AutomaticBean bean = new AutomaticBean();

        final Configuration config = new Configuration();
        config.setName("testModule");
        config.addAttribute("testInteger", "invalidType");

        bean.tryCopyProperty("testModule", "testInteger", true);

        assertNull(bean.getConfiguration());
        final String message = "Cannot set property 'testInteger' to 'invalidType' in module testModule";
        throw new AssertionError(message);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild() throws Exception {
        final AutomaticBean parent = new AutomaticBean();
        final Configuration childConfig = new Configuration();

        parent.setupChild(childConfig);

        final String message = "Child configuration is not allowed as a child in AutomaticBean";
        throw new AssertionError(message);
    }

    @Test
    public void testSetupChild_NullChildConfig() throws Exception {
        final AutomaticBean parent = new AutomaticBean();

        parent.setupChild(null);

        assertNull(parent.getConfiguration());
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final AutomaticBean bean = new AutomaticBean();

        final Object value = Arrays.asList("element1", "element2").toArray();
        final Object convertedValue = bean.convert(List.class, value);

        assertTrue(convertedValue instanceof String[]);
        assertArrayEquals(new String[]{"element1", "element2"}, (String[])convertedValue);
    }

    @Test
    public void testRelaxedStringArrayConverter_EmptyList() {
        final AutomaticBean bean = new AutomaticBean();

        final Object value = Collections.emptyList().toArray();
        final Object convertedValue = bean.convert(List.class, value);

        assertTrue(convertedValue instanceof String[]);
        assertArrayEquals(new String[0], (String[])convertedValue);
    }

    @Test
    public void testRelaxedStringArrayConverter_NullList() {
        final AutomaticBean bean = new AutomaticBean();

        final Object value = null;
        final Object convertedValue = bean.convert(List.class, value);

        assertNull(convertedValue);
    }

}