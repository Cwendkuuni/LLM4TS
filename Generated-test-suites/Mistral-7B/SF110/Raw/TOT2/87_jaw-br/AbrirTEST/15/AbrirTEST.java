package jaw.entrada.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import org.junit.*;
import org.mockito.*;

import jaw.entrada.*;
import jaw.gui.*;
import jaw.*;
import static jaw.entrada.test.TestUtil.*;

public class AbrirTest {
   @Test
   public void testAbrirStringConstructor() {
      // Test method 1
      AbrirStringConstructorTest.testAbrirStringConstructor();
   }

   @Test
   public void testInvalidArquivoType() {
      // Test method 1
      AbrirStringConstructorTest.testInvalidArquivoType();
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNullArquivo() {
      // Test method 1
      AbrirStringConstructorTest.testNullArquivo();
   }

   @Test
   public void testAbrirXMLFile() {
      // Test method 2
      AbrirIntConstructorTest.testAbrirXMLFile();
   }

   @Test
   public void testInvalidFileType() {
      // Test method 2
      AbrirIntConstructorTest.testInvalidFileType();
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNullFile() {
      // Test method 2
      AbrirIntConstructorTest.testNullFile();
   }

   @Test
   public void testValidJawFile() {
      // Test method 3
      AbrirSerializadoTest.testValidJawFile();
   }

   @Test
   public void testInvalidJawFile() {
      // Test method 3
      AbrirSerializadoTest.testInvalidJawFile();
   }

   @Test
   public void testValidXMLFile() {
      // Test method 4
      AbrirXMLTest.testValidXMLFile();
   }

   @Test
   public void testInvalidXMLFile() {
      // Test method 4
      AbrirXMLTest.testInvalidXMLFile();
   }
}