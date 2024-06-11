package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AutomaticBean.class })
public class AutomaticBeanTest {

  @Test
  public void testConfigureWithValidAttributes() throws Exception {
    final Configuration config = new Configuration();
    config.addAttribute("attribute1", "value1");
    config.addAttribute("attribute2", "value2");

    final AutomaticBean bean = new AutomaticBean();
    bean.configure(config);

    final PropertyDescriptor descriptor1 = PropertyUtils.getPropertyDescriptor(bean, "attribute1");
    final PropertyDescriptor descriptor2 = PropertyUtils.getPropertyDescriptor(bean, "attribute2");

    assertEquals("value1", descriptor1.getReadMethod().invoke(bean));
    assertEquals("value2", descriptor2.getReadMethod().invoke(bean));
  }

  @Test(expected = CheckstyleException.class)
  public void testConfigureWithMissingAttribute() throws Exception {
    final Configuration config = new Configuration();

    final AutomaticBean bean = new AutomaticBean();
    bean.configure(config);

    final String message = "Property 'missingAttribute' in module AutomaticBean does not exist, please check the documentation";
    try {
      final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "missingAttribute");
    } catch (NoSuchPropertyException e) {
      throw new AssertionError(message);
    }
  }

  @Test(expected = CheckstyleException.class)
  public void testConfigureWithInvalidAttributeType() throws Exception {
    final Configuration config = new Configuration();
    config.addAttribute("attribute", new Object());

    final AutomaticBean bean = new AutomaticBean();
    bean.configure(config);

    final String message = "illegal value 'java.lang.Object' for property 'attribute' of module AutomaticBean";
    try {
      final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "attribute");
      descriptor.getWriteMethod().invoke(bean, new Object());
    } catch (IllegalArgumentException e) {
      throw new AssertionError(message);
    }
  }

  @Test
  public void testContextualizeWithValidAttributes() throws Exception {
    final Configuration context = new Configuration();
    context.putAttribute("attribute1", "value1");
    context.putAttribute("attribute2", "value2");

    final AutomaticBean bean = new AutomaticBean();
    bean.contextualize(context);

    final PropertyDescriptor descriptor1 = PropertyUtils.getPropertyDescriptor(bean, "attribute1");
    final PropertyDescriptor descriptor2 = PropertyUtils.getPropertyDescriptor(bean, "attribute2");

    assertEquals("value1", descriptor1.getReadMethod().invoke(bean));
    assertEquals("value2", descriptor2.getReadMethod().invoke(bean));
  }

  @Test(expected = CheckstyleException.class)
  public void testContextualizeWithMissingAttribute() throws Exception {
    final Configuration context = new Configuration();

    final AutomaticBean bean = new AutomaticBean();
    bean.contextualize(context);

    final String message = "Property 'missingAttribute' in module AutomaticBean does not exist, please check the documentation";
    try {
      final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "missingAttribute");
    } catch (NoSuchPropertyException e) {
      throw new AssertionError(message);
    }
  }

  @Test
  public void testTryCopyPropertyWithValidValues() throws Exception {
    final Configuration config = new Configuration();
    config.addAttribute("attribute1", "value1");
    config.addAttribute("attribute2.nested.attribute", "nestedValue");

    final AutomaticBean bean = new AutomaticBean();
    bean.configure(config);

    bean.tryCopyProperty(AutomaticBean.class.getName(), "attribute1", "value1", true);
    bean.tryCopyProperty(AutomaticBean.class.getName(), "attribute2.nested.attribute", "nestedValue", true);

    final PropertyDescriptor descriptor1 = PropertyUtils.getPropertyDescriptor(bean, "attribute1");
    final PropertyDescriptor descriptor2 = PropertyUtils.getPropertyDescriptor(bean, "attribute2.nested.attribute");

    assertEquals("value1", descriptor1.getReadMethod().invoke(bean));
    assertEquals("nestedValue", descriptor2.getReadMethod().invoke(bean));
  }

  @Test(expected = CheckstyleException.class)
  public void testTryCopyPropertyWithInvalidPropertyName() throws Exception {
    final Configuration config = new Configuration();
    config.addAttribute("attribute1", "value1");

    final AutomaticBean bean = new AutomaticBean();
    bean.configure(config);

    final String message = "Property 'attribute2.nested.attribute' in module AutomaticBean does not exist, please check the documentation";
    try {
      bean.tryCopyProperty(AutomaticBean.class.getName(), "attribute2.nested.attribute", " nestedValue", true);
    } catch (CheckstyleException e) {
      assertEquals(message, e.getMessage());
    }
  }

  @Test(expected = CheckstyleException.class)
  public void testTryCopyPropertyWithInvalidValueType() throws Exception {
    final Configuration config = new Configuration();
    config.addAttribute("attribute1", "value1");

    final AutomaticBean bean = new AutomaticBean();
    bean.configure(config);

    final String message = "Cannot set property 'attribute1' to 'value2' in module AutomaticBean";
    try {
      bean.tryCopyProperty(AutomaticBean.class.getName(), "attribute1", new Object(), true);
    } catch (CheckstyleException e) {
      assertEquals(message, e.getMessage());
    }
  }

  @Test
  public void testSetupChildWithNullChild() throws Exception {
    final Configuration childConfig = null;

    final AutomaticBean bean = new AutomaticBean();
    bean.setupChild(childConfig);

    assertNull(bean.getConfiguration());
  }

  @Test(expected = CheckstyleException.class)
  public void testSetupChildWithValidChild() throws Exception {
    final Configuration childConfig = new Configuration();

    final AutomaticBean bean = new AutomaticBean();
    bean.setupChild(childConfig);

    final String message = childConfig.getName() + " is not allowed as a child in " + AutomaticBean.class.getName();
    throw new CheckstyleException(message);
  }
}