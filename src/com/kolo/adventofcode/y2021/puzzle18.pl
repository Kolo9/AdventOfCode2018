use strict;
use FindBin qw( $Bin );
use File::Spec;

sub add {
  my ($eq, $eq2) = @_;
  my $r = $eq?"[${eq},${eq2}]":$eq2;
  my $f = 1;
  while($f) {
    $f = 0;
    my $o = 0;
    my $expl = 0;
    $r =~ s/[[\]]/$o+=$& eq'['?1:-1;$expl=$expl||$-[0]if$o==5/reg;

    # Only accounts for 5-nested at the beginning of the string, won't remove balanced constructs in between.
    # if (s/((?:\[[^[]*){3}\[[^[\d]*)(\d*)([^[]*)\[(\d+),(\d+)\](?:(\D*)(\d+))?/$1.((length$2)?$2+$4:'').$3."0".$6.((length$7)?$5+$7:'')/e) {

    # Recursive regex and self loathing. Won't work, no way to recurse on only part of the pattern AFAICT.
    # if (s/((?:\[[^[]*\[(?:[^\][]|(?R))*\]){3}\[[^[\d]*)(\d*)([^[]*)\[(\d+),(\d+)\](?:(\D*)(\d+))?/$1.((length$2)?$2+$4:'').$3."0".$6.((length$7)?$5+$7:'')/e) {
    if ($expl) {
      $r =~ s|^(.{$expl})\[(\d+),(\d+)\](.*)|my($s,$x,$y,$e,$n1,$n1e,$n2,$n2e,$x1,$x2)=($1,$2,$3,$4,'',0,'',0,'','');$n1=$x+$2,$n1e=1,$x1=$3 if$s=~s/(.*\D)(\d+)(\D*)/$1/;$n2=$y+$2,$n2e=1,$x2=$1 if$e=~s/(.*?\D)(\d+)(.*)/$3/;$s.$n1.$x1.'0'.$x2.$n2.$e|e;
      # print"explode now @ ${expl}:$_\n";
      $f = /\[,/?0:1;
    } elsif ($r =~ s#\d\d+#'['.(0|($&/2)).','.(0|($&/2+.5)).']'#e) {
      # print"split now: $_\n";
      $f = 1;
    }
  }

  return $r;
}

sub mag {
  my ($e) = @_;
  $e = substr$e,1,-1;
  my @m = $e =~ /\[(?:[^\][]|(?R))*\]/g;
  my $l;
  my $r;
  if (~~@m == 2) {
    ($l, $r) = map {mag($_)} @m;
  } elsif (~~@m) {
    ($l, $r) = $e =~ /\[(\d)/ ? ($1, mag($m[0])) : (mag($m[0]), substr$e,-1)
  } else {
    /(\d).*(\d)/;
    ($l, $r) = ($1, $2);
  }
  return $l*3 + $r*2;
}

my $filename = File::Spec->catfile($Bin, 'in18');
open(FH, '<', $filename) or die $!;

my @lines;
chomp,push@lines,$_ for<FH>;

my $eq="";
$eq = add($eq, $_) for @lines;
print mag($eq) . $/;

my $max = 0;
for my $i(0...$#lines) {
  for my $j(0..$#lines) {
    next if $i == $j;
    my $mag = mag(add($lines[$i], $lines[$j]));
    $max = $mag if $mag > $max;
  }
}
print "$max\n";