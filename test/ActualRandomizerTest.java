import org.junit.Before;
import org.junit.Test;

import java.util.List;

import dungeon.Dungeon;
import dungeon.DungeonImpl;
import location.Location;
import randomizer.ActualRandomizer;
import randomizer.RandomGenerator;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test class to check all the implementation of the random class used to
 * generate random number values.
 */
public class ActualRandomizerTest {

  private RandomGenerator randomGenerator;

  @Before
  public void setUp() throws Exception {
    randomGenerator = new ActualRandomizer();
  }

  @Test
  public void getNextInt() {
    assertTrue(2 <= randomGenerator.getNextInt(2, 4)
            && randomGenerator.getNextInt(2, 4) <= 4);
  }

  @Test
  public void shuffleList() {
    Dungeon dWrap = new DungeonImpl("Harry", 4, 5,
            2, 30, true, randomGenerator, 5);
    List<Location> caveLi = dWrap.getCavesList();
    randomGenerator.shuffleList(caveLi);
    assertNotEquals(dWrap.getCavesList(), caveLi);
  }

}