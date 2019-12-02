import re

bots = map(lambda s : list(map(int, re.findall(r'-?\d+', s))), open('puzzle23', 'r').read().split('\n'))
bots = [((b[0], b[1], b[2]), b[3]) for b in bots]


def myabs(i):
  return If(i < 0, -i, i)    

from z3 import *

##(x, y, z) = (Int('x'), Int('y'), Int('z'))
##o = Optimize()
##in_range_bits = []
##for i in range(len(bots)):
##    in_range_bits.append(Int('in_range_bit' + str(i)))
##    (bx, by, bz), br = bots[i]
##    o.add(in_range_bits[i] == If(myabs(x - bx) + myabs(y - by) + myabs(z - bz) <= br, 1, 0))
##in_range_count = Int('in_range_count')
##o.add(in_range_count == sum(in_range_bits))
##dist = Int('dist')
##o.add(dist == myabs(x) + myabs(y) + myabs(z))
##z = o.maximize(in_range_count)
##z = o.minimize(dist)
o = Optimize()
x = Int('x')
y = Int('y')
o.add(y == myabs(x) - 3)
z1 = o.minimize(y)
z2 = o.maximize(x)
o.check()
print(o.lower(z1), o.upper(z1))
print(o.lower(z2), o.upper(z2))
