
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
			if (processoCorrente == null || listaProcessosProntos.size() > 0) {
				processoCorrente = listaProcessosProntos.removeFirst();
				contadorInstrucoesProcessoCorrente = 0;
			} else if (listaProcessosBloqueados.size() > 0) {
				passarBloqueadoParaPronto(null);
			}
			
			while (!processoCorrente.getBloqueado() && contadorInstrucoesProcessoCorrente <= quantum) {
				String instrucao = processoCorrente.getProximaInstrucao();
			}
			
		}
		
	}

	/**
	 * Como a lista de bloqueados tambem acaba funcionando como uma fila, 
	 * basta pegar o primeiro para passar para a lista de prontos em qualquer
	 * tipo de chamada para o mesmo.
	 */
	private static void passarBloqueadoParaPronto(Processo p) {
		if (p == null) 
			p = listaProcessosBloqueados.removeFirst();
			
		p.setAnteriormenteBloqueado(1);
		p.setBloqueado(false);
		p.setTempoEsperaBloqueio(2);
		listaProcessosProntos.add(p);
	}
	
	/**
	 * Cada vez que um processo e executado, a variavel espera de bloqueio 
	 * de todos os processos bloqueados deve ser atualizada, e se ja estiverem
	 * o tempo necessario devem passar para a lista de pronto.
	 */
	private static void atualizarListaBloqueados() {
		LinkedList<Processo> listaAuxiliar = listaProcessosBloqueados;
		ListIterator<Processo> it = listaAuxiliar.listIterator();
		if (it.hasNext()) {
			int index = it.nextIndex();
			Processo p = listaProcessosBloqueados.get(index);
			p.setTempoEsperaBloqueio(p.getTempoEsperaBloqueio() - 1);
			if (p.getTempoEsperaBloqueio() == 0) {
				listaProcessosBloqueados.remove(index);
				passarBloqueadoParaPronto(p);
			}
			it.next();
		}
	}
	
	

}
