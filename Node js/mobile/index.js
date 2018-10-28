
const mysql = require('mysql');
const express = require('express');
const bodyParser = require('body-parser');
const dateFormat = require('dateformat');
const nodemailer = require('nodemailer');


const app = express();

const connection = mysql.createConnection({host: "localhost",user: "root",password: "2758",database: "mobile"});

app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

app.listen(3000, function(){
    console.log("Port dinleniyor 3000...");
});

app.get('*', function(req, res){
   response.redirect("http://muhammederendursun.com/");
});

app.post('/api/server', function(req, res) {

  var serviceCheck = req.body.serviceCheck;

  if (serviceCheck=="com.med.splash") {
    connection.query("Select serviceCheck From main ", function (err, resultSelect, fields) {
          if (err){
            var response = [{serviceCheck: "false"}];
            res.send(response);
          }
          else {
            var response = [{serviceCheck: resultSelect[0]["serviceCheck"]}];
            res.send(response);
          }
    });
  }
  else {
    var response = [{serviceCheck: "false"}];
    res.send(response);
  }

});

app.post('/api/login', function(req, res) {
  const userMail = req.body.userMail;
  const userPassword = req.body.userPassword;
  const deviceToken = req.body.deviceToken;
  const appCheck = req.body.appCheck;
  var now = new Date();
  var timeNow = dateFormat(now, 'dd-mm-yyyy HH:MM:ss');

  if (userMail != null && userPassword != null && deviceToken != null && appCheck == "com.med.splash") {

        connection.query("Select userMail,userName,userPassword From users where userMail = ? and userPassword = ?",[userMail , userPassword], function (err, resultSelect, fields) {
              if (err){
                const response = [{status: false, message: 'Bağlantı sağlanamadı'}];
                res.send(response);
              }
              else if (resultSelect.length){
                  connection.query("UPDATE users SET deviceToken = ? , lastLogin = ? WHERE userMail = ?",[deviceToken , timeNow , resultSelect[0]["userMail"]], function (err, resultUpdate, fields) {
                      if (err){
                        const response = [{status: false, message: 'Bağlantı sağlanamadı'}];
                        res.send(response);
                      }
                      else {
                        resultSelect[0]["status"] = true;
                        resultSelect[0]["message"] = "Başarıyla giriş yapıldı";
                        resultSelect[0]["deviceToken"] = deviceToken;
                        resultSelect[0]["lastLogin"] = timeNow;
                        res.send(resultSelect);
                      }
                  });
                }
                else {
                  var response = [{status: false, message: 'Kullanıcı adı veya parola hatalı'}];
                  res.send(response);
                }
        });
    }
    else {
      const response = [{status: false, message: 'İzinsiz api kullanımı'}];
      res.send(response);
    }
});

app.post('/api/signup', function(req, res) {
  const userName = req.body.userName;
  const userMail = req.body.userMail;
  const userPassword = req.body.userPassword;
  const deviceToken = req.body.deviceToken;
  const appCheck = req.body.appCheck;
  var now = new Date();
  var timeNow = dateFormat(now, 'dd-mm-yyyy HH:MM:ss');

  if (userMail != null && userPassword != null && deviceToken != null && appCheck == "com.med.splash") {
      connection.query("Select id From users where userMail = ? ", [userMail], function (err, resultSelect, fields) {
              if (err){
                const response = [{status: false, message: 'Bağlantı sağlanamadı'}];
                res.send(response);
              }
              else if (resultSelect.length){

                const response = [{status: false, message: 'Kullanıcı adı kullanılmaktadır'}];
                res.send(response);

              }
              else {
                connection.query("INSERT INTO users SET  userMail = ?, userName = ?, userPassword= ?, deviceToken = ?, accountVerification = ?",[userMail , userName , userPassword, deviceToken , "false"], function (err, resultInsert, fields) {
                    if (err){
                        var response = [{status: false, message: 'Kayıt tamamlanamadı. Tekrar deneyin'}];
                        res.send(response);
                    }
                    else {
                      var response = [{status: true, message: 'Merhaba ' + userName +' giriş yapabilirsin'}];
                      res.send(response);

                    }
                });
              }
      });

    }
    else {
      const response = [{status: false, message: 'İzinsiz api kullanımı'}];
      res.send(response);
    }
});

app.post('/api/autologin', function(req, res) {
    const userMail = req.body.userMail;
    const userPassword = req.body.userPassword;
    const deviceToken = req.body.deviceToken;
    const appCheck = req.body.appCheck;
    var now = new Date();
    var timeNow = dateFormat(now, 'dd-mm-yyyy HH:MM:ss');

    if (userMail != null && userPassword != null && deviceToken != null && appCheck == "com.med.splash") {

      connection.query("Select id,deviceToken,userMail,userName From users where userMail = ? and userPassword = ?",[userMail , userPassword], function (err, resultSelect, fields) {
              if (err){
                const response = [{status: false, message: 'Bağlantı sağlanamadı'}];
                res.send(response);
              }
              else if (resultSelect.length){
                if (resultSelect[0]["deviceToken"]==deviceToken) {
                  connection.query("UPDATE users SET lastLogin = ? WHERE userMail = ?",[timeNow , resultSelect[0]["userMail"]], function (err, resultUpdate, fields) {
                      if (err){
                        const response = [{status: false, message: 'Bağlantı sağlanamadı'}];
                        res.send(response);
                      }
                      else {
                        const response = [{status: true, message: 'Merhaba ' + resultSelect[0]["userName"]}];
                        res.send(response);
                      }
                  });
                }
                else {
                  const response = [{status: "deviceError", message: 'Başka bir cihazdan hesabına giriş yapılmış'}];
                  res.send(response);
                }
              }
              else {
                const response = [{status: false, message: 'Kullanıcı adı veya parola hatalı'}];
                res.send(response);
              }
      });
    }
    else {
      const response = [{status: false, message: 'İzinsiz api kullanımı'}];
      res.send(response);
    }

});
