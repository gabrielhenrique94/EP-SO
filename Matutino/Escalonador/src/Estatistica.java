/** Escola de Artes Ciencias e Humanidades
 * da Universidade de Sao Paulo (EACH-USP)
 * 
 * Curso de Sistemas de Informacao (Matutino)
 * 2 Semestre de 2014
 * 
 * Primeiro Exercicio Programa (EP1) 
 * da disciplina de Sistemas Operacionais 
 * 
 * @author Amandha Adulis
 * @author Gustavo Gamino
 * @author Heloisa Carbone
 * @author Julia Murano
 */

import java.util.ArrayList;

/**
 * Classe responsável por gerir as estatisticas
 * referentes a execução dos processos atribuida 
 * ao escalonador
 */
public class Estatistica {
	
	//********************* VARIAVEIS GLOBAIS *********************//
	
	/**
	 * Lista de arrays com numero de instruções
	 */
	static private ArrayList<Integer> numeroDeInstrucoes = new ArrayList<>();
	
	/**
	 * Variavel para definir o número total de processos
	 */
	private int numeroDeProcessos = 0;

	//************************** METODOS **************************//
	
	/**
	 * Atribui o valor total de numero de processos que
	 * o escalonador deverá gerenciar
	 * @param numProcessos - número de Processos
	 */
	public void setNumeroDeProcessos(int numProcessos){
		numeroDeProcessos = numProcessos;
	}

	/**
	 * Realiza a adição do número de instruções
	 * que foram executadas por quantum a uma lista
	 * @param instrucoes
	 */
	public void addInstrucoes(int instrucoes){
		numeroDeInstrucoes.add(instrucoes);
	}
	
	/**
	 * Cálcula o número médio de instruções por quantum
	 * Dado o total de quantuns usados para terminar de 
	 * executar todos os processos 
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
	 * Cálcula a média de trocas realizadas entre processos
	 * sobre o número total de processos escalonados
	 * @return média de trocas
	 */
	public double mediaTrocas(){
		return (double) (numeroDeInstrucoes.size() - 1)/numeroDeProcessos;
	}
}