const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const _firestoreref =  admin.firestore();

exports.NewUser = functions.auth.user().onCreate(async (user) => {
  var name = user.displayName ;
  if(user.displayName == null){
    name = 'Pati Kullanıcısı';
  }
  /*
  privacySettings is an indexed array which means that every index has a meaning.
  values -> 0 = Show to only myself  || 1 = show only not blocked || 2 = show everyone
  index 0 = Profile setting
  index 1 = Photo setting
  */
  const userdata = {
    'deviceToken':"",
    'name':name,
    'bio':"",
    "privacySettings":[2,2],
    'photoUrl':'',
    'usertype':0,
  };
  const userRef = _firestoreref.collection('Users').doc(user.uid);
  userRef.set(userdata).then(res => {
    functions.logger.info('New user named: ',user.displayName,' has been created,',res.writeTime);
    return;
  }).catch(err =>{
    functions.logger.error('User named',user.displayName,' could not created, error:',err);
    return;
  });
});

function DeleteAllReferenceInCollection(collection,param,reference) {
  _firestoreref.collection(collection).where(param,'==',reference).get().then(snaps => {
    snaps.docs.forEach(documentSnapshot => {
      documentSnapshot.ref.delete();
    });
  });
}
function UpdateVotesAndDelete(votecollection,affectedcollection,param,reference,uid){
  _firestoreref.collection(votecollection).where(param,'==',reference).get().then(snaps =>{
    snaps.docs.forEach(documentSnapshot =>{
      const votedata =  documentSnapshot.data();
      //update the comment according to the users votedata....
      //I do not know the data structure of it so it remains undeleted
      const comment = votedata.comment;
      functions.logger.log(votedata);
      functions.logger.log(votedata.comment);
      functions.logger.log(comment);
    });
  }).catch(err =>{
    functions.logger.error('An error occured when deleting records and updating ',votecollection ,' of ',uid,', error: ',err);
  }).then(res =>{functions.logger.log( votecollection,' records of ',uid,' deleted and updated with succes',res)});
}
exports.deleteUser = functions.auth.user().onDelete(async (user) => {
  const userref = (await _firestoreref.collection('Users').doc(user.uid).get()).ref;
  DeleteAllReferenceInCollection('Favorites','user',userref);
  UpdateVotesAndDelete('Votes','Comments','user',userref,user.uid);
  //update this.
  // _firestoreref.collection('Votes').where('user','==',userref).get().then(snaps =>{
  //   snaps.docs.forEach(documentSnapshot =>{
  //     const votedata =  documentSnapshot.data();
  //     //update the comment according to the users votedata....
  //     //I do not know the data structure of it so it remains undeleted
  //     const comment = votedata.comment;
  //     functions.logger.log(votedata);
  //     functions.logger.log(votedata.comment);
  //     functions.logger.log(comment);
  //   });
  // }).catch(err =>{
  //   functions.logger.error('An error occured when deleting votes of ',user.uid,', error: ',err);
  // }).then(res =>{functions.logger.log('Votes deleted with succes',res)});
  

  /* 

  DeleteAllReferenceInCollection('Favorites','user',userref);
  DeleteAllReferenceInCollection('Favorites','user',userref);
  DeleteAllReferenceInCollection('Favorites','user',userref);

  */




});
exports.onFavoriteSeen = functions.firestore.
    document("Animals/{animal}").
    onUpdate(async (snapshot,context) => {
      console.log(context.timestamp);
      const animalreference =  snapshot.ref;
      const animaldata = snapshot.after.data();
      //for debug
      functions.logger.info('DEBUG ONLY: ',animaldata);
      
      console.log('There is a change in animal ',animaldata.name,' referenced at:',animalreference);
      const favoritesCollection = _firestoreref.collectionGroup('Favorites');
      const users = await favoritesCollection.where('animal','==',animalreference).get();
      
      if(!users.empty){
      console.log(users.size,' Users have added this animal to their favorites list');

      const payload = {notification:{
        title: 'Favori Hayvanlarından biri görüldü !',
        body: animaldata.name + ' ' + animaldata.lastseen + ' tarihinde görüldü !',
        sound: 'default'},
        data:{
            click_action:'FLUTTER_NOTIFICATION_CLICK',
            animal: animalreference,
            seenpositions: animaldata.seenpositions,
        },
     };

      users.forEach(documentSnapshot => {
        const userdata = documentSnapshot.data();
       admin.messaging().sendToDevice(userdata._fieldsproto.devicetoken.stringValue,payload);
      });
      }
    });



//Have not deployed this yet lol.
/* 
exports.OnCommentVote = functions.firestore.document("Animals/{Animal}/{comment}").onUpdate((snapshot,context) =>{
  functions.logger.log(context.timestamp);
  const data = snapshot.after.data();
  const beforedata = snapshot.before.data();
  if(data.points > beforedata.points){
  const payload = {notification:{
    title: 'Bir yorumuna beğeni geldi!',
    body: 'Yorumunun toplamda '+data.points+' puanı oldu !',
    sound: 'default'},
    data:{
        click_action:'FLUTTER_NOTIFICATION_CLICK',
        animal: data.animal,

    },
 };
  data.user.get().then(documentreference =>
    {admin.messaging().sendToDevice(documentreference.data().devietoken,payload);
    });
  }
});
*/

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
