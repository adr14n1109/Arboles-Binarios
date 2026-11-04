import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
public class BinaryTree {

    TreeNode root;

    public BinaryTree() {
        this.root = null;
    }

    public BinaryTree(TreeNode root) {
        this.root = root;
    }

    /*
    IMPORTANTE: respete EXACTAMENTE los nombres y firmas de los metodos que se
    piden abajo; la revision automatica los busca por nombre. Puede agregar
    todos los metodos auxiliares (privados) que necesite.
    */


    //1. Construccion del arbol: implemente aqui

    public void insertRoot(Object data){
        if (root == null) {
            root = new TreeNode(data);
        }
    }

    public TreeNode search(Object data){
        return search(root, data);
    }
    private TreeNode search(TreeNode node, Object data) {
        if (node == null) {
            return null;
        }
        if (node.data == null ? data == null : node.data.equals(data)) {
            return node;
        }
        TreeNode found = search(node.left, data);
        if (found != null) {
            return found;
        }
        return search(node.right, data);
    }

    public boolean insertLeft(Object parent, Object data){
        TreeNode p = search(parent);
        if (p == null || p.left != null) {
            return false;
        }
        p.left = new TreeNode(data);
        return true;
    }

    public boolean insertRight(Object parent, Object data){
        TreeNode p = search(parent);
        if (p == null || p.right != null) {
            return false;
        }
        p.right = new TreeNode(data);
        return true;
    }

    private static final String STOP = "\u0000STOP";

