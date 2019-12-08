open(FH, '<', 'in08') or die $!;
$_=<FH>;
@layers = /.{150}/g;
$fewestZeroes = 151;
$ans1 = 0;
for (@layers) {
    $zeroes = y/0//;
    if ($zeroes < $fewestZeroes) {
        $fewestZeroes = $zeroes;
        $ans = y/1// * y/2//;
    }
}
print "$ans1\n";

@ans2 = (split//, $layers[0]);
for (@layers) {
    @cur = split//,$_;
    for ($i = 0; $i < length; $i++) {
        $ans2[$i] = $cur[$i] if $ans2[$i] == 2;
    }
}
@ans2 = (join'',@ans2) =~ /.{25}/g;
print join$/,@ans2;