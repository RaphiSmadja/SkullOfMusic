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
	res.type("json");
    sess = req.cookies;
    var userid = sess.userId;
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t heartStroke music'});
	} else {
		Music.find({
		"where": {
				idMusic: req.body.idMusic
			}
		}).then(musica => {
			if (musica){
				HeartStroke.find({
				"where": {
						musicId: req.body.idMusic,
						flagmanId: userid
					}
				}).then(hs => {
				if (hs){
					HeartStroke.destroy({
						"where": {
							musicId: req.body.idMusic,
							flagmanId: userid
						}
					})
					res.send({ msg: 'heartstroke destroy'});	
				} else {
					 return HeartStroke.create({
							musicId : req.body.idMusic,
							flagmanId: sess.userId
						}).then(heart => {
							res.send({msg: heart});
						}).catch(err => { res.send({msg: 'nok', err: err}); });
					}
				}).catch(err => { throw err; });
			} else {
				res.send({ msg: 'the music chosen doesn\'t exist '});	
			}
		}).catch(err => { throw err; });
	}
})

module.exports = router;
