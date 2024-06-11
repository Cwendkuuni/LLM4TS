package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

    private AutomaticBean bean;
    private Configuration config;

    @Before
    public void setUp() {
        config = new Configuration();
        config.addAttribute("testBoolean", true);
        config.addAttribute("testByte", (byte) 10);
        config.addAttribute("testCharacter", 'A');
        config.addAttribute("testDouble", 10.0);
        config.addAttribute("testFloat", 10.1f);
        config.addAttribute("testInteger", 10);
        config.addAttribute("testLong", 10L);
        config.addAttribute("testShort", (short) 10);
        config.addAttribute("testStringArray", new String[]{"test1", "test2"});
        config.addAttribute("testConfiguration", new Configuration());

        bean = new AutomaticBean();
        bean.configure(config);
    }

    @Test
    public void testConfigure() {
        Configuration configuration = bean.getConfiguration();
        assertEquals("testConfiguration", configuration.getName());
        List<String> attributeNames = configuration.getAttributeNames();
        assertEquals(10, attributeNames.size());
        List<String> attributeNamesExpected = Arrays.asList("testBoolean", "testByte", "testCharacter", "testDouble",
                "testFloat", "testInteger", "testLong", "testShort", "testStringArray");
        ListUtils.isEqualCollection(attributeNames, attributeNamesExpected);

        Object testBoolean = configuration.getAttribute("testBoolean");
        assertTrue((Boolean) testBoolean);

        Object testByte = configuration.getAttribute("testByte");
        assertEquals((byte) 10, testByte);

        Object testCharacter = configuration.getAttribute("testCharacter");
        assertEquals('A', testCharacter);

        Object testDouble = configuration.getAttribute("testDouble");
        assertEquals(10.0, testDouble, 0.01);

        Object testFloat = configuration.getAttribute("testFloat");
        assertEquals(10.1, testFloat, 0.01);

        Object testInteger = configuration.getAttribute("testInteger");
        assertEquals(10, testInteger);

        Object testLong = configuration.getAttribute("testLong");
        assertEquals(10L, testLong);

        Object testShort = configuration.getAttribute("testShort");
        assertEquals((short) 10, testShort);

        Object testStringArray = configuration.getAttribute("testStringArray");
        assertArrayEquals(new String[]{"test1", "test2"}, (String[]) testStringArray);

        Configuration childConfiguration = (Configuration) configuration.getAttribute("testConfiguration");
        assertEquals(0, childConfiguration.getAttributeNames().size());

        // Test for exception when trying to set an invalid attribute
        try {
            config.setAttribute("testInvalid", new Object());
            bean = new AutomaticBean();
            bean.configure(config);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }
    }

    @Test
    public void testCopyProperty() {
        Boolean testBoolean = false;
        Character testCharacter = 'B';
        String testString = "testString";
        Short testShort = (short) 20;

        bean.tryCopyProperty(bean.getClass().getName(), "testBoolean", testBoolean, true);
        assertEquals(testBoolean, bean.getConfiguration().getAttribute("testBoolean"));

        bean.tryCopyProperty(bean.getClass().getName(), "testCharacter", testCharacter, true);
        assertEquals(testCharacter, bean.getConfiguration().getAttribute("testCharacter"));

        bean.tryCopyProperty(bean.getClass().getName(), "testString", testString, true);
        assertEquals(testString, bean.getConfiguration().getAttribute("testString"));

        bean.tryCopyProperty(bean.getClass().getName(), "testShort", testShort, true);
        assertEquals(testShort, bean.getConfiguration().getAttribute("testShort"), 0);

        // Test for exception when trying to copy property with an invalid name
        try {
            bean.tryCopyProperty(bean.getClass().getName(), "testInvalid", new Object(), true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }
    }

    @Test
    public void testContextualize() {
        bean.contextualize(new Context() {
            @Override
            public String getName() {
                return "testContext";
            }

            @Override
            public Object get(String key) {
                if ("testBoolean".equals(key)) {
                    return true;
                }
                return null;
            }
        });

        assertEquals(true, bean.getBooleanAttribute("testBoolean"));
        assertNull(bean.getConfiguration().getAttribute("testCharacter"));
    }

    @Test
    public void testGetConfiguration() {
        assertEquals(config, bean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() {
        // Empty test
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithInvalidConfiguration() {
        Configuration childConfig = new Configuration();
        bean.setupChild(childConfig);
    }
}