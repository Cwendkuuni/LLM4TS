package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleKeyListenerHelperTest {

 Component testComponent = new JFrame();
 SimpleKeyListener listener = new SimpleKeyListener() {
 @Override
 public void enter(Component component) {
 assertEquals(testComponent, component);
 }

 @Override
 public void insert(Component component) {
 fail("Should not be called in this test.");
 }

 @Override
 public void delete(Component component) {
 fail("Should not be called in this test.");
 }

 @Override
 public void tab(Component component) {
 fail("Should not be called in this test.");
 }
 };

 @Test
 public void testSimpleKeyListenerHelper() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
 helper.addListener(listener);
 testComponent.requestFocus();
 testComponent.dispatchEvent(new KeyEvent(testComponent, 10, System.currentTimeMillis(), 0, 0, KeyEvent.VK_ENTER, false));
 assertEquals(10, listener.getLastKeyCode());
 helper.detach();
 }

 @Test
 public void testSimpleKeyListenerHelper_keyPressed_Enter() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
 helper.addListener(listener);
 testComponent.requestFocus();
 testComponent.dispatchEvent(new KeyEvent(testComponent, 10, System.currentTimeMillis(), 0, 0, KeyEvent.VK_ENTER, false));
 assertEquals(10, listener.getLastKeyCode());
 helper.detach();
 }

 @Test
 public void testSimpleKeyListenerHelper_keyPressed_Tab() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
 helper.addListener(listener);
 testComponent.requestFocus();
 testComponent.dispatchEvent(new KeyEvent(testComponent, 9, System.currentTimeMillis(), 0, 0, KeyEvent.VK_TAB, false));
 assertEquals(9, listener.getLastKeyCode());
 helper.detach();
 }

 @Test
 public void testSimpleKeyListenerHelper_keyPressed_Backspace() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
 helper.addListener(listener);
 testComponent.requestFocus();
 testComponent.dispatchEvent(new KeyEvent(testComponent, 127, System.currentTimeMillis(), 0, 0, KeyEvent.VK_BACK_SPACE, false));
 assertEquals(127, listener.getLastKeyCode());
 helper.detach();
 }

 @Test
 public void testSimpleKeyListenerHelper_keyPressed_Delete() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
 helper.addListener(listener);
 testComponent.requestFocus();
 testComponent.dispatchEvent(new KeyEvent(testComponent, 155, System.currentTimeMillis(), 0, 0, KeyEvent.VK_DELETE, false));
 assertEquals(155, listener.getLastKeyCode());
 helper.detach();
 }

 @Test
 public void testSimpleKeyListenerHelper_Detach() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(testComponent);
 helper.addListener(listener);
 helper.detach();
 testComponent.dispatchEvent(new KeyEvent(testComponent, 10, System.currentTimeMillis(), 0, 0, KeyEvent.VK_ENTER, false));
 assertNull(listener.getLastKeyCode());
 }

 @Test(expected = NullPointerException.class)
 public void testSimpleKeyListenerHelper_NullComponent() {
 new SimpleKeyListenerHelper(null);
 }
}