package randomizer;


/**
 * It represents the randomizer. All methods generate random values or used for testing.
 */
public interface RandomGenerator<T> {

  /**
   * Generates a random number from given range.
   */
  int getNextInt(int min, int max);

  /**
   * Generates a random number from given range.
   */
  int getVal(int min, int max);

  /**
   * Shuffles a list.
   */
  void shuffleList(T arraylist);
}
