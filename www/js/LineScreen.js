//Mi creo la mia schermata con le linee
function onCreateLineScreen() {
  showScreen("#LineScreen");
  // Registrazione di un nuovo utente
  if (localStorage.getItem("sid") === null) {
    communicationController.register(
      function (response) {
        // Memorizzo il sid del nuovo utente nel localStorage
        let sid = response.sid;
        localStorage.setItem("sid", sid);
        getDataLines(sid);
      },
      function (error) {
        showShortToast("Si è verificato un errore");
        console.log(error);
      }
    );
  } else {
    let sid = localStorage.getItem("sid");
    getDataLines(sid);
  }

  function getDataLines(sid) {
    $("#lineList").empty();
    model.clearLines();

    communicationController.getLines(
      sid,
      function (response) {
        let lines = response.lines;
        console.log("response" + lines);

        for (let line of lines) {
          console.log(line.terminus1.sname);
          let terminus1 = line.terminus1.sname;
          let terminus2 = line.terminus2.sname;
          let did1 = line.terminus1.did;
          let did2 = line.terminus2.did;

          let newLine1 = new Line(terminus1, terminus2, did1, terminus1);
          model.addLine(newLine1);

          $("#lineList").append(newLinePairRow(newLine1));

          let newLine2 = new Line(terminus1, terminus2, did2, terminus2);
          model.addLine(newLine2);

          $("#lineList").append(newLineOddRow(newLine2));
        }

        //Creo la funzione di click sulla riga
        $(".line").click(onRowLineClick);
      },
      function (error) {
        //showShortToast("Si è verificato un errore");
        console.log(error);
      }
    );
  }

  function newLinePairRow(line) {
    return (
      '<li class="line list-group-item list-group-item-action" style="background-color:#B8D3E8; text-align:center; font-style:italic;">' +
      line.arrival +
      " - " +
      line.departure +
      "\n" +
      "direzione " +
      line.direction +
      "</li>"
    );
  }
  function newLineOddRow(line) {
    return (
      '<li class="line list-group-item list-group-item-action" style="background-color:#CACACA; text-align:center;font-style:italic;">' +
      line.arrival +
      " - " +
      line.departure +
      "\n" +
      "direzione " +
      line.direction +
      "</li>"
    );
  }

  function onRowLineClick() {
    let i = $(this).index();
    let did = model.getPositionLineDid(i);
    let direction = model.getLineDirection(did);
    let oppositeDid = model.getLineOppositeDirection(did);
    let oppositeDirection = model.getLineDirection(oppositeDid);

    console.log("è stato cliccata la riga " + i + " , did tratta: " + did);
    console.log(" did tratta: " + oppositeDid);
    console.log(direction);

    localStorage.setItem("did", did);
    localStorage.setItem("direction", direction);
    localStorage.setItem("oppositeDid", oppositeDid);
    localStorage.setItem("oppositeDirection", oppositeDirection);

    onCreateBoardScreen(did, direction);
  }
}
