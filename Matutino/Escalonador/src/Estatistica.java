/** <p>Escola de Artes Ciencias e Humanidades
 * da Universidade de Sao Paulo (EACH-USP)</p>
 * <br />
 * <p>Curso de Sistemas de Informacao (Matutino)
 * 2 Semestre de 2014</p>
 * <br />
 * <p>Primeiro Exercicio Programa (EP1)
 * da disciplina de Sistemas Operacionais </p>
 * <br />
 * @author Amandha Adulis
 * @author Gustavo Gamino
 * @author Heloisa Carbone
 * @author Julia Murano
 */

import java.util.ArrayList;

/**
 * Classe responsável por gerir as estatisticas<br />
 * referentes a execução dos processos atribuida <br />
 * ao escalonador.<br />
 */
public class Estatistica {
	
	//********************* VARIAVEIS GLOBAIS *********************//
	
	/**
	 * Lista de arrays com numero de instruções.<br />
	 */
	static private ArrayList<Integer> numeroDeInstrucoes = new ArrayList<>();
	
	/**
	 * Variavel para definir o número total de processos.<br />
	 */
	private int numeroDeProcessos = 0;

	//************************** METODOS **************************//
	
	/**
	 * Atribui o valor total de numero de processos que<br />
	 * o escalonador deverá gerenciar.<br />
	 * @param numProcessos - número de Processos
	 */
	public void setNumeroDeProcessos(int numProcessos){
		numeroDeProcessos = numProcessos;
	}

	/**
	 * Realiza a adição do número de instruções<br />
	 * que foram executadas por quantum a uma lista.<br />
	 * @param instrucoes
	 */
	public void addInstrucoes(int instrucoes){
		numeroDeInstrucoes.add(instrucoes);
	}
	
	/**
	 * Cálcula o número médio de instruções por quantum<br />
	 * Dado o total de quantuns usados para terminar de <br />
	 * executar todos os processos.<br />
	 * @return média de instruções
	 */
	public double mediaInstrucoes(){
		int soma = 0;
		for(Integer xi: numeroDeInstrucoes){
			soma = soma + xi;
		}
		return (double) soma/numeroDeInstrucoes.size();
	}
	
	/**
	 * Cálcula a média de trocas realizadas entre processos<br />
	 * sobre o número total de processos escalonados.<br />
	 * @return média de trocas
	 */
	public double mediaTrocas(){
		return (double) (numeroDeInstrucoes.size() - 1)/numeroDeProcessos;
	}
}