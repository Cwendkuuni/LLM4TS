package com.lts.event;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private SimpleKeyListener mockSimpleKeyListener;
 private Component mockComponent;

 @Before
 public void setUp() {
 mockSimpleKeyListener = Mockito.mock(SimpleKeyListener.class);
 mockComponent = Mockito.mock(Component.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(mockComponent);
 }

 @Test
 public void testSimpleKeyListenerHelperConstructor() {
 assertNotNull(simpleKeyListenerHelper);
 assertSame(mockComponent, simpleKeyListenerHelper.component);
 }

 @Test
 public void testAddKeyListener() {
 mockComponent.addKeyListener(simpleKeyListenerHelper);
 assertTrue(mockComponent.getKeyListeners().contains(simpleKeyListenerHelper));
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.component);
 assertFalse(mockComponent.getKeyListeners().contains(simpleKeyListenerHelper));
 }

 @Test
 public void testNotifyListenerEnter() {
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
 Mockito.verify(mockSimpleKeyListener).enter(mockComponent);
 }

 @Test
 public void testNotifyListenerInsert() {
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_BACK_SPACE);
 Mockito.verify(mockSimpleKeyListener).insert(mockComponent);
 }

 @Test
 public void testNotifyListenerDelete() {
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_BACK_SPACE);
 Mockito.verify(mockSimpleKeyListener).delete(mockComponent);
 }

 @Test
 public void testNotifyListenerTab() {
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 Mockito.verify(mockSimpleKeyListener).tab(mockComponent);
 }

 @Test
 public void testKeyPressedEnter() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEYCODE_ENTER, 1, 1));
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_ENTER);
 Mockito.verify(mockSimpleKeyListener).enter(mockComponent);
 }

 @Test
 public void testKeyPressedTab() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.VK_TAB, 1, 1));
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 Mockito.verify(mockSimpleKeyListener).tab(mockComponent);
 }

 @Test
 public void testKeyPressedDelete() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEYCODE_BACK_SPACE, 1, 1));
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_BACK_SPACE);
 Mockito.verify(mockSimpleKeyListener).delete(mockComponent);
 }

 @Test
 public void testKeyPressedInsert() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEYCODE_INS, 1, 1));
 simpleKeyListenerHelper.fire(KeyEvent.KEYCODE_INS);
 Mockito.verify(mockSimpleKeyListener).insert(mockComponent);
 }
}