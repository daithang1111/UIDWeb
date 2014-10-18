/**
 * Global variable
 */
var svgWidth = 980;
var svgHeight = 500;
var FIX_MOVE = 30;
var leftRightWidth = 40;
var topBottomHeight = 40;
var distWidth = 160;
var c = d3.scale.category20();

var svg, svgLeft, svgTop, svgRight, svgBottom, svgDist;

var xScale, yScale, rScale, dxScal, dyScale;

var xAxis, yAxis, dxAxis, dyAxis;

var lineFunction, leftLine, topLine, rightLine, bottomLine;

var dataset;

var numTopics = 20;
var numSenators = 100;

var margin = {
	top : 80,
	right : 80,
	bottom : 100,
	left : 120
}, width = svgWidth - margin.left - margin.right, height = svgHeight
		- margin.top - margin.bottom;

var dmargin = {
	dtop : 60,
	dright : 20,
	dbottom : 20,
	dleft : 80
}, dwidth = distWidth - dmargin.dleft - dmargin.dright, dheight = svgHeight
		- dmargin.dtop - dmargin.dbottom;

// status variables
var selectedTopic = null;
var selectedSenator = null;

$(document).ready(function() {
	// 1. init svgs
	init();
	drawOtherSvgs();
	// 2. get data
	updateData();
	// 3. visualize()
	visualize();
});

/**
 * Methods
 */

