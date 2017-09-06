"use strict";

const express = require("express");
let session = require('express-session');
const router = express.Router();
const models = require("../models");
const request = require("request");
const HeartStroke = models.heartStroke;
const Music = models.Music;
const User = models.User;
let sess;

router.post("/heartStrokeMusic", function(req, res, next) {
	console.log("dedefef");
	res.type("json");
    sess = req.cookies;
    var userid = sess.userId;
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t heart stroke music'});
	} else {
		Music.find({
			"where": {
				"idMusic": req.body.idMusic
			}
		}).then(musica => {
			if (musica) {
				HeartStroke.create({
					musicId: req.body.idMusic,
					flagmanId: userid
				}).then(heart => {
					if (heart) {
						res.send({msg: "Success"});
					} else {
						res.send({msg: "Fail"});
					}
				}).catch(err => {
					res.send({err: err});
				});
			} else {
				res.send({
					msg: "Music Not found"
				});
			}
		}).catch(err => {
			res.send({msg: 'Unable to search music',err: err});
		});
	}
});

module.exports = router;
