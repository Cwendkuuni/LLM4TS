package corina.formats.test;

import static org.junit.Assert.*;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

  @Test
  public void testToString() {
    TucsonSimple t = new TucsonSimple();
    assertEquals("format.tucsonsimple", t.toString());
  }

  @Test
  public void testDefaultExtension() {
    TucsonSimple t = new TucsonSimple();
    assertEquals(".RWL", t.getDefaultExtension());
  }

  @Test(expected = IOException.class)
  public void testLoadEmptyReader() throws IOException {
    TucsonSimple t = new TucsonSimple();
    BufferedReader reader = new BufferedReader(new StringReader(""));
    t.load(reader);
  }

  @Test(expected = WrongFiletypeException.class)
  public void testLoadWrongFiletypeException() throws IOException {
    TucsonSimple t = new TucsonSimple();
    BufferedReader reader = new BufferedReader(new StringReader("InvalidData"));
    t.load(reader);
  }

  // Add more test cases for other methods and edge cases

  @After
  public void afterTests() {
    // Perform any cleanup or tear-down actions here
  }

}