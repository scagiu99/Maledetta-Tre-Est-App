class Line {
  constructor(arrival, departure, didArrival, direction) {
    this._departure = departure;
    this._arrival = arrival;
    this._did = didArrival;
    this._direction = direction;
  }

  get didArrival() {
    return this._did;
  }

  get arrival() {
    return this._arrival;
  }

  get departure() {
    return this._departure;
  }

  get direction() {
    return this._direction;
  }
}

class Station {
  constructor(name, lat, lon) {
    this._sname = name;
    this._latitudine = lat;
    this._longitudine = lon;
  }

  get sname() {
    return this._sname;
  }

  get lat() {
    return this._latitudine;
  }

  get lon() {
    return this._longitudine;
  }
}

class Author {
  constructor(uid) {
    this._uid = uid;
    this._picture = null;
    this._pversion = 0;
  }

  get uid() {
    return this._uid;
  }

  get picture() {
    return this._picture;
  }

  set picture(picture) {
    this._picture = picture;
  }

  set pversion(pversion) {
    this._pversion = pversion;
  }

  get pversion() {
    return this._pversion;
  }
}

class Post {
  constructor(
    delay,
    comment,
    status,
    uid,
    authorName,
    pversion,
    followingAuthor,
    datetime
  ) {
    this._delay = delay;
    this._comment = comment;
    this._status = status;
    this._uid = uid;
    this._authorName = authorName;
    this._followingAuthor = followingAuthor;
    this._pversion = pversion;
    this._datetime = datetime;
  }

  get delay() {
    return this._delay;
  }

  get comment() {
    return this._comment;
  }

  get status() {
    return this._status;
  }

  get datetime() {
    return this._datetime.substring(0, 16);
  }

  get followingAuthor() {
    return this._followingAuthor;
  }

  set followingAuthor(follow) {
    this._followingAuthor = follow;
  }

  get pversionPost() {
    return this._pversion;
  }
  get authorUid() {
    return this._uid;
  }

  get authorName() {
    return this._authorName;
  }
}

class Model {
  constructor() {
    this._lines = [];
    this._posts = [];
    this._officialPosts = [];
    this._stations = [];
    this._users = new Map();
    this._posizione = "";
    this.db = window.sqlitePlugin.openDatabase({
      name: "pictures.db",
      location: "default",
    });
    let queries = [
      "CREATE TABLE IF NOT EXISTS ProfilePictureUtente (uid PRIMARY KEY, picture VARCHAR(137000), pversion INTEGER)",
    ];
    this.db.sqlBatch(
      queries,
      function () {
        console.log("db creato");
      },
      function () {
        console.error("db non creato");
      }
    );
  }

  /* --------------------------------------------- LINE ------------------------------------------ */

  getPositionLineDid(position) {
    return this._lines[position].didArrival;
  }

  getSizeLines() {
    return this._lines.length;
  }

  addLine(line) {
    this._lines.push(line);
  }

  getLineDirection(tratta) {
    for (let line of this._lines) {
      if (line.didArrival == tratta) {
        return (
          line.arrival +
          " - " +
          line.departure +
          "\n" +
          "direzione " +
          line.direction
        );
      }
    }
    return "";
  }

  getLine(tratta) {
    for (let line of this._lines) {
      if (line.didArrival == tratta) {
        return line;
      }
    }
    return null;
  }

  getLineOppositeDirection(tratta) {
    let linea = null;
    let did = "";
    for (let line of this._lines) {
      if (line.didArrival == tratta) {
        linea = line;
      }
    }
    for (let line of this._lines) {
      if (
        line.didArrival != linea.didArrival &&
        (line.departure == linea.arrival || line.arrival == linea.arrival)
      ) {
        console.log("OppositeDirection " + line.didArrival);
        did = line.didArrival;
      }
    }
    return did;
  }

  clearLines() {
    this._lines = [];
  }

  /* ------------------------------------------- STATIONS ---------------------------------------- */

  getPositionStation(position) {
    console.log("station " + this._stations[position]);
    return this._stations[position];
  }

  getSizeStations() {
    console.log("getSizeStations " + this._stations.length);
    return this._stations.length;
  }

  lastStation() {
    return this._stations[this._stations.length - 1];
  }

