import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * Representa um processo
 *
 */
public class Processo {
	
	private String nomePrograma;
	/**
	 * Contador do programa, que e um registrador de uso especifico
	 */
	private int PC;
	
	/**
	 * Registrador de uso geral
	 */
	private int X;
	
	/**
	 * Registrador de uso geral
	 */
	private int Y;
	
	/**
	 * Instrucoes que devem ser executadas para esse processo
	 */
	public LinkedList<String> instrucoes = new LinkedList<String> ();
	
	/**
	 * Avisa se a instrucao anterior entrou para a conta do PC. Pois caso tenha 
	 * tido uma E/S a instrucao nao entra na contagem e o processo e bloqueado
	 * 0 -> entrou para conta, 1 -> nao entrou para a conta. 
	 */
	private int anteriormenteBloqueado; 
	
	/**
	 * Quando um processo e bloqueado ele recebe true para essa variavel
	 */
	private Boolean bloqueado;
	/**
	 * Quando o processo for bloqueado, ele deve esperar outros dois processos executarem para retornar.
	 * A variavel e entao utilizada quando o processo e bloqueado.
	 */
	private int tempoEsperaBloqueio;
	
	/**
	 * Construtor da classe.
	 * Seta os parametros PC com 0, pois ainda não comecou a contagem do programa
	 * e das instrucoes, com um array contendo as instrucoes
	 * @param instrucoes
	 */
	public Processo(String nomePrograma, LinkedList<String> instrucoes) {
		this.nomePrograma = nomePrograma;
		this.PC = 0;
		this.tempoEsperaBloqueio = 2;
		this.anteriormenteBloqueado = 0;
		this.instrucoes = instrucoes;
		this.bloqueado = false;
	}
	
	/**
	 * Get da variavel x
	 * @return
	 */
	public int getX() {
		return X;
	}

	/**
	 * Set da variavel x
	 * @param x
	 */
	public void setX(int x) {
		X = x;
	}

	/**
	 * Get da variavel y
	 * @return
	 */
	public int getY() {
		return Y;
	}

	/**
	 * Set da variavel y
	 * @param y
	 */
	public void setY(int y) {
		Y = y;
	}

	/**
	 * Get da variavel PC
	 * @return
	 */
	public int getPC() {
		return PC;
	}

	/**
	 * Set da variavel PC, feio de forma diferente, nao substituindo um valor 
	 * pelo outro, mas sim somando o valor enviado ao valor ja fixado em PC
	 * @param soma
	 */
	public void setPC(int soma) {
		PC = PC + soma;
	}
	
	/**
	 * Retorna a proxima instrucao do Processo, que e uma String
	 * @return
	 */
	public String getProximaInstrucao() {
		if (instrucoes.size() > 0) 
			return instrucoes.removeFirst();
		
		return "";
	}

	public int getTempoEsperaBloqueio() {
		return tempoEsperaBloqueio;
	}

	public void setTempoEsperaBloqueio(int esperaDoBloqueio) {
		this.tempoEsperaBloqueio = esperaDoBloqueio;
	}

	public int getAnteriormenteBloqueado() {
		return anteriormenteBloqueado;
	}

	public void setAnteriormenteBloqueado(int anteriormenteBloqueado) {
		this.anteriormenteBloqueado = anteriormenteBloqueado;
	}

	public String getNomePrograma() {
		return nomePrograma;
	}

	public void setNomePrograma(String nomePrograma) {
		this.nomePrograma = nomePrograma;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

}
