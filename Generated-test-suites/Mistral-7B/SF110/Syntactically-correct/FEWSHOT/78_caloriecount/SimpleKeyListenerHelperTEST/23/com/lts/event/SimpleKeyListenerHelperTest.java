package com.lts.event;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleKeyListenerHelperTest {

 @Mock
 private Component component;
 @Mock
 private SimpleKeyListener listener;

 @Test
 public void testConstructor() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 assertNull(helper.getComponent());
 }

 @Test
 public void testAttach() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 verify(this.component).addKeyListener(helper);
 }

 @Test
 public void testDetach() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.detach();
 verify(this.component).removeKeyListener(helper);
 }

 @Test
 public void testNotifyListenerEnter() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.fire(KeyEvent.KEY_CODE_ENTER);
 verify(this.listener).enter(this.component);
 }

 @Test
 public void testNotifyListenerInsert() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.fire(KeyEvent.KEY_CODE_INSERT);
 verify(this.listener).insert(this.component);
 }

 @Test
 public void testNotifyListenerDelete() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
 verify(this.listener).delete(this.component);
 }

 @Test
 public void testNotifyListenerTab() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.fire(KeyEvent.KEY_CODE_Tab);
 verify(this.listener).tab(this.component);
 }

 @Test
 public void testKeyPressedBackSpace() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.keyPressed(new KeyEvent(this.component, KeyEvent.KEY_CODE_BACK_SPACE, 1, 0));
 helper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
 verify(this.listener).delete(this.component);
 }

 @Test
 public void testKeyPressedEnter() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.keyPressed(new KeyEvent(this.component, KeyEvent.KEY_CODE_ENTER, 1, 0));
 helper.fire(KeyEvent.KEY_CODE_ENTER);
 verify(this.listener).enter(this.component);
 }

 @Test
 public void testKeyPressedTab() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.keyPressed(new KeyEvent(this.component, KeyEvent.KEY_CODE_Tab, 1, 0));
 helper.fire(KeyEvent.KEY_CODE_Tab);
 verify(this.listener).tab(this.component);
 }

 @Test
 public void testKeyPressedOtherKey() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.keyPressed(new KeyEvent(this.component, 5, 1, 0));
 helper.fire(5);
 assertNull(listener);
 }

 @Test
 public void testKeyReleased() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.keyReleased(new KeyEvent(this.component, KeyEvent.KEY_CODE_ENTER, 1, 0));
 }

 @Test
 public void testKeyTyped() {
 SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(this.component);
 helper.attach();
 helper.keyTyped(new KeyEvent(this.component, KeyEvent.KEY_CODE_A, 1, 0));
 }

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }
}