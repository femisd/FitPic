//Imports.
const functions = require('firebase-functions');
const admin = require("firebase-admin");
const fs=require('fs'); 
const nodemailer = require('nodemailer');

admin.initializeApp();

//Define email variables.
const gmailEmail = "team17.uos@gmail.com";
const gmailPassword = "Team17inches-";
const mailTransport = nodemailer.createTransport({
  service: 'gmail', //use Gmail as the service.
  auth: {
    user: gmailEmail,
    pass: gmailPassword,
  },
});

//Locate HTML file.
var htmlmail=fs.readFileSync("welcome.html","utf-8").toString();

//Structure email.
exports.sendWelcomeEmail = functions.auth.user().onCreate((user) => {
    const recipent_email = user.email; 
   
    const mailOptions = {
        from: '"FitPic" <admin@fitpic.com>',
        to: recipent_email,
        subject: 'Welcome to FitPic!',
         html: htmlmail
    };
    
  //Try to send the email.  
  try {
    mailTransport.sendMail(mailOptions);
    console.log('mail send');
    
  } catch(error) {
 	//Display error if email fails.
    console.error('There was an error while sending the email:', error);
  }
return null; 
});
