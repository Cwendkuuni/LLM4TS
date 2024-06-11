package jaw.entrada.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.FormProcessarEntidades;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

 private Abrir abrir;
 private FormProcessarEntidades form;
 private File arquivoXml;
 private File arquivoSerializado;
 private FileDialog dialog;

 @Before
 public void setUp() {
 form = mock(FormProcessarEntidades.class);
 abrir = new Abrir(form, null);
 arquivoXml = new File("teste.jaw.xml");
 arquivoSerializado = new File("teste.jaw");
 dialog = mock(FileDialog.class);
 }

 @Test
 public void testAbrirXmlEmptyConstructor() {
 assertNull(abrir.getXml());
 }

 @Test
 public void testAbrirXmlWithFileConstructor() {
 when(dialog.getSelectedFile()).thenReturn(arquivoXml);
 abrir = new Abrir(form, dialog);
 assertEquals(arquivoXml.getAbsolutePath(), abrir.getXml());
 }

 @Test(expected = Exception.class)
 public void testAbrirXmlWithInvalidFile() {
 when(dialog.getSelectedFile()).thenReturn(new File("teste.txt"));
 abrir = new Abrir(form, dialog);
 }

 @Test
 public void testAbrirSerializadoEmptyConstructor() {
 assertNull(abrir.getXml());
 }

 @Test
 public void testAbrirSerializadoWithFileConstructor() throws Exception {
 when(dialog.getSelectedFile()).thenReturn(arquivoSerializado);
 when(new Serializacao().abrir(arquivoSerializado.getAbsolutePath())).thenReturn(new Object[0]);
 abrir = new Abrir(form, Abrir.ABRIR, dialog);
 assertNull(abrir.getXml());
 }

 @Test(expected = Exception.class)
 public void testAbrirSerializadoWithInvalidFile() {
 when(dialog.getSelectedFile()).thenReturn(new File("teste.txt"));
 abrir = new Abrir(form, Abrir.ABRIR, dialog);
 }

 @Test
 public void testAbrirSerializadoWithXmlFile() throws Exception {
 when(dialog.getSelectedFile()).thenReturn(arquivoXml);
 abrir = new Abrir(form, Abrir.ABRIR, dialog);
 assertNull(abrir.getXml());
 }

 @Test
 public void testAbrirXmlWithXmlFile() throws Exception {
 when(dialog.getSelectedFile()).thenReturn(arquivoXml);
 when(new SerializacaoXML().xml2Entidades(new FileReader(arquivoXml))).thenReturn(new Object[0]);
 abrir = new Abrir(form, Abrir.ABRIR_XML, dialog);
 assertNull(abrir.getXml());
 }

 @Test(expected = Exception.class)
 public void testAbrirXmlWithInvalidXmlFile() {
 when(dialog.getSelectedFile()).thenReturn(new File("teste.txt"));
 abrir = new Abrir(form, Abrir.ABRIR_XML, dialog);
 }
}