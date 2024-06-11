package com.lts.event;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import org.junit.Test;
import org.mockito.Mockito;

public class SimpleKeyListenerHelperTest {

 Component mockComponent;
 SimpleKeyListener helper;
 SimpleKeyListener mockListener;

 @Before
 public void setUp() {
 mockComponent = Mockito.mock(Component.class);
 mockListener = Mockito.mock(SimpleKeyListener.class);
 helper = new SimpleKeyListenerHelper(mockComponent);
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 assertNotNull(helper);
 verify(mockComponent).addKeyListener(helper);
 }

 @Test
 public void testDetach() {
 helper.detach();
 assertNull(helper.component);
 verify(mockComponent).removeKeyListener(helper);
 }

 @Test
 public void testNotifyListenerEnter() {
 when(mockComponent.isFocused()).thenReturn(true);
 helper.notifyListener(mockListener, 10, null);
 verify(mockListener).enter(mockComponent);
 }

 @Test
 public void testNotifyListenerInsert() {
 when(mockComponent.isFocused()).thenReturn(true);
 helper.notifyListener(mockListener, 155, null);
 verify(mockListener).insert(mockComponent);
 }

 @Test
 public void testNotifyListenerDelete() {
 when(mockComponent.isFocused()).thenReturn(true);
 helper.notifyListener(mockListener, 127, null);
 verify(mockListener).delete(mockComponent);
 }

 @Test
 public void testNotifyListenerTab() {
 when(mockComponent.isFocused()).thenReturn(true);
 helper.notifyListener(mockListener, 9, null);
 verify(mockListener).tab(mockComponent);
 }

 @Test
 public void testKeyPressedEnter() {
 helper.keyPressed(new KeyEvent(mockComponent, 10, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, "Enter"));
 verify(mockListener).enter(mockComponent);
 }

 @Test
 public void testKeyPressedTab() {
 helper.keyPressed(new KeyEvent(mockComponent, 9, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, "Tab"));
 verify(mockListener).tab(mockComponent);
 }

 @Test
 public void testKeyPressedDelete() {
 helper.keyPressed(new KeyEvent(mockComponent, 127, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, "Delete"));
 verify(mockListener).delete(mockComponent);
 }

 @Test
 public void testKeyPressedInsert() {
 helper.keyPressed(new KeyEvent(mockComponent, 155, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, "Insert"));
 verify(mockListener).insert(mockComponent);
 }
}