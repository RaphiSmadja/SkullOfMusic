"use strict";

const express = require("express");
let session = require('express-session');
const router = express.Router();
const models = require("../models");
const request = require("request");
const like = models.like;
const Music = models.Music;
let sess;

router.post("/like_music", function(req, res, next) {
	console.log("dedefef");
	res.type("json");
    sess = req.cookies;
    var userid = sess.userId;
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t like music'});
	} else {
		Music.find({
		"where": {
				idMusic: req.body.idMusic
			}
		}).then(musica => {
			if (musica){
				like.find({
				"where": {
						musicId: req.body.idMusic,
						likerId: userid
					}
				}).then(lUl => {
				if (lUl){
					like.destroy({
						"where": {
							musicId: req.body.idMusic,
							likerId: userid
						}
					})
					res.send({ msg: 'like destroy'});	
				} else {
					 return like.create({
							musicId : req.body.idMusic,
							likerId: sess.userId
						}).then(Like => {
							res.send({msg: Like});
						}).catch(err => { res.send({msg: 'nok', err: err}); });
					}
				}).catch(err => { throw err; });
			} else {
				res.send({ msg: 'the music chosen doesn\'t exist '});	
			}
		}).catch(err => { throw err; });
	}
});

module.exports = router;
