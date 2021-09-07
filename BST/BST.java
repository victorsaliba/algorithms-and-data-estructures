public class BST<Key extends Comparable<Key>, Value> {
    
    private Node root;  // raiz da BST

    private class Node {
        
        private final Key key;      // chave do nó       
        private Value val;          // valor salvo no nó
        private int size;           // quantidade de nós da subárvore cuja raiz é o nó
        private Node left, right;   // referências aos "filhos" do nó

        public Node(Key key, Value val, int size) {
            // salva o par (key, value) referente ao nó criado
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }
    
    public BST() {
        // cria uma árvore binária de busca inicialmente vazia (root = null e size = 0)
    }


    //==============================================================================================================
    // se a árvore estiver vazia, retorna TRUE
    public boolean isEmpty() {
        return root == null;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Retorna a quantidade de nós que a BST possui
    public int size() {
        return size(root);
    }
    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Retorna o valor correspondente a uma dada chave
    public Value get(Key key) {
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
    // Retorna TRUE se a chave key contém um valor na tabela de símbolos
    public boolean contains(Key key) {
        return get(key) != null;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Imprime os elementos da tabela de símbolos por ordem de chaves
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
    // Adiciona um par (key, value) na tabela de símbolos e retorna a RAIZ da árvore
    public Node put(Key key, Value val) {
        return root = put(root, key, val);
    }
    private Node put(Node x, Key key, Value val) {
        // quando encontrar a posição correta do novo nó na árvore, adicione esse nó na árvore
        if (x == null) return new Node(key, val, 1);   

        // laço recursivo para encontrar a posição correta do novo nó na árvore binária de busca
        int cmd = key.compareTo(x.key);
        // se a nova chave for menor, adicione o novo par na subárvore esquerda e atualize os links entre os nós
        if      (cmd < 0) x.left = put(x.left, key, val);
        // se a nova chave for maior, adicione o novo par na subárvore direita e atualize os links entre os nós
        else if (cmd > 0) x.right = put(x.right, key, val);
        // caso a chave já exista, atualize o valor
        else              x.val = val;

        // atualiza a quantidade de nós das subárvores
        x.size = size(x.left) + size(x.right) + 1;

        return x;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Imprime os elementos da tabela de símbolos por nível da árvore, começando pela raiz
    public void levelOrder() {
        this.levelOrder(this.root);
    }
    private void levelOrder(Node x) {
        if (x == null) return;                  // se a árvore for vazia, não há o que imprimir
        Queue<Key> keys = new Queue<Key>();     // guarda as chaves dos nós da árvore
        Queue<Node> queue = new Queue<Node>();  // guarda os nós da árvore
        queue.enqueue(x);                       // adiciona a raíz da árvore na fila de nós
        keys.enqueue(x.key);                    // adiciona a chave da raíz na fila de chaves
        // laço que analisa todos os nós da árvore
        while (!queue.isEmpty() && !keys.isEmpty()) {
            /* imprime todos os nós que estão em um mesmo nível devido à propriedade 
            da fila de adicionar nós sempre na última posição*/
            Node y = queue.dequeue();
            StdOut.print(keys.dequeue() + " ");
            if (y.left != null) {
                // adiciona um novo par (key, value) na fila para ser imprimido posteriormente
                // note que os pares adicionados estão um nível abaixo do nó impresso
                queue.enqueue(y.left);
                keys.enqueue(y.left.key);
            }
            if (y.right != null) {
                // adiciona um novo par (key, value) na fila para ser imprimido posteriormente
                queue.enqueue(y.right);
                keys.enqueue(y.right.key);
            }   
        }
    }
    //==============================================================================================================


    //==============================================================================================================
    // Imprime os elementos da tabela de símbolos por nível da árvore, começando das folhas no último nível
    public void levelOrderFolhas() {
        this.levelOrderFolhas(this.root);
    }
    private void levelOrderFolhas(Node x) {
        // basta invertemos a saída do método leveOrder(root)
        if (x == null) return;
        Queue<Key> keys = new Queue<Key>();
        Queue<Node> queue = new Queue<Node>();
        String out = "";                        // guarda a saída 
        queue.enqueue(x);
        keys.enqueue(x.key);
        while (!queue.isEmpty() && !keys.isEmpty()) {
            Node y = queue.dequeue();
            out = keys.dequeue() + " " + out;   // inverte a saída que conseguimos no método levelOrder(root)
            if (y.left != null) {
                queue.enqueue(y.left);
                keys.enqueue(y.left.key);
            }
            if (y.right != null) {
                queue.enqueue(y.right);
                keys.enqueue(y.right.key);
            }   
        }

        StdOut.print(out);
    }
    //==============================================================================================================


    //==============================================================================================================
    // Retorna o valor associado à menor chave da tabela de símbolos
    public Value min() {
        return min(root);
    }
    private Value min(Node x) {
        /* o nó cuja chave é a menor da tabela é o nó x tal que x.left = null,
        pois não haverá uma chave menor que x.key, visto que, pela estrutura
        da árvore binária de busca, x.left.key <= x.key <= x.right.key */
        if (x.left != null)
            return min(x.left);

        return x.val;   // quando for x.left == null, retorne o valor associado ao nó x
    }
    //==============================================================================================================


    //==============================================================================================================
    // Retorna o valor associado à maior chave da tabela de símbolos
    public Value max() {
        return max(root);
    }
    private Value max(Node x) {
        /* o nó cuja chave é a maior da tabela é o nó x tal que x.right = null,
        pois não haverá uma chave maior que x.key, visto que, pela estrutura
        da árvore binária de busca, x.left.key <= x.key <= x.right.key */
        if (x.right != null)
            return max(x.right);

        return x.val;   // quando for x.right == null, retorne o valor associado ao nó x
    }
    //==============================================================================================================


    //==============================================================================================================
    // Imprime todas as chaves menores que key
    public void menores(Key key){
        menores(root, key);
    }
    private void menores(Node x, Key key) {
        if (x ==  null) return;
        // se o nó tiver uma chave menor que key, imprima a sua chave
        if (key.compareTo(x.key) > 0)
            StdOut.print(x.key + " ");
        
        /* laço recursivo que nos permite percorrer toda a árvore
        em busca das chaves menores que key*/
        menores(x.left, key);
        menores(x.right, key);
        
        return;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Imprime todas as chaves maiores que key
    public void maiores(Key key){
        maiores(root, key);
    }
    private void maiores(Node x, Key key) {
        if (x == null) return;
        // se o nó tiver uma chave maior que key, imprima a sua chave
        if (key.compareTo(x.key) < 0)
            StdOut.print(x.key + " ");
        
        /* laço recursivo que nos permite percorrer toda a árvore
        em busca das chaves maiores que key*/
        maiores(x.left, key);
        maiores(x.right, key);
        
        return;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Imprime todas as chaves maiores que "menor" e menores que "maior"
    public void entre(Key menor, Key maior){
        entre(root, menor, maior);
    }
    private void entre(Node x, Key menor, Key maior){
        if (x == null) return;
        // se o nó tiver uma chave entre 'menor' e 'maior', imprima a sua chave
        if ((maior.compareTo(x.key) > 0) && (menor.compareTo(x.key) < 0))
            StdOut.print(x.key + " ");
        
        /* laço recursivo que nos permite percorrer toda a árvore
        em busca das chaves maiores que 'menor' e menores que 'maior'*/
        entre(x.left, menor, maior);
        entre(x.right, menor, maior);
        
        return;
    }
    //==============================================================================================================


    //==============================================================================================================
    // Remove o nó com menor chave na tabela de símbolos e retorna o valor associado a ele
    public Value removeMin() {
        Value min = min(root);  // salva o valor do atual nó com menor chave
        root = removeMin(root); // retira o nó de menor chave da árvore e atualiza a raiz da árvore
        return min;
    }
    private Node removeMin(Node x) {
        // caso x.left == null, x é o nó com menor chave
        if (x.left == null) return x.right;

        // se x.left != null, remova o nó com menor chave da subárvore esquerda e atualize os links entre os nós
        x.left = removeMin(x.left);

        // atualiza a quantidade de nós das subárvores
        x.size = size(x.left) + size(x.right) + 1;

        return x;
    }
    //==============================================================================================================


    public static void main(String[] args) { 
        BST<Integer, Integer> st = new BST<Integer, Integer>(); // cria uma árvore binária de busca vazia
        for (int i = 0; !StdIn.isEmpty(); i++) {
            int key = StdIn.readInt();
            st.put(key, i);                                     // adiciona os pares (key, value) à arvore st
        }

        StdOut.println();
        StdOut.print("Chaves em ordem crescente: ");
        StdOut.println();
        st.inOrder();
        StdOut.println();

        StdOut.println();
        StdOut.print("Chaves em ordem de níveis: ");
        StdOut.println();
        st.levelOrder();
        StdOut.println();

        StdOut.println();
        StdOut.print("Chaves em ordem reversa de níveis: ");
        StdOut.println();
        st.levelOrderFolhas();
        StdOut.println();

        StdOut.println();
        StdOut.print("Valor associado à menor chave: " + st.min());
        StdOut.println();

        StdOut.println();
        StdOut.print("Valor associado à maior chave: " + st.max());
        StdOut.println();

        StdOut.println();
        StdOut.print("Quantidade de elementos da tabela de símbolos: " + st.size());
        StdOut.println();

        StdOut.println();
        int in1 = 20;
        StdOut.print("Chaves menores que " + in1 + ": ");
        StdOut.println();
        st.menores(in1);
        StdOut.println();

        StdOut.println();
        int in2 = 7;
        StdOut.print("Chaves maiores que " + in2 + ": ");
        StdOut.println();
        st.maiores(in2);
        StdOut.println();

        StdOut.println();
        StdOut.print("Chaves que estão entre 7 e 20: ");
        StdOut.println();
        st.entre(in2, in1);
        StdOut.println();

        while (!st.isEmpty()) {
            StdOut.println();
            int x = st.removeMin();
            StdOut.print("-O elemento associado à menor chave (" + x + ") foi removido da tabela-");
            StdOut.println();
            StdOut.print("Chaves em ordem crescente (após a remoção de " + x + "): ");
            StdOut.println();
            st.inOrder();
            StdOut.println();
            StdOut.print("Quantidade de elementos da tabela de símbolos: " + st.size());
            StdOut.println();
        }
    }
}
