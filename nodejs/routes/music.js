"use strict";

const express = require("express");
let session = require('express-session');
const router = express.Router();
const models = require("../models");
const request = require("request");
const Music = models.Music;
const User = models.User;
var multer  = require('multer')

let sess;

// router.post('/api/musics/:id/upload', upload.single('resources'), (req, res) => {
// 	// req.file.mimetype conditionne si image ou son
// 	// req.file.(filename, destination) condition là où on sauve le fichier
// 	// update de la base avec le chemin du fichier
// })

var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, './ressources/musics/songs')
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname)
  }
})

var upload = multer({
   storage : storage,
   limits: { fileSize: 7340032 },
   fileFilter: function (req, file, cb) {
     if (file.originalname.match(/([\w-]|_|-)+.mp3/) == null) {
     	console.log("weche pb");
     	return cb(null, false, new Error('Attention to special characters'));
     }
     if (file.mimetype !== 'audio/mpeg') {
       	return cb(null, false, new Error('I don\'t have a clue!'));
     }
     cb(null, true);
   }

 }).single('fileMusic');


router.post('/api/musics',upload, function (req, res) {
	res.type('json');
	console.log("ee");
	console.log("title " +req.body.title1);
	console.log("artist " +req.body.artist1);
	console.log("gender " +req.body.gender1);
	console.log("ooeoeoe " +req.file.path);
	console.log("ooeoeoeassssssssssssssssssss " +req.file.size);
	console.log("ooeoeoeassssssssssssssssssss " +req.file.path);	
	req.file.path = "file:/home/raphael/Bureau/Rattrapage2/nodejs/ressources/musics/songs/"+req.file.filename;
    console.log("deededeeedeefefeffegeege");
    sess = req.session;
    var userid = sess.userId;
    let music_title = req.body.title1;
    console.log(music_title);
    let music_artist = req.body.artist1;
    let music_pathMusic = req.file.path;
    let music_gender = req.body.gender1;
    console.log("ldle");
    console.log("deefefef");
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
	}).then(tre => {
		if (tre) {
			res.json({
				message: "music is already used"
			});
		} else {
			console.log("ldlqqqqqqqqe");
			Music.create({
				ownerIdMusic: userid,
                title: music_title,
                artist: music_artist,
                pathMusic: music_pathMusic,
                gender: music_gender
			}).then(user => {
				if (user) {
					res.json({
						message: "Success"
					});
				} else {
					res.json({
						message: "Fail"
					});
				}
			}).catch(err => {
				res.json({
					err: err
				});
			});
		}
	});
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

//Display All News Music
router.get("/display_music", function(req, res) {
	res.type("json");
	sess = req.session;
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t display'});
	} else {
		Music.findAll({
			"where": {
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
