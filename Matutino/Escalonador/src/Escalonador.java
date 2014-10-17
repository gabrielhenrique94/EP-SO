
import java.util.List;

public class Escalonador {
	private static List <Processo> listaProcessosProntos;
	private List <Processo> listaProcessosBloqueados;
	private static int quantum;
	
	/**
	 * Funcao que faz todas as chamadas de inicializacao do escalonador.
	 */
	public static void escalonarProcessos() {
		listaProcessosProntos = LeitorArquivos.carregarProcessos();
		quantum = LeitorArquivos.carregarQuantum(); 
		
	}
	
	

}
