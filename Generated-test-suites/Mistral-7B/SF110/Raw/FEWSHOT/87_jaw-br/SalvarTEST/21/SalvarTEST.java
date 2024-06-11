package jaw.entrada;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import jaw.gui.*;
import jaw.metamodelo.*;
import jaw.salvar.*;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 form = mock(ProcessarEntidades.class);
 salvar = new Salvar(form, 20);
 }

 @Test
 public void testSalvarJawFile() throws IOException {
 final List<Entidade> entidades = new ArrayList<>();
 when(form.getEntidades()).thenReturn(entidades);

 final JFileChooser escolhedor = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(escolhedor);

 final File arquivo = new File("path/to/file.jaw");
 when(escolhedor.getSelectedFile()).thenReturn(arquivo);

 salvar.salvar(entidades, arquivo);

 verify(form, times(1)).getEntidades();
 verify(form, times(1)).getFileChooser();
 verify(escolhedor, times(1)).showSaveDialog(eq(form));
 verify(escolhedor, times(1)).getSelectedFile();

 final Serializacao serializacao = new Serializacao();
 serializacao.salvar(entidades, arquivo);

 verify(form, times(1)).getNomeArquivoSalvo();
 verify(form, times(1)).setNomeArquivoSalvo(eq(arquivo.getAbsolutePath() + File.separator + "file.jaw"));
 }

 @Test
 public void testSalvarXmlFile() throws IOException {
 final List<Entidade> entidades = new ArrayList<>();
 when(form.getEntidades()).thenReturn(entidades);

 final JFileChooser escolhedor = mock(JFileChooser.class);
 when(form.getFileChooser()).thenReturn(escolhedor);

 final File arquivo = new File("path/to/file.jaw.xml");
 when(escolhedor.getSelectedFile()).thenReturn(arquivo);

 salvar.salvar(entidades, arquivo);

 verify(form, times(1)).getEntidades();
 verify(form, times(1)).getFileChooser();
 verify(escolhedor, times(1)).showSaveDialog(eq(form));
 verify(escolhedor, times(1)).getSelectedFile();

 final SerializacaoXML serializacaoXML = new SerializacaoXML();
 String xml = serializacaoXML.entidades2XML(entidades);
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final PrintWriter writer = new PrintWriter(outputStream);
 writer.print(xml);
 writer.flush();

 final FileWriter fileWriter = mock(FileWriter.class);
 when(fileWriter.write(anyString())).thenAnswer(i -> outputStream.toByteArray());
 when(fileWriter.flush()).thenAnswer(i -> {});
 when(fileWriter.close()).thenAnswer(i -> {});

 salvar.salvar(entidades, arquivo);

 verify(form, times(1)).getNomeArquivoSalvo();
 verify(form, times(1)).setNomeArquivoSalvo(eq(arquivo.getAbsolutePath() + File.separator + "file.jaw.xml"));
 verify(form, times(1)).getFileChooser();
 verify(escolhedor, times(1)).showSaveDialog(eq(form));
 verify(escolhedor, times(1)).getSelectedFile();
 verify(form, times(1)).getFileWriter();
 verify(fileWriter, times(1)).write(anyString());
 verify(fileWriter, times(1)).flush();
 verify(fileWriter, times(1)).close();
 }

 @Test
 public void testDoNotSaveFileWhenFileChooserReturnsNull() {
 salvar.salvar(new ArrayList<>(), null);

 verify(form, times(1)).getEntidades();
 verify(form, times(1)).getFileChooser();
 verify(form, times(0)).setNomeArquivoSalvo(anyString());
 }
}