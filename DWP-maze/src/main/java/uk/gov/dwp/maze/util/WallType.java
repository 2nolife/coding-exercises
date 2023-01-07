package uk.gov.dwp.maze.util;

public enum WallType {
  WALL('X'), EMPTY(' '), START('S'), EXIT('E');

  private Character character;

  WallType(Character character) {
    this.character = character;
  }

  public Character getCharacter() {
    return character;
  }

  public static WallType fromCharacter(Character c) {
    switch (c) {
      case 'X': return WALL;
      case 'S': return START;
      case 'E': return EXIT;
      default: return EMPTY;
    }
  }
}
