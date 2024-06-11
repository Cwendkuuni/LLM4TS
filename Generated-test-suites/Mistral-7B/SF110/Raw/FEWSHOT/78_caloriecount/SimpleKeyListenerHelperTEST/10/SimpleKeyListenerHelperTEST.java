package com.lts.event;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper helper;
 private Component component;
 private SimpleKeyListener listener;

 @Before
 public void setUp() {
 component = mock(Component.class);
 listener = mock(SimpleKeyListener.class);
 helper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testConstruction() {
 assertNull(helper.component);
 }

 @Test
 public void testAddKeyListener() {
 helper.addKeyListener(listener);
 verify(component).addKeyListener(listener);
 }

 @Test
 public void testDetach() {
 helper.detach();
 verify(component).removeKeyListener(helper);
 }

 @Test
 public void testNotifyListener_enter() {
 helper.notifyListener(listener, 10, null);
 verify(listener).enter(component);
 }

 @Test
 public void testNotifyListener_insert() {
 helper.notifyListener(listener, 155, null);
 verify(listener).insert(component);
 }

 @Test
 public void testNotifyListener_delete() {
 helper.notifyListener(listener, 127, null);
 verify(listener).delete(component);
 }

 @Test
 public void testNotifyListener_tab() {
 helper.notifyListener(listener, 9, null);
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressed_enter() {
 helper.keyPressed(new KeyEvent(component, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ""));
 helper.fire(KeyEvent.VK_ENTER);
 verify(listener).enter(component);
 }

 @Test
 public void testKeyPressed_tab() {
 helper.keyPressed(new KeyEvent(component, 9, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ""));
 helper.fire(KeyEvent.VK_TAB);
 verify(listener).tab(component);
 }

 @Test
 public void testKeyPressed_delete() {
 helper.keyPressed(new KeyEvent(component, 127, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, ""));
 helper.fire(KeyEvent.VK_DELETE);
 verify(listener).delete(component);
 }

 @Test
 public void testKeyPressed_insert() {
 helper.keyPressed(new KeyEvent(component, 155, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, ""));
 helper.fire(KeyEvent.VK_INSERT);
 verify(listener).insert(component);
 }
}