package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

 private SimpleKeyListenerHelper listenerHelper;
 private Component mockComponent;
 private SimpleKeyListener mockListener;

 @Before
 public void setup() {
 mockComponent = Mockito.mock(Component.class);
 mockListener = Mockito.mock(SimpleKeyListener.class);
 listenerHelper = new SimpleKeyKeyListenerHelper(mockComponent);
 }

 @Test
 public void testAddKeyListener() {
 mockComponent.addKeyListener(listenerHelper);
 verify(mockComponent).addKeyListener(listenerHelper);
 }

 @Test
 public void testDetach() {
 listenerHelper.detach();
 assertNull(listenerHelper.component);
 }

 @Test
 public void testNotifyListenerEnter() {
 final int keyCodeEnter = KeyEvent.VK_ENTER;
 when(mockListener.enter(any(Component.class))).thenReturn(true);
 listenerHelper.fire(keyCodeEnter);
 verify(mockListener).enter(mockComponent);
 }

 @Test
 public void testNotifyListenerInsert() {
 final int keyCodeInsert = KeyEvent.VK_INSERT;
 when(mockListener.insert(any(Component.class))).thenReturn(true);
 listenerHelper.fire(keyCodeInsert);
 verify(mockListener).insert(mockComponent);
 }

 @Test
 public void testNotifyListenerDelete() {
 final int keyCodeDelete = KeyEvent.VK_DELETE;
 when(mockListener.delete(any(Component.class))).thenReturn(true);
 listenerHelper.fire(keyCodeDelete);
 verify(mockListener).delete(mockComponent);
 }

 @Test
 public void testNotifyListenerTab() {
 final int keyCodeTab = KeyEvent.VK_TAB;
 when(mockListener.tab(any(Component.class))).thenReturn(true);
 listenerHelper.fire(keyCodeTab);
 verify(mockListener).tab(mockComponent);
 }

 @Test
 public void testKeyPressedEnter() {
 when(mockComponent.isFocusOwnerForKeyEvent(any(KeyEvent.class))).thenReturn(true);
 listenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.KEY_LOCATION_UNDEFINED, false, false));
 verify(mockListener).enter(mockComponent);
 }

 @Test
 public void testKeyPressedTab() {
 when(mockComponent.isFocusOwnerForKeyEvent(any(KeyEvent.class))).thenReturn(true);
 listenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.KEY_LOCATION_UNDEFINED, false, false));
 verify(mockListener).tab(mockComponent);
 }

 @Test
 public void testKeyPressedBackspace() {
 when(mockComponent.isFocusOwnerForKeyEvent(any(KeyEvent.class))).thenReturn(true);
 listenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE, KeyEvent.KEY_LOCATION_UNDEFINED, false, false));
 verifyZeroInteractions(mockListener);
 }

 @Test
 public void testKeyPressedOtherKey() {
 when(mockComponent.isFocusOwnerForKeyEvent(any(KeyEvent.class))).thenReturn(true);
 listenerHelper.keyPressed(new KeyEvent(mockComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A, KeyEvent.KEY_LOCATION_UNDEFINED, false, false));
 verifyZeroInteractions(mockListener);
 }

 @Test
 public void testKeyReleased() {
 listenerHelper.keyReleased(new KeyEvent(mockComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.KEY_LOCATION_UNDEFINED, false, false));
 verifyZeroInteractions(mockListener);
 }

 @Test
 public void testKeyTyped() {
 listenerHelper.keyTyped(new KeyEvent(mockComponent, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, 'A', KeyEvent.KEY_LOCATION_UNDEFINED, false, false));
 verifyZeroInteractions(mockListener);
 }
}