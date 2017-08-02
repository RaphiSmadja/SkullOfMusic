"use strict";

const express = require("express");
let session = require('express-session');
const router = express.Router();
const models = require("../models");
const request = require("request");
const Music = models.Music;
let sess;

router.post('/add_music', (req, res) => {
    res.type('json');
    sess = req.session;
    let music_title = req.body.title;
    let music_artist = req.body.artist;
    let music_pathMusic = req.body.pathMusic;
    let music_pathPicture = req.body.pathPicture;
    let music_gender = req.body.gender;
    if(!sess.emailAddress) {
		res.json({ msg: 'you are not connected so you can\'t add music'});
	} else {
		if(music_pathMusic && music_title)
        Music.find({
            "where": {
        	$or: [
        	    {
        	      title: {
        	      	$eq : music_title
        	      }
        	    },
        	    {
        	      pathMusic: {
        	      	$eq : music_pathMusic
        	      }
        	    }
        	  ]
            }
        }).then(musicos => {
            if(musicos)
                res.json({ msg: 'Music already existed', err: {musicos} });
            else
                Music.create({
                    ownerIdMusic: sess.userId,
                    title: music_title,
                    artist: music_artist,
                    pathMusic: music_pathMusic,
                    pathPicture: music_pathPicture,
                    gender: music_gender
                }).then(muse => {
                    if(muse)
                        res.json({
						message: "Success"
					});
                    else
                        res.json({ error: 'Error while creating music' });
                }).catch(err => { throw err; });
        }).catch(err => { res.json({ error: 'Error...', err: err }); });
    else
        res.json({ msg: 'Bad entry !' });
	}
});

router.post("/search_by_gender", function(req, res, next) {
	res.type("json");
	sess = req.session;
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t search'});
	} else {
		Music.findAll({
			"where": {
				      gender: req.body.gender
				  }
		}).then(musictype => {
			if (musictype){
				console.log("we find")
				res.send({msg: musictype});
			}
			else {
				console.log("we don't find")
				res.send({msg: 'We don\'t find !'});
			}
		}).catch(err => { throw err; });
	}
})

//musique par genre de moins d'une semaine
router.post("/search_by_gender_news", function(req, res, next) {
	res.type("json");
	sess = req.session;
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t search'});
	} else {
		Music.findAll({
			"where": {
				      gender: req.body.gender,
				      createdAt: {
				          $gt: new Date((new Date() - 168 * 60 * 60 * 1000))
				        }
				  }
		}).then(musictype => {
			if (musictype){
				console.log("we find")
				res.send({msg: musictype});
			}
			else {
				console.log("we don't find")
				res.send({msg: 'We don\'t find !'});
			}
		}).catch(err => { throw err; });
	}
})

module.exports = router;