function init() {

	// main svg
	svg = d3.select("body").append("svg").attr("class", "main").style("left",
			leftRightWidth).style("top", topBottomHeight).attr("width",
			svgWidth).attr("height", svgHeight).append("g").attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");

	xScale = d3.scale.ordinal().rangePoints([ FIX_MOVE, FIX_MOVE + width ]);
	yScale = d3.scale.ordinal().rangePoints([ height + FIX_MOVE, FIX_MOVE ]);

	rScale = d3.scale.linear();

	xAxis = d3.svg.axis().scale(xScale).orient("top");
	yAxis = d3.svg.axis().scale(yScale).orient("left");

	svg.append("g").attr("class", "x axis").call(xAxis);

	svg.append("g").attr("class", "y axis").call(yAxis);

	// dist svg
	svgDist = d3.select("body").append("svg").attr("class", "dist").attr(
			"width", dwidth + dmargin.dleft + dmargin.dright).attr("height",
			dheight + dmargin.dtop + dmargin.dbottom).style("left",
			2 * leftRightWidth + svgWidth).style("top", topBottomHeight)
			.append("g").attr("transform",
					"translate(" + dmargin.dleft + "," + dmargin.dtop + ")");

	dyScale = d3.scale.ordinal().rangeRoundBands([ dheight, 0 ], .1);

	dxScale = d3.scale.linear().range([ 0, dwidth ]);

	dxAxis = d3.svg.axis().scale(dxScale).orient("bottom").ticks(3);

	dyAxis = d3.svg.axis().scale(dyScale).orient("left");

	svgDist.append("g").attr("class", "dx axis").attr("transform",
			"translate(0," + dheight + ")").call(dxAxis);

	svgDist.append("g").attr("class", "dy axis").call(dyAxis);

	svgDist.append("text").attr("class", "dtitle").attr("x",
			(dwidth - dmargin.dleft) / 2).attr("y", 0 - (dmargin.dtop / 2))
			.attr("text-anchor", "middle").style("font-size", "14px").text(
					"Topic");

	hideTopicDistribution();
	// tooltip
	// define 'div' for tooltips
	tooltip = d3.select("body").append("div").attr("class", "tooltip").style(
			"opacity", 0);

}
function drawOtherSvgs() {
	// line function
	lineFunction = d3.svg.line().x(function(d) {
		return d.x;
	}).y(function(d) {
		return d.y;
	}).interpolate("linear");
	// other svgs

	svgLeft = d3.select("body").append("svg").attr("class", "left").attr(
			"width", leftRightWidth).attr("height", svgHeight).style("left",
			"0").style("top", topBottomHeight);

	svgTop = d3.select("body").append("svg").attr("class", "top").attr("width",
			svgWidth).attr("height", topBottomHeight).style("left",
			leftRightWidth).style("top", "0");

	svgRight = d3.select("body").append("svg").attr("class", "right").attr(
			"width", leftRightWidth).attr("height", svgHeight).style("left",
			svgWidth + leftRightWidth).style("top", topBottomHeight);

	svgBottom = d3.select("body").append("svg").attr("class", "bottom").attr(
			"width", svgWidth).attr("height", topBottomHeight).style("left",
			leftRightWidth).style("top", topBottomHeight + svgHeight);

	// draw left arrow
	leftLine = [ {
		"x" : leftRightWidth,
		"y" : 0
	}, {
		"x" : 0,
		"y" : svgHeight / 2
	}, {
		"x" : leftRightWidth,
		"y" : svgHeight
	} ];

	svgLeft.append("path").attr("d", lineFunction(leftLine)).attr("stroke",
			"#ccc").attr("stroke-width", 1).attr("fill", "#ccc");

	// draw top arrow
	topLine = [ {
		"x" : 0,
		"y" : topBottomHeight
	}, {
		"x" : svgWidth / 2,
		"y" : 0
	}, {
		"x" : svgWidth,
		"y" : topBottomHeight
	} ];

	svgTop.append("path").attr("d", lineFunction(topLine)).attr("stroke",
			"#ccc").attr("stroke-width", 1).attr("fill", "#ccc");

	// draw right arrow
	rightLine = [ {
		"x" : 0,
		"y" : 0
	}, {
		"x" : leftRightWidth,
		"y" : svgHeight / 2
	}, {
		"x" : 0,
		"y" : svgHeight
	} ];

	svgRight.append("path").attr("d", lineFunction(rightLine)).attr("stroke",
			"#ccc").attr("stroke-width", 1).attr("fill", "#ccc");
	// draw bottom line
	bottomLine = [ {
		"x" : 0,
		"y" : 0
	}, {
		"x" : svgWidth / 2,
		"y" : topBottomHeight
	}, {
		"x" : svgWidth,
		"y" : 0
	} ];

	svgBottom.append("path").attr("d", lineFunction(bottomLine)).attr("stroke",
			"#ccc").attr("stroke-width", 1).attr("fill", "#ccc");

	/**
	 * SET ACTIONS ON ARROWS
	 */
	svgLeft.on("mouseover", function() {
		setcursor("pointer");
	}).on("mouseout", function() {
		setcursor("default");
	});

	svgTop.on("mouseover", function() {
		setcursor("pointer");
	}).on("mouseout", function() {
		setcursor("default");
	});

	svgRight.on("mouseover", function() {
		setcursor("pointer");
	}).on("mouseout", function() {
		setcursor("default");
	});

	svgBottom.on("mouseover", function() {
		setcursor("pointer");
	}).on("mouseout", function() {
		setcursor("default");
	});

	svgRight.on("click", function() {
		// TODO
	});

	svgLeft.on("click", function() {
		// TODO
	});

	svgTop.on("click", function() {
		// TODO
	});

	svgBottom.on("click", function() {
		// TODO
	});

}

function setWidth(w) {
	svgWidth = w;
}
function setHeight(h) {
	svgHeight = h;
}
function resize(newH, newW) {
	setHeight(newH);
	setWidth(newW);

	svg.attr("width", svgWidth);
	svg.attr("height", svgHeight);
}

function setcursor(cursor) {
	d3.select("body").style("cursor", cursor);
}

function updateData() {
	var data = [];
	var numDataPoints = 100;
	var numTimeRanges = 5;

	for (var k = 0; k < numTimeRanges; k++) {
		var subData = [];

		for (var i = 0; i < numDataPoints; i++) {
			var senator = (i % numSenators) + 1;

			for (var j = 0; j < numTopics; j++) {
				var topic = j + 1;// Math.floor(Math.random() * numTopics) +
				// 1;

				var prop = Math.random();
				subData.push([ senator, topic, prop ]);
			}
			data[k] = subData;
		}

	}
	dataset = interpolateData(data);
}

