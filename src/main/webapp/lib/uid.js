var XML_CHAR_MAP = {
	'<' : '&lt;',
	'>' : '&gt;',
	'&' : '&amp;',
	'"' : '&quot;',
	"'" : '&apos;'
};
var avgEData, minEData, maxEData, lowerEData, upperEData;
// on ready
$(document).ready(function() {

	$('#getEntropyButton').click(function(e) {
		getEntropy();
		e.preventDefault();
	});

	$(".sampleButton, .inactive").click(function() {
		var id = $(this).attr("id");
		$(".sampleButton").each(function() {
			if ($(this).attr("id") != id) {
				$(this).removeClass("active").addClass("inactive");
			}
		});

		$(this).removeClass("inactive").addClass("active");

		// get graph for this corpus
		var windowSize = $("#windowSize").val();

		getCorpusGraph(id, windowSize);
	});
});

function getEntropy() {

	var inputText = escapeXml($("#inputTextArea").val().replace(/\\/g, ""));
	var topHL = $("#topHL").val();
	var LM = $("#LM").val(); // language model?
	var corpus = $("#corpus").val();
	var windowSize = $("#windowSize").val();
	$("#outputArea").html("<h2>CLICKED</h2><p>" + inputText + "</p>");
	
	$.ajax({
		url : "getEntropy.do",
		data : {
			inputText : inputText,
			topHL : topHL,
			LM : LM,
			corpus : corpus,
			windowSize : windowSize
		},// $('form').serialize(),
		dataType : "json",
		type : 'post',
		success : function(data) {
			
			$(".sampleButton").each(function() {
				$(this).removeClass("active").addClass("inactive");
			});
			$("#longestStreak").html(data.longestStreak);
			displayDocument(data.results);
			displayGraph(data.results, windowSize, corpus);
			setEntropyTableContent(data.results);
		},
		error : function(jq, status, errorMsg) {

			$('<div class=error/>').text(
					"Status: " + status + " Error: " + errorMsg).insertAfter(
					'h1');
		},
		complete : function(jq, status) {
//			 alert(status);
		}
	});
}

function displayDocument(results) {
	var len = results.length;
	$("#document").html("<h3>Document</h3><br>");

	if (typeof len != "undefined") {

		for (var i = 0; i < len; i++) {
			var sent = results[i].sentence;
			var hl = results[i].hightlight;
			var entropy = results[i].entropy;
			var line = results[i].line;
			if (hl == 1) {
				$("#document").append(
						"<br><font color='#a20000'>Line=" + line + ", Entropy="
								+ entropy + ":" + sent + ".</font><br>");
			} else {
				$("#document").append(sent + ".");
			}

		}
	} else {
		$("#document").append("<div style='color:red'>NO DOC DISPLAY</div>");
	}

}
function setEntropyTableContent(results) {
	var len = results.length;
	var tableContent = "<table id='entropyTable'><thead><tr><th class='entropyCell'>Index</th><th>Sentence</th><th class='entropyCell'>Entropy</th></tr></thead><tbody>";
	if (typeof len != "undefined") {
		for (var i = 0; i < len; i++) {
			var sent = results[i].sentence;
			var entropy = results[i].entropy;
			tableContent += "<tr><td class='entropyCell'>" + i + "</td><td>"
					+ sent + "</td><td class='entropyCell'>" + entropy
					+ "</td></tr>";
		}
	}
	tableContent += "</tbody></table>";
	$("#outputArea").html(tableContent);
	$('#entropyTable').tablesorter();
}

function escapeXml(s) {
	return s.replace(/[<>&"']/g, function(ch) {
		return XML_CHAR_MAP[ch];
	});
}

function getCorpusGraph(corpus, windowSize) {
	// dumppy line
	var dummyLine = [ {
		"line" : 0,
		"entropy" : 0
	} ];
	displayGraph(dummyLine, windowSize, corpus);

}

