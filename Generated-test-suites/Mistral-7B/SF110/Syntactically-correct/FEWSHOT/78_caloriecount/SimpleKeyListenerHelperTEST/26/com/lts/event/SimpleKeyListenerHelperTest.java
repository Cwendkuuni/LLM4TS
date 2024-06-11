package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener listener;

 @Before
 public void setup() {
 component = mock(Component.class);
 listener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyKeyListenerHelper(component);
 }

 @Test
 public void testConstructor() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testAttach() {
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 verify(component).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 verify(component).removeKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testEnter() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(10);
 verify(listener).enter(component);
 }

 @Test
 public void testInsert() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(155);
 verify(listener).insert(component);
 }

 @Test
 public void testDelete() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(127);
 verify(listener).delete(component);
 }

 @Test
 public void testTab() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(9);
 verify(listener).tab(component);
 }

 @Test
 public void testFireKeyCodeNotHandled() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(1);
 // No verification expected
 }

 @Test(expected = NullPointerException.class)
 public void testFireNullListener() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(10);
 simpleKeyListenerHelper.setComponent(null);
 simpleKeyListenerHelper.fire(10);
 }
}