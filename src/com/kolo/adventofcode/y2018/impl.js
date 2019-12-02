run();
function run() {
	var elf1 = 0;
	var elf2 = 1;
	var scores = [];
	var z = 0;
	scores.push(3);
	scores.push(7);
	var input = [7, 6, 8, 0, 7, 1];
	while (true) {
		var toAdd = parse2(scores[elf1] + scores[elf2]);
		for (var x of toAdd) {
		 	scores.push(x);
		}
		var start = -1;
		for (start = scores.length - toAdd.length - input.length; start < scores.length - input.length && start >= 0; start++) {
			var found = true;
			for (var numChecked = 0; numChecked < input.length; numChecked++) {
				if (scores[start + numChecked] != input[numChecked]) {
					found = false;
					break;
				}
			}
			if (found) {
				console.log(start);
				return;
			}
		}

		elf1 += 1 + scores[elf1];
		elf1 %= scores.length;
		elf2 += 1 + scores[elf2];
		elf2 %= scores.length;
	}
return scores;
}

function parse2(i) {
	var l = [];
	if (i === 0) {
		l.push(0);
		return l;
	}
	while (i > 0) {
		l.push(i % 10);
		i = 0|(i/10);
	}
	l = l.reverse();
	return l;
}