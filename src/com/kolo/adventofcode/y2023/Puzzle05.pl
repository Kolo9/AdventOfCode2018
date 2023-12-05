use List::Util qw(min max);
open( F, '<', 'in05' );

$_ = <F>;
#s/( \d+)/push@s,[$1, $1]/reg;
s/( \d+)( \d+)/push@s,[$1, $1+$2-1]/reg;
print "Starting with:\n";
print"@$_\n"for @s;


@r=();
for (<F>) {
	chomp;
	if (!$_) {
		@ns=();
		@r=sort{$a->[0]<=>$b->[0]}@r;
		@s=sort{$a->[0]<=>$b->[0]}@s;
		print "cur:\n";
		print"@$_\n"for@s;
		print "parsing maps:\n";
		print"@$_\n"for@r;
		$i=0;
		outer:
		for(0..$#s) {
			$i++ while($i<~~@r&&$r[$i][1]<$s[$_][0]);
			if ($i>$#r) {
				push@ns,$s[$_];
				next;
			}
			$ss = $s[$_][0];
			$se = $s[$_][1];
			$rs = $r[$i][0];
			$re = $r[$i][1];
			$rd = $r[$i][2];
			if ($rs>$se) {
				push@ns,$s[$_];
				next;
			}
			if ($rs>$ss) {
				push@ns,[$ss,$rs-1];
			}
			$h=min($re,$se);
			push@ns,[(max($rs,$ss)+$rd),$h+$rd];
			# Oh no.
			while($i<$#r&&$se>$re) {
				$i++;
				$rs = $r[$i][0];
				$re = $r[$i][1];
				$rd = $r[$i][2];
				print "Range cut at $h. now checking against [$rs, $re, $rd]\n";
				if ($rs>$se) {
					push@ns,[$h+1,$se];
					next outer;
				}
				$nh = min($re,$se);
				push@ns,[$h+1+$rd,$nh+$rd];
				$h=$nh;
			}
			if ($i==$#r && $se>$re){
				push@ns,[$re+1,$se];
			}
		}
		@s=@ns;
		@r=();
		print "Reset! Now:\n";
		print"@$_\n"for @s;
		next;
	}
	if ( $_ !~ /\d/ ) {
		next;
	}
	@v = split $", $_;
	push@r,[$v[1], $v[1] + $v[2] - 1, $v[0] - $v[1]];
}

print min(map{$_->[0]}@s);
