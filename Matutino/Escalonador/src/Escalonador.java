import java.util.LinkedList;


public class Escalonador {
	private static LinkedList<Processo> listaProcessosProntos;
	private LinkedList<Processo> listaProcessosBloqueados;
	private static int quantum;
	
	/**
	 * Funcao que faz todas as chamadas de inicializacao do escalonador.
	 */
	public static void escalonarProcessos() {
		listaProcessosProntos = LeitorArquivos.carregarProcessos();
		quantum = LeitorArquivos.carregarQuantum(); 
		
	}

}
