import java.util.ArrayList;

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
	private ArrayList<String> instrucoes = new ArrayList<String> ();
	
	/**
	 * Avisa se a instrucao anterior entrou para a conta do PC. Pois caso tenha 
	 * tido uma E/S a instrucao nao entra na contagem e o processo e bloqueado. 
	 */
	private boolean anteriormenteBloqueado = false; 
	
	public Processo(ArrayList<String> instrucoes) {
		this.PC = 0;
		this.instrucoes = instrucoes;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getPC() {
		return PC;
	}

	public void setPC(int soma) {
		PC = PC + soma;
	}
	
	public String getProximaInstrucao() {
		
		if (instrucoes.size() != 0) {
			String instrucao = instrucoes.get(0);
			instrucoes.remove(0);
			return instrucao;
		}
		
		return "";
	
	}

	public boolean isAnteriormenteBloqueado() {
		return anteriormenteBloqueado;
	}

	public void setAnteriormenteBloqueado(boolean anteriormenteBloqueado) {
		this.anteriormenteBloqueado = anteriormenteBloqueado;
	}

}
