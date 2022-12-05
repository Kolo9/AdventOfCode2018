open(F,'<','in04');

# part 1
#($a,$},$c,$d)=/\d+/g,$%+=$a<=$c&&$}>=$d||$c<=$a&&$d>=$}for<F>;print$%

# part 2
($a,$},$c,$d)=/\d+/g,$%+=$a<=$d&&$}>=$d||$c<=$}&&$d>=$}for<F>;print$%
