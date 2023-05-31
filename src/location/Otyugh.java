package location;

/**
 * This represents an Otyugh creature which has a health attribute.
 */
public class Otyugh implements Creature {
  private int health;

  /**
   * Constructor of the Otyugh which sets the health.
   */
  public Otyugh() {
    this.health = 100;
  }

  /**
   * Copy Constructor of the Otyugh.
   */
  Otyugh(Creature c) {
    this.health = c.getHealth();
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public void hit() {
    health = health - 50;
  }
}
