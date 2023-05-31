package dungeon;

/**
 * Edge of the graph which represents a dungeon.
 * Code by: https://www.techiedelight.com/kruskals-algorithm-for-finding-minimum-spanning-tree/
 */
public class Edge {
  private int src;
  private int dest;
  private int weight;

  Edge(int src, int dest, int weight) {
    this.src = src;
    this.dest = dest;
    this.weight = weight;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", src, dest);
  }

  int getSrc() {
    return src;
  }

  int getDest() {
    return dest;
  }

  int getWeight() {
    return weight;
  }

}
