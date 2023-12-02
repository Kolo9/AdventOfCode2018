open(F,'<','in02');
for(<F>){
	%m=();
	for$g(split/;/){
		while($g=~/(\d+) (\w+)/g) {
			$m{$2}=$1>$m{$2}?$1:$m{$2};
		}
	}
	if($m{red}<13&&$m{green}<14&&$m{blue}<15){
		/Game (\d+)/;$p1+=$1;
	}
	$p2+=($m{red}||0)*($m{green}||0)*($m{blue}||0)
}
print"$p1
$p2";
