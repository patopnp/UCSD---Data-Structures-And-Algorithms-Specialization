import java.util.*;

public class Dijkstra {
    private long distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {

        int[] dist = dijkstra(adj, cost, s);
        if (dist[t] == Integer.MAX_VALUE)
            dist[t] = -1;
        return dist[t];
    }

    class MinHeapPriorityQueue {
        private Node[] heap;
        private int[] keyToHeapIndex;
        private int size;

        public MinHeapPriorityQueue(int capacity) {
            heap = new Node[capacity];
            keyToHeapIndex = new int[capacity];
            size = 0;
        }

        public void printHeap() {
            for (Node element : heap) {
                System.out.println(element.key + " " + element.dist);
            }
        }

        public void insert(int key, int dist) {
            if (size == heap.length) {
                resizeHeap();
            }
            heap[size] = new Node(key, dist);
            size++;
            keyToHeapIndex[key] = size - 1;
            swim(size - 1);
        }

        public Node removeMin() {
            if (isEmpty()) {
                throw new IllegalStateException("Priority queue is empty");
            }
            Node max = heap[0];
            swap(0, size - 1);
            heap[size - 1] = null;
            size--;
            sink(0);
            return max;
        }

        public void changePriority(int key, int newDist) {
            int index = keyToHeapIndex[key];
            if (heap[index] != null) {
                int oldDist = heap[index].dist;
                heap[index].dist = newDist;
                if (oldDist > newDist) {
                    swim(index);
                } else {
                    sink(index);
                }
            }
        }

        public boolean isElementOnTheQueue(int key){
            return heap[keyToHeapIndex[key]] != null;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        private void swim(int k) {
            while (k > 0 && heap[parent(k)].dist > heap[k].dist) {
                swap(k, parent(k));
                k = parent(k);
            }
        }

        private void sink(int k) {
            while (leftChild(k) < size) {
                int j = leftChild(k);
                if (j < size - 1 && heap[j].dist > heap[j + 1].dist) {
                    j++;
                }
                if (heap[k].dist <= heap[j].dist) {
                    break;
                }
                swap(k, j);
                k = j;
            }
        }

        private void swap(int i, int j) {
            Node temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
            keyToHeapIndex[heap[i].key] = i;
            keyToHeapIndex[heap[j].key] = j;
        }

        private void resizeHeap() {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }

        private int parent(int i) {
            return (i - 1) / 2;
        }

        private int leftChild(int i) {
            return 2 * i + 1;
        }

        private int findNodeIndex(int key) {
            for (int i = 0; i < size; i++) {
                if (heap[i].key == key) {
                    return i;
                }
            }
            return -1;
        }

    }

    class Node {
        int key;
        int dist;

        public Node(int key, int dist) {
            this.key = key;
            this.dist = dist;
        }
    }

    public int[] dijkstra(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s) {

        int[] dist = new int[adj.length];
        int[] prev = new int[adj.length];

        MinHeapPriorityQueue h = new MinHeapPriorityQueue(adj.length);
        //ArrayList<Node> nodes = new ArrayList<Node>(adj.length);

        for (int i = 0; i < adj.length; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            h.insert(i, dist[i]);
        }

        dist[s] = 0;
        h.changePriority(s, 0);

        while (h.isEmpty() == false) {
            Node u = h.removeMin();

            for (int i = 0; i < adj[u.key].size(); i++)
            {
                if (dist[u.key] != Integer.MAX_VALUE) {
                    if (dist[adj[u.key].get(i)] > dist[u.key] + cost[u.key].get(i)) {
                        dist[adj[u.key].get(i)] = dist[u.key] + cost[u.key].get(i);
                        prev[adj[u.key].get(i)] = u.key;
                        h.changePriority(adj[u.key].get(i), dist[adj[u.key].get(i)]);
                    }
                }
            }
        }
        
        return dist;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        Dijkstra shortestPathAlgorithm = new Dijkstra();
        System.out.println(shortestPathAlgorithm.distance(adj, cost, x, y));
    }
}

