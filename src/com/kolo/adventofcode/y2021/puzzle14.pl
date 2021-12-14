use FindBin qw( $Bin );
use File::Spec;
use List::Util qw(max min);
 
$filename = File::Spec->catfile($Bin, 'in14');
open(FH, '<', $filename) or die $!;

chomp($t=<FH>);
<FH>;
%i = ();
%counts = ();
$t =~ s/../$counts{$&}++/reg;
$t =~ s/(?<=.)../$counts{$&}++/reg;
for(<FH>){
    / -> /;
    $s=$`;
    $z=$';
    $z=~s/\s//g;
    $i{$s}=[substr($s, 0, 1).$z,  $z.substr($s, 1, 1)];
}
for (1..40) {
  %newCounts = ();
  for $i(keys %i) {
    $count = $counts{$i};
    next if !$count;
    $newCounts{$i{$i}[0]} += $count;
    $newCounts{$i{$i}[1]} += $count;
  }
  %counts = %newCounts;
}
%letterCounts = ();
for (keys %counts) {
  $count = $counts{$_};
  s/./$letterCounts{$&}+=$count*.5/reg;
}
# letter count is ###.5 for first and last letter, round up.
$min = 0|((min values %letterCounts) + .5);
$max = 0|((max values %letterCounts) + .5);
print($max - $min);