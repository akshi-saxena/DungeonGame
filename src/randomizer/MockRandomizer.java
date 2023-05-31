package randomizer;

/**
 * This class represents the mock randomizer which implements all the methods
 * of the interface RandomGenerator and is used for testing of the dungeon.
 */
public class MockRandomizer<T> implements RandomGenerator<T> {

  private int[] arr;
  private int index;


  /**
   * Constructs a mock random generator by taking varargs as the input.
   *
   * @param arr Varargs as integers
   */
  public MockRandomizer(int... arr) {
    this.arr = arr;
    this.index = 0;
  }

  @Override
  public int getNextInt(int min, int max) {
    int val = arr[index++];
    index = index >= arr.length ? 0 : index;
    return val;
  }

  @Override
  public int getVal(int min, int max) {
    return (min + max) / 2;
  }

  @Override
  public void shuffleList(T list) {
    T newList = list;
  }

}
