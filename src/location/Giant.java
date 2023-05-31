package location;

/**
 * This represents a moving creature Giant which has a health and location.
 */
public class Giant implements Creature {

  private int health;
  private Location loc;

  public Giant(Location start) {
    this.loc = start;
    this.health = 100;
  }

  /**
   * Copy Constructor of the Otyugh.
   */
  public Giant(Creature c) {
    this.health = c.getHealth();
    this.loc = c.getLoc();
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public void hit() {
    this.health = 0;
  }

  @Override
  public Location getLoc() {
    return this.loc;
  }

  @Override
  public void setLoc(Location l) {
    this.loc = l;
  }
}
