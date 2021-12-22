import os
import re

input = open(os.path.dirname(os.path.abspath(__file__)) + '/in17', 'r').read()
matches = re.findall("=(\S+)\.\.(\S*\d)", input)
trenchX = tuple(map(int, matches[0]))
trenchY = tuple(map(int, matches[1]))
# print(trenchX)
# print(trenchY)
# minX = min(0, trenchX[0])
# minY = min(0, trenchY[0])
maxX = max(0, trenchX[1])
# maxY = max(0, trenchY[1])

validVelocities = []
# ¯\_(ツ)_/¯
for dyInit in range(-1000, 1000):
  for dxInit in range(0, maxX + 1):
    dx = dxInit
    dy = dyInit
    pos = [0, 0]
    # found = False
    while True:
      if pos[0] > trenchX[1]:
        break
      if pos[0] + (dx * (dx + 1) / 2) < trenchX[0]:
        break
      if pos[1] < trenchY[0] and dy < 0:
        break
      if trenchX[0] <= pos[0] <= trenchX[1] and trenchY[0] <= pos[1] <= trenchY[1]:
        # found = True
        validVelocities.append((dxInit, dyInit))
        break
      pos[0] += dx
      pos[1] += dy
      dx -= 1 if dx > 0 else 0
      dy -= 1
    # if found:
    #   break
print(validVelocities[-1][1] * (validVelocities[-1][1] + 1) // 2)
print(len(validVelocities))