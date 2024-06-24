var uidsProfileImageMancanti;

function onCreateBoardScreen(did, direction) {
  showScreen("#BoardScreen");
  if (localStorage.getItem("did") == undefined) {
    onCreateLineScreen();
  }
  $("#boardTitle").text(direction);
  if (did != localStorage.getItem("did")) {
    did = localStorage.getItem("did");
  }
  getDetailStations(did);
  getDetailsPosts(did);
  getDetailsOfficialPosts(did);
  currentlyLocation();
}

function getDetailsPosts(did) {
  $("#postsList").empty();
  model.clearPosts();
  uidsProfileImageMancanti = new Map();
  let sid = localStorage.getItem("sid");

  communicationController.getPosts(
    sid,
    did,
    (response) => {
      console.log(response.posts);
      postResponse(response);
    },
    (error) => {
      showShortToast("Errore nel caricamento");
      console.error(error);
    }
  );
}

function postResponse(networkResponse) {
  let arrPosts = networkResponse.posts;
  console.log("JSON" + arrPosts);

  for (let post of arrPosts) {
    console.log("JSON" + arrPosts);
    //Aggiungo il post al model
    let nuovoPost = model.createPostObject(post);
    model.addPost(nuovoPost);

    $("#postsList").append(generatePostView(nuovoPost));
    eventPostFollow(post);

    //Aggiungo la profile picture (VUOTA) al model (Struttura dati SET)
    let nuovaProfilePictureUtente = new Author(post.author);
    model.addUserProfilePicture(nuovaProfilePictureUtente);

    if (!model.isProfilePictureInModelUpdated(post.pversion, post.author)) {
      uidsProfileImageMancanti.set(post.author, post.pversion);
    } else {
      //Entro qui anche quando non è stata impostata un immagine
      console.log("debugging prendo dal model");
      displayProfileImageFromSource(
        post.author,
        model.getProfilePicture(post.author)
      );
    }
  }
  //Richiedo le immagini mancanti
  for (let [uid, pversion] of uidsProfileImageMancanti.entries()) {
    getProfilePictureAndViewWhenReady(uid, pversion);
  }
}

function generatePostView(post) {
  let content = updateContent(post);
  if (post.followingAuthor) {
    return (
      '<li data-pid="' +
      post.authorUid +
      '"class="list-group-item post">' +
      '<div class="container" style="background-color:#FFF9C8"><p class="profilePostName"><b>' +
      post.authorName +
      '</b><span class="time-right"><button type="button" id="' +
      post.authorUid +
      '" class="btn btn-secondary unfollow">Non seguire</button></span></p><img data-uid="' +
      post.authorUid +
      '" class="profilePostImage rounded-circle" src="./img/profilo.jpeg">' +
      '<p class="text-break postText">' +
      content +
      '</p> <span class="time-right">' +
      post.datetime +
      "</span></div></li>"
    );
  } else {
    return (
      '<li data-pid="' +
      post.authorUid +
      '"class="list-group-item post">' +
      '<div class="container"><p class="profilePostName"><b>' +
      post.authorName +
      '</b><span class="time-right"><button type="button" id="' +
      post.authorUid +
      '" class="btn btn-primary follow">Segui</button></span></p><img data-uid="' +
      post.authorUid +
      '" class="profilePostImage rounded-circle" src="./img/profilo.jpeg">' +
      '<p class="text-break postText">' +
      content +
      '</p> <span class="time-right">' +
      post.datetime +
      "</span></div></li>"
    );
  }
}

/********************************** FUNZIONI FOLLOW/UNFOLLOW ********************************************/

function eventPostFollow(post) {
  if (post.followingAuthor) {
    let uid = post.author;
    $("#" + uid).on("click", function () {
      userUnfollow(uid);
    });
  }

  if (!post.followingAuthor) {
    let uid = post.author;
    $("#" + uid).on("click", function () {
      userFollow(uid);
    });
  }
}

