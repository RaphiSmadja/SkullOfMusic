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
					res.json({Session: sess, User: user, msg: 'you are connected'});
			    	} else
					res.json({msg: 'wrong login infos'});
			});
		} else
			res.json({msg: 'wrong login infos'});
	}).catch(err => { res.json({msg: 'nok', err: err}); });
});

router.get("/logout", function(req, res) {
	 res.type("json");
	 console.log("dedef");
	 console.log("___ " +req.cookies);
	 sess = req.cookies;
	console.log(" ___ " + sess.emailAddress);
	console.log(sess.userId);
	console.log(sess.lastName);
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t you connected '});
		console.log("you're not connected");
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
;
router.post("/registration", function(req, res){
	let mail1 = req.body.emailAddress1;
	let firstname1 = req.body.firstName1;
	let lastname1 = req.body.lastName1;
	let password1 = req.body.password1;
	let password2 = req.body.password2;
	let pseudo1 = req.body.pseudo1;
	console.log(mail1+"  "+firstname1+"  "+lastname1+"  "+password1+"  "+password2+"  "+pseudo1+"  ");
	if (!validator.isEmail(mail1)) {
		res.json({
			message: "It's not a mail address"
		});
	} else if(firstname1.length<3 || lastname1.length <3) {
			res.json({
				message: "firstname or lastname is too small minimum 4"
			});	
		} else if(password1.length<5 || password2.length <5) {
				res.json({
					message: "The password size must be greater than 5"
				});	
			} else if(password1 != password2) {
					res.json({
						message: "The password aren't same"
					});	
				} else{
					User.find({
						"where": {
							"emailAddress": req.body.emailAddress1
						}
					}).then(user => {
						if (user) {
							res.json({
								message: "Email address is already used"
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
					}).catch(err => {
						res.json({
							msg: 'Unable to search user',
							err: err
						});
					});
				}
		
});

router.get("/checkIdentity", function(req, res) {
	res.type("json");
	sess = req.cookies;
	if(!sess.emailAddress){
		res.send({ msg: 'you are not connected'});
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
			res.json({
				message: 'Unable to check user',
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
						console.log("p)pfefe");
				if (!bcrypt.compareSync(oldPassword, user.hashedPassword)) {
					res.send({msg: "Old Password is not correct"});
				} else {
						console.log("dplzlpzefe");
					if (newPassword.length < 5 || newPassword == "") {
						console.log("pb");
						res.send({msg: "New Password 5 char minimum"});
					} else {
						console.log("defefe");
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

module.exports = router;