"use strict";

const express = require("express");
let session = require('express-session');
const router = express.Router();
const models = require("../models");
const request = require("request");
const likeUnlike = models.likeUnlike;
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
				likeUnlike.find({
				"where": {
						musicId: req.body.idMusic,
						likerUnlikerId: userid
					}
				}).then(lUl => {
				if (lUl){
					lUl.updateAttributes({like: 1})
					res.json({ msg: 'update like'});	
				} else {
					 return likeUnlike.create({
						musicId : req.body.idMusic,
						likerUnlikerId: sess.userId,
						like: 1
						}).then(Like => {
							res.send(Like);
						}).catch(err => { res.send({msg: 'nok', err: err}); });
					}
				}).catch(err => { throw err; });
			} else {
				res.json({ msg: 'the music chosen doesn\'t exist '});	
			}
		}).catch(err => { throw err; });
	}
})

router.post("/unlike_music", function(req, res, next) {
	res.type("json");
	sess = req.session;
	console.log(sess.id);
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t unlike music'});
	} else {
		Music.find({
		"where": {
				idMusic: req.body.idMusic
			}
		}).then(musica => {
			if (musica){
				likeUnlike.find({
				"where": {
						musicId: req.body.idMusic,
						likerUnlikerId: sess.userId
					}
				}).then(lUl => {
				if (lUl){
					lUl.updateAttributes({like: 0})
					res.json({ msg: 'update like'});	
				} else {
					 return likeUnlike.create({
						musicId: req.body.idMusic,
						likerUnlikerId: sess.userId,
						like: 0
						}).then(Like => {
							res.send(Like);
						}).catch(err => { res.send({msg: 'nok', err: err}); });
					}
				}).catch(err => { throw err; });
			} else {
				res.json({ msg: 'the music chosen doesn\'t exist '});	
			}
		}).catch(err => { throw err; });
	}
})

module.exports = router;
