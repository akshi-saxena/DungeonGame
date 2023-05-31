package randomizer;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class represents the randomizer class which implements all the methods
 * of the RandomGenerator interface.
 */
public class ActualRandomizer<T> implements RandomGenerator<T> {

  @Override
  public int getNextInt(int min, int max) {
    Random rn = new Random();
    return rn.nextInt(max - min) + min;
  }

  @Override
  public int getVal(int min, int max) {
    return min + (int) (Math.random() * ((max - min) + 1));
  }

  @Override
  public void shuffleList(T list) {
    T newList = list;
    Collections.shuffle((List) newList);
  }

}
