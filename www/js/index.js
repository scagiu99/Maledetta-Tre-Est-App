function showScreen(idscreen) {
  $(".screen").hide();
  $(idscreen).show();
}

function showShortToast(text) {
  window.plugins.toast.showShortBottom(text, null, null);
}

document.addEventListener("deviceready", onDeviceReady, false);
document.addEventListener("offline", checkConnection, false);

function checkConnection() {
  showShortToast("Connessione internet assente");
}

// Funzione che inizializza gli eventi degli elementi statici
function inizializeEvent() {
  // Elementi per il cambio di schermata
  $(".changeBoardScreen").on("click", onCreateBoardScreen);
  $(".changeLineScreen").on("click", onCreateLineScreen);
  $(".changeProfileScreen").on("click", onCreateProfileScreen);
  $("#addPostButton").on("click", onClickAddPost);
  //addPostButton
  $("#backButtonPost").on("click", onCreateBoardScreen);
  $("#backButtonMap").on("click", onCreateBoardScreen);
  $("#backButtonOfficial").on("click", onCreateBoardScreen);
  $("#aggiungiPostButton").on("click", addNewPost);
  //Profile button
  $("#changeImage").on("click", onClickChangeImage);
  $("#saveName").on("click", setNameProfile);
  //Change Bacheca
  $(".changeDirection").on("click", changeDirection);
  //Show Mappa
  $(".showMap").on("click", onClickShowMap);
}
function onDeviceReady() {
  $("body").show();
  model = new Model();
  communicationController = new CommunicationController();

  inizializeEvent();
  if (localStorage.getItem("did") == undefined) {
    onCreateLineScreen();
  } else {
    let did = localStorage.getItem("did");
    let direction = localStorage.getItem("direction");
    onCreateBoardScreen(did, direction);
  }

  console.log("Running cordova-" + cordova.platformId + "@" + cordova.version);
}
