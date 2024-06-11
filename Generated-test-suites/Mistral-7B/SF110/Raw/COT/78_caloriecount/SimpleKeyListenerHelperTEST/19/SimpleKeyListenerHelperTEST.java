package com.lts.event.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.lts.event.SimpleKeyListenerHelper;
import com.lts.event.SimpleKeyListener;
import java.awt.Component;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener simpleKeyListener;
 private Component component;

 @Before
 public void setup() {
 simpleKeyListener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 // Empty implementation
 }

 @Override
 public void insert(Component component) {
 // Empty implementation
 }

 @Override
 public void delete(Component component) {
 // Empty implementation
 }

 @Override
 public void tab(Component component) {
 // Empty implementation
 }
 };
 component = new Component() {
 // Empty implementation
 };
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstructor() {
 SimpleKeyListenerHelper expectedSimpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 assertEquals(expectedSimpleKeyListenerHelper, simpleKeyListenerHelper);
 }

 @Test
 public void testAttach() {
 simpleKeyListenerHelper.attach();
 assertTrue(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).containsKey(KeyEvent.VK_ENTER));
 assertTrue(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).containsKey(KeyEvent.VK_INSERT));
 assertTrue(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).containsKey(KeyEvent.VK_DELETE));
 assertTrue(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).containsKey(KeyEvent.VK_TAB));
 }

 @Test(expected = NullPointerException.class)
 public void testDetachNullComponent() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(null);
 simpleKeyListenerHelper.detach();
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.component);
 assertFalse(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).containsKey(KeyEvent.VK_ENTER));
 assertFalse(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).containsKey(KeyEvent.VK_INSERT));
 assertFalse(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).containsKey(KeyEvent.VK_DELETE));
 assertFalse(component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).containsKey(KeyEvent.VK_TAB));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDetachNullListenerHelper() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper = null;
 simpleKeyListenerHelper.detach();
 }

 @Test
 public void testNotifyListenerEnter() {
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);
 assertTrue(simpleKeyListener.isInvoke);
 reset(simpleKeyListener);
 simpleKeyListener.isInvoke = false;
 }

 @Test
 public void testNotifyListenerInsert() {
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);
 assertTrue(simpleKeyListener.isInvoke);
 reset(simpleKeyListener);
 simpleKeyListener.isInvoke = false;
 }

 @Test
 public void testNotifyListenerDelete() {
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.VK_DELETE);
 assertTrue(simpleKeyListener.isInvoke);
 reset(simpleKeyListener);
 simpleKeyListener.isInvoke = false;
 }

 @Test
 public void testNotifyListenerTab() {
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 assertTrue(simpleKeyListener.isInvoke);
 reset(simpleKeyListener);
 simpleKeyListener.isInvoke = false;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyListenerNullListener() {
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyListenerInvalidType() {
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(128);
 }
}