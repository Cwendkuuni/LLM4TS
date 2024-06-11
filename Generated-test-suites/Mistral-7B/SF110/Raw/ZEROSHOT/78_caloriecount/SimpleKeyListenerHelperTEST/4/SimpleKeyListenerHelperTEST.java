package com.lts.event;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper simpleKeyListenerHelper;
 private Component component;
 private SimpleKeyListener mockSimpleKeyListener;

 @Before
 public void setUp() {
 component = Mockito.mock(Component.class);
 mockSimpleKeyListener = Mockito.mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertNotNull(simpleKeyListenerHelper);
 }

 @Test
 public void testComponentAddKeyListener() {
 assertNotNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testDetach() {
 simpleKeyListenerHelper.detach();
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testNotifyListenerEnter() {
 Mockito.when(component.isFocused()).thenReturn(true);
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
 Mockito.verify(mockSimpleKeyListener).enter(component);
 }

 @Test
 public void testNotifyListenerInsert() {
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
 Mockito.verify(mockSimpleKeyListener).insert(component);
 }

 @Test
 public void testNotifyListenerDelete() {
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
 Mockito.verify(mockSimpleKeyListener).delete(component);
 }

 @Test
 public void testNotifyListenerTab() {
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 Mockito.verify(mockSimpleKeyListener).tab(component);
 }

 @Test
 public void testKeyPressedEnter() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_CODE_ENTER, 1, 0));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_ENTER);
 Mockito.verify(mockSimpleKeyListener).enter(component);
 }

 @Test
 public void testKeyPressedTab() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.VK_TAB, 1, 0));
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);
 Mockito.verify(mockSimpleKeyListener).tab(component);
 }

 @Test
 public void testKeyPressedBackSpace() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_CODE_BACK_SPACE, 1, 0));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_BACK_SPACE);
 Mockito.verify(mockSimpleKeyListener).delete(component);
 }

 @Test
 public void testKeyPressedDelete() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, KeyEvent.KEY_CODE_DELETE, 1, 0));
 simpleKeyListenerHelper.fire(KeyEvent.KEY_CODE_DELETE);
 Mockito.verify(mockSimpleKeyListener).delete(component);
 }

 @Test
 public void testKeyPressedOtherKey() {
 simpleKeyListenerHelper.keyPressed(new KeyEvent(component, 1, 1, 0));
 simpleKeyListenerHelper.fire(1);
 Mockito.verifyNoMoreInteractions(mockSimpleKeyListener);
 }
}