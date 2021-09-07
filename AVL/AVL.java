public class AVL<Key extends Comparable<Key>, Value> {
    
    private Node root;  // raiz da AVL
    
    private class Node {

        private final Key key;      // chave do nó    
        private Value val;          // valor salvo no nó
        private int size;           // quantidade de nós da subárvore cuja raiz é o nó
        private int height;         // altura da subárvore cuja raiz é o nó
        private Node left, right;   // referências aos "filhos" do nó

        public Node(Key key, Value val, int size, int height) {
            // salva o par (key, value) referente ao nó criado
            this.key = key;
            this.val = val;
            this.size = size;
            this.height = height;
        }
    }
    
    public AVL() {
        // cria uma árvore AVL inicialmente vazia (root = null)
    }


    //==============================================================================================================
    // se a árvore estiver vazia, retorna TRUE
    public boolean isEmpty() {
        return root == null;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Retorna a quantidade de nós que a árvore AVL possui
    public int size() {
        return size(root);
    }
    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Retorna a áltura da árvore AVL
    public int height() {
        return height(root);
    }
    private int height(Node x) {
        if (x == null) return -1;   // Para nós sem filhos, temos h = 0, para um nó null temos h = -1;
        else return x.height;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Retorna o valor correspondente a uma dada chave
    public Value get(Key key){
        return get(root, key);
    }
    private Value get(Node x, Key key) {
        if (x == null) return null;                 // se a árvore estiver vazia, não há o que retornar
        int cmp = key.compareTo(x.key);             // diz se a chave procurada é maior ou menor que x.key
        if      (cmp < 0) return get(x.left, key);  // se for menor, procure a chave na subárvore esquerda
        else if (cmp > 0) return get(x.right, key); // se for maior, procure a chave na subárvore direita
        else              return x.val;             // caso tenha encontrado a chave, retorne o valor correspondente
    }
    //==============================================================================================================


    //==============================================================================================================
    // Retorna o valor associado à menor chave da árvore
    public Value min() {
        return min(root).val;
    }
    private Node min(Node x) {
        /* o nó cuja chave é a menor da tabela é o nó x tal que x.left = null,
        pois não haverá uma chave menor que x.key, visto que, pela estrutura
        da árvore binária de busca, x.left.key <= x.key <= x.right.key */
        if (x.left != null)
            return min(x.left);

        return x;   // quando for x.left == null, retorne o valor associado ao nó x
    }
    //==============================================================================================================

    
    //==============================================================================================================
    // Retorna TRUE se a chave key contém um valor na tabela de símbolos
    public boolean contains(Key key) {
        return get(key) != null;
    }
    //==============================================================================================================

    
    //==============================================================================================================
    // Imprime os elementos da tabela de símbolo por ordem de chaves
    public void inOrder() {
        inOrder(root);
    }
    private void inOrder(Node x) {
        if (x == null) return;      // se o nó for vazio, imprima nada
        inOrder(x.left);            // imprime a subávore esquerda
        StdOut.print(x.key + " ");  // imprime o nó
        inOrder(x.right);           // imprime a subárvore direita
    }
    //==============================================================================================================


    //==============================================================================================================
    // calcula o balanceamento do nó x
    // o balanceamento de um nó x é a diferença entre as alturas dos filhos de x
    private int balance(Node x) {
        return height(x.right) - height(x.left);
    }
    //==============================================================================================================


    //==============================================================================================================
    private Node balancing(Node x) {
        /* verifica se a altura da subárvore direita é 
        maior que a da subárvore esquerda, isto é, se 
        o desbalanceamento ocorreu na subárvore direita */
        if (balance(x) > 1) {
            /* se o desbalanceamento da subárvore direita
            de x estiver na subárvore esquerda, faça uma dupla
            rotação à esquerda */
            if (balance(x.right) < 0) {
                x.right = rotateRight(x.right);
            }
            /* caso contráio, faça uma
            rotação à esquerda simples */
            x = rotateLeft(x);
        }
        /* caso contráio, o desbalanceamento ocorreu na
        subárvore esquerda */
        else if (balance(x) < -1) {
            /* se o desbalanceamento da subárvore esquerda
            de x estiver na subárvore direita, faça uma dupla
            rotação à direita */
            if (balance(x.left) > 0) {
                x.left = rotateLeft(x.left);
            }
            /* caso contráio, faça uma
            rotação à direita simples */
            x = rotateRight(x);
        }

        return x;
    }
    //==============================================================================================================


    //==============================================================================================================
    private Node rotateLeft(Node x) {
        /* Faz a rotação à esquerda trocando x e y(x.right) de lugar
        e colocando o fiho esquerdo de y como o novo filho direito
        de x*/
        Node y = x.right;
        x.right = y.left;
        y.left = x;

        // atualiza a quantidade de nós e a altura das subárvores
        y.size = size(y.left) + size(y.right) + 1;
        x.size = size(x.left) + size(x.right) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }
    //==============================================================================================================


    //==============================================================================================================
    private Node rotateRight(Node x) {
        /* Faz a rotação à direita trocando x e y(x.left) de lugar
        e colocando o fiho direito de y como o novo filho esquerdo
        de x*/
        Node y = x.left;
        x.left = y.right;
        y.right = x;

        // atualiza a quantidade de nós e a altura das subárvores
        y.size = size(y.left) + size(y.right) + 1;
        x.size = size(x.left) + size(x.right) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Adiciona um par (key, value) na tabela de símbolos e retorna a RAIZ da árvore AVL
    public Node put(Key key, Value val) {
        return root = put(root, key, val);
    }    
    private Node put(Node x, Key key, Value val) {
        // quando encontrar a posição correta do novo nó na árvore, adicione esse nó na árvore
        if (x == null) return new Node(key, val, 1, 0);   

        // laço recursivo para encontrar a posição correta do novo nó na árvore binária de busca
        int cmd = key.compareTo(x.key);
        // se a nova chave for menor, adicione o novo par na subárvore esquerda e atualize os links entre os nós
        if      (cmd < 0) x.left = put(x.left, key, val);
        // se a nova chave for maior, adicione o novo par na subárvore direita e atualize os links entre os nós
        else if (cmd > 0) x.right = put(x.right, key, val);
        // caso a chave já exista, atualize o valor
        else              x.val = val;
        
        // atualiza a quantidade de nós e a altura das subárvores
        x.size = size(x.left) + size(x.right) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        
        // Corrige o balanceamento da árvore AVL
        if (Math.abs(balance(x)) > 1) {
            x = balancing(x);
        }
        return x;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Remove elemento com menor chave
    public Value removeMin() {
        Value min = min(root).val;  // salva o valor do atual nó com menor chave
        root = removeMin(root); // retira o nó de menor chave da árvore e atualiza a raiz da árvore
        return min;
    }
    private Node removeMin(Node x) {
        // caso x.left == null, x é o nó com menor chave
        if (x.left == null) return x.right;

        // se x.left != null, remova o nó com menor chave da subárvore esquerda e atualize os links entre os nós
        x.left = removeMin(x.left);
        
        // atualiza a quantidade de nós e a altura das subárvores
        x.size = size(x.left) + size(x.right) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        
        // Corrige o balanceamento da árvore AVL
        if (Math.abs(balance(x)) > 1) {
            x = balancing(x);
        }
        return x;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Remove o nó com chave "key" e retorna o valor correspondente a essa chave
    public Value remove(Key key) {
        Value val = get(key);
        root = remove(root, key);
        return val;
    }   
    private Node remove(Node x, Key key) {
        if (x == null) return null;

        // procura na árvore a chave que se deseja remover
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = remove(x.left,  key);
        else if (cmp > 0) x.right = remove(x.right, key);
        // quando a chave é encontrada tem-se cmp = 0
        else {
            // se x não tiver filho direito, remova x e coloque no seu lugar o seu filho esquerdo
            if (x.right == null) return x.left;
            
            // se x não tiver filho esquerdo, remova x e coloque no seu lugar o seu filho direito
            if (x.left  == null) return x.right;

            // Caso tenha os dois, remova x e coloque no seu lugar a menor chave do seu filho direito
            /* Após isso, o novo filho direito de x será a subárvore direita do x original após a
            remoção da sua menor chave, já o filho esquerdo permanecerá o mesmo de antes*/
            Node t = x;
            x = min(t.right);
            x.right = removeMin(t.right);
            x.left = t.left;
        }
        
        // atualiza a quantidade de nós e a altura das subárvores
        x.size = size(x.left) + size(x.right) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        
        // Corrige o balanceamento da árvore AVL
        if (Math.abs(balance(x)) > 1) {
            x = balancing(x);
        }
        return x;
    }
    //==============================================================================================================

    
    //==============================================================================================================
    // Remove todos os nós da árvore
    public void clean() {
        root = clean(root);
    }
    private Node clean(Node x) {
        // Após a remoção de todos os nós de uma árvore cuja raiz é x tem-se x = null (a árvore está vazia)
        return x = null;
    }
    //==============================================================================================================


    // Embaralha os elementos do vetor de inteiros a
    private static void shuffle(int[] a) {
        // troca dois elementos aleatórios de 'a' de lugar 'a.length' vezes
        for (int k = 0; k < a.length; k++) {
            int i = (int)(Math.random()*a.length);
            int j = (int)(Math.random()*a.length);
            int num = a[i];
            a[i] = a[j];
            a[j] = num;
        }
    }


    public static void main(String[] args) {

        int[] in = new int[]{100, 1000, 10000, 100000}; // vetor que guarda os valores de entrada

        AVL<Integer, Integer> tree_1 = new AVL<Integer, Integer>(); // Árvore AVL construida inserindo chaves em ordem crescente
        AVL<Integer, Integer> tree_2 = new AVL<Integer, Integer>(); // Árvore AVL construida inserindo chaves em ordem decrescente
        AVL<Integer, Integer> tree_3 = new AVL<Integer, Integer>(); // Árvore AVL construida inserindo chaves em ordem aleatória

        for (int k = 0; k < in.length; k++) {
            int n = in[k];
            
            Stopwatch sw = new Stopwatch();
            // constrói tree_1
            for (int i = 1; i <= n; i++) {
                tree_1.put(i, 0);
            }
            double t1 = sw.elapsedTime();

            sw = new Stopwatch();
            // constrói tree_2
            for (int i = n; i > 0; i--) {
                tree_2.put(i, 0);
            }
            double t2 = sw.elapsedTime();

            // constrói uma lista com os números de 1 a n
            int[] keys = new int[n];
            for (int i = 1; i <= n; i++) {
                keys[i-1] = i;
            }
            /* embaralha a lista de números, obtendo assim
            uma permutação aleatória de {1,2,...,n}*/
            shuffle(keys);

            sw = new Stopwatch();
            // constrói tree_3 a partir da permutação aleatória obtida
            for (int i = 0; i < keys.length; i++) {
                tree_3.put(keys[i], 0);
            }
            double t3 = sw.elapsedTime();

            StdOut.printf("RELATÓRIO DA ENTRADA n = %d:\n", n);
            StdOut.printf("- Tempo para a construção e altura h da árvore AVL inserindo as chaves em ordem crescente: %f segundos, h = %d;\n", t1, tree_1.height());
            StdOut.printf("- Tempo para a construção e altura h da árvore AVL inserindo as chaves em ordem decrescente: %f segundos, h = %d;\n", t2, tree_2.height());
            StdOut.printf("- Tempo para a construção e altura h da árvore AVL inserindo as chaves em ordem aleatória: %f segundos, h = %d.\n", t3, tree_3.height());
            StdOut.println();

            // reinicia as três árvores para receberem uma nova entrada na próxima chamada
            tree_1.clean();
            tree_2.clean();
            tree_3.clean();
        }
    }
}
