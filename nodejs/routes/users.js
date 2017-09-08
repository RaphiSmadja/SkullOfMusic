"use strict";

const express = require("express");
const cookieParser = require('cookie-parser');
const router = express.Router();
const models = require("../models");
const request = require("request");
const Music = models.Music;
const User = models.User;
const bcrypt = require("bcrypt");
const validator = require("validator");

let sess;

router.post("/login", function(req, res, next) {
	res.type("json");
	let emailSend = req.body.emailAddress1;
	let passwordSend = req.body.password1;
	User.find({
		where: {
			emailAddress: emailSend
		}
	}).then(user => {
		if (user){
			bcrypt.compare(passwordSend, user.hashedPassword, function(err, lol) {
				if(lol == true){
					sess = req.cookies;
					console.log(req.cookies);
					sess.userId = user.id;
					sess.emailAddress = user.emailAddress;
					sess.isAdmin = user.isAdmin;
					res.send({Session: sess, User: user, msg: 'You are connected'});
			    } else
				res.send({msg: 'Incorrect password'});
			});
		} else
			res.send({msg: 'Wrong login infos'});
	}).catch(err => { res.send({msg: 'nok', err: err}); });
});

router.get("/logout", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if(!sess.emailAddress){
		res.send({ msg: 'you are not connected'});
	} else {
		console.log(sess.emailAddress);
		res.clearCookie(sess.emailAddress);
		res.clearCookie(sess.userId);
		console.log("kokok");
		console.log("___ " +sess.emailAddress);
		console.log("___ " +sess.userId);
		res.send({msg: 'you are disconnected'});
	}
});

router.post("/registration", function(req, res){
	res.type("json");
	let mail1 = req.body.emailAddress1;
	let firstname1 = req.body.firstName1;
	let lastname1 = req.body.lastName1;
	let password1 = req.body.password1;
	let password2 = req.body.password2;
	let pseudo1 = req.body.pseudo1;
	console.log(mail1+"  "+firstname1+"  "+lastname1+"  "+password1+"  "+password2+"  "+pseudo1+"  ");
	if (!validator.isEmail(mail1)) {
		res.send({msg: "Error on email address"});
	} else if(firstname1.length<3 || lastname1.length <3) {
			res.send({msg: "Error Firstname or Lastname"});	
	} else if(password1.length<5 || password2.length <5) {
			res.send({msg: "Error The password"});	
	} else if(password1 != password2) {
			res.send({msg: "The password aren't same"});	
	} else {
		User.find({
			"where": {
				"emailAddress": req.body.emailAddress1
			}
		}).then(user => {
			if (user) {
				res.send({
					msg: "Email address is already used"
				});
			} else {
				User.create({
					emailAddress: mail1,
					firstName: firstname1,
					lastName: lastname1,
					hashedPassword: bcrypt.hashSync(password1, 5),
					pseudo: pseudo1,
					isAdmin: 0
				}).then(user => {
					if (user) {
						res.send({msg: "Success"});
					} else {
						res.send({msg: "Fail"});
					}
				}).catch(err => {
					res.send({err: err});
				});
			}
		}).catch(err => {
			res.send({msg: 'Unable to search user',err: err});
		});
	}
		
});

router.get("/checkIdentity", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if(!sess.emailAddress){
		res.send({ msg: 'You are not connected'});
	} else {
		User.find({
			"where" : {
				id:sess.userId
			}
		}).then(checkUse => {
			if(checkUse) {
				res.send({msg: checkUse});
			} else {
				res.send({
					msg: "We don't find this user"
				})
			}
		}).catch(err =>{
			res.send({
				msg: 'Unable to check user',
				err: err
			});
		});
	}
});

