public class Merge {
	
	//============================================================================
	/*constrói uma lista contendo n palavras de 10 caracteres 
	aleatórias utilizando o alfabeto (abcdefghijklmnopqrstuvwxyz)*/
	public static String createWord(int l, String alfabeto) {

		char[] palavra = new char[l];

		for (int i = 0; i < l; i++) {
			int t = StdRandom.uniform(alfabeto.length());
			palavra[i] = alfabeto.charAt(t);
		}

		return new String(palavra);
	}
	
	public static String[] createList(int n) {

		String alfabeto = "abcdefghijklmnopqrstuvwxyz";
		String[] lista = new String[n];

		for (int i = 0; i < n; i++) {
			lista[i] = createWord(10, alfabeto);
		}

		return lista;
	}
	//============================================================================

	//============================================================================
	/*cria a lista de palavras aleatórias, chama o método principal e imprime a
	lista ordenada*/
	public static void mergeSort(int n) {

		String[] lista = createList(n);

		//imprime a lista não ordenada
		for (int i = 0; i < n; i++) {
			StdOut.println(lista[i]);
		}

		StdOut.println();

		mergeSort(lista, 0, n-1);

		//imprime a lista ordenada
		for (int i = 0; i < n; i++) {
			StdOut.println(lista[i]);
		}
	}
	//============================================================================

	//============================================================================
	//ordena a lista de palavras
	public static void mergeSort(String[] lista, int low, int high) {

		int mid = (low + high)/2;

		//CASO BASE
		/*quando o subvetor possuir apenas um elemento, este subvetor já estará
		ordenado, logo basta encerrarmos a chamada recursiva*/
		if (low == high) {
			return;
		}

		/*caso o subvetor possua mais de um elemento, divida o subvetor em
		dois outros subvetores até chegarmos ao caso base*/
		if (low < high) {
			mergeSort(lista, low, mid);
			mergeSort(lista, mid+1, high);
			/*quando as chamadas recursivas encerrarem, combinamos dois
			subvetores ordenados em um vetor ordenado através do
			método 'combine'*/
			combine(lista, low, mid, high);
		}
	}

	public static void combine(String[] lista, int low, int mid, int high) {
		
		/*vetor auxiliar que guarda os elementos parcialmente ordenados
		para depois copiá-los no vetor final a fim de evitar problemas de 
		sobreposição*/
		String[] aux = new String[high-low+1]; 
		
		int i = low;	//índice que percorre o primeiro subvetor
		int j = mid + 1;	//índice que percorre o segundo subvetor

		for (int k = 0; k < aux.length; k++) {
			/*caso já tivermos percorrido todo o segundo subvetor, basta
			copiarmos o primeiro subvetor no vetor final*/
			if (j == (high + 1)) {
				aux[k] = lista[i];
				i++;
			}
			/*caso já tivermos percorrido todo o primeiro subvetor, basta
			copiarmos o segundo subvetor no vetor "final"(aux)*/
			else if (i == (mid + 1)) {
				aux[k] = lista[j];
				j++;
			}
			/*caso contrário, comparamos os elementos dos subvetores um a um 
			e verificamos qual o "menor" elemento, este elemento será copiado
			para o vetor "final"(aux)*/
		 	else if (lista[i].compareTo(lista[j]) < 0) {
				aux[k] = lista[i];
				i++;
			}
			else {
				aux[k] = lista[j];
				j++;
			}
		}
		
		/*terminada a ordenação dos elementos no vetor auxiliar,
		copiamos os elementos para o vetor final, de forma que o vetor
		resultante esteja totalmente ordenado*/
		for (int c = 0; c < aux.length; c++) {
			lista[low + c] = aux[c];
		}

		return;
	}
	//============================================================================

	public static void main(String[] args) {

		//recebe o número de palavras que a lista irá possuir
		int n = Integer.parseInt(args[0]);

		double startTime = System.currentTimeMillis() / 1000.0;
		mergeSort(n);
		double stopTime = System.currentTimeMillis() / 1000.0;

		double deltaTime = stopTime - startTime;
		StdOut.printf("%f seconds\n", deltaTime);
	}
}