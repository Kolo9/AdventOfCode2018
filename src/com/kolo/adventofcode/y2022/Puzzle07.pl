open(F,'<','in07');

%root = ();
@cur = ();
for(<F>) {
	if(/^\$/) {
		if (/cd (.*)/) {
			if ($1 eq '/') {
				@cur = ();
			} elsif($1 eq '..') {
				pop@cur;
			} else {
				push@cur, $1;
			}
		}
	} else {
		$d = \%root;
		for $dir (@cur) {
			$d = $d->{$dir};
		}
		if (/dir (.*)/) {
			my %h = ();
			$d->{$1} = \%h;
		} else {
			/(\d+) (.*)/;
			$d->{$2} = $1; 
		}
	}
}
$part1 = 0;
$part2req = 30000000 - (70000000 - parse_dir(\%root, 0));
$part2 = 1e9;
$z = 1;
parse_dir(\%root, 0);
print($part1.$/);
print($part2);

sub parse_dir {
	my ($r, $d) = @_;
	my $s = 0;
	for(keys%$r) {
		$val = %$r{$_};
		if (ref($val) eq 'HASH') {
			print $" x ($d * 2),$_,$/;
			$s += parse_dir($val, $d+1);
		} else {
			$s += $val;
			print($" x ($d * 2),"$_: $val\n")
		}
	}
	$part1 += $s <= 100000 ? $s : 0;
	if ($z) {
		if ($s >= $part2req && $s < $part2) {
			$part2 = $s;
		}
	}
	if ($s >= 25034295) {
		print "size $s\n";	
	}
	return $s;
}