import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import javax.sound.sampled.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class MP3Test {

 private MP3 mp3;
 private AudioInputStream mockedAudioInputStream;

 @Before
 public void setUp() {
 mockedAudioInputStream = mock(AudioInputStream.class);
 mp3 = new MP3("test.mp3");
 }

 @Test
 public void testRunWithValidFile() throws Exception {
 given(mockedAudioInputStream.getFormat()).willReturn(new AudioFormat(1, 44100, 16, 1, 2, 44100, false));
 given(AudioSystem.getAudioInputStream(eq("test.mp3"))).willReturn(mockedAudioInputStream);

 mp3.run();

 verify(mockedAudioInputStream).close();
 }

 @Test(expected = FileNotFoundException.class)
 public void testRunWithInvalidFile() {
 given(AudioSystem.getAudioInputStream(eq("test.mp3"))).willThrow(new FileNotFoundException("File not found"));

 mp3.run();
 }

 @Test(expected = IOException.class)
 public void testRunWithIOException() {
 given(mockedAudioInputStream.read(any(byte[].class), anyInt(), anyInt()))
 .willThrow(new IOException("Read error"));

 mp3.run();
 }

 @Test(expected = UnsupportedAudioFileException.class)
 public void testRunWithUnsupportedFile() {
 given(mockedAudioInputStream.getFormat()).willReturn(new AudioFormat(1, 44100, 2, 1, 2, 44100, false));

 mp3.run();
 }

 @Test
 public void testThreadStart() throws Exception {
 Thread thread = new Thread(mp3);
 thread.start();
 thread.join();
 verify(mockedAudioInputStream).close();
 }
}