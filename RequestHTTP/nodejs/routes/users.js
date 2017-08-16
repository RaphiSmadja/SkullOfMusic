"use strict";

const session = require("express-session");
const express = require("express");
const router = express.Router();
const models = require("../models");
const User = models.User;
//const bcrypt = require("bcrypt");
const validator = require("validator");
const Music = models.Music;

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
			if(passwordSend == user.hashedPassword){
				sess = req.session;
				sess.userId = user.id;
                sess.firstName = user.firstName;
         		sess.lastName = user.lastName;
				sess.emailAddress = user.emailAddress;
				sess.isAdmin = user.isAdmin;
				res.json({Session: sess, User: user, msg: 'you are connected'});
			} else {
				res.json({msg: 'wrong login infos'});
			}
			/*bcrypt.compare(passwordSend, user.hashedPassword, function(err, lol) {
				if(lol == true){
					sess = req.session;
					sess.userId = user.id;
                    sess.firstName = user.firstName;
           			sess.lastName = user.lastName;
					sess.emailAddress = user.emailAddress;
					sess.isAdmin = user.isAdmin;
					res.json({Session: sess, User: user, msg: 'you are connected'});
			    	} else
					res.json({msg: 'wrong login infos'});
			});*/
		} else
			res.json({msg: 'wrong login infos'});
	}).catch(err => { res.json({msg: 'nok', err: err}); });
});

router.get("/logout", function(req, res) {
	res.type("json");
	console.log(sess.emailAddress);
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t you connected '});
		console.log("you're not connected");
	} else {
		console.log(sess.emailAddress);
		req.session.destroy(err => {
            		if(err)
                	res.json({ msg: 'Error while trying to disconnect !', err: err });
            		else
                	res.json({ msg: 'You are disconnected !', });
        	});
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
								hashedPassword: /*bcrypt.hashSync(*/password1/*, 5)*/,
								picture: "",
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

router.post("/editPassword", (req, res, next) => {
	res.type("json");
	if (!sess) {
		res.json({
			message: "You are not connected"
		});
	}

	if (req.body.newpassword1 !== req.body.newpassword2) {
		res.json({
			message: "Passwords are not equals"
		});
	}

	User.find({
		where: {
			emailAddress : req.body.emailAddress
		}
	}).then(user => {
		if (user) {
			if (!bcrypt.compareSync(req.body.actual, user.hashedPassword)) {
				res.json({
					message: "Password is not correct"
				});
			}

			user.updateAttributes({
				hashedPassword: bcrypt.hashSync(req.body.password1)
			});

			res.json({
				message: "Success"
			});
		} else {
			res.json({
				message: "Email address doesn't exists",
			});
		}
	}).catch (err => {
		res.json({
			err: err
		});
	});
});

router.post("/edit", (req, res, next) => {
	res.type("json");

	if (!sess) {
		res.json({
			message: "You are not connected"
		});
	}

	User.findOne({
		"where": {
			"emailAddress": req.body.existingEmailAddress
		}
	}).then(user => {
		if (user) {
			user.updateAttributes({
				handle: req.body.handle,
				emailAddress: req.body.emailAddress,
				lastName: req.body.lastName,
				firstName: req.body.firstName,
				phoneNumber: req.body.phoneNumber,
				newsletter: req.body.newsletter,
				picture: req.body.picture
			});
			res.json({
				message: "User has been modified"
			});
		} else {
			res.json({
				message: "User doesn't exists"
			});
		}
	}).catch(err => {
		res.json({
			message: 'Unable to edit user',
			err: err
		});
	});
});

router.get("/display_music", function(req, res) {
	res.type("json");
	console.log(sess.emailAddress);
	console.log(" ___ " + sess.emailAddress);
	console.log(sess.userId);
	console.log(sess.lastName);
	if(!sess.emailAddress){
		res.json({ msg: 'you are not connected so you can\'t display'});
	} else {
		console.log(new Date((new Date() - 168 * 60 * 60 * 1000)));
		Music.findAll({
			"where": {
				      createdAt: {
				          $gt: new Date((new Date() - 168 * 60 * 60 * 1000))
				        }
				  }
		}).then(musictype => {
			if (musictype){
				console.log("we find " + musictype);
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