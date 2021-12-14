import os
import numpy as np

def foldY(grid, y):
  top = grid[:y]
  bottom = grid[y+1:]
  return top | np.flipud(bottom)

def foldX(grid, x):
  left = np.array([line[:x] for line in grid])
  right = np.array([line[x+1:] for line in grid])
  return left | np.fliplr(right)

lines = open(os.path.dirname(os.path.abspath(__file__)) + '/in13', 'r').read().split("\n")
maxX = maxY = -1
points = []
instructions = []
parsingPoints=1
for line in lines:
  if not line:
    parsingPoints=0
    continue
  if parsingPoints:
    x,y = line.split(',')
    x=int(x)
    y=int(y)
    maxX = max(maxX, x)
    maxY = max(maxY, y)
    points.append((x, y))
  else:
    type,value = line.split('=')
    value = int(value)
    instructions.append((type, value))


grid = np.zeros((maxY + 1, maxX + 1), dtype=bool)
for (x, y) in points:
  grid[y][x] = True

for instruction in instructions:
  print(instruction)
  if 'y' in instruction[0]:
    grid = foldY(grid, instruction[1])
  else:
    grid = foldX(grid, instruction[1])
  print(np.count_nonzero(grid))
print("\n".join(["".join(map(lambda l: '|' if l else ' ', line)) for line in grid]))