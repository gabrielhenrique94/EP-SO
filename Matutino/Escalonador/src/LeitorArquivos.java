import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 
 * Classe destinada a ler arquivos e criar Processos
 *
 */
public class LeitorArquivos {
	
	/**
	 * Le o arquivo que contem as instrucoes destinadas a um Processo, e devolve-as
	 * @param nomeProcesso
	 * @return
	 */
	public static ArrayList<String> lerProcesso (String nomeProcesso){
		
		ArrayList<String> processo = new ArrayList<String>();
		 
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
		for (final File fileEntry : folder.listFiles()) {

			if (fileEntry.isFile()) {
				temp = fileEntry.getName();
				if (temp != "quantum.txt") {  
					if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("txt")) {
						ArrayList<String> instrucoes = lerProcesso(folder.getAbsolutePath()+ "\\" + fileEntry.getName());
						aux = null;
						if (instrucoes != null) {
							aux = new Processo(instrucoes);
							processos.add(aux);
						}
					}
				}
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

			return quantum;
 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return -1;
	}
}