router.post("/editProfil", function(req, res){
	res.type("json");
	sess = req.cookies;
	let oldPassword = req.body.oldPassword;
	let newPassword = req.body.newPassword;
	if (!sess.emailAddress) {
		res.send({ msg: "You are not connected" });
	} else {
		User.findOne({
			"where": {
				"emailAddress": req.body.email1
			}
		}).then(user => {
			if(user) {
				if (!bcrypt.compareSync(oldPassword, user.hashedPassword)) {
					res.send({msg: "Old Password is not correct"});
				} else {
						console.log("dplzlpzefe");
					if (newPassword.length < 5 || newPassword == "") {
						res.send({msg: "New Password 5 char minimum"});
					} else {
						user.updateAttributes({
							pseudo: req.body.pseudo1,
							emailAddress: req.body.email1,
							lastName: req.body.lastName1,
							firstName: req.body.firstName1,
							hashedPassword: bcrypt.hashSync(newPassword, 5),
						});
						res.send({msg: "User has been modified"});
					}
				}
			}
		}).catch(err =>{
			res.send({
				msg: 'Unable to update information user',err: err
			});
		});
	}
});

router.get("/admin/displayAllUsers", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if(sess.isAdmin != 1){
		res.send({ msg: 'You are not admin'});
	} else {
		User.findAll({
			attributes: ['id', 'pseudo', 'emailAddress', 'firstName' ,'lastName', 'isAdmin', 'createdAt', 'deletedAt']
		}).then(displayUser => {
			if(displayUser) {
				res.send({msg: displayUser});
			} else {
				res.send({
					msg: "We don't find all users"
				})
			}
		}).catch(err =>{
			res.send({
				msg: 'Unable to display list of users',
				err: err
			});
		});
	}
});

router.post("/admin/addUserAdmin", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if (sess.isAdmin != 1) {
		res.send({ msg: "You are not admin" });
	} else {
		User.findOne({
			"where": {
				"emailAddress": req.body.email1
			}
		}).then(user => {
			if(user) {
					user.updateAttributes({
						isAdmin: 1
					});
					res.send({msg: "User is Admin"});
				}
		}).catch(err =>{
			res.send({
				msg: 'Unable to update information user',err: err
			});
		});
	}
});

router.post("/admin/demotionAdmin", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if (sess.isAdmin != 1) {
		res.send({ msg: "You are not admin" });
	} else {
		User.findOne({
			"where": {
				"emailAddress": req.body.email1
			}
		}).then(user => {
			if(user) {
					user.updateAttributes({
						isAdmin: 0
					});
					res.send({msg: "User is now not Admin"});
				}
		}).catch(err =>{
			res.send({
				msg: 'Unable to update information user',err: err
			});
		});
	}
});

router.post("/admin/banishUser", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if (sess.isAdmin != 1) {
		res.send({ msg: "You are not admin" });
	} else {
		User.destroy({
			"where": {
				"emailAddress": req.body.email1
			}
		}).then(user => {
			if(user) {	
				res.send({msg: "User is deleted"});
			} else {
				res.send({msg: "erreur"});
			}
		}).catch(err =>{
			res.send({
				msg: 'Unable to delete user',err: err
			});
		});
	}
});

router.post("/admin/deleteMusic", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if (sess.isAdmin != 1) {
		res.send({ msg: "You are not admin" });
	} else {
		Music.destroy({
			"where": {
				"idMusic": req.body.idMuse
			}
		}).then(musicDelete => {
			if(musicDelete) {	
				res.send({msg: "Music is deleted"});
			} else {
				res.send({msg: "erreur"});
			}
		}).catch(err =>{
			res.send({
				msg: 'Unable to delete user',err: err
			});
		});
	}
});

router.post("/admin/restUser", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if (sess.isAdmin != 1) {
		res.send({ msg: "You are not admin" });
	} else {
		User.findOne({
			"where": {
				"emailAddress": req.body.email1
			}
		}).then(user => {
			if(user) {
					user.updateAttributes({
						deletedAt: "NULL"
					});
					res.send({msg: "User is now not Admin"});
				}
		}).catch(err =>{
			res.send({
				msg: 'Unable to update information user',err: err
			});
		});
	}
});

router.post("/admin/restUser", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if (sess.isAdmin != 1) {
		res.send({ msg: "You are not admin" });
	} else {
		User.findOne({
			"where": {
				"emailAddress": req.body.email1
			}
		}).then(user => {
			if(user) {
					user.updateAttributes({
						deletedAt: "NULL"
					});
					res.send({msg: "User is now not Admin"});
				}
		}).catch(err =>{
			res.send({
				msg: 'Unable to update information user',err: err
			});
		});
	}
});

module.exports = router;