function interpolateData(inputData) {
	var MAX_SENATOR = 10;
	var startPoint = 0;
	var timeRange = 0;
	var data = inputData[timeRange].slice(20 * startPoint,
			20 * (startPoint + MAX_SENATOR));

	return $.map(data, function(d) {

		return {
			senator : d[0],
			topic : d[1],
			prop : d[2] * d[2]
		};

	});
}

function visualize() {
	d3.select("svg .main").data([]).exit().remove();

	// 1. set domain for scales
	xScale.domain(dataset.sort(function(a, b) {
		return d3.ascending(parseInt(a.topic), parseInt(b.topic));
	}).map(function(d) {
		return d.topic;
	}));

	yScale.domain(dataset.sort(function(a, b) {
		return d3.descending(parseInt(a.senator), parseInt(b.senator));
	}).map(function(d) {
		return d.senator;
	}));

	rScale.domain([ 0, 1 ]).rangeRound([ 2, 10 ]);
	// 2. update xAxis, yAxis
	xAxis.tickFormat(function(d) {
		return "Topic " + d;
	});
	yAxis.tickFormat(function(d) {
		return "Senator " + d
	});
	// 3. update svg with new axis

	// y axis
	svg.selectAll("g.y.axis").call(yAxis).selectAll("text").on(
			"mouseover",
			function(d) {
				selectedSenator = d;
				setcursor("pointer");
				setyAxisColor();
				// d3.select(this).style("fill", "red");
				tooltip.transition().duration(200).style("opacity", 100);
				tooltip.html("Click for more information about Senator " + d)
						.style("left", (d3.event.pageX) + "px").style("top",
								(d3.event.pageY - 28) + "px");

				setCirclesColor();
				// svg.selectAll("circle").style("fill", function(dCircle, i) {
				// if (dCircle.senator == d) {
				// return "red";
				// } else {
				// return c(dCircle.topic);
				// }
				// });

			}).on("mouseout", function(d) {
		setcursor("default");
		// d3.select(this).style("fill", yDefaultColor());
		tooltip.transition().duration(200).style("opacity", 0);
		// svg.selectAll("circle").style("fill", function(dCircle, i) {
		// return c(dCircle.topic);
		// });

	}).on("click", function(d) {
		showSenatorVisualization(d);
	});
	svg.selectAll("g.y.axis .tick").style("fill", function(d, i) {
		return yDefaultColor();
	});

	// x axis
	svg.selectAll("g.x.axis").call(xAxis).selectAll("text").style("font-size",
			"12px").style("text-anchor", "end").attr("dx", "40")
			.attr("dy", "0").attr("transform", function(d) {
				return "rotate(-50)"
			}).on("mouseover", function(d) {
				selectedTopic = d;
				setcursor("pointer");

				setxAxisColor();

				// d3.select(this).style("fill", "red");
				setCirclesColor();
				// svg.selectAll("circle").style("fill", function(dCircle, i) {
				// if (dCircle.topic == d) {
				// return "red";
				// } else {
				// return c(dCircle.topic);
				// }
				// });

				// show topic distribution
				showTopicDistribution(d);
			}).on("mouseout", function(d) {
				setcursor("default");
				// d3.select(this).style("fill", function(d) {
				// return c(d);
				// });
				tooltip.transition().duration(200).style("opacity", 0);
				// svg.selectAll("circle").style("fill", function(dCircle, i) {
				//
				// return c(dCircle.topic);
				// });
			});

	setxAxisColor();
	// 4. draw measure line

	var measureLine = d3.svg.line().defined(function(d) {
		return d.measure != null;
	}).x(function(d) {
		return xScale(d.line);
	}).y(function(d) {
		return yScale(d.measure);
	});

	for (var i = 0; i < numSenators; i++) {

		var lineData = [ {
			"line" : 1,
			"measure" : i + 1
		}, {
			"line" : numTopics,
			"measure" : i + 1
		} ];

		svg.append("path").attr("class", "measureLine").attr("d",
				measureLine(lineData));
	}
	// 5. add circles

	var circles = svg.selectAll("circle").data(dataset).enter()
			.append("circle");

	circles.attr("cx", function(d) {
		return xScale(d.topic);
	}).attr("cy", function(d) {
		return yScale(d.senator);
	}).attr("r", function(d, i) {
		return rScale(d.prop);
	}).style("fill", function(d, i) {
		return c(d.topic);
	}).style("fill-opacity", ".5");

	// 6. show move actions
	svg.on("mousemove", function() {

		// console.log(d3.event);
		if (d3.event.pageY > (topBottomHeight + svgHeight / 2)) {
			// if (d3.event.pageY > svgHeight ) {
			$(".bottom").fadeIn(400);
			$(".top").fadeOut(400);

		} else {
			$(".top").fadeIn(400);
			$(".bottom").fadeOut(400);
		}

		if (d3.event.pageX > (leftRightWidth + svgWidth / 2)) {
			// if (d3.event.pageX > svgWidth) {
			$(".right").fadeIn(400);
			$(".left").fadeOut(400);
		} else {
			$(".left").fadeIn(400);
			$(".right").fadeOut(400);
		}

	});
}

