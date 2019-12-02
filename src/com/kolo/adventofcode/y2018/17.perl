@a = ();
$minX = 1e10;
$minY = 1e10;
$maxX = 0;
$maxY = 0;
open(my $fh, '<:encoding(UTF-8)', "puzzle17")
  or die "Could not open file 'puzzle17' $!";
for (<$fh>) {
    /x=([\d.]+)/;
    $xt=$1;
    /y=([\d.]+)/;
    $yt=$1;
    
    for $x (eval$xt) {
        for $y (eval$yt) {
            $a[$y][$x] = '#';
            $minX = $minX < $x ? $minX : $x;
            $minY = $minY < $y ? $minY : $y;
            $maxX = $maxX > $x ? $maxX : $x;
            $maxY = $maxY > $y ? $maxY : $y;
        }
    }
}
print "minX:${minX}$/";
print "minY:${minY}$/";
print "maxX:${maxX}$/";
print "maxY:${maxY}$/";
for $x ($minX-2..$maxX+2) {
    for $y ($minY..$maxY) {
        if (!$a[$y][$x]) {
            $a[$y][$x] |= '.';
        }
    }
}
$a[$minY][500] = '|';

$ans = 1;
@pointsToCheck = ();
push(@pointsToCheck, 500, $minY+1);
@nextPointsToCheck = ();
%seen = (("500 " . ($minY+1)) => 1);
$maxYWater = $minY;
for (1..105000) {
 #print ~~@pointsToCheck;
 #print "\n";
 @nextPointsToCheck = ();
 for ($i = 0; $i < ~~@pointsToCheck; $i++) {
    $x = $pointsToCheck[$i++];
	$y = $pointsToCheck[$i];
	if ($x < $minX-1 || $x > $maxX+1 || $y < $minY || $y > $maxY) {
	  next;
	}
	$change = 0;
        $c = $a[$y][$x];
        #if ($c =~ '(#|z)' ) {
        #    
        #} elsif ($c eq 'y') {
        #  # $a[$y][$x] = 'z';       
        #} elsif ($c eq 'd') {
        #    #if ($a[$y][$x+1] =~ '(d|y|z|#)') {
		#	#    $change = 1;
        #    #    $a[$y][$x] = 'y';
        #    #}
        #} elsif ($c eq '|') {
        #    if ($a[$y+1][$x] =~ '(d|y|z|#)' && $a[$y][$x+1] =~ '(#|y|z|d)') {
		#		$change = 1;
        #        $a[$y][$x] = 'd';
        #    }
        #} elsif ($c eq '.') {
        #    if ($a[$y][$x+1] =~ '(d|y|z)') {
		#		$change = 1;
        #        $a[$y][$x] = '|';
        #    } elsif($a[$y-1][$x] eq '|') {
		#	$change = 1;
        #        $a[$y][$x] = '|';
        #    } elsif($a[$y][$x+1] eq '|' && $a[$y+1][$x+1] =~ '(#|y|z|d)') {
		#	$change = 1;
        #        $a[$y][$x] = '|';
        #    } elsif($a[$y][$x-1] eq '|' && $a[$y+1][$x-1] =~ '(#|y|z|d)') {
		#	$change = 1;
        #        $a[$y][$x] = '|';
        #    }
        #    if ($a[$y][$x] eq '|') {
        #        $ans++;
        #    }
        #}
		##here='|' if here=='.' and (map[up]=='|' or map[right] =~ /=|~|</ or map[left] =~ /=|~|>/) 
		##here='=' if here=='|' and map[down]=~/#|~/           
		##here = '>' if here=='=' and map[left]=~/[>#~]/ 
		##here = '~' if here=='>' and map[right]=~/[#~]/
		$up = $a[$y-1][$x];
		$down = $a[$y+1][$x];
		$left = $a[$y][$x-1];
		$right = $a[$y][$x+1];
		if ($c eq '.' && ($up eq '|' || $right =~ '(=|~|>)' || $left =~ '(=|~|>)')) {
			$a[$y][$x] = '|';
			$change = 1;
			$ans++;
		} elsif ($c eq '|' && $down =~ '(#|~)') {
			$a[$y][$x] = '=';
			$change = 1;
		} elsif ($c eq '=' && $left =~ '(#|~|>)') {
			$a[$y][$x] = '>';
			$change = 1;
		} elsif ($c eq '>' && $right =~ '(#|~)') {
			$a[$y][$x] = '~';
			$change = 1;
		}
		
        if ($change) {
			push(@nextPointsToCheck, $x-1, $y-1, $x, $y-1, $x+1, $y-1, $x-1, $y, $x, $y, $x+1, $y, $x-1, $y+1, $x, $y+1, $x+1, $y+1);
			#push(@nextPointsToCheck, $x-1, $y, $x+1, $y, $x, $y+1, $x, $y-1);
		}
 }
 @pointsToCheck = ();
 for ($j = 0; $j < ~~@nextPointsToCheck; $j++) {
 $key = $nextPointsToCheck[$j++] . " " . $nextPointsToCheck[$j];
   #if (!$seen{$key}) {
      $seen{$key} = 1;
	  $maxYWater = $maxYWater > $nextPointsToCheck[$j] ? $maxYWater : $nextPointsToCheck[$j];
	  push(@pointsToCheck, $nextPointsToCheck[$j-1], $nextPointsToCheck[$j]);
   #}
 }
 #@t = ();
 #for ($j = 0; $j < ~~@pointsToCheck; $j+=2) {
#	if ($a[$pointsToCheck[$j+1]][$pointsToCheck[$j]] !~ '(y|#)') {
#		push(@t, $pointsToCheck[$j], $pointsToCheck[$j+1]);
#	}
 #}
 #@pointsToCheck = @t;
}

# for my $r (@a) {
#     print join("", @{$r}), "\n";
# }
 for ($j = 0; $j < ~~@pointsToCheck; $j+=2) {
   $a[ $pointsToCheck[$j+1]][$pointsToCheck[$j]] = 'X';
 }
#for $y ($minY..$maxY) {
#	for $x ($minX-1..$maxX+1) {
#		print($a[$y][$x]);
#	}
#	print"\n";
#}
print$ans;
print$/;

$ans2 = 0;
for $y ($minY..$maxY) {
	for $x ($minX-1..$maxX+1) {
		$ans2++ if $a[$y][$x] eq '~';
	}
}
print$ans2;
print$/;