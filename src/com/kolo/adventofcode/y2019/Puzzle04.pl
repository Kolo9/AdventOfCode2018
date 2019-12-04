$ans=0;
for (353096..843212) {
    $ans++ if /(.)\1/ and $_ eq join'',sort split//,$_;
}
print"$ans\n";
$ans=0;
for (353096..843212) {
    $ans++ if /(?:^|(.)(?!\1))(.)\2(?!\2)/ and $_ eq join'',sort split//,$_;
}
print"$ans\n";
