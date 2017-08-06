const express = require('express');
const path = require('path');
const favicon = require('serve-favicon');
const logger = require('morgan');
const cookieParser = require('cookie-parser');
const bodyParser = require('body-parser');
const net = require('net');
const http = require('http');

const express_session = require('express-session');
const index = require('./routes/index');
const users = require("./routes/users");
const music = require("./routes/music");
const likeUnlike = require("./routes/likeUnlike");

const models = require("./models");
models.sequelize.sync();

let port_app = '3001';
let port_server = '3002';
let server = net.createServer();

var app = express();

app.use(express_session({
    secret: 'AsdSDFgGlDSfAOsfgrgZaFznSndjgdC',
    resave: true,
    saveUninitialized: true
}));

server.listen(port_server, function () {
    console.log('server nodejs started at port ' + port_server + '...');
});
app.listen(port_app, function () {
    console.log('App nodejs started at port ' + port_app + '...');
});
app.use('/', index);
app.use("/users", users);
app.use("/music", music);
app.use("/likeUnlike", likeUnlike);
server.on('connection',function(socket){

  console.log("connection de "+socket.localAddress+":"+socket.localPort+"\n");

  socket.write('{"status":"ok"}\n');

  //pour une raison ou pour une autre la deco déclanche une erreur
  
  socket.on('data', function(data){
    console.log(data);
    let entree = data.toString('utf8');//convertie l'entré du client connecté en string
    console.log(entree);
    console.log("\n\nreceived from client "+socket.localAddress+"\n"+entree);
    try {
        let json = JSON.parse(entree.split("\n",1));//convertie l'entré string en objet JSON
        let action=json.action;
        console.log(action);
        console.log(json);

        //si le joueur désir créer une partie. attend en JSON l'index de la map.
        //retourne l'id de la map que le client devra utiliser pour le rejoindre juste après
        //et à chaque fois qu'il souhaite bouger un player dessus
        if(action=="registerMember"){
          try{
            app.use("/users/registration", users);
          }catch(e){
            console.log("erreur lors de la creation d'un user : "+e+"\n");
            socket.write('{"erreur":"erreur lors d\'un user"}');
          }

        }
      } catch(e){
        console.log
      }
  });
  
  socket.on('error', function(err) {

  });
   socket.on('close',function(data){
    console.log("dede");
  });
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});
