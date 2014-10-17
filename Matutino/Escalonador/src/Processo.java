import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Representa um processo
 *
 */
public class Processo {
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
	public List<String> instrucoes = new LinkedList<String> ();
	
	/**
	 * Avisa se a instrucao anterior entrou para a conta do PC. Pois caso tenha 
	 * tido uma E/S a instrucao nao entra na contagem e o processo e bloqueado. 
	 */
	private boolean anteriormenteBloqueado = false; 
	
	/**
	 * Construtor da classe.
	 * Seta os parametros PC com 0, pois ainda não comecou a contagem do programa
	 * e das instrucoes, com um array contendo as instrucoes
	 * @param instrucoes
	 */
	public Processo(LinkedList<String> instrucoes) {
		this.PC = 0;
		this.instrucoes = instrucoes;
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
		
		if (instrucoes.isEmpty()) {
			String instrucao = instrucoes.get(0);
			instrucoes.remove(0);
			return instrucao;
		}
		
		return "";
	
	}

	/**
	 * Verifica se a variavel anteriormenteBloqueado e Verdadeira ou Falsa
	 * @return
	 */
	public boolean isAnteriormenteBloqueado() {
		return anteriormenteBloqueado;
	}

	/**
	 * Seta a variavel anteriormenteBloqueado
	 * @param anteriormenteBloqueado
	 */
	public void setAnteriormenteBloqueado(boolean anteriormenteBloqueado) {
		this.anteriormenteBloqueado = anteriormenteBloqueado;
	}

}
