no warnings;
open(F,'<','in06');
$l=14;

$_=<F>;/.{$l}/,%h=(),($&=~s/./$h{$&}++/reg,-$l+keys%h||die$%+14),$%+=s/.// while$_