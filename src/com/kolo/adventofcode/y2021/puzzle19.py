import os
import re

input = open(os.path.dirname(os.path.abspath(__file__)) + '/in19', 'r').read()
#matches = re.findall("--- scanner \d+ ---\n[^-]+", input)
scanners = re.split("--- scanner \d+ ---", input)
scanners = [re.findall("\S+", r) for r in scanners][1:]
scanners = [[list(map(int, re.findall("-?\d+", coord))) for coord in scanner] for scanner in scanners]
scannerDiffs = []
for i in range(len(scanners)):
  scanner = scanners[i]
  scannerDiffs.append(set())
  for c1i in range(len(scanner)):
    for c2i in range(c1i + 1, len(scanner)):
      c1 = scanner[c1i]
      c2 = scanner[c2i]
      scannerDiffs[-1].add(tuple([abs(c1[ci] - c2[ci]) for ci in range(len(c1))]))
# print(scanners)
print(scannerDiffs[0])
print(scannerDiffs[1])
# print(scannerDiffs[0] == scannerDiffs[1])
for i in range(len(scannerDiffs)):
  for j in range(i + 1, len(scannerDiffs)):
    print(i, j, len(scannerDiffs[i].intersection(scannerDiffs[j])))
