package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {

 Menu menu = new Menu();
 Menu menuWithTitle = new Menu("Title", null, null, null, false, null);
 Menu menuWithTitlePicAndAudio = new Menu("Title", "pic.jpg", "audio.mp3", "text.txt", false, null);
 Menu menuWithPal = new Menu("Title", null, null, null, true, null);

 @Test
 public void testMenuInstance() {
 Menu menuInstance = new Menu();
 assertNotNull(menuInstance);
 }

 @Test
 public void testMenuWithTitleInstance() {
 Menu menuInstanceWithTitle = new Menu("Title", null, null, null, false, null);
 assertEquals("Title", menuInstanceWithTitle.strTitle);
 }

 @Test
 public void testMenuWithTitlePicAndAudioInstance() {
 Menu menuInstanceWithTitlePicAndAudio = new Menu("Title", "pic.jpg", "audio.mp3", "text.txt", false, null);
 assertEquals("Title", menuInstanceWithTitlePicAndAudio.strTitle);
 assertEquals("pic.jpg", menuInstanceWithTitlePicAndAudio.strPicPath);
 assertEquals("audio.mp3", menuInstanceWithTitlePicAndAudio.strAudioPath);
 assertEquals("text.txt", menuInstanceWithTitlePicAndAudio.strTextFilePath);
 }

 @Test
 public void testMenuWithPalInstance() {
 Menu menuInstanceWithPal = new Menu("Title", null, null, null, true, null);
 assertTrue(menuInstanceWithPal.pal_menu);
 }

 @Test
 public void testInitFalse() {
 Menu menuInstance = new Menu();
 assertFalse(menuInstance.init());
 }

 @Test
 public void testInitTrue() {
 Menu menuInstanceWithTitle = new Menu("Title", null, null, null, false, null);
 assertTrue(menuInstanceWithTitle.init());
 }

 @Test
 public void testCreateXMLFalse() {
 Menu menuInstance = new Menu();
 assertFalse(menuInstance.createXML());
 }

 @Test
 public void testCreateXMLTrue() {
 Menu menuInstanceWithTitle = new Menu("Title", null, null, null, false, null);
 assertTrue(menuInstanceWithTitle.createXML());
 }

}