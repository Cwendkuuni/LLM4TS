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
 private SimpleKeyListener simpleKeyListener;

 @Before
 public void setUp() {
 simpleKeyListener = mock(SimpleKeyListener.class);
 simpleKeyListenerHelper = new SimpleKeyListenerHelper(new Component() {});
 }

 @Test
 public void testSimpleKeyListenerHelperConstruction() {
 // Test that the SimpleKeyListenerHelper is instantiated correctly
 assertNull(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testComponentAddKeyListener() {
 // Test that the component has a key listener attached
 simpleKeyListenerHelper.addListener(simpleKeyListener);
 verify(new Component() {}).addKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testComponentRemoveKeyListener() {
 // Test that the component has the key listener removed when detach is called
 simpleKeyListenerHelper.detach();
 verify(new Component() {}).removeKeyListener(simpleKeyListenerHelper);
 }

 @Test
 public void testEnterKeyEvent() {
 // Given
 simpleKeyListenerHelper.addListener(simpleKeyListener);

 // When
 simpleKeyListenerHelper.fire(KeyEvent.VK_ENTER);

 // Then
 verify(simpleKeyListener).enter(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testInsertKeyEvent() {
 // Given
 simpleKeyListenerHelper.addListener(simpleKeyListener);

 // When
 simpleKeyListenerHelper.fire(KeyEvent.VK_INSERT);

 // Then
 verify(simpleKeyListener).insert(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testDeleteKeyEvent() {
 // Given
 simpleKeyListenerHelper.addListener(simpleKeyListener);

 // When
 simpleKeyListenerHelper.fire(KeyEvent.VK_DELETE);

 // Then
 verify(simpleKeyListener).delete(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testTabKeyEvent() {
 // Given
 simpleKeyListenerHelper.addListener(simpleKeyListener);

 // When
 simpleKeyListenerHelper.fire(KeyEvent.VK_TAB);

 // Then
 verify(simpleKeyListener).tab(simpleKeyListenerHelper.getComponent());
 }

 @Test
 public void testKeyTypedEvent() {
 // Given
 simpleKeyListenerHelper.addListener(simpleKeyListener);

 // When
 simpleKeyListenerHelper.keyTyped(new KeyEvent(new Component() {}, KeyEvent.KEY_TYPED, 0, 'a'));

 // Then
 // KeyTyped event is not handled in the SimpleKeyListenerHelper, it is an unnecessary method
 }
}