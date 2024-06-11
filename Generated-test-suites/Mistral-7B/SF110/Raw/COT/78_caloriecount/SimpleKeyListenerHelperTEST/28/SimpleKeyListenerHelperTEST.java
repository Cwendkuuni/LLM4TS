package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener simpleKeyListener;

 @Before
 public void setup() {
 component = mock(Component.class);
 simpleKeyListener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testAttach() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 verify(component).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.detach();
 verify(component).removeKeyListener(simpleKeyListenerHelper);
 assertNull(simpleKeyListenerHelper.component);
 }

 @Test
 public void testNotifyListener_Enter() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
 verify(simpleKeyListener).enter(component);
 }

 @Test
 public void testNotifyListener_Insert() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_INSERT);
 verify(simpleKeyListener).insert(component);
 }

 @Test
 public void testNotifyListener_Delete() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_DELETE);
 verify(simpleKeyListener).delete(component);
 }

 @Test
 public void testNotifyListener_Tab() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 verify(simpleKeyListener).tab(component);
 }

 @Test(expected = NullPointerException.class)
 public void testNotifyListener_NullListener() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.notifyListener(null, 10, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNotifyListener_InvalidType() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.notifyListener(simpleKeyListener, 123, null);
 }

 @Test
 public void testKeyPressed_Enter() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEYCODE_ENTER, 0, 0));
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
 verify(simpleKeyListener).enter(component);
 }

 @Test
 public void testKeyPressed_Insert() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEYCODE_INSERT, 0, 0));
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_INSERT);
 verify(simpleKeyListener).insert(component);
 }

 @Test
 public void testKeyPressed_Delete() {
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeyListenerHelper.attach();
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEYCODE_DELETE, 0, 0));
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_DELETE);
 verify(simpleKeyListener).delete(component);
 }

 @Test
 public void testKeyPressed_Tab() {
 simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeylistenerHelper.attach();
 simpleKeylistenerHelper.keyPressed(new KeyEvent(component, KeyEvent.VK_TAB, 0, 0));
 simpleKeylistenerHelper.fire(KeyEvent.VK_TAB);
 verify(simpleKeylistener).tab(component);
 }

 @Test
 public void testKeyPressed_OtherKeyCodes() {
 simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeylistenerHelper.attach();
 simpleKeylistenerHelper.keyPressed(new KeyEvent(component, 123, 0, 0));
 simpleKeylistenerHelper.fire(123);
 // No verification as no listener method called
 }

 @Test
 public void testKeyReleased() {
 simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeylistenerHelper.attach();
 simpleKeylistenerHelper.keyReleased(new KeyEvent(component, KeyEvent.KEYCODE_ENTER, 0, 0));
 // No verification as no listener method called
 }

 @Test
 public void testKeyTyped() {
 simpleKeylistenerHelper = new SimpleKeyListenerHelper(component);
 simpleKeylistenerHelper.attach();
 simpleKeylistenerHelper.keyTyped(new KeyEvent(component, KeyEvent.KEYCODE_ENTER, 0, 0));
 // No verification as no listener method called
 }

}