function userFollow(uid) {
  console.log("Follow");
  let sid = localStorage.getItem("sid");

  communicationController.follow(
    sid,
    uid,
    (response) => {
      console.log("Response", "Follow " + response);
      model.setFollowResponse(uid, true);
      getDetailsPosts(localStorage.getItem("did"));
    },
    (error) => {
      console.log("Error");
    }
  );
}

function userUnfollow(uid) {
  console.log("Unfollow");
  let sid = localStorage.getItem("sid");

  communicationController.unfollow(
    sid,
    uid,
    (response) => {
      model.setFollowResponse(uid, false);
      console.log("Response", "Unfollow " + response);
      getDetailsPosts(localStorage.getItem("did"));
    },
    (error) => {
      console.log("Error");
    }
  );
}

/********************************** CONTENUTO POST ********************************************/

function updateContent(post) {
  let newPost;
  newPost = delayContent(post.delay);
  if (post.delay !== "" && (post.status !== "" || post.comment != "")) {
    console.log(
      typeof post.delay +
        " - " +
        typeof post.status +
        " - " +
        typeof post.comment
    );
    console.log(post.delay + " - " + post.status + " - " + post.comment);
    newPost += "<br>";
  }
  newPost += statusContent(post.status);
  if (post.status !== "" && post.comment != "") {
    console.log(newPost);
    newPost += "<br>";
  }
  if (post.comment != "") {
    newPost += "<b>Commento:</b> " + post.comment;
  }
  return newPost;
}

function delayContent(delay) {
  let nDelay = "<b>Ritardo:</b> ";
  switch (delay) {
    case 0:
      return nDelay + "In orario";
    case 1:
      return nDelay + "Ritardo di pochi minuti";
    case 2:
      return nDelay + "Ritardo oltre i 15 minuti";
    case 3:
      return nDelay + "Treni soppressi";
    default:
      return "";
  }
}

function statusContent(status) {
  let nStatus = "<b>Stato del viaggio:</b> ";
  switch (status) {
    case 0:
      return nStatus + "Situazione ideale";
    case 1:
      return nStatus + "Accettabile";
    case 2:
      return nStatus + "Gravi problemi per i passeggeri";
    default:
      return "";
  }
}

/******************************** GESTIONE IMMAGINI DEL PROFILO ******************************************/

function getProfilePictureAndViewWhenReady(uid, pversion) {
  model.getProfilePictureFromDB(uid, pversion, function (result) {
    if (result.rows.length > 0) {
      //Se l'immagine è memorizzata nel db e la sua pversion è giusta
      console.log("debugging prendo dal db " + uid);
      let row = result.rows.item(0);
      model.setPictureAndPversion(row.uid, row.picture, row.pversion);
      displayProfileImageFromSource(row.uid, row.picture);
    } else {
      console.log("debugging prendo dala rete " + uid);
      getProfilePictureFromNetworkAndView(uid);
    }
  });
}

function getProfilePictureFromNetworkAndView(uid) {
  let sid = localStorage.getItem("sid");

  communicationController.getUserPicture(
    sid,
    uid,
    (response) => {
      console.log(
        "ritornata immagine di " + uid + " pversion: " + response.pversion
      );
      model.setPictureAndPversion(uid, response.picture, response.pversion);

      displayProfileImageFromSource(uid, response.picture);
      model.memorizeProfilePictureInDB(
        uid,
        response.picture,
        response.pversion
      );
    },
    (error) => {
      showShortToast("Si è verificato un errore");
      console.error(error);
    }
  );
}

function displayProfileImageFromSource(uid, source) {
  let selector = '[data-uid="' + uid + '"]';

  if (source != null) {
    let imgSrc = "data:image/jpeg;base64, " + source;
    console.log(imgSrc);
    //Faccio la crop delle immagini per renderle quadrate in caso non lo fossero
    resizeImage(
      imgSrc,
      function success(cropImg) {
        $(selector).attr("src", cropImg);
      },
      function error() {
        $(selector).attr("src", "./img/profilo.jpeg");
      }
    );
  }
}

