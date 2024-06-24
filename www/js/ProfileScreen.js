//Mi creo la mia schermata con il profilo utente
function onCreateProfileScreen() {
  showScreen("#ProfileScreen");
  let sid = localStorage.getItem("sid");

  communicationController.getProfile(
    sid,
    function (response) {
      showProfileName(response.name);
      showProfileImage(response.pversion, response.picture);
    },
    function (error) {
      console.log(error);
    }
  );
}

function showProfileName(name) {
  $("#nameEditText").val(name);
}

function showProfileImage(pversion, base64string) {
  if (pversion > 0) {
    $("#profileImage").attr("src", "data:image/jpeg;base64, " + base64string);
  }
}

/*--------------------------------------- SET NAME PROFILE --------------------------------------------*/

function setNameProfile() {
  let sid = localStorage.getItem("sid");
  let name = $("#nameEditText").val();

  if (name.length <= 20) {
    communicationController.setProfile(
      sid,
      name,
      null,
      (response) => {
        $("#nameEditText").attr("#nameEditText", name);
        showShortToast("Il nome è stato aggiornato");
      },
      (error) => {
        console.log(error);
        showShortToast("Il nome è troppo lungo");
      }
    );
  }
}

/*--------------------------------------- CHANGE IMAGE FROM GALLERY --------------------------------------------*/

function onClickChangeImage() {
  var srcType = Camera.PictureSourceType.SAVEDPHOTOALBUM;
  var options = setOptionsFileUri(srcType);

  navigator.camera.getPicture(
    function cameraSuccess(imageData) {
      resizeImage(imageData, successResizing, null);
    },
    function cameraError(error) {
      showShortToast("Si è verificato un errore");
      console.error("Unable to obtain picture: " + error, "app");
    },
    options
  );
}

function successResizing(imgSrc) {
  let base64string = imgSrc.split(",")[1];
  let sid = localStorage.getItem("sid");

  if (base64string.length < 137000) {
    communicationController.setProfile(
      sid,
      null,
      base64string,
      (response) => {
        $("#profileImage").attr("src", imgSrc);
        showShortToast("Immagine aggiornata con successo");
      },
      (error) => {
        showShortToast("Errore nel caricamento dell'immagine");
      }
    );
  } else {
    showShortToast("L'immagine è troppo grande");
  }
}

/*--------------------------------------- FIX IMAGE --------------------------------------------*/

function setOptionsFileUri(srcType) {
  var options = {
    quality: 5,
    destinationType: Camera.DestinationType.FILE_URI,
    sourceType: srcType,
    encodingType: Camera.EncodingType.JPEG,
    mediaType: Camera.MediaType.PICTURE,
    correctOrientation: true,
  };
  return options;
}

function resizeImage(url, success, error) {
  var tempImg = new Image();
  tempImg.src = url;
  tempImg.onload = function () {
    var crop = Math.min(tempImg.width, tempImg.height);
    var canvas = document.createElement("canvas");
    canvas.width = crop;
    canvas.height = crop;
    var ctx = canvas.getContext("2d");
    ctx.drawImage(this, 0, 0, tempImg.width, tempImg.height, 0, 0, crop, crop);

    success(canvas.toDataURL("image/jpeg"));
  };
  tempImg.onerror = error;
}
