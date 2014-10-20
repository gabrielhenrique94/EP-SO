import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * 
 * Classe destinada a ler arquivos e criar Processos
 *
 */
public class LeitorArquivos {
	
	/**
	 * Le o arquivo que contem as instrucoes destinadas a um Processo, e devolve-as
	 * @param nomeProcesso
	 * @param posicao
	 * @return
	 */
	public static LinkedList<String> lerProcesso (String nomeProcesso) {
		LinkedList<String> processo = new LinkedList<String>();
		 
		try {
 
			String instrucao;
 
			BufferedReader arquivoProcesso = new BufferedReader(new FileReader(nomeProcesso));
 
			while ((instrucao = arquivoProcesso.readLine()) != null) {
				processo.add(instrucao);
			}
			
			arquivoProcesso.close();
			return processo;
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return null;
		
	}
	
	/**
	 * Carrega todos os arquivos de processos que se encontram na pasta 'src/processos_entrada'.
	 * Devolve uma lista ligada com os processos ja criados.
	 * @return
	 */
	public static LinkedList<Processo> carregarProcessos() {
		LinkedList<Processo> processos = new LinkedList<Processo>();
		Processo aux;
		File folder = new File("src/processos_entrada/");
		String temp = "";
		
		LinkedList<String> files = new LinkedList<String>();
		
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isFile()) {
				temp = fileEntry.getName();
				if (!temp.equals("quantum.txt"))
					files.add(temp);
			}
		}
		
		Collections.sort(files);
		
		ListIterator<String> listIterator = files.listIterator();
		
		while (listIterator.hasNext()) {
            LinkedList<String> instrucoes = lerProcesso("src/processos_entrada/" + listIterator.next());
			aux = null;
			if (instrucoes != null) {
				aux = new Processo(instrucoes.removeFirst(), instrucoes);
				processos.add(aux);
			}
        }

		return processos;
	}
	
	/**
	 * Carrega o valor inteiro do quantum que e disponibilizado em um arquivo dentro da pasta 'src/processos_entrada' com o nome de 
	 * 'quantum.txt'. Retorna esse valor.
	 * @return
	 */
	public static int carregarQuantum() {
		try {
			 
			int quantum;
 
			BufferedReader arquivoProcesso = new BufferedReader(new FileReader("src/processos_entrada/quantum.txt"));
 
			quantum = Integer.parseInt(arquivoProcesso.readLine());

			arquivoProcesso.close();
			
			return quantum;
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return -1;
	}
	
	public static Logger inicializaLogFile(String name) {
		Logger logger = Logger.getLogger("name");  
	    FileHandler fh;  

	    try { 
	    	File f = new File("src/saida/" + name + ".txt");
	        fh = new FileHandler("src/saida/" + name + ".txt");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
 
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	    
		return logger;  
	}
}

