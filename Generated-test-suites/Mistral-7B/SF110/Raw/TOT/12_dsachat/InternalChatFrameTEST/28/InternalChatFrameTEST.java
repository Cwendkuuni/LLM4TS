package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private MainFrame parent;
 private InternalChatFrame chatFrame;

 @Before
 public void setUp() {
 parent = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parent);
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello there";
 chatFrame.addText(line);
 assertEquals(line, chatFrame.getChatlog().substring(0, chatFrame.getChatlog().lastIndexOf("\n") + line.length()));
 }

 @Test
 public void testAddTextWithSpecialCharacter() {
 String line = "*User1*: Hello there";
 chatFrame.addText(line);
 assertEquals(line, chatFrame.getChatlog().substring(0, chatFrame.getChatlog().lastIndexOf("\n") + line.length()));
 }

 @Test
 public void testAddTextWithMultipleUsers() {
 String line1 = "User1: Hello User2";
 String line2 = "User2: Hello User1";
 chatFrame.addText(line1);
 chatFrame.addText(line2);
 assertEquals(line1 + "\n" + line2, chatFrame.getChatlog().substring(0, chatFrame.getChatlog().lastIndexOf("\n") + (line1.length() + line2.length())));
 }

 @Test
 public void testAddTextWithError() {
 String line = "Error: Invalid input";
 chatFrame.addText(line);
 assertFalse(line.matches(".*:.*"));
 assertEquals("Error: Invalid input\n", chatFrame.getChatlog().substring(0, chatFrame.getChatlog().lastIndexOf("\n") + line.length()));
 }

 @Test
 public void testMouseClickOnList() {
 MouseEvent mouseEvent = mock(MouseEvent.class);
 chatFrame.list.dispatchEvent(mouseEvent);
 verify(chatFrame).maybeShowPopup(mouseEvent);
 }

 @Test
 public void testMouseClickOnText() {
 MouseEvent mouseEvent = mock(MouseEvent.class);
 chatFrame.showTxt.dispatchEvent(mouseEvent);
 verify(chatFrame, never()).maybeShowPopup(mouseEvent);
 }
}