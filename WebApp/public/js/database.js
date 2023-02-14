// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.13.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.13.0/firebase-analytics.js";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyBqkX6_iG60ijhP8ugTv9KTsE1n3Yj5b7A",
  authDomain: "graduationproject-391b2.firebaseapp.com",
  databaseURL: "https://graduationproject-391b2-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "graduationproject-391b2",
  storageBucket: "graduationproject-391b2.appspot.com",
  messagingSenderId: "938688812975",
  appId: "1:938688812975:web:3f40409d583224003c815a",
  measurementId: "G-434FM5Y3FN"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

export { app, analytics };