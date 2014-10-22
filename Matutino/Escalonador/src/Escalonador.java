
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

public class Escalonador {
	private static BufferedWriter logfile;
	private static LinkedList <Processo> listaProcessosProntos;
	private static LinkedList <Processo> listaProcessosBloqueados;
	private static  int quantum;
	
	/**
	 * Funcao que faz todas as chamadas de inicializacao do escalonador.
	 */
	public static void escalonarProcessos() {
		listaProcessosProntos = LeitorArquivos.carregarProcessos();
		quantum = LeitorArquivos.carregarQuantum(); 
		logfile = LeitorArquivos.inicializaLogFile("log" + quantum);
		ListIterator<Processo> listIterator = listaProcessosProntos.listIterator();
		while(listIterator.hasNext()){
			Processo p = listIterator.next();
			//Exemplo de como add log na file
			try {
				logfile.write(p.getNomePrograma()+'\n');
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LeitorArquivos.closeLogFile(logfile);
		
		escalonar();
		
	}

	private static void escalonar() {
		Processo processoCorrente = null;
		int contadorInstrucoesProcessoCorrente = 0;
		while(listaProcessosProntos.size() > 0 || listaProcessosBloqueados.size() > 0) {
			if (processoCorrente == null) {
				processoCorrente = listaProcessosProntos.removeFirst();
				contadorInstrucoesProcessoCorrente = 0;
			}
			
			while (!processoCorrente.getBloqueado() && contadorInstrucoesProcessoCorrente <= quantum) {
				
			}
			
		}
		
	}
	
	

}
