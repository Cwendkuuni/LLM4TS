package jaw.entrada.test;

import static org.junit.Assert.*;
import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;

public class AbrirTest {

 private ProcessarEntidades form;
 private FileDialog dialog;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 dialog = new FileDialog(new Frame(), "Open Dialog", FileDialog.OPEN);
 }

 @After
 public void tearDown() {
 form = null;
 dialog = null;
 }

 @Test
 public void testAbrirXML() {
 String xmlFilePath = "test/resources/jaw.xml";
 Abrir abrir = new Abrir(form, xmlFilePath);
 assertTrue(abrir.xml.length() > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithWrongFileType() {
 String wrongFilePath = "test/resources/test.txt";
 Abrir abrir = null;
 try {
 abrir = new Abrir(form, wrongFilePath);
 fail("IllegalArgumentException was expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirXMLWithWrongFileLocation() {
 String wrongFilePath = "wrong/location/jaw.xml";
 Abrir abrir = null;
 try {
 abrir = new Abrir(form, wrongFilePath);
 fail("FileNotFoundException was expected");
 } catch (FileNotFoundException e) {
 // expected
 }
 }

 @Test
 public void testAbrirSerializado() {
 String serializadoFilePath = "test/resources/serializado.jaw";
 Abrir abrir = new Abrir(form, SerializationType.SERIALIZED_FILE.getValue());
 dialog.setFiles("*.jaw");
 dialog.show();
 File selectedFile = dialog.getFile();
 if (selectedFile != null) {
 selectedFile = new File(selectedFile.getAbsolutePath() + File.separator + serializadoFilePath);
 }
 if (selectedFile != null) {
 try {
 abrir.abrirSerializado(selectedFile.getAbsolutePath());
 assertTrue(form.getEntidades() != null);
 } catch (IOException | ClassNotFoundException e) {
 fail("IOException or ClassNotFoundException was expected");
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithWrongFileType() {
 String wrongFilePath = "test/resources/test.txt";
 Abrir abrir = null;
 try {
 abrir = new Abrir(form, SerializationType.SERIALIZED_FILE.getValue());
 dialog.setFiles("*.txt");
 dialog.show();
 File selectedFile = dialog.getFile();
 if (selectedFile != null) {
 try {
 abrir.abrirSerializado(selectedFile.getAbsolutePath());
 fail("IllegalArgumentException was expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 } catch (IOException e) {
 // not expected
 } finally {
 dialog.dispose();
 }
 }

 @Test
 public void testAbrirXMLWithJFileChooser() {
 String xmlFilePath = "test/resources/jaw.xml";
 Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
 dialog.setFiles("*.xml");
 dialog.show();
 File selectedFile = dialog.getFile();
 if (selectedFile != null) {
 try {
 abrir.abrirXML(selectedFile.getAbsolutePath());
 assertTrue(form.getEntidades() != null);
 } catch (IOException e) {
 fail("IOException was expected");
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithWrongFileFilter() {
 String xmlFilePath = "test/resources/jaw.xml";
 Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
 dialog.setFiles("*.jaw");
 dialog.show();
 File selectedFile = dialog.getFile();
 if (selectedFile != null) {
 try {
 abrir.abrirXML(selectedFile.getAbsolutePath());
 fail("IllegalArgumentException was expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 }

 @Test
 public void testAbrirSerializadoWithJFileChooser() {
 String serializadoFilePath = "test/resources/serializado.jaw";
 Abrir abrir = new Abrir(form, Abrir.ABRIR);
 dialog.setFiles("*.jaw");
 dialog.show();
 File selectedFile = dialog.getFile();
 if (selectedFile != null) {
 try {
 abrir.abrirSerializado(selectedFile.getAbsolutePath());
 assertTrue(form.getEntidades() != null);
 } catch (IOException | ClassNotFoundException e) {
 fail("IOException or ClassNotFoundException was expected");
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithWrongFileFilter() {
 String xmlFilePath = "test/resources/jaw.xml";
 Abrir abrir = new Abrir(form, Abrir.ABRIR);
 dialog.setFiles("*.xml");
 dialog.show();
 File selectedFile = dialog.getFile();
 if (selectedFile != null) {
 try {
 abrir.abrirSerializado(selectedFile.getAbsolutePath());
 fail("IllegalArgumentException was expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
 }
}