/*************************************** ADD NEW POST ************************************/

function onClickAddPost() {
  let did = localStorage.getItem("did");
  console.log(did);

  onCreateAddPostScreen(did);
}

/*************************************** GET STATIONS ************************************/

function getDetailStations(did) {
  let sid = localStorage.getItem("sid");

  model.clearStations();
  communicationController.getStations(
    sid,
    did,
    (response) => {
      stationResponse(response);
      console.log(
        "Response",
        "Model size stations: " + model.getSizeStations()
      );
    },
    (error) => {
      console.log("Error");
      showShortToast("Errore di caricamento");
    }
  );
}

function stationResponse(response) {
  let stations = response.stations;

  for (let station of stations) {
    model.addStation(station);
  }
}

//Prendi la posizione corrente
function currentlyLocation() {
  let options = { timeout: 3000, enableHighAccuracy: true };
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      locationSuccess,
      function error() {
        showShortToast("Posizione non disponibile");
      },
      options
    );
  } else {
    showShortToast("Si è verificato un errore");
  }
}

function locationSuccess(position) {
  let lat = position.coords.latitude;
  let lon = position.coords.longitude;
  console.log("LocationSuccess " + lat + "-" + lon);
  model.addPosition(lat, lon);
}

function onClickShowMap() {
  onCreateMapScreen();
}

/*************************************** CHANGE OPPOSITE DIRECTION ************************************/

function changeDirection() {
  let did = localStorage.getItem("oppositeDid");
  let direction = localStorage.getItem("oppositeDirection");
  let oppositeDid = localStorage.getItem("did");
  let oppositeDirection = localStorage.getItem("direction");

  localStorage.setItem("did", did);
  localStorage.setItem("direction", direction);
  localStorage.setItem("oppositeDid", oppositeDid);
  localStorage.setItem("oppositeDirection", oppositeDirection);

  onCreateBoardScreen(did, direction);
}

/*************************************** ESAME GENNAIO ************************************/

function getDetailsOfficialPosts(did) {
  $("#officialPost").empty();
  model.clearOfficialPosts();

  communicationController.getOfficialPost(
    did,
    (response) => {
      console.log(response.officialposts);
      officialPostResponse(response, did);
    },
    (error) => {
      showShortToast("Errore nel caricamento");
      console.error(error);
    }
  );
}

function officialPostResponse(networkResponse, did) {
  let arrOfficialPosts = networkResponse.officialposts;
  console.log("JSON" + arrOfficialPosts);

  for (let officialPost of arrOfficialPosts) {
    console.log("JSON" + arrOfficialPosts);
    //Aggiungo il post al model
    let nuovoPost = model.createOfficialPostObject(officialPost);
    model.addOfficialPost(nuovoPost);

    $("#officialPost").append(generateOfficialPostView(nuovoPost));
    eventPostFollow(officialPost);

    //Creo la funzione di click sulla riga
    $(".official").click(eventOfficialPost);
  }
  console.log(
    "esamegennaio " +
      "did: [" +
      did +
      "] numero di post ufficiali: [" +
      model.getSizeOfficialPosts() +
      "]"
  );
}

function generateOfficialPostView(officialPost) {
  return (
    '<li class="official list-group-item post">' +
    '<div class="container" style="background-color:#29D8C8"><p class="profilePostName"><b>' +
    officialPost.authorName +
    '</b></p><img class="profilePostImage rounded-circle" src="./img/polizia.jpeg">' +
    '<p class="text-break postText">' +
    officialPost.comment +
    '</p> <span class="time-right">' +
    officialPost.datetime +
    "</span></div></li>"
  );
}

function eventOfficialPost() {
  let i = $(this).index();
  let officialPost = model.getPositionOfficialPost(i);
  onCreateDetailOfficialPostScreen(officialPost);
}
