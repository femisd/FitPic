//Imports.
const functions = require('firebase-functions');
const admin = require("firebase-admin");
const fs=require('fs'); 
const nodemailer = require('nodemailer');

admin.initializeApp(functions.config().firebase);

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

	admin.database().ref('users').child(user.uid).set({
		mCaloriesBurned : 0,
    	mFollowers : 0,
    	mFollowing : 0,
    	mLocation : "Guildford, UK",
    	mPhotos : 0,
    	mPoints : 0,
    	mProfilePicture : "",
    	mSteps : 0,
    	mUid : user.uid,
    	mUsername : "",
    	mVIP : false
	});
return null; 
});

// Send notifications 
//exports.sendNotification = functions.firestore.document("Users/{user_id}/Notification/{notification_id}").onWrite(event =>{

//  const user_id = event.params.user_id;
//  const notification_id = event.params.notification_id; 

//  console.log("User ID: " + user_id + " | Notification ID :" + notification_id);
  

//});
