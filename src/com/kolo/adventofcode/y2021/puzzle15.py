from heapq import *
import os

def shortestPathRisk(grid, start):
    queue = []
    heappush(queue, (0, *start))
    seen = set([start])
    height = len(grid)
    width = len(grid[0])
    while queue:
        weight, x, y = heappop(queue)
        if y == height - 1 and x == width - 1:
            return weight
        for x2, y2 in ((x+1,y), (x-1,y), (x,y+1), (x,y-1)):
            if 0 <= x2 < width and 0 <= y2 < height and (x2, y2) not in seen:
                heappush(queue, (weight + grid[y2][x2], x2, y2))
                seen.add((x2, y2))

lines = open(os.path.dirname(os.path.abspath(__file__)) + '/in15', 'r').read().split("\n")
grid = [list(map(int, line)) for line in lines]

print(shortestPathRisk(grid, (0, 0)))

for i in range(0, len(grid)):
  orig = grid[i].copy()
  for r in range(1, 5):
    grid[i].extend([(v+r)%9 or 9 for v in orig])
orig = grid.copy()
for r in range(1, 5):
  grid.extend([list(map(lambda v: (v+r)%9 or 9, line)) for line in orig])

print(shortestPathRisk(grid, (0, 0)))