import org.junit.Before;
import org.junit.Test;

import java.util.List;

import dungeon.Dungeon;
import dungeon.DungeonImpl;
import location.Location;
import randomizer.MockRandomizer;
import randomizer.RandomGenerator;

import static org.junit.Assert.assertEquals;

/**
 * Test class to check all the implementation of the mock random generator.
 */
public class MockRandomizerTest {

  private RandomGenerator fixedRandGenerator;

  @Before
  public void setUp() throws Exception {
    fixedRandGenerator = new MockRandomizer(2);
  }

  @Test
  public void getNextInt() {
    assertEquals(2, fixedRandGenerator.getNextInt(2, 4));
  }

  @Test
  public void shuffleList() {
    Dungeon dWrap = new DungeonImpl("Harry", 4, 5, 2,
            30, true, fixedRandGenerator, 5);
    List<Location> caveLi = dWrap.getCavesList();
    fixedRandGenerator.shuffleList(caveLi);
    assertEquals(dWrap.getCavesList(), caveLi);
  }

}