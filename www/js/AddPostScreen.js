//Mi creo la mia schermata con il profilo utente
let delayPost;
let statusPost;
let commentPost;
let did;

function onCreateAddPostScreen(didBacheca) {
  showScreen("#AddPostScreen");
  did = didBacheca;
  $("#commentEditText").val("");
  checkRadioButtonDelay();
}

function checkRadioButtonDelay() {
  if (document.getElementById("inOrario").checked) {
    delayPost = 0;
    document.getElementById("inOrario").checked = false;
    console.log("delayPost " + delayPost);
  } else if (document.getElementById("pochiMinuti").checked) {
    delayPost = 1;
    document.getElementById("pochiMinuti").checked = false;
    console.log("delayPost " + delayPost);
  } else if (document.getElementById("totMinuti").checked) {
    delayPost = 2;
    document.getElementById("totMinuti").checked = false;
    console.log("delayPost " + delayPost);
  } else if (document.getElementById("soppressi").checked) {
    delayPost = 3;
    document.getElementById("soppressi").checked = false;
    console.log("delayPost " + delayPost);
  } else {
    delayPost = null;
    console.log("delayPost " + delayPost);
  }
  checkRadioButtonStatus();
}

function checkRadioButtonStatus() {
  if (document.getElementById("ideale").checked) {
    statusPost = 0;
    document.getElementById("ideale").checked = false;
    console.log("statusPost " + statusPost);
  } else if (document.getElementById("accettabile").checked) {
    statusPost = 1;
    document.getElementById("accettabile").checked = false;
    console.log("statusPost " + statusPost);
  } else if (document.getElementById("graviProblemi").checked) {
    statusPost = 2;
    document.getElementById("graviProblemi").checked = false;
    console.log("statusPost " + statusPost);
  } else {
    statusPost = null;
    console.log("statusPost " + statusPost);
  }
}

function addNewPost() {
  let sid = localStorage.getItem("sid");
  commentPost = $("#commentEditText").val();
  $("#commentEditText").val("");
  checkRadioButtonDelay();

  if (delayPost === null && statusPost === null && commentPost === "") {
    console.log(
      typeof delayPost + " - " + typeof statusPost + " - " + typeof commentPost
    );
    showShortToast("Non puoi pubblicare un post vuoto!");
  } else if (commentPost.length > 100) {
    showShortToast("Il commento non può essere più lungo di 100 caratteri!");
  } else {
    communicationController.addPost(
      sid,
      did,
      delayPost,
      statusPost,
      commentPost,
      (response) => {
        console.log("Response", "Model size posts: " + model.getSizePosts());
        onCreateBoardScreen(did);
      },
      (error) => {
        console.log(
          "addPost",
          delayPost + " " + statusPost + " " + commentPost
        );
        showShortToast("Errore di caricamento");
      }
    );
  }
}
