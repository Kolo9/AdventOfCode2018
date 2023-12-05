open(F,'<','in04');
$c{$_}++for 0..192;
for(<F>) {
	%h=();
	/((?:\d+ +)+)\|((?: +\d+)+)/;
	$w=$1;$c=$2;
	$h{0|$_}++for$w=~/\d+/g;
	$m= ~~grep{$h{0|$_}}$c=~/\d+/g;
	$f+=2**($m-1);
	$c{$_}+=$c{$%}for$%+1..$%+$m;
	$%++
}
print$f;
print$/;
print eval join'+',values%c;