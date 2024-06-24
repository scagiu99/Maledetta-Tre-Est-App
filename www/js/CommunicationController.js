class CommunicationController {
  constructor() {
    this._BASE_URL = "https://ewserver.di.unimi.it/mobicomp/treest/";
  }

  register(responseCallback, errorCallback) {
    let service_url = "register.php";
    let url = this._BASE_URL + service_url;

    $.post({
      url: url,
      dataType: "json",
      success: (response) => responseCallback(response),
      error: (error) => errorCallback(error),
    });
  }

  getProfile(sid, responseCallback, errorCallback) {
    let service_url = "getProfile.php";
    let url = this._BASE_URL + service_url;
    let data = { sid: sid };

    $.post({
      url: url,
      data: JSON.stringify(data),
      dataType: "json",
      success: (response) => responseCallback(response),
      error: (error) => errorCallback(error),
    });
  }

  setProfile(sid, name, picture, responseCallback, errorCallback) {
    let service_url = "setProfile.php";
    let url = this._BASE_URL + service_url;

    if (name !== null && picture === null) {
      let data = { sid: sid, name: name };
      $.post({
        url: url,
        data: JSON.stringify(data),
        dataType: "json",
        success: (response) => responseCallback(response),
        error: (error) => errorCallback(error),
      });
    } else {
      let data = { sid: sid, picture: picture };
      $.post({
        url: url,
        data: JSON.stringify(data),
        dataType: "json",
        success: (response) => responseCallback(response),
        error: (error) => errorCallback(error),
      });
    }
  }

  getLines(sid, responseCallback, errorCallback) {
    let service_url = "getLines.php";
    let url = this._BASE_URL + service_url;
    let data = { sid: sid };

    $.post({
      url: url,
      data: JSON.stringify(data),
      dataType: "json",
      success: (response) => responseCallback(response),
      error: (error) => errorCallback(error),
    });
  }

  getStations(sid, did, responseCallback, errorCallback) {
    let service_url = "getStations.php";
    let url = this._BASE_URL + service_url;
    let data = { sid: sid, did: did };

    $.post({
      url: url,
      contentType: "text/plain",
      data: JSON.stringify(data),
      dataType: "json",
      success: (response) => responseCallback(response),
      error: (error) => errorCallback(error),
    });
  }

  getPosts(sid, did, responseCallback, errorCallback) {
    let service_url = "getPosts.php";
    let url = this._BASE_URL + service_url;
    let data = { sid: sid, did: did };

    $.post({
      url: url,
      contentType: "text/plain",
      data: JSON.stringify(data),
      dataType: "json",
      success: (response) => responseCallback(response),
      error: (error) => errorCallback(error),
    });
  }

  addPost(sid, did, delay, status, comment, responseCallback, errorCallback) {
    let service_url = "addPost.php";
    let url = this._BASE_URL + service_url;
    let data = {
      sid: sid,
      did: did,
      delay: delay,
      status: status,
      comment: comment,
    };

    $.post({
      url: url,
      data: JSON.stringify(data),
      dataType: "json",
      contentType: "text/plain",
      success: (response) => responseCallback(response),
      error: (error) => errorCallback(error),
    });
  }

  getUserPicture(sid, uid, responseCallback, errorCallback) {
    let service_url = "getUserPicture.php";
    let url = this._BASE_URL + service_url;
    let data = { sid: sid, uid: uid };

    $.post({
      url: url,
      data: JSON.stringify(data),
      dataType: "json",
      success: (response) => responseCallback(response),
      error: (error) => errorCallback(error),
    });
  }

  follow(sid, uid, responseCallback, errorCallback) {
    let service_url = "follow.php";
    let url = this._BASE_URL + service_url;
    let data = { sid: sid, uid: uid };

    $.post({
      url: url,
      data: JSON.stringify(data),
      dataType: "json",
      success: (response) => responseCallback(response),
      error: (error) => errorCallback(error),
    });
  }

  unfollow(sid, uid, responseCallback, errorCallback) {
    let service_url = "unfollow.php";
    let url = this._BASE_URL + service_url;
    let data = { sid: sid, uid: uid };

    $.post({
      url: url,
      data: JSON.stringify(data),
      dataType: "json",
      success: (response) => responseCallback(response),
      error: (error) => errorCallback(error),
    });
  }
}
