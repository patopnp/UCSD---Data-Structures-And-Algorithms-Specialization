import java.util.*;

public class ConnectingPoints {

    private double minimumDistance(int[] x, int[] y) {
        double result = 0.;
        //write your code here
	int n = x.length;
    ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
    ArrayList<Double>[] cost = (ArrayList<Double>[]) new ArrayList[n];

	for (int i = 0; i < n; i++) {
		adj[i] = new ArrayList<Integer>();
		cost[i] = new ArrayList<Double>();
        for (int j = 0; j < n; j++) {
            if (i == j) continue;
            double distanceToPointSquared = ((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
            double distanceToPoint = Math.sqrt(distanceToPointSquared);
			adj[i].add(j);
			cost[i].add(distanceToPoint);
		}
	}
	    result = prim(adj, cost);
        return result;
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

        public void insert(int key, double dist) {
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

        public void changePriority(int key, double newDist) {
            int index = keyToHeapIndex[key];
            if (heap[index] != null) {
                double oldDist = heap[index].dist;
                heap[index].dist = newDist;
                if (oldDist > newDist) {
                    swim(index);
                } else {
                    sink(index);
                }
            }
        }

        public boolean isElementInTheQueue(int key) {
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
        double dist;

        public Node(int key, double dist) {
            this.key = key;
            this.dist = dist;
        }
    }

    public double prim(ArrayList<Integer>[] adj, ArrayList<Double>[] cost) {

        double totalDistance = 0;
        double[] dist = new double[adj.length];

        MinHeapPriorityQueue h = new MinHeapPriorityQueue(adj.length);

        for (int i = 0; i < adj.length; i++) {
            dist[i] = Double.MAX_VALUE;
            h.insert(i, dist[i]);
        }

        // s is a random vertex in the queue
        int s = 0;
        dist[s] = 0;
        h.changePriority(s, 0);

        while (h.isEmpty() == false) {
            Node u = h.removeMin();
            totalDistance += dist[u.key];
            for (int i = 0; i < adj[u.key].size(); i++) {
                if (h.isElementInTheQueue(adj[u.key].get(i)) && dist[adj[u.key].get(i)] > cost[u.key].get(i)) {
                    dist[adj[u.key].get(i)] = cost[u.key].get(i);
                    h.changePriority(adj[u.key].get(i), dist[adj[u.key].get(i)]);
                }
            }
        }

        return totalDistance;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
        }
        ConnectingPoints minimumSpanningTreeAlgorithm = new ConnectingPoints();
        System.out.println(minimumSpanningTreeAlgorithm.minimumDistance(x, y));
    }
}
