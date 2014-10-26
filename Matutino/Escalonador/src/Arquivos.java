import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * 
 * Classe destinada a ler arquivos e criar Processos
 *
 */
public class Arquivos {
	
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
	public static Map<String, BCP> carregarProcessos(BufferedWriter logfile, Queue<String> fila) {
		Map<String, BCP> processos = new HashMap<String, BCP>();
		BCP aux;
		File folder = new File("src/processos_entrada/");

		for (int i = 1; i < folder.listFiles().length; i++) {
			String numProcesso;
			if (i < 10) 
				numProcesso = "0" + i;
			else 
				numProcesso = Integer.toString(i);
			
			LinkedList<String> instrucoes = lerProcesso("src/processos_entrada/" + numProcesso + ".txt");
			aux = null;
			if (instrucoes != null) {
				aux = new BCP(instrucoes.removeFirst(), instrucoes);
				escreveLog(logfile, "Carregando " + aux.getNomePrograma());
				processos.put(aux.getNomePrograma(), aux);
				fila.offer(aux.getNomePrograma());
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
	
	public static BufferedWriter inicializaLogFile(String name) {

	    try {
	    	File f = new File("src/saida/" + name + ".txt");
	    	BufferedWriter out = new BufferedWriter(new FileWriter("src/saida/" + name + ".txt"));
	    	return out;
 
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	    
		return null;  
	}
	
	public static void closeLogFile(BufferedWriter file) {
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void escreveLog(BufferedWriter log, String info) {
		try {
			log.write(info + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

