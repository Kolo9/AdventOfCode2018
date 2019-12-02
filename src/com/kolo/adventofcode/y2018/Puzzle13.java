package com.kolo.adventofcode.y2018;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Puzzle13 {	
	private static final class Track {
		Map<Direction, Boolean> moves = new HashMap<>();
		Boolean isIntersection = null;

		boolean isIntersection() {
			if (isIntersection == null) {
				isIntersection = moves.get(Direction.UP) && moves.get(Direction.DOWN) && moves.get(Direction.RIGHT) && moves.get(Direction.LEFT);
			}
			return isIntersection;
		}

		 @Override
	        public String toString() {
	            if (moves.get(Direction.UP)) {
	                if (moves.get(Direction.DOWN)) {
	                    if (moves.get(Direction.LEFT)) {
	                        if (moves.get(Direction.RIGHT)) {
	                            return "+";
	                        } else {
	                            return "ERROR1";
	                        }
	                    } else if (moves.get(Direction.RIGHT)) {
	                        return "ERROR2";
	                    } else {
	                        return "|";
	                    }
	                } else if (moves.get(Direction.LEFT)) {
	                    if (moves.get(Direction.RIGHT)) {
	                        return "ERROR3";
	                    } else {
	                        return "/";
	                    }
	                } else if (moves.get(Direction.RIGHT)) {
	                    return "\\";
	                } else {
	                    return "ERROR4";
	                }
	            } else if (moves.get(Direction.DOWN)) {
	                if (moves.get(Direction.LEFT)) {
	                    if (moves.get(Direction.RIGHT)) {
	                        return "ERROR5";
	                    } else {
	                        return "\\";
	                    }
	                } else if (moves.get(Direction.RIGHT)) {
	                    return "/";
	                } else {
	                    return "ERROR6";
	                }
	            } else if (moves.get(Direction.LEFT)) {
	                if (moves.get(Direction.RIGHT)) {
	                    return "-";
	                } else {
	                    return "ERROR7";
	                }
	            } else if (moves.get(Direction.RIGHT)) {
	                return "X";
	            } else {
	                return " ";
	            }
	        }
	}

	private static enum Direction {
		UP, RIGHT, DOWN, LEFT;
	}

	private static class Cart {
		boolean dead = false;
		Direction direction;
		int nextIntersection = -1;
		int x;
		int y;

		Cart moveAndCheckCollision(Track[][] track, List<Cart> carts) {
			Track cur = track[y][x];
			Direction moveDirection = null;
			if (cur.isIntersection()) {
				int newOrd = ((direction.ordinal() + nextIntersection) + direction.values().length) % direction.values().length;
				moveDirection = Direction.values()[newOrd];
				nextIntersection++;
				if (nextIntersection == 2) {
					nextIntersection = -1;
				}
			} else if (cur.moves.get(direction)) {
				moveDirection = direction;
			} else {
				for (Direction d : Direction.values()) {
					if (d == direction || (Math.abs(d.ordinal() - direction.ordinal()) == 2) || !cur.moves.get(d)) {
						continue;
					}
					moveDirection = d;
					break;
				}
			}
			if (moveDirection == null) {
				System.err.println("OH NO!!!");
				System.exit(1);
			}
			direction = moveDirection;

			switch (moveDirection) {
				case UP:
					y--;
					break;
				case DOWN:
					y++;
					break;
				case LEFT:
					x--;
					break;
				case RIGHT:
					x++;
					break;
			}

			for (Cart other : carts) {
				if (this == other) {
					continue;
				}
				if (other.x == x && other.y == y) {
					return other;
				}
			}
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		List<Character> slashes = Arrays.asList('\\', '/');
		List<String> ss = Files.readAllLines(Paths.get("puzzle13"));
		Track[][] track = new Track[ss.size()][ss.get(0).length()];
		List<Cart> carts = new ArrayList<>();
		int z = 0;
		for (String s : ss) {
			for (int i = 0; i < s.length(); i++) {
				track[z][i] = new Track();
				char c = s.charAt(i);
				track[z][i].moves.put(Direction.UP, (z != 0 && c != ' ' && c != '-' && ss.get(z-1).charAt(i) != ' ' && ss.get(z-1).charAt(i) != '-' && !(slashes.contains(c) && slashes.contains(ss.get(z-1).charAt(i)))));
				track[z][i].moves.put(Direction.DOWN, (z != ss.size() - 1 && c != ' ' && c != '-' && ss.get(z+1).charAt(i) != ' ' && ss.get(z+1).charAt(i) != '-' && !(slashes.contains(c) && slashes.contains(ss.get(z+1).charAt(i)))));
				track[z][i].moves.put(Direction.LEFT, (i != 0 && c != ' ' && c != '|' && s.charAt(i-1) != ' ' && s.charAt(i-1) != '|' && !(slashes.contains(c) && slashes.contains(s.charAt(i-1)))));
				track[z][i].moves.put(Direction.RIGHT, (i != s.length() - 1 && c != ' ' && c != '|' && s.charAt(i+1) != ' ' && s.charAt(i+1) != '|' && !(slashes.contains(c) && slashes.contains(s.charAt(i+1)))));
				if (c == 'v') {
					Cart cart = new Cart();
					cart.direction = Direction.DOWN;
					cart.x = i;
					cart.y = z;
					carts.add(cart);
				} else if (c == '^') {
					Cart cart = new Cart();
					cart.direction = Direction.UP;
					cart.x = i;
					cart.y = z;
					carts.add(cart);
				} else if (c == '>') {
					Cart cart = new Cart();
					cart.direction = Direction.RIGHT;
					cart.x = i;
					cart.y = z;
					carts.add(cart);
				} else if (c == '<') {
					Cart cart = new Cart();
					cart.direction = Direction.LEFT;
					cart.x = i;
					cart.y = z;
					carts.add(cart);
				}
			}
			z++;
		}

		int y = -1;
	    StringBuilder fileOutput = new StringBuilder();

	    System.out.println(carts.size() + " carts.");

		while(carts.size() > 1) {
			y++;
//			System.out.println(y);
			fileOutput.append("" + y + "\n");
			for (int i = 0; i < track.length; i++) {
				for (int j = 0; j < track[i].length; j++) {
					boolean hasCart = false;
					for (Cart cart : carts) {
						if (cart.x == j && cart.y == i) {
							switch (cart.direction) {
								case UP:
									fileOutput.append("^");
									break;
								case DOWN:
									fileOutput.append("v");
									break;
								case LEFT:
									fileOutput.append("<");
									break;
								case RIGHT:
									fileOutput.append(">");
									break;
							}
							hasCart = true;
						}
					}
					if (!hasCart) {
						fileOutput.append(track[i][j] + "");
					}
				}
				fileOutput.append("\n");
			}
			Collections.sort(carts, (c1, c2) -> {if (c1.y == c2.y) {return c1.x - c2.x;} else {return c1.y - c2.y;}});
			for (Cart cart : carts) {
//				System.out.println("Cart at (" + cart.x + ", " + cart.y + ")");
				Cart collisionCart = cart.moveAndCheckCollision(track, carts);
				if (collisionCart != null) {
					System.out.println("Collision at (" + cart.x + ", " + cart.y + ")");
					cart.dead = true;
					collisionCart.dead = true;
					System.out.println(cart.x + "," + cart.y);
//					PrintWriter out = new PrintWriter("out13");
//					out.println(fileOutput);
//					out.close();
//					System.exit(0);
				}
			}
			carts.removeIf(c -> c.dead);
//			System.out.println();
//			System.out.println();
		}
		System.out.println(carts.get(0).x + "," + carts.get(0).y);
		
	}

}