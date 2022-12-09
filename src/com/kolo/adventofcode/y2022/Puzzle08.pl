open(F,'<','in08');

$ROWS = 0;
for(<F>){
	$ROWS++;
	chomp;
	push@_,[split//,$_];
	push@v,[];
	push@pv,[];
	$COLS = length;
	push(@{$v[-1]}, 0) for 1..length;
	push(@{$pv[-1]}, 1) for 1..length;
}

$part1 = 0;
$part2 = 0;

# part1 left-right
for$ri(0..$ROWS - 1){
	$max = -1;
	for $ci(0..$COLS - 1) {
		$v=$_[$ri]->[$ci];
		if ($v > $max) {
			$part1 += !$v[$ri]->[$ci];
			$v[$ri]->[$ci] = 1;
			$max = $v;
		}
	}

	$max = -1;
	for $ci (reverse(0..$COLS - 1)) {
		$v=$_[$ri]->[$ci];
		if ($v > $max) {
			$part1 += !$v[$ri]->[$ci];
			$v[$ri]->[$ci] = 1;
			$max = $v;
		}
	}
}
# part1 up-down
for $ci (0..$COLS - 1){
	$max = -1;
	for $ri(0..$ROWS - 1) {
		$v=$_[$ri]->[$ci];
		if ($v > $max) {
			$part1 += !$v[$ri]->[$ci];
			$v[$ri]->[$ci] = 1;
			$max = $v;
		}
	}

	$max = -1;
	for $ri (reverse(0..$ROWS - 1)) {
		$v=$_[$ri]->[$ci];
		if ($v > $max) {
			$part1 += !$v[$ri]->[$ci];
			$v[$ri]->[$ci] = 1;
			$max = $v;
		}
	}
}

# part2 left-right
for$ri(0..$ROWS - 1){
	@stack = ();
	for $ci(0..$COLS - 1) {
		$v=$_[$ri]->[$ci];
		$point_vis = 0;
		while (~~@stack && $v > $stack[-1]->[0]) {
			$other_point_vis = pop@stack;
			$point_vis += $other_point_vis->[1] + 1;
		}
		$pv[$ri]->[$ci] *= $point_vis + (~~@stack?1:0);
		push@stack,[$v, $point_vis];
	}

	@stack = ();
	for $ci(reverse(0..$COLS - 1)) {
		$v=$_[$ri]->[$ci];
		$point_vis = 0;
		while (~~@stack && $v > $stack[-1]->[0]) {
			$other_point_vis = pop@stack;
			$point_vis += $other_point_vis->[1] + 1;
		}
		$pv[$ri]->[$ci] *= $point_vis + (~~@stack?1:0);
		push@stack,[$v, $point_vis];
	}
}

# part2 up-down
for $ci (0..$COLS - 1){
	@stack = ();
	for $ri(0..$ROWS - 1) {
		$v=$_[$ri]->[$ci];
		$point_vis = 0;
		while (~~@stack && $v > $stack[-1]->[0]) {
			$other_point_vis = pop@stack;
			$point_vis += $other_point_vis->[1] + 1;
		}
		$pv[$ri]->[$ci] *= $point_vis + (~~@stack?1:0);
		push@stack,[$v, $point_vis];
	}

	@stack = ();
	for $ri (reverse(0..$ROWS - 1)) {
		$v=$_[$ri]->[$ci];
		$point_vis = 0;
		while (~~@stack && $v > $stack[-1]->[0]) {
			$other_point_vis = pop@stack;
			$point_vis += $other_point_vis->[1] + 1;
		}
		$pv[$ri]->[$ci] *= $point_vis + (~~@stack?1:0);
		push@stack,[$v, $point_vis];
		if ($pv[$ri]->[$ci] > $part2) {
			$part2 = $pv[$ri]->[$ci];
		}
	}
}

print "$part1\n$part2\n";