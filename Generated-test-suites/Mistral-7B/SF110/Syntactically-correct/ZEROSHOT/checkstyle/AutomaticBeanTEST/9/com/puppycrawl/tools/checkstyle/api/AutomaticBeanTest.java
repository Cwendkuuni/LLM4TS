package com.example.automaticbean;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.beanutils.propertyeditors.StringTrimmerEditor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AutomaticBean.class})
public class AutomaticBeanTest {

private static final String CONFIG_NAME = "Configuration";
private static final String PROPERTY_NAME = "property";
private static final String PROPERTY_VALUE = "value";
private static final String CHILD_CONFIG_NAME = "ChildConfiguration";

@Test
public void testConfigure() throws Exception {
Configuration config = Mockito.mock(Configuration.class);
PowerMockito.when(config.getAttributeNames()).thenReturn(new String[] {PROPERTY_NAME});
PowerMockito.when(config.getAttribute(PROPERTY_NAME)).thenReturn(PROPERTY_VALUE);

AutomaticBean automaticBean = new AutomaticBean();
automaticBean.configure(config);

assertEquals(PROPERTY_VALUE, automaticBean.getConfiguration().getAttribute(PROPERTY_NAME));
}

@Test(expected = CheckstyleException.class)
public void testConfigureInvalidProperty() throws Exception {
Configuration config = Mockito.mock(Configuration.class);
PowerMockito.when(config.getAttributeNames()).thenReturn(new String[] {"INVALID_PROPERTY_NAME"});

AutomaticBean automaticBean = new AutomaticBean();
automaticBean.configure(config);

fail("Should have thrown CheckstyleException");
}

@Test
public void testTryCopyProperty() throws Exception {
AutomaticBean automaticBean = new AutomaticBean();

BeanUtilsBean beanUtilsBean = PowerMockito.spy(new BeanUtilsBean());
PowerMockito.doAnswer(invocation -> {
PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, invocation.getArgument(1));
return descriptor;
}).when(beanUtilsBean).getPropertyDescriptor(automaticBean, PROPERTY_NAME);

Mockito.doThrow(new CheckstyleException("Property does not exist")).when(beanUtilsBean).copyProperty(any(Object.class), eq(PROPERTY_NAME), eq(PROPERTY_VALUE), anyBoolean());

try {
automaticBean.tryCopyProperty(CONFIG_NAME, PROPERTY_NAME, PROPERTY_VALUE, true);
fail("Should have thrown CheckstyleException");
} catch (CheckstyleException e) {
assertEquals("Property 'property' in module Configuration does not exist, please check the documentation", e.getMessage());
}

ConvertUtils.register(new BooleanConverter(), Boolean.TYPE);
ConvertUtils.register(new BooleanConverter(), Boolean.class);

automaticBean.setConfiguration(Mockito.mock(Configuration.class));
PowerMockito.doAnswer(invocation -> {
PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, invocation.getArgument(1));
BeanUtils.copyProperty(automaticBean, descriptor.getName(), PROPERTY_VALUE);
return null;
}).when(beanUtilsBean).copyProperty(any(Object.class), eq(PROPERTY_NAME), eq(PROPERTY_VALUE), anyBoolean());

automaticBean.tryCopyProperty(CONFIG_NAME, PROPERTY_NAME, PROPERTY_VALUE, true);
assertEquals(PROPERTY_VALUE, automaticBean.getConfiguration().getAttribute(PROPERTY_NAME));
}

@Test(expected = CheckstyleException.class)
public void testTryCopyPropertyInvalidValue() throws Exception {
AutomaticBean automaticBean = new AutomaticBean();
automaticBean.setConfiguration(Mockito.mock(Configuration.class));

BeanUtilsBean beanUtilsBean = PowerMockito.spy(new BeanUtilsBean());
PowerMockito.doAnswer(invocation -> {
PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, invocation.getArgument(1));
return descriptor;
}).when(beanUtilsBean).getPropertyDescriptor(automaticBean, PROPERTY_NAME);

PowerMockito.doThrow(new CheckstyleException("Invalid value")).when(beanUtilsBean).copyProperty(any(Object.class), eq(PROPERTY_NAME), eq("invalidValue"), anyBoolean());

try {
automaticBean.tryCopyProperty(CONFIG_NAME, PROPERTY_NAME, "invalidValue", true);
fail("Should have thrown CheckstyleException");
} catch (CheckstyleException e) {
assertEquals("illegal value 'invalidValue' for property 'property' of module Configuration", e.getMessage());
}
}

@Test(expected = CheckstyleException.class)
public void testTryCopyPropertyInvalidType() throws Exception {
AutomaticBean automaticBean = new AutomaticBean();
automaticBean.setConfiguration(Mockito.mock(Configuration.class));

BeanUtilsBean beanUtilsBean = PowerMockito.spy(new BeanUtilsBean());
PowerMockito.doAnswer(invocation -> {
PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, invocation.getArgument(1));
return descriptor;
}).when(beanUtilsBean).getPropertyDescriptor(automaticBean, PROPERTY_NAME);

PowerMockito.doAnswer(invocation -> {
throw new CheckstyleException("Invalid type");
}).when(beanUtilsBean).copyProperty(any(Object.class), eq(PROPERTY_NAME), any(), anyBoolean());

try {
automaticBean.tryCopyProperty(CONFIG_NAME, "INVALID_PROPERTY_NAME", "value", true);
fail("Should have thrown CheckstyleException");
} catch (CheckstyleException e) {
assertEquals("Cannot set property 'INVALID_PROPERTY_NAME' to 'value' in module Configuration", e.getMessage());
}
}

@Test
public void testContextualize() throws Exception {
Context context = Mockito.mock(Context.class);
PowerMockito.when(context.getAttributeNames()).thenReturn(new String[] {PROPERTY_NAME});
PowerMockito.when(context.get(PROPERTY_NAME)).thenReturn(PROPERTY_VALUE);

AutomaticBean automaticBean = new AutomaticBean();
automaticBean.contextualize(context);
assertEquals(PROPERTY_VALUE, automaticBean.getConfiguration().getAttribute(PROPERTY_NAME));
}

@Test
public void testFinishLocalSetup() throws Exception {
AutomaticBean automaticBean = new AutomaticBean() {
@Override
protected void finishLocalSetup() throws CheckstyleException {
fail("finishLocalSetup should not be called");
}
};

try {
automaticBean.configure(Mockito.mock(Configuration.class));
fail("Should have thrown CheckstyleException");
} catch (CheckstyleException e) {
assertEquals("finishLocalSetup should be implemented in " + AutomaticBean.class.getName(), e.getMessage());
}
}

@Test(expected = CheckstyleException.class)
public void testSetupChildInvalidChild() throws Exception {
Configuration childConfig = Mockito.mock(Configuration.class);
AutomaticBean automaticBean = new AutomaticBean();
automaticBean.setConfiguration(Mockito.mock(Configuration.class));

try {
automaticBean.setupChild(childConfig);
fail("Should have thrown CheckstyleException");
} catch (CheckstyleException e) {
assertEquals(childConfig.getName() + " is not allowed as a child in " + CONFIG_NAME, e.getMessage());
}
}

@Test
public void testGetConfiguration() {
AutomaticBean automaticBean = new AutomaticBean();
assertNotNull(automaticBean.getConfiguration());
}
}