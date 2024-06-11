import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import jaw.gui.ProcessarEntidades;
import static org.mockito.Mockito.*;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;

 @Test
 public void testSalvarComEntidadesValidasEXml() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 form = mock(ProcessarEntidades.class);
 when(form.getEntidades()).thenReturn(entidades);
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, "teste.jaw.xml");
 Assert.assertTrue(new File("teste.jaw.xml").exists());
 }

 @Test
 public void testSalvarComEntidadesValidasEECampoNaoInformado() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 form = mock(ProcessarEntidades.class);
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, "teste.jaw.xml");
 Assert.assertFalse(new File("teste.jaw.xml").exists());
 }

 @Test(expected = IOException.class)
 public void testSalvarComEntidadesValidasEEformatoInvalido() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 form = mock(ProcessarEntidades.class);
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("teste.txt");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, "teste.jaw.xml");
 }

 @Test
 public void testSalvarComEntidadesValidasEEJaw() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 form = mock(ProcessarEntidades.class);
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw");
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.salvar(entidades, "teste.jaw");
 Assert.assertTrue(new File("teste.jaw").exists());
 }

 @Test(expected = IOException.class)
 public void testSalvarComEntidadesValidasEEArquivoNaoEscritavel() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 Formatter formatter = mock(Formatter.class);
 when(formatter.format(anyString())).thenThrow(IOException.class);
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, "teste.jaw");
 }

 @Test
 public void testSalvarComEntidadesValidasEEArquivoExistente() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 File arquivoExistente = new File("teste.jaw");
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw");
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.salvar(entidades, "teste.jaw");
 Assert.assertTrue(arquivoExistente.exists());
 }

 @Test
 public void testSalvarComNovasEntidadesEEArquivoXmlBemFormatado() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 form = mock(ProcessarEntidades.class);
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw.xml");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, "teste.jaw.xml");
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>teste1</nome>\n<descricao>teste1</descricao>\n</entidade>\n<entidade>\n<nome>teste2</nome>\n<descricao>teste2</descricao>\n</entidade>\n</entidades>";
 File arquivo = new File("teste.jaw.xml");
 Assert.assertEquals(xml, new String(readFileContent(arquivo)));
 }

 @Test(expected = IOException.class)
 public void testSalvarComNovasEntidadesEEArquivoXmlNaoEscritavel() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 Formatter formatter = mock(Formatter.class);
 when(formatter.format(anyString())).thenThrow(IOException.class);
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw.xml");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, "teste.jaw.xml");
 }

 @Test
 public void testSalvarComNovasEntidadesEEArquivoXmlValidoMasNaoSalva() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>teste1</nome>\n<descricao>teste1</descricao>\n</entidade>\n<entidade>\n<nome>teste2</nome>\n<descricao>teste2</descricao>\n</entidade>\n</entidades>";
 File arquivo = new File("teste.jaw.xml");
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw.xml");
 when(new FileWriter(arquivo).write(xml)).thenReturn(false);
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, "teste.jaw.xml");
 Assert.assertFalse(arquivo.exists());
 }

 @Test
 public void testSalvarComNovasEntidadesEEArquivoXmlInvalido() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 form = mock(ProcessarEntidades.class);
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw.xml");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, "teste.jaw.xml");
 Assert.assertFalse(new File("teste.jaw.xml").exists());
 }

 @Test
 public void testSalvarComNovasEntidadesEEArquivoXmlNaoFormatado() {
 List<Entidade> entidades = new ArrayList<>();
 Entidade entidade1 = new Entidade("teste1", "teste1");
 Entidade entidade2 = new Entidade("teste2", "teste2");
 entidades.add(entidade1);
 entidades.add(entidade2);
 String xml = "<entidades><entidade><nome>teste1</nome><descricao>teste1</descricao></entidade><entidade><nome>teste2</nome><descricao>teste2</descricao></entidade></entidades>";
 File arquivo = new File("teste.jaw.xml");
 when(form.getEntidades()).thenReturn(entidades);
 when(form.getNomeArquivoSalvo()).thenReturn("teste.jaw.xml");
 when(new FileWriter(arquivo).write(xml)).thenReturn(false);
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar(entidades, "teste.jaw.xml");
 Assert.assertFalse(arquivo.exists());
 }

 private byte[] readFileContent(File file) {
 try {
 byte[] bytes = new byte[(int) file.length()];
 FileInputStream in = new FileInputStream(file);
 in.read(bytes);
 in.close();
 return bytes;
 }
 catch (IOException e) {
 throw new RuntimeException(e);
 }
 }
}