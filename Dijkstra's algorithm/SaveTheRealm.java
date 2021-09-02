public class SaveTheRealm {

	public static Queue<Integer> membros = new Queue<Integer>();	// guarda a cidade inicial de cada um dos membros da comunidade
	public static int biroliro;										// cidade inicial do Biroliro
	
	// representa a 'distância' entre dois vértices u e v tais que não existe um caminho entre u e v
	public static int infty;
	// aqui será igual a (n-1)*M, onde M = max{w(uv):u,v pertencem a v(D)} e n = #v(D).
	// dessa forma, é garantido que infty > peso de qualquer caminho do digrafo
	
	// matriz que guarda o tempo necessário para cada membro chegar em cada cidade do reino
	public static int[][] matrix;
	/* Exemplo: Se existem k membros na comunidade,
	matrix[0][0] = tempo necessário para o primeiro membro da comunidade chegar até a cidade 0 do reino;
	matrix[k][0] = tempo necessário para o Biroliro chegar até a cidade 0 do reino.

	Mais geralmente:
	matrix[i][j] = tempo necessário para o (i+1)-ésimo membro da comunidade chegar até a cidade j do reino;
	matrix[k][j] = tempo necessário para o Biroliro chegar até a cidade j do reino,
	onde i varia de 0 a k-1 e j varia de 0 a n-1 */


	//===============================================================================================================================
	public static class DirectedGraph {

		// tabela de símbolos: key = vértice v, value = {(key x, value w(vx)) : x pertence a N+(v)}
    	private ST<Integer, ST<Integer, Integer>> digraph;
    	// quantidade de arestas
    	private int E;

    	public DirectedGraph() {
        	digraph = new ST<Integer, ST<Integer, Integer>>();
    	}

    	public DirectedGraph(String filename) {
        	digraph = new ST<Integer, ST<Integer, Integer>>();
        	In in = new In(filename);

        	int n = in.readInt();	// número de cidades do reino
        	int m = in.readInt();	// número de estradas
        	int k = in.readInt();	// número de membros da comunidade

        	matrix = new int[k + 1][n];

        	// Constrói o digrafo (mapa do reino)
        	for (int i = 0; i < m; i++) {
        		// par de vértices que compõem a aresta
        		// cidades que estão ligadas por uma estrada
        		int a = in.readInt();
        		int b = in.readInt();

        		// peso da aresta
        		// tempo que se leva para percorrer a estrada
        		int t = in.readInt();

        		// encontra o valor de (n-1)*M
        		if (infty < ((n - 1)*t)) infty = (n - 1)*t;

        		addEdge(a, b, t);
        	}

        	// Guarda a cidade inicial dos membros na fila 'membros'
        	for (int i = 0; i < k; i++)
        		membros.enqueue(in.readInt());

        	// Guarda a cidade inicial do biroliro em 'biroliro'
        	biroliro = in.readInt();
   		}

  		
  		// retorna a quantidade de vértices do digrafo
    	public int V() {
        	return digraph.size();
    	}

  
  		// retorna a quantidade de arestas do digrafo
    	public int E() {
        	return E;
    	}


    	// retorna o peso da aresta key1key2
    	public int get(int key1, int key2) {
        	return digraph.get(key1).get(key2);
    	}


    	// verifica se v é um vértice do digrafo
    	private void validateVertex(int v) {
        	if (!hasVertex(v)) throw new IllegalArgumentException(v + " is not a vertex");
    	}

  	
  		// adiciona a aresta vu ao digrafo
    	public void addEdge(int v, int u, int w) {
        	if (!hasVertex(v)) addVertex(v);
        	if (!hasVertex(u)) addVertex(u);
        	if (!hasEdge(v, u)) E++;
        	digraph.get(v).put(u, w);
    	}

 	
 		// adiciona o vértice v ao digrafo
    	public void addVertex(int v) {
        	if (!hasVertex(v)) digraph.put(v, new ST<Integer, Integer>());
    	}


    	// retorna todos os vértice do digrafo
    	public Iterable<Integer> vertices() {
        	return digraph.keys();
    	}


    	// retorna todos os vértices pertencentes a N+(v)
    	public Iterable<Integer> adjacentTo(int v) {
        	validateVertex(v);
        	return digraph.get(v);
    	}

  
  		// verifica se o vértice v pertence ao digrafo
    	public boolean hasVertex(int v) {
        	return digraph.contains(v);
    	}

 		
 		// verifica se existe a aresta vu no digrafo
    	public boolean hasEdge(int v, int u) {
        	validateVertex(v);
        	validateVertex(u);
        	return digraph.get(v).contains(u);
    	}
    }
	//===============================================================================================================================


	//===============================================================================================================================
	/* Dijkstra: estrutura que calcula (usando o algoritmo de Dijkstra) e guarda os caminhos mínimos de um vértice s a todos os 
	outros vértices de um digrafo g */
	public static class Dijkstra {
		
		// prev[v] = vértice predecessor no menor caminho de s a v
    	// dist[v] = comprimento do menor caminho de s a v
		private ST<Integer, Integer>  prev = new ST<Integer, Integer>();
    	private ST<Integer, Integer> dist = new ST<Integer, Integer>();

    	// encontra o menor caminho do vértice s a todos os outros vértices do digrafo g
    	public Dijkstra(DirectedGraph g, int s) {
        	// guarda os vértices que ainda não foram visitados
			ST<Integer, Integer>  unvisited_nodes = new ST<Integer, Integer>();
			/* Obs.: O valor guardado em cada chave de unvisited_nodes não possui relevância.
        	Elas são necessárias na estrutura ST, que foi utilizada a fim de se usar o laço 
        	'for each key in unvisited_nodes' */

        	// define a distância inicial dos vértices
        	// inicialmente nenhum vértice foi visitado
        	for (Integer w : g.vertices()) {
        		unvisited_nodes.put(w, 0);
        		prev.put(w, null);
        		dist.put(w, infty);
        	}
        	dist.put(s, 0);
        	
        	// enquanto houver vértices não visitados faça isso
        	while (!unvisited_nodes.isEmpty()) {
        		// encontra o vértice não visitado x cuja distância de s a x é a menor
        		int x = unvisited_nodes.min();
        		for (Integer w : unvisited_nodes.keys()) {
        			if (dist.get(w) < dist.get(x)) {
        				x = w;
        			}
        		}

        		// o vértice x foi visitado nessa iteração
        		unvisited_nodes.remove(x);

        		// relaxa todos os vértices adjacentes* a x que ainda não foram visitados
        		// *pela estrutura DirectedGraph, os vértices adjacentes a x são aqueles que pertencem a N+(x)
            	for (Integer y : g.adjacentTo(x)) {
                	if (unvisited_nodes.contains(y)) {
                		// relaxa o vértice w
                		if ((dist.get(x) + g.get(x, y)) < dist.get(y)) {
                			dist.put(y, dist.get(x) + g.get(x, y));
                			prev.put(y, x);
                		}
                	}
            	}
        	}
    	}


    	// retorna o comprimento do menor caminho de s a v
    	public int distanceTo(int v) {
        	if (!hasPathTo(v)) return infty;
        	return dist.get(v);
    	}


    	// diz se existe um caminho de s a v
    	public boolean hasPathTo(int v) {
        	return dist.contains(v);
    	}
	}
	//===============================================================================================================================


	//===============================================================================================================================
	/* BellmanFord: estrutura que calcula (usando o algoritmo de Bellman-Ford) e guarda os caminhos mínimos de um vértice s a todos 
	os outros vértices de um digrafo g */
	public static class BellmanFord {

		// prev[v] = vértice predecessor no menor caminho de s a v
    	// dist[v] = comprimento do menor caminho de s a v
		private ST<Integer, Integer>  prev = new ST<Integer, Integer>();
    	private ST<Integer, Integer> dist = new ST<Integer, Integer>();

    	// encontra o menor caminho do vértice s a todos os outros vértices do digrafo g
    	public BellmanFord(DirectedGraph g, int s) {
    		// define a distância inicial dos vértices
        	for (Integer w : g.vertices()) {
        		prev.put(w, null);
        		dist.put(w, infty);
        	}
        	dist.put(s, 0);

        	/* relaxa todos as arestas do digrafo v(g) vezes, garantindo assim 
        	que, ao final do algoritmo, todas as distâncias vão estar corretas */
        	for (int i = 1; i < g.V(); i++) {
        		for (Integer x : g.vertices()) {
        			for (Integer y : g.adjacentTo(x)) {
                		// relaxa a aresta xy
                		if ((dist.get(x) + g.get(x, y)) < dist.get(y)) {
                			dist.put(y, dist.get(x) + g.get(x, y));
                			prev.put(y, x);
                		}
            		}
        		}
        	}
    	}


    	// retorna o comprimento do menor caminho de s a v
    	public int distanceTo(int v) {
        	if (!hasPathTo(v)) return infty;
        	return dist.get(v);
    	}


    	// diz se existe um caminho de s a v
    	public boolean hasPathTo(int v) {
        	return dist.contains(v);
    	}
	}
	//===============================================================================================================================



	public static void main(String[] args) {
		
		String filename = args[0];	// arquivo de entrada
		int option = Integer.parseInt(args[1]);
		// option 1: algoritmo de Dijkstra
		// option 2: algoritmo de Bellman-Ford
		
		// constrói o reino e guarda as cidades iniciais dos membros e do Biroliro
		DirectedGraph realm = new DirectedGraph(filename);

		/* usa o algoritmo escolhido para calcular o tmepo mínimo que os membros e o Biroliro 
		levam para chegar em todas as cidades */
		if (option == 1) {
			while (!membros.isEmpty()) {
				for (int i = 0; i < matrix.length; i++) {
					// se for o Biroliro
					if (i == (matrix.length - 1)) {
						Dijkstra minimum_times_Biroliro = new Dijkstra(realm, biroliro);
						for (int j = 0; j < matrix[0].length; j++)
							matrix[i][j] = minimum_times_Biroliro.distanceTo(j);
					}
					// se for um membro da comunidade
					else {
						int membro = membros.dequeue();
						Dijkstra minimum_times = new Dijkstra(realm, membro);
						for (int j = 0; j < matrix[0].length; j++)
							matrix[i][j] = minimum_times.distanceTo(j);
					}
				}			
			}
		}
		else {
			while (!membros.isEmpty()) {
				for (int i = 0; i < matrix.length; i++) {
					// se for o Biroliro
					if (i == (matrix.length - 1)) {
						BellmanFord minimum_times_Biroliro = new BellmanFord(realm, biroliro);
						for (int j = 0; j < matrix[0].length; j++)
							matrix[i][j] = minimum_times_Biroliro.distanceTo(j);
					}
					// se for um membro da comunidade
					else {
						int membro = membros.dequeue();
						BellmanFord minimum_times_membro = new BellmanFord(realm, membro);
						for (int j = 0; j < matrix[0].length; j++)
							matrix[i][j] = minimum_times_membro.distanceTo(j);
					}
				}			
			}
		}

		boolean o_reino_está_salvo = false;
		int s = 0;	// quantidade de cidades seguras
		Queue<Integer> cidades_seguras = new Queue<Integer>();

		int i_Biroliro = matrix.length - 1;	// linha referente ao Biroliro na matriz
		for (int j = 0; j < matrix[0].length; j++) {
			int max_dist_membros = matrix[0][j];	// maior tempo que um membro leva para chegar na cidade j
			for (int i = 0; i < (matrix.length - 1); i++) {
				// encontra o maior tempo que um membro leva para chegar na cidade j
				if (matrix[i][j] > max_dist_membros)
					max_dist_membros = matrix[i][j];
			}
			/* se o tempo que o Biroliro leva para chegar até a cidade j for estritamente 
			maior que tempo que um membro da comunidade leva para chegar na cidade j, então 
			a cidade j é segura*/
			if (max_dist_membros < matrix[i_Biroliro][j]) {
				o_reino_está_salvo = true;
				s = s + 1;
				cidades_seguras.enqueue(j);
			}
		}
		
		if (o_reino_está_salvo) {
			StdOut.println("O REINO ESTÁ SALVO!");
			StdOut.println(s);
			while (!cidades_seguras.isEmpty()) {
				StdOut.print(cidades_seguras.dequeue());
				StdOut.print(" ");
			}
			StdOut.println();
		}
		else StdOut.println("INFELIZMENTE O PRECONCEITO VENCEU :(");
	}
}