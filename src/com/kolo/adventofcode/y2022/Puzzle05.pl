no warnings;
open(F,'<','in05');

# part 1
#$_=<F>,s#\[(.)\]#unshift@{$_[$-[0]/4+1]},$1#eg while!/^
#/;($},$f,$t)=/\d+/g,map{push@{$_[$t]},pop@{$_[$f]}}1..$}for<F>;print@$_[-1]for@_

# part 2
$_=<F>,s#\[(.)\]#unshift@{$_[$-[0]/4+1]},$1#eg while!/^
/;($},$f,$t)=/\d+/g,push@{$_[$t]},splice@{$_[$f]},-$},$}for<F>;print@$_[-1]for@_