function showSenatorVisualization(senatorName) {
	// TODO
	// window.location = ctx + '/visualize?senatorName='+senatorName;
}

function hideTopicDistribution() {

	svgDist.style("display", "none");

}

function showTopicDistribution(topicName) {
	svgDist.style("display", "inline");

	// set title for dist
	d3.select(".dtitle").text("Topic " + topicName);

	// generate topic-word dist data
	var topicData = [];
	var numWords = 20;
	var sum = 0;
	for (var i = 0; i < numWords; i++) {
		var p = Math.random();
		sum += p;
		topicData.push([ p ]);
	}
	var topicWordDist = $.map(topicData, function(d, i) {
		return {
			prop : d[0] / sum,
			word : "Word " + (i + 1)
		};
	});
	// generate bar chart
	// create a new SVG for topic distribution
	dyScale.domain(topicWordDist.map(function(d) {
		return d.word;
	}));
	dxScale.domain([ 0, d3.max(topicWordDist, function(d) {
		return d.prop;
	}) ]);

	svgDist.selectAll(".bar").data([]).exit().remove();

	svgDist.selectAll("g.dx.axis").call(dxAxis);
	svgDist.selectAll("g.dy.axis").call(dyAxis);
	var bars = svgDist.selectAll(".bar").data(topicWordDist).enter().append(
			"rect").attr("class", "bar").attr("x", function(d) {
		return 0;
	}).attr("height", dyScale.rangeBand()).attr("y", function(d) {
		return dheight - dyScale(d.word) - dyScale.rangeBand();
	}).attr("width", function(d) {
		return dxScale(d.prop);
	}).style("fill", "#ccc").on(
			"mouseover",
			function(d) {
				d3.select(this).style("fill", "red");

				tooltip.transition().duration(200).style("opacity", 100);
				tooltip.html(d.prop).style("left", (d3.event.pageX) + "px")
						.style("top", (d3.event.pageY - 28) + "px");

			}).on("mouseout", function(d) {
		d3.select(this).style("fill", "#ccc");
		tooltip.transition().duration(200).style("opacity", 0);
	});

}

function yDefaultColor() {
	return "#787878";
}

function setxAxisColor() {
	svg.selectAll("g.x.axis .tick text").style("fill", function(d) {
		if (d == selectedTopic) {
			return "red";
		} else {
			return c(d);
		}

	});

}

function setyAxisColor() {
	svg.selectAll("g.y.axis .tick text").style("fill", function(d) {
		if (d == selectedSenator) {
			return "red";
		} else {
			return yDefaultColor();
		}

	});

}

function setCirclesColor() {
	svg.selectAll("circle").style("fill", function(d, i) {
		if (d.topic == selectedTopic || d.senator == selectedSenator) {
			return "red";
		} else {
			return c(d.topic);
		}
	});
}