# part 1
open(F,'<','in01');
$%+=sprintf"%d%d",/(\d)/,/.*(\d)/ for<F>;print$%;

# part 2
print$/;
open(F,'<','in01');
# 'two' first to avoid 'twone' situation.
%m=('one'=>1,'two'=>2,'three'=>3,'four'=>4,'five'=>5,'six'=>6,'seven'=>7,'eight'=>8,'nine'=>9);
$m{$_}=$_ for 1..9;
$j=join'|',keys%m;
$}+=sprintf"%d%d",map$m{$_},/(\d|$j)/,/.*(\d|$j)/ for<F>;print$};