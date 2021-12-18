use strict;
use FindBin qw( $Bin );
use File::Spec;
use List::Util qw(max min reduce);
 
sub parsePacket {
  my $t = $_[0];
  return if !$t;
  $t =~s/(...)(...)//;
  my $version = oct("0b" . $1);
  my $type = oct("0b" . $2);
  my $f="";
  my $bitsParsed = 6;
  my $value = 0;
  if ($type == 4) {
    while($t=~s/(.)(.{4})//) {
      $f.=$2;
      $bitsParsed += 5;
      last if $1 eq "0";
    }
    $value += oct("0b".$f);
  } else {
    $t=~s/.//;
    $bitsParsed++;
    my $mode=$&;
    my @subValues = ();
    if ($mode == 0) {
      $t=~s/.{15}//;
      my $sublength = oct("0b".$&);
      $bitsParsed += 15 + $sublength;
      while ($sublength > 0) {
        ($t, my $subBitsParsed, my $subVersion, my $subVal) = @{parsePacket($t)};
        $sublength -= $subBitsParsed;
        $version += $subVersion;
        push(@subValues, $subVal);
      }
    } else {
      $t=~s/.{11}//;
      $bitsParsed += 11;
      my $subcount = oct("0b".$&); 
      while ($subcount--) {
        ($t, my $subBitsParsed, my $subVersion,  my $subVal) = @{parsePacket($t)};
        $bitsParsed += $subBitsParsed;
        $version += $subVersion;
        push(@subValues, $subVal);
      }
    }

    if ($type == 0) {
      $value = reduce {$a + $b} @subValues;
    } elsif ($type == 1) {
      $value = reduce {$a * $b} @subValues;
    } elsif ($type == 2) {
      $value = min(@subValues);
    } elsif ($type == 3) {
      $value = max(@subValues);
    } elsif ($type == 5) {
      $value = $subValues[0] > $subValues[1] ? 1 : 0;
    } elsif ($type == 6) {
      $value = $subValues[0] < $subValues[1] ? 1 : 0;
    } elsif ($type == 7) {
      $value = $subValues[0] == $subValues[1] ? 1 : 0;
    }
  }

  return [$t, $bitsParsed, $version, $value];
}


my $filename = File::Spec->catfile($Bin, 'in16');
open(FH, '<', $filename) or die $!;

my %hex2bin = (
  '0' => '0000',
  '1' => '0001',
  '2' => '0010',
  '3' => '0011',
  '4' => '0100',
  '5' => '0101',
  '6' => '0110',
  '7' => '0111',
  '8' => '1000',
  '9' => '1001',
  'A' => '1010',
  'B' => '1011',
  'C' => '1100',
  'D' => '1101',
  'E' => '1110',
  'F' => '1111'
);

my $t=<FH>;
# int overflow.
# $t = sprintf("%b", hex($t));
# $t='0'.$t while ((length$t) % 4); 
$t=~s/./$hex2bin{$&}/eg;
my @ans = @{parsePacket($t)}; 
print $ans[2].$/.$ans[3];

