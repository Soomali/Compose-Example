const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const axios = require("axios");
const Vision = require("@google-cloud/vision");
const formidable = require("formidable-serverless");
const { Storage } = require("@google-cloud/storage");
const storage = new Storage();
const fs = require("fs");
const vision = new Vision.ImageAnnotatorClient();

const axiosBase = axios.default.create({
  baseURL: "https://backend.poolytech.com/",
});

exports.sendNotification = functions.https.onRequest(async (req, res) => {
  try {
    /*body has to have:
      type:String,
      payload:{
        text:String,
        ...additionalFields
      },
      deviceTokens:[String]

    */
    const notificationPayload = req.body;
    //TODO add APNs headers for IOS
    const payload = {
      android: {
        priority: "high",
      },
      apns: {
        payload: {
          aps: {
            contentAvailable: true,
          }
        },
        headers: {
          'apns-push-type': 'background',
          'apns-priority': '5',
          'apns-topic': 'org.poolytech', // your app bundle identifier
        },
      },
      data: { type: notificationPayload.type, ...notificationPayload.payload },
      tokens: notificationPayload.deviceTokens,
    };
    await admin.messaging().sendMulticast(payload);
    res.status(204).json();
  } catch (e) {
    res.status(400).json(e);
  }
});

const moreOrEqualThanPossible = (resp) => {
  return resp == "LIKELY" || resp == "VERY_POSSIBLE";
};
exports.sendImage = functions.https.onRequest(async (req, res) => {
  try {
    const allowedFilePaths = [
      "profilePictures",
      "messageImages",
      "communityImages",
    ];
    const { filePath, userID } = req.query;
    const accessToken = req.headers.authorization.split(" ")[1];
    if (!allowedFilePaths.includes(filePath)) {
      return res.status(400).json({
        error: "given filepath is not allowed. filepath was " + filePath,
      });
    }

    if (!accessToken) {
      return res.status(401).json("Authorization Failed");
    }
    const isAccessTokenValid = await axiosBase.get(
      "token/is-access-token-valid",
      {
        headers: { Authorization: "Bearer " + accessToken },
      }
    );

    if (!isAccessTokenValid) {
      return res.status(401).json("Authorization Failed");
    }
    const form = new formidable.IncomingForm();
    form.parse(req, async (err, fields, files) => {
      if (err) {
        return res.status(400).json({ error: "Could not handle request." });
      }
      try {
        const fileData = fs.readFileSync(files.file.path);
        const data = await vision.safeSearchDetection(fileData);

        const safeSearch = data[0].safeSearchAnnotation;
        if (
          moreOrEqualThanPossible(safeSearch.adult) ||
          moreOrEqualThanPossible(safeSearch.violence) ||
          moreOrEqualThanPossible(safeSearch.racy) ||
          moreOrEqualThanPossible(safeSearch.medical) ||
          moreOrEqualThanPossible(safeSearch.spoof)
        ) {
          return res.status(403).json({ error: "Resim uygunsuz içerik içeriyor." });
        }

        const bucket = storage.bucket("gs://pooly-tech.appspot.com");
        const options = {
          resumable: false,
          public: true,
          metadata: {
            contentType: "image/png",
          },
        };
        const time = new Date().getTime();
        let destination = filePath + "/" + userID;
        if (filePath == 'messageImages') {
          destination += time.toString();
        }
        const file = bucket.file(destination);
        return file.save(fileData, options).then(() => {
          res
            .status(200)
            .json(
              `https://storage.googleapis.com/${bucket.name}/${destination}?${time}`
            );
        });
      } catch (e) {
        functions.logger.error(e);
        res.status(500).json();
      }
    });
  } catch (e) {
    if (e.response) {
      return res.status(e.response.status).json(e.response.data);
    }
    res.status(400).json(e);
  }
});