  addStation(station) {
    console.log("AddStation " + station);
    this._stations.push(station);
  }

  addPosition(latitudine, longitude) {
    this._posizione = new Station("Tu sei qui", latitudine, longitude);
    console.log("Position " + this._posizione.lat + " " + this._posizione.lon);
  }

  getPosition() {
    return this._posizione;
  }

  clearStations() {
    this._stations = [];
  }

  /* --------------------------------------------- POST ------------------------------------------ */

  getPositionPost(position) {
    return this._posts[position];
  }

  getSizePosts() {
    return this._posts.length;
  }

  createPostObject(post) {
    return new Post(
      model.checkPost(post)[0],
      model.checkPost(post)[1],
      model.checkPost(post)[2],
      post.author,
      post.authorName,
      post.pversion,
      post.followingAuthor,
      post.datetime
    );
  }

  addPost(post) {
    this._posts.push(post);
  }

  checkPost(post) {
    let arrPost = [];
    if (post.delay == null) {
      arrPost.push("");
    } else {
      arrPost.push(post.delay);
    }
    if (post.comment == null) {
      arrPost.push("");
    } else {
      arrPost.push(post.comment);
    }
    if (post.status == null) {
      arrPost.push("");
    } else {
      arrPost.push(post.status);
    }
    return arrPost;
  }

  setFollowResponse(author, follow) {
    for (let p of this._posts) {
      if (p.authorUid == author) {
        p.followingAuthor = follow;
      }
    }
  }

  clearPosts() {
    this._posts = [];
  }

  /* --------------------------------------------- DATABASE + USERS ------------------------------------------ */

  //Users Profile Pictures

  addUserProfilePicture(user) {
    if (this._users.has(user.uid)) {
      console.log("Uid già esistente");
      return;
    } else {
      this._users.set(user.uid, user);
    }
  }

  printUsers() {
    this._users.forEach(function (u) {
      console.log(u);
    });
  }

  getPversion(uid) {
    if (this._users.has(uid)) {
      return this._users.get(uid).pversion;
    }
    return -1;
  }

  getProfilePicture(uid) {
    if (this._users.has(uid)) {
      return this._users.get(uid).picture;
    }
    return null;
  }

  setPictureAndPversion(uid, picture, pversion) {
    if (this._users.has(uid)) {
      this._users.get(uid).picture = picture;
      this._users.get(uid).pversion = pversion;
    }
    return;
  }

  isProfilePictureInModelUpdated(pversionObtained, uid) {
    let pversionInModel = model.getPversion(uid);
    if (pversionObtained == pversionInModel) {
      return true;
    }
    return false;
  }

  memorizeProfilePictureInDB(uid, picture, pversion) {
    let query = "INSERT INTO ProfilePictureUtente VALUES(?,?,?)";
    this.db.executeSql(
      query,
      [uid, picture, pversion],
      function () {
        console.log("inserimento nel db avvenuto, uid: " + uid);
      },
      function (error) {
        console.error(error);
        console.error("inserimento nel db non avvenuto, uid: " + uid);
      }
    );
  }

  getProfilePictureFromDB(uid, pversion, selectSuccess) {
    let query = "SELECT * FROM ProfilePictureUtente WHERE uid=? AND pversion=?";
    this.db.executeSql(query, [uid, pversion], selectSuccess, function () {
      console.error("select non avvenuta");
    });
  }

  /* -------------------------------------- ESAME GENNAIO -------------------------------------- */

  getPositionOfficialPost(position) {
    console.log("PositionOfficial " + this._officialPosts[position]);
    return this._officialPosts[position];
  }

  getSizeOfficialPosts() {
    return this._officialPosts.length;
  }

  createOfficialPostObject(officialPost) {
    return new Post(
      null,
      officialPost.description,
      null,
      null,
      officialPost.title,
      null,
      null,
      officialPost.timestamp
    );
  }

  addOfficialPost(officialPost) {
    console.log(
      "esamegennaio " +
        "title: [" +
        officialPost.authorName +
        "] timestamp: [" +
        officialPost.datetime +
        "]"
    );
    this._officialPosts.push(officialPost);
  }

  clearOfficialPosts() {
    this._officialPosts = [];
  }
}