function displayGraph(data, windowSize, corpus) {
	$("#timeline").html("");
	// set the dimensions of the canvas / graph
	var margin = {
		top : 20,
		right : 100,
		bottom : 30,
		left : 100
	}, width = 980 - margin.left - margin.right, height = 520 - margin.top
			- margin.bottom;

	// set the ranges
	var x = d3.scale.linear().range([ 0, width ]);
	var y = d3.scale.linear().range([ height, 0 ]);

	// define the axes
	var xAxis = d3.svg.axis().scale(x).orient("bottom");

	var yAxis = d3.svg.axis().scale(y).orient("left");

	// define the total line
	var entropyLine = d3.svg.line().x(function(d) {
		return x(d.line);
	}).y(function(d) {
		return y(d.entropy);
	});

	// define the measure line
	var measureLine = d3.svg.line().x(function(d) {
		return x(d.line);
	}).y(function(d) {
		return y(d.measure);
	});

	/** ********ALways display wsj lines (avg, min, max, lower, upper)********** */
	var avgELine = d3.svg.line().x(function(d) {
		return x(d.line);
	}).y(function(d) {
		return y(d.average);
	});

	var minELine = d3.svg.line().x(function(d) {
		return x(d.line);
	}).y(function(d) {
		return y(d.min);
	});

	var maxELine = d3.svg.line().x(function(d) {
		return x(d.line);
	}).y(function(d) {
		return y(d.max);
	});

	var lowerELine = d3.svg.line().x(function(d) {
		return x(d.line);
	}).y(function(d) {
		return y(d.lower);
	});

	var upperELine = d3.svg.line().x(function(d) {
		return x(d.line);
	}).y(function(d) {
		return y(d.upper);
	});

	/** ********END********** */

	// define 'div' for tooltips
	var div = d3.select("#timeline").append("div").attr("class", "tooltip")
			.style("opacity", 0);

	// adds the svg canvas
	var svg = d3.select("#timeline").append("svg").attr("width",
			width + margin.left + margin.right).attr("height",
			height + margin.top + margin.bottom).append("g").attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");

	// process data
	data.forEach(function(d) {
		d.entropy = +d.entropy;
		d.line = +d.line;
	});
	// var maxLine = d3.max(maxEData, function(d) {
	// return d.line;
	// });

	var MAX_LINE = 100;
	var MAX_ENTROPY = 20;
	var MIN_LINE = 0;

	if (corpus == "corps") {
		MAX_LINE = 400;
		MAX_ENTROPY = 25;
	}
	if (corpus == "wsjTrain") {
		MAX_LINE = 50;
		MAX_ENTROPY = 20;
	}
	if (corpus == "europa") {
		MAX_LINE = 40;
		MAX_ENTROPY = 20;
	}

	var maxLine = d3.max(data, function(d) {
		return d.line;
	});
	if (maxLine != "undefined" && maxLine > MAX_LINE) {
		MAX_LINE = maxLine;
	}

	var maxEntropy = d3.max(data, function(d) {
		return d.entropy;
	});
	if (maxEntropy != "undefined" && maxEntropy > MAX_ENTROPY) {
		MAX_ENTROPY = maxEntropy;
	}

	maxLine = MAX_LINE;
	var minLine = MIN_LINE;
	maxEntropy = MAX_ENTROPY;

	// var minLine = d3.min(maxEData, function(d) {
	// return d.line;
	// });
	// var maxEntropy = d3.max(maxEData, function(d) { // maxEData instead of
	// data
	// return d.max;// entropy
	// });
	var step = getStep(maxEntropy);

	// scale the range of the data
	x.domain([ MIN_LINE, MAX_LINE ]);
	// x.domain(d3.extent(data, function(d) {
	// return d.line;
	// }));
	y.domain([ 0, getMaxDomain(maxEntropy) ]);

	// create measure lines
	for (var i = 0; i < Math.floor(maxEntropy / step) + 1; i++) {
		var measure = (i + 1) * step;
		var lineData = [ {
			"line" : minLine,
			"measure" : measure
		}, {
			"line" : maxLine,
			"measure" : measure
		} ];

		// add the measureline path.
		svg.append("path").attr("class", "measureLine").attr("d",
				measureLine(lineData));
	}

	// add the entropy path.
	svg.append("path").attr("class", "entropyLine")
			.attr("d", entropyLine(data));

	// draw the scatterplot
	svg.selectAll("dot").data(data).enter().append("circle").attr("r",
			function(d, i) {
				return 4;
			}).attr("cx", function(d) {
		return x(d.line);
	}).attr("cy", function(d) {
		return y(d.entropy);
	}).on(
			"mouseover",
			function(d) {
				div.transition().duration(200).style("opacity", .9);
				div.html(d.line + "." + d.sentence).style("left",
						(d3.event.pageX) + "px").style("top",
						(d3.event.pageY - 28) + "px");
			}).on("mouseout", function(d) {
		div.transition().duration(500).style("opacity", 0);
	});

	/** ****************** */
	var minFile = "data/" + corpus + "." + windowSize + ".min.tsv";
	var maxFile = "data/" + corpus + "." + windowSize + ".max.tsv";
	var avgFile = "data/" + corpus + "." + windowSize + ".avg.tsv";
	var lowerFile = "data/" + corpus + "." + windowSize + ".lower.tsv";
	var upperFile = "data/" + corpus + "." + windowSize + ".upper.tsv";

	// MIN
	d3.tsv(minFile, function(error, data) {
		minEData = JSON.parse(JSON.stringify(data));
		svg.append("path").attr("class", "minELine").attr("d",
				minELine(minEData));
	});
	// MAX
	d3.tsv(maxFile, function(error, data) {
		maxEData = JSON.parse(JSON.stringify(data));
		svg.append("path").attr("class", "maxELine").attr("d",
				maxELine(maxEData));
	});
	// AVG
	d3.tsv(avgFile, function(error, data) {
		avgEData = JSON.parse(JSON.stringify(data));
		svg.append("path").attr("class", "avgELine").attr("d",
				avgELine(avgEData));
	});
	// LOWER
	d3.tsv(lowerFile, function(error, data) {
		lowerEData = JSON.parse(JSON.stringify(data));
		svg.append("path").attr("class", "lowerELine").attr("d",
				lowerELine(lowerEData));
	});
	// UPPER

	d3.tsv(upperFile, function(error, data) {
		upperEData = JSON.parse(JSON.stringify(data));
		svg.append("path").attr("class", "upperELine").attr("d",
				upperELine(upperEData));
	});
	/** ********END******** */

	// add the X Axis
	svg.append("g").attr("class", "x axis").attr("transform",
			"translate(0," + height + ")").call(xAxis).append("text").attr(
			"class", "x label").attr("text-anchor", "end").attr("x", width)
			.attr("y", -10).text("Line Number");

	// add the Y Axis
	svg.append("g").attr("class", "y axis").call(yAxis).append("text").attr(
			"class", "y label").attr("text-anchor", "end").attr("transform",
			"rotate(-90)").attr("y", 16).text("Entropy");

}

function getStep(entropy) {
	var base10 = 10;
	var i = 1;
	while (Math.pow(base10, i) < entropy) {
		i++;
	}
	var lower = Math.pow(base10, i - 1);
	var x = Math.floor(entropy / lower);
	x += (2 - x % 2);

	return Math.pow(base10, i - 2) * x;

}

function getMaxDomain(entropy) {
	var x = getStep(entropy);
	var i = 1;
	while (x * i < entropy) {
		i++;
	}
	return x * i;
}