    public void buildTree(Scanner scanner) {
        root = null;

        System.out.println("Indicaciones: Enter o '-' = no hay hijo | '.' = terminar el arbol");
        System.out.print("Raiz: ");
        String value = readValue(scanner);
        if (value == null || value.equals(STOP)) {
            System.out.println("Arbol vacio.");
            return;
        }
        insertRoot(value);

        // Recorrido por niveles (cola). Se enlaza directamente al nodo de la
        // cola, asi funciona bien incluso si hay datos repetidos.

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();

            System.out.print("Hijo izquierdo de " + current.data + ": ");
            String left = readValue(scanner);
            if (STOP.equals(left)) {
                break;
            }
            if (left != null) {
                current.left = new TreeNode(left);
                queue.add(current.left);
            }

            System.out.print("Hijo derecho de " + current.data + ": ");
            String right = readValue(scanner);
            if (STOP.equals(right)) {
                break;
            }
            if (right != null) {
                current.right = new TreeNode(right);
                queue.add(current.right);
            }
        }
        System.out.println("Arbol construido.");
    }

    private String readValue(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            return STOP;
        }
        String line = scanner.nextLine().trim();
        if (line.equals(".")) {
            return STOP;
        }
        if (line.isEmpty() || line.equals("-")) {
            return null;
        }
        return line;
    }


    //2. Visualizacion: implemente aqui

    public void printTree(){
        System.out.print(render(false));
    }

    private String render(boolean withBalance) {
        if (root == null) {
            return "(arbol vacio)\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(raiz) ").append(describe(root, withBalance)).append("\n");
        render(root, "", withBalance, sb);
        return sb.toString();
    }

    private void render(TreeNode node, String prefix, boolean withBalance, StringBuilder sb) {
        List<TreeNode> kids = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        if (node.left != null) {
            kids.add(node.left);
            labels.add("(Izq) ");
        }
        if (node.right != null) {
            kids.add(node.right);
            labels.add("(Der) ");
        }
        for (int i = 0; i < kids.size(); i++) {
            boolean last = (i == kids.size() - 1);
            sb.append(prefix).append(last ? "`-- " : "|-- ")
                    .append(labels.get(i)).append(describe(kids.get(i), withBalance)).append("\n");
            render(kids.get(i), prefix + (last ? "    " : "|   "), withBalance, sb);
        }
    }

    private String describe(TreeNode node, boolean withBalance) {
        return withBalance ? node.data + " [FE=" + balanceFactor(node) + "]"
                : String.valueOf(node.data);
    }


    /*3. Recorridos: implemente aqui. Cada recorrido debe ser una funcion
    RECURSIVA independiente (por ejemplo, un metodo privado que reciba un
    TreeNode). Los metodos publicos devuelven la secuencia separada por
    espacios, p.ej. "A B D H I E C F G".*/

    public String preorder(){
        StringBuilder sb = new StringBuilder();
        preorder(root, sb);
        return sb.toString().trim();
    }

    private void preorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        sb.append(node.data).append(" ");
        preorder(node.left, sb);
        preorder(node.right, sb);
    }
    public String inorder(){
        StringBuilder sb = new StringBuilder();
        inorder(root, sb);
        return sb.toString().trim();
    }

    private void inorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inorder(node.left, sb);
        sb.append(node.data).append(" ");
        inorder(node.right, sb);
    }
    public String postorder(){
        StringBuilder sb = new StringBuilder();
        postorder(root, sb);
        return sb.toString().trim();
    }

    private void postorder(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        postorder(node.left, sb);
        postorder(node.right, sb);
        sb.append(node.data).append(" ");
    }

    //4. Factores de equilibrio: implemente aqui

    public int height(TreeNode node){
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }


    public int balanceFactor(TreeNode node){
        if (node == null) {
            return 0;
        }
        return height(node.right) - height(node.left);
    }

    public boolean isBalanced(){
        return isBalanced(root);
    }

    private boolean isBalanced(TreeNode node) {
        if (node == null) {
            return true;
        }
        int bf = balanceFactor(node);
        return bf >= -1 && bf <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }

    public List<Object> unbalancedNodes(){
        List<Object> result = new ArrayList<>();
        collectUnbalanced(root, result);
        return result;
    }

    private void collectUnbalanced(TreeNode node, List<Object> result) {
        if (node == null) {
            return;
        }
        collectUnbalanced(node.left, result);
        int bf = balanceFactor(node);
        if (bf < -1 || bf > 1) {
            result.add(node.data);
        }
        collectUnbalanced(node.right, result);
    }

    public void printBalanceFactors(){
        System.out.print(render(true));
    }

    /*5. Balanceo AVL: implemente aqui. Cada rotacion recibe la raiz del
    subarbol desbalanceado y devuelve la NUEVA raiz de ese subarbol.*/

    //rotacion simple a la derecha
    public TreeNode rotateRight(TreeNode node){
        TreeNode pivot = node.right;
        node.right = pivot.left;
        pivot.left = node;
        return pivot;
    }

    // rotacion simple a la izquierda
    public TreeNode rotateLeft(TreeNode node){
        TreeNode pivot = node.right;
        node.right = pivot.left;
        pivot.left = node;
        return pivot;
    }

    //rotacion doble izquierda-derecha
    public TreeNode rotateLeftRight(TreeNode node){
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    //rotacion doble derecha-izquierda
    public TreeNode rotateRightLeft(TreeNode node){
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    public void balance(){
        System.out.println("--- Antes de balancear ---");
        printBalanceFactors();
        List<Object> bad = unbalancedNodes();
        if (bad.isEmpty()) {
            System.out.println("El arbol ya esta balanceado (todos los FE estan entre -1 y 1).");
            return;
        }
        System.out.println("Nodos desbalanceados: " + bad);

        root = balanceNode(root);

        System.out.println("--- Despues de balancear ---");
        printBalanceFactors();
        System.out.println("Arbol balanceado: " + isBalanced());
    }
    private TreeNode balanceNode(TreeNode node) {
        if (node == null) {
            return null;
        }
        node.left = balanceNode(node.left);
        node.right = balanceNode(node.right);

        int bf = balanceFactor(node);
        if (bf < -1) {                                   // pesado a la izquierda
            if (balanceFactor(node.left) <= 0) {
                System.out.println("Rotacion simple a la derecha en " + node.data);
                return rotateRight(node);
            }
            System.out.println("Rotacion doble izquierda-derecha en " + node.data);
            return rotateLeftRight(node);
        }
        if (bf > 1) {                                    // pesado a la derecha
            if (balanceFactor(node.right) >= 0) {
                System.out.println("Rotacion simple a la izquierda en " + node.data);
                return rotateLeft(node);
            }
            System.out.println("Rotacion doble derecha-izquierda en " + node.data);
            return rotateRightLeft(node);
        }
        return node;
    }
}
