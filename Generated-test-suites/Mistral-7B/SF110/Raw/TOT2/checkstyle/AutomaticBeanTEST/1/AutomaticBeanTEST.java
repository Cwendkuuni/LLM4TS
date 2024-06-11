package com.puppycrawl.tools.checkstyle.api.test;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullModuleName() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty(null, "key", "value", true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullKey() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty("moduleName", null, "value", true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullValue() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty("moduleName", "key", null, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithRecheckButPropertyDoesNotExist() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty("moduleName", "nonexistentProperty", "value", true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidValueType() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty("moduleName", "key", new Object() {}, true);
    }

    @Test
    public void testContextualize() {
        Context context = new Context();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.contextualize(context);
        // add assertions if necessary
    }

    @Test
    public void testGetConfiguration() {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration config = automaticBean.getConfiguration();
        assertNotNull(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullChildConf() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConf = null;
        automaticBean.setupChild(childConf);
    }
}