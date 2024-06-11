package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

   @Test
   public void testConfigure() throws Exception {
      AutomaticBean automaticBean = new AutomaticBean();
      Configuration config = new Configuration();
      automaticBean.configure(config);
      assertEquals(config, automaticBean.getConfiguration());
   }

   @Test(expected = CheckstyleException.class)
   public void testConfigureWithNullConfig() {
      AutomaticBean automaticBean = new AutomaticBean();
      automaticBean.configure(null);
      Assert.assertNull(automaticBean.getConfiguration());
   }

   @Test
   public void testTryCopyProperty() throws Exception {
      AutomaticBean automaticBean = new AutomaticBean();
      automaticBean.tryCopyProperty("testModule", "testKey", "testValue", true);
      assertEquals("testValue", automaticBean.getConfiguration().getAttribute("testKey"));
   }

   @Test(expected = CheckstyleException.class)
   public void testTryCopyPropertyWithNullKey() throws Exception {
      AutomaticBean automaticBean = new AutomaticBean();
      automaticBean.tryCopyProperty(null, "testKey", "testValue", true);
      Assert.assertNull(automaticBean.getConfiguration());
   }

   @Test(expected = CheckstyleException.class)
   public void testTryCopyPropertyWithNullValue() throws Exception {
      AutomaticBean automaticBean = new AutomaticBean();
      automaticBean.tryCopyProperty("testModule", "testKey", null, true);
      Assert.assertNull(automaticBean.getConfiguration().getAttribute("testKey"));
   }

   @Test(expected = CheckstyleException.class)
   public void testTryCopyPropertyWithInvalidType() throws Exception {
      AutomaticBean automaticBean = new AutomaticBean();
      automaticBean.tryCopyProperty("testModule", "testKey", new Object(){}, true);
      Assert.assertNull(automaticBean.getConfiguration().getAttribute("testKey"));
   }

   @Test
   public void testContextualize() throws Exception {
      AutomaticBean automaticBean = new AutomaticBean();
      Context context = new Context() {
         @Override
         public String[] getAttributeNames() {
            return new String[]{"testKey1", "testKey2"};
         }

         @Override
         public Object get(String name) {
            if ("testKey1".equals(name)) {
               return "testValue1";
            } else if ("testKey2".equals(name)) {
               return "testValue2";
            }
            return null;
         }
      };

      automaticBean.contextualize(context);
      assertEquals("testValue1", automaticBean.getConfiguration().getAttribute("testKey1"));
      assertEquals("testValue2", automaticBean.getConfiguration().getAttribute("testKey2"));
   }

   @Test(expected = CheckstyleException.class)
   public void testContextualizeWithNullContext() {
      AutomaticBean automaticBean = new AutomaticBean();
      automaticBean.contextualize(null);
      Assert.assertNull(automaticBean.getConfiguration());
   }

   @Test
   public void testConfigurationGetters() throws Exception {
      AutomaticBean automaticBean = new AutomaticBean();
      Configuration config = new Configuration();
      automaticBean.configure(config);
      Configuration childConfig = new Configuration();
      automaticBean.setupChild(childConfig);

      assertEquals(config, automaticBean.getConfiguration());
      assertEquals(childConfig, automaticBean.getChildConfigs().get(0));
      assertTrue(automaticBean.isConfigured());
      assertTrue(automaticBean.isContextualized());
   }

   @Test
   public void testCreateBeanUtilsBean() throws Exception {
      AutomaticBean automaticBean = new AutomaticBean();
      BeanUtilsBean beanUtilsBean = automaticBean.createBeanUtilsBean();
      assertNotNull(beanUtilsBean);
   }
}