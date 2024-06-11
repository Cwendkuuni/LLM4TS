package com.example.junit4;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();

        // Given: An empty configuration object
        automaticBean.configure(config);

        // When: Trying to set properties on the AutomaticBean using an empty configuration
        assertNull(automaticBean.getConfiguration());

        // Given: A configuration object with properties
        config.addAttribute("testKey1", "testValue1");
        config.addAttribute("testKey2", "testValue2");

        // When: Trying to set properties on the AutomaticBean using the configuration
        automaticBean.configure(config);

        // Then: The properties should have been copied to the AutomaticBean
        Configuration configuration = automaticBean.getConfiguration();
        assertEquals("testValue1", configuration.getAttribute("testKey1"));
        assertEquals("testValue2", configuration.getAttribute("testKey2"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfiguration() {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithInvalidProperty() {
        Configuration config = new Configuration();
        config.addAttribute("testKey1", null);
        AutomaticBean automaticBean = new AutomaticBean();

        // When: Trying to set a property with a null value to the AutomaticBean using the configuration
        automaticBean.configure(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithInvalidDataType() {
        Configuration config = new Configuration();
        config.addAttribute("testKey1", new Object());
        AutomaticBean automaticBean = new AutomaticBean();

        // When: Trying to set a property with an invalid data type to the AutomaticBean using the configuration
        automaticBean.configure(config);
    }
}