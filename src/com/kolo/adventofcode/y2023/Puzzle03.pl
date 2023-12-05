open(F,'<','in03');
for(<F>) {
	chomp;
	push@_,['.',(split//,$_),'.']
}
push@_,[('.')x~~@{$_[0]}];
unshift@_,[('.')x~~@{$_[0]}];
for$i(0..$#_) {
	@l=@{$_[$i]};
	$n='';
	$v=$g=0;	
	for$j(0..$#l) {
		$c=$l[$j];
		$%+=$v*$n,($g?(push@{$g{$g}},$n):''),$n='',$v=$g=0,next if$c!~/\d/;
		$n.=$c;
		for$k($i-1..$i+1){
			for$l($j-1..$j+1){
				$g=$k*1000+$l,$v=1if$_[$k][$l]=~/[^0-9.]/;
			}
		}
	}
}
print$%;
print eval join'+',map{@$_[0]*@$_[1]}values%g;