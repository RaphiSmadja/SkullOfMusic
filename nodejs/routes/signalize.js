"use strict";

const express = require("express");
let session = require('express-session');
const router = express.Router();
const models = require("../models");
const request = require("request");
const Signalize = models.signalize;
const Music = models.Music;
const User = models.User;
let sess;

router.post("/signalize_music", function(req, res, next) {
	console.log("dedefef");
	res.type("json");
    sess = req.cookies;
    var userid = sess.userId;
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t signalize music'});
	} else {
		Music.find({
			"where": {
				"idMusic": req.body.idMusic
			}
		}).then(musica => {
			if (musica) {
				Signalize.create({
					musicId: req.body.idMusic,
					flagmanId: userid
				}).then(signalizer => {
					if (signalizer) {
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

router.get("/display_music_signalized", function(req, res) {
	console.log("yello");
	Signalize.belongsTo(Music,{foreignKey: 'musicId', targetKey: 'idMusic'});
	Signalize.belongsTo(User,{foreignKey: 'flagmanId', targetKey: 'id'});
	Music.belongsTo(User,{foreignKey: 'ownerIdMusic', targetKey: 'id'})
	Signalize.findAll({
		include: [
			{
		    	model: Music, 
	      		include: [
	        		User
	      		]
	      	}, 
	      	{
	      		model: User,
	      		attributes : ['pseudo']
	      	}
      	]
	}).then(displayEveryThing => {
		if(displayEveryThing) {
			res.send({msg: displayEveryThing});
		} else {
			res.send({
				msg: "We don't find"
			})
		}
	}).catch(err =>{
		res.send({
			msg: 'Unable to display list of users signalize',
			err: err
		});
	});
});
module.exports = router;
