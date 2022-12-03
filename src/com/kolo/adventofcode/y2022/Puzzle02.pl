open(F,'<','in02');

# part 1
#/ /,$}=ord($')%87,$d=$}-ord($`)%64+1,$s+=$d%3*3+$}for<F>;print$s

# part 2
/ /,$o=ord($`)%64,$m=($o+ord$')%3+1,$s+=$m+3*ord($')%88 for<F>;print$s
