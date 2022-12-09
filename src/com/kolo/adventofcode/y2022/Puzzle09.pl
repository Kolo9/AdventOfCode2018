no warnings;
open(F,'<','in09');

## part 1
#$TAIL_COUNT = 1;
# part 2
$TAIL_COUNT = 9;

@head=(0, 0);
@tails=();
for (1..$TAIL_COUNT) {
	push@tails,[0,0];
}
for (<F>) {
	/(.) (\d+)/;
	for (1..$2) {
		if ($1 eq 'U') {
			$head[1]++;
		} elsif ($1 eq 'D') {
			$head[1]--;
		} elsif ($1 eq 'R') {
			$head[0]++;
		} else {
			$head[0]--;
		}
		for $i (0..$TAIL_COUNT - 1) {
			$h=$i? $tails[$i-1] : \@head;
			$t=$tails[$i];
			$move_h = $h->[0] - $t->[0];
			$move_v = $h->[1] - $t->[1];

			if (abs($move_h) > 1 || abs($move_v) > 1) {
				$t->[0] += $move_h ? $move_h > 0 ? 1 : -1 : 0;
				$t->[1] += $move_v ? $move_v > 0 ? 1 : -1 : 0;
			}
			
			if ($i == $TAIL_COUNT - 1) {
				$v{$t->[0] * 1e6 + $t->[1]} = 1;
			}
		}
	}
}
print ~~ keys%v;