package dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Kruskal's algorithm creates dungeon.
 * Code by: https://www.techiedelight.com/kruskals-algorithm-for-finding-minimum-spanning-tree/
 */
public class Kruskal {

  private final Map<Integer, Integer> parent = new HashMap<>();

  private void makeSet(int n) {
    for (int i = 0; i < n; i++) {
      parent.put(i, i);
    }
  }

  private int find(int k) {
    if (parent.get(k) == k) {
      return k;
    }
    return find(parent.get(k));
  }

  private void union(int a, int b) {
    int x = find(a);
    int y = find(b);

    parent.put(x, y);
  }

  // Function to construct MST using Kruskal’s algorithm
  static List<Edge> kruskalAlgo(List<Edge> edges, int n) {
    List<Edge> mst = new ArrayList();

    Kruskal k = new Kruskal();
    k.makeSet(n);

    int index = 0;
    Collections.sort(edges, Comparator.comparingInt(e -> e.getWeight()));

    while (mst.size() != n - 1) {
      Edge next_edge = edges.get(index++);

      int x = k.find(next_edge.getSrc());
      int y = k.find(next_edge.getDest());

      if (x != y) {
        mst.add(next_edge);
        k.union(x, y);
      }
    }
    return mst;
  }
}
