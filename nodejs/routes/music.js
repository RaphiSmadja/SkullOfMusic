"use strict";

const express = require("express");
const cookieParser = require('cookie-parser');
const router = express.Router();
const models = require("../models");
const request = require("request");
const Music = models.Music;
const User = models.User;
const Signalize = models.signalize;
const Like = models.like;
const HeartStroke = models.heartStroke;
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
	console.log(req.cookies);
	console.log("title " +req.body.title1);
	console.log("artist " +req.body.artist1);
	console.log("gender " +req.body.gender1);
	console.log("file path " +req.file.path);
	console.log("file size " +req.file.size);
	req.file.path = "file:///C:/Users/raphi//Desktop/Rattrapage/nodejs/ressources/musics/songs/"+req.file.filename;
    sess = req.cookies;
    var userid = sess.userId;
    let music_title = req.body.title1;
    let music_artist = req.body.artist1;
    let music_pathMusic = req.file.path;
    let music_gender = req.body.gender1;
    if(!sess.emailAddress){
		res.send({ msg: 'you are not connected'});
	} else {
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
				res.send({msg: "music is already used"});
			} else {
				Music.create({
					ownerIdMusic: userid,
	                title: music_title,
	                artist: music_artist,
	                pathMusic: music_pathMusic,
	                gender: music_gender
				}).then(user => {
					if (user) {
						res.json({msg: "Success"});
					} else {
						res.json({msg: "Fail"});
					}
				}).catch(err => {
					res.json({err: err});
				});
			}
		});
	}
});


router.get("/display_Allmusic", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if(!sess.emailAddress){
		res.send({ msg: 'you are not connected so you can\'t display'});
	} else {
		Music.findAll().then(allMusic => {
			if (allMusic){
				console.log("we find");
				console.log(allMusic);
				res.send({msg: allMusic});
			}
			else {
				console.log("we don't find")
				res.send({msg: 'We don\'t find !'});
			}
		}).catch(err => { throw err; });
	}
});

//musique par genre de moins d'une semaine
router.post("/search_by_gender_news", function(req, res, next) {
	res.type("json");
	sess = req.cookies;
	if(!sess.emailAddress){
		res.send({ msg: 'you are not connected'});
	} else {
		Music.findAll({
			"where": {
				      gender: req.body.gender1,
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
	sess = req.cookies;
	console.log(sess.emailAddress);
	console.log(" ___ " + sess.emailAddress);
	console.log(sess.userId);
	console.log(sess.lastName);
	
	if(!sess.emailAddress){
		res.send({ msg: 'you are not connected so you can\'t display'});
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
});

/** directory **/

router.get("/directory/display_music_added", function(req, res) {
	sess = req.cookies;
	console.log(sess.userId);
	Music.findAll({
		"where": {
			ownerIdMusic: 5
		}
	}).then(displayEveryThingDirectory => {
		if(displayEveryThingDirectory) {
			res.send({msg: displayEveryThingDirectory});
		} else {
			res.send({
				msg: "We don't find"
			})
		}
	}).catch(err =>{
		res.send({
			msg: 'Unable to display list of music added',
			err: err
		});
	});
});

router.get("/directory/display_music_liked", function(req, res) {
	sess = req.cookies;
	console.log(sess.userId);
	Like.belongsTo(User,{foreignKey: 'likerId', targetKey: 'id'});
	Like.belongsTo(Music,{foreignKey: 'musicId', targetKey: 'idMusic'});
	Like.findAll({
		"where": {
			likerId: 5
		},
		include: [
			{
		    	model: Music,
		    	attributes : ['title','artist','pathMusic','gender']
	      	}
	    ]
	}).then(displayEveryThingDirectory => {
		if(displayEveryThingDirectory) {
			res.send({msg: displayEveryThingDirectory});
		} else {
			res.send({
				msg: "We don't find"
			})
		}
	}).catch(err =>{
		res.send({
			msg: 'Unable to display list of music liked',
			err: err
		});
	});
});

router.get("/directory/display_music_heartstroke", function(req, res) {
	sess = req.cookies;
	console.log(sess.userId);
	HeartStroke.belongsTo(User,{foreignKey: 'flagmanId', targetKey: 'id'});
	HeartStroke.belongsTo(Music,{foreignKey: 'musicId', targetKey: 'idMusic'});
	HeartStroke.findAll({
		"where": {
			flagmanId: 5
		},
		include: [
			{
		    	model: Music,
		    	attributes : ['title','artist','pathMusic','gender']
	      	}
	    ]
	}).then(displayEveryThingDirectoryStroke => {
		if(displayEveryThingDirectoryStroke) {
			res.send({msg: displayEveryThingDirectoryStroke});
		} else {
			res.send({
				msg: "We don't find"
			})
		}
	}).catch(err =>{
		res.send({
			msg: 'Unable to display list of music heart',
			err: err
		});
	});
});
module.exports = router;
