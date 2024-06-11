package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import jaw.gui.ProcessarEntidades;
import jaw.util.Serializacao;
import jaw.util.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private JFileChooser fileChooser;
 private File selectedFile;

 @Before
 public void setup() {
 form = Mockito.mock(ProcessarEntidades.class);
 fileChooser = Mockito.spy(new JFileChooser());
 }

 @Test
 public void testAbrirXmlWithValidFileName() {
 String xmlFileName = "test.jaw.xml";
 new Abrir(form, xmlFileName);

 verify(form).setNomeArquivoSalvo(xmlFileName);
 try {
 FileReader fileReader = new FileReader(xmlFileName);
 SerializacaoXML xmlParser = new SerializacaoXML();
 assertTrue(xmlParser.xml2Entidades(fileReader).size() > 0);
 } catch (IOException e) {
 fail("Failed to read file: " + xmlFileName);
 }
 }

 @Test
 public void testAbrirSerializadoWithValidFileName() {
 String serializadoFileName = "test.jaw";
 new Abrir(form, 20, new File(serializadoFileName));

 verify(form).setNomeArquivoSalvo(serializadoFileName);
 try {
 form.getEntidades().size();
 } catch (Exception e) {
 fail("Failed to open serialized file: " + serializadoFileName);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirInvalidFileNameType() {
 File invalidFile = new File("test.txt");
 new Abrir(form, 20, invalidFile);
 }

 @Test(expected = IOException.class)
 public void testAbrirXmlWithInvalidFileName() {
 String invalidXmlFileName = "test.xml";
 when(fileChooser.showOpenDialog(anyObject())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File(invalidXmlFileName));
 new Abrir(form, 10, fileChooser);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidFileName() {
 File invalidSerializadoFile = new File("test.txt");
 new Abrir(form, 20, invalidSerializadoFile);
 }

 @Test
 public void testAbrirNullFile() {
 when(fileChooser.showOpenDialog(anyObject())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(null);
 new Abrir(form, 20, fileChooser);
